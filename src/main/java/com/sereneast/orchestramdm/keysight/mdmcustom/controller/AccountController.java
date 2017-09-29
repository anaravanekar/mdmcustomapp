package com.sereneast.orchestramdm.keysight.mdmcustom.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.ApplicationProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.pagination.DataTableRequest;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.pagination.DataTableResults;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.pagination.PaginationCriteria;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.AppUtil;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ResultSetToHashMapRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AccountController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private NamedParameterJdbcTemplate oracleDbNamedParameterJdbcTemplate;

	// inject via application.properties
	@Value("${welcome.message:test}")
	private String message = "Hello World";

	private static final String COLUMN_LIST_ACCOUNT ="MDMACCOUNTID,ACCOUNTNUMBER,ACCOUNTNAME,DAQAMETADATA_STATE,DAQAMETADATA_CLUSTERID,DAQAMETADATA_SCORE,DAQAMETADATA_TARGETRECORD_,STATUS,SYSTEMID,SYSTEMNAME,CUSTOMERTYPE,REGISTRYID,ALIAS,NAMEPRONUNCIATION,TAXPAYERID,TAXREGISTRATIONNUMBER,CUSTOMERCATEGORY,ACCOUNTDESCRIPTION,REGION,CLASSIFICATION,ACCOUNTTYPE,SALESCHANNEL,REFERENCE,NLSLANGUAGECODE,NAMELOCALLANGUAGE,CUSTOMERSCREENING,LASTCREDITREVIEWDATE,NEXTCREDITREVIEWDATE,CREDITREVIEWCYCLE,PARENTPARTY,EMGLASTTRANS,GROUPINGDESCRIPTION,GROUPINGID,IXIACLASSIFICATION,PROFILECLASS";

	private static final String COLUMN_LIST_ADDRESS = "SITENUMBER as \"Site Number\",ADDRESSLINE1 as \"Address Line 1\",ADDRESSLINE2 as \"Address Line 2\",ADDRESSLINE3 as \"Address Line 3\",ADDRESSLINE4 as \"Address Line 4\",ADDRESS as \"Address\",CITY as \"City\",COUNTY as \"County\",ADDRESSSTATE as \"Address State\",PROVINCE as \"Province\",POSTALCODE as \"Postal Code\",CALCPOSTALCODE as \"Calculated Postal Code\",COUNTRY as \"Country\",SITEID as \"Site Id\",SITENAME as \"Site Name\",MDMADDRESSID as \"MDM Address Id\",MDMACCOUNTID_ as \"MDM Account Id\",DAQAMETADATA_STATE as \"State\",DAQAMETADATA_CLUSTERID as \"Cluster\",DAQAMETADATA_SCORE as Score,SYSTEMID as \"System Id\",SYSTEMNAME as \"System Name\",KEYSIGHTSFAADDRESSID as \"Keysight SFA Address Id\",ADDRESSEE as \"Addressee\",STATUS as \"Status\",IDENTIFYINGADDRESS as \"Identifying Address\",REFERENCE as \"Reference\",TRANSLATION as \"Translation\",OPERATINGUNIT as \"Operating Unit\",RPLCHECK as \"RPL Check\",NLSLANGUAGE as \"NLS Language\",ADDRESSLINE1LOCALLANGUAGE as \"Address Line1: Local Language\",ADDRESSLINE2LOCALLANGUAGE as \"Address Line2: Local Language\",ADDRESSLINE3LOCALLANGUAGE as \"Address Line3: Local Language\",ADDRESSLINE4LOCALLANGUAGE as \"Address Line4: Local Language\",CITYLOCALLANGUAGE as \"City:Local Language\",STATELOCALLANGUAGE as \"State:Local Language\",POSTALLOCALLANGUAGE as \"Postal:Local Language\",PROVINCELOCALLANGUAGE as \"Province:Local Language\",COUNTYLOCALLANGUAGE as \"County: Local Language\",COUNTRYLOCALLANGUAGE as \"Country: Local Language\",SENDACKNOWLEDGEMENT as \"Send Acknowledgement\",INVOICECOPIES as \"Invoice Copies or Suppression\",ACCOUNTSITETYPE as \"Account Site Type\",CONTEXTVALUE as \"Context Value\",TAXABLEPERSON as \"Taxable Person\",INDUSTRYCLASSIFICATION as \"Industry Classification\",TAXCERTIFICATEDATE as \"Tax Certificate Issue Date\",BUSINESSNUMBER as \"Business Number\",INDUSTRYSUBCLASSIFICATION as \"Industry Subclassification\",ADDRESSSITECATEGORY as \"Address Site Category\",ATS as \"ATS\",BUSINESSPURPOSE as \"Business Purpose\",LOCATION as \"Location\",BILLTOLOCATION as \"Bill To Location\",PRIMARYPURPOSE as \"Primary Purpose\",REVENUERECOGNITION as \"Revenue Recognition\",ORGSEGMENT as \"Org Segment\",SUBSEGMENT as \"Sub Segment\",ACCOUNTCLASS as \"Account Class\",RECEIVABLE as \"Receivable\",REVENUE as \"Revenue\",PAYMENTTERMS as \"Payment Terms\",SALESPERSON as \"Sales Person\",ORDERTYPE as \"Order Type\",PRICELIST as \"Price List\",DEMANDCLASS as \"Demand Class\",RECEIPTMETHODS as \"Receipt Methods\",PRIMARYPAYMENT as \"Primary Payment\",STARTDATE as \"Start Date\",ENDDATE as \"End Date\",DEFAULTREPORTINGCOUNTRYNAME as \"Default Reporting Country\",DEFAULTREPORSTRATIONNUMBER as \"Default Reporting Number\",TAXREGISTRATIONACTIVE as \"Tax Registration Active\",REGIMECODE as \"Regime Code\",TAX as \"Tax\",TAXJURISDICTIONCODE as \"Tax Jurisdiction Code\",TAXREGISTRATIONNUMBER as \"Tax Registration Number\",SOURCE as \"Source\",DEFAULTTAXREGISTRATION as \"Default Tax Registration\",EFECTIVEFROM as \"Efective From\",EFFECTIVETO as \"Effective To\",ROUNDINGRULE as \"Rounding Rule\"";

	@RequestMapping("/")
	public String index(Map<String, Object> model) {
		return "index";
	}

	@RequestMapping("/homePage")
	public String welcome(Map<String, Object> model) {
/*		String message = this.message;
		model.put("message", message);*/
		try{
//			String query = "select * from ( select * from EBX_MDM_ACCOUNT order by ACCOUNTNAME) where ROWNUM <= 1000";
			/*String query = "select * from ("
					+" SELECT ACCOUNTNAME, ACCOUNTNUMBER, MDMACCOUNTID, DAQAMETADATA_STATE, DAQAMETADATA_CLUSTERID, DAQAMETADATA_SCORE, T_LAST_USER_ID, T_CREATOR_ID, T_CREATION_DATE, T_LAST_WRITE, DAQAMETADATAEFIRSTMATCHING, DAQAMETADATASECONDMATCHING, DAQAMETADATAEMATCHINGSCORE, DAQAMETADATA_TARGETRECORD_, DAQAMETADATA_MERGEORIGIN, DAQAMETADATA_TIMESTAMP, DAQAMETADATA_WASGOLDEN, DAQAMETADATA_AUTOCREATED, DAQAMETADATAFIELDSMATCHING, DAQAMETADATAATONCE_GROUPED, DAQAMETADATACE_CLUSTERSIZE, DAQAMETADATA_WORKFLOW_UUID, DAQAMETADATA_WORKFLOW_NAME, DAQAMETADATAFLOW_TIMESTAMP, DAQAMETADATA_WORKFLOW_USER, DAQAMETADATACESSPOLICYCODE, DAQAMETADATAHINGPOLICYCODE, DAQAMETADATASHIPPOLICYCODE, DAQAMETADATA_OPERATIONCODE, DAQAMETADATATOPERATIONCODE, DAQAMETADATAHOPERATIONCODE, DAQAMETADATACLEANSINGSTATE, DAQAMETADATAGPROCEDURECODE, DAQAMETADATANSINGOPERATION, DAQAMETADATA_EXECUTIONDATE, DAQAMETADATAGMETADATA_USER, DAQAMETADATAMETADATA_FIELD, DAQAMETADATA_QUALITYDEFECT, STATUS, SYSTEMID, SYSTEMNAME, CUSTOMERTYPE, REGISTRYID, ALIAS, NAMEPRONUNCIATION, TAXPAYERID, TAXREGISTRATIONNUMBER, CUSTOMERCATEGORY, ACCOUNTDESCRIPTION, REGION, CLASSIFICATION, ACCOUNTTYPE, SALESCHANNEL, REFERENCE, NLSLANGUAGECODE, NAMELOCALLANGUAGE, CUSTOMERSCREENING, LASTCREDITREVIEWDATE, NEXTCREDITREVIEWDATE, CREDITREVIEWCYCLE, PARENTPARTY, EMGLASTTRANS, GROUPINGDESCRIPTION, GROUPINGID, IXIACLASSIFICATION, PROFILECLASS "
					+" from EBX_MDM_ACCOUNT order by ACCOUNTNAME) where ROWNUM <= 100";
			List<Map<String, Object>> resultList = oracleDbNamedParameterJdbcTemplate.query(query,new ResultSetToHashMapRowMapper());
			message = message + " No of accounts: " + resultList.size();
			model.put("message", message);
			model.put("columnlist", new ArrayList<String>(resultList.get(0).keySet()));
			model.put("resultList",resultList);*/
		}catch (Exception e){
			e.printStackTrace();
			LOGGER.error("",e);
		}
		return "homePage";
	}

	@RequestMapping("/account/{accId}/address")
	public String welcome(@PathVariable("accId") String accId, Map<String, Object> model) {
		String query = "SELECT "+ COLUMN_LIST_ADDRESS +" FROM EBX_MDM_ACCOUNT_ADDRESS WHERE MDMACCOUNTID_ = "+accId;
		LOGGER.debug("Address query: "+query);
		List<Map<String, Object>> resultList = oracleDbNamedParameterJdbcTemplate.query(query,new ResultSetToHashMapRowMapper());
		message = message + " No of addresses: " + resultList.size();
		model.put("message", message);
		model.put("columnlist", new ArrayList<String>(resultList.get(0).keySet()));
		model.put("resultList",resultList);
		return "accountAddress :: contents";
	}

	@RequestMapping("/accountColumns")
	public String accoutColumns(Map<String, Object> model){
		String query = "select * from ("
				+" SELECT "+ COLUMN_LIST_ACCOUNT
				+" from EBX_MDM_ACCOUNT order by ACCOUNTNAME) where ROWNUM <= 1";
		List<Map<String, Object>> resultList = oracleDbNamedParameterJdbcTemplate.query(query,new ResultSetToHashMapRowMapper());
		model.put("columnlist", new ArrayList<String>(resultList.get(0).keySet()));
		return "columnsFragment :: contents";
	}

	@RequestMapping(path="/accounts")
	@ResponseBody
	public String getAllEmployees(HttpServletRequest request, HttpServletResponse response, Model model){
		String baseQuery = "select "+ COLUMN_LIST_ACCOUNT
				+" from ( select rownum rnum, a.* from ("
				+" SELECT "+ COLUMN_LIST_ACCOUNT
				+" from EBX_MDM_ACCOUNT #WHERE_CLAUSE#  #ORDER_CLASUE# "
				+") a where rownum < #LIMIT#  ) where rnum >= #START#";
		List<Map<String, Object>> resultList = null;
		String result = "";
		DataTableResults<Map<String, Object>> dataTableResult = new DataTableResults<Map<String,Object>>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		try{
			DataTableRequest<Object> dataTableInRQ = new DataTableRequest<Object>(request);
			PaginationCriteria pagination = dataTableInRQ.getPaginationRequest();
			LOGGER.debug(pagination.toString());
			LOGGER.debug(dataTableInRQ.toString());
			String paginatedQuery = AppUtil.buildPaginatedQueryOracle(baseQuery, pagination);
			LOGGER.debug(paginatedQuery);

			resultList = oracleDbNamedParameterJdbcTemplate.query(paginatedQuery,new ResultSetToHashMapRowMapper());
			SqlParameterSource namedParameters = new MapSqlParameterSource("dummy", 1);
			Integer totalRecords = oracleDbNamedParameterJdbcTemplate.queryForObject("SELECT COUNT(*) FROM EBX_MDM_ACCOUNT WHERE 1=:dummy",namedParameters,Integer.class);
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("data",resultList);

			result = mapper.writeValueAsString(resultList);

			dataTableResult.setDraw(dataTableInRQ.getDraw());
			dataTableResult.setData(resultList);
			if (!AppUtil.isObjectEmpty(resultList)) {
				dataTableResult.setRecordsTotal(String.valueOf(totalRecords));
				if (dataTableInRQ.getPaginationRequest().isFilterByEmpty()) {
					dataTableResult.setRecordsFiltered(String.valueOf(totalRecords));
				} else {
					dataTableResult.setRecordsFiltered(String.valueOf(totalRecords));
				}
			}
			LOGGER.debug(new Gson().toJson(dataTableResult));
			result = mapper.writeValueAsString(dataTableResult);
			LOGGER.debug(result);
		}catch (Exception e){
			e.printStackTrace();
			LOGGER.error("",e);
		}
		return result;
	}

}