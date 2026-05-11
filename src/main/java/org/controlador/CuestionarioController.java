package org.controlador;

import org.modelo.ApiService;
import org.modelo.UsuarioModel;
import org.vista.CuestionarioView;

import javax.swing.*;
import java.util.Map;

public class CuestionarioController {

    private CuestionarioView vista;
    private UsuarioModel usuario;
    private ApiService apiService;

    public CuestionarioController(CuestionarioView vista, UsuarioModel usuario) {
        this.vista      = vista;
        this.usuario    = usuario;
        this.apiService = new ApiService();

        this.vista.getBotonEnviar().addActionListener(e -> manejarEnvio());
    }

    private void manejarEnvio() {
        Map<Integer, Integer> respuestas = vista.getRespuestas();

        if (respuestas == null) {
            vista.setMensaje("Por favor responde todas las preguntas antes de continuar.");
            return;
        }

        vista.setBloqueado(true);
        vista.setMensaje("Enviando respuestas...");

        new Thread(() -> {
            try {
                apiService.enviarCuestionario(usuario.getId(), respuestas);

                javax.swing.SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            vista,
                            "¡Respuestas guardadas correctamente!",
                            "Cuestionario completado",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    vista.dispose();
                });

            } catch (Exception ex) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    vista.setMensaje(ex.getMessage());
                    vista.setBloqueado(false);
                });
            }
        }).start();
    }
}