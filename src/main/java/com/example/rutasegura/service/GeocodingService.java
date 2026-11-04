package com.example.rutasegura.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class GeocodingService {

    private final RestTemplate restTemplate;

    public GeocodingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public double[] geocodificar(String municipio, String puntoReferencia) {
        String query = puntoReferencia + ", " + municipio + ", Cundinamarca, Colombia";
        String url = UriComponentsBuilder.fromHttpUrl("https://nominatim.openstreetmap.org/search")
                .queryParam("q", query)
                .queryParam("format", "json")
                .queryParam("limit", 1)
                .toUriString();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "RutaSeguraBackend/1.0");
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Object[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Object[].class
            );

            Object[] array = response.getBody();
            if (array != null && array.length > 0) {
                @SuppressWarnings("unchecked")
                Map<String, Object> location = (Map<String, Object>) array[0];
                double lat = Double.parseDouble(location.get("lat").toString());
                double lon = Double.parseDouble(location.get("lon").toString());
                return new double[]{lat, lon};
            }
        } catch (Exception ignored) {}

        return new double[]{4.6097, -74.0817};
    }

    public double calcularDistanciaKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return Math.round(R * c * 100.0) / 100.0;
    }
}