<%@ page import="globaz.al.vb.parametres.ALSignetsListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>

<%@page import="globaz.al.vb.parametres.ALSignetsViewBean"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.globall.util.JAVector"%>
<%@page import="ch.globaz.envoi.business.contantes.IENFormatType.ENFormatType"%>
<%@page import="ch.globaz.envoi.business.contantes.IENFormatType"%>


<%-- tpl:insert attribute="zoneScripts" --%>
	<%
	ALSignetsListViewBean viewBean = (ALSignetsListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
    detailLink = "al?userAction=al.parametres.signets.afficher&selectedId=";
	%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>

<%-- tpl:insert attribute="zoneHeaders" --%>

<script type="text/javascript">

function customOnLoad(){
}
</script>
	<th align="left"><ct:FWLabel key="AL0107_COLONNE_NOM_SIGNET"/></th>
	<th align="left"><ct:FWLabel key="AL0107_COLONNE_DESCRIPTION"/></th>
	<th align="left"><ct:FWLabel key="AL0107_COLONNE_TYPE_DE_DONNEES"/></th>
    <%-- /tpl:insert --%> 

<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>  
	<%    
	ALSignetsViewBean line = (ALSignetsViewBean) viewBean.getEntity(i);

	String detailUrl = "parent.location.href='" + detailLink + line.getSignetModel().getSimpleSignetModel().getId()+"'";
	
	String typeDataString = "";

	IENFormatType.ENFormatType[] allValues = IENFormatType.ENFormatType.values();
	for(int iValue = 0; iValue<allValues.length;iValue++){
		IENFormatType.ENFormatType currentFormat=allValues[iValue];
		if(currentFormat.getValue().equals(line.getSignetModel().getSimpleSignetModel().getCode())){
			typeDataString = currentFormat.toString();
		}
	}
	%>  
    <%-- /tpl:insert --%>

<%@ include file="/theme/list/lineStyle.jspf" %>

	<%-- tpl:insert attribute="zoneList" --%>
	<%
	// mode sélection
	if (isSelection) { // mode sélection
		actionDetail = "document.body.innerHTML='';parent.location.href='" + globaz.fweb.taglib.FWChooserTag.getSelectLink(pageContext, i) + "'";
	} else { 
		// détail "normal"
		actionDetail = targetLocation  + "='" + detailLink + line.getId()+"'";
	}
	%>
	<td class="mtd" align="left" nowrap onClick="<%=detailUrl%>"><%=line.getSignetModel().getSimpleSignetModel().getSignet()%></td>
	<td class="mtd" align="left" nowrap onClick="<%=detailUrl%>"><%=line.getSignetModel().getSimpleSignetModel().getLibelle()%></td>
	<td class="mtd" align="left" nowrap onClick="<%=detailUrl%>"><%=typeDataString%></td>
	<%-- /tpl:insert --%>

<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	