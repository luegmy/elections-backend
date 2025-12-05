package com.gage.elections.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "parties")
public class Party {
    @Id
    private String acronym;        // Siglas como ID único (ej: AP, FP)
    private String name;           // Nombre Oficial completo
    private String registrationId; // ID oficial del JNE para el partido
    private String logoUrl;        // URL del logo para el frontend
    private String ideology;       // Eje ideológico (ej: Socialdemócrata, Liberal)
    private String founder;        // Fundador o líder histórico
    private List<String> currentLeaders; // Directiva actual
    private List<LegalHistoryEntry> partyHistory; // Historial judicial del partido (multas, investigaciones, etc.)
    private CompositeScore partyScores; // Score de Coherencia y Transparencia del partido
}
