package com.gage.elections.service;

import com.gage.elections.controller.dto.CandidateCreateRequest;
import com.gage.elections.controller.dto.MatchResponse;
import com.gage.elections.model.candidate.*;
import com.gage.elections.repository.CandidateRepository;
import com.gage.elections.service.mapper.CandidateMapper;
import com.gage.elections.service.mapper.MatchMapper;
import com.gage.elections.util.SearchUtils;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ScoringEngine scoringService;
    private final MongoTemplate mongoTemplate;
    private final MatchMapper matchMapper;
    private final CandidateMapper candidateMapper;
    private final SequenceGeneratorService generateSequence;

    public void createCandidate(CandidateCreateRequest request) {
        Candidate candidate = candidateMapper.toCandidate(request);
        long seq = generateSequence.generateSequence("candidate_sequence");
        candidate.setCode(String.valueOf(seq));
        recalculateScore(candidate);
        candidateRepository.save(candidate);
    }

    public void createCandidates(List<Candidate> candidates) {
        candidates.forEach(this::recalculateScore);
        candidateRepository.saveAll(candidates);
    }

    public Candidate updateCandidate(String code, Candidate candidate) {
        candidate.setCode(code);
        recalculateScore(candidate);
        return candidateRepository.save(candidate);
    }

    public Candidate updateHistory(String code, List<LegalHistoryEntry> history) {
        return updateCandidate(code, c -> {
            c.getScores().setJudicialScore(
                    scoringService.getJudicialCalculator(history)
            );
            c.setHistory(history);
        });
    }

    public Candidate updateProposals(String code, List<Proposal> proposals) {
        return updateCandidate(code, c -> {
            c.getScores().setPlanScore(
                    scoringService.getPlanCalculator(proposals)
            );
            c.setProposals(proposals);
        });
    }

    public Candidate updateTrust(String code, Trust trust) {
        return updateCandidate(code, c -> {
            c.getScores().setTrustScore(
                    scoringService.getTrustCalculator(trust)
            );
            c.setTrust(trust);
        });
    }

    public Candidate updateAchievements(String code, List<Achievement> achievements) {
        return updateCandidate(code, c -> {
            c.getScores().setContributionScore(
                    scoringService.getContributionCalculator(achievements)
            );
            c.setAchievements(achievements);
        });
    }

    public Candidate updateTransparency(String code, Transparency transparency) {
        return updateCandidate(code, c -> {
            c.getScores().setTransparencyScore(
                    scoringService.getTransparencyCalculator(transparency)
            );
            c.setTransparency(transparency);
        });
    }

    private void recalculateScore(Candidate candidate) {
        CompositeScore scores = scoringService.calculateAll(candidate);
        int level = scoringService.determineRankingLevel(scores.getFinalScore());
        candidate.updateScoring(scores, level);
    }

    private Candidate updateCandidate(String code, Consumer<Candidate> updater) {

        Candidate candidate = getCandidateByCode(code);
        ensureScoreInitialized(candidate);

        updater.accept(candidate);

        scoringService.recalculateFinalScore(candidate);
        candidate.setRankingLevel(
                scoringService.determineRankingLevel(
                        candidate.getScores().getFinalScore()
                )
        );

        return candidateRepository.save(candidate);
    }

    private void ensureScoreInitialized(Candidate candidate) {
        if (candidate.getScores() == null) {
            candidate.setScores(new CompositeScore());
        }
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

        Aggregation aggregation = Aggregation.newAggregation(
                buildAtlasSearchStage(query),
                Aggregation.limit(20)
        );

        return mongoTemplate
                .aggregate(aggregation, "candidates", Candidate.class)
                .getMappedResults()
                .stream()
                .map(c -> matchMapper.toMatchResponse(c, query)) // Devuelve Optional<MatchResponse>
                .flatMap(Optional::stream)                      // Convierte Optional a Stream y filtra vacíos
                .toList();
    }

    private String sanitizeAndValidate(String rawQuery) {
        String sanitized = SearchUtils.sanitize(rawQuery);
        return SearchUtils.isValid(sanitized) ? sanitized : null;
    }

    private AggregationOperation buildAtlasSearchStage(String query) {
        return context -> new Document("$search",
                new Document("index", "default")
                        .append("compound", new Document("should", List.of(
                                searchInPlanKeywords(query),
                                searchInCandidateProfile(query)
                        )))
        );
    }

    private Document searchInPlanKeywords(String query) {
        return createTextSearch(query, List.of("proposals.keywords"), 2.0);
    }

    private Document searchInCandidateProfile(String query) {
        return createTextSearch(
                query,
                List.of(
                        "name",
                        "position",
                        "proposals.title",
                        "proposals.description",
                        "proposals.detailDescription",
                        "history.title",
                        "history.description"
                ),
                1.0
        );
    }

    private Document createTextSearch(String query, List<String> paths, double boost) {
        Document text = new Document()
                .append("query", query)
                .append("path", paths.size() == 1 ? paths.get(0) : paths)
                .append("fuzzy", new Document("maxEdits", 2));

        if (boost > 1.0) {
            text.append("score",
                    new Document("boost", new Document("value", boost)));
        }

        return new Document("text", text);
    }
}
