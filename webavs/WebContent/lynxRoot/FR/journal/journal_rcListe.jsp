<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
globaz.lynx.db.journal.LXJournalListViewBean viewBean = (globaz.lynx.db.journal.LXJournalListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

globaz.lynx.db.journal.LXJournalViewBean journal = null;

detailLink ="lynx?userAction=lynx.journal.journal.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<TH width="30">&nbsp;</TH>
	<TH width="30">Num&eacute;ro</TH>
	<TH width="110">Date comptable</TH>
	<TH width="200">Soci&eacute;t&eacute; d&eacute;bitrice</TH>
	<TH>Libell&eacute;</TH>
	<TH width="90">Propri&eacute;taire</TH>
	<TH width="110">Etat</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    journal = (globaz.lynx.db.journal.LXJournalViewBean) viewBean.getEntity(i);
    actionDetail = "parent.location.href='"+detailLink+journal.getIdJournal()+"'";
    %>

    <td class="mtd" width="16">
		<ct:menuPopup menu="LX-Journal" label="<%=optionsPopupLabel%>" target="top.fr_main">
			<ct:menuParam key="selectedId" value="<%=journal.getIdJournal()%>"/>
			<ct:menuParam key="idSociete" value="<%=journal.getIdSociete()%>"/>
			<ct:menuParam key="idJournal" value="<%=journal.getIdJournal()%>"/>
	    </ct:menuPopup>
	</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=journal.getIdJournal()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=journal.getDateValeurCG()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=journal.getNomSociete()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=journal.getLibelle()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=journal.getProprietaire()%>&nbsp;</td>
    <%
	String image = "";
	if (!journal.isOuvert()) {
		if (journal.isComptabilise()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/information.gif\"";
		} else if (journal.isTraitement()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/avertissement.gif\"";
		} else if (journal.isAnnule()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/avertissement2.gif\"";
		}

		if (!image.equals("")) {
			image += "\\>";
		}
	}
%>
    
    
    <td class="mtd" onClick="<%=actionDetail%>"><%=image%><%=journal.getUcEtat().getLibelle()%></td>

    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>