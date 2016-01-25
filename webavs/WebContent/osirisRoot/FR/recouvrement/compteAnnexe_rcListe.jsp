<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.osiris.db.comptes.*"%>
<%
	CACompteAnnexeManagerListViewBean viewBean = (CACompteAnnexeManagerListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "osiris?userAction=osiris.recouvrement.sections.chercher&selectedId=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<TH>Numéro</TH>
	<TH>R&ocirc;le</TH>
	<TH>Description</TH>
	<TH>Solde</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    	CACompteAnnexeViewBean line = (CACompteAnnexeViewBean) viewBean.getEntity(i);
	    detailLink = "osiris?userAction=osiris.recouvrement.sections.chercher&" + ch.globaz.utils.VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + line.getIdTiers() + "&selectedId=";

		// on encode la description pour empecher les erreurs dues aux caractères speciaux
		actionDetail = targetLocation  + "='" + detailLink + line.getIdCompteAnnexe()+"&idExterneRole="+line.getIdExterneRole()+"&description="+java.net.URLEncoder.encode(line.getDescription())+"'";
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