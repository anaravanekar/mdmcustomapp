package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationFilter;
import com.orchestranetworks.schema.Path;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AsianFilter implements AdaptationFilter {

    private List<String> asianCountries;

    public AsianFilter(){
        asianCountries = new ArrayList<String>(Arrays.asList("KR", "TW", "CN", "MO"));
    }

    @Override
    public boolean accept(Adaptation adaptation) {
        return asianCountries.contains(adaptation.getString(Paths._Account._Country));
    }
}
