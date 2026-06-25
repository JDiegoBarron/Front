package org.modelo;

import java.awt.Color;

public class ResultadoEvaluacion {

    public static final String[] NOMBRES_AREAS = {
            "Académica", "Sueño / Descanso", "Personal / Emocional",
            "Social", "Equilibrio vida-estudio", "Física"
    };

    private final double   cargaAcademica;  // 1-5 (calculada de tareas)
    private final double[] areas;           // 6 scores del cuestionario (1-5)
    private final double   estresPercibido; // promedio global del cuestionario
    private final double   nivelGlobal;    // 1-5 salida difusa
    private final String   situacion;
    private final String   nivelLabel;

    public ResultadoEvaluacion(double carga, double[] areas, double estresPercibido,
                               double nivelGlobal, String situacion, String nivelLabel) {
        this.cargaAcademica  = carga;
        this.areas           = areas;
        this.estresPercibido = estresPercibido;
        this.nivelGlobal     = nivelGlobal;
        this.situacion       = situacion;
        this.nivelLabel      = nivelLabel;
    }

    public double   getCargaAcademica()  { return cargaAcademica; }
    public double[] getAreas()           { return areas; }
    public double   getEstresPercibido() { return estresPercibido; }
    public double   getNivelGlobal()     { return nivelGlobal; }
    public String   getSituacion()       { return situacion; }
    public String   getNivelLabel()      { return nivelLabel; }

    public static Color colorNivel(double nivel) {
        if (nivel <= 2.0) return new Color(46, 125, 50);
        if (nivel <= 3.4) return new Color(230, 119, 0);
        return new Color(183, 28, 28);
    }

    public static String etiquetaNivel(double nivel) {
        if (nivel <= 2.0) return "Bajo";
        if (nivel <= 3.4) return "Medio";
        return "Alto";
    }
}