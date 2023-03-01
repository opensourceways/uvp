package org.opensourceway.uvp.batch.step;

import org.apache.commons.collections4.ListUtils;
import org.jetbrains.annotations.NotNull;
import org.opensourceway.uvp.clients.provider.PartialVulnProvider;
import org.opensourceway.uvp.clients.provider.merger.PartialVulnMerger;
import org.opensourceway.uvp.dao.PackageRepository;
import org.opensourceway.uvp.entity.Package;
import org.opensourceway.uvp.pojo.osv.OsvVulnerability;
import org.opensourceway.uvp.utility.OsvEntityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.util.List;
import java.util.stream.Collectors;

public class PartialVulnProviderStep implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(PartialVulnProviderStep.class);

    private final PartialVulnProvider partialVulnProvider;

    private final PartialVulnMerger partialVulnMerger;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private OsvEntityHelper osvEntityHelper;

    public PartialVulnProviderStep(PartialVulnProvider partialVulnProvider, PartialVulnMerger partialVulnMerger) {
        this.partialVulnProvider = partialVulnProvider;
        this.partialVulnMerger = partialVulnMerger;
    }

    @Override
    @Retryable(backoff = @Backoff(delay = 15000, multiplier = 2.0))
    public RepeatStatus execute(@NotNull StepContribution contribution, @NotNull ChunkContext chunkContext) {
        logger.info("Start to import vulnerabilities from <{}>", partialVulnProvider.getVulnSource());

        var purls = packageRepository.findAll().stream()
                .map(Package::getPurl)
                .filter(partialVulnProvider::filter)
                .toList();
        logger.info("Get <{}> purls for <{}>", purls.size(), partialVulnProvider.getVulnSource());

        var vulns = ListUtils.partition(purls, partialVulnProvider.getQueryBatchLimit())
                .stream()
                .map(partialVulnProvider::queryBatch)
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(OsvVulnerability::getId, Collectors.toList()))
                .values()
                .stream()
                .map(partialVulnMerger::merge)
                .map(it -> osvEntityHelper.toVuln(partialVulnProvider.getVulnSource(), it))
                .toList();
        osvEntityHelper.batchUpsert(partialVulnProvider.getVulnSource(), vulns);

        logger.info("End to import vulnerabilities from <{}>", partialVulnProvider.getVulnSource());
        return RepeatStatus.FINISHED;
    }
}
