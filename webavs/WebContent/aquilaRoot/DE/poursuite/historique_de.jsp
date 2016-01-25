<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.aquila.db.poursuite.*"%>
<%@page import="globaz.aquila.db.access.poursuite.*"%>
<%@page import="globaz.aquila.db.access.batch.COEtapeInfo"%>
<%
	COHistoriqueViewBean viewBean = (COHistoriqueViewBean) session.getAttribute("viewBean");
	COContentieux contentieuxViewBean = (COContentieux) session.getAttribute("contentieuxViewBean");

	selectedIdValue = viewBean.getIdContentieux();

	idEcran = "GCO0017";

	bButtonUpdate = false;
	bButtonDelete = false;
	bButtonValidate = false;
	bButtonCancel = true;
%>
<%@page import="globaz.aquila.api.ICOContentieux"%>
<%@page import="globaz.aquila.api.ICOSequence"%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CO-OptionsDossierContexte" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=contentieuxViewBean.getIdContentieux()%>"/>
	<ct:menuSetAllParams key="libSequence" value="<%=contentieuxViewBean.getSequence().getLibSequence()%>"/>
	<% if (contentieuxViewBean.getLibSequence().equals(ICOSequence.CS_SEQUENCE_ARD)) { %>
	<ct:menuActivateNode active="yes" nodeId="AQUILA_OPTIONS_ARD"/>
	<% } else { %>
	<ct:menuActivateNode active="no" nodeId="AQUILA_OPTIONS_ARD"/>
	<% } %>
</ct:menuChange>

<SCRIPT language="javascript">
	function add() {}

	function upd() {}

	function validate() {}

	function cancel() {

	if (document.forms[0].elements('_method').value == "add")
	  document.forms[0].elements('userAction').value="back";
	}

	function del() {}

	function init(){
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<span class="postItIcon">
			<ct:FWNote sourceId="<%=contentieuxViewBean.getIdContentieux()%>" tableSource="<%=contentieuxViewBean.getTableName()%>"/>
			</span>
			Detail einer Verlaufszeile<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain" --%>
	<TR>
	<TD>
		<TABLE>
			<jsp:include page="../headerContentieuxMin.jsp" flush="true"/>
			<TR>
				<TD class="label">Betreibungsnummer</TD>
				<TD class="control"><INPUT type="text" value="<%=viewBean.getNumPoursuite()%>" class="numero disabled" style="width: 100%" readonly></TD>
				<TD colspan="6"></TD>
			</TR>
			<TR>
				<TD colspan="8"><HR></TD>
			</TR>
<%
	String annule = "";
	if (viewBean.isAnnule().booleanValue()) {
		annule = "<font color='red'><strong> annulée</strong></font>";
	}
%>
			<TR>
				<TD class="label">Etappe<%=annule %></TD>
				<TD class="control" colspan="3"><INPUT type="text" value="<%=viewBean.getEtape().getLibEtapeLibelle()%>" class="disabled" style="width: 100%" readonly></TD>
				<TD class="label">Auslösungsdatum</TD>
				<TD class="control" colspan="3"><INPUT type="text" value="<%=viewBean.getDateDeclenchement()%>" class="disabled" style="width: 100%" readonly></TD>
			</TR>
			<TR>
				<TD class="label">Grund</TD>
				<TD class="control" colspan="7"><INPUT type="text" value="<%=viewBean.getMotif()%>" class="disabled" style="width: 100%" readonly></TD>
			</TR>
			<%@page import="java.util.Iterator"%>
			<%
			// liste les etapes infos "standards"
			Iterator infoIter = viewBean.etapesInfosIterator();

			// pour pouvoir le récupérer dans la etapeInfo.jsp
			request.setAttribute("globaz.aquila.db.poursuite.ETAPE_INFO", infoIter);

			for (; infoIter.hasNext();) {%>
				<TR>
				<jsp:include page="etapeInfo.jsp" flush="true"/>
				<% if (infoIter.hasNext()) {%>
					<jsp:include page="etapeInfo.jsp" flush="true"/>
				<%} else {%>
				<TD colspan="4"></TD>
				</TR><%}
			}

			Boolean dateExecutionRemplacee = (Boolean) request.getAttribute("globaz.aquila.db.poursuite.DATE_EXEC_REMPLACEE");

			if (dateExecutionRemplacee == null || !dateExecutionRemplacee.booleanValue()) {%>
			<TR>
				<TD class="label">Ausführungsdatum</TD>
				<TD class="control"><INPUT type="text" value="<%=viewBean.getDateExecution()%>" class="date disabled" style="width: 100%" readonly></TD>
				<TD colspan="6">&nbsp;</TD>
			</TR>
			<%}%>
			<TR>
				<TD class="label">Ausführung durch</TD>
				<TD class="control"><INPUT type="text" value="<%=viewBean.getUser()%>" class="disabled" style="width: 100%" readonly></TD>
				<TD colspan="6">&nbsp;</TD>
			</TR>
			<TR>
				<TD colspan="8">&nbsp;</TD>
			</TR>
				<% for (infoIter = viewBean.etapesInfosNonConfigIterator(); infoIter.hasNext();) {
					COEtapeInfo info = (COEtapeInfo) infoIter.next(); %>
			<TR>
				<TD class="label"></TD>
				<TD class="control"><INPUT type="text" value="<%=info.getComplement3()%>" class="libelleLong disabled" readonly></TD>
				<TD class="control"><INPUT type="text" value="<%=info.getValeur()%>" class="montant disabled" readonly></TD>
				<TD colspan="5">Angerechnet&nbsp;<INPUT type="checkbox" <%=new Boolean(info.getComplement4()).booleanValue()?"CHECKED":""%>></TD>
			</TR>
				<% } %>
			<TR>
				<TD colspan="8"><HR></TD>
				<TD colspan="5">&nbsp;</TD>
			</TR>
			<TR>
				<TD colspan="2" align="right" class="label">Saldo</TD>
				<TD class="control"><INPUT type="text" value="<%=viewBean.getSolde()%>" class="montant disabled" style="width: 100%" readonly></TD>
				<TD colspan="5">&nbsp;</TD>
			</TR>
			<TR>
				<TD colspan="2" align="right" class="label">Davon Gebühren und Zins</TD>
				<TD class="control"><INPUT type="text" value="<%=viewBean.getTaxes()%>" class="montant disabled" style="width: 100%" readonly></TD>
				<TD colspan="5">&nbsp;</TD>
			</TR>
		</TABLE>
	</TD>
</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>