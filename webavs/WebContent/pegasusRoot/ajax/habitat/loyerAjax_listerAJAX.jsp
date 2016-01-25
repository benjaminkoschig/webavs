<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<%@page import="globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere"%>

<%@page import="globaz.pegasus.vb.habitat.PCLoyerAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.habitat.Loyer"%>

<%
PCLoyerAjaxViewBean viewBean=(PCLoyerAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

%>

<message>
	<liste>
		<%
		String idGroup=null;
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			Loyer donnee=((Loyer)itDonnee.next());
			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}
			%>
			
			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td><%=objSession.getCodeLibelle(donnee.getSimpleLoyer().getCsTypeLoyer()) %></td>
				<td><%=donnee.getSimpleLoyer().getNbPersonnes() %></td>
				<td><%=new FWCurrency(donnee.getSimpleLoyer().getMontantLoyerNet()).toStringFormat() %></td>
				<td><%=new FWCurrency(donnee.getSimpleLoyer().getMontantCharges()).toStringFormat() %></td>
				<td>
				<% if(donnee.getSimpleLoyer().getIsFauteuilRoulant().booleanValue()){%>
					<img src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
					<%} else {
						%>&#160;<%
				}%>
				</td>
				<td><%=donnee.getSimpleLoyer().getRevenuSousLocation() %></td>
				<td><%=donnee.getSimpleLoyer().getFraisPlacementEnfant() %></td>
				<td><% if(donnee.getSimpleLoyer().getIsTenueMenage().booleanValue()){%>
					<img src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
					<%} else {
						%>&#160;<%
				}%></td>
				<td><%=donnee.getSimpleLoyer().getPensionNonReconnue() %></td>
				<td><%= new FWCurrency(donnee.getSimpleLoyer().getTaxeJournalierePensionNonReconnue()).toStringFormat()%></td>
				<td><%=donnee.getSimpleDonneeFinanciereHeader().getDateDebut() %> - <%=donnee.getSimpleDonneeFinanciereHeader().getDateFin() %></td>
			</tr>
         <%
			idGroup=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup();
		}%>
	</liste>
	<%@ include file="/pegasusRoot/ajax/commonListDonneeFinanciere.jspf" %>
</message>
