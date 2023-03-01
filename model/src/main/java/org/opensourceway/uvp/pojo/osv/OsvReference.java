package org.opensourceway.uvp.pojo.osv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.opensourceway.uvp.enums.ReferenceType;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OsvReference {

    @JsonProperty(required = true)
    private ReferenceType type;

    @JsonProperty(required = true)
    private String url;

    public ReferenceType getType() {
        return type;
    }

    public void setType(ReferenceType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OsvReference that = (OsvReference) o;
        return type == that.type && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, url);
    }
}
