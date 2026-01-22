
package com.gage.elections;

import com.gage.elections.config.properties.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({
        JudicialProperties.class,
        TransparencyProperties.class,
        TrustProperties.class,
        ContributionProperties.class,
        PlanProperties.class,
        ScoringWeightsProperties.class
})
@SpringBootApplication
public class ElectionsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElectionsApplication.class, args);
    }

}
