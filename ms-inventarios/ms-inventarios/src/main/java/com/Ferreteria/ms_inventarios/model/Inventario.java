package com.Ferreteria.ms_inventarios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del producto del ms-productos
    @Column(nullable = false)
    private Long productoId;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Integer stockMinimo;

    @Column(nullable = false, length = 100)
    private String ubicacion;
}