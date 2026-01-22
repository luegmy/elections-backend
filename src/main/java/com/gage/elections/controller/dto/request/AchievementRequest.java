package com.gage.elections.controller.dto.request;

import com.gage.elections.model.candidate.AchievementType;

import java.util.List;

public record AchievementRequest(
        String description,
        AchievementType type,
        Integer relevance,    // Cambiado de int a Integer
        Integer quantity,     // Cambiado de int a Integer
        Double impactScore,   // Cambiado de double a Double
        Boolean verified,     // Cambiado de boolean a Boolean
        String sourceUrl,
        List<String> tags
) {
}

