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
<%-- tpl:put name="zoneTitle" --%>Création d'un sursis - Phase 1 - Comptes Annexes<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<tr>
		<td><h5>Veuillez selectionner un compte annexe :</h5></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<TD class="label">Numéro du compte annexe&nbsp;</TD>
		<TD class="control"><input type="text" name="fromIdExterneRole">&nbsp;</TD>
		<TD class="control">
             <select name="forSelectionRole" >
             	<%=globaz.osiris.db.comptes.CARoleViewBean.createOptionsTags(objSession, request.getParameter("forIdRole"))%>
             </select>
        </TD>
	</tr>
	<tr>
		<td class="label">
			S&eacute;lection des comptes&nbsp;
		</td>
		<td class="control">
              <select name="forSelectionCompte" class="libelleCourt" tabindex="3">
			<% String sForSelectionCompte = request.getParameter("forSelectionCompte");
			   if (sForSelectionCompte == null)
				    sForSelectionCompte ="1000";
			%>
                <option <%=(sForSelectionCompte.equals("1000")) ? "selected" : "" %> value="1000">tous</option>
                <option <%=(sForSelectionCompte.equals("1")) ? "selected" : "" %> value="1">ouverts</option>
                <option <%=(sForSelectionCompte.equals("2")) ? "selected" : "" %> value="2">sold&eacute;s</option>
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