package com.gage.elections.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "electoral.scoring.contribution")
public class ContributionProperties {
    double maxSubScore;
    int maxItemsAllowed;
    int maxYearsExperience;
    double lawProposedPoints;

    BonusStructure bonuses;
    Map<String, Double> penalties;

    @Getter @Setter
    public static class BonusStructure {
        Academic academic;
        Relevance relevance;
        Double socialLeadership;
    }

    @Getter @Setter
    public static class Academic {
        double doctorate;
        double mastery;
        double bachelor;
    }

    @Getter @Setter
    public static class Relevance {
        double high;
        double medium;
        double low;
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
