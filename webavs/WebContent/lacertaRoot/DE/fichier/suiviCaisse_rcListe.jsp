<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	LASuiviCaisseListViewBean viewBean = (LASuiviCaisseListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "lacerta?userAction=lacerta.fichier.suiviCaisse.afficherSuivi&selectedId=";
	targetLocation = "parent.fr_detail";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<%@page import="db.LAInsertionFichierViewBean"%>
<%@page import="db.LAFichierCentralListViewBean"%>
<%@page import="globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationListViewBean"%>
	<%@page import="globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation"%>
	<%@page import="db.LASuiviCaisseListViewBean"%>
<%@page import="db.LASuiviCaisseViewBean"%>
<TH>Genre Caisse</TH>
	<TH>N° Caisse</TH>
	<TH>N° Affilié</TH>
	<TH>Date début</TH>
	<TH>Date Fin</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
		LASuiviCaisseViewBean entity = (LASuiviCaisseViewBean) viewBean.getEntity(i);
		actionDetail = "parent.fr_detail.location.href='"+detailLink+entity.getSuiviCaisseId()+"'";
		%>
		<TD align="center" valign ="top" class="mtd" onClick="<%=actionDetail%>"><%=entity.getGenreCaisseLibelle()%>&nbsp;</TD>
		<TD align="center" valign ="top" class="mtd" onClick="<%=actionDetail%>"><%=entity.getNumCaisse()%>&nbsp;</TD>
		<TD align="center" valign ="top" class="mtd" onClick="<%=actionDetail%>"><%=entity.getNumeroAffileCaisse()%>&nbsp;</TD>
		<TD align="center" valign ="top" class="mtd" onClick="<%=actionDetail%>"><%=entity.getDateDebut()%>&nbsp;</TD>
		<TD align="center" valign ="top" class="mtd" onClick="<%=actionDetail%>"><%=entity.getDateFin()%>&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>