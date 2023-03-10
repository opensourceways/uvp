package org.opensourceway.uvp.schedule.job;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.opensourceway.uvp.entity.VulnSourceConfig;
import org.opensourceway.uvp.enums.VulnSource;
import org.opensourceway.uvp.schedule.constant.QuartzConstant;
import org.opensourceway.uvp.utility.VulnSourceConfigUtil;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Proactively capture changes of {@link VulnSourceConfig#getImportCron()}.
 * If importCron changes, then reschedule corresponding import job.
 */
public class CaptureImportCronJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(CaptureImportCronJob.class);

    private static final Map<VulnSource, String> sourceToCron = new HashMap<>();

    @Autowired
    private VulnSourceConfigUtil vulnSourceConfigUtil;

    @Autowired
    private ApplicationContext context;

    /**
     * Record the import crons when this job was initialized for the first time.
     */
    @PostConstruct
    private void recordImportCron() {
        Arrays.stream(VulnSource.values()).forEach(source ->
                sourceToCron.putIfAbsent(source, vulnSourceConfigUtil.getImportCron(source)));
    }

    protected void executeInternal(@NotNull JobExecutionContext quartzJobContext) {
        Arrays.stream(VulnSource.values()).forEach(this::captureChange);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void captureChange(VulnSource source) {
        var oldCron = sourceToCron.get(source);
        var newCron = vulnSourceConfigUtil.getImportCron(source);

        if (StringUtils.equals(oldCron, newCron)) {
            return;
        }

        Scheduler scheduler = (Scheduler) context.getBean(QuartzConstant.QUARTZ_SCHEDULER);
        Trigger oldTrigger;
        try {
            oldTrigger = scheduler.getTrigger(TriggerKey.triggerKey(QuartzConstant.SOURCE_TO_TRIGGER.get(source)));
        } catch (Exception e) {
            // If no trigger is found, it means the source is not configured in QuartzJobConfig.
            return;
        }

        try {
            TriggerBuilder builder = oldTrigger.getTriggerBuilder();
            var trigger = builder.withSchedule(CronScheduleBuilder.cronSchedule(newCron)).build();
            scheduler.rescheduleJob(oldTrigger.getKey(), trigger);
        } catch (Exception e) {
            // If failed to change import cron, former trigger will be kept.
            logger.warn("Failed to change import cron for <{}> from <{}> to <{}>", source, oldCron, newCron);
            return;
        }

        sourceToCron.put(source, newCron);
        logger.info("Import cron for <{}> is changed from <{}> to <{}>", source, oldCron, newCron);
    }
}
