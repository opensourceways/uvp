package org.opensourceway.uvp.helper.ecosystem;

import org.opensourceway.uvp.enums.Ecosystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EcosystemHelperSelector {

    @Autowired
    @Qualifier("defaultEcosystemHelper")
    @Lazy
    private EcosystemHelper defaultEcosystemHelper;

    private final Map<Ecosystem, EcosystemHelper> ecosystemToHelper = new HashMap<>();

    public void register(Ecosystem ecosystem, EcosystemHelper helper) {
        ecosystemToHelper.put(ecosystem, helper);
    }

    /**
     * Get a {@link EcosystemHelper} that supports the given ecosystem.
     *
     * @param ecosystem An {@link Ecosystem}.
     * @return The {@link EcosystemHelper} that supports the given ecosystem if exists,
     * otherwise the default {@link DefaultEcosystemHelper}.
     */
    public EcosystemHelper getHelper(Ecosystem ecosystem) {
        return ecosystemToHelper.getOrDefault(ecosystem, defaultEcosystemHelper);
    }
}
