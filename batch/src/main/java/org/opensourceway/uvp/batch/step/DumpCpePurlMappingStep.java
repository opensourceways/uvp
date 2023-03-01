package org.opensourceway.uvp.batch.step;

import org.jetbrains.annotations.NotNull;
import org.opensourceway.uvp.dao.CpePurlRepository;
import org.opensourceway.uvp.entity.CpePurl;
import org.opensourceway.uvp.utility.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DumpCpePurlMappingStep implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(DumpCpePurlMappingStep.class);

    private static final String CPES_YML = "cpes.yml";

    private static final String PURLS_YML = "purls.yml";

    @Value("${purl2cpe.dump.url}")
    private String purl2cpeDumpUrl;

    @Autowired
    private WebUtil webUtil;

    @Autowired
    private CpePurlRepository cpePurlRepository;

    @Override
    @Retryable(backoff = @Backoff(delay = 5000, multiplier = 2.0))
    public RepeatStatus execute(@NotNull StepContribution contribution, @NotNull ChunkContext chunkContext) throws Exception {
        logger.info("Start to dump CPE-PURL mapping.");

        Map<Pair<String, String>, Cpes> cpeMap = new HashMap<>();
        Map<Pair<String, String>, Purls> purlMap = new HashMap<>();
        try (ZipInputStream is = new ZipInputStream(webUtil.getFileContext(purl2cpeDumpUrl)
                .asInputStream(true))) {
            ZipEntry entry;
            while ((entry = is.getNextEntry()) != null) {
                if (entry.getName().endsWith(CPES_YML)) {
                    var vendor = Path.of(entry.getName()).getParent().getFileName().toString();
                    var product = Path.of(entry.getName()).getParent().getParent().getFileName().toString();
                    cpeMap.put(Pair.of(vendor, product),
                            new Yaml(new Constructor(Cpes.class)).load(new ByteArrayInputStream(is.readAllBytes())));
                } else if (entry.getName().endsWith(PURLS_YML)) {
                    var vendor = Path.of(entry.getName()).getParent().getFileName().toString();
                    var product = Path.of(entry.getName()).getParent().getParent().getFileName().toString();
                    purlMap.put(Pair.of(vendor, product),
                            new Yaml(new Constructor(Purls.class)).load(new ByteArrayInputStream(is.readAllBytes())));
                }
            }
        }

        cpePurlRepository.deleteAll();
        cpePurlRepository.flush();
        cpePurlRepository.saveAll(cpeMap.entrySet()
                .stream()
                .map(entry -> {
                    var cpePurl = new CpePurl();
                    cpePurl.setVendor(entry.getKey().getFirst());
                    cpePurl.setProduct(entry.getKey().getSecond());
                    cpePurl.setCpes(entry.getValue().getCpes());
                    cpePurl.setPurls(purlMap.get(entry.getKey()).getPurls());
                    return cpePurl;
                })
                .toList());

        logger.info("End to dump <{}> CPE-PURL mapping.", cpeMap.size());
        return RepeatStatus.FINISHED;
    }

    public static class Cpes {
        private List<String> cpes;

        public Cpes() {

        }

        public Cpes(List<String> cpes) {
            this.cpes = cpes;
        }

        public List<String> getCpes() {
            return cpes;
        }

        public void setCpes(List<String> cpes) {
            this.cpes = cpes;
        }
    }

    public static class Purls {
        private List<String> purls;

        public Purls() {

        }

        public Purls(List<String> purls) {
            this.purls = purls;
        }

        public List<String> getPurls() {
            return purls;
        }

        public void setPurls(List<String> purls) {
            this.purls = purls;
        }
    }
}
