package org.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CrearTareaView extends JPanel {

    private final JTextField  campoTitulo;
    private final JTextArea   campoDescripcion;
    private final JComboBox<String> comboCategoria;
    private final JComboBox<String> comboPrioridad;
    private final JSlider     sliderDificultad;
    private final JLabel      lblDificultadValor;
    private final JTextField  campoFecha;

    private final JButton btnGuardar;
    private final JButton btnLimpiar;
    private final JLabel  lblMensaje;

    private Runnable onGuardar;

    public CrearTareaView() {
        setLayout(new BorderLayout());
        setBackground(BienvenidaView.CONTENT_BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 222, 235)),
                new EmptyBorder(18, 28, 18, 28)
        ));
        JLabel tituloHeader = new JLabel("Crear nueva tarea");
        tituloHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tituloHeader.setForeground(BienvenidaView.TEXT_DARK);
        header.add(tituloHeader, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BienvenidaView.CONTENT_BG);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 218, 235), 1, true),
                new EmptyBorder(30, 36, 30, 36)
        ));
        form.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        form.add(buildLabel("Título *"));
        campoTitulo = new JTextField();
        campoTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        estilizarCampo(campoTitulo);
        form.add(campoTitulo);
        form.add(Box.createRigidArea(new Dimension(0, 16)));

        form.add(buildLabel("Descripción"));
        campoDescripcion = new JTextArea(4, 20);
        campoDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoDescripcion.setLineWrap(true);
        campoDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(campoDescripcion);
        scrollDesc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 202, 220), 1),
                new EmptyBorder(6, 10, 6, 10)
        ));
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        scrollDesc.setAlignmentX(LEFT_ALIGNMENT);
        form.add(scrollDesc);
        form.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel filaCP = new JPanel(new GridLayout(1, 2, 16, 0));
        filaCP.setBackground(Color.WHITE);
        filaCP.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        filaCP.setAlignmentX(LEFT_ALIGNMENT);

        JPanel panelCat = new JPanel();
        panelCat.setLayout(new BoxLayout(panelCat, BoxLayout.Y_AXIS));
        panelCat.setBackground(Color.WHITE);
        panelCat.add(buildLabel("Categoría"));
        comboCategoria = new JComboBox<>(new String[]{"Curricular", "Extracurricular", "Recreación"});
        estilizarCombo(comboCategoria);
        panelCat.add(comboCategoria);

        JPanel panelPrio = new JPanel();
        panelPrio.setLayout(new BoxLayout(panelPrio, BoxLayout.Y_AXIS));
        panelPrio.setBackground(Color.WHITE);
        panelPrio.add(buildLabel("Prioridad"));
        comboPrioridad = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});
        comboPrioridad.setSelectedItem("Media");
        estilizarCombo(comboPrioridad);
        panelPrio.add(comboPrioridad);

        filaCP.add(panelCat);
        filaCP.add(panelPrio);
        form.add(filaCP);
        form.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel filaDif = new JPanel(new BorderLayout(12, 0));
        filaDif.setBackground(Color.WHITE);
        filaDif.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        filaDif.setAlignmentX(LEFT_ALIGNMENT);

        JPanel panelDif = new JPanel();
        panelDif.setLayout(new BoxLayout(panelDif, BoxLayout.Y_AXIS));
        panelDif.setBackground(Color.WHITE);
        panelDif.add(buildLabel("Dificultad (1 = fácil, 5 = muy difícil)"));

        sliderDificultad = new JSlider(1, 5, 1);
        sliderDificultad.setMajorTickSpacing(1);
        sliderDificultad.setPaintTicks(true);
        sliderDificultad.setSnapToTicks(true);
        sliderDificultad.setBackground(Color.WHITE);
        sliderDificultad.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sliderDificultad.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        lblDificultadValor = new JLabel("●○○○○");
        lblDificultadValor.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblDificultadValor.setForeground(BienvenidaView.ACCENT);

        sliderDificultad.addChangeListener(e -> {
            int v = sliderDificultad.getValue();
            StringBuilder dots = new StringBuilder();
            for (int i = 1; i <= 5; i++) dots.append(i <= v ? "●" : "○");
            lblDificultadValor.setText(dots.toString());
        });

        panelDif.add(sliderDificultad);
        filaDif.add(panelDif, BorderLayout.CENTER);
        filaDif.add(lblDificultadValor, BorderLayout.EAST);
        form.add(filaDif);
        form.add(Box.createRigidArea(new Dimension(0, 16)));

        form.add(buildLabel("Fecha límite *  (YYYY-MM-DD)"));
        campoFecha = new JTextField();
        campoFecha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        estilizarCampo(campoFecha);
        campoFecha.setText("YYYY-MM-DD");
        campoFecha.setForeground(Color.GRAY);
        campoFecha.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (campoFecha.getText().equals("YYYY-MM-DD")) {
                    campoFecha.setText(""); campoFecha.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (campoFecha.getText().isEmpty()) {
                    campoFecha.setText("YYYY-MM-DD"); campoFecha.setForeground(Color.GRAY);
                }
            }
        });
        form.add(campoFecha);
        form.add(Box.createRigidArea(new Dimension(0, 20)));

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMensaje.setForeground(new Color(200, 50, 50));
        lblMensaje.setAlignmentX(CENTER_ALIGNMENT);
        form.add(lblMensaje);
        form.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setAlignmentX(CENTER_ALIGNMENT);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnLimpiar.setBackground(Color.WHITE);
        btnLimpiar.setForeground(new Color(100, 100, 140));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 202, 220), 1, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        btnGuardar = new JButton("Guardar tarea");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setBackground(BienvenidaView.ACCENT);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setBorder(new EmptyBorder(10, 24, 10, 24));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> { if (onGuardar != null) onGuardar.run(); });

        btnPanel.add(btnLimpiar);
        btnPanel.add(btnGuardar);
        form.add(btnPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.6;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(30, 30, 30, 30);
        wrapper.add(form, gbc);

        JScrollPane scrollWrapper = new JScrollPane(wrapper);
        scrollWrapper.setBorder(BorderFactory.createEmptyBorder());
        scrollWrapper.getViewport().setBackground(BienvenidaView.CONTENT_BG);
        add(scrollWrapper, BorderLayout.CENTER);
    }

    public void setOnGuardar(Runnable cb) { onGuardar = cb; }

    public String getTitulo()      { return campoTitulo.getText().trim(); }
    public String getDescripcion() { return campoDescripcion.getText().trim(); }
    public String getCategoria()   { return (String) comboCategoria.getSelectedItem(); }
    public String getPrioridad()   { return (String) comboPrioridad.getSelectedItem(); }
    public int    getDificultad()  { return sliderDificultad.getValue(); }
    public String getFechaLimite() {
        String f = campoFecha.getText().trim();
        return f.equals("YYYY-MM-DD") ? "" : f;
    }

    public void setMensaje(String msg)       { lblMensaje.setText(msg); }
    public void setMensajeExito(String msg)  {
        lblMensaje.setForeground(new Color(27, 94, 32));
        lblMensaje.setText(msg);
    }
    public void setMensajeError(String msg)  {
        lblMensaje.setForeground(new Color(183, 28, 28));
        lblMensaje.setText(msg);
    }
    public void setBloqueado(boolean b)      { btnGuardar.setEnabled(!b); btnLimpiar.setEnabled(!b); }

    public void limpiarFormulario() {
        campoTitulo.setText("");
        campoDescripcion.setText("");
        comboCategoria.setSelectedIndex(0);
        comboPrioridad.setSelectedItem("Media");
        sliderDificultad.setValue(1);
        campoFecha.setText("YYYY-MM-DD");
        campoFecha.setForeground(Color.GRAY);
        lblMensaje.setText("");
        setBloqueado(false);
    }

    private JLabel buildLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(90, 95, 140));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(0, 0, 4, 0));
        return lbl;
    }

    private void estilizarCampo(JComponent campo) {
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 202, 220), 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        if (campo instanceof JComponent)
            ((JComponent) campo).setAlignmentX(LEFT_ALIGNMENT);
    }

    private void estilizarCombo(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(new Color(200, 202, 220), 1));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        combo.setAlignmentX(LEFT_ALIGNMENT);
    }
}
