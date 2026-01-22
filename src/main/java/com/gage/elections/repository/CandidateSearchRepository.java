package com.gage.elections.repository;

import com.gage.elections.model.candidate.Candidate;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CandidateSearchRepository {

    final MongoTemplate mongoTemplate;

    public List<Candidate> searchByAtlas(String query) {
        // La clave es el orden: 1. Search, 2. Add Score Field, 3. Sort por Score
        Aggregation aggregation = Aggregation.newAggregation(
                buildAtlasSearchStage(query),
                // Extraemos el score calculado por Atlas para poder ordenar
                context -> new Document("$addFields",
                        new Document("score", new Document("$meta", "searchScore"))),
                Aggregation.sort(org.springframework.data.domain.Sort.Direction.DESC, "score"),
                Aggregation.limit(20)
        );

        return mongoTemplate.aggregate(
                aggregation,
                "candidates",
                Candidate.class
        ).getMappedResults();
    }

    AggregationOperation buildAtlasSearchStage(String query) {
        return context -> new Document("$search",
                new Document("index", "default")
                        .append("compound", new Document("should", List.of(
                                searchInPlanKeywords(query),
                                searchInNameOnly(query), // Nuevo: prioridad máxima
                                searchInProfileGeneral(query) // Resto de campos
                        ))
                                .append("minimumShouldMatch", 1))
        );
    }

    Document searchInPlanKeywords(String query) {
        return createTextSearch(query, List.of("proposals.keywords"), 3.0, 1);
    }

    // Separamos el nombre para darle un impulso (Boost) mucho más alto
    Document searchInNameOnly(String query) {
        return createTextSearch(query, List.of("name"), 10.0, 1);
    }

    Document searchInProfileGeneral(String query) {
        return createTextSearch(
                query,
                List.of(
                        "biography",
                        "position",
                        "proposals.title",
                        "proposals.description",
                        "proposals.detailDescription",
                        "history.title",
                        "history.description"
                ),
                1.0,
                1 // Fuzzy 1 es más preciso para evitar ruido
        );
    }

    Document createTextSearch(String query, List<String> paths, double boost, int maxEdits) {
        Document text = new Document()
                .append("query", query)
                .append("path", paths.size() == 1 ? paths.get(0) : paths)
                .append("fuzzy", new Document("maxEdits", maxEdits));

        if (boost > 1.0) {
            text.append("score",
                    new Document("boost", new Document("value", boost)));
        }

        return new Document("text", text);
    }
}
