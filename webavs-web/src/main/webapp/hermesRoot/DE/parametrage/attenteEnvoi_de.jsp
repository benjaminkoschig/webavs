<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<%@page import="globaz.hermes.db.gestion.HEAnnoncesViewBean"%>
<%@page import="globaz.hermes.api.IHEAnnoncesViewBean"%>
<%@page import="globaz.pyxis.constantes.IConstantes"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pyxis.db.tiers.TITiersViewBean"%>
<%@page import="java.util.HashSet"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%
subTableWidth = "";
globaz.hermes.db.parametrage.HEAttenteEnvoiViewBean viewBean = (globaz.hermes.db.parametrage.HEAttenteEnvoiViewBean)session.getAttribute("viewBean");
viewBean.loadChamps();
selectedIdValue = request.getParameter("selectedId");
idEcran="GAZ0006";
String ref1 = "";
String ref2 = "";
boolean isDateNSS = globaz.hermes.utils.HEUtil.isNNSSActif(viewBean.getSession());
boolean isMotifCert = globaz.hermes.utils.HEUtil.isMotifCert(viewBean.getSession(),viewBean.getMotifArc());
String MOTIF_11 = viewBean.CODE_ARC_11;
String MOTIF_31 = viewBean.CODE_ARC_31;
String motif = viewBean.getMotifArc();

boolean alreadySent = false;
if((request.getParameter("isARC39")==null?false:request.getParameter("isARC39").equals("true"))||(!viewBean.getStatut().equals(IHEAnnoncesViewBean.CS_EN_ATTENTE) || !viewBean.isUserPermit())){
	bButtonDelete = false;
	bButtonUpdate = false;
	alreadySent = true;
}

HashSet except = new HashSet();
except.add("503003");
except.add("503005");
except.add("503006");
except.add("503007");

%>
<SCRIPT language="JavaScript">
top.document.title = "<ct:FWLabel key="HERMES_JSP_GAZ0006_TITRE_JSP"/>";
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/avsParser.js"></SCRIPT>
<SCRIPT language="javascript">

$(function(){
	$("#wantPolitesse").hide();
	$("#langueCorrespondance").change(function () {
		if($(this).val()==<%=IConstantes.CS_TIERS_LANGUE_ALLEMAND%>){
			$("#wantPolitesse").show();
		}else {
			$("#wantPolitesse").hide();
			$("#formulePolitesse").val("");
		}
	});
	
	if($("#langueCorrespondance").val()==<%=IConstantes.CS_TIERS_LANGUE_ALLEMAND%>) {
		$("#wantPolitesse").show();
	}else {
		$("#wantPolitesse").hide();
		$("#formulePolitesse").val("");
	}

});


function upperCase(input){
	input.value = input.value.toUpperCase();
}

function add() {}
function upd() {
		catscript();
}

function validate() {
    //state = validateFields();
    state = true;
    document.forms[0].elements('userAction').value="hermes.parametrage.attenteEnvoi.modifier";   
    document.forms[0].elements('_method').value=""; 
    return state;
} 

function cancel() {
	<%if(request.getParameter("modeSaisie")==null?false:request.getParameter("modeSaisie").equals("true")){%>
		document.forms[0].elements('userAction').value="hermes.gestion.inputAnnonce.afficher";
		document.forms[0].elements('_method').value="add";
		document.forms[0].elements('_valid').value="error";
		document.forms[0].elements('modeCancel').value="true";
	<%}else{%>
  		document.forms[0].elements('userAction').value="hermes.parametrage.attenteEnvoi.chercher";
  	<%}%>
  	document.forms[0].submit();
}

function del() {
	<%if(!viewBean.getStatut().equals(IHEAnnoncesViewBean.CS_EN_ATTENTE)){%>
		alert("<ct:FWLabel key="HERMES_JSP_GAZ0006_IMPOSSIBLE_DE_MODIFIER"/>");
	<%} else if(!viewBean.isUserPermit()){%>
		alert("<ct:FWLabel key="HERMES_JSP_GAZ0006_SUPPRESSION_IMPOSSIBLE"/>");
	<% }else{%>
		 if (window.confirm("<ct:FWLabel key="HERMES_JSP_GAZ0006_ARC_DELETE_CONFIRM"/>")){
	         document.forms[0].elements('userAction').value="hermes.parametrage.attenteEnvoi.supprimer";
	   	     document.forms[0].submit();
	   	    }
	 	<%}%>
}

function init(){

}

function postInit()
{
	Acatscript();
}

