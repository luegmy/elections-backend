package com.gage.elections.model.candidate;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Achievement {
    private String description;
    private AchievementType type;
    private int relevance; // Nivel de Relevancia (ej: 1=Bajo, 2=Medio, 3=Alto)
    private int quantity;  // Para contar años, o número de proyectos
    private List<String> tags;

}
