package org.opensourceway.uvp.batch.chunk.processor.dumper;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.opensourceway.uvp.clients.Nvd;
import org.opensourceway.uvp.dao.CpePurlRepository;
import org.opensourceway.uvp.entity.Vulnerability;
import org.opensourceway.uvp.enums.EventType;
import org.opensourceway.uvp.enums.OsvSchemaVersion;
import org.opensourceway.uvp.enums.ReferenceType;
import org.opensourceway.uvp.enums.ScoringSystem;
import org.opensourceway.uvp.enums.VersionType;
import org.opensourceway.uvp.enums.VulnSource;
import org.opensourceway.uvp.pojo.nvd.Configuration;
import org.opensourceway.uvp.pojo.nvd.Cpe;
import org.opensourceway.uvp.pojo.nvd.Description;
import org.opensourceway.uvp.pojo.nvd.Metrics;
import org.opensourceway.uvp.pojo.nvd.Node;
import org.opensourceway.uvp.pojo.nvd.NvdCve;
import org.opensourceway.uvp.pojo.nvd.NvdCveWrapper;
import org.opensourceway.uvp.pojo.nvd.Reference;
import org.opensourceway.uvp.pojo.osv.OsvAffected;
import org.opensourceway.uvp.pojo.osv.OsvEvent;
import org.opensourceway.uvp.pojo.osv.OsvRange;
import org.opensourceway.uvp.pojo.osv.OsvReference;
import org.opensourceway.uvp.pojo.osv.OsvSeverity;
import org.opensourceway.uvp.pojo.osv.OsvVulnerability;
import org.opensourceway.uvp.utility.OsvEntityHelper;
import org.opensourceway.uvp.utility.PurlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import us.springett.parsers.cpe.CpeParser;
import us.springett.parsers.cpe.exceptions.CpeParsingException;
import us.springett.parsers.cpe.values.LogicalValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NvdProcessor implements ItemProcessor<Integer, List<Vulnerability>>, StepExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(NvdProcessor.class);

    private Map<String, List<String>> cpePurlMapping = new HashMap<>();

    @Autowired
    private Nvd nvd;

    @Autowired
    private CpePurlRepository cpePurlRepository;

    @Autowired
    private OsvEntityHelper osvEntityHelper;

    @Override
    public List<Vulnerability> process(@NotNull Integer startIndex) {
        logger.info("Start to get NVD CVE with startIndex: <{}>.", startIndex);

        if (startIndex == Integer.MIN_VALUE) {
            return null;
        }

        var resp = nvd.dumpBatch(startIndex);
        // Maybe some failure occur, try to call NVD API next time.
        if (Objects.isNull(resp)) {
            logger.warn("Get null response from NVD.");
            throw new RuntimeException("Get null response from NVD.");
        }

        try {
            var vulns = resp.getVulnerabilities()
                    .stream()
                    .map(NvdCveWrapper::cve)
                    .map(this::toOsv)
                    .map(it -> osvEntityHelper.toVuln(VulnSource.NVD, it))
                    .distinct()
                    .toList();

            logger.info("End to process NVD CVE.");
            return vulns;
        } catch (Exception e) {
            logger.warn("Exception occurs when process NVD CVE.", e);
            throw new RuntimeException(e);
        }
    }

    private OsvVulnerability toOsv(NvdCve cve) {
        var osv = new OsvVulnerability();
        osv.setSchemaVersion(OsvSchemaVersion.V1_3_0);
        osv.setId(cve.getId());
        osv.setModified(convertNvdTimestamp(cve.getLastModified()));
        osv.setPublished(convertNvdTimestamp(cve.getPublished()));
        osv.setWithdrawn(null);
        osv.setAliases(null);
        osv.setRelated(null);
        osv.setSummary(null);
        osv.setDetails(cve.getDescriptions().stream()
                .filter(it -> StringUtils.equals(it.lang(), Description.Lang.EN.getLang())).map(Description::value)
                .findFirst().orElse(null));
        osv.setSeverity(toOsv(cve.getMetrics()));
        osv.setAffected(toOsv(cve.getConfigurations()));
        osv.setReferences(cve.getReferences().stream().map(this::toOsv).distinct().toList());
        osv.setCredits(null);
        return osv;
    }

    private List<OsvAffected> toOsv(List<Configuration> configurations) {
        if (ObjectUtils.isEmpty(configurations)) {
            return null;
        }

        return configurations.stream()
                .map(Configuration::nodes)
                .filter(nodes -> !ObjectUtils.isEmpty(nodes))
                .flatMap(List::stream)
                .map(Node::cpeMatch)
                .filter(cpes -> !ObjectUtils.isEmpty(cpes))
                .flatMap(List::stream)
                .map(this::toOsv)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .toList();
    }

    private List<OsvAffected> toOsv(Cpe cpe) {
        if (!cpe.vulnerable()) {
            return null;
        }

        if (!cpePurlMapping.containsKey(cpe.criteria())) {
            return null;
        }

        return cpePurlMapping.get(cpe.criteria()).stream().map(purl -> toOsv(cpe, purl)).toList();
    }

    private OsvAffected toOsv(Cpe cpe, String purl) {
        try {
            var osvAffected = new OsvAffected();

            osvAffected.setPkg(PurlUtil.purlToOsvPackage(purl));
            osvAffected.setRanges(List.of(toOsvRange(cpe)));
            osvAffected.setVersions(getVersionsFromCpe(cpe));

            return osvAffected;
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> getVersionsFromCpe(Cpe cpe) {
        List<String> versions = new ArrayList<>();
        String version;
        if ((version = getVersionFromCpe(cpe.criteria())) != null) {
            versions.add(version);
        }
        // TODO Expand versions from Starting and Ending
        return versions;
    }

    private String getVersionFromCpe(String cpe) {
        try {
            var cpeObj = CpeParser.parse(cpe);
            if (Arrays.stream(LogicalValue.values()).map(LogicalValue::getAbbreviation).toList()
                    .contains(cpeObj.getVersion())) {
                return null;
            }
            if (Arrays.stream(LogicalValue.values()).map(LogicalValue::getAbbreviation).toList()
                    .contains(cpeObj.getUpdate())) {
                return cpeObj.getVersion();
            }
            return "%s-%s".formatted(cpeObj.getVersion(), cpeObj.getUpdate());
        } catch (CpeParsingException e) {
            // The exception should not be thrown.
            logger.warn("Invalid CPE: <{}>", cpe);
            return null;
        }
    }

    private OsvRange toOsvRange(Cpe cpe) {
        var osvRange = new OsvRange();
        osvRange.setType(VersionType.ECOSYSTEM);

        List<OsvEvent> osvEvents = new ArrayList<>();
        if (StringUtils.isNotBlank(cpe.versionStartIncluding())) {
            var osvEvent = new OsvEvent();
            osvEvent.put(EventType.INTRODUCED, cpe.versionStartIncluding());
            osvEvents.add(osvEvent);
        }
        if (StringUtils.isNotBlank(cpe.versionStartExcluding())) {
            // TODO Find the next version
        }
        if (StringUtils.isNotBlank(cpe.versionEndIncluding())) {
            var osvEvent = new OsvEvent();
            osvEvent.put(EventType.LAST_AFFECTED, cpe.versionEndIncluding());
            osvEvents.add(osvEvent);
        }
        if (StringUtils.isNotBlank(cpe.versionEndExcluding())) {
            var osvEvent = new OsvEvent();
            osvEvent.put(EventType.FIXED, cpe.versionEndExcluding());
            osvEvents.add(osvEvent);
        }
        osvRange.setEvents(osvEvents);

        return osvRange;
    }

    private List<OsvSeverity> toOsv(Metrics metrics) {
        List<OsvSeverity> severities = new ArrayList<>();
        if (!ObjectUtils.isEmpty(metrics.cvssMetricV2())) {
            var severity = new OsvSeverity();
            severity.setType(ScoringSystem.CVSS_V2);
            severity.setScore(metrics.cvssMetricV2().get(0).cvssData().vectorString());
            severities.add(severity);
        }
        if (!ObjectUtils.isEmpty(metrics.cvssMetricV30())) {
            var severity = new OsvSeverity();
            severity.setType(ScoringSystem.CVSS_V3);
            severity.setScore(metrics.cvssMetricV30().get(0).cvssData().vectorString());
            severities.add(severity);
        }
        if (!ObjectUtils.isEmpty(metrics.cvssMetricV31())) {
            var severity = new OsvSeverity();
            severity.setType(ScoringSystem.CVSS_V3);
            severity.setScore(metrics.cvssMetricV31().get(0).cvssData().vectorString());
            severities.add(severity);
        }
        return severities;
    }

    private OsvReference toOsv(Reference reference) {
        var osvReference = new OsvReference();
        osvReference.setType(ObjectUtils.isEmpty(reference.tags()) ? ReferenceType.WEB :
                reference.tags().get(0).getOsvReferenceType());
        osvReference.setUrl(reference.url());
        return osvReference;
    }

    /**
     * Parse NVD timestamp to ISO-8601 timestamp.
     * <p>1970-01-01T00:00:00 -> 1970-01-01T00:00:00Z</p>
     *
     * @param timestamp NVD timestamp
     * @return ISO-8601 timestamp
     */
    private String convertNvdTimestamp(String timestamp) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS").parse(timestamp).toInstant().toString();
        } catch (ParseException e) {
            // If failed to parse the timestamp, return 1970-01-01T00:00:00Z as an alternative.
            return Instant.EPOCH.toString();
        }
    }

    @Override
    public void beforeStep(@NotNull StepExecution stepExecution) {
        logger.info("Fetch CPE-PURL mapping from database.");
        cpePurlRepository.findAll().forEach(it -> it.getCpes().forEach(cpe -> cpePurlMapping.put(cpe, it.getPurls())));
    }

    @Override
    public ExitStatus afterStep(@NotNull StepExecution stepExecution) {
        cpePurlMapping.clear();
        return null;
    }
}
