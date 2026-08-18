package com.Ferreteria.ms_producto.controller;

import com.Ferreteria.ms_producto.dto.ProductoDTO;
import com.Ferreteria.ms_producto.service.ProductoService;
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

@WebMvcTest(ProductoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ProductoController Tests")
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoService productoService;

    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        productoDTO = ProductoDTO.builder()
                .id(1L)
                .nombre("Martillo")
                .descripcion("Martillo de acero")
                .precio(9990.0)
                .sku("MART-001")
                .categoria("Herramientas")
                .marca("Stanley")
                .build();
    }

    @Test
    @DisplayName("POST /api/productos debe retornar 201 Created")
    void testCreateProducto() throws Exception {
        when(productoService.save(any())).thenReturn(productoDTO);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Martillo"));
    }

    @Test
    @DisplayName("GET /api/productos debe retornar 200 OK con la lista")
    void testGetAll() throws Exception {
        when(productoService.getAll()).thenReturn(List.of(productoDTO));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marca").value("Stanley"));
    }

    @Test
    @DisplayName("GET /api/productos/{id} debe retornar 200 OK")
    void testGetById() throws Exception {
        when(productoService.getById(1L)).thenReturn(productoDTO);

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.precio").value(9990.0));
    }

    @Test
    @DisplayName("GET /api/productos/{id} inexistente debe retornar 404")
    void testGetByIdNotFound() throws Exception {
        when(productoService.getById(99L))
                .thenThrow(new RuntimeException("Producto no encontrado"));

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/productos/{id} debe retornar 200 OK")
    void testUpdate() throws Exception {
        when(productoService.update(any(), any())).thenReturn(productoDTO);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/productos/{id} debe retornar 200 OK cuando existe")
    void testDelete() throws Exception {
        when(productoService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/productos/categoria/{categoria} debe retornar 200 OK")
    void testGetByCategoria() throws Exception {
        when(productoService.getByCategoria("Herramientas")).thenReturn(List.of(productoDTO));

        mockMvc.perform(get("/api/productos/categoria/Herramientas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoria").value("Herramientas"));
    }
}