
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
  <%
globaz.osiris.db.comptes.CAJournalManagerListViewBean viewBean = (globaz.osiris.db.comptes.CAJournalManagerListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
size = viewBean.size();
globaz.osiris.db.comptes.CAJournal _journal = null;
session.setAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT,viewBean);
detailLink ="osiris?userAction=osiris.comptes.apercuJournal.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH colspan="2" align="left">Num&eacute;ro</TH>
    <TH width="332" align="left">Description</TH>
    <TH nowrap width="90">Date création</TH>
    <TH nowrap width="90">Date comptable</TH>
    <TH width="50" align="left">Propri&eacute;taire</TH>
    <TH width="110" align="left">Etat</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    _journal = (globaz.osiris.db.comptes.CAJournal) viewBean.getEntity(i);
    actionDetail = "parent.location.href='"+detailLink+_journal.getIdJournal()+"'";
    %>
<!--    <TD width="17"><A href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuJournal.afficher&id=<%=_journal.getIdJournal()%>" target="fr_main"><IMG src="<%=request.getContextPath()%>/images/loupe.gif" border="0"></A></TD>-->
    <TD class="mtd" width="16" >
    <% 		if (!"1".equals(_journal.getIdJournal())) { %>
	<ct:menuPopup menu="CA-JournalOperation" label="<%=optionsPopupLabel%>" target="top.fr_main">
		<ct:menuParam key="id" value="<%=_journal.getIdJournal()%>"/>
		<% if (!_journal.isAnnule()) {%>
		<ct:menuExcludeNode nodeId="journal_rouvrir"/>
		<% } %>
    </ct:menuPopup>
    <% 	} %>

	</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="70"><%=_journal.getIdJournal()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="332"><%=_journal.getLibelle()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" nowrap width="90" align="center"><%=_journal.getDate()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" nowrap width="90" align="center"><%=_journal.getDateValeurCG()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=_journal.getProprietaire()%></TD>

    <%
	String image = "";
	if (!_journal.isOuvert()) {
		if (_journal.isComptabilise()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/information.gif\">";
		} else if (_journal.isTraitement() || _journal.isPartiel()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/avertissement.gif\">";
		} else if (_journal.isAnnule()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/avertissement2.gif\">";
		} else if (_journal.isErreur()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/erreur2.gif\">";
		}
	}
	%>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=image%><%=_journal.getUcEtat().getLibelle()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>