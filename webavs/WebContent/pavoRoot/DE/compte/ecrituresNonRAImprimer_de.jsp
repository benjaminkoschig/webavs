 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
globaz.pavo.db.compte.CIEcrituresNonRAImprimerViewBean viewBean = (globaz.pavo.db.compte.CIEcrituresNonRAImprimerViewBean)session.getAttribute("viewBean");
selectedIdValue="";
userActionValue="pavo.compte.ecrituresNonRAImprimer.executer";
idEcran = "CCI0027";



%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script>

function validate(){
document.forms[0].submit();}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ausdruck der unbekannten IK's im VR<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		 <tr>
						<td>Journal</td>
						<td><input type="text" name="idJournal" value="<%=viewBean.getIdJournal()%>" class="disabled" readonly></td>

						</tr>
						<tr>
						<td>Beschreibung</td>
						<td><input type="text" size="75" name="journalDescription" value="<%=viewBean.getJournalDescription()%>" class="disabled" readonly></td>
						</tr>
						<tr>
						<td>E-Mail Adresse</td>
						<td><input name = "eMailAddress" type="text" value ="<%=viewBean.getEMailAddress()%>"></td>
						</tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>