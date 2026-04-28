package org.vista;

import javax.swing.*;
import java.awt.*;

public class BienvenidaView extends JFrame {

    private JButton[] botones = new JButton[6];

    public BienvenidaView(String nombreCompleto) {
        setTitle("Menú Principal");
        setSize(420, 570);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));

        JPanel panelSaludo = new JPanel();
        panelSaludo.setBackground(new Color(80, 90, 200));
        panelSaludo.setBorder(BorderFactory.createEmptyBorder(24, 20, 24, 20));
        JLabel saludo = new JLabel("Hola, " + nombreCompleto, SwingConstants.CENTER);
        saludo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        saludo.setForeground(Color.WHITE);
        panelSaludo.add(saludo);

        JPanel panelBotones = new JPanel(new GridLayout(6, 1, 0, 12));
        panelBotones.setBackground(new Color(245, 247, 250));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(28, 40, 28, 40));

        String[] etiquetas = {
                "Calendario",
                "Lista de tareas próximas",
                "Crear nueva tarea",
                "Cuestionario de bienestar",
                "Gestión del perfil"
        };

        for (int i = 0; i < etiquetas.length; i++) {
            botones[i] = crearBoton(etiquetas[i]);
            panelBotones.add(botones[i]);
        }

        botones[5] = new JButton("Cerrar sesion");
        botones[5].setFont(new Font("Segoe UI", Font.PLAIN, 12));
        botones[5].setBackground(new Color(245, 247, 250));
        botones[5].setForeground(new Color(80, 90, 200));
        botones[5].setFocusPainted(false);
        botones[5].setBorderPainted(false);
        botones[5].setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelBotones.add(botones[5]);

        panel.add(panelSaludo, BorderLayout.NORTH);
        panel.add(panelBotones, BorderLayout.CENTER);
        add(panel);
        setVisible(true);
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(40, 40, 80));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 215)),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(new Color(235, 237, 255)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(Color.WHITE); }
        });
        return btn;
    }

    // Getter para que el controlador pueda asignar acciones
    public JButton getBoton(int index) { return botones[index]; }
}