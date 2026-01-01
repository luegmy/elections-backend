package com.gage.elections.service.calculator;

import com.gage.elections.config.properties.TransparencyProperties;
import com.gage.elections.model.candidate.Transparency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransparencyScoreCalculator {

    private final TransparencyProperties props;

    public double calculate(Transparency transparency) {
        if (transparency == null) return 0.0;

        double score = 0.0;

        if (transparency.isSubmittedDeclaration()) {
            score += props.getDeclarationBase();

            int inconsistency  = transparency.getDeclarationInconsistencies();
            score += props.getInconsistencyBonuses()
                    .getOrDefault(inconsistency, 0.0);
        }

        if (transparency.isPublishedIncome()) score += props.getEconomicInfoItem();
        if (transparency.isPublishedExpenses()) score += props.getEconomicInfoItem();
        if (transparency.isAuditsAvailable()) score += props.getEconomicInfoItem();

        if (transparency.isWasPublicOfficer()) {
            score += transparency.getAttendancePercentage() * props.getAttendanceWeight();
        } else {
            score += props.getDefaultAttendancePoint();
        }

        return score;
    }
}

