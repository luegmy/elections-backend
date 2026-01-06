package com.gage.elections.controller.dto;

public record MatchResponse(
        String code,
        String name,
        String party,
        String partyAcronym,
        String position,
        String matchType,
        String matchTitle,       // Agregar
        String matchDescription,
        String matchDetailDescription,// Agregar
        String sourcePlan,       // Agregar
        Double finalScore,
        Integer rankingLevel
) {}
