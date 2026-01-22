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
    String id;
    LocalDate date;
    String title;
    String description;
    String expedientNumber;
    String source;   // URL o entidad que originó la denuncia
    boolean verified; // Si la información es comprobada
    LegalStatus status;
    IncidentSeverity severity;
    LegalCategory category;
}
