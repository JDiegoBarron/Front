package org.vista;

import org.modelo.CosmeticoModel;
import org.modelo.TemaApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class PerfilPanelView extends JPanel {

    private JTextField  campoCorreo;
    private JTextField  campoCarrera;
    private JSpinner    campoSemestre;
    private JButton     btnGuardarPerfil;
    private JLabel      lblMensajePerfil;

    private JPanel      gridTemas;
    private JPanel      gridMarcos;
    private JLabel      lblMonedas;

    private AvatarPanel avatarPanel;
    private JLabel      lblNombre;
    private JLabel      lblUsername;
    private JLabel      lblMonedasHeader;
    private JLabel      lblRachaHeader;

    private Runnable               onGuardarPerfil;
    private IntConsumer            onComprarCosmético;   // id del cosmético
    private IntConsumer            onActivarCosmético;   // id del cosmético
    private Runnable               onRefreshPerfil;

    public PerfilPanelView() {
        setLayout(new BorderLayout());
        setBackground(BienvenidaView.CONTENT_BG);

        add(buildTopHeader(), BorderLayout.NORTH);
        add(buildTabs(),      BorderLayout.CENTER);
    }

    private JPanel buildTopHeader() {
        TemaApp.Tema tema = TemaApp.getTema();

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 222, 235)),
                new EmptyBorder(20, 28, 20, 28)));

        avatarPanel = new AvatarPanel(52);
        avatarPanel.setPreferredSize(new Dimension(64, 64));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(0, 14, 0, 0));

        lblNombre = new JLabel("Cargando...");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNombre.setForeground(BienvenidaView.TEXT_DARK);

        lblUsername = new JLabel("@usuario");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUsername.setForeground(Color.GRAY);

        infoPanel.add(lblNombre);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(lblUsername);

        JPanel izquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        izquierdo.setBackground(Color.WHITE);
        izquierdo.add(avatarPanel);
        izquierdo.add(infoPanel);
        header.add(izquierdo, BorderLayout.WEST);

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        statsPanel.setBackground(Color.WHITE);

        statsPanel.add(buildStatChip("", "0", "Monedas", e -> lblMonedasHeader = e));
        statsPanel.add(buildStatChip("", "0", "Racha",   e -> lblRachaHeader   = e));

        header.add(statsPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel buildStatChip(String icono, String valor, String etiqueta,
                                  Consumer<JLabel> exportarLabel) {
        JPanel chip = new JPanel();
        chip.setLayout(new BoxLayout(chip, BoxLayout.Y_AXIS));
        chip.setBackground(new Color(248, 249, 252));
        chip.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 228, 245), 1, true),
                new EmptyBorder(8, 16, 8, 16)));

        JLabel iconLbl = new JLabel(icono + "  " + valor, SwingConstants.CENTER);
        iconLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        iconLbl.setForeground(BienvenidaView.TEXT_DARK);
        iconLbl.setAlignmentX(CENTER_ALIGNMENT);

        JLabel tagLbl = new JLabel(etiqueta, SwingConstants.CENTER);
        tagLbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        tagLbl.setForeground(Color.GRAY);
        tagLbl.setAlignmentX(CENTER_ALIGNMENT);

        chip.add(iconLbl);
        chip.add(tagLbl);

        exportarLabel.accept(iconLbl);
        return chip;
    }

    private JPanel buildTabs() {
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabs.setBackground(BienvenidaView.CONTENT_BG);
        tabs.setBorder(BorderFactory.createEmptyBorder());

        tabs.addTab("  Mi perfil  ",    buildTabPerfil());
        tabs.addTab("  Cosmética  ",    buildTabCosmetica());
        // tabs.addTab("  Estadísticas  ", buildTabEstadisticas()); todo: futura implementacion

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BienvenidaView.CONTENT_BG);
        wrapper.add(tabs, BorderLayout.CENTER);
        return wrapper;
    }

    // ── Pestaña 1: Mi Perfil ──────────────────────────────────────────────────

    private JPanel buildTabPerfil() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BienvenidaView.CONTENT_BG);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 218, 235), 1, true),
                new EmptyBorder(28, 32, 28, 32)));

        form.add(buildLabel("Correo electrónico"));
        campoCorreo = buildTextField("correo@ejemplo.com");
        form.add(campoCorreo);
        form.add(Box.createRigidArea(new Dimension(0, 14)));

        form.add(buildLabel("Carrera"));
        campoCarrera = buildTextField("Nombre de tu carrera");
        form.add(campoCarrera);
        form.add(Box.createRigidArea(new Dimension(0, 14)));

        form.add(buildLabel("Semestre"));
        campoSemestre = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        campoSemestre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoSemestre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        campoSemestre.setAlignmentX(LEFT_ALIGNMENT);
        form.add(campoSemestre);
        form.add(Box.createRigidArea(new Dimension(0, 20)));

        lblMensajePerfil = new JLabel("", SwingConstants.CENTER);
        lblMensajePerfil.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMensajePerfil.setForeground(new Color(183, 28, 28));
        lblMensajePerfil.setAlignmentX(CENTER_ALIGNMENT);
        form.add(lblMensajePerfil);
        form.add(Box.createRigidArea(new Dimension(0, 10)));

        btnGuardarPerfil = buildBotonPrimario("Guardar cambios");
        btnGuardarPerfil.addActionListener(e -> { if (onGuardarPerfil != null) onGuardarPerfil.run(); });
        btnGuardarPerfil.setAlignmentX(CENTER_ALIGNMENT);
        form.add(btnGuardarPerfil);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.55;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(28, 28, 28, 28);
        outer.add(form, gbc);

        JScrollPane sp = new JScrollPane(outer);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BienvenidaView.CONTENT_BG);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(sp, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildTabCosmetica() {
        JPanel outer = new JPanel();
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
        outer.setBackground(BienvenidaView.CONTENT_BG);
        outer.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel balancePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        balancePanel.setBackground(BienvenidaView.CONTENT_BG);
        balancePanel.setAlignmentX(LEFT_ALIGNMENT);
        balancePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        lblMonedas = new JLabel("0 monedas disponibles");
        lblMonedas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMonedas.setForeground(new Color(140, 100, 0));
        balancePanel.add(lblMonedas);

        outer.add(balancePanel);
        outer.add(Box.createRigidArea(new Dimension(0, 20)));

        outer.add(buildSeccionTitulo("Temas de color"));
        outer.add(Box.createRigidArea(new Dimension(0, 12)));

        gridTemas = new JPanel(new WrapLayout(FlowLayout.LEFT, 12, 12));
        gridTemas.setBackground(BienvenidaView.CONTENT_BG);
        gridTemas.setAlignmentX(LEFT_ALIGNMENT);
        outer.add(gridTemas);
        outer.add(Box.createRigidArea(new Dimension(0, 24)));

        outer.add(buildSeccionTitulo("Marcos de avatar"));
        outer.add(Box.createRigidArea(new Dimension(0, 12)));

        gridMarcos = new JPanel(new WrapLayout(FlowLayout.LEFT, 12, 12));
        gridMarcos.setBackground(BienvenidaView.CONTENT_BG);
        gridMarcos.setAlignmentX(LEFT_ALIGNMENT);
        outer.add(gridMarcos);
        outer.add(Box.createRigidArea(new Dimension(0, 30)));

        JScrollPane sp = new JScrollPane(outer);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BienvenidaView.CONTENT_BG);
        sp.getVerticalScrollBar().setUnitIncrement(14);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(sp, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildTabEstadisticas() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BienvenidaView.CONTENT_BG);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 218, 235), 1, true),
                new EmptyBorder(28, 32, 28, 32)));

        content.add(buildStatRow("Racha actual",     "0 días",  new Color(211, 47, 47)));
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        content.add(buildStatRow("Mejor racha",      "0 días",  new Color(180, 130, 0)));
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        content.add(buildStatRow("Monedas acumuladas","0",      new Color(120, 80, 0)));
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        content.add(buildStatRow("Tareas completadas","0",      new Color(27, 94, 32)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.6;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(28, 28, 28, 28);
        outer.add(content, gbc);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BienvenidaView.CONTENT_BG);
        wrapper.add(outer, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildStatRow(String nombre, String valor, Color color) {
        JPanel row = new JPanel(new BorderLayout(14, 0));
        row.setBackground(new Color(248, 249, 252));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, color),
                new EmptyBorder(12, 16, 12, 16)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        row.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lNombre = new JLabel(nombre);
        lNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lNombre.setForeground(BienvenidaView.TEXT_DARK);

        JLabel lValor = new JLabel(valor, SwingConstants.RIGHT);
        lValor.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lValor.setForeground(color);

        row.add(lNombre, BorderLayout.WEST);
        row.add(lValor,  BorderLayout.EAST);
        return row;
    }

    public void mostrarCosmeticos(List<CosmeticoModel> cosmeticos) {
        gridTemas.removeAll();
        gridMarcos.removeAll();

        for (CosmeticoModel c : cosmeticos) {
            JPanel card = buildCosmeticoCard(c);
            if (c.getTipo() == CosmeticoModel.Tipo.TEMA)  gridTemas.add(card);
            else                                           gridMarcos.add(card);
        }

        gridTemas.revalidate();
        gridTemas.repaint();
        gridMarcos.revalidate();
        gridMarcos.repaint();
    }

    private JPanel buildCosmeticoCard(CosmeticoModel c) {
        TemaApp.Tema tema = TemaApp.getTema();
        boolean esActivo  = c.isActivo();
        boolean esGratis  = c.getPrecio() == 0;

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(esActivo ? new Color(tema.accent.getRed(),
                tema.accent.getGreen(), tema.accent.getBlue(), 15) : Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        esActivo ? tema.accent : new Color(215, 218, 235), esActivo ? 2 : 1, true),
                new EmptyBorder(12, 14, 12, 14)));
        card.setPreferredSize(new Dimension(148, 130));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Previsualización del cosmético
        JPanel preview = buildCosmeticoPreview(c);
        preview.setAlignmentX(CENTER_ALIGNMENT);
        card.add(preview);
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        // Nombre
        JLabel lNombre = new JLabel(c.getNombre(), SwingConstants.CENTER);
        lNombre.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lNombre.setForeground(BienvenidaView.TEXT_DARK);
        lNombre.setAlignmentX(CENTER_ALIGNMENT);
        card.add(lNombre);
        card.add(Box.createRigidArea(new Dimension(0, 4)));

        // Estado / precio
        JPanel botonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        botonPanel.setOpaque(false);
        botonPanel.setAlignmentX(CENTER_ALIGNMENT);

        if (esActivo) {
            JLabel lActivo = new JLabel("Activo");
            lActivo.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lActivo.setForeground(tema.accent);
            botonPanel.add(lActivo);
        } else if (c.isComprado() || esGratis) {
            JButton btnActivar = buildBotonMini("Activar", tema.accent);
            btnActivar.addActionListener(e -> { if (onActivarCosmético != null) onActivarCosmético.accept(c.getId()); });
            botonPanel.add(btnActivar);
        } else {
            JButton btnComprar = buildBotonMini("" + c.getPrecio(), new Color(170, 120, 0));
            btnComprar.addActionListener(e -> { if (onComprarCosmético != null) onComprarCosmético.accept(c.getId()); });
            botonPanel.add(btnComprar);
        }

        card.add(botonPanel);

        // Hover efecto
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!esActivo) card.setBackground(new Color(248, 249, 255));
            }
            public void mouseExited(MouseEvent e) {
                if (!esActivo) card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    private JPanel buildCosmeticoPreview(CosmeticoModel c) {
        if (c.getTipo() == CosmeticoModel.Tipo.MARCO) {
            return new JPanel() {
                { setOpaque(false); setPreferredSize(new Dimension(48, 48)); }
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    // Avatar placeholder
                    g2.setColor(new Color(200, 210, 230));
                    g2.fillOval(6, 6, 36, 36);
                    g2.setColor(Color.WHITE.darker());
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    g2.drawString("A", 17, 30);
                    // Marco
                    int savedMarco = TemaApp.getMarcoIdx();
                    TemaApp.setMarco(c.getIndiceLocal());
                    TemaApp.dibujarMarco(g2, 24, 24, 18);
                    TemaApp.setMarco(savedMarco); // restaurar
                    g2.dispose();
                }
            };
        } else {
            TemaApp.Tema[] temas = TemaApp.TEMAS;
            int idx = c.getIndiceLocal();
            if (idx < 0 || idx >= temas.length) idx = 0;
            TemaApp.Tema t = temas[idx];

            JPanel mini = new JPanel(null);
            mini.setBackground(t.sidebarBg);
            mini.setPreferredSize(new Dimension(68, 42));
            mini.setBorder(BorderFactory.createLineBorder(new Color(200, 202, 220), 1, true));

            JPanel bar = new JPanel();
            bar.setBackground(t.sidebarDark);
            bar.setBounds(0, 0, 18, 42);
            mini.add(bar);

            JPanel cont = new JPanel();
            cont.setBackground(t.contentBg);
            cont.setBounds(18, 0, 50, 42);
            mini.add(cont);

            JPanel acc = new JPanel() {
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(t.accent);
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }
            };
            acc.setBounds(40, 16, 10, 10);
            acc.setOpaque(false);
            mini.add(acc);

            return mini;
        }
    }

    public void setNombre(String nombre)   { lblNombre.setText(nombre); }
    public void setUsername(String u)      { lblUsername.setText("@" + u); }

    public void setMonedas(int monedas) {
        lblMonedasHeader.setText("" + monedas);
        lblMonedas.setText(monedas + " monedas disponibles");
    }

    public void setRacha(int racha) {
        lblRachaHeader.setText("" + racha);
    }

    // Pestaña perfil
    public String getCorreo()   {
        String t = campoCorreo.getText().trim();
        return t.equals("correo@ejemplo.com") ? "" : t;
    }
    public String getCarrera()  {
        String t = campoCarrera.getText().trim();
        return t.equals("Nombre de tu carrera") ? "" : t;
    }
    public int getSemestre()    { return (int) campoSemestre.getValue(); }

    public void setCorreo(String v)    {
        if (v != null && !v.isEmpty()) { campoCorreo.setText(v); campoCorreo.setForeground(Color.BLACK); }
    }
    public void setCarrera(String v)   {
        if (v != null && !v.isEmpty()) { campoCarrera.setText(v); campoCarrera.setForeground(Color.BLACK); }
    }
    public void setSemestre(int v)     { campoSemestre.setValue(v); }

    public void setMensajePerfil(String msg)      { lblMensajePerfil.setForeground(new Color(183,28,28)); lblMensajePerfil.setText(msg); }
    public void setMensajePerfilExito(String msg) { lblMensajePerfil.setForeground(new Color(27,94,32));  lblMensajePerfil.setText(msg); }
    public void setBloqueadoPerfil(boolean b)     { btnGuardarPerfil.setEnabled(!b); }

    // Callbacks
    public void setOnGuardarPerfil(Runnable cb)          { onGuardarPerfil    = cb; }
    public void setOnComprarCosmetico(IntConsumer cb)     { onComprarCosmético = cb; }
    public void setOnActivarCosmetico(IntConsumer cb)     { onActivarCosmético = cb; }
    public void setOnRefresh(Runnable cb)                 { onRefreshPerfil    = cb; }


    private JLabel buildLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(90, 95, 140));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(0, 0, 4, 0));
        return lbl;
    }

    private JTextField buildTextField(String placeholder) {
        JTextField tf = new JTextField(placeholder);
        tf.setForeground(Color.GRAY);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 202, 220), 1),
                new EmptyBorder(8, 12, 8, 12)));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        tf.setAlignmentX(LEFT_ALIGNMENT);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) {
                    tf.setText(""); tf.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) {
                    tf.setText(placeholder); tf.setForeground(Color.GRAY);
                }
            }
        });
        return tf;
    }

    private JButton buildBotonPrimario(String texto) {
        TemaApp.Tema tema = TemaApp.getTema();
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(tema.accent);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(10, 28, 10, 28));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(220, 44));
        return btn;
    }

    private JButton buildBotonMini(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn.setForeground(color);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 1, true),
                new EmptyBorder(3, 8, 3, 8)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color); btn.setForeground(Color.WHITE); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(Color.WHITE); btn.setForeground(color); }
        });
        return btn;
    }

    private JLabel buildSeccionTitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(BienvenidaView.TEXT_DARK);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 222, 235)),
                new EmptyBorder(0, 0, 8, 0)));
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return lbl;
    }

    public class AvatarPanel extends JPanel {
        private String iniciales = "?";
        private final int radio;

        public AvatarPanel(int radio) {
            this.radio = radio;
            setOpaque(false);
            setPreferredSize(new Dimension(radio * 2 + 12, radio * 2 + 12));
        }

        public void setIniciales(String nombre) {
            String[] partes = nombre.trim().split("\\s+");
            iniciales = partes.length >= 2
                    ? String.valueOf(partes[0].charAt(0)) + partes[1].charAt(0)
                    : String.valueOf(nombre.charAt(0));
            iniciales = iniciales.toUpperCase();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            TemaApp.Tema tema = TemaApp.getTema();
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;

            // Círculo de fondo
            g2.setColor(tema.sidebarBg);
            g2.fill(new Ellipse2D.Float(cx - radio, cy - radio, radio * 2, radio * 2));

            // Iniciales
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, (int)(radio * 0.65)));
            FontMetrics fm = g2.getFontMetrics();
            int textW = fm.stringWidth(iniciales);
            int textH = fm.getAscent() - fm.getDescent();
            g2.drawString(iniciales, cx - textW / 2, cy + textH / 2);

            // Marco
            TemaApp.dibujarMarco(g2, cx, cy, radio);

            g2.dispose();
        }
    }

    private static class WrapLayout extends FlowLayout {
        public WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }

        @Override
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }

        @Override
        public Dimension minimumLayoutSize(Container target) {
            return layoutSize(target, false);
        }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;
                if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;

                int hgap = getHgap(), vgap = getVgap();
                Insets insets = target.getInsets();
                int maxWidth = targetWidth - insets.left - insets.right;

                int x = 0, y = insets.top + vgap, height = 0;
                for (Component m : target.getComponents()) {
                    if (!m.isVisible()) continue;
                    Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                    if (x + d.width > maxWidth && x > 0) {
                        y += height + vgap;
                        x = 0;
                        height = 0;
                    }
                    x += d.width + hgap;
                    height = Math.max(height, d.height);
                }
                y += height + vgap + insets.bottom;
                return new Dimension(targetWidth, y);
            }
        }
    }

    public AvatarPanel getAvatarPanel() {
        return avatarPanel;
    }
}
