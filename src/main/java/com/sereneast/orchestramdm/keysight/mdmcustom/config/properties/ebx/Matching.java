package com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.ebx;

import java.util.List;
import java.util.Map;

public class Matching {
    private Map<String,List<String>> lovsToMerge;

    public Map<String, List<String>> getLovsToMerge() {
        return lovsToMerge;
    }

    public void setLovsToMerge(Map<String, List<String>> lovsToMerge) {
        this.lovsToMerge = lovsToMerge;
    }
}
