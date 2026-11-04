package com.example.rutasegura.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthReq(
        @NotBlank @Email String email,
        @NotBlank String password
) {}