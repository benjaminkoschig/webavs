<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GCA60014";
	rememberSearchCriterias = true;
	bButtonNew = false;
	bButtonFind = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript">
	var usrAction = "osiris.recouvrement.sections.lister";
	bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Création d'un sursis - Phase 2 - Recherche de sections pour le sursis<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<!--selectedId=1&idExterneRole=100.008&description=Jeanmaire Vivien-->
							<tr>
								<td><h5>Veuillez selectionner une ou plusieurs sections :</h5></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<TD class="label">
									N° Affilié&nbsp;
								</TD>
								<TD class="control">
									<input type="text" name="" value="<%=request.getParameter("idExterneRole")%>" class=disabled readonly>
								</TD>
							</tr>
							<tr>
								<TD class="label">Nom d'affilié&nbsp;</TD>
								<TD class="control"><input type="text" name="" value="<%=request.getParameter("description")%>" size="<%=request.getParameter("description").length()+2%>" class="libelleLongDisabled" readonly></TD>
							</tr>
							<input type="hidden" name="forIdCompteAnnexe" value="<%=request.getParameter("selectedId")%>">
							<input type="hidden" name="orderBy" value="DATEDEBUTPERIODE">
							<input type="hidden" name="forSoldeNot" value="0">
							<input type="hidden" name="ForIdPlanRecouvrement" value="0">

						<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>