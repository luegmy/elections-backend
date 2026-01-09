package com.gage.elections.model.candidate;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Proposal {
    private String id;
    private String title;
    private String description;
    private String detailDescription;
    private String area;
    private String sourcePlan;
    private List<String> keywords;

    private double feasibilityScore; // 0.0 - 1.0: Puntaje de Viabilidad Técnica y Económica
    private double impactScore;      // 0.0 - 1.0: Puntaje de Impacto Social o Económico Esperado
    private String costEstimate;     // Estimación del costo de implementación (Ej: "S/ 500 Millones" o "Bajo")

    private boolean requiresConstitutionalReform;
    private boolean violatesInternationalTreaties;
}
