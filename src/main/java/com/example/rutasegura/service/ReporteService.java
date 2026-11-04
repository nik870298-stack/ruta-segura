package com.example.rutasegura.service;

import com.example.rutasegura.dto.ReporteReq;
import com.example.rutasegura.dto.ReporteRes;
import com.example.rutasegura.model.CategoriaRiesgo;
import com.example.rutasegura.model.EstadoReporte;
import com.example.rutasegura.model.Reporte;
import com.example.rutasegura.model.Usuario;
import com.example.rutasegura.repository.ReporteRepository;
import com.example.rutasegura.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final UsuarioRepository usuarioRepository;
    private final GeocodingService geocodingService;

    public ReporteService(ReporteRepository reporteRepository,
                          UsuarioRepository usuarioRepository,
                          GeocodingService geocodingService) {
        this.reporteRepository = reporteRepository;
        this.usuarioRepository = usuarioRepository;
        this.geocodingService = geocodingService;
    }

    public ReporteRes crearReporte(ReporteReq req, String userEmail) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Reporte reporte = new Reporte(
                req.municipio(),
                req.puntoReferencia(),
                req.descripcion(),
                req.categoria(),
                usuario
        );

        double[] coords = geocodingService.geocodificar(req.municipio(), req.puntoReferencia());
        reporte.setLatitud(coords[0]);
        reporte.setLongitud(coords[1]);

        Reporte guardado = reporteRepository.save(reporte);
        return mapToResponse(guardado, null);
    }

    public List<ReporteRes> obtenerReportesFiltradosYOrdenados(
            CategoriaRiesgo categoria,
            String municipio,
            Double userLat,
            Double userLon) {

        List<Reporte> reportes;

        if (categoria != null && municipio != null) {
            reportes = reporteRepository.findByMunicipioIgnoreCaseAndCategoria(municipio, categoria);
        } else if (categoria != null) {
            reportes = reporteRepository.findByCategoria(categoria);
        } else if (municipio != null) {
            reportes = reporteRepository.findByMunicipioIgnoreCase(municipio);
        } else {
            reportes = reporteRepository.findAll();
        }

        return reportes.stream()
                .map(r -> {
                    Double distancia = null;
                    if (userLat != null && userLon != null && r.getLatitud() != null && r.getLongitud() != null) {
                        distancia = geocodingService.calcularDistanciaKm(userLat, userLon, r.getLatitud(), r.getLongitud());
                    }
                    return mapToResponse(r, distancia);
                })
                .sorted(Comparator.comparing(ReporteRes::distanciaKm, Comparator.nullsLast(Double::compareTo)))
                .toList();
    }

    public ReporteRes cambiarEstadoReporte(Long id, EstadoReporte nuevoEstado) {
        Reporte r = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con ID: " + id));
        r.setEstado(nuevoEstado);
        return mapToResponse(reporteRepository.save(r), null);
    }

    public void eliminarReporte(Long id) {
        if (!reporteRepository.existsById(id)) {
            throw new RuntimeException("Reporte no encontrado con ID: " + id);
        }
        reporteRepository.deleteById(id);
    }

    private ReporteRes mapToResponse(Reporte r, Double distancia) {
        return new ReporteRes(
                r.getId(),
                r.getMunicipio(),
                r.getPuntoReferencia(),
                r.getDescripcion(),
                r.getCategoria(),
                r.getEstado(),
                r.getLatitud(),
                r.getLongitud(),
                r.getFechaReporte(),
                r.getUsuario().getNombre(),
                distancia
        );
    }
}