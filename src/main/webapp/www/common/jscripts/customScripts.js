/*function updateRelatedOptions(countryCode, currentStateValue, currentProvinceValue) {
    if (!countryCode) {
        return;
    }
    var urlForOptions = "'"+mdmRestProtocol+'://'+mdmRestHost+':'+mdmRestPort+'/mdmcustomapp/selectOptions/BReference/';
    var statePrefixedPath = addressPrefixedPaths.AddressState;
    var provincePrefixedPath = addressPrefixedPaths.Province;

    var stateSelect = document.getElementById("customStateSelect");
    var provinceSelect = document.getElementById("customProvinceSelect");
    var blankOption = new Option("[not defined]", "");
    if (territoryTypeMap[countryCode] === "STATE") {
        urlForOptions = urlForOptions + "state/" + countryCode;
        toggeleStandardFields(false);
    } else if (territoryTypeMap[countryCode] === "PROVINCE") {
        urlForOptions = urlForOptions + "province/" + countryCode;
        toggeleStandardFields(false);
    } else {
        while (provinceSelect.options.length > 1) {
            provinceSelect.remove(1);
        }
        while (stateSelect.options.length > 1) {
            stateSelect.remove(1);
        }
        ebx_form_setValue(statePrefixedPath, null);
        ebx_form_setValue(provincePrefixedPath, null);
        toggeleStandardFields(true);
        try {
            ebx_form_setValue(statePrefixedPath, currentStateValue);
            ebx_form_setValue(provincePrefixedPath, currentProvinceValue);
        } catch (err) {}
        return;
    }
    var xhr = new XMLHttpRequest();
    xhr.open("GET", urlForOptions);
    xhr.onload = function() {
        if (xhr.status === 200) {
            var obj = JSON.parse(xhr.responseText);
            var selectOptions = obj.options;
            if (territoryTypeMap[countryCode] === "STATE") {
                while (provinceSelect.options.length > 1) {
                    provinceSelect.remove(1);
                }
                while (stateSelect.options.length > 1) {
                    stateSelect.remove(1);
                }
                if (selectOptions) {
                    var i;
                    for (i = 0; i < selectOptions.length; i++) {
                        var newOption = new Option(selectOptions[i].Option, selectOptions[i].OptionValue);
                        stateSelect.options.add(newOption);
                    }
                }
                ebx_form_setValue(statePrefixedPath, null);
                ebx_form_setValue(provincePrefixedPath, null);
                if (currentStateValue) {
                    try {
                        document.getElementById('customStateSelect').value = currentStateValue;
                        ebx_form_setValue(statePrefixedPath, currentStateValue);
                    } catch (err) {}
                }
            } else if (territoryTypeMap[countryCode] === "PROVINCE") {
                while (provinceSelect.options.length > 1) {
                    provinceSelect.remove(1);
                }
                while (stateSelect.options.length > 1) {
                    stateSelect.remove(1);
                }
                if (selectOptions) {
                    var i;
                    for (i = 0; i < selectOptions.length; i++) {
                        var newOption = new Option(selectOptions[i].Option, selectOptions[i].OptionValue);
                        provinceSelect.options.add(newOption);
                    }
                }
                ebx_form_setValue(statePrefixedPath, null);
                ebx_form_setValue(provincePrefixedPath, null);
                if (currentProvinceValue) {
                    try {
                        document.getElementById('customProvinceSelect').value = currentProvinceValue;
                        ebx_form_setValue(provincePrefixedPath, currentProvinceValue);
                    } catch (err) {}
                }
            } else {
                while (provinceSelect.options.length > 1) {
                    provinceSelect.remove(1);
                }
                while (stateSelect.options.length > 1) {
                    stateSelect.remove(1);
                }
                ebx_form_setValue(statePrefixedPath, null);
                ebx_form_setValue(provincePrefixedPath, null);
            }
        } else {
            while (provinceSelect.options.length > 1) {
                provinceSelect.remove(1);
            }
            while (stateSelect.options.length > 1) {
                stateSelect.remove(1);
            }
            ebx_form_setValue(statePrefixedPath, null);
            ebx_form_setValue(provincePrefixedPath, null);
            try {
                ebx_form_setValue(statePrefixedPath, currentStateValue);
                ebx_form_setValue(provincePrefixedPath, currentProvinceValue);
            } catch (err) {}
        }
    };
    xhr.send();
}

function updateRelatedLocalOptions(countryCode, currentStateValue, currentProvinceValue) {
    if (!countryCode) {
        return;
    }
    var urlForOptions = "'"+mdmRestProtocol+'://'+mdmRestHost+':'+mdmRestPort+'/mdmcustomapp/selectOptions/BReference/';
    var statePrefixedPath = addressPrefixedPaths.StateLocalLanguage;
    var provincePrefixedPath = addressPrefixedPaths.ProvinceLocalLanguage;

    var stateSelect = document.getElementById("customStateLocalSelect");
    var provinceSelect = document.getElementById("customProvinceLocalSelect");
    var blankOption = new Option("[not defined]", "");
    if (territoryTypeMap[countryCode] === "STATE" && ("JP" === countryCode || "RU" === countryCode)) {
        urlForOptions = urlForOptions + "state/" + countryCode;
        toggleStandardFieldsLocal(false);
    } else if (territoryTypeMap[countryCode] === "PROVINCE" && ("CN" === countryCode || "KR" === countryCode)) {
        urlForOptions = urlForOptions + "province/" + countryCode;
        toggleStandardFieldsLocal(false);
    } else {
        while (provinceSelect.options.length > 1) {
            provinceSelect.remove(1);
        }
        while (stateSelect.options.length > 1) {
            stateSelect.remove(1);
        }
        ebx_form_setValue(statePrefixedPath, null);
        ebx_form_setValue(provincePrefixedPath, null);
        toggleStandardFieldsLocal(true);
        try {
            ebx_form_setValue(statePrefixedPath, currentStateValue);
            ebx_form_setValue(provincePrefixedPath, currentProvinceValue);
        } catch (err) {}
        return;
    }
    var xhr = new XMLHttpRequest();
    xhr.open("GET", urlForOptions);
    xhr.onload = function() {
        if (xhr.status === 200) {
            var obj = JSON.parse(xhr.responseText);
            var selectOptions = obj.options;
            if (territoryTypeMap[countryCode] === "STATE") {
                while (provinceSelect.options.length > 1) {
                    provinceSelect.remove(1);
                }
                while (stateSelect.options.length > 1) {
                    stateSelect.remove(1);
                }
                if (selectOptions) {
                    var i;
                    for (i = 0; i < selectOptions.length; i++) {
                        var newOption = new Option(selectOptions[i].Option, selectOptions[i].OptionValue);
                        stateSelect.options.add(newOption);
                    }
                }
                ebx_form_setValue(statePrefixedPath, null);
                ebx_form_setValue(provincePrefixedPath, null);
                if (currentStateValue) {
                    try {
                        document.getElementById('customStateLocalSelect').value = currentStateValue;
                        ebx_form_setValue(statePrefixedPath, currentStateValue);
                    } catch (err) {}
                }
            } else if (territoryTypeMap[countryCode] === "PROVINCE") {
                while (provinceSelect.options.length > 1) {
                    provinceSelect.remove(1);
                }
                while (stateSelect.options.length > 1) {
                    stateSelect.remove(1);
                }
                if (selectOptions) {
                    var i;
                    for (i = 0; i < selectOptions.length; i++) {
                        var newOption = new Option(selectOptions[i].Option, selectOptions[i].OptionValue);
                        provinceSelect.options.add(newOption);
                    }
                }
                ebx_form_setValue(statePrefixedPath, null);
                ebx_form_setValue(provincePrefixedPath, null);
                if (currentProvinceValue) {
                    try {
                        document.getElementById('customProvinceLocalSelect').value = currentProvinceValue;
                        ebx_form_setValue(provincePrefixedPath, currentProvinceValue);
                    } catch (err) {}
                }
            } else {
                while (provinceSelect.options.length > 1) {
                    provinceSelect.remove(1);
                }
                while (stateSelect.options.length > 1) {
                    stateSelect.remove(1);
                }
                ebx_form_setValue(statePrefixedPath, null);
                ebx_form_setValue(provincePrefixedPath, null);
            }
        } else {
            while (provinceSelect.options.length > 1) {
                provinceSelect.remove(1);
            }
            while (stateSelect.options.length > 1) {
                stateSelect.remove(1);
            }
            ebx_form_setValue(statePrefixedPath, null);
            ebx_form_setValue(provincePrefixedPath, null);
            try {
                ebx_form_setValue(statePrefixedPath, currentStateValue);
                ebx_form_setValue(provincePrefixedPath, currentProvinceValue);
            } catch (err) {}
        }
    };
    xhr.send();
}

function calculatedFields(countryCode) {
    updateRelatedOptions(countryCode, null, null);
    updateRelatedLocalOptions(countryCode, null, null);
    var stateValue = ebx_form_getValue(addressPrefixedPaths.AddressState);
    var xhr = new XMLHttpRequest();
    xhr.open('GET', "'"+mdmRestProtocol+'://'+mdmRestHost+':'+mdmRestPort+'/mdmcustomapp/calculatedFields/country/BReference/Account/' + countryCode);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function() {
        if (xhr.status === 200) {
            var calculatedFieldsJson = JSON.parse(xhr.responseText);
            if (calculatedFieldsJson && calculatedFieldsJson.hasOwnProperty('TaxRegimeCode')) {
                var valueTax = calculatedFieldsJson.TaxRegimeCode;
                ebx_form_setValue(addressPrefixedPaths.TaxRegimeCode, valueTax);
            } else {
                ebx_form_setValue(addressPrefixedPaths.TaxRegimeCode, null);
            }
        }
        var valueOne = {
            "key": "1",
            "label": "One copy"
        };
        var valueTwo = {
            "key": "2",
            "label": "Two copies"
        };
        var valueThree = {
            "key": "3",
            "label": "Three copies"
        };
        var valueFour = {
            "key": "4",
            "label": "Four copies"
        };
        var valueFive = {
            "key": "5",
            "label": "Five copies"
        };
        var sk = {
            "key": "N",
            "label": "N"
        };
        var sky = {
            "key": "Y",
            "label": "Y"
        };
        if ("US" === countryCode) {
            ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, valueTwo);
        } else {
            ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, valueOne);
        }
        if ("KR" === countryCode) {
            ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, valueThree);
        }
        var valueIndia = {
            "key": "0.3",
            "label": "Suppress because a Government Invoice number is required"
        };
        if ("IN" === countryCode) {
            ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, valueIndia);
        }
        var ic = {
            "key": "0.2",
            "label": "Suppress because of special format requirements"
        };
        if ("JP" === countryCode) {
            ebx_form_setValue(addressPrefixedPaths.SendAcknowledgement, sk);
            ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, ic);
        } else {
            ebx_form_setValue(addressPrefixedPaths.SendAcknowledgement, sky);
        }
        if ("PL" === countryCode) {
            ebx_form_setValue(addressPrefixedPaths.SendAcknowledgement, sk);
        }
        if ("GU" === countryCode || "VE" === countryCode || "PR" === countryCode) {
            ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, valueTwo);
        }
        if ("PL" === countryCode) {
            ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, valueThree);
        }
        if ("MX" === countryCode) {
            ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, valueFour);
        }
        if ("AR" === countryCode) {
            ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, valueFive);
        }
    };
    xhr.send();
}

function saveAssignment(dataSpace, newAssignment, table, primaryKey) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', "'"+mdmRestProtocol+'://'+mdmRestHost+':'+mdmRestPort+'/mdmcustomapp/' + table + '/updateAssignment/' + dataSpace + '/' + primaryKey + '/' + newAssignment.key);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function() {
        if (xhr.status === 200) {
            document.getElementById("divLoading").classList.remove("show");
        } else {
            document.getElementById("divLoading").classList.remove("show");
        }
    };
    xhr.send();
    document.getElementById("divLoading").classList.add("show");
}
*/
function toggeleStandardFields(displayStandard) {
    if (displayStandard) {
        document.getElementById('stateCustomDiv').style.display = 'none';
        document.getElementById('provinceCustomDiv').style.display = 'none';
        document.getElementById('stateStandardDiv').style.display = 'block';
        document.getElementById('provinceStandardDiv').style.display = 'block';
    } else {
        document.getElementById('stateCustomDiv').style.display = 'block';
        document.getElementById('provinceCustomDiv').style.display = 'block';
        document.getElementById('stateStandardDiv').style.display = 'none';
        document.getElementById('provinceStandardDiv').style.display = 'none';
    }
}

