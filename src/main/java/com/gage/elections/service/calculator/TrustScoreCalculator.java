package com.gage.elections.service.calculator;

import com.gage.elections.config.properties.TrustProperties;
import com.gage.elections.model.candidate.Trust;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrustScoreCalculator {

    private final TrustProperties props;

    public double calculate(Trust t) {
        if (t == null) return props.getInitialScore();

        double score = props.getInitialScore();

        score -= t.getMajorSanctions() * props.getPenalties().getMajorScandal();
        score -= t.getPartySwitches() * props.getPenalties().getMinorControversy();

        double factCheckPenalty = Math.min(
                t.getFactCheckFailures() * props.getPenalties().getFactCheckUnit(),
                props.getPenalties().getFactCheckMax()
        );
        score -= factCheckPenalty;

        if (t.isEthicsSanction()) score -= props.getPenalties().getEthicsSanction();

        // Incoherencia política (Transfuguismo)
        int switches = t.getPartySwitches();
        score -= (switches < props.getPenalties().getPartySwitches().size())
                ? props.getPenalties().getPartySwitches().get(switches)
                : props.getPenalties().getPartySwitches().get(3); // El máximo castigo

        return Math.max(score, 0.0);
    }
}

