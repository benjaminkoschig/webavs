<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">


<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.helios.db.interfaces.*,globaz.helios.db.comptes.*,globaz.helios.translation.*, globaz.globall.util.*" %>
<%
	idEcran="GCF0004";
	rememberSearchCriterias = true;
	CGEcritureViewBean viewBean = (CGEcritureViewBean) session.getAttribute("viewBean");
	CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean) session.getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

	if (!viewBean.isJournalEditable()) {
		bButtonNew = false;
	}

	actionNew = servletContext + mainServletPath + "?userAction=helios.comptes.ecritureModele.afficher&_method=add&idJournal="+viewBean.getIdJournal();
	String actionNewEcriture = servletContext + mainServletPath + "?userAction=helios.ecritures.gestionEcriture.afficher&_method=add&idJournal="+viewBean.getIdJournal();

	btnNewLabel = "Vorlage";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CG-journaux" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdJournal()%>"/>
</ct:menuChange>

<SCRIPT>
usrAction = "helios.comptes.ecriture.listerEcritures";
bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Verbuchung<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
				<tr>
					<td colspan="4">Mandant <input class='libelleLongDisabled' readonly value='<%=exerciceComptable.getMandat().getLibelle()%>'><input type="hidden" name="forIdJournal" value="<%=viewBean.getIdJournal()%>"> Rechnungsjahr <input class='disabled' readonly value='<%=exerciceComptable.getFullDescription()%>'> Journal <input class='disabled' readonly value="<%=viewBean.getJournalLibelle()%>"></td>
				</tr>
				<tr>
					<td colspan="4"><hr></td>
				</tr>
				<tr>
					<td>Ansicht</td>
					<td><ct:FWCodeSelectTag name="reqAffichage" defaut="<%=CodeSystem.CS_AFF_PIECE_COMPTABLE%>" codeType="CGECRAFF" except="<%=viewBean.getExceptTypeAffichage()%>"/> #</td>
					<td>Betrag</td>
					<td><ct:FWCodeSelectTag name="reqMontant" defaut="" codeType="CGECRMON" /></td>
				</tr>
				<tr>
					<td>Ab</td>
					<td><input name='reqLibelle' class='libelle' value=''></td>
					<td>Sortierung</td>
					<td><ct:FWCodeSelectTag name="reqCritere" defaut="<%=CodeSystem.CS_TRI_NUMERO_COMPTE%>" codeType="CGECRTRI" /></td>
				</tr>


                        <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%
				if (viewBean.isJournalEditable()) {
				%>
					<INPUT type="button" name="btnNewEcriture" value="Neu" onClick="document.location.href='<%=actionNewEcriture%>'" >
				<%
				}
				%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%
	String totalDebit = JANumberFormatter.fmt(globaz.helios.db.utils.CGUtils.getTotalDebit(objSession, viewBean.getIdJournal()),true,true,false,2);
	String totalCredit = JANumberFormatter.fmt(globaz.helios.db.utils.CGUtils.getTotalCredit(objSession, viewBean.getIdJournal()),true,true,false,2);
%>
	<table class="find" cellspacing="0">
		<tr class="somme">
			<td class="mtdBold">&nbsp;Total</td>
			<td class="mtdMontant" id="totalDebit" <%=totalDebit.equals(totalCredit)?"":"style=\"background-color:#FF0000; font-weight:bold\""%>><%=totalDebit%>&nbsp;</td>
			<td class="mtdMontant" id="totalCredit" <%=totalDebit.equals(totalCredit)?"":"style=\"background-color:#FF0000; font-weight:bold\""%>><%=totalCredit%>&nbsp;</td>
			<td width="12px" align="right">&nbsp;</td>
		</tr>
	</table>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>