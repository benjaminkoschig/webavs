<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.pavo.db.splitting.*,globaz.globall.util.*"%>
<%
	target = "parent.fr_detail";
	targetLocation = target+".location.href";
	detailLink ="pavo?userAction=pavo.splitting.revenuSplitting.afficher&selectedId=";
    globaz.pavo.db.splitting.CIRevenuSplittingListViewBean viewBean = (globaz.pavo.db.splitting.CIRevenuSplittingListViewBean)request.getAttribute ("viewBean");
    size =viewBean.getSize();
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 

<Th width="16">&nbsp;</Th>

<Th width="">Année</Th>
<Th width="">Revenu</Th>    

    
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
     <% CIRevenuSplitting line = (CIRevenuSplitting)viewBean.getEntity(i); %>
     <TD class="mtd" width=""><ct:FWOptionSelectorTag name="<%=\"item\"+i%>" selectedId="<%=line.getIdRevenuSplitting()%>"/></TD>
     <% actionDetail = targetLocation+"='"+detailLink+line.getIdRevenuSplitting()+"'"; %>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getAnnee()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="" align="right"><%=JANumberFormatter.format(JANumberFormatter.formatZeroValues(line.getRevenu(),true,true))%>&nbsp;</TD>
  
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> 
	<% if(size!=0) {
		CIRevenuSplitting line = (CIRevenuSplitting)viewBean.getEntity(size-1); %>
		<input type="hidden" name="lastYear" value="<%=Integer.parseInt(line.getAnnee())+1%>">
	<% } %>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>