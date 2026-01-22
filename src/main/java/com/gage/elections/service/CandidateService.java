package com.gage.elections.service;

import com.gage.elections.controller.dto.response.MatchResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CandidateService {

    final CandidateRepository candidateRepository;
    final CandidateSearchRepository candidateSearchRepository;
    final GovernmentPlanRepository governmentPlanRepository;
    final ScoringEngine scoringService;
    final MatchMapper matchMapper;
    final CandidateMapper candidateMapper;

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

//    public Candidate updateCandidateAll(String code, CandidateCreateRequest request) {
//        Candidate existing = getCandidateByCode(code);
//        Candidate candidate = candidateMapper.toCandidate(request);
//        candidate.setCode(existing.getCode());
//        calculateScore(candidate);
//        return candidateRepository.save(candidate);
//    }

    public Candidate updateCandidateAll(String code, CandidateCreateRequest request) {
        // 1. Buscamos el original (el que tiene el ID de Mongo y los scores actuales)
        Candidate existing = getCandidateByCode(code);

        // 2. IMPORTANTE: En lugar de toCandidate, usamos patchToCandidate
        // Esto copia los datos del request SOBRE el objeto existente
        candidateMapper.putToCandidate(request, existing);

        // 3. Ahora 'existing' tiene los datos nuevos pero conserva su ID y consistencia
        calculateScore(existing);

        // 4. Guardamos el objeto original actualizado
        return candidateRepository.save(existing);
    }

    public Candidate updateCandidate(String code, CandidateUpdateRequest request) {
        return updateCandidateWithoutScore(code, candidate -> candidateMapper.patchToCandidate(request, candidate));
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

    public Page<CandidateResponse> findAll(String position, Pageable pageable) {
        Page<Candidate> page;

        // Si el filtro es "all", traemos todo paginado. Si no, filtramos por posición.
        if ("all".equalsIgnoreCase(position)) {
            page = candidateRepository.findAll(pageable);
        } else {
            page = candidateRepository.findByPositionIgnoreCase(position, pageable);
        }

        return page.map(v -> new CandidateResponse(
                v.getCode(),
                v.getName(),
                v.getPhoto(),
                v.getParty(),
                v.getRankingLevel()
        ));
    }

    public Candidate getCandidateByCode(String id) {
        return candidateRepository.findByCode(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidato no encontrado: " + id));
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

    void calculateScore(Candidate candidate) {
        CompositeScore scores = scoringService.calculateAll(candidate);
        int level = scoringService.determineRankingLevel(scores.getFinalScore());
        candidate.updateScoring(scores, level);
    }

    void recalculateScore(Candidate candidate) {
        scoringService.recalculateFinalScore(candidate);
    }

    Candidate updateCandidateWithScore(String code, Consumer<Candidate> updater) {
        Candidate candidate = getCandidateByCode(code);
        updater.accept(candidate);
        recalculateScore(candidate);
        candidate.setRankingLevel(scoringService.determineRankingLevel(candidate.getScores().getFinalScore()));
        return candidateRepository.save(candidate);
    }

    Candidate updateCandidateWithoutScore(String code, Consumer<Candidate> updater) {
        Candidate candidate = getCandidateByCode(code);
        updater.accept(candidate);
        return candidateRepository.save(candidate);
    }

    String sanitizeAndValidate(String rawQuery) {
        String sanitized = SearchUtils.sanitize(rawQuery);
        return SearchUtils.isValid(sanitized) ? sanitized : null;
    }
}
