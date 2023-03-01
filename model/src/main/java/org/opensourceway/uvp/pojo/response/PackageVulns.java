package org.opensourceway.uvp.pojo.response;

import org.opensourceway.uvp.pojo.osv.OsvVulnerability;

import java.util.List;

public record PackageVulns(String purl, List<OsvVulnerability> vulns) {}
