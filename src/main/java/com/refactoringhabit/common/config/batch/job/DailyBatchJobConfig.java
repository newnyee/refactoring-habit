package com.refactoringhabit.common.config.batch.job;

import com.refactoringhabit.common.config.batch.listener.CustomJobListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DailyBatchJobConfig {

    @Bean
    public Job dailyStatsJob(CustomJobListener jobListener, JobRepository jobRepository,
        @Qualifier("productTotalSalesStatsUpdateStep") Step productTotalSalesStatsUpdateStep,
        @Qualifier("hostTotalSalesStatsUpdateStep") Step hostTotalSalesStatsUpdateStep) {
        return new JobBuilder("statsJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .listener(jobListener)
            .start(productTotalSalesStatsUpdateStep)
            .next(hostTotalSalesStatsUpdateStep)
            .build();
    }
}
