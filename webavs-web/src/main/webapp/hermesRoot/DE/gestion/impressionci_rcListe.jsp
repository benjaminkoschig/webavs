 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
globaz.hermes.db.gestion.HEImpressionciListViewBean viewBean = (globaz.hermes.db.gestion.HEImpressionciListViewBean)request.getAttribute("viewBean");
size = viewBean.size();
detailLink = "hermes?userAction=hermes.gestion.impressionci.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <th>Datum</th>
    <th>SVN</th>
    <th>SZ</th>
    <th>Kasse</th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
		globaz.hermes.db.gestion.HEImpressionciViewBean line = (globaz.hermes.db.gestion.HEImpressionciViewBean)viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdAnnonce() + "&refUnique=" + line.getRefUnique()+"&isArchivage="+line.getArchivage() + "'";    
	%>
    <td class="mtd" onClick="<%=actionDetail%>"><%= (line.getDateAnnonce().equals(""))?"&nbsp;":line.getDateAnnonce()%></td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=(line.getNumeroAVS().equals(""))?"&nbsp;":globaz.commons.nss.NSUtil.formatAVSNew(line.getNumeroAVS(),line.getNumeroAvsNNSS().equals("true"))%> </td>
    <td class="mtd" onClick="<%=actionDetail%>"><%= (line.getMotif().equals(""))?"&nbsp;":line.getMotif() %></td>
    <td class="mtd" onClick="<%=actionDetail%>"><%= (line.getNumeroCaisse().equals(""))?"&nbsp;":line.getNumeroCaisse() %></td>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>