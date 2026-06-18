package com.Ferreteria.ms_producto.service;

import com.Ferreteria.ms_producto.dto.ProductoDTO;
import com.Ferreteria.ms_producto.model.Producto;
import com.Ferreteria.ms_producto.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void save_debeGuardarProductoYRetornarDTOConId() {
        ProductoDTO dtoEntrada = ProductoDTO.builder()
                .nombre("Martillo")
                .descripcion("Martillo de acero, mango de goma")
                .precio(5000.0)
                .categoria("Herramientas")
                .marca("Stanley")
                .build();

        Producto productoGuardadoSimulado = Producto.builder()
                .id(1L)
                .nombre("Martillo")
                .descripcion("Martillo de acero, mango de goma")
                .precio(5000.0)
                .categoria("Herramientas")
                .marca("Stanley")
                .build();

        when(productoRepository.save(any(Producto.class)))
                .thenReturn(productoGuardadoSimulado);

        ProductoDTO resultado = productoService.save(dtoEntrada);

        assertEquals(1L, resultado.getId());
        assertEquals("Martillo", resultado.getNombre());
        assertEquals(5000.0, resultado.getPrecio());
        assertEquals("Herramientas", resultado.getCategoria());
        assertEquals("Stanley", resultado.getMarca());
    }

    @Test
    void delete_cuandoProductoExiste_debeEliminarYRetornarTrue() {
        Long idExistente = 1L;
        when(productoRepository.existsById(idExistente)).thenReturn(true);

        boolean resultado = productoService.delete(idExistente);

        assertTrue(resultado);
        verify(productoRepository, times(1)).deleteById(idExistente);
    }

    @Test
    void delete_cuandoProductoNoExiste_debeRetornarFalseSinEliminar() {
        Long idInexistente = 99L;
        when(productoRepository.existsById(idInexistente)).thenReturn(false);

        boolean resultado = productoService.delete(idInexistente);

        assertFalse(resultado);
        verify(productoRepository, never()).deleteById(any());
    }

    @Test
    void getById_cuandoProductoExiste_debeRetornarDTO() {
        Long id = 1L;
        Producto productoEncontrado = Producto.builder()
                .id(id)
                .nombre("Destornillador")
                .descripcion("Destornillador plano")
                .precio(1500.0)
                .categoria("Herramientas")
                .marca("Bahco")
                .build();

        when(productoRepository.findById(id)).thenReturn(Optional.of(productoEncontrado));

        ProductoDTO resultado = productoService.getById(id);

        assertEquals(id, resultado.getId());
        assertEquals("Destornillador", resultado.getNombre());
    }

    @Test
    void getById_cuandoProductoNoExiste_debeLanzarExcepcion() {
        Long idInexistente = 99L;
        when(productoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            productoService.getById(idInexistente);
        });
    }
}