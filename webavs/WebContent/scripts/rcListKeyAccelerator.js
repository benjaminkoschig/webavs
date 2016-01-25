/*
 * raccourcits clavier pour ecran rcList
 *
 * OCA - 5.03.2003
 */ 

document.attachEvent("onkeydown",processKey)
document.attachEvent("onkeyup",processKeyUp)
parent.document.attachEvent("onkeydown",processKey)

var cpt= 0;
var save_key = -2;
var save_index = 0;
var index  = -1;

function processKey() {
  if (event == null) {
    myevent = parent.event;
   } else {
     myevent = event;
   }
    
   if ((myevent.ctrlKey)&&(myevent.altKey)) {
     if (myevent.keyCode==65){
      /*
       *  a : item suivant 
       */  
       index++;
       var elem = document.getElementById("item"+index);
      
       if (elem == null) {
         index --;
       } else {
         elem.focus();
       }
     } else if (myevent.keyCode==81){
       
       /*
        * q : item precedent
        */ 
       index --;
       var elem = document.getElementById("item"+index);
       if (elem == null) {
             index ++;
       } else {
         elem.focus();
       }
     } else if (myevent.keyCode==89){
       /*
        * y : ouverture du menu sur item courant
        */
       var elem = document.getElementById("item"+index);
       if (elem != null) {
         elem.click();
       }
     }
     
     /*
      * 0 et ° : revient sur le premier champ input du rc
      */
     if ((myevent.keyCode ==48)||(myevent.keyCode ==191)) {
       var  elem = parent.document.getElementsByTagName("input")[0];
        if (elem != null) {

          FWOptionSelectorButton_myDiv.style.visibility="hidden";
          elem.focus();
          elem.select();
        }
     } else if ((myevent.keyCode >=49) && (myevent.keyCode <=57)) {
       /*
        * 1 - 9 : raccourcit sur les 9 premier enregistrements. 
        *         si "double key" sur 1-9, on va au detail.
        */
     
        // anti-rebond
        if ((save_key != myevent.keyCode)
          ||(FWOptionSelectorButton_myDiv.style.visibility=="hidden")) {
          
             save_key = myevent.keyCode;
             save_index = index;
             index = myevent.keyCode-49;
             var elem = document.getElementById("item"+index);
           
             if (elem == null) {
               index = save_index;
             } else {
                elem.focus();
                elem.click();
              }
        } 
     } else {
       cpt=0;
     }
  }
}
function processKeyUp() {
  if (event == null) {
    myevent = parent.event;
  } else {
    myevent = event;
  }
  if ((myevent.ctrlKey)&&(myevent.altKey)) {
    
    /*
     * pour gestion du "double key" avec anti rebond pour aller au detail
     */
    if ((myevent.keyCode >=49) && (myevent.keyCode <=57)) {
      cpt++
      if (cpt==2){
        
        index = myevent.keyCode-49;
        var elem = document.getElementsByTagName("tr")[index+3];

		/* trouve le bon tr a utilisé, car une meme donné peut être répartie sur plusieur tr.
		 * par exemple :  ecran de rech de tiers (1 lg pour chaque  adresse du tiers)
		 */
		
		elems = document.getElementsByTagName("input");
		cpt=0;
		for (i = 0;i<elems.length;i++) {
			if (elems[i].className="FWOptionSelectorButton") {
				
				if (cpt == index) {
					// input -> td -> tr
					elem = elems[i].parentNode.parentNode;
					break;
				} else {
					cpt ++;
				}
			}
		}
		// on a le bon tr


        if (elem == null) {
          index = save_index;
        } else {
     if (selection) {
            myTd= elem.getElementsByTagName("img")[0];
          } else {
            myTd= elem.getElementsByTagName("td")[1];
     }
          if (myTd!= null) {
            myTd.click();
          }
        }
      }
    }
  }
}

//_____________________ fin raccourcits claviers  _____________________
