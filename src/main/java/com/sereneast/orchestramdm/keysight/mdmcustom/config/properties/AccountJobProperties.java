package com.sereneast.orchestramdm.keysight.mdmcustom.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="keysight.job.account")
public class AccountJobProperties extends JobProperties {
}
