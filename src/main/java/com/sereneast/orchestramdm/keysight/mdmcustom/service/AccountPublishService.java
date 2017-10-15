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

public class AccountPublishService extends PublishService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountPublishService.class);

    public AccountPublishService() throws ClassNotFoundException, IllegalAccessException {
        super();
        setObjectName("ACCOUNT");
        setDaqaTargetFieldPath(Paths._Account._DaqaMetaData_TargetRecord);
        setJitterbitBaseUrl("https://Keysight.jitterbit.net/Development/1.0/MDM_Customer_to_Oracle");
        setFlagFieldPath(Paths._Account._Published);
        setJitterbitrp("Keysight@123");
        setJitterbitUsername("MDM_USER");
        setMdmRestBaseUrl("http://localhost:8080/ebx-dataservices/rest/data/v1");
        setMdmRestUsername("admin");
        setMdmrp("admin");
        setReferenceDataSpaceUrl("BReference");
        setReferenceDataSetUrl("Account");
        setTablePathUrl("root/Account");
        setObjectPrimaryKeyPath(Paths._Account._MDMAccountId);
        setObjectPrimaryKeyType(Integer.class);
        setTablePathInSchema(Paths._Account.getPathInSchema());
        setSystemIdPath(Paths._Account._SystemId);
        setSystemNamePath(Paths._Account._SystemName);
        Map<String, Path> pathFieldsMap = null;
        ApplicationCacheUtil applicationCacheUtil = new ApplicationCacheUtil();
        setFieldPathMap(applicationCacheUtil.getObjectDirectFields(Paths._Account.class.getName()));
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
            ObjectKey oKey = null;
            oKey = ObjectKey.forName("ACCOUNT"+i);
            aBuilder.registerRecordOrDataSet(oKey,record);
            getObjectKeys().add(oKey);
            i++;
        }
    }
}
