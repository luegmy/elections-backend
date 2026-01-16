package com.gage.elections.repository;

import com.gage.elections.model.candidate.Candidate;
import com.gage.elections.repository.projection.CandidateListView;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends MongoRepository<Candidate, String> {

    Optional<Candidate> findByCode(String code);
    List<CandidateListView> findByPosition (String position);
}
