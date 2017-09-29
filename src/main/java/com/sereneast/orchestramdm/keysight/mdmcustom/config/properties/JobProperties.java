package com.sereneast.orchestramdm.keysight.mdmcustom.config.properties;

import java.util.Map;

public class JobProperties {
    private Boolean enabled=false;
    private String name;
    private Integer chunksize;
    private Integer fetchSize;
    private Integer batchSize;
    private Boolean writeIndividual=false;
    private Map<String,String> mapping;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getChunksize() {
        return chunksize;
    }

    public void setChunksize(Integer chunksize) {
        this.chunksize = chunksize;
    }

    public Integer getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(Integer fetchSize) {
        this.fetchSize = fetchSize;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Map<String, String> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }

    public Boolean getWriteIndividual() {
        return writeIndividual;
    }

    public void setWriteIndividual(Boolean writeIndividual) {
        this.writeIndividual = writeIndividual;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
