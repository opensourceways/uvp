package org.opensourceway.uvp.clients.provider.merger;

import org.opensourceway.uvp.clients.provider.PartialVulnProvider;
import org.opensourceway.uvp.pojo.osv.OsvVulnerability;

import java.util.List;

/**
 * Given a list of {@link OsvVulnerability} converted from {@link PartialVulnProvider},
 * merge them to an {@link OsvVulnerability}.
 */
public interface PartialVulnMerger {
    OsvVulnerability merge(List<OsvVulnerability> vulns);
}
