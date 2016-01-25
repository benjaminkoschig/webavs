 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.phenix.db.divers.CPRevenuCotisationCantonListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	CPRevenuCotisationCantonListViewBean viewBean = (CPRevenuCotisationCantonListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size ();
	session.setAttribute("listViewBean",viewBean);
    	detailLink ="phenix?userAction=phenix.divers.revenuCotisationCanton.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <Th nowrap width="16">&nbsp;</Th>
    <Th nowrap width="*">Kanton</Th>
    <Th width="25%">Jahr</Th>
    <Th width="25%">Von</Th>
    <TH width="20%">mit Beiträge</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%actionDetail = "parent.location.href='"+detailLink+viewBean.getIdRevCotiCanton(i)+"'";
		String tmp = detailLink+viewBean.getIdRevCotiCanton(i);%>
		<TD class="mtd" width="">
		<ct:menuPopup menu="CP-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
		</TD>
		<TD class="mtd" width="*" onclick="<%=actionDetail%>" align="left"><%=viewBean.getLibelleCanton(i)%></TD>
		<TD class="mtd" width="25%" onclick="<%=actionDetail%>" align="center"><%=viewBean.getAnneeDebut(i)%></TD>
		<TD class="mtd" width="25%" onclick="<%=actionDetail%>" align="center"><%=viewBean.getDateActivite(i)%></TD>
		<TD class="mtd" onclick="<%=actionDetail%>" width="20%" align="center"> <IMG src="<%=request.getContextPath()%><%=viewBean.getAvecCotisation(i)?"/images/select.gif" : "/images/blank.gif"%>"></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>