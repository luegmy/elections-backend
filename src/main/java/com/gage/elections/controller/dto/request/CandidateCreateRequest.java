package com.gage.elections.controller.dto.request;

import com.gage.elections.model.candidate.GovernmentPlan;

import java.util.List;

public record CandidateCreateRequest(
        String name,
        String position,
        String photo,
        String party,
        String partyAcronym,
        String biography,
        List<LegalHistoryEntryRequest> history,
        List<AchievementRequest> achievements,
        TransparencyRequest transparency,
        TrustRequest trust,
        GovernmentPlanRequest governmentPlan
) {}


