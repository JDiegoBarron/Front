package org.modelo;

public class UsuarioModel {
    private int id;
    private String username;
    private String nombreCompleto;

    public UsuarioModel(int id, String username, String nombreCompleto) {
        this.id = id;
        this.username = username;
        this.nombreCompleto = nombreCompleto;
    }

    public int getId()               { return id; }
    public String getUsername()      { return username; }
    public String getNombreCompleto(){ return nombreCompleto; }
}