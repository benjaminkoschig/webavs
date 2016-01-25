<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.globall.util.*" %>
<%
	globaz.aquila.db.administrateurs.COAdministrateurListViewBean viewBean = (globaz.aquila.db.administrateurs.COAdministrateurListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "aquila?userAction=aquila.administrateurs.administrateur.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<TH>&nbsp;</TH>
	<TH>N°</TH>
	<TH>Nom</TH>
	<TH>Solde</TH>
	    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%
		globaz.aquila.db.administrateurs.COAdministrateurViewBean line = (globaz.aquila.db.administrateurs.COAdministrateurViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdCompteAnnexe()+"&forIdExterneLike="+viewBean.getLikeIdExterneRole()+"&nomAffilie="+java.net.URLEncoder.encode(viewBean.getNomAffilie())+"'";
		if (line.isCompteAnnexeOk()){
	%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>

    <TD class="mtd" width="18">
    <ct:menuPopup menu="CO-OptionsAdministrateursDossier" label="<%=optionsPopupLabel%>" target="top.fr_main">
		<ct:menuParam key="selectedId" value="<%=line.getIdCompteAnnexe()%>"/>
		<ct:menuParam key="id" value="<%=line.getIdCompteAnnexe()%>"/>
		<ct:menuParam key="idCompteAnnexe" value="<%=line.getIdCompteAnnexe()%>"/>
		<ct:menuParam key="forIdExterneLike" value="<%=viewBean.getLikeIdExterneRole()%>"/>
		<ct:menuParam key="nomAffilie" value="<%=java.net.URLEncoder.encode(viewBean.getNomAffilie())%>"/>
		<ct:menuParam key="numeroAdministrateur" value="<%=line.getIdExterneRole()%>"/>
		<ct:menuParam key="idAdministrateurSrc" value="<%=line.getIdCompteAnnexe()%>"/>
		<ct:menuParam key="idTiers" value="<%=line.getIdTiers()%>"/>
    </ct:menuPopup>
    </TD>
    <TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getIdExterneRole()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDescription()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="right"><%=globaz.globall.util.JANumberFormatter.formatNoRound(line.getSolde(), 2)%></TD>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%}%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>