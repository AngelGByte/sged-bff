package cl.duocuc.sged.bff.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

public class BffDTO {

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public static class DashboardDocente {
        private Object usuario;
        private List<Object> cursos;
        private List<Object> ultimasNotas;
    }

    @Data
    public static class DashboardEstudiante {
        private Object usuario;
        private List<Object> notas;
        private Object resumenAsistencia;
        private List<Object> anotaciones;
    }

    @Data
    public static class HojaVidaEstudiante {
        private Object datosPersonales;
        private List<Object> notas;
        private List<Object> anotaciones;
        private Object resumenAsistencia;
        private Map<String, Double> promediosPorAsignatura;
    }

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
