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
    String id;
    @Indexed(unique = true)
    String partyCode;
    List<Proposal> proposals;
    String documentUrl; // Enlace al PDF oficial del JNE
}

