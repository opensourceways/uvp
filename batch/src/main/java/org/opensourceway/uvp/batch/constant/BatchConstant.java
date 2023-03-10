package org.opensourceway.uvp.batch.constant;

import org.opensourceway.uvp.enums.VulnSource;

import java.util.Map;

public class BatchConstant {
    // Job names are defined in import-vuln.xml
    public static final String DUMP_FROM_OSV_JOB_NAME = "dumpFromOsvJob";

    public static final String DUMP_FROM_NVD_JOB_NAME = "dumpFromNvdJob";

    public static final String DUMP_FROM_OSS_INDEX_JOB_NAME = "dumpFromOssIndexJob";

    public static final Map<VulnSource, String> SOURCE_TO_JOB_NAME = Map.of(
            VulnSource.OSV, DUMP_FROM_OSV_JOB_NAME,
            VulnSource.NVD, DUMP_FROM_NVD_JOB_NAME,
            VulnSource.OSS_INDEX, DUMP_FROM_OSS_INDEX_JOB_NAME
    );
}
