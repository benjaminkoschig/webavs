<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GCA60013";
	rememberSearchCriterias = true;
	bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript">
	var usrAction = "osiris.recouvrement.compteAnnexe.lister";
	bFind = false;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Erstellung einer Zahlungsvereinbarung - Phase 1 - Abrechnungskonti<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<tr>
		<td><h5>Bitte wählen Sie ein Abrechnungskonto :</h5></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<TD class="label">Beiliegendes Konto-Nr.&nbsp;</TD>
		<TD class="control"><input type="text" name="fromIdExterneRole">&nbsp;</TD>
		<TD class="control">
             <select name="forSelectionRole" >
             	<%=globaz.osiris.db.comptes.CARoleViewBean.createOptionsTags(objSession, request.getParameter("forIdRole"))%>
             </select>
        </TD>
	</tr>
	<tr>
		<td class="label">
			Auswahl der Konti&nbsp;
		</td>
		<td class="control">
              <select name="forSelectionCompte" class="libelleCourt" tabindex="3">
			<% String sForSelectionCompte = request.getParameter("forSelectionCompte");
			   if (sForSelectionCompte == null)
				    sForSelectionCompte ="1000";
			%>
                <option <%=(sForSelectionCompte.equals("1000")) ? "selected" : "" %> value="1000">alle</option>
                <option <%=(sForSelectionCompte.equals("1")) ? "selected" : "" %> value="1">eröffnet</option>
                <option <%=(sForSelectionCompte.equals("2")) ? "selected" : "" %> value="2">saldiert</option>
              </select>
	</tr>
	<input type="hidden" name="forIdRoleee" value="<%=globaz.osiris.external.IntRole.ROLE_AFFILIE%>">
	<input type="hidden" name="orderBy" value="IDEXTERNEROLE">

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>