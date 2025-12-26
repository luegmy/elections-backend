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
    private double judicialScore;
    private double contributionScore;
    private double transparencyScore;
    private double trustScore;
    private double finalScore;
}

