package org.opensourceway.uvp.clients.provider.ossindex.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ComponentReportElement implements Serializable {
    /** The Package URL coordinates. */
    private String coordinates;

    /** The description of the component. */
    private String description;

    /** The reference URL of the component on OSS Index itself. */
    private String reference;

    /** The list of known vulnerabilities. */
    private List<OssIndexVulnerability> vulnerabilities;

    private Float sonatypeOssiScore;

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<OssIndexVulnerability> getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilities(List<OssIndexVulnerability> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

    public Float getSonatypeOssiScore() {
        return sonatypeOssiScore;
    }

    public void setSonatypeOssiScore(Float sonatypeOssiScore) {
        this.sonatypeOssiScore = sonatypeOssiScore;
    }
}
