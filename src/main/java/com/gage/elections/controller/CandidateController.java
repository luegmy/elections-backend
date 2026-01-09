package com.gage.elections.controller;


import com.gage.elections.controller.dto.request.CandidateCreateRequest;
import com.gage.elections.controller.dto.MatchResponse;
import com.gage.elections.model.candidate.*;
import com.gage.elections.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(origins =
        {
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
    public ResponseEntity<String> createAll(@RequestBody List<Candidate> candidates) {
        candidateService.createCandidates(candidates);
        return ResponseEntity.ok("Candidatos agregados correctamente");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Candidate> update(
            @PathVariable String id,
            @RequestBody Candidate candidate) {

        Candidate updated = candidateService.updateCandidate(id, candidate);
        return ResponseEntity.ok(updated);
    }

    // PATCH: actualizar solo history
    @PatchMapping("/{id}/history")
    public ResponseEntity<Candidate> updateHistory(
            @PathVariable String id,
            @RequestBody List<LegalHistoryEntry> history) {

        return ResponseEntity.ok(candidateService.updateHistory(id, history));
    }

    // PATCH: actualizar solo proposals
    @PatchMapping("/{id}/proposals")
    public ResponseEntity<Candidate> updateProposals(
            @PathVariable String id,
            @RequestBody List<Proposal> proposals) {

        return ResponseEntity.ok(candidateService.updateProposals(id, proposals));
    }

    // PATCH: transparencia
    @PatchMapping("/{id}/transparency")
    public ResponseEntity<Candidate> updateTransparency(
            @PathVariable String id,
            @RequestBody Transparency transparency) {

        return ResponseEntity.ok(candidateService.updateTransparency(id, transparency));
    }

    // PATCH: confianza / trust
    @PatchMapping("/{id}/trust")
    public ResponseEntity<Candidate> updateTrust(
            @PathVariable String id,
            @RequestBody Trust trust) {

        return ResponseEntity.ok(candidateService.updateTrust(id, trust));
    }

    // PATCH: achievements
    @PatchMapping("/{id}/achievements")
    public ResponseEntity<Candidate> updateAchievements(
            @PathVariable String id,
            @RequestBody List<Achievement> achievements) {

        return ResponseEntity.ok(candidateService.updateAchievements(id, achievements));
    }

    @GetMapping
    public ResponseEntity<List<Candidate>> getAll() {
        return ResponseEntity.ok(candidateService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> get(@PathVariable String id) {
        return ResponseEntity.ok(candidateService.getCandidateByCode(id));
    }

    @GetMapping("/match")
    public ResponseEntity<List<MatchResponse>> match(
            @RequestParam String query) {
        return ResponseEntity.ok(
                candidateService.searchCandidatesAtlas(query)
        );
    }
}