function checkKey(input){

	//var re = /[^a-zA-Z\-'äöü,\s].*/
	var re = /[^a-zA-Z\-'àäöôüéèëêçÄÂÀÉÈËÊÖÔÜÇ,\s].*/
	
	if (re.test(input.value)) {
		input.value = input.value.substr(0,input.value.length-1);
	}
}

function clearFields(tag){
	if (tag.select && tag.select.selectedIndex != -1) {
		element = tag.select[tag.select.selectedIndex];	
		element.nom = "";
		element.date = "";
		element.sexe = "316000";
		element.paysCode = "100";
	}
}

function toUpperCase(tagName){
	var mySt = document.forms[0].elements(tagName).value;
	document.forms[0].elements(tagName).value = mySt.toUpperCase();
}


function nameWithHyphen(input){
    var regex = new RegExp("\\s*-\\s*","g");
    input.value=input.value.replace(regex,"-");
}


function validateCharForDate(input, event) {
	var valueStr = new String(input.value);	
	var keyCode = new String(String.fromCharCode(event.keyCode));
	
	if (genericFilter(new RegExp("[0-9\.]"), keyCode)) {
	}
	else {		
		event.keyCode = '';
		return;
	}	
}


function validatePartDateOnKeyPressed(input, event) {
	var valueStr = new String(input.value);	
	var keyCode = new String(String.fromCharCode(event.keyCode));	
	
	if (genericFilter(new RegExp("[0-9\.]"), keyCode)) {
	}
	else {		
		event.keyCode = '';
		return;
	}	

	if (valueStr.length>=4) {
		if (valueStr.charAt(2)!='.')
			event.keyCode = '';
	}
}




function validatePartDate(input) {	
	var valueStr = new String(input.value);	
    var re1 = /^\d{2}\.\d{2}$/;
    var re2 = /^\d{4}$/;
	

    if ((re1.test(valueStr))!= true && (re2.test(valueStr))!= true){
    
		errorObj.text='<%=globaz.globall.util.JAUtil.replaceString(viewBean.getSession().getLabel("HERMES_00003"),"'","\\'")%>';
		showErrors();
		document.getElementById(input.name).focus();
	}	
}

function changeName(input)
{
	while (input.value.search('ä') != -1)
		input.value=input.value.replace('ä','AE');
	while (input.value.search('ö') != -1)
		input.value=input.value.replace('ö','OE');
	while (input.value.search('ü') != -1)
		input.value=input.value.replace('ü','UE');
	while (input.value.search('é') != -1)
		input.value=input.value.replace('é','E');
	while (input.value.search('è') != -1)
		input.value=input.value.replace('è','E');
	while (input.value.search('ë') != -1)
		input.value=input.value.replace('ë','E');
	while (input.value.search('ê') != -1)
		input.value=input.value.replace('ê','E');					
	while (input.value.search('ô') != -1)
		input.value=input.value.replace('ô','O');
	while (input.value.search('à') != -1)
		input.value=input.value.replace('à','A');
		
	while (input.value.search('Ä') != -1)
		input.value=input.value.replace('Ä','AE');
	while (input.value.search('À') != -1)
		input.value=input.value.replace('À','A');
	while (input.value.search('Â') != -1)
		input.value=input.value.replace('Â','A');					
	while (input.value.search('É') != -1)
		input.value=input.value.replace('É','E');	
	while (input.value.search('È') != -1)
		input.value=input.value.replace('È','E');				
	while (input.value.search('Ë') != -1)
		input.value=input.value.replace('Ë','E');
	while (input.value.search('Ê') != -1)
		input.value=input.value.replace('Ê','E');	
	while (input.value.search('Ö') != -1)
		input.value=input.value.replace('Ö','OE');
	while (input.value.search('Ô') != -1)
		input.value=input.value.replace('Ô','O');
	while (input.value.search('Ü') != -1)
		input.value=input.value.replace('Ü','UE');	
				
		
	while (input.value.search('ç') != -1)
		input.value=input.value.replace('ç','C');	
	while (input.value.search('Ç') != -1)
		input.value=input.value.replace('Ç','C');
	
	input.value=input.value.toUpperCase();
}
function checkChar(input){
//	var re = /[^a-zA-Z\-'äöü,\s].*/
	var re = /[^a-zA-Z0-9\-.,-/äöüÄÖÜéèôà,\s].*/	
	if (re.test(input.value)) {
		input.value = input.value.substr(0,input.value.length-1);
	}
}
function updateForm(tag){
	if (tag.select && tag.select.selectedIndex != -1) {
		element = tag.select[tag.select.selectedIndex];
		if(document.forms[0].elements('118008')!=null){
			document.forms[0].elements('118008').value = element.nom;						
		}
		if(document.forms[0].elements('TOSTR118010')!=null){
			document.forms[0].elements('TOSTR118010').value = element.date;
		}
		if(document.forms[0].elements('118009')!=null){
			var vSexe = element.sexe;
			if(vSexe == '316000'){
				document.forms[0].elements('118009').value = '1';
			}
			if(vSexe == '316001'){		
				document.forms[0].elements('118009').value = '2';
			}
		}
		if(document.forms[0].elements('118011')!=null){
			document.forms[0].elements('118011').value = element.paysCode;
		}		
	}
}

function updateEmployeurPartenaire(tag) {
	if (tag.select) {
		document.getElementById('nomAffilie').value = tag.select[tag.select.selectedIndex].nom;
		document.getElementById('langueCorrespondance').value = tag.select[tag.select.selectedIndex].langue;
	}
}
function setNotFound(tag){
	document.getElementById('nomAffilie').value = '';
	document.getElementById('langueCorrespondance').value = '';
	tag.select = false;
}

function updateJspNameCat()
{
<% if(isDateNSS) {%>
 
	var n = document.getElementById('categorie').value;
	var empl = '<%=IHEAnnoncesViewBean.CS_CATEGORIE_EMPLOYEUR%>';
	var indé = '<%=IHEAnnoncesViewBean.CS_CATEGORIE_INDEPENDANT%>';
	var rentier = '<%=IHEAnnoncesViewBean.CS_CATEGORIE_RENTIER%>';
	
	switch (n)
		{
			case (empl): numeroAffiliePopupTag.updateJspName("<%=servletContext + mainServletPath + "Root/ti_select_emp.jsp?like="%>"); 

						   	
			break	
			
			case (indé): numeroAffiliePopupTag.updateJspName("<%=servletContext + mainServletPath + "Root/ti_select_ind.jsp?like="%>");
							 
			break
			
			default:			  				
		}	
<%}%>
}

function catscript()
{
<% if(isDateNSS && isMotifCert) {%> 			
		var n = document.getElementById('categorie').value;
		
		var empl = '<%=IHEAnnoncesViewBean.CS_CATEGORIE_EMPLOYEUR%>';
		var indé = '<%=IHEAnnoncesViewBean.CS_CATEGORIE_INDEPENDANT%>';
		var rentier = '<%=IHEAnnoncesViewBean.CS_CATEGORIE_RENTIER%>';

		if (n==empl){											
				document.getElementById('txtarea').disabled = true;
				document.getElementById('txtarea').style.backgroundColor = '#b3c4db';
				document.getElementById('txtarea').value = "";
				document.getElementById('numeroAffilie').disabled = false;
				document.getElementById('numeroAffilie').style.backgroundColor = '#ffffff';
				document.getElementById('titreRentier').style.backgroundColor = '#b3c4db';
				document.getElementById('titreRentier').disabled = true;
				document.getElementById('titreRentier').value = "";
				document.getElementById('formulePolitesse').style.backgroundColor = '#b3c4db';
				document.getElementById('formulePolitesse').disabled = true;
				document.getElementById('formulePolitesse').value = "";
				if (document.getElementById('dateEngagement') != null) {
					document.getElementById('dateEngagement').disabled = false;
					document.getElementById('dateEngagement').style.backgroundColor = '#ffffff';
					document.getElementById('anchor_dateEngagement').disabled = false;
				}	
				   
		} else if (n==indé){	
				document.getElementById('txtarea').disabled = true;
				document.getElementById('txtarea').style.backgroundColor = '#b3c4db';
				document.getElementById('txtarea').value = "";
				document.getElementById('numeroAffilie').disabled = false;
				document.getElementById('numeroAffilie').style.backgroundColor = '#ffffff';				
				document.getElementById('titreRentier').style.backgroundColor = '#b3c4db';
				document.getElementById('titreRentier').disabled = true;
				document.getElementById('titreRentier').value = "";
				document.getElementById('formulePolitesse').style.backgroundColor = '#b3c4db';
				document.getElementById('formulePolitesse').disabled = true;
				document.getElementById('formulePolitesse').value = "";
				if (document.getElementById('dateEngagement') != null) {
					document.getElementById('dateEngagement').disabled = true;
					document.getElementById('anchor_dateEngagement').disabled = true;
					document.getElementById('dateEngagement').style.backgroundColor = '#b3c4db';
					document.getElementById('dateEngagement').value = "";
				}			
				
		}else if (n==rentier){	
				document.getElementById('txtarea').disabled = false;
				document.getElementById('txtarea').style.backgroundColor = '#ffffff';
				document.getElementById('numeroAffilie').disabled = true;
				document.getElementById('numeroAffilie').style.backgroundColor = '#b3c4db';
				document.getElementById('numeroAffilie').value = "";
				document.getElementById('titreRentier').style.backgroundColor = '#ffffff';
				document.getElementById('titreRentier').disabled = false;
				document.getElementById('formulePolitesse').style.backgroundColor = '#ffffff';
				document.getElementById('formulePolitesse').disabled = false;
				
				if (document.getElementById('dateEngagement') != null) {
					document.getElementById('dateEngagement').disabled = true;
					document.getElementById('anchor_dateEngagement').disabled = true;
					document.getElementById('dateEngagement').style.backgroundColor = '#b3c4db';
					document.getElementById('dateEngagement').value = "";
				}					
				
		}else if (n==" "){
				document.getElementById('txtarea').disabled = true;
				document.getElementById('txtarea').style.backgroundColor = '#b3c4db';
				document.getElementById('numeroAffilie').disabled = true;
				document.getElementById('numeroAffilie').style.backgroundColor = '#b3c4db';
				document.getElementById('titreRentier').style.backgroundColor = '#b3c4db';
				document.getElementById('titreRentier').disabled = true;
				document.getElementById('formulePolitesse').style.backgroundColor = '#b3c4db';
				document.getElementById('formulePolitesse').disabled = true;
				
				if (document.getElementById('dateEngagement') != null) {
					document.getElementById('dateEngagement').disabled = true;
					document.getElementById('anchor_dateEngagement').disabled = true;
					document.getElementById('dateEngagement').style.backgroundColor = '#b3c4db';
				}
		}
			<%}%>
}

function updFieldsBoundedToCategorie(){
	catscript();
	
	document.getElementById('numeroAffilie').value = '';
	document.getElementById('langueCorrespondance').value = '';
	document.getElementById('nomAffilie').value = '';
	document.getElementById('numeroSuccursale').value = '';
	document.getElementById('numeroEmploye').value = '';	
}

function Acatscript()
{
<% if(isDateNSS) {%> 			
		var n = document.getElementById('categorie').value;
		
		var empl = '<%=IHEAnnoncesViewBean.CS_CATEGORIE_EMPLOYEUR%>';
		var indé = '<%=IHEAnnoncesViewBean.CS_CATEGORIE_INDEPENDANT%>';
		var rentier = '<%=IHEAnnoncesViewBean.CS_CATEGORIE_RENTIER%>';
		
		switch (n)
		{
			case (n=empl):						
				
				document.getElementById('txtarea').style.backgroundColor = '#b3c4db';			
				document.getElementById('titreRentier').style.backgroundColor = '#b3c4db';
				document.getElementById('formulePolitesse').style.backgroundColor = '#b3c4db';
				break	 	
				   
			case (n=indé):		
				document.getElementById('txtarea').style.backgroundColor = '#b3c4db';			
				document.getElementById('titreRentier').style.backgroundColor = '#b3c4db';
				document.getElementById('formulePolitesse').style.backgroundColor = '#b3c4db';
				document.getElementById('dateEngagement').style.backgroundColor = '#b3c4db';
				break
				
			case (n=rentier):	
				document.getElementById('numeroAffilie').style.backgroundColor = '#b3c4db';
				document.getElementById('dateEngagement').style.backgroundColor = '#b3c4db';						
				break					
					
			default:			  				
		}
			<%}%>
}



</SCRIPT>
<%if(IHEAnnoncesViewBean.CS_A_TRAITER.equals(viewBean.getStatut())&&globaz.pavo.util.CIUtil.isSpecialist(viewBean.getSession())){%>
	<ct:menuChange displayId="options" menuId="HE-attenteEnvoiSpetzDetail" showTab="options">
			<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdAnnonce()%>"/>
	</ct:menuChange>
<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="HERMES_JSP_GAZ0006_TITRE_JSP"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <%
String messageRetour = null;
boolean champNSSExiste = false;
for (int i = 0; i < viewBean.getChampsSize() ; i++){
	globaz.hermes.db.parametrage.HEAttenteEnvoiChampsViewBean line = (globaz.hermes.db.parametrage.HEAttenteEnvoiChampsViewBean) viewBean.getChampsEnvoiAt(i);
	if(messageRetour==null){
		messageRetour = line.getMessageRetour();
	}
	if(!line.isHidden()){
		String hermesKey = line.getIdChamp();
		if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isDateField(hermesKey)) {			
%>
          <tr> 
            <td width="40%">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
            <td>
            	<%	String name = ("TOSTR").concat(hermesKey);
            		String valeur = ((!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("isFieldEmpty")))||globaz.globall.util.JAUtil.isStringEmpty(request.getParameter(name)))?globaz.hermes.utils.StringUtils.unformatDate(line.getValeur()):request.getParameter(name); 
            		if(alreadySent){%>
            			<input type="text" class="disabled" readonly value="<%=globaz.hermes.utils.StringUtils.unformatDate(line.getValeur())%>" />
            		<%}else{%>
            			<ct:FWCalendarTag name='<%=name%>'  value="<%=valeur%>" doClientValidation="CALENDAR"/>
            			<script>
							document.getElementById("<%="TOSTR"+(String)hermesKey%>").onkeypress=function() {validateCharForDate(this, window.event);}
						</script>	
            		<%}%>
			</td>
          </tr>
          <%
		} else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isCustomField(hermesKey)){
			String selected = null;
			boolean isSelected = false;
			if (request.getParameterValues((String) hermesKey) != null) {
				selected = request.getParameterValues((String) hermesKey)[2];
				isSelected = globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("isFieldEmpty"));
			}
%>
          <tr> 
            <td width="40%">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
            <td> 
              <input <%=alreadySent?"class=\"disabled\" readonly":""%> type="text" name="<%=hermesKey%>" size="4" maxlength="4" value="<%=globaz.globall.util.JAUtil.isStringEmpty(request.getParameter((String)hermesKey))?line.getValeur(hermesKey,0):request.getParameterValues((String)hermesKey)[0]%>" id="frm1">
              <ct:FWLabel key="HERMES_JSP_GAZ0006_A"/> 
              <input <%=alreadySent?"class=\"disabled\" readonly":""%> type="text" name="<%=hermesKey%>" size="4" maxlength="4" value="<%=globaz.globall.util.JAUtil.isStringEmpty(request.getParameter((String)hermesKey))?line.getValeur(hermesKey,1):request.getParameterValues((String)hermesKey)[1]%>" id="frm1">
              	, <ct:FWLabel key="HERMES_JSP_GAZ0006_CHIFFRE_CLEF"/> 
              <select <%=alreadySent?"class=\"disabled\" readonly":""%> name="<%=(String)hermesKey%>">
                <option <%=!isSelected?line.isSelected(hermesKey,""):""%> value=""></option>
                <option <%=!isSelected?line.isSelected(hermesKey,"1"):""%> <%=isSelected&&selected.equals("1")?"selected":""%> value="1">1</option>
                <option <%=!isSelected?line.isSelected(hermesKey,"2"):""%> <%=isSelected&&selected.equals("2")?"selected":""%> value="2">2</option>
                <option <%=!isSelected?line.isSelected(hermesKey,"3"):""%> <%=isSelected&&selected.equals("3")?"selected":""%> value="3">3</option>
                <option <%=!isSelected?line.isSelected(hermesKey,"4"):""%> <%=isSelected&&selected.equals("4")?"selected":""%> value="4">4</option>
                <option <%=!isSelected?line.isSelected(hermesKey,"5"):""%> <%=isSelected&&selected.equals("5")?"selected":""%> value="5">5</option>
              </select>
            </td>
          </tr>
          <%
		}else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isCountryField((String)hermesKey)){
			String valeur = (!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("isFieldEmpty")))||globaz.globall.util.JAUtil.isStringEmpty(request.getParameter(line.getIdChamp()))?line.getValeur():request.getParameter(line.getIdChamp());
			%>
				<tr>
					<td class="detail">&nbsp;<%=line.getLibelleChamp()%></td>
					<td class="detail">
					<%if(alreadySent){%>
            			<input type="text" class="disabled" readonly value="<%=viewBean.getLibelleOrigine(valeur)%>" name="<%=line.getIdChamp()%>"/>
            		<%}else{%>
						<ct:FWListSelectTag name="<%=line.getIdChamp()%>"
							defaut="<%=valeur%>" 
							data="<%=viewBean.getCountries((String)hermesKey)%>"/>
					<%}%>
					</td>
				</tr>		
			<%
		}else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isNumeroAVS(hermesKey)) {
		    champNSSExiste = true;
%>
          <tr> 
            <td width="40%">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
            <td> 
              <%if(alreadySent){
            	 	String nssOrNAVS =  (!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("isFieldEmpty")))||globaz.globall.util.JAUtil.isStringEmpty(request.getParameter(line.getIdChamp()))?globaz.commons.nss.NSUtil.formatAVSUnknown(line.getValeur()):request.getParameter(line.getIdChamp());
            		String nss = globaz.hermes.utils.HENNSSUtils.convertNegatifToNNSS(nssOrNAVS);
            		nss = globaz.commons.nss.NSUtil.formatAVSUnknown(nss);
            	%>
              	<!--  <input class="disabled" readonly name="<%=line.getIdChamp()%>" value="<%=(!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("isFieldEmpty")))||globaz.globall.util.JAUtil.isStringEmpty(request.getParameter(line.getIdChamp()))?globaz.commons.nss.NSUtil.formatAVSUnknown(line.getValeur()):request.getParameter(line.getIdChamp())%>"> -->
              	<input class="disabled" readonly name="<%=line.getIdChamp()%>" value="<%=nss%>">
              <%}else{
              		String onChange = "updateForm(tag);"; 
              		String select = servletContext + mainServletPath + "Root/ci_select.jsp"; 
              		String nssOrNAVS =  (!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("isFieldEmpty")))||globaz.globall.util.JAUtil.isStringEmpty(request.getParameter(line.getIdChamp()))?globaz.commons.nss.NSUtil.formatAVSUnknown(line.getValeur()):request.getParameter(line.getIdChamp());
              		String nss = globaz.hermes.utils.HENNSSUtils.convertNegatifToNNSS(nssOrNAVS);
              		
              		String isNNSS = globaz.hermes.utils.HENNSSUtils.isNNSSFromNumAVS(nssOrNAVS);
              		 if (isNNSS.equals("true")) nss = globaz.commons.nss.NSUtil.formatWithoutPrefixe(nss,true);
              	%>
              	<nss:nssPopup name="<%=line.getIdChamp()%>"  value="<%=nss%>" 
	            	onChange="<%=onChange%>" onFailure="<%=onChange%>" cssclass="libelle" jspName="<%=select%>" 
	            	avsAutoNbrDigit="11" nssAutoNbrDigit="10"  avsMinNbrDigit="5" nssMinNbrDigit="8" newnss="<%=isNNSS%>" />			
              <%}%>

              <!--ancien champs <input <%=alreadySent?"class=\"disabled\" readonly":""%> name="<%=line.getIdChamp()%>" value="<%=(!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("isFieldEmpty")))||globaz.globall.util.JAUtil.isStringEmpty(request.getParameter(line.getIdChamp()))?globaz.commons.nss.NSUtil.formatAVSUnknown(line.getValeur()):request.getParameter(line.getIdChamp())%>" size="<%=line.getLongueur(5)%>" maxlength="<%=line.getLongueur(3)%>">-->
            </td>
            <%
		} else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isCustomSelectField((String) hermesKey)){
		    	String valeur = (!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("isFieldEmpty")))||globaz.globall.util.JAUtil.isStringEmpty(request.getParameter(hermesKey))?line.getValeur():request.getParameter(hermesKey);
