package com.gage.elections.service.calculator;

import com.gage.elections.config.properties.TransparencyProperties;
import com.gage.elections.model.candidate.Transparency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransparencyScoreCalculator {

    private final TransparencyProperties props;

    public double calculate(Transparency t) {
        if (t == null) return 0.0;

        double score = 0.0;

        // 1. Declaración jurada
        if (t.isSubmittedDeclaration()) {
            score += props.getDeclarationBase();

            int inconsistence = t.getDeclarationInconsistencies();
            // Lógica para mapear inconsistencias a bonos del array en YAML
            score += (inconsistence < props.getInconsistencyBonuses().size())
                    ? props.getInconsistencyBonuses().get(inconsistence) : 0.0;
        }

        // 2. Info económica
        if (t.isPublishedIncome()) score += props.getEconomicInfoItem();
        if (t.isPublishedExpenses()) score += props.getEconomicInfoItem();
        if (t.isAuditsAvailable()) score += props.getEconomicInfoItem();

        // 3. Gestión Pública (Asistencia)
        if (t.isWasPublicOfficer()) {
            // Ejemplo: 95% asistencia * 0.1 = 9.5 puntos
            score += t.getAttendancePercentage() * props.getAttendanceWeight();
        }

        return score;
    }
}

