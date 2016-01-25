<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
   globaz.phenix.db.principale.CPRemarqueTypeListViewBean viewBean = (globaz.phenix.db.principale.CPRemarqueTypeListViewBean )request.getAttribute ("viewBean");
    size = viewBean.getSize();
    session.setAttribute("listViewBean",viewBean);
    detailLink ="phenix?userAction=phenix.principale.remarqueType.afficher&selectedId="; 
    menuName="Principale-remarqueType";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
	  <Th width="5%">&nbsp;</Th>
      <TH width="%" align="left">Remarque type</TH>
      <TH width="5%">Type</TH>
      <TH width="5%">Langue</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
     <%
		 actionDetail = "parent.location.href='"+detailLink+viewBean.getIdRemarqueType(i)+"'";
		 String tmp = detailLink + viewBean.getIdRemarqueType(i);
	 %>
      <TD class="mtd" onclick="<%=actionDetail%>" width="5%"><%=viewBean.getIdRemarqueType(i)%>&nbsp;</TD>
      <TD class="mtd" onclick="<%=actionDetail%>" width="%"><%=viewBean.getTexteRemarqueType(i)%>&nbsp;</TD>
      <TD class="mtd" onclick="<%=actionDetail%>" width="5%"><%=viewBean.getEmplacement(i)%>&nbsp;</TD>
      <TD class="mtd" onclick="<%=actionDetail%>" width="5%"><%=viewBean.getLangue(i)%>&nbsp;</TD>
     <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>