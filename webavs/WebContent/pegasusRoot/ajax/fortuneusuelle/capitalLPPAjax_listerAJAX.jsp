<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCCapitalLPPAjaxViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>


<%
PCCapitalLPPAjaxViewBean viewBean=(PCCapitalLPPAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>


<%@page import="globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.CapitalLPP"%>
<%@page import="ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere"%>
<message>
	<liste>
		<%
		String idGroup=null;
		String currentId = "-1";
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){			
			CapitalLPP donnee=((CapitalLPP)itDonnee.next());									
			String nomInstitution="";
			if(donnee.getCaisse()!=null){
				nomInstitution=donnee.getCaisse().getTiers().getDesignation1();
			}			
					
			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}							
			%>
			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td><%=objSession.getCode(donnee.getSimpleCapitalLPP().getCsTypePropriete()) %></td>
				<td><%=donnee.getSimpleCapitalLPP().getPartProprieteNumerateur() %> / <%=donnee.getSimpleCapitalLPP().getPartProprieteDenominateur() %></td>
				<td style="text-align:right;"><%=new FWCurrency(donnee.getSimpleCapitalLPP().getMontantCapitalLPP()).toStringFormat()%></td>
				<td><%=donnee.getSimpleCapitalLPP().getNoPoliceNoCompte()%></td>
				<td><%=nomInstitution %></td>			
				<td><% if(donnee.getSimpleCapitalLPP().getIsSansInteret().booleanValue()){%>
					<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
					<%} else {
						%>&#160;<%
					}%></td>
				<td style="text-align:right;"><%=new FWCurrency(donnee.getSimpleCapitalLPP().getMontantInteret()).toStringFormat()%></td>
				<td style="text-align:right;"><%=new FWCurrency(donnee.getSimpleCapitalLPP().getMontantFrais()).toStringFormat()%></td>
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
