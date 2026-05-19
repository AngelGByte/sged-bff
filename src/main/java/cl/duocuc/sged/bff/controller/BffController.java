package cl.duocuc.sged.bff.controller;

import cl.duocuc.sged.bff.dto.BffDTO;
import cl.duocuc.sged.bff.service.BffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bff")
@CrossOrigin(origins = "*")
public class BffController {

    @Autowired
    private BffService bffService;

    // ── AUTH ──────────────────────────────────────────────────────────────
    @PostMapping("/auth/login")
    @Tag(name = "BFF Auth", description = "Autenticación centralizada")
    @Operation(summary = "Login unificado — delega a ms-usuarios y retorna JWT")
    public ResponseEntity<?> login(@RequestBody BffDTO.LoginRequest request) {
        try {
            return ResponseEntity.ok(bffService.login(request));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new BffDTO.ErrorResponse("Credenciales inválidas", 401));
        }
    }

    // ── DASHBOARD DOCENTE ─────────────────────────────────────────────────
    @GetMapping("/dashboard/docente/{docenteId}")
    @Tag(name = "BFF Dashboard", description = "Vistas agregadas para el frontend")
    @Operation(summary = "Dashboard del docente — agrega usuario + cursos",
               description = "PATRÓN FACADE: una sola llamada del frontend obtiene datos de ms-usuarios y ms-cursos")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> dashboardDocente(
            @PathVariable Long docenteId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            return ResponseEntity.ok(bffService.dashboardDocente(docenteId, token));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new BffDTO.ErrorResponse("Error al obtener dashboard", 500));
        }
    }

    // ── DASHBOARD ESTUDIANTE ──────────────────────────────────────────────
    @GetMapping("/dashboard/estudiante/{estudianteId}")
    @Tag(name = "BFF Dashboard")
    @Operation(summary = "Dashboard del estudiante — agrega notas + asistencia + anotaciones",
               description = "PATRÓN FACADE: una sola llamada del frontend obtiene datos de ms-notas y ms-cursos")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> dashboardEstudiante(
            @PathVariable Long estudianteId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            return ResponseEntity.ok(bffService.dashboardEstudiante(estudianteId, token));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new BffDTO.ErrorResponse("Error al obtener dashboard", 500));
        }
    }

    // ── HOJA DE VIDA ──────────────────────────────────────────────────────
    @GetMapping("/hoja-vida/{estudianteId}")
    @Tag(name = "BFF Vistas Consolidadas")
    @Operation(summary = "Hoja de vida completa del estudiante",
               description = "Consolida datos de ms-usuarios, ms-notas y ms-cursos en una sola respuesta")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> hojaVida(
            @PathVariable Long estudianteId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            return ResponseEntity.ok(bffService.hojaVida(estudianteId, token));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new BffDTO.ErrorResponse("Error al obtener hoja de vida", 500));
        }
    }

    // ── PROXY GENÉRICO: usuarios ──────────────────────────────────────────
    @GetMapping("/usuarios/{id}")
    @Tag(name = "BFF Proxy Usuarios")
    @Operation(summary = "Proxy hacia ms-usuarios: obtener usuario por ID")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getUsuario(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            return ResponseEntity.ok(bffService.dashboardDocente(id, token).getUsuario());
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new BffDTO.ErrorResponse("Usuario no encontrado", 404));
        }
    }
}
