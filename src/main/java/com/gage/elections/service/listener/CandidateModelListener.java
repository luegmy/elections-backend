package com.gage.elections.service.listener;

import com.gage.elections.model.candidate.Candidate;
import com.gage.elections.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CandidateModelListener extends AbstractMongoEventListener<Candidate> {
    final SequenceGeneratorService sequenceGenerator;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Candidate> event) {
        if (event.getSource().getCode() == null) {
            event.getSource().setCode(String.valueOf(
                    sequenceGenerator.generateSequence(Candidate.SEQUENCE_NAME)));
        }
    }
}
