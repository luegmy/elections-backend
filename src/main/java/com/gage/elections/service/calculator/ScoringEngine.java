package com.gage.elections.service.calculator;

import com.gage.elections.model.candidate.*;
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

        CompositeScore score = (candidate.getScores() != null)
                ? candidate.getScores()
                : new CompositeScore();

        score.setJudicialScore(this.getJudicialCalculator(candidate.getHistory()));
        score.setTransparencyScore(this.getTransparencyCalculator(candidate.getTransparency()));
        score.setContributionScore( this.getContributionCalculator(candidate.getAchievements()));
        score.setTrustScore(this.getTrustCalculator(candidate.getTrust()));
        score.setPlanScore(this.getPlanCalculator(candidate.getProposals()));

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

        double finalScore = (p1Judicial * 0.40) +
                            (p5Plan * 0.20) +
                            (p2Transparency * 0.15) +
                            (p4Trust * 0.15) +
                            (p3Contribution * 0.10);

        // Penalizaciones estructurales
        if (p1Judicial < 50.0) finalScore *= 0.5;

        if (p5Plan < 40.0) finalScore -= 10.0;

        s.setFinalScore(Math.max(0.0, round2(finalScore)));
    }

    public int determineRankingLevel(double finalScore) {
        if (finalScore >= 85) return 1;
        if (finalScore >= 65) return 2;
        if (finalScore >= 40) return 3;
        return 4;
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
