package com.Ferreteria.ms_producto.service;

import com.Ferreteria.ms_producto.dto.ProductoDTO;
import com.Ferreteria.ms_producto.model.Producto;
import com.Ferreteria.ms_producto.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {
    private static final Logger log =
            LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<ProductoDTO> save(ProductoDTO dto) {
        Producto entity = Producto.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .categoria(dto.getCategoria())
                .marca((dto.getMarca()))
                .build();
        Producto saved = productoRepository.save(entity);
        log.info("Producto creado: {}", saved.getNombre());
        return convertirADTO(saved);

    }

    

}
