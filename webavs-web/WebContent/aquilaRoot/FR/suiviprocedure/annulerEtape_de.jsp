<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/process/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "GCO0032";
%>
<%@ page import="globaz.aquila.db.suiviprocedure.*"%>
<%
	COAnnulerEtapeViewBean viewBean = (COAnnulerEtapeViewBean) session.getAttribute("viewBean");
	userActionValue = "aquila.batch.transition.annulerdernieretransition";
	selectedIdValue = request.getParameter("selectedId");
%>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--//hide this script from non-javascript-enabled browsers

function validate() {
	//jscss("add", document.getElementById("btnOk"), "hidden");
	document.getElementById("btnOk").disabled = true;
	return true;
}
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
Annulation Etape
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>

<TR>
	<TD>
	<TABLE>
	 <jsp:include flush="true" page="../headerContentieux.jsp" />
		<TR>
			<TD colspan="8"><BR>
			<HR>
			<BR>
			</TD>
		</TR>
		<input type="hidden" name="libSequence"
			value="<%=request.getParameter("libSequence")%>" />
		<input type="hidden" name="idContentieux"
			value="<%=request.getParameter("idContentieux")%>" />
		<tr>
			<td width="170px">E-mail :</td>
			<td class="control">&nbsp;<input type="text" name="eMailAddress"
				value="<%=viewBean.getSession().getUserEMail()%>"
				class="libelleLong"></td>
		</tr>
		<tr>
			<td width="170px">Extourner les opérations :</td>
			<td class="control"><input type="checkbox" name="idExtourne"
				checked="true"></td>
		</tr>

	</TABLE>
	</TD>
</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<%
if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) {
%>
<%
}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf"%>
<%-- /tpl:insert --%>