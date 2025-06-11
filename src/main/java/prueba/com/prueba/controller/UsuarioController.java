package prueba.com.prueba.controller;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prueba.com.prueba.dto.UsuarioDTO;
import prueba.com.prueba.dto.UsuarioDTOConverter;
import prueba.com.prueba.dto.VentaDTO;
import prueba.com.prueba.model.Usuario;
import prueba.com.prueba.service.UsuarioService;
import prueba.com.prueba.service.VentaClientService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioDTOConverter usuarioDTOConverter;
    private final VentaClientService ventaClientService;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Usuario corriendo OK :)");
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.getAllUsuarios()
            .stream()
            .map(usuarioDTOConverter::convertToDTO)
            .toList();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        logger.info("Fetching usuario with id: {}", id);
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        Usuario createdUsuario = usuarioService.saveUsuario(usuario);
        return ResponseEntity.status(201).body(createdUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        if (!usuarioService.getUsuarioById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        usuario.setId(id);
        Usuario updatedUsuario = usuarioService.saveUsuario(usuario);
        return ResponseEntity.accepted().body(updatedUsuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        if (!usuarioService.getUsuarioById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.deleteUsuario(id);
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/ventas")
    public ResponseEntity<List<VentaDTO>> getVentasPorUsuario(@PathVariable Long id) {
        List<VentaDTO> ventas = ventaClientService.getVentasPorUsuario(id);
        if (ventas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ventas);
    }
}
