package org.controlador;

import org.modelo.ApiService;
import org.modelo.TareaModel;
import org.modelo.UsuarioModel;
import org.vista.ListaTareasView;

import javax.swing.*;
import java.util.List;

public class ListaTareasController {

    private final ListaTareasView vista;
    private final UsuarioModel    usuario;
    private final ApiService      apiService;

    public ListaTareasController(ListaTareasView vista, UsuarioModel usuario) {
        this.vista      = vista;
        this.usuario    = usuario;
        this.apiService = new ApiService();

        vista.setOnRefresh(this::cargarTareas);
        vista.setOnCompletar(this::completarTarea);
        vista.setOnEliminar(this::eliminarTarea);
    }

    public void cargarTareas() {
        vista.setBloqueado(true);
        vista.setEstado("Cargando...");

        new Thread(() -> {
            try {
                List<TareaModel> tareas = apiService.obtenerTareasProximas(usuario.getId());

                SwingUtilities.invokeLater(() -> {
                    vista.mostrarTareas(tareas);
                    vista.setEstado(tareas.size() + " tarea(s)");
                    vista.setBloqueado(false);
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    vista.setEstado("Error: " + ex.getMessage());
                    vista.setBloqueado(false);
                });
            }
        }).start();
    }

    private void completarTarea(int tareaId) { // todo: revisar porque endpoint no funciona en Front (hice prueba con Postman y funciona bien)
        vista.setBloqueado(true);

        new Thread(() -> {
            try {
                apiService.completarTarea(tareaId);
                // Refrescar la lista tras marcar
                List<TareaModel> tareas = apiService.obtenerTareasProximas(usuario.getId());
                SwingUtilities.invokeLater(() -> {
                    vista.mostrarTareas(tareas);
                    vista.setEstado(tareas.size() + " tarea(s)");
                    vista.setBloqueado(false);
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    vista.setEstado("Error: " + ex.getMessage());
                    vista.setBloqueado(false);
                });
            }
        }).start();
    }

    private void eliminarTarea(int tareaId) {
        vista.setBloqueado(true);

        new Thread(() -> {
            try {
                apiService.eliminarTarea(tareaId);
                List<TareaModel> tareas = apiService.obtenerTareasProximas(usuario.getId());
                SwingUtilities.invokeLater(() -> {
                    vista.mostrarTareas(tareas);
                    vista.setEstado(tareas.size() + " tarea(s)");
                    vista.setBloqueado(false);
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    vista.setEstado("Error al eliminar: " + ex.getMessage());
                    vista.setBloqueado(false);
                });
            }
        }).start();
    }
}
