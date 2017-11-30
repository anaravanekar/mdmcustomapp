package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.RequestResult;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.ObjectKey;
import com.orchestranetworks.userservice.UserServiceObjectContextBuilder;
import com.orchestranetworks.userservice.UserServiceSetupObjectContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

public class BusinessPurposePublishServiceBulk extends PublishServiceBulk {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessPurposePublishServiceBulk.class);

    public BusinessPurposePublishServiceBulk() throws ClassNotFoundException, IllegalAccessException {
        super();
        setObjectName("BUSINESSPURPOSE");
//        setFlagFieldPath(Paths._BusinessPurpose._Published);
        setReferenceDataSpaceUrl("BReference");
        setReferenceDataSetUrl("Account");
        setTablePathUrl("root/BusinessPurpose");
//        setObjectPrimaryKeyPath(Paths._BusinessPurpose._MDMPurposeId);
        setObjectPrimaryKeyType(Integer.class);
//        setTablePathInSchema(Paths._BusinessPurpose.getPathInSchema());
        Map<String, Path> pathFieldsMap = null;
        ApplicationCacheUtil applicationCacheUtil = new ApplicationCacheUtil();
        setFieldPathMap(applicationCacheUtil.getObjectDirectFields(Paths._BusinessPurpose.class.getName()));
        setCheckParentIsPublished(true);
        setParentIdPath(Paths._Address._MDMAddressId);
//        setParentForeignKeyPath(Paths._BusinessPurpose._MDMAddressId);
        setParentPathInSchema(Paths._Address.getPathInSchema());
        setCheckParentIsPublished(true);
    }

    @Override
    public void setupObjectContext(
            UserServiceSetupObjectContext<TableViewEntitySelection> aContext,
            UserServiceObjectContextBuilder aBuilder)
    {
        LOGGER.debug("In Setup Object Context");
        LOGGER.debug("Dataspace name/label="+aContext.getEntitySelection().getDataspace().getLabelOrName(Locale.ENGLISH));
        LOGGER.debug("Dataspace label="+aContext.getEntitySelection().getDataspace().getLabel());
        RequestResult result = aContext.getEntitySelection().getSelectedRecords().execute();
        int i= 0;
        for (Adaptation record; (record = result.nextAdaptation()) != null; ) {
            ObjectKey oKey = null;//
            oKey = ObjectKey.forName("ADDRESS"+i);
            aBuilder.registerRecordOrDataSet(oKey,record);
            getObjectKeys().add(oKey);
            i++;
        }
    }
}
