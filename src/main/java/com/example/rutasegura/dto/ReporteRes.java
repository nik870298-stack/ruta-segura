package com.example.rutasegura.dto;

import com.example.rutasegura.model.CategoriaRiesgo;
import com.example.rutasegura.model.EstadoReporte;
import java.time.LocalDateTime;

public record ReporteRes(
        Long id,
        String municipio,
        String puntoReferencia,
        String descripcion,
        CategoriaRiesgo categoria,
        EstadoReporte estado,
        Double latitud,
        Double longitud,
        LocalDateTime fechaReporte,
        String usuarioNombre,
        Double distanciaKm
) {}