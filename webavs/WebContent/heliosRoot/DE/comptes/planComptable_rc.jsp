
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">


<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %> 
<%@ page import="globaz.helios.db.interfaces.*,globaz.helios.db.comptes.*,globaz.helios.translation.*" %>
<%
rememberSearchCriterias = true;
idEcran="GCF0019";
CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean )session.getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CG-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CG-OnlyDetail"/>

<%
	globaz.framework.menu.FWMenuBlackBox bb = (globaz.framework.menu.FWMenuBlackBox) session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_USER_MENU);
	bb.setNodeOpen(false, "parameters", "CG-MenuPrincipal");
%>

<SCRIPT>
usrAction = "helios.comptes.planComptable.lister";
timeWaiting = 1;
bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Kontoplan<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
				<tr>
				<td colspan="4">&nbsp;Mandant&nbsp;<input name='libelle' class='libelleLongDisabled' readonly value='<%=exerciceComptable.getMandat().getLibelle()%>'>&nbsp;Rechnungsjahr&nbsp;<input name='fullDescription' class="disabled" readonly value='<%=exerciceComptable.getFullDescription()%>'>&nbsp;Buchhaltung&nbsp;<ct:FWCodeSelectTag name="reqComptabilite" defaut="<%=CodeSystem.CS_DEFINITIF%>" codeType="CGPRODEF"/>
					<script>
						document.getElementById("reqComptabilite").style.width="5cm";
					</script>
				</td>
				</tr>
				<tr><td colspan="4"><hr></td></tr>

				<tr>
				<td>&nbsp;Sortierung&nbsp;</td>
				<td> <ct:FWCodeSelectTag name="reqCritere" defaut="<%=CodeSystem.CS_TRI_NUMERO_COMPTE%>" codeType="CGSCPTRI" /> </td>
				<td>&nbsp;Kontoart&nbsp;</td>
				<td> <ct:FWCodeSelectTag name="reqGenreCompte" defaut="<%=CGCompte.CS_GENRE_TOUS%>" codeType="CGGENCPT" /> </td>

				</tr>

				<tr>
				<td>&nbsp;Ab&nbsp;</td>
				<td> <input name='reqLibelle' class='libelle' value=''> </td>
				<td>&nbsp;Bereich&nbsp;</td>
				<td> <ct:FWCodeSelectTag name="reqDomaine" defaut="<%=CGCompte.CS_COMPTE_TOUS%>" codeType="CGDOMCPT" /> 
				<input type="hidden" name="forIdExerciceComptable" value="<%=exerciceComptable.getIdExerciceComptable()%>">
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