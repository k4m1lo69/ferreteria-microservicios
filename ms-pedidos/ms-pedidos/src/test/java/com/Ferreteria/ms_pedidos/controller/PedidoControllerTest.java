package com.Ferreteria.ms_pedidos.controller;

import com.Ferreteria.ms_pedidos.dto.PedidoDTO;
import com.Ferreteria.ms_pedidos.service.PedidoService;
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

@WebMvcTest(PedidoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("PedidoController Tests")
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PedidoService pedidoService;

    private PedidoDTO pedidoDTO;

    @BeforeEach
    void setUp() {
        pedidoDTO = PedidoDTO.builder()
                .id(1L)
                .clienteId(1L)
                .productoId(1L)
                .cantidad(5)
                .precioUnitario(100.0)
                .total(500.0)
                .estado("CONFIRMADO")
                .build();
    }

    @Test
    @DisplayName("POST /api/pedidos debe retornar 201 Created")
    void testCreatePedido() throws Exception {
        when(pedidoService.save(any())).thenReturn(pedidoDTO);

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("CONFIRMADO"));
    }

    @Test
    @DisplayName("GET /api/pedidos/{id} debe retornar 200 OK")
    void testGetPedidoById() throws Exception {
        when(pedidoService.getById(1L)).thenReturn(pedidoDTO);

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(5));
    }

    @Test
    @DisplayName("PUT /api/pedidos/{id}/estado debe actualizar estado")
    void testUpdatePedidoEstado() throws Exception {
        pedidoDTO.setEstado("ENTREGADO");
        when(pedidoService.updateEstado(1L, "ENTREGADO")).thenReturn(pedidoDTO);

        mockMvc.perform(put("/api/pedidos/1/estado?estado=ENTREGADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ENTREGADO"));
    }
}