package madstodolist.controller;

import madstodolist.Application;
import madstodolist.model.Usuario;
import madstodolist.repository.UsuarioRepository;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.not;
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testListaUsuarios() throws Exception {
        // Inserta un usuario de prueba en la base de datos
        Usuario usuario = new Usuario("user@ua");
        usuarioRepository.save(usuario); // Asegúrate de que el método save() esté implementado en Us
        // Simula un usuario autenticado
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("idUsuarioLogeado", 1L);

        // Verifica que la lista de usuarios se muestra correctamente
        mockMvc.perform(get("/registrados").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Usuarios Registrados")))
                .andExpect(content().string(containsString("user@ua"))); // Cambia según los datos de prueba
    }

    @Test
    public void testDescripcionUsuario() throws Exception {
        // Inserta un usuario de prueba
        Usuario usuario = new Usuario("user@ua");
        usuario.setNombre("Usuario Prueba");
        usuario.setFechaNacimiento(new SimpleDateFormat("yyyy-MM-dd").parse("2025-06-10"));
        usuario = usuarioRepository.save(usuario);

        // Simula un usuario autenticado
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("idUsuarioLogeado", usuario.getId());
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(usuario.getFechaNacimiento());
        // Realiza la solicitud y verifica los datos
        mockMvc.perform(get("/registrados/" + usuario.getId()).session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Descripción del Usuario")))
                .andExpect(content().string(containsString("user@ua")))
                .andExpect(content().string(containsString("Usuario Prueba")))
                .andExpect(content().string(containsString(formattedDate)))
                .andExpect(content().string(not(containsString("password")))); // Verifica que no se muestra la contraseña
    }
}
