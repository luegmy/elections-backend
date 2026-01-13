package com.gage.elections.model.scoring;

public record RuleKey(LegalStatus status, IncidentSeverity severity, LegalCategory category) {
}
