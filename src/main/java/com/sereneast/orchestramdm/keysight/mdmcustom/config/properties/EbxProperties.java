package com.sereneast.orchestramdm.keysight.mdmcustom.config.properties;

import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.ebx.Matching;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix="keysight.ebx")
@Component
public class EbxProperties {
    private Matching matching;

    private Integer retryWaitJb;

    private Integer retryWaitMdm;

    private Integer maxRetryJb;

    private Integer maxRetryMdm;

    private List<String> doNotAssignToUsers;

    public Matching getMatching() {
        return matching;
    }

    public void setMatching(Matching matching) {
        this.matching = matching;
    }

    public Integer getRetryWaitJb() {
        return retryWaitJb;
    }

    public void setRetryWaitJb(Integer retryWaitJb) {
        this.retryWaitJb = retryWaitJb;
    }

    public Integer getRetryWaitMdm() {
        return retryWaitMdm;
    }

    public void setRetryWaitMdm(Integer retryWaitMdm) {
        this.retryWaitMdm = retryWaitMdm;
    }

    public Integer getMaxRetryJb() {
        return maxRetryJb;
    }

    public void setMaxRetryJb(Integer maxRetryJb) {
        this.maxRetryJb = maxRetryJb;
    }

    public Integer getMaxRetryMdm() {
        return maxRetryMdm;
    }

    public void setMaxRetryMdm(Integer maxRetryMdm) {
        this.maxRetryMdm = maxRetryMdm;
    }

    public List<String> getDoNotAssignToUsers() {
        return doNotAssignToUsers;
    }

    public void setDoNotAssignToUsers(List<String> doNotAssignToUsers) {
        this.doNotAssignToUsers = doNotAssignToUsers;
    }
}
