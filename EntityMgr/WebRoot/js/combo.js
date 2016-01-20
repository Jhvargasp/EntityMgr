function GetXmlHttpObject(handler) {
	var objXMLHttp = null
	if (window.XMLHttpRequest) {
		objXMLHttp = new XMLHttpRequest()
	} else if (window.ActiveXObject) {
		objXMLHttp = new ActiveXObject("Microsoft.XMLHTTP")
	}
	return objXMLHttp
}

function stateChanged() {
	if (xmlHttp.readyState == 4 || xmlHttp.readyState == "complete") {
		var xmlData = xmlHttp.responseText; 
		
		
		// //
	} else {
		// alert(xmlHttp.status);
	}
}

function htmlData(idField, query) {
	if (document.getElementById(idField)==null) {
		alert(idField+ " equals FAIL");
		return;
	}
	xmlHttp = GetXmlHttpObject()
	if (xmlHttp == null) {
		alert("Browser does not support HTTP Request");
		return;
	}

	var select = document.getElementById(idField);
	xmlHttp.onreadystatechange =  function() {
        if (xmlHttp.readyState == 4) {
        	if (xmlHttp.status == 200) 
	     	{
        		try{
        				var padre = select.parentNode;
        				var antValue=select.value;
        				var selectedIndex=0;
        				var divAux = document.createElement("select");
        				divAux.setAttribute("id", select.id);
        				divAux.setAttribute("name", select.name);
        				select.parentNode.insertBefore(divAux, select);
        				padre.removeChild(select);
        				//divAux.parentNode.insertBefore(padre, divAux);
        				
		     		   var resultsRequest= xmlHttp.responseXML;
		     		   var responses = resultsRequest.getElementsByTagName("row");
		     		   for ( var i = 0; i < responses.length; i++) {
		     			   var id="";
		     			   var value="";
		     				if (responses[i].getElementsByTagName("data0").length > 0) {
		     					id=  responses[i].getElementsByTagName("data0")[0].firstChild.data;
		     					value=id;
		     					try{
		     						value=  responses[i].getElementsByTagName("data1")[0].firstChild.data;
		     					}catch(ex){}
		     				}
		     				divAux.options[divAux.options.length] = new Option(id, value);
		     				if(antValue==id){
		     					divAux.selectedIndex=i;
		     				}
		     			}
		     		   
		     		   
        		}catch(err){
        			alert (err);
        			
        		}
           	}
            else{
        	 	//alert("Problem: " + ajax.statusText);
        	}
        }
        
    };
	xmlHttp.open("GET", "./populateCombo.html?query=" + query, true);
	xmlHttp.send();
}