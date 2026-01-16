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
            // 1. Base por presentar (ahora es 15.0)
            score += props.getDeclarationBase();

            // 2. Aplicar Inconsistency Bonuses (que ahora incluye negativos)
            int inconsistency = transparency.getDeclarationInconsistencies();

            // Usamos un valor de castigo por defecto (ej. -30.0) si tiene más de 3 inconsistencias
            double inconsistencyImpact = props.getInconsistencyBonuses()
                    .getOrDefault(inconsistency, -30.0);

            score += inconsistencyImpact;
        }

        // 3. Info Económica (Ingresos, Gastos, Auditorías: 10.0 c/u)
        if (transparency.isPublishedIncome()) score += props.getEconomicInfoItem();
        if (transparency.isPublishedExpenses()) score += props.getEconomicInfoItem();
        if (transparency.isAuditsAvailable()) score += props.getEconomicInfoItem();

        // 4. Asistencia (Peso 0.3)
        if (transparency.isWasPublicOfficer()) {
            score += (transparency.getAttendancePercentage() * props.getAttendanceWeight());
        } else {
            // Candidatos nuevos solo reciben 5.0
            score += props.getDefaultAttendancePoint();
        }

        // Mantenemos el piso en 0.0 para que no afecte negativamente al promedio de los 5 pilares
        return Math.max(score, 0.0);
    }
}

