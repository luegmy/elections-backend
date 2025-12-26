package com.gage.elections.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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

    // Métodos de conveniencia para no repetir lógica
    public double getAcademicBonus(int relevance) {
        return switch (relevance) {
            case 3 -> bonuses.getOrDefault("academic.doctorado", 25.0);
            case 2 -> bonuses.getOrDefault("academic.maestria", 15.0);
            case 1 -> bonuses.getOrDefault("academic.bachiller", 8.0);
            default -> 0.0;
        };
    }

    public double getRelevanceScore(int relevance) {
        return switch (relevance) {
            case 3 -> bonuses.getOrDefault("relevance.alta", 15.0);
            case 2 -> bonuses.getOrDefault("relevance.media", 7.0);
            default -> bonuses.getOrDefault("relevance.baja", 2.0);
        };
    }
}
