<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>

<%@page import="globaz.perseus.vb.creancier.PFCreancierListViewBean"%>
<%@page import="globaz.perseus.vb.creancier.PFCreancierViewBean"%>
<%@page import="globaz.perseus.utils.creancier.PFCreancierHandler"%>

<%
// Les labels de cette page commence par la préfix "JSP_PF_PARAM_ZONE_FORFAIT_L"

	PFCreancierListViewBean viewBean=(PFCreancierListViewBean)request.getAttribute("viewBean");
	size = viewBean.getSize();
	detailLink = baseLink + "afficher&selectedId=";
	String idDemande = request.getParameter("creancierSearchModel.forIdDemande");

%> 
<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	 <%-- tpl:insert attribute="zoneHeaders" --%>
	 
    <th><ct:FWLabel key="JSP_PF_CREANCIER_L_CREANCIER"/></th>
 	<th data-g-amountformatter=" "><ct:FWLabel key="JSP_PF_CREANCIER_L_MONTANT_REVENDIQUE"/></th>
 	<th data-g-amountformatter=" " ><ct:FWLabel key="JSP_PF_CREANCIER_L_MONTANT_MONTANT_REPARTI"/></th>
 	<th><ct:FWLabel key="JSP_PF_CREANCIER_L_MONTANT_TYPE"/></th>
 	<th><ct:FWLabel key="JSP_PF_CREANCIER_L_MONTANT_NO"/></th>
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <% PFCreancierViewBean line = (PFCreancierViewBean)viewBean.getEntity(i); 
       String detailUrl = "parent.fr_detail.location.href='" + detailLink + line.getCreancier().getId() + "&idDemande=" + idDemande + "'";
    %>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		<td onClick="<%=detailUrl%>"><%=PFCreancierHandler.displayCreancierTiers(line.getCreancier().getSimpleTiers())%></td>
		<td onClick="<%=detailUrl%>"><%=line.getCreancier().getSimpleCreancier().getMontantRevendique()%></td>
		<td onClick="<%=detailUrl%>"><%=viewBean.getMontantRepartiByCreancier(line.getCreancier().getSimpleCreancier().getIdCreancier())%></td>
		<td onClick="<%=detailUrl%>"><%= objSession.getCodeLibelle(line.getCreancier().getSimpleCreancier().getCsTypeCreance())%></td>
		<td onClick="<%=detailUrl%>"><%=line.getCreancier().getSimpleCreancier().getIdCreancier()%></td>
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	