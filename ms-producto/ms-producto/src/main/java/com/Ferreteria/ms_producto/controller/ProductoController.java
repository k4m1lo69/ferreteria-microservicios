package com.Ferreteria.ms_producto.controller;

import com.Ferreteria.ms_producto.dto.ProductoDTO;
import com.Ferreteria.ms_producto.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> save(
            @Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productoService.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getAll() {
        return ResponseEntity.ok(productoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(productoService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(productoService.update(id, dto));
    }

    @


}