package com.Ferreteria.ms_inventarios.service;

import com.Ferreteria.ms_inventarios.dto.InventarioDTO;
import com.Ferreteria.ms_inventarios.model.Inventario;
import com.Ferreteria.ms_inventarios.repository.InventarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    @Test
    void save_debeGuardarInventarioYRetornarDTO() {
        InventarioDTO dtoEntrada = InventarioDTO.builder()
                .productoId(1L)
                .stock(100)
                .stockMinimo(10)
                .ubicacion("Bodega A")
                .build();

        Inventario inventarioGuardado = Inventario.builder()
                .id(1L)
                .productoId(1L)
                .stock(100)
                .stockMinimo(10)
                .ubicacion("Bodega A")
                .build();

        when(inventarioRepository.save(any(Inventario.class)))
                .thenReturn(inventarioGuardado);

        InventarioDTO resultado = inventarioService.save(dtoEntrada);

        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getProductoId());
        assertEquals(100, resultado.getStock());
        assertEquals("Bodega A", resultado.getUbicacion());
    }

    @Test
    void getByProductoId_cuandoExiste_debeRetornarInventario() {
        Long productoId = 5L;
        Inventario inventario = Inventario.builder()
                .id(1L)
                .productoId(productoId)
                .stock(50)
                .stockMinimo(5)
                .ubicacion("Bodega B")
                .build();

        when(inventarioRepository.findByProductoId(productoId))
                .thenReturn(Optional.of(inventario));

        InventarioDTO resultado = inventarioService.getByProductoId(productoId);

        assertEquals(productoId, resultado.getProductoId());
        assertEquals(50, resultado.getStock());
    }

    @Test
    void getByProductoId_cuandoNoExiste_debeLanzarExcepcion() {
        Long productoId = 99L;
        when(inventarioRepository.findByProductoId(productoId))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            inventarioService.getByProductoId(productoId);
        });
    }

    @Test
    void delete_cuandoExiste_debeEliminarYRetornarTrue() {
        Long id = 1L;
        when(inventarioRepository.existsById(id)).thenReturn(true);

        boolean resultado = inventarioService.delete(id);

        assertTrue(resultado);
        verify(inventarioRepository, times(1)).deleteById(id);
    }

    @Test
    void delete_cuandoNoExiste_debeRetornarFalse() {
        Long id = 99L;
        when(inventarioRepository.existsById(id)).thenReturn(false);

        boolean resultado = inventarioService.delete(id);

        assertFalse(resultado);
        verify(inventarioRepository, never()).deleteById(any());
    }

    @Test
    void update_cuandoInventarioNoExiste_debeLanzarExcepcion() {
        Long id = 99L;
        when(inventarioRepository.findById(id)).thenReturn(Optional.empty());

        InventarioDTO dto = InventarioDTO.builder()
                .productoId(1L)
                .stock(20)
                .stockMinimo(5)
                .ubicacion("Bodega C")
                .build();

        assertThrows(RuntimeException.class, () -> {
            inventarioService.update(id, dto);
        });
    }
}