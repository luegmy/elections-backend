package com.gage.elections.service;

import com.gage.elections.controller.dto.CandidateCreateRequest;
import com.gage.elections.controller.dto.MatchResponse;
import com.gage.elections.service.mapper.CandidateMapper;
import com.gage.elections.service.mapper.MatchMapper;
import com.gage.elections.model.candidate.*;
import com.gage.elections.repository.CandidateRepository;
import com.gage.elections.util.SearchUtils;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ScoringEngine scoringService;
    private final MongoTemplate mongoTemplate;
    private final MatchMapper matchMapper;
    private final CandidateMapper candidateMapper;
    private static final String FIELD_SCORE = "score";

    public void createCandidate(CandidateCreateRequest candidate) {

        Candidate candidateCreated = candidateMapper.toCandidate(candidate);

        recalculateScore(candidateCreated);

        candidateRepository.save(candidateCreated);
    }
    public void createCandidates(List<Candidate> candidates) {

        candidates.forEach(this::recalculateScore);

        candidateRepository.saveAll(candidates);
    }

    public Candidate updateCandidate(String id, Candidate candidate) {

        candidate.setCode(id);
        recalculateScore(candidate);

        return candidateRepository.save(candidate);
    }

    public Candidate updateHistory(String code, List<LegalHistoryEntry> history) {
        Candidate c = getCandidateByCode(code);
        c.setHistory(history);
        return candidateRepository.save(c);
    }

    public Candidate updateProposals(String code, List<Proposal> proposals) {
        Candidate c = getCandidateByCode(code);
        c.setProposals(proposals);
        return candidateRepository.save(c);
    }

    public Candidate updateTrust(String code, Trust trust) {
        Candidate c = getCandidateByCode(code);
        c.setTrust(trust);
        return candidateRepository.save(c);
    }

    public Candidate updateAchievements(String code, List<Achievement> achievements) {
        Candidate c = getCandidateByCode(code);
        c.setAchievements(achievements);
        return candidateRepository.save(c);
    }

    public Candidate updateTransparency(String code, Transparency transparency) {
        Candidate c = getCandidateByCode(code);
        c.setTransparency(transparency);
        return candidateRepository.save(c);
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
        if (query == null) {
            return Collections.emptyList();
        }

        Aggregation aggregation = Aggregation.newAggregation(
                buildAtlasSearchStage(query),
                Aggregation.limit(20)
        );

        return mongoTemplate
                .aggregate(aggregation, "candidates", Candidate.class)
                .getMappedResults()
                .stream()
                .map(candidate -> matchMapper.toMatchResponse(candidate, query))
                .toList();
    }


    private void recalculateScore(Candidate candidate) {

        CompositeScore newScores = scoringService.calculateAll(candidate);
        int level = scoringService.determineRankingLevel(newScores.getFinalScore());

        candidate.updateScoring(newScores, level);
    }

    private String sanitizeAndValidate(String rawQuery) {

        String sanitized = SearchUtils.sanitize(rawQuery);

        return SearchUtils.isValid(sanitized)
                ? sanitized
                : null;
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
        return createTextSearch(
                query,
                List.of("planKeywords"),
                2.0
        );
    }

    private Document searchInCandidateProfile(String query) {
        return createTextSearch(
                query,
                List.of(
                        "name",
                        "position",
                        "proposals.title",
                        "proposals.description",
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

