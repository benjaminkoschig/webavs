<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.helios.db.interfaces.*,globaz.helios.db.comptes.*,globaz.helios.tools.CGSessionDataContainerHelper" %>
<%
	idEcran="GCF0012";
	rememberSearchCriterias = true;
CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean )session.getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

	String selectPeriodeId = "";
	CGSessionDataContainerHelper sessionContainerHelper = new CGSessionDataContainerHelper();
	selectPeriodeId = (String)sessionContainerHelper.getData(session,CGSessionDataContainerHelper.KEY_LAST_SELECTED_ID_PERIODE);


	String toutLexercice = "Tout l'exercice";
	if (languePage.equalsIgnoreCase("de"))
		toutLexercice = "Ganzes Rechnungsjahr";

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
usrAction = "helios.comptes.journal.lister";
bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Journal<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

		<tr>
	            <TD colspan="6">&nbsp;Mandant&nbsp;<input name='libelle' class="libelleLongDisabled" readonly value="<%=exerciceComptable.getMandat().getLibelle()%>">&nbsp;Rechnungsjahr&nbsp;<input name='fullDescription' readonly class="libelleLongDisabled" value="<%=exerciceComptable.getFullDescription()%>">
	            </TD>
		</tr>
		<tr><td colspan="6"><hr></td></tr>

		<tr>
				<TD width="" align="right">&nbsp;Status&nbsp;</TD>
		            <td> <ct:FWCodeSelectTag name="forIdEtat" defaut="<%=CGJournal.CS_ETAT_TOUS%>" codeType="CGJOETAT"/>
		<script>
			document.getElementById("forIdEtat").style.width = "4cm";
		</script>
		</td>
		    <TD width="">&nbsp;Bis zu&nbsp;</TD>
		    <TD width=""><INPUT name="reqUntilLibelle" class="libelle" value="" size="20"></TD>
		    <TD width="" align="right">&nbsp;Auswahl&nbsp;</TD>
		    <td> <ct:FWCodeSelectTag name="reqUntilCritere" defaut="" codeType="CGJOURTRI"/>
				<script>
					document.getElementById("reqUntilCritere").style.width = "4cm";
				</script>
				<input type="hidden" name="forIdExerciceComptable" value="<%=exerciceComptable.getIdExerciceComptable()%>">
			</td>
		</tr>
		<tr>
			<td>Periode</td>
			<td><ct:FWListSelectTag name="forIdPeriodeComptable" defaut="<%=selectPeriodeId%>" data="<%=globaz.helios.translation.CGListes.getPeriodeComptableListe(session,toutLexercice)%>"/></td>
		            <TD width="">&nbsp;Ab&nbsp;</TD>
		            <TD width=""><INPUT name="reqLibelle" class="libelle" value="" size="20"></TD>
		            <TD width="" align="right">&nbsp;Auswahl&nbsp;</TD>
		            <td> <ct:FWCodeSelectTag name="reqCritere" defaut="" codeType="CGJOURTRI"/>
						<script>
							document.getElementById("reqCritere").style.width = "4cm";
						</script>
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