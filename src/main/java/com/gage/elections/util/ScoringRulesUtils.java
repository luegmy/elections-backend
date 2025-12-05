package com.gage.elections.util;

import com.gage.elections.model.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

import static com.gage.elections.model.IncidentSeverity.*;
import static com.gage.elections.model.LegalStatus.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScoringRulesUtils {
    public static final Map<RuleKey, Integer> PENALTY_RULES;

    static {

        // ---- GRAVES ----

        PENALTY_RULES = Map.ofEntries(
                Map.entry(new RuleKey(SENTENCIA_FIRME_CONDENATORIA, GRAVE), 50),
                Map.entry(new RuleKey(SENTENCIA_PRIMERA_INSTANCIA, GRAVE), 35),
                Map.entry(new RuleKey(PROCESO_JUDICIAL_ABIERTO, GRAVE), 25),
                Map.entry(new RuleKey(INVESTIGACION_PRELIMINAR, GRAVE), 10),
                Map.entry(new RuleKey(PRESCRITO, GRAVE), 0),
                Map.entry(new RuleKey(SENTENCIA_ABSOLUTORIA, GRAVE), 0),

                // ---- MODERADOS ----
                Map.entry(new RuleKey(SENTENCIA_FIRME_CONDENATORIA, MODERADO), 30),
                Map.entry(new RuleKey(SENTENCIA_PRIMERA_INSTANCIA, MODERADO), 20),
                Map.entry(new RuleKey(PROCESO_JUDICIAL_ABIERTO, MODERADO), 15),
                Map.entry(new RuleKey(INVESTIGACION_PRELIMINAR, MODERADO), 5),
                Map.entry(new RuleKey(PRESCRITO, MODERADO), 0),
                Map.entry(new RuleKey(SENTENCIA_ABSOLUTORIA, MODERADO), 0),

                // ---- LEVES ----
                Map.entry(new RuleKey(SENTENCIA_FIRME_CONDENATORIA, LEVE), 10),
                Map.entry(new RuleKey(SENTENCIA_PRIMERA_INSTANCIA, LEVE), 5),
                Map.entry(new RuleKey(PROCESO_JUDICIAL_ABIERTO, LEVE), 3),
                Map.entry(new RuleKey(INVESTIGACION_PRELIMINAR, LEVE), 1),
                Map.entry(new RuleKey(PRESCRITO, LEVE), 0),
                Map.entry(new RuleKey(SENTENCIA_ABSOLUTORIA, LEVE), 0));

    }

    public static int calculateJudicialScore(List<LegalHistoryEntry> history) {
        if (history == null || history.isEmpty()) return 0;

        return history.stream()
                .mapToInt(entry -> PENALTY_RULES.getOrDefault(
                        new RuleKey(entry.getStatus(), entry.getSeverity()),
                        0
                ))
                .sum();
    }

    public static int calculateContributionScore(List<Achievement> achievements) {
        if (achievements == null || achievements.isEmpty()) {
            return 0;
        }

        int laborPublicaScore = 0;
        int experienciaScore = 0;

        // Límites por categoría
        int maxLawsApproved = 5;
        int maxProjectsCompleted = 5;

        int countLawsApproved = 0;
        int countPublicProjects = 0;

        boolean tieneDoctorado = false;
        boolean tieneMaestria = false;
        boolean tieneBachiller = false;

        int puntosIniciativa = 0;
        int aniosExperiencia = 0;

        for (Achievement ach : achievements) {

            switch (ach.getType()) {

                case LAW_APPROVED -> {
                    if (countLawsApproved < maxLawsApproved) {
                        laborPublicaScore += scoreLabor(ach.getRelevance());
                        countLawsApproved++;
                    }
                }

                case PUBLIC_PROJECT_COMPLETED -> {
                    if (countPublicProjects < maxProjectsCompleted) {
                        laborPublicaScore += scoreLabor(ach.getRelevance());
                        countPublicProjects++;
                    }
                }

                case LAW_PROPOSED ->
                        puntosIniciativa = increaseInitiative(puntosIniciativa);

                case ACADEMIC_EXPERIENCE -> {
                    tieneDoctorado |= ach.getRelevance() == 3;
                    tieneMaestria |= ach.getRelevance() == 2;
                    tieneBachiller |= ach.getRelevance() == 1;
                }

                case PUBLIC_SECTOR_EXPERIENCE ->
                        aniosExperiencia += ach.getQuantity();

                case SOCIAL_PROJECT_LEADERSHIP ->
                        experienciaScore += scoreSocialLeadership(ach);
            }
        }

        // Aplicar puntos finales
        laborPublicaScore += puntosIniciativa;               // máx +5
        experienciaScore += calculateAcademicScore(tieneDoctorado, tieneMaestria, tieneBachiller);
        experienciaScore += Math.min(aniosExperiencia, 10);  // máx 10

        // Cap global por categoría
        int laborFinal = Math.min(laborPublicaScore, 50);
        int expFinal = Math.min(experienciaScore, 50);

        return laborFinal + expFinal;
    }

    private static int scoreLabor(int relevance) {
        return switch (relevance) {
            case 3 -> 15;
            case 2 -> 7;
            default -> 2;
        };
    }

    private static int increaseInitiative(int puntos) {
        return puntos < 5 ? puntos + 1 : puntos;
    }

    private static int scoreSocialLeadership(Achievement ach) {
        return ach.getRelevance() >= 2 ? 15 : 0;
    }

    private static int calculateAcademicScore(boolean hasPhD, boolean hasMaster, boolean hasBachiller) {
        if (hasPhD) return 25;
        if (hasMaster) return 15;
        if (hasBachiller) return 8;
        return 0;
    }

    public static int calculateTransparencyScore(Transparency data) {
        if (data == null) return 0;

        int score = 0;

        // ============================================================
        // 1. DECLARACIÓN JURADA (0–40)
        // ============================================================
        if (data.isSubmittedDeclaration()) {
            score += 20; // Presentada

            int inconsist = data.getDeclarationInconsistencies();
            if (inconsist == 0) score += 20;      // Perfecta
            else if (inconsist == 1) score += 10; // Casi perfecta
            // >1 → no suma más
        }


        // ============================================================
        // 2. COHERENCIA / TRAYECTORIA (0–30)
        // ============================================================
        int trayectoria = switch (data.getPartySwitches()) {
            case 0 -> 30;  // Nunca cambió → impecable
            case 1 -> 20;  // Un cambio → aceptable
            case 2 -> 10;  // Dos cambios → dudoso
            default -> 0;  // 3 o más → cero puntos
        };

        score += trayectoria;

        // ============================================================
        // 3. ASISTENCIA (0–20)
        // ============================================================
        int asistencia;

        if (!data.isWasPublicOfficer()) {
            // No fue funcionario → no tiene faltas
            asistencia = 20;
        } else {
            // Si lo fue → asistencia real (0.0 a 1.0)
            asistencia = (int) Math.round(data.getAttendancePercentage() * 20);
        }

        score += asistencia;

        // ============================================================
        // 4. FINANZAS / TRANSPARENCIA EXTRA (0–10)
        // ============================================================
        int finanzas = 0;

        if (data.isPublishedIncome())   finanzas += 4;  // Mostró ingresos
        if (data.isPublishedExpenses()) finanzas += 3;  // Mostró gastos campaña
        if (data.isAuditsAvailable())   finanzas += 3;  // Auditorías públicas

        score += finanzas;

        // ============================================================
        // RETORNO FINAL (0–100)
        // ============================================================
        return Math.min(score, 100);
    }


    public static int calculateTrustScore(Trust data) {
        if (data == null)
            // Si no hay datos, se asume confiabilidad (no hay negativos)
            return 100;

        int score = 100;

        // 1. Penalización por Escándalos Graves
        score -= (data.getMajorScandals() * 30);

        // 2. Penalización por Sanción de Ética
        if (data.isEthicsSanction()) score -= 25;

        // 3. Penalización por Controversias Menores
        score -= (data.getMinorControversies() * 10);

        // 4. Penalización por Fact-Checking (con tope de -25)
        score -= Math.min(data.getFactCheckFailures() * 5, 25); // Topamos la penalización

        // El score mínimo es 0
        return Math.max(0, score);
    }
}
