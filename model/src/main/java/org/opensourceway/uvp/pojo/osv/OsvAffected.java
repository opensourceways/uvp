package org.opensourceway.uvp.pojo.osv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OsvAffected {

    @JsonProperty("package")
    private OsvPackage pkg;

    private List<OsvRange> ranges;

    private List<String> versions;

    @JsonProperty("ecosystem_specific")
    private Map<Object, Object> ecosystemSpecific;

    @JsonProperty("database_specific")
    private Map<Object, Object> databaseSpecific;

    public OsvPackage getPkg() {
        return pkg;
    }

    public void setPkg(OsvPackage pkg) {
        this.pkg = pkg;
    }

    public List<OsvRange> getRanges() {
        return ranges;
    }

    public void setRanges(List<OsvRange> ranges) {
        this.ranges = ranges;
    }

    public List<String> getVersions() {
        return versions;
    }

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }

    public Map<Object, Object> getEcosystemSpecific() {
        return ecosystemSpecific;
    }

    public void setEcosystemSpecific(Map<Object, Object> ecosystemSpecific) {
        this.ecosystemSpecific = ecosystemSpecific;
    }

    public Map<Object, Object> getDatabaseSpecific() {
        return databaseSpecific;
    }

    public void setDatabaseSpecific(Map<Object, Object> databaseSpecific) {
        this.databaseSpecific = databaseSpecific;
    }
}
