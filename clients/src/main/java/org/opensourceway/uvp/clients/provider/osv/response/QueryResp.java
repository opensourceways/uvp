package org.opensourceway.uvp.clients.provider.osv.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.opensourceway.uvp.pojo.osv.OsvVulnerability;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record QueryResp(List<OsvVulnerability> vulns, @JsonProperty("next_page_token") String nextPageToken) {}
