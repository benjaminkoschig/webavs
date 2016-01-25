<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.pavo.db.compte.*,globaz.globall.util.*"%>
<%

	globaz.pavo.db.compte.CIExceptionsListViewBean viewBean = (globaz.pavo.db.compte.CIExceptionsListViewBean)request.getAttribute("viewBean");

	size = viewBean.getSize();
	
	detailLink = "pavo?userAction=pavo.compte.exceptions.afficher&selectedId=";
	
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH>Affilié</TH>                                                                                                                                                                                                           
    <TH>Assuré</TH>
    <TH>Date de naissance</TH>
    <TH>Sexe</TH>
    <TH>Pays</TH>
    <TH>Date d'engagement</TH>
    <TH>Date de licenciement</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
globaz.pavo.db.compte.CIExceptions line = (globaz.pavo.db.compte.CIExceptions)viewBean.getEntity(i);
actionDetail = targetLocation + "='" +detailLink+line.getIdExceptions()+"'";
%>
<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getAffilieDesignation())?"&nbsp;":line.getAffilieDesignation()%></TD>
<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getAvsNomPrenom())?"&nbsp;":line.getAvsNomPrenom()%></TD>                                                                                       
<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getDateNaissance())?"&nbsp;":line.getDateNaissance()%></TD>
<TD class="mtd" align="center" onclick="<%=actionDetail%>"><%="".equals(line.getSexeForNNSS())?"&nbsp;":line.getSexeForNNSS()%></TD>
<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getPaysCode())?"&nbsp;":line.getPaysCode()%></TD>
<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getDateEngagement())?"&nbsp;":line.getDateEngagement()%></TD>
<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getDateLicenciement())?"&nbsp;":line.getDateLicenciement()%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>