package org.vista;

import org.modelo.RachaModel;
import org.modelo.TemaApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RachaNotificacionView extends JDialog {

    public RachaNotificacionView(Frame parent, RachaModel racha) {
        super(parent, "¡Buen trabajo!", true);
        setUndecorated(true);
        setSize(360, 320);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0)); // fondo transparente para rounded corners

        JPanel root = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fill(new RoundRectangle2D.Float(4, 4, getWidth() - 4, getHeight() - 4, 24, 24));
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 4, getHeight() - 4, 24, 24));
                g2.dispose();
            }
        };
        root.setOpaque(false);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(30, 36, 28, 36));

        TemaApp.Tema tema = TemaApp.getTema();

        JLabel emojiLbl = new JLabel(racha.getRachaActual() >= 7 ? "Genial" : "Bien hecho", SwingConstants.CENTER);
        emojiLbl.setFont(new Font("Segoe UI", Font.PLAIN, 52));
        emojiLbl.setAlignmentX(CENTER_ALIGNMENT);
        root.add(emojiLbl);
        root.add(Box.createRigidArea(new Dimension(0, 12)));

        JLabel titulo;
        if (racha.getRachaActual() == 1) {
            titulo = new JLabel("¡Primer día!", SwingConstants.CENTER);
        } else if (racha.getRachaActual() >= 7) {
            titulo = new JLabel("¡" + racha.getRachaActual() + " días de racha!", SwingConstants.CENTER);
        } else {
            titulo = new JLabel("¡" + racha.getRachaActual() + " días seguidos!", SwingConstants.CENTER);
        }
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(40, 40, 80));
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        root.add(titulo);
        root.add(Box.createRigidArea(new Dimension(0, 6)));

        JLabel sub = new JLabel("Iniciaste sesión hoy. ¡Sigue así!", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(Color.GRAY);
        sub.setAlignmentX(CENTER_ALIGNMENT);
        root.add(sub);
        root.add(Box.createRigidArea(new Dimension(0, 22)));

        JPanel monPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        monPanel.setOpaque(false);
        monPanel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel monIcon = new JLabel("");

        JPanel monTextos = new JPanel();
        monTextos.setLayout(new BoxLayout(monTextos, BoxLayout.Y_AXIS));
        monTextos.setOpaque(false);

        JLabel monGanadas = new JLabel("+" + racha.getMonedasGanadas() + " monedas");
        monGanadas.setFont(new Font("Segoe UI", Font.BOLD, 18));
        monGanadas.setForeground(new Color(180, 130, 0));

        JLabel monTotal = new JLabel("Total: " + racha.getMonedasTotal() + " monedas");
        monTotal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        monTotal.setForeground(Color.GRAY);

        monTextos.add(monGanadas);
        monTextos.add(monTotal);
        monPanel.add(monIcon);
        monPanel.add(monTextos);
        root.add(monPanel);
        root.add(Box.createRigidArea(new Dimension(0, 8)));

        if (racha.getRachaActual() == racha.getMejorRacha() && racha.getMejorRacha() > 1) {
            JLabel record = new JLabel("¡Nuevo récord personal!", SwingConstants.CENTER);
            record.setFont(new Font("Segoe UI", Font.BOLD, 12));
            record.setForeground(new Color(200, 140, 0));
            record.setAlignmentX(CENTER_ALIGNMENT);
            root.add(record);
            root.add(Box.createRigidArea(new Dimension(0, 4)));
        }

        root.add(Box.createVerticalGlue());

        JButton btnCerrar = new JButton("¡Entendido!");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setBackground(tema.accent);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setBorder(new EmptyBorder(12, 40, 12, 40));
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setAlignmentX(CENTER_ALIGNMENT);
        btnCerrar.setMaximumSize(new Dimension(220, 46));
        btnCerrar.addActionListener(e -> dispose());
        btnCerrar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnCerrar.setBackground(tema.sidebarHover); }
            public void mouseExited(MouseEvent e)  { btnCerrar.setBackground(tema.accent); }
        });
        root.add(btnCerrar);

        setContentPane(root);
    }
}
