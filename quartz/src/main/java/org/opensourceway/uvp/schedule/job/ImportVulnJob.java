package org.opensourceway.uvp.schedule.job;

import org.jetbrains.annotations.NotNull;
import org.opensourceway.uvp.batch.job.BatchJobConfig;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ImportVulnJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(ImportVulnJob.class);

    @Autowired
    private BatchJobConfig batchJobConfig;

    protected void executeInternal(@NotNull JobExecutionContext quartzJobContext) {
        logger.info("Launch ImportVulnJob");
        try {
            batchJobConfig.launchImportVulnJob();
        } catch (Exception e) {
            logger.error("Exception occurs when execute ImportVulnJob", e);
        }
        logger.info("Finish ImportVulnJob.");
    }
}
