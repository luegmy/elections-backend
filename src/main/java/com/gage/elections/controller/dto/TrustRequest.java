package com.gage.elections.controller.dto;

public record TrustRequest(
        int majorSanctions,
        int minorSanctions,
        int partySwitches,
        int factCheckFailures,
        boolean ethicsSanction
) {}
