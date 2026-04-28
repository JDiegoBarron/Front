package org.vista;

import javax.swing.*;
import java.awt.*;

public class PerfilView extends JFrame {

    private JTextField campoCorrecto;
    private JTextField campoCarrera;
    private JSpinner campoSemestre;
    private JButton botonGuardar;
    private JButton botonOmitir;
    private JLabel etiquetaMensaje;

    public PerfilView(String nombreCompleto) {
        setTitle("Completa tu perfil");
        setSize(400, 420);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // no cerrar con la X
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Título
        JLabel titulo = new JLabel("Hola, " + nombreCompleto + " 👋", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(40, 40, 80));
        gbc.gridy = 0; panel.add(titulo, gbc);

        // Subtítulo
        JLabel subtitulo = new JLabel("Cuéntanos un poco sobre ti", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitulo.setForeground(Color.GRAY);
        gbc.gridy = 1; panel.add(subtitulo, gbc);

        // Campo correo
        campoCorrecto = new JTextField("Correo electrónico");
        campoCorrecto.setForeground(Color.GRAY);
        estilizarCampo(campoCorrecto);
        campoCorrecto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (campoCorrecto.getText().equals("Correo electrónico")) {
                    campoCorrecto.setText(""); campoCorrecto.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (campoCorrecto.getText().isEmpty()) {
                    campoCorrecto.setText("Correo electrónico"); campoCorrecto.setForeground(Color.GRAY);
                }
            }
        });
        gbc.gridy = 2; panel.add(campoCorrecto, gbc);

        // Campo carrera
        campoCarrera = new JTextField("Carrera");
        campoCarrera.setForeground(Color.GRAY);
        estilizarCampo(campoCarrera);
        campoCarrera.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (campoCarrera.getText().equals("Carrera")) {
                    campoCarrera.setText(""); campoCarrera.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (campoCarrera.getText().isEmpty()) {
                    campoCarrera.setText("Carrera"); campoCarrera.setForeground(Color.GRAY);
                }
            }
        });
        gbc.gridy = 3; panel.add(campoCarrera, gbc);

        // Spinner semestre
        JLabel labelSemestre = new JLabel("Semestre:");
        labelSemestre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelSemestre.setForeground(new Color(40, 40, 80));
        gbc.gridy = 4; panel.add(labelSemestre, gbc);

        campoSemestre = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        campoSemestre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoSemestre.setPreferredSize(new Dimension(280, 40));
        gbc.gridy = 5; panel.add(campoSemestre, gbc);

        // Mensaje
        etiquetaMensaje = new JLabel("", SwingConstants.CENTER);
        etiquetaMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        etiquetaMensaje.setForeground(new Color(200, 50, 50));
        gbc.gridy = 6; panel.add(etiquetaMensaje, gbc);

        // Botón guardar
        botonGuardar = new JButton("Guardar perfil");
        botonGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonGuardar.setBackground(new Color(80, 90, 200));
        botonGuardar.setForeground(Color.WHITE);
        botonGuardar.setFocusPainted(false);
        botonGuardar.setBorderPainted(false);
        botonGuardar.setPreferredSize(new Dimension(280, 42));
        botonGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 7; panel.add(botonGuardar, gbc);

        // Botón omitir
        botonOmitir = new JButton("Omitir por ahora");
        botonOmitir.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        botonOmitir.setBackground(new Color(245, 247, 250));
        botonOmitir.setForeground(Color.GRAY);
        botonOmitir.setFocusPainted(false);
        botonOmitir.setBorderPainted(false);
        botonOmitir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 8; panel.add(botonOmitir, gbc);

        add(panel);
        setVisible(true);
    }

    private void estilizarCampo(JComponent campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 215)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        campo.setPreferredSize(new Dimension(280, 40));
    }

    public String getCorreo()   { return campoCorrecto.getText(); }
    public String getCarrera()  { return campoCarrera.getText(); }
    public int getSemestre()    { return (int) campoSemestre.getValue(); }
    public JButton getBotonGuardar() { return botonGuardar; }
    public JButton getBotonOmitir()  { return botonOmitir; }
    public void setMensaje(String msg) { etiquetaMensaje.setText(msg); }
    public void setBloqueado(boolean b) { botonGuardar.setEnabled(!b); }
}