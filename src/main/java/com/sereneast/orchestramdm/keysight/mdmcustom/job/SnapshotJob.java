package com.sereneast.orchestramdm.keysight.mdmcustom.job;

import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.service.BackupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SnapshotJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(SnapshotJob.class);

    @Scheduled(cron = "${keysight.jobs.takeSnapshots.cron}")
    public void run() {
        LOGGER.debug("Executing SnapshotJob...");
        BackupService backupService = (BackupService) SpringContext.getApplicationContext().getBean("backupService");
        String name = backupService.takeSnapshot("Reference");
        boolean status = backupService.doExport("Reference",name);
        LOGGER.info("takeSnapshots Reference Status: "+status);
        status = false;
        name = null;
        name = backupService.takeSnapshot("CMDReference");
        status = backupService.doExport("CMDReference",name);
        LOGGER.info("takeSnapshots CMDReference Status: "+status);
    }
}
