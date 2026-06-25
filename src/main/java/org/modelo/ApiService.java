package org.modelo;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        conn.setRequestProperty("Accept", "application/json");

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

    private TareaModel parseTarea(JSONObject obj) {
        return new TareaModel(
                obj.getInt("id"),
                obj.optString("titulo", ""),
                obj.optString("descripcion", ""),
                obj.optString("fecha_limite", ""),
                obj.optString("categoria", "Curricular"),
                obj.optString("prioridad", "Media"),
                obj.optInt("dificultad", 1),
                obj.optBoolean("completada", false),
                obj.optBoolean("vencida", false)
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

    public List<TareaModel> obtenerTareasMes(int usuarioId, java.time.YearMonth mes) throws Exception {
        String mesStr = mes.getYear() + "-" + String.format("%02d", mes.getMonthValue());
        JSONArray arr = requestArray("/tareas/calendario/" + usuarioId + "?mes=" + mesStr);
        List<TareaModel> lista = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) lista.add(parseTarea(arr.getJSONObject(i)));
        return lista;
    }

    public void crearTarea(int usuarioId, String titulo, String descripcion,
                           String fechaLimite, String categoria,
                           String prioridad, int dificultad) throws Exception {
        JSONObject body = new JSONObject();
        body.put("usuarioId", usuarioId);
        body.put("titulo", titulo);
        body.put("descripcion", descripcion);
        body.put("fecha_limite", fechaLimite);
        body.put("categoria", categoria);
        body.put("prioridad", prioridad);
        body.put("dificultad", dificultad);
        request("POST", "/tareas", body);
    }

    public void editarTarea(int tareaId, String titulo, String descripcion,
                            String fechaLimite, String categoria,
                            String prioridad, int dificultad) throws Exception {
        JSONObject body = new JSONObject();
        body.put("titulo", titulo);
        body.put("descripcion", descripcion);
        body.put("fecha_limite", fechaLimite);
        body.put("categoria", categoria);
        body.put("prioridad", prioridad);
        body.put("dificultad", dificultad);
        requestVoid("PUT", "/tareas/" + tareaId, body);
    }

    public void completarTarea(int tareaId) throws Exception {
        requestVoid("PUT", "/tareas/" + tareaId + "/completar", null);
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
        System.out.println("obtener perfil" + request("GET", "/perfil/" + usuarioId, null));
        return request("GET", "/perfil/" + usuarioId, null);
    }

    public void guardarPerfil(int usuarioId, String correo, String carrera, int semestre) throws Exception {
        JSONObject body = new JSONObject();
        body.put("correo", correo);
        body.put("carrera", carrera);
        body.put("semestre", semestre);
        requestVoid("PUT", "/perfil/" + usuarioId, body);
    }

    public RachaModel registrarLogin(int usuarioId) throws Exception {
        JSONObject res = request("POST", "/racha/registro/" + usuarioId, null);
        System.out.println("Registrar login" + res);
        return parseRacha(res);
    }

    public RachaModel obtenerRacha(int usuarioId) throws Exception {
        JSONObject res = request("GET", "/racha/" + usuarioId, null);
        System.out.println("Obtener racha" + res);
        return parseRacha(res);
    }

    private RachaModel parseRacha(JSONObject res) {
        return new RachaModel(
                res.optInt("rachaActual", 0),
                res.optInt("mejorRacha", 0),
                res.optInt("monedasGanadas", 0),
                res.optInt("monedasTotal", 0),
                res.optBoolean("loginNuevo", false)
        );
    }

    public List<CosmeticoModel> obtenerCosmeticos(int usuarioId) throws Exception {
        JSONArray arr = requestArray("/cosmeticos/usuario/" + usuarioId);
        List<CosmeticoModel> lista = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            lista.add(new CosmeticoModel(
                    obj.getInt("id"),
                    obj.optString("nombre", ""),
                    obj.optString("descripcion", ""),
                    "MARCO".equals(obj.optString("tipo", "TEMA"))
                            ? CosmeticoModel.Tipo.MARCO
                            : CosmeticoModel.Tipo.TEMA,
                    obj.optInt("precio", 0),
                    obj.optInt("indiceLocal", 0),
                    obj.optBoolean("comprado", false),
                    obj.optBoolean("activo", false)
            ));
        }
        return lista;
    }

    public void comprarCosmetico(int usuarioId, int cosmeticoId) throws Exception {
        JSONObject body = new JSONObject();
        body.put("usuarioId",    usuarioId);
        body.put("cosmeticoId",  cosmeticoId);
        requestVoid("POST", "/cosmeticos/comprar", body);
    }

    public void activarCosmetico(int usuarioId, int cosmeticoId) throws Exception {
        JSONObject body = new JSONObject();
        body.put("usuarioId",   usuarioId);
        body.put("cosmeticoId", cosmeticoId);
        requestVoid("PUT", "/cosmeticos/activar", body);
    }

    public Map<String, EstadoSeccionDto> obtenerEstadoCuestionario(int usuarioId) throws Exception {
        JSONArray arr = requestArray("/cuestionario/usuario/" + usuarioId + "/estado");
        Map<String, EstadoSeccionDto> mapa = new java.util.HashMap<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            Map<Integer, Integer> ultimosValores = new java.util.HashMap<>();
            JSONObject valoresObj = obj.optJSONObject("ultimosValores");
            if (valoresObj != null) {
                for (String key : valoresObj.keySet()) {
                    ultimosValores.put(Integer.parseInt(key), valoresObj.getInt(key));
                }
            }
            mapa.put(obj.getString("clave"), new EstadoSeccionDto(
                    obj.getString("clave"),
                    obj.getString("nombre"),
                    obj.getBoolean("disponible"),
                    obj.optString("proximaDisponible", null),
                    ultimosValores
            ));
        }
        return mapa;
    }

    public void guardarCuestionario(int usuarioId, Map<String, List<int[]>> respuestasPorSeccion) throws Exception {
        JSONObject body = new JSONObject();
        body.put("usuarioId", usuarioId);

        JSONObject porSeccion = new JSONObject();
        for (var entry : respuestasPorSeccion.entrySet()) {
            JSONArray respuestasArr = new JSONArray();
            for (int[] par : entry.getValue()) { // par = {numeroPreguntaGlobal, valor}
                JSONObject r = new JSONObject();
                r.put("pregunta", par[0]);
                r.put("valor", par[1]);
                respuestasArr.put(r);
            }
            porSeccion.put(entry.getKey(), respuestasArr);
        }
        body.put("respuestasPorSeccion", porSeccion);

        request("POST", "/cuestionario", body);
    }

    public int[] obtenerUltimasRespuestas(int usuarioId) throws Exception {
        JSONObject res = request("GET", "/cuestionario/usuario/" + usuarioId + "/ultimas-respuestas", null);
        if (res.isNull("respuestas")) return null;

        JSONArray arr = res.getJSONArray("respuestas");
        int[] respuestas = new int[21];
        for (int i = 0; i < 21; i++) respuestas[i] = arr.getInt(i);
        return respuestas;
    }
}
