package org.opensourceway.uvp.pojo.vo;

import java.util.List;

public record PackageVulnsWithSource(String purl, List<OsvVulnWithSource> vulns) {
}
