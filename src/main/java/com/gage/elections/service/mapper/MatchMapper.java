package com.gage.elections.service.mapper;

import com.gage.elections.controller.dto.MatchResponse;
import com.gage.elections.model.candidate.Candidate;
import com.gage.elections.model.candidate.LegalHistoryEntry;
import com.gage.elections.model.candidate.Proposal;
import com.gage.elections.util.SearchUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;
@Mapper(componentModel = "spring")
public abstract class MatchMapper {

    @Mapping(target = "matchType", ignore = true)
    @Mapping(target = "matchTitle", ignore = true)
    @Mapping(target = "matchDescription", ignore = true)
    @Mapping(target = "matchDetailDescription", ignore = true)
    @Mapping(target = "sourcePlan", ignore = true)
    @Mapping(target = "finalScore", source = "candidate.scores.finalScore")
    protected abstract MatchResponse mapCommonFields(Candidate candidate);

    public Optional<MatchResponse> toMatchResponse(Candidate candidate, String search) {
        if (candidate == null || search == null || search.isBlank()) {
            return Optional.empty();
        }

        MatchResponse base = mapCommonFields(candidate);
        String query = SearchUtils.normalize(search);

        // Caso 1: Match en Propuestas
        Optional<Proposal> proposalMatch = findProposalMatch(candidate, query);
        if (proposalMatch.isPresent()) {
            Proposal p = proposalMatch.get();
            return Optional.of(rebuild(
                    base,
                    "PROPUESTA:",
                    p.getTitle(),
                    p.getDescription(),
                    p.getDetailDescription(),
                    p.getSourcePlan()
            ));
        }

        // Caso 2: Match en Antecedentes Judiciales
        Optional<LegalHistoryEntry> historyMatch = findLegalHistoryMatch(candidate, query);
        if (historyMatch.isPresent()) {
            LegalHistoryEntry h = historyMatch.get();
            return Optional.of(rebuild(
                    base,
                    "ANTECEDENTE:",
                    h.getTitle(),
                    h.getExpedientNumber(),
                    h.getDescription(),
                    h.getSource()
            ));
        }

        // Caso 3: Match por Nombre
        if (contains(candidate.getName(), query)) {
            return Optional.of(rebuild(
                    base,
                    "POSICION:",
                    candidate.getName(),
                    candidate.getParty(),
                    candidate.getBiography(),
                    candidate.getPosition()
            ));
        }

        return Optional.empty();
    }


    private Optional<Proposal> findProposalMatch(Candidate candidate, String query) {
        if (candidate.getProposals() == null) return Optional.empty();

        return candidate.getProposals().stream()
                .filter(p ->
                        contains(p.getTitle(), query) ||
                                contains(p.getDescription(), query) ||
                                contains(p.getDetailDescription(), query) ||
                                containsAny(p.getKeywords(), query)
                )
                .findFirst();
    }

    private Optional<LegalHistoryEntry> findLegalHistoryMatch(Candidate candidate, String query) {
        if (candidate.getHistory() == null) return Optional.empty();

        return candidate.getHistory().stream()
                .filter(h ->
                        contains(h.getTitle(), query) ||
                                contains(h.getDescription(), query)
                )
                .findFirst();
    }

    private boolean contains(String field, String query) {
        if (field == null) return false;
        return SearchUtils.normalize(field).contains(query);
    }

    private boolean containsAny(List<String> list, String query) {
        if (list == null) return false;
        return list.stream().anyMatch(v -> contains(v, query));
    }

    private MatchResponse rebuild(
            MatchResponse base,
            String type,
            String matchTitle,
            String matchDescription,
            String matchDetailDescription,
            String sourcePlan
    ) {
        return new MatchResponse(
                base.code(),
                base.name(),
                base.party(),
                base.partyAcronym(),
                base.position(),
                type,
                matchTitle,
                matchDescription,
                matchDetailDescription,
                sourcePlan,
                base.finalScore(),
                base.rankingLevel()
        );
    }
}
