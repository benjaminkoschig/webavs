<!--hide this script from non-javascript-enabled browsers

function openPage(section, destination) {
  var pageWin = null;

  pageWin = window.open('master.jsp?_type=rc&_section=' + section + '&_dest=' + destination, 'winMain', 'top=0, left=0, width=' + (screen.availWidth - 10) + ', height=' + (screen.availHeight - 30) + ', scrollbars=yes, menubars=no, resizable=yes');
  pageWin.focus();
}

function showInline(id) {
    document.all(id).style.display = 'inline';
}

function showBlock(id) {
    document.all(id).style.display = 'block';
}

function hide(id) {
    document.all(id).style.display = 'none';
}

function reverseBlockDisplay(id) {
    var currStyle = document.all(id).style.display;
    document.all(id).style.display = (currStyle=='block')?'none':'block';
}

function reverseInlineDisplay(id) {
    var currStyle = document.all(id).style.display;
    document.all(id).style.display = (currStyle=='inline')?'none':'inline';
}

var KEY_TAB = 9;

function focusOn(elName) { 
    if(window.event.keyCode == KEY_TAB) {
        document.all(elName).focus();
        document.all(elName).select();
        event.returnValue = false;
    }
}



/** ----------------------------------------------------------   
 ** changeSingleElementBackground - oca
 **
 ** utilisé pour la valiadation client,
 ** permet de changer la couleur de fonds d'un element HTML si
 ** l'element est mal rempli. bien rempli -> blanc, mal rempli -> rouge
 **
 ** Entrée : elem, nom de l'element
 **          valid, flag bien rempli(true), mal rempli (false)
 **
 **---------------------------------------------------------- */
function changeSingleElementBackground (elem,valid) {
    
       if (valid)
         elem.style.background="white";
       else
         elem.style.background="#ffaaaa";
}

/** ----------------------------------------------------------   
 ** changeElementBackground - oca
 **
 ** utilisé pour la valiadation client,
 ** permet de changer la couleur de fonds d'un element HTML si
 ** l'element est mal rempli. bien rempli -> blanc, mal rempli -> rouge
 ** tous les sous elements de l'element HTML change de couleur egalement
 **
 **
 ** Entrée : elem, nom de l'element
 **          valid, flag bien rempli(true), mal rempli (false)
 **
 **---------------------------------------------------------- */
function changeElementBackground (elem,valid) {
    var elems = document.getElementsByName(elem);
   
    for(var i=0;i<elems.length;i++) {
	changeSingleElementBackground(elems[i],valid)
    }
}



/**----------------------------------------------------------   
 **
 ** fieldFormat - OCA
 **
 **---------------------------------------------------------*/
