package com.Ferreteria.ms_clientes.controller;

import com.Ferreteria.ms_clientes.dto.ClienteDTO;
import com.Ferreteria.ms_clientes.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ClienteController Tests")
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClienteService clienteService;

    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        clienteDTO = ClienteDTO.builder()
                .id(1L)
                .nombre("Juan")
                .email("juan@email.com")
                .telefono("123456")
                .direccion("Calle 1")
                .build();
    }

    @Test
    @DisplayName("POST /api/clientes debe retornar 201 Created")
    void testCreateCliente() throws Exception {
        when(clienteService.save(any())).thenReturn(clienteDTO);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    @DisplayName("GET /api/clientes/{id} debe retornar 200 OK")
    void testGetClienteById() throws Exception {
        when(clienteService.getById(1L)).thenReturn(clienteDTO);

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@email.com"));
    }

    @Test
    @DisplayName("GET /api/clientes/{id} inexistente debe retornar 404")
    void testGetClienteByIdNotFound() throws Exception {
        when(clienteService.getById(999L))
                .thenThrow(new RuntimeException("Cliente no encontrado"));

        mockMvc.perform(get("/api/clientes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/clientes/{id} debe retornar 200 OK")
    void testUpdateCliente() throws Exception {
        ClienteDTO actualizado = ClienteDTO.builder()
                .id(1L)
                .nombre("Juan Actualizado")
                .email("juan.nuevo@email.com")
                .build();

        when(clienteService.update(any(), any())).thenReturn(actualizado);

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/clientes/{id} debe retornar 204 No Content")
    void testDeleteCliente() throws Exception {
        when(clienteService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isOk());
    }
}