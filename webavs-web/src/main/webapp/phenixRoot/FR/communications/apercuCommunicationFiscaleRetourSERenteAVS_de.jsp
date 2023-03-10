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
	}
}

bButtonDelete = false;
idEcran="CCP1017";
globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean viewBean = (globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean)session.getAttribute ("viewBean");
key="globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean-idRetour"+viewBean.getIdRetour();

if(!viewBean.isNonActif()){
	bButtonUpdate = false;
}

%>
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
top.document.title = "Cotisation - Donn?es fiscales D?tail"
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
			<%-- tpl:put name="zoneTitle" --%>Donn?es fiscales<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<div id="tabs"> 
		<ul>
  			<li><a onclick="afficherDonneesCommunication()" href="#">Donn?es communication</a></li>
  			<li><a onclick="afficherContribuable()" href="#">Contribuable</a></li>
  			<li><a onclick="afficherDonneesBase()" href="#">Donn?es de base</a></li>
  			<li><a onclick="afficherDonneesPrivees()" href="#">Donn?es priv?es</a></li>
  			<li><a onclick="afficherDonneesCommerciales()" href="#">Donn?es commerciales</a></li>
  			<li><a onclick="afficherConjoint()" href="#">Conjoint</a></li>
  			<li><a class="selected">Rente(s) AVS</a></li>
		</ul>
		</div>
		
		 <TR>
			<TD>Montant total des rentes AVS</TD>
			<%if(viewBean.isNonActif()) {%>
				<TD><ct:inputText style="width : 2.45cm;" name="montantTotalRenteAVS"  defaultValue="<%=viewBean.getMontantTotalRenteAVS()%>"   notation="data-g-amount='blankAsZero:false'" /></TD>
	     	<%} else { %>
	     		<TD><ct:inputText style="width : 2.45cm;" name="montantTotalRenteAVS"  defaultValue="<%=viewBean.getMontantTotalRenteAVS()%>" disabled="disabled" styleClass="montantDisabled" readonly="readonly"   notation="data-g-amount='blankAsZero:false'" /></TD>
	     	<% } %>
	     </TR>
	     <TR>
			<TD>Message du WebService</TD>
			<TD><INPUT name="messageRenteAVS" type="text" value="<%=viewBean.getMessageRenteAVS()%>" class="libelleLong20Disabled" readonly="readonly"></TD>
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