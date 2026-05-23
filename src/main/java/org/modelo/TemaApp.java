package org.modelo;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TemaApp {

    public static class Tema {
        public final String nombre;
        public final String descripcion;
        public final Color  sidebarBg;
        public final Color  sidebarDark;
        public final Color  sidebarHover;
        public final Color  sidebarActive;
        public final Color  contentBg;
        public final Color  accent;
        public final Color  textDark;
        public final int    cosmeticoId; // 0 = gratuito / predeterminado

        public Tema(String nombre, String descripcion, Color sidebarBg,
                    Color sidebarDark, Color sidebarHover, Color sidebarActive,
                    Color contentBg, Color accent, Color textDark, int cosmeticoId) {
            this.nombre       = nombre;
            this.descripcion  = descripcion;
            this.sidebarBg    = sidebarBg;
            this.sidebarDark  = sidebarDark;
            this.sidebarHover = sidebarHover;
            this.sidebarActive= sidebarActive;
            this.contentBg    = contentBg;
            this.accent       = accent;
            this.textDark     = textDark;
            this.cosmeticoId  = cosmeticoId;
        }
    }

    public static final Tema[] TEMAS = {
        new Tema("Clásico Azul", "El tema predeterminado de Mi Agenda",
            new Color(32,40,120),  new Color(24,30,95),   new Color(55,65,170),
            new Color(80,90,200),  new Color(245,247,250),new Color(80,90,200),
            new Color(40,40,80),   0),

        new Tema("Verde Bosque", "Tranquilidad natural",
            new Color(27,94,32),   new Color(20,74,25),   new Color(46,125,50),
            new Color(76,175,80),  new Color(241,248,233),new Color(67,160,71),
            new Color(27,50,27),   1),

        new Tema("Rojo Carmesí", "Energía y determinación",
            new Color(127,29,29),  new Color(107,21,21),  new Color(153,27,27),
            new Color(220,38,38),  new Color(255,245,245),new Color(220,38,38),
            new Color(69,10,10),   2),

        new Tema("Morado Real", "Creatividad y enfoque",
            new Color(74,20,140),  new Color(56,14,112),  new Color(106,27,154),
            new Color(142,36,170), new Color(249,240,255),new Color(142,36,170),
            new Color(40,10,70),   3),

        new Tema("Teal Océano", "Calma y concentración",
            new Color(0,77,64),    new Color(0,60,50),    new Color(0,105,92),
            new Color(0,137,123),  new Color(224,242,241),new Color(0,137,123),
            new Color(0,40,35),    4),

        new Tema("Naranja Otoño", "Calidez y motivación",
            new Color(120,53,15),  new Color(92,40,10),   new Color(154,52,18),
            new Color(234,88,12),  new Color(255,247,237),new Color(234,88,12),
            new Color(67,20,7),    5),
    };

    public static final String[] NOMBRES_MARCOS = {
        "Sin marco",        // id marco 0
        "Dorado Clásico",   // id marco 1
        "Neón Azul",        // id marco 2
        "Rosa Degradado",   // id marco 3
        "Arcoíris",         // id marco 4
        "Plateado Élite",   // id marco 5
    };

    private static int   temaIdx  = 0;
    private static int   marcoIdx = 0;
    private static final List<Runnable> listeners = new ArrayList<>();

    private static final String CONFIG_FILE = System.getProperty("user.home") + "/.agenda_upiiz.properties";

    public static void cargar() {
        Properties p = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            p.load(fis);
            temaIdx  = Integer.parseInt(p.getProperty("temaIdx",  "0"));
            marcoIdx = Integer.parseInt(p.getProperty("marcoIdx", "0"));

            if (temaIdx  < 0 || temaIdx  >= TEMAS.length)       temaIdx  = 0;
            if (marcoIdx < 0 || marcoIdx >= NOMBRES_MARCOS.length) marcoIdx = 0;
        } catch (Exception ignored) {

        }
    }

    private static void guardar() {
        Properties p = new Properties();
        p.setProperty("temaIdx",  String.valueOf(temaIdx));
        p.setProperty("marcoIdx", String.valueOf(marcoIdx));
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            p.store(fos, "Mi Agenda UPIIZ - Configuración");
        } catch (Exception ignored) {}
    }

    public static Tema  getTema()           { return TEMAS[temaIdx]; }
    public static int   getTemaIdx()        { return temaIdx; }
    public static int   getMarcoIdx()       { return marcoIdx; }

    public static void setTema(int idx) {
        if (idx < 0 || idx >= TEMAS.length) return;
        temaIdx = idx;
        guardar();
        notificar();
    }

    public static void setMarco(int idx) {
        if (idx < 0 || idx >= NOMBRES_MARCOS.length) return;
        marcoIdx = idx;
        guardar();
        notificar();
    }

    public static void addListener(Runnable l)    { listeners.add(l); }
    public static void removeListener(Runnable l) { listeners.remove(l); }
    private static void notificar()               { listeners.forEach(Runnable::run); }

    /**
     * @param g2     Graphics2D del componente
     * @param cx     Centro X
     * @param cy     Centro Y
     * @param radio  Radio del avatar
     */
    public static void dibujarMarco(Graphics2D g2, int cx, int cy, int radio) {
        int marcoGrosor = 4;
        int r = radio + marcoGrosor;

        g2.setStroke(new BasicStroke(marcoGrosor, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        switch (marcoIdx) {
            case 0 -> { /* sin marco */ }
            case 1 -> { // Dorado Clásico
                g2.setColor(new Color(212, 175, 55));
                g2.drawOval(cx - r, cy - r, r * 2, r * 2);
            }
            case 2 -> { // Neón Azul
                g2.setColor(new Color(0, 200, 255));
                g2.setStroke(new BasicStroke(marcoGrosor + 1));
                g2.drawOval(cx - r, cy - r, r * 2, r * 2);
            }
            case 3 -> { // Rosa Degradado (aproximado con 2 arcos)
                g2.setColor(new Color(255, 100, 180));
                g2.drawOval(cx - r, cy - r, r * 2, r * 2);
                g2.setColor(new Color(255, 180, 230, 120));
                g2.setStroke(new BasicStroke(marcoGrosor - 1));
                g2.drawOval(cx - r - 2, cy - r - 2, (r + 2) * 2, (r + 2) * 2);
            }
            case 4 -> { // Arcoíris (6 segmentos)
                Color[] arco = {
                    new Color(255,0,0), new Color(255,165,0),
                    new Color(255,255,0), new Color(0,200,0),
                    new Color(0,0,255), new Color(148,0,211)
                };
                g2.setStroke(new BasicStroke(marcoGrosor + 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                for (int i = 0; i < 6; i++) {
                    g2.setColor(arco[i]);
                    g2.drawArc(cx - r, cy - r, r * 2, r * 2, i * 60, 60);
                }
            }
            case 5 -> { // Plateado Élite (doble borde)
                g2.setColor(new Color(192, 192, 192));
                g2.setStroke(new BasicStroke(marcoGrosor + 2));
                g2.drawOval(cx - r - 2, cy - r - 2, (r + 2) * 2, (r + 2) * 2);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1));
                g2.drawOval(cx - r + 1, cy - r + 1, (r - 1) * 2, (r - 1) * 2);
            }
        }
    }
}
