package com.gage.elections.controller.dto;

import com.gage.elections.model.candidate.AchievementType;

import java.util.List;

public record AchievementRequest(
        String description,
        AchievementType type,
        int relevance,          // 1=Bajo, 2=Medio, 3=Alto
        int quantity,
        List<String> tags
) {}

