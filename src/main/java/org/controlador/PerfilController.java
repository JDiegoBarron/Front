package org.controlador;

import org.modelo.ApiService;
import org.modelo.UsuarioModel;
import org.vista.BienvenidaView;
import org.vista.PerfilView;

public class PerfilController {

    private PerfilView vista;
    private UsuarioModel usuario;
    private ApiService apiService;

    public PerfilController(PerfilView vista, UsuarioModel usuario) {
        this.vista = vista;
        this.usuario = usuario;
        this.apiService = new ApiService();

        this.vista.getBotonGuardar().addActionListener(e -> manejarGuardar());
        this.vista.getBotonOmitir().addActionListener(e -> irABienvenida());
    }

    private void manejarGuardar() {
        String correo  = vista.getCorreo();
        String carrera = vista.getCarrera();
        int semestre   = vista.getSemestre();

        if (correo.isEmpty() || correo.equals("Correo electrónico") ||
                carrera.isEmpty() || carrera.equals("Carrera")) {
            vista.setMensaje("Completa todos los campos");
            return;
        }

        vista.setBloqueado(true);
        vista.setMensaje("Guardando...");

        new Thread(() -> {
            try {
                apiService.guardarPerfil(usuario.getId(), correo, carrera, semestre);

                javax.swing.SwingUtilities.invokeLater(() -> irABienvenida());

            } catch (Exception ex) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    vista.setMensaje(ex.getMessage());
                    vista.setBloqueado(false);
                });
            }
        }).start();
    }

    private void irABienvenida() {
        vista.dispose();
        BienvenidaView bienvenida = new BienvenidaView(usuario.getNombreCompleto());
        new BienvenidaController(bienvenida, usuario);
    }
}