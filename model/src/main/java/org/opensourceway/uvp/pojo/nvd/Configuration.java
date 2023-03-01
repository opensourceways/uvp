package org.opensourceway.uvp.pojo.nvd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Configuration(List<Node> nodes) {
}
