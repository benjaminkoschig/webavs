<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="globaz.naos.util.AFIDEUtil"%>
<%@page import="globaz.naos.db.ide.AFIdeSearchListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	
	AFIdeSearchListViewBean viewBean = (AFIdeSearchListViewBean)request.getAttribute ("viewBean");
	size =viewBean.size();
	wantPagination = false;
	wantPaginationPosition = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>

	
	<TH><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_NUMERO_IDE"/></TH>
	<TH><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_STATUT"/></TH>
	<TH><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_RAISON_SOCIALE"/></TH>
	<TH><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_ADRESSE"/></TH>
	

<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>
  	<%
	detailLink = AFIDEUtil.giveMeLienRegistreIde(viewBean.getSession(), viewBean.getNumeroIDE(i));
  	actionDetail = targetLocation + "='" + detailLink + "'";
	%>

	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getNumeroIDE(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getStatut(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getRaisonSociale(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getAdresse(i)%></TD>
	
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>