<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.aquila.util.COGedUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></SCRIPT>
<%@ page import="globaz.aquila.db.poursuite.COContentieuxListViewBean"%>
<%@page import="globaz.aquila.api.ICOSequence"%>
<%@page import="globaz.aquila.db.access.poursuite.COContentieux"%>
<%
	COContentieuxListViewBean viewBean = (COContentieuxListViewBean) request.getAttribute("viewBean");
	String folderGed = COGedUtils.getFolder(viewBean.getSession().getApplication());
	String serviceGed = COGedUtils.getService(viewBean.getSession().getApplication());
	size = viewBean.size();
	detailLink = "aquila?userAction=aquila.poursuite.contentieux.afficher&refresh=true&libSequence="+ICOSequence.CS_SEQUENCE_AVS+"&selectedId=";

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<TH>&nbsp;</TH>
	<TH>Abr.-Nr.</TH>
	<TH>Rolle</TH>
	<TH>Mitglied</TH>
	<TH>Sektion-Nr.</TH>
	<TH>Sektionsdatum</TH>
	<TH>Saldo</TH>
	<TH>Letzte Etappe</TH>
	<TH>Auslösungsdatum</TH>
	<TH>Ausführungsdatum</TH>
	<TH>Betreibung-Nr.</TH>
	<TH>Eröffnungsdatum</TH>
	<TH>Sequenz</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
  	<%
  		COContentieux line = (COContentieux) viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdContentieux()+"'" ;
	%>
    <TD class="mtd" width="">
    <ct:menuPopup menu="CO-OptionsDossierContexte" label="<%=optionsPopupLabel%>" target="top.fr_main">
		<ct:menuParam key="selectedId" value="<%=line.getIdContentieux()%>"/>
		<ct:menuParam key="libSequence" value="<%=line.getSequence().getLibSequence()%>"/>
			<ct:menuParam key="idSection" value="<%=line.getIdSection()%>"/>
			<ct:menuParam key="noAffiliationId" value="<%=line.getCompteAnnexe().getIdExterneRole()%>"/>
			<ct:menuParam key="idRole" value="<%=line.getCompteAnnexe().getIdRole()%>"/>
			<ct:menuParam key="idContentieux" value="<%=line.getIdContentieux()%>"/>
			<ct:menuParam key="serviceNameId" value="<%=serviceGed%>"/>
			<ct:menuParam key="gedFolderType" value="<%=folderGed%>"/>					
		<ct:menuParam key="refresh" value="true"/>
		<% if (!line.getLibSequence().equals(ICOSequence.CS_SEQUENCE_ARD)) { %>
		<ct:menuExcludeNode nodeId="AQUILA_OPTIONS_ARD"/>
		<% } %>
    </ct:menuPopup>
    </TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getCompteAnnexe().getIdExterneRole()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getCompteAnnexe().getRole().getDescription()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getCompteAnnexe().getDescription()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getSection().getIdExterne()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getSection().getDateSection()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" style="text-align: right;"><%= line.getSoldeFormate()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getEtape().getLibEtapeLibelle()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getDateDeclenchement()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getDateExecution()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getNumPoursuite()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getDateOuverture()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getSequence().getLibSequenceLibelle()%>&nbsp;</TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>