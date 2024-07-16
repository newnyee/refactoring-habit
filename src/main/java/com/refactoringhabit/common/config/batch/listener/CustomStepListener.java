package com.refactoringhabit.common.config.batch.listener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
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

            LocalDateTime startTime = stepExecution.getStartTime();
            LocalDateTime endTime = LocalDateTime.now();

            long totalExecutionMilliSeconds = Duration.between(startTime, endTime).toMillis();

            log.info("{} total execution time : {}H {}m {}s {}ms",
                stepExecution.getStepName(),
                TimeUnit.MILLISECONDS.toHours(totalExecutionMilliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(totalExecutionMilliSeconds) % 60,
                TimeUnit.MILLISECONDS.toSeconds(totalExecutionMilliSeconds) % 60,
                totalExecutionMilliSeconds % 1000);
        }
        return ExitStatus.COMPLETED;
    }
}
