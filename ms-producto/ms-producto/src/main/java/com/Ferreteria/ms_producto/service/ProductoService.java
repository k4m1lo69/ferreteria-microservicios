package com.Ferreteria.ms_producto.service;

import com.Ferreteria.ms_producto.dto.ProductoDTO;
import com.Ferreteria.ms_producto.model.Producto;
import com.Ferreteria.ms_producto.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {
    private static final Logger log =
            LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public ProductoDTO save(ProductoDTO dto) {
        Producto entity = Producto.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .categoria(dto.getCategoria())
                .marca(dto.getMarca())
                .build();
        Producto saved = productoRepository.save(entity);
        log.info("Producto creado: {}", saved.getNombre());
        return convertirADTO(saved);
    }
    public List<ProductoDTO> getAll() {
        log.info("consultando todos los productos");
        return  productoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO getById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() ->{
                    log.error("Producto no encontrado: {}", id);
                    return new RuntimeException(
                            "Producto con ID " + id + "no encontradp");
                });
        return convertirADTO(producto);
    }

    public ProductoDTO update(Long id, ProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Producto con ID " + id + "no encontrado"));
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setMarca(dto.getMarca());
        log.info("Producto eliminado: {}", id);
        return convertirADTO(productoRepository.save(producto));
    }

    public boolean delete(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.existsById(id);
            log.info("Producto eliminado {}", id);
            return true;
        }
        log.warn("Intento de eliminar producto inexistente: {}", id);
        return false;
    }

    public List<ProductoDTO> getByCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }


    private ProductoDTO convertirADTO(Producto producto) {
        return ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .categoria(producto.getCategoria())
                .marca(producto.getMarca())
                .build();
    }

}
