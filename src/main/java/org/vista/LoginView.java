package org.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginView extends JFrame {

    private JTextField campoUsuario;
    private JPasswordField campoPassword;
    private JButton botonEntrar;
    private JButton botonRegistrar;
    private JLabel etiquetaMensaje;

    public LoginView() {
        setTitle("Iniciar Sesión");
        setSize(380, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel titulo = new JLabel("Bienvenido", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(40, 40, 80));
        gbc.gridy = 0; panel.add(titulo, gbc);

        campoUsuario = new JTextField("Usuario");
        campoUsuario.setForeground(Color.GRAY);
        estilizarCampo(campoUsuario);
        campoUsuario.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (campoUsuario.getText().equals("Usuario")) {
                    campoUsuario.setText(""); campoUsuario.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (campoUsuario.getText().isEmpty()) {
                    campoUsuario.setText("Usuario"); campoUsuario.setForeground(Color.GRAY);
                }
            }
        });
        gbc.gridy = 1; panel.add(campoUsuario, gbc);

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
        gbc.gridy = 2; panel.add(campoPassword, gbc);

        etiquetaMensaje = new JLabel("", SwingConstants.CENTER);
        etiquetaMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        etiquetaMensaje.setForeground(new Color(200, 50, 50));
        gbc.gridy = 3; panel.add(etiquetaMensaje, gbc);

        botonEntrar = new JButton("Entrar");
        botonEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonEntrar.setBackground(new Color(80, 90, 200));
        botonEntrar.setForeground(Color.WHITE);
        botonEntrar.setFocusPainted(false);
        botonEntrar.setBorderPainted(false);
        botonEntrar.setPreferredSize(new Dimension(280, 42));
        botonEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 4; panel.add(botonEntrar, gbc);

        // Botón registrar
        botonRegistrar = new JButton("¿No tienes cuenta? Registrate");
        botonRegistrar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        botonRegistrar.setBackground(new Color(245, 247, 250));
        botonRegistrar.setForeground(new Color(80, 90, 200));
        botonRegistrar.setFocusPainted(false);
        botonRegistrar.setBorderPainted(false);
        botonRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 5;
        panel.add(botonRegistrar, gbc);

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

    // Getters para el controlador
    public String getUsuario() { return campoUsuario.getText(); }
    public String getPassword() { return String.valueOf(campoPassword.getPassword()); }
    public JButton getBotonEntrar() { return botonEntrar; }
    public void setMensaje(String msg) { etiquetaMensaje.setText(msg); }
    public void setBloqueado(boolean b) { botonEntrar.setEnabled(!b); }
    public JButton getBotonRegistrar() { return botonRegistrar; }
}