package org.opensourceway.uvp.clients.provider.ossindex.converter;

import org.opensourceway.uvp.clients.provider.converter.VulnConverter;
import org.opensourceway.uvp.clients.provider.ossindex.response.OssIndexVulnerability;
import org.opensourceway.uvp.enums.OsvSchemaVersion;
import org.opensourceway.uvp.enums.ReferenceType;
import org.opensourceway.uvp.pojo.osv.OsvAffected;
import org.opensourceway.uvp.pojo.osv.OsvReference;
import org.opensourceway.uvp.pojo.osv.OsvSeverity;
import org.opensourceway.uvp.pojo.osv.OsvVulnerability;
import org.opensourceway.uvp.utility.CvssUtil;
import org.opensourceway.uvp.utility.PurlUtil;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class OssIndexConverter implements VulnConverter<Pair<String, OssIndexVulnerability>> {
    @Override
    public OsvVulnerability convert(Pair<String, OssIndexVulnerability> vulnObj) {
        var osv = new OsvVulnerability();
        osv.setSchemaVersion(OsvSchemaVersion.V1_3_0);
        osv.setId(vulnObj.getSecond().getId());
        osv.setSummary(vulnObj.getSecond().getTitle());
        osv.setDetails(vulnObj.getSecond().getDescription());

        var osvSeverity = new OsvSeverity();
        osvSeverity.setType(CvssUtil.judgeScoringSystem(vulnObj.getSecond().getCvssVector()));
        osvSeverity.setScore(vulnObj.getSecond().getCvssVector());
        osv.setSeverity(List.of(osvSeverity));

        osv.setReferences(
                Stream.concat(Stream.of(vulnObj.getSecond().getReference()),
                                vulnObj.getSecond().getExternalReferences().stream())
                        .map(reference -> {
                            var osvRef = new OsvReference();
                            osvRef.setType(ReferenceType.WEB);
                            osvRef.setUrl(vulnObj.getSecond().getReference());
                            return osvRef;
                        })
                        .distinct()
                        .toList());

        var osvAffected = new OsvAffected();
        osvAffected.setPkg(PurlUtil.purlToOsvPackage(vulnObj.getFirst()));
        osvAffected.setVersions(List.of(PurlUtil.newPurl(vulnObj.getFirst()).getVersion()));
        osv.setAffected(List.of(osvAffected));

        return osv;
    }
}
