package org.opensourceway.uvp.clients.provider.ossindex.merger;

import org.opensourceway.uvp.clients.provider.merger.PartialVulnMerger;
import org.opensourceway.uvp.pojo.osv.OsvVulnerability;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OssIndexMerger implements PartialVulnMerger {
    @Override
    public OsvVulnerability merge(List<OsvVulnerability> vulns) {
        var vuln = vulns.get(0);
        vuln.setAffected(vulns.stream().map(OsvVulnerability::getAffected).flatMap(List::stream).toList());
        return vuln;
    }
}
