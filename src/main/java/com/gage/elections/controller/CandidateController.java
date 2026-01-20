package com.gage.elections.controller;

import com.gage.elections.controller.dto.request.CandidateCreateRequest;
import com.gage.elections.controller.dto.request.CandidateUpdateRequest;
import com.gage.elections.controller.dto.response.MatchResponse;
import com.gage.elections.controller.dto.response.CandidateResponse;
import com.gage.elections.model.candidate.*;
import com.gage.elections.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(origins = {
        "http://localhost:4200",
        "https://eleccionesperu2026.vercel.app/"
})
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody CandidateCreateRequest candidate) {
        candidateService.createCandidate(candidate);
        return ResponseEntity.ok("Candidatos agregados correctamente");
    }

    @PostMapping("/batch")
    public ResponseEntity<String> createAll(@RequestBody List<CandidateCreateRequest> candidates) {
        candidateService.createCandidates(candidates);
        return ResponseEntity.ok("Candidatos agregados correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidate> updateAll(
            @PathVariable String id,
            @RequestBody CandidateCreateRequest candidate) {
        Candidate updated = candidateService.updateCandidateAll(id, candidate);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Candidate> update(
            @PathVariable String id,
            @RequestBody CandidateUpdateRequest candidate) {
        Candidate updated = candidateService.updateCandidate(id, candidate);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/history")
    public ResponseEntity<Candidate> updateHistory(
            @PathVariable String id,
            @RequestBody List<LegalHistoryEntry> history) {
        return ResponseEntity.ok(candidateService.updateHistory(id, history));
    }

    @PatchMapping("/{id}/proposals")
    public ResponseEntity<Candidate> updateProposals(
            @PathVariable String id,
            @RequestBody GovernmentPlan proposals) {
        return ResponseEntity.ok(candidateService.updateProposals(id, proposals));
    }

    @PatchMapping("/{id}/transparency")
    public ResponseEntity<Candidate> updateTransparency(
            @PathVariable String id,
            @RequestBody Transparency transparency) {
        return ResponseEntity.ok(candidateService.updateTransparency(id, transparency));
    }

    @PatchMapping("/{id}/trust")
    public ResponseEntity<Candidate> updateTrust(
            @PathVariable String id,
            @RequestBody Trust trust) {
        return ResponseEntity.ok(candidateService.updateTrust(id, trust));
    }

    @PatchMapping("/{id}/achievements")
    public ResponseEntity<Candidate> updateAchievements(
            @PathVariable String id,
            @RequestBody List<Achievement> achievements) {
        return ResponseEntity.ok(candidateService.updateAchievements(id, achievements));
    }

    @GetMapping
    public ResponseEntity<Page<CandidateResponse>> getAll(
            @RequestParam(required = false, defaultValue = "all") String position,
            @PageableDefault(sort = "rankingLevel", direction = Sort.Direction.ASC) Pageable pageable)  {
        return ResponseEntity.ok(candidateService.findAll(position, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> get(@PathVariable String id) {
        return ResponseEntity.ok(candidateService.getCandidateByCode(id));
    }

    @GetMapping("/match")
    public ResponseEntity<List<MatchResponse>> match(@RequestParam String query) {
        return ResponseEntity.ok(candidateService.searchCandidatesAtlas(query));
    }
}
