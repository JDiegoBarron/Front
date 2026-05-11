package org.controlador;

import org.modelo.UsuarioModel;
import org.vista.*;

public class BienvenidaController {

    private final BienvenidaView        vista;
    private final UsuarioModel          usuario;

    // Controladores de paneles
    private ListaTareasController  listaTareasCtrl;
    private InicioController       inicioCtrl;

    // Respuestas del cuestionario (null = aún no completado)
    private int[] respuestasCuestionario = null;

    public BienvenidaController(BienvenidaView vista, UsuarioModel usuario) {
        this.vista   = vista;
        this.usuario = usuario;

        inyectarPaneles();
        conectarNavegacion();
        vista.switchCard(BienvenidaView.CARD_INICIO);
    }

    // ── Inyección de paneles ───────────────────────────────────────────────────

    private void inyectarPaneles() {

        // ── Inicio (dashboard de estrés) ─────────────────────────────────────
        InicioView inicioView = new InicioView();
        inicioCtrl = new InicioController(
                inicioView,
                usuario,
                () -> respuestasCuestionario,                        // proveedor de respuestas
                () -> vista.switchCard(BienvenidaView.CARD_BIENESTAR) // botón "Ir al cuestionario"
        );
        vista.addCard(inicioView, BienvenidaView.CARD_INICIO);

        // ── Lista de tareas ───────────────────────────────────────────────────
        ListaTareasView listaTareasView = new ListaTareasView();
        listaTareasCtrl = new ListaTareasController(listaTareasView, usuario);
        vista.addCard(listaTareasView, BienvenidaView.CARD_TAREAS);

        // ── Crear tarea → al guardar, va a la lista ───────────────────────────
        CrearTareaView crearTareaView = new CrearTareaView();
        new CrearTareaController(crearTareaView, usuario, () -> {
            listaTareasCtrl.cargarTareas();
            vista.switchCard(BienvenidaView.CARD_TAREAS);
        });
        vista.addCard(crearTareaView, BienvenidaView.CARD_CREAR_TAREA);

        // ── Cuestionario de bienestar ─────────────────────────────────────────
        CuestionarioView cuestionarioView = new CuestionarioView();
        new CuestionarioController(cuestionarioView, respuestas -> {
            respuestasCuestionario = respuestas;  // guardar en el controlador
            inicioCtrl.cargar();                  // recalcular dashboard
            vista.switchCard(BienvenidaView.CARD_INICIO);
        });
        vista.addCard(cuestionarioView, BienvenidaView.CARD_BIENESTAR);
    }

    // ── Navegación del sidebar ────────────────────────────────────────────────

    private void conectarNavegacion() {
        conectarNavBtn(BienvenidaView.CARD_INICIO,      () -> inicioCtrl.cargar());
        conectarNavBtn(BienvenidaView.CARD_CALENDARIO,  () -> {});
        conectarNavBtn(BienvenidaView.CARD_TAREAS,      () -> listaTareasCtrl.cargarTareas());
        conectarNavBtn(BienvenidaView.CARD_CREAR_TAREA, () -> {});
        conectarNavBtn(BienvenidaView.CARD_BIENESTAR,   () -> {});
        conectarNavBtn(BienvenidaView.CARD_PERFIL,      () -> {});

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
        new LoginController(new LoginView());
    }
}