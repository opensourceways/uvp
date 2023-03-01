package org.opensourceway.uvp.utility;

import org.opensourceway.uvp.enums.ScoringSystem;
import us.springett.cvss.Cvss;
import us.springett.cvss.CvssV3;

public class CvssUtil {
    public static Double calculateScore(String vector) {
        return Cvss.fromVector(vector).calculateScore().getBaseScore();
    }

    public static ScoringSystem judgeScoringSystem(String vector) {
        return Cvss.fromVector(vector) instanceof CvssV3 ? ScoringSystem.CVSS_V3 : ScoringSystem.CVSS_V2;
    }
}
