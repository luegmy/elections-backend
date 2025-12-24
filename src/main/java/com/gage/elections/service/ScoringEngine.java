package com.gage.elections.service;

import com.gage.elections.model.candidate.Candidate;
import com.gage.elections.model.candidate.CompositeScore;
import com.gage.elections.service.calculator.ContributionScoreCalculator;
import com.gage.elections.service.calculator.JudicialScoreCalculator;
import com.gage.elections.service.calculator.TransparencyScoreCalculator;
import com.gage.elections.service.calculator.TrustScoreCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoringEngine {

    private final JudicialScoreCalculator judicialCalculator;
    private final TransparencyScoreCalculator transparencyCalculator;
    private final ContributionScoreCalculator contributionCalculator;
    private final TrustScoreCalculator trustCalculator;

    /**
     * Orquesta el cálculo integral del candidato.
     * Pilar 1: Judicial (40%)
     * Pilar 2: Transparencia (25%)
     * Pilar 3: Contribución/Trayectoria (15%)
     * Pilar 4: Veracidad/Confianza (20%)
     */
    public CompositeScore calculateAll(Candidate c) {
        // Ejecución de calculadores modulares
        double p1Judicial = judicialCalculator.calculate(c.getHistory());
        double p2Transparency = transparencyCalculator.calculate(c.getTransparency());
        double p3Contribution = contributionCalculator.calculate(c.getAchievements());
        double p4Trust = trustCalculator.calculate(c.getTrust());

        // Cálculo del promedio ponderado según pesos establecidos
        double finalScore = (p1Judicial * 0.40) +
                (p2Transparency * 0.25) +
                (p3Contribution * 0.15) +
                (p4Trust * 0.20);

        // Redondeo a 2 decimales para presentación profesional
        finalScore = Math.round(finalScore * 100.0) / 100.0;

        return new CompositeScore(
                p1Judicial,
                p2Transparency,
                p3Contribution,
                p4Trust,
                finalScore
        );
    }

    /**
     * Determina el nivel de ranking basado en el score final.
     * Útil para visualización en la API (Semáforo electoral).
     */
    public int determineRankingLevel(double finalScore) {
        if (finalScore >= 85) return 1; // Sobresaliente (Verde)
        if (finalScore >= 65) return 2; // Aceptable (Amarillo)
        if (finalScore >= 40) return 3; // En Riesgo (Naranja)
        return 4;                       // Crítico (Rojo)
    }
}

