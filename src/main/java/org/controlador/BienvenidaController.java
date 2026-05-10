package org.controlador;

import org.modelo.UsuarioModel;
import org.vista.BienvenidaView;
import org.vista.CrearTareaView;
import org.vista.ListaTareasView;
import org.vista.LoginView;

public class BienvenidaController {

    private final BienvenidaView         vista;
    private final UsuarioModel           usuario;
    private       ListaTareasController  listaTareasCtrl;

    public BienvenidaController(BienvenidaView vista, UsuarioModel usuario) {
        this.vista   = vista;
        this.usuario = usuario;

        inyectarPaneles();
        conectarNavegacion();
        vista.switchCard(BienvenidaView.CARD_INICIO);
    }

    private void inyectarPaneles() {
        ListaTareasView listaTareasView = new ListaTareasView();
        listaTareasCtrl = new ListaTareasController(listaTareasView, usuario);
        vista.addCard(listaTareasView, BienvenidaView.CARD_TAREAS);

        CrearTareaView crearTareaView = new CrearTareaView();
        new CrearTareaController(crearTareaView, usuario, () -> {
            listaTareasCtrl.cargarTareas();
            vista.switchCard(BienvenidaView.CARD_TAREAS);
        });
        vista.addCard(crearTareaView, BienvenidaView.CARD_CREAR_TAREA);
    }

    private void conectarNavegacion() {
        conectarNavBtn(BienvenidaView.CARD_INICIO,      () -> { /* sin acción extra */ });
        conectarNavBtn(BienvenidaView.CARD_CALENDARIO,  () -> { /* pendiente */ });
        conectarNavBtn(BienvenidaView.CARD_TAREAS,      () -> listaTareasCtrl.cargarTareas());
        conectarNavBtn(BienvenidaView.CARD_CREAR_TAREA, () -> { /* sin acción extra */ });
        conectarNavBtn(BienvenidaView.CARD_BIENESTAR,   () -> { /* pendiente */ });
        conectarNavBtn(BienvenidaView.CARD_PERFIL,      () -> { /* pendiente */ });

        vista.getBotonCerrarSesion().addActionListener(e -> cerrarSesion());
    }

    private void conectarNavBtn(String cardKey, Runnable hook) {
        vista.getNavButton(cardKey).addActionListener(e -> {
            hook.run();
            vista.switchCard(cardKey);
        });
    }

    private void cerrarSesion() {
        vista.dispose();
        LoginView loginView = new LoginView();
        new LoginController(loginView);
    }
}
