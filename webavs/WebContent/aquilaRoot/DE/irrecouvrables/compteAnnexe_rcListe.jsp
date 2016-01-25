<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.aquila.db.irrecouvrables.*"%>
<%
	COCompteAnnexeListViewBean viewBean = (COCompteAnnexeListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "aquila?userAction=aquila.irrecouvrables.sections.chercher&selectedId=";
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<TH>Nummer</TH>
	<TH>Rolle</TH>
	<TH>Beschreibung</TH>
	<TH>Kontosaldo</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
		COCompteAnnexeViewBean line = (COCompteAnnexeViewBean) viewBean.getEntity(i);
		// on encode la description pour empecher les erreurs dues aux caract�res speciaux
		actionDetail = targetLocation  + "='" + detailLink + line.getIdCompteAnnexe() + "&idExterneRole=" + line.getIdExterneRole() + "&description=" + java.net.URLEncoder.encode(line.getDescription()) 
		+ "&" + ch.globaz.utils.VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + line.getIdTiers() + "'";
	%>
    <TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getIdExterneRole()%></TD>    
    <TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getRole().getDescription()%></TD>    
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDescription()%></TD>    
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getSoldeFormate()%></TD>  

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>