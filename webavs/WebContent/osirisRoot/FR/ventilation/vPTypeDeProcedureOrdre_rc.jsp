<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%idEcran = "GCA4024";
rememberSearchCriterias = true;
%>
<%@page import="globaz.osiris.db.ventilation.CAVPTypeDeProcedureOrdreManager"%>
<script>
	usrAction="osiris.ventilation.vPTypeDeProcedureOrdre.lister";
	bFind = true;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Recherche des ordres par type de procédure<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>Type de procédure</td>
							<td>&nbsp;</td>
							<td> <ct:FWCodeSelectTag
							name="forTypeProcedure" codeType="OSITYPROC" defaut="" wantBlank="true" /></td>
						</tr>
						<tr>
							<td>N° rubrique depuis</td>
							<td>&nbsp;</td>
							<td><input type="text" name="fromIdExterne" value=""></td>
						</tr>
						<tr>
							<td>Tri</td>
							<td>&nbsp;</td>
							<td>
								<select name="orderBy" class="libelleCourt" tabindex="3">
							 		<% String sOrderBy = request.getParameter("forSelectionCompte");
			   							if (sOrderBy == null) {
				   							sOrderBy = CAVPTypeDeProcedureOrdreManager.ORDER_BY_ORDRE_ASC_ID_EXTERNE_RUBRIQUE_ASC;
			   							}%>
                					<option <%=(sOrderBy.equals("1")) ? "selected" : "" %> value="1">Rubrique</option>
                					<option <%=(sOrderBy.equals("2")) ? "selected" : "" %> value="2">Ordre</option>
              					</select>
							</td>
						</tr>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>