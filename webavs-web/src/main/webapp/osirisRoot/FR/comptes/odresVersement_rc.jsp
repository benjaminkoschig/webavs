<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
idEcran = "GCA0061";
rememberSearchCriterias = true;
String idCompteAnnexe = request.getParameter("idCompteAnnexe");
bButtonNew = false;

CAOdresVersementListViewBean manager = new CAOdresVersementListViewBean();
manager.setSession((globaz.globall.db.BSession)controller.getSession());
manager.setForIdCompteAnnexe(request.getParameter("idCompteAnnexe"));
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.comptes.odresVersement.lister";
bFind = true;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Ordres en attente<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
				            <td width="70" valign="top" rowspan="3">Compte
				            <input type="hidden" name="forIdCompteAnnexe" value="<%=idCompteAnnexe%>"/>
				            </td>
				            <td valign="top" rowspan="3" width="200">
				              <TEXTAREA cols="40" rows="3" class="libelleLongDisabled" readonly><%=manager.getCompteAnnexeTitulaireEntete()%></TEXTAREA>
				            </td>
				            <td width="300">&nbsp;</td>
				            <td align="right">
					            Ordres en attente de versement
								&nbsp;
								<input type="checkbox" name="forOrdresNonVerse" value="true" <%if ((!manager.isCompteAnnexeRoleRentier().booleanValue())&&(!manager.isIJRoleRentier().booleanValue())&&(!manager.isIJRoleAPG().booleanValue())) {%>checked<%}%>/>
				            </td>
						</tr>
						<tr>
							<td colspan="4" align="right">
								Ordres en attente de recouvrement
								&nbsp;
								<input type="checkbox" name="forOrdresNonRecouvert" value="true" <%if ((!manager.isCompteAnnexeRoleRentier().booleanValue())&&(!manager.isIJRoleRentier().booleanValue())&&(!manager.isIJRoleAPG().booleanValue())) {%>checked<%}%>/>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="right">
								Ordres bloqués
								&nbsp;
								<input type="checkbox" name="forOrdresBloquesOnly" value="true"/>
								<input type="hidden" name="forTousOrdresEnAttente" value="true"/>
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