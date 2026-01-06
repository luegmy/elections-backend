
package com.gage.elections;

import com.gage.elections.config.properties.ContributionProperties;
import com.gage.elections.config.properties.JudicialProperties;
import com.gage.elections.config.properties.TransparencyProperties;
import com.gage.elections.config.properties.TrustProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({
        JudicialProperties.class,
        TransparencyProperties.class,
        TrustProperties.class,
        ContributionProperties.class
})
@SpringBootApplication
public class ElectionsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElectionsApplication.class, args);
    }

}
