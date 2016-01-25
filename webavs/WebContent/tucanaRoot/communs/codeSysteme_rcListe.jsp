<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
    globaz.tucana.db.communs.TUCodeSystemeListViewBean viewBean = (globaz.tucana.db.communs.TUCodeSystemeListViewBean)request.getAttribute ("viewBean");
    size = viewBean.getSize();
    detailLink = "tucana?userAction=tucana.communs.codeSysteme.afficher&selectedId=";
    session.setAttribute("listViewBean",viewBean);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
			<TH><ct:FWLabel key="CODE" /></TH>
			<TH><ct:FWLabel key="LIBELLE" /></TH>
			<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
			<%
			globaz.tucana.db.communs.TUCodeSystemeViewBean line = (globaz.tucana.db.communs.TUCodeSystemeViewBean)viewBean.getEntity(i);
		
			%>
			<TD class="mtd" ><%="".equals(line.getCurrentCodeUtilisateur().getCodeUtilisateur())?"&nbsp;":line.getCurrentCodeUtilisateur().getCodeUtilisateur()%></TD>
			<TD class="mtd" ><%="".equals(line.getCurrentCodeUtilisateur().getLibelle())?"&nbsp;":line.getCurrentCodeUtilisateur().getLibelle()%></TD>


			<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
