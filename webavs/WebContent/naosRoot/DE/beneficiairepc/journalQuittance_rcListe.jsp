
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%
    AFJournalQuittanceListViewBean viewBean = (AFJournalQuittanceListViewBean)request.getAttribute ("viewBean");
    size = viewBean.size ();
    detailLink ="naos?userAction=naos.beneficiairepc.journalQuittance.afficher&selectedId=";
    session.setAttribute("listViewBean",viewBean);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
     <%@page import="globaz.naos.db.beneficiairepc.AFQuittanceListViewBean"%>
    <%@page import="globaz.naos.db.beneficiairepc.AFQuittanceViewBean"%>
    <%@page import="globaz.naos.db.beneficiairepc.AFQuittance"%>
<%@page import="globaz.naos.db.beneficiairepc.AFJournalQuittanceListViewBean"%>
<%@page import="globaz.naos.db.beneficiairepc.AFJournalQuittance"%>
<%@page import="globaz.naos.db.beneficiairepc.AFJournalQuittanceViewBean"%>

<th>&nbsp;</th>
<TH>Journal-Nr.</TH>
<TH>Datum</TH>
<TH>Beschreibung</TH>
<TH>Status</TH>

    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%
    AFJournalQuittance lineBean  = (AFJournalQuittance)viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+lineBean.getIdJournalQuittance()+"'";
	String tmp = detailLink + lineBean.getIdJournalQuittance();
	%>
	<TD class="mtd" width="2">
	     <ct:menuPopup menu="AF-journalQuittances" label="<%=optionsPopupLabel%>" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>" target="top.fr_main">
		 	<ct:menuParam key="idJournalQuittances" value="<%=lineBean.getIdJournalQuittance()%>"/>  
		 </ct:menuPopup>
	</TD>
    <TD class="mtd" width="3%" onclick="<%=actionDetail%>"  align="center"><%=lineBean.getIdJournalQuittance()%></TD>
    <TD class="mtd" width="7%" onclick="<%=actionDetail%>"  align="center"><%=lineBean.getDateCreation()%></TD>
    <TD class="mtd" width="70%" onclick="<%=actionDetail%>"  align="left"><%=lineBean.getDescriptionJournal()%></TD>
    <TD class="mtd" width="20%" onclick="<%=actionDetail%>"  align="left"><%=lineBean.getEtatLibelle()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
		<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>