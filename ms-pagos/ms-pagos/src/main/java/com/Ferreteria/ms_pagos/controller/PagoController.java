package com.Ferreteria.ms_pagos.controller;

import com.Ferreteria.ms_pagos.dto.PagoDTO;
import com.Ferreteria.ms_pagos.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<PagoDTO> save(
            @Valid @RequestBody PagoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pagoService.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<PagoDTO>> getAll() {
        return ResponseEntity.ok(pagoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.getById(id));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<PagoDTO>> getByPedido(
            @PathVariable Long pedidoId) {
        return ResponseEntity.ok(pagoService.getByPedidoId(pedidoId));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<PagoDTO> updateEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        return ResponseEntity.ok(pagoService.updateEstado(id, estado));
    }
}
