package org.opensourceway.uvp.batch.step;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ImportVulnEndStep implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(ImportVulnEndStep.class);

    @Override
    public RepeatStatus execute(@NotNull StepContribution contribution, @NotNull ChunkContext chunkContext) {
        logger.info("Import vulns successfully");
        return RepeatStatus.FINISHED;
    }
}
