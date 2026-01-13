package com.gage.elections.service.calculator;

import com.gage.elections.config.properties.PlanProperties;
import com.gage.elections.model.candidate.Proposal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PlanScoreCalculator {

    private final PlanProperties props;

    public double calculate(List<Proposal> proposals) {
        if (proposals == null || proposals.isEmpty()) return 0.0;

        double totalWeightedScore = 0.0;
        int count = 0;

        for (Proposal p : proposals) {
            double realFeasibility = calculateTechnicalFeasibility(p);

            double proposalScore = p.getImpactScore() * realFeasibility;

            if ("Muy Alto".equalsIgnoreCase(p.getCostEstimate())) {
                proposalScore *= props.getCostEstimate();
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
            baseFeasibility *= props.getRequiresConstitutionalReform();
        }

        if (p.isViolatesInternationalTreaties()) {
            baseFeasibility *= props.getViolatesInternationalTreaties();
        }

        return baseFeasibility;
    }
}
