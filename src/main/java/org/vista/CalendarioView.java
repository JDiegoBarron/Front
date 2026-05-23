package org.vista;

import org.modelo.TareaModel;
import org.modelo.TemaApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class CalendarioView extends JPanel {

    private YearMonth  mesActual    = YearMonth.now();
    private LocalDate  diaActivo    = LocalDate.now();

    private Map<LocalDate, List<TareaModel>> tareasPorDia = new HashMap<>();

    private JLabel  lblMesAnio;
    private JPanel  gridDias;
    private JLabel  lblFechaDetalle;
    private JPanel  listaTareasDetalle;
    private JLabel  lblEstado;

    private Consumer<YearMonth> onMesCambiado;   // se llama al navegar mes
    private Consumer<LocalDate> onDiaSeleccionado;

    private static final String[] DIAS_SEMANA = {"Lun","Mar","Mié","Jue","Vie","Sáb","Dom"};

    public CalendarioView() {
        setLayout(new BorderLayout());
        setBackground(BienvenidaView.CONTENT_BG);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCuerpo(), BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        TemaApp.Tema tema = TemaApp.getTema();

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 222, 235)),
                new EmptyBorder(18, 28, 18, 28)));

        JLabel titulo = new JLabel("Calendario");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(BienvenidaView.TEXT_DARK);
        header.add(titulo, BorderLayout.WEST);

        lblEstado = new JLabel("", SwingConstants.RIGHT);
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstado.setForeground(Color.GRAY);
        header.add(lblEstado, BorderLayout.EAST);

        return header;
    }

    private JSplitPane buildCuerpo() {
        JPanel izquierdo = new JPanel(new BorderLayout());
        izquierdo.setBackground(Color.WHITE);
        izquierdo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 222, 235)),
                new EmptyBorder(0, 0, 0, 0)));

        izquierdo.add(buildNavMes(),  BorderLayout.NORTH);
        izquierdo.add(buildGridDias(), BorderLayout.CENTER);
        izquierdo.add(buildLeyenda(), BorderLayout.SOUTH);

        JPanel derecho = new JPanel(new BorderLayout());
        derecho.setBackground(BienvenidaView.CONTENT_BG);

        JPanel detalleHeader = new JPanel(new BorderLayout());
        detalleHeader.setBackground(Color.WHITE);
        detalleHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 222, 235)),
                new EmptyBorder(14, 20, 14, 20)));

        lblFechaDetalle = new JLabel("Selecciona un día");
        lblFechaDetalle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFechaDetalle.setForeground(BienvenidaView.TEXT_DARK);
        detalleHeader.add(lblFechaDetalle, BorderLayout.WEST);
        derecho.add(detalleHeader, BorderLayout.NORTH);

        listaTareasDetalle = new JPanel();
        listaTareasDetalle.setLayout(new BoxLayout(listaTareasDetalle, BoxLayout.Y_AXIS));
        listaTareasDetalle.setBackground(BienvenidaView.CONTENT_BG);
        listaTareasDetalle.setBorder(new EmptyBorder(16, 18, 16, 18));

        JScrollPane scrollDetalle = new JScrollPane(listaTareasDetalle);
        scrollDetalle.setBorder(BorderFactory.createEmptyBorder());
        scrollDetalle.getViewport().setBackground(BienvenidaView.CONTENT_BG);
        scrollDetalle.getVerticalScrollBar().setUnitIncrement(12);
        derecho.add(scrollDetalle, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, izquierdo, derecho);
        split.setDividerLocation(430);
        split.setResizeWeight(0.55);
        split.setBorder(BorderFactory.createEmptyBorder());
        split.setDividerSize(3);
        return split;
    }

    private JPanel buildNavMes() {
        TemaApp.Tema tema = TemaApp.getTema();

        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(Color.WHITE);
        nav.setBorder(new EmptyBorder(14, 20, 10, 20));

        JButton btnPrev = buildNavBtn("‹");
        btnPrev.addActionListener(e -> {
            mesActual = mesActual.minusMonths(1);
            actualizarGrid();
            if (onMesCambiado != null) onMesCambiado.accept(mesActual);
        });

        JButton btnNext = buildNavBtn("›");
        btnNext.addActionListener(e -> {
            mesActual = mesActual.plusMonths(1);
            actualizarGrid();
            if (onMesCambiado != null) onMesCambiado.accept(mesActual);
        });

        lblMesAnio = new JLabel("", SwingConstants.CENTER);
        lblMesAnio.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblMesAnio.setForeground(BienvenidaView.TEXT_DARK);

        nav.add(btnPrev,   BorderLayout.WEST);
        nav.add(lblMesAnio, BorderLayout.CENTER);
        nav.add(btnNext,   BorderLayout.EAST);

        return nav;
    }

    private JButton buildNavBtn(String text) {
        TemaApp.Tema tema = TemaApp.getTema();
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setForeground(tema.accent);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(36, 36));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(tema.sidebarActive); }
            public void mouseExited(MouseEvent e)  { btn.setForeground(tema.accent); }
        });
        return btn;
    }

    private JPanel buildGridDias() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new EmptyBorder(0, 12, 8, 12));

        JPanel cabecera = new JPanel(new GridLayout(1, 7, 4, 0));
        cabecera.setBackground(Color.WHITE);
        cabecera.setBorder(new EmptyBorder(0, 0, 6, 0));
        for (String dia : DIAS_SEMANA) {
            JLabel lbl = new JLabel(dia, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lbl.setForeground(new Color(130, 135, 170));
            cabecera.add(lbl);
        }

        gridDias = new JPanel(new GridLayout(6, 7, 4, 4));
        gridDias.setBackground(Color.WHITE);

        wrapper.add(cabecera, BorderLayout.NORTH);
        wrapper.add(gridDias, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildLeyenda() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 8));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 232, 240)));
        p.add(buildLeyendaItem(new Color(211, 47, 47),  "Vencida"));
        p.add(buildLeyendaItem(new Color(245, 124, 0),  "Pendiente"));
        p.add(buildLeyendaItem(new Color(56, 142, 60),  "Completada"));
        return p;
    }

    private JPanel buildLeyendaItem(Color color, String texto) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setBackground(Color.WHITE);
        JPanel dot = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        dot.setPreferredSize(new Dimension(9, 9));
        dot.setOpaque(false);
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(Color.GRAY);
        p.add(dot);
        p.add(lbl);
        return p;
    }

   public void actualizarGrid() {
        TemaApp.Tema tema = TemaApp.getTema();
        gridDias.removeAll();

        String nombreMes = mesActual.getMonth()
                .getDisplayName(TextStyle.FULL, new Locale("es", "MX"));
        nombreMes = Character.toUpperCase(nombreMes.charAt(0)) + nombreMes.substring(1);
        lblMesAnio.setText(nombreMes + " " + mesActual.getYear());

        LocalDate primerDia = mesActual.atDay(1);
        int offsetInicio = primerDia.getDayOfWeek().getValue() - 1; // 0=Lun … 6=Dom

        int diasEnMes  = mesActual.lengthOfMonth();
        LocalDate hoy  = LocalDate.now();

        for (int i = 0; i < offsetInicio; i++) {
            gridDias.add(new JPanel() {{ setBackground(Color.WHITE); }});
        }

        for (int d = 1; d <= diasEnMes; d++) {
            LocalDate fecha = mesActual.atDay(d);
            List<TareaModel> tareasDia = tareasPorDia.getOrDefault(fecha, Collections.emptyList());
            gridDias.add(buildDiaCelda(fecha, tareasDia, hoy, tema));
        }

        int total   = offsetInicio + diasEnMes;
        int restante= 42 - total;
        for (int i = 0; i < restante; i++) {
            gridDias.add(new JPanel() {{ setBackground(Color.WHITE); }});
        }

        gridDias.revalidate();
        gridDias.repaint();
    }

    private JPanel buildDiaCelda(LocalDate fecha, List<TareaModel> tareas,
                                  LocalDate hoy, TemaApp.Tema tema) {
        boolean esHoy    = fecha.equals(hoy);
        boolean esActivo = fecha.equals(diaActivo);

        JPanel celda = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (esActivo) {
                    g2.setColor(new Color(tema.accent.getRed(), tema.accent.getGreen(),
                            tema.accent.getBlue(), 220));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                } else if (esHoy) {
                    g2.setColor(new Color(tema.accent.getRed(), tema.accent.getGreen(),
                            tema.accent.getBlue(), 25));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setColor(new Color(tema.accent.getRed(), tema.accent.getGreen(),
                            tema.accent.getBlue(), 180));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
                }
                super.paintComponent(g);
                g2.dispose();
            }
        };
        celda.setOpaque(false);
        celda.setLayout(new BorderLayout(0, 1));
        celda.setBorder(new EmptyBorder(3, 4, 3, 4));
        celda.setCursor(new Cursor(Cursor.HAND_CURSOR));
        celda.setPreferredSize(new Dimension(52, 52));

        JLabel numLbl = new JLabel(String.valueOf(fecha.getDayOfMonth()), SwingConstants.CENTER);
        numLbl.setFont(new Font("Segoe UI", esHoy ? Font.BOLD : Font.PLAIN, 13));
        numLbl.setForeground(esActivo ? Color.WHITE
                : esHoy ? tema.accent
                : new Color(50, 55, 90));
        celda.add(numLbl, BorderLayout.CENTER);

        if (!tareas.isEmpty()) {
            JPanel dots = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
            dots.setOpaque(false);
            int maxDots = Math.min(tareas.size(), 3);
            for (int i = 0; i < maxDots; i++) {
                TareaModel t = tareas.get(i);
                Color dotColor = t.isVencida()    ? new Color(211, 47, 47)
                               : t.isCompletada() ? new Color(56, 142, 60)
                               :                   new Color(245, 124, 0);
                if (esActivo) dotColor = new Color(255, 255, 255, 200);

                final Color dc = dotColor;
                JPanel dot = new JPanel() {
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(dc);
                        g2.fillOval(0, 1, 6, 6);
                        g2.dispose();
                    }
                };
                dot.setPreferredSize(new Dimension(6, 8));
                dot.setOpaque(false);
                dots.add(dot);
            }
            celda.add(dots, BorderLayout.SOUTH);
        }

        celda.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!fecha.equals(diaActivo)) {
                    celda.setBackground(new Color(tema.accent.getRed(),
                            tema.accent.getGreen(), tema.accent.getBlue(), 15));
                    celda.setOpaque(true);
                }
            }
            public void mouseExited(MouseEvent e) {
                celda.setOpaque(false);
                celda.repaint();
            }
            public void mouseClicked(MouseEvent e) {
                seleccionarDia(fecha);
                if (onDiaSeleccionado != null) onDiaSeleccionado.accept(fecha);
            }
        });

        return celda;
    }

    private void seleccionarDia(LocalDate fecha) {
        diaActivo = fecha;
        actualizarGrid();

        String nombreDia = fecha.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, new Locale("es", "MX"));
        String nombreMes = fecha.getMonth()
                .getDisplayName(TextStyle.FULL, new Locale("es", "MX"));
        nombreDia = Character.toUpperCase(nombreDia.charAt(0)) + nombreDia.substring(1);
        nombreMes = Character.toUpperCase(nombreMes.charAt(0)) + nombreMes.substring(1);

        lblFechaDetalle.setText(nombreDia + " " + fecha.getDayOfMonth() + " de " + nombreMes);

        mostrarDetalleDia(tareasPorDia.getOrDefault(fecha, Collections.emptyList()));
    }

    private void mostrarDetalleDia(List<TareaModel> tareas) {
        listaTareasDetalle.removeAll();

        if (tareas.isEmpty()) {
            JLabel vacio = new JLabel("Sin tareas este día ✓");
            vacio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            vacio.setForeground(new Color(160, 170, 185));
            vacio.setAlignmentX(LEFT_ALIGNMENT);
            vacio.setBorder(new EmptyBorder(20, 0, 0, 0));
            listaTareasDetalle.add(vacio);
        } else {
            for (TareaModel t : tareas) {
                listaTareasDetalle.add(buildDetalleTareaRow(t));
                listaTareasDetalle.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        listaTareasDetalle.revalidate();
        listaTareasDetalle.repaint();
    }

    private JPanel buildDetalleTareaRow(TareaModel t) {
        boolean vencida    = t.isVencida();
        boolean completada = t.isCompletada();

        Color borderColor = vencida    ? new Color(211, 47, 47, 90)
                          : completada ? new Color(56, 142, 60, 90)
                          :             new Color(210, 215, 235);

        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(completada ? new Color(248, 252, 248) : Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                new EmptyBorder(10, 14, 10, 14)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        row.setAlignmentX(LEFT_ALIGNMENT);

        JPanel prioBar = new JPanel();
        prioBar.setBackground(colorPrioridad(t.getPrioridad()));
        prioBar.setPreferredSize(new Dimension(4, 0));
        row.add(prioBar, BorderLayout.WEST);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(row.getBackground());

        JLabel lTitulo = new JLabel(t.getTitulo());
        lTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lTitulo.setForeground(completada ? new Color(150, 150, 165) : BienvenidaView.TEXT_DARK);
        lTitulo.setAlignmentX(LEFT_ALIGNMENT);

        JPanel chips = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        chips.setOpaque(false);
        chips.setAlignmentX(LEFT_ALIGNMENT);
        chips.add(buildChip(t.getCategoria(), colorCategoria(t.getCategoria())));
        if (vencida)    chips.add(buildChip("Vencida",    new Color(183, 28, 28)));
        if (completada) chips.add(buildChip("Completada", new Color(27, 94, 32)));

        centro.add(lTitulo);
        centro.add(Box.createRigidArea(new Dimension(0, 4)));
        centro.add(chips);
        row.add(centro, BorderLayout.CENTER);

        return row;
    }

    public void setTareas(List<TareaModel> tareas) {
        tareasPorDia.clear();
        for (TareaModel t : tareas) {
            if (t.getFechaLimite() == null || t.getFechaLimite().isEmpty()) continue;
            try {
                LocalDate fecha = LocalDate.parse(t.getFechaLimite()); // YYYY-MM-DD
                tareasPorDia.computeIfAbsent(fecha, k -> new ArrayList<>()).add(t);
            } catch (Exception ignored) {}
        }
        actualizarGrid();
        mostrarDetalleDia(tareasPorDia.getOrDefault(diaActivo, Collections.emptyList()));
    }

    public void setEstado(String texto)                          { lblEstado.setText(texto); }
    public void setOnMesCambiado(Consumer<YearMonth> cb)        { onMesCambiado = cb; }
    public void setOnDiaSeleccionado(Consumer<LocalDate> cb)    { onDiaSeleccionado = cb; }
    public YearMonth getMesActual()                              { return mesActual; }

    private Color colorPrioridad(String p) {
        return switch (p) {
            case "Alta"  -> new Color(211, 47, 47);
            case "Baja"  -> new Color(56, 142, 60);
            default      -> new Color(245, 124, 0);
        };
    }

    private Color colorCategoria(String c) {
        return switch (c) {
            case "Curricular"      -> new Color(21, 101, 192);
            case "Extracurricular" -> new Color(106, 27, 154);
            case "Recreación"      -> new Color(46, 125, 50);
            default                -> Color.GRAY;
        };
    }

    private JLabel buildChip(String texto, Color color) {
        JLabel chip = new JLabel(texto);
        chip.setFont(new Font("Segoe UI", Font.BOLD, 10));
        chip.setForeground(Color.WHITE);
        chip.setBackground(color);
        chip.setOpaque(true);
        chip.setBorder(new EmptyBorder(2, 6, 2, 6));
        return chip;
    }
}
