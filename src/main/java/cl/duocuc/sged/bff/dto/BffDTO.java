package cl.duocuc.sged.bff.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

// PATRÓN: DTO agregado (Facade Pattern)
// El BFF consolida respuestas de múltiples microservicios en un solo DTO
// para que el frontend haga una sola petición.

public class BffDTO {

    // ── Auth ──────────────────────────────────────────────────────────────
    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    // ── Dashboard del Docente ─────────────────────────────────────────────
    // Agrega: usuario + cursos asignados + últimas notas ingresadas
    @Data
    public static class DashboardDocente {
        private Object usuario;
        private List<Object> cursos;
        private List<Object> ultimasNotas;
    }

    // ── Dashboard del Estudiante ──────────────────────────────────────────
    // Agrega: usuario + notas + resumen asistencia + anotaciones
    @Data
    public static class DashboardEstudiante {
        private Object usuario;
        private List<Object> notas;
        private Object resumenAsistencia;
        private List<Object> anotaciones;
    }

    // ── Hoja de Vida Consolidada ──────────────────────────────────────────
    // Agrega: info del estudiante + notas + anotaciones + asistencia
    @Data
    public static class HojaVidaEstudiante {
        private Object datosPersonales;
        private List<Object> notas;
        private List<Object> anotaciones;
        private Object resumenAsistencia;
        private Map<String, Double> promediosPorAsignatura;
    }

    // ── Respuesta genérica de error ───────────────────────────────────────
    @Data
    public static class ErrorResponse {
        private String mensaje;
        private int codigo;

        public ErrorResponse(String mensaje, int codigo) {
            this.mensaje = mensaje;
            this.codigo = codigo;
        }
    }
}
