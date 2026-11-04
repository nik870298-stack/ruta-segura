package com.example.rutasegura.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String municipio;

    @Column(nullable = false)
    private String puntoReferencia;

    @Column(nullable = false)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaRiesgo categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReporte estado;

    private Double latitud;
    private Double longitud;

    @Column(nullable = false)
    private LocalDateTime fechaReporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Reporte() {}

    public Reporte(String municipio, String puntoReferencia, String descripcion, CategoriaRiesgo categoria, Usuario usuario) {
        this.municipio = municipio;
        this.puntoReferencia = puntoReferencia;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.usuario = usuario;
        this.estado = EstadoReporte.PENDIENTE;
        this.fechaReporte = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }
    public String getPuntoReferencia() { return puntoReferencia; }
    public void setPuntoReferencia(String puntoReferencia) { this.puntoReferencia = puntoReferencia; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public CategoriaRiesgo getCategoria() { return categoria; }
    public void setCategoria(CategoriaRiesgo categoria) { this.categoria = categoria; }
    public EstadoReporte getEstado() { return estado; }
    public void setEstado(EstadoReporte estado) { this.estado = estado; }
    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
    public LocalDateTime getFechaReporte() { return fechaReporte; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}