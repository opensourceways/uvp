package org.opensourceway.uvp.pojo.nvd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NvdCve {

    /**
     * CVE id.
     */
    private String id;

    /**
     * The date and time that the CVE was published to the NVD.
     */
    private String published;

    /**
     * The date and time that the CVE was last modified.
     */
    private String lastModified;

    /**
     * A description of the CVE in one or more languages.
     */
    private List<Description> descriptions;

    /**
     * CVSSv2 or CVSSv3 information on the CVE's impact.
     */
    private Metrics metrics;

    /**
     * The CVE applicability statements that convey which product, or products,
     * are associated with the vulnerability according to the NVD analysis
     */
    private List<Configuration> configurations;

    /**
     * Supplemental information relevant to the vulnerability.
     */
    private List<Reference> references;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public List<Description> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<Description> descriptions) {
        this.descriptions = descriptions;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    public List<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<Configuration> configurations) {
        this.configurations = configurations;
    }

    public List<Reference> getReferences() {
        return references;
    }

    public void setReferences(List<Reference> references) {
        this.references = references;
    }
}
