package prueba.com.prueba.dto;

import lombok.Data;

@Data
public class VentaDTO {
    private Long id;
    private Long usuarioId;
    private String fecha;
    private Double total;
}
