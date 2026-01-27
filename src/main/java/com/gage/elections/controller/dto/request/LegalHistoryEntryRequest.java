package com.gage.elections.controller.dto.request;

import com.gage.elections.model.scoring.IncidentSeverity;
import com.gage.elections.model.scoring.LegalCategory;
import com.gage.elections.model.scoring.LegalStatus;

import java.time.LocalDate;

public record LegalHistoryEntryRequest(
        String id,
        LocalDate date,
        String title,
        String description,
        String expedientNumber,
        String source,
        boolean verified,
        LegalStatus status,
        IncidentSeverity severity,
        LegalCategory category
) {}

