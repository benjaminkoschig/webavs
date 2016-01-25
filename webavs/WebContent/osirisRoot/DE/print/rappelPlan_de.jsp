
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA60011"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.ordres.*" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%@ page import="globaz.osiris.db.print.CARappelPlanViewBean"%>
<%
CARappelPlanViewBean viewBean = (CARappelPlanViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".print.rappelPlan.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Generierung der Mahnungen der Zahlungsvereinbarungen - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ausdruck der Mahnung der Einzahlungsvereinbarung<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<INPUT type="hidden" name="idPlanRecouvrement" value="<%=viewBean.getIdPlanRecouvrement()%>"/>
		<TR>
			<TD class="label">E-Mail</TD>
			<%
				int inputSize = viewBean.getSession().getUserEMail().length() + 5;
				if(inputSize < 20){
					inputSize = 20;
				}
			%>
			<TD class="control"><INPUT type="text" name="eMailAddress" value="<%=viewBean.getSession().getUserEMail()%>" size="<%=inputSize%>"></TD>
		</TR>
		<TR>
			<TD class="label">Dokumentdatum</TD>
			<TD class="control"><ct:FWCalendarTag name="dateRef" doClientValidation="CALENDAR" value="<%=JACalendar.todayJJsMMsAAAA()%>"/></TD>
		</TR>
		<tr>
			<td class="label">Ausdrucken</td>
			<td class="control"><input type="checkbox" name="withEcheancier" id="echeancier" value="on"><label for="echeancier">mit dem Terminkalender</label></td>
		</tr>
        <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>