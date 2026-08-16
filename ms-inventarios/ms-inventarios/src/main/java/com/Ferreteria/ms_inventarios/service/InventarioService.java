package com.Ferreteria.ms_inventarios.service;


import com.Ferreteria.ms_inventarios.dto.InventarioDTO;
import com.Ferreteria.ms_inventarios.model.Inventario;
import com.Ferreteria.ms_inventarios.repository.InventarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventarioService {

    private static final Logger log =
            LoggerFactory.getLogger(InventarioService.class);

    private final InventarioRepository inventarioRepository;

    public InventarioService(
            InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    public InventarioDTO save(InventarioDTO dto) {

        Inventario inventario = Inventario.builder()
                .productoId(dto.getProductoId())
                .cantidad(dto.getCantidad())
                .cantidadMinima(dto.getCantidadMinima())
                .ubicacion(dto.getUbicacion())
                .build();

        Inventario saved =
                inventarioRepository.save(inventario);

        log.info("Inventario creado para producto {}",
                saved.getProductoId());

        return convertirADTO(saved);
    }

    public List<InventarioDTO> getAll() {

        return inventarioRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public InventarioDTO getById(Long id) {

        Inventario inventario =
                inventarioRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Inventario no encontrado"));

        return convertirADTO(inventario);
    }

    public InventarioDTO update(
            Long id,
            InventarioDTO dto) {

        Inventario inventario =
                inventarioRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Inventario no encontrado"));

        inventario.setProductoId(dto.getProductoId());
        inventario.setCantidad(dto.getCantidad());
        inventario.setCantidadMinima(dto.getCantidadMinima());
        inventario.setUbicacion(dto.getUbicacion());

        log.info("Inventario actualizado {}", id);

        return convertirADTO(
                inventarioRepository.save(inventario));
    }

    public boolean delete(Long id) {

        if (inventarioRepository.existsById(id)) {

            inventarioRepository.deleteById(id);

            log.info("Inventario eliminado {}", id);

            return true;
        }

        return false;
    }

    public InventarioDTO getByProductoId(Long productoId) {
        return inventarioRepository.findByProductoId(productoId)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
    }

    public synchronized InventarioDTO descontarStock(Long productoId, Integer cantidad) {

        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new RuntimeException(
                        "Inventario no encontrado para producto " + productoId));

        if (inventario.getCantidad() < cantidad) {
            throw new RuntimeException(
                    "Stock insuficiente. Disponible: " + inventario.getCantidad()
                            + ", solicitado: " + cantidad);
        }

        inventario.setCantidad(inventario.getCantidad() - cantidad);

        log.info("Stock descontado: producto {} -{} unidades (queda: {})",
                productoId, cantidad, inventario.getCantidad());

        return convertirADTO(inventarioRepository.save(inventario));
    }

    private InventarioDTO convertirADTO(Inventario inventario) {
        return InventarioDTO.builder()
                .id(inventario.getId())
                .productoId(inventario.getProductoId())
                .cantidad(inventario.getCantidad())
                .cantidadMinima(inventario.getCantidadMinima())
                .ubicacion(inventario.getUbicacion())
                .build();
    }

}