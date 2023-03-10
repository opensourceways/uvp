package org.opensourceway.uvp.batch.job;

import org.opensourceway.uvp.batch.constant.BatchConstant;
import org.opensourceway.uvp.enums.VulnSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;
import java.util.Random;

@Configuration
public class BatchJobConfig {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private Map<String, Job> jobs;

    /**
     * {@link JobLauncher#run} is synchronized, thread will be blocked until the job finishes or fails.
     */
    public void launchDumpJob(VulnSource source) throws Exception {
        // Sleep random seconds to avoid database exception:
        // Reason code: Canceled on identification as a pivot, during conflict out checking.
        Thread.sleep(new Random().nextInt(500, 5000));
        jobLauncher.run(jobs.get(BatchConstant.SOURCE_TO_JOB_NAME.get(source)), new JobParametersBuilder()
                .addLong("startTimestamp", System.currentTimeMillis())
                .toJobParameters());
    }
}