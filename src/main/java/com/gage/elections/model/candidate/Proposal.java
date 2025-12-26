package com.gage.elections.model.candidate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Proposal {
    private String id;
    private String title;
    private String description;
    private String detailDescription;
    private String area;            // Eje Temático (Ej: Salud, Educación, Minería)
    private String sourcePlan;      // Sección del Plan de Gobierno donde se encuentra

    // Atributos CLAVE para la objetividad
    private double feasibilityScore; // 0.0 - 1.0: Puntaje de Viabilidad Técnica y Económica
    private double impactScore;      // 0.0 - 1.0: Puntaje de Impacto Social o Económico Esperado
    private String costEstimate;     // Estimación del costo de implementación (Ej: "S/ 500 Millones" o "Bajo")
    
}
