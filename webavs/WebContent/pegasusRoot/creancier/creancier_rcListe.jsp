<%@page import="globaz.pegasus.vb.creancier.PCCreancierListViewBean"%>
<%@page import="globaz.pegasus.utils.PCCreancierHandler"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaits"%>
<%@page import="globaz.pegasus.vb.parametre.PCZoneForfaitsViewBean"%>
<%@page import="globaz.pegasus.vb.creancier.PCCreancierViewBean"%>

<%
// Les labels de cette page commence par la préfix "JSP_PC_PARAM_ZONE_FORFAIT_L"

	PCCreancierListViewBean viewBean=(PCCreancierListViewBean)request.getAttribute("viewBean");
	size = viewBean.getSize();
	detailLink = baseLink + "afficher&selectedId=";
	String idDemande = request.getParameter("idDemandePc");
%> 
<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	 <%-- tpl:insert attribute="zoneHeaders" --%>
	 
    <th><ct:FWLabel key="JSP_PC_CREANCIER_L_CREANCIER"/></th>
 	<th data-g-amountformatter=" "><ct:FWLabel key="JSP_PC_CREANCIER_L_MONTANT_REVENDIQUE"/></th>
 	<th data-g-amountformatter=" " ><ct:FWLabel key="JSP_PC_CREANCIER_L_MONTANT_MONTANT_REPARTI"/></th>
 	<th><ct:FWLabel key="JSP_PC_CREANCIER_L_MONTANT_TYPE"/></th>
 	<th><ct:FWLabel key="JSP_PC_CREANCIER_L_MONTANT_NO"/><input type="hidden" name="creancierSearch.forIdDemande" value="idDemande" /></th>
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <% PCCreancierViewBean line = (PCCreancierViewBean)viewBean.getEntity(i); 
       String detailUrl = "parent.fr_detail.location.href='" + detailLink + line.getCreancier().getId() +"'";
    %>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		<td onClick="<%=detailUrl%>"><%=PCCreancierHandler.displayCreancierTiers(line.getCreancier().getSimpleTiers())%></td>
		<td onClick="<%=detailUrl%>"><%=line.getCreancier().getSimpleCreancier().getMontant()%></td>
		<td onClick="<%=detailUrl%>"><%=viewBean.getMontantRepartiByCreancier(line.getCreancier().getSimpleCreancier().getIdCreancier())%></td>
		<td onClick="<%=detailUrl%>"><%= objSession.getCodeLibelle(line.getCreancier().getSimpleCreancier().getCsTypeCreance())%></td>
		<td onClick="<%=detailUrl%>"><%=line.getCreancier().getSimpleCreancier().getIdCreancier()%></td>
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	