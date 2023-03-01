package org.opensourceway.uvp.pojo.osv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OsvCredit {

    @JsonProperty(required = true)
    private String name;

    private List<String> contact;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getContact() {
        return contact;
    }

    public void setContact(List<String> contact) {
        this.contact = contact;
    }
}
