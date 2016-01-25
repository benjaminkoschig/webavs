<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
		detailLink ="osiris?userAction=osiris.ventilation.tauxRubriques.afficher&selectedId=";
		globaz.osiris.db.ventilation.CATauxRubriquesListViewBean viewBean = (globaz.osiris.db.ventilation.CATauxRubriquesListViewBean)request.getAttribute("viewBean");
		size = viewBean.getSize();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <%@page import="globaz.osiris.db.ventilation.CATauxRubriquesViewBean"%>
		<th align="left">Rubrik</th>
		<th align="right">Verbandskasse</th>
	    <th align="right">Datum</th>
	    <th align="right">Beitragsatz Arbeitgeber</th>
	    <th align="right">Beitragsatz Arbeitnehmer</th>
	    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%globaz.osiris.db.comptes.CATauxRubriques line = (globaz.osiris.db.comptes.CATauxRubriques)viewBean.getEntity(i); %>
	<% actionDetail = targetLocation+"='"+detailLink+line.getIdTauxRubrique()+"'"; %>
	<td class="mtd" onclick="<%=actionDetail%>"><%=line.getDescription()%> </td>
	<td class="mtd" onclick="<%=actionDetail%>" align="right"><%=line.getCaisseProfessionnelleNumero()%> </td>
	<td class="mtd" onclick="<%=actionDetail%>" align="right"><%=line.getDate()%> </td>
	<td class="mtd" onclick="<%=actionDetail%>" align="right"><%=line.getTauxEmployeur()%> </td>
	<td class="mtd" onclick="<%=actionDetail%>" align="right"><%=line.getTauxSalarie()%> </td>

		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>