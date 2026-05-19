package cl.duocuc.sged.bff;

import cl.duocuc.sged.bff.dto.BffDTO;
import cl.duocuc.sged.bff.service.BffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BffDtoTest {

    // Test unitario de los DTOs del BFF (sin levantar contexto Spring)
    @Test
    void loginRequest_debeCrearseCorrecto() {
        BffDTO.LoginRequest request = new BffDTO.LoginRequest();
        request.setEmail("docente@colegio.cl");
        request.setPassword("pass123");

        assertEquals("docente@colegio.cl", request.getEmail());
        assertEquals("pass123", request.getPassword());
    }

    @Test
    void dashboardDocente_debeTenerCampos() {
        BffDTO.DashboardDocente dash = new BffDTO.DashboardDocente();
        dash.setUsuario(null);
        dash.setCursos(java.util.List.of());
        dash.setUltimasNotas(java.util.List.of());

        assertNotNull(dash);
        assertTrue(dash.getCursos().isEmpty());
    }

    @Test
    void dashboardEstudiante_debeTenerCampos() {
        BffDTO.DashboardEstudiante dash = new BffDTO.DashboardEstudiante();
        dash.setNotas(java.util.List.of());
        dash.setAnotaciones(java.util.List.of());

        assertNotNull(dash);
        assertTrue(dash.getNotas().isEmpty());
        assertTrue(dash.getAnotaciones().isEmpty());
    }

    @Test
    void errorResponse_debeMostrarMensaje() {
        BffDTO.ErrorResponse error = new BffDTO.ErrorResponse("No autorizado", 401);

        assertEquals("No autorizado", error.getMensaje());
        assertEquals(401, error.getCodigo());
    }

    @Test
    void hojaVida_debeTenerCampos() {
        BffDTO.HojaVidaEstudiante hoja = new BffDTO.HojaVidaEstudiante();
        hoja.setNotas(java.util.List.of());
        hoja.setAnotaciones(java.util.List.of());
        hoja.setPromediosPorAsignatura(java.util.Map.of());

        assertNotNull(hoja);
        assertTrue(hoja.getNotas().isEmpty());
    }
}
