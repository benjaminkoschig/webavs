<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
if(session.getAttribute("bouton")!=null){
	if(session.getAttribute("bouton")=="false"){
		bButtonUpdate = false;
		bButtonDelete = false;
	}
}
bButtonDelete = false;
idEcran="CCP1017";
globaz.phenix.db.communications.CPSedexDonneesBase viewBean = (globaz.phenix.db.communications.CPSedexDonneesBase)session.getAttribute ("viewBean");
boolean hasJournal = true;
key="globaz.phenix.db.communications.CPCommunicationFiscaleRetourSEDEXViewBean-idRetour"+viewBean.getIdRetour();
%>

<%@page import="globaz.phenix.db.communications.CPLienCommunicationsPlausiManager"%>
<%@page import="globaz.phenix.db.communications.CPLienCommunicationsPlausi"%>
<%@page import="globaz.phenix.interfaces.ICommunicationRetour"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/CodeSystemPopup.js"></SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Cotisation - Données fiscales Détail"
function add() {
	document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.ajouter"
}
function upd() {
}
function validate() {


	state = validateFields(); 

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.ajouter";
	else
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.modifierCustom";
	return (state);
}
function cancel() {
	document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.chercher";
}
function del() {
	if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.supprimer";
		document.forms[0].submit();
	}
}

function afficherDonneesPrivees() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesPrivees";
		document.forms[0].submit();
}

function afficherDonneesCommunication() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesCommunication";
		document.forms[0].submit();
}

function afficherContribuable() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherContribuable";
		document.forms[0].submit();
}

function afficherDonneesBase() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesBase";
		document.forms[0].submit();
}

function afficherDonneesCommerciales() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesCommerciales";
		document.forms[0].submit();
}

function afficherConjoint() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherConjoint";
		document.forms[0].submit();
}
function afficherRenteAVSWIRR() {
	document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherRenteAVSWIRR";
	document.forms[0].submit();
}
function init(){}

window.onload = document.write("<span id='infobulle' style='position:absolute;visibility:hidden;padding:3px;'>&nbsp;</span>");

function infobulle(corps,event){
   var couleur_fond = 'white';
   var couleur_texte = 'black';
   var couleur_bordure = 'gray';
   var type_bordure = 'solid'; //(solid dashed dotted double)
   var taille_bordure = '1px'; //px
   
   document.getElementById('infobulle').style.color = couleur_texte;
   document.getElementById('infobulle').style.backgroundColor = couleur_fond;
   document.getElementById('infobulle').style.borderColor = couleur_bordure;
   document.getElementById('infobulle').style.borderStyle = type_bordure;
   document.getElementById('infobulle').style.borderWidth = taille_bordure;
   document.getElementById('infobulle').innerHTML = corps;
   document.getElementById('infobulle').style.visibility = 'visible';
   document.getElementById('infobulle').style.left = event.clientX+10+"px";
   document.getElementById('infobulle').style.top = event.clientY+20+"px";
}

function infobulle_cache(){
   document.getElementById('infobulle').style.visibility = 'hidden';
}

