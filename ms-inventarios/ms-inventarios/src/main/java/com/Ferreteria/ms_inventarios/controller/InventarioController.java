package com.Ferreteria.ms_inventarios.controller;
import com.Ferreteria.ms_inventarios.dto.InventarioDTO;
import com.Ferreteria.ms_inventarios.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventarios")
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
    @Operation(summary = "Obtener inventario por ID de producto")
    @ApiResponse(responseCode = "200", description = "Inventario encontrado")
    @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    public ResponseEntity<InventarioDTO> getByProductoId(
            @PathVariable
            @Parameter(description = "ID del producto")
            Long productoId) {

        return ResponseEntity.ok(
                inventarioService.getByProductoId(productoId));
    }

    @PatchMapping("/producto/{productoId}/descontar")
    @Operation(summary = "Descuenta stock de un producto tras confirmar un pedido")
    @ApiResponse(responseCode = "200", description = "Stock descontado")
    @ApiResponse(responseCode = "409", description = "Stock insuficiente")
    @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    public ResponseEntity<InventarioDTO> descontarStock(
            @PathVariable Long productoId,
            @RequestBody Map<String, Integer> body) {

        Integer cantidad = body.get("cantidad");

        try {
            return ResponseEntity.ok(
                    inventarioService.descontarStock(productoId, cantidad));
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().startsWith("Stock insuficiente")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}