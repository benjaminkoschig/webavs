<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<%
		detailLink ="pavo?userAction=pavo.nnss.concordanceNNSS.afficher&selectedId=";
		globaz.pavo.db.nnss.CIConcordanceNNSSListViewBean viewBean = (globaz.pavo.db.nnss.CIConcordanceNNSSListViewBean)request.getAttribute("viewBean");
		size = viewBean.getSize();
	%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
       	<Th width="">Abr.-Nr.</th>
    	<Th width="">Vers.-Nr.</th>
	   	<Th width="">NSVN</th>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%globaz.pavo.db.nnss.CIConcordanceNNSS line = (globaz.pavo.db.nnss.CIConcordanceNNSS)viewBean.getEntity(i); %>
		<% actionDetail = targetLocation+"='"+detailLink+line.getConcordanceId()+"'"; %>
		<td class="mtd" onclick="<%=actionDetail%>"><%=line.getAffilieForRCListe()%> </td>
		<td class="mtd" onclick="<%=actionDetail%>"><%=line.getInfoAssureForRCListe()%> </td>
		<td class="mtd" onclick="<%=actionDetail%>"><%=line.getNNSSForRCListe()%> </td>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>