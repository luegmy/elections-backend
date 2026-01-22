package com.gage.elections.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "electoral.weights")
public class ScoringWeightsProperties {
    private double judicial;
    private double plan;
    private double transparency;
    private double trust;
    private double contribution;
}
