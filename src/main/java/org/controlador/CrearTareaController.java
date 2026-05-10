package org.controlador;

import org.modelo.ApiService;
import org.modelo.UsuarioModel;
import org.vista.CrearTareaView;

import javax.swing.*;
import java.util.regex.Pattern;

public class CrearTareaController {

    private static final Pattern FECHA_PATTERN =
            Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    private final CrearTareaView vista;
    private final UsuarioModel   usuario;
    private final ApiService     apiService;

    private final Runnable onExito;

    public CrearTareaController(CrearTareaView vista, UsuarioModel usuario, Runnable onExito) {
        this.vista      = vista;
        this.usuario    = usuario;
        this.apiService = new ApiService();
        this.onExito    = onExito;

        vista.setOnGuardar(this::manejarCrearTarea);
    }

    private void manejarCrearTarea() {
        String titulo      = vista.getTitulo();
        String descripcion = vista.getDescripcion();
        String categoria   = vista.getCategoria();
        String prioridad   = vista.getPrioridad();
        int    dificultad  = vista.getDificultad();
        String fechaLimite = vista.getFechaLimite();

        if (titulo.isEmpty()) {
            vista.setMensajeError("El título es obligatorio.");
            return;
        }
        if (titulo.length() > 120) {
            vista.setMensajeError("El título no puede superar 120 caracteres.");
            return;
        }
        if (fechaLimite.isEmpty()) {
            vista.setMensajeError("La fecha límite es obligatoria.");
            return;
        }
        if (!FECHA_PATTERN.matcher(fechaLimite).matches()) {
            vista.setMensajeError("Formato de fecha inválido. Usa YYYY-MM-DD.");
            return;
        }

        vista.setBloqueado(true);
        vista.setMensaje("Guardando...");

        new Thread(() -> {
            try {
                apiService.crearTarea(
                        usuario.getId(),
                        titulo,
                        descripcion,
                        fechaLimite,
                        categoria,
                        prioridad,
                        dificultad
                );

                SwingUtilities.invokeLater(() -> {
                    vista.setMensajeExito("¡Tarea creada correctamente!");
                    vista.limpiarFormulario();
                    if (onExito != null) {
                        Timer t = new Timer(1200, ev -> onExito.run());
                        t.setRepeats(false);
                        t.start();
                    }
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    vista.setMensajeError("Error: " + ex.getMessage());
                    vista.setBloqueado(false);
                });
            }
        }).start();
    }
}
