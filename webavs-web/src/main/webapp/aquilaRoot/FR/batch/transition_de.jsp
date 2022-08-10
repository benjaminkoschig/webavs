<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.aquila.db.batch.*"%>
<%@ page import="globaz.aquila.db.access.poursuite.*"%>
<%@ page import="globaz.aquila.util.*"%>
<%@ page import="globaz.aquila.service.taxes.COTaxe"%>
<%@ page import="globaz.osiris.process.interetmanuel.visualcomponent.CAInteretManuelVisualComponent"%>
<%@ page import="globaz.globall.db.BSession"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="globaz.osiris.application.CAApplication" %>
<%@ page import="globaz.jade.client.util.JadeStringUtil" %>
<%
	COTransitionViewBean transitionViewBean = (COTransitionViewBean) session.getAttribute("viewBean");
	COContentieux viewBean = (COContentieux) session.getAttribute("contentieuxViewBean");
	selectedIdValue = viewBean.getIdContentieux();
	
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

	userActionValue = "aquila.batch.transition.effectuertransition";

	idEcran = "GCO2001";
%>
<%
    boolean eBillAquilaActif = CAApplication.getApplicationOsiris().getCAParametres().isEBillAquilaActifEtDansListeCaisses(viewBean.getSession());
	boolean eBillAccountID = !JadeStringUtil.isBlankOrZero(viewBean.getCompteAnnexe().getEBillAccountID());
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%
	// Hack : Afin de ne pas afficher toutes les lignes cach�es lors d'une erreur
	boolean memo = autoShowErrorPopup; // m�moriser l'ancien �tat
	autoShowErrorPopup = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<jsp:include flush="true" page="../menuChange.jsp"/>
<%
	autoShowErrorPopup = memo; // restaurer l'ancien �tat
