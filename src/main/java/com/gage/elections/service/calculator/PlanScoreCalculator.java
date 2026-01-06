package com.gage.elections.service.calculator;

import com.gage.elections.model.candidate.Proposal;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlanScoreCalculator {

    public double calculate(List<Proposal> proposals) {
        if (proposals == null || proposals.isEmpty()) return 0.0;

        double totalWeightedScore = 0.0;
        int count = 0;

        for (Proposal p : proposals) {
            double realFeasibility = calculateTechnicalFeasibility(p);

            double proposalScore = p.getImpactScore() * realFeasibility;

            if ("Muy Alto".equalsIgnoreCase(p.getCostEstimate())) {
                proposalScore *= 0.8;
            }

            totalWeightedScore += proposalScore;
            count++;
        }

        if (count == 0) return 0.0;
        return (totalWeightedScore / count) * 100.0;
    }

    private double calculateTechnicalFeasibility(Proposal p) {
        double baseFeasibility = p.getFeasibilityScore();

        if (p.isRequiresConstitutionalReform()) {
            baseFeasibility *= 0.4; // Dependencia total del Congreso
        }

        if (p.isViolatesInternationalTreaties()) {
            baseFeasibility *= 0.2; // Barrera diplomática y jurídica internacional
        }

        return baseFeasibility;
    }
}
