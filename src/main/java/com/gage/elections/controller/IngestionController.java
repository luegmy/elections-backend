package com.gage.elections.controller;

import com.gage.elections.client.ElectionIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/ingestion")
@RequiredArgsConstructor
public class IngestionController {

    private final ElectionIngestionService ingestionService;

    @PostMapping("/sync-candidates")
    public ResponseEntity<Map<String, String>> syncAll() {
        // Ejecutamos la lógica de ingesta
        ingestionService.sincronizarTodo();

        return ResponseEntity.ok(Map.of(
                "status", "Proceso de sincronización iniciado",
                "message", "Se están procesando candidatos presidenciales, senadores y diputados."
        ));
    }
}
