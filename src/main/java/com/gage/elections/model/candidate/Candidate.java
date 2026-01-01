package com.gage.elections.model.candidate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "candidates")
public class Candidate {
    @MongoId
    private String code;
    private String name;
    private String position; // Cargo al que postula (Presidente, Congresista, etc.)
    private String party;
    private String partyAcronym;
    private String biography;
    private List<LegalHistoryEntry> history;
    private List<Achievement> achievements; // Logros, leyes, etc. (para Pilar 3)
    private Transparency transparency;
    private Trust trust;
    private CompositeScore scores;
    private int rankingLevel; // Para guardar el resultado del IRQ/Ranking
    private List<Proposal> proposals;
    private List<String> planKeywords;

    private LocalDateTime lastAuditDate;
    private String dataSourceVersion; // Ej: "JNE-V1-2025"

    public void updateScoring(CompositeScore scores, int rankingLevel) {
        this.scores = scores;
        this.rankingLevel = rankingLevel;
        this.lastAuditDate = LocalDateTime.now();
    }
}
