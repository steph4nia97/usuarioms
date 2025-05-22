package prueba.com.prueba.dto;

import org.springframework.stereotype.Component;
import prueba.com.prueba.model.Usuario;

@Component
public class UsuarioDTOConverter {

    public UsuarioDTO convertToDTO(Usuario usuario) {
        if (usuario == null) return null;
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .correo(usuario.getCorreo())
                .password(usuario.getPassword())
                .build();
    }

    public Usuario convertToEntity(UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null) return null;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioDTO.getId());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setPassword(usuarioDTO.getPassword());
        return usuario;
    }
}
