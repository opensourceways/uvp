package org.opensourceway.uvp.pojo.nvd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Metrics(List<CvssMetric> cvssMetricV2, List<CvssMetric> cvssMetricV30, List<CvssMetric> cvssMetricV31) {
}
