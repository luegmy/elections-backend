package com.gage.elections.service.calculator;

import com.gage.elections.config.properties.PlanProperties;
import com.gage.elections.model.candidate.Proposal;
import com.gage.elections.util.SearchUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class PlanScoreCalculator {

    final PlanProperties props;
    // Regex para detectar metas (%, números, hectáreas, km) y referencias a leyes/derogaciones
    static final Pattern TECHNICAL_PATTERN = Pattern.compile(".*(\\d+%|\\d+ (hectáreas|km|unidades)|derog|ley|artículo|normativa|NDC).*");

    public double calculate(List<Proposal> proposals) {
        if (proposals == null || proposals.isEmpty()) return 0.0;

        List<String> mandatoryAreas = List.of("SOCIAL", "ECONOMIA", "INSTITUCIONAL", "AMBIENTAL");
        Map<String, Double> areaScores = new HashMap<>();
        Map<String, Integer> areaCounts = new HashMap<>();

        for (Proposal p : proposals) {
            String area = (p.getArea() != null) ? p.getArea().toUpperCase() : "DESCONOCIDO";

            // 1. Viabilidad técnica con "Bono de Rigor"
            double realFeasibility = calculateTechnicalFeasibility(p);

            // 2. Score base
            double proposalScore = p.getImpactScore() * realFeasibility;

            // 3. Penalización Económica Diferenciada
            if (p.getCostEstimate() == null || p.getCostEstimate().isBlank() ||
                    "No especifica".equalsIgnoreCase(p.getCostEstimate())) {

                // Si es un plan detallado (>300 caracteres) como el de López-Chau,
                // asumimos que el sustento técnico compensa la falta de presupuesto explícito.
                if (p.getDetailDescription() != null && p.getDetailDescription().length() > 300) {
                    proposalScore *= 0.95; // Penalización mínima (5%)
                } else {
                    proposalScore *= 0.70; // Penalización por "promesa al aire" (30%)
                }
            } else if ("Muy Alto".equalsIgnoreCase(p.getCostEstimate())) {
                proposalScore *= props.getCostEstimateHigh();
            }

            areaScores.merge(area, proposalScore, Double::sum);
            areaCounts.merge(area, 1, Integer::sum);
        }

        double totalPlanPoints = 0.0;
        for (String area : mandatoryAreas) {
            if (areaScores.containsKey(area)) {
                double areaAverage = areaScores.get(area) / areaCounts.get(area);
                totalPlanPoints += areaAverage;
            }
        }

        double finalScore = (totalPlanPoints / mandatoryAreas.size()) * 100.0;
        return SearchUtils.round2Decimals(finalScore);
    }

    double calculateTechnicalFeasibility(Proposal p) {
        double baseFeasibility = p.getFeasibilityScore();
        String detail = (p.getDetailDescription() != null) ? p.getDetailDescription().trim() : "";

        // 4. EL FILTRO DE RELLENO (Agresivo)
        if (detail.length() < 100) {
            baseFeasibility *= 0.4; // Si es un eslogan, pierde el 60% de viabilidad
        } else if (detail.length() > 400) {
            baseFeasibility *= 1.1; // Bono por profundidad de análisis
        }

        // 5. EL BONO ESTADISTA (Detección de Metas y Leyes)
        // Si menciona derogación de leyes pro-crimen o metas como "100% NDC"
        String lowerDetail = detail.toLowerCase();
        if (TECHNICAL_PATTERN.matcher(lowerDetail).find()) { // .find() en vez de .matches()
            baseFeasibility *= 1.15;
        }

        if (p.isRequiresConstitutionalReform()) {
            baseFeasibility *= props.getRequiresConstitutionalReform();
        }

        if (p.isViolatesInternationalTreaties()) {
            baseFeasibility *= props.getViolatesInternationalTreaties();
        }

        return Math.min(baseFeasibility, 1.0); // No puede superar el 100%
    }
}
