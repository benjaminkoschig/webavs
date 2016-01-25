 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%@ page import="globaz.aquila.db.suiviprocedure.*" %>
 <%
CODetailARDListViewBean viewBean = (CODetailARDListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

CODetailARDViewBean ard = null;
detailLink ="aquila?userAction=aquila.suiviprocedure.detailARD.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <%@page import="globaz.aquila.db.suiviprocedure.CODetailARDListViewBean"%>
	<%@page import="globaz.aquila.db.suiviprocedure.CODetailARDViewBean"%>
	<%@page import="globaz.globall.util.JANumberFormatter"%>
	<TH>&nbsp;</TH>
	<TH align="left">VGS Datum</TH>
	<TH align="right">Ursprünglicher Betrag</TH>
	<TH align="left">Oppositionsdatum</TH>
	<TH align="left">Einspruchsdatum KVG</TH>
	<TH align="left">Einspruchsdatum EVG</TH>
	<TH align="left">Annullierungsdatum</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%	
    ard = (CODetailARDViewBean) viewBean.get(i); 
    actionDetail = "parent.location.href='"+detailLink+ard.getIdDetailARD()+"'";
    %>
    <TD class="mtd" width="16" >
	<ct:menuPopup menu="CO-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink + ard.getIdDetailARD()%>">
		<ct:menuParam key="selectedId" value="<%=ard.getIdDetailARD()%>"/>  
    </ct:menuPopup>
	
	</TD>
    <TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=ard.getDateARD()%></TD>    
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="right"><%=JANumberFormatter.formatNoRound(ard.getMontantARD(), 2)%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=ard.getDateOpposition()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=ard.getDateRecours()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=ard.getDateRecoursTFA()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=ard.getDateAnnulation()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>