package com.refactoringhabit.stats.domain.scheduler;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobRestartException;

@ExtendWith(MockitoExtension.class)
class StatsSchedulerTest {

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private JobRegistry jobRegistry;

    @Mock
    private Job job;

    @InjectMocks
    private StatsScheduler statsScheduler;

    @Test
    void testRunDailyJob_Success() throws JobExecutionException {
        when(jobRegistry.getJob("statsJob")).thenReturn(job);

        statsScheduler.runDailyJob();
        verify(jobLauncher, times(1)).run(any(Job.class), any());
    }

    @Test
    void testRunDailyJob_NoSuchJobException() throws JobExecutionException {
        when(jobRegistry.getJob("statsJob")).thenThrow(NoSuchJobException.class);

        statsScheduler.runDailyJob();
        verify(jobLauncher, times(0)).run(any(Job.class), any());
    }

    @Test
    void testRunDailyJob_JobRestartException() throws JobExecutionException {
        when(jobRegistry.getJob("statsJob")).thenReturn(job);
        when(jobLauncher.run(any(Job.class), any())).thenThrow(JobRestartException.class);

        statsScheduler.runDailyJob();
        verify(jobLauncher, times(1)).run(any(Job.class), any());
    }
}