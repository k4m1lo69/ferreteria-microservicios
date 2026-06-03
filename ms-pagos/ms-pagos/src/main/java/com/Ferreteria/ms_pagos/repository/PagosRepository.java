package com.Ferreteria.ms_pagos.repository;

import com.Ferreteria.ms_pagos.model.Pagos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagosRepository extends JpaRepository<Pagos, Long> {
    List<Pagos> findByPedidoId(Long pedidoId);
}
