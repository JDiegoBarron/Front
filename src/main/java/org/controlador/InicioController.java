package org.controlador;

import org.modelo.ApiService;
import org.modelo.EvaluadorEstres;
import org.modelo.ResultadoEvaluacion;
import org.modelo.TareaModel;
import org.modelo.UsuarioModel;
import org.vista.InicioView;

import javax.swing.*;
import java.util.List;
import java.util.function.Supplier;

public class InicioController {
    private final InicioView      vista;
    private final UsuarioModel    usuario;
    private final ApiService      apiService;
    private final Supplier<int[]> respuestasProveedor;
    private final Runnable        irACuestionario;

    public InicioController(InicioView vista, UsuarioModel usuario,
                            Supplier<int[]> respuestasProveedor,
                            Runnable irACuestionario) {
        this.vista               = vista;
        this.usuario              = usuario;
        this.apiService           = new ApiService();
        this.respuestasProveedor  = respuestasProveedor;
        this.irACuestionario      = irACuestionario;
        vista.setOnActualizar(this::cargar);
        vista.setOnIrCuestionario(() -> { if (irACuestionario != null) irACuestionario.run(); });
    }

    public void cargar() {
        int[] respuestasEnMemoria = respuestasProveedor.get();

        if (respuestasEnMemoria != null) {
            cargarConRespuestas(respuestasEnMemoria);
            return;
        }

        vista.mostrarCargando();
        new Thread(() -> {
            try {
                int[] respuestasGuardadas = apiService.obtenerUltimasRespuestas(usuario.getId());
                if (respuestasGuardadas == null) {
                    SwingUtilities.invokeLater(vista::mostrarSinDatos);
                    return;
                }
                cargarConRespuestas(respuestasGuardadas);
            } catch (Exception ex) {
                SwingUtilities.invokeLater(vista::mostrarSinDatos);
            }
        }).start();
    }

    private void cargarConRespuestas(int[] respuestas) {
        vista.mostrarCargando();
        new Thread(() -> {
            try {
                List<TareaModel> tareas = apiService.obtenerTareasProximas(usuario.getId());
                ResultadoEvaluacion resultado = EvaluadorEstres.evaluar(tareas, respuestas);
                SwingUtilities.invokeLater(() -> vista.mostrarResultados(resultado));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    ResultadoEvaluacion resultado = EvaluadorEstres.evaluar(null, respuestas);
                    vista.mostrarResultados(resultado);
                });
            }
        }).start();
    }
}