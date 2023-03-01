package org.opensourceway.uvp.helper.ecosystem;

import org.opensourceway.uvp.enums.Ecosystem;
import org.springframework.stereotype.Component;

@Component
public class MavenHelper extends AbstractEcosystemHelper {
    @Override
    protected void register() {
        ecosystemHelperSelector.register(Ecosystem.MAVEN, this);
    }
}
