package org.controlador;

import org.modelo.ApiService;
import org.modelo.EstadoSeccionDto;
import org.vista.CuestionarioView;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CuestionarioController {
    private final CuestionarioView vista;
    private final int              usuarioId;
    private final Consumer<int[]>  onRespuestasGuardadas;
    private final ApiService       apiService = new ApiService();

    public CuestionarioController(CuestionarioView vista, int usuarioId, Consumer<int[]> onRespuestasGuardadas) {
        this.vista                 = vista;
        this.usuarioId             = usuarioId;
        this.onRespuestasGuardadas = onRespuestasGuardadas;
        vista.setOnEnviar(this::manejarEnvio);
        cargarEstadoSecciones();
    }

    public void cargarEstadoSecciones() {
        new Thread(() -> {
            try {
                Map<String, EstadoSeccionDto> estado = apiService.obtenerEstadoCuestionario(usuarioId);
                SwingUtilities.invokeLater(() -> vista.aplicarEstadoSecciones(estado));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        vista.setMensaje("No se pudo cargar el estado del cuestionario: " + ex.getMessage()));
            }
        }).start();
    }

    private void manejarEnvio() {
        Map<String, List<int[]>> respuestasPorSeccion = vista.getRespuestasPorSeccionHabilitada();

        int[] respuestasCompletas = vista.getRespuestas(); // 21 valores, para EvaluadorEstres
        vista.setBloqueado(true);
        vista.setMensajeExito("Procesando evaluación...");

        new Thread(() -> {
            try {
                apiService.guardarCuestionario(usuarioId, respuestasPorSeccion);
                SwingUtilities.invokeLater(() -> {
                    if (onRespuestasGuardadas != null) onRespuestasGuardadas.accept(respuestasCompletas);
                    vista.setMensajeExito("¡Evaluación guardada! Revisa el Inicio para ver tus resultados.");
                    vista.setBloqueado(false);
                    cargarEstadoSecciones();
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    vista.setMensaje("Error al guardar: " + ex.getMessage());
                    vista.setBloqueado(false);
                });
            }
        }).start();
    }
}