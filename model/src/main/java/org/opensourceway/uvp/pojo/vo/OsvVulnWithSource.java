package org.opensourceway.uvp.pojo.vo;

import org.opensourceway.uvp.entity.Vulnerability;
import org.opensourceway.uvp.enums.VulnSource;
import org.opensourceway.uvp.pojo.osv.OsvVulnerability;

/**
 * This is not a standard osv-schema class.
 * <p>The class is used to record the source of vulnerabilities (like {@link Vulnerability#setSource(VulnSource)}),
 * and for further aggregating the same vulnerabilities from different sources.</p>
 *
 * @see OsvVulnerability
 */
public record OsvVulnWithSource(OsvVulnerability osvVulnerability, VulnSource vulnSource) {
}
