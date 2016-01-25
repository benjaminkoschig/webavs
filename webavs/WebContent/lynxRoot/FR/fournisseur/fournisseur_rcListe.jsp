<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
globaz.lynx.db.fournisseur.LXFournisseurListViewBean viewBean = (globaz.lynx.db.fournisseur.LXFournisseurListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

globaz.lynx.db.fournisseur.LXFournisseurViewBean fournisseur = null;

detailLink ="lynx?userAction=lynx.fournisseur.fournisseur.afficher&selectedId=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>

    <TH width="30">&nbsp;</TH>
	<TH width="100">Num&eacute;ro</TH>
	<TH width="120">Num. TVA / IDE</TH>
	<TH width="200">Nom</TH>
	<TH width="200">Compl&eacute;ment</TH>
	<TH width="400">Adresse</TH>
	<TH width="15">Bloqu&eacute;</TH>

<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

    <%
    fournisseur = (globaz.lynx.db.fournisseur.LXFournisseurViewBean) viewBean.getEntity(i);
    actionDetail = "parent.location.href='" + detailLink + fournisseur.getIdFournisseur() + "&idFournisseur=" + fournisseur.getIdFournisseur() + "&idExterne=" + fournisseur.getIdExterne() + "'";

    String bloque = "";
    if(fournisseur.getEstBloque().booleanValue()) {
    	bloque = "<img title=\"Bloqué\" src=\"" + request.getContextPath()+"/images/cadenas.gif\"";
    }
    %>

    <td class="mtd" width="16">
	<ct:menuPopup menu="LX-Fournisseur" label="<%=optionsPopupLabel%>" target="top.fr_main">
		<ct:menuParam key="selectedId" value="<%=fournisseur.getIdFournisseur()%>"/>
		<ct:menuParam key="idFournisseur" value="<%=fournisseur.getIdFournisseur()%>"/>
		<ct:menuParam key="idExterne" value="<%=fournisseur.getIdExterne()%>"/>
		<ct:menuParam key="lynx.fournisseur.idExterne" value="<%=fournisseur.getIdExterne()%>"/>

		<ct:menuExcludeNode nodeId="lynx_ged"/>
    </ct:menuPopup>
	</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=fournisseur.getIdExterne()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=fournisseur.getNoTva()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=fournisseur.getNom()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=fournisseur.getComplement()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=fournisseur.getAdresse()%>&nbsp;</td>
	<td class="mtd" onClick="<%=actionDetail%>" align="center"><%=bloque%>&nbsp;</td>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>