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
    @Mapping(target = "searchScore", source = "scores.finalScore") // Usamos el score del candidato como base
    @Mapping(target = "rankingLevel", source = "rankingLevel")
    protected abstract MatchResponse mapCommonFields(Candidate c);

    // 2. Lógica de "Hallazgo" (Match)
    public MatchResponse toMatchResponse(Candidate c, String search) {
        if (c == null) return null;

        MatchResponse dto = mapCommonFields(c);
        String query = search != null ? search.toLowerCase().trim() : "";

        // Cascada de prioridades para el Snippet y el MatchType
        if (contains(c.getName(), query)) {
            return rebuild(dto, "NOMBRE", "Candidato: " + c.getName());
        }

        if (containsAny(c.getPlanKeywords(), query)) {
            return rebuild(dto, "PLAN_GOBIERNO", "Coincidencia en palabras clave del plan estratégico.");
        }

        return findProposalMatch(c, query)
                .map(p -> rebuild(dto, "PROPUESTA", p.getTitle() + ": " + p.getDescription()))
                .orElseGet(() ->
                        findLegalHistoryMatch(c, query)
                                .map(h -> rebuild(dto, "ANTECEDENTE", h.getTitle() + ": " + h.getDescription()))
                                .orElse(rebuild(dto, "GENERAL", "Perfil del candidato verificado."))
                );
    }

    /**
     * Reconstruye el Record con la información específica del hallazgo.
     */
    private MatchResponse rebuild(MatchResponse b, String type, String snippet) {
        // En Java 17, los records se instancian pasando todos los argumentos
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

    // --- Helpers de búsqueda ---

    private Optional<Proposal> findProposalMatch(Candidate c, String query) {
        if (c.getProposals() == null) return Optional.empty();
        return c.getProposals().stream()
                .filter(p -> contains(p.getTitle(), query) || contains(p.getDescription(), query))
                .findFirst();
    }

    private Optional<LegalHistoryEntry> findLegalHistoryMatch(Candidate c, String query) {
        if (c.getHistory() == null) return Optional.empty();
        return c.getHistory().stream()
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
