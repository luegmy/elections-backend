package com.gage.elections.model.candidate;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Achievement {
    String description;
    AchievementType type;
    int relevance; // Nivel de Relevancia (ej: 1=Bajo, 2=Medio, 3=Alto)
    int quantity;  // Para contar años, o número de proyectos
    double impactScore; // NUEVO: de -1.0 (Daño) a 1.0 (Beneficio total)
    boolean verified;   // NUEVO: ¿Está chequeado por el sistema?
    String sourceUrl;   // NUEVO: Link a la noticia o reporte oficial
    List<String> tags;
}
