
<%@page import="ch.globaz.al.business.constantes.ALCSEnvoi"%>
<%@ page import="globaz.al.vb.envois.ALParametresListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>

<%@page import="globaz.al.vb.envois.ALParametresViewBean"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>

<%-- tpl:insert attribute="zoneScripts" --%>
	<%
	ALParametresListViewBean viewBean = (ALParametresListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
    detailLink = "al?userAction=al.envois.parametres.afficher&selectedId=";
	%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>

<%-- tpl:insert attribute="zoneHeaders" --%>

<script type="text/javascript">

function customOnLoad(){
}

$(function() {
});

</script>

	<th align="left"><ct:FWLabel key="AL0103_COLONNE_TYPE_PARAMETRE"/></th>
	<th align="left"><ct:FWLabel key="AL0103_COLONNE_VALEUR_PARAMETRE"/></th>
    <%-- /tpl:insert --%> 

<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>  
	<%    
	ALParametresViewBean line = (ALParametresViewBean) viewBean.getEntity(i);
	String detailUrl = "parent.location.href='" + detailLink + line.getId()+"'";
	%>  
    <%-- /tpl:insert --%>

<%@ include file="/theme/list/lineStyle.jspf" %>

	<%-- tpl:insert attribute="zoneList" --%>
	<%
	if (isSelection) { // mode sélection
		actionDetail = "document.body.innerHTML='';parent.location.href='" + globaz.fweb.taglib.FWChooserTag.getSelectLink(pageContext, i) + "'";
	} else { // détail "normal"
		actionDetail = targetLocation  + "='" + detailLink + line.getId()+"'";
	}
	%>
	<td class="mtd" nowrap align="left" onClick="<%=detailUrl%>"><%=objSession.getCodeLibelle(line.getEnvoiParametres().getCsTypeParametre())%></td>
	<td class="mtd" nowrap align="left" onClick="<%=detailUrl%>"><%=line.getEnvoiParametres().getValeurParametre()%></td>
	<%-- /tpl:insert --%>

<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	