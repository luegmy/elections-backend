package com.gage.elections.controller.dto;

public record MatchResponse(
        String code,            // ID público del candidato
        String name,
        String party,
        String partyAcronym,
        String position,      // Cargo al que postula
        String matchType,     // ¿Hizo match en "Plan de Gobierno" o "Biografía"?
        String matchSnippet,  // Un fragmento del texto donde hubo coincidencia
        double finalScore,   // Relevancia de la búsqueda (Atlas Search)
        int rankingLevel      // Semáforo (1-4) calculado por tu ScoringEngine
) {}
