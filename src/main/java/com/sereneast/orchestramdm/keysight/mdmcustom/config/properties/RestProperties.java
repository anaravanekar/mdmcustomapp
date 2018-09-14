package com.sereneast.orchestramdm.keysight.mdmcustom.config.properties;

import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.ws.Jitterbit;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.ws.Orchestra;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.ws.Sfdc;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="keysight.rest")
@Component
public class RestProperties {
    private Orchestra orchestra;
    private Jitterbit jitterbit;
    private Sfdc sfdc;

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

    public Sfdc getSfdc() {
        return sfdc;
    }

    public void setSfdc(Sfdc sfdc) {
        this.sfdc = sfdc;
    }
}
