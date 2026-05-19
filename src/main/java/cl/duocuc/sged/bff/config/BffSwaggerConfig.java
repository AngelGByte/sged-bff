package cl.duocuc.sged.bff.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BffSwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SGED BFF API - Colegio Bernardo O'Higgins")
                .description("""
                    Backend For Frontend del Sistema Integral de Gestión Estudiantil Digital.
                    Este BFF agrega y enruta llamadas hacia los microservicios:
                    - ms-usuarios (puerto 8081)
                    - ms-notas   (puerto 8082)
                    - ms-cursos  (puerto 8083)
                    """)
                .version("1.0.0"))
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new Components()
                .addSecuritySchemes("Bearer Authentication",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}
