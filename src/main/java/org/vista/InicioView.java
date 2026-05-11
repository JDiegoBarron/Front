package org.vista;

import org.modelo.ResultadoEvaluacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InicioView extends JPanel {

    private final CardLayout  innerLayout  = new CardLayout();
    private final JPanel      innerPanel   = new JPanel(innerLayout);

    private static final String CARD_CARGANDO   = "cargando";
    private static final String CARD_SIN_DATOS  = "sin_datos";
    private static final String CARD_RESULTADOS = "resultados";

    private JLabel  lblNivelTexto;
    private JLabel  lblNivelValor;
    private JLabel  lblSituacion;
    private JLabel  lblDescSituacion;
    private JPanel  gaugeFill;
    private JLabel  lblCarga;
    private JPanel  fillCarga;
    private JLabel  lblEstresPerc;
    private JPanel  fillEstresPerc;
    private JLabel[] lblAreas;
    private JPanel[] fillAreas;
    private JButton btnActualizar;
    private JButton btnCuestionario;

    private Runnable onActualizar;
    private Runnable onIrCuestionario;

    public InicioView() {
        setLayout(new BorderLayout());
        setBackground(BienvenidaView.CONTENT_BG);

        innerPanel.setBackground(BienvenidaView.CONTENT_BG);
        innerPanel.add(buildCardCargando(),  CARD_CARGANDO);
        innerPanel.add(buildCardSinDatos(),  CARD_SIN_DATOS);
        innerPanel.add(buildCardResultados(),CARD_RESULTADOS);
        add(innerPanel, BorderLayout.CENTER);

        mostrarCargando();
    }

    public void mostrarCargando()  { innerLayout.show(innerPanel, CARD_CARGANDO); }
    public void mostrarSinDatos()  { innerLayout.show(innerPanel, CARD_SIN_DATOS); }
    public void mostrarResultados(ResultadoEvaluacion r) {
        actualizarDashboard(r);
        innerLayout.show(innerPanel, CARD_RESULTADOS);
    }

    public void setOnActualizar(Runnable cb)      { onActualizar     = cb; }
    public void setOnIrCuestionario(Runnable cb)  { onIrCuestionario = cb; }

    private JPanel buildCardCargando() {
        JPanel p = centrado(BienvenidaView.CONTENT_BG);
        JLabel l = new JLabel("Calculando evaluación...");
        l.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        l.setForeground(Color.GRAY);
        p.add(l);
        return p;
    }

    private JPanel buildCardSinDatos() {
        JPanel p = centrado(BienvenidaView.CONTENT_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.insets = new Insets(8, 0, 8, 0);

        gbc.gridy = 0;
        JLabel ic = new JLabel("📋", SwingConstants.CENTER);
        ic.setFont(new Font("Segoe UI", Font.PLAIN, 52));
        p.add(ic, gbc);

        gbc.gridy = 1;
        JLabel tl = new JLabel("Completa el cuestionario de bienestar");
        tl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tl.setForeground(BienvenidaView.TEXT_DARK);
        p.add(tl, gbc);

        gbc.gridy = 2;
        JLabel sl = new JLabel("Tu evaluación de estrés aparecerá aquí una vez respondido.");
        sl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sl.setForeground(Color.GRAY);
        p.add(sl, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(18, 0, 0, 0);
        JButton btn = buildBoton("Ir al cuestionario", BienvenidaView.ACCENT);
        btn.addActionListener(e -> { if (onIrCuestionario != null) onIrCuestionario.run(); });
        p.add(btn, gbc);
        return p;
    }

    private JPanel buildCardResultados() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BienvenidaView.CONTENT_BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 222, 235)),
                new EmptyBorder(16, 28, 16, 28)));

        JLabel titulo = new JLabel("Evaluación de estrés");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(BienvenidaView.TEXT_DARK);
        header.add(titulo, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Color.WHITE);
        btnCuestionario = buildBoton("↻  Nuevo cuestionario", new Color(100, 105, 160));
        btnCuestionario.addActionListener(e -> { if (onIrCuestionario != null) onIrCuestionario.run(); });
        btnActualizar = buildBoton("↺  Actualizar datos", BienvenidaView.ACCENT);
        btnActualizar.addActionListener(e -> { if (onActualizar != null) onActualizar.run(); });
        btnPanel.add(btnCuestionario);
        btnPanel.add(btnActualizar);
        header.add(btnPanel, BorderLayout.EAST);
        wrapper.add(header, BorderLayout.NORTH);

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(BienvenidaView.CONTENT_BG);
        contenido.setBorder(new EmptyBorder(24, 36, 30, 36));

        JPanel filaTop = new JPanel(new GridLayout(1, 2, 24, 0));
        filaTop.setBackground(BienvenidaView.CONTENT_BG);
        filaTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        filaTop.setAlignmentX(LEFT_ALIGNMENT);
        filaTop.add(buildCardGauge());
        filaTop.add(buildCardSituacion());
        contenido.add(filaTop);
        contenido.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel filaCombo = new JPanel(new GridLayout(1, 2, 24, 0));
        filaCombo.setBackground(BienvenidaView.CONTENT_BG);
        filaCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        filaCombo.setAlignmentX(LEFT_ALIGNMENT);
        filaCombo.add(buildCardMetrica("Carga académica (tareas)", a -> {
            lblCarga  = (JLabel) a[0]; fillCarga  = (JPanel) a[1];
        }));
        filaCombo.add(buildCardMetrica("Estrés percibido (cuestionario)", a -> {
            lblEstresPerc = (JLabel) a[0]; fillEstresPerc = (JPanel) a[1];
        }));
        contenido.add(filaCombo);
        contenido.add(Box.createRigidArea(new Dimension(0, 20)));

        contenido.add(buildAreasTitulo());
        contenido.add(Box.createRigidArea(new Dimension(0, 10)));
        contenido.add(buildAreasGrid());

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BienvenidaView.CONTENT_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildCardGauge() {
        JPanel card = card();
        card.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL;

        g.gridy = 0;
        JLabel lTitle = new JLabel("Nivel global de estrés", SwingConstants.CENTER);
        lTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lTitle.setForeground(Color.GRAY);
        card.add(lTitle, g);

        g.gridy = 1; g.insets = new Insets(10, 0, 4, 0);
        lblNivelValor = new JLabel("—", SwingConstants.CENTER);
        lblNivelValor.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblNivelValor.setForeground(BienvenidaView.TEXT_DARK);
        card.add(lblNivelValor, g);

        g.gridy = 2; g.insets = new Insets(0, 20, 6, 20);
        JPanel gaugeBg = new JPanel(new BorderLayout());
        gaugeBg.setBackground(new Color(220, 222, 235));
        gaugeBg.setPreferredSize(new Dimension(0, 10));
        gaugeBg.setBorder(BorderFactory.createLineBorder(new Color(200, 202, 220)));
        gaugeFill = new JPanel();
        gaugeFill.setBackground(Color.GRAY);
        gaugeBg.add(gaugeFill, BorderLayout.WEST);
        card.add(gaugeBg, g);

        g.gridy = 3; g.insets = new Insets(0, 0, 0, 0);
        lblNivelTexto = new JLabel("—", SwingConstants.CENTER);
        lblNivelTexto.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNivelTexto.setForeground(Color.GRAY);
        card.add(lblNivelTexto, g);

        return card;
    }

    private JPanel buildCardSituacion() {
        JPanel card = card();
        card.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL;

        g.gridy = 0;
        JLabel lTitle = new JLabel("Situación identificada", SwingConstants.CENTER);
        lTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lTitle.setForeground(Color.GRAY);
        card.add(lTitle, g);

        g.gridy = 1; g.insets = new Insets(14, 8, 6, 8);
        lblSituacion = new JLabel("—", SwingConstants.CENTER);
        lblSituacion.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSituacion.setForeground(BienvenidaView.TEXT_DARK);
        card.add(lblSituacion, g);

        g.gridy = 2; g.insets = new Insets(0, 12, 0, 12);
        lblDescSituacion = new JLabel("—", SwingConstants.CENTER);
        lblDescSituacion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDescSituacion.setForeground(Color.GRAY);
        card.add(lblDescSituacion, g);

        return card;
    }

    private JPanel buildCardMetrica(String titulo, java.util.function.Consumer<Object[]> refs) {
        JPanel card = card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel tl = new JLabel(titulo);
        tl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tl.setForeground(Color.GRAY);
        tl.setAlignmentX(LEFT_ALIGNMENT);
        card.add(tl);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel valLabel = new JLabel("—");
        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        valLabel.setForeground(BienvenidaView.TEXT_DARK);
        valLabel.setAlignmentX(LEFT_ALIGNMENT);
        card.add(valLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        JPanel baraBg = new JPanel(new BorderLayout());
        baraBg.setBackground(new Color(220, 222, 235));
        baraBg.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        baraBg.setPreferredSize(new Dimension(0, 8));
        JPanel fill = new JPanel();
        fill.setBackground(Color.GRAY);
        baraBg.add(fill, BorderLayout.WEST);
        baraBg.setAlignmentX(LEFT_ALIGNMENT);
        card.add(baraBg);

        refs.accept(new Object[]{ valLabel, fill });
        return card;
    }

    private JLabel buildAreasTitulo() {
        JLabel l = new JLabel("Desglose por área");
        l.setFont(new Font("Segoe UI", Font.BOLD, 15));
        l.setForeground(BienvenidaView.TEXT_DARK);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private JPanel buildAreasGrid() {
        JPanel grid = new JPanel(new GridLayout(3, 2, 14, 10));
        grid.setBackground(BienvenidaView.CONTENT_BG);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        grid.setAlignmentX(LEFT_ALIGNMENT);

        String[] nombres = ResultadoEvaluacion.NOMBRES_AREAS;
        lblAreas  = new JLabel[6];
        fillAreas = new JPanel[6];
        Color[] colores = {
                new Color(21,101,192), new Color(0,131,143), new Color(106,27,154),
                new Color(46,125,50),  new Color(230,119,0), new Color(183,28,28)
        };

        for (int i = 0; i < 6; i++) {
            final int idx = i;
            JPanel mini = card();
            mini.setLayout(new BoxLayout(mini, BoxLayout.Y_AXIS));

            JPanel filaNombre = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            filaNombre.setBackground(Color.WHITE);
            JPanel dot = new JPanel();
            dot.setBackground(colores[i]);
            dot.setPreferredSize(new Dimension(10, 10));
            filaNombre.add(dot);
            filaNombre.add(Box.createRigidArea(new Dimension(6,0)));
            JLabel lNombre = new JLabel(nombres[i]);
            lNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lNombre.setForeground(Color.GRAY);
            filaNombre.add(lNombre);
            filaNombre.setAlignmentX(LEFT_ALIGNMENT);
            mini.add(filaNombre);
            mini.add(Box.createRigidArea(new Dimension(0,6)));

            lblAreas[i] = new JLabel("—");
            lblAreas[i].setFont(new Font("Segoe UI", Font.BOLD, 22));
            lblAreas[i].setForeground(colores[i]);
            lblAreas[i].setAlignmentX(LEFT_ALIGNMENT);
            mini.add(lblAreas[i]);
            mini.add(Box.createRigidArea(new Dimension(0,5)));

            JPanel baraBg = new JPanel(new BorderLayout());
            baraBg.setBackground(new Color(220,222,235));
            baraBg.setMaximumSize(new Dimension(Integer.MAX_VALUE, 6));
            baraBg.setPreferredSize(new Dimension(0, 6));
            fillAreas[i] = new JPanel();
            fillAreas[i].setBackground(colores[i]);
            baraBg.add(fillAreas[i], BorderLayout.WEST);
            baraBg.setAlignmentX(LEFT_ALIGNMENT);
            mini.add(baraBg);
            grid.add(mini);
        }
        return grid;
    }

    private void actualizarDashboard(ResultadoEvaluacion r) {
        // Nivel global
        Color color = ResultadoEvaluacion.colorNivel(r.getNivelGlobal());
        lblNivelValor.setText(String.format("%.1f", r.getNivelGlobal()));
        lblNivelValor.setForeground(color);
        lblNivelTexto.setText("Estrés " + r.getNivelLabel());
        lblNivelTexto.setForeground(color);
        setBaraPorcentaje(gaugeFill, r.getNivelGlobal(), color);

        // Situación
        lblSituacion.setText(r.getSituacion());
        lblDescSituacion.setText(descSituacion(r.getSituacion()));

        // Carga académica
        Color colorCarga = ResultadoEvaluacion.colorNivel(r.getCargaAcademica());
        lblCarga.setText(String.format("%.1f", r.getCargaAcademica()));
        lblCarga.setForeground(colorCarga);
        setBaraPorcentaje(fillCarga, r.getCargaAcademica(), colorCarga);

        // Estrés percibido
        Color colorPerc = ResultadoEvaluacion.colorNivel(r.getEstresPercibido());
        lblEstresPerc.setText(String.format("%.1f", r.getEstresPercibido()));
        lblEstresPerc.setForeground(colorPerc);
        setBaraPorcentaje(fillEstresPerc, r.getEstresPercibido(), colorPerc);

        // Áreas
        double[] areas = r.getAreas();
        for (int i = 0; i < 6; i++) {
            Color ca = ResultadoEvaluacion.colorNivel(areas[i]);
            lblAreas[i].setText(String.format("%.1f", areas[i])
                    + "  " + ResultadoEvaluacion.etiquetaNivel(areas[i]));
            lblAreas[i].setForeground(ca);
            fillAreas[i].setBackground(ca);
            setBaraPorcentaje(fillAreas[i], areas[i], ca);
        }

        revalidate(); repaint();
    }

    private void setBaraPorcentaje(JPanel fill, double valor, Color color) {
        fill.setBackground(color);
        fill.putClientProperty("pct", valor / 5.0);
        fill.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentResized(java.awt.event.ComponentEvent e) {
                Component parent = fill.getParent();
                if (parent == null) return;
                Double pct = (Double) fill.getClientProperty("pct");
                if (pct == null) return;
                int w = (int)(parent.getWidth() * pct);
                fill.setPreferredSize(new Dimension(w, parent.getHeight()));
                parent.revalidate();
            }
        });
    }

    private String descSituacion(String s) {
        return switch (s) {
            case "Riesgo alto"               -> "Carga alta y estrés elevado. Considera pedir apoyo.";
            case "Estudiante resiliente"     -> "Alta carga pero bajo estrés. ¡Vas muy bien!";
            case "Posible problema emocional"-> "Estrés elevado con poca carga. Habla con alguien.";
            default                          -> "Tu carga y estrés están en equilibrio.";
        };
    }

    private JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 218, 235), 1, true),
                new EmptyBorder(16, 18, 16, 18)));
        return p;
    }

    private JPanel centrado(Color bg) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(bg);
        return p;
    }

    private JButton buildBoton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}