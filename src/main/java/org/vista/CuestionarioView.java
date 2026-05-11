package org.vista;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class CuestionarioView extends JFrame {

    private Map<Integer, ButtonGroup> grupos = new LinkedHashMap<>();
    private JButton botonEnviar;
    private JLabel etiquetaMensaje;

    // Preguntas agrupadas por área
    private static final String[][] PREGUNTAS = {
            {"Área académica", "1", "Me siento abrumado por mis tareas y responsabilidades escolares."},
            {"Área académica", "2", "Siento que no tengo suficiente tiempo para cumplir con mis actividades académicas."},
            {"Área académica", "3", "Me preocupa reprobar alguna materia o evaluación."},
            {"Área académica", "4", "Me cuesta concentrarme cuando estudio."},
            {"Área académica", "5", "Siento que mi carga académica es excesiva."},
            {"Área de sueño y descanso", "6", "Duermo las horas suficientes para sentirme descansado."},
            {"Área de sueño y descanso", "7", "Tengo dificultades para dormir por preocupaciones."},
            {"Área de sueño y descanso", "8", "Me siento cansado durante el día."},
            {"Área de sueño y descanso", "9", "Mi horario de sueño es irregular."},
            {"Área personal/emocional", "10", "Me siento estresado o ansioso sin una razón clara."},
            {"Área personal/emocional", "11", "Me siento motivado para realizar mis actividades diarias."},
            {"Área personal/emocional", "12", "Me siento emocionalmente agotado."},
            {"Área personal/emocional", "13", "Tengo pensamientos negativos frecuentes."},
            {"Área social", "14", "Siento que tengo apoyo de amigos o familia."},
            {"Área social", "15", "Me siento aislado o solo."},
            {"Área social", "16", "Tengo conflictos personales que afectan mi estado de ánimo."},
            {"Área de equilibrio", "17", "Tengo tiempo para actividades recreativas o hobbies."},
            {"Área de equilibrio", "18", "Siento que el estudio consume todo mi tiempo."},
            {"Área de equilibrio", "19", "Puedo desconectarme mentalmente de la escuela cuando descanso."},
            {"Área física", "20", "Realizo actividad física regularmente."},
            {"Área física", "21", "Tengo dolores físicos relacionados con el estrés (dolor de cabeza, tensión, etc.)."},
    };

    public CuestionarioView() {
        setTitle("Cuestionario de Bienestar");
        setSize(800, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con scroll
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(new Color(245, 247, 250));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JLabel titulo = new JLabel("Cuestionario de Bienestar Estudiantil", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(40, 40, 80));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        panelContenido.add(titulo);

        JLabel subtitulo = new JLabel("Responde con confianza. No hay respuestas correctas o incorrectas.", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitulo.setForeground(Color.GRAY);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        panelContenido.add(subtitulo);

        String areaActual = "";
        JPanel panelArea = null;

        for (String[] pregunta : PREGUNTAS) {
            String area   = pregunta[0];
            int numero    = Integer.parseInt(pregunta[1]);
            String texto  = pregunta[2];

            if (!area.equals(areaActual)) {
                if (panelArea != null) {
                    panelContenido.add(panelArea);
                    panelContenido.add(Box.createVerticalStrut(12));
                }
                panelArea = new JPanel();
                panelArea.setLayout(new BoxLayout(panelArea, BoxLayout.Y_AXIS));
                panelArea.setBackground(Color.WHITE);
                TitledBorder borde = BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 215)),
                        area
                );
                borde.setTitleFont(new Font("Segoe UI", Font.BOLD, 13));
                borde.setTitleColor(new Color(80, 90, 200));
                panelArea.setBorder(BorderFactory.createCompoundBorder(
                        borde,
                        BorderFactory.createEmptyBorder(8, 12, 12, 12)
                ));
                areaActual = area;
            }

            panelArea.add(crearFilaPregunta(numero, texto));
            if (numero < PREGUNTAS.length) {
                panelArea.add(Box.createVerticalStrut(6));
            }
        }
        if (panelArea != null) {
            panelContenido.add(panelArea);
        }

        etiquetaMensaje = new JLabel("", SwingConstants.CENTER);
        etiquetaMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        etiquetaMensaje.setForeground(new Color(200, 50, 50));
        etiquetaMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        etiquetaMensaje.setBorder(BorderFactory.createEmptyBorder(16, 0, 8, 0));
        panelContenido.add(etiquetaMensaje);

        botonEnviar = new JButton("Enviar respuestas");
        botonEnviar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonEnviar.setBackground(new Color(80, 90, 200));
        botonEnviar.setForeground(Color.WHITE);
        botonEnviar.setFocusPainted(false);
        botonEnviar.setBorderPainted(false);
        botonEnviar.setMaximumSize(new Dimension(280, 42));
        botonEnviar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonEnviar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelContenido.add(botonEnviar);
        panelContenido.add(Box.createVerticalStrut(20));

        JScrollPane scroll = new JScrollPane(panelContenido);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);

        add(scroll);
        setVisible(true);
    }

    private JPanel crearFilaPregunta(int numero, String texto) {
        JPanel fila = new JPanel(new BorderLayout(10, 4));
        fila.setBackground(Color.WHITE);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        // Texto de la pregunta
        JLabel labelPregunta = new JLabel("<html><b>" + numero + ".</b> " + texto + "</html>");
        labelPregunta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelPregunta.setForeground(new Color(40, 40, 80));
        fila.add(labelPregunta, BorderLayout.CENTER);

        // Botones de radio 1–5
        JPanel panelOpciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        panelOpciones.setBackground(Color.WHITE);

        ButtonGroup grupo = new ButtonGroup();
        for (int i = 1; i <= 5; i++) {
            JRadioButton radio = new JRadioButton(String.valueOf(i));
            radio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            radio.setBackground(Color.WHITE);
            radio.setForeground(new Color(80, 90, 200));
            radio.setActionCommand(String.valueOf(i));
            grupo.add(radio);
            panelOpciones.add(radio);
        }
        grupos.put(numero, grupo);
        fila.add(panelOpciones, BorderLayout.EAST);

        return fila;
    }

    public Map<Integer, Integer> getRespuestas() {
        Map<Integer, Integer> respuestas = new LinkedHashMap<>();
        for (Map.Entry<Integer, ButtonGroup> entry : grupos.entrySet()) {
            ButtonModel seleccion = entry.getValue().getSelection();
            if (seleccion == null) return null; // pregunta sin responder
            respuestas.put(entry.getKey(), Integer.parseInt(seleccion.getActionCommand()));
        }
        return respuestas;
    }

    public JButton getBotonEnviar()        { return botonEnviar; }
    public void setMensaje(String msg)     { etiquetaMensaje.setText(msg); }
    public void setBloqueado(boolean b)    { botonEnviar.setEnabled(!b); }
}