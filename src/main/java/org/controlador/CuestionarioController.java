package org.controlador;

import org.modelo.EvaluadorEstres;
import org.vista.CuestionarioView;

import java.util.function.Consumer;

public class CuestionarioController {

    private final CuestionarioView        vista;
    private final Consumer<int[]>         onRespuestasGuardadas;

    public CuestionarioController(CuestionarioView vista, Consumer<int[]> onRespuestasGuardadas) {
        this.vista                  = vista;
        this.onRespuestasGuardadas  = onRespuestasGuardadas;
        vista.setOnEnviar(this::manejarEnvio);
    }

    private void manejarEnvio() {
        int[] respuestas = vista.getRespuestas();

        vista.setBloqueado(true);
        vista.setMensajeExito("Procesando evaluación...");

        javax.swing.SwingUtilities.invokeLater(() -> {
            if (onRespuestasGuardadas != null) onRespuestasGuardadas.accept(respuestas);
            vista.setMensajeExito("¡Evaluación guardada! Revisa el Inicio para ver tus resultados.");
            vista.setBloqueado(false);
        });
    }
}