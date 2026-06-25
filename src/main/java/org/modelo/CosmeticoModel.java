package org.modelo;

public class CosmeticoModel {

    public enum Tipo { TEMA, MARCO }

    private final int    id;
    private final String nombre;
    private final String descripcion;
    private final Tipo   tipo;
    private final int    precio;       // en monedas
    private final int    indiceLocal;
    private       boolean comprado;
    private       boolean activo;

    public CosmeticoModel(int id, String nombre, String descripcion,
                          Tipo tipo, int precio, int indiceLocal,
                          boolean comprado, boolean activo) {
        this.id          = id;
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.tipo        = tipo;
        this.precio      = precio;
        this.indiceLocal = indiceLocal;
        this.comprado    = comprado;
        this.activo      = activo;
    }

    public int     getId()          { return id; }
    public String  getNombre()      { return nombre; }
    public String  getDescripcion() { return descripcion; }
    public Tipo    getTipo()        { return tipo; }
    public int     getPrecio()      { return precio; }
    public int     getIndiceLocal() { return indiceLocal; }
    public boolean isComprado()     { return comprado; }
    public boolean isActivo()       { return activo; }

    public void setComprado(boolean b) { comprado = b; }
    public void setActivo(boolean b)   { activo   = b; }
}
