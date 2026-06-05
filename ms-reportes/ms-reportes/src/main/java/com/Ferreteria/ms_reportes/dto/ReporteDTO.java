package com.Ferreteria.ms_reportes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ReporteDTO {

    @NotBlank(message = "tipo obligatorio")
    private String tipo;

    @NotBlank(message = "descripcion pbligatoria")
    private String descripcion;

    private String fechaGeneracion;
    private String estado;
}
