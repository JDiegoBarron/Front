package org.modelo;

public class TareaModel {

    private int    id;
    private String titulo;
    private String descripcion;
    private String fechaLimite;   // "YYYY-MM-DD"
    private String categoria;
    private String prioridad;
    private int    dificultad;    // 1-5
    private boolean completada;
    private boolean vencida;

    public TareaModel(int id, String titulo, String descripcion, String fechaLimite,
                      String categoria, String prioridad, int dificultad,
                      boolean completada, boolean vencida) {
        this.id          = id;
        this.titulo      = titulo;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.categoria   = categoria;
        this.prioridad   = prioridad;
        this.dificultad  = dificultad;
        this.completada  = completada;
        this.vencida     = vencida;
    }

    public int     getId()          { return id; }
    public String  getTitulo()      { return titulo; }
    public String  getDescripcion() { return descripcion; }
    public String  getFechaLimite() { return fechaLimite; }
    public String  getCategoria()   { return categoria; }
    public String  getPrioridad()   { return prioridad; }
    public int     getDificultad()  { return dificultad; }
    public boolean isCompletada()   { return completada; }
    public boolean isVencida()      { return vencida; }
}
