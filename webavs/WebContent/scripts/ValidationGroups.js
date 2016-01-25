/**
 ** Class ValidationGroups - Olivier Carnal
 **
 **
 **
 ** utilisé pour la validation des champs depuis le client.
 ** permet de definir des groupes de control (ou d'element HTML).
 **
 ** un groupe ne peux avoir qu'une valeur ( un seul de ces element rempli)
 **
 ** entrée : la methode add(elemName,groupName)  permet de définir le groupe
 **          pour un element HTML
 **
 ** sortie : la methode getMistakenFields() return aun tableau contenant le nom
 **          de tout les elements d'un groupe, qui aurait plusieur valeurs
 **
 **/

function ValidationGroups () {

    this.groups = new Array();
    this.groupsElements = new Array();

    this.add =  function (elemName,groupName,notEmpty) {
	
        this.groupsElements[elemName] = groupName;


        if (notEmpty==null) {
		notEmpty=false
	}
	if (notEmpty) {
		this.groups[groupName]=true;
		
	} else {
		if ((this.groups[groupName]==null)||(this.groups[groupName]!=true)){
			this.groups[groupName]=false;	
		}		
		
	}
    }


    this.getMistakenFields = function () {

       var tabAll = new Array();
       
       for (var group in this.groups) {
            var tabs =  this.validateGroup(group);
            for (var  t in tabs){
                tabAll.push(tabs[t])
            }
       }

       return tabAll;
    } // fin methode

    this.validateGroup = function (group) {


        var nbElementWithValue = 0;
        var listElementFail = new Array();

        for (var elemName in this.groupsElements) {
            if (this.groupsElements[elemName] == group) {
                var elems = document.getElementsByName(elemName);
                var str = "";
                for(var i=0;i<elems.length;i++) {
                   if (elems[i].value != null) {
                        str += elems[i].value;
                    }
                }

                if ((str != null)&&(str!="")) {
                    nbElementWithValue ++;
                }
            }
        }

        if (nbElementWithValue >1) {
            for (var elemName in this.groupsElements) {
                if (this.groupsElements[elemName] == group) {
                       	
			var elems = document.getElementsByName(elemName);
                	var str = "";
                	for(var i=0;i<elems.length;i++) {
                   		if (elems[i].value != null) {
                        		str += elems[i].value;
                    		}
                	}

                	if ((str != null)&&(str!="")) {
				listElementFail.push(elemName);
                	}			
                }
            }
        }

	// si le group doit contenir une valeur
	if ((this.groups[group]==true) &&(nbElementWithValue==0)){
		
		for (var elemName in this.groupsElements) {
			if (this.groupsElements[elemName] == group) {
				listElementFail.push(elemName);
			}
		}
	}


        return  listElementFail;
    } // fin methode
    this.getGroupElements = function (group) {
       
	    var listElement = new Array();

                for(var e in this.groupsElements) {
               	    
		    if (this.groupsElements[e] == group) {
		 	listElement.push(e);
			
                    }
                
                }
		
        
        return  listElement;
    } // fin methode	

} // fin class


var keyboardChars 	= /[\x00\x03\x08\x0D\x16\x18\x1A\x09]/;

function genericFilter(expr, strKey) {
	return keyboardChars.test(strKey) || expr.test(strKey);
}

function filterCharForFloat(event) {
	return genericFilter(new RegExp("[0-9\.-]"), String.fromCharCode(event.keyCode));
}

function filterCharForPositivFloat(event) {
	return genericFilter(new RegExp("[0-9\.]"), String.fromCharCode(event.keyCode));
}

function filterCharForInteger(event) {
	return genericFilter(new RegExp("[0-9-]"), String.fromCharCode(event.keyCode));
}

function filterCharForPositivInteger(event) {
	return genericFilter(new RegExp("[0-9]"), String.fromCharCode(event.keyCode));
}
	
function filterCharForDate(event) {
		return genericFilter(new RegExp("[0-9\.]"), String.fromCharCode(event.keyCode));
}

function validateFloatNumber(input, precision) {
	if (!precision) precision=2;
	var valueStr = new String(input.value);
	var decote=/'/g;
	valueStr = valueStr.replace(decote,"");
	if (isNaN(valueStr))
		valueStr="";
	else
		valueStr = (new Number(valueStr)).toFixed(precision);
	var i = valueStr.indexOf(".");
	var j = 0;
	while (i>0) {
		if (j%3==0 && j!=0 && valueStr.charAt(i-1)!='-') {
			valueStr = valueStr.substring(0,i) +"'"+ valueStr.substring(i);
			j++;
			i--;
		}
		i--;
		j++;
	}
	input.value = valueStr;
}

function validateIntegerNumber(input) {
	var valueStr = new String(input.value);
	var decote=/'/g;
	valueStr = valueStr.replace(decote,"");
	if (isNaN(valueStr))
		valueStr="";
	else
		valueStr = (new Number(valueStr)).toFixed(0);
	var i = valueStr.length-1;
	var j = 0;
	while (i>0) {
		if (j%3==0 && j!=0 && valueStr.charAt(i-1)!='-') {
			valueStr = valueStr.substring(0,i) +"'"+ valueStr.substring(i);
			j++;
			i--;
		}
		i--;
		j++;
	}
	input.value = valueStr;
}