function fieldFormat(elem,type, dotAddDays) {
	if (dotAddDays == null) {
		dotAddDays = 0;
	}
   //
   // format CALENDAR
   // 
   if (type.toUpperCase()=="CALENDAR") {
        re = /^\d{8}$/;
        if ((re.test(elem.value))== true){
           elem.innerText=elem.value.substr(0,2)+"."+
                          elem.value.substr(2,2)+"."+
                          elem.value.substr(4);
        }

		re = /^\d{6}$/;
        if ((re.test(elem.value))== true){
			var millenium = new Date().getFullYear().toString().substr(0,2); // current
	  		var d2Year = elem.value.substr(4);
	  		if(d2Year >= 30) {
	  			millenium -= 1;
	  		}
           elem.innerText=elem.value.substr(0,2)+"."+
                          elem.value.substr(2,2)+"."+
			  				millenium +
                          elem.value.substr(4);
	   
        }
        
        
        re = /^\d{1,2}\.\d{1,2}\.(\d{2}|\d{4})$/;
		if ((re.test(elem.value))== true){

			v = elem.value;
			var day = v.substr(0,v.indexOf('.'))
			v = v.substr(v.indexOf('.')+1);
			var month = v.substr(0,v.indexOf('.'))
			v = v.substr(v.indexOf('.')+1);
			var year = v.substr(0)

			if (day.length==1) {day ="0"+day}
			if (month.length==1) {month ="0"+month}
			if (year.length==2) {
				d2Year=year;
				millenium = new Date().getFullYear().toString().substr(0,2);
				if(d2Year >= 30) {
	  				millenium -= 1;
	  			}
				year= millenium+year;
			}


			elem.innerText=day+"."+month+"."+year
        }
        
        

        if (elem.value=="."){

	  var date = new Date();
	  date.addDays(dotAddDays );
	  var day = date.getDate();
	  if (day < 10) day = "0"+day;
	  var month= date.getMonth();
	  month++;
	  if (month < 10) month = "0"+month;
 
           elem.innerText= day+"."+
                          month+"."+
			  date.getFullYear();
	   
        }


    }

   if (type.toUpperCase()=="CALENDAR_MONTH") {
		// dans le cas où on a 6 digits
		re = /^\d{6}$/;
		if ((re.test(elem.value))== true){
			elem.innerText=elem.value.substr(0,2)+"."+
				elem.value.substr(2);
		}
		// dans le cas où on a 4 digit
		re = /^\d{4}$/;
		if ((re.test(elem.value))== true){
			var millenium = new Date().getFullYear().toString().substr(0,2); // current
			var d2Year = elem.value.substr(2);
			if(d2Year >= 30) {
				millenium -= 1;
			}
			elem.innerText=elem.value.substr(0,2)+"."+
			millenium + elem.value.substr(2);
		}
        
		re = /^\d{1,2}\.(\d{2}|\d{4})$/;
		if ((re.test(elem.value))== true){
			v = elem.value;
			var month = v.substr(0,v.indexOf('.'))
			v = v.substr(v.indexOf('.')+1);
			var year = v.substr(0)

			if (month.length==1) {month ="0"+month}
			if (year.length==2) {
				d2Year=year;
				millenium = new Date().getFullYear().toString().substr(0,2);
				if(d2Year >= 30) {
					millenium -= 1;
				}
				year= millenium+year;
			}
			elem.innerText=month+"."+year
		}

        if (elem.value==".") {
			var date = new Date();
			var month= date.getMonth();
			month++;
			if (month < 10) {
				month = "0" + month;
			}
	        elem.innerText = month + "." + date.getFullYear();
        }
    }


   //
   // format NUMERO_AVS
   // 
   if (type.toUpperCase()=="NUMERO_AVS") {
            re = /^\d{11}$/;
            if ((re.test(elem.value))== true){
                    elem.innerText=elem.value.substr(0,3)+"."+
                    elem.value.substr(3,2)+"."+
                    elem.value.substr(5,3)+"."+
                    elem.value.substr(8);
            }
    }

    // validation non-bloquante
    return true;//validateElement(elem,false); 
}

/**----------------------------------------------------------   
 **
 ** buildGroups - OCA
 **
 **---------------------------------------------------------*/
function buildGroups (elem) {
	

    var j=0;
    
    var rules = new Array();

    	if (elem.doClientValidation!=null){
        	rules = elem.doClientValidation.split(",");
    	} 


    // construit l'information sur les groupes
    for (var j=0;j<rules.length;j++) {

       /**----------------------------------------------------------   
        ** Regle GROUP:groupName
        ** 
        ** 
        ** voir fichier ValidationGroups.js
        **---------------------------------------------------------*/

        re = /^GROUP/;
        if ((re.test(rules[j]))== true){
                
            var tab = new Array();
            tab = rules[j].split(':');
         
            // on construit juste l'obet mygroup qui contiendra les
	    // informations pour faire la validation de group 
	    var groupName = tab[1];	
          
	    if (mygroups!=null) {
		
		if ((tab.length==3)&&(tab[2]=="NOT_EMPTY")){	
	    	   mygroups.add(elem.name,groupName,true);
			
		} else {
	    	   mygroups.add(elem.name,groupName,false);
		}

	    }
	    //changeElementBackground(elem.name,true);
                
        }

    }



}

/**----------------------------------------------------------   
 **
 ** validateElement - OCA
 **
 **---------------------------------------------------------*/
