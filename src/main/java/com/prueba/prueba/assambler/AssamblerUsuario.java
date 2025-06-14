package com.prueba.prueba.assambler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.prueba.prueba.controller.UsuarioController;
import com.prueba.prueba.dto.UsuarioDTO;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AssamblerUsuario implements RepresentationModelAssembler<UsuarioDTO, EntityModel<UsuarioDTO>> {

    @Override
    public EntityModel<UsuarioDTO> toModel(UsuarioDTO dto) {
        return EntityModel.of(dto,
                        linkTo(methodOn(UsuarioController.class).getUsuarioById(dto.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).getAllUsuarios()).withRel("tickets"),
                linkTo(methodOn(UsuarioController.class).updateUsuario(dto.getId(), null)).withRel("update"),
                linkTo(methodOn(UsuarioController.class).deleteUsuario(dto.getId())).withRel("delete")
        );
 
    }

}

