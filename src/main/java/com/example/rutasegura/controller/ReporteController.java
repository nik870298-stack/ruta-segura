package com.example.rutasegura.controller;

import com.example.rutasegura.dto.CambiarEstadoReq;
import com.example.rutasegura.dto.ReporteReq;
import com.example.rutasegura.dto.ReporteRes;
import com.example.rutasegura.model.CategoriaRiesgo;
import com.example.rutasegura.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes de Riesgo", description = "Gestión, consulta y moderación de puntos de riesgo")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @PostMapping
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Crear nuevo reporte de riesgo (Usuarias autenticadas)")
    public ResponseEntity<ReporteRes> crearReporte(
            @Valid @RequestBody ReporteReq req,
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(reporteService.crearReporte(req, email));
    }

    @GetMapping
    @Operation(summary = "Listar reportes filtrados por categoría/municipio y ordenados por cercanía")
    public ResponseEntity<List<ReporteRes>> listarReportes(
            @RequestParam(required = false) CategoriaRiesgo categoria,
            @RequestParam(required = false) String municipio,
            @RequestParam(required = false) Double userLat,
            @RequestParam(required = false) Double userLon) {
        return ResponseEntity.ok(reporteService.obtenerReportesFiltradosYOrdenados(categoria, municipio, userLat, userLon));
    }

    @PatchMapping("/{id}/estado")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Cambiar estado del reporte (Moderadores y Administradores)")
    public ResponseEntity<ReporteRes> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoReq req) {
        return ResponseEntity.ok(reporteService.cambiarEstadoReporte(id, req.nuevoEstado()));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Eliminar un reporte inapropiado o falso (Moderadores y Administradores)")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }
}