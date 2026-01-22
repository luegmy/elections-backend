package com.gage.elections.model.candidate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompositeScore {
    double judicialScore;
    double contributionScore;
    double transparencyScore;
    double trustScore;
    double planScore;
    double finalScore;
}

