package com.gage.elections.controller.dto.request;

public record ProposalRequest(
        String title,
        String description,
        String area,
        String sourcePlan
) {}

