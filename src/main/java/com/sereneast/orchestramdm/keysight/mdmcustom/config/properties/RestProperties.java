package com.sereneast.orchestramdm.keysight.mdmcustom.config.properties;

import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.ws.Jitterbit;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.ws.Orchestra;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="keysight.rest")
@Component
public class RestProperties {
    private Orchestra orchestra;
    private Jitterbit jitterbit;

    public Orchestra getOrchestra() {
        return orchestra;
    }

    public void setOrchestra(Orchestra orchestra) {
        this.orchestra = orchestra;
    }

    public Jitterbit getJitterbit() {
        return jitterbit;
    }

    public void setJitterbit(Jitterbit jitterbit) {
        this.jitterbit = jitterbit;
    }
}