function validateElement (elem,selectField) {

    

    var j=0;
    dateFormat = "dd.MM.yyyy";
    elementNotFoundError = "validation of element '"+elem.name+"'\nelement not found : ";

    var rules = new Array();
    if (elem.doClientValidation!=null){
        rules = elem.doClientValidation.split(",");
    } else {
        return true;
    }

    var belongToAGroup = false;
    var stateField = true;
    var group



	


    for (var j=0;j<rules.length;j++) {
       

  
       /**----------------------------------------------------------   
        ** Regle CALENDAR
        ** 
        ** 
        **
        **---------------------------------------------------------*/
        if (rules[j]=="CALENDAR")  {    
            if (elem.value!="") {
                if (isDate(elem.value,dateFormat) == false){
                    stateField &= false;
                }
            }
        }
   
       
       /**----------------------------------------------------------   
        ** Regle DATE_COMPARE:element:REGLE
        ** 
        ** REGLE peut etre "GREATER" ou "EQUALS" ou "GREATER_OR_EQUALS"
        **         
        **
        **---------------------------------------------------------*/
        re = /^DATE_COMPARE/;
        if ((re.test(rules[j]))== true){
       
            var tab = new Array();
            tab = rules[j].split(':');
	    
            if (elem.value!="") {
        	var elemCheck = document.getElementsByName(tab[1])[0];
        	if (elemCheck == null) {
            		alert(elementNotFoundError+tab[1]); // ne devrais pas arriver

                } else {    
             
                 var result = compareDates(elem.value,
                 dateFormat,
                 elemCheck.value,
                 dateFormat);

            if ((result != 1)&&(tab[2]=="GREATER")) {
            // si on a la comparaison "GREATER" et que le result de la 
            // comparaison donne autre chose que 1(1=greater), fail
            
                stateField &= false;

            } else if (tab[2]=="EQUALS") {
            // si on a la comparaison est "EQUALS" 
                
                if (elem.value != elemCheck.value){
                // et que l'on est pas egale : fail

                    stateField &= false;
                }

            } else if ((result != 1)&&(tab[2]=="GREATER_OR_EQUALS")) {
            // si on a la comparaison est "GREATER_OR_EQUALS" et que le result de la 
            // comparaison donne autre chose que 1(1=greater) ...
            	
                if (elem.value != elemCheck.value){
                // on regarde si on est egal, si ce n est pas le cas : fail
                    stateField &= false;
		    
                }
            }   
        }
            }
        }

    
       /**----------------------------------------------------------   
        ** Regle NOT_EMPTY
        ** 
        ** 
        **
        **---------------------------------------------------------*/
        if (rules[j]=="NOT_EMPTY")  {  
           
            var elemGroup = new Array();
	    
	    re = /^GROUP/
	    for(t=0;t<rules.length;t++) {
		
		if (re.test(rules[t])) {
			var tab = new Array();
            		tab = rules[t].split(':');
			group = tab[1];
			elemGroup = mygroups.getGroupElements (group);
			belongToAGroup = true;
			
		}		
	    }	    


	    if (belongToAGroup) {
		
		var nbElemWithValue = 0;
		for (var e=0;e<elemGroup.length;e++ ) {
			
			if ((document.getElementsByName(elemGroup[e])[0].value!=null)&&
			    (document.getElementsByName(elemGroup[e])[0].value != "")) {
        	        	nbElemWithValue ++;	
            		}
		}
		
		if (nbElemWithValue ==0) {
			stateField &= false;
		}
		
		

	    } else {	

		    if ((elem.value!=null)&&(elem.value == "")) {
        	        stateField &= false;
            	    }
	  }
        }


       /**----------------------------------------------------------   
        ** Regle NOT_EMPTY_IF:element[:value:value:value ...]
        ** 
        ** 
        **
        ** test relatif a un autre champ de la page
        ** utile si on veut un condition type 
        ** champ 'a' ne peut pas etre vide si champ b vaut '123', mais
        ** champ 'a' peut être vide si champ b vaut autre
        ** chose que '123'
        ** 
	** ou dans le genre de cas champ "a" doit etre reseigner si champ "b"
        ** est lui même renseigner.
        **---------------------------------------------------------*/

        re = /^NOT_EMPTY_IF/;
        if ((re.test(rules[j]))== true){
                
            var tab = new Array();
            tab = rules[j].split(':');
         
            // element qui va definir si elem peut etre vide ou non    
            var elemCheck = document.getElementsByName(tab[1])[0];
            if (elemCheck == null) {
                alert(elementNotFoundError+tab[1]) // ne devrais pas arriver
            } else {
           
                // par defaut, j'admet que le champ peut être vide   
                var docheck = false;

		
		if (tab.length==2) {

			// si on a juste "NOT_EMPTY_IF:element"
			// et que l'element n'est pas vide, faire le check
			
			if ((elemCheck.value!=null)&&(elemCheck.value!="")) {
				
				docheck = true;
			}

		} else {

			// si on a au moin au valeur "NOT_EMPTY_IF:element:value[...]"
			// on check les valeurs

                	for (i=2;i<tab.length;i++) {
         
         	           if (elemCheck.value == tab[i]){
                	        // si l'on rencontre une des valeur qui interdit un 
                        	// champ vide, set le flag docheck 
	                        docheck= true;  
        	            }  
                	}
		}
		

             }
        
             // si on a rencontré une valeur interdisant un champ vide, fait le test
             if ((elem.value!=null)&&(elem.value == "")&&(docheck== true)) {
                stateField &= false;
             } 
        }




    }
       /**
        **
        ** fin des rules
        **
        **/
   


       if (stateField == true) {
             if (selectField== true) { 
               	if (belongToAGroup) {
			
			elemGroup = mygroups.getGroupElements (group);
			for (var e=0;e<elemGroup.length;e++ ) {
				changeSingleElementBackground (document.getElementsByName(elemGroup[e])[0],true ); 
			}

		}else {
		
			changeSingleElementBackground (elem,true); 
		}
                
             }
        } else {
            if (selectField== true) {
                

	     	if (belongToAGroup) {
			
			elemGroup = mygroups.getGroupElements (group);
			for (var e=0;e<elemGroup.length;e++ ) {
				changeSingleElementBackground (document.getElementsByName(elemGroup[e])[0],false ); 
			}
			

		}else {
		
			changeSingleElementBackground (elem,false); 
		}

                if (elem.type=="text") {
                  elem.select();
                }
            }
        }
   

    return stateField;

   
}



