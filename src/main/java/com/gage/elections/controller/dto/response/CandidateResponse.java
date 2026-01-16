package com.gage.elections.controller.dto.response;

public record CandidateResponse(
        String code,
        String name,
        String party,
        int rankingLevel
) {
}
