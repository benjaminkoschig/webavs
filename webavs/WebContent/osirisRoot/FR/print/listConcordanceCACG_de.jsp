<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA2033"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.parser.*" %>
<%@ page import="globaz.osiris.db.print.CAListConcordanceCACGViewBean" %>
<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	CAListConcordanceCACGViewBean viewBean = (CAListConcordanceCACGViewBean) session.getAttribute("viewBean");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script><!--//hide this script from non-javascript-enabled browsers
	<%
		userActionValue = "osiris.print.listConcordanceCACG.executer";
	%>
	top.document.title = "Liste des concordance CA/CG" + top.location.href;
// stop hiding -->
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Liste des concordances CA/CG (Excel)<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<tr height="40">
		<td colspan="4" class="control">
			Liste des concordances pour le compte :
			<input type="text" id="forIdExterne" name="forIdExterne" class="libelleDisabledNoBorder" readonly="readonly" value='<%=request.getParameter("forIdExterne") %>'>
		</td>
	</tr>

	<tr>
		<td width="150">E-mail</td>
		<td colspan="3">
			<input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong">
		</td>
	</tr>
	<tr>
		<td>Date début</td>
		<td colspan="3" class="control">
			<ct:FWCalendarTag name="dateDebut" doClientValidation="CALENDAR" displayType="MONTH" value='<%="01." + String.valueOf(JACalendar.today().getYear())%>'/>
		</td>
	</tr>
	<tr>
		<td >Date fin</td>
		<td colspan="3" class="control">
			<ct:FWCalendarTag name="dateFin" doClientValidation="CALENDAR" displayType="MONTH" value='<%="12." + String.valueOf(JACalendar.today().getYear())%>'/>
		</td>
	</tr>
	<tr>
		<td></td>
		<td colspan="3">
			<input type="checkbox" name="printOnlyDiff" id="printOnlyDiff"><label for="printOnlyDiff">N'afficher que les différences.</label>
		</td>
	</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %><%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>