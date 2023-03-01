package org.opensourceway.uvp.pojo.osv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.opensourceway.uvp.enums.VersionType;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OsvRange {

    @JsonProperty(required = true)
    private VersionType type;

    private String repo;

    @JsonProperty(required = true)
    private List<OsvEvent> events;

    @JsonProperty("database_specific")
    private Map<Object, Object> databaseSpecific;

    public VersionType getType() {
        return type;
    }

    public void setType(VersionType type) {
        this.type = type;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public List<OsvEvent> getEvents() {
        return events;
    }

    public void setEvents(List<OsvEvent> events) {
        this.events = events;
    }

    public Map<Object, Object> getDatabaseSpecific() {
        return databaseSpecific;
    }

    public void setDatabaseSpecific(Map<Object, Object> databaseSpecific) {
        this.databaseSpecific = databaseSpecific;
    }
}
