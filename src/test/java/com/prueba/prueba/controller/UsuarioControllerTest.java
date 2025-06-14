package com.prueba.prueba.controller;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.prueba.prueba.dto.UsuarioDTOConverter;
import com.prueba.prueba.model.Usuario;
import com.prueba.prueba.service.UsuarioService;
import com.prueba.prueba.service.VentaClientService;


@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

@MockBean private VentaClientService ventaClientService;
@Autowired private MockMvc mockMvc;
@MockBean private UsuarioDTOConverter usuarioDTOConverter;
@MockBean private UsuarioService usuarioService;

@Test 
void listar_deberiaRetornarListaUsuarios() throws Exception {
    // Given
    List<Usuario> usuarios = List.of(new Usuario(), new Usuario(), new Usuario());
    when(usuarioService.getAllUsuarios()).thenReturn(usuarios);


    mockMvc.perform(get("/api/v1/usuarios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3))); //$ es el objeto raiz y se espera que tenga 3 usuarios een el hasSize

}

@Test
void guardar_deberiaGuardarUsuario() throws Exception {
    // Given
    Usuario usuario = new Usuario();
    usuario.setId(1L);
    usuario.setCorreo("juan@email.com");
    usuario.setPassword("123");
    when(usuarioService.saveUsuario(any(Usuario.class))).thenReturn(usuario);

    mockMvc.perform(post("/api/v1/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":\"1\",\"correo\":\"juan@email.com\",\"password\":\"123\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.correo").value("juan@email.com"))
            .andExpect(jsonPath("$.password").value("123"));
    }


    
@Test
void actualizar_deberiaActualizarUsuario() throws Exception {
        // Given
        Long id = 1L;
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setCorreo("viejo@email.com");
        usuarioExistente.setPassword("antigua");
    
        // Usuario actualizado (lo que retorna el servicio)
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(id);
        usuarioActualizado.setCorreo("nuevo@email.com");
        usuarioActualizado.setPassword("nueva");
    
        when(usuarioService.updateUsuario(eq(id), any(Usuario.class))).thenReturn(usuarioActualizado);
        when(usuarioService.getUsuarioById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioService.saveUsuario(any(Usuario.class))).thenReturn(usuarioActualizado);
        mockMvc.perform(put("/api/v1/usuarios/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"correo\":\"nuevo@email.com\",\"password\":\"nueva\"}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.correo").value("nuevo@email.com"))
                .andExpect(jsonPath("$.password").value("nueva"));
    }
    
    

@Test
void eliminar_deberiaEliminarUsuario() throws Exception {
    // Given
    Long id = 1L;

    mockMvc.perform(delete("/api/v1/usuarios/{id}", id))
            .andExpect((status().isNotFound()));

    }

@Test
void eliminar_deberiaRetornarNotFoundSiUsuarioNoExiste() throws Exception {
        
    Long id = 1L;
    doThrow(new RuntimeException("Usuario no encontrado")).when(usuarioService).deleteUsuario(id);
    mockMvc.perform(delete("/api/v1/usuarios/{id}", id))
    .andExpect(status().isNotFound());
    }
    
@Test
 void obtenerPorId_deberiaRetornarUsuarioPorId() throws Exception {
        
    Long id = 1L;
    Usuario usuario = new Usuario();
    usuario.setId(id);
    when(usuarioService.getUsuarioById(id)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/v1/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

}