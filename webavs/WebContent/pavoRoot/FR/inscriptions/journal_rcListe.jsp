<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	String userActionUpd = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".modifier";
    globaz.pavo.db.inscriptions.CIJournalListViewBean viewBean = (globaz.pavo.db.inscriptions.CIJournalListViewBean)request.getAttribute ("viewBean");
    detailLink ="pavo?userAction=pavo.inscriptions.journal.afficher&selectedId=";

		menuName = "journalNoRight-detail";


	size = viewBean.getSize();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

<Th colspan="2">Date</Th>
<Th>Numéro</Th>
<Th width="32%">Description</Th>
<Th width="10%">Ann&eacute;e de cotisation</Th>
<Th width="15%">Etat</Th>
<Th>Revenus inscrits</Th>
<TH>No de facturation</TH>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<% globaz.pavo.db.inscriptions.CIJournal line = (globaz.pavo.db.inscriptions.CIJournal)viewBean.getEntity(i); %>
     <TD class="mtd">        <ct:menuPopup menu="journalNoRight-detail" label="<%=optionsPopupLabel%>" target="top.fr_main"
     detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink+line.getIdJournal()%>">
			<ct:menuParam key="selectedId" value="<%=line.getIdJournal()%>"/>
			<ct:menuParam key="idJournal" value="<%=line.getIdJournal()%>"/>
			<ct:menuParam key="fromIdJournal" value="<%=line.getIdJournal()%>"/>
     	</ct:menuPopup></TD>
	 <% actionDetail = targetLocation+"='"+detailLink+line.getIdJournal()+"'";%>

	 <%
	 String image = "";
	 if (!line.getIdEtat().equals(globaz.pavo.db.inscriptions.CIJournal.CS_OUVERT)) {
		if (line.getIdEtat().equals(globaz.pavo.db.inscriptions.CIJournal.CS_COMPTABILISE)) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/information.gif\">";
		} else if (line.getIdEtat().equals(globaz.pavo.db.inscriptions.CIJournal.CS_PARTIEL)) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/avertissement.gif\">";
		}
	 }
	 %>

     <TD class="mtd" onClick="<%=actionDetail%>"><%=(line.getDateFormatee().equals(""))?"&nbsp;":line.getDateFormatee()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>"><%=(line.getIdJournal().equals(""))?"&nbsp;":line.getIdJournal()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>"><%=line.getDescription()%>&nbsp;</TD>
     <TD align="center" class="mtd" onClick="<%=actionDetail%>"><%if(!line.getAnneeCotisation().equals("0")){%><%=line.getAnneeCotisation()%><%}%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>"><%=image%><%=(line.getIdEtat().equals(""))?"&nbsp;":globaz.pavo.translation.CodeSystem.getLibelle(line.getIdEtat(), session)%>&nbsp;</TD>
     <TD align="right" class="mtd" onClick="<%=actionDetail%>"><%=(line .getTotalInscrit().equals(""))?"&nbsp;":line.getTotalInscritFormat()%>&nbsp;</TD>
	 <TD align="center" class="mtd" onClick="<%=actionDetail%>"><%="0".equals(line.getRefExterneFacturation())?"&nbsp;":line.getRefExterneFacturation()%>&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>