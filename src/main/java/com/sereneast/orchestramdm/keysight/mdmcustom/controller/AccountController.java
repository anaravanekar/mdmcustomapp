package com.sereneast.orchestramdm.keysight.mdmcustom.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.pagination.DataTableRequest;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.pagination.DataTableResults;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.pagination.PaginationCriteria;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.AppUtil;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ResultSetToHashMapRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	private NamedParameterJdbcTemplate ebxDbNamedParameterJdbcTemplate;

	//	private static final String COLUMN_LIST_ACCOUNT ="MDMACCOUNTID,ACCOUNTNUMBER,ACCOUNTNAME,DAQAMETADATA_STATE,DAQAMETADATA_CLUSTERID,DAQAMETADATA_SCORE,DAQAMETADATA_TARGETRECORD_,STATUS,SYSTEMID,SYSTEMNAME,CUSTOMERTYPE,REGISTRYID,ALIAS,NAMEPRONUNCIATION,TAXPAYERID,TAXREGISTRATIONNUMBER,CUSTOMERCATEGORY,ACCOUNTDESCRIPTION,REGION,CLASSIFICATION,ACCOUNTTYPE,SALESCHANNEL,REFERENCE,NLSLANGUAGECODE,NAMELOCALLANGUAGE,CUSTOMERSCREENING,LASTCREDITREVIEWDATE,NEXTCREDITREVIEWDATE,CREDITREVIEWCYCLE,PARENTPARTY,EMGLASTTRANS,GROUPINGDESCRIPTION,GROUPINGID,IXIACLASSIFICATION,PROFILECLASS";
	private static final String COLUMN_LIST_ACCOUNT ="MDMACCOUNTID,INTERNALACCOUNTID,ACCOUNTNAME,DAQAMETADATA_STATE,DAQAMETADATA_CLUSTERID,DAQAMETADATA_SCORE,DAQAMETADATA_TARGETRECORD_,STATUS,SYSTEMID,SYSTEMNAME,CUSTOMERTYPE,REGISTRYID,NAMEPRONUNCIATION,TAXPAYERID,TAXREGISTRATIONNUMBER,CUSTOMERCATEGORY,REGION,CLASSIFICATION,ACCOUNTTYPE,SALESCHANNEL,REFERENCE,NLSLANGUAGECODE,NAMELOCALLANGUAGE,CUSTOMERSCREENING,PARENTPARTY,EMGLASTTRANS,GROUPINGDESCRIPTION,GROUPINGID,PROFILECLASS,ASSIGNEDTO,ISGCLASSIFICATION,NOTES,PAYMENTENDDATE,PAYMENTRECEIPTMETHOD,PAYMENTSTARTDATE,PRIMARYPAYMENT,PUBLISHED";
	private static final String COLUMN_LIST_ADDRESS = "ADDRESSLINE1 as \"Address Line 1\",ADDRESSLINE2 as \"Address Line 2\",ADDRESSLINE3 as \"Address Line 3\",ADDRESSLINE4 as \"Address Line 4\",ADDRESS as \"Address\",CITY as \"City\",COUNTY as \"County\",ADDRESSSTATE as \"Address State\",PROVINCE as \"Province\",POSTALCODE as \"Postal Code\",COUNTRY as \"Country\",MDMADDRESSID as \"MDM Address Id\",MDMACCOUNTID_ as \"MDM Account Id\",DAQAMETADATA_STATE as \"DAQAMETADATA_STATE\",DAQAMETADATA_CLUSTERID as \"DAQAMETADATA_CLUSTERID\",DAQAMETADATA_SCORE as \"DAQAMETADATA_SCORE\",SYSTEMID as \"System Id\",SYSTEMNAME as \"System Name\",STATUS as \"Status\",IDENTIFYINGADDRESS as \"Identifying Address\",REFERENCE as \"Reference\",OPERATINGUNIT as \"Operating Unit\",RPLCHECK as \"RPL Check\",NLSLANGUAGE as \"NLS Language\",ADDRESSLINE1LOCALLANGUAGE as \"Address Line1: Local Language\",ADDRESSLINE2LOCALLANGUAGE as \"Address Line2: Local Language\",ADDRESSLINE3LOCALLANGUAGE as \"Address Line3: Local Language\",ADDRESSLINE4LOCALLANGUAGE as \"Address Line4: Local Language\",CITYLOCALLANGUAGE as \"City:Local Language\",STATELOCALLANGUAGE as \"State:Local Language\",POSTALLOCALLANGUAGE as \"Postal:Local Language\",PROVINCELOCALLANGUAGE as \"Province:Local Language\",COUNTYLOCALLANGUAGE as \"County: Local Language\",COUNTRYLOCALLANGUAGE as \"Country: Local Language\",SENDACKNOWLEDGEMENT as \"Send Acknowledgement\",INVOICECOPIES as \"Invoice Copies or Suppression\",CONTEXTVALUE as \"Context Value\",TAXABLEPERSON as \"Taxable Person\",INDUSTRYCLASSIFICATION as \"Industry Classification\",TAXCERTIFICATEDATE as \"Tax Certificate Issue Date\",BUSINESSNUMBER as \"Business Number\",INDUSTRYSUBCLASSIFICATION as \"Industry Subclassification\",ADDRESSSITECATEGORY as \"Address Site Category\",ATS as \"ATS\",BILLTOLOCATION as \"Bill To Location\",REVENUERECOGNITION as \"Revenue Recognition\",ORGSEGMENT as \"Org Segment\",DEMANDCLASS as \"Demand Class\",TAXREGISTRATIONACTIVE as \"Tax Registration Active\",TAX as \"Tax\",TAXREGISTRATIONNUMBER as \"Tax Registration Number\",SOURCE as \"Source\",DEFAULTTAXREGISTRATION as \"Default Tax Registration\",ASSIGNEDTO as \"Assigned To\",BATCHCODE as \"Batch Code\",INTERNALACCOUNTID as \"Internal Account Id\",INTERNALADDRESSID as \"Internal Address Id\",PARENTSYSTEMID as \"Parent System Id\",PUBLISHED as \"Published\",SPECIALHANDLING as \"Special Handling\",SUBSEGMET as \"Sub Segment\",TAXEFFECTIVEFROM as \"Tax Effective From\",TAXEFFECTIVETO as \"Tax Effective To\",TAXREGIMECODE as \"Tax Regime Code\"";
	private static final String COLUMN_LIST_ADDRESS_NEW = "COUNTRY as \"Country\",'' as \"Business Purpose\",OPERATINGUNIT as \"Operating Unit \",SYSTEMNAME as \"Source System\",ADDRESS as \"Address\",CITY as \"City\",POSTALCODE as \"Postal Code\",PROVINCE as \"Province\",REFERENCE as \"Reference\",ADDRESSLINE1 as \"Address Line 1\",ADDRESSLINE2 as \"Address Line 2\",ADDRESSLINE3 as \"Address Line 3\",ADDRESSLINE4 as \"Address Line 4\", MDMADDRESSID as \"MDM Address Id\"";
	private static final String COLUMN_LIST_ADDRESS_CL = "ac.DAQAMETADATA_CLUSTERID as \"Cluster\",'' as \"Business Purpose\",ac.MDMACCOUNTID as \"MDM Account Id\", ac.INTERNALACCOUNTID as \"Internal Id\",ac.ACCOUNTNAME as \"Account Name\", ad.COUNTRY as \"Country\",ad.OPERATINGUNIT as \"Operating Unit \", ad.SYSTEMNAME as \"Source System\",ad.ADDRESS as \"Address\",ad.CITY as \"City\",ad.POSTALCODE as \"Postal Code\",ad.COUNTY as \"County\",ad.PROVINCE as \"Province\",ad.REFERENCE as \"Reference\",ad.ADDRESSLINE1 as \"Address Line 1\",ad.ADDRESSLINE2 as \"Address Line 2\",ad.ADDRESSLINE3 as \"Address Line 3\",ad.ADDRESSLINE4 as \"Address Line 4\", ad.MDMADDRESSID as \"MDM Address Id\"";
	private static final String BP_COLUMN_LIST = "bp.PRIMARY as \"Primary\", BUSINESSPURPOSE as \"Business Purpose\",STATUS as \"Status\",LOCATION as \"Location\", SYSTEMID as \"System Id\", SYSTEMNAME as \"System Name\"";

	@RequestMapping("/")
	public String index(Map<String, Object> model) {
		return "index";
	}

	@RequestMapping("/homePage")
	public String welcome(Map<String, Object> model) {
		return "homePage";
	}

	@RequestMapping("/homePageNew")
	public String homePageNew(Map<String, Object> model) {
		return "homePageNew";
	}

	@RequestMapping("/account/{accId}/address")
	public String getAccountAddresses(@PathVariable("accId") String accId, Map<String, Object> model) {
		String query = "SELECT "+ COLUMN_LIST_ADDRESS +" FROM EBX_MDM_ACCOUNT_ADDRESS WHERE MDMACCOUNTID_ = "+accId;
		LOGGER.debug("Address query: "+query);
		List<Map<String, Object>> resultList = ebxDbNamedParameterJdbcTemplate.query(query,new ResultSetToHashMapRowMapper());
		model.put("columnlist", new ArrayList<String>(resultList.get(0).keySet()));
		model.put("resultList",resultList);
		return "accountAddress :: contents";
	}

	@RequestMapping("/account/{accId}/addressnew")
	public String getAccountAddressesNew(@PathVariable("accId") String accId, Map<String, Object> model) {
		String query = "SELECT "+ COLUMN_LIST_ADDRESS_NEW +" FROM EBX_MDM_ACCOUNT_ADDRESS WHERE MDMACCOUNTID_ = "+accId;
		LOGGER.debug("Address query: "+query);
		List<Map<String, Object>> resultList = ebxDbNamedParameterJdbcTemplate.query(query,new ResultSetToHashMapRowMapper());
		List<String> columnList = resultList!=null && !resultList.isEmpty()?new ArrayList<String>(resultList.get(0).keySet()):new ArrayList<>();
		model.put("columnlist", columnList);
		model.put("resultList",resultList);
		model.put("accId",accId);
		return "accountAddress :: contents";
	}

	@RequestMapping("/address/{addressId}/businessPurpose")
	public String getAddressesBusinessPurpose(@PathVariable("addressId") String addressId, Map<String, Object> model) {
		String query = "select "+BP_COLUMN_LIST+" FROM EBX_MDM_ACCOUNT_ADDRESS_BP bp WHERE MDMADDRESSID_= "+addressId;
		LOGGER.debug("Business Purpose query: "+query);
		List<Map<String, Object>> resultList = ebxDbNamedParameterJdbcTemplate.query(query,new ResultSetToHashMapRowMapper());
		List<String> columnList = resultList!=null && !resultList.isEmpty()?new ArrayList<String>(resultList.get(0).keySet()):new ArrayList<>();
		model.put("columnlist", columnList);
		model.put("resultList",resultList);
		model.put("addressId",addressId);
		return "businessPurpose :: contents";
	}

	@RequestMapping("/clusterView")
	public String getClusterView(HttpServletRequest request, Map<String, Object> model) {
		String clusterId = request.getParameter("clusterId");
		model.put("clusterId",clusterId);
		return "clusterView";
	}

	@RequestMapping("/account/{clusterId}/claddress")
	public String getClusterAddresses(@PathVariable("clusterId") String clusterId, Map<String, Object> model) {

		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		List columnList = new ArrayList<String>();
		if(clusterId!=null && !"".equals(clusterId)) {
			String query = "SELECT " + COLUMN_LIST_ADDRESS_CL + " FROM EBX_MDM_ACCOUNT_ADDRESS ad INNER JOIN EBX_MDM_ACCOUNT ac ON ad.MDMACCOUNTID_ = ac.MDMACCOUNTID WHERE ac.DAQAMETADATA_CLUSTERID = " + clusterId;
			resultList = ebxDbNamedParameterJdbcTemplate.query(query, new ResultSetToHashMapRowMapper());
			if(resultList!=null && !resultList.isEmpty()) {
				LOGGER.debug("Address query: " + query);
				columnList = new ArrayList<String>(resultList.get(0).keySet());
			}
		}
		model.put("columnlist", columnList);
		model.put("resultList", resultList);
		return "clusterAddress :: contents";
	}

	@RequestMapping("/accountColumns")
	public String accoutColumns(Map<String, Object> model){
		String query = "select * from ("
				+" SELECT "+ COLUMN_LIST_ACCOUNT
				+" from EBX_MDM_ACCOUNT order by ACCOUNTNAME) where ROWNUM <= 1";
		List<Map<String, Object>> resultList = ebxDbNamedParameterJdbcTemplate.query(query,new ResultSetToHashMapRowMapper());
		model.put("columnlist", new ArrayList<String>(resultList.get(0).keySet()));
		return "columnsFragment :: contents";
	}

	@RequestMapping(path="/accounts")
	@ResponseBody
	public String getAllAccounts(HttpServletRequest request, HttpServletResponse response, Model model){
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

			resultList = ebxDbNamedParameterJdbcTemplate.query(paginatedQuery,new ResultSetToHashMapRowMapper());
			SqlParameterSource namedParameters = new MapSqlParameterSource("dummy", 1);
			Integer totalRecords = ebxDbNamedParameterJdbcTemplate.queryForObject("SELECT COUNT(*) FROM EBX_MDM_ACCOUNT WHERE 1=:dummy",namedParameters,Integer.class);
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