package com.sereneast.orchestramdm.keysight.mdmcustom;

import com.orchestranetworks.schema.Path;
import com.orchestranetworks.schema.SchemaExtensions;
import com.orchestranetworks.schema.SchemaExtensionsContext;
import com.orchestranetworks.service.AccessRule;
import com.orchestranetworks.service.ServiceKey;
import com.sereneast.orchestramdm.keysight.mdmcustom.service.RecordSampleServiceDeclaration;
import com.sereneast.orchestramdm.keysight.mdmcustom.service.RecordUpdateShowUpdatedByService;
import com.sereneast.orchestramdm.keysight.mdmcustom.service.SampleServiceDeclarationAbstract;

public class SchemaServices implements SchemaExtensions {
    @Override
    public void defineExtensions(SchemaExtensionsContext aContext)
    {
       /* ServiceKey serviceKey = ServiceKey.forName(RecordUpdateShowUpdatedByService.class.getSimpleName());
        SampleServiceDeclarationAbstract service = new RecordSampleServiceDeclaration(
                serviceKey,
                RecordUpdateShowUpdatedByService.class,
                "Update Record",
                "Update Record",
                "Update Selected Record",
                toPath("/root/Account"));
            // Register custom user service declaration.
        aContext.setAccessRuleOnOccurrence(Paths._Account.getPathInSchema(), AccessRule.ALWAYS_READ_ONLY);
        aContext.registerUserService(service);*/
    }

    private static Path[] toPath(String... paths)
    {
        Path[] parsedPaths = new Path[paths.length];
        for (int i = 0; i < paths.length; ++i)
        {
            parsedPaths[i] = Path.parse(paths[i]);
        }
        return parsedPaths;
    }
}
