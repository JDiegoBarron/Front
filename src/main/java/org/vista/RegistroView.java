package org.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegistroView extends JFrame {

    private JTextField campoUsername;
    private JTextField campoNombre;
    private JPasswordField campoPassword;
    private JButton botonRegistrar;
    private JButton botonVolver;
    private JLabel etiquetaMensaje;

    public RegistroView() {
        setTitle("Crear cuenta");
        setSize(380, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel titulo = new JLabel("Crear cuenta", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(40, 40, 80));
        gbc.gridy = 0; panel.add(titulo, gbc);

        campoNombre = new JTextField("Nombre completo");
        campoNombre.setForeground(Color.GRAY);
        estilizarCampo(campoNombre);
        campoNombre.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (campoNombre.getText().equals("Nombre completo")) {
                    campoNombre.setText(""); campoNombre.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (campoNombre.getText().isEmpty()) {
                    campoNombre.setText("Nombre completo"); campoNombre.setForeground(Color.GRAY);
                }
            }
        });
        gbc.gridy = 1; panel.add(campoNombre, gbc);

        campoUsername = new JTextField("Usuario");
        campoUsername.setForeground(Color.GRAY);
        estilizarCampo(campoUsername);
        campoUsername.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (campoUsername.getText().equals("Usuario")) {
                    campoUsername.setText(""); campoUsername.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (campoUsername.getText().isEmpty()) {
                    campoUsername.setText("Usuario"); campoUsername.setForeground(Color.GRAY);
                }
            }
        });
        gbc.gridy = 2; panel.add(campoUsername, gbc);

        campoPassword = new JPasswordField();
        campoPassword.setEchoChar((char) 0);
        campoPassword.setText("Contraseña");
        campoPassword.setForeground(Color.GRAY);
        estilizarCampo(campoPassword);
        campoPassword.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(campoPassword.getPassword()).equals("Contraseña")) {
                    campoPassword.setText(""); campoPassword.setForeground(Color.BLACK);
                    campoPassword.setEchoChar('•');
                }
            }
            public void focusLost(FocusEvent e) {
                if (campoPassword.getPassword().length == 0) {
                    campoPassword.setEchoChar((char) 0);
                    campoPassword.setText("Contraseña"); campoPassword.setForeground(Color.GRAY);
                }
            }
        });
        gbc.gridy = 3; panel.add(campoPassword, gbc);

        etiquetaMensaje = new JLabel("", SwingConstants.CENTER);
        etiquetaMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        etiquetaMensaje.setForeground(new Color(200, 50, 50));
        gbc.gridy = 4; panel.add(etiquetaMensaje, gbc);

        botonRegistrar = new JButton("Crear cuenta");
        botonRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonRegistrar.setBackground(new Color(80, 90, 200));
        botonRegistrar.setForeground(Color.WHITE);
        botonRegistrar.setFocusPainted(false);
        botonRegistrar.setBorderPainted(false);
        botonRegistrar.setPreferredSize(new Dimension(280, 42));
        botonRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 5; panel.add(botonRegistrar, gbc);

        botonVolver = new JButton("← Volver al inicio de sesión");
        botonVolver.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        botonVolver.setBackground(new Color(245, 247, 250));
        botonVolver.setForeground(new Color(80, 90, 200));
        botonVolver.setFocusPainted(false);
        botonVolver.setBorderPainted(false);
        botonVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 6; panel.add(botonVolver, gbc);

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

    public String getNombre()   { return campoNombre.getText(); }
    public String getUsername() { return campoUsername.getText(); }
    public String getPassword() { return String.valueOf(campoPassword.getPassword()); }
    public JButton getBotonRegistrar() { return botonRegistrar; }
    public JButton getBotonVolver()    { return botonVolver; }
    public void setMensaje(String msg) { etiquetaMensaje.setText(msg); }
    public void setMensajeExito(String msg) {
        etiquetaMensaje.setForeground(new Color(40, 160, 80));
        etiquetaMensaje.setText(msg);
    }
    public void setBloqueado(boolean b) { botonRegistrar.setEnabled(!b); }
}