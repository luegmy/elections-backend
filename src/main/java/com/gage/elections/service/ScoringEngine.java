package com.gage.elections.service;

import com.gage.elections.model.candidate.*;
import com.gage.elections.service.calculator.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoringEngine {

    private final JudicialScoreCalculator judicialCalculator;
    private final TransparencyScoreCalculator transparencyCalculator;
    private final ContributionScoreCalculator contributionCalculator;
    private final TrustScoreCalculator trustCalculator;
    private final PlanScoreCalculator planCalculator;


    public CompositeScore calculateAll(Candidate candidate) {

        CompositeScore score = new CompositeScore();

        score.setJudicialScore(this.getJudicialCalculator(candidate.getHistory()));
        score.setTransparencyScore(this.getTransparencyCalculator(candidate.getTransparency()));
        score.setContributionScore( this.getContributionCalculator(candidate.getAchievements()));
        score.setTrustScore(this.getTrustCalculator(candidate.getTrust()));
        score.setPlanScore(this.getPlanCalculator(candidate.getProposals()));

        candidate.setScores(score);
        recalculateFinalScore(candidate);

        return score;
    }

    public void recalculateFinalScore(Candidate candidate) {

        CompositeScore s = candidate.getScores();
        if (s == null) return;

        double p1Judicial = s.getJudicialScore();
        double p2Transparency = s.getTransparencyScore();
        double p3Contribution = s.getContributionScore();
        double p4Trust = s.getTrustScore();
        double p5Plan = s.getPlanScore();

        double finalScore =
                (p1Judicial * 0.40) +
                        (p5Plan * 0.20) +
                        (p2Transparency * 0.15) +
                        (p4Trust * 0.15) +
                        (p3Contribution * 0.10);

        // Penalizaciones estructurales
        if (p1Judicial < 50.0) {
            finalScore *= 0.5;
        }

        if (p5Plan < 40.0) {
            finalScore -= 10.0;
        }

        finalScore = Math.max(0.0, round2(finalScore));

        s.setFinalScore(finalScore);
    }

    public int determineRankingLevel(double finalScore) {
        if (finalScore >= 85) return 1; // Oro
        if (finalScore >= 65) return 2; // Plata
        if (finalScore >= 40) return 3; // Bronce
        return 4; // Rojo
    }

    public double getPlanCalculator(List<Proposal> proposals) {
        return planCalculator.calculate(proposals);
    }

    public double getTrustCalculator(Trust trust) {
        return trustCalculator.calculate(trust);
    }

    public double getJudicialCalculator(List<LegalHistoryEntry> history) {
        return judicialCalculator.calculate(history);
    }

    public double getTransparencyCalculator(Transparency transparency) {
        return transparencyCalculator.calculate(transparency);
    }

    public double getContributionCalculator(List<Achievement> achievements) {
        return Math.min((contributionCalculator.calculate(achievements)/50.0) * 100.00, 100.00);
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
