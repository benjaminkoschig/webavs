<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.osiris.db.ventilation.CAVPImprimerVentilationListViewBean"%>
<%@page import="globaz.osiris.db.ventilation.CAVPImprimerVentilationViewBean"%>
<SCRIPT language="JavaScript">
	var selectedSections = new Array();
</SCRIPT>
<%
	CAVPImprimerVentilationListViewBean viewBean = (CAVPImprimerVentilationListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "osiris?userAction=osiris.comptes.apercuSectionDetaille.chercher&id=";
%>
<%@ taglib uri="/WEB-INF/osiris.tld" prefix="ca"%>
<script language="JavaScript">
	function toggleCheckBox(checkbox, idCompteAnnexe){
		if(checkbox.checked==true){
			checkbox.checked=false;
			for(var i=0;i<selectedSections.length;i++){
				if(selectedSections[i]==checkbox.value){
					selectedSections[i]=-1;
				}
			}
			document.forms[0].elements["selectedIds"].value=selectedSections.toString();
			document.forms[0].elements["idCompteAnnexe"].value=idCompteAnnexe.toString();
		} else {
			checkbox.checked=true;
			selectedSections.push(checkbox.value);
			document.forms[0].elements["selectedIds"].value=selectedSections.toString();
			document.forms[0].elements["idCompteAnnexe"].value=idCompteAnnexe.toString();
		}
	}

	function submitVentilation() {
		if (selectedSections.length==0) {
			alert("Il faut sélectionner au moins une section");
		} else {
			document.forms[0].submit();
		}
	}
</script>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

	<!--<TH>&nbsp;</TH>    -->
	<TH>Numéro</TH>
	<TH>Date</TH>
	<TH>Période</TH>
	<TH>Description</TH>
	<TH>Montant Facture</TH>
	<TH>Paiements compensation</TH>
	<TH>Solde</TH>
	<TH>Ventilation</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
		CAVPImprimerVentilationViewBean line = (CAVPImprimerVentilationViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdSection()+"'";
	%>
	<TD class="mtd" nowrap="nowrap" onClick="javascript:toggleCheckBox(checkbox_<%=line.getIdSection()%>,<%=line.getIdCompteAnnexe()%>)"><%=line.getIdExterne()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="javascript:toggleCheckBox(checkbox_<%=line.getIdSection()%>,<%=line.getIdCompteAnnexe()%>)"><%=line.getDateSection()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="javascript:toggleCheckBox(checkbox_<%=line.getIdSection()%>,<%=line.getIdCompteAnnexe()%>)"><%=line.getDateDebutPeriode()+"-"+line.getDateFinPeriode()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="javascript:toggleCheckBox(checkbox_<%=line.getIdSection()%>,<%=line.getIdCompteAnnexe()%>)"><%=line.getCSTypeSection()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="javascript:toggleCheckBox(checkbox_<%=line.getIdSection()%>,<%=line.getIdCompteAnnexe()%>)"><%=line.getBaseFormate()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="javascript:toggleCheckBox(checkbox_<%=line.getIdSection()%>,<%=line.getIdCompteAnnexe()%>)"><%=line.getPmtCmpFormate()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="javascript:toggleCheckBox(checkbox_<%=line.getIdSection()%>,<%=line.getIdCompteAnnexe()%>)"><%=line.getSoldeFormate()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="javascript:toggleCheckBox(checkbox_<%=line.getIdSection()%>,<%=line.getIdCompteAnnexe()%>)"><INPUT onclick="javascript:toggleCheckBox(checkbox_<%=line.getIdSection()%>,<%=line.getIdCompteAnnexe()%>)" type="checkbox" value="<%=line.getIdSection()%>" name="checkbox_<%=line.getIdSection()%>"></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<tr>
		<TD class="mtd" colspan="8" align="center">

			<FORM method="get" target="fr_main">
				<input type="hidden" name="userAction" value="osiris.ventilation.vPImprimerVentilation.afficher">
				<INPUT type="hidden" name="selectedIds">
				<INPUT type="hidden" name="idCompteAnnexe">
				<input type="button" value="Ventilation" onclick="submitVentilation();">
				<INPUT type="hidden" name="_method" value="upd">
			</FORM>
		</TD>
	</tr>

<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>