package com.Ferreteria.ms_inventarios.controller;

import com.Ferreteria.ms_inventarios.dto.InventarioDTO;
import com.Ferreteria.ms_inventarios.service.InventarioService;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventarioController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("InventarioController Tests")
class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InventarioService inventarioService;

    private InventarioDTO inventarioDTO;

    @BeforeEach
    void setUp() {
        inventarioDTO = InventarioDTO.builder()
                .id(1L)
                .productoId(1L)
                .cantidad(100)
                .cantidadMinima(5)
                .ubicacion("Pasillo A")
                .build();
    }

    @Test
    @DisplayName("POST /api/inventarios debe retornar 201 Created")
    void testCreateInventario() throws Exception {
        when(inventarioService.save(any())).thenReturn(inventarioDTO);

        mockMvc.perform(post("/api/inventarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventarioDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cantidad").value(100));
    }

    @Test
    @DisplayName("GET /api/inventarios debe retornar 200 OK con la lista")
    void testGetAll() throws Exception {
        when(inventarioService.getAll()).thenReturn(List.of(inventarioDTO));

        mockMvc.perform(get("/api/inventarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productoId").value(1));
    }

    @Test
    @DisplayName("GET /api/inventarios/{id} debe retornar 200 OK")
    void testGetById() throws Exception {
        when(inventarioService.getById(1L)).thenReturn(inventarioDTO);

        mockMvc.perform(get("/api/inventarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ubicacion").value("Pasillo A"));
    }

    @Test
    @DisplayName("PUT /api/inventarios/{id} debe retornar 200 OK")
    void testUpdate() throws Exception {
        when(inventarioService.update(any(), any())).thenReturn(inventarioDTO);

        mockMvc.perform(put("/api/inventarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventarioDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/inventarios/{id} debe retornar 200 OK cuando existe")
    void testDeleteExistente() throws Exception {
        when(inventarioService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/inventarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/inventarios/{id} debe retornar 404 cuando no existe")
    void testDeleteInexistente() throws Exception {
        when(inventarioService.delete(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/inventarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/inventarios/producto/{productoId} debe retornar 200 OK")
    void testGetByProductoId() throws Exception {
        when(inventarioService.getByProductoId(1L)).thenReturn(inventarioDTO);

        mockMvc.perform(get("/api/inventarios/producto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productoId").value(1));
    }

    @Test
    @DisplayName("PATCH /api/inventarios/producto/{productoId}/descontar debe retornar 200 OK")
    void testDescontarStock() throws Exception {
        InventarioDTO descontado = InventarioDTO.builder()
                .id(1L).productoId(1L).cantidad(95).cantidadMinima(5).ubicacion("Pasillo A")
                .build();

        when(inventarioService.descontarStock(1L, 5)).thenReturn(descontado);

        mockMvc.perform(patch("/api/inventarios/producto/1/descontar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("cantidad", 5))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(95));
    }

    @Test
    @DisplayName("PATCH descontar con stock insuficiente debe retornar 409")
    void testDescontarStockInsuficiente() throws Exception {
        when(inventarioService.descontarStock(anyLong(), any()))
                .thenThrow(new RuntimeException("Stock insuficiente. Disponible: 2, solicitado: 10"));

        mockMvc.perform(patch("/api/inventarios/producto/1/descontar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("cantidad", 10))))
                .andExpect(status().isConflict());
    }
}