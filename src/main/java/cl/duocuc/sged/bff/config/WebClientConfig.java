package cl.duocuc.sged.bff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

// PATRÓN: Facade
// El BFF actúa como fachada única ante el frontend, ocultando la complejidad
// de múltiples microservicios y agregando sus respuestas.
@Configuration
public class WebClientConfig {

    @Value("${ms.usuarios.url}")
    private String msUsuariosUrl;

    @Value("${ms.notas.url}")
    private String msNotasUrl;

    @Value("${ms.cursos.url}")
    private String msCursosUrl;

    @Bean("webClientUsuarios")
    public WebClient webClientUsuarios() {
        return WebClient.builder()
                .baseUrl(msUsuariosUrl)
                .build();
    }

    @Bean("webClientNotas")
    public WebClient webClientNotas() {
        return WebClient.builder()
                .baseUrl(msNotasUrl)
                .build();
    }

    @Bean("webClientCursos")
    public WebClient webClientCursos() {
        return WebClient.builder()
                .baseUrl(msCursosUrl)
                .build();
    }
}
