package org.opensourceway.uvp.utility;

import org.junit.jupiter.api.Test;
import org.opensourceway.uvp.enums.ScoringSystem;

import static org.assertj.core.api.Assertions.assertThat;

class CvssUtilTest {

    @Test
    void calculateScore() {
        assertThat(CvssUtil.calculateScore("CVSS:3.1/AV:N/AC:H/PR:N/UI:N/S:C/C:H/I:H/A:H")).isEqualTo(9.0);
        assertThat(CvssUtil.calculateScore("AV:N/AC:H/PR:N/UI:N/S:C/C:H/I:H/A:H")).isEqualTo(9.0);
        assertThat(CvssUtil.calculateScore("CVSS:3.0/AV:N/AC:L/PR:N/UI:R/S:U/C:H/I:N/A:N")).isEqualTo(6.5);
        assertThat(CvssUtil.calculateScore("AV:N/AC:L/PR:N/UI:R/S:U/C:H/I:N/A:N")).isEqualTo(6.5);
        assertThat(CvssUtil.calculateScore("(AV:N/AC:H/Au:N/C:P/I:P/A:P)")).isEqualTo(5.1);
        assertThat(CvssUtil.calculateScore("AV:N/AC:H/Au:N/C:P/I:P/A:P")).isEqualTo(5.1);
    }

    @Test
    void judgeScoringSystem() {
        assertThat(CvssUtil.judgeScoringSystem("CVSS:3.1/AV:N/AC:H/PR:N/UI:N/S:C/C:H/I:H/A:H")).isEqualTo(ScoringSystem.CVSS_V3);
        assertThat(CvssUtil.judgeScoringSystem("AV:N/AC:H/PR:N/UI:N/S:C/C:H/I:H/A:H")).isEqualTo(ScoringSystem.CVSS_V3);
        assertThat(CvssUtil.judgeScoringSystem("CVSS:3.0/AV:N/AC:L/PR:N/UI:R/S:U/C:H/I:N/A:N")).isEqualTo(ScoringSystem.CVSS_V3);
        assertThat(CvssUtil.judgeScoringSystem("AV:N/AC:L/PR:N/UI:R/S:U/C:H/I:N/A:N")).isEqualTo(ScoringSystem.CVSS_V3);
        assertThat(CvssUtil.judgeScoringSystem("(AV:N/AC:H/Au:N/C:P/I:P/A:P)")).isEqualTo(ScoringSystem.CVSS_V2);
        assertThat(CvssUtil.judgeScoringSystem("AV:N/AC:H/Au:N/C:P/I:P/A:P")).isEqualTo(ScoringSystem.CVSS_V2);
    }
}