function toggleStandardFieldsLocal(displayStandard) {
    if (displayStandard) {
        document.getElementById('stateLocalCustomDiv').style.display = 'none';
        document.getElementById('provinceLocalCustomDiv').style.display = 'none';
        document.getElementById('stateLocalStandardDiv').style.display = 'block';
        document.getElementById('provinceLocalStandardDiv').style.display = 'block';
    } else {
        document.getElementById('stateLocalCustomDiv').style.display = 'block';
        document.getElementById('provinceLocalCustomDiv').style.display = 'block';
        document.getElementById('stateLocalStandardDiv').style.display = 'none';
        document.getElementById('provinceLocalStandardDiv').style.display = 'none';
    }
}

function toggleAdditionalInfo(contextValue) {
    var malaysia_fields = [addressPrefixedPaths.AddressSiteCategory, addressPrefixedPaths.ATS];
    var korea_fields = [addressPrefixedPaths.TaxablePerson, addressPrefixedPaths.TaxCertificateDate, addressPrefixedPaths.IndustryClassification, addressPrefixedPaths.IndustrySubclassification, addressPrefixedPaths.BusinessNumber];
    var brazil_fields = [addressPrefixedPaths.InscriptionType, addressPrefixedPaths.InsciptionNumber, addressPrefixedPaths.InscriptionBranch, addressPrefixedPaths.InscriptionDigit, addressPrefixedPaths.StateInscription];
    if (contextValue === "JA.KR.ARXCUDCI.VAT") {
        clearInfo(malaysia_fields);
        clearInfo(brazil_fields);
        hideInfo("malaysia_info");
        hideInfo("brazil_info");
        showInfo("korea_info");
    } else if (contextValue === "XXAT.MYM.CUST.SITE.INFO") {
        clearInfo(korea_fields);
        clearInfo(brazil_fields);
        hideInfo("korea_info");
        hideInfo("brazil_info");
        showInfo("malaysia_info");
    } else if (contextValue === "JL.BR.ARXCUDCI.Additional") {
        clearInfo(korea_fields);
        clearInfo(malaysia_fields);
        hideInfo("korea_info");
        hideInfo("malaysia_info");
        showInfo("brazil_info");
    } else {
        clearInfo(korea_fields);
        clearInfo(malaysia_fields);
        clearInfo(brazil_fields);
        hideInfo("korea_info");
        hideInfo("malaysia_info");
        hideInfo("brazil_info");
    }
}

