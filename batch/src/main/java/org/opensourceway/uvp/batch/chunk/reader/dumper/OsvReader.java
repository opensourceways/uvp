package org.opensourceway.uvp.batch.chunk.reader.dumper;

import org.opensourceway.uvp.utility.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class OsvReader implements ItemReader<String> {
    
    private static final Logger logger = LoggerFactory.getLogger(OsvReader.class);

    private static final String ECOSYSTEMS_TXT = "ecosystems.txt";

    @Value("${osv.bucket.url}")
    private String osvBucketBaseUrl;

    @Autowired
    private WebUtil webUtil;

    private List<String> ecosystems;

    @Override
    public String read() {
        if (Objects.isNull(ecosystems)) {
            synchronized (this) {
                initMapper();
            }
        }

        if (ObjectUtils.isEmpty(ecosystems)) {
            // End of the chunks loops.
            return null;
        }

        return ecosystems.remove(0);
    }

    private void initMapper() {
        if (Objects.nonNull(ecosystems)) {
            return;
        }

        logger.info("Start to get osv ecosystems for downloading vulns.");

        var url = "%s/%s".formatted(this.osvBucketBaseUrl, ECOSYSTEMS_TXT);
        InputStream obj;
        try {
            obj = webUtil.getFileContext(url).asInputStream(true);
        } catch (WebClientResponseException.NotFound e) {
            logger.warn("{} does not exist.", ECOSYSTEMS_TXT);
            return;
        }
        ecosystems = new BufferedReader(new InputStreamReader(obj)).lines()
                // Can't use unmodifiableList (can't remove).
                .collect(Collectors.toCollection(CopyOnWriteArrayList::new));

        logger.info("Number of ecosystems: <{}>.", ecosystems.size());
    }
}
