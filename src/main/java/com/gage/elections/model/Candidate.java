package com.gage.elections.model;

import com.gage.elections.util.ScoringRulesUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Getter
@Setter
@Document(collection = "candidates")
public class Candidate {
    @MongoId
    private long code;
    private String name;
    private String position; // Cargo al que postula (Presidente, Congresista, etc.)
    private Party partyInfo; // Referencia al objeto del Partido (Nueva Clase)
    private String biography;

    @TextIndexed
    private List<Proposal> proposals;
    private List<LegalHistoryEntry> history;
    private List<Achievement> achievements; // Logros, leyes, etc. (para Pilar 3)
    private Transparency transparency;
    private Trust trust;
    private CompositeScore scores;
    private int rankingLevel; // Para guardar el resultado del IRQ/Ranking
    @TextIndexed
    private List<String> planKeywords;

    public void recalculateScore() {
        int judicialScore = ScoringRulesUtils.calculateJudicialScore(history);
        int contributionScore = ScoringRulesUtils.calculateContributionScore(achievements);
        int transparencyScore = ScoringRulesUtils.calculateTransparencyScore(transparency);
        int trustScore = ScoringRulesUtils.calculateTrustScore(trust);

        this.scores = new CompositeScore(judicialScore, contributionScore, transparencyScore, trustScore, 0);

        // Asignar rankingLevel según el finalScore
        this.rankingLevel = determineRankingLevel(this.scores.getFinalScore());
    }

    private static int determineRankingLevel(int finalScore) {
        // Supongamos finalScore en 0..100
        if (finalScore >= 85) return 5;   // Excelente / TOP
        if (finalScore >= 70) return 4;   // Muy bueno
        if (finalScore >= 55) return 3;   // Bueno
        if (finalScore >= 40) return 2;   // Regular
        return 1;                          // Bajo
    }

}
