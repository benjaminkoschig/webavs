<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.aquila.db.batch.COParamTaxesViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<script language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%
	idEcran = "GCO0039";
	rememberSearchCriterias = true;
	bButtonNew = objSession.hasRight("aquila.batch.parametreTaxe.afficher", "ADD");
	bButtonFind = true;

	String typeTaxe = "";
	String etape = "";
	String idTaxe = "";
	String libelle = "";
	String idRubrique = "";
	String selectedId = "";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" />
<ct:menuChange displayId="options" menuId="CO-ParamTaxe" showTab="options">
	<ct:menuActivateNode active="yes" nodeId="a_id_c_p2"/>
	<ct:menuActivateNode active="yes" nodeId="a_id_c_p3"/>
	<ct:menuActivateNode active="no" nodeId="a_id_c_p4"/>
</ct:menuChange>
<script language="JavaScript">
	var usrAction = "aquila.batch.parametreTaxe.lister";
	bFind = true;
	<%
		if (JadeStringUtil.isBlank(request.getParameter("typeTaxeEtape"))) {
			typeTaxe = (String) session.getAttribute("typeTaxeEtape");
		} else {
			typeTaxe = request.getParameter("typeTaxeEtape");
		}
		if (JadeStringUtil.isBlank(request.getParameter("Etape"))) {
			etape = (String) session.getAttribute("Etape");
		} else {
			etape = request.getParameter("Etape");
		}
		if (JadeStringUtil.isBlank(request.getParameter("idTaxe"))) {
			idTaxe = (String) session.getAttribute("idTaxe");
		} else {
			idTaxe = request.getParameter("idTaxe");
		}
		if (JadeStringUtil.isBlank(request.getParameter("Libelle"))) {
			libelle = (String) session.getAttribute("Libelle");
		} else {
			libelle = request.getParameter("Libelle");
		}
		if (JadeStringUtil.isBlank(request.getParameter("idRubrique"))) {
			idRubrique = (String) session.getAttribute("idRubrique");
		} else {
			idRubrique = request.getParameter("idRubrique");
		}
		if (JadeStringUtil.isBlank(request.getParameter("selectedId"))) {
			selectedId = (String) session.getAttribute("selectedId");
		} else {
			selectedId = request.getParameter("selectedId");
		}
	%>
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Liste der Gebührparameter<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
		<tr>
			<TD class="label">
				Gebührtyp
			</TD>
			<TD class="control">
				<input type="text" name="" value='<%=objSession.getCodeLibelle(typeTaxe)%>' class="libelleSectionLongDisabled" readonly>
			</TD>
		</tr>
		<tr>
			<TD class="label">
				Etappe
			</TD>
			<TD class="control">
				<input type="text" name="" value='<%=objSession.getCodeLibelle(etape)%>' class="libelleSectionLongDisabled" readonly>
			</TD>
		</tr>
		<tr>
			<TD class="label">
				Bezeichnung
			</TD>
			<TD class="control">
				<input type="text" name="" value='<%=libelle%>' class="libelleSectionLongDisabled" readonly>
			</TD>
			<td>
				<input type="hidden" name="forIdCalculTaxe" value='<%=idTaxe%>'>
			</td>
		</tr>
		<%
			session.setAttribute("idCalculTaxe", idTaxe);
			session.setAttribute("selectedId", selectedId);
		%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>