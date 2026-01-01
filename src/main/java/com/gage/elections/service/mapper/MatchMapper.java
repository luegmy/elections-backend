package com.gage.elections.service.mapper;

import com.gage.elections.controller.dto.MatchResponse;
import com.gage.elections.model.candidate.Candidate;
import com.gage.elections.model.candidate.LegalHistoryEntry;
import com.gage.elections.model.candidate.Proposal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class MatchMapper {

    @Mapping(target = "matchType", ignore = true)
    @Mapping(target = "matchSnippet", ignore = true)
    @Mapping(target = "finalScore", source = "candidate.scores.finalScore")
    protected abstract MatchResponse mapCommonFields(Candidate candidate);

    public MatchResponse toMatchResponse(Candidate candidate, String search) {
        if (candidate == null) return null;

        MatchResponse dto = mapCommonFields(candidate);
        String query = search != null ? search.toLowerCase().trim() : "";

        if (contains(candidate.getName(), query)) {
            return rebuild(dto, "NOMBRE", "Candidato: " + candidate.getName());
        }

        if (containsAny(candidate.getPlanKeywords(), query)) {
            return rebuild(dto, "PLAN_GOBIERNO", "Coincidencia en palabras clave del plan estratégico.");
        }

        return findProposalMatch(candidate, query)
                .map(p -> rebuild(dto, "PROPUESTA", p.getTitle() + ": " + p.getDescription()))
                .orElseGet(() ->
                        findLegalHistoryMatch(candidate, query)
                                .map(h -> rebuild(dto, "ANTECEDENTE", h.getTitle() + ": " + h.getDescription()))
                                .orElse(rebuild(dto, "GENERAL", "Perfil del candidato verificado."))
                );
    }

    private MatchResponse rebuild(MatchResponse b, String type, String snippet) {
        return new MatchResponse(
                b.code(),
                b.name(),
                b.party(),
                b.partyAcronym(),
                b.position(),
                type,
                snippet,
                b.finalScore(),
                b.rankingLevel()
        );
    }

    private Optional<Proposal> findProposalMatch(Candidate candidate, String query) {
        if (candidate.getProposals() == null) return Optional.empty();
        return candidate.getProposals().stream()
                .filter(p -> contains(p.getTitle(), query) || contains(p.getDescription(), query))
                .findFirst();
    }

    private Optional<LegalHistoryEntry> findLegalHistoryMatch(Candidate candidate, String query) {
        if (candidate.getHistory() == null) return Optional.empty();
        return candidate.getHistory().stream()
                .filter(h -> contains(h.getTitle(), query) || contains(h.getDescription(), query))
                .findFirst();
    }

    private boolean contains(String field, String query) {
        return field != null && field.toLowerCase().contains(query);
    }

    private boolean containsAny(List<String> list, String query) {
        return list != null && list.stream().anyMatch(v -> contains(v, query));
    }
}
