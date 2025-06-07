package prueba.com.prueba.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import prueba.com.prueba.model.Usuario;
import prueba.com.prueba.repository.UsuarioRepository;
import prueba.com.prueba.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService service;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    void listar_deberiaListarTodosUsuarios(){
        //Given
        List<Usuario> usuarios = List.of(new Usuario(), new Usuario(), new Usuario());
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        //When
        List<Usuario> resultado = service.getAllUsuarios();

        //Then
        assertEquals(usuarios, resultado);
    }
    @Test
    void Guardar_deberiaGuardarUsuario(){
        // Given
        Usuario usuario = new Usuario(1L, "juan@correo.cl", "1234");
        Usuario usuarioGuardado = new Usuario(1L, "juan@correo.cl", "1234");

        when(usuarioRepository.save(usuario)).thenReturn(usuarioGuardado);

        // When
        Usuario resultado = service.saveUsuario(usuario);

        // Then
        assertEquals(usuario.getCorreo(), resultado.getCorreo());
    }

    @Test
    void Actualizar_deberiaActualizarUsuario(){
        // Given
        String correo = "aaa";
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        when(usuarioRepository.existsById(id)).thenReturn(true);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        // When
        Usuario resultado = service.updateUsuario(id, usuario);

        // Then
        assertEquals(usuario, resultado);
    
    }
    
    @Test
    void Eliminar_deberiaEliminarUsuario(){
        // Given
        Long id = 1L;
        when(usuarioRepository.existsById(id)).thenReturn(true);

        // When
        boolean resultado = service.deleteUsuario(id);

        // Then
        assertEquals(true, resultado);
    }
    @Test
    void ObtenerPorId_deberiaRetornarUsuarioPorId(){
        // Given
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // When
        Optional<Usuario> resultado = service.getUsuarioById(id);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }
    
    @Test
    void ObtenerPorId_deberiaRetornarVacioSiNoExisteUsuario(){
        // Given
        Long id = 1L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Usuario> resultado = service.getUsuarioById(id);

        // Then
        assertFalse(!resultado.isPresent());
    }
    
    @Test
    void Actualizar_deberiaRetornarNullSiNoExisteUsuario(){
        // Given
        Long id = 1L;
        Usuario usuario = new Usuario();
        when(usuarioRepository.existsById(id)).thenReturn(false);

        // When
        Usuario resultado = service.updateUsuario(id, usuario);

        // Then
        assertEquals(null, resultado);
    }
    @Test
    void Eliminar_deberiaRetornarFalseSiNoExisteUsuario(){
        // Given
        Long id = 1L;
        when(usuarioRepository.existsById(id)).thenReturn(false);

        // When
        boolean resultado = service.deleteUsuario(id);

        // Then
        assertEquals(false, resultado);
    }
    

}
