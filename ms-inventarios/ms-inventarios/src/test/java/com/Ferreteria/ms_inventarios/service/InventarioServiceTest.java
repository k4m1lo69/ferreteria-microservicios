package com.Ferreteria.ms_inventarios.service;

import com.Ferreteria.ms_inventarios.dto.InventarioDTO;
import com.Ferreteria.ms_inventarios.model.Inventario;
import com.Ferreteria.ms_inventarios.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("InventarioService Tests")
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Debe registrar inventario")
    void testSaveInventario() {
        InventarioDTO dto = InventarioDTO.builder()
                .productoId(1L)
                .cantidad(100)
                .cantidadMinima(5)
                .ubicacion("Pasillo A")
                .build();

        Inventario inventario = Inventario.builder()
                .id(1L)
                .productoId(1L)
                .cantidad(100)
                .cantidadMinima(5)
                .ubicacion("Pasillo A")
                .build();

        when(inventarioRepository.save(any())).thenReturn(inventario);
        InventarioDTO resultado = inventarioService.save(dto);

        assertNotNull(resultado);
        assertEquals(100, resultado.getCantidad());
    }

    @Test
    @DisplayName("Debe obtener inventario por ID")
    void testGetInventarioById() {
        Inventario inventario = new Inventario(1L, 1L, 100, 5, "Pasillo A");
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        InventarioDTO resultado = inventarioService.getById(1L);

        assertEquals(100, resultado.getCantidad());
    }

    @Test
    @DisplayName("Debe actualizar cantidad de inventario")
    void testUpdateInventario() {
        Inventario inventarioExistente = new Inventario(1L, 1L, 100, 5, "Pasillo A");
        Inventario inventarioActualizado = new Inventario(1L, 1L, 150, 5, "Pasillo A");

        InventarioDTO dto = InventarioDTO.builder().cantidad(150).build();

        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventarioExistente));
        when(inventarioRepository.save(any())).thenReturn(inventarioActualizado);

        InventarioDTO resultado = inventarioService.update(1L, dto);

        assertEquals(150, resultado.getCantidad());
    }

    @Test
    @DisplayName("Debe descontar stock cuando hay suficiente cantidad")
    void testDescontarStockConCantidadSuficiente() {
        Inventario inventario = new Inventario(1L, 1L, 100, 5, "Pasillo A");
        Inventario inventarioDescontado = new Inventario(1L, 1L, 95, 5, "Pasillo A");

        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any())).thenReturn(inventarioDescontado);

        InventarioDTO resultado = inventarioService.descontarStock(1L, 5);

        assertEquals(95, resultado.getCantidad());
        verify(inventarioRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Debe rechazar el descuento cuando no hay stock suficiente")
    void testDescontarStockSinStockSuficiente() {
        Inventario inventario = new Inventario(1L, 1L, 3, 5, "Pasillo A");

        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inventario));

        assertThrows(RuntimeException.class, () ->
                inventarioService.descontarStock(1L, 10));

        verify(inventarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepcion si no existe inventario para el producto")
    void testDescontarStockProductoSinInventario() {
        when(inventarioRepository.findByProductoId(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                inventarioService.descontarStock(99L, 1));

        verify(inventarioRepository, never()).save(any());
    }
}