function appendToFormHeader(textToAppend) {
    var span = document.createElement("span");
    var t = document.createTextNode(textToAppend);
    span.appendChild(t);
    document.getElementById("ebx_WorkspaceHeader").getElementsByTagName("h2")[0].appendChild(span);
}

function changeDropDownValue(prefixedPath, selectedValue, selectedText) {
    ebx_form_setValue(prefixedPath, {
        'key': selectedValue,
        'label': selectedText
    });
}

function hideInfo(className) {
    var rows = document.getElementsByClassName(className);
    for (var i = 0; i < rows.length; i++) {
        rows[i].style.display = "none";
    }
}

function showInfo(className) {
    var rows = document.getElementsByClassName(className);
    for (var i = 0; i < rows.length; i++) {
        rows[i].style.display = "table-row";
    }
}

function clearInfo(fields) {
    for (var i = 0; i < fields.length; i++) {
        ebx_form_setValue(fields[i], null);
    }
}

function populateLovsOnLoad() {
    var currentStateValue = ebx_form_getValue(addressPrefixedPaths.AddressState);
    var currentProvinceValue = ebx_form_getValue(addressPrefixedPaths.Province);
    var currentCountryValue = ebx_form_getValue(addressPrefixedPaths.Country);
    var currentStateLocalValue = ebx_form_getValue(addressPrefixedPaths.StateLocalLanguage);
    var currentProvinceLocalValue = ebx_form_getValue(addressPrefixedPaths.ProvinceLocalLanguage);
    if (currentCountryValue && currentStateValue && currentProvinceValue) {
        updateRelatedOptions(currentCountryValue, currentStateValue, currentProvinceValue);
    } else if (currentCountryValue && currentStateValue) {
        updateRelatedOptions(currentCountryValue, currentStateValue, null);
    } else if (currentCountryValue && currentProvinceValue) {
        updateRelatedOptions(currentCountryValue, null, currentProvinceValue);
    } else if (currentCountryValue) {
        updateRelatedOptions(currentCountryValue, null, null);
    } else {
        updateRelatedOptions('AF', null, null);
    }
    if (currentCountryValue && currentStateLocalValue && currentProvinceLocalValue) {
        updateRelatedLocalOptions(currentCountryValue, currentStateLocalValue, currentProvinceLocalValue);
    } else if (currentCountryValue && currentStateLocalValue) {
        updateRelatedLocalOptions(currentCountryValue, currentStateLocalValue, null);
    } else if (currentCountryValue && currentProvinceLocalValue) {
        updateRelatedLocalOptions(currentCountryValue, null, currentProvinceLocalValue);
    } else if (currentCountryValue) {
        updateRelatedLocalOptions(currentCountryValue, null, null);
    } else {
        updateRelatedLocalOptions('AF', null, null);
    }
}

