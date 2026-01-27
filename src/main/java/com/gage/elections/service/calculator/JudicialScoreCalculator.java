package com.gage.elections.service.calculator;

import com.gage.elections.config.properties.JudicialProperties;
import com.gage.elections.model.candidate.LegalHistoryEntry;
import com.gage.elections.model.scoring.RuleKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JudicialScoreCalculator {

    final JudicialProperties props;

    public double calculate(List<LegalHistoryEntry> history) {
        if (history == null || history.isEmpty()) return props.getBaseScore();

        // 1. Agrupamos hitos por expediente para no castigar doble por el mismo caso
        Map<String, List<LegalHistoryEntry>> grouped = history.stream()
                .filter(h -> h.getExpedientNumber() != null)
                .collect(Collectors.groupingBy(LegalHistoryEntry::getExpedientNumber));

        double totalPenalty = 0;
        // Definimos los pesos de tu escala: 100% (20), 50% (10), 25% (5), 10% (2)...
        double[] weights = {1.0, 0.5, 0.25, 0.1, 0.1};

        for (List<LegalHistoryEntry> milestones : grouped.values()) {
            // Ordenamos por fecha: el hito más antiguo es el primero en la escala
            milestones.sort(Comparator.comparing(LegalHistoryEntry::getDate));

            for (int i = 0; i < milestones.size(); i++) {
                double basePoints = getPointsFromRule(milestones.get(i));

                // Seleccionamos el peso según la posición (si i > 4, usa el último peso de 0.1)
                double currentWeight = (i < weights.length) ? weights[i] : weights[weights.length - 1];

                double pointsToSubtract = basePoints * currentWeight;
                totalPenalty += pointsToSubtract;
            }
        }

        return Math.max(0, props.getBaseScore() - totalPenalty);
    }

    private double getPointsFromRule(LegalHistoryEntry h) {
        // Aquí el RuleKey ya va con la categoría limpia que viene del Mapper/JSON
        RuleKey key = new RuleKey(h.getStatus(), h.getSeverity(), h.getCategory());
        Double penalty = props.getPenaltyMap().get(key);

        return (penalty != null) ? penalty : 0.0;
    }

}

