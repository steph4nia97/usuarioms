package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

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

}
