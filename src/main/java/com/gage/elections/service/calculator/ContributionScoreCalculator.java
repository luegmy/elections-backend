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
        if (achievements == null || achievements.isEmpty()) return 0;

        double publicWorkPoints = 0;
        int lawsCount = 0;
        int projectsCount = 0;
        double initiativePoints = 0;
        double yearsOfExperience = 0;
        int topAcademicRelevance = 0;
        double leaderPoints = 0;

        for (Achievement a : achievements) {
            switch (a.getType()) {
                case LAW_APPROVED -> {
                    if (lawsCount++ < props.getMaxItemsAllowed())
                        publicWorkPoints += props.getRelevanceScore(a.getRelevance());
                }
                case PUBLIC_PROJECT_COMPLETED -> {
                    if (projectsCount++ < props.getMaxItemsAllowed())
                        publicWorkPoints += props.getRelevanceScore(a.getRelevance());
                }
                case LAW_PROPOSED ->
                        initiativePoints = Math.min(initiativePoints + 1, props.getMaxItemsAllowed());

                case ACADEMIC_EXPERIENCE ->
                        topAcademicRelevance = Math.max(topAcademicRelevance, a.getRelevance());

                case PUBLIC_SECTOR_EXPERIENCE ->
                        yearsOfExperience += a.getQuantity();

                case SOCIAL_PROJECT_LEADERSHIP -> {
                    if (a.getRelevance() >= 2)
                        leaderPoints += props.getBonuses().getOrDefault("social-leadership", 15.0);
                }
                case PROMISE_BROKEN ->
                        publicWorkPoints -= props.getPenalties().getOrDefault("promise-broken", 20.0);
            }
        }

        double totalPublicWork = Math.min(publicWorkPoints + initiativePoints, props.getMaxSubScore());

        double totalExperience = leaderPoints
                + props.getAcademicBonus(topAcademicRelevance)
                + Math.min(yearsOfExperience, props.getMaxYearsExperience());

        totalExperience = Math.min(totalExperience, props.getMaxSubScore());

        return Math.max(0, totalPublicWork + totalExperience);
    }
}
