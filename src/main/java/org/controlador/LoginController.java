package org.controlador;

import org.modelo.ApiService;
import org.modelo.UsuarioModel;
import org.vista.LoginView;
import org.vista.BienvenidaView;
import org.vista.RegistroView;

public class LoginController {

    private LoginView vista;
    private ApiService apiService;

    public LoginController(LoginView vista) {
        this.vista = vista;
        this.apiService = new ApiService();

        // Conectar botón de la vista con la lógica
        this.vista.getBotonEntrar().addActionListener(e -> manejarLogin());
        this.vista.getBotonRegistrar().addActionListener(e -> {
            RegistroView registro = new RegistroView();
            new RegistroController(registro);
            vista.dispose();
        });
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

        new Thread(() -> {
            try {
                UsuarioModel usuarioModel = apiService.login(usuario, password);

                javax.swing.SwingUtilities.invokeLater(() -> {
                    vista.dispose();
                    BienvenidaView bienvenida = new BienvenidaView(usuarioModel.getNombreCompleto());
                    new BienvenidaController(bienvenida, usuarioModel); // ya lleva el id
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