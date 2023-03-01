package org.opensourceway.uvp.service;

import org.opensourceway.uvp.cache.UvpQueryCache;
import org.opensourceway.uvp.constant.CacheConstant;
import org.opensourceway.uvp.dao.PackageRepository;
import org.opensourceway.uvp.entity.Package;
import org.opensourceway.uvp.helper.aggragator.VulnAggregator;
import org.opensourceway.uvp.pojo.osv.OsvVulnerability;
import org.opensourceway.uvp.pojo.response.PackageVulns;
import org.opensourceway.uvp.utility.PurlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UvpServiceImpl implements UvpService {
    private static final Logger logger = LoggerFactory.getLogger(UvpServiceImpl.class);

    @Autowired
    private VulnLocalService vulnLocalService;

    @Autowired
    private VulnAggregator vulnAggregator;

    @Autowired
    private UvpQueryCache uvpQueryCache;

    @Autowired
    private PackageRepository packageRepository;

    @Override
    @Cacheable(value = {CacheConstant.UVP_QUERY_CACHE})
    public List<OsvVulnerability> query(String purl) {
        PurlUtil.validate(purl);

        var result = vulnLocalService.query(purl);
        return vulnAggregator.aggregate(result);
    }

    @Override
    public List<PackageVulns> queryBatch(List<String> purls) {
        purls = purls.stream().distinct().toList();
        PurlUtil.validate(purls);

        var result = vulnLocalService.queryBatch(purls);
        return vulnAggregator.batchAggregate(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importBatch(List<String> purls) {
        purls = purls.stream().distinct().collect(Collectors.toList());
        PurlUtil.validate(purls);

        packageRepository.lockTable();

        purls.removeAll(packageRepository.findAll().stream().map(Package::getPurl).toList());
        packageRepository.saveAll(purls.stream().map(PurlUtil::purlToPackage).toList());
    }
}
