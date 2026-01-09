package com.gage.elections.controller.dto.request;

import com.gage.elections.controller.dto.AchievementRequest;
import com.gage.elections.controller.dto.ProposalRequest;
import com.gage.elections.controller.dto.TransparencyRequest;
import com.gage.elections.controller.dto.TrustRequest;

import java.util.List;

public record CandidateCreateRequest(
        String name,
        String position,
        String party,
        String partyAcronym,
        String biography,
        List<ProposalRequest> proposals,
        List<AchievementRequest> achievements,
        TransparencyRequest transparency,
        TrustRequest trust
) {}


