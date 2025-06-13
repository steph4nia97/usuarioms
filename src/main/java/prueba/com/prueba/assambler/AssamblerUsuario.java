
package main.java.prueba.com.prueba.assambler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.EntityModel; 
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import prueba.com.prueba.controller.UsuarioController;
import prueba.com.prueba.dto.UsuarioDTO;
import prueba.com.prueba.model.UsuarioDTO;



@Component
public class AssamblerUsuario implements RepresentationModelAssembler<UsuarioDTO, EntityModel<UsuarioDTO>> {

    @Override
    public EntityModel<UsuarioDTO> toModel(@NonNull UsuarioDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(UsuarioController.class).getTicketById(dto.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).getAllTickets()).withRel("tickets"),
                linkTo(methodOn(UsuarioController.class).updateTicket(dto.getId(), null)).withRel("update"),
                linkTo(methodOn(UsuarioController.class).deleteTicket(dto.getId())).withRel("delete")
        );
    }
}

