<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">


<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.helios.db.interfaces.*,globaz.helios.db.comptes.*,globaz.helios.translation.*" %>

<%
	idEcran="GCF0008";
	rememberSearchCriterias = true;
CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean) session.getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

	String toutLexercice = "Tout l'exercice";
	if (languePage.equalsIgnoreCase("de"))
		toutLexercice = "Ganzes Rechnungsjahr";

bButtonNew = false;
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

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>

<SCRIPT>
usrAction = "helios.comptes.ecritureSeeker.lister";
bFind = false;
IFrameHeight = "300";

function onCompteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Das Konto existiert nicht.");
	}
}


</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Buchungen suchen<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
				<tr>
					<td colspan="6">Mandant <input class='libelleLongDisabled' readonly value='<%=exerciceComptable.getMandat().getLibelle()%>'> Rechnungsjahr <input class='disabled' readonly value='<%=exerciceComptable.getFullDescription()%>'></td>
				</tr>
				<tr>
					<td colspan="6"><hr></td>
				</tr>
				<tr>
					<td>&nbsp;Ansicht&nbsp;</td>
					<td><ct:FWCodeSelectTag name="reqAffichage" defaut="<%=CodeSystem.CS_AFF_PIECE_COMPTABLE%>" codeType="CGECRAFF"/></td>
					<td>&nbsp;Betrag&nbsp;</td>
					<td><ct:FWCodeSelectTag name="reqMontant" defaut="" codeType="CGECRMON" /></td>
					<td>&nbsp;Periode&nbsp;</td>
					<td>
						<ct:FWListSelectTag name="forIdPeriodeComptable" defaut="0" data="<%=globaz.helios.translation.CGListes.getPeriodeComptableListe(session,toutLexercice)%>"/>
				</tr>
				<tr>
					<td>&nbsp;Konto&nbsp;</td>
					<td>

<%
	String jspLocation = servletContext + "/heliosRoot/compte_select.jsp";
	String params = "idExerciceComptable=" + exerciceComptable.getIdExerciceComptable() + "&isMandatAVS=" + exerciceComptable.getMandat().isEstComptabiliteAVS();
%>
						<input type="hidden" name="idCompte" value="">
						<input type="hidden" name="forIdExerciceComptable" value="<%=exerciceComptable.getIdExerciceComptable()%>">
		<%
			int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();
		%>
						<ct:FWPopupList name="forIdExterne" onFailure="onCompteFailure(window.event);" validateOnChange="true" params="<%=params%>" value="" className="libelle" jspName="<%=jspLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true"/>
					</td>
					<td>&nbsp;Datum&nbsp;</td>
					<td><ct:FWCalendarTag name="forDate" value=""/></td>
					<td>&nbsp;Belegnummer&nbsp;</td>
					<td><input name='forPiece' class='libelle' value=''></td>
				</tr>
				<tr>
					<td>&nbsp;Bezeichnung&nbsp;</td>
					<td><input name='forLibelleLike' class='libelle' value=''></td>
					<td>&nbsp;Betrag CHF&nbsp;</td>
					<td><input name='forMontant' class='libelle' value=''></td>
					<td>&nbsp;Betrag Währung&nbsp;</td>
					<td><input name='forMontantMonnaieEtrangere' class='libelle' value=''></td>
				</tr>

				<tr>
					<td>&nbsp;Buchhaltung&nbsp;</td>
					<td><ct:FWCodeSelectTag name="reqComptabilite" defaut="<%=CodeSystem.CS_DEFINITIF%>" codeType="CGPRODEF" /></td>
					<td colspan="4">&nbsp;</td>
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