package com.gage.elections.controller.dto.request;

public record CandidateUpdateRequest(
        String name,
        String position,
        String photo,
        String party,
        String partyAcronym,
        String biography
) {
}
