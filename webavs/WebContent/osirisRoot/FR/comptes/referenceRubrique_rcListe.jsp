<%-- tpl:insert page="/theme/list.jtpl" --%>

<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 

<%@page import="globaz.osiris.db.comptes.CAReferenceRubriqueManagerListViewBean"%>
<%@page import="globaz.osiris.db.comptes.CAReferenceRubrique"%>

<%
	CAReferenceRubriqueManagerListViewBean viewBean = (CAReferenceRubriqueManagerListViewBean) session.getAttribute ("listViewBean");
	size = viewBean.getSize();
	detailLink = "osiris?userAction=osiris.comptes.referenceRubrique.afficher&_s_push=no&selectedId=";
	target="parent.fr_detail";
	targetLocation = target + ".location.href";
	String parentLocation = "parent.fr_detail";
	CAReferenceRubrique line = null; 
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%> 

    <TH nowrap width="100" align="left"><ct:FWLabel key="GCA0060_RUBRIQUE"/></TH>            
    <TH align="left"><ct:FWLabel key="GCA0060_DESCRIPTION"/></TH>
    <TH align="left"><ct:FWLabel key="GCA0060_REFERENCE"/></TH>
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%> 

    <%	
    	line = (CAReferenceRubrique) viewBean.getEntity(i);
    	actionDetail = targetLocation+"='"+detailLink+line.getIdRefRubrique()+"'";
    %>
	<TD class="mtd" onClick="<%=actionDetail%>" align="left"><%=line.getRubriqueByCodeReference(line.getIdCodeReference()).getIdExterne()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" align="left"><%=line.getRubriqueByCodeReference(line.getIdCodeReference()).getDescription()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" align="left"><%=line.getCsCodeReference().getCurrentCodeUtilisateur().getLibelle()%></TD>
    
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>