/**----------------------------------------------------------   
 **
 ** validateFields - OCA
 ** 
 **
 ** fonction pour la validation automatique des champs
 ** d'un document html  
 ** 
 **
 **---------------------------------------------------------*/
function validateFields() {

     var state = true;
     var mi = 0;

     mygroups = new ValidationGroups();
	
     var inputTags = document.all.tags("input");
     for (varmi=0; mi<inputTags.length; mi++) {
        
	buildGroups(inputTags(mi));
	
     } 
     
     var selectTags = document.all.tags("select");
     for (var mi=0; mi<selectTags.length; mi++) {
        buildGroups(selectTags(mi));
     }
	
	
     // test les groups d'element HTML
		

     var mistakenElements = mygroups.getMistakenFields();
		
     for (i=0;i<mistakenElements.length;i++) {
        changeElementBackground(mistakenElements[i],false);
     }			
	
     if (i>0) {
	alert("le formulaire n'est pas valide (plusieurs valeurs pour un même groupe)");
	return false;
     }

     
     //test les elements


     var inputTags = document.body.getElementsByTagName("input");
     for (var mi=0; mi<inputTags.length; mi++) {
        // validation bloquante 
        
	state &= validateElement(inputTags(mi),true);
	
     } 
    
     var selectTags = document.all.tags("select");
     for (var mi=0; mi<selectTags.length; mi++) {
            // validation bloquante
	   
            state &= validateElement(selectTags(mi),true);
     }
 
     
     if (state == false) {
        alert("un ou plusieurs champs obligatoires ne sont pas remplis ou ne sont pas valides");
	return false;
     } 





     return state;
}

/************************************************************/
// FONCTIONS DE MANIP DE CLASSES (html class)

/*
This example function takes four parameters:

a 
	defines the action you want the function to perform. 
o 
	the object in question. 
c1 
	the name of the first class 
c2 
	the name of the second class 


Possible actions are:

swap 
	replaces class c1 with class c2 in object o. 
add 
	adds class c1 to the object o. 
remove 
	removes class c1 from the object o. 
check 
	test if class c1 is already applied to object o and returns true or false. 
*/
function jscss(a,o,c1,c2)
{
  switch (a){
    case 'swap':
      o.className=!jscss('check',o,c1)?o.className.replace(c2,c1):o.className.replace(c1,c2);
    break;
    case 'add':
      if(!jscss('check',o,c1)) {
      	o.className += o.className ? ' ' + c1 : c1;
      }
//      alert(o.className);
    break;
    case 'remove':
      var rep=o.className.match(' '+c1)?' '+c1:c1;
      o.className=o.className.replace(rep,'');
    break;
    case 'check':
      return new RegExp('\\b'+c1+'\\b').test(o.className)
    break;
  }
//  alert(o.id + " # " + o.className);
}

// FIN FONCTIONS DE MANIP DE CLASSES (html class)
/************************************************************/

// stop hiding -->