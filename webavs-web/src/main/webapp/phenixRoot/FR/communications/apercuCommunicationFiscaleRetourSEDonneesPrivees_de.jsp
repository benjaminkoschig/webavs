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
globaz.phenix.db.communications.CPSedexDonneesPrivees viewBean = (globaz.phenix.db.communications.CPSedexDonneesPrivees)session.getAttribute ("viewBean");
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
  			<li><a class="selected">Données privées</a></li>
  			<li><a  onclick="afficherDonneesCommerciales()" href="#">Données commerciales</a></li>
  			<li><a  onclick="afficherConjoint()" href="#">Conjoint</a></li>
		</ul>
		</div>
		<TR style="font-size : 14px;">
			<TD> &nbsp;</TD>
	     	<TD align="center" class="libelleLongDisabled">
				<%=viewBean.getSession().getApplication().getLabel("CONTRIBUABLE", "FR")%>
			 </TD>
			 <TD width="50" nowrap="nowrap">
				&nbsp;
			 </TD>
	     	<TD align="center" class="libelleLongDisabled">
			   <%=viewBean.getSession().getApplication().getLabel("CONJOINT", "FR")%>
	     	</TD>
	     </TR>
	    <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PENSIONS", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="pensions" value="<%=viewBean.getPensions()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="pensionsCjt" value="<%=viewBean.getPensionsCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
        <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PENSIONSPILLAR1", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="pensionsPillar1" value="<%=viewBean.getPensionsPillar1()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="pensionsPillar1Cjt" value="<%=viewBean.getPensionsPillar1Cjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PENSIONSPILLAR2", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="pensionsPillar2" value="<%=viewBean.getPensionsPillar2()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="pensionsPillar2Cjt" value="<%=viewBean.getPensionsPillar2Cjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PENSIONSPILLAR3A", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="pensionsPillar3a" value="<%=viewBean.getPensionsPillar3a()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="pensionsPillar3aCjt" value="<%=viewBean.getPensionsPillar3aCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	   
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PENSIONSPILLAR3B", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="pensionsPillar3b" value="<%=viewBean.getPensionsPillar3b()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="pensionsPillar3bCjt" value="<%=viewBean.getPensionsPillar3bCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("ANNUITIES", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="annuities" value="<%=viewBean.getAnnuities()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="annuitiesCjt" value="<%=viewBean.getAnnuitiesCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("OTHERPENSIONS", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="otherPensions" value="<%=viewBean.getOtherPensions()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="otherPensionsCjt" value="<%=viewBean.getOtherPensionsCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("MILITARYINSURANCEPENSIONS", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="militaryInsurancePensions" value="<%=viewBean.getMilitaryInsurancePensions()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="militaryInsurancePensionsCjt" value="<%=viewBean.getMilitaryInsurancePensionsCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	      <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PERDIEMALLOWANCE", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="perDiemAllowance" value="<%=viewBean.getPerDiemAllowance()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="perDiemAllowanceCjt" value="<%=viewBean.getPerDiemAllowanceCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
         <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("MAINTENANCECONTRIBUTION", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="maintenanceContribution" value="<%=viewBean.getMaintenanceContribution()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="maintenanceContributionCjt" value="<%=viewBean.getMaintenanceContributionCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("CHILDALLOWANCE", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="childAllowance" value="<%=viewBean.getChildAllowance()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="childAllowanceCjt" value="<%=viewBean.getChildAllowanceCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PATENTLICENSE", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="patentLicense" value="<%=viewBean.getPatentLicense()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="patentLicenseCjt" value="<%=viewBean.getPatentLicenseCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	   <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("RESIDENCYENTITLEMENT", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="residencyEntitlement" value="<%=viewBean.getResidencyEntitlement()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="residencyEntitlementCjt" value="<%=viewBean.getResidencyEntitlementCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SECURITIES", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="securities" value="<%=viewBean.getSecurities()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="securitiesCjt" value="<%=viewBean.getSecuritiesCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	    <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("CASH", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="cash" value="<%=viewBean.getCash()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="cashCjt" value="<%=viewBean.getCashCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("LIFEINSURANCE", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="lifeInsurance" value="<%=viewBean.getLifeInsurance()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="lifeInsuranceCjt" value="<%=viewBean.getLifeInsuranceCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("MOTORVEHICLE", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="motorVehicle" value="<%=viewBean.getMotorVehicle()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="motorVehicleCjt" value="<%=viewBean.getMotorVehicleCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("INHERITANCE", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="inheritance" value="<%=viewBean.getInheritance()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="inheritanceCjt" value="<%=viewBean.getInheritanceCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	      <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("OTHERASSETS", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="otherAssets" value="<%=viewBean.getOtherAssets()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="otherAssetsCjt" value="<%=viewBean.getOtherAssetsCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	      <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("REALESTATEPROPERTIES", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="realEstateProperties" value="<%=viewBean.getRealEstateProperties()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="realEstatePropertiesCjt" value="<%=viewBean.getRealEstatePropertiesCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	    
	    <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("COMPANYSHARES", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="companyShares" value="<%=viewBean.getCompanyShares()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="companySharesCjt" value="<%=viewBean.getCompanySharesCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     
	      <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("DEBTS", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="debts" value="<%=viewBean.getDebts()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="debtsCjt" value="<%=viewBean.getDebtsCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	      <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("TAXABLEINCOMEINACCORDANCEWITHDBG", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="taxableIncomeInAccordanceWithDBG" value="<%=viewBean.getTaxableIncomeInAccordanceWithDBG()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="taxableIncomeInAccordanceWithDBGCjt" value="<%=viewBean.getTaxableIncomeInAccordanceWithDBGCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
	   <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("TAXABLEINCOMEINACCORDANCEWITHEXPENSETAXATION", "FR")%>" type="text" class="libelleLongDisabled" readonly>
   			 </TD>
	     	<TD>
				<INPUT type="text" style="text-align : right;" name="taxableIncomeExpenseTaxation" value="<%=viewBean.getTaxableIncomeExpenseTaxation()%>" class="libelleLong">
	  		</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			     <INPUT type="text" style="text-align : right;" name="taxableIncomeExpenseTaxationCjt" value="<%=viewBean.getTaxableIncomeExpenseTaxationCjt()%>" class="libelleLong">
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