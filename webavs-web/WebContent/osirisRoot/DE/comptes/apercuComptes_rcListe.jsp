
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.osiris.db.comptes.*"%>
<%@ page import="globaz.osiris.translation.*" %>
<%@ page import="globaz.jade.client.util.*" %>
<%
targetLocation = "location.href";
CAApercuComptesListViewBean viewBean = (CAApercuComptesListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);

session.setAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT,viewBean);
detailLink ="osiris?userAction=osiris.comptes.apercuComptes.afficher&selectedId=";
String directLink = "osiris?userAction=osiris.comptes.apercuParSection.chercher&selectedId=";

size = viewBean.size();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH colspan="2" nowrap width="160">Nummer</TH>
    <TH nowrap width="100">Verzeichnis</TH>
	<TH width="300">Beschreibung</TH>
    <TH width="110" nowrap align="right">Saldo</TH>
    <TH width="4" align="center">MuB</TH>
    <TH width="4" align="center">&Uuml;berw.</TH>
	<TH width="70" nowrap>Gesperrt</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<style type="text/css">
.FWOptionSelectorSelect {
	width: 325px;
} 
</style>
<%
CACompteAnnexe line = (CACompteAnnexe) viewBean.getEntity(i);
directLink ="osiris?userAction=osiris.comptes.apercuParSection.chercher&" + ch.globaz.utils.VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + line.getIdTiers() + "&selectedId=";
actionDetail = "parent.location.href='" + directLink + line.getIdCompteAnnexe() + "'";
%>
	<TD class="mtd" width="16" >
	<%
		String tmpDescription = JadeStringUtil.change(line.getDescription(), "\"", "&quot;");	
		tmpDescription = JadeStringUtil.change(tmpDescription, "\'", "&#130;");
	%>

    <ct:menuPopup menu="CA-RechercheDesComptesAnnexes" label="<%=optionsPopupLabel%>" target="top.fr_main">
		<ct:menuParam key="id" value="<%=line.getIdCompteAnnexe()%>"/>
		<ct:menuParam key="selectedId" value="<%=line.getIdCompteAnnexe()%>"/>
		<ct:menuParam key="idCompteAnnexe" value="<%=line.getIdCompteAnnexe()%>"/>
		<ct:menuParam key="idTiers" value="<%=line.getIdTiers()%>"/>
		<ct:menuParam key="forIdExterneRoleLike" value="<%=line.getIdExterneRole()%>"/>
		<ct:menuParam key="forIdRole" value="<%=line.getIdRole()%>"/>
		<ct:menuParam key="idExterneRole" value="<%=line.getIdExterneRole()%>"/>
		<ct:menuParam key="description" value="<%=tmpDescription%>"/>
		<ct:menuParam key="idTiersVueGlobale" value="<%=line.getIdTiers()%>"/>
    </ct:menuPopup>
    </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=line.getIdExterneRole()%></TD>
	<TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=line.getCARole().getDescription()%></TD>
	<TD class="mtd" nowrap onClick="<%=actionDetail%>">
	<% if (!globaz.jade.client.util.JadeStringUtil.isBlank(line.getRemarque())){ %>
    <img src="<%=request.getContextPath()%>/images/attach.png" style="float:right;" title="<%=line.getRemarque()%>">
    <% } %>
    <%=line.getDescription()%></TD>
	<TD class="mtd" nowrap onClick="<%=actionDetail%>" align="right"><%=line.getSoldeFormate()%></TD>
	<TD class="mtd" width="5" nowrap onClick="<%=actionDetail%>" align="center"><%if (line.isCompteBloque(globaz.globall.util.JACalendarGregorian.todayJJsMMsAAAA())) {%><IMG src="<%=servletContext%>/images/cadenas.gif" /><%}%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" align="center"><%if (line.isASurveiller().booleanValue()){%> <IMG src="<%=servletContext%>/images/asurveiller.gif" title="Sous surveillance"><%}%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" align="center"><%if (line.getEstVerrouille().booleanValue()){%> <IMG src="<%=servletContext%>/images/cadenas.gif" /><%}%></TD>


    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>