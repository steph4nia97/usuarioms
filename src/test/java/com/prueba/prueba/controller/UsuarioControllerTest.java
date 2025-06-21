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
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.prueba.prueba.assambler.AssamblerUsuario;
import com.prueba.prueba.dto.UsuarioDTO;
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
@MockBean private AssamblerUsuario assamblerUsuario;

@Test 
void listar_deberiaRetornarListaUsuarios() throws Exception {
    // Given
    List<Usuario> usuarios = List.of(new Usuario(), new Usuario(), new Usuario());
    when(usuarioService.getAllUsuarios()).thenReturn(usuarios);
    when(usuarioDTOConverter.convertToDTO(any(Usuario.class))).thenReturn(new UsuarioDTO());
    when(assamblerUsuario.toModel(any(UsuarioDTO.class))).thenReturn(EntityModel.of(new UsuarioDTO()));

    mockMvc.perform(get("/api/v1/usuarios"))
            .andExpect(status().isOk());
}

@Test
void guardar_deberiaGuardarUsuario() throws Exception {
    // Given
    Usuario usuario = new Usuario();
    usuario.setId(1L);
    usuario.setCorreo("juan@email.com");
    usuario.setPassword("123");

    UsuarioDTO usuarioDTO = UsuarioDTO.builder()
            .id(1L)
            .correo("juan@email.com")
            .password("123")
            .build();

    
    when(usuarioDTOConverter.convertToEntity(any())).thenReturn(usuario);
    when(usuarioService.saveUsuario(any(Usuario.class))).thenReturn(usuario);
    when(usuarioDTOConverter.convertToDTO(any(Usuario.class))).thenReturn(usuarioDTO);
    when(assamblerUsuario.toModel(any(UsuarioDTO.class))).thenReturn(EntityModel.of(usuarioDTO));

    // No envíes el id en el JSON de entrada
    mockMvc.perform(post("/api/v1/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"correo\":\"juan@email.com\",\"password\":\"123\"}")) //no le envio el id xq es autoincremental
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

    Usuario usuarioActualizado = new Usuario();
    usuarioActualizado.setId(id);
    usuarioActualizado.setCorreo("nuevo@email.com");
    usuarioActualizado.setPassword("nueva");

    UsuarioDTO usuarioDTO = UsuarioDTO.builder()
            .id(id)
            .correo("nuevo@email.com")
            .password("nueva")
            .build();

    when(usuarioService.getUsuarioById(id)).thenReturn(Optional.of(usuarioExistente));
    when(usuarioDTOConverter.convertToEntity(any())).thenReturn(usuarioActualizado);
    when(usuarioService.saveUsuario(any(Usuario.class))).thenReturn(usuarioActualizado);
    when(usuarioDTOConverter.convertToDTO(any(Usuario.class))).thenReturn(usuarioDTO);
    when(assamblerUsuario.toModel(any(UsuarioDTO.class))).thenReturn(EntityModel.of(usuarioDTO));

    // No envíes el id en el JSON de entrada
    mockMvc.perform(put("/api/v1/usuarios/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"correo\":\"nuevo@email.com\",\"password\":\"nueva\"}"))
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
    usuario.setCorreo("juan@email.com");
    usuario.setPassword("123");

    UsuarioDTO usuarioDTO = UsuarioDTO.builder()
            .id(id)
            .correo("juan@email.com")
            .password("123")
            .build();

    // Mock converter y assembler
    when(usuarioService.getUsuarioById(id)).thenReturn(Optional.of(usuario));
    when(usuarioDTOConverter.convertToDTO(usuario)).thenReturn(usuarioDTO);
    when(assamblerUsuario.toModel(usuarioDTO)).thenReturn(EntityModel.of(usuarioDTO));

    mockMvc.perform(get("/api/v1/usuarios/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.correo").value("juan@email.com"))
            .andExpect(jsonPath("$.password").value("123"));
}
}