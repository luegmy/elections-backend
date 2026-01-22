package com.gage.elections.controller.dto.request;

import java.util.List;

public record TransparencyRequest(
        boolean submittedDeclaration,
        int declarationInconsistencies,
        List<String> inconsistencyDetails,
        String economicAuditNotes,

        boolean wasPublicOfficer,
        double attendancePercentage,

        boolean publishedIncome,
        boolean publishedExpenses,
        boolean auditsAvailable
) {}

