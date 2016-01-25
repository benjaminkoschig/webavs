 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran="CCP1029";%>
<%@ page import="globaz.phenix.db.communications.*" %>
<%@ page import="java.util.LinkedList" %>
<%
	CPProcessAbandonSelectionJournalRetourViewBean viewBean = (CPProcessAbandonSelectionJournalRetourViewBean) session.getAttribute("viewBean");
	userActionValue = "phenix.communications.validationJournalRetour.processAbandonnerSelection";
	selectedIdValue = "";
%>
<%
String MSG_PROCESS_OK = "The process successfully started.";
if ("FR".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "La t�che a d�marr� avec succ�s.";
} else if ("DE".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "Prozess erfolgreich gestartet.";
}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%@page import="globaz.phenix.process.communications.CPProcessAbandonSelectionJournalRetourViewBean"%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "R�c�ption des donn�es fiscales";
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Abandon des donn�es fiscales<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
							<TR>
								<TD width="300px"><b><u><%=viewBean.getNombreAbandon()%></u> communications vont �tre abandonn�es </b></TD>
							</TR>
							<TR>
								<TD width="300px"><b></b></TD>
							</TR>	
								<TR>
					            <TD width="300px">Adresse E-Mail</TD>
					            <TD><input type="text" name='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value="<%=viewBean.getEMailAddress()%>"></TD>
					    </TR>
						<INPUT type="hidden" name="forAnnee" value="<%=viewBean.getForAnnee()%>">
						<INPUT type="hidden" name="likeNumAffilie" value="<%=viewBean.getLikeNumAffilie()%>">
						<INPUT type="hidden" name="forGrpTaxation" value="<%=viewBean.getForGrpTaxation()%>">
						<INPUT type="hidden" name="forGrpExtraction" value="<%=viewBean.getForGrpExtraction()%>">
						<INPUT type="hidden" name="forJournal" value="<%=viewBean.getForJournal()%>">
						<INPUT type="hidden" name="forGenreAffilie" value="<%=viewBean.getForGenreAffilie()%>">
						<INPUT type="hidden" name="forTypeDecision" value="<%=viewBean.getForTypeDecision()%>">
						<INPUT type="hidden" name="forStatus" value="<%=viewBean.getForStatus()%>">

						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>