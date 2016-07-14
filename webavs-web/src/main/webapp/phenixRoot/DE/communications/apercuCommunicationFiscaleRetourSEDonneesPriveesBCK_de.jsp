<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.phenix.db.communications.CPSedexDonneesPrivees"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
bButtonUpdate = false;
bButtonDelete = false;
idEcran="CCP1017";
CPSedexDonneesPrivees viewBean = (CPSedexDonneesPrivees)session.getAttribute ("viewBean");
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
  			<li><a onclick="afficherDonneesBase()" href="#">Basisdaten</a></li>
  			<li><a class="selected">Privatdaten</a></li>
  			<li><a onclick="afficherDonneesCommerciales()" href="#">Geschaeftsdaten</a></li>
  			<li><a onclick="afficherConjoint()" href="#">Ehepartner</a></li>
  			<li><a onclick="afficherRenteAVSWIRR()" href="#">Rente(s) AVS</a></li> 	
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
			    <INPUT type="hidden"  name="isForBackup" value="<%=viewBean.isForBackup()%>">
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PENSIONS", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="pensions" value="<%=viewBean.getPensions()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="pensionsCjt" value="<%=viewBean.getPensionsCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
        <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PENSIONSPILLAR1", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="pensionsPillar1" value="<%=viewBean.getPensionsPillar1()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="pensionsPillar1Cjt" value="<%=viewBean.getPensionsPillar1Cjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PENSIONSPILLAR2", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="pensionsPillar2" value="<%=viewBean.getPensionsPillar2()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="pensionsPillar2Cjt" value="<%=viewBean.getPensionsPillar2Cjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PENSIONSPILLAR3A", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="pensionsPillar3a" value="<%=viewBean.getPensionsPillar3a()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="pensionsPillar3aCjt" value="<%=viewBean.getPensionsPillar3aCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	   
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PENSIONSPILLAR3B", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="pensionsPillar3b" value="<%=viewBean.getPensionsPillar3b()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="pensionsPillar3bCjt" value="<%=viewBean.getPensionsPillar3bCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("ANNUITIES", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="annuities" value="<%=viewBean.getAnnuities()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="annuitiesCjt" value="<%=viewBean.getAnnuitiesCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("OTHERPENSIONS", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="otherPensions" value="<%=viewBean.getOtherPensions()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="otherPensionsCjt" value="<%=viewBean.getOtherPensionsCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("MILITARYINSURANCEPENSIONS", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="militaryInsurancePensions" value="<%=viewBean.getMilitaryInsurancePensions()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="militaryInsurancePensionsCjt" value="<%=viewBean.getMilitaryInsurancePensionsCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	      <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PERDIEMALLOWANCE", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="perDiemAllowance" value="<%=viewBean.getPerDiemAllowance()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="perDiemAllowanceCjt" value="<%=viewBean.getPerDiemAllowanceCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
         <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("MAINTENANCECONTRIBUTION", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="maintenanceContribution" value="<%=viewBean.getMaintenanceContribution()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="maintenanceContributionCjt" value="<%=viewBean.getMaintenanceContributionCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("CHILDALLOWANCE", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="childAllowance" value="<%=viewBean.getChildAllowance()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="childAllowanceCjt" value="<%=viewBean.getChildAllowanceCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PATENTLICENSE", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="patentLicense" value="<%=viewBean.getPatentLicense()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="patentLicenseCjt" value="<%=viewBean.getPatentLicenseCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	   <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("RESIDENCYENTITLEMENT", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="residencyEntitlement" value="<%=viewBean.getResidencyEntitlement()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="residencyEntitlementCjt" value="<%=viewBean.getResidencyEntitlementCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SECURITIES", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="securities" value="<%=viewBean.getSecurities()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="securitiesCjt" value="<%=viewBean.getSecuritiesCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	    <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("CASH", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="cash" value="<%=viewBean.getCash()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="cashCjt" value="<%=viewBean.getCashCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("LIFEINSURANCE", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="lifeInsurance" value="<%=viewBean.getLifeInsurance()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="lifeInsuranceCjt" value="<%=viewBean.getLifeInsuranceCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("MOTORVEHICLE", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="motorVehicle" value="<%=viewBean.getMotorVehicle()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="motorVehicleCjt" value="<%=viewBean.getMotorVehicleCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("INHERITANCE", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="inheritance" value="<%=viewBean.getInheritance()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="inheritanceCjt" value="<%=viewBean.getInheritanceCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	      <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("OTHERASSETS", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="otherAssets" value="<%=viewBean.getOtherAssets()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="otherAssetsCjt" value="<%=viewBean.getOtherAssetsCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	      <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("REALESTATEPROPERTIES", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="realEstateProperties" value="<%=viewBean.getRealEstateProperties()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="realEstatePropertiesCjt" value="<%=viewBean.getRealEstatePropertiesCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	    
	    <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("COMPANYSHARES", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="companyShares" value="<%=viewBean.getCompanyShares()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="companySharesCjt" value="<%=viewBean.getCompanySharesCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	      <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("DEBTS", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="debts" value="<%=viewBean.getDebts()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="debtsCjt" value="<%=viewBean.getDebtsCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	      <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("TAXABLEINCOMEINACCORDANCEWITHDBG", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="taxableIncomeInAccordanceWithDBG" value="<%=viewBean.getTaxableIncomeInAccordanceWithDBG()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="taxableIncomeInAccordanceWithDBGCjt" value="<%=viewBean.getTaxableIncomeInAccordanceWithDBGCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	   <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("TAXABLEINCOMEINACCORDANCEWITHEXPENSETAXATION", "DE")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" name="taxableIncomeExpenseTaxation" value="<%=viewBean.getTaxableIncomeExpenseTaxation()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" name="taxableIncomeExpenseTaxationCjt" value="<%=viewBean.getTaxableIncomeExpenseTaxationCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	     

       <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="CP-OnlyDetail" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="idRetour" value="<%=viewBean.getIdRetour()%>" checkAdd="no"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>