package com.Ferreteria.ms_pagos.service;

import com.Ferreteria.ms_pagos.dto.PagoDTO;
import com.Ferreteria.ms_pagos.model.Pago;
import com.Ferreteria.ms_pagos.repository.PagoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

    @Test
    void save_debeAsignarEstadoPendienteAlCrear() {
        PagoDTO dtoEntrada = PagoDTO.builder()
                .pedidoId(1L)
                .monto(15000.0)
                .metodoPago("TRANSFERENCIA")
                .build();

        Pago pagoGuardado = Pago.builder()
                .id(1L)
                .pedidoId(1L)
                .monto(15000.0)
                .metodoPago("TRANSFERENCIA")
                .estado("PENDIENTE")
                .fecha(LocalDateTime.now())
                .build();

        when(pagoRepository.save(any(Pago.class)))
                .thenReturn(pagoGuardado);

        PagoDTO resultado = pagoService.save(dtoEntrada);

        assertEquals("PENDIENTE", resultado.getEstado());
        assertEquals(15000.0, resultado.getMonto());
        assertEquals("TRANSFERENCIA", resultado.getMetodoPago());
    }

    @Test
    void getById_cuandoExiste_debeRetornarPago() {
        Long id = 1L;
        Pago pago = Pago.builder()
                .id(id)
                .pedidoId(1L)
                .monto(20000.0)
                .metodoPago("EFECTIVO")
                .estado("PENDIENTE")
                .fecha(LocalDateTime.now())
                .build();

        when(pagoRepository.findById(id))
                .thenReturn(Optional.of(pago));

        PagoDTO resultado = pagoService.getById(id);

        assertEquals(id, resultado.getId());
        assertEquals(20000.0, resultado.getMonto());
    }

    @Test
    void getById_cuandoNoExiste_debeLanzarExcepcion() {
        Long id = 99L;
        when(pagoRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            pagoService.getById(id);
        });
    }

    @Test
    void updateEstado_cuandoExiste_debeActualizarEstado() {
        Long id = 1L;
        Pago pago = Pago.builder()
                .id(id)
                .pedidoId(1L)
                .monto(15000.0)
                .metodoPago("TRANSFERENCIA")
                .estado("PENDIENTE")
                .fecha(LocalDateTime.now())
                .build();

        Pago pagoActualizado = Pago.builder()
                .id(id)
                .pedidoId(1L)
                .monto(15000.0)
                .metodoPago("TRANSFERENCIA")
                .estado("PAGADO")
                .fecha(LocalDateTime.now())
                .build();

        when(pagoRepository.findById(id)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoActualizado);

        PagoDTO resultado = pagoService.updateEstado(id, "PAGADO");

        assertEquals("PAGADO", resultado.getEstado());
    }

    @Test
    void updateEstado_cuandoNoExiste_debeLanzarExcepcion() {
        Long id = 99L;
        when(pagoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            pagoService.updateEstado(id, "PAGADO");
        });
    }
}