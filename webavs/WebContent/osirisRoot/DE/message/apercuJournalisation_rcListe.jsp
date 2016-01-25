 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
  <%@ page import="globaz.globall.util.*" %>
  <%
globaz.osiris.db.utils.CAElementJournalisationManagerListViewBean viewBean =
(globaz.osiris.db.utils.CAElementJournalisationManagerListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
globaz.osiris.db.utils.CAElementJournalisation _elementJournalisation = null ;
size = viewBean.size();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <TH>Datum</TH>
    <TH width="300">Beschreibung</TH>
    <TH width="400">Grund</TH>
    <TH width="80">Benutzer</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%if (!viewBean.isEmpty()){%>
    <% _elementJournalisation = (globaz.osiris.db.utils.CAElementJournalisation) viewBean.getEntity(i); %>
    <TD class="mtd" nowrap><%=_elementJournalisation.getDate()%> <%=new JATime(_elementJournalisation.getHeure()).toStr(JACalendar.getTimeSeparator())%></TD>
    <TD class="mtd" nowrap><%=_elementJournalisation.getMotifJournalisation().getCurrentCodeUtilisateur().getLibelle()%></TD>
    <TD class="mtd" nowrap><%=_elementJournalisation.getTexte()%></TD>
    <TD class="mtd" nowrap><%=_elementJournalisation.getUser()%></TD>
    <%}%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>