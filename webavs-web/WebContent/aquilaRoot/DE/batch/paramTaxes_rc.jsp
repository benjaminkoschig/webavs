<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	rememberSearchCriterias = true;
	idEcran = "GCO0035";
%>

<%@page import="globaz.aquila.db.batch.COParamTaxesViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.aquila.db.access.batch.COSequence"%>
<%@page import="globaz.aquila.db.access.batch.COSequenceManager"%><SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></SCRIPT>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>

<script language="JavaScript">
	usrAction = "aquila.batch.paramTaxes.lister";
	bFind = true;

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
<%-- tpl:put name="zoneTitle" --%>Suche von Gebühren<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
		<tr>
        	<td class="label">Etappe</td>
			<td class="control">
				<ct:select tabindex="0" id="forEtape" name="forEtape" wantBlank="true">
					<ct:optionsCodesSystems csFamille="COETAPP"></ct:optionsCodesSystems>
				</ct:select>
  			</td>
      	</tr>
      	<tr>
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