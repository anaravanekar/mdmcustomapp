package com.sereneast.orchestramdm.keysight.mdmcustom.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="keysight.job.address")
public class AddressJobProperties extends JobProperties {
}
