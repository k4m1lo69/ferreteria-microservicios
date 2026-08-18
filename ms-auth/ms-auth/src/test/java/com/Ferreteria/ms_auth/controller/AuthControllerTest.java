package com.Ferreteria.ms_auth.controller;

import com.Ferreteria.ms_auth.dto.AuthResponse;
import com.Ferreteria.ms_auth.dto.LoginRequest;
import com.Ferreteria.ms_auth.dto.RegisterRequest;
import com.Ferreteria.ms_auth.service.AuthService;
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

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setUsername("jperez");
        loginRequest.setPassword("clave123");

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("jperez");
        registerRequest.setPassword("clave123");
        registerRequest.setRol("ADMIN");

        authResponse = AuthResponse.builder()
                .token("token-de-prueba")
                .username("jperez")
                .rol("ADMIN")
                .build();
    }

    @Test
    @DisplayName("POST /api/auth/login debe retornar 200 OK con token")
    void testLogin() throws Exception {
        when(authService.login(any())).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-de-prueba"))
                .andExpect(jsonPath("$.username").value("jperez"));
    }

    @Test
    @DisplayName("POST /api/auth/login con credenciales invalidas debe retornar 400")
    void testLoginCredencialesInvalidas() throws Exception {
        when(authService.login(any()))
                .thenThrow(new RuntimeException("Contraseña incorrecta"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/register debe retornar 201 Created")
    void testRegister() throws Exception {
        when(authService.register(any())).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Usuario registrado exitosamente"));
    }

    @Test
    @DisplayName("POST /api/auth/register con usuario existente debe retornar 400")
    void testRegisterUsuarioExistente() throws Exception {
        when(authService.register(any()))
                .thenThrow(new RuntimeException("El usuario ya existe"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/auth/usuarios debe retornar 200 OK con la lista")
    void testGetAllUsuarios() throws Exception {
        when(authService.getAll()).thenReturn(List.of(authResponse));

        mockMvc.perform(get("/api/auth/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("jperez"));
    }
}