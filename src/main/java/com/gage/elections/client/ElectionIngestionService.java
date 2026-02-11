package com.gage.elections.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gage.elections.model.candidate.Candidate;
import com.gage.elections.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ElectionIngestionService {

    private final JneClient jneClient;

    private final CandidateRepository candidateRepository;

    private final ObjectMapper objectMapper;

    @Value("${jne.api.auth-token}")
    private String authToken;

    @Value("${jne.api.user-id}")
    private int userId;

    public void sincronizarTodo() {
        // 1: Presidencial, 20: Senadores
        int[] tiposEleccion = {1, 20};

        for (int tipo : tiposEleccion) {
            try {
                String jsonResponse = jneClient.fetchCandidatos(tipo, authToken, userId);
                if (jsonResponse != null) {
                    processJsonResponse(jsonResponse);
                }
            } catch (Exception e) {
                System.err.println("Error procesando tipo " + tipo + ": " + e.getMessage());
            }
        }
    }

    private void processJsonResponse(String json) throws Exception {
        JsonNode root = objectMapper.readTree(json);
        // El JNE devuelve la lista dentro de "data"
        JsonNode lista = root.get("data");

        if (lista != null && lista.isArray()) {
            for (JsonNode node : lista) {
                Candidate candidate = mapToCandidate(node);
                // Upsert: Si el DNI existe, actualiza; si no, crea.
                candidateRepository.save(candidate);
            }
        }
    }

    private Candidate mapToCandidate(JsonNode node) {
        Candidate c = new Candidate();
        // Usamos el DNI como ID único en tu DB
        c.setCode(node.get("strDocumentoIdentidad").asText());

        String nombreCompleto = String.format("%s %s %s",
                node.get("strNombres").asText(),
                node.get("strApellidoPaterno").asText(),
                node.get("strApellidoMaterno").asText());

        c.setName(nombreCompleto);
        c.setPosition(node.get("strCargo").asText());
        c.setParty(node.get("strOrganizacionPolitica").asText());
        c.setPhoto("https://votoinformado.jne.gob.pe/voto/Resources/Imagenes/Candidato/" + node.get("strGuidFoto").asText());

        c.setDataSourceVersion("JNE_EG_2026_OFFICIAL");
        c.setLastAuditDate(LocalDateTime.now());

        return c;
    }
}
