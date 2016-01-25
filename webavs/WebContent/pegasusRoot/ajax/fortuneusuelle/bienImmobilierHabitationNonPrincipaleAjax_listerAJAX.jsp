<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCBienImmobilierHabitationNonPrincipaleAjaxViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>


<%
PCBienImmobilierHabitationNonPrincipaleAjaxViewBean viewBean=(PCBienImmobilierHabitationNonPrincipaleAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>


<%@page import="globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierHabitationNonPrincipale"%>
<%@page import="ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere"%>
<message>
	<liste>
		<%
		String idGroup=null;
		String currentId = "-1";
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){			
			BienImmobilierHabitationNonPrincipale donnee=((BienImmobilierHabitationNonPrincipale)itDonnee.next());									
									
			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}							
			%>
			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td><%=objSession.getCode(donnee.getSimpleBienImmobilierHabitationNonPrincipale().getCsTypePropriete()) %></td>
				<td><%=donnee.getSimpleBienImmobilierHabitationNonPrincipale().getPartProprieteNumerateur() %> / <%=donnee.getSimpleBienImmobilierHabitationNonPrincipale().getPartProprieteDenominateur() %></td>
				<td><%=objSession.getCodeLibelle(donnee.getSimpleBienImmobilierHabitationNonPrincipale().getCsTypeBien()) %></td>
				<td style="text-align:right;"><%=new FWCurrency(donnee.getSimpleBienImmobilierHabitationNonPrincipale().getValeurVenale()).toStringFormat()%></td>
				<td style="text-align:right;"><%=new FWCurrency(donnee.getSimpleBienImmobilierHabitationNonPrincipale().getMontantValeurLocative()).toStringFormat()%></td>
				<td style="text-align:right;"><%=new FWCurrency(donnee.getSimpleBienImmobilierHabitationNonPrincipale().getMontantLoyesEncaisses()).toStringFormat()%></td>
				<td style="text-align:right;"><%=new FWCurrency(donnee.getSimpleBienImmobilierHabitationNonPrincipale().getMontantSousLocation()).toStringFormat()%></td>
				<td style="text-align:right;"><%=new FWCurrency(donnee.getSimpleBienImmobilierHabitationNonPrincipale().getMontantDetteHypothecaire()).toStringFormat()%></td>
				<td style="text-align:right;"><%=new FWCurrency(donnee.getSimpleBienImmobilierHabitationNonPrincipale().getMontantInteretHypothecaire()).toStringFormat()%></td>								
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
