package com.gage.elections.model.candidate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "candidates")
public class Candidate {

    @Transient
    public static final String SEQUENCE_NAME = "candidate_sequence";

    @Id
    String code;
    String name;
    String position;
    String photo;
    @Indexed
    String party;
    String partyAcronym;
    String biography;
    List<LegalHistoryEntry> history;
    List<Achievement> achievements;
    Transparency transparency;
    Trust trust;
    CompositeScore scores;
    int rankingLevel;
    @DocumentReference
    GovernmentPlan governmentPlan;

    LocalDateTime lastAuditDate;
    String dataSourceVersion;

    public void updateScoring(CompositeScore scores, int rankingLevel) {
        this.scores = scores;
        this.rankingLevel = rankingLevel;
        this.lastAuditDate = LocalDateTime.now();
    }
}
