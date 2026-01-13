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

    private BonusStructure bonuses;
    private Map<String, Double> penalties;

    @Getter @Setter
    public static class BonusStructure {
        private Academic academic;
        private Relevance relevance;
        private Double socialLeadership;
    }

    @Getter @Setter
    public static class Academic {
        private double doctorate;
        private double mastery;
        private double bachelor;
    }

    @Getter @Setter
    public static class Relevance {
        private double high;
        private double medium;
        private double low;
    }

    public double getAcademicBonus(int level) {
        if (bonuses == null || bonuses.academic == null) return 0.0;
        return switch (level) {
            case 3 -> bonuses.academic.doctorate;
            case 2 -> bonuses.academic.mastery;
            case 1 -> bonuses.academic.bachelor;
            default -> 0.0;
        };
    }

    public double getRelevanceScore(int impact) {
        if (bonuses == null || bonuses.relevance == null) return 2.0;
        return switch (impact) {
            case 3 -> bonuses.relevance.high;
            case 2 -> bonuses.relevance.medium;
            default -> bonuses.relevance.low;
        };
    }
}