/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Steuermeldungsdaten<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<div id="tabs"> 
		<ul>
  			<li><a onclick="afficherDonneesCommunication()" href="#">Steuermeldungsdaten</a></li>
  			<li><a onclick="afficherContribuable()" href="#">Steuer</a></li>
  			<li><a class="selected">Basisdaten</a></li>
  			<li><a onclick="afficherDonneesPrivees()" href="#">Privatdaten</a></li>
  			<li><a onclick="afficherDonneesCommerciales()" href="#">Geschaeftsdaten</a></li>
  			<li><a onclick="afficherConjoint()" href="#">Ehepartner</a></li>
  			<li><a onclick="afficherRenteAVSWIRR()" href="#">AHV Einkommen</a></li> 	
		</ul>
		</div>
	
		<TR style="font-size : 14px;">
			<TD> &nbsp;</TD>
	     	<TD align="center" class="libelleLongDisabled">
				<%=viewBean.getSession().getApplication().getLabel("CONTRIBUABLE", "DE")%>
			 </TD>
			 <TD width="50" nowrap="nowrap">
				&nbsp;
			 </TD>
	     	<TD align="center" class="libelleLongDisabled">
			   <%=viewBean.getSession().getApplication().getLabel("CONJOINT", "DE")%>
	     	</TD>
	     </TR>
	     
		  <TR>
			<TD>
				<INPUT type="text" tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("EMPLOYMENTINCOME", "DE")%>" type="text" class="libelleLongDisabled" readonly>
				<IMG onMouseOver="infobulle('<%=viewBean.getSession().getLabel("INFO_EMPLOYMENTINCOME")%>',event)" onMouseOut="infobulle_cache()" src="<%=request.getContextPath()%>/images/small_info.png" border="0"> 
            </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="employmentIncome" value="<%=viewBean.getEmploymentIncome()%>" class="libelleLong">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="employmentIncomeCjt" value="<%=viewBean.getEmploymentIncomeCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("INCOMEFROMSELFEMPLOYMENT", "DE")%>" type="text" class="libelleLongDisabled" readonly>
				<IMG onMouseOver="infobulle('<%=viewBean.getSession().getLabel("INFO_INCOMEFROMSELFEMPLOYMENT")%>',event)" onMouseOut="infobulle_cache()" src="<%=request.getContextPath()%>/images/small_info.png" border="0">
			</TD>
	     	
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="incomeFromSelfEmployment" value="<%=viewBean.getIncomeFromSelfEmployment()%>" class="libelleLong">
			 </TD>
			 <TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="incomeFromSelfEmploymentCjt" value="<%=viewBean.getIncomeFromSelfEmploymentCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PENSIONINCOME", "DE")%>" type="text" class="libelleLongDisabled" readonly>
				<IMG onMouseOver="infobulle('<%=viewBean.getSession().getLabel("INFO_PENSIONINCOME")%>',event)" onMouseOut="infobulle_cache()" src="<%=request.getContextPath()%>/images/small_info.png" border="0">
            </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="pensionIncome"  value="<%=viewBean.getPensionIncome()%>" class="libelleLong">
			</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="pensionIncomeCjt"  value="<%=viewBean.getPensionIncomeCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("MAININCOMEINAGRICULTURE", "DE")%>" type="text" class="libelleLongDisabled" readonly>
				<IMG onMouseOver="infobulle('<%=viewBean.getSession().getLabel("INFO_MAININCOMEINAGRICULTURE")%>',event)" onMouseOut="infobulle_cache()" src="<%=request.getContextPath()%>/images/small_info.png" border="0">
			 </TD>
			  <TD >
			    <INPUT type="text" style="text-align : right;" name="mainIncomeInAgriculture"  value="<%=viewBean.getMainIncomeInAgriculture()%>" class="libelleLong">
			</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="mainIncomeInAgricultureCjt"  value="<%=viewBean.getMainIncomeInAgricultureCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("CAPITAL", "DE")%>" type="text" class="libelleLongDisabled" readonly>
				<IMG onMouseOver="infobulle('<%=viewBean.getSession().getLabel("INFO_CAPITAL")%>',event)" onMouseOut="infobulle_cache()" src="<%=request.getContextPath()%>/images/small_info.png" border="0">
			 </TD>
			 <TD >
			    <INPUT type="text" style="text-align : right;" name="capital"  value="<%=viewBean.getCapital()%>" class="libelleLong">
			</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="capitalCjt"  value="<%=viewBean.getCapitalCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("ASSETS", "DE")%>" type="text" class="libelleLongDisabled" readonly>
				<IMG onMouseOver="infobulle('<%=viewBean.getSession().getLabel("INFO_ASSETS")%>',event)" onMouseOut="infobulle_cache()" src="<%=request.getContextPath()%>/images/small_info.png" border="0">
			 </TD>
			 <TD >
			    <INPUT type="text" style="text-align : right;" name="assets"  value="<%=viewBean.getAssets()%>" class="libelleLong">
			</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="assetsCjt"  value="<%=viewBean.getAssetsCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("NONDOMESTICINCOMEPRESENT", "DE")%>" type="text" class="libelleLongDisabled" readonly>
				<IMG onMouseOver="infobulle('<%=viewBean.getSession().getLabel("INFO_NONDOMESTICINCOMEPRESENT")%>',event)" onMouseOut="infobulle_cache()" src="<%=request.getContextPath()%>/images/small_info.png" border="0">
			 </TD>
			 <TD >
			    <INPUT type="text" style="text-align : right;" name="nonDomesticIncomePresent"  value="<%=viewBean.getNonDomesticIncomePresent()%>" class="libelleLong">
			</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="nonDomesticIncomePresentCjt"  value="<%=viewBean.getNonDomesticIncomePresentCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PURCHASINGLPP", "DE")%>" type="text" class="libelleLongDisabled" readonly>
				<IMG onMouseOver="infobulle('<%=viewBean.getSession().getLabel("INFO_PURCHASINGLPP")%>',event)" onMouseOut="infobulle_cache()" src="<%=request.getContextPath()%>/images/small_info.png" border="0">
            </TD>
            <TD >
			    <INPUT type="text" style="text-align : right;" name="purchasingLPP"  value="<%=viewBean.getPurchasingLPP()%>" class="libelleLong">
			</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="purchasingLPPCjt"  value="<%=viewBean.getPurchasingLPPCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("OASIBRIDGINGPENSION", "DE")%>" type="text" class="libelleLongDisabled" readonly>
				<IMG onMouseOver="infobulle('<%=viewBean.getSession().getLabel("INFO_OASIBRIDGINGPENSION")%>',event)" onMouseOut="infobulle_cache()" src="<%=request.getContextPath()%>/images/small_info.png" border="0">
            </TD >
			 <TD>
			    <INPUT type="text" style="text-align : right;" name="OASIBridgingPension"  value="<%=viewBean.getOASIBridgingPension()%>" class="libelleLong">
			 </TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="OASIBridgingPensionCjt"  value="<%=viewBean.getOASIBridgingPensionCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
        
        
       <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="CP-communicationFiscaleRetour" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="idRetour" value="<%=viewBean.getIdRetour()%>" checkAdd="no"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>