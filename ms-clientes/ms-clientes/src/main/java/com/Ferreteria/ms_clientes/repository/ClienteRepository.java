package com.Ferreteria.ms_clientes.repository;


import com.Ferreteria.ms_clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository
        extends JpaRepository<Cliente, Long> {

    boolean existsByEmail(String email);
}