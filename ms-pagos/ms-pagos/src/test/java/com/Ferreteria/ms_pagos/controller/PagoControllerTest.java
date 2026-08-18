package com.Ferreteria.ms_pagos.controller;

import com.Ferreteria.ms_pagos.dto.PagoDTO;
import com.Ferreteria.ms_pagos.service.PagoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("PagoController Tests")
class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PagoService pagoService;

    private PagoDTO pagoDTO;

    @BeforeEach
    void setUp() {
        pagoDTO = PagoDTO.builder()
                .id(1L)
                .pedidoId(1L)
                .monto(500.0)
                .metodoPago("TARJETA")
                .estado("PENDIENTE")
                .build();
    }

    @Test
    @DisplayName("POST /api/pagos debe retornar 201 Created")
    void testCreatePago() throws Exception {
        when(pagoService.save(any())).thenReturn(pagoDTO);

        mockMvc.perform(post("/api/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.metodoPago").value("TARJETA"));
    }

    @Test
    @DisplayName("GET /api/pagos debe retornar 200 OK con la lista")
    void testGetAll() throws Exception {
        when(pagoService.getAll()).thenReturn(List.of(pagoDTO));

        mockMvc.perform(get("/api/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pedidoId").value(1));
    }

    @Test
    @DisplayName("GET /api/pagos/{id} debe retornar 200 OK")
    void testGetById() throws Exception {
        when(pagoService.getById(1L)).thenReturn(pagoDTO);

        mockMvc.perform(get("/api/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monto").value(500.0));
    }

    @Test
    @DisplayName("GET /api/pagos/{id} inexistente debe retornar 404")
    void testGetByIdNotFound() throws Exception {
        when(pagoService.getById(99L))
                .thenThrow(new RuntimeException("Pago no encontrado"));

        mockMvc.perform(get("/api/pagos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/pagos/pedido/{pedidoId} debe retornar 200 OK")
    void testGetByPedido() throws Exception {
        when(pagoService.getByPedidoId(1L)).thenReturn(List.of(pagoDTO));

        mockMvc.perform(get("/api/pagos/pedido/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pedidoId").value(1));
    }

    @Test
    @DisplayName("PUT /api/pagos/{id}/estado debe retornar 200 OK")
    void testUpdateEstado() throws Exception {
        pagoDTO.setEstado("PAGADO");
        when(pagoService.updateEstado(1L, "PAGADO")).thenReturn(pagoDTO);

        mockMvc.perform(put("/api/pagos/1/estado?estado=PAGADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("PAGADO"));
    }
}