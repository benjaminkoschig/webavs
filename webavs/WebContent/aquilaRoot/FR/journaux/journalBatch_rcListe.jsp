
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.aquila.db.journaux.*" %>
 <%
COJournalBatchListViewBean viewBean = (COJournalBatchListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

COJournalBatchViewBean journal = null;
detailLink ="aquila?userAction=aquila.journaux.journalBatch.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH colspan="2" align="left">Num&eacute;ro</TH>
    <TH width="332" align="left">Description</TH>
    <TH nowrap width="90">Date</TH>
    <TH width="200" align="left">Propri&eacute;taire</TH>
    <TH width="110" align="left">Etat</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    journal = (COJournalBatchViewBean) viewBean.get(i);
    actionDetail = "parent.location.href='"+detailLink+journal.getIdJournal()+"'";
    %>

    <%
	String image = "";
	if (!journal.isOuvert()) {
		if (journal.isTraite()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/information.gif\">";
		} else if (journal.isTraitement() || journal.isPartiel()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/avertissement.gif\">";
		} else if (journal.isAnnule()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/avertissement2.gif\">";
		} else if (journal.isErreur()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/erreur2.gif\">";
		}
	}
	%>

    <TD class="mtd" width="16" >
	<ct:menuPopup menu="CO-JournalElements" label="<%=optionsPopupLabel%>" target="top.fr_main">
		<ct:menuParam key="selectedId" value="<%=journal.getIdJournal()%>"/>
    </ct:menuPopup>

	</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="70"><%=journal.getIdJournal()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="332"><%=journal.getLibelle()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" nowrap width="90" align="center"><%=journal.getDateCreation()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="200"><%=journal.getUser()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="110"><%=image%><%=journal.getEtatLibelle()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>