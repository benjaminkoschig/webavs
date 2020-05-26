<%@page import="globaz.apg.businessimpl.service.APRulesServiceImpl"%>
<%@page import="globaz.prestation.interfaces.fx.PRGestionnaireHelper"%>
<%@page import="globaz.apg.db.annonces.APBreakRule"%>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.apg.vb.annonces.APBreakRuleListViewBean"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil" %>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%
	
	APBreakRuleListViewBean viewBean = (APBreakRuleListViewBean) request.getAttribute("viewBean");
	Iterator it = viewBean.iterator();
	size = viewBean.getSize ();

%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	    
        <TH width="150px"><ct:FWLabel key="APG_DATE_QUITTANCE"/></TH>
	    <TH><ct:FWLabel key="JSP_GESTIONNAIRE"/></TH>
	    <TH><ct:FWLabel key="APG_ERR_CODE"/></TH>
	    <TH><ct:FWLabel key="APG_MESSAGE"/></TH>
	    
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
<%

	APBreakRule courant = (APBreakRule) it.next(); 

%>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		
		<TD class="mtd" nowrap><%=courant.getDateQuittance()%>&nbsp;</TD>
		<TD class="mtd" nowrap><%=PRGestionnaireHelper.getDetailGestionnaire(objSession, courant.getGestionnaire())%>&nbsp;</TD>
		<TD class="mtd" nowrap><%=courant.getBreakRuleCode()%>&nbsp;</TD>
<% if(JadeStringUtil.isBlankOrZero(courant.getLibelleBreakCode())) {  %>
		<TD class="mtd" nowrap><%=objSession.getLabel(APRulesServiceImpl.PREFIX_LABEL+courant.getBreakRuleCode())%>&nbsp;</TD>
<%} else{  %>
		<TD class="mtd" nowrap><%=courant.getLibelleBreakCode()%>&nbsp;</TD>
<%}  %>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	