%>
<script type="text/javascript">
	var nextRowToShow = <%=transitionViewBean.getShowRows()%>;
	var maxRows = <%=transitionViewBean.getMaxRows()%>;

	function validate() {
		jscss("add", document.getElementById("btnOk"), "hidden");
		//document.getElementById("btnOk").disabled = true;
		return true;
	}

	function postInit(){
		hideRows();

		// On appele la fonction qui aurait du �tre appel�e sans le hack
		showErrors();
	}

	function effacerContenu(nomChamp, texte) {
		if (document.forms[0].elements(nomChamp).value == texte) {
			document.forms[0].elements(nomChamp).value = '';
		}
	}

	function remettreContenu(nomChamp, texte) {
		if (document.forms[0].elements(nomChamp).value == '') {
			document.forms[0].elements(nomChamp).value = texte;
		}
	}

	function hideRows() {
		for (i=nextRowToShow; i<maxRows; i++) {
			document.getElementById("ligne" + i).style.display = "none";
		}
	}

	function showNextRow() {
		if (nextRowToShow < maxRows) {
			document.getElementById("ligne" + nextRowToShow).style.display = "block";
			nextRowToShow ++;
		}
	}

	function refreshEBillInputs() {
		<% if (eBillAquilaActif && eBillAccountID) {%>
			$("#eBillPrintable").attr("checked", true);
		<%} else {%>
			$("#eBillPrintable").attr("checked", false);
		<%}%>
	}

	function postInit(){
		refreshEBillInputs();
	}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
	<span class="postItIcon"><ct:FWNote sourceId="<%=viewBean.getIdContentieux()%>" tableSource="<%=viewBean.getTableName()%>"/></span>
	Ex�cution / saisie d'une �tape
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<TR>
	<TD>
		<TABLE>
			<jsp:include flush="true" page="../headerContentieux.jsp"/>
			<TR>
				<TD colspan="8"><BR><HR noshade size="3"><BR></TD>
			</TR>
			<TR>
				<TD class="label">Ex�cuter l'�tape</TD>
				<TD class="control" colspan="3">
					<INPUT type="text" value="<%=transitionViewBean.getEtapeSuivante().getLibActionLibelle()%>" class="disabled" style="width: 100%" readonly>
					<INPUT type="hidden" name="idEtapeSuivante" value="<%=transitionViewBean.getIdEtapeSuivante()%>">
					<INPUT type="hidden" name="libSequence" value="<%=viewBean.getLibSequence()%>">
				</TD>
				<TD class="label">Date d�clench.</TD>
				<TD class="control" colspan="3"><INPUT type="text" value="<%=viewBean.getProchaineDateDeclenchement()%>" class="dateDisabled" readonly></TD>
			</TR>

			<% if (eBillAquilaActif && eBillAccountID) {%>
				<tr>
				  <td nowrap><ct:FWLabel key="EBILL_PRINTABLE"/></td>
				  <td nowrap>
					<input type="checkbox" name="eBillPrintable" id="eBillPrintable" <%=(viewBean.getEBillPrintable()) ? "checked" : "unchecked"%> >
				  </td>
				  <td>&nbsp;</td>
				  <td nowrap>&nbsp;</td>
				  <td nowrap>&nbsp;</td>
				</tr>
            <%}%>

			<%
			try {
				Iterator infoIter = transitionViewBean.getEtapeSuivante().loadEtapeInfoConfigs().iterator();

				// pour pouvoir le r�cup�rer dans la etapeInfo.jsp
				request.setAttribute("globaz.aquila.db.access.batch.ETAPE_INFO", infoIter);

				while (infoIter.hasNext()) {%>
					<TR>
					<jsp:include page="etapeInfo.jsp" flush="true"/>
					<% if (infoIter.hasNext()) {%>
						<jsp:include page="etapeInfo.jsp" flush="true"/>
					<%} else {%>
					<TD colspan="4"></TD>
					</TR><%}
				}
			} catch (Exception e) {
			}

			Boolean dateExecutionRemplacee = (Boolean) request.getAttribute("globaz.aquila.db.access.batch.DATE_EXEC_REMPLACEE");

			if (dateExecutionRemplacee == null || !dateExecutionRemplacee.booleanValue()) {%>
			<TR>
				<TD class="label">Date d'ex�cution</TD>
				<TD class="control" colspan="7"><ct:FWCalendarTag name="dateExecution" value="<%=globaz.globall.util.JACalendar.todayJJsMMsAAAA()%>"/></TD>
			</TR>
			<%}%>
			<jsp:include flush="true" page="<%=\"snippets/\" + transitionViewBean.getFormSnippet()%>"/>
			<TR>
				<TD class="label">Motif</TD>
				<TD class="control" colspan="7"><TEXTAREA name="motif"></TEXTAREA></TD>
			</TR>
			<tr>
				<td colspan="8"><hr></td>
			</tr>


		<%
		if (transitionViewBean.getInteretCalcule() != null && transitionViewBean.getInteretCalcule().size()!=0){%>
			<tr>
				<td colspan="8"><h5>Int�r�ts moratoires calcul�s</h5></td>
			</tr>
			<tr>
				<th>Rubrique</th>
				<th>Montant</th>
				<th>Imputer</th>
				<td colspan="5"></td>
			</tr>
		
		<%			
			for (int i=0;i< transitionViewBean.getInteretCalcule().size(); i++ ) {
			    CAInteretManuelVisualComponent element = (CAInteretManuelVisualComponent)  transitionViewBean.getInteretCalcule().get(i);				
		%>
			<tr>
				<td><%=element.getInteretMoratoire().getRubrique().getIdExterne()%> - <%=element.getInteretMoratoire().getRubrique().getDescription()%></td>
				<td><input type="text" name="montantIM<%=i%>" value="<%= globaz.globall.util.JANumberFormatter.format(element.montantInteretTotalCalcule())%>" class="montantDisabled" readonly></td>
				<td align="center"><input type="checkbox" name="imputerIM<%=i%>" CHECKED></td>
				<td colspan="5"></td>
			</tr>
		<%}%>
		<%}%>

			<%if (transitionViewBean.getTaxes().size()!=0){%>
			<tr>
				<td colspan="8"><br/></td>
			</tr>
			<TR>
				<TD colspan="8"><h5>Taxes</h5></TD>
			</TR>
			<TR>
				<th>Libell�</th>
				<th>Montant</th>
				<th>Imputer</th>
				<td colspan="5"></td>
			</TR>
			<%}%>

			<%
				for (int i=0; i<transitionViewBean.getTaxes().size(); i++){
					 COTaxe taxe = (COTaxe)(transitionViewBean.getTaxes().get(i));
			%>
			<TR>
				<TD><%=taxe.getLibelle(transitionViewBean.getSession())%></TD>
				<TD><input type="text" name="montant<%=i%>" value="<%=taxe.getMontantTaxe()%>" class="montantDisabled"  readonly></TD>
				<TD align="center"><input type="checkbox" name="imputer<%=i%>" <%=taxe.isImputerTaxe()?"CHECKED":""%>></TD>
				<TD colspan="5"></TD>
			</TR>
			<%}%>

			<%//if (transitionViewBean.isFraisVariables() || transitionViewBean.isInteretsVariables()){%>
			<tr>
				<td colspan="8"><br/></td>
			</tr>
			<tr>
				<td colspan="8"><h5>Frais et int�r�ts</h5></td>
			</tr>
			<tr><td colspan="8">
				<table cellspacing="0" class="borderBlack">
					<tr>
						<th align="left">Rubrique</th>
						<th align="left">Libell�</th>
						<th align="left">Montant</th>
					</tr>

	<%
		for (int i=0; i < transitionViewBean.getMaxRows(); i++) {
	%>

					<tr id="ligne<%=i%>">
						<td align="left">
							<select size="1" name="rubriqueFrais<%=i%>">
								<%=transitionViewBean.getRubriquesCode() %>
							</select>
						</td>
						<td align="left"><input type="text" name="libelleFrais<%=i%>" value="" class="libelleLong" onfocus="" onblur=""></td>
						<td><input type="text" name="montantFrais<%=i%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></td>
					</tr>
	<% } %>
					<tr>
						<td align="right" height="24" colspan="3">
						<a href="#" border="noborder" onclick="showNextRow();" tabindex="-1"><img tabindex="-1" src="<%=request.getContextPath()%>/images/plus.jpg" border="0" title="Ajouter une ligne"/></a>
						</td>
					</tr>
				</table>
			</td></tr>
		</TABLE>
	</TD>
</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>

<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>