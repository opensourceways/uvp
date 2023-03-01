package org.opensourceway.uvp.helper.aggragator;

import org.opensourceway.uvp.pojo.osv.OsvVulnerability;
import org.opensourceway.uvp.pojo.response.PackageVulns;
import org.opensourceway.uvp.pojo.vo.OsvVulnWithSource;
import org.opensourceway.uvp.pojo.vo.PackageVulnsWithSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class VulnAggregatorImpl implements VulnAggregator {

    private static final Logger logger = LoggerFactory.getLogger(VulnAggregatorImpl.class);

    @Override
    public List<OsvVulnerability> aggregate(List<OsvVulnWithSource> vulns) {
        logger.info("Aggregate <{}> vulns", vulns.size());

        // Map vuln id and aliases.
        Map<String, OsvVulnWithSource> vulnIdToVuln = new HashMap<>();
        vulns.forEach(vuln -> {
            vulnIdToVuln.merge(vuln.osvVulnerability().getId(), vuln, (oldValue, value) ->
                    value.vulnSource().hasHigherPriority(oldValue.vulnSource()) ? value : oldValue);

            Optional.ofNullable(vuln.osvVulnerability().getAliases())
                    .ifPresent(aliases -> aliases.forEach(alias -> vulnIdToVuln.merge(alias, vuln, (oldValue, value) ->
                            value.vulnSource().hasHigherPriority(oldValue.vulnSource()) ? value : oldValue)));
        });
        var result = vulnIdToVuln.values().stream()
                .map(OsvVulnWithSource::osvVulnerability).distinct().toList();

        logger.info("Get <{}> vulns after aggregating", result.size());
        return result;
    }

    @Override
    public List<PackageVulns> batchAggregate(List<PackageVulnsWithSource> pkgVulns) {
        logger.info("Start to batch aggregate");
        var result = pkgVulns.stream()
                .map(it -> new PackageVulns(it.purl(), aggregate(it.vulns())))
                .toList();

        logger.info("End to batch aggregate");
        return result;
    }
}
