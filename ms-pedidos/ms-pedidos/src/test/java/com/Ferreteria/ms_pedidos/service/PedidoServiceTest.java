package com.Ferreteria.ms_pedidos.service;

import com.Ferreteria.ms_pedidos.dto.InventarioDTO;
import com.Ferreteria.ms_pedidos.dto.PedidoDTO;
import com.Ferreteria.ms_pedidos.dto.ProductoDTO;
import com.Ferreteria.ms_pedidos.model.Pedido;
import com.Ferreteria.ms_pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked", "rawtypes"})
@DisplayName("PedidoService Tests")
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.build()).thenReturn(webClient);
        pedidoService = new PedidoService(pedidoRepository, webClientBuilder);
    }

    @Test
    @DisplayName("Debe crear pedido cuando hay stock disponible")
    void testCrearPedidoConStockDisponible() {
        PedidoDTO dto = PedidoDTO.builder()
                .clienteId(1L)
                .productoId(1L)
                .cantidad(5)
                .precioUnitario(100.0)
                .build();

        ProductoDTO producto = ProductoDTO.builder()
                .id(1L)
                .nombre("Martillo")
                .precio(100.0)
                .build();

        InventarioDTO inventario = InventarioDTO.builder()
                .id(1L)
                .productoId(1L)
                .cantidad(10)
                .cantidadMinima(2)
                .build();

        Pedido pedidoGuardado = Pedido.builder()
                .id(1L)
                .clienteId(1L)
                .productoId(1L)
                .cantidad(5)
                .precioUnitario(100.0)
                .total(500.0)
                .estado("CONFIRMADO")
                .fecha(LocalDateTime.now())
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object[].class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(eq(ProductoDTO.class))).thenReturn(Mono.just(producto));
        when(responseSpec.bodyToMono(eq(InventarioDTO.class))).thenReturn(Mono.just(inventario));

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoGuardado);

        PedidoDTO resultado = pedidoService.save(dto);

        assertNotNull(resultado);
        assertEquals("CONFIRMADO", resultado.getEstado());
        assertEquals(500.0, resultado.getTotal());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Debe rechazar el pedido cuando no hay stock suficiente")
    void testCrearPedidoSinStockDisponible() {
        PedidoDTO dto = PedidoDTO.builder()
                .clienteId(1L)
                .productoId(1L)
                .cantidad(50)
                .precioUnitario(100.0)
                .build();

        ProductoDTO producto = ProductoDTO.builder()
                .id(1L)
                .nombre("Martillo")
                .precio(100.0)
                .build();

        InventarioDTO inventario = InventarioDTO.builder()
                .id(1L)
                .productoId(1L)
                .cantidad(3)
                .cantidadMinima(2)
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object[].class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(eq(ProductoDTO.class))).thenReturn(Mono.just(producto));
        when(responseSpec.bodyToMono(eq(InventarioDTO.class))).thenReturn(Mono.just(inventario));

        assertThrows(RuntimeException.class, () -> pedidoService.save(dto));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe obtener pedido por ID")
    void testGetPedidoById() {
        Pedido pedido = Pedido.builder()
                .id(1L)
                .clienteId(1L)
                .productoId(1L)
                .cantidad(5)
                .precioUnitario(100.0)
                .total(500.0)
                .estado("CONFIRMADO")
                .fecha(LocalDateTime.now())
                .build();

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        PedidoDTO resultado = pedidoService.getById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("CONFIRMADO", resultado.getEstado());
    }

    @Test
    @DisplayName("Debe actualizar estado del pedido")
    void testActualizarEstadoPedido() {
        Pedido pedido = Pedido.builder()
                .id(1L)
                .clienteId(1L)
                .productoId(1L)
                .cantidad(5)
                .precioUnitario(100.0)
                .total(500.0)
                .estado("CONFIRMADO")
                .fecha(LocalDateTime.now())
                .build();

        Pedido pedidoActualizado = new Pedido();
        pedidoActualizado.setId(1L);
        pedidoActualizado.setEstado("ENTREGADO");

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any())).thenReturn(pedidoActualizado);

        PedidoDTO resultado = pedidoService.updateEstado(1L, "ENTREGADO");

        assertEquals("ENTREGADO", resultado.getEstado());
        verify(pedidoRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Debe obtener pedidos por cliente")
    void testGetPedidosByCliente() {
        Long clienteId = 1L;

        assertDoesNotThrow(() -> {
            pedidoService.getByClienteId(clienteId);
        });
    }
}
