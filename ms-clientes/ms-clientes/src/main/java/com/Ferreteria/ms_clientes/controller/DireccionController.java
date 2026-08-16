package com.Ferreteria.ms_clientes.controller;

import com.Ferreteria.ms_clientes.dto.DireccionDTO;
import com.Ferreteria.ms_clientes.service.DireccionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes/{clienteId}/direcciones")
public class DireccionController {

    private final DireccionService direccionService;

    public DireccionController(DireccionService direccionService) {
        this.direccionService = direccionService;
    }

    @PostMapping
    public ResponseEntity<DireccionDTO> save(
            @PathVariable Long clienteId,
            @Valid @RequestBody DireccionDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(direccionService.save(clienteId, dto));
    }

    @GetMapping
    public ResponseEntity<List<DireccionDTO>> getAll(
            @PathVariable Long clienteId) {

        return ResponseEntity.ok(direccionService.getByClienteId(clienteId));
    }

    @DeleteMapping("/{direccionId}")
    public ResponseEntity<String> delete(
            @PathVariable Long clienteId,
            @PathVariable Long direccionId) {

        if (direccionService.delete(clienteId, direccionId)) {
            return ResponseEntity.ok("Direccion eliminada");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Direccion no encontrada");
    }
}