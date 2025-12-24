package com.gage.elections.controller.dto;

import com.gage.elections.model.scoring.IncidentSeverity;
import com.gage.elections.model.scoring.LegalStatus;

import java.time.LocalDate;

public record LegalHistoryEntryRequest(
        LocalDate date,
        String title,
        String description,
        String expedientNumber,
        String source,
        boolean verified,
        LegalStatus status,
        IncidentSeverity severity
) {}

