package com.gage.elections.util;

import com.gage.elections.model.candidate.Candidate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Map;

public class ExtractorCandidatosJNE {

    public static void main(String[] args) {
        // Datos obtenidos de tu inspección de red
        String authToken = "1454eebb-4b05-4400-93ac-25f0d0690d4b";
        int userId = 1381;
        int idProceso = 124;

        // Construcción del JSON según el Payload que compartiste
        String jsonPayload = String.format(
                "{" +
                        "  \"oToken\": { \"AuthToken\": \"%s\", \"UserId\": %d }," +
                        "  \"oFiltro\": { \"idProcesoElectoral\": %d, \"strUbiDepartamento\": \"\", \"idTipoEleccion\": 1 }" +
                        "}", authToken, userId, idProceso);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://sije.jne.gob.pe/ServiciosWeb/WSCandidato/ListaCandidatos"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json, text/plain, */*")
                // El User-Agent es crítico para evitar bloqueos del firewall Imperva
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36")
                .header("Origin", "https://votoinformado.jne.gob.pe")
                .header("Referer", "https://votoinformado.jne.gob.pe/")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        try {
            System.out.println("Enviando petición al JNE...");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("--- Datos Recibidos con Éxito ---");
                System.out.println(response.body());
                // Aquí podrías usar una librería como Jackson o Gson para procesar el JSON
            } else {
                System.err.println("Error del servidor: " + response.statusCode());
                System.err.println("Respuesta: " + response.body());
                System.out.println("\nNota: Es posible que el AuthToken haya expirado o necesites incluir Cookies.");
            }
        } catch (Exception e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
    }
    public Candidate mapToEntity(Map<String, Object> jneJson) {
        Candidate candidate = new Candidate();

        // Mapeo de Identidad
        candidate.setCode((String) jneJson.get("strDocumentoIdentidad"));

        // Unimos los nombres para tu campo 'name'
        String fullName = String.format("%s %s %s",
                jneJson.get("strNombres"),
                jneJson.get("strApellidoPaterno"),
                jneJson.get("strApellidoMaterno"));
        candidate.setName(fullName);

        // Mapeo de Posición y Partido
        candidate.setPosition((String) jneJson.get("strCargo"));
        candidate.setParty((String) jneJson.get("strOrganizacionPolitica"));

        // Foto: El JNE usa un servicio de imágenes. La URL se construye así:
        String fotoGuid = (String) jneJson.get("strGuidFoto");
        candidate.setPhoto("https://votoinformado.jne.gob.pe/voto/Resources/Imagenes/Candidato/" + fotoGuid);

        // Metadatos de auditoría
        candidate.setDataSourceVersion("JNE_EG_2026");
        candidate.setLastAuditDate(LocalDateTime.now());

        return candidate;
    }
}
