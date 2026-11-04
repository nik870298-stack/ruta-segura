package com.example.rutasegura.dto;

import com.example.rutasegura.model.Rol;

public record AuthRes(
        String token,
        String email,
        String nombre,
        Rol rol
) {}