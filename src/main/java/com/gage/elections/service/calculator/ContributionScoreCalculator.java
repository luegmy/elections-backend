package com.gage.elections.service.calculator;

import com.gage.elections.config.properties.ContributionProperties;
import com.gage.elections.model.candidate.Achievement;
import com.gage.elections.model.candidate.AchievementType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ContributionScoreCalculator {

    final ContributionProperties props;

    public double calculate(List<Achievement> achievements) {
        if (achievements == null || achievements.isEmpty()) return 0.0;

        double totalManagement = calculateManagementScore(achievements);
        double totalHumanCapital = calculateHumanCapitalScore(achievements);

        return round(Math.min(totalManagement + totalHumanCapital, 100.0));
    }

    double calculateManagementScore(List<Achievement> achievements) {
        double positivePoints = 0.0;
        double penaltyPoints = 0.0;
        int itemsCount = 0;

        for (Achievement a : achievements) {
            if (a.getType() == null) continue;

            double relevance = props.getRelevanceScore(a.getRelevance());
            double impact = Math.max(0, a.getImpactScore());

            switch (a.getType()) {
                // NIVELACIÓN: Sumamos impacto ya sea en Sector Público o Social (Rectoría, ONGs, etc.)
                case PUBLIC_PROJECT_COMPLETED, SOCIAL_PROJECT_LEADERSHIP,
                     INFRASTRUCTURE_SUCCESS, LAW_APPROVED -> {
                    if (itemsCount++ < props.getMaxItemsAllowed()) {
                        // Eliminamos el multiplicador excluyente. El impacto es impacto.
                        positivePoints += (relevance * impact);
                    }
                }

                case LAW_PROPOSED -> positivePoints += 1.0; // Puntos por iniciativa legislativa

                case PROMISE_BROKEN, FISCAL_DEBT_INCREASE -> {
                    double penaltyBase = props.getPenalties().getOrDefault(a.getType().name(), 15.0);
                    penaltyPoints += penaltyBase * Math.abs(a.getImpactScore());
                }
                default -> {}
            }
        }
        return Math.max(0.0, Math.min(positivePoints - penaltyPoints, 50.0));
    }

    double calculateHumanCapitalScore(List<Achievement> achievements) {
        double yearsOfExp = 0.0;
        int topAcademicLevel = 0;

        for (Achievement a : achievements) {
            switch (a.getType()) {
                case ACADEMIC_EXPERIENCE ->
                        topAcademicLevel = Math.max(topAcademicLevel, a.getRelevance());

                // Aquí sumamos TODA la experiencia, pública o privada (como Rector)
                case PUBLIC_SECTOR_EXPERIENCE ->
                        yearsOfExp += a.getQuantity();

                default -> {}
            }
        }

        double expPoints = Math.min(yearsOfExp, props.getMaxYearsExperience());
        double academicBonus = props.getAcademicBonus(topAcademicLevel);

        // Max 25 por "Kilometraje" + Max 25 por "Grado Académico" = 50
        return Math.min(expPoints + academicBonus, 50.0);
    }

    double round(double value) { return Math.round(value * 100.0) / 100.0; }
}
