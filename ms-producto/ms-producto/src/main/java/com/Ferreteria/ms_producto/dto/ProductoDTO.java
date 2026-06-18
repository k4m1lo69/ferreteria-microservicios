package com.Ferreteria.ms_producto.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {

    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @NotNull(message = "El precio no puede ser nulo")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Double precio;

    @NotBlank(message = "La categoría no puede estar vacía")
    private String categoria;

    @NotBlank(message = "La marca no puede estar vacía")
    private String marca;
}