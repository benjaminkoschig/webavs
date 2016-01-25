<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	String jspLocation =  servletContext + mainServletPath + "Root/rubrique_select.jsp";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%idEcran = "GCA4022";
	bButtonNew = false;
	rememberSearchCriterias = true;
%>
<%@page import="globaz.osiris.db.comptes.CATauxRubriquesManager"%>
<script>
	usrAction="osiris.ventilation.tauxRubriques.lister";
	bFind = true;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Recherche des taux<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>N° de rubrique</td>
							<td>&nbsp;</td>
							<td>
								<ct:FWPopupList
									validateOnChange="true" value=""
									name="forIdExterne" size="15" className="visible" jspName="<%=jspLocation%>"
									minNbrDigit="3" autoNbrDigit="11" />
							</td>
						</tr>
						<tr>
							<td>N° de rubrique depuis</td>
							<td>&nbsp;</td>
							<td><input type="text" name="fromIdExterne" value="" size="15"> </td>
							<td><input type="hidden" name="orderBy" value="<%=CATauxRubriquesManager.ORDER_BY_IDEXTERNE_ASC_DATE_ASC%>"> </td>
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