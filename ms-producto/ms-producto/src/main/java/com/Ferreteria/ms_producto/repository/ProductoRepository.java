package com.Ferreteria.ms_producto.repository;

import com.Ferreteria.ms_producto.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Buscar por categoría
    List<Producto> findByCategoria(String categoria);

    // Buscar por marca
    List<Producto> findByMarca(String marca);
}