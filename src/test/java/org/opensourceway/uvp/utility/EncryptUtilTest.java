package org.opensourceway.uvp.utility;

import org.junit.jupiter.api.Test;
import org.opensourceway.uvp.UvpApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {UvpApplication.class})
public class EncryptUtilTest {
    @Autowired
    private EncryptUtil encryptUtil;

    @Test
    public void testDecrypt() {
        assertThat(encryptUtil.decrypt("TfYXYl/aMFKY73RQ/+eU+g==")).isEqualTo("message");
        assertThat(encryptUtil.decrypt("3D6PJ0/Ion0fJw+9BnqIKg==")).isEqualTo("message");
    }
}
