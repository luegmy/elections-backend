package com.gage.elections.service.calculator;

import com.gage.elections.config.properties.TrustProperties;
import com.gage.elections.model.candidate.Trust;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrustScoreCalculator {

    private final TrustProperties props;

    public double calculate(Trust trust) {
        if (trust == null) return props.getInitialScore();

        double score = props.getInitialScore();

        score -= trust.getMajorSanctions() * props.getPenalties().getMajorScandal();
        score -= trust.getPartySwitches() * props.getPenalties().getMinorControversy();

        double factCheckPenalty = Math.min(
                trust.getFactCheckFailures() * props.getPenalties().getFactCheckUnit(),
                props.getPenalties().getFactCheckMax()
        );
        score -= factCheckPenalty;

        if (trust.isEthicsSanction()) score -= props.getPenalties().getEthicsSanction();

        int changeParty = trust.getPartySwitches();
        score -= props.getPenalties().getPartySwitches().getOrDefault(changeParty,0.0);

        return Math.max(score, 0.0);
    }
}