function hideCreate() {
    var createButtons = document.querySelectorAll('[title="Create a record"]');
    if (createButtons) {
        createButtons.forEach(function(domNode, index) {
            domNode.parentNode.style.display = 'none';
            domNode.parentNode.nextSibling.style.display = 'none';
        });
    }
}

function toggleInternalInfo(accountType) {
    var internalRows = document.getElementsByClassName("internal_info");
    var i;
    if (accountType === "I") {
        for (i = 0; i < internalRows.length; i++) {
            internalRows[i].style.display = "table-row";
        }
    } else {
        ebx_form_setValue(accountPrefixedPaths.InternalAccountId, null);
        for (i = 0; i < internalRows.length; i++) {
            internalRows[i].style.display = "none";
        }
    }
}

function showWarningChar(fieldValue,args){
    var error = false;
    var byteString = '';
    try{
        byteString=utf8.encode(fieldValue);
    }catch(err){
        error=true;
    }
    var limitChar = args.split(",")[0];
    var limitByte = args.split(",")[1];
    var fname = args.split(",")[2];
    if(!error){
        if(byteString && byteString.length>limitByte){
            alert("Warning - "+fname+" length exceeds "+limitChar+" characters and may exceed the "+limitByte+" byte integration limit");
        }
    }else{
        if(fieldValue && fieldValue.length>limitChar){
            alert("Warning - "+fname+" length exceeds "+limitChar+" characters and may exceed the "+limitByte+" byte integration limit");
        }
    }
}

function showWarningByte(fieldValue,args){
    var error = false;
    var byteString = '';
    try{
        byteString=utf8.encode(fieldValue);
    }catch(err){
        error=true;
    }
    var limitChar = args.split(",")[0];
    var limitByte = args.split(",")[1];
    var fname = args.split(",")[2];
    if(!error){
        if(byteString && byteString.length>limitByte){
            alert("Warning - "+fname+" length exceeding "+limitByte+" bytes will exceed Oracle limits");
        }
    }else{
        if(fieldValue && fieldValue.length>limitChar){
            alert("Warning - "+fname+" length exceeding "+limitByte+" bytes will exceed Oracle limits");
        }
    }
}

function changeStateLocalStandard(newValue){
    ebx_form_setValue(addressPrefixedPaths.StateLocalLanguage,newValue);
}

function changeProvinceLocalStandard(newValue){
    ebx_form_setValue(addressPrefixedPaths.ProvinceLocalLanguage,newValue);
}

function changeStateStandard(newValue){
    ebx_form_setValue(addressPrefixedPaths.AddressState,newValue);
}

function changeProvinceStandard(newValue){
    ebx_form_setValue(addressPrefixedPaths.Province,newValue);
}

setInterval(lookForStateLocalChange, 200);
setInterval(lookForProvinceLocalChange, 200);
setInterval(lookForStateChange, 200);
setInterval(lookForProvinceChange, 200);

