package com.example.rutasegura.dto;

import com.example.rutasegura.model.CategoriaRiesgo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReporteReq(
        @NotBlank String municipio,
        @NotBlank String puntoReferencia,
        @NotBlank String descripcion,
        @NotNull CategoriaRiesgo categoria
) {}