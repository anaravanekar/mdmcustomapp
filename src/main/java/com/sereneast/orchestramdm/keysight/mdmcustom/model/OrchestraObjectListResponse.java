package com.sereneast.orchestramdm.keysight.mdmcustom.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrchestraObjectListResponse {
    private List<OrchestraObjectResponse> rows;

    public List<OrchestraObjectResponse> getRows() {
        return rows;
    }

    public void setRows(List<OrchestraObjectResponse> rows) {
        this.rows = rows;
    }
}
