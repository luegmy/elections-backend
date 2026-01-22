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

        // Suma de los dos pilares (Max 50 cada uno)
        return round(Math.min(totalManagement + totalHumanCapital, 100.0));
    }

    double calculateManagementScore(List<Achievement> achievements) {
        double positivePoints = 0.0;
        double penaltyPoints = 0.0;
        int itemsCount = 0;
        int proposedLawsCount = 0;

        // Identificamos perfil
        boolean isPublicOfficial = achievements.stream()
                .anyMatch(a -> a.getType() == AchievementType.PUBLIC_SECTOR_EXPERIENCE);

        for (Achievement a : achievements) {
            if (a.getType() == null) continue;

            switch (a.getType()) {
                // 1. LOGROS CONCRETOS (Obras o Leyes aprobadas)
                case LAW_APPROVED, PUBLIC_PROJECT_COMPLETED, INFRASTRUCTURE_SUCCESS -> {
                    if (itemsCount++ < props.getMaxItemsAllowed()) {
                        double base = props.getRelevanceScore(a.getRelevance());
                        double multiplier = isPublicOfficial ? 1.0 : 0.5;
                        positivePoints += (base * Math.max(0, a.getImpactScore()) * multiplier);
                    }
                }

                // 2. LA PIEZA FALTANTE: LEYES PROPUESTAS
                case LAW_PROPOSED -> proposedLawsCount++;

                // 3. NIVELACIÓN PARA PRIVADOS (Rectores/Dirigentes)
                case SOCIAL_PROJECT_LEADERSHIP -> {
                    if (!isPublicOfficial && itemsCount++ < props.getMaxItemsAllowed()) {
                        positivePoints += props.getRelevanceScore(a.getRelevance()) * Math.max(0, a.getImpactScore());
                    }
                }

                // 4. PENALIDADES (Restan de la gestión)
                case PROMISE_BROKEN, FISCAL_DEBT_INCREASE -> {
                    String key = (a.getType() == AchievementType.FISCAL_DEBT_INCREASE) ? "fiscalDebtIncrease" : "promiseBroken";
                    double penaltyBase = props.getPenalties().getOrDefault(key, 15.0);
                    penaltyPoints += penaltyBase * Math.abs(a.getImpactScore());
                }
                default -> {}
            }
        }

        // Sumamos las leyes propuestas con un tope (ejemplo: 1 pto cada una, máximo 5 pts)
        double initiativePoints = Math.min(proposedLawsCount, 5.0);

        return Math.max(0.0, Math.min(positivePoints + initiativePoints - penaltyPoints, 50.0));
    }

    double calculateHumanCapitalScore(List<Achievement> achievements) {
        double yearsOfExp = 0.0;
        int topAcademic = 0;
        boolean hasLeadershipBonus = false;

        for (Achievement a : achievements) {
            switch (a.getType()) {
                case ACADEMIC_EXPERIENCE -> topAcademic = Math.max(topAcademic, a.getRelevance());
                case PUBLIC_SECTOR_EXPERIENCE -> yearsOfExp += a.getQuantity();
                case SOCIAL_PROJECT_LEADERSHIP -> {
                    if (a.getRelevance() >= 2) hasLeadershipBonus = true;
                }
                default -> {}
            }
        }

        double expPoints = Math.min(yearsOfExp, props.getMaxYearsExperience());
        double leaderBonus = hasLeadershipBonus ? props.getBonuses().getSocialLeadership() : 0.0;

        // Trayectoria (Max 25) + Académico (Max 25) = 50
        double careerPath = Math.min(expPoints + leaderBonus, 25.0);
        double academicPoints = props.getAcademicBonus(topAcademic);

        return Math.min(careerPath + academicPoints, 50.0);
    }

    double round(double value) { return Math.round(value * 100.0) / 100.0; }
}
