<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
	var selectedSections = new Array();
</SCRIPT>
<%@ page import="globaz.osiris.db.comptes.*"%>
<%
	CASectionManagerListViewBean viewBean = (CASectionManagerListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "osiris?userAction=osiris.recouvrement.sursis.afficher";
%>
<script language="JavaScript">
	function toggleCheckBox(checkbox, ordre){
		if(checkbox.checked==true){
			checkbox.checked=false;
			ordre.value='';
		} else {
			checkbox.checked=true;
		}
	}

	function ordreSection(ordre){
		for(var i=0;i<selectedSections.length;i++){
			var idSectionOrdre = ordre.name.substr(6, ordre.name.length);
			var idSectionArray = selectedSections[i];
			// Caractère "*" utilisé comme séparateur. Voir CARecouvrementAction.java
			if (selectedSections[i].indexOf('*') != -1) {
				idSectionArray = selectedSections[i].substr(0, selectedSections[i].indexOf('*'));
			}
			if(idSectionArray==idSectionOrdre){
				selectedSections[i]=idSectionArray + '*' + ordre.value;
			}
		}
		document.forms[0].elements["selectedIds"].value=selectedSections.toString();
	}

	function checkSelection() {
		<%
		for (int i=0; i < size ; i++) {
			CASectionViewBean ln = (CASectionViewBean) viewBean.getEntity(i);
		%>
			// Controle pour <%=ln.getIdSection()%>
			checkbox = checkbox_<%=ln.getIdSection()%>;
			ordre = ordre_<%=ln.getIdSection()%>;
			if (checkbox.checked==true) {
				if(ordre.value != ''){
					selectedSections.push(checkbox.value + '*' + ordre.value);
				} else {
					selectedSections.push(checkbox.value);
				}
			}
		<%
		}
		%>
		document.forms[0].elements["selectedIds"].value=selectedSections.toString();
		submitPage();
	}

	function submitPage() {
		if (selectedSections.length==0) {
			alert("Il faut sélectionner au moins une section");
		} else {
			document.forms[0].submit();
		}
	}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

	<!--<th>&nbsp;</th>    -->
	<th>Numéro</th>
	<th>Date</th>
	<th>Description</th>
	<th>Base</th>
	<th>Pmt / comp.</th>
	<th>Solde</th>
	<th>Couvert</th>
	<th>Ordre</th>

    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    CASectionViewBean line = (CASectionViewBean) viewBean.getEntity(i);
		//actionDetail = targetLocation  + "='" + detailLink + line.getIdSection()+"'";
		actionDetail = targetLocation  + "='" + detailLink + "'";
	%>

	<TD class="mtd" nowrap="nowrap" onClick="javascript:toggleCheckBox(checkbox_<%=line.getIdSection()%>, ordre_<%=line.getIdSection()%>)"><%=line.getIdExterne()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="javascript:toggleCheckBox(checkbox_<%=line.getIdSection()%>, ordre_<%=line.getIdSection()%>)"><%=line.getDateSection()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="javascript:toggleCheckBox(checkbox_<%=line.getIdSection()%>, ordre_<%=line.getIdSection()%>)"><%=line.getDescription()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="javascript:toggleCheckBox(checkbox_<%=line.getIdSection()%>, ordre_<%=line.getIdSection()%>)"><%=line.getBaseFormate()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="javascript:toggleCheckBox(checkbox_<%=line.getIdSection()%>, ordre_<%=line.getIdSection()%>)"><%=line.getPmtCmpFormate()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="javascript:toggleCheckBox(checkbox_<%=line.getIdSection()%>, ordre_<%=line.getIdSection()%>)"><%=line.getSoldeFormate()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="javascript:toggleCheckBox(checkbox_<%=line.getIdSection()%>, ordre_<%=line.getIdSection()%>)"><INPUT onclick="javascript:toggleCheckBox(checkbox_<%=line.getIdSection()%>, ordre_<%=line.getIdSection()%>)" type="checkbox" value="<%=line.getIdSection()%>" name="checkbox_<%=line.getIdSection()%>"></TD>

	<TD class="mtd" nowrap="nowrap"><INPUT type="text" name="ordre_<%=line.getIdSection()%>"></TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf"%>
	<%-- tpl:put name="zoneTableFooter" --%>
	<tr>
		<td class="mtd" colspan="8" align="center">

			<form method="get" target="fr_main">
			<table>
			<tr>
					<%
						// HashSet pour définir les codes systèmes qui ne doivent pas venir dans la liste
				    	java.util.HashSet exceptVen = new java.util.HashSet();
				    	exceptVen.add(globaz.osiris.db.access.recouvrement.CAPlanRecouvrement.CS_VEN_VPRO);
				    %>
				<td>Choix du mode de ventilation</td>
				<td><ct:FWCodeSelectTag codeType="OSIPLRVEN" defaut="" name="idModeVentilation" except="<%=exceptVen%>" /></td>
			</tr><tr>
				<td colspan="2">
					<input type="hidden" name="userAction" value="osiris.recouvrement.sursis.afficher">
					<input type="hidden" name="selectedIds">
					<input type="button" value="Etape suivante" onclick="checkSelection();">
					<input type="hidden" name="_method" value="add">

					<input type="hidden" name="idCompteAnnexe" value="<%= viewBean.getForIdCompteAnnexe() %>">
					<input type="hidden" name="idModeVentilation">
				</td>
			</tr></table>
			</form>
		</td>
	</tr>

<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>