package com.example.rutasegura.repository;

import com.example.rutasegura.model.CategoriaRiesgo;
import com.example.rutasegura.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByCategoria(CategoriaRiesgo categoria);
    List<Reporte> findByMunicipioIgnoreCase(String municipio);
    List<Reporte> findByMunicipioIgnoreCaseAndCategoria(String municipio, CategoriaRiesgo categoria);
}