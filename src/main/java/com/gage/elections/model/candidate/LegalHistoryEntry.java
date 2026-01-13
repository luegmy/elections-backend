package com.gage.elections.model.candidate;

import com.gage.elections.model.scoring.IncidentSeverity;
import com.gage.elections.model.scoring.LegalCategory;
import com.gage.elections.model.scoring.LegalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class LegalHistoryEntry {
    private String id;
    private LocalDate date;
    private String title;
    private String description;
    private String expedientNumber;
    private String source;   // URL o entidad que originó la denuncia
    private boolean verified; // Si la información es comprobada
    private LegalStatus status;
    private IncidentSeverity severity;
    private LegalCategory category;
}
