<%-- Page de liste des monnaies etrangeres --%>
<%-- Creator: SCE, 6.10 --%>

<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	//Les labels de cette page commence par le préfix "JSP_PC_PARAM_HOMES_L"
	PCMonnaieEtrangereListViewBean viewBean = (PCMonnaieEtrangereListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	menuName = "pegasus-menuprincipal";	
	detailLink = baseLink + "afficher&selectedId=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.pegasus.vb.monnaieetrangere.PCMonnaieEtrangereListViewBean"%>
<%@page import="globaz.pegasus.vb.monnaieetrangere.PCMonnaieEtrangereViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%><TH><ct:FWLabel key="JSP_PC_PARAM_MONETR_L_MONNAIE"/></TH>
	    <TH><ct:FWLabel key="JSP_PC_PARAM_MONETR_L_DATE_DEBUT"/></TH>
	    <TH><ct:FWLabel key="JSP_PC_PARAM_MONETR_L_DATE_FIN"/></TH>
	    <TH><ct:FWLabel key="JSP_PC_PARAM_MONETR_L_TAUX"/></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
		<%
		PCMonnaieEtrangereViewBean monnaieEtrangere = (PCMonnaieEtrangereViewBean) viewBean.getEntity(i);
		String detailUrl = "parent.fr_detail.location.href='" + detailLink + monnaieEtrangere.getMonnaieEtrangere().getId() + "'";
		%>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		

		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%= objSession.getCodeLibelle(monnaieEtrangere.getMonnaieEtrangere().getSimpleMonnaieEtrangere().getCsTypeMonnaie())%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%= monnaieEtrangere.getStringForTabValue(monnaieEtrangere.getMonnaieEtrangere().getSimpleMonnaieEtrangere().getDateDebut())%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%= monnaieEtrangere.getStringForTabValue(monnaieEtrangere.getMonnaieEtrangere().getSimpleMonnaieEtrangere().getDateFin())%></TD>
		<TD class="montant" nowrap onclick="<%=detailUrl%>"><%= JadeStringUtil.toNotNullString(monnaieEtrangere.getMonnaieEtrangere().getSimpleMonnaieEtrangere().getTaux())%></TD>
		
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>