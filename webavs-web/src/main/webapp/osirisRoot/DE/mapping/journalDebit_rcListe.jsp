<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="globaz.osiris.db.mapping.CAJournalDebit"%>
<%@page import="globaz.osiris.db.mapping.CAJournalDebitListViewBean"%>
<%@page language="java" errorPage="/errorPage.jsp" %>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink ="osiris?userAction=osiris.mapping.journalDebit.afficher&selectedId=";
	CAJournalDebitListViewBean viewBean = (globaz.osiris.db.mapping.CAJournalDebitListViewBean)request.getAttribute("viewBean");
	size = viewBean.getSize();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
    <th nowrap colspan="2" width="10%">Nummer</th>
    <th nowrap width="10%">Mandat</th>
    <th nowrap width="20%">Quellekontokorrent</th>
    <th nowrap width="20%">Quellegegenleistung</th>
    <th nowrap width="20%">Zielkontokorrent</th>
    <th nowrap width="20%">Zielgegenleistung</th>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<% CAJournalDebit line = (CAJournalDebit)viewBean.getEntity(i); %>
		<% actionDetail = targetLocation+"='"+detailLink+line.getIdLink()+"'"; %>
		<td class="mtd" width="16" >
			<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+line.getIdLink())%>"/>
		</td>
	    <td class="mtd" nowrap onClick="<%=actionDetail%>"><%=line.getIdLink()%></td>
	    <td class="mtd" nowrap onClick="<%=actionDetail%>"><%=line.getIdMandat()%></td>
	    <td class="mtd" nowrap onClick="<%=actionDetail%>"><%=line.getCompteCourantSrc()%></td>
	    <td class="mtd" nowrap onClick="<%=actionDetail%>"><%=line.getContrePartieSrc()%></td>
	   	<td class="mtd" nowrap onClick="<%=actionDetail%>"><%=line.getCompteCourantDest()%></td>
	    <td class="mtd" nowrap onClick="<%=actionDetail%>"><%=line.getContrePartieDest()%></td>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>