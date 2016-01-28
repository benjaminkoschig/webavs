 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.phenix.db.divers.CPParametreCantonListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	CPParametreCantonListViewBean viewBean = (CPParametreCantonListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size ();
	session.setAttribute("listViewBean",viewBean);
    	detailLink ="phenix?userAction=phenix.divers.parametreCanton.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <Th nowrap width="16">&nbsp;</Th>
    <Th nowrap width="15%">Kanton</Th>
    <Th width="20%">Mitgliedsart</Th>
    <Th width="25%">Funktionalität</Th>
    <Th width="25%">Modus</Th>
    <Th width="*">Von</Th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%actionDetail = "parent.location.href='"+detailLink+viewBean.getIdParametreCanton(i)+"'";
		String tmp = detailLink+viewBean.getIdParametreCanton(i);%>
		<TD class="mtd" width="">
		<ct:menuPopup menu="CP-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
		</TD>
		<TD class="mtd" width="15%" onclick="<%=actionDetail%>" align="left"><%=viewBean.getLibelleCanton(i)%></TD>
		<TD class="mtd" width="20%" onclick="<%=actionDetail%>" align="left"><%=viewBean.getLibelleGenreAffilie(i)%></TD>
		<TD class="mtd" width="25%" onclick="<%=actionDetail%>" align="left"><%=viewBean.getLibelleTypeParametre(i)%></TD>
		<TD class="mtd" width="25%" onclick="<%=actionDetail%>" align="left"><%=viewBean.getLibelleCodeParametre(i)%></TD>
		<TD class="mtd" width="*" onclick="<%=actionDetail%>" align="left"><%=viewBean.getDateDebut(i)%></TD>
	 </tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>