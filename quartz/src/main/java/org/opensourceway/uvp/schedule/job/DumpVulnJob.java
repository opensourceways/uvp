package org.opensourceway.uvp.schedule.job;

import org.jetbrains.annotations.NotNull;
import org.opensourceway.uvp.batch.job.BatchJobConfig;
import org.opensourceway.uvp.enums.VulnSource;
import org.opensourceway.uvp.schedule.constant.QuartzConstant;
import org.opensourceway.uvp.utility.VulnSourceConfigUtil;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class DumpVulnJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(DumpVulnJob.class);

    @Autowired
    private BatchJobConfig batchJobConfig;

    @Autowired
    private VulnSourceConfigUtil vulnSourceConfigUtil;

    protected void executeInternal(@NotNull JobExecutionContext quartzJobContext) {
        VulnSource source = (VulnSource) quartzJobContext.getMergedJobDataMap().get(QuartzConstant.VULN_SOURCE_KEY);
        logger.info("Launch DumpVulnJob for <{}>", source);

        if (!vulnSourceConfigUtil.importEnabled(source)) {
            logger.info("DumpVulnJob for <{}> is disabled!", source);
            return;
        }

        try {
            batchJobConfig.launchDumpJob(source);
        } catch (Exception e) {
            logger.error("Exception occurs when execute DumpVulnJob for <{}>", source, e);
        }
        logger.info("Finish DumpVulnJob for <{}>.", source);
    }
}
