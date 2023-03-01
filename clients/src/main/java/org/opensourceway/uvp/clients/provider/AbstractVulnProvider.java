package org.opensourceway.uvp.clients.provider;

import org.opensourceway.uvp.pojo.osv.OsvVulnerability;
import org.springframework.util.ObjectUtils;

import java.util.List;

public abstract class AbstractVulnProvider implements VulnProvider {

    @Override
    public List<OsvVulnerability> query(String purl) {
        if (!filter(purl)) {
            return List.of();
        }
        return internalQuery(purl);
    }

    protected abstract List<OsvVulnerability> internalQuery(String purl);

    @Override
    public List<OsvVulnerability> queryBatch(List<String> purls) {
        purls = purls.stream().filter(this::filter).toList();
        if (ObjectUtils.isEmpty(purls)) {
            return List.of();
        }
        return internalQueryBatch(purls);
    }

    protected abstract List<OsvVulnerability> internalQueryBatch(List<String> purls);
}
