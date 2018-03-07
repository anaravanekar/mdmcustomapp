package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationName;
import com.orchestranetworks.instance.HomeKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.service.*;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.RestProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.SOAPConnector;
import com.sereneast.orchestramdm.keysight.mdmcustom.ws.CreateVersionRequestType;
import com.sereneast.orchestramdm.keysight.mdmcustom.ws.CreateVersionResponseType;
import com.sereneast.orchestramdm.keysight.mdmcustom.ws.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@Service
public class BackupService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackupService.class);

    public String takeSnapshot(String dataSpace){
        RestProperties restProperties = (RestProperties)SpringContext.getApplicationContext().getBean("restProperties");
        ObjectFactory factory = new ObjectFactory();
        CreateVersionRequestType requestType = factory.createCreateVersionRequestType();
        requestType.setBranch(dataSpace);
        Object request = factory.createCreateVersion(requestType);
        SOAPConnector soapConnector = (SOAPConnector) SpringContext.getApplicationContext().getBean("soapConnector");
        WebServiceTemplate template = soapConnector.getWebServiceTemplate();
        JAXBElement<CreateVersionResponseType> response = (JAXBElement<CreateVersionResponseType>)template.marshalSendAndReceive(request, message->{
            SOAPMessage soapMessage = ((SaajSoapMessage)message).getSaajMessage();
            SOAPHeader header = null;
            try {
                header = soapMessage.getSOAPHeader();
                SOAPHeaderElement security = header.addHeaderElement(new QName("http://schemas.xmlsoap.org/ws/2002/04/secext", "Security", "sec"));
                SOAPElement usernameToken = security.addChildElement("UsernameToken", "sec");
                SOAPElement username = usernameToken.addChildElement("Username", "sec");
                SOAPElement password = usernameToken.addChildElement("Password", "sec");

                username.setTextContent(restProperties.getOrchestra().getUsername());
                password.setTextContent(restProperties.getOrchestra().getPassword());
            } catch (SOAPException e) {
                e.printStackTrace();
            }
        });
        LOGGER.debug("snapshot soap response status = "+response.getValue().getStatus());
        return response.getValue().getVersionName().substring(1,response.getValue().getVersionName().length());
    }

    public boolean doExport(String snapshotDataSpace, String snapshotName){
        RestProperties restProperties = (RestProperties)SpringContext.getApplicationContext().getBean("restProperties");
        Session session = Repository.getDefault().createSessionFromLoginPassword(restProperties.getOrchestra().getUsername(), restProperties.getOrchestra().getPassword());
        AdaptationHome dataSpace = Repository.getDefault().lookupHome(HomeKey.forVersionName(snapshotName));
        ProgrammaticService svc = ProgrammaticService.createForSession(session, dataSpace);
        Procedure proc = new Procedure()
        {
            public void execute(ProcedureContext pContext) throws Exception
            {
                ArchiveExportSpec spec = new ArchiveExportSpec();
                spec.addInstance(AdaptationName.forName("Account"),true);
                String fileName = snapshotDataSpace+"_"+snapshotName.replaceAll(":","").substring(0,15)+".ebx";
                java.nio.file.Path exportFilePath = java.nio.file.Paths.get(System.getProperty("ebx.home"),"ebxRepository","archives",fileName);
                if(!Files.exists(exportFilePath)){
                    Files.createFile(exportFilePath);
                }
                //OutputStream f = new FileOutputStream(System.getProperty("ebx.home")+"\\ebxRepository\\archives\\"+snapshotDataSpace+"_"+snapshotName.replaceAll(":","").substring(0,15)+".ebx");
                OutputStream outputStream = new FileOutputStream(exportFilePath.toFile());
                Archive archive = Archive.forOutputStream(outputStream);
                spec.setArchive(archive);
                pContext.doExportArchive(spec);
                try{
                    outputStream.close();
                }catch (IOException e){
                    //ignore
                }
            }
        };
        ProcedureResult res = svc.execute(proc);
        if(res.hasFailed()){
            LOGGER.error("Error exporting snapshot- "+snapshotName+" ",res.getException());
        }
        return !res.hasFailed();
    }
}
