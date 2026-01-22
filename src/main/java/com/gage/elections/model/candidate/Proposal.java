package com.gage.elections.model.candidate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Getter
@Setter
public class Proposal {
    String id;
    String title;
    String description;
    String detailDescription;
    String area;
    String sourcePlan;
    @Indexed
    List<String> keywords;

    double feasibilityScore; // 0.0 - 1.0: Puntaje de Viabilidad Técnica y Económica
    double impactScore;      // 0.0 - 1.0: Puntaje de Impacto Social o Económico Esperado
    String costEstimate;     // Estimación del costo de implementación (Ej: "S/ 500 Millones" o "Bajo")

    boolean requiresConstitutionalReform;
    boolean violatesInternationalTreaties;
}
