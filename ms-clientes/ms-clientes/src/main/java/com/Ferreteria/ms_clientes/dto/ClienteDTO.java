package com.Ferreteria.ms_clientes.dto;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {

    private Long id;

    @NotBlank(message = "Nombre obligatorio")
    private String nombre;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email obligatorio")
    private String email;

    @NotBlank(message = "Teléfono obligatorio")
    private String telefono;

    @NotBlank(message = "Dirección obligatoria")
    private String direccion;

}