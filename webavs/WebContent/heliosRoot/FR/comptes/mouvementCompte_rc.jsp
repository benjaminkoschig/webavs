<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.helios.db.interfaces.*,globaz.helios.db.comptes.*,globaz.helios.translation.*,globaz.globall.util.*,globaz.framework.util.*,globaz.helios.tools.CGSessionDataContainerHelper" %>
<%
idEcran="GCF0015";
rememberSearchCriterias = true;
CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean )session.getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
CGPlanComptableViewBean viewBean = (CGPlanComptableViewBean)  session.getAttribute("viewBean");
bButtonNew = false;
IFrameHeight = "330";
String toutLexercice = "Tout l'exercice";
String labelPiece = "Pièce ";
String labelCentreCharge = "Centre ch.";

if (languePage.equalsIgnoreCase("de")) {
	toutLexercice = "Ganze Rechnungsjahr";
	labelPiece = "Belegnummer";
	labelCentreCharge = "Kostenstellen";
}


	String selectPeriodeId = "";
	CGSessionDataContainerHelper sessionContainerHelper = new CGSessionDataContainerHelper();
	selectPeriodeId = (String)sessionContainerHelper.getData(session,CGSessionDataContainerHelper.KEY_LAST_SELECTED_ID_PERIODE);
	String selectTypeComptaId = (String)sessionContainerHelper.getData(session,CGSessionDataContainerHelper.KEY_LAST_SELECTED_ID_TYPE_COMPTA);

	String noCentreCharge = request.getParameter("forIdCentreCharge");
	boolean isCentreCharge=true;
	if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(noCentreCharge))
		isCentreCharge = false;
%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CG-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CG-OnlyDetail"/>

<SCRIPT>
usrAction = "helios.comptes.mouvementCompte.listerMouvements";
bFind=false;

function changeVue() {
	if (document.getElementById("reqVue").value=='<%=CodeSystem.CS_CENTRE_CHARGE%>'){
		document.getElementById("vueLibelle").innerText='<%=labelCentreCharge%>';

	} else {
		document.getElementById("vueLibelle").innerText='<%=labelPiece%>';
	}



}

