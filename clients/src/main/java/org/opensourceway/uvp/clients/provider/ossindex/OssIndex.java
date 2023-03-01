package org.opensourceway.uvp.clients.provider.ossindex;

import org.opensourceway.uvp.clients.provider.AbstractVulnProvider;
import org.opensourceway.uvp.clients.provider.PartialVulnProvider;
import org.opensourceway.uvp.clients.provider.converter.VulnConverter;
import org.opensourceway.uvp.clients.provider.ossindex.request.RequestBody;
import org.opensourceway.uvp.clients.provider.ossindex.response.ComponentReportElement;
import org.opensourceway.uvp.clients.provider.ossindex.response.OssIndexVulnerability;
import org.opensourceway.uvp.enums.Ecosystem;
import org.opensourceway.uvp.enums.VulnSource;
import org.opensourceway.uvp.pojo.osv.OsvVulnerability;
import org.opensourceway.uvp.utility.PurlUtil;
import org.opensourceway.uvp.utility.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Client for <a href="https://ossindex.sonatype.org/">Sonatype OSS Index</a> API calls
 * <p>API doc: <a href="https://ossindex.sonatype.org/rest">Sonatype OSS Index API doc</a></p>
 */
@Component
public class OssIndex extends AbstractVulnProvider implements PartialVulnProvider {

    private static final Logger logger = LoggerFactory.getLogger(OssIndex.class);

    /**
     * According to <a href="https://ossindex.sonatype.org/doc/rest">OSS Index API</a>,
     * there is a limit of 128 components per HTTP request.
     */
    private static final Integer CLIENT_QUERY_BATCH_LIMIT = 128;

    @Value("${ossindex.api.url}")
    private String baseUrl;

    @Value("${ossindex.api.tokens}")
    private String[] tokens;

    @Autowired
    private WebUtil webUtil;

    @Autowired
    @Qualifier("ossIndexConverter")
    private VulnConverter<Pair<String, OssIndexVulnerability>> vulnConverter;

    @Override
    public VulnSource getVulnSource() {
        return VulnSource.OSS_INDEX;
    }

    @Override
    public Integer getQueryBatchLimit() {
        return CLIENT_QUERY_BATCH_LIMIT;
    }

    @Override
    public Boolean filter(String purl) {
        try {
            return !Ecosystem.UNKNOWN.equals(PurlUtil.purlToPackage(purl).getEcosystem());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected List<OsvVulnerability> internalQuery(String purl) {
        return internalQueryBatch(List.of(purl));
    }

    @Override
    protected List<OsvVulnerability> internalQueryBatch(List<String> purls) {
        logger.info("Query OSS Index for <{}> purls", purls.size());
        return Arrays.stream(getComponentReport(purls))
                .map(it -> it.getVulnerabilities()
                        .stream()
                        .map(vuln -> vulnConverter.convert(Pair.of(it.getCoordinates(), vuln)))
                        .toList())
                .flatMap(List::stream)
                // DO NOT distinct() here, because OsvVulnerability converted from OSS Index with the same vuln id
                // can have different OsvAffected field which should be merged later.
                .toList();
    }

    private ComponentReportElement[] getComponentReport(List<String> coordinates) {
        var client = webUtil.createWebClient(this.baseUrl);
        RequestBody body = new RequestBody(coordinates);

        if (ObjectUtils.isEmpty(tokens)) {
            return client.post()
                    .uri("/api/v3/component-report")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(ComponentReportElement[].class)
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(10))
                            .filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests))
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
                    .block(Duration.ofSeconds(120));
        }
        return client.post()
                .uri("/api/v3/authorized/component-report")
                .contentType(MediaType.APPLICATION_JSON)
                // Randomly select a token.
                .header("Authorization",
                        MessageFormat.format("Basic {0}", tokens[new Random().nextInt(0, tokens.length)]))
                .bodyValue(body)
                .retrieve()
                .bodyToMono(ComponentReportElement[].class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(10))
                        .filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
                .block(Duration.ofSeconds(120));
    }
}
