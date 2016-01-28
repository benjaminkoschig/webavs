<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%
	idEcran = "GCO0010";
	rememberSearchCriterias = true;
	bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CO-OptionsDefaut" showTab="menu"/>

<SCRIPT language="JavaScript">
	var usrAction = "aquila.irrecouvrables.compteAnnexe.lister";
	bFind = false;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Irrecouvrables - Comptes Annexes<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<tr>
								<TD class="label">N� Affili�&nbsp;</TD>
								<TD class="control"><input type="text" name="likeIdExterneRole"></TD>
								<TD class="control">
					              <select name="forSelectionRole" >
					              	<%=globaz.aquila.db.irrecouvrables.CORoleViewBean.createOptionsTags(objSession, request.getParameter("forIdRole"))%>
					              </select>
					            </TD>
							</tr>
							<input type="hidden" name="forIdRoleee" value="<%=globaz.aquila.db.irrecouvrables.COCompteAnnexeListViewBean.CS_AFFILIE%>">
							<input type="hidden" name="orderBy" value="IDEXTERNEROLE">

						<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>