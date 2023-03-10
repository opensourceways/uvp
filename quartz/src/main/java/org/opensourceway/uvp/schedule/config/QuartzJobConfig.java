package org.opensourceway.uvp.schedule.config;

import org.opensourceway.uvp.enums.VulnSource;
import org.opensourceway.uvp.schedule.constant.QuartzConstant;
import org.opensourceway.uvp.schedule.job.CaptureImportCronJob;
import org.opensourceway.uvp.schedule.job.DumpVulnJob;
import org.opensourceway.uvp.utility.VulnSourceConfigUtil;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class QuartzJobConfig {

    @Autowired
    private VulnSourceConfigUtil vulnSourceConfigUtil;

    @Bean
    public JobDetail captureImportCronJobDetail() {
        return JobBuilder.newJob(CaptureImportCronJob.class)
                .withIdentity(QuartzConstant.CAPTURE_IMPORT_CRON_JOB_DETAIL_ID)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger captureImportCronJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(captureImportCronJobDetail())
                .withIdentity(QuartzConstant.CAPTURE_IMPORT_CRON_TRIGGER_ID)
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("*/10 * * * * ? *"))
                .build();
    }

    @Bean
    public JobDetail dumpFromOsvJobDetail() {
        return JobBuilder.newJob(DumpVulnJob.class)
                .withIdentity(QuartzConstant.DUMP_FROM_OSV_JOB_DETAIL_ID)
                .storeDurably()
                .usingJobData(new JobDataMap(Map.of(QuartzConstant.VULN_SOURCE_KEY, VulnSource.OSV)))
                .build();
    }

    @Bean
    public Trigger dumpFromOsvJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(dumpFromOsvJobDetail())
                .withIdentity(QuartzConstant.DUMP_FROM_OSV_TRIGGER_ID)
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(vulnSourceConfigUtil.getImportCron(VulnSource.OSV)))
                .build();
    }

    @Bean
    public JobDetail dumpFromNvdJobDetail() {
        return JobBuilder.newJob(DumpVulnJob.class)
                .withIdentity(QuartzConstant.DUMP_FROM_NVD_JOB_DETAIL_ID)
                .storeDurably()
                .usingJobData(new JobDataMap(Map.of(QuartzConstant.VULN_SOURCE_KEY, VulnSource.NVD)))
                .build();
    }

    @Bean
    public Trigger dumpFromNvdJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(dumpFromNvdJobDetail())
                .withIdentity(QuartzConstant.DUMP_FROM_NVD_TRIGGER_ID)
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(vulnSourceConfigUtil.getImportCron(VulnSource.NVD)))
                .build();
    }

    @Bean
    public JobDetail dumpFromOssIndexJobDetail() {
        return JobBuilder.newJob(DumpVulnJob.class)
                .withIdentity(QuartzConstant.DUMP_FROM_OSS_INDEX_JOB_DETAIL_ID)
                .storeDurably()
                .usingJobData(new JobDataMap(Map.of(QuartzConstant.VULN_SOURCE_KEY, VulnSource.OSS_INDEX)))
                .build();
    }

    @Bean
    public Trigger dumpFromOssIndexJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(dumpFromOssIndexJobDetail())
                .withIdentity(QuartzConstant.DUMP_FROM_OSS_INDEX_TRIGGER_ID)
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(vulnSourceConfigUtil.getImportCron(VulnSource.OSS_INDEX)))
                .build();
    }
}
