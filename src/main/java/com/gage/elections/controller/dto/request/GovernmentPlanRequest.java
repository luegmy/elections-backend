package com.gage.elections.controller.dto.request;

import java.util.List;

public record GovernmentPlanRequest(
        String id,
        String partyCode,
        List<ProposalRequest> proposals,
        String documentUrl
) {}
