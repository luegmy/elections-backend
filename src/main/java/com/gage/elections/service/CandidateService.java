package com.gage.elections.service;

import com.gage.elections.controller.dto.request.CandidateCreateRequest;
import com.gage.elections.controller.dto.MatchResponse;
import com.gage.elections.model.candidate.*;
import com.gage.elections.repository.CandidateRepository;
import com.gage.elections.repository.CandidateSearchRepository;
import com.gage.elections.service.calculator.ScoringEngine;
import com.gage.elections.service.mapper.CandidateMapper;
import com.gage.elections.service.mapper.MatchMapper;
import com.gage.elections.util.SearchUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidateSearchRepository candidateSearchRepository;
    private final ScoringEngine scoringService;
    private final MatchMapper matchMapper;
    private final CandidateMapper candidateMapper;

    public void createCandidate(CandidateCreateRequest request) {
        Candidate candidate = candidateMapper.toCandidate(request);
        calculateScore(candidate);
        candidateRepository.save(candidate);
    }

    public void createCandidates(List<Candidate> candidates) {
        candidates.forEach(this::calculateScore);
        candidateRepository.saveAll(candidates);
    }

    public Candidate updateCandidate(String code, Candidate request) {
        return updateCandidateWithoutScore(code, candidate -> {

            if (candidate.getName() == null) candidate.setName(request.getName());
            if (candidate.getParty() == null) candidate.setParty(request.getParty());
            if (candidate.getPosition() == null) candidate.setPosition(request.getPosition());
            if (candidate.getBiography() != null) candidate.setBiography(request.getBiography());
            if (candidate.getPartyAcronym() == null) candidate.setPartyAcronym(request.getPartyAcronym());

        });
    }

    public Candidate updateHistory(String code, List<LegalHistoryEntry> history) {
        return updateCandidateWithScore(code, c -> {
            c.getScores().setJudicialScore(scoringService.getJudicialCalculator(history));
            c.setHistory(history);
        });
    }

    public Candidate updateProposals(String code, List<Proposal> proposals) {
        return updateCandidateWithScore(code, c -> {
            c.getScores().setPlanScore(scoringService.getPlanCalculator(proposals));
            c.setProposals(proposals);
        });
    }

    public Candidate updateTrust(String code, Trust trust) {
        return updateCandidateWithScore(code, c -> {
            c.getScores().setTrustScore(scoringService.getTrustCalculator(trust));
            c.setTrust(trust);
        });
    }

    public Candidate updateAchievements(String code, List<Achievement> achievements) {
        return updateCandidateWithScore(code, c -> {
            c.getScores().setContributionScore(scoringService.getContributionCalculator(achievements));
            c.setAchievements(achievements);
        });
    }

    public Candidate updateTransparency(String code, Transparency transparency) {
        return updateCandidateWithScore(code, c -> {
            c.getScores().setTransparencyScore(scoringService.getTransparencyCalculator(transparency));
            c.setTransparency(transparency);
        });
    }

    public List<Candidate> findAll() {
        return candidateRepository.findAll();
    }

    public Candidate getCandidateByCode(String id) {
        return candidateRepository.findByCode(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found: " + id));
    }

    public List<MatchResponse> searchCandidatesAtlas(String rawQuery) {

        String query = sanitizeAndValidate(rawQuery);
        if (query == null) return Collections.emptyList();

        return candidateSearchRepository.searchByAtlas(query)
                .stream()
                .map(candidate -> matchMapper.toMatchResponse(candidate, query))
                .flatMap(Optional::stream)
                .toList();
    }

    private void calculateScore(Candidate candidate) {
        CompositeScore scores = scoringService.calculateAll(candidate);
        int level = scoringService.determineRankingLevel(scores.getFinalScore());
        candidate.updateScoring(scores, level);
    }

    private void recalculateScore(Candidate candidate) {
        scoringService.recalculateFinalScore(candidate);
    }

    private Candidate updateCandidateWithScore(String code, Consumer<Candidate> updater) {

        Candidate candidate = getCandidateByCode(code);

        updater.accept(candidate);
        recalculateScore(candidate);
        candidate.setRankingLevel(scoringService.determineRankingLevel(candidate.getScores().getFinalScore()));

        return candidateRepository.save(candidate);
    }

    private Candidate updateCandidateWithoutScore(String code,Consumer<Candidate> updater) {

        Candidate candidate = getCandidateByCode(code);

        updater.accept(candidate);

        return candidateRepository.save(candidate);
    }


    private String sanitizeAndValidate(String rawQuery) {
        String sanitized = SearchUtils.sanitize(rawQuery);
        return SearchUtils.isValid(sanitized) ? sanitized : null;
    }
}
