package org.opensourceway.uvp.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchJobConfig {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private Job importVulnJob;

    /**
     * {@link JobLauncher#run} is synchronized, thread will be blocked until the job finishes or fails.
     */
    public void launchImportVulnJob() throws Exception {
        jobLauncher.run(importVulnJob, new JobParametersBuilder()
                .addLong("startTimestamp", System.currentTimeMillis())
                .toJobParameters());
    }
}