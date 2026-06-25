package org.modelo;

import java.util.Map;

public class EstadoSeccionDto {
    public final String clave;
    public final String nombre;
    public final boolean disponible;
    public final String proximaDisponible; // ISO string o null
    public final Map<Integer, Integer> ultimosValores; // numeroPreguntaGlobal -> valor

    public EstadoSeccionDto(String clave, String nombre, boolean disponible,
                            String proximaDisponible, Map<Integer, Integer> ultimosValores) {
        this.clave = clave;
        this.nombre = nombre;
        this.disponible = disponible;
        this.proximaDisponible = proximaDisponible;
        this.ultimosValores = ultimosValores;
    }
}