package org.modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class EvaluadorEstres {

    private static double trapmf(double x, double a, double b, double c, double d) {
        if (x <= a || x >= d) return 0.0;
        if (x >= b && x <= c) return 1.0;
        if (x < b) return (x - a) / (b - a);
        return (d - x) / (d - c);
    }

    private static double trimf(double x, double a, double b, double c) {
        return trapmf(x, a, b, b, c);
    }

    private static double mfBajo(double v)   { return trapmf(v, 1.0, 1.0, 2.0, 3.0); }
    private static double mfMedio(double v)  { return trimf (v, 2.0, 3.0, 4.0);       }
    private static double mfAlto(double v)   { return trapmf(v, 3.0, 4.0, 5.0, 5.0); }

    public static double calcularCarga(List<TareaModel> tareas) {
        if (tareas == null || tareas.isEmpty()) return 1.0;

        LocalDate hoy = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        double suma = 0.0;
        for (TareaModel t : tareas) {
            if (t.isCompletada()) continue;

            double prioridad = switch (t.getPrioridad()) {
                case "Alta"  -> 1.00;
                case "Media" -> 0.60;
                default      -> 0.30; // "Baja"
            };

            double dificultad = t.getDificultad() / 5.0; // normaliza 1-5 → [0.2, 1.0]

            double urgencia = 0.5; // default si no hay fecha
            if (t.getFechaLimite() != null && !t.getFechaLimite().isEmpty()) {
                try {
                    long dias = ChronoUnit.DAYS.between(hoy, LocalDate.parse(t.getFechaLimite(), fmt));
                    urgencia = urgenciaDesdeDias(dias);
                } catch (Exception ignored) {}
            }

            suma += prioridad * dificultad * urgencia;
        }

        return 1.0 + 4.0 * (1.0 - Math.exp(-suma * 0.8));
    }

    private static double urgenciaDesdeDias(long dias) {
        if (dias <= 0)  return 1.00; // vencida
        if (dias <= 2)  return 0.95;
        if (dias <= 5)  return 0.80;
        if (dias <= 10) return 0.60;
        if (dias <= 20) return 0.40;
        if (dias <= 30) return 0.20;
        return 0.08;
    }

    private static final boolean[] ES_INVERTIDA = {
            //  1      2      3      4      5      6      7      8      9
            false, false, false, false, false, true,  false, false, false,
            //  10     11     12     13     14     15     16
            false, true,  false, false, true,  false, false,
            //  17     18     19     20     21
            true,  false, true,  true,  false
    };

    public static double invertirSiNecesario(int idx0, int valor) {
        return ES_INVERTIDA[idx0] ? (6 - valor) : valor;
    }

    public static double[] calcularAreas(int[] respuestas) {
        return new double[] {
                promedioArea(respuestas, 0, 4),   // Académica          Q1-5
                promedioArea(respuestas, 5, 8),   // Sueño/Descanso     Q6-9
                promedioArea(respuestas, 9, 12),  // Personal/Emocional Q10-13
                promedioArea(respuestas, 13, 15), // Social             Q14-16
                promedioArea(respuestas, 16, 18), // Equilibrio         Q17-19
                promedioArea(respuestas, 19, 20), // Física             Q20-21
        };
    }

    private static double promedioArea(int[] resp, int desde, int hasta) {
        double suma = 0;
        for (int i = desde; i <= hasta; i++) suma += invertirSiNecesario(i, resp[i]);
        return suma / (hasta - desde + 1);
    }

    public static double promedioGlobal(double[] areas) {
        double s = 0; for (double a : areas) s += a; return s / areas.length;
    }

    public static ResultadoEvaluacion evaluar(List<TareaModel> tareas, int[] respuestas) {
        double carga          = calcularCarga(tareas);
        double[] areas        = calcularAreas(respuestas);
        double estresPerc     = promedioGlobal(areas);
        double nivelFuzzy     = inferencia(carga, estresPerc);
        String situacion      = situacion(carga, estresPerc);
        String label          = ResultadoEvaluacion.etiquetaNivel(nivelFuzzy);
        return new ResultadoEvaluacion(carga, areas, estresPerc, nivelFuzzy, situacion, label);
    }

    /**
     * mamdani con 9 reglas
     * centroides con peso
     * reglas
       cargaAlta  ∧ estresAlto  → riesgo 5.0   (riesgo alto)
       cargaAlta  ∧ estresMedio → riesgo 3.8
       cargaAlta  ∧ estresBajo  → riesgo 2.5   (resiliente)
       cargaMedia ∧ estresAlto  → riesgo 4.0
       cargaMedia ∧ estresMedio → riesgo 3.0
       cargaMedia ∧ estresBajo  → riesgo 2.0
       cargaBaja  ∧ estresAlto  → riesgo 3.5   (prob. emocional)
       cargaBaja  ∧ estresMedio → riesgo 2.0
       cargaBaja  ∧ estresBajo  → riesgo 1.2
     */
    private static double inferencia(double carga, double estres) {
        double cL = mfBajo(carga),  cM = mfMedio(carga),  cH = mfAlto(carga);
        double eL = mfBajo(estres), eM = mfMedio(estres), eH = mfAlto(estres);

        double[][] reglas = {
                { Math.min(cH, eH), 5.0 },
                { Math.min(cH, eM), 3.8 },
                { Math.min(cH, eL), 2.5 },
                { Math.min(cM, eH), 4.0 },
                { Math.min(cM, eM), 3.0 },
                { Math.min(cM, eL), 2.0 },
                { Math.min(cL, eH), 3.5 },
                { Math.min(cL, eM), 2.0 },
                { Math.min(cL, eL), 1.2 },
        };

        double num = 0, den = 0;
        for (double[] r : reglas) { num += r[0] * r[1]; den += r[0]; }

        return den < 1e-9 ? 3.0 : Math.max(1.0, Math.min(5.0, num / den));
    }

    private static String situacion(double carga, double estres) {
        double riesgo     = Math.min(mfAlto(carga),  mfAlto(estres));
        double resiliente = Math.min(mfAlto(carga),  mfBajo(estres));
        double emocional  = Math.min(mfBajo(carga),  mfAlto(estres));
        double estable    = Math.min(mfBajo(carga),  mfBajo(estres));

        double max = Math.max(Math.max(riesgo, resiliente), Math.max(emocional, estable));
        if (max < 0.25) return "Estado moderado";
        if (max == riesgo)     return "Riesgo alto";
        if (max == resiliente) return "Estudiante resiliente";
        if (max == emocional)  return "Posible problema emocional";
        return "Estado estable";
    }
}