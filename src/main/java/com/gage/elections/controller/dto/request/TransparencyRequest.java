package com.gage.elections.controller.dto.request;

public record TransparencyRequest(
        boolean submittedDeclaration,
        int declarationInconsistencies,

        boolean wasPublicOfficer,
        double attendancePercentage,

        boolean publishedIncome,
        boolean publishedExpenses,
        boolean auditsAvailable
) {}

