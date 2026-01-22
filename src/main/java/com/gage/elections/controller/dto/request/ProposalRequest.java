package com.gage.elections.controller.dto.request;

import java.util.List;

public record ProposalRequest(
        String id,
        String title,
        String description,
        String detailDescription,
        String area,
        String sourcePlan,
        List<String> keywords,
        Double feasibilityScore, // 0.0 - 1.0: Puntaje de Viabilidad Técnica y Económica
        Double impactScore,      // 0.0 - 1.0: Puntaje de Impacto Social o Económico Esperado
        String costEstimate,     // Estimación del costo de implementación (Ej: "S/ 500 Millones" o "Bajo")

        boolean requiresConstitutionalReform,
        boolean violatesInternationalTreaties
) {
}

