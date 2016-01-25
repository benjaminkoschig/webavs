<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.musca.db.facturation.FAModuleFacturationListViewBean viewBean = (globaz.musca.db.facturation.FAModuleFacturationListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size();
	detailLink = "musca?userAction=musca.facturation.moduleFacturation.afficher&selectedId=";
	session.setAttribute("listViewBean",viewBean);
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <th nowrap width="16">&nbsp;</th>
      <th  width="35%">Module</th>
      <TH  width="8%">Numéro</TH>
      <th  width="25%">Type de module</th>
      <th  width="8%">Afacts modifiables</th>
      <th  width="8%">Niveau d'appel</th>
 <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	actionDetail = "parent.location.href='"+detailLink+viewBean.getIdModuleFacturation(i)+"'";
	String detailAction = detailLink + viewBean.getIdModuleFacturation(i);
%>
    <TD class="mtd" width="16" >
    	<ct:menuPopup menu="FA-Detail" labelId="OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailAction%>" />
    </TD>
    <TD class="mtd" width="35%" onClick="<%=actionDetail%>"><%=viewBean.getLibelleModule(i)%></TD>
    <TD class="mtd" width="8%" align="right" onClick="<%=actionDetail%>"><%=viewBean.getIdModuleFacturation(i)%></TD>
    <TD class="mtd" width="25%" onClick="<%=actionDetail%>"><%=viewBean.getLibelleType(i)%></TD>
      <% if (viewBean.getModifierAfact(i).booleanValue()){%>
      <TD class="mtd" align="center"><IMG src="<%=request.getContextPath()%>/images/ok.gif" >&nbsp;</TD>
      <% }else{%>
      <TD class="mtd" align="center" width="10">&nbsp;</TD>
	<% }%>
      <TD class="mtd" width="8%" onClick="<%=actionDetail%>"><%=viewBean.getNiveauAppel(i)%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>