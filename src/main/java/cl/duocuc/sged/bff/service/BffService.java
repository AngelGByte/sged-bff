package cl.duocuc.sged.bff.service;

import cl.duocuc.sged.bff.dto.BffDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class BffService {

    @Autowired
    @Qualifier("webClientUsuarios")
    private WebClient webClientUsuarios;

    @Autowired
    @Qualifier("webClientNotas")
    private WebClient webClientNotas;

    @Autowired
    @Qualifier("webClientCursos")
    private WebClient webClientCursos;

    public Object login(BffDTO.LoginRequest request) {
        return webClientUsuarios.post()
                .uri("/api/auth/login")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    public BffDTO.DashboardDocente dashboardDocente(Long docenteId, String token) {
        Object usuario = getUsuario(docenteId, token);
        List<Object> cursos = getCursos(token);

        BffDTO.DashboardDocente dashboard = new BffDTO.DashboardDocente();
        dashboard.setUsuario(usuario);
        dashboard.setCursos(cursos);
        dashboard.setUltimasNotas(List.of()); // se puede ampliar con query por docenteId
        return dashboard;
    }

    public BffDTO.DashboardEstudiante dashboardEstudiante(Long estudianteId, String token) {
        Object usuario = getUsuario(estudianteId, token);
        List<Object> notas = getNotasEstudiante(estudianteId, token);
        Object resumenAsistencia = getResumenAsistencia(estudianteId, token);
        List<Object> anotaciones = getAnotacionesEstudiante(estudianteId, token);

        BffDTO.DashboardEstudiante dashboard = new BffDTO.DashboardEstudiante();
        dashboard.setUsuario(usuario);
        dashboard.setNotas(notas);
        dashboard.setResumenAsistencia(resumenAsistencia);
        dashboard.setAnotaciones(anotaciones);
        return dashboard;
    }

    public BffDTO.HojaVidaEstudiante hojaVida(Long estudianteId, String token) {
        Object datosPersonales = getUsuario(estudianteId, token);
        List<Object> notas = getNotasEstudiante(estudianteId, token);
        List<Object> anotaciones = getAnotacionesEstudiante(estudianteId, token);
        Object resumenAsistencia = getResumenAsistencia(estudianteId, token);

        BffDTO.HojaVidaEstudiante hoja = new BffDTO.HojaVidaEstudiante();
        hoja.setDatosPersonales(datosPersonales);
        hoja.setNotas(notas);
        hoja.setAnotaciones(anotaciones);
        hoja.setResumenAsistencia(resumenAsistencia);
        hoja.setPromediosPorAsignatura(Map.of()); // se puede enriquecer con calls adicionales
        return hoja;
    }

    private Object getUsuario(Long id, String token) {
        return webClientUsuarios.get()
                .uri("/api/usuarios/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorResume(e -> Mono.just(Map.of("error", "Usuario no disponible")))
                .block();
    }

    private List<Object> getCursos(String token) {
        return webClientCursos.get()
                .uri("/api/cursos")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToFlux(Object.class)
                .onErrorResume(e -> Flux.empty())
                .collectList()
                .block();
    }

    private List<Object> getNotasEstudiante(Long estudianteId, String token) {
        return webClientNotas.get()
                .uri("/api/notas/estudiante/{id}", estudianteId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToFlux(Object.class)
                .onErrorResume(e -> Flux.empty())
                .collectList()
                .block();
    }

    private Object getResumenAsistencia(Long estudianteId, String token) {
        return webClientCursos.get()
                .uri("/api/asistencias/resumen/{id}", estudianteId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorResume(e -> Mono.just(Map.of("error", "Asistencia no disponible")))
                .block();
    }

    private List<Object> getAnotacionesEstudiante(Long estudianteId, String token) {
        return webClientNotas.get()
                .uri("/api/anotaciones/estudiante/{id}", estudianteId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToFlux(Object.class)
                .onErrorResume(e -> Flux.empty())
                .collectList()
                .block();
    }
}
