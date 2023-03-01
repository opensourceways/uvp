package org.opensourceway.uvp.service;

import org.opensourceway.uvp.dao.AffectedPackageRepository;
import org.opensourceway.uvp.entity.AffectedEvent;
import org.opensourceway.uvp.entity.AffectedPackage;
import org.opensourceway.uvp.entity.AffectedRange;
import org.opensourceway.uvp.entity.Vulnerability;
import org.opensourceway.uvp.enums.Ecosystem;
import org.opensourceway.uvp.enums.EventType;
import org.opensourceway.uvp.helper.ecosystem.EcosystemHelper;
import org.opensourceway.uvp.helper.ecosystem.EcosystemHelperSelector;
import org.opensourceway.uvp.pojo.vo.OsvVulnWithSource;
import org.opensourceway.uvp.pojo.vo.PackageVulnsWithSource;
import org.opensourceway.uvp.utility.OsvEntityHelper;
import org.opensourceway.uvp.utility.PurlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class VulnLocalServiceImpl implements VulnLocalService {

    private static final Logger logger = LoggerFactory.getLogger(VulnLocalServiceImpl.class);

    @Autowired
    private EcosystemHelperSelector ecosystemHelperSelector;

    @Autowired
    private OsvEntityHelper osvEntityHelper;

    @Autowired
    private AffectedPackageRepository affectedPackageRepository;

    @Override
    public List<OsvVulnWithSource> query(String purl) {
        logger.info("Try to query vulns from local databases for <{}>.", purl);

        var helper = ecosystemHelperSelector.getHelper(
                Ecosystem.findByPurlType(PurlUtil.newPurl(purl).getType()));
        var version = PurlUtil.newPurl(purl).getVersion();

        var vulns = affectedPackageRepository.findByPurl(helper.normalizePurl(purl)).stream()
                .filter(affectedPackage -> isAffected(helper, version, affectedPackage))
                .map(AffectedPackage::getVulnerability)
                .toList();
        var vulnSourceMap = vulns.stream()
                .collect(Collectors.groupingBy(Vulnerability::getSource, Collectors.counting()));
        var result = vulns.stream()
                .map(it -> osvEntityHelper.toVulnWithSource(it))
                .toList();

        logger.info("End to query vulns from local databases, get <{}> vulns ({}) affecting <{}> before aggregation.",
                result.size(), vulnSourceMap, purl);
        return result;
    }

    private boolean isAffected(EcosystemHelper helper, String version, AffectedPackage affectedPackage) {
        if (helper.isAffectedVersion(version, affectedPackage.getVersions())) {
            return true;
        }

        for (AffectedRange affectedRange : affectedPackage.getRanges()) {
            if (helper.isAffectedVersion(
                    version,
                    getValueByEventType(affectedRange, EventType.INTRODUCED),
                    getValueByEventType(affectedRange, EventType.FIXED),
                    getValueByEventType(affectedRange, EventType.LAST_AFFECTED),
                    getValueByEventType(affectedRange, EventType.LIMIT))) {
                return true;
            }
        }

        return false;
    }

    private String getValueByEventType(AffectedRange affectedRange, EventType eventType) {
        return affectedRange.getEvents().stream()
                .filter(affectedEvent -> eventType.equals(affectedEvent.getType()))
                .map(AffectedEvent::getValue)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<PackageVulnsWithSource> queryBatch(List<String> purls) {
        logger.info("Try to batch query vulns from local databases for <{}>.", purls);

        var result = purls.stream()
                .map(purl -> new PackageVulnsWithSource(purl, query(purl))).toList();

        logger.info("End to batch query vulns from local databases for <{}>, get <{}> vulns.",
                purls, result.stream().map(PackageVulnsWithSource::vulns).mapToLong(List::size).sum());
        return result;
    }
}
