<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%
	idEcran = "GCO0011";
	rememberSearchCriterias = true;
	bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CO-OptionsDefaut" showTab="menu"/>

<SCRIPT language="JavaScript">
	var usrAction = "aquila.irrecouvrables.sections.lister";
	bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Suche der Uneinbringlichen Sektionen<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<!--selectedId=1&idExterneRole=100.008&description=Jeanmaire Vivien-->
							<tr>
								<TD class="label">
									Abr.-Nr.&nbsp;
								</TD>
								<TD class="control">
									<input type="text" name="" value="<%=request.getParameter("idExterneRole")%>" class=disabled readonly>
								</TD>
								<TD class="label">Mitgliedsname&nbsp;</TD>
								<TD class="control"><input type="text" name="" value="<%=request.getParameter("description")%>" size="<%=request.getParameter("description").length()+2%>" class=disabled readonly></TD>								
							</tr>
							<tr>
								<td class="label">Ab</td>
								<td class="control">
									<input type="text" name="likeIdExterne" value="">
								</td>
								<td class="label">
									Sektionen&nbsp;
								</td>
								<td class="control"><select name="forSelectionSections" class="libelleCourt">
					                <option value="1000">alle</option>
					                <option value="1">offene</option>
					                <option value="2">saldierte</option>
					              </select></td>
							</tr>
							<input type="hidden" name="forIdCompteAnnexe" value="<%=request.getParameter("selectedId")%>">
							<input type="hidden" name="orderBy" value="DATEDEBUTPERIODE">
						<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>