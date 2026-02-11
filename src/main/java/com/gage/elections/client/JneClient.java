package com.gage.elections.client;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class JneClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String fetchCandidatos(int idTipoEleccion, String authToken, int userId) {
        int idProceso = 124; // EG 2026

        // Usamos el parámetro idTipoEleccion dinámicamente
        String jsonPayload = String.format(
                "{" +
                        "  \"oToken\": { \"AuthToken\": \"%s\", \"UserId\": %d }," +
                        "  \"oFiltro\": { \"idProcesoElectoral\": %d, \"strUbiDepartamento\": \"\", \"idTipoEleccion\": %d }" +
                        "}", authToken, userId, idProceso, idTipoEleccion);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://sije.jne.gob.pe/ServiciosWeb/WSCandidato/ListaCandidatos"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json, text/plain, */*")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36")
                .header("Origin", "https://votoinformado.jne.gob.pe")
                .header("Referer", "https://votoinformado.jne.gob.pe/")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                System.err.println("Error JNE: " + response.statusCode() + " - " + response.body());
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error crítico llamando al JNE", e);
        }
    }
}
