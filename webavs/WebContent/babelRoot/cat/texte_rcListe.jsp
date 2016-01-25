<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%--
INFO: les labels de cette page sont prefixes avec 'JSP_CTT_L'
--%>
<%

globaz.babel.vb.cat.CTTexteListViewBean viewBean = (globaz.babel.vb.cat.CTTexteListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize ();
detailLink = servletContext + mainServletPath + "?userAction=" + globaz.babel.servlet.CTMainServletAction.ACTION_TEXTES_SAISIE + ".afficher&borneInferieure=" + viewBean.getBorneInferieure() + "&selectedId=";
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH><ct:FWLabel key="JSP_CTT_L_NIVEAU"/></TH>
    <TH><ct:FWLabel key="JSP_CTT_L_POSITION"/></TH>
    <TH><ct:FWLabel key="JSP_CTT_L_DESCRIPTION"/></TH>
    <TH><ct:FWLabel key="JSP_CTT_L_LANGUE"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
globaz.babel.vb.cat.CTTexteViewBean courant = (globaz.babel.vb.cat.CTTexteViewBean) viewBean.get(i);
String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdElement() + "'";
%>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getNiveau()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getPosition()%></TD>
		<TD class="mtd" onclick="<%=detailUrl%>"><%=courant.getDescriptionEscaped()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getCodeIsoLangue()%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>