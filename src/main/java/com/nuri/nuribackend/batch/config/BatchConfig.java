package com.nuri.nuribackend.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job monthlyFeedbackJob(Step monthlyFeedbackStep) {
        return new JobBuilder("monthlyFeedbackJob", jobRepository)
                .start(monthlyFeedbackStep)
                .build();
    }

    @Bean
    public Step monthlyFeedbackStep(Tasklet monthlyFeedbackTasklet) {
        return new StepBuilder("monthlyFeedbackStep", jobRepository)
                .tasklet(monthlyFeedbackTasklet, transactionManager)
                .build();
    }
}
