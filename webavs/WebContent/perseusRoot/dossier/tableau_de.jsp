<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="globaz.perseus.utils.dossier.PFCelluleHandler"%>
<%@page import="globaz.perseus.utils.dossier.PFLigneHandler"%>
<%@page import="globaz.perseus.utils.dossier.PFTableauHandler"%>
<%@page import="globaz.perseus.vb.dossier.PFTableauViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<%

	PFTableauViewBean viewBean = (PFTableauViewBean) session.getAttribute("viewBean"); 
	idEcran="PPF0521";
	autoShowErrorPopup = true;
	
	bButtonDelete = false;
	bButtonUpdate = false;
	

%>

<style type="text/css"> 

.areaDataTable {
    border: 1px solid #226194;
    border-collapse: collapse;
  	empty-cells: show;
    font-family: Verdana,Arial;
    font-size: x-small;
}

.areaDataTable th {
    border-color: #226194 #88AAFF #88AAFF;
    border-left: 1px solid #88AAFF;
    border-right: 1px solid #88AAFF;
    border-style: solid;
    border-width: 1px;
}

.areaDataTable td {
    border: 1px solid #B3C4DB;
    text-align: center;
	background-color: white;
    
}

</style>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<link type="text/css" href="<%=servletContext%>/theme/ajax/templateZoneAjax.css" rel="stylesheet" />


<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_DOS_R_TITRE_TABLEAU"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
						<tr><td>
	<table>
		<tr>
			<td width="300px"><ct:FWLabel key="JSP_PF_DOS_D_ASSURE"/></td>
			<td><%=PFUserHelper.getDetailAssure(objSession, viewBean.getDossier().getDemandePrestation().getPersonneEtendue()) %></td>
		</tr>
	</table>
	<hr />
	<br />
						</tr></td>
						<tr><td>
	
	<table cellpadding="5" cellspacing="0" border="1px solid" class="areaDataTable" >
		<% for (PFLigneHandler ligne : viewBean.getPfTableauHandler().getListLignes()) { %>
			<tr>
				<% 
					for (PFCelluleHandler cellule : ligne.getListCellules()) { 
						if (cellule.getTitre()) {
				%>
							<th><%=cellule.getTexte() %></th>
						<% } else { 
							String bordure = (cellule.getMoisCourant()) ? "border-left:2px solid black; border-right:2px solid black;" : ""; 
							%>
							<td nowrap="nowrap" style="<%=(cellule.getTotal()) ? "border-top:2px solid black; font-weight:bold; " : "" %> 
								<%=(cellule.getColored()) ? "background-color:'yellow'; " : "" %> 
								<%=(cellule.getErreur()) ? "background-color:'red'; " : "" %> 
								<%=bordure %>">
								<%=(cellule.getRetroActif()) ? "<i>" : "" %>
									<%=JadeStringUtil.toNotNullString(cellule.getMontantMensuel()) %>
								<%=(cellule.getRetroActif()) ? "</i>" : "" %>
								<% if (!JadeStringUtil.isEmpty(cellule.getMontantRetro())) { %>
									<a href="#" title="<%=cellule.getMontantRetro() %>">D</a>
								<% } %>
							</td>
						<% } %>
				<% } %>
			</tr>
		<% } %>
	</table>					
						
						</td></tr>
						<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
