<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.pavo.db.splitting.*"%>
<%
	target = "parent.fr_detail";
	targetLocation = target+".location.href";
	detailLink ="pavo?userAction=pavo.splitting.domicileSplitting.afficher&selectedId=";

    CIDomicileSplittingListViewBean viewBean = (CIDomicileSplittingListViewBean)request.getAttribute ("viewBean");
    size = viewBean.getSize();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<Th width="16">&nbsp;</Th>

<Th width="">Beginndatum</Th>
<Th width="">Enddatum</Th>
<Th width="">Bezeichnung</Th>    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
     <% CIDomicileSplitting line = (CIDomicileSplitting)viewBean.getEntity(i); %>
     <TD class="mtd" width=""><ct:FWOptionSelectorTag name="<%=\"item\"+i%>" selectedId="<%=line.getIdDomicileSplitting()%>"/></TD>
     <% actionDetail = targetLocation+"='"+detailLink+line.getIdDomicileSplitting()+"'"; %>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getDateDebut()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getDateFin()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getLibelle()%>&nbsp;</TD>
  
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>