package org.controlador;

import org.json.JSONObject;
import org.modelo.*;
import org.vista.PerfilPanelView;

import javax.swing.*;
import java.util.List;

public class PerfilPanelController {

    private final PerfilPanelView vista;
    private final UsuarioModel    usuario;
    private final ApiService      apiService;

    public PerfilPanelController(PerfilPanelView vista, UsuarioModel usuario) {
        this.vista      = vista;
        this.usuario    = usuario;
        this.apiService = new ApiService();

        vista.setNombre(usuario.getNombreCompleto());
        vista.setUsername(usuario.getUsername());
        vista.getAvatarPanel().setIniciales(usuario.getNombreCompleto());

        vista.setOnGuardarPerfil(this::manejarGuardarPerfil);
        vista.setOnComprarCosmetico(this::manejarComprar);
        vista.setOnActivarCosmetico(this::manejarActivar);
        vista.setOnRefresh(this::cargar);
    }

    // ── Carga inicial ─────────────────────────────────────────────────────────

    public void cargar() {
        new Thread(() -> {
            try {
                JSONObject perfil    = apiService.obtenerPerfil(usuario.getId());
                RachaModel  racha    = apiService.obtenerRacha(usuario.getId());
                List<CosmeticoModel> cosmeticos = apiService.obtenerCosmeticos(usuario.getId());

                SwingUtilities.invokeLater(() -> {
                    // Datos de perfil
                    vista.setCorreo(perfil.optString("correo",   ""));
                    vista.setCarrera(perfil.optString("carrera",  ""));
                    vista.setSemestre(perfil.optInt("semestre",   1));

                    // Monedas y racha
                    vista.setMonedas(racha.getMonedasTotal());
                    vista.setRacha(racha.getRachaActual());

                    // Cosméticos
                    vista.mostrarCosmeticos(cosmeticos);

                    // Aplicar cosméticos activos localmente
                    for (CosmeticoModel c : cosmeticos) {
                        if (c.isActivo()) {
                            if (c.getTipo() == CosmeticoModel.Tipo.TEMA) {
                                TemaApp.setTema(c.getIndiceLocal());
                            } else {
                                TemaApp.setMarco(c.getIndiceLocal());
                            }
                        }
                    }
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        vista.setMensajePerfil("Error al cargar: " + ex.getMessage()));
            }
        }).start();
    }

    // ── Guardar perfil ─────────────────────────────────────────────────────────

    private void manejarGuardarPerfil() {
        String correo  = vista.getCorreo();
        String carrera = vista.getCarrera();
        int    semestre= vista.getSemestre();

        if (correo.isEmpty()) {
            vista.setMensajePerfil("El correo no puede estar vacío.");
            return;
        }
        if (!correo.contains("@")) {
            vista.setMensajePerfil("Ingresa un correo válido.");
            return;
        }
        if (carrera.isEmpty()) {
            vista.setMensajePerfil("La carrera no puede estar vacía.");
            return;
        }

        vista.setBloqueadoPerfil(true);
        vista.setMensajePerfil("Guardando...");

        new Thread(() -> {
            try {
                apiService.guardarPerfil(usuario.getId(), correo, carrera, semestre);
                SwingUtilities.invokeLater(() -> {
                    vista.setMensajePerfilExito("¡Perfil actualizado correctamente!");
                    vista.setBloqueadoPerfil(false);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    vista.setMensajePerfil("Error: " + ex.getMessage());
                    vista.setBloqueadoPerfil(false);
                });
            }
        }).start();
    }

    // ── Comprar cosmético ──────────────────────────────────────────────────────

    private void manejarComprar(int cosmeticoId) {
        new Thread(() -> {
            try {
                apiService.comprarCosmetico(usuario.getId(), cosmeticoId);
                // Recargar lista actualizada
                List<CosmeticoModel> cosmeticos = apiService.obtenerCosmeticos(usuario.getId());
                RachaModel racha = apiService.obtenerRacha(usuario.getId());

                SwingUtilities.invokeLater(() -> {
                    vista.setMonedas(racha.getMonedasTotal());
                    vista.mostrarCosmeticos(cosmeticos);
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(null,
                                ex.getMessage(), "Error al comprar",
                                JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    // ── Activar cosmético ──────────────────────────────────────────────────────

    private void manejarActivar(int cosmeticoId) {
        new Thread(() -> {
            try {
                apiService.activarCosmetico(usuario.getId(), cosmeticoId);
                List<CosmeticoModel> cosmeticos = apiService.obtenerCosmeticos(usuario.getId());

                SwingUtilities.invokeLater(() -> {
                    vista.mostrarCosmeticos(cosmeticos);
                    // Aplicar cambio de tema/marco inmediatamente
                    for (CosmeticoModel c : cosmeticos) {
                        if (c.getId() == cosmeticoId && c.isActivo()) {
                            if (c.getTipo() == CosmeticoModel.Tipo.TEMA) {
                                TemaApp.setTema(c.getIndiceLocal());
                            } else {
                                TemaApp.setMarco(c.getIndiceLocal());
                            }
                        }
                    }
                    vista.getAvatarPanel().repaint();
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(null,
                                ex.getMessage(), "Error al activar",
                                JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }
}
