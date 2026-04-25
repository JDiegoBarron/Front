package org.modelo;

public class UsuarioModel {
    private String username;
    private String token;
    private String nombreCompleto;

    public UsuarioModel(String username, String token, String nombreCompleto) {
        this.username = username;
        this.token = token;
        this.nombreCompleto = nombreCompleto;
    }

    public String getUsername()      { return username; }
    public String getToken()         { return token; }
    public String getNombreCompleto(){ return nombreCompleto; }
}