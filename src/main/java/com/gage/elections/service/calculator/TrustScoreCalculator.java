package com.gage.elections.service.calculator;

import com.gage.elections.config.properties.TrustProperties;
import com.gage.elections.model.candidate.Trust;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrustScoreCalculator {

    final TrustProperties props;

    public double calculate(Trust trust) {
        if (trust == null) return props.getInitialScore();

        double score = props.getInitialScore();

        // 1. Penalización por Escándalos Mayores
        score -= (trust.getMajorSanctions() * props.getPenalties().getMajorScandal());

        // 2. Penalización por Controversias Menores (Aquí entran las multas y quejas éticas)
        score -= (trust.getMinorSanctions() * props.getPenalties().getMinorControversy());

        // 3. Lógica de Fact-Checking con el tope de 25.0
        double factCheckPenalty = Math.min(
                trust.getFactCheckFailures() * props.getPenalties().getFactCheckUnit(),
                props.getPenalties().getFactCheckMax()
        );
        score -= factCheckPenalty;

        // 4. Sanción Ética (Difamaciones probadas / Resoluciones del JNE)
        if (trust.isEthicsSanction()) {
            score -= props.getPenalties().getEthicsSanction();
        }

        // 5. Penalización por Transfuguismo (Uso del mapa de penalidades)
        int switches = trust.getPartySwitches();
        score -= props.getPenalties().getPartySwitches().getOrDefault(switches, 0.0);

        // MANTENER EL PISO EN 0 PARA NO AFECTAR NEGATIVAMENTE EL PROMEDIO DE LOS 5 PILARES
        return Math.max(score, 0.0);
    }
}

