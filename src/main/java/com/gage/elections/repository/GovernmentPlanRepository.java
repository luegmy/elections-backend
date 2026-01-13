package com.gage.elections.repository;

import com.gage.elections.model.candidate.GovernmentPlan;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GovernmentPlanRepository extends MongoRepository<GovernmentPlan, String> {
}
