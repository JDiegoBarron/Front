package org.modelo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject; // necesitas la librería org.json

public class ApiService {

    private static final String BASE_URL = "https://tu-api.com"; // cambia estojejeje

    public UsuarioModel login(String username, String password) throws Exception {
        URL url = new URL(BASE_URL + "/auth/login");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Construir cuerpo JSON
        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes("UTF-8"));
        }

        int status = conn.getResponseCode();
        if (status == 200) {
            // Leer respuesta
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8")
            );
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();

            // Parsear JSON
            JSONObject json = new JSONObject(sb.toString());
            return new UsuarioModel(
                    json.getString("username"),
                    json.getString("token"),
                    json.getString("nombreCompleto")
            );
        } else if (status == 401) {
            throw new Exception("Credenciales incorrectas");
        } else {
            throw new Exception("Error del servidor: " + status);
        }
    }
}