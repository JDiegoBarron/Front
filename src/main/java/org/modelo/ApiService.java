package org.modelo;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiService {

    private static final String BASE_URL = "https://back-k3t4.onrender.com/api";


    private JSONObject request(String metodo, String ruta, JSONObject body) throws Exception {
        URL url = new URL(BASE_URL + ruta);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(metodo);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        if (body != null) {
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.toString().getBytes("UTF-8"));
            }
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        if (status == 401) throw new Exception("Usuario o contraseña incorrectos");
        if (status == 404) throw new Exception("Recurso no encontrado");
        if (status >= 400) throw new Exception("Error del servidor: " + status);

        return new JSONObject(sb.toString());
    }

    private JSONArray requestArray(String ruta) throws Exception {
        URL url = new URL(BASE_URL + ruta);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8")
        );
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        return new JSONArray(sb.toString());
    }

    // Auth

    public UsuarioModel login(String username, String password) throws Exception {
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("password", password);

        JSONObject res = request("POST", "/auth/login", body);
        return new UsuarioModel(
                res.getInt("id"),
                res.getString("username"),
                res.getString("nombre_completo")
        );
    }

    public UsuarioModel registrar(String username, String password, String nombreCompleto) throws Exception {
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("password", password);
        body.put("nombre_completo", nombreCompleto);

        JSONObject res = request("POST", "/auth/registrar", body);
        return new UsuarioModel(
                res.getInt("id"),
                res.getString("username"),
                res.getString("nombre_completo")
        );
    }

    // Tareas

    public JSONArray obtenerTareas(int usuarioId) throws Exception {
        return requestArray("/tareas/usuario/" + usuarioId);
    }

    public JSONArray obtenerTareasProximas(int usuarioId) throws Exception {
        return requestArray("/tareas/proximas/" + usuarioId);
    }

    public void crearTarea(int usuarioId, String titulo, String descripcion, String fechaLimite) throws Exception {
        JSONObject body = new JSONObject();
        body.put("usuarioId", usuarioId);
        body.put("titulo", titulo);
        body.put("descripcion", descripcion);
        body.put("fecha_limite", fechaLimite);
        request("POST", "/tareas", body);
    }

    public void completarTarea(int tareaId) throws Exception {
        request("PATCH", "/tareas/" + tareaId + "/completar", null);
    }

    public void eliminarTarea(int tareaId) throws Exception {
        URL url = new URL(BASE_URL + "/tareas/" + tareaId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.getResponseCode();
    }

    // Perfil

    public JSONObject obtenerPerfil(int usuarioId) throws Exception {
        return request("GET", "/perfil/" + usuarioId, null);
    }

    public void guardarPerfil(int usuarioId, String correo, String carrera, int semestre) throws Exception {
        JSONObject body = new JSONObject();
        body.put("correo", correo);
        body.put("carrera", carrera);
        body.put("semestre", semestre);
        request("PUT", "/perfil/" + usuarioId, body);
    }
}