package com.Ferreteria.ms_clientes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DireccionDTO {

    private Long id;

    @NotBlank(message = "Calle obligatoria")
    private String calle;

    @NotBlank(message = "Ciudad obligatoria")
    private String ciudad;

    @NotBlank(message = "Region obligatoria")
    private String region;

    private String codigoPostal;

    private String tipo;
}