package org.vista;

import org.modelo.EstadoSeccionDto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class CuestionarioView extends JPanel {

    private static final String[][] PREGUNTAS_POR_AREA = {
            { // 0: Académica (Q1-5)
                    "Me siento abrumado por mis tareas y responsabilidades escolares.",
                    "Siento que no tengo suficiente tiempo para mis actividades académicas.",
                    "Me preocupa reprobar alguna materia o evaluación.",
                    "Me cuesta concentrarme cuando estudio.",
                    "Siento que mi carga académica es excesiva."
            },
            { // 1: Sueño y descanso (Q6-9)
                    "Duermo las horas suficientes para sentirme descansado. ↩",
                    "Tengo dificultades para dormir por preocupaciones.",
                    "Me siento cansado durante el día.",
                    "Mi horario de sueño es irregular."
            },
            { // 2: Personal / Emocional (Q10-13)
                    "Me siento estresado o ansioso sin una razón clara.",
                    "Me siento motivado para realizar mis actividades diarias. ↩",
                    "Me siento emocionalmente agotado.",
                    "Tengo pensamientos negativos frecuentes."
            },
            { // 3: Social (Q14-16)
                    "Siento que tengo apoyo de amigos o familia. ↩",
                    "Me siento aislado o solo.",
                    "Tengo conflictos personales que afectan mi estado de ánimo."
            },
            { // 4: Equilibrio vida-estudio (Q17-19)
                    "Tengo tiempo para actividades recreativas o hobbies. ↩",
                    "Siento que el estudio consume todo mi tiempo.",
                    "Puedo desconectarme mentalmente de la escuela cuando descanso. ↩"
            },
            { // 5: Física (Q20-21)
                    "Realizo actividad física regularmente. ↩",
                    "Tengo dolores físicos relacionados con el estrés (cabeza, tensión, etc.)."
            }
    };

    private static final String[] TITULOS_AREAS = {
            "Área académica", "Área de sueño y descanso", "Área personal / emocional",
            "Área social", "Área de equilibrio vida-estudio", "Área física"
    };

    private static final String[] CLAVES_AREAS = {
            "academica", "sueno", "emocional", "social", "equilibrio", "fisica"
    };

    private final JSlider[] sliders = new JSlider[21];
    private final int[]     areaPorSlider = new int[21];
    private final JLabel[]  lblEstadoArea = new JLabel[6];

    private final JButton   btnEnviar;
    private final JLabel    lblMensaje;
    private Runnable        onEnviar;

    private static final Color[] COLORES_AREA = {
            new Color(21,  101, 192), // Académica
            new Color(0,   131, 143), // Sueño
            new Color(106,  27, 154), // Emocional
            new Color(46,  125,  50), // Social
            new Color(230, 119,   0), // Equilibrio
            new Color(183,  28,  28), // Física
    };

    public CuestionarioView() {
        setLayout(new BorderLayout());
        setBackground(BienvenidaView.CONTENT_BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 222, 235)),
                new EmptyBorder(18, 28, 18, 28)));

        JLabel titulo = new JLabel("Cuestionario de bienestar");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(BienvenidaView.TEXT_DARK);
        header.add(titulo, BorderLayout.WEST);

        JLabel subtitulo = new JLabel("Escala: 1 = nunca / muy poco  ·  5 = siempre / mucho");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitulo.setForeground(Color.GRAY);
        header.add(subtitulo, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(BienvenidaView.CONTENT_BG);
        contenido.setBorder(new EmptyBorder(24, 36, 24, 36));

        int idxGlobal = 0;
        for (int a = 0; a < PREGUNTAS_POR_AREA.length; a++) {
            JPanel headerArea = buildSeccionHeader(TITULOS_AREAS[a], COLORES_AREA[a]);
            contenido.add(headerArea);

            JLabel lblEstado = new JLabel(" ");
            lblEstado.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblEstado.setForeground(new Color(120, 120, 120));
            lblEstado.setAlignmentX(LEFT_ALIGNMENT);
            lblEstado.setBorder(new EmptyBorder(4, 4, 4, 0));
            lblEstadoArea[a] = lblEstado;
            contenido.add(lblEstado);

            contenido.add(Box.createRigidArea(new Dimension(0, 10)));

            for (String texto : PREGUNTAS_POR_AREA[a]) {
                areaPorSlider[idxGlobal] = a;
                contenido.add(buildPreguntaRow(idxGlobal, texto));
                contenido.add(Box.createRigidArea(new Dimension(0, 6)));
                idxGlobal++;
            }
            contenido.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMensaje.setForeground(new Color(183, 28, 28));
        lblMensaje.setAlignmentX(CENTER_ALIGNMENT);
        contenido.add(lblMensaje);
        contenido.add(Box.createRigidArea(new Dimension(0, 12)));

        btnEnviar = new JButton("Enviar cuestionario");
        btnEnviar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEnviar.setBackground(BienvenidaView.ACCENT);
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFocusPainted(false);
        btnEnviar.setBorderPainted(false);
        btnEnviar.setBorder(new EmptyBorder(12, 32, 12, 32));
        btnEnviar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEnviar.setAlignmentX(CENTER_ALIGNMENT);
        btnEnviar.addActionListener(e -> { if (onEnviar != null) onEnviar.run(); });
        contenido.add(btnEnviar);
        contenido.add(Box.createRigidArea(new Dimension(0, 30)));

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BienvenidaView.CONTENT_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildSeccionHeader(String titulo, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setBackground(BienvenidaView.CONTENT_BG);
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JLabel lbl = new JLabel("  " + titulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);
        lbl.setBackground(color);
        lbl.setOpaque(true);
        lbl.setBorder(new EmptyBorder(6, 14, 6, 14));
        p.add(lbl);
        return p;
    }

    private JPanel buildPreguntaRow(int idx, String texto) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 227, 240), 1),
                new EmptyBorder(10, 16, 10, 16)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setAlignmentX(LEFT_ALIGNMENT);

        String textoLimpio = texto.replace(" ↩", "");
        boolean invertida  = texto.endsWith("↩");

        JLabel lblTexto = new JLabel("<html><b style='color:#888'>" + (idx + 1) + ".</b> "
                + textoLimpio
                + (invertida ? " <span style='color:#1565C0;font-size:10px'>(invertida)</span>" : "")
                + "</html>");
        lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTexto.setPreferredSize(new Dimension(460, 40));
        row.add(lblTexto, BorderLayout.WEST);

        JSlider slider = new JSlider(1, 5, 3);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setBackground(Color.WHITE);
        slider.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        slider.setPreferredSize(new Dimension(220, 48));
        sliders[idx] = slider;
        row.add(slider, BorderLayout.EAST);

        return row;
    }

    public void aplicarEstadoSecciones(Map<String, EstadoSeccionDto> estadoPorClave) {
        for (int a = 0; a < CLAVES_AREAS.length; a++) {
            EstadoSeccionDto estado = estadoPorClave.get(CLAVES_AREAS[a]);
            if (estado == null) continue;

            boolean disponible = estado.disponible;

            if (disponible) {
                lblEstadoArea[a].setText(" ");
            } else {
                lblEstadoArea[a].setText("Disponible nuevamente: " + formatearFecha(estado.proximaDisponible));
            }

            for (int idxGlobal = 0; idxGlobal < 21; idxGlobal++) {
                if (areaPorSlider[idxGlobal] != a) continue;

                JSlider slider = sliders[idxGlobal];
                slider.setEnabled(disponible);

                if (!disponible) {
                    Integer ultimoValor = estado.ultimosValores.get(idxGlobal + 1);
                    if (ultimoValor != null) slider.setValue(ultimoValor);
                }
            }
        }
    }

    private String formatearFecha(String iso) {
        if (iso == null) return "pronto";
        return iso.substring(0, 10);
    }

    public void setOnEnviar(Runnable cb)     { onEnviar = cb; }
    public void setMensaje(String msg)       { lblMensaje.setForeground(new Color(183, 28, 28)); lblMensaje.setText(msg); }
    public void setMensajeExito(String msg)  { lblMensaje.setForeground(new Color(46, 125, 50)); lblMensaje.setText(msg); }
    public void setBloqueado(boolean b)      { btnEnviar.setEnabled(!b); }

    public int[] getRespuestas() {
        int[] r = new int[21];
        for (int i = 0; i < 21; i++) r[i] = sliders[i].getValue();
        return r;
    }

    public Map<String, java.util.List<int[]>> getRespuestasPorSeccionHabilitada() {
        Map<String, java.util.List<int[]>> resultado = new java.util.LinkedHashMap<>();
        for (int idxGlobal = 0; idxGlobal < 21; idxGlobal++) {
            JSlider slider = sliders[idxGlobal];
            if (!slider.isEnabled()) continue; // sección bloqueada, no se reenvía

            String clave = CLAVES_AREAS[areaPorSlider[idxGlobal]];
            resultado.computeIfAbsent(clave, k -> new java.util.ArrayList<>())
                    .add(new int[]{ idxGlobal + 1, slider.getValue() });
        }
        return resultado;
    }
}