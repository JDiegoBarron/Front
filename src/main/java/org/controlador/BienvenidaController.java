package org.controlador;

import org.modelo.UsuarioModel;
import org.vista.BienvenidaView;
import javax.swing.JOptionPane;

public class BienvenidaController {

    private BienvenidaView vista;
    private UsuarioModel usuario;

    public BienvenidaController(BienvenidaView vista, UsuarioModel usuario) {
        this.vista   = vista;
        this.usuario = usuario;
        asignarAcciones();
    }

    private void asignarAcciones() {
        vista.getBoton(0).addActionListener(e -> abrirCalendario());
        vista.getBoton(1).addActionListener(e -> abrirListaTareas());
        vista.getBoton(2).addActionListener(e -> abrirCrearTarea());
        vista.getBoton(3).addActionListener(e -> abrirCuestionario());
        vista.getBoton(4).addActionListener(e -> abrirPerfil());
    }

    private void abrirCalendario()    { JOptionPane.showMessageDialog(vista, "Calendario");              }
    private void abrirListaTareas()   { JOptionPane.showMessageDialog(vista, "Lista de tareas");         }
    private void abrirCrearTarea()    { JOptionPane.showMessageDialog(vista, "Crear nueva tarea");       }
    private void abrirCuestionario()  { JOptionPane.showMessageDialog(vista, "Cuestionario de bienestar");}
    private void abrirPerfil()        { JOptionPane.showMessageDialog(vista, "Gestión del perfil");      }
}