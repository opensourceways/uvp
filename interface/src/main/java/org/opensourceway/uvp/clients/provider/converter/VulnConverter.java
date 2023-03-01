package org.opensourceway.uvp.clients.provider.converter;

import org.opensourceway.uvp.clients.provider.VulnProvider;
import org.opensourceway.uvp.pojo.osv.OsvVulnerability;

/**
 * Given a vuln object from an API call of a certain {@link VulnProvider} (OSS Index, etc.),
 * try to convert it to an osv-schema vulnerability.
 */
public interface VulnConverter<T> {
    OsvVulnerability convert(T vulnObj);
}
