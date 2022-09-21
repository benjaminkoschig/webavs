			var keyboardChars 	= /[\x00\x03\x08\x0D\x16\x18\x1A]/;

			function nssCheckTouche(event, tagName){
				nssCheckChar(event.keyCode, tagName);
			}

			function nssCheckChar(charac, tagName) {
				
				var partialTagName = "partial" + tagName;
				var NNSSTagName = tagName + "NNSS";
				//-
				if(charac == 45){
					document.getElementsByName(tagName + 'NssPrefixe')[0].style.visibility='hidden';
					document.getElementsByName(tagName + 'NssPrefixe')[0].style.display='none';
					document.getElementById(partialTagName).maxLength = "14";
					document.getElementsByName(NNSSTagName)[0].value = "false";
					eval(removeDots(partialTagName) + "PopupTag").setNNSS(false);
					nssAction(tagName);
					//+
				}else if(charac == 43){
					document.getElementsByName(tagName + 'NssPrefixe')[0].style.visibility='visible';
					document.getElementsByName(tagName + 'NssPrefixe')[0].style.display='inline';
					document.getElementById(partialTagName).maxLength = "12";
					document.getElementsByName(NNSSTagName)[0].value = "true";
					eval(removeDots(partialTagName) + "PopupTag").setNNSS(true);
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
				var numAVS = document.getElementById("partial" + tagName).value;
				numAVS = removeDots(numAVS);
				numAVS = trim(numAVS);
				var newNumAVS = "";
				if (shouldFormat) {
					if(document.getElementsByName(NNSSTagName)[0].value == "false"){ //AVS
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
					}else{ // NNSS
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
					document.forms[0].elements("partial" + tagName).value = newNumAVS;
				}
				concatPrefixAndPartial(tagName);
			}

			function concatPrefixAndPartial(tagName) {
				var NNSSTagName = tagName + "NNSS";
				if(document.getElementsByName(NNSSTagName)[0].value == "false"){
					document.getElementsByName(tagName)[0].value = trim(document.getElementById("partial" + tagName).value);
				} else {
					var partialValue = document.getElementById("partial" + tagName).value;
					if (partialValue != "") {
						document.getElementsByName(tagName)[0].value = trim(document.getElementsByName(tagName + "NssPrefixe")[0].value + document.getElementById("partial" + tagName).value);
					} else {
						document.getElementsByName(tagName)[0].value = "";
					}
				}
			}

			function trim(valueToTrim)
			{
			  var lre = /^\s*/;
			  var rre = /\s*$/;

			  valueToTrim = valueToTrim.replace(lre, "");
			  valueToTrim = valueToTrim.replace(rre, "");
			  // tester si le num�ro avs entr� comporte slt des num�ros et/ou des .
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