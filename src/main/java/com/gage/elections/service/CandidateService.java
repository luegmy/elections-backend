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

import java.time.LocalDateTime;
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

    public List<MatchResponse> searchCandidatesAtlas(String search) {

        String sanitizedQuery = SearchUtils.sanitize(search);

        if (!SearchUtils.isValid(sanitizedQuery)) {
            return Collections.emptyList();
        }

        // 1. Stage $search (Atlas Search ordena por relevancia automáticamente)
        AggregationOperation searchStage = context -> new Document("$search",
                new Document("index", "default")
                        .append("compound", new Document()
                                .append("should", List.of(
                                        // Boost a palabras clave del plan (Importancia 2x)
                                        createTextFieldSearch(
                                                sanitizedQuery,
                                                List.of("planKeywords"),
                                                2.0
                                        ),
                                        // Búsqueda general en campos relevantes
                                        createTextFieldSearch(
                                                sanitizedQuery,
                                                List.of(
                                                        "name",
                                                        "position",
                                                        "proposals.title",
                                                        "proposals.description",
                                                        "history.title",
                                                        "history.description"
                                                ),
                                                1.0
                                        )
                                ))
                        )
        );

        // 2. Agregación final (sin score explícito)
        Aggregation aggregation = Aggregation.newAggregation(
                searchStage,
                Aggregation.limit(20)
        );

        List<Candidate> candidates = mongoTemplate
                .aggregate(aggregation, "candidates", Candidate.class)
                .getMappedResults();

        return candidates.stream()
                .map(c -> matchMapper.toMatchResponse(c, sanitizedQuery))
                .toList();
    }

    private Document createTextFieldSearch(String query, List<String> paths, double boost) {

        Document textNode = new Document()
                .append("query", query)
                .append("path", paths.size() == 1 ? paths.get(0) : paths)
                .append("fuzzy", new Document("maxEdits", 2));

        if (boost > 1.0) {
            textNode.append("score",
                    new Document("boost", new Document("value", boost)));
        }

        return new Document("text", textNode);
    }

    private void recalculateScore(Candidate candidate) {
        // Calculamos el nuevo score
        CompositeScore newScores = scoringService.calculateAll(candidate);
        int level = scoringService.determineRankingLevel(newScores.getFinalScore());

        // Aplicamos al modelo (Setter profesional)
        candidate.applyScore(newScores, level);
        candidate.setLastAuditDate(LocalDateTime.now());
    }

}

