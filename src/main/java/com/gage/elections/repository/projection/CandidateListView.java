package com.gage.elections.repository.projection;

public interface CandidateListView {
    String getCode();
    String getName();
    String getParty();
    int getRankingLevel();
}
