package com.sereneast.orchestramdm.keysight.mdmcustom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationHomeBridge;
import com.onwbp.adaptation.AdaptationName;
import com.orchestranetworks.instance.BranchKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.service.*;
import com.orchestranetworks.service.Session;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraContent;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObject;
import com.sereneast.orchestramdm.keysight.mdmcustom.rest.client.JitterbitRestClient;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.SOAPConnector;
import com.sereneast.orchestramdm.keysight.mdmcustom.ws.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MdmcustomApplicationTests {


	private static final String DATA_SPACE = "BReference";

	private static final String DATA_SET = "Account";

	private static final String PATH_ACCOUNT = "root/Account";

	private static final String PATH_ADDRESS = "root/Address";

	@Test
	public void contextLoads() {
	}

	/*@Test
	public void testProps() {
		System.out.println(((RestProperties)SpringContext.getApplicationContext().getBean("restProperties")).getOrchestra().getBaseURI());
	}*/

	public static void main(String[] args) throws IllegalAccessException, IOException {
/*
		Map<String,Path> fields = new HashMap<>();
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
*/
//		ObjectMapper mapper = new ObjectMapper();
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

		/*Path pth = java.nio.file.Paths.get("D:/FFOutput/old");
		if(Files.exists(pth)) {
			Files.walk(pth).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
		}*/
		/*if(!Files.exists(pth)) {
			System.out.println("does not exist");
			pth = Files.createDirectory(pth);
		}*/
		String blah = "12:12:12";
		System.out.println(blah.replaceAll(":",""));
	}

	@Test
	public void testCaching() throws IOException {
		ApplicationCacheUtil applicationCacheUtil = (ApplicationCacheUtil)SpringContext.getApplicationContext().getBean("applicationCacheUtil");
		/*Map<String,Map<String,String>> countryReferenceFieldsMap = applicationCacheUtil.CountryReferenceFieldsMap("BReference");
		List<Map<String,String>> states = new ArrayList<>();
		Map<String,String> fieldMap = new HashMap<>();
		fieldMap.put("StateCode","CA");
		fieldMap.put("State","California");
		states.add(fieldMap);
		fieldMap = new HashMap<>();
		fieldMap.put("StateCode","TX");
		fieldMap.put("State","Texas");
		states.add(fieldMap);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(states));
		Map<String,String> resultItem = countryReferenceFieldsMap!=null?countryReferenceFieldsMap.get("US"):null;
		System.out.println(resultItem.get("ProfileClass"));*/
/*		ObjectMapper mapper = new ObjectMapper();
		List<String> finalOuList = new ArrayList<>();
		finalOuList.add("blah");
		finalOuList.add("yada");
		finalOuList.add("dssf");
		System.out.println("content is : "+mapper.writeValueAsString(new OrchestraContent(finalOuList)));*/

	/*	HashSet<String> addressOuSet = new HashSet<>();
		OrchestraRestClient orchestraRestClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
		OrchestraObjectListResponse orchestraObjectListResponse = orchestraRestClient.get("BCMDReference","Account","root/Address/3000025/OperatingUnit",null);
		if(orchestraObjectListResponse.getContent()!=null){
			List contentList = (List)orchestraObjectListResponse.getContent();
			for(Object content: contentList){
				if(content!=null && ((Map)content).get("content")!=null) {
					addressOuSet.add(((Map)content).get("content").toString());
				}
			}
		}*/

		Client client = ClientBuilder.newClient();
//		WebTarget target = client.target("http://localhost:8080/mdmcustomapp/BCMDReference/Account/checkIfOuExists");
		WebTarget target = client.target("http://localhost:8080/mdmcustomapp/checkIfExists/country/BReference/Account/US");
		Map<String,String> parameters = new HashMap<>();
//		parameters.put("mdmAddressId","3000025");
//		parameters.put("operatingUnit","US-OU-8213");
		if (parameters != null)
			for (Map.Entry<String, String> entry : parameters.entrySet())
				target = target.queryParam(entry.getKey(), entry.getValue());
		Invocation.Builder request = target.request(MediaType.APPLICATION_JSON);
		Response response = request.get();
		System.out.println(response.readEntity(String.class));
	}

	@Test
	public void testEnc() throws IOException {
//		DatabaseProperties databaseProperties = (DatabaseProperties)SpringContext.getApplicationContext().getBean("databaseProperties");
//		System.out.println(databaseProperties.getEbx().getPassword());
		JitterbitRestClient client = (JitterbitRestClient)SpringContext.getApplicationContext().getBean("databaseProperties");
		client.publishBulk(null,null,null);
	}

	@Test
	public void testRest() throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HHmm");
		String id = LocalTime.now().format(dtf);
		String objectName = "ACCOUNT";
		StringBuilder sb = new StringBuilder("{\"rows\":[");
		sb.append("]}");
		java.nio.file.Path file = java.nio.file.Paths.get(System.getProperty("ebx.home"),"mdm_"+objectName+"_"+ id +".json");
		try { file = Files.createFile(file); } catch(FileAlreadyExistsException ignored){}

		RandomAccessFile stream = new RandomAccessFile(file.toFile(), "rw");
		FileChannel channel = stream.getChannel();

		FileLock lock = null;
		try {
			lock = channel.tryLock();
		} catch (final OverlappingFileLockException e) {
			stream.close();
			channel.close();
		}
		String value = "{\"rows\":[";
		byte[] strBytes = value.getBytes();
		ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);
		buffer.put(strBytes);
		buffer.flip();
		channel.write(buffer);

		List<OrchestraObject> objs = new ArrayList<>();
		OrchestraObject obj = new OrchestraObject();
		Map<String,OrchestraContent> content = new HashMap<>();
		content.put("name",new OrchestraContent("ashish"));
		obj.setContent(content);
		objs.add(obj);
		objs.add(obj);
		System.out.println(mapper.writeValueAsString(objs));

		System.out.println(mapper.writeValueAsString(objs).substring(1,mapper.writeValueAsString(objs).length()-1));
		strBytes = mapper.writeValueAsString(objs).substring(1,mapper.writeValueAsString(objs).length()-1).getBytes();
		buffer = ByteBuffer.allocate(strBytes.length);
		buffer.put(strBytes);
		buffer.flip();
		channel.write(buffer);

		strBytes = "]}".getBytes();
		buffer = ByteBuffer.allocate(strBytes.length);
		buffer.put(strBytes);
		buffer.flip();
		channel.write(buffer);

		lock.release();
		stream.close();
		channel.close();

		/*Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8039/mdmcustomapp/testrest");
		Invocation.Builder request = target.request(MediaType.APPLICATION_JSON);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		JsonFactory jfactory = new JsonFactory();
		JsonGenerator jGenerator = jfactory.createGenerator(stream, JsonEncoding.UTF8);
		jGenerator.writeStartObject();
		jGenerator.writeStringField("content", "Tom");
		jGenerator.writeEndObject();
		jGenerator.close();
		Response response = request.post(Entity.json(new String(stream.toByteArray(), "UTF-8")));
		System.out.println(response.getStatus());*/
	}

	@Test
	public void testWs(){
		ObjectFactory factory = new ObjectFactory();
		CreateVersionRequestType requestType = factory.createCreateVersionRequestType();
		requestType.setBranch("Reference");
		Object request = factory.createCreateVersion(requestType);
		SOAPConnector soapConnector = (SOAPConnector) SpringContext.getApplicationContext().getBean("soapConnector");
		WebServiceTemplate template = soapConnector.getWebServiceTemplate();
		JAXBElement<CreateVersionResponseType> response = (JAXBElement<CreateVersionResponseType>)template.marshalSendAndReceive(request,message->{
			SOAPMessage soapMessage = ((SaajSoapMessage)message).getSaajMessage();
			SOAPHeader header = null;
			try {
				header = soapMessage.getSOAPHeader();
				SOAPHeaderElement security = header.addHeaderElement(new QName("http://schemas.xmlsoap.org/ws/2002/04/secext", "Security", "sec"));
				SOAPElement usernameToken = security.addChildElement("UsernameToken", "sec");
				SOAPElement username = usernameToken.addChildElement("Username", "sec");
				SOAPElement password = usernameToken.addChildElement("Password", "sec");

				username.setTextContent("admin");
				password.setTextContent("Serene*123");
			} catch (SOAPException e) {
				e.printStackTrace();
			}
		});
		//JAXBElement<CreateVersionResponseType> response = (JAXBElement<CreateVersionResponseType>)soapConnector.callWebService("http://localhost:8080/ebx-dataservices/connector",request);
		System.out.println("response status = "+response.getValue().getStatus());
		String name = response.getValue().getVersionName();
		Session session = Repository.getDefault().createSessionFromLoginPassword("admin", "Serene*123");
		AdaptationHome dataSpace = Repository.getDefault().lookupHome(BranchKey.parse("Reference"));
		ProgrammaticService svc = ProgrammaticService.createForSession(session, dataSpace);
		Procedure proc = new Procedure()
		{
			public void execute(ProcedureContext pContext) throws Exception
			{
				ArchiveExportSpec spec = new ArchiveExportSpec();
				spec.addInstance(AdaptationName.forName("Account"),true);
				/*ValueContextForUpdate vc = pContext.getContext(userRecord.getAdaptationName());
				pContext.setAllPrivileges(true);
				for(com.orchestranetworks.schema.Path param : updates.keySet())
					vc.setValue(updates.get(param), param);
				pContext.doModifyContent(userRecord, vc);
				pContext.setAllPrivileges(false);*/
			}
		};
		ProcedureResult res = svc.execute(proc);
		res.hasFailed();
	}

}
