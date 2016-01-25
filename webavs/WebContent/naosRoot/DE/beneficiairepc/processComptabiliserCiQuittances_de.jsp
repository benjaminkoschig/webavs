 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.naos.db.beneficiairepc.AFQuittancePCGComptabilisationCIProcessViewBean"%>
<%@page import="globaz.naos.db.beneficiairepc.AFQuittancePCGFacturationProcessViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran="";%>
<%@ page import="globaz.phenix.db.communications.*" %>
<%@ page import="java.util.LinkedList" %>
<%
idEcran = "CAF0073";
AFQuittancePCGComptabilisationCIProcessViewBean viewBean = (AFQuittancePCGComptabilisationCIProcessViewBean) session.getAttribute("viewBean");
	//userActionValue = "naos.beneficiairepc.journalQuittances.executerGenerer";
	userActionValue = "naos.beneficiairepc.processComptabiliserCiQuittances.executer";
	selectedIdValue = "";
	%>
<%
String MSG_PROCESS_OK = "The process successfully started.";
if ("FR".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "La tâche a démarré avec succès.";
} else if ("DE".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "Prozess erfolgreich gestartet.";
}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Comptabilisation CI des quittances";

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Comptabilisation CI des quittances<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
<TR>
    <TD nowrap="nowrap" width="350">Numéro du journal</TD>
    <TD><input type="text" name='dJournalQuittance' class='libelleLongDisabled' value="<%=viewBean.getIdJournalQuittances()%>" readonly="readonly"></TD>
</TR>
<TR>
    <TD nowrap="nowrap" width="350">Libelle du journal</TD>
    <TD><input type="text" name='libelle' class='libelleLongDisabled' value="<%=viewBean.getLibelleJournal()%>" readonly="readonly"></TD>
</TR>

<TR>
    <TD nowrap="nowrap" width="350">Adresse E-Mail</TD>
    <TD><input type="text" name='eMailAddress' class='libelleLong' value="<%=viewBean.getEMailAddress()%>"></TD>
</TR>


						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>