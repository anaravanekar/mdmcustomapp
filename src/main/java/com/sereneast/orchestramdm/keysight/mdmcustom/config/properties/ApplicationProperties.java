package com.sereneast.orchestramdm.keysight.mdmcustom.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix="keysight")
public class ApplicationProperties {

    private Map<String,String> queries = new HashMap<String,String>();

    private Map<String,String> orchestraRest = new HashMap<>();

    public Map<String, String> getQueries() {
        return queries;
    }

    public void setQueries(Map<String, String> queries) {
        this.queries = queries;
    }

    public Map<String, String> getOrchestraRest() {
        return orchestraRest;
    }

    public void setOrchestraRest(Map<String, String> orchestraRest) {
        this.orchestraRest = orchestraRest;
    }
}

