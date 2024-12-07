package com.nuri.nuribackend.batch.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job processFeedbackJob;

    public BatchScheduler(JobLauncher jobLauncher, Job processFeedbackJob) {
        this.jobLauncher = jobLauncher;
        this.processFeedbackJob = processFeedbackJob;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void runFeedbackJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(processFeedbackJob, jobParameters);
        } catch (Exception e) {
            log.error("배치처리 하는 도중 에러 발생함.", e);
        }
    }
}
