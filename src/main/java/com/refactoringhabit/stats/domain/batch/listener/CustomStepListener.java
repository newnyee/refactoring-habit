package com.refactoringhabit.stats.domain.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        if (stepExecution.getStatus() == BatchStatus.STARTED) {
            log.info("{} start", stepExecution.getStepName());
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("{} completed", stepExecution.getStepName());
        }
        return ExitStatus.COMPLETED;
    }
}
