package com.gage.elections.service.calculator;

import com.gage.elections.config.properties.JudicialProperties;
import com.gage.elections.model.candidate.LegalHistoryEntry;
import com.gage.elections.model.scoring.RuleKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class JudicialScoreCalculator {

    private final JudicialProperties props;

    public double calculate(List<LegalHistoryEntry> history) {
        if (history == null || history.isEmpty()) {
            return props.getBaseScore();
        }

        double totalPenalty = history.stream()
                .mapToDouble(h -> props.getPenaltyMap()
                        .getOrDefault(new RuleKey(h.getStatus(), h.getSeverity(), h.getCategory()), 0.0))
                .sum();

        return Math.max(0, props.getBaseScore() - totalPenalty);
    }

}

