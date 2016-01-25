<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="java.math.*" %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
targetLocation = "location.href";
CAAuxiliaireManagerListViewBean viewBean = (CAAuxiliaireManagerListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
size = viewBean.size();

CAAuxiliaire auxiliaire = null;

BigDecimal montantTotal = new BigDecimal(0);

if (request.getParameter("montantTotal") != null && !request.getParameter("montantTotal").equals("")) {
	montantTotal = new BigDecimal(request.getParameter("montantTotal"));
}

findNextLink += "&montantBase=" + montantTotal;

if (request.getParameter("montantBase") != null && !request.getParameter("montantBase").equals("")) {
	findPreviousLink += "&montantTotal=" + request.getParameter("montantBase");
}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
			<TH nowrap width="100" align="left" colspan="3">Datum</TH>
			<TH nowrap align="left">Rubrik</TH>
			<TH nowrap align="left">Beschreibung</TH>
			<TH nowrap align="left">Zahlungsherkunft</TH>
			<TH nowrap align="right">Betrag</TH>
			<TH nowrap align="right">Saldo</TH>
			<% detailLink ="osiris?userAction=osiris.comptes."; %>
			<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	auxiliaire = (globaz.osiris.db.comptes.CAAuxiliaire) viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+auxiliaire.getTypeOperation().getNomPageDetail()+".afficher&selectedId="+auxiliaire.getIdOperation()+"'";
	String thisSelectedId = auxiliaire.getIdOperation();
%>

<TD class="mtd" width="16" >
<% String tmp = (detailLink+auxiliaire.getTypeOperation().getNomPageDetail()+".afficher&selectedId="+auxiliaire.getIdOperation()); %>
<ct:menuPopup menu="CA-ExtournerOperation" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>">
	<ct:menuParam key="id" value="<%=thisSelectedId%>"/>
</ct:menuPopup>
</TD>
<TD class="mtd"></TD>
<TD class="mtd" nowrap onClick="<%=actionDetail%>" width="100"><%=auxiliaire.getDate()%></TD>
<% if (auxiliaire.getCompte() != null) {%>
<TD class="mtd" nowrap onClick="<%=actionDetail%>" width="100"><%=auxiliaire.getCompte().getIdExterne()%></TD>
<% } else {%>
<TD class="mtd" nowrap onClick="<%=actionDetail%>" width="100"></TD>
<% } %>
<TD class="mtd" nowrap onClick="<%=actionDetail%>">
<%
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(auxiliaire.getLibelle())) {
		out.print(auxiliaire.getLibelle());
	} else {
		if (auxiliaire.getCompte() != null && !JadeStringUtil.isBlank(auxiliaire.getCompte().getDescription())) {
			out.print(auxiliaire.getCompte().getDescription());
		} else {
			out.print(auxiliaire.getIdTypeOperation());
		}
	}
%>

</TD>
<TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=auxiliaire.getProvenancePmtLibelle()%> </TD>

<TD class="mtd" nowrap onClick="<%=actionDetail%>" align="right"><%=JANumberFormatter.formatNoRound(auxiliaire.getMontant(),2)%></TD>

<%
	montantTotal = montantTotal.add(new BigDecimal(auxiliaire.getMontant()));
%>

<TD class="mtd" nowrap onClick="<%=actionDetail%>" align="right"><%=JANumberFormatter.formatNoRound(String.valueOf(montantTotal),2)%></TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%
		findNextLink += "&montantTotal=" + montantTotal;
	%>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>