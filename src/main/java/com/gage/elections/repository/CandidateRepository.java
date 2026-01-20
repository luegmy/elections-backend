package com.gage.elections.repository;

import com.gage.elections.model.candidate.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CandidateRepository extends MongoRepository<Candidate, String> {
    Optional<Candidate> findByCode(String code);
    Page<Candidate> findByPositionIgnoreCase(String position, Pageable pageable);
}
