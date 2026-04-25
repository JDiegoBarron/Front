package org.controlador;

import org.modelo.ApiService;
import org.modelo.UsuarioModel;
import org.vista.LoginView;
import org.vista.BienvenidaView;

public class LoginController {

    private LoginView vista;
    private ApiService apiService;

    public LoginController(LoginView vista) {
        this.vista = vista;
        this.apiService = new ApiService();

        // Conectar botón de la vista con la lógica
        this.vista.getBotonEntrar().addActionListener(e -> manejarLogin());
    }

    private void manejarLogin() {
        String usuario  = vista.getUsuario();
        String password = vista.getPassword();

        if (usuario.isEmpty() || usuario.equals("Usuario") ||
                password.isEmpty() || password.equals("Contraseña")) {
            vista.setMensaje("Completa todos los campos");
            return;
        }

        vista.setBloqueado(true);
        vista.setMensaje("Conectando...");

        // Llamada a la API en hilo separado para no bloquear la UI
        new Thread(() -> {
            try {
                UsuarioModel usuario_model = apiService.login(usuario, password);

                // Volver al hilo de Swing para actualizar la UI
                javax.swing.SwingUtilities.invokeLater(() -> {
                    vista.dispose();
                    BienvenidaView bienvenida = new BienvenidaView(usuario_model.getNombreCompleto());
                    new BienvenidaController(bienvenida, usuario_model);
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