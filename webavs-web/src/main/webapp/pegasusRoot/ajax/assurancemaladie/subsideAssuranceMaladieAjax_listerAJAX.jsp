<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<%
PCSubsideAssuranceMaladieAjaxViewBean viewBean=(PCSubsideAssuranceMaladieAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>

<%@ page import="ch.globaz.pegasus.business.models.assurancemaladie.SubsideAssuranceMaladie" %>
<%@ page import="globaz.pegasus.vb.assurancemaladie.PCSubsideAssuranceMaladieAjaxViewBean" %>

<message>
	<liste>
		<%
		FWCurrency montant = new FWCurrency("0.00");

		String idGroup=null;
		
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			SubsideAssuranceMaladie donnee=((SubsideAssuranceMaladie)itDonnee.next());
			
			montant = new FWCurrency(donnee.getSimpleSubsideAssuranceMaladie().getMontant());
			
			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}
			%>

			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td style="text-align:right;"><%=montant.toStringFormat() %></td>
				<td><%=donnee.getSimpleDonneeFinanciereHeader().getDateDebut() %> - <%=donnee.getSimpleDonneeFinanciereHeader().getDateFin() %></td>
			</tr>
		<%
			idGroup=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup();
		}%>
	</liste>
	<%@ include file="/pegasusRoot/ajax/commonListDonneeFinanciere.jspf" %>
</message>
