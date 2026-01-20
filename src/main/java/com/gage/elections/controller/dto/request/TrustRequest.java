package com.gage.elections.controller.dto.request;

public record TrustRequest(
        int majorSanctions,
        int minorSanctions,
        int partySwitches,
        int factCheckFailures,
        boolean ethicsSanction
) {}
