package com.gage.elections.controller.dto;

public record ProposalRequest(
        String title,
        String description,
        String area,
        String sourcePlan
) {}

