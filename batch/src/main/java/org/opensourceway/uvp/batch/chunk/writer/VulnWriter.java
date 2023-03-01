package org.opensourceway.uvp.batch.chunk.writer;

import org.jetbrains.annotations.NotNull;
import org.opensourceway.uvp.entity.Vulnerability;
import org.opensourceway.uvp.enums.VulnSource;
import org.opensourceway.uvp.utility.OsvEntityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class VulnWriter implements ItemWriter<List<Vulnerability>> {

    private static final Logger logger = LoggerFactory.getLogger(VulnWriter.class);

    private final VulnSource vulnSource;

    @Autowired
    private OsvEntityHelper osvEntityHelper;

    public VulnWriter(VulnSource vulnSource) {
        this.vulnSource = vulnSource;
    }

    @Override
    public void write(@NotNull Chunk<? extends List<Vulnerability>> chunk) {
        logger.info("Start to persist batch vulns from <{}>", vulnSource);
        var vulns = chunk.getItems().stream().flatMap(List::stream).distinct().toList();
        osvEntityHelper.batchUpsert(vulnSource, vulns);
        logger.info("End to persist batch vulns from <{}>", vulnSource);
    }
}
