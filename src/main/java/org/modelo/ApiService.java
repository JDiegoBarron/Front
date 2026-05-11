package org.modelo;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApiService {

    private static final String BASE_URL = "https://back-k3t4.onrender.com/api";

    private JSONObject request(String metodo, String ruta, JSONObject body) throws Exception {
        URL url = new URL(BASE_URL + ruta);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(metodo);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept",       "application/json");

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
        if (status == 409) throw new Exception("El usuario ya existe");
        if (status >= 400) throw new Exception("Error del servidor: " + status);

        return new JSONObject(sb.toString());
    }

    private JSONArray requestArray(String ruta) throws Exception {
        URL url = new URL(BASE_URL + ruta);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        if (status >= 400) throw new Exception("Error del servidor: " + status);
        return new JSONArray(sb.toString());
    }

    private void requestVoid(String metodo, String ruta, JSONObject body) throws Exception {
        URL url = new URL(BASE_URL + ruta);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(metodo);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept",       "application/json");

        if (body != null) {
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.toString().getBytes("UTF-8"));
            }
        }

        int status = conn.getResponseCode();
        if (status == 404) throw new Exception("Recurso no encontrado");
        if (status >= 400) throw new Exception("Error del servidor: " + status);
    }

    private String leerStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();
        return sb.toString();
    }

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
        body.put("username",        username);
        body.put("password",        password);
        body.put("nombre_completo", nombreCompleto);

        JSONObject res = request("POST", "/auth/registrar", body);
        return new UsuarioModel(
                res.getInt("id"),
                res.getString("username"),
                res.getString("nombre_completo")
        );
    }

    private TareaModel parseTarea(JSONObject obj) {
        return new TareaModel(
                obj.getInt("id"),
                obj.optString("titulo",      ""),
                obj.optString("descripcion", ""),
                obj.optString("fecha_limite",""),
                obj.optString("categoria",   "Curricular"),
                obj.optString("prioridad",   "Media"),
                obj.optInt("dificultad",     1),
                obj.optBoolean("completada", false),
                obj.optBoolean("vencida",    false)
        );
    }

    public List<TareaModel> obtenerTareas(int usuarioId) throws Exception {
        JSONArray arr = requestArray("/tareas/usuario/" + usuarioId);
        List<TareaModel> lista = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) lista.add(parseTarea(arr.getJSONObject(i)));
        return lista;
    }

    public List<TareaModel> obtenerTareasProximas(int usuarioId) throws Exception {
        JSONArray arr = requestArray("/tareas/proximas/" + usuarioId);
        List<TareaModel> lista = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) lista.add(parseTarea(arr.getJSONObject(i)));
        return lista;
    }

    public void crearTarea(int usuarioId, String titulo, String descripcion,
                           String fechaLimite, String categoria,
                           String prioridad, int dificultad) throws Exception {
        JSONObject body = new JSONObject();
        body.put("usuarioId",    usuarioId);
        body.put("titulo",       titulo);
        body.put("descripcion",  descripcion);
        body.put("fecha_limite", fechaLimite);
        body.put("categoria",    categoria);
        body.put("prioridad",    prioridad);
        body.put("dificultad",   dificultad);
        request("POST", "/tareas", body);
    }

    public void editarTarea(int tareaId, String titulo, String descripcion,
                            String fechaLimite, String categoria,
                            String prioridad, int dificultad) throws Exception {
        JSONObject body = new JSONObject();
        body.put("titulo",       titulo);
        body.put("descripcion",  descripcion);
        body.put("fecha_limite", fechaLimite);
        body.put("categoria",    categoria);
        body.put("prioridad",    prioridad);
        body.put("dificultad",   dificultad);
        requestVoid("PUT", "/tareas/" + tareaId, body);
    }

    public void completarTarea(int tareaId) throws Exception {
        requestVoid("PATCH", "/tareas/" + tareaId + "/completar", null);
    }

    private void setMethod(HttpURLConnection conn, String metodo) throws Exception {
        if (!metodo.equalsIgnoreCase("PATCH")) {
            conn.setRequestMethod(metodo);
            return;
        }
        try {
            Field field = HttpURLConnection.class.getDeclaredField("method");
            field.setAccessible(true);
            field.set(conn, "PATCH");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
        }
    }


    public void eliminarTarea(int tareaId) throws Exception {
        requestVoid("DELETE", "/tareas/" + tareaId, null);
    }

    public JSONObject obtenerPerfil(int usuarioId) throws Exception {
        return request("GET", "/perfil/" + usuarioId, null);
    }

    public void guardarPerfil(int usuarioId, String correo, String carrera, int semestre) throws Exception {
        JSONObject body = new JSONObject();
        body.put("correo",    correo);
        body.put("carrera",   carrera);
        body.put("semestre",  semestre);
        requestVoid("PUT", "/perfil/" + usuarioId, body);
    }
}
