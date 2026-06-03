package com.Ferreteria.ms_clientes.controller;


import com.Ferreteria.ms_clientes.dto.ClienteDTO;
import com.Ferreteria.ms_clientes.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(
            ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> save(
            @Valid @RequestBody ClienteDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteService.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getAll() {

        return ResponseEntity.ok(
                clienteService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                clienteService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ClienteDTO dto) {

        return ResponseEntity.ok(
                clienteService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        if (clienteService.delete(id)) {
            return ResponseEntity.ok(
                    "Cliente eliminado");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Cliente no encontrado");
    }
}

