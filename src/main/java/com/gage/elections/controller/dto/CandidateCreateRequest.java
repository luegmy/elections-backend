package com.gage.elections.controller.dto;

import java.util.List;

public record CandidateCreateRequest(
        String name,
        String position,
        String party,
        String partyAcronym,
        String biography,

        List<ProposalRequest> proposals,
        List<String> planKeywords,

        List<AchievementRequest> achievements,
        TransparencyRequest transparency,
        TrustRequest trust
) {}


