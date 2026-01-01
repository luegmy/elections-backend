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

    public CompositeScore calculateAll(Candidate c) {

        double p1Judicial = judicialCalculator.calculate(c.getHistory());
        double p2Transparency = transparencyCalculator.calculate(c.getTransparency());
        double p3Contribution = contributionCalculator.calculate(c.getAchievements());
        double p4Trust = trustCalculator.calculate(c.getTrust());

        double p3normalized = (p3Contribution / 50.0) * 100.0;

        double finalScore = (p1Judicial * 0.40) +
                (p2Transparency * 0.25) +
                (p3normalized * 0.15) +
                (p4Trust * 0.20);

        finalScore = Math.round(finalScore * 100.0) / 100.0;

        return new CompositeScore(
                p1Judicial,
                p2Transparency,
                p3Contribution,
                p4Trust,
                finalScore
        );
    }

    public int determineRankingLevel(double finalScore) {
        if (finalScore >= 85) return 1;
        if (finalScore >= 65) return 2;
        if (finalScore >= 40) return 3;
        return 4;
    }
}

