package com.example.rutasegura.dto;

import com.example.rutasegura.model.Rol;
import jakarta.validation.constraints.NotNull;

public record CambiarRolReq(
        @NotNull Rol nuevoRol
) {}