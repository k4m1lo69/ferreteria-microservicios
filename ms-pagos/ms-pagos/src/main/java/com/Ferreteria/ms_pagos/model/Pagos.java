package com.Ferreteria.ms_pagos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Pagos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pedidoId;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false, length = 50)
    private String metodoPago;

    @Column(nullable = false, length = 50)
    private String estado;

    @Column(nullable = false)
    private LocalDateTime fecha;
}