var stateLocal = "";
var provinceLocal = "";
var stateCustom = "";
var provinceCustom="";

function lookForStateLocalChange()
{
    if(document.getElementById("customStateLocalSelect")){
        var newStateVal = document.getElementById("customStateLocalSelect").value;
        if (newStateVal != stateLocal) {
            stateLocal = newStateVal;
            ebx_form_setValue(addressPrefixedPaths.StateLocalLanguage,newStateVal);
            validateOption('customStateLocalSelect',newStateVal,false);
        }
    }
}

function lookForProvinceLocalChange()
{
    if(document.getElementById("customProvinceLocalSelect")){
        var newProvinceVal = document.getElementById("customProvinceLocalSelect").value;
        if (newProvinceVal != provinceLocal) {
            provinceLocal = newProvinceVal;
            ebx_form_setValue(addressPrefixedPaths.ProvinceLocalLanguage,newProvinceVal);
            validateOption('customProvinceLocalSelect',newProvinceVal,false);
        }
    }
}

function lookForStateChange()
{
    if(document.getElementById("customStateSelect")){
        var newStateVal = document.getElementById("customStateSelect").value;
        if (newStateVal != stateCustom) {
            stateCustom = newStateVal;
            ebx_form_setValue(addressPrefixedPaths.AddressState,newStateVal);
            validateOption('customStateSelect',newStateVal,false);
        }
    }
}

function lookForProvinceChange()
{
    if(document.getElementById("customProvinceSelect")){
        var newProvinceVal = document.getElementById("customProvinceSelect").value;
        if (newProvinceVal != provinceCustom) {
            provinceCustom = newProvinceVal;
            ebx_form_setValue(addressPrefixedPaths.Province,newProvinceVal);
            validateOption('customProvinceSelect',newProvinceVal,false);
        }
    }
}

function createEditableSelectStateLocal(optionsArray){
    var container = document.getElementById("stateLocalCustomDiv");
    while(container.hasChildNodes()) {
      container.removeChild(container.lastChild);
    }
    var att = document.createAttribute("selectBoxOptions");
    var separator = ';';
    att.value = optionsArray.join([separator]);
    var idAttr = document.createAttribute("id");
    idAttr.value = "customStateLocalSelect";
    var onInputAttr = document.createAttribute("oninput");
    onInputAttr.value = 'changeStateLocalStandard(this.value)';
    var onChangeAttr = document.createAttribute("onchange");
    onChangeAttr.value = "validateOption('customStateLocalSelect',this.value,true)";
    var newElement = document.createElement("input");
    newElement.setAttributeNode(att);
    newElement.setAttributeNode(idAttr);
    newElement.setAttributeNode(onInputAttr);
    newElement.setAttributeNode(onChangeAttr);
    container.appendChild(newElement);
    createEditableSelect(newElement);
}

function createEditableSelectProvinceLocal(optionsArray){
    var container = document.getElementById("provinceLocalCustomDiv");
    while(container.hasChildNodes()) {
      container.removeChild(container.lastChild);
    }
    var att = document.createAttribute("selectBoxOptions");
    var separator = ';';
    att.value = optionsArray.join([separator]);
    var idAttr = document.createAttribute("id");
    idAttr.value = "customProvinceLocalSelect";
    var onInputAttr = document.createAttribute("oninput");
    onInputAttr.value = 'changeProvinceLocalStandard(this.value)';
    var onChangeAttr = document.createAttribute("onchange");
    onChangeAttr.value = "validateOption('customProvinceLocalSelect',this.value,true)";
    var newElement = document.createElement("input");
    newElement.setAttributeNode(att);
    newElement.setAttributeNode(idAttr);
    newElement.setAttributeNode(onInputAttr);
    newElement.setAttributeNode(onChangeAttr);
    container.appendChild(newElement);
    createEditableSelect(newElement);
}

function createEditableSelectState(optionsArray){
    var container = document.getElementById("stateCustomDiv");
    while(container.hasChildNodes()) {
      container.removeChild(container.lastChild);
    }
    var att = document.createAttribute("selectBoxOptions");
    var separator = ';';
    att.value = optionsArray.join([separator]);
    var idAttr = document.createAttribute("id");
    idAttr.value = "customStateSelect";
    var onInputAttr = document.createAttribute("oninput");
    onInputAttr.value = "changeStateStandard(this.value)";
    var onChangeAttr = document.createAttribute("onchange");
    onChangeAttr.value = "validateOption('customStateSelect',this.value,true)";
    var newElement = document.createElement("input");
    newElement.setAttributeNode(att);
    newElement.setAttributeNode(idAttr);
    newElement.setAttributeNode(onInputAttr);
    newElement.setAttributeNode(onChangeAttr);
    container.appendChild(newElement);
    createEditableSelect(newElement);
}

