
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@ page import="globaz.helios.db.comptes.*, globaz.framework.util.*" %>
<%
    	CGJournalListViewBean viewBean = (CGJournalListViewBean)request.getAttribute ("viewBean");
    	size =viewBean.getSize();
    	detailLink ="helios?userAction=helios.comptes.journal.afficher&selectedId=";
%>


<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

<Th width="16">&nbsp;</Th>

<Th width="">Nummer</Th>
<Th width="">Datum</Th>
<Th width="">Bezeichnung</Th>
<Th width="">Benutzer</Th>
<Th width="">Status</Th>



<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	CGJournal entity = (CGJournal)viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+entity.getIdJournal()+"'";
%>
     <TD class="mtd" width="">
     <ct:menuPopup menu="CG-journaux" label="<%=optionsPopupLabel%>" target="top.fr_main">
		<ct:menuParam key="selectedId" value="<%=entity.getIdJournal()%>"/>
	 </ct:menuPopup>
     </TD>

     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=(entity.getNumero().equals(""))?"&nbsp;":entity.getNumero()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=(entity.getDate().equals(""))?"&nbsp;":entity.getDate()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=(entity.getLibelle().equals(""))?"&nbsp;":entity.getLibelle()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=(entity.getProprietaire().equals(""))?"&nbsp;":entity.getProprietaire()%>&nbsp;</TD>
<%
	String image = "";
	if (!entity.isOuvert()) {
		if (entity.isComptabilise()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/information.gif\"";
		} else if (entity.isTraitement() || entity.isPartiel()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/avertissement.gif\"";
		} else if (entity.isAnnule()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/avertissement2.gif\"";
		} else if (entity.isErreur()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/erreur2.gif\"";
		}

		if (!image.equals("")) {
			FWLog log = entity.retrieveLog();
			if (log != null && !log.isNew()) {
				image += " title=\""+log.getMessagesToString()+"\"";
			}

			image += "\\>";
		}
	}
%>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=image%><%=(entity.getEtatLibelle().equals(""))?"&nbsp;":entity.getEtatLibelle()%>&nbsp;</TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>