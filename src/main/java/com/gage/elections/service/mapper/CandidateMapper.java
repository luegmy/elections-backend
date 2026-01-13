package com.gage.elections.service.mapper;

import com.gage.elections.controller.dto.LegalHistoryEntryRequest;
import com.gage.elections.controller.dto.request.CandidateCreateRequest;
import com.gage.elections.controller.dto.request.CandidateUpdateRequest;
import com.gage.elections.model.candidate.Candidate;
import com.gage.elections.model.candidate.LegalHistoryEntry;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface CandidateMapper {
    Candidate toCandidate (CandidateCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void patch(CandidateUpdateRequest request, @MappingTarget Candidate candidate);


    LegalHistoryEntry toLegalHistory(LegalHistoryEntryRequest request);

    List<LegalHistoryEntry> toLegalHistoryList(List<LegalHistoryEntryRequest> requests);

}
