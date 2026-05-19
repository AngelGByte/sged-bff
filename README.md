# sged-bff — Backend For Frontend — SGED Colegio Bernardo O'Higgins

Componente **BFF (Backend For Frontend)** del Sistema Integral de Gestión Estudiantil Digital.
Actúa como punto de entrada único para el frontend, agregando y enrutando llamadas a los microservicios.

## Tecnologías
- Java 17 · Spring Boot 3.2.5 · Maven · WAR
- Spring WebFlux (WebClient reactivo para llamar microservicios)
- Spring Security + JWT
- SpringDoc OpenAPI (Swagger UI)
- Lombok

## Puerto
`8080`

## Prerequisitos
- JDK 17+ · Maven 3.8+
- Los 3 microservicios deben estar corriendo:
  - ms-usuarios en `localhost:8081`
  - ms-notas en `localhost:8082`
  - ms-cursos en `localhost:8083`

## Orden de arranque recomendado
```
1. ms-usuarios  (puerto 8081)
2. ms-notas     (puerto 8082)
3. ms-cursos    (puerto 8083)
4. sged-bff     (puerto 8080)  ← este último
```

## Instalación y ejecución

```bash
git clone https://github.com/tu-org/sged-bff.git
cd sged-bff
mvn clean install
mvn spring-boot:run
```

## Swagger UI
```
http://localhost:8080/swagger-ui.html
```

## Endpoints del BFF

| Método | URL | Descripción |
|--------|-----|-------------|
| POST | `/api/bff/auth/login` | Login unificado → JWT |
| GET | `/api/bff/dashboard/docente/{id}` | Dashboard del docente |
| GET | `/api/bff/dashboard/estudiante/{id}` | Dashboard del estudiante |
| GET | `/api/bff/hoja-vida/{estudianteId}` | Hoja de vida consolidada |

## Patrones de diseño aplicados
- **Facade Pattern**: el BFF oculta la complejidad de múltiples microservicios
- **DTO Agregado**: consolida respuestas de varios servicios en un único DTO
- **Proxy**: enruta peticiones del frontend hacia el microservicio correcto

## Pruebas unitarias
```bash
mvn test
```
