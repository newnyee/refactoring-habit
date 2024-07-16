package com.refactoringhabit.common.config.batch.listener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.STARTED) {
            log.info("{} start", jobExecution.getJobInstance().getJobName());
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("{} completed", jobExecution.getJobInstance().getJobName());

            LocalDateTime startTime = jobExecution.getStartTime();
            LocalDateTime endTime = LocalDateTime.now();

            long totalExecutionMilliSeconds = Duration.between(startTime, endTime).toMillis();

            log.info("{} total execution time : {}H {}m {}s {}ms",
                jobExecution.getJobInstance().getJobName(),
                TimeUnit.MILLISECONDS.toHours(totalExecutionMilliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(totalExecutionMilliSeconds) % 60,
                TimeUnit.MILLISECONDS.toSeconds(totalExecutionMilliSeconds) % 60,
                totalExecutionMilliSeconds % 1000);
        }
    }
}
