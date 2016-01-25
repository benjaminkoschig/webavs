<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.pavo.db.splitting.*"%>
<%
	target = "parent.fr_detail";
	targetLocation = target+".location.href";
	detailLink = servletContext + mainServletPath+"?userAction=phenix.principale.remarqueDecision.afficher&selectedId=";

    globaz.phenix.db.principale.CPRemarqueDecisionListViewBean  viewBean = (globaz.phenix.db.principale.CPRemarqueDecisionListViewBean)request.getAttribute ("viewBean");
    size = viewBean.getSize();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	  <TH width="90%" align="left">Bemerkung auf der Verfügung</TH>
      <TH width="5%">Empl.</TH>
      <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
     <% actionDetail = targetLocation+"='"+detailLink+viewBean.getIdRemarqueDecision(i)+"';";
      	String tmp = detailLink+viewBean.getIdRemarqueDecision(i);%>
     	
      	<TD class="mtd" onclick="<%=actionDetail%>" width="90%"><%=viewBean.getTexteRemarque(i)%>&nbsp;</TD>
      	<TD class="mtd" onclick="<%=actionDetail%>" width="5%"><%=viewBean.getEmplacement(i)%>&nbsp;</TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>