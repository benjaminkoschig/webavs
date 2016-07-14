<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.phenix.db.communications.CPSedexDonneesCommerciales"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
bButtonUpdate = false;
bButtonDelete = false;
idEcran="CCP1017";
CPSedexDonneesCommerciales viewBean = (CPSedexDonneesCommerciales)session.getAttribute ("viewBean");
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
  			<li><a onclick="afficherDonneesCommunication()" href="#">Données communication</a></li>
  			<li><a onclick="afficherContribuable()" href="#">Contribuable</a></li>
  			<li><a onclick="afficherDonneesBase()" href="#">Données de base</a></li>
  			<li><a onclick="afficherDonneesPrivees()" href="#">Données privées</a></li>
  			<li><a class="selected">Données commerciales</a></li>
  			<li><a onclick="afficherConjoint()" href="#">Conjoint</a></li>
  			<li><a onclick="afficherRenteAVSWIRR()" href="#">Rente(s) AVS</a></li>
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
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("COMMENCEMENTOFSELFEMPLOYMENT", "FR")%>" type="text" class="libelleLongDisabled" readonly>
            </TD>
	     	<TD>
			    <INPUT type="text" name="commencementOfSelfEmployment" value="<%=viewBean.getCommencementOfSelfEmployment()%>" class="libelleLong">
			     <INPUT type="hidden"  name="isForBackup" value="<%=viewBean.isForBackup()%>">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" name="commencementOfSelfEmploymentCjt" value="<%=viewBean.getCommencementOfSelfEmploymentCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>
		  <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("ENDOFSELFEMPLOYMENT", "FR")%>" type="text" class="libelleLongDisabled" readonly>
            </TD>
	     	<TD>
			    <INPUT type="text" name="endOfSelfEmployment" value="<%=viewBean.getEndOfSelfEmployment()%>" class="libelleLong">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" name="endOfSelfEmploymentCjt" value="<%=viewBean.getEndOfSelfEmploymentCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>	    
	     		     
	      <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("MAININCOME", "FR")%>" type="text" class="libelleLongDisabled" readonly>
            </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="mainIncome" value="<%=viewBean.getMainIncome()%>" class="libelleLong">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="mainIncomeCjt" value="<%=viewBean.getMainIncomeCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>	   
	   <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("MAININCOMEINAGRICULTURE", "FR")%>" type="text" class="libelleLongDisabled" readonly>
            </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="mainIncomeInAgriculture" value="<%=viewBean.getMainIncomeInAgriculture()%>" class="libelleLong">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="mainIncomeInAgricultureCjt" value="<%=viewBean.getMainIncomeInAgricultureCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>	   
        <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("MAININCOMEINREALESTATETRADE", "FR")%>" type="text" class="libelleLongDisabled" readonly>
            </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="mainIncomeInRealEstateTrade" value="<%=viewBean.getMainIncomeInRealEstateTrade()%>" class="libelleLong">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="mainIncomeInRealEstateTradeCjt" value="<%=viewBean.getMainIncomeInRealEstateTradeCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>	 
	     
	      <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PARTTIMEEMPLOYMENT", "FR")%>" type="text" class="libelleLongDisabled" readonly>
            </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="partTimeEmployment" value="<%=viewBean.getPartTimeEmployment()%>" class="libelleLong">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="partTimeEmploymentCjt" value="<%=viewBean.getPartTimeEmploymentCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>	 
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PARTTIMEEMPLOYMENTINAGRICULTURE", "FR")%>" type="text" class="libelleLongDisabled" readonly>
            </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="partTimeEmploymentInAgriculture" value="<%=viewBean.getPartTimeEmploymentInAgriculture()%>" class="libelleLong">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="partTimeEmploymentInAgricultureCjt" value="<%=viewBean.getPartTimeEmploymentInAgricultureCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>	
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PARTTIMEEMPLOYMENTINREALESTATETRADE", "FR")%>" type="text" class="libelleLongDisabled" readonly>
            </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="partTimeEmploymentInRealEstateTrade" value="<%=viewBean.getPartTimeEmploymentInRealEstateTrade()%>" class="libelleLong">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="partTimeEmploymentInRealEstateTradeCjt" value="<%=viewBean.getPartTimeEmploymentInRealEstateTradeCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>	
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("REALISATIONPROFIT", "FR")%>" type="text" class="libelleLongDisabled" readonly>
            </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="realisationProfit" value="<%=viewBean.getRealisationProfit()%>" class="libelleLong">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="realisationProfitCjt" value="<%=viewBean.getRealisationProfitCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>	
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("COOPERATIVESHARES", "FR")%>" type="text" class="libelleLongDisabled" readonly>
            </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="cooperativeShares" value="<%=viewBean.getCooperativeShares()%>" class="libelleLong">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="cooperativeSharesCjt" value="<%=viewBean.getCooperativeSharesCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>	
	     
	    <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SECURITIESINCOME", "FR")%>" type="text" class="libelleLongDisabled" readonly>
            </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="securitiesIncome" value="<%=viewBean.getSecuritiesIncome()%>" class="libelleLong">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="securitiesIncomeCjt" value="<%=viewBean.getSecuritiesIncomeCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>	
	     
	     <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("OTHERINCOME", "FR")%>" type="text" class="libelleLongDisabled" readonly>
            </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="otherIncome" value="<%=viewBean.getOtherIncome()%>" class="libelleLong">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="otherIncomeCjt" value="<%=viewBean.getOtherIncomeCjt()%>" class="libelleLong">
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
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("INCOMEREALESTATE", "FR")%>" type="text" class="libelleLongDisabled" readonly>
            </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="incomeRealEstate" value="<%=viewBean.getIncomeRealEstate()%>" class="libelleLong">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="incomeRealEstateCjt" value="<%=viewBean.getIncomeRealEstateCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>	
	     
	      <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("DEBTINTEREST", "FR")%>" type="text" class="libelleLongDisabled" readonly>
            </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="debtInterest" value="<%=viewBean.getDebtInterest()%>" class="libelleLong">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="debtInterestCjt" value="<%=viewBean.getDebtInterestCjt()%>" class="libelleLong">
	     	</TD>
	     </TR>	
        
       <TR>
			<TD>
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("DONATIONS", "FR")%>" type="text" class="libelleLongDisabled" readonly>
            </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="donations" value="<%=viewBean.getDonations()%>" class="libelleLong">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="donationsCjt" value="<%=viewBean.getDonationsCjt()%>" class="libelleLong">
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
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("ASSETS", "FR")%>" type="text" class="libelleLongDisabled" readonly>
            </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="assets" value="<%=viewBean.getAssets()%>" class="libelleLong">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="assetsCjt" value="<%=viewBean.getAssetsCjt()%>" class="libelleLong">
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
				<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("TOTALASSETS", "FR")%>" type="text" class="libelleLongDisabled" readonly>
            </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="totalAssets" value="<%=viewBean.getTotalAssets()%>" class="libelleLong">
	     	</TD>
	     	<TD>
				&nbsp;
			 </TD>
	     	<TD>
			    <INPUT type="text" style="text-align : right;" name="totalAssetsCjt" value="<%=viewBean.getTotalAssetsCjt()%>" class="libelleLong">
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