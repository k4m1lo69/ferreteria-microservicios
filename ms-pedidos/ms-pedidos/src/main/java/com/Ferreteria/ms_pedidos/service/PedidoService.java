package com.Ferreteria.ms_pedidos.service;

import com.Ferreteria.ms_pedidos.dto.InventarioDTO;
import com.Ferreteria.ms_pedidos.dto.PedidoDTO;
import com.Ferreteria.ms_pedidos.dto.ProductoDTO;
import com.Ferreteria.ms_pedidos.model.Pedido;
import com.Ferreteria.ms_pedidos.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private static final Logger log =
            LoggerFactory.getLogger(PedidoService.class);

    private final PedidoRepository pedidoRepository;
    private final WebClient webClient;

    public PedidoService(PedidoRepository pedidoRepository,
                          WebClient.Builder webClientBuilder) {
        this.pedidoRepository = pedidoRepository;
        this.webClient = webClientBuilder.build();
    }

    public PedidoDTO save(PedidoDTO dto) {

        ProductoDTO producto = obtenerProducto(dto.getProductoId());
        InventarioDTO inventario = obtenerInventario(dto.getProductoId());

        if (inventario.getCantidad() < dto.getCantidad()) {
            throw new RuntimeException(
                    "Stock insuficiente para el producto "
                            + producto.getNombre()
                            + " (disponible: " + inventario.getCantidad() + ")");
        }

        Double precioReal = producto.getPrecio();
        Double total = dto.getCantidad() * precioReal;

        Pedido pedido = Pedido.builder()
                .clienteId(dto.getClienteId())
                .productoId(dto.getProductoId())
                .cantidad(dto.getCantidad())
                .precioUnitario(precioReal)
                .total(total)
                .estado("CONFIRMADO")
                .fecha(LocalDateTime.now())
                .build();

        Pedido saved = pedidoRepository.save(pedido);

        log.info("Pedido creado: {}", saved.getId());

        return convertirADTO(saved);
    }

    private ProductoDTO obtenerProducto(Long productoId) {
        try {
            return webClient.get()
                    .uri("http://ms-producto/api/productos/{id}", productoId)
                    .retrieve()
                    .bodyToMono(ProductoDTO.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new RuntimeException("Producto no encontrado: " + productoId);
        } catch (Exception e) {
            log.error("Error consultando ms-producto: {}", e.getMessage());
            throw new RuntimeException("ms-producto no disponible en este momento");
        }
    }

    private InventarioDTO obtenerInventario(Long productoId) {
        try {
            return webClient.get()
                    .uri("http://ms-inventarios/api/inventarios/producto/{productoId}", productoId)
                    .retrieve()
                    .bodyToMono(InventarioDTO.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new RuntimeException("No hay inventario registrado para el producto: " + productoId);
        } catch (Exception e) {
            log.error("Error consultando ms-inventarios: {}", e.getMessage());
            throw new RuntimeException("ms-inventarios no disponible en este momento");
        }
    }

    public List<PedidoDTO> getAll() {

        return pedidoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO getById(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Pedido no encontrado"));

        return convertirADTO(pedido);
    }

    public PedidoDTO updateEstado(
            Long id,
            String estado) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Pedido no encontrado"));

        pedido.setEstado(estado);

        log.info("Pedido actualizado {}", id);

        return convertirADTO(
                pedidoRepository.save(pedido));
    }

    public boolean delete(Long id) {

        if (pedidoRepository.existsById(id)) {

            pedidoRepository.deleteById(id);

            log.info("Pedido eliminado {}", id);

            return true;
        }

        return false;
    }

    public List<PedidoDTO> getByClienteId(Long clienteId) {

        return pedidoRepository.findByClienteId(clienteId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private PedidoDTO convertirADTO(Pedido pedido) {

        return PedidoDTO.builder()
                .id(pedido.getId())
                .clienteId(pedido.getClienteId())
                .productoId(pedido.getProductoId())
                .cantidad(pedido.getCantidad())
                .precioUnitario(pedido.getPrecioUnitario())
                .total(pedido.getTotal())
                .estado(pedido.getEstado())
                .build();
    }
}