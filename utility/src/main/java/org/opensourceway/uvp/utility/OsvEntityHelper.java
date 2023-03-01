package org.opensourceway.uvp.utility;

import org.opensourceway.uvp.dao.VulnerabilityRepository;
import org.opensourceway.uvp.entity.AffectedEvent;
import org.opensourceway.uvp.entity.AffectedPackage;
import org.opensourceway.uvp.entity.AffectedRange;
import org.opensourceway.uvp.entity.Credit;
import org.opensourceway.uvp.entity.Reference;
import org.opensourceway.uvp.entity.Severity;
import org.opensourceway.uvp.entity.Vulnerability;
import org.opensourceway.uvp.enums.CvssSeverity;
import org.opensourceway.uvp.enums.VulnSource;
import org.opensourceway.uvp.pojo.osv.OsvAffected;
import org.opensourceway.uvp.pojo.osv.OsvCredit;
import org.opensourceway.uvp.pojo.osv.OsvEvent;
import org.opensourceway.uvp.pojo.osv.OsvPackage;
import org.opensourceway.uvp.pojo.osv.OsvRange;
import org.opensourceway.uvp.pojo.osv.OsvReference;
import org.opensourceway.uvp.pojo.osv.OsvSeverity;
import org.opensourceway.uvp.pojo.osv.OsvVulnerability;
import org.opensourceway.uvp.pojo.vo.OsvVulnWithSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OsvEntityHelper {

    @Autowired
    private VulnerabilityRepository vulnerabilityRepository;

    public Vulnerability toVuln(VulnSource source, OsvVulnerability osvVulnerability) {
        if (Objects.isNull(osvVulnerability)) {
            return null;
        }

        var vuln = new Vulnerability();
        vuln.setSource(source);
        return toEntity(osvVulnerability, vuln);
    }

    private Vulnerability toEntity(OsvVulnerability osvVuln, Vulnerability vuln) {
        if (Objects.isNull(osvVuln)) {
            return null;
        }

        vuln.setSchemaVersion(osvVuln.getSchemaVersion());
        vuln.setVulnId(osvVuln.getId());
        vuln.setAliases(osvVuln.getAliases());
        vuln.setRelated(osvVuln.getRelated());
        vuln.setModified(Objects.isNull(osvVuln.getModified()) ? null
                : Timestamp.from(Instant.parse(osvVuln.getModified())));
        vuln.setPublished(Objects.isNull(osvVuln.getPublished()) ? null
                : Timestamp.from(Instant.parse(osvVuln.getPublished())));
        vuln.setWithdrawn(Objects.isNull(osvVuln.getWithdrawn()) ? null
                : Timestamp.from(Instant.parse(osvVuln.getWithdrawn())));
        vuln.setSummary(osvVuln.getSummary());
        vuln.setDetail(osvVuln.getDetails());
        vuln.setDatabaseSpecific(osvVuln.getDatabaseSpecific());
        vuln.setSeverities(Objects.isNull(osvVuln.getSeverity()) ? new ArrayList<>()
                : osvVuln.getSeverity().stream()
                .map(it -> toEntity(it, vuln)).filter(Objects::nonNull).collect(Collectors.toList()));
        vuln.setReferences(Objects.isNull(osvVuln.getReferences()) ? new ArrayList<>()
                : osvVuln.getReferences().stream()
                .map(it -> toEntity(it, vuln)).filter(Objects::nonNull).collect(Collectors.toList()));
        vuln.setCredits(Objects.isNull(osvVuln.getCredits()) ? new ArrayList<>()
                : osvVuln.getCredits().stream()
                .map(it -> toEntity(it, vuln)).filter(Objects::nonNull).collect(Collectors.toList()));
        vuln.setAffectedPackages(Objects.isNull(osvVuln.getAffected()) ? new ArrayList<>()
                : osvVuln.getAffected().stream()
                .map(it -> toEntity(it, vuln)).filter(Objects::nonNull).collect(Collectors.toList()));
        return vuln;
    }

    private Severity toEntity(OsvSeverity osvSeverity, Vulnerability vuln) {
        if (Objects.isNull(osvSeverity)) {
            return null;
        }

        var entity = new Severity();
        entity.setScoringSystem(osvSeverity.getType());
        Double score = CvssUtil.calculateScore(osvSeverity.getScore());
        entity.setScore(score);
        entity.setVector(osvSeverity.getScore());
        entity.setSeverity(CvssSeverity.calculateCvssSeverity(osvSeverity.getType(), score));
        entity.setVulnerability(vuln);
        return entity;
    }

    private Reference toEntity(OsvReference osvReference, Vulnerability vuln) {
        if (Objects.isNull(osvReference) || Objects.isNull(osvReference.getType())
                || Objects.isNull(osvReference.getUrl())) {
            return null;
        }

        var entity = new Reference();
        entity.setType(osvReference.getType());
        entity.setUrl(osvReference.getUrl());
        entity.setVulnerability(vuln);
        return entity;
    }

    private Credit toEntity(OsvCredit osvCredit, Vulnerability vuln) {
        if (Objects.isNull(osvCredit) || Objects.isNull(osvCredit.getName())
                || ObjectUtils.isEmpty(osvCredit.getContact())) {
            return null;
        }

        var entity = new Credit();
        entity.setName(osvCredit.getName());
        entity.setContacts(osvCredit.getContact());
        entity.setVulnerability(vuln);
        return entity;
    }

    private AffectedPackage toEntity(OsvAffected osvAffected, Vulnerability vuln) {
        if (Objects.isNull(osvAffected)) {
            return null;
        }

        var entity = new AffectedPackage();
        entity.setEcosystem(osvAffected.getPkg().getEcosystem());
        entity.setName(osvAffected.getPkg().getName());
        entity.setPurl(osvAffected.getPkg().getPurl());
        entity.setVersions(osvAffected.getVersions());
        entity.setEcosystemSpecific(osvAffected.getEcosystemSpecific());
        entity.setDatabaseSpecific(osvAffected.getDatabaseSpecific());
        entity.setRanges(Objects.isNull(osvAffected.getRanges()) ? new ArrayList<>()
                : osvAffected.getRanges().stream()
                .map(it -> toEntity(it, entity)).filter(Objects::nonNull).collect(Collectors.toList()));
        entity.setVulnerability(vuln);
        return entity;
    }

    private AffectedRange toEntity(OsvRange osvRange, AffectedPackage affectedPackage) {
        if (Objects.isNull(osvRange)) {
            return null;
        }

        var entity = new AffectedRange();
        entity.setType(osvRange.getType());
        entity.setRepo(osvRange.getRepo());
        entity.setEvents(Objects.isNull(osvRange.getEvents()) ? new ArrayList<>()
                : osvRange.getEvents().stream()
                .map(it -> toEntity(it, entity)).filter(Objects::nonNull).collect(Collectors.toList()));
        entity.setAffectedPackage(affectedPackage);
        return entity;
    }

    private AffectedEvent toEntity(OsvEvent osvEvent, AffectedRange affectedRange) {
        if (Objects.isNull(osvEvent)) {
            return null;
        }

        var entity = new AffectedEvent();
        if (osvEvent.size() != 1) {
            throw new RuntimeException("An osv event must only contains one event. Invalid record: %s"
                    .formatted(osvEvent));
        }
        entity.setType(osvEvent.keySet().stream().toList().get(0));
        entity.setValue(osvEvent.values().stream().toList().get(0));
        entity.setRange(affectedRange);
        return entity;
    }

    public OsvEvent toVo(AffectedEvent affectedEvent) {
        var vo = new OsvEvent();
        vo.put(affectedEvent.getType(), affectedEvent.getValue());
        return vo;
    }

    private OsvAffected toVo(AffectedPackage affectedPackage) {
        var vo = new OsvAffected();
        var osvPackage = new OsvPackage();
        osvPackage.setEcosystem(affectedPackage.getEcosystem());
        osvPackage.setName(affectedPackage.getName());
        osvPackage.setPurl(affectedPackage.getPurl());
        vo.setPkg(osvPackage);
        vo.setRanges(affectedPackage.getRanges().stream().map(this::toVo).toList());
        vo.setVersions(affectedPackage.getVersions());
        vo.setDatabaseSpecific(affectedPackage.getDatabaseSpecific());
        vo.setEcosystemSpecific(affectedPackage.getEcosystemSpecific());
        return vo;
    }

    private OsvRange toVo(AffectedRange affectedRange) {
        var vo = new OsvRange();
        vo.setType(affectedRange.getType());
        vo.setRepo(affectedRange.getRepo());
        vo.setEvents(affectedRange.getEvents().stream().map(this::toVo).toList());
        vo.setDatabaseSpecific(affectedRange.getDatabaseSpecific());
        return vo;
    }

    private OsvCredit toVo(Credit credit) {
        var vo = new OsvCredit();
        vo.setName(credit.getName());
        vo.setContact(credit.getContacts());
        return vo;
    }

    private OsvReference toVo(Reference reference) {
        var vo = new OsvReference();
        vo.setType(reference.getType());
        vo.setUrl(reference.getUrl());
        return vo;
    }

    private OsvSeverity toVo(Severity severity) {
        var vo = new OsvSeverity();
        vo.setType(severity.getScoringSystem());
        vo.setScore(severity.getVector());
        return vo;
    }

    private OsvVulnerability toVo(Vulnerability vuln) {
        var vo = new OsvVulnerability();
        vo.setSchemaVersion(vuln.getSchemaVersion());
        vo.setId(vuln.getVulnId());
        vo.setModified(Objects.isNull(vuln.getModified()) ? null : vuln.getModified().toInstant().toString());
        vo.setPublished(Objects.isNull(vuln.getPublished()) ? null : vuln.getPublished().toInstant().toString());
        vo.setWithdrawn(Objects.isNull(vuln.getWithdrawn()) ? null : vuln.getWithdrawn().toInstant().toString());
        vo.setAliases(vuln.getAliases());
        vo.setRelated(vuln.getRelated());
        vo.setSummary(vuln.getSummary());
        vo.setDetails(vuln.getDetail());
        vo.setSeverity(vuln.getSeverities().stream().map(this::toVo).toList());
        vo.setAffected(vuln.getAffectedPackages().stream().map(this::toVo).toList());
        vo.setReferences(vuln.getReferences().stream().map(this::toVo).toList());
        vo.setCredits(vuln.getCredits().stream().map(this::toVo).toList());
        vo.setDatabaseSpecific(vuln.getDatabaseSpecific());
        return vo;
    }

    public OsvVulnWithSource toVulnWithSource(Vulnerability vuln) {
        return new OsvVulnWithSource(toVo(vuln), vuln.getSource());
    }

    private Vulnerability update(Vulnerability existVuln, Vulnerability newVuln) {
        if (Objects.isNull(existVuln)) {
            return newVuln;
        }

        if (Objects.equals(existVuln, newVuln)) {
            return existVuln;
        }

        existVuln.setSchemaVersion(newVuln.getSchemaVersion());
        existVuln.setAliases(newVuln.getAliases());
        existVuln.setRelated(newVuln.getRelated());
        existVuln.setModified(newVuln.getModified());
        existVuln.setPublished(newVuln.getPublished());
        existVuln.setWithdrawn(newVuln.getWithdrawn());
        existVuln.setSummary(newVuln.getSummary());
        existVuln.setDetail(newVuln.getDetail());
        existVuln.setDatabaseSpecific(newVuln.getDatabaseSpecific());

        existVuln.getSeverities().stream().filter(it -> !newVuln.getSeverities().contains(it)).toList()
                .forEach(existVuln::removeSeverity);
        newVuln.getSeverities().forEach(existVuln::addSeverity);

        existVuln.getReferences().stream().filter(it -> !newVuln.getReferences().contains(it)).toList()
                .forEach(existVuln::removeReference);
        newVuln.getReferences().forEach(existVuln::addReference);

        existVuln.getCredits().stream().filter(it -> !newVuln.getCredits().contains(it)).toList()
                .forEach(existVuln::removeCredit);
        newVuln.getCredits().forEach(existVuln::addCredit);

        existVuln.getAffectedPackages().stream().filter(it -> !newVuln.getAffectedPackages().contains(it)).toList()
                .forEach(existVuln::removeAffectedPackage);
        newVuln.getAffectedPackages().forEach(existVuln::addAffectedPackage);

        return existVuln;
    }

    private static final Logger logger = LoggerFactory.getLogger(OsvEntityHelper.class);

    /**
     * Batch update existing vulnerabilities or insert new vulnerabilities.
     *
     * @param vulns new vulnerabilities.
     */
    public void batchUpsert(VulnSource vulnSource, List<Vulnerability> vulns) {
        var existVulns = vulnerabilityRepository
                .findBySourceAndVulnIds(vulnSource, vulns.stream().map(Vulnerability::getVulnId).toList())
                .stream()
                .collect(Collectors.toMap(Vulnerability::getVulnId, Function.identity()));
        var newVulns = vulns.stream().map(it -> update(existVulns.get(it.getVulnId()), it)).collect(Collectors.toList());
        newVulns.removeAll(existVulns.values());
        if (!ObjectUtils.isEmpty(newVulns)) {
            vulnerabilityRepository.saveAll(newVulns);
        }
    }
}
