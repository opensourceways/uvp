package org.opensourceway.uvp.batch.chunk.reader.dumper;

import org.opensourceway.uvp.clients.Nvd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

public class NvdReader implements ItemReader<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(NvdReader.class);

    @Value("${nvd.api.page_size}")
    private Integer nvdPageSize;

    @Autowired
    private Nvd nvd;

    private Integer startIndex = null;

    private Integer totalResults = Integer.MIN_VALUE;

    @Override
    public Integer read() {
        if (totalResults == Integer.MIN_VALUE) {
            initMapper();
        }

        // Failed to get totalResults, pass Integer.MIN_VALUE to processor to retry.
        if (totalResults == Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }

        // End of loop.
        if (totalResults <= startIndex) {
            return null;
        }

        return startIndex += nvdPageSize;
    }

    private void initMapper() {
        logger.info("Query NVD CVE total size");

        var resp = nvd.dumpBatch(0);
        // Maybe some failure occur, try to call NVD API next time.
        if (Objects.isNull(resp)) {
            logger.warn("Get null response from NVD.");
            return;
        }

        totalResults = resp.getTotalResults();
        startIndex = -nvdPageSize;
        logger.info("Number of NVD CVE: <{}>.", totalResults);
    }
}
