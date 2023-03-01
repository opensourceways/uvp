package org.opensourceway.uvp.batch.chunk.reader.provider;

import org.apache.commons.collections4.ListUtils;
import org.opensourceway.uvp.clients.provider.CompleteVulnProvider;
import org.opensourceway.uvp.dao.PackageRepository;
import org.opensourceway.uvp.entity.Package;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class CompleteVulnProviderReader implements ItemReader<List<String>> {

    private static final Logger logger = LoggerFactory.getLogger(CompleteVulnProviderReader.class);

    private List<List<String>> purlChunks;

    private final CompleteVulnProvider completeVulnProvider;

    @Autowired
    private PackageRepository packageRepository;

    public CompleteVulnProviderReader(CompleteVulnProvider completeVulnProvider) {
        this.completeVulnProvider = completeVulnProvider;
    }

    @Override
    public List<String> read() {
        logger.info("Start to read packages for querying vulns.");

        if (Objects.isNull(purlChunks)) {
            initMapper();
        }

        if (ObjectUtils.isEmpty(purlChunks)) {
            // End of the chunks loops.
            return null;
        }
        return purlChunks.remove(0);
    }

    private void initMapper() {
        var purls = packageRepository.findAll().stream().map(Package::getPurl).toList();
        purlChunks = ListUtils.partition(purls, completeVulnProvider.getQueryBatchLimit())
                .stream()
                // Can't use ArrayList.subList (can't restart).
                .map(ArrayList::new)
                // Can't use unmodifiableList (can't remove).
                .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
        logger.info("Number of packages to query: <{}>, number of chunks: <{}>", purls.size(), purlChunks.size());
    }
}
