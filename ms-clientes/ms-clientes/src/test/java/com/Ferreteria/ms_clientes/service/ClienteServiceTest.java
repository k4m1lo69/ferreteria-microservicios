package com.Ferreteria.ms_clientes.service;

import com.Ferreteria.ms_clientes.dto.ClienteDTO;
import com.Ferreteria.ms_clientes.model.Cliente;
import com.Ferreteria.ms_clientes.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void save_debeGuardarClienteYRetornarDTO() {
        ClienteDTO dtoEntrada = ClienteDTO.builder()
                .nombre("Juan Perez")
                .email("juan@email.com")
                .telefono("912345678")
                .direccion("Calle 123")
                .build();

        Cliente clienteGuardado = Cliente.builder()
                .id(1L)
                .nombre("Juan Perez")
                .email("juan@email.com")
                .telefono("912345678")
                .direccion("Calle 123")
                .build();

        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(clienteGuardado);

        ClienteDTO resultado = clienteService.save(dtoEntrada);

        assertEquals(1L, resultado.getId());
        assertEquals("Juan Perez", resultado.getNombre());
        assertEquals("juan@email.com", resultado.getEmail());
    }

    @Test
    void getById_cuandoExiste_debeRetornarCliente() {
        Long id = 1L;
        Cliente cliente = Cliente.builder()
                .id(id)
                .nombre("Maria Lopez")
                .email("maria@email.com")
                .telefono("987654321")
                .direccion("Av. Principal 456")
                .build();

        when(clienteRepository.findById(id))
                .thenReturn(Optional.of(cliente));

        ClienteDTO resultado = clienteService.getById(id);

        assertEquals(id, resultado.getId());
        assertEquals("Maria Lopez", resultado.getNombre());
    }

    @Test
    void getById_cuandoNoExiste_debeLanzarExcepcion() {
        Long id = 99L;
        when(clienteRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            clienteService.getById(id);
        });
    }

    @Test
    void delete_cuandoExiste_debeEliminarYRetornarTrue() {
        Long id = 1L;
        when(clienteRepository.existsById(id)).thenReturn(true);

        boolean resultado = clienteService.delete(id);

        assertTrue(resultado);
        verify(clienteRepository, times(1)).deleteById(id);
    }

    @Test
    void delete_cuandoNoExiste_debeRetornarFalse() {
        Long id = 99L;
        when(clienteRepository.existsById(id)).thenReturn(false);

        boolean resultado = clienteService.delete(id);

        assertFalse(resultado);
        verify(clienteRepository, never()).deleteById(any());
    }
}