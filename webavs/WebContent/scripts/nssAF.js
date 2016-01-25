			var keyboardChars 	= /[\x00\x03\x08\x0D\x16\x18\x1A]/;
			var aff = false;
			var isNSS = false;

			function nssCheckTouche(event, tagName){
				nssCheckChar(event.keyCode, tagName);
			}

			function nssCheckChar(charac, tagName) {
				var partialTagName = "partial" + tagName;
				var NNSSTagName = tagName + "NNSS";
				//-
				if(charac == 45){
					
					document.getElementById(tagName + 'NssPrefixe').style.visibility='hidden';
					document.getElementById(tagName + 'NssPrefixe').style.display='none';
					document.getElementById(partialTagName).maxLength = "14";
					document.getElementById(NNSSTagName).value = "false";
					eval(partialTagName + "PopupTag").setNNSS(false);
					document.getElementById("idNSS").style.display='none';
					document.getElementById("idAVS").style.display='inline';
					document.getElementById("idAFF").style.display='none';
					document.getElementById("test").style.display='inline';
					document.getElementById("numNomSearch").style.display='none';
					document.getElementById("test").focus();
					aff = false;
					nssAction(tagName);
				//+
				}else if(charac == 43){
				
					document.getElementById(tagName + 'NssPrefixe').style.visibility='visible';
					document.getElementById(tagName + 'NssPrefixe').style.display='inline';
					document.getElementById(partialTagName).maxLength = "12";
					document.getElementById(NNSSTagName).value = "true";
					eval(partialTagName + "PopupTag").setNNSS(true);
					document.getElementById("idNSS").style.display='inline';
					document.getElementById("idAVS").style.display='none';
					document.getElementById("idAFF").style.display='none';
					document.getElementById("test").style.display='inline';
					document.getElementById("numNomSearch").style.display='none';
					document.getElementById("test").focus();
					aff = false;
					nssAction(tagName);
				//*
				}else if(charac == 42){
					document.getElementById(tagName + 'NssPrefixe').style.visibility='hidden';
					document.getElementById(tagName + 'NssPrefixe').style.display='none';
					document.getElementById(partialTagName).maxLength = "99";
					document.getElementById(NNSSTagName).value = "false";
					eval(partialTagName + "PopupTag").setNNSS(false);
					document.getElementById("idNSS").style.display='none';
					document.getElementById("idAVS").style.display='none';
					document.getElementById("idAFF").style.display='inline';
					document.getElementById("test").style.display='none';
					document.getElementById("numNomSearch").style.display='inline';
					document.getElementById("numNomSearch").focus();
					aff = true;
					nssAction(tagName);
					
				}
			}

			function filterCharForPositivFloat(event) {
				return genericFilter(new RegExp("[0-9\.]"), String.fromCharCode(event.keyCode));				
			}

			function genericFilter(expr, strKey) {
				return keyboardChars.test(strKey) || expr.test(strKey);
			}

			function removeDots(aString) {
				var currentIndex = aString.indexOf(".");
				if (currentIndex > -1) {
					if (currentIndex == 0) {
						if (aString.length == 1) {
							return "";
						} else {
							return removeDots(aString.substring(1));
						}
					} else if (currentIndex == aString.length - 1) {
						return removeDots(aString.substring(0, currentIndex));
					} else {
						return removeDots(aString.substring(0, currentIndex)) + removeDots(aString.substring(currentIndex + 1));
					}
				}
				return aString;
			}
	
			function nssAction(tagName) {
				var shouldFormat = true;
				try {
					shouldFormat = event.keyCode != 8 && event.keyCode != 46;
				} catch (e){}
//				alert(event.keyCode + " >format? " + shouldFormat);
				var NNSSTagName = tagName + "NNSS";
				var numAVS = document.forms[0].elements("partial" + tagName).value;
				var newNumAVS = "";
				if (shouldFormat) {
					if(aff == true){
						if(numAVS.length !=0){
							newNumAVS = numAVS
						}					
					}else if(document.getElementById(NNSSTagName).value == "false" && aff == false){ //AVS
						numAVS = removeDots(numAVS);
						numAVS = trim(numAVS);
						if(numAVS.length != 0){
								newNumAVS = numAVS.substring(0,3);
								if (numAVS.length > 2) {
									newNumAVS += "." + numAVS.substring(3,5);
									if (numAVS.length > 4) {
										newNumAVS += "." + numAVS.substring(5,8);
										if (numAVS.length > 8) {
											newNumAVS += "." + numAVS.substring(8,11);
										}
									}
								}
							}
					}else{ //NSS
						numAVS = removeDots(numAVS);
						numAVS = trim(numAVS);
						isNSS = true;
						if(aff == false){
							if(numAVS.length != 0){
								newNumAVS = numAVS.substring(0,4);
								if (numAVS.length > 3) {
									newNumAVS += "." + numAVS.substring(4,8);
									if (numAVS.length > 7) {
										newNumAVS += "." + numAVS.substring(8,11);
									}
								}
							}		
						}				
					}					
					document.forms[0].elements("partial" + tagName).value = newNumAVS;
					if(isNSS == true){
						document.getElementById("numNomSearch").value = "756."+newNumAVS;
						isNSS = false;
					}
					else{
						document.getElementById("numNomSearch").value = newNumAVS;
					}					
				}
				concatPrefixAndPartial(tagName);
			}
			

			function concatPrefixAndPartial(tagName) {
				var NNSSTagName = tagName + "NNSS";
				if(document.getElementById(NNSSTagName).value == "false"){
					document.getElementById(tagName).value = trim(document.getElementById("partial" + tagName).value);
				} else {
					var partialValue = document.getElementById("partial" + tagName).value;
					if (partialValue != "") {
						document.getElementById(tagName).value = trim(document.getElementById(tagName + "NssPrefixe").value + document.getElementById("partial" + tagName).value);
					} else {
						document.getElementById(tagName).value = "";
					}
				}
			}

			function trim(valueToTrim)
			{
			  var lre = /^\s*/;
			  var rre = /\s*$/;

			  valueToTrim = valueToTrim.replace(lre, "");
			  valueToTrim = valueToTrim.replace(rre, "");
			  // tester si le numéro avs entré comporte slt des numéros et/ou des .
			  var cre = /((\d|\.)+)/;
			  if (!cre.test(valueToTrim)) {
				valueToTrim = "";
			  }
			  return valueToTrim;
			}
			
			function nssOnKeyUp(name) {
				//alert("yo");
				concatPrefixAndPartial(name);
			}
			