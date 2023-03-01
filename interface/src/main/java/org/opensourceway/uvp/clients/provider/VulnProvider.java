package org.opensourceway.uvp.clients.provider;

import org.opensourceway.uvp.enums.VulnSource;
import org.opensourceway.uvp.pojo.osv.OsvVulnerability;

import java.util.List;

/**
 * A client for <strong>querying</strong> vulnerabilities from a specific vulnerability database.
 */
public interface VulnProvider {
    /**
     * @return The vulnerability database that the client queries.
     */
    VulnSource getVulnSource();

    /**
     * @return The maximum package size per query batch call.
     */
    Integer getQueryBatchLimit();

    /**
     * Filter a purl that MUST NOT be queried by the client.
     * @param purl Package URL.
     * @return true if the purl needs to be queried, otherwise false.
     */
    Boolean filter(String purl);

    /**
     * Query vulnerabilities that affect the given package from a remote vuln database.
     *
     * @param purl Package URL.
     * @return A list of osv-schema vulnerabilities.
     */
    List<OsvVulnerability> query(String purl);

    /**
     * Batch query vulnerabilities that affect the given packages from a remote vuln database.
     *
     * @param purls A list of Package URLs.
     * @return A list of osv-schema vulnerabilities.
     */
    List<OsvVulnerability> queryBatch(List<String> purls);
}
