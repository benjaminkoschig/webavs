<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.phenix.toolbox.CPToolBox"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
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
globaz.phenix.db.communications.CPSedexConjoint viewBean = (globaz.phenix.db.communications.CPSedexConjoint)session.getAttribute ("viewBean");
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
	document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.ajouter";
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
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
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
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Données fiscales<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<div id="tabs"> 
		<ul>
  			<li><a  onclick="afficherDonneesCommunication()" href="#">Données communication</a></li>
  			<li><a  onclick="afficherContribuable()" href="#">Contribuable</a></li>
  			<li><a  onclick="afficherDonneesBase()" href="#">Données de base</a></li>
  			<li><a  onclick="afficherDonneesPrivees()" href="#">Données privées</a></li>
  			<li><a  onclick="afficherDonneesCommerciales()" href="#">Données commerciales</a></li>
  			<li><a  class="selected">Conjoint</a></li>
  			<li><a onclick="afficherRenteAVSWIRR()" href="#">Rente(s) AVS</a></li> 		
		</ul>
		</div>
		
		<TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SP_VN", "FR")%>" type="text" class="libelleLongDisabled" readonly>

			    <INPUT type="text" name="vn" value="<%=viewBean.getVn()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SP_PERSONIDCATEGORY", "FR")%>" type="text" class="libelleLongDisabled" readonly>

			    <INPUT type="text" name="personIdCategory" value="<%=viewBean.getPersonIdCategory()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SP_PERSONID", "FR")%>" type="text" class="libelleLongDisabled" readonly>

			    <INPUT type="text" name="personIdy" value="<%=viewBean.getPersonId()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SP_OFFICIALNAME", "FR")%>" type="text" class="libelleLongDisabled" readonly>

			    <INPUT type="text" name="officialName" value="<%=viewBean.getOfficialName()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SP_FIRSTNAME", "FR")%>" type="text" class="libelleLongDisabled" readonly>

			    <INPUT type="text" name="firstName" value="<%=viewBean.getFirstName()%>" class="libelleLong">	   
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SP_SEX", "FR")%>" type="text" class="libelleLongDisabled" readonly>

			    <INPUT type="text" name="sex" value="<%=CPToolBox.getLibSexe(viewBean.getSession(), viewBean.getSex())%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SP_YEARMONTHDAY", "FR")%>" type="text" class="libelleLongDisabled" readonly>

			    <INPUT type="text" name="yearMonthDay" value="<%=viewBean.getYearMonthDay()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SP_STREET", "FR")%>" type="text" class="libelleLongDisabled" readonly>

			    <INPUT type="text" name="street" value="<%=viewBean.getStreet()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SP_HOUSENUMBER", "FR")%>" type="text" class="libelleLongDisabled" readonly>

			    <INPUT type="text" name="houseNumber" value="<%=viewBean.getHouseNumber()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SP_TOWN", "FR")%>" type="text" class="libelleLongDisabled" readonly>

			    <INPUT type="text" name="town" value="<%=viewBean.getTown()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SP_SWISSZIPCODE", "FR")%>" type="text" class="libelleLongDisabled" readonly>

			    <INPUT type="text" name="swissZipCode" value="<%=viewBean.getSwissZipCode()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SP_COUNTRY", "FR")%>" type="text" class="libelleLongDisabled" readonly>

			    <INPUT type="text" name="country" value="<%=viewBean.getCountry()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SP_MARITALSTATUS", "FR")%>" type="text" class="libelleLongDisabled" readonly>

			    <INPUT type="text" name="maritalStatus" value="<%=CPToolBox.getLibEtatCivil(viewBean.getSession(), viewBean.getMaritalStatus())%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SP_DATEOFMARITALSTATUS", "FR")%>" type="text" class="libelleLongDisabled" readonly>

			    <INPUT type="text" name="dateOfMaritalStatus" value="<%=viewBean.getDateOfMaritalStatus()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SP_DATEOFENTRY", "FR")%>" type="text" class="libelleLongDisabled" readonly>

			    <INPUT type="text" name="dateOfEntry" value="<%=viewBean.getDateOfEntry()%>" class="libelleLong">
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