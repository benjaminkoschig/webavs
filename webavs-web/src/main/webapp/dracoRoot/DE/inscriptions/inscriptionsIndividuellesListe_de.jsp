<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.draco.application.DSApplication"%>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.draco.db.inscriptions.DSInscriptionsIndividuellesListeViewBean viewBean = (globaz.draco.db.inscriptions.DSInscriptionsIndividuellesListeViewBean )session.getAttribute("viewBean");
	String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";
	userActionValue = "draco.inscriptions.inscriptionsIndividuellesListe.modifier";
 	boolean wantPagination = true;
	boolean wantPaginationPosition = false;
	String baseLink = mainServletPath+"?userAction="+request.getParameter("userAction");
	baseLink = baseLink.substring(1,baseLink.lastIndexOf(".")+1);
	String findPreviousLink = baseLink+"precedantPerso";
	String findNextLink = baseLink+"suivantPerso";
	String ajouterPar15 = baseLink+"ajouter15";
	selectedIdValue = viewBean.getIdDeclaration();
	String rechercher = baseLink +"afficher&selectedId="+selectedIdValue;
	bButtonDelete = false;
	bButtonValidate = false;
	bButtonCancel = false;
	bButtonUpdate = false;
	idEcran = "CDS0004";
	
	boolean rightAdd = objSession.hasRight("draco.inscriptions.inscriptionsIndividuellesListe.ajouter15","ADD");
	boolean rightUpdate = objSession.hasRight(userActionValue,"UPDATE");
	boolean rightDelete = objSession.hasRight(userActionValue,"DELETE");
	
	String gedFolderType = "";
	String gedServiceName = "";
	try {
		globaz.globall.api.BIApplication osiApp = globaz.globall.api.GlobazSystem.getApplication(DSApplication.DEFAULT_APPLICATION_DRACO);
		gedFolderType = osiApp.getProperty("ged.folder.type", "");
		gedServiceName = osiApp.getProperty("ged.servicename.id", "");
	} catch (Exception e){
		// Le reste de la page doit tout de même fonctionner
	}
	
	

%>
<%@page import="globaz.draco.db.declaration.DSDeclarationListViewBean"%>
<%@page import="globaz.draco.db.declaration.DSDeclarationViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<style  type="text/css">
.libelleRead{
    font-family : Verdana,Arial;
    background-color : #b3c4db;
    font-weight : bold;
}

.validTableClass {
width: 1256px;
}
</style>

<%@ taglib uri="/WEB-INF/dyntable.tld" prefix="ta" %>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/avsParser.js"></SCRIPT>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 


<script>

//BZ 9292 supprimer le moteur de notation pour gagner en performance 
 notationManager.b_stop = true;
 
	function updateChecked(event)
	{
		
		event.parentElement().parentElement().children[0].value = "true";

	}
	
	function del() 
	{
	    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
	        document.forms[0].elements('userAction').value="draco.inscriptions.inscriptionsIndividuellesListe.supprimer";
	        document.forms[0].submit();
	    }
	}
	
	function init()
	{
		 window.setInterval(
				    function() {  
				      // Ping to keep session alive on this mask
				      $.ajax({ 
				        url: "<%=request.getContextPath()%>/draco?userAction=draco.ping",  
				        type: "POST",
				        success: function(msg){
				          $("#ping").html (msg)
				        }
				      });
				    }, 600000
				  );

	}
function helpGetOffset(obj, coord) {
	var val = obj["offset"+coord] ;
	if (coord == "Top") val += obj.offsetHeight;
	while ((obj = obj.offsetParent )!=null) {
		val += obj["offset"+coord];
		if (obj.border && obj.border != 0) val++;
}
return val;
}
function helpDown () {
	document.all.helpBox.style.visibility = "hidden";

}
function helpOver2(event,num){
	try{
		var oObjectDescription = document.all.item('description');
		var ptrObj, ptrLayer;
		ptrObj = event.srcElement;
		ptrLayer = document.all.helpBox;
		if (!ptrObj.onmouseout) ptrObj.onmouseout = helpDown;
		var str = '<DIV CLASS="helpBoxDIV">'+oObjectDescription(num).value+'</DIV>';
		ptrLayer.innerHTML = str;
		ptrLayer.style.top  = helpGetOffset (ptrObj,"Top") + 25;
		ptrLayer.style.left = helpGetOffset (ptrObj,"Left");
		ptrLayer.style.visibility = "visible"; 
	}catch(e){
	}

	   	
}
function changeName(input)
{
	input.value=input.value.replace('ä','AE');
	input.value=input.value.replace('ö','OE');
	input.value=input.value.replace('ü','UE');
	input.value=input.value.replace('Ä','AE');
	input.value=input.value.replace('Ö','OE');
	input.value=input.value.replace('Ü','UE');	
	
	input.value=input.value.replace('é','E');
	input.value=input.value.replace('è','E');
	input.value=input.value.replace('ô','O');
	input.value=input.value.replace('à','A');		
	
	input.value=input.value.toUpperCase();
}

