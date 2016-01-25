<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.helios.db.interfaces.*,globaz.helios.db.comptes.*,globaz.helios.translation.*,globaz.helios.tools.CGSessionDataContainerHelper" %>
<%
idEcran="GCF0020";
rememberSearchCriterias = true;
CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean )session.getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
bButtonNew = false;

String selectPeriodeId = "";
CGSessionDataContainerHelper sessionContainerHelper = new CGSessionDataContainerHelper();
selectPeriodeId = (String)sessionContainerHelper.getData(session,CGSessionDataContainerHelper.KEY_LAST_SELECTED_ID_PERIODE);
String selectTypeComptaId = (String)sessionContainerHelper.getData(session,CGSessionDataContainerHelper.KEY_LAST_SELECTED_ID_TYPE_COMPTA);
String toutLexercice = "Tout l'exercice";
if (languePage.equalsIgnoreCase("de")) {
	toutLexercice = "Ganze Rechnungsjahr";
}

	String aucun = "Aucun";
	if (languePage.equalsIgnoreCase("DE"))
		aucun = "Kein";

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
usrAction = "helios.comptes.soldesDesComptes.lister";
timeWaiting = 0;
bFind = true;
</SCRIPT>

<SCRIPT language="JavaScript">
function manageFuntionNotOfas() {
	if (document.getElementById('groupIdCompteOfas').checked) {
		document.getElementById('reqCritere').value = "<%=globaz.helios.translation.CodeSystem.CS_TRI_NUMERO_COMPTE%>";
		document.getElementById('reqCritere').disabled = true;
		document.getElementById('reqLibelle').value = "";
		document.getElementById('reqLibelle').disabled = true;
		document.getElementById('reqMontant').value = "<%=globaz.helios.translation.CodeSystem.CS_SHOW_MONNAIE_SUISSE%>";
		document.getElementById('reqMontant').disabled = true;
	} else {
		document.getElementById('reqCritere').disabled = false;
		document.getElementById('reqLibelle').disabled = false;
		document.getElementById('reqMontant').disabled = false;
	}
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aperçu des soldes des comptes<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
				<tr>
					<td align="left" nowrap>Mandat&nbsp;</td>
					<td align="left" nowrap><input name='_libelle' class='libelleLongDisabled' readonly value='<%=exerciceComptable.getMandat().getLibelle()%>'/></td>
					<td align="right" nowrap>Exercice&nbsp;</td>
					<td align="left" nowrap><input name='_exercice' class='disabled' readonly value='<%=exerciceComptable.getFullDescription()%>'/></td>
					<td align="right" nowrap>Période&nbsp;</td>
					<td align="left" nowrap><ct:FWListSelectTag name="reqPeriodeComptable" defaut="<%=selectPeriodeId%>" data="<%=globaz.helios.translation.CGListes.getPeriodeComptableListe(session,toutLexercice)%>"/>
						<script>
						document.getElementById("reqPeriodeComptable").style.width = "5cm";
						</script>
					</td>
				</tr>

				<tr><td colspan="6" width="100%"><hr/></td></tr>

				<tr>
					<td align="left" nowrap>Tri&nbsp;</td>
					<td align="left" nowrap><ct:FWCodeSelectTag name="reqCritere" defaut="<%=CodeSystem.CS_TRI_NUMERO_COMPTE%>" codeType="CGSCPTRI" />
					<input type="hidden" name="reqCritereOfas" value="<%=globaz.helios.translation.CodeSystem.CS_TRI_NUMERO_COMPTE%>"/>
					</td>
					<td align="right" nowrap>Comptabilité&nbsp;</td>
					<td align="left" nowrap><ct:FWCodeSelectTag name="reqComptabilite" defaut="<%=selectTypeComptaId%>" codeType="CGPRODEF" /> </td>
					<td align="right" nowrap>Montant&nbsp;</td>
					<td align="left" nowrap><ct:FWCodeSelectTag name="reqMontant" defaut="" codeType="CGECRMON" />
					<input type="hidden" name="reqMontantOfas" value="<%=globaz.helios.translation.CodeSystem.CS_SHOW_MONNAIE_SUISSE%>"/>
					</td>
				</tr>

				<tr>
					<td align="left" nowrap>A partir de&nbsp;</td>
					<td align="left" nowrap><input name='reqLibelle' class='libelleLong' value=''/></td>
					<td align="right" nowrap>Domaine&nbsp;</td>
					<td align="left" nowrap>
						<ct:FWCodeSelectTag name="reqDomaine" defaut="<%=CGCompte.CS_COMPTE_TOUS%>" codeType="CGDOMCPT" />
						<input type="hidden" name="forIdExerciceComptable" value="<%=exerciceComptable.getIdExerciceComptable()%>">
					</td>

					<% if (!exerciceComptable.getMandat().isEstComptabiliteAVS().booleanValue()) {%>
						<td align="right" nowrap>Centre de charge&nbsp;</td>
						<td align="left" nowrap>
							<ct:FWListSelectTag name="forIdCentreCharge" defaut="" data="<%=globaz.helios.translation.CGListes.getCentreChargeListe(aucun, session, exerciceComptable.getIdMandat())%>"/>
						</td>
					<%} else {%>
						<td align="right" nowrap>Plan comptable OFAS&nbsp;</td>
						<td align="left" nowrap>
							<input type="checkbox" value="true" name="groupIdCompteOfas" onclick="manageFuntionNotOfas()"/>
						</td>
					<%}%>
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