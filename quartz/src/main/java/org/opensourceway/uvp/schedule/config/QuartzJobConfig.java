package org.opensourceway.uvp.schedule.config;

import org.opensourceway.uvp.schedule.job.ImportVulnJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzJobConfig {

    @Bean
    public JobDetail importVulnJobDetail() {
        return JobBuilder.newJob(ImportVulnJob.class)
                .withIdentity("importVuln")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger importVulnJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(importVulnJobDetail())
                .withIdentity("importVuln")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 2 * * ? *"))
                .build();
    }
}
