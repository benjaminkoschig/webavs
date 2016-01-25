<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@ page import="java.util.Iterator" %>
<%@ page import="globaz.aquila.db.access.poursuite.COContentieux"%>
<%@ page import="globaz.aquila.db.poursuite.COContentieuxViewBean"%>
<%@ page import="globaz.aquila.db.access.batch.COSequence"%>
<%@ page import="globaz.aquila.db.access.batch.COSequenceManager"%>
<%@ page import="globaz.aquila.db.access.batch.COEtapeManager"%>
<%@ page import="globaz.globall.db.BSessionUtil"%>
<%@page import="globaz.aquila.db.access.batch.COEtape"%>
<script language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%
	idEcran = "GCO0001";
	rememberSearchCriterias = true;
	bButtonNew = false;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CO-OptionsDefaut"/>

<script language="JavaScript">
	var usrAction = "aquila.poursuite.contentieux.lister";
	bFind = false;

	function gereControle() {
	<%
		COSequenceManager seq = new COSequenceManager();
		seq.setSession(objSession);
		try {
			seq.find();
		} catch (Exception e) {
		}

		Iterator k = seq.getContainer().iterator();
		while (k.hasNext()) {
			COSequence sequence = (COSequence) k.next();
	%>
			//document.getElementById('seq_<%=sequence.getIdSequence()%>').style.display='none';
			jscss("add", document.getElementById('seq_<%=sequence.getIdSequence()%>'), "hidden");
			document.getElementById('etapes_<%=sequence.getIdSequence()%>').name = "a";

	<% } %>

		if (document.getElementById('forIdSequence').options.value != '') {
			//document.getElementById("seq_" + document.getElementById('forIdSequence').options.value).style.display='';
			jscss("remove", document.getElementById("seq_" + document.getElementById('forIdSequence').options.value), "hidden");
			document.getElementById("etapes_" + document.getElementById('forIdSequence').options.value).name = "forIdEtape";
		}

		return true;
	}
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Suche eines Dossiers<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
		<%-- tpl:put name="zoneMain" --%>
		<tr>
		<td class="label">Mitglied-Nr.</td>
		<%
			String forNumAffilie ="";
			if (!globaz.jade.client.util.JadeStringUtil.isEmpty((String)session.getAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_NUMAFF_CTX))){
				forNumAffilie = (String)session.getAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_NUMAFF_CTX);
			%>
				<script>bFind = true</script>
			<%
			}
		%>
			<td class="control"><input type="text" name="forNumAffilie" value="<%=forNumAffilie %>"></td>
		<td class="label">Rolle</td>
			<td class="control">
				<select name="forSelectionRole" >
	              	<%=globaz.aquila.db.irrecouvrables.CORoleViewBean.createOptionsTags(objSession, request.getParameter("forIdRole"))%>
	            </select>
	        </td>
	        <td class="label">Kategorie</td>
		<td class="control">
              	<%
              		String selectCategorieSelect = globaz.osiris.parser.CASelectBlockParser.getForIdCategorieSelectBlock(objSession, true);

				if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCategorieSelect)) {
					out.print(selectCategorieSelect);
				}
              	%>
		</td>
		</tr>
		<tr>
		<td class="label">Sektion-Nr.</td>
			<td class="control"><input type="text" name="forNumSection"></td>
		<td class="label">Sequenz</td>
			<td class="control">
				<select name="forIdSequence" onChange="gereControle();">
				<%
					Iterator i = seq.getContainer().iterator();
					while (i.hasNext()) {
						COSequence sequence = (COSequence) i.next();
				%>
					<option value="<%=sequence.getIdSequence()%>"><%=sequence.getLibSequenceLibelle()%></option>
				<% } %>
				</select>
				<input type="hidden" name="orderByLibEtapeCSOrder" value="true">
			</td>
		<td class="label">Betreibung-Nr.</td>
			<td class="control"><input type="text" name="forNumPoursuite"></td>
		</tr>
		<tr>
		<td class="label">Auslösungsdatum</td>
			<td class="control"><ct:FWCalendarTag name="forDateDeclenchement" doClientValidation="CALENDAR" value=""/></td>
		<td class="label">Ausführungsdatum</td>
			<td class="control"><ct:FWCalendarTag name="forDateExecution" doClientValidation="CALENDAR" value=""/></td>
		<td class="label">Eröffnungsdatum</td>
			<td class="control"><ct:FWCalendarTag name="forDateOuverture" doClientValidation="CALENDAR" value=""/></td>
		</tr>
		<tr>
		<td class="label">Saldo</td>
			<td class="control">
				<select name="forSoldeOperator">
					<option value="=">=</option>
					<option value="&gt;=">&gt;=</option>
					<option value="&lt;=">&lt;=</option>
					<option value="&lt;&gt;" selected>&lt;&gt;</option>
				</select>
				<input type="text" name="forSolde" value="0">
			</td>
		<td class="label">1. Sortierung Vorsorgliche Liste und Dokumente</td>
		<td class="control">
              <select name="forTriListeCA" >
                <option value="1">beiliegendes Konto (Name)</option>
                <option value="2" selected>beiliegendes Konto (Nummer)</option>
              </select>
            </TD>
            <td class="label">2. Sortierung Vorsorgliche Liste und Dokumente</td>
			<td class="control">
              <select name="forTriListeSection" >
                <option value="1">Sektion (Nummer)</option>
                <option value="2">Sektion (Datum)</option>
              </select>
            </TD>
		</tr>

<%
	Iterator j = seq.getContainer().iterator();
	while (j.hasNext()) {
		COSequence sequence = (COSequence) j.next();
%>
		<tr id="seq_<%=sequence.getIdSequence()%>" <%=sequence.getLibSequence().equals(COSequence.CS_SEQUENCE_AVS)?"":"class=hidden"%>>
			<td class="label">Letzte Etappe</td>
			<td class="control" colspan="5">
				<input type="hidden" name="forTriListeCA" value="3">
				<select name=<%=sequence.getLibSequence().equals(COSequence.CS_SEQUENCE_AVS)?"forIdEtape":"a"%> id="etapes_<%=sequence.getIdSequence()%>">
					<option value=''></option>
					<%
						COEtapeManager manager = new COEtapeManager();
						manager.setSession(objSession);
						manager.setForLibSequence(sequence.getLibSequence());
						manager.setOrderByLibEtapeCSOrder("true");
						manager.find();
						if (manager.size() > 0) {
							Iterator optionsIter = manager.iterator();
							while (optionsIter.hasNext()) {
								COEtape etape = (COEtape) optionsIter.next();
					%>
					<option id="etape_<%=etape.getIdEtape()%>_<%=sequence.getIdSequence()%>" value="<%=etape.getIdEtape()%>">
						<%=etape.getLibEtapeLibelle()%>
					</option>
					<%
							}
						}
					%>
				</select>
			</td>
		</tr>
<% } %>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>