package com.sereneast.orchestramdm.keysight.mdmcustom.config.properties;

import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.ebx.Matching;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="keysight.ebx")
@Component
public class EbxProperties {
    private Matching matching;

    public Matching getMatching() {
        return matching;
    }

    public void setMatching(Matching matching) {
        this.matching = matching;
    }
}