%>
          <tr> 
            <td width="40%">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
            <td>
            	<%if(alreadySent){%>
		            	<input type="text" name="<%=((String)hermesKey)%>" class="disabled" readonly value="<%=globaz.hermes.db.gestion.HEAnnoncesViewBean.getCustomSelectFieldValue(valeur,(String)hermesKey,languePage)%>" />
		        <%}else{%>
            			<ct:FWListSelectTag name="<%=((String)hermesKey)%>" defaut="<%=valeur%>" data="<%=globaz.hermes.db.gestion.HEAnnoncesViewBean.getCustomSelectFieldValues((String)hermesKey,languePage)%>"/>
            	<%}%>
            </td>
          </tr>
          <%}else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isReferenceInterne((String) hermesKey)){
				if("true".equals(viewBean.getSession().getApplication().getProperty("service.input"))){
					String[] refTab = request.getParameterValues((String) hermesKey);
					String 	ref;
					if(refTab == null){
						ref	= line.getValeur();
					}else if(refTab.length==0){
						ref = line.getValeur();
					}else if(refTab.length==1){
						ref = globaz.globall.util.JAUtil.isStringEmpty(refTab[0])?line.getValeur():refTab[0];
					}else if(refTab.length==2){
						if(globaz.globall.util.JAUtil.isStringEmpty(refTab[0])){
							ref = line.getValeur();
						}else{
							ref = refTab[0]+"/"+refTab[1];
						}
					}else{
						ref = line.getValeur();	
					}
					if(ref!=null){
						if(ref.indexOf("/")==0){
							if(ref.length()>1){
								ref1 = ref.substring(1,ref.length());	
							}
						}else{
							if(ref.indexOf("/")>0){
								ref1 = ref.substring(0,ref.indexOf("/"));
								ref2 = ref.substring(ref.indexOf("/")+1,ref.length());
							}else{
								ref1 = ref;
							}
						}
					}
					%>
					<tr>
						<td width="40%">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
						<td>
							<input <%=alreadySent?"class=\"disabled\" readonly":""%> type="text" name="<%=hermesKey%>" value="<%=ref1%>" size="4" maxlength="4" id="frm1" onchange="changeName(this)" onkeydown="checkChar(this)">
							-
							<input type="text" name="" value="/" size="1" maxlength="1" id="frm1" class="disabled" readonly tabindex="-1">
							-
							<input <%=alreadySent?"class=\"disabled\" readonly":""%> type="text" name="<%=hermesKey%>" value="<%=ref2%>" size="18" maxlength="16" id="frm1" onchange="changeName(this)" onkeydown="checkChar(this)">
						</td>
					</tr>
				<%}else{%>
					<tr>
						<td width="40%">&nbsp;<%=line.getLibelleChamp()%></td>
						<td>
							<input <%=alreadySent?"class=\"disabled\" readonly":""%> name="<%=hermesKey%>" type="text"
							size="<%=line.getLongueur(hermesKey)%>"
							value="<%=(!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("isFieldEmpty")))||globaz.globall.util.JAUtil.isStringEmpty(request.getParameter(hermesKey))?line.getValeur():request.getParameter(hermesKey)%>"
								maxlength="<%=line.getLongueur(hermesKey)%>" id="frm1" onKeyUp="checkChar(this)" onKeyDown="checkChar(this)" onchange="changeName(this)">
						</td>
					</tr>
				<%}		
			}else if(((String)hermesKey).equals(IHEAnnoncesViewBean.MOTIF_ANNONCE)){%>
	          <tr> 
	            <td width="40%">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
	            <td> 		
					<input tabindex="-1" type="text" name="<%=hermesKey%>" value="<%=line.getValeur()%>" class="disabled" readonly id="frm1" size="<%=line.getLongueur(2)%>"/>
					-
	            	<input tabindex="-1" type="text" value="<%=viewBean.getLibelleMotif()%>" class="disabled" readonly id="frm1" size="70"/>
	            </td>
	          </tr>
          		<%if("97".equals(viewBean.getMotifArc()) && "true".equals(viewBean.getSession().getApplication().getProperty("adresse.input"))){
          			bButtonUpdate = objSession.hasRight(userActionUpd, globaz.framework.secure.FWSecureConstants.UPDATE);
          		%>
							<tr>
								<td width="40%" valign="top">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0006_TITRE_PERSONNE"/>&nbsp;:&nbsp;</td>
								<td> 
									<ct:FWListSelectTag name="titreAssure"
										defaut="<%=viewBean.getTitreAssure()%>" 
										data="<%=viewBean.getListeTitre()%>"/>
								</td>
							</tr>
							<tr id="wantPolitesse">
								<td width="400" valign="top">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0006_FORMULE_DE_POLITESSE"/>&nbsp;:&nbsp;</td>
								<td>
									<input name="formulePolitesse" id="formulePolitesse" size="39" type="text" value="<%=viewBean.getFormulePolitesse()%>" maxlength="40"/> 
								</td>
							</tr>
							<tr>
								<td width="40%" valign="top">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0006_NOM_PRENOM_ADRESSE_ASSURE"/>&nbsp;:&nbsp;</td>
								<td> 
									<TEXTAREA name="adresseAssure" rows="4" cols="40" id="frm1"><%=viewBean.getAdresseAssure()%></TEXTAREA>
								</td>
							</tr>
							<tr>
								<td width="40%" valign="top">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0006_LANGUE_DE_LA_CORRESPONDANCE"/>&nbsp;:&nbsp;</td>
								<td> 
									<%
										String langue = JadeStringUtil.isEmpty(viewBean.getLangueCorrespondance())?TITiersViewBean.CS_FRANCAIS:viewBean.getLangueCorrespondance();
									%>
									<ct:FWCodeSelectTag name="langueCorrespondance" codeType="PYLANGUE" wantBlank="true" defaut="<%=langue%>" except="<%=except%>"/> 
								</td>
							</tr>		
				<%}%>
          		<%if((globaz.hermes.db.gestion.HEAnnoncesViewBean.isMotifCA(viewBean.getMotifArc())
          			||
          			globaz.hermes.db.gestion.HEAnnoncesViewBean.isMotifForDeclSalaire(viewBean.getMotifArc()))
          			|| isMotifCert
          			&& "true".equals(viewBean.getSession().getApplication().getProperty("affilie.input"))
          			){
          			
          			String defaultcat = viewBean.getCategorie();
          			
          			%>
          			
          			<!--Categorie -->
          			<%
          				if(isMotifCert && isDateNSS) {
          					          				
          			%>		
          			<tr>
						<td width="40%">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0006_CATEGORIE"/>&nbsp;:&nbsp;</td>
						<td>
								<ct:FWListSelectTag name="categorie" 
									defaut="<%=defaultcat%>" 
									data="<%=viewBean.getListeCat()%>"/>
								<script type="text/javascript">
								document.getElementById("categorie").onclick = catscript;
								document.getElementById("categorie").onchange = updFieldsBoundedToCategorie;
								</script>
						</td>
					</tr>
          		    <%}%>
							<tr>
								<td width="40%">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0006_NUMERO_AFFILIE"/>&nbsp;:&nbsp;<input type="hidden" id="chkMemoriserNumeroAffilie" name="chkMemoriserNumeroAffilie" value="<%=request.getParameter("chkMemoriserNumeroAffilie")%>"></td>
								<td> 
									<%
										String defaultNumeroAffilie=viewBean.getNumeroAffilieFormatte();
										if(alreadySent){%>
            								<input type="text" class="disabled" readonly value="<%=defaultNumeroAffilie%>" name="numeroAffilie""/>
            							<%}else{
											String jspLocation = servletContext + mainServletPath + "Root/ti_select.jsp";
										%>
										<ct:FWPopupList onChange="updateEmployeurPartenaire(tag);"
											onFailure="setNotFound(tag);" name="numeroAffilie" size="15"
											jspName="<%=jspLocation%>" minNbrDigit="3"
											autoNbrDigit="-1" forceSelection="true" maxlength="14" value="<%=defaultNumeroAffilie%>"/>
										<%}%>
											<script type="text/javascript">									
											document.getElementById("numeroAffilie").onclick=updateJspNameCat;
											document.getElementById("numeroAffilie").onchange=updateJspNameCat;
											</script>	
										<input type="text" name="nomAffilie" size="36" class="disabled" readonly value="<%=!globaz.globall.util.JAUtil.isStringEmpty(defaultNumeroAffilie)?viewBean.getAffilieNom():""%>" tabindex="-1">
								</td>
							</tr>
							<tr>
								<td width="40%" valign="top">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0006_LANGUE_CORRESPONDANCE"/>&nbsp;:&nbsp;</td>
									<td>
										<%
										String defaultLangue = "";
										defaultLangue = viewBean.getLangueCorrespondance();
										%>
										<ct:FWCodeSelectTag codeType="PYLANGUE" name="langueCorrespondance" defaut="<%=defaultLangue%>" wantBlank="false" except="<%=except%>"/>
										
								</td>
							</tr>										
							<tr>
								<td width="40%">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0006_NUMERO_SUCCURSALE_DEPARTEMENT"/>&nbsp;:&nbsp;</td>
								<td>
									<%if(alreadySent){%>
            							<input type="text" size="15" class="disabled" readonly value="<%=viewBean.getNumeroSuccursale()%>" name="numeroSuccursale"/>
            						<%}else{%> 
            							<input type="text" size="15" value="<%=viewBean.getNumeroSuccursale()%>" name="numeroSuccursale"/>
									<%}%>
								</td>
							</tr>
							<tr>
								<td width="40%">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0006_NUMERO_EMPLOYE"/>&nbsp;:&nbsp;</td>
								<td> 
									<%if(alreadySent){%>
            							<input type="text" size="15" class="disabled" readonly value="<%=viewBean.getNumeroEmploye()%>" name="numeroEmploye"/>
            						<%}else{%> 
            							<input type="text" size="15" value="<%=viewBean.getNumeroEmploye()%>" name="numeroEmploye"/>
									<%}%>
								</td>
							</tr>
				<%}
					if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isMotifForDeclSalaire(viewBean.getMotifArc())
          				&& "true".equals(viewBean.getSession().getApplication().getProperty("affilie.input")) || isMotifCert){
          				String defaut = globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getDateEngagement())?"":viewBean.getDateEngagement();
          				%>
          				<tr>
							<td width="40%">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0006_DATE_ENGAGEMENT"/>&nbsp;:&nbsp;</td>
							<td>
								<%if(alreadySent){%>
									<input type="text" class="disabled" readonly value="<%=defaut%>" name="dateEngagement""/>
								<%}else{%>
									<ct:FWCalendarTag name="dateEngagement" value="<%=defaut%>" doClientValidation="CALENDAR" />
								<%}%>
							</td>
						</tr>
						
				
				
				<!--Adresse + Titre rentier -->	
				<%
						if(isMotifCert && isDateNSS) {
				%>		
				<tr>
					<td width="40%" valign="top">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0006_TITRE_PERSONNE"/>&nbsp;:&nbsp;</td>
					<td> 
						<%String titre = viewBean.getTitreRentier();%>
						<ct:FWListSelectTag name="titreRentier"
							defaut="<%=titre%>" 
							data="<%=viewBean.getListeTitre()%>"/>
					</td>
				</tr>		
				<tr id="wantPolitesse">
					<td width="400" valign="top">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0006_FORMULE_DE_POLITESSE"/>&nbsp;:&nbsp;</td>
					<td>
						<input name="formulePolitesse" id="formulePolitesse" size="39" type="text" value="<%=viewBean.getFormulePolitesse()%>" maxlength="40"/> 
					</td>
				</tr>						
				<tr>
					<td width="40%">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0006_NOM_ET_ADRESSE"/>&nbsp;:&nbsp;</td>
					<%
					
					String adr = viewBean.getAdresseRentier();
					%>
					
					<td>
						<textarea disabled="true" name="adresseRentier" id="txtarea" rows="4" cols="40" class="forceDisable"  disabled="true" ><%=adr%></textarea> 
					</td>
				</tr>
				<%} %>
			
  			<%}						
				} else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isReadOnlyField((String) hermesKey)){
			%>

          <tr> 
            <td width="40%">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
            <td> 
              <input type="text" name="<%=(String)hermesKey%>" tabindex="-1" size="<%=line.getLongueur(2)%>" maxlength="<%=line.getLongueur()%>" class="disabled" readonly value="<%=line.getValeur()%>" >
            </td>
          </tr>
          <%
			} else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isNameField((String) hermesKey)){
%>
          <tr> 
            <td class="detail">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
            <td class="detail">
              <input  <%=alreadySent?"class=\"disabled\" readonly":""%> name="<%=line.getIdChamp()%>" value="<%=(!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("isFieldEmpty")))||globaz.globall.util.JAUtil.isStringEmpty(request.getParameter(line.getIdChamp()))?line.getValeur():request.getParameter(line.getIdChamp())%>" size="<%=line.getLongueur(2)%>" maxlength="<%=line.getLongueur()%>" onKeyUp="checkKey(this)" onKeyDown="checkKey(this)" onKeyUp="checkKey(this)" onKeyDown="checkKey(this)" onChange="changeName(this);nameWithHyphen(this);">
            </td>
          </tr>
          <%
			} else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isCustomInputField((String) hermesKey)){
		  %>
          <tr> 
            <td class="detail">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
            <td class="detail"> 
              <input  <%=alreadySent?"class=\"disabled\" readonly":""%> name="<%=line.getIdChamp()%>" value="<%=(!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("isFieldEmpty")))||globaz.globall.util.JAUtil.isStringEmpty(request.getParameter(line.getIdChamp()))?line.getValeur((String)hermesKey):request.getParameter(line.getIdChamp())%>" 
              	size="<%=line.getLongueur((String)hermesKey)%>" maxlength="<%=line.getLongueur((String)hermesKey)%>"
              	onkeypress="javascript:validatePartDateOnKeyPressed(this,window.event)"
				onchange="javascript:validatePartDate(this)">
            </td>
          </tr>
          <%			
			} else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isCurrencyField((String) hermesKey)){
%>
          <tr>
            <td class="detail">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
            <td class="detail"> 
              <input  <%=alreadySent?"class=\"disabled\" readonly":""%> name="<%=line.getIdChamp()%>" value="<%=viewBean.isRevenuCache()?line.getSession().getLabel("HERMES_10001"):line.getValeur((String)hermesKey)%>" size="<%=line.getLongueur(2)%>" maxlength="<%=line.getLongueur((String)hermesKey)%>">
            </td>
          </tr>
          <%			
			} else{
%>
          <tr> 
            <td class="detail">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
            <td class="detail"> 
              <input  <%=alreadySent||line.isReadOnly()?"class=\"disabled\" readonly":""%> name="<%=line.getIdChamp()%>" onchange="changeName(this);upperCase(this);" value="<%=(!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("isFieldEmpty")))||globaz.globall.util.JAUtil.isStringEmpty(request.getParameter(line.getIdChamp()))?line.getValeur():request.getParameter(line.getIdChamp())%>" size="<%=line.getLongueur(2)%>" maxlength="<%=line.getLongueur()%>" />
            </td>
          </tr>
          <%	
		}
	}
}	
		  %>
		<% // Ne concerne que les motifs 11 et 31
		if((MOTIF_11.equals(motif) || MOTIF_31.equals(motif)) && champNSSExiste){ %>
		<tr>
			<td width="400">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0004_AJOUT_ARC_61"/></td>
			<td>
				<INPUT type="checkbox" value="on" id="idChkCreerArc61" name="isArc61Cree" <%=viewBean.getIsArc61Cree().booleanValue()?"CHECKED":""%>>&nbsp;
			</td>
		</tr>
		
		<% }%>
		<tr>
		  <td>
		  	<input type="hidden" name="modeSaisie" value="<%=request.getParameter("modeSaisie")==null?"false":request.getParameter("modeSaisie")%>">
		  	<input type="hidden" name="critere" value="<%=request.getParameter("critere")%>">
			<input type="hidden" name="motif" value="<%=request.getParameter("motif")%>">
			<input type="hidden" name="numAVS" value="<%=request.getParameter("numAVS")%>">
			<input type="hidden" name="modeCancel" value="">
		  	
		  	<%if("true".equals(viewBean.getSession().getApplication().getProperty("service.input"))){
					%>
					<%--<input type="hidden" name="<%=globaz.hermes.db.gestion.HEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE%>" value="<%=ref1%>">
					<input type="hidden" name="<%=globaz.hermes.db.gestion.HEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE%>" value="<%=ref2%>">
					<input type="hidden" name="numeroAffilie" value="<%=request.getParameter("numeroAffilie")%>">--%>
				<%}else{%>
					<input type="hidden" name="<%=IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE%>" value="<%=request.getParameter(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE)%>">
				<%}%>		
		  </td>
		</tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<script>
document.getElementById('btnCan').onclick=cancel;
</script>
<% if(IHEAnnoncesViewBean.CS_A_TRAITER.equals(viewBean.getStatut())){%>
<ct:menuChange displayId="options" menuId="HE-attenteEnvoiSpetzDetail" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdAnnonce()%>"/>
</ct:menuChange>
<%} %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>