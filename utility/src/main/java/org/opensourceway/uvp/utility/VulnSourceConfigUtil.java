package org.opensourceway.uvp.utility;

import org.opensourceway.uvp.dao.VulnSourceConfigRepository;
import org.opensourceway.uvp.entity.VulnSourceConfig;
import org.opensourceway.uvp.enums.VulnSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VulnSourceConfigUtil {
    @Autowired
    private VulnSourceConfigRepository repository;

    public Boolean importEnabled(VulnSource source) {
        return repository.findBySource(source).map(VulnSourceConfig::getImportEnabled).orElse(false);
    }

    public String getImportCron(VulnSource source) {
        return repository.findBySource(source).map(VulnSourceConfig::getImportCron).orElse(null);
    }

    public Boolean subscribeEnabled(VulnSource source) {
        return repository.findBySource(source).map(VulnSourceConfig::getSubscribeEnabled).orElse(false);
    }

    public Boolean queryEnabled(VulnSource source) {
        return repository.findBySource(source).map(VulnSourceConfig::getQueryEnabled).orElse(false);
    }

    public Map<String, String> getConfig(VulnSource source) {
        return repository.findBySource(source).map(VulnSourceConfig::getConfig).orElse(Map.of());
    }

    public String getToken(VulnSource source) {
        return getConfig(source).get(VulnSourceConfig.ConfigKey.TOKEN.name());
    }
}
