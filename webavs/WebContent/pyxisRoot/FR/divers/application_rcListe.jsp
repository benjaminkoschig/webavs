 
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
 <%
	globaz.pyxis.db.divers.TIApplicationListViewBean viewBean = (globaz.pyxis.db.divers.TIApplicationListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size ();
	detailLink ="pyxis?userAction=pyxis.divers.application.afficher&selectedId=";
    %>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%>
  <Th width="16">&nbsp;</Th>    
  <Th width="40%">Libellé</Th>    
  <Th nowrap width="20%">Interne</Th>
  <Th nowrap width="20%">Externe</Th>
  <Th nowrap width="20%">Identifiant</Th>
  
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%>
      <TD class="mtd" width="16" ><ct:FWOptionSelectorTag name="<%=\"item\"+i%>" selectedId="<%=viewBean.getIdApplication(i)%>"/><%=(i<9)?"<span style='background:#b0c0e0;border:solid 1 black ;margin-left:4px'>"+(i+1)+"</span>":""%></TD>
      <TD class="mtd" onClick="<%=actionDetail%>" width="40%"><%=viewBean.getLibelleApplication(i)%></TD>
      <% if (viewBean.getIsInterne(i).booleanValue()){%>
      <TD align="center" width="*"><IMG src="<%=request.getContextPath()%>/images/select2.gif" ></TD>
      <% }else{%>
      <TD class="mtd" align="center" width="*"><%=""%></TD>
	<% }%>
      <% if (viewBean.getIsExterne(i).booleanValue()){%>
      <TD align="center" width="*"><IMG src="<%=request.getContextPath()%>/images/select2.gif" ></TD>
      <% }else{%>
      <TD class="mtd" align="center" width="*"><%=""%></TD>
	<% }%>
      <TD class="mtd" onClick="<%=actionDetail%>" width="20%"><%=viewBean.getIdApplication(i)%></TD>     
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>