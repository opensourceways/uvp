package org.opensourceway.uvp.schedule.constant;

import org.opensourceway.uvp.enums.VulnSource;

import java.util.Map;

public class QuartzConstant {
    public static final String VULN_SOURCE_KEY = "vulnSource";

    public static final String QUARTZ_SCHEDULER = "quartzScheduler";

    public static final String CAPTURE_IMPORT_CRON_JOB_DETAIL_ID = "captureImportCronJobDetail";

    public static final String CAPTURE_IMPORT_CRON_TRIGGER_ID = "captureImportCronJobTrigger";

    public static final String DUMP_FROM_OSV_JOB_DETAIL_ID = "dumpFromOsvJobDetail";

    public static final String DUMP_FROM_OSV_TRIGGER_ID = "dumpFromOsvJobTrigger";

    public static final String DUMP_FROM_NVD_JOB_DETAIL_ID = "dumpFromNvdJobDetail";

    public static final String DUMP_FROM_NVD_TRIGGER_ID = "dumpFromNvdJobTrigger";

    public static final String DUMP_FROM_OSS_INDEX_JOB_DETAIL_ID = "dumpFromOssIndexJobDetail";

    public static final String DUMP_FROM_OSS_INDEX_TRIGGER_ID = "dumpFromOssIndexJobTrigger";

    public static final Map<VulnSource, String> SOURCE_TO_TRIGGER = Map.of(
            VulnSource.OSV, DUMP_FROM_OSV_TRIGGER_ID,
            VulnSource.NVD, DUMP_FROM_NVD_TRIGGER_ID,
            VulnSource.OSS_INDEX, DUMP_FROM_OSS_INDEX_TRIGGER_ID
    );
}
