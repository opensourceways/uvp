package org.opensourceway.uvp.pojo.nvd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Cpe(Boolean vulnerable, String criteria, String versionStartExcluding, String versionStartIncluding,
                  String versionEndExcluding, String versionEndIncluding) {
}
