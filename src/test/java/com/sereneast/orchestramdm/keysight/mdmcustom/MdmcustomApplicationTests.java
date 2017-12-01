package com.sereneast.orchestramdm.keysight.mdmcustom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.orchestranetworks.schema.Path;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.EbxProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.RestProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.email.EmailHtmlSender;
import com.sereneast.orchestramdm.keysight.mdmcustom.email.EmailStatus;
import com.sereneast.orchestramdm.keysight.mdmcustom.exception.ApplicationRuntimeException;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraContent;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObject;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObjectList;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.RestResponse;
import com.sereneast.orchestramdm.keysight.mdmcustom.rest.client.JitterbitRestClient;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.AppUtil;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.context.Context;

import javax.ws.rs.core.Response;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MdmcustomApplicationTests {
	private static final Logger LOGGER = LoggerFactory.getLogger(MdmcustomApplication.class);
	private static final String DATA_SPACE = "BReference";

	private static final String DATA_SET = "Account";

	private static final String PATH_ACCOUNT = "root/Account";

	private static final String PATH_ADDRESS = "root/Address";

	@Test
	public void contextLoads() {
	}

	@Test
	public void testProps() {
		System.out.println("\npropvalue="+((RestProperties)SpringContext.getApplicationContext().getBean("restProperties")).getOrchestra().getPassword());
	}

	public static void main(String[] args) throws IllegalAccessException, IOException {
		/*Map<String,Path> fields = new HashMap<>();
		Field[] accountPathFields = Paths._Account.class.getDeclaredFields();
		for(Field pathField: accountPathFields){
			System.out.println(!Modifier.isPrivate(pathField.getModifiers()));
			if(!Modifier.isPrivate(pathField.getModifiers())) {
				Path path = (Path) pathField.get(null);
				String key = path.format().replaceAll("\\.\\/", "");
				if(!key.contains("DaqaMetaData/")) {
					System.out.println(path);
					System.out.println(key);
					fields.put(key,path);
				}
			}
		}
		ObjectMapper mapper = new ObjectMapper();*/
		/*OrchestraRestClient orchestraRestClient = new OrchestraRestClient();
		orchestraRestClient.setBaseUrl("http://localhost:8080/ebx-dataservices/rest/data/v1");
		orchestraRestClient.setFeature(HttpAuthenticationFeature.basic("admin", "admin"));
		String jsonString = "{\"rows\":[{\"content\":{\"ATS\":{\"content\":null},\"FullAddress\":{\"content\":\"Test AD 107  SUCCESS \"},\"SiteName\":{\"content\":null},\"Address\":{\"content\":null},\"SalesPerson\":{\"content\":null},\"TaxRegistrationActive\":{\"content\":null},\"IdentifyingAddress\":{\"content\":null},\"Tax\":{\"content\":null},\"StartDate\":{\"content\":null},\"AddressLine4LocalLanguage\":{\"content\":null},\"AddressLine2LocalLanguage\":{\"content\":null},\"AccountSiteType\":{\"content\":null},\"OrderType\":{\"content\":null},\"NLSLanguage\":{\"content\":null},\"ReceiptMethods\":{\"content\":null},\"IndustrySubclassification\":{\"content\":null},\"ProvinceLocalLanguage\":{\"content\":null},\"EffectiveTo\":{\"content\":null},\"BillToLocation\":{\"content\":null},\"Status\":{\"content\":null},\"AccountClass\":{\"content\":null},\"Translation\":{\"content\":null},\"RoundingRule\":{\"content\":null},\"IndustryClassification\":{\"content\":null},\"City\":{\"content\":\"Santa Clara\"},\"Province\":{\"content\":null},\"TaxCertificateDate\":{\"content\":null},\"AddressLine3\":{\"content\":\"SUCCESS\"},\"AddressLine2\":{\"content\":null},\"PrimaryPayment\":{\"content\":null},\"AddressLine1\":{\"content\":\"Test AD 108\"},\"TaxJurisdictionCode\":{\"content\":null},\"AddressLine4\":{\"content\":null},\"Country\":{\"content\":\"US\"},\"EfectiveFrom\":{\"content\":null},\"Revenue\":{\"content\":null},\"DefaultTaxRegistration\":{\"content\":null},\"TaxablePerson\":{\"content\":null},\"CountyLocalLanguage\":{\"content\":null},\"BusinessPurpose\":{\"content\":null},\"SiteId\":{\"content\":null},\"BusinessNumber\":{\"content\":null},\"OperatingUnit\":{\"content\":null},\"StateLocalLanguage\":{\"content\":null},\"CalcPostalCOde\":{\"content\":null},\"PostalCode\":{\"content\":null},\"CountryLocalLanguage\":{\"content\":null},\"PaymentTerms\":{\"content\":null},\"County\":{\"content\":null},\"Source\":{\"content\":null},\"AddressLine3LocalLanguage\":{\"content\":null},\"InvoiceCopies\":{\"content\":null},\"SubSegment\":{\"content\":null},\"AddressLine1LocalLanguage\":{\"content\":null},\"OrgSegment\":{\"content\":null},\"SystemName\":{\"content\":\"KS_EBS\"},\"PriceList\":{\"content\":null},\"CityLocalLanguage\":{\"content\":null},\"PostalLocalLanguage\":{\"content\":null},\"DefaultReportingRegistrationNumber\":{\"content\":null},\"RevenueRecognition\":{\"content\":null},\"MDMAccountId\":{\"content\":\"7\"},\"Addressee\":{\"content\":null},\"Reference\":{\"content\":null},\"RegimeCode\":{\"content\":null},\"PrimaryPurpose\":{\"content\":null},\"SystemId\":{\"content\":\"TAD108\"},\"SiteNumber\":{\"content\":null},\"KeysightSFAAddressId\":{\"content\":null},\"EndDate\":{\"content\":null},\"MDMAddressId\":{\"content\":null},\"DemandClass\":{\"content\":null},\"ContextValue\":{\"content\":null},\"Receivable\":{\"content\":null},\"TaxRegistrationNumber\":{\"content\":null},\"DefaultReportingCountryName\":{\"content\":null},\"MergedTargetRecord\":{\"content\":null},\"SendAcknowledgement\":{\"content\":null},\"RPLCheck\":{\"content\":null},\"AddressSiteCategory\":{\"content\":null},\"AddressState\":{\"content\":\"CA\"},\"Location\":{\"content\":null}}}]}";
		OrchestraObjectList orchestraObjectList = mapper.readValue(jsonString, OrchestraObjectList.class);
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("updateOrInsert", "true");
		parameters.put("updateOrInsert", "true");
		OrchestraResponseDetails responseDetails = responseDetails = orchestraRestClient.insert(DATA_SPACE, DATA_SET, PATH_ADDRESS, orchestraObjectList, parameters);
		System.out.println(responseDetails.toString());*/

/*		JitterbitRestClient jitterbitRestClient = new JitterbitRestClient();
		jitterbitRestClient.setBaseUrl("http://localhost:8080/ebx-dataservices/rest/data/v1/BReference/Account/root/Address?updateOrInsert=true");
		jitterbitRestClient.setFeature(HttpAuthenticationFeature.basic("admin", "admin"));
		String jsonString = "{\"rows\":[{\"content\":{\"ATS\":{\"content\":null},\"FullAddress\":{\"content\":\"Test AD 107  SUCCESS \"},\"SiteName\":{\"content\":null},\"Address\":{\"content\":null},\"SalesPerson\":{\"content\":null},\"TaxRegistrationActive\":{\"content\":null},\"IdentifyingAddress\":{\"content\":null},\"Tax\":{\"content\":null},\"StartDate\":{\"content\":null},\"AddressLine4LocalLanguage\":{\"content\":null},\"AddressLine2LocalLanguage\":{\"content\":null},\"AccountSiteType\":{\"content\":null},\"OrderType\":{\"content\":null},\"NLSLanguage\":{\"content\":null},\"ReceiptMethods\":{\"content\":null},\"IndustrySubclassification\":{\"content\":null},\"ProvinceLocalLanguage\":{\"content\":null},\"EffectiveTo\":{\"content\":null},\"BillToLocation\":{\"content\":null},\"Status\":{\"content\":null},\"AccountClass\":{\"content\":null},\"Translation\":{\"content\":null},\"RoundingRule\":{\"content\":null},\"IndustryClassification\":{\"content\":null},\"City\":{\"content\":\"Santa Clara\"},\"Province\":{\"content\":null},\"TaxCertificateDate\":{\"content\":null},\"AddressLine3\":{\"content\":\"SUCCESS\"},\"AddressLine2\":{\"content\":null},\"PrimaryPayment\":{\"content\":null},\"AddressLine1\":{\"content\":\"Test AD 108\"},\"TaxJurisdictionCode\":{\"content\":null},\"AddressLine4\":{\"content\":null},\"Country\":{\"content\":\"US\"},\"EfectiveFrom\":{\"content\":null},\"Revenue\":{\"content\":null},\"DefaultTaxRegistration\":{\"content\":null},\"TaxablePerson\":{\"content\":null},\"CountyLocalLanguage\":{\"content\":null},\"BusinessPurpose\":{\"content\":null},\"SiteId\":{\"content\":null},\"BusinessNumber\":{\"content\":null},\"OperatingUnit\":{\"content\":null},\"StateLocalLanguage\":{\"content\":null},\"CalcPostalCOde\":{\"content\":null},\"PostalCode\":{\"content\":null},\"CountryLocalLanguage\":{\"content\":null},\"PaymentTerms\":{\"content\":null},\"County\":{\"content\":null},\"Source\":{\"content\":null},\"AddressLine3LocalLanguage\":{\"content\":null},\"InvoiceCopies\":{\"content\":null},\"SubSegment\":{\"content\":null},\"AddressLine1LocalLanguage\":{\"content\":null},\"OrgSegment\":{\"content\":null},\"SystemName\":{\"content\":\"KS_EBS\"},\"PriceList\":{\"content\":null},\"CityLocalLanguage\":{\"content\":null},\"PostalLocalLanguage\":{\"content\":null},\"DefaultReportingRegistrationNumber\":{\"content\":null},\"RevenueRecognition\":{\"content\":null},\"MDMAccountId\":{\"content\":\"7\"},\"Addressee\":{\"content\":null},\"Reference\":{\"content\":null},\"RegimeCode\":{\"content\":null},\"PrimaryPurpose\":{\"content\":null},\"SystemId\":{\"content\":\"TAD108\"},\"SiteNumber\":{\"content\":null},\"KeysightSFAAddressId\":{\"content\":null},\"EndDate\":{\"content\":null},\"MDMAddressId\":{\"content\":null},\"DemandClass\":{\"content\":null},\"ContextValue\":{\"content\":null},\"Receivable\":{\"content\":null},\"TaxRegistrationNumber\":{\"content\":null},\"DefaultReportingCountryName\":{\"content\":null},\"MergedTargetRecord\":{\"content\":null},\"SendAcknowledgement\":{\"content\":null},\"RPLCheck\":{\"content\":null},\"AddressSiteCategory\":{\"content\":null},\"AddressState\":{\"content\":\"CA\"},\"Location\":{\"content\":null}}}]}";
		Response response = jitterbitRestClient.insert(jsonString,null);
		System.out.println(response.getStatus());*/
		/*String displayname = "BAGCHI,KUMARESH (Non-K-SantaClara,ex1)";
		if(displayname.contains(" ")){
			displayname = displayname.split("\\s")[0];
		}
		if(displayname.contains(",")){
			System.out.println("first:"+displayname.split(",")[1]);
			System.out.println("last:"+displayname.split(",")[0]);
		}else{
			System.out.println("last:"+displayname);
		}*/

		/*Files.list(Paths.get("E:\\tomcat1\\lib")).forEach((path)->{
			System.out.println(path.getFileName());
		});*/
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HHmm");
		System.out.println(LocalTime.now().format(dtf));


	}

	@Test
	public void testJitterBit() throws IOException, InterruptedException {
		String objectName = "Account";
		String rows = "{\"rows\":[{\"content\":{\"ISGClassification\":{\"content\":null},\"NameLocalLanguage\":{\"content\":null},\"TaxpayerId\":{\"content\":null},\"SalesChannel\":{\"content\":null},\"RMTId\":{\"content\":null},\"GroupingId\":{\"content\":null},\"PaymentStartDate\":{\"content\":null},\"Classification\":{\"content\":\"COMMERCIAL\"},\"SystemName\":{\"content\":\"KS_EBS\"},\"NLSLanguageCode\":{\"content\":null},\"ProfileClass\":{\"content\":\"NEW U.S.\"},\"Status\":{\"content\":\"A\"},\"CustomerScreening\":{\"content\":null},\"MDMAccountId\":{\"content\":15000079919},\"Reference\":{\"content\":null},\"SystemId\":{\"content\":null},\"InternalAccountId\":{\"content\":null},\"ParentParty\":{\"content\":null},\"CustomerType\":{\"content\":\"ORGANIZATION\"},\"AccountType\":{\"content\":\"R\"},\"NamePronunciation\":{\"content\":null},\"CustomerCategory\":{\"content\":null},\"PrimaryPayment\":{\"content\":null},\"TaxRegistrationNumber\":{\"content\":null},\"EmgLastTrans\":{\"content\":null},\"Region\":{\"content\":\"Region - America\"},\"Country\":{\"content\":[{\"content\":\"US\"}]},\"PaymentEndDate\":{\"content\":null},\"Published\":{\"content\":\"U\"},\"CrossReference\":{\"content\":[{\"content\":{\"MDMAccountId\":{\"content\":1500007},\"SystemId\":{\"content\":null},\"SystemName\":{\"content\":\"KS_EBS\"},\"DQState\":{\"content\":\"Golden\"}}}]},\"RegistryId\":{\"content\":null},\"PaymentReceiptMethod\":{\"content\":\"Lockbox\"},\"GroupingDescription\":{\"content\":null},\"AccountName\":{\"content\":\"Test update\"}}}]}";
		OrchestraObjectList original = null;
		ObjectMapper mapper = new ObjectMapper();
		List<OrchestraObject> recordsToUpdateInJitterbit = null;
		original = mapper.readValue(rows,OrchestraObjectList.class);
		recordsToUpdateInJitterbit = original.getRows();
		LOGGER.debug("original:\n",original);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.setDateFormat(df);
		for(OrchestraObject orchestraObject:recordsToUpdateInJitterbit){
			if(!"U".equals(orchestraObject.getContent().get("Published").getContent())){
				orchestraObject.getContent().put("Published",new OrchestraContent("I"));
			}
		}
		OrchestraObjectList orchestraObjectList = new OrchestraObjectList();
		orchestraObjectList.setRows(recordsToUpdateInJitterbit);
            JitterbitRestClient jitterbitRestClient = (JitterbitRestClient)SpringContext.getApplicationContext().getBean("jitterbitRestClient");
            RestResponse response = null;
            int retryCount = 0;
            do {
                if (retryCount > 0) {
                    Thread.sleep(1000L);
                }
                response = jitterbitRestClient.insert(mapper.writeValueAsString(orchestraObjectList), null,objectName.toLowerCase());
                retryCount++;
            } while (retryCount < 1 && (response == null || response.getStatus() >= 300));
            if(response.getStatus()!=200 && response.getStatus()!=201){
				throw new ApplicationRuntimeException("Error publishing to Jitterbit: "+String.valueOf(response.getResponseBody().get("errorMsg")));
            }
	}

	@Test
	public void testKsProps(){
		EbxProperties ebxProperties = (EbxProperties) SpringContext.getApplicationContext().getBean("ebxProperties");
		if(ebxProperties.getRetryWaitJb()!=null){
			System.out.println(ebxProperties.getRetryWaitJb());
		}
		if(ebxProperties.getRetryWaitMdm()!=null){
			System.out.println(ebxProperties.getRetryWaitMdm());
		}
	}
}
