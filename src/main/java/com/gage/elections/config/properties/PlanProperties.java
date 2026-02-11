package com.gage.elections.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "electoral.scoring.plan")
public class PlanProperties {

    double costEstimateHigh;
    double requiresConstitutionalReform;
    double violatesInternationalTreaties;
}
