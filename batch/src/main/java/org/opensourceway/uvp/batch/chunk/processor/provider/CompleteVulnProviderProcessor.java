package org.opensourceway.uvp.batch.chunk.processor.provider;

import org.jetbrains.annotations.NotNull;
import org.opensourceway.uvp.clients.provider.CompleteVulnProvider;
import org.opensourceway.uvp.entity.Vulnerability;
import org.opensourceway.uvp.utility.OsvEntityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

public class CompleteVulnProviderProcessor implements ItemProcessor<List<String>, List<Vulnerability>> {

    private static final Logger logger = LoggerFactory.getLogger(CompleteVulnProviderProcessor.class);

    private final CompleteVulnProvider completeVulnProvider;

    @Autowired
    private OsvEntityHelper osvEntityHelper;

    public CompleteVulnProviderProcessor(CompleteVulnProvider completeVulnProvider) {
        this.completeVulnProvider = completeVulnProvider;
    }

    @Override
    public List<Vulnerability> process(@NotNull List<String> purls) {
        logger.info("Start to query vulns from {}", completeVulnProvider.getVulnSource());
        try {
            var vulns = completeVulnProvider.queryBatch(purls).stream()
                    .distinct()
                    .map(osvVulnerability ->
                            osvEntityHelper.toVuln(completeVulnProvider.getVulnSource(), osvVulnerability))
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();
            logger.info("End to query vulns from {}, number of vulns: <{}>",
                    completeVulnProvider.getVulnSource(), vulns.size());
            return vulns;
        } catch (Exception e) {
            logger.warn("Exception occurs when query vulns from <{}>, purls: {}",
                    completeVulnProvider.getVulnSource(), purls, e);
            throw new RuntimeException(e);
        }
    }
}
