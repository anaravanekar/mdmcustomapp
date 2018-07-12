package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationFilter;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;

public class JapanFilter implements AdaptationFilter {
    @Override
    public boolean accept(Adaptation adaptation) {
        return "JP".equals(adaptation.getString(Paths._Account._Country));
    }
}
