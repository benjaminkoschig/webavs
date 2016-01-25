<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	target = "parent.fr_detail";
	targetLocation = target+".location.href";
	globaz.pavo.db.compte.CIInscriptionsManuellesListViewBean viewBean = (globaz.pavo.db.compte.CIInscriptionsManuellesListViewBean)request.getAttribute("viewBean");
	size=viewBean.getSize();
	detailLink = "pavo?userAction=pavo.compte.InscriptionsManuelles.afficher&selectedId=";
	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH>Mitglied</TH>
    <TH>Beitragsjahr</TH>
    <TH>Bezeichnung</TH>
    <TH>Betrag</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	globaz.pavo.db.compte.CIInscriptionsManuelles line = (globaz.pavo.db.compte.CIInscriptionsManuelles)viewBean.getEntity(i);
	actionDetail = targetLocation + "='" + detailLink + line.getIdCorrection()+"'";
%>
<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getEmployeurNoNom())?"&nbsp;":line.getEmployeurNoNom()%></TD>												                                              
<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getAnnee())?"&nbsp;":line.getAnnee()%></TD>
<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getLibelle())?"&nbsp;":line.getLibelle()%></TD>
<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getMontant())?"nbsp;":line.getMontant()%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>