package com.Ferreteria.ms_clientes.service;

import com.Ferreteria.ms_clientes.dto.DireccionDTO;
import com.Ferreteria.ms_clientes.model.Cliente;
import com.Ferreteria.ms_clientes.model.Direccion;
import com.Ferreteria.ms_clientes.repository.ClienteRepository;
import com.Ferreteria.ms_clientes.repository.DireccionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DireccionService {

    private static final Logger log =
            LoggerFactory.getLogger(DireccionService.class);

    private final DireccionRepository direccionRepository;
    private final ClienteRepository clienteRepository;

    public DireccionService(DireccionRepository direccionRepository,
                             ClienteRepository clienteRepository) {
        this.direccionRepository = direccionRepository;
        this.clienteRepository = clienteRepository;
    }

    public DireccionDTO save(Long clienteId, DireccionDTO dto) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Direccion direccion = Direccion.builder()
                .calle(dto.getCalle())
                .ciudad(dto.getCiudad())
                .region(dto.getRegion())
                .codigoPostal(dto.getCodigoPostal())
                .tipo(dto.getTipo())
                .cliente(cliente)
                .build();

        Direccion saved = direccionRepository.save(direccion);

        log.info("Direccion creada para cliente {}", clienteId);

        return convertirADTO(saved);
    }

    public List<DireccionDTO> getByClienteId(Long clienteId) {

        if (!clienteRepository.existsById(clienteId)) {
            throw new RuntimeException("Cliente no encontrado");
        }

        return direccionRepository.findByClienteId(clienteId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public boolean delete(Long clienteId, Long direccionId) {

        Direccion direccion = direccionRepository.findById(direccionId)
                .orElseThrow(() -> new RuntimeException("Direccion no encontrada"));

        if (!direccion.getCliente().getId().equals(clienteId)) {
            throw new RuntimeException("La direccion no pertenece a este cliente");
        }

        direccionRepository.deleteById(direccionId);

        log.info("Direccion {} eliminada del cliente {}", direccionId, clienteId);

        return true;
    }

    private DireccionDTO convertirADTO(Direccion direccion) {
        return DireccionDTO.builder()
                .id(direccion.getId())
                .calle(direccion.getCalle())
                .ciudad(direccion.getCiudad())
                .region(direccion.getRegion())
                .codigoPostal(direccion.getCodigoPostal())
                .tipo(direccion.getTipo())
                .build();
    }
}