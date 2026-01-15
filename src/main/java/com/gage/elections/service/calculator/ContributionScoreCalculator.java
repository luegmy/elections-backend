package com.gage.elections.service.calculator;

import com.gage.elections.config.properties.ContributionProperties;
import com.gage.elections.model.candidate.Achievement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ContributionScoreCalculator {

    private final ContributionProperties props;

    public double calculate(List<Achievement> achievements) {
        if (achievements == null || achievements.isEmpty()) return 0.0;

        double totalPublicWork = calculatePublicManagementScore(achievements);
        double totalExperience = calculateHumanCapitalScore(achievements);

        // 2. Resultado final con tope y redondeo
        double rawResult = Math.max(0.0, totalPublicWork + totalExperience);
        return round(rawResult);
    }

    private double calculatePublicManagementScore(List<Achievement> achievements) {
        double approvedPoints = 0.0;
        int itemsCount = 0;
        double proposedCount = 0.0;
        double penaltyPoints = 0.0;

        for (Achievement a : achievements) {
            if (a.getType() == null) continue;

            switch (a.getType()) {
                case LAW_APPROVED, PUBLIC_PROJECT_COMPLETED -> {
                    if (itemsCount++ < props.getMaxItemsAllowed()) {
                        approvedPoints += props.getRelevanceScore(a.getRelevance());
                    }
                }

                case PUBLIC_SECTOR_EXPERIENCE -> {
                    if (a.getRelevance() >= 3 && itemsCount++ < props.getMaxItemsAllowed()) {
                        approvedPoints += props.getRelevanceScore(a.getRelevance());
                    }
                }

                case LAW_PROPOSED -> proposedCount++;

                case PROMISE_BROKEN -> {
                    if (props.getPenalties() != null) {
                        penaltyPoints += props.getPenalties().getOrDefault("promiseBroken", 0.0);
                    }
                }

                default -> {
                    // No sumamos puntos aquí para ACADEMIC_EXPERIENCE o PUBLIC_SECTOR_EXPERIENCE
                    // ya que estos pertenecen al bloque de Capital Humano.
                }
            }
        }

        double initiativePoints = Math.min(proposedCount, props.getMaxItemsAllowed());

        return Math.max(0.0, Math.min(approvedPoints + initiativePoints - penaltyPoints, props.getMaxSubScore()));
    }

    private double calculateHumanCapitalScore(List<Achievement> achievements) {
        double yearsOfExperience = 0.0;
        int topAcademicRelevance = 0;
        double leaderPoints = 0.0;

        for (Achievement a : achievements) {
            switch (a.getType()) {
                case ACADEMIC_EXPERIENCE ->
                        topAcademicRelevance = Math.max(topAcademicRelevance, a.getRelevance());
                case PUBLIC_SECTOR_EXPERIENCE ->
                        yearsOfExperience += a.getQuantity();
                case SOCIAL_PROJECT_LEADERSHIP -> {
                    if (a.getRelevance() >= 2 && props.getBonuses() != null) {
                        leaderPoints += props.getBonuses().getSocialLeadership();
                    }
                }

                default -> {
                    // No sumamos puntos aquí para ACADEMIC_EXPERIENCE o PUBLIC_SECTOR_EXPERIENCE
                    // ya que estos pertenecen al bloque de Capital Humano.
                }
            }
        }

        double cappedExperience = Math.min(yearsOfExperience, props.getMaxYearsExperience());
        double experienceAndLeadership = Math.min(cappedExperience + leaderPoints, 25.0);

        double totalHumanCapital = experienceAndLeadership + props.getAcademicBonus(topAcademicRelevance);

        return Math.min(totalHumanCapital, props.getMaxSubScore());
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
