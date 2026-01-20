package com.gage.elections.repository.projection;

public interface CandidateListView {
    String getCode();
    String getName();
    String getPhoto();
    String getParty();
    int getRankingLevel();
}
