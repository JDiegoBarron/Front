package org.controlador;

import org.modelo.ApiService;
import org.vista.RegistroView;

public class RegistroController {

    private RegistroView vista;
    private ApiService apiService;

    public RegistroController(RegistroView vista) {
        this.vista = vista;
        this.apiService = new ApiService();

        this.vista.getBotonRegistrar().addActionListener(e -> manejarRegistro());
        this.vista.getBotonVolver().addActionListener(e -> vista.dispose());
    }

    private void manejarRegistro() {
        String nombre   = vista.getNombre();
        String username = vista.getUsername();
        String password = vista.getPassword();

        // Validaciones
        if (nombre.isEmpty() || nombre.equals("Nombre completo") ||
                username.isEmpty() || username.equals("Usuario") ||
                password.isEmpty() || password.equals("Contraseña")) {
            vista.setMensaje("Completa todos los campos");
            return;
        }

        vista.setBloqueado(true);
        vista.setMensaje("Creando cuenta...");

        new Thread(() -> {
            try {
                apiService.registrar(username, password, nombre);

                javax.swing.SwingUtilities.invokeLater(() -> {
                    vista.setMensajeExito("Ya puedes iniciar sesión");
                    vista.setBloqueado(false);
                });

            } catch (Exception ex) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    vista.setMensaje(ex.getMessage());
                    vista.setBloqueado(false);
                    vista.setMensajeError("Error creando registro.");
                });
            }
        }).start();
        vista.dispose();
    }
}