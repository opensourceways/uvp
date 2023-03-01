package org.opensourceway.uvp.batch.chunk.processor.dumper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.opensourceway.uvp.entity.Vulnerability;
import org.opensourceway.uvp.enums.Ecosystem;
import org.opensourceway.uvp.enums.VulnSource;
import org.opensourceway.uvp.pojo.osv.OsvVulnerability;
import org.opensourceway.uvp.utility.OsvEntityHelper;
import org.opensourceway.uvp.utility.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipInputStream;

public class OsvProcessor implements ItemProcessor<String, List<Vulnerability>> {

    private static final Logger logger = LoggerFactory.getLogger(OsvProcessor.class);

    private static final String ALL_ZIP = "all.zip";

    @Value("${osv.bucket.url}")
    private String osvBucketBaseUrl;

    @Autowired
    private WebUtil webUtil;

    @Autowired
    private OsvEntityHelper osvEntityHelper;

    @Override
    public List<Vulnerability> process(@NotNull String ecosystem) {
        logger.info("Start to download osv vulns for ecosystem: <{}>.", ecosystem);
        try {
            var eco = Ecosystem.findByEcosystem(ecosystem);
            if (Objects.isNull(eco)) {
                logger.warn("<{}> is not recorded in Ecosystem Enum.", ecosystem);
                return List.of();
            }

            List<Vulnerability> vulns = new ArrayList<>();
            var url = "%s/%s/%s".formatted(this.osvBucketBaseUrl,
                    URLEncoder.encode(ecosystem, StandardCharsets.UTF_8).replace("+", "%20"),
                    ALL_ZIP);
            try (ZipInputStream is = new ZipInputStream(webUtil.getFileContext(url).asInputStream(true))) {
                var mapper = new ObjectMapper();
                while (is.getNextEntry() != null) {
                    Optional.ofNullable(osvEntityHelper.toVuln(VulnSource.OSV,
                                    mapper.readValue(new ByteArrayInputStream(is.readAllBytes()), OsvVulnerability.class)))
                            .ifPresent(vulns::add);
                }
            }

            logger.info("End to download osv vulns for ecosystem: <{}>, number of vulns: <{}>.", ecosystem, vulns.size());
            return vulns.stream().distinct().toList();
        } catch (WebClientResponseException.NotFound e) {
            logger.warn("Osv bucket for ecosystem <{}> does not exist.", ecosystem);
            return List.of();
        } catch (Exception e) {
            logger.warn("Exception occurs when download osv vulns for ecosystem: <{}>.", ecosystem, e);
            throw new RuntimeException(e);
        }
    }
}
