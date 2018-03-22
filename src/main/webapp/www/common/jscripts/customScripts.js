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
    boolean error = false;
    var byteString = '';
    try{
        byteString=utf8.encode(fieldValue);
    }catch(err){
        error=true;
    }
    var limitChar = args.split(/\s*,\s*/)[0];
    var limitByte = args.split(/\s*,\s*/)[1];
    if(!error){
        if(byteString && byteString.length>limitByte){
            alert("Warning - Field length exceeds "+limitChar+" characters and may exceed the "+limitByte+" byte integration limit");
        }
    }else{
        if(fieldValue && fieldValue.length>limitChar){
            alert("Warning - Field length exceeds "+limitChar+" characters and may exceed the "+limitByte+" byte integration limit");
        }
    }
    console.log("byteString.length"+byteString.length);
}

function showWarningByte(fieldValue,args){
    boolean error = false;
    var byteString = '';
    try{
        byteString=utf8.encode(fieldValue);
    }catch(err){
        error=true;
    }
    var limitChar = args.split(/\s*,\s*/)[0];
    var limitByte = args.split(/\s*,\s*/)[1];
    if(!error){
        if(byteString && byteString.length>limitByte){
            alert("Warning - Field length exceeding "+limitByte+" will exceed Oracle limits");
        }
    }else{
        if(fieldValue && fieldValue.length>limitChar){
            alert("Warning - Field length exceeding "+limitByte+" will exceed Oracle limits");
        }
    }
    console.log("byteString.length"+byteString.length);
}