package com.gage.elections.controller.dto.request;

import java.util.List;
import java.util.Map;

public record TrustRequest(
        int majorSanctions,
        int minorSanctions,
        Map<String, List<String>> sanctionsDetail,
        int partySwitches,
        int factCheckFailures,
        List<String> factCheckSources,
        boolean ethicsSanction
) {}
