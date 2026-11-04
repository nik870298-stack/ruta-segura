package com.example.rutasegura.dto;

import com.example.rutasegura.model.EstadoReporte;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoReq(
        @NotNull EstadoReporte nuevoEstado
) {}