function createEditableSelectProvince(optionsArray){
    var container = document.getElementById("provinceCustomDiv");
    while(container.hasChildNodes()) {
      container.removeChild(container.lastChild);
    }
    var att = document.createAttribute("selectBoxOptions");
    var separator = ';';
    att.value = optionsArray.join([separator]);
    var idAttr = document.createAttribute("id");
    idAttr.value = "customProvinceSelect";
    var onInputAttr = document.createAttribute("oninput");
    onInputAttr.value = "changeProvinceStandard(this.value)";
    var onChangeAttr = document.createAttribute("onchange");
    onChangeAttr.value = "validateOption('customProvinceSelect',this.value,true)";
    var newElement = document.createElement("input");
    newElement.setAttributeNode(att);
    newElement.setAttributeNode(idAttr);
    newElement.setAttributeNode(onInputAttr);
    newElement.setAttributeNode(onChangeAttr);
    container.appendChild(newElement);
    createEditableSelect(newElement);
}

function validateOption(id,option,isOnChange){
 //    if(option){
        var editableSelectField = document.getElementById(id);
        var optionsArray = editableSelectField.getAttribute("selectBoxOptions").split(";");
         if(optionsArray.length>0 && option!="" && option!=undefined && optionsArray.indexOf(option)<0){
            if(isOnChange){
                if(id.includes("StateLocal")){
                    showErrorCustomSelect("StateLocalTd", "stateLocalCustomDiv", option);//alert("Warning: State Local Language value is invalid");
                }else if(id.includes("State")){
                    showErrorCustomSelect("StateTd", "stateCustomDiv", option);//alert("Warning: State value is invalid");
                }else if(id.includes("ProvinceLocal")){
                    showErrorCustomSelect("ProvinceLocalTd", "provinceLocalCustomDiv", option);//alert("Warning: Province Local Language value is invalid");
                }else if(id.includes("Province")){
                    showErrorCustomSelect("ProvinceTd", "provinceCustomDiv", option);//alert("Warning: Province value is invalid");
                }
            }
         }else{
            if(id.includes("StateLocal")){
                removeErrorCustomSelect("StateLocalTd", "stateLocalCustomDiv");//alert("Warning: State Local Language value is invalid");
            }else if(id.includes("State")){
                removeErrorCustomSelect("StateTd", "stateCustomDiv");//alert("Warning: State value is invalid");
            }else if(id.includes("ProvinceLocal")){
                removeErrorCustomSelect("ProvinceLocalTd", "provinceLocalCustomDiv");//alert("Warning: Province Local Language value is invalid");
            }else if(id.includes("Province")){
                removeErrorCustomSelect("ProvinceTd", "provinceCustomDiv");//alert("Warning: Province value is invalid");
            }
         }
 //    }
}

function showErrorCustomSelect(labelTdId, containerDivId, currentValue) {
	var containerDivElem = document.getElementById(containerDivId);
	var errorMessageDivElem = document.getElementById(containerDivId+"Error");
	if(errorMessageDivElem){
		containerDivElem.removeChild(errorMessageDivElem);
	}
    var errorMessage = "The value '"+currentValue+"' is not valid";
	var errorMessageDivNode = getNewErrorMessageDiv(errorMessage,containerDivId+"Error");
	var labelTdElem = document.getElementById(labelTdId);
	var labelSpanElem = labelTdElem.querySelector("span.ebx_RawLabel");
	addClass(labelSpanElem,"ebx_Error");
	containerDivElem.appendChild(errorMessageDivNode);
	var submitElements = document.querySelectorAll("button[type='submit']");
	var i;
	for(i=0;i<submitElements.length;i++){
		if(!hasClass(submitElements[i],"ebx_Disabled")){
			addClass(submitElements[i],"ebx_Disabled");
			submitElements[i].disabled = true;
		}
	}
}

function removeErrorCustomSelect(labelTdId, containerDivId){
	var labelTdElem = document.getElementById(labelTdId);
	var labelSpanElem = labelTdElem.querySelector("span.ebx_RawLabel");
	removeClass(labelSpanElem,"ebx_Error");
	var containerDivElem = document.getElementById(containerDivId);
	var errorMessageDivElem = document.getElementById(containerDivId+"Error");
	if(errorMessageDivElem){
		containerDivElem.removeChild(errorMessageDivElem);
	}
	var resultsArray = document.querySelectorAll("div.customErrorContainer");
	if(!resultsArray || !resultsArray.length>0){
		var submitElements = document.querySelectorAll("button[type='submit']");
		var i;
		for(i=0;i<submitElements.length;i++){
			if(hasClass(submitElements[i],"ebx_Disabled")){
				removeClass(submitElements[i],"ebx_Disabled");
				submitElements[i].disabled = false;
			}
		}
	}
}

function getNewErrorMessageDiv(errorMessage,divId){
    var div1 = document.createElement("div");
    var errorId = document.createAttribute("id");
	errorId.value = divId;
    var classmc = document.createAttribute("class");
	classmc.value = "ebx_MessageContainer customErrorContainer";
	div1.setAttributeNode(errorId);
	div1.setAttributeNode(classmc);

	var div2 = document.createElement("div");
    var classie = document.createAttribute("class");
	classie.value = "ebx_IconError";
	div2.setAttributeNode(classie);

	var div3 = document.createElement("div");
    var classe = document.createAttribute("class");
	classe.value = "ebx_Error";
	div3.setAttributeNode(classe);

	var textnode = document.createTextNode(errorMessage);

	div3.appendChild(textnode);
	div2.appendChild(div3);
	div1.appendChild(div2);
	return div1;
}

function hasClass(ele,cls) {
  return !!ele.className.match(new RegExp('(\\s|^)'+cls+'(\\s|$)'));
}