function changeComptabilite() {
	if (document.getElementById("reqComptabilite").value=='<%=CodeSystem.CS_DEFINITIF%>') {
		document.getElementById("solde").value = document.getElementById("soldeDefinitif").value;
		document.getElementById("soldeMonnaie").value = document.getElementById("soldeDefinitifMonnaie").value;
	}
	else {
		document.getElementById("solde").value = document.getElementById("soldeProvisoire").value;
		document.getElementById("soldeMonnaie").value = document.getElementById("soldeProvisoireMonnaie").value;
	}
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aperçu des mouvements du compte<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
				<tr>
					<td align="right">&nbsp;Mandat&nbsp;</td>
					<td><input name='mandatLibelle' class='libelleLongDisabled' readonly value='<%=exerciceComptable.getMandat().getLibelle()%>'></td>
					<td align="right">&nbsp;Exercice&nbsp;</td>
					<td colspan="2"><input type="hidden" name="forIdExerciceComptable" value="<%=exerciceComptable.getIdExerciceComptable()%>"><input name='exercice' class='disabled' readonly value='<%=exerciceComptable.getFullDescription()%>'></td>
					<td align="right">&nbsp;Période&nbsp;</td>
					<td>
						<ct:FWListSelectTag name="reqPeriodeComptable" defaut="<%=selectPeriodeId%>" data="<%=globaz.helios.translation.CGListes.getPeriodeComptableListe(session,toutLexercice)%>"/>
						<script>
						document.getElementById("reqPeriodeComptable").style.width = "4cm";
						</script>
					</td>
				</tr>
				<tr>
					<td colspan="7"><hr></td>
				</tr>
				<tr>
					<td align="right">&nbsp;Compte&nbsp;</td>
					<td><input type="hidden" name="idNature" value="<%=viewBean.getIdNature()%>"><input name='libelle' class='libelleLongDisabled' readonly value="<%=viewBean.getLibelle()%>"></td>

					<td align="right">&nbsp;Solde Exercice&nbsp;</td>
					<td>
						<input name='solde' class='disabled' style="text-align : right" readonly value="<%=JANumberFormatter.fmt(viewBean.getSoldeProvisoire(),true,true,false,2)%>">
					</td>
					<td>
						<input name='CHFIsoMonnaie' class='disabled' style="text-align : right; width=1cm" readonly value="CHF">

						<input name="soldeDefinitif" type="hidden" value="<%=JANumberFormatter.fmt(viewBean.getSolde(),true,true,false,2)%>">
						<input name="soldeProvisoire" type="hidden" value="<%=JANumberFormatter.fmt(viewBean.getSoldeProvisoire(),true,true,false,2)%>">
					</td>
					<td align="right">&nbsp;Vue&nbsp;</td>
					<td>
						<ct:FWCodeSelectTag name="reqVue" except="<%=CGMouvementCompteListViewBean.getExceptVue(exerciceComptable, viewBean)%>" defaut='<%=isCentreCharge?CodeSystem.CS_CENTRE_CHARGE:"0"%>' codeType="CGECRVUE" />
						<script>
							document.getElementById("reqVue").style.width = "4cm";
							document.getElementById("reqVue").onchange=changeVue;
						</script>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><input name='dummy' class='libelleLongDisabled' readonly value="<%=viewBean.getIdExterne()%>"></td>

					<%if (CGCompte.CS_MONNAIE_ETRANGERE.equals(viewBean.getIdNature())) {%>
						<td align="right">&nbsp;Solde&nbsp;</td>
						<td>
							<input name='soldeMonnaie' class='disabled' style="text-align : right" readonly value="<%=JANumberFormatter.fmt(viewBean.getSoldeProvisoireMonnaie(),true,true,false,2)%>">
						</td>
						<td>
							<input name='codeIsoMonnaie' class='disabled' style="text-align : right; width=1cm" readonly value="<%=viewBean.getCodeISOMonnaie()%>">
							<input name="soldeDefinitifMonnaie" type="hidden" value="<%=JANumberFormatter.fmt(viewBean.getSoldeMonnaie(),true,true,false,2)%>">
							<input name="soldeProvisoireMonnaie" type="hidden" value="<%=JANumberFormatter.fmt(viewBean.getSoldeProvisoireMonnaie(),true,true,false,2)%>">
						</td>
						<td colspan="2"></td>
					<%} else {%>
						<td>
							<input name='soldeMonnaie' type='hidden'>
							<input name='codeIsoMonnaie' type='hidden' value='CHF'>
							<input name="soldeDefinitifMonnaie" type='hidden'>
							<input name="soldeProvisoireMonnaie" type='hidden'>
						</td>
						<td colspan="4">&nbsp;</td>
					<%}%>
				</tr>
				<tr>
				<td colspan="7"><hr></td>
				</tr>
				<tr>
					<td align="right">&nbsp;Libellé contient&nbsp;</td>
					<td> <input name='beginWithLibelle' class='libelleLong' value=''> </td>
					<td align="right">&nbsp;Comptabilité&nbsp;</td>

					<td><ct:FWCodeSelectTag name="reqComptabilite" defaut="<%=selectTypeComptaId%>" codeType="CGPRODEF" />
						<script>
						document.getElementById("reqComptabilite").style.width = "4cm";
						document.getElementById("reqComptabilite").onchange = changeComptabilite;
						</script>
					</td>
					<td align="right">&nbsp;A partir du&nbsp;</td>
					<td colspan="2"> <ct:FWCalendarTag name="fromDate" value="" doClientValidation="CALENDAR"/> </td>

				</tr>
				<tr>

					<td align="right">&nbsp;Montant&nbsp;</td>
					<td> <input name='forMontant' class='libelle' style="text-align : right" value=''> </td>
					<td id="vueLibelle" align="right">&nbsp;<%=labelPiece%></td>
					<td><input name='forElement' class='libelle' value='<%=isCentreCharge?noCentreCharge:""%>'></td>
					<td align="right">&nbsp;<% if(exerciceComptable.getMandat().isUtiliseLivres().booleanValue() ){ %>Livre<%}%>&nbsp;</td>
					<td colspan="2">
						<% if(exerciceComptable.getMandat().isUtiliseLivres().booleanValue() ){ %>
						<ct:FWCodeSelectTag name="" defaut="" codeType="CGLIVRE" />
						<%}%>
						<input type="hidden" name="forIdCompte" value="<%=request.getParameter("selectedId")%>">

					</td>
				</tr>
						<script>
							changeVue();
						</script>


                        <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>