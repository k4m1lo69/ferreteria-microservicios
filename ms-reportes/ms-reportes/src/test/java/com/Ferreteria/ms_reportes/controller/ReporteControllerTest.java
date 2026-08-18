package com.Ferreteria.ms_reportes.controller;

import com.Ferreteria.ms_reportes.dto.ReporteDTO;
import com.Ferreteria.ms_reportes.service.ReporteService;
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

@WebMvcTest(ReporteController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ReporteController Tests")
class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReporteService reporteService;

    private ReporteDTO reporteDTO;

    @BeforeEach
    void setUp() {
        reporteDTO = ReporteDTO.builder()
                .tipo("VENTAS")
                .descripcion("Reporte mensual de ventas")
                .fechaGeneracion("2026-08-01T10:00:00")
                .estado("GENERADO")
                .build();
    }

    @Test
    @DisplayName("POST /api/reportes debe retornar 201 Created")
    void testCreateReporte() throws Exception {
        when(reporteService.save(any())).thenReturn(reporteDTO);

        mockMvc.perform(post("/api/reportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reporteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("VENTAS"));
    }

    @Test
    @DisplayName("GET /api/reportes debe retornar 200 OK con la lista")
    void testGetAll() throws Exception {
        when(reporteService.getAll()).thenReturn(List.of(reporteDTO));

        mockMvc.perform(get("/api/reportes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("GENERADO"));
    }

    @Test
    @DisplayName("GET /api/reportes/{id} debe retornar 200 OK")
    void testGetById() throws Exception {
        when(reporteService.getById(1L)).thenReturn(reporteDTO);

        mockMvc.perform(get("/api/reportes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").value("Reporte mensual de ventas"));
    }

    @Test
    @DisplayName("DELETE /api/reportes/{id} debe retornar 200 OK cuando existe")
    void testDelete() throws Exception {
        when(reporteService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/reportes/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/reportes/{id} inexistente debe retornar 404")
    void testDeleteNotFound() throws Exception {
        when(reporteService.delete(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/reportes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/reportes/tipo/{tipo} debe retornar 200 OK")
    void testGetByTipo() throws Exception {
        when(reporteService.getByTipo("VENTAS")).thenReturn(List.of(reporteDTO));

        mockMvc.perform(get("/api/reportes/tipo/VENTAS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("VENTAS"));
    }
}