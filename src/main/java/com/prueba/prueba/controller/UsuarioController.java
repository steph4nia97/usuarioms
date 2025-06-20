package com.prueba.prueba.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.prueba.prueba.assambler.AssamblerUsuario;
import com.prueba.prueba.dto.UsuarioCreateDTO;
import com.prueba.prueba.dto.UsuarioDTO;
import com.prueba.prueba.dto.UsuarioDTOConverter;
import com.prueba.prueba.dto.VentaDTO;
import com.prueba.prueba.model.Usuario;
import com.prueba.prueba.service.UsuarioService;
import com.prueba.prueba.service.VentaClientService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioDTOConverter usuarioDTOConverter;
    private final VentaClientService ventaClientService;
    private final AssamblerUsuario assamblerUsuario;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Usuario corriendo OK :)");
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UsuarioDTO>>> getAllUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.getAllUsuarios()
            .stream()
            .map(usuarioDTOConverter::convertToDTO)
            .toList();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<EntityModel<UsuarioDTO>> usuariosModel = usuarios.stream()
            .map(assamblerUsuario::toModel)
            .toList();
        return ResponseEntity.ok(CollectionModel.of(usuariosModel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UsuarioDTO>> getUsuarioById(@PathVariable Long id) {
        logger.info("Fetching usuario with id: {}", id);
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        return usuario
            .map(u -> usuarioDTOConverter.convertToDTO(u))
            .map(assamblerUsuario::toModel)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

@PostMapping
public ResponseEntity<EntityModel<UsuarioDTO>> createUsuario(@RequestBody UsuarioCreateDTO usuarioCreateDTO) {
    Usuario usuario = usuarioDTOConverter.convertToEntity(usuarioCreateDTO);
    Usuario createdUsuario = usuarioService.saveUsuario(usuario);
    UsuarioDTO dto = usuarioDTOConverter.convertToDTO(createdUsuario);
    EntityModel<UsuarioDTO> model = assamblerUsuario.toModel(dto);
    return ResponseEntity.status(201).body(model);
}

@PutMapping("/{id}")
public ResponseEntity<EntityModel<UsuarioDTO>> updateUsuario(@PathVariable Long id, @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
    if (!usuarioService.getUsuarioById(id).isPresent()) {
        return ResponseEntity.notFound().build();
    }
    Usuario usuario = usuarioDTOConverter.convertToEntity(usuarioCreateDTO);
    usuario.setId(id);
    Usuario updatedUsuario = usuarioService.saveUsuario(usuario);
    UsuarioDTO dto = usuarioDTOConverter.convertToDTO(updatedUsuario);
    EntityModel<UsuarioDTO> model = assamblerUsuario.toModel(dto);
    return ResponseEntity.accepted().body(model);
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