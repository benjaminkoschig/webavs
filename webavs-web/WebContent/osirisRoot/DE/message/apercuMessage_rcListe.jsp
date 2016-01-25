 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
  <%
globaz.framework.util.FWMessageManager viewBean =
(globaz.framework.util.FWMessageManager) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
globaz.framework.util.FWMessage _message = null ;
size = viewBean.size();	
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <TH align="left">Mitteilung</TH>
    <TH align="left">Quelle</TH>
    <TH align="left">Datum</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <% _message = (globaz.framework.util.FWMessage) viewBean.getEntity(i); %>
    <TD class="mtd" nowrap  style="width : 50%;"> 
      <%if (_message.getTypeMessage().equalsIgnoreCase(_message.getINFORMATION())) { %>
      <IMG src="<%=request.getContextPath()%>/images/information.gif" border=\"0\"> 
      <% } %>
      <%if (_message.getTypeMessage().equalsIgnoreCase(_message.getAVERTISSEMENT())) { %>
      <IMG src="<%=request.getContextPath()%>/images/avertissement.gif" border=\"0\"> 
      <% } %>
      <%if ((_message.getTypeMessage().equalsIgnoreCase(_message.getERREUR())) || (_message.getTypeMessage().equalsIgnoreCase(_message.getFATAL()))) { %>
      <IMG src="<%=request.getContextPath()%>/images/erreur.gif" > 
      <% } %>
      <%=_message.getMessageText()%></TD>
    <TD class="mtd" nowrap style="width : 30%;"><%=_message.getIdSource()%></TD>
    <TD class="mtd" nowrap align="right" style="width : 20%;"><%=_message.getDate()%> <%=_message.getHeure()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>