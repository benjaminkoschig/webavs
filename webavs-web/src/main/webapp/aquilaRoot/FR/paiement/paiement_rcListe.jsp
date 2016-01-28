<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.aquila.db.paiement.COPaiementListViewBean viewBean = (COPaiementListViewBean) request.getAttribute("viewBean");

	size = viewBean.size();
	detailLink = "aquila?userAction=aquila.paiement.paiement.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<%@page import="globaz.aquila.db.paiement.COPaiementViewBean"%>
<%@page import="globaz.aquila.db.paiement.COPaiementListViewBean"%>
<%@page import="globaz.aquila.db.access.paiement.COPaiement"%>
<%@page import="globaz.globall.util.JANumberFormatter"%>
<%@page import="globaz.aquila.api.ICOSequence"%>
<TH>&nbsp;</TH>
<TH>Num�ro</TH>
<TH>R�le</TH>
	<TH>Compte annexe</TH>
	<TH>section</TH>
	<TH>Date op�ration</TH>
	<TH>Montant</TH>
	<TH>Etape</TH>
	    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
	COPaiementViewBean line = (COPaiementViewBean)viewBean.getEntity(i);
	detailLink = "aquila?userAction=aquila.poursuite.historique.chercher&refresh=true&libSequence="+line.getIdSequence()+"&selectedId=";
	//detailLink = "aquila?userAction=aquila.poursuite.historique.chercher&refresh=true&selectedId=";
	actionDetail = targetLocation  + "='" + detailLink + line.getIdContentieux() +"'" ;
	%>
	<TD class="mtd" width="18"></TD>
    <TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><% if (line.getCompteAnnexe() != null) {%><%=line.getCompteAnnexe().getIdExterneRole()%><%}%></TD>
    <TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><% if (line.getCompteAnnexe() != null) {%><%=line.getCompteAnnexe().getCARole().getDescription()%><%}%></TD>
    <TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><% if (line.getCompteAnnexe() != null) {%><%=line.getCompteAnnexe().getDescription()%><%}%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><% if (line.getSection() != null) {%><%=line.getSection().getIdExterne()%> <%=line.getSection().getDescription()%><%}%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateOperation()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="right"><%=JANumberFormatter.format(line.getMontant())%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><% if (line.getSection() != null) {%><%=line.getLibelleEtape()%> <%}%></TD>

		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>