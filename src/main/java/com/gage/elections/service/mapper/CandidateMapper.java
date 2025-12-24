package com.gage.elections.service.mapper;

import com.gage.elections.controller.dto.CandidateCreateRequest;
import com.gage.elections.model.candidate.Candidate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidateMapper {
    Candidate toCandidate (CandidateCreateRequest request);
}
