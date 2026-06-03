package com.Ferreteria.ms_inventarios.controller;
import com.Ferreteria.ms_inventarios.dto.InventarioDTO;
import com.Ferreteria.ms_inventarios.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(
            InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @PostMapping
    public ResponseEntity<InventarioDTO> save(
            @Valid @RequestBody InventarioDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventarioService.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<InventarioDTO>> getAll() {

        return ResponseEntity.ok(
                inventarioService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioDTO> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                inventarioService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody InventarioDTO dto) {

        return ResponseEntity.ok(
                inventarioService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        if (inventarioService.delete(id)) {
            return ResponseEntity.ok(
                    "Inventario eliminado");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Inventario no encontrado");
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<InventarioDTO> getByProductoId(
            @PathVariable Long productoId) {

        return ResponseEntity.ok(
                inventarioService.getByProductoId(productoId));
    }
}