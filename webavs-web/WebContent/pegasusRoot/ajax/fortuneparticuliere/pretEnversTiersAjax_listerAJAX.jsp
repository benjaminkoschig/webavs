<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.fortuneparticuliere.PCPretEnversTiersAjaxViewBean"%>

<%
PCPretEnversTiersAjaxViewBean viewBean=(PCPretEnversTiersAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>


<%@page import="globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.PretEnversTiers"%>
<%@page import="ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere"%>


<%@page import="globaz.framework.util.FWCurrency"%><message>
	<liste>
		<%
		String idGroup=null;
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			PretEnversTiers donnee=((PretEnversTiers)itDonnee.next());
			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}
			%>
			
			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td><%=objSession.getCode(donnee.getSimplePretEnversTiers().getCsTypePropriete()) %></td>
				<td><%=donnee.getSimplePretEnversTiers().getPartProprieteNumerateur() %> / <%=donnee.getSimplePretEnversTiers().getPartProprieteDenominateur() %></td>
				<td><%=donnee.getSimplePretEnversTiers().getTypePret() %></td>
				<td><%=new FWCurrency(donnee.getSimplePretEnversTiers().getMontantPret()).toStringFormat() %></td>
				<td><%=donnee.getSimplePretEnversTiers().getNomPrenomBeneficiaire() %></td>
				<td><%=donnee.getSimplePretEnversTiers().getDateEcheance() %></td>
				<td><% if(donnee.getSimpleDonneeFinanciereHeader().getIsDessaisissementFortune().booleanValue()){%>
					<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
					<%} else {
						%>&#160;<%
					}%></td>
				<td><% if(donnee.getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu().booleanValue()){%>
					<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
					<%} else {
						%>&#160;<%
					}%></td>
				<td><%=donnee.getSimpleDonneeFinanciereHeader().getDateDebut() %> - <%=donnee.getSimpleDonneeFinanciereHeader().getDateFin() %></td>
			</tr>
<%
			idGroup=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup();
		}%>
	</liste>
	<%@ include file="/pegasusRoot/ajax/commonListDonneeFinanciere.jspf" %>
</message>
