package com.Ferreteria.ms_pagos.service;

import com.Ferreteria.ms_pagos.dto.PagoDTO;
import com.Ferreteria.ms_pagos.model.Pago;
import com.Ferreteria.ms_pagos.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public PagoDTO save(PagoDTO dto) {
        Pago pago = Pago.builder()
                .pedidoId(dto.getPedidoId())
                .monto(dto.getMonto())
                .metodoPago(dto.getMetodoPago())
                .estado("PENDIENTE")
                .fecha(LocalDateTime.now())
                .build();
        return toDTO(pagoRepository.save(pago));
    }

    public List<PagoDTO> getAll() {
        return pagoRepository.findAll().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public PagoDTO getById(Long id) {
        return toDTO(pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado")));
    }

    public List<PagoDTO> getByPedidoId(Long pedidoId) {
        return pagoRepository.findByPedidoId(pedidoId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public PagoDTO updateEstado(Long id, String estado) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        pago.setEstado(estado);
        return toDTO(pagoRepository.save(pago));
    }

    private PagoDTO toDTO(Pago p) {
        return PagoDTO.builder()
                .id(p.getId())
                .pedidoId(p.getPedidoId())
                .monto(p.getMonto())
                .metodoPago(p.getMetodoPago())
                .estado(p.getEstado())
                .fecha(p.getFecha().toString())
                .build();
    }
}
