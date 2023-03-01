package org.opensourceway.uvp.pojo.nvd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CvssMetric(CvssData cvssData) {
}
