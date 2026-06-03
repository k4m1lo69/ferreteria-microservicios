package com.Ferreteria.ms_clientes.service;


import com.Ferreteria.ms_clientes.dto.ClienteDTO;
import com.Ferreteria.ms_clientes.model.Cliente;
import com.Ferreteria.ms_clientes.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private static final Logger log =
            LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteDTO save(ClienteDTO dto) {

        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException(
                    "Ya existe un cliente con ese email");
        }

        Cliente cliente = Cliente.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .build();

        Cliente saved = clienteRepository.save(cliente);

        log.info("Cliente creado: {}", saved.getNombre());

        return convertirADTO(saved);
    }

    public List<ClienteDTO> getAll() {
        return clienteRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO getById(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cliente no encontrado"));

        return convertirADTO(cliente);
    }

    public ClienteDTO update(Long id, ClienteDTO dto) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cliente no encontrado"));

        cliente.setNombre(dto.getNombre());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());

        return convertirADTO(
                clienteRepository.save(cliente));
    }

    public boolean delete(Long id) {

        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        }

        return false;
    }

    private ClienteDTO convertirADTO(Cliente cliente) {

        return ClienteDTO.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .email(cliente.getEmail())
                .telefono(cliente.getTelefono())
                .direccion(cliente.getDireccion())
                .build();
    }
}
