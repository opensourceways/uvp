package org.opensourceway.uvp.helper.ecosystem;

import jakarta.annotation.PostConstruct;
import org.opensourceway.uvp.utility.PurlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.List;

public abstract class AbstractEcosystemHelper implements EcosystemHelper {

    @Autowired
    protected EcosystemHelperSelector ecosystemHelperSelector;

    /**
     * Register the EcosystemHelper to {@link EcosystemHelperSelector}.
     */
    @PostConstruct
    protected abstract void register();

    /**
     * By default, retain the type, namespace and name of a Package URL.
     * <p>Example:</p>
     * <p>pkg:maven/org.apache.logging.log4j/log4j-core@2.16.0 -> pkg:maven/org.apache.logging.log4j/log4j-core</p>
     */
    public String normalizePurl(String purl) {
        var packageURL = PurlUtil.newPurl(purl);
        return PurlUtil.canonicalize(PurlUtil.newPurl(packageURL.getType(), packageURL.getNamespace(),
                packageURL.getName(), null, null, null));
    }

    /**
     * Check whether the given version is in the list of affected versions.
     */
    @Override
    public boolean isAffectedVersion(String version, List<String> affectedVersions) {
        return ObjectUtils.isEmpty(version) ||
                (!ObjectUtils.isEmpty(affectedVersions) && affectedVersions.contains(version));
    }

    /**
     * Version ordering of unknown ecosystem is uncertain.
     */
    @Override
    public boolean isAffectedVersion(String version, String introduced, String fixed, String lastAffected, String limit) {
        return ObjectUtils.isEmpty(version);
    }
}
