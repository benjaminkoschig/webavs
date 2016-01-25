<%@ page import="globaz.al.vb.parametres.ALFormulesListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>

<%@page import="globaz.al.vb.parametres.ALFormulesViewBean"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.globall.util.JAVector"%>
<%@page import="globaz.globall.parameters.FWParametersCode"%>
<%@page import="globaz.globall.parameters.FWParametersSystemCodeManager"%>


<%-- tpl:insert attribute="zoneScripts" --%>
	<%
	ALFormulesListViewBean viewBean = (ALFormulesListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
    detailLink = "al?userAction=al.parametres.formules.afficher&selectedId=";
	%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>

<%-- tpl:insert attribute="zoneHeaders" --%>

<script type="text/javascript">

function customOnLoad(){
}
</script>
	<th><ct:FWLabel key="AL0105_COLONNE_CODE_DOCUMENT"/></th>
	<th><ct:FWLabel key="AL0105_COLONNE_DOCUMENT"/></th>
	<th><ct:FWLabel key="AL0105_COLONNE_DESCRIPTION"/></th>
    <%-- /tpl:insert --%> 

<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>  
	<%    
	ALFormulesViewBean line = (ALFormulesViewBean) viewBean.getEntity(i);

	String detailUrl = "parent.location.href='" + detailLink + line.getEnvoiTemplate().getEnvoiTemplateSimpleModel().getIdFormule()+"'";
	
	String codeLibelleShort = "";
	for (Iterator it = line.getListeDocumentsFromCS().iterator(); it.hasNext();) {
		FWParametersCode code = (FWParametersCode) it.next();
		if(line.getEnvoiTemplate().getFormuleList().getDefinitionformule().getCsDocument().equals(code.getId())){
			codeLibelleShort = code.getLibelle().trim(); // Libelle Short
			break;
		}
	}

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
	<td class="mtd" align="center" nowrap onClick="<%=detailUrl%>"><%=codeLibelleShort%></td>
	<td class="mtd" nowrap onClick="<%=detailUrl%>"><%=objSession.getCodeLibelle(line.getEnvoiTemplate().getFormuleList().getDefinitionformule().getCsDocument())%></td>
	<td class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getEnvoiTemplate().getFormuleList().getFormule().getLibelleDocument() %></td>
	<%-- /tpl:insert --%>

<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	