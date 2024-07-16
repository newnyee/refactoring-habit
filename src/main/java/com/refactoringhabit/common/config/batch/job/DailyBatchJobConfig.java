package com.refactoringhabit.common.config.batch.job;

import com.refactoringhabit.common.config.batch.listener.CustomJobListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DailyBatchJobConfig {

    @Bean
    public Job dailyStatsJob(CustomJobListener jobListener, JobRepository jobRepository,
        Step productTotalSalesStatsUpdateStep) {
        return new JobBuilder("statsJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .listener(jobListener)
            .start(productTotalSalesStatsUpdateStep)
            .build();
    }
}
