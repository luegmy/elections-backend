package com.gage.elections.controller.dto.request;

import com.gage.elections.controller.dto.*;

import java.util.List;

public record CandidateCreateRequest(
        String name,
        String position,
        String party,
        String partyAcronym,
        String biography,
        List<LegalHistoryEntryRequest> history,
        List<ProposalRequest> proposals,
        List<AchievementRequest> achievements,
        TransparencyRequest transparency,
        TrustRequest trust
) {}


