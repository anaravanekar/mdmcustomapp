package com.sereneast.orchestramdm.keysight.mdmcustom.config.properties;

import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.db.Ebx;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="keysight.database")
@Component
public class DatabaseProperties {
    private Ebx ebx;

    public Ebx getEbx() {
        return ebx;
    }

    public void setEbx(Ebx ebx) {
        this.ebx = ebx;
    }
}
