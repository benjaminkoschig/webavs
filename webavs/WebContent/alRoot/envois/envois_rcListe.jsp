
<%@page import="ch.globaz.al.business.constantes.ALCSEnvoi"%>
<%@ page import="globaz.al.vb.envois.ALEnvoisListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>

<%@page import="globaz.al.vb.envois.ALEnvoisViewBean"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>

<%-- tpl:insert attribute="zoneScripts" --%>
	<%
	ALEnvoisListViewBean viewBean = (ALEnvoisListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
    detailLink = "al?userAction=al.envois.envois.afficher&selectedId=";
    int customIncrement=0;
    String customLastJobId="-1";
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

	<th><ct:FWLabel key="AL0101_COLONNE_JOB"/></th>
	<th><ct:FWLabel key="AL0101_COLONNE_DATE"/></th>
	<th><ct:FWLabel key="AL0101_COLONNE_STATUS"/></th>
	<th><ct:FWLabel key="AL0101_COLONNE_DETAILS"/></th>
	<th align="left"><ct:FWLabel key="AL0101_COLONNE_DESCRIPTION"/></th>
	<th><ct:FWLabel key="AL0101_COLONNE_UTILISATEUR"/></th>
    <%-- /tpl:insert --%> 

<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>  
	<%    
	ALEnvoisViewBean line = (ALEnvoisViewBean) viewBean.getEntity(i);
	// Uniquement 1 ligne par job
	if(customLastJobId.equals(line.getEnvoiComplexModel().getEnvoiJobSimpleModel().getId())){
		continue;
	}else{
		customIncrement++;
		customLastJobId=line.getEnvoiComplexModel().getEnvoiJobSimpleModel().getId();
		condition = (customIncrement % 2 == 0);
	}

	String detailUrl = "parent.location.href='" + detailLink + line.getId()+"'";
	String nbDocuments = line.getNbDocuments();
	
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
	<td class="mtd" nowrap align="center" onClick="<%=detailUrl%>"><%=line.getEnvoiComplexModel().getEnvoiJobSimpleModel().getId()%></td>
	<td class="mtd" nowrap align="center" onClick="<%=detailUrl%>"><%=line.getEnvoiComplexModel().getEnvoiJobSimpleModel().getJobDate() %></td>
	<td class="mtd" nowrap align="center" onClick="<%=detailUrl%>"><%=objSession.getCodeLibelle(line.getEnvoiComplexModel().getEnvoiJobSimpleModel().getJobStatus())%></td>
	<td class="mtd" nowrap align="center" onClick="<%=detailUrl%>"><%=nbDocuments%> document(s)</td>
	<td class="mtd" nowrap align="left" onClick="<%=detailUrl%>"><%=line.getEnvoiComplexModel().getEnvoiJobSimpleModel().getJobDescription() %></td>
	<td class="mtd" nowrap align="center" onClick="<%=detailUrl%>"><%=line.getEnvoiComplexModel().getEnvoiJobSimpleModel().getJobUser() %></td>
	<%-- /tpl:insert --%>

<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	