package org.controlador;

import org.modelo.ApiService;
import org.modelo.RachaModel;
import org.modelo.TemaApp;
import org.modelo.UsuarioModel;
import org.vista.*;

import javax.swing.*;

public class BienvenidaController {

    private final BienvenidaView  vista;
    private final UsuarioModel    usuario;
    private final ApiService      apiService = new ApiService();

    // Controladores de paneles con estado
    private ListaTareasController  listaTareasCtrl;
    private InicioController       inicioCtrl;
    private CalendarioController   calendarioCtrl;
    private PerfilPanelController  perfilPanelCtrl;

    private int[] respuestasCuestionario = null;

    public BienvenidaController(BienvenidaView vista, UsuarioModel usuario) {
        this.vista   = vista;
        this.usuario = usuario;

        // Cargar tema guardado antes de construir paneles
        TemaApp.cargar();

        inyectarPaneles();
        conectarNavegacion();
        vista.switchCard(BienvenidaView.CARD_INICIO);

        // Registrar login diario (asíncrono para no bloquear la UI)
        registrarLoginDiario();
    }

    private void inyectarPaneles() {

        InicioView inicioView = new InicioView();
        inicioCtrl = new InicioController(
                inicioView,
                usuario,
                () -> respuestasCuestionario,
                () -> vista.switchCard(BienvenidaView.CARD_BIENESTAR)
        );
        vista.addCard(inicioView, BienvenidaView.CARD_INICIO);

        CalendarioView calendarioView = new CalendarioView();
        calendarioCtrl = new CalendarioController(calendarioView, usuario);
        vista.addCard(calendarioView, BienvenidaView.CARD_CALENDARIO);

        ListaTareasView listaTareasView = new ListaTareasView();
        listaTareasCtrl = new ListaTareasController(listaTareasView, usuario);
        vista.addCard(listaTareasView, BienvenidaView.CARD_TAREAS);

        CrearTareaView crearTareaView = new CrearTareaView();
        new CrearTareaController(crearTareaView, usuario, () -> {
            listaTareasCtrl.cargarTareas();
            vista.switchCard(BienvenidaView.CARD_TAREAS);
        });
        vista.addCard(crearTareaView, BienvenidaView.CARD_CREAR_TAREA);

        CuestionarioView cuestionarioView = new CuestionarioView();
        new CuestionarioController(cuestionarioView, usuario.getId(), respuestas -> {
            respuestasCuestionario = respuestas;
            inicioCtrl.cargar();
            vista.switchCard(BienvenidaView.CARD_BIENESTAR);
        });
        vista.addCard(cuestionarioView, BienvenidaView.CARD_BIENESTAR);

        PerfilPanelView perfilView = new PerfilPanelView();
        perfilPanelCtrl = new PerfilPanelController(perfilView, usuario);
        vista.addCard(perfilView, BienvenidaView.CARD_PERFIL);
    }

    private void conectarNavegacion() {
        conectarNavBtn(BienvenidaView.CARD_INICIO,      () -> inicioCtrl.cargar());
        conectarNavBtn(BienvenidaView.CARD_CALENDARIO,  () -> calendarioCtrl.cargar());
        conectarNavBtn(BienvenidaView.CARD_TAREAS,      () -> listaTareasCtrl.cargarTareas());
        conectarNavBtn(BienvenidaView.CARD_CREAR_TAREA, () -> {});
        conectarNavBtn(BienvenidaView.CARD_BIENESTAR,   () -> {});
        conectarNavBtn(BienvenidaView.CARD_PERFIL,      () -> perfilPanelCtrl.cargar());

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

    private void registrarLoginDiario() {
        new Thread(() -> {
            try {
                RachaModel racha = apiService.registrarLogin(usuario.getId());

                if (racha.isLoginNuevo()) {
                    SwingUtilities.invokeLater(() -> {
                        RachaNotificacionView dlg = new RachaNotificacionView(vista, racha);
                        Timer t = new Timer(600, e -> dlg.setVisible(true));
                        t.setRepeats(false);
                        t.start();
                    });
                }
            } catch (Exception ex) {
                System.err.println("Error al registrar login diario: " + ex.getMessage());
            }
        }).start();
    }
}