package org.opensourceway.uvp.helper.aggragator;

import org.opensourceway.uvp.enums.VulnSource;
import org.opensourceway.uvp.pojo.osv.OsvVulnerability;
import org.opensourceway.uvp.pojo.response.PackageVulns;
import org.opensourceway.uvp.pojo.vo.OsvVulnWithSource;
import org.opensourceway.uvp.pojo.vo.PackageVulnsWithSource;

import java.util.List;

/**
 * Aggregate vulnerabilities from different sources.
 *
 * @see VulnSource
 * @see OsvVulnWithSource
 */
public interface VulnAggregator {
    /**
     * Aggregate all vulns collected from different {@link VulnSource}s that affect a specific package.
     *
     * @param vulns All vulns collected from different sources that affect a specific package.
     * @return Aggregated vulns. There SHOULD NOT exist duplicate vulns with the same id.
     */
    List<OsvVulnerability> aggregate(List<OsvVulnWithSource> vulns);

    /**
     * Batch aggregate all vulns collected from different {@link VulnSource}s that affect a specific package.
     *
     * @param pkgVulns All pkg-vulns pairs collected from different sources that affect a specific package.
     * @return Aggregated pkg-vulns pairs. There SHOULD NOT exist duplicate vulns with the same id
     * that affect the same package.
     */
    List<PackageVulns> batchAggregate(List<PackageVulnsWithSource> pkgVulns);
}
