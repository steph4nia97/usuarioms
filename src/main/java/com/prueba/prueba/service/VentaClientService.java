package com.prueba.prueba.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.prueba.prueba.dto.VentaDTO;

import java.util.Arrays;
import java.util.List;

@Service
public class VentaClientService {
    private final RestTemplate restTemplate = new RestTemplate();

    public List<VentaDTO> getVentasPorUsuario(Long usuarioId) {
        String url = "http://localhost:8081/api/v1/ventas/usuario/" + usuarioId;
        VentaDTO[] ventas = restTemplate.getForObject(url, VentaDTO[].class);
        return Arrays.asList(ventas);
    }
}