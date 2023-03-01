package org.opensourceway.uvp.pojo.osv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.opensourceway.uvp.enums.ScoringSystem;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OsvSeverity {

    @JsonProperty(required = true)
    private ScoringSystem type;

    /**
     * The score here is in fact a vector.
     */
    @JsonProperty(required = true)
    private String score;

    public ScoringSystem getType() {
        return type;
    }

    public void setType(ScoringSystem type) {
        this.type = type;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
