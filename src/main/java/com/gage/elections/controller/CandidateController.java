package com.gage.elections.controller;

import com.gage.elections.controller.dto.MatchRequest;
import com.gage.elections.model.Candidate;
import com.gage.elections.model.LegalHistoryEntry;
import com.gage.elections.service.CandidateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(origins = "*")
public class CandidateController {

    private final CandidateService service;

    public CandidateController(CandidateService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Candidate> create(@RequestBody Candidate candidate) {
        Candidate saved = service.save(candidate);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidate> update(
            @PathVariable long id,
            @RequestBody Candidate candidate) {

        Candidate updated = service.update(id, candidate);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<Candidate>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> get(@PathVariable long id) {
        Candidate c = service.findById(id);
        return c != null ? ResponseEntity.ok(c) : ResponseEntity.notFound().build();
    }

    @PostMapping("/match")
    public ResponseEntity<List<Candidate>> match(
            @RequestBody MatchRequest request) {

        return ResponseEntity.ok(
                service.searchCandidates(request.question())
        );
    }


    // -----------------------------
    // Add Legal History
    // -----------------------------
    @PostMapping("/{code}/history")
    public ResponseEntity<Candidate> addHistory(
            @PathVariable long code,
            @RequestBody LegalHistoryEntry entry) {

        Candidate updated = service.addHistory(code, entry);
        return ResponseEntity.ok(updated);
    }

}

