package org.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

public class BienvenidaView extends JFrame {

    public static final Color SIDEBAR_BG     = new Color(32, 40, 120);
    public static final Color SIDEBAR_DARK   = new Color(24, 30, 95);
    public static final Color SIDEBAR_HOVER  = new Color(55, 65, 170);
    public static final Color SIDEBAR_ACTIVE = new Color(80, 90, 200);
    public static final Color CONTENT_BG     = new Color(245, 247, 250);
    public static final Color ACCENT         = new Color(80, 90, 200);
    public static final Color TEXT_DARK      = new Color(40, 40, 80);

    private final CardLayout                 cardLayout   = new CardLayout();
    private final JPanel                     contentArea  = new JPanel(cardLayout);
    private final Map<String, JButton>       navButtons   = new LinkedHashMap<>();
    private       String                     activeCard   = "";

    private JButton botonCerrarSesion;

    public static final String CARD_INICIO      = "inicio";
    public static final String CARD_CALENDARIO  = "calendario";
    public static final String CARD_TAREAS      = "tareas";
    public static final String CARD_CREAR_TAREA = "crear_tarea";
    public static final String CARD_BIENESTAR   = "bienestar";
    public static final String CARD_PERFIL      = "perfil";

    public BienvenidaView(String nombreCompleto) {
        setTitle("Mi Agenda · UPIIZ");
        setSize(960, 620);
        setMinimumSize(new Dimension(780, 520));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        add(buildSidebar(nombreCompleto), BorderLayout.WEST);

        contentArea.setBackground(CONTENT_BG);
        add(contentArea, BorderLayout.CENTER);

        addCard(buildPlaceholder("📅", "Calendario", "Próximamente"), CARD_CALENDARIO);
        addCard(buildPlaceholder("❤", "Bienestar", "Cuestionario de bienestar próximamente"), CARD_BIENESTAR);
        addCard(buildPlaceholder("◉", "Mi Perfil", "Gestión del perfil próximamente"), CARD_PERFIL);

        setVisible(true);
    }

    private JPanel buildSidebar(String nombreCompleto) {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(210, 0));

        sidebar.add(buildSidebarHeader());
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));

        String[][] items = {
                { CARD_INICIO,      "\u2302", "Inicio"          },
                { CARD_CALENDARIO,  "\u25A1", "Calendario"       },
                { CARD_TAREAS,      "\u2611", "Lista de tareas"  },
                { CARD_CREAR_TAREA, "+",      "Crear tarea"      },
                { CARD_BIENESTAR,   "\u2665", "Bienestar"        },
                { CARD_PERFIL,      "\u25CE", "Mi perfil"        },
        };
        for (String[] item : items) {
            JButton btn = buildNavBtn(item[1] + "  " + item[2], item[0]);
            navButtons.put(item[0], btn);
            sidebar.add(btn);
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(buildUserFooter(nombreCompleto));

        return sidebar;
    }

    private JPanel buildSidebarHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(SIDEBAR_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(22, 20, 22, 20));
        header.setMaximumSize(new Dimension(210, 88));
        header.setAlignmentX(LEFT_ALIGNMENT);

        JLabel appName = new JLabel("Mi Agenda");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 17));
        appName.setForeground(Color.WHITE);
        appName.setAlignmentX(LEFT_ALIGNMENT);

        JLabel campus = new JLabel("UPIIZ · IPN");
        campus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        campus.setForeground(new Color(160, 170, 230));
        campus.setAlignmentX(LEFT_ALIGNMENT);

        header.add(appName);
        header.add(Box.createRigidArea(new Dimension(0, 4)));
        header.add(campus);
        return header;
    }

    private JButton buildNavBtn(String label, String cardKey) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(new Color(195, 205, 245));
        btn.setBackground(SIDEBAR_BG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(210, 42));
        btn.setPreferredSize(new Dimension(210, 42));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 16));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (!cardKey.equals(activeCard)) {
                    btn.setBackground(SIDEBAR_HOVER);
                    btn.setForeground(Color.WHITE);
                }
            }
            @Override public void mouseExited(MouseEvent e) {
                if (!cardKey.equals(activeCard)) {
                    btn.setBackground(SIDEBAR_BG);
                    btn.setForeground(new Color(195, 205, 245));
                }
            }
        });
        return btn;
    }

    private JPanel buildUserFooter(String nombreCompleto) {
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBackground(SIDEBAR_DARK);
        footer.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        footer.setMaximumSize(new Dimension(210, 100));
        footer.setAlignmentX(LEFT_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(70, 80, 180));
        sep.setMaximumSize(new Dimension(170, 1));
        sep.setAlignmentX(LEFT_ALIGNMENT);
        footer.add(sep);
        footer.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel nameLabel = new JLabel(nombreCompleto);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(LEFT_ALIGNMENT);
        footer.add(nameLabel);

        footer.add(Box.createRigidArea(new Dimension(0, 6)));

        botonCerrarSesion = new JButton("Cerrar sesión");
        botonCerrarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        botonCerrarSesion.setForeground(new Color(180, 185, 230));
        botonCerrarSesion.setBackground(SIDEBAR_DARK);
        botonCerrarSesion.setFocusPainted(false);
        botonCerrarSesion.setBorderPainted(false);
        botonCerrarSesion.setContentAreaFilled(false);
        botonCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonCerrarSesion.setHorizontalAlignment(SwingConstants.LEFT);
        botonCerrarSesion.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        botonCerrarSesion.setAlignmentX(LEFT_ALIGNMENT);
        footer.add(botonCerrarSesion);

        return footer;
    }

    public void addCard(JPanel panel, String cardKey) {
        contentArea.add(panel, cardKey);
    }

    public void switchCard(String cardKey) {
        if (!activeCard.isEmpty()) {
            JButton prev = navButtons.get(activeCard);
            if (prev != null) {
                prev.setBackground(SIDEBAR_BG);
                prev.setForeground(new Color(195, 205, 245));
                prev.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            }
        }
        activeCard = cardKey;
        JButton active = navButtons.get(cardKey);
        if (active != null) {
            active.setBackground(SIDEBAR_ACTIVE);
            active.setForeground(Color.WHITE);
            active.setFont(new Font("Segoe UI", Font.BOLD, 13));
        }
        cardLayout.show(contentArea, cardKey);
    }

    public JButton getNavButton(String key)      { return navButtons.get(key); }
    public JButton getBotonCerrarSesion()        { return botonCerrarSesion; }

    public static JPanel buildPlaceholder(String icon, String title, String subtitle) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CONTENT_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        gbc.gridy = 0;
        JLabel ic = new JLabel(icon, SwingConstants.CENTER);
        ic.setFont(new Font("Segoe UI", Font.PLAIN, 52));
        ic.setForeground(new Color(180, 185, 220));
        panel.add(ic, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(14, 0, 6, 0);
        JLabel tl = new JLabel(title, SwingConstants.CENTER);
        tl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        tl.setForeground(TEXT_DARK);
        panel.add(tl, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel sl = new JLabel(subtitle, SwingConstants.CENTER);
        sl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sl.setForeground(Color.GRAY);
        panel.add(sl, gbc);

        return panel;
    }
}
