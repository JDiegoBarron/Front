package org;
import org.controlador.LoginController;
import org.vista.LoginView;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginView vista = new LoginView();
            new LoginController(vista);
        });
    }
}