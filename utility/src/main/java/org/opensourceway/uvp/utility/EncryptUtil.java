package org.opensourceway.uvp.utility;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.opensourceway.uvp.constant.CacheConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class EncryptUtil {

    @Value("${encryption.password}")
    private String encryptionPassword;

    private PooledPBEStringEncryptor encryptor;

    @PostConstruct
    private void initEncryptor() throws Exception {
        if (StringUtils.isEmpty(encryptionPassword)) {
            throw new Exception("Get empty encryption password, " +
                    "please set the <ENCRYPTION_PASSWORD> environment variable");
        }
        encryptor = new PooledPBEStringEncryptor();
        var config = new SimpleStringPBEConfig();
        config.setPassword(encryptionPassword);
        config.setKeyObtentionIterations("1000");
        config.setPoolSize(1);
        encryptor.setConfig(config);
    }

    public String encrypt(String message) {
        return encryptor.encrypt(message);
    }

    @Cacheable(value = {CacheConstant.DECRYPT_CACHE})
    public String decrypt(String encryptedMessage) {
        return encryptor.decrypt(encryptedMessage);
    }
}
