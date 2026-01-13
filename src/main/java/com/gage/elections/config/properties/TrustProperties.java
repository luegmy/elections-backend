package com.gage.elections.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "electoral.scoring.trust")
public class TrustProperties {
    private double initialScore;
    private TrustPenalties penalties = new TrustPenalties();

    @Getter
    @Setter
    public static class TrustPenalties {
        private double majorScandal;
        private double minorControversy;
        private double factCheckMax;
        private double factCheckUnit;
        private double ethicsSanction;
        private Map<Integer, Double> partySwitches = new HashMap<>();
    }
}
