package prueba.com.prueba.controller;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import prueba.com.prueba.model.Usuario;
import prueba.com.prueba.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {
 @Autowired private MockMvc mockMvc;

@MockBean private UsuarioService usuarioService;

@Test 
void listar_deberiaRetornarListaUsuarios() throws Exception {
    // Given
    List<Usuario> usuarios = List.of(new Usuario(), new Usuario(), new Usuario());
    when(usuarioService.getAllUsuarios()).thenReturn(usuarios);


    mockMvc.perform(get("/usuarios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3))); //$ es el objeto raiz y se espera que tenga 3 usuarios een el hasSize


}
@Test
void guardar_deberiaGuardarUsuario() throws Exception {
    // Given
    Usuario usuario = new Usuario();
    usuario.setId(1L);
    usuario.setNombre("Juan");
    usuario.setCorreo("juan@email.com");
    usuario.setPassword("123");
    when(usuarioService.saveUsuario(any(Usuario.class))).thenReturn(usuario);

    mockMvc.perform(post("/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nombre\":\"Juan\",\"email\":\"juan@email.com\",\"password\":\"123\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.nombre").value("Juan"))
            .andExpect(jsonPath("$.email").value("juan@email.com"))
            .andExpect(jsonPath("$.password").value("123"));
        }
/*@Test
void guardar_deberiaGuardarUsuario() throws Exception {
    // Given
    Usuario usuario = new Usuario();
    when(usuarioService.saveUsuario(any(Usuario.class))).thenReturn(usuario);

    mockMvc.perform(post("/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nombre\":\"Juan\",\"email\":\"juan@email.com\"}"))
            .andExpect(status().isCreated());*/

    
    private void andExpect(ResultMatcher value) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'andExpect'");
}
    @Test
    void actualizar_deberiaActualizarUsuario() throws Exception {
    // Given
    Long id = 1L;
    Usuario usuario = new Usuario();
    usuario.setId(id);
    when(usuarioService.updateUsuario(eq(id), any(Usuario.class))).thenReturn(usuario);

    mockMvc.perform(put("/usuarios/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nombre\":\"Juan Actualizado\",\"email\":\"juan@email.com\"}"))
            .andExpect(status().isOk());    
    

}
@Test
void eliminar_deberiaEliminarUsuario() throws Exception {
    // Given
    Long id = 1L;
    doNothing().when(usuarioService).deleteUsuario(id);

    mockMvc.perform(delete("/usuarios/{id}", id))
            .andExpect(status().isOk());

}
    @Test
    void eliminar_deberiaRetornarNotFoundSiUsuarioNoExiste() throws Exception {
        
        Long id = 1L;
        doThrow(new RuntimeException("Usuario no encontrado")).when(usuarioService).deleteUsuario(id);

        mockMvc.perform(delete("/usuarios/{id}", id))
                .andExpect(status().isNotFound());

    }
    @Test
    void obtenerPorId_deberiaRetornarUsuarioPorId() throws Exception {
        
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        when(usuarioService.getUsuarioById(id)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

    }




}