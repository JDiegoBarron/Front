package org.controlador;

import org.modelo.ApiService;
import org.modelo.TareaModel;
import org.modelo.UsuarioModel;
import org.vista.CalendarioView;

import javax.swing.*;
import java.time.YearMonth;
import java.util.List;

public class CalendarioController {

    private final CalendarioView vista;
    private final UsuarioModel   usuario;
    private final ApiService     apiService;

    public CalendarioController(CalendarioView vista, UsuarioModel usuario) {
        this.vista      = vista;
        this.usuario    = usuario;
        this.apiService = new ApiService();

        vista.setOnMesCambiado(mes -> cargarMes(mes));

        vista.actualizarGrid();
    }

    public void cargar() {
        cargarMes(vista.getMesActual());
    }

    private void cargarMes(YearMonth mes) {
        vista.setEstado("Cargando...");

        new Thread(() -> {
            try {
                List<TareaModel> tareas = apiService.obtenerTareas(usuario.getId());

                SwingUtilities.invokeLater(() -> {
                    vista.setTareas(tareas);
                    long pendientes = tareas.stream().filter(t -> !t.isCompletada()).count();
                    vista.setEstado(pendientes + " pendientes este mes");
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        vista.setEstado("Error al cargar: " + ex.getMessage()));
            }
        }).start();
    }
}
