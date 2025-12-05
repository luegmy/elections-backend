package com.gage.elections.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompositeScore {
    private int judicialScore;
    private int contributionScore;
    private int transparencyScore;
    private int trustScore;
    private int finalScore;

    public void computeFinalScore() {
        this.finalScore = (int) Math.round(
                judicialScore * 0.45 +
                        contributionScore * 0.25 +
                        transparencyScore * 0.20 +
                        trustScore * 0.10
        );
    }
}

