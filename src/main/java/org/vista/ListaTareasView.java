package org.vista;

import org.modelo.TareaModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ListaTareasView extends JPanel {

    /** Para actualizar */
    private Runnable onRefresh;
    /** Para completar */
    private java.util.function.Consumer<Integer> onCompletar;
    /** Para eliminar */
    private java.util.function.Consumer<Integer> onEliminar;

    private final JPanel      listaPanel;
    private final JLabel      estadoLabel;
    private final JButton     btnActualizar;

    public ListaTareasView() {
        setLayout(new BorderLayout());
        setBackground(BienvenidaView.CONTENT_BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 222, 235)),
                new EmptyBorder(18, 28, 18, 28)
        ));

        JLabel titulo = new JLabel("Tareas próximas");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(BienvenidaView.TEXT_DARK);
        header.add(titulo, BorderLayout.WEST);

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        headerRight.setBackground(Color.WHITE);

        estadoLabel = new JLabel("");
        estadoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        estadoLabel.setForeground(Color.GRAY);
        headerRight.add(estadoLabel);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnActualizar.setBackground(BienvenidaView.ACCENT);
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setBorderPainted(false);
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnActualizar.setBorder(new EmptyBorder(8, 16, 8, 16));
        btnActualizar.addActionListener(e -> { if (onRefresh != null) onRefresh.run(); });
        headerRight.add(btnActualizar);

        header.add(headerRight, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        listaPanel.setBackground(BienvenidaView.CONTENT_BG);
        listaPanel.setBorder(new EmptyBorder(20, 28, 20, 28));

        JScrollPane scroll = new JScrollPane(listaPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(BienvenidaView.CONTENT_BG);
        scroll.getViewport().setBackground(BienvenidaView.CONTENT_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    public void setOnRefresh(Runnable cb)                              { onRefresh = cb; }
    public void setOnCompletar(java.util.function.Consumer<Integer> cb){ onCompletar = cb; }
    public void setOnEliminar(java.util.function.Consumer<Integer> cb) { onEliminar = cb; }

    public void setEstado(String texto) {
        estadoLabel.setText(texto);
    }

    public void setBloqueado(boolean b) {
        btnActualizar.setEnabled(!b);
    }

    public void mostrarTareas(List<TareaModel> tareas) {
        listaPanel.removeAll();

        if (tareas.isEmpty()) {
            JPanel vacio = buildVacio();
            listaPanel.add(vacio);
        } else {
            for (TareaModel t : tareas) {
                listaPanel.add(buildTareaCard(t));
                listaPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        listaPanel.revalidate();
        listaPanel.repaint();
    }

    private JPanel buildTareaCard(TareaModel tarea) {
        boolean esVencida    = tarea.isVencida();
        boolean esCompletada = tarea.isCompletada();

        Color cardBorder = esVencida    ? new Color(211, 47, 47, 80)
                : esCompletada ? new Color(56, 142, 60, 80)
                :                new Color(210, 212, 230);

        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(esCompletada ? new Color(248, 252, 248) : Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cardBorder, 1, true),
                new EmptyBorder(14, 18, 14, 18)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        card.setAlignmentX(LEFT_ALIGNMENT);

        JPanel prioBar = new JPanel();
        prioBar.setBackground(colorPrioridad(tarea.getPrioridad()));
        prioBar.setPreferredSize(new Dimension(4, 0));
        card.add(prioBar, BorderLayout.WEST);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(card.getBackground());

        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        fila1.setBackground(card.getBackground());
        fila1.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblTitulo = new JLabel(tarea.getTitulo());
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setForeground(esCompletada ? Color.GRAY : BienvenidaView.TEXT_DARK);
        if (esCompletada) {
            lblTitulo.setForeground(new Color(140, 140, 140));
        }
        fila1.add(lblTitulo);

        fila1.add(buildChip(tarea.getCategoria(), colorCategoria(tarea.getCategoria())));
        fila1.add(buildChip(tarea.getPrioridad(),  colorPrioridad(tarea.getPrioridad())));

        if (esVencida)    fila1.add(buildChip("Vencida",    new Color(183, 28, 28)));
        if (esCompletada) fila1.add(buildChip("Completada", new Color(27, 94, 32)));

        centro.add(fila1);
        centro.add(Box.createRigidArea(new Dimension(0, 6)));

        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        fila2.setBackground(card.getBackground());
        fila2.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblFecha = new JLabel(tarea.getFechaLimite());
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFecha.setForeground(esVencida ? new Color(183, 28, 28) : Color.GRAY);
        fila2.add(lblFecha);

        JLabel lblDif = new JLabel("Dif. " + buildDots(tarea.getDificultad()));
        lblDif.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDif.setForeground(Color.GRAY);
        fila2.add(lblDif);

        centro.add(fila2);
        card.add(centro, BorderLayout.CENTER);

        if (!esCompletada) {
            JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
            acciones.setBackground(card.getBackground());

            JButton btnCompletar = buildAccionBtn(null, new Color(56, 142, 60), "Marcar como completada");
            btnCompletar.addActionListener(e -> { if (onCompletar != null) onCompletar.accept(tarea.getId()); });

            JButton btnEliminar = buildAccionBtn(null, new Color(183, 28, 28), "Eliminar tarea");
            btnEliminar.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                        this, "¿Eliminar \"" + tarea.getTitulo() + "\"?",
                        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION && onEliminar != null)
                    onEliminar.accept(tarea.getId());
            });

            acciones.add(btnCompletar);
            acciones.add(btnEliminar);
            card.add(acciones, BorderLayout.EAST);
        }

        return card;
    }

    private JPanel buildVacio() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BienvenidaView.CONTENT_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        gbc.gridy = 0;
        JLabel ic = new JLabel("", SwingConstants.CENTER); // todo
        ic.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        ic.setForeground(new Color(200, 210, 200));
        p.add(ic, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(12, 0, 4, 0);
        JLabel tl = new JLabel("Sin tareas pendientes");
        tl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tl.setForeground(new Color(140, 140, 160));
        p.add(tl, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel sl = new JLabel("¡Todo al día! Puedes crear una tarea nueva desde el menú.");
        sl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sl.setForeground(Color.LIGHT_GRAY);
        p.add(sl, gbc);

        return p;
    }

    private JLabel buildChip(String texto, Color color) {
        JLabel chip = new JLabel(texto);
        chip.setFont(new Font("Segoe UI", Font.BOLD, 10));
        chip.setForeground(Color.WHITE);
        chip.setBackground(color);
        chip.setOpaque(true);
        chip.setBorder(new EmptyBorder(2, 7, 2, 7));
        return chip;
    }

    private JButton buildAccionBtn(String symbol, Color color, String tooltip) {
        JButton btn = new JButton(symbol);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(color);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 1, true),
                new EmptyBorder(4, 10, 4, 10)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(tooltip);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(color); btn.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.WHITE); btn.setForeground(color);
            }
        });
        return btn;
    }

    private String buildDots(int dificultad) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 5; i++)
            sb.append(i <= dificultad ? "\u25A0" : "\u25A1");
        return sb.toString();

    }

    private Color colorCategoria(String categoria) {
        switch (categoria) {
            case "Curricular":     return new Color(21, 101, 192);
            case "Extracurricular":return new Color(106, 27, 154);
            case "Recreación":     return new Color(46, 125, 50);
            default:               return Color.GRAY;
        }
    }

    private Color colorPrioridad(String prioridad) {
        switch (prioridad) {
            case "Alta":  return new Color(211, 47, 47);
            case "Media": return new Color(245, 124, 0);
            case "Baja":  return new Color(56, 142, 60);
            default:      return Color.GRAY;
        }
    }
}
