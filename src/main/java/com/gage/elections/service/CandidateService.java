package com.gage.elections.service;

import com.gage.elections.controller.dto.MatchResponse;
import com.gage.elections.model.Candidate;
import com.gage.elections.model.CompositeScore;
import com.gage.elections.model.LegalHistoryEntry;
import com.gage.elections.repository.CandidateRepository;
import com.gage.elections.util.MatchUtils;
import com.gage.elections.util.ScoringRulesUtils;
import com.gage.elections.util.TextSimilarityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository repo;
    private final SequenceGeneratorService sequenceGenerator;
    private final MongoTemplate mongoTemplate;

    public Candidate save(Candidate c) {

        if (c.getCode() == 0) {
            long newId = sequenceGenerator.generateSequence("candidate_seq");
            c.setCode(newId);
        }

        c.recalculateScore();

        return repo.save(c);
    }

    public Candidate update(long id, Candidate updated) {

        updated.setCode(id);
        updated.recalculateScore();

        return repo.save(updated);
    }

    public List<Candidate> findAll() {
        return repo.findAll();
    }

    public Candidate findById(long id) {
        return repo.findByCode(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found: " + id));
    }

    public List<Candidate> searchCandidates(String text) {

        String normalized = MatchUtils.normalize(text);
        String[] words = normalized.split("\\s+");

        List<Criteria> criteriaList = new ArrayList<>();

        for (String w : words) {

            Criteria wordCriteria = new Criteria().orOperator(
                    // Proposals
                    Criteria.where("proposals.title").regex(w, "i"),
                    Criteria.where("proposals.description").regex(w, "i"),
                    Criteria.where("proposals.area").regex(w, "i"),

                    // Keywords
                    Criteria.where("planKeywords").regex(w, "i")
            );

            criteriaList.add(wordCriteria);
        }

        Query query = new Query(new Criteria().andOperator(
                criteriaList.toArray(new Criteria[0])
        ));

        return mongoTemplate.find(query, Candidate.class);
    }


    public List<MatchResponse> findBestMatches(String question, int limit) {
        return repo.findAll().stream()
                .map(c -> {

                    // Construcción del texto
                    String proposalsText = c.getProposals() == null ? "" :
                            c.getProposals().stream()
                                    .map(p -> p.getTitle() + " " + p.getDescription())
                                    .collect(Collectors.joining(" "));

                    String keywords = c.getPlanKeywords() == null ? "" :
                            String.join(" ", c.getPlanKeywords());

                    String achievementTags = c.getAchievements() == null ? "" :
                            c.getAchievements().stream()
                                    .filter(a -> a.getTags() != null)
                                    .flatMap(a -> a.getTags().stream())
                                    .collect(Collectors.joining(" "));

                    String finalText = proposalsText + " " + keywords + " " + achievementTags;

                    // score de similitud (tú lo decides si lo usas en algún sub-score)
                   TextSimilarityUtil.similarity(question, finalText);

                    // Crear CompositeScore
                    CompositeScore score = new CompositeScore();

                    // Aquí tú decides cómo llenar cada score
                    score.setJudicialScore(
                            ScoringRulesUtils.calculateJudicialScore(c.getHistory())
                    );

                    score.setContributionScore(
                            ScoringRulesUtils.calculateContributionScore(c.getAchievements())
                    );

                    score.setTransparencyScore(
                            ScoringRulesUtils.calculateTransparencyScore(c.getTransparency())
                    );

                    score.setTrustScore(
                            ScoringRulesUtils.calculateTrustScore(c.getTrust())
                    );

                    score.computeFinalScore();

                    return new MatchResponse(c, score);
                })
                .limit(limit)
                .toList();
    }

    public Candidate addHistory(long code, LegalHistoryEntry entry) {

        Candidate candidate = findById(code);

        // Previene NPE
        if (candidate.getHistory() == null) {
            candidate.getHistory();
        }

        candidate.getHistory().add(entry);
        candidate.recalculateScore();

        return repo.save(candidate);
    }
}

