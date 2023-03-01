package org.opensourceway.uvp.clients.nvd;

import org.opensourceway.uvp.clients.Nvd;
import org.opensourceway.uvp.pojo.nvd.NvdCveResp;
import org.opensourceway.uvp.utility.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;

/**
 * Client for retrieving vulnerabilities from NVD.
 * <p><a href="https://nvd.nist.gov/developers/vulnerabilities">NVD API DOC</a></p>
 */
@Component
public class NvdImpl implements Nvd {

    @Value("${nvd.api.url}")
    private String baseUrl;

    @Value("${nvd.api.token}")
    private String token;

    @Value("${nvd.api.page_size}")
    private Integer nvdPageSize;

    @Autowired
    private WebUtil webUtil;

    @Override
    public NvdCveResp dumpBatch(Integer startIndex) {
        return webUtil.createWebClient(baseUrl)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/rest/json/cves/2.0")
                        .queryParam("startIndex", startIndex)
                        .queryParam("resultsPerPage", nvdPageSize)
                        .queryParam("noRejected")
                        .build())
                .headers(httpHeaders -> {
                    if (!ObjectUtils.isEmpty(token)) {
                        httpHeaders.add("apiKey", token);
                    }
                })
                .retrieve()
                .bodyToMono(NvdCveResp.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(10))
                        .filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
                .block(Duration.ofSeconds(120));
    }

}
