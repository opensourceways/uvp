package org.opensourceway.uvp.clients.provider.osv;

import org.opensourceway.uvp.clients.provider.AbstractVulnProvider;
import org.opensourceway.uvp.clients.provider.CompleteVulnProvider;
import org.opensourceway.uvp.clients.provider.osv.request.RequestBody;
import org.opensourceway.uvp.clients.provider.osv.response.QueryResp;
import org.opensourceway.uvp.enums.Ecosystem;
import org.opensourceway.uvp.enums.VulnSource;
import org.opensourceway.uvp.pojo.osv.OsvVulnerability;
import org.opensourceway.uvp.utility.PurlUtil;
import org.opensourceway.uvp.utility.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Client for <a href="https://osv.dev">OSV</a> API calls
 * <p>API doc: <a href="https://osv.dev/docs/">OSV API doc</a></p>
 */

// OSV is integrated by dumping their data in a GCS bucket.
// The API caller class is not used, and therefore not annotated with @Component
public class Osv extends AbstractVulnProvider implements CompleteVulnProvider {

    /**
     * In fact, there is no such limit defined by OSV, but for performance consideration, it is set to 128.
     * @see Osv#internalQueryBatch
     */
    private static final Integer CLIENT_QUERY_BATCH_LIMIT = 128;

    @Value("${osv.api.url}")
    private String baseUrl;
    @Autowired
    private WebUtil webUtil;

    /**
     * For cached-call in the same bean.
     */
    @Lazy
    @Autowired
    private Osv self;

    @Override
    public VulnSource getVulnSource() {
        return VulnSource.OSV;
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
        List<OsvVulnerability> result = new ArrayList<>();
        var client = webUtil.createWebClient(this.baseUrl);
        recursiveQuery(RequestBody.fromPurl(purl), client, result);
        return result;
    }

    /**
     * OSV query api returns a list of 16 detailed vulns, and a next_page_token that points to the next page of vulns.
     * @param body OSV query body.
     * @param client A reusable WebClient.
     * @param result List of osv-schema vulns.
     */
    private void recursiveQuery(RequestBody body, WebClient client, List<OsvVulnerability> result) {
        var resp = client.post()
                .uri("/v1/query")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(QueryResp.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
                .block(Duration.ofSeconds(120));

        if (ObjectUtils.isEmpty(resp) || ObjectUtils.isEmpty(resp.vulns())) {
            return;
        }

        result.addAll(resp.vulns());
        if (!ObjectUtils.isEmpty(resp.nextPageToken())) {
            body.setPageToken(resp.nextPageToken());
            recursiveQuery(body, client, result);
        }
    }

    /**
     * The batch query api of OSV will not return detailed vulns. Therefore, as a workaround,
     * call the single query api in parallel to simulate batch query.
     */
    @Override
    protected List<OsvVulnerability> internalQueryBatch(List<String> purls) {
        return purls.parallelStream().map(purl -> self.internalQuery(purl)).flatMap(List::stream).distinct().toList();
    }
}
