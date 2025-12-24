package com.gage.elections.config.properties;

import com.gage.elections.model.scoring.IncidentSeverity;
import com.gage.elections.model.scoring.LegalStatus;
import com.gage.elections.model.scoring.RuleKey;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "electoral.scoring.judicial")
public class JudicialProperties {

    private double baseScore;
    private List<PenaltyRuleItem> penalties;

    // Mapa procesado para búsqueda rápida O(1)
    private Map<RuleKey, Double> penaltyMap = new HashMap<>();

    @PostConstruct
    public void init() {
        // Convertimos la lista del YAML en el Mapa de reglas que ya tenías
        for (PenaltyRuleItem item : penalties) {
            penaltyMap.put(
                    new RuleKey(item.getStatus(), item.getSeverity()),
                    item.getValue()
            );
        }
    }

    @Getter
    @Setter
    public static class PenaltyRuleItem {
        private LegalStatus status;
        private IncidentSeverity severity;
        private double value;
    }
}
