package com.gage.elections.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "electoral.scoring.contribution")
public class ContributionProperties {
    private double maxSubScore;
    private int maxItemsAllowed;
    private int maxYearsExperience;
    private Map<String, Double> bonuses;
    private Map<String, Double> penalties;

    public double getAcademicBonus(int academicLevel) {
        return switch (academicLevel) {
            case 3 -> bonuses.getOrDefault("academic.doctorate", 25.0);
            case 2 -> bonuses.getOrDefault("academic.mastery", 15.0);
            case 1 -> bonuses.getOrDefault("academic.bachelor", 8.0);
            default -> 0.0;
        };
    }

    public double getRelevanceScore(int impactLevel) {
        return switch (impactLevel) {
            case 3 -> bonuses.getOrDefault("relevance.high", 15.0);
            case 2 -> bonuses.getOrDefault("relevance.medium", 7.0);
            default -> bonuses.getOrDefault("relevance.low", 2.0);
        };
    }
}
