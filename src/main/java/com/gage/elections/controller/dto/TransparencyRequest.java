package com.gage.elections.controller.dto;

public record TransparencyRequest(
        boolean submittedDeclaration,
        int declarationInconsistencies,

        boolean wasPublicOfficer,
        double attendancePercentage,

        boolean publishedIncome,
        boolean publishedExpenses,
        boolean auditsAvailable
) {}

