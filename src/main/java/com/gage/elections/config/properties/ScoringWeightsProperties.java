package com.gage.elections.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "electoral.scoring.weights")
@Component
public class ScoringWeightsProperties {
    private double judicial;      // 0.40
    private double plan;          // 0.20
    private double transparency;  // 0.15
    private double trust;         // 0.15
    private double contribution;  // 0.10
}
