package org.opensourceway.uvp.clients;

import org.opensourceway.uvp.pojo.nvd.NvdCveResp;

/**
 * Client for retrieving vulnerabilities from NVD.
 * <p><a href="https://nvd.nist.gov/developers/vulnerabilities">NVD API DOC</a></p>
 */
public interface Nvd {

    /**
     * Dump a batch of CVE via NVD API.
     * @param startIndex The index of the first CVE to be returned in the response data.
     * @return NVD-defined response.
     */
    NvdCveResp dumpBatch(Integer startIndex);
}
