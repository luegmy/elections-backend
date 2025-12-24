package com.gage.elections.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "electoral.scoring.trust")
public class TrustProperties {
    private double initialScore;
    private TrustPenalties penalties;

    @Getter
    @Setter
    public static class TrustPenalties {
        private double majorScandal;
        private double minorControversy;
        private double factCheckMax;
        private double factCheckUnit;
        private double ethicsSanction;
        private List<Double> partySwitches; // Mapea [0.0, 5.0, 15.0, 30.0]
    }
}
