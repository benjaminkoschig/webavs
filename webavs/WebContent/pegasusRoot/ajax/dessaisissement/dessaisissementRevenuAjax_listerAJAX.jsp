<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.pegasus.vb.dessaisissement.PCDessaisissementRevenuAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenu"%>
<%@page import="ch.globaz.pegasus.business.models.dessaisissement.SimpleDessaisissementRevenu"%>
<%
PCDessaisissementRevenuAjaxViewBean viewBean=(PCDessaisissementRevenuAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

%>

<message>
	<liste>
		<%
		String idGroup=null;
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			DessaisissementRevenu donneeComplex=((DessaisissementRevenu)itDonnee.next());
			if(!donneeComplex.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}
			SimpleDessaisissementRevenu donnee=donneeComplex.getSimpleDessaisissementRevenu();
			%>
			
			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donneeComplex.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td><%=donnee.getLibelleDessaisissement() %></td>
				<td><%=new FWCurrency(donnee.getMontantBrut()).toStringFormat() %></td>
				<td><%=new FWCurrency(donnee.getDeductionMontantDessaisi()).toStringFormat() %></td>
				<td><%=donneeComplex.getSimpleDonneeFinanciereHeader().getDateDebut() %> - <%=donneeComplex.getSimpleDonneeFinanciereHeader().getDateFin() %></td>
			</tr>
<%
			idGroup=donneeComplex.getSimpleDonneeFinanciereHeader().getIdEntityGroup();
		}%>
	</liste>
	<%@ include file="/pegasusRoot/ajax/commonListDonneeFinanciere.jspf" %>
</message>
