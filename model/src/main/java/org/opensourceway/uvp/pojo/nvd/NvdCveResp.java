package org.opensourceway.uvp.pojo.nvd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * See <a href="https://nvd.nist.gov/developers/vulnerabilities">NVD API DOC</a> for details.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NvdCveResp {

    /**
     * The maximum number of CVE records to be returned in a single API response.
     * The default value and maximum allowable limit is 2,000.
     */
    private Integer resultsPerPage;

    /**
     * The index of the first CVE to be returned in the response data.
     */
    private Integer startIndex;

    /**
     * The number of CVE that match the request criteria.
     */
    private Integer totalResults;

    /**
     * A list of objects equal to the number of CVE returned in the response.
     */
    private List<NvdCveWrapper> vulnerabilities;

    public Integer getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(Integer resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public List<NvdCveWrapper> getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilities(List<NvdCveWrapper> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }
}
