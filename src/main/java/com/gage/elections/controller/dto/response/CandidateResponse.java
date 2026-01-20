package com.gage.elections.controller.dto.response;

public record CandidateResponse(
        String code,
        String name,
        String photo,
        String party,
        int rankingLevel
) {
}
