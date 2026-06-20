package com.Ferreteria.ms_pedidos.service;

import com.Ferreteria.ms_pedidos.dto.PedidoDTO;
import com.Ferreteria.ms_pedidos.model.Pedido;
import com.Ferreteria.ms_pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void save_debeCalcularTotalYAsignarEstadoPendiente() {
        PedidoDTO dtoEntrada = PedidoDTO.builder()
                .clienteId(1L)
                .productoId(2L)
                .cantidad(3)
                .precioUnitario(5000.0)
                .build();

        Pedido pedidoGuardado = Pedido.builder()
                .id(1L)
                .clienteId(1L)
                .productoId(2L)
                .cantidad(3)
                .precioUnitario(5000.0)
                .total(15000.0)
                .estado("PENDIENTE")
                .build();

        when(pedidoRepository.save(any(Pedido.class)))
                .thenReturn(pedidoGuardado);

        PedidoDTO resultado = pedidoService.save(dtoEntrada);

        assertEquals(15000.0, resultado.getTotal());
        assertEquals("PENDIENTE", resultado.getEstado());
        assertEquals(1L, resultado.getClienteId());
    }

    @Test
    void getById_cuandoExiste_debeRetornarPedido() {
        Long id = 1L;
        Pedido pedido = Pedido.builder()
                .id(id)
                .clienteId(1L)
                .productoId(2L)
                .cantidad(2)
                .precioUnitario(3000.0)
                .total(6000.0)
                .estado("PENDIENTE")
                .build();

        when(pedidoRepository.findById(id))
                .thenReturn(Optional.of(pedido));

        PedidoDTO resultado = pedidoService.getById(id);

        assertEquals(id, resultado.getId());
        assertEquals(6000.0, resultado.getTotal());
        assertEquals("PENDIENTE", resultado.getEstado());
    }

    @Test
    void getById_cuandoNoExiste_debeLanzarExcepcion() {
        Long id = 99L;
        when(pedidoRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            pedidoService.getById(id);
        });
    }

    @Test
    void delete_cuandoExiste_debeEliminarYRetornarTrue() {
        Long id = 1L;
        when(pedidoRepository.existsById(id)).thenReturn(true);

        boolean resultado = pedidoService.delete(id);

        assertTrue(resultado);
        verify(pedidoRepository, times(1)).deleteById(id);
    }

    @Test
    void delete_cuandoNoExiste_debeRetornarFalse() {
        Long id = 99L;
        when(pedidoRepository.existsById(id)).thenReturn(false);

        boolean resultado = pedidoService.delete(id);

        assertFalse(resultado);
        verify(pedidoRepository, never()).deleteById(any());
    }

    @Test
    void updateEstado_cuandoNoExiste_debeLanzarExcepcion() {
        Long id = 99L;
        when(pedidoRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            pedidoService.updateEstado(id, "ENVIADO");
        });
    }
}