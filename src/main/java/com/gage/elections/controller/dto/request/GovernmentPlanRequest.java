package com.gage.elections.controller.dto.request;

import java.util.List;

public record GovernmentPlanRequest(
        String partyCode,
        List<ProposalRequest> proposals,
        String documentUrl
) {}
