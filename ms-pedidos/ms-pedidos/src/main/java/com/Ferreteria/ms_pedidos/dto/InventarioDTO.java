package com.Ferreteria.ms_pedidos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventarioDTO {
    private Long id;
    private Long productoId;
    private Integer cantidad;
    private Integer cantidadMinima;
    private String ubicacion;
}
