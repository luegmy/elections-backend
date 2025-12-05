package com.gage.elections.controller.dto;

import com.gage.elections.model.Candidate;
import com.gage.elections.model.CompositeScore;

public record MatchResponse(Candidate candidate, CompositeScore score) {
}
