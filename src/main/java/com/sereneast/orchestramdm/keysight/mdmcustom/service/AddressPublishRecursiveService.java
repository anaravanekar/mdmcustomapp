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

public class AddressPublishRecursiveService extends PublishServiceRecursive {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressPublishRecursiveService.class);

    public AddressPublishRecursiveService() throws ClassNotFoundException, IllegalAccessException {
        super();
        setObjectName("ADDRESS");
        setDaqaTargetFieldPath(Paths._Address._DaqaMetaData_TargetRecord);
        setJitterbitBaseUrl("https://Keysight.jitterbit.net/Development/1.0/MDM_Customer_Address");
        setFlagFieldPath(Paths._Address._Published);
        setJitterbitrp("Keysight@123");
        setJitterbitUsername("MDM_USER");
        setMdmRestBaseUrl("http://localhost:8080/ebx-dataservices/rest/data/v1");
        setMdmRestUsername("admin");
        setMdmrp("admin");
        setReferenceDataSpaceUrl("BReference");
        setReferenceDataSetUrl("Account");
        setTablePathUrl("root/Address");
        setObjectPrimaryKeyPath(Paths._Address._MDMAddressId);
        setObjectPrimaryKeyType(Integer.class);
        setTablePathInSchema(Paths._Address.getPathInSchema());
        setSystemIdPath(Paths._Address._SystemId);
        setSystemNamePath(Paths._Address._SystemName);
        Map<String, Path> pathFieldsMap = null;
        ApplicationCacheUtil applicationCacheUtil = new ApplicationCacheUtil();
        setFieldPathMap(applicationCacheUtil.getObjectDirectFields(Paths._Address.class.getName()));
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
