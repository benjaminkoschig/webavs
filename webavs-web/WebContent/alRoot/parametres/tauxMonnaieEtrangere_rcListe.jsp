
<%@page import="globaz.al.vb.parametres.ALTauxMonnaieEtrangereListViewBean"%>
<%@page import="ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
	<%
	ALTauxMonnaieEtrangereListViewBean viewBean = (ALTauxMonnaieEtrangereListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	 detailLink = "al?userAction=al.parametres.tauxMonnaieEtrangere.afficher&selectedId=";
 
	%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>
<%@page import="globaz.globall.parameters.FWParametersSystemCodeManager" %>
<%@page import="globaz.globall.db.BSession"%>	
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>

	
	

	<th>&nbsp;</th>
		<th><ct:FWLabel key="AL0040_LIST_ENTETE_MONNAIE"/></th>
		<th><ct:FWLabel key="AL0040_LIST_ENTETE_VALIDITE_DEBUT"/></th>
		<th><ct:FWLabel key="AL0040_LIST_ENTETE_VALIDITE_FIN"/></th>
		<th><ct:FWLabel key="AL0040_LIST_ENTETE_TAUX"/></th>
		
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>  
	<%    
	TauxMonnaieEtrangereModel line =  (TauxMonnaieEtrangereModel)viewBean.getTauxMonnaieEtrangereSearch().getSearchResults()[i];	
	condition = (i % 2 == 0);
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
		<TD class="mtd">
				<ct:menuPopup menu="tauxMonnaieEtrangere-recherchePopup" target="top.fr_main">
					<ct:menuParam key="selectedId" value="<%=line.getId()%>"/>
					<!--  pour les customAction (radier), car on leur passe directement id et pas selectedId -->
					<%-- <ct:menuParam key="id" value="<%=line.getDossierComplexModel().getId()%>"/>--%>
				</ct:menuPopup>			
			</TD>
			<TD class="center" onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getTypeMonnaie())%></TD>
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getDebutTaux()%></TD>	
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getFinTaux()%></TD>		
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getTauxMonnaie()%></TD>	
		
			<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>

	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>