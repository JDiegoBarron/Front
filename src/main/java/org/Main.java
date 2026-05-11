package org;
import org.controlador.LoginController;
import org.vista.LoginView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginView vista = new LoginView();
            new LoginController(vista);
        });
    }
}