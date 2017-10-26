package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.RequestResult;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.ObjectKey;
import com.orchestranetworks.userservice.UserServiceObjectContextBuilder;
import com.orchestranetworks.userservice.UserServiceSetupObjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class AlignForeignKeysAddressService extends RealignForeignKeysService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlignForeignKeysAddressService.class);

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
            oKey = ObjectKey.forName("ADDRESS"+i);
            aBuilder.registerRecordOrDataSet(oKey,record);
            getObjectKeys().add(oKey);
            i++;
        }
    }
}