function checkKey(input){
	var re = /[^a-zA-Z\-'äöüÄÖÜéèôà,\s].*/	
	if (re.test(input.value)) {
		input.value = input.value.substr(0,input.value.length-1);
	}
}

	
	function upd() 
	{
		document.getElementById('btnVal').style.visibility='visible';
		document.getElementById('btnVal').style.display = "inline";
		document.forms[0].elements('btnVal').onclick= new Function ("","validate();");	
		action(UPDATE);
		
		//Pour gérer les cas où l'insc est au CI, on bloque le montant et le No AVS
		<%if(rightUpdate){%>
		document.forms[0].elements('btnUpd').style.visibility = 'hidden';		
		document.forms[0].elements('btnUpd').style.display = 'none';
		<%}%>
		document.forms[0].elements('btnVal').active= true;
		document.forms[0].elements('btnVal').onclick= new Function ("","validate();");	
		document.forms[0].elements('modeAjout').value='maj';
		
		
		<%if(!viewBean.canDoNext()){%>
			document.forms[0].elements('suivant').disabled = true;
		<%}%>
		<%if(!viewBean.canDoPrev()){%>
			document.forms[0].elements('precedant').disabled = true;
		<%}%>
		
		//Retirer les cas qu'on a mis en readonly, si couleur orange//ancienne version :						
		
		//var oObjectAvs = document.all.item("numeroAvs");
		
		var name = "partialnumeroAvs";		
		var nbr_input = (document.all.item("genreEcriture").length-1);
		
		var oObjectAvs	= new Array();
		
		for (i = 0; i <= nbr_input; i++)
		{
			oObjectAvs[i] = document.getElementById(name+(i+1));
			
		}
		
				
   		for (i = 0; i < (oObjectAvs.length-1); i++)
   		{
         	oObjectAvs[i].readOnly = false;
   		}
   	
   		
   	//Pour les inscriptions déjà comptabilisées genre, montant et No Avs doivent être non modifiable	
   	var oObjectIsCi = document.all.item('isCI');
	var oObjectGenreEcriture = document.all.item("genreEcriture");
	var oObjectMontant = document.all.item("montant");
	
	if (oObjectIsCi != null)
	{
		if (oObjectIsCi.length != null)
		{
	      		for (ind = 0; ind < oObjectIsCi.length; ind++)
	      		{
	      			if(oObjectIsCi(ind).value == 'true')
	      			{
	      			
	      			//Permet afficher les insc en grisé sans disabled (non transmis à lentity)      			
	      			      		
						oObjectAvs[ind].readOnly = true;      			
						oObjectAvs[ind].style.color = "#ACA899";
						oObjectMontant(ind).readOnly = true;      			
						oObjectMontant(ind).style.color = "#ACA899";
						oObjectGenreEcriture(ind).readOnly = true;      			
						oObjectGenreEcriture(ind).style.color = "#ACA899";
	      			}
	      		}
	   		}
	}
	
	var oObjectisRa = document.all.item("isRA");
	var oObjectisNomin = document.all.item("nomPrenom");
	if (oObjectisRa != null){
	if (oObjectisRa.length != null){
      		for (i = 0; i < oObjectisRa.length; i++){
      			if(oObjectisRa(i).value == 'true'){
      				oObjectisNomin(i).readonly = true;
	   				oObjectisNomin(i).disabled = true;
      			}
			}
   		}

	}		

	}
	function add()
	{
	}
	
	function validate() 
	{
		document.getElementById('btnVal').style.visibility='hidden';
		document.getElementById('btnVal').style.display = 'none';
		var oObject = document.all.item("soumisCheck");
		var oObjectHidden = document.all.item("soumis");
		
		if (oObject != null)
		{
	   		if (oObject.length != null)
	   		{
	      		for (i = 0; i < oObject.length; i++)
	      		{
	         		oObjectHidden(i).value= oObject(i).checked;
	      		}
	   		}
	
		}
		var oObjectNNSSVal = document.all.item("nnss");
		for( k = 0; k < 15; k++){
			var nameNss = "numeroAvs"+ (k+1) +"NNSS";
			if(oObjectNNSSVal(k) != null && document.forms[0].elements(nameNss) != null){
				oObjectNNSSVal(k).value = document.forms[0].elements(nameNss).value;
			}
		
		}

		// fonction a ajouter 		
		changeNSSInput();
		
				
	   if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="draco.inscriptions.inscriptionsIndividuellesListe.ajouter";
	    else
	        document.forms[0].elements('userAction').value="draco.inscriptions.inscriptionsIndividuellesListe.modifier";
	     action(COMMIT);
	}
	
	
	function search()
	{
	  	document.forms[0].elements('userAction').value="draco.inscriptions.inscriptionsIndividuellesListe.afficher";
     	action(COMMIT);
	}
	
		
	function cancel()
	{
		if (document.forms[0].elements('_method').value == "add")
	  		document.forms[0].elements('userAction').value="back";
	 	else
	  		document.forms[0].elements('userAction').value="draco.inscriptions.inscriptionsIndividuellesListe.afficher";
	}
	
	function postInit()
	{
		
		
		if(document.getElementById('_valid').value != 'fail')
		{
			readOnly(true);
		}	
		
		<%if(viewBean.canDoNext()){%>
			document.forms[0].elements('suivant').disabled = false;
		<%}%>
		<%if(viewBean.canDoPrev()){%>
			document.forms[0].elements('precedant').disabled = false;
		<%}%>
		if(document.getElementById('_valid').value != 'fail')
		{
			document.getElementById('btnVal').style.visibility='hidden';
			document.getElementById('btnVal').style.display = 'none';
			document.getElementById('btnVal').onclick= new Function("","validate();");
			<%if(rightUpdate){%>
				document.forms[0].elements('btnUpd').disabled = false;
				document.forms[0].elements('btnUpd').readonly = false;
			<%}else{%>
			document.getElementById('delete').style.visibility='hidden';
			document.getElementById('delete').style.display = 'none';
			<%}%>			
		}else{	
			document.getElementById('btnVal').style.visibility='visible';
			document.getElementById('btnVal').style.display = 'inLine';
			document.getElementById('btnVal').onclick= new Function("","validate();");

		}
		
		//activer les boutons rechercher et ajouter
		<%if(rightAdd){%>	
			document.forms[0].elements('ajout').disabled = false;
			document.forms[0].elements('ajout').readonly = false;
		<%}%>
		document.forms[0].elements('rechercher').disabled = false;
		document.forms[0].elements('rechercher').readonly = false;
		document.forms[0].elements('partialfromNumeroAvs').disabled = false;
		document.forms[0].elements('partialfromNumeroAvs').readonly = false;	
		document.forms[0].elements('fromNomPrenom').disabled = false;
		document.forms[0].elements('fromNomPrenom').readonly = false;
		document.forms[0].elements('aTraiter').disabled = false;
		document.forms[0].elements('aTraiter').readonly = false;
		document.forms[0].elements('forAvertissement').disabled = false;
		document.forms[0].elements('forAvertissement').readonly = false;
		document.forms[0].elements('tri').disabled = false;
		document.forms[0].elements('tri').readonly = false;
		document.forms[0].elements('forMontantSigne').disabled = false;
		document.forms[0].elements('forMontantSigne').readonly = false;
		document.forms[0].elements('forMontantSigneValue').disabled = false;
		document.forms[0].elements('forMontantSigneValue').readonly = false;
			//}else{
			//document.forms[0].elements('btnUpd').style.visibility = 'hidden';		
			//document.forms[0].elements('btnUpd').style.display = 'none';
			//document.getElementById('btnVal').style.visibility='visible';
			//document.getElementById('btnVal').style.display = 'inLine';
			//document.getElementById('btnVal').onclick= new Function("","validate();");	
			
		//}
		<%if(!viewBean.canDoNext()){%>
			document.forms[0].elements('suivant').disabled = true;
		<%}%>
		<%if(!viewBean.canDoPrev()){%>
			document.forms[0].elements('precedant').disabled = true;
		<%}%>
	
	//Gérer le mouseover pour afficher sexe et date de naissance
	try{
	document.forms[0].elements('partialNumeroAvs1').onmouseover = new Function("",'helpOver2 (event,0);');	 
	document.forms[0].elements('partialNumeroAvs2').onmouseover = new Function("",'helpOver2 (event,1);');	 
	document.forms[0].elements('partialNumeroAvs3').onmouseover = new Function("",'helpOver2 (event,2);');	 
	document.forms[0].elements('partialNumeroAvs4').onmouseover = new Function("",'helpOver2 (event,3);');	 
	document.forms[0].elements('partialNumeroAvs5').onmouseover = new Function("",'helpOver2 (event,4);');	 
	document.forms[0].elements('partialNumeroAvs6').onmouseover = new Function("",'helpOver2 (event,5);');	 
	document.forms[0].elements('partialNumeroAvs7').onmouseover = new Function("",'helpOver2 (event,6);');	 
	document.forms[0].elements('partialNumeroAvs8').onmouseover = new Function("",'helpOver2 (event,7);');	 
	document.forms[0].elements('partialNumeroAvs9').onmouseover = new Function("",'helpOver2 (event,8);');	 
	document.forms[0].elements('partialNumeroAvs10').onmouseover = new Function("",'helpOver2 (event,9);');	 
	document.forms[0].elements('partialNumeroAvs11').onmouseover = new Function("",'helpOver2 (event,10);');	 
	document.forms[0].elements('partialNumeroAvs12').onmouseover = new Function("",'helpOver2 (event,11);');
	document.forms[0].elements('partialNumeroAvs13').onmouseover = new Function("",'helpOver2 (event,12);');
	document.forms[0].elements('partialNumeroAvs14').onmouseover = new Function("",'helpOver2 (event,13);');
	document.forms[0].elements('partialNumeroAvs15').onmouseover = new Function("",'helpOver2 (event,14);');
	}catch(e){
	}
	
	
	var oObject = document.all.item("soumisCheck");
	var oObjectHidden = document.all.item("soumis");
	if (oObject != null){
   		if (oObjectHidden.length != null){
      		for (i = 0; i < oObjectHidden.length; i++){
         		if(oObjectHidden(i).value=='true'){
         			oObject(i).checked = "checked";
         		}
      		}
   		}

	}
	
	var oObjectCas = document.all.item("casSpecial");
	var oObjectAvs	= new Array();
	var name = "partialnumeroAvs";		
	var nbr_input = (document.all.item("genreEcriture").length-1);
		for (i = 0; i <= nbr_input; i++)
		{
			oObjectAvs[i] = document.getElementById(name+(i+1));
			
		}
		
				
   		for (i = 0; i < (oObjectAvs.length-1); i++)
   		{
         	oObjectAvs[i].readOnly = false;
   		}

	//var oObjectAvs = document.all.item("partialnumeroAvs");
	//Si cas spécial on met le num AVS en orange
	if (oObjectCas != null){
	if (oObjectCas.length != null){
      		for (i = 0; i < oObjectCas.length; i++){
         		if(oObjectCas(i).value=='true'){
         			oObjectAvs[i].style.color = "#d57a01";
         			oObjectAvs[i].disabled = false;
         			oObjectAvs[i].readOnly = true;
         		}
         	//oObjectAvs(i).maxLength = "14";
         	//oObjectAvs(i).onkeypress= new Function ("","return filterCharForPositivFloat(window.event);")
         		
      		}
   		}

	}

	//Pour les periodes
	var oObjectPeriodeDebut = document.all.item("periodeDebut");
	if (oObjectPeriodeDebut != null){
	if (oObjectPeriodeDebut.length != null){
      		for (i = 0; i < oObjectPeriodeDebut.length; i++){
				oObjectPeriodeDebut(i).onkeypress= new Function ("","return filterCharForPositivFloat(window.event);")
        		oObjectPeriodeDebut(i).maxLength = "5";
     		
      		}
   		}

	}	
	var oObjectRem = document.all.item("remarqueCont");
	if (oObjectRem != null){
	if (oObjectRem.length != null){
      		for (i = 0; i < oObjectRem.length; i++){
				oObjectRem(i).maxLength = "20";
     		
      		}
   		}

	}
	var oObjectPeriodeFin = document.all.item("periodeFin");
	if (oObjectPeriodeFin != null){
	if (oObjectPeriodeFin.length != null){
      		for (i = 0; i < oObjectPeriodeFin.length; i++){
				oObjectPeriodeFin(i).onkeypress= new Function ("","return filterCharForPositivFloat(window.event);")
        		oObjectPeriodeFin(i).maxLength = "5";
     		
      		}
   		}

	}
	
	//pour l'annee
	var oObjectanneeInsc = document.all.item("anneeInsc");
	if (oObjectanneeInsc != null){
	if (oObjectanneeInsc.length != null){
      		for (i = 0; i < oObjectanneeInsc.length; i++){
				oObjectanneeInsc(i).onkeypress= new Function ("","return filterCharForPositivFloat(window.event);")
        		oObjectanneeInsc(i).maxLength = "4";
     		
      		}
   		}

	}	
	
	//Pour nomPrenom, on met le tabindex
	var oObjectNomPrenom = document.all.item("nomPrenom");
	if (oObjectNomPrenom != null){
	if (oObjectNomPrenom.length != null){
      		for (i = 0; i < oObjectNomPrenom.length; i++){
				oObjectNomPrenom(i).tabIndex= "-1";
      		}
   		}

	}
	
	var oObjectGenreEcriture = document.all.item("genreEcriture");
	if (oObjectGenreEcriture != null){
	if (oObjectGenreEcriture.length != null){
      		for (i = 0; i < oObjectGenreEcriture.length; i++){
      			oObjectGenreEcriture(i).onkeypress= new Function ("","return filterCharForPositivInteger(window.event);")
        		oObjectGenreEcriture(i).maxLength = "2";
		
      		}
   		}

	}

	   	//Pour les inscriptions déjà comptabilisées genre, montant et No Avs doivent être non modifiable	
   	oObjectGenreEcriture = document.all.item("genreEcriture");
	//oObjectAvs = document.all.item("partialnumeroAvs");
	var oObjectMontant = document.all.item("montant");
	var oObjectIsCi = document.all.item('isCI');
	if (oObjectIsCi != null){
		if (oObjectIsCi.length != null){
			for (ind = 0; ind < oObjectIsCi.length; ind++){
      			if(oObjectIsCi(ind).value == 'true'){
      			//Permet afficher les insc en grisé sans disabled (non transmis à l entity)
      				//alert(oObjectAvs(ind).value);
					oObjectAvs[ind].disabled = false;
					oObjectAvs[ind].style.color = "#ACA899";
					oObjectAvs[ind].readOnly = true;
					oObjectMontant(ind).disabled = false;
					oObjectMontant(ind).style.color = "#ACA899";
					oObjectMontant(ind).readOnly = true;      			
					oObjectGenreEcriture(ind).disabled = false;
					oObjectGenreEcriture(ind).readOnly = true;      			
					oObjectGenreEcriture(ind).style.color = "#ACA899";					
      			}
      		}
   		}

	}
	
	

			
	//Pour les montants on autorise le négatifs	

		
		//readOnly(false);

		oObjectNNSS = document.all.item("nnss");
		if (oObjectNNSS!= null){
		if (oObjectNNSS.length != null){
			
			for (indi = 0; indi < oObjectNNSS.length; indi++){
				//alert(oObjectNNSS(indi).value);
				if(oObjectNNSS(indi).value == 'true'){
    				nssCheckChar('43', "numeroAvs"+(indi+1));

      			}else{
          			nssCheckChar('45', "numeroAvs"+(indi+1) );
      			}
      		
      			//alert(numNSS);
      			//document.getElementById(numNSS).onchange = new Function ("", "return updateCI(tag);");
      			//document.getElementById(numNSS).onfailure = new Function ("", "return resetCI(indi);");


      		}
   		}

	}

	//alert("test");
	//alert(document.forms[0].elements('numeroAvs1').value);
	var oObjectisRa = document.all.item("isRA");
	var oObjectisNomin = document.all.item("nomPrenom");
	if (oObjectisRa != null){
	if (oObjectisRa.length != null){
      		for (i = 0; i < oObjectisRa.length; i++){
      			//alert(oObjectisRa(i).value);
      			if(oObjectisRa(i).value == 'true'){
      				oObjectisNomin(i).readonly = true;
	   				oObjectisNomin(i).disabled = true;
      			}
        		
			}
   		}

	}							   
		//document.forms[0].elements('partialnumeroAvs1').alt = "salut";
		//alert("alt succes");
	document.body.onkeyup = function() {
		if(window.event.altKey){
			return;
		}	
		if (window.event.keyCode == 13) {
			if (window.event.ctrlKey) {
				document.getElementById('btnVal').click();
			}
			else{
				//Modif 1.5.7.1 rechercher uniquement si on est pas en mode saisie
				if(document.getElementById('btnVal').style.visibility=='hidden'){
					search();
				}
			}
		}
	};
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
	

function updateHiddenATraiter(){

	if(document.getElementById('aTraiter').checked){
		document.getElementById('aTraiterStr').value = "True";
	}else{
		document.getElementById('aTraiterStr').value = "False";
	}
	
}
function updateHiddenAvertissement(){
	if(document.getElementById('forAvertissement').checked){
		document.getElementById('forAvertissementStr').value = "True";
	}else{
		document.getElementById('forAvertissementStr').value = "False";
	}
}
function updateCI(tag){
var oObjectIsCi = document.all.item('isCI');
	
   var oObjectNomPrenom = document.all.item('nomPrenom');
   var oObjectRAIs = document.all.item('isRA');
   if (tag.select && tag.select.selectedIndex != -1) {
   		var sizeSISupp = 15 - oObjectNomPrenom.length;
		var indice = tag.anchorName.substring(16,18);
		indice = indice -1;
		indice = indice - sizeSISupp;
		oObjectNomPrenom(indice).value = tag.select[tag.select.selectedIndex].nom;
	   	oObjectNomPrenom(indice).readonly = true;
	   	oObjectNomPrenom(indice).disabled = true;
	   	oObjectRAIs(indice).value = "true";
	   	
	   	
	}
 }
function resetCI (tag){
	var oObjectNomPrenom = document.all.item('nomPrenom');
   	var oObjectRAIs = document.all.item('isRA');
   	var indice = tag.anchorName.substring(16,18);
   	var sizeSISupp = 15 - oObjectNomPrenom.length;
	indice = indice -1;
	indice = indice - sizeSISupp;
 	oObjectNomPrenom(indice).value = "";
 	oObjectNomPrenom(indice).readonly = false;
   	oObjectNomPrenom(indice).disabled = false;
   	oObjectRAIs(indice).value = "false";
}
</script>
<STYLE TYPE="text/css">
<!--
#helpBox {
position: absolute;
z-index:2;
}
DIV.helpBoxDIV {
	width: 200px;
	padding: 2px;
	background: #FFFFCF;
	border: 1px solid #000000;
	color: #000000;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
line-height: 14px;
	position:absolute;
	left:75px;
	bottom:0px;
	
}
-->
</STYLE>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detaillierte Eingabe einer Lohnbescheinigung  <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
                              <TR>
                                    <td>
                                    	<TABLE>
                            				<tr>
												<TD>
													Mitglied   &nbsp;
												</td>
												<td colspan = "2">
													<TEXTAREA 
													cols="35" class="libelleLongDisabled" tabindex="-1"
													readonly="readonly" rows="3"><%=viewBean.getdescrionTiersForSaisieInd()%></TEXTAREA>
												</td>
												<td>
													Jahr
												</TD>
												<TD> 
												   <%	String annee="";
														if (!JadeStringUtil.equals(viewBean.getAnnee(),"0",true)) {
														annee = viewBean.getAnnee();
													}
            										%>		
													<INPUT type ="text" size="4" class="disabled"  tabindex="-1"  value="<%=annee%>" readonly>
												</td>
												<td colspan = "2">
													Kontrolltotal &nbsp;
												
													<input class="montantDisabled" readonly type = "text" value ="<%=viewBean.getTotalControleDSFormate()%>" tabindex="-1" >
												</TD>
												
												<td colspan = "2">
													Sortierung &nbsp;
												<SELECT id='tri' name='tri' doClientValidation=''>
            										<OPTION value="nom" <%="nom".equals(viewBean.getTri())?"selected":""%>>Name</OPTION>
            										<OPTION value="nss" <%="nss".equals(viewBean.getTri())?"selected":""%>>SVN</OPTION>
            										<OPTION value="ordre" <%="ordre".equals(viewBean.getTri())?"selected":""%>>Buchungsordnung</OPTION> 
            										<OPTION value="montant" <%="montant".equals(viewBean.getTri())?"selected":""%>>Betrag</OPTION>
            										<OPTION value="canton" <%="canton".equals(viewBean.getTri())?"selected":""%> >Kanton</OPTION>
            									</SELECT>
													
												</TD>
											</tr>
											<TR>
												
													<td>
														SVN
													</td>
													<td>	
														<nss:nssPopup name="fromNumeroAvs"  value="<%=viewBean.getFromNumeroAvsPartial()%>" 
	            						            	 avsMinNbrDigit="99" nssMinNbrDigit="99" />  
													</td>
													<td>
													&nbsp;Zu behandeln &nbsp; 												
									              	<input type="checkbox" onclick="updateHiddenATraiter();" name="aTraiter" <%=(viewBean.getATraiter().booleanValue())? "checked" : "unchecked"%>>
              										<input type="hidden" name="aTraiterStr" value="<%=(viewBean.getATraiter().booleanValue())? "True" : "False"%>">
													</TD>
													<TD>
														Total AHV
													</TD>
													<TD colspan="3">
														<input class="montantDisabled" readonly type= "text" value="<%=viewBean.getTotalInscritJournal()%>" tabindex="-1"> &nbsp;
														Total ALV1 &nbsp; <input class="montantDisabled" readonly type= "text" value="<%=viewBean.getTotalControleAcForDisplay()%>" tabindex="-1"> &nbsp;
													</TD>
													<TD>
														
														Total FZ  &nbsp; <input class="montantDisabled" readonly type= "text" value="<%=viewBean.getTotalControleAfForDisplay()%>" tabindex="-1">
													</TD>
											</tr>
											<tr>
												<td>Name
												</td>
												<td>
													 <input type="text" onKeyDown="checkKey(this)" onChange="changeName(this)" onKeyUp="checkKey(this)" value="<%=viewBean.getFromNomPrenom()%>" name="fromNomPrenom" size="25">
												</td>
												<td colspan="5">
												Betrag &nbsp; 
												<SELECT name="forMontantSigne" >
            										
													<option value="=" <%="=".equals(viewBean.getForMontantSigne())?"selected":""%> >=</option>
													<option value="&gt;" <%=">".equals(viewBean.getForMontantSigne())?"selected":""%>>&gt;</option>
													<option value="&lt;"<%="<".equals(viewBean.getForMontantSigne())?"selected":""%> >&lt;</option>
													
            								
            									</SELECT>
            									&nbsp;
            									<input type="text" name= "forMontantSigneValue"  value="<%=viewBean.getForMontantSigneValue()%>" onChange="validateFloatNumber(this)" >
            									&nbsp;Warnung &nbsp; 												
									              	<input type="checkbox" onclick="updateHiddenAvertissement();" name="forAvertissement" <%=(viewBean.getForAvertissement().booleanValue())? "checked" : "unchecked"%>>
              										<input type="hidden" name="forAvertissementStr" value="<%=(viewBean.getForAvertissement())? "True" : "False"%>">
												</td>
												
												     
										          <TD valign="top"  width="100">
														<%
										             		String gedAffilieNumero = viewBean.getAffiliation().getAffilieNumero();
										             		String gedNumAvs = viewBean.getAffiliation().getTiers().getNumAvsActuel();
										             		String gedIdTiers = viewBean.getIdTiers();
										             		String gedIdRole = "";
										             	%>
														
														<%@ include file="/theme/gedCall.jspf" %>
													</TD>
			
          
											</tr>
											
											<tr>
												<td>
													<%if(rightAdd){%>
													<input name="ajout"  type="button" value="Hinzufügen" onClick="this.disabled=true;location.href='<%=ajouterPar15%>'"  >
													<%}%>
												</td>
												<td>
													<INPUT type="button" name="rechercher" value="Suchen" onclick="this.disabled=true;search();" >
												</TD>
													<td colspan = "6">
														<%if(!viewBean.isApresMiseEnProdEtCompl()){%>
															<p style ="color : #d57a01; font-size:15px" >Achtung, die ALV 1 und 2 müssen kontrollierte werden für die Lohnbescheinigung die vorhergehend der Installation von Web@AHV sind 

															</p>
														<%}%>	
														<%if(viewBean.isSalaireDifferePresent()){%>
															<p style ="color : #ff0000; font-size:15px" ><ct:FWLabel key="WARNING_SALAIRE_DIFFERE_EXISTANTE"/>
															</p>
														<%}%>
													</td>
											</TR>
										</TABLE>
									</td>
								</TR>
							<TR>
												<tr>
								<td>&nbsp;</td>
							</tr>	
							<tr>
							<TD colspan = "11">
							<DIV ID="helpBox"></DIV>
							<div style="overflow-x: scroll; width: 1100px;">
							<ta:table id = "validTable" height="400" readOnly="false" showAddBtn="no" htmlClass="validTableClass">
							
								<ta:column title="SVN" width="175" >
								<ta:inputNSS  name ="numeroAvs" avsMinNbrDigit="5" nssMinNbrDigit="8" jspName="<%=jspLocation%>"									
										 avsAutoNbrDigit="11" onchange="updateCI(tag);" onFailure ="resetCI(tag);"
										 nssAutoNbrDigit="10" type= "text">									
									</ta:inputNSS>
								</ta:column>								
								<ta:column title="SZ" width="28" >
									<ta:input  name ="genreEcriture" type="text"></ta:input>		
								</ta:column>
								<ta:column title="Beg" width="42">
									<ta:input  name ="periodeDebut" type="text"></ta:input>	
								</ta:column>
								<ta:column title="End" width="42">
									<ta:input  name ="periodeFin" type="text"></ta:input>	
								</ta:column>
								<%if(DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(viewBean.getTypeDeclaration())){%>>
								<ta:column title="Jahr" width="40">
									<ta:input  name ="anneeInsc" style="text-align: right" type="text"></ta:input>
								</ta:column>
								<%}%>
								<ta:column title="Kat" width="36">
									<ta:select wantBlank="true" name ="categoriePerso" >
										<ta:optioncollection csFamille="CICATPER" wantCode="true"></ta:optioncollection>
									</ta:select>
								</ta:column>
								<ta:column title="AHV Betrag" width="100">
									<ta:input  name ="montant" onchange="validateFloatNumber(this);" style="text-align: right" type="text"></ta:input>
								</ta:column>
								<ta:column title="ALV1" width="82">
									<ta:input  name ="acI" onchange="validateFloatNumber(this);" style="text-align: right" type="text"></ta:input>
								</ta:column>
								<ta:column title="ALV2" width="82">
									<ta:input  name ="acII" onchange="validateFloatNumber(this);" style="text-align: right" type="text"></ta:input>
								</ta:column>
								<ta:column title="FAK" width="100">
									<ta:input  name ="montantAF" onchange="validateFloatNumber(this);" style="text-align: right" type="text"></ta:input>
								</ta:column>
								<ta:column title="KA" width="42">
									<ta:select wantBlank="false" name ="codeCanton" >
										<ta:optioncollection csFamille="PYCANTON" wantCode="true"></ta:optioncollection>
									</ta:select>
								</ta:column>
								<ta:column title="Name" width="200">
									<ta:input  name ="nomPrenom" type="text" onchange="changeName(this)"></ta:input>
								</ta:column>
								<ta:column title="ALV" width="30">
									<ta:input  name ="soumisCheck" style="text-align: right" type="checkBox"></ta:input>
								</ta:column>
								<ta:column title="Bemerkung" width="200">
									<ta:input  name ="remarqueCont" type="text"></ta:input>
								</ta:column>
								<ta:column width ="0" >
									<ta:input style = "display : none" name = "idInscription" type="hidden" />	
									<ta:input name = "soumis" type="hidden" ></ta:input>
									<ta:input name = "casSpecial" type ="hidden"></ta:input>
									<ta:input name = "isCI" type ="hidden"></ta:input>
									<ta:input name = "nnss" type="hidden" ></ta:input>				
									<ta:input name = "isRA" type="hidden" ></ta:input>
									<ta:input name = "description" type ="hidden" ></ta:input>
								</ta:column>
						</ta:table>
						</div>
						</td>
						</TR>
						<tr>
							<td>

								<input type="hidden" name="modeAjout" value="<%=viewBean.getModeAjout()%>">
							</td>
						</tr>
						<tr>
							<td>					
								<%if (wantPagination) {%>
									
									<%if (viewBean.canDoPrev()) {%>
									<input  style=" font-size :7pt;" type="button" value="&lt;&lt;" name="precedant" accesskey="," onclick="this.disabled=true;location.href='<%=findPreviousLink%>'" active="true" >
									<% } else {%>
									<input  style=" font-size :7pt;" name="precedant" type="button" value="&lt;&lt;" disabled >
									<%}
									if(rightUpdate){%>
										<input class="btnCtrl" id="btnUpd" type="button" value="<%=btnUpdLabel%>" onclick="upd();" active="true">
									<%}%>
									
										<INPUT class="btnCtrl" id="btnVal" type="button" value="<%=btnValLabel%>" onclick="validate();" active="true" >
									
									<script>

										
									</script>
									<script>
										var nextLink = "<%=findNextLink%>" 
									</script>		
									<%if (viewBean.canDoNext()) {%>
										<input style=" font-size :7pt;" type="button" value="&gt;&gt;" name = "suivant" accesskey="." onclick="this.disabled=true;location.href='<%=findNextLink%>'" active="true"  >
									<%} else {%>
									<input style=" font-size :7pt;" name="suivant" type="button" value="&gt;&gt;" disabled >
									<%}%>
								<%}%>
								<span id="ping"> </span>
								
							
							</td>
						</tr>

						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="DS-OptionsDeclaration" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDeclaration()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>