function addClass(ele,cls) {
  if (!hasClass(ele,cls)) ele.className += " "+cls;
}

function removeClass(ele,cls) {
  if (hasClass(ele,cls)) {
    var reg = new RegExp('(\\s|^)'+cls+'(\\s|$)');
    ele.className=ele.className.replace(reg,' ');
  }
}

function validateNlsLanguage(country,ou,nlsLang){
	if(country){
		if(nlsLang){
			if(ou){
				if(lookupObj){
                    //console.log(lookupObj);
                    //console.log(lookupObj["VALIDATE_NLS"]);
                    //console.log(lookupObj["VALIDATE_NLS"]["CN_"+ou]);
                    if(lookupObj["VALIDATE_NLS"][country+"_"+ou]){
                        if(nlsLang!=lookupObj["VALIDATE_NLS"][country+"_"+ou]){
                            var msgs = new EBX_ValidationMessage();
                            msgs.warnings = ['Invalid NLS Code'];
                            ebx_form_setNodeMessage(addressPrefixedPaths.NLSLanguage,msgs);
                        }else{
                            ebx_form_setNodeMessage(addressPrefixedPaths.NLSLanguage,null);
                        }
				    }else{
                            ebx_form_setNodeMessage(addressPrefixedPaths.NLSLanguage,null);
				    }
				}
			}else{
                ebx_form_setNodeMessage(addressPrefixedPaths.NLSLanguage,null);
            }
		}else{
			if(!nlsLang && (country=='CN' || country=='KR' || country=='TW')){
				var a1 = ebx_form_getValue(addressPrefixedPaths.AddressLine1LocalLanguage);
				var a2 = ebx_form_getValue(addressPrefixedPaths.AddressLine2LocalLanguage);
				var a3 = ebx_form_getValue(addressPrefixedPaths.AddressLine3LocalLanguage);
				var a4 = ebx_form_getValue(addressPrefixedPaths.AddressLine4LocalLanguage);
				var city = ebx_form_getValue(addressPrefixedPaths.CityLocalLanguage);
				var state = ebx_form_getValue(addressPrefixedPaths.StateLocalLanguage);
				var postal = ebx_form_getValue(addressPrefixedPaths.PostalLocalLanguage);
				var province = ebx_form_getValue(addressPrefixedPaths.ProvinceLocalLanguage);
				var county = ebx_form_getValue(addressPrefixedPaths.CountyLocalLanguage);
				var country = ebx_form_getValue(addressPrefixedPaths.CountryLocalLanguage);
				if(a1 || a2 || a3 || a4 || city || state || postal || province || county || country){
					var msgs = new EBX_ValidationMessage();
					msgs.warnings = ['Invalid Address NLS Code'];
					ebx_form_setNodeMessage(addressPrefixedPaths.NLSLanguage,msgs);
				}else{
					ebx_form_setNodeMessage(addressPrefixedPaths.NLSLanguage,null);
				}
			}else{
			    ebx_form_setNodeMessage(addressPrefixedPaths.NLSLanguage,null);
			}
		}
	}
}

function validateNlsLanguageHelper(){
	var country = ebx_form_getValue(addressPrefixedPaths.Country);
	var ou = null;
	var ouElementName = '___40_cfvAO__OperatingUnit_5b_0_5d_';
	if(document.getElementsByName(ouElementName) && document.getElementsByName(ouElementName).length>0){
		ou = document.getElementsByName(ouElementName)[0].value;
	}
	var nlsLang = ebx_form_getValue(addressPrefixedPaths.NLSLanguage);
	if(nlsLang){
	    nlsLang=nlsLang.key;
	}
	//console.log('country='+country+' ou='+ou+' nlsLang='+nlsLang);
	validateNlsLanguage(country,ou,nlsLang);
}

function validateTaxId() {
    var country = ebx_form_getValue(addressPrefixedPaths.Country);
    var value = ebx_form_getValue(addressPrefixedPaths.TaxRegistrationNumber);
    console.log('country='+country);
    console.log('value='+value);
    if (value && country) {
        if (lookupObj) {
            if (lookupObj["VALIDATE_TAX_ID"][country]) {
                var patt = new RegExp(lookupObj["VALIDATE_TAX_ID"][country], "g");
                if (!patt.exec(value)) {
                    var msgs = new EBX_ValidationMessage();
                    msgs.warnings = ['Invalid Tax Id'];
                    ebx_form_setNodeMessage(addressPrefixedPaths.TaxRegistrationNumber, msgs);
                } else {
                    ebx_form_setNodeMessage(addressPrefixedPaths.TaxRegistrationNumber, null);
                }
            } else {
                ebx_form_setNodeMessage(addressPrefixedPaths.TaxRegistrationNumber, null);
            }
        }
    } else {
        ebx_form_setNodeMessage(addressPrefixedPaths.TaxRegistrationNumber, null);
    }
}

