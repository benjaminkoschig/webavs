  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
idEcran="CFA0012";
selectedIdValue = request.getParameter("selectedId");
%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.musca.db.facturation.*"%>

<%
	//Ce bean n'est pas utilisé dans cette page, mais est déclaré pour des impératifs dû au template
	globaz.musca.db.facturation.FAPassageModuleFacturationViewBean viewBean = new globaz.musca.db.facturation.FAPassageModuleFacturationViewBean();
	userActionValue="musca.facturation.passageFacturation.listes";
	FWController controller = (FWController) session.getAttribute("objController");
	BSession objSession = (BSession)controller.getSession();
%>


<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.musca.util.FAUtil"%><SCRIPT language="JavaScript">
//permet de jongler avec les pages
function lien(){
	var userActionInput = document.getElementById('userAction');

	userActionInput.value = document.mainForm.userChoice.value;
	document.getElementById('selectedId').value = <%=request.getParameter("selectedId") %>;
}

top.document.title = "Musca - Impression des décomptes"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Listes<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD>IdPassage</TD>
            <TD><INPUT name="idPassage" type="text" value="<%=request.getParameter("selectedId") %>" class="numeroCourtDisabled" readonly></TD>
          </TR>
          <tr> 
            <td> Choix de la liste </td>
            <td>
            	<select name="userChoice" style="width:8cm;" onchange="lien();">
            		<option value="musca.facturation.passageFacturation.listes">Choisir</option>
            		<option value="musca.facturation.passageFacturation.listerAfacts">Liste des afacts</option>
            		<option value="musca.facturation.passageFacturation.listerCompensations">Liste de compensation</option>
            		<option value="musca.facturation.passageFacturation.listerDecomptes">Liste des décomptes</option>
	           		<option value="musca.facturation.passageFacturation.genererIntMoratoire">Décision d'intérêts</option>
	           		<option value="musca.facturation.passageFacturation.genererLettreTaxeCo2">Lettre redistribution taxe CO2</option>
	           		<option value="musca.facturation.passageFacturation.imprimerLettreRentier">Lettre pour rentier NA</option>
	           		
	           		<%if(FAPassage.CS_ETAT_COMPTABILISE.equalsIgnoreCase(FAUtil.getPassageStatus(request.getParameter("selectedId"),request.getSession()))) { %>
		           		<ct:ifhasright element="musca.facturation.passageFacturation.genererSoldeBVR" crud="c">
		           			<option value="musca.facturation.passageFacturation.genererSoldeBVR">Bulletins de soldes</option>
		           		</ct:ifhasright>
		           	<%}%>
	           		
	           		<option value="musca.facturation.passageFacturation.listerTaxation">Taxations définitives</option>
	           		<option value="musca.facturation.passageFacturation.listerDecisionControle">Décisions de contrôle</option>
            	</select>
            </td>
          </tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>