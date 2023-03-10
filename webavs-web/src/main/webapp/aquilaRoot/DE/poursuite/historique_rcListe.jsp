<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.aquila.db.poursuite.*" %>
<%@ page import="globaz.globall.util.*" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%
	COHistoriqueListViewBean viewBean = (COHistoriqueListViewBean)request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "aquila?userAction=aquila.poursuite.historique.afficher&selectedId=";
%>
<%
    boolean eBillAquilaActif = CAApplication.getApplicationOsiris().getCAParametres().isEBillAquilaActifEtDansListeCaisses(viewBean.getSession());
%>
<%@page import="globaz.aquila.db.access.poursuite.COHistorique"%>
<%@ page import="globaz.osiris.application.CAApplication" %>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></SCRIPT>
<script type="text/javascript">

	function refreshEBillInputs() {
		<% if (eBillAquilaActif) {%>
			$('input[name="btnFind"]').click();
		<%}%>
	}

	function postInit(){
		refreshEBillInputs();
	}

</script>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<TH></TH>
	<TH>Etappe</TH>
	<TH>Grund</TH>
	<TH>Auslösungsdatum</TH>
	<TH>Ausführungsdatum</TH>
	<TH>Saldo</TH>
	<TH>Davon Gebühren</TH>
	<TH>eBill</TH>
	<TH>Benutzer</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
		COHistoriqueViewBean line = (COHistoriqueViewBean)viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdHistorique() + "'";		
	%>

	<%
		COHistorique historique = (COHistorique) viewBean.getEntity(i);
		String styleAndTitle = "";
		if (historique.isImpute().booleanValue()) {
			styleAndTitle = " style=\"font-style: italic; color:#606060;\"";
		}
		if (historique.isAnnule().booleanValue()) {
			styleAndTitle = " style=\"color:#C0C0C0;\"";
		} else {
			styleAndTitle = " style=\"color:#000000;\"";
		}


	%>

	<TD class="mtd" nowrap="nowrap" <%=styleAndTitle%>>
    <ct:menuPopup menu="CO-OptionsHistorique" label="<%=optionsPopupLabel%>" target="top.fr_main">
		<ct:menuParam key="selectedId" value="<%=line.getIdHistorique()%>"/>
		<ct:menuParam key="id" value="<%=line.getIdJournal()%>"/>
	    <% if (JadeStringUtil.isIntegerEmpty(line.getIdJournal())) { %>
			<ct:menuExcludeNode nodeId="AFFICHER_JOURNAL"/>
	    <% } %>
    </ct:menuPopup>
	</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" <%=styleAndTitle%>><%=line.getEtapeLibelle()%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" <%=styleAndTitle%>><%=line.getMotif()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" <%=styleAndTitle%>><%=line.getDateDeclenchement()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" <%=styleAndTitle%>><%=line.getDateExecution()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" style="text-align: right;" onClick="<%=actionDetail%>" <%=styleAndTitle%>><%=line.getSoldeFormate()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" style="text-align: right;" onClick="<%=actionDetail%>" <%=styleAndTitle%>><%=line.getTaxesFormatte()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" style="text-align: center;" onClick="<%=actionDetail%>" <%=styleAndTitle%>>
		<% if (line.isEBillPrinted()) {%> <IMG src="<%= servletContext %>/images/eBill_black.png" title="TransactionID : <%=line.getEBillTransactionID()%>">
		<%} else {%> &nbsp; <%}%>
	</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" <%=styleAndTitle%>><%=line.getUser()%>&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>