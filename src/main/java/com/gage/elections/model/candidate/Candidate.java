package com.gage.elections.model.candidate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "candidates")
public class Candidate {

    @Transient
    public static final String SEQUENCE_NAME = "candidate_sequence";

    @Id
    private String code;
    private String name;
    private String position;
    @Indexed
    private String party;
    private String partyAcronym;
    private String biography;
    private List<LegalHistoryEntry> history;
    private List<Achievement> achievements;
    private Transparency transparency;
    private Trust trust;
    private CompositeScore scores;
    private int rankingLevel;
    @DocumentReference
    private GovernmentPlan governmentPlan;

    private LocalDateTime lastAuditDate;
    private String dataSourceVersion;

    public void updateScoring(CompositeScore scores, int rankingLevel) {
        this.scores = scores;
        this.rankingLevel = rankingLevel;
        this.lastAuditDate = LocalDateTime.now();
    }
}
