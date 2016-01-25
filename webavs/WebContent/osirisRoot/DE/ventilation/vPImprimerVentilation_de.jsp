<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA3018"; %>
<%
    globaz.osiris.db.ventilation.CAVPImprimerVentilationViewBean viewBean  = (globaz.osiris.db.ventilation.CAVPImprimerVentilationViewBean) session.getAttribute("viewBean");
	viewBean.setListIdSections(Arrays.asList(JadeStringUtil.split(request.getParameter("selectedIds"), ',', Integer.MAX_VALUE)));
	viewBean.setIdCompteAnnexe(request.getParameter("idCompteAnnexe"));
	String email = viewBean.getSession().getUserEMail();

	userActionValue = "osiris.ventilation.vPImprimerVentilation.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Umlagen eines Abrechnungskonto<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<%@page import="java.util.Arrays"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<tr>
	<td class="label">E-Mail</td>
	<td class="control">
		<input type ="text" name ="eMailAddress" value=<%=email%>>
	</td>
</tr>
<tr>
	<td class="label">Verfahrenstyp</td>
	<td class="control">
		<ct:FWCodeSelectTag
		name="typeDeProcedure" defaut=""
		codeType="OSITYPROC" wantBlank="false" />
	</td>
</tr>
<tr>
	<td class="label">Gesamte Gliederung</td>
	<td class="control">
		<input type="checkbox" name="ventilationGlobale" id="ventilationGlobaleStr" <%=viewBean.getVentilationGlobale().booleanValue()? "checked" : "unchecked"%>><label for="ventilationGlobaleStr">Die Gliederung wird nach Rubrik auf der Gesamtheit der ausgewählten Sektionen ausgeführt.</label>
    </td>
</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>