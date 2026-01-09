package com.gage.elections.service.mapper;

import com.gage.elections.controller.dto.request.CandidateCreateRequest;
import com.gage.elections.model.candidate.Candidate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidateMapper {
    Candidate toCandidate (CandidateCreateRequest request);
}
