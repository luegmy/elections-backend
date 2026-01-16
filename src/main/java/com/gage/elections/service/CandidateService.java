package com.gage.elections.service;

import com.gage.elections.controller.dto.MatchResponse;
import com.gage.elections.controller.dto.request.CandidateCreateRequest;
import com.gage.elections.controller.dto.request.CandidateUpdateRequest;
import com.gage.elections.controller.dto.response.CandidateResponse;
import com.gage.elections.model.candidate.*;
import com.gage.elections.repository.CandidateRepository;
import com.gage.elections.repository.CandidateSearchRepository;
import com.gage.elections.repository.GovernmentPlanRepository;
import com.gage.elections.service.calculator.ScoringEngine;
import com.gage.elections.service.mapper.CandidateMapper;
import com.gage.elections.service.mapper.MatchMapper;
import com.gage.elections.util.SearchUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidateSearchRepository candidateSearchRepository;
    private final GovernmentPlanRepository governmentPlanRepository;
    private final ScoringEngine scoringService;
    private final MatchMapper matchMapper;
    private final CandidateMapper candidateMapper;

    public void createCandidate(CandidateCreateRequest request) {
        Candidate candidate = candidateMapper.toCandidate(request);
        calculateScore(candidate);
        candidateRepository.save(candidate);
    }

    public void createCandidates(List<CandidateCreateRequest> requests) {
        List<Candidate> candidates = requests.stream()
                .map(candidateMapper::toCandidate)
                .toList();
        candidates.forEach(this::calculateScore);
        candidateRepository.saveAll(candidates);
    }

    public Candidate updateCandidate(String code, CandidateUpdateRequest request) {
        return updateCandidateWithoutScore(code, candidate -> candidateMapper.patch(request, candidate));
    }

    public Candidate updateHistory(String code, List<LegalHistoryEntry> history) {
        return updateCandidateWithScore(code, c -> {
            c.setHistory(history);
            c.getScores().setJudicialScore(scoringService.getJudicialCalculator(history));
        });
    }

    public Candidate updateProposals(String code, GovernmentPlan request) {
        return updateCandidateWithScore(code, c -> {
            GovernmentPlan plan = c.getGovernmentPlan();
            if (plan == null) {
                plan = new GovernmentPlan();
                plan.setId(request.getId());
                plan.setPartyCode(request.getPartyCode());
            }
            plan.setProposals(request.getProposals());
            plan.setDocumentUrl(request.getDocumentUrl());

            governmentPlanRepository.save(plan);

            c.setGovernmentPlan(plan);
            c.getScores().setPlanScore(scoringService.getPlanCalculator(request.getProposals()));
        });
    }

    public Candidate updateTrust(String code, Trust trust) {
        return updateCandidateWithScore(code, c -> {
            c.setTrust(trust);
            c.getScores().setTrustScore(scoringService.getTrustCalculator(trust));
        });
    }

    public Candidate updateAchievements(String code, List<Achievement> achievements) {
        return updateCandidateWithScore(code, c -> {
            c.setAchievements(achievements);
            c.getScores().setContributionScore(scoringService.getContributionCalculator(achievements));
        });
    }

    public Candidate updateTransparency(String code, Transparency transparency) {
        return updateCandidateWithScore(code, c -> {
            c.setTransparency(transparency);
            c.getScores().setTransparencyScore(scoringService.getTransparencyCalculator(transparency));
        });
    }


    public List<CandidateResponse> findAll(String position) {

            return candidateRepository.findByPosition(position)
                    .stream()
                    .map(v -> new CandidateResponse(
                            v.getCode(),
                            v.getName(),
                            v.getParty(),
                            v.getRankingLevel()
                    ))
                    .sorted(Comparator
                            .comparingInt(CandidateResponse::rankingLevel)
                    )
                    .toList();
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

    private Candidate updateCandidateWithoutScore(String code, Consumer<Candidate> updater) {

        Candidate candidate = getCandidateByCode(code);

        updater.accept(candidate);

        return candidateRepository.save(candidate);
    }


    private String sanitizeAndValidate(String rawQuery) {
        String sanitized = SearchUtils.sanitize(rawQuery);
        return SearchUtils.isValid(sanitized) ? sanitized : null;
    }
}
