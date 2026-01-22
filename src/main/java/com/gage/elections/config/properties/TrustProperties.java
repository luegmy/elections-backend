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
    double initialScore;
    TrustPenalties penalties = new TrustPenalties();

    @Getter
    @Setter
    public static class TrustPenalties {
        double majorScandal;
        double minorControversy;
        double factCheckMax;
        double factCheckUnit;
        double ethicsSanction;
        Map<Integer, Double> partySwitches = new HashMap<>();
    }
}