function validateCity() {
    var country = ebx_form_getValue(addressPrefixedPaths.Country);
    var city = ebx_form_getValue(addressPrefixedPaths.City);
    console.log('country='+country);
    console.log('city='+city);
    if (city && country) {
        if (cityLookup) {
            if (cityLookup[country]) {
                var cityArray = cityLookup[country];
                if (cityArray.indexOf(city.toLowerCase())===-1) {
                    var msgs = new EBX_ValidationMessage();
                    msgs.warnings = ['Invalid City'];
                    ebx_form_setNodeMessage(addressPrefixedPaths.City, msgs);
                } else {
                    //var msgs = ebx_form_getNodeMessage(addressPrefixedPaths.City);
                    //if(){
                        //var index = array.indexOf('Invalid City');
                        //if (index !== -1) array.splice(index, 1);
                        ebx_form_setNodeMessage(addressPrefixedPaths.City, null);
                    //}
                }
            } else {
                ebx_form_setNodeMessage(addressPrefixedPaths.City, null);
            }
        }
    } else {
        ebx_form_setNodeMessage(addressPrefixedPaths.City, null);
    }
}

function validatePostalCode(){
    var country = ebx_form_getValue(addressPrefixedPaths.Country);
    var postalCode = ebx_form_getValue(addressPrefixedPaths.PostalCode);
	if(country){
		if(postalCode && lookupObj){
            if(lookupObj["VALIDATE_POSTAL_FORMAT"][country]){
                var patt = new RegExp(lookupObj["VALIDATE_POSTAL_FORMAT"][country], "g");
                if (!patt.exec(postalCode)) {
                    var msgs = new EBX_ValidationMessage();
                    msgs.warnings = ['Invalid Zip Code'];
                    ebx_form_setNodeMessage(addressPrefixedPaths.PostalCode,msgs);
                }else{
                    ebx_form_setNodeMessage(addressPrefixedPaths.PostalCode,null);
                }
            }else{
                ebx_form_setNodeMessage(addressPrefixedPaths.PostalCode,null);
            }
		}else if(!postalCode && lookupObj ){
            if(lookupObj["VALIDATE_POSTAL_NOT_NULL"][country]){
                var msgs = new EBX_ValidationMessage();
                msgs.warnings = ['Invalid or Null Postal Code'];
                ebx_form_setNodeMessage(addressPrefixedPaths.PostalCode,msgs);
            }else{
                ebx_form_setNodeMessage(addressPrefixedPaths.PostalCode,null);
            }
		}else{
            ebx_form_setNodeMessage(addressPrefixedPaths.PostalCode,null);
		}
	}
}

function defaultUsingLookup(thisField,keyField,isLov){
    var lovs = {};
    lovs["DEFAULT_InvoiceCopies"] = {"1":{key:"1",label:"One copy"},"2":{key:"2",label:"Two copies"},"3":{key:"3",label:"Three copies"},"4":{key:"4",label:"Four copies"},"5":{key:"5",label:"Five copies"},"0.3":{key:"0.3",label:"Suppress because a Government Invoice number is required"},"0.2":{key:"0.2",label:"Suppress because of special format requirements"}};
    lovs["DEFAULT_SendAcknowledgement"] = {"Y":{key:"Y",label:"Y"},"N":{key:"1",label:"One copy"}};
    var keyValue = ebx_form_getValue(addressPrefixedPaths[keyField]);
    if(lookupObj){
		if(keyValue){
            if(lookupObj["DEFAULT_"+thisField][keyValue]){
                var value = lookupObj["DEFAULT_"+thisField][keyValue];
                if(isLov){
                    ebx_form_setValue(addressPrefixedPaths[thisField], lovs["DEFAULT_"+thisField][value]);
                }else{
                    ebx_form_setValue(addressPrefixedPaths[thisField], value);
                }
            }
		}
    }
}

function validateUsingLookup(param){
    var params = param.split("|");
    var thisField = params[0];
    var keyField = params[1];
    var message = params[2];
    var keyValue = ebx_form_getValue(addressPrefixedPaths[keyField]);
    var thisValue = ebx_form_getValue(addressPrefixedPaths[thisField]);
    if(lookupObj){
		if(keyValue){
            if(lookupObj["VALIDATE_"+thisField][keyValue]){
                var expr = lookupObj["VALIDATE_"+thisField][keyValue];
                var patt = new RegExp(expr, "g");
                if (!patt.exec(thisValue)) {
                    var msgs = new EBX_ValidationMessage();
                    msgs.warnings = [message];
                    ebx_form_setNodeMessage(addressPrefixedPaths[thisField],msgs);
                }else{
                    ebx_form_setNodeMessage(addressPrefixedPaths[thisField],null);
                }
            }else{
                ebx_form_setNodeMessage(addressPrefixedPaths[thisField],null);
            }
		}else{
            ebx_form_setNodeMessage(addressPrefixedPaths[thisField],null);
        }
    }else{
        ebx_form_setNodeMessage(addressPrefixedPaths[thisField],null);
    }
}

function calculatedFields(countryCode) {
    defaultUsingLookup("InvoiceCopies","Country",true);
    defaultUsingLookup("SendAcknowledgement","Country",true);
    defaultUsingLookup("TaxRegimeCode","Country",true);
    updateRelatedOptions(countryCode, null, null);
    updateRelatedLocalOptions(countryCode, null, null);
    validatePostalCode();
    validateCity();
    validateTaxId();
    validateNlsLanguageHelper();
    validateUsingLookup("InvoiceCopies|Country|Invalid Invoice Copies");
}