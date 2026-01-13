package com.gage.elections.model.candidate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "government_plans")
public class GovernmentPlan {
    @Id
    private String id;
    @Indexed(unique = true)
    private String partyCode;
    private List<Proposal> proposals;
    private String documentUrl; // Enlace al PDF oficial del JNE
}

