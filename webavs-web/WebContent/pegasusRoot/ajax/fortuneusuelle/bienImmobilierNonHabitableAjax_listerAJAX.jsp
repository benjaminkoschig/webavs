<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCBienImmobilierNonHabitableAjaxViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>


<%
PCBienImmobilierNonHabitableAjaxViewBean viewBean=(PCBienImmobilierNonHabitableAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>


<%@page import="globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierNonHabitable"%>
<%@page import="ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere"%>
<message>
	<liste>
		<%
		String idGroup=null;
		String currentId = "-1";
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){			
			BienImmobilierNonHabitable donnee=((BienImmobilierNonHabitable)itDonnee.next());									
					
			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}							
			%>
			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td><%=objSession.getCode(donnee.getSimpleBienImmobilierNonHabitable().getCsTypePropriete()) %></td>
				<td><%=donnee.getSimpleBienImmobilierNonHabitable().getPartProprieteNumerateur() %> / <%=donnee.getSimpleBienImmobilierNonHabitable().getPartProprieteDenominateur() %></td>
				<td title="<%=donnee.getSimpleBienImmobilierNonHabitable().getAutresTypeBien() %>"><%=objSession.getCodeLibelle(donnee.getSimpleBienImmobilierNonHabitable().getCsTypeBien()) %></td>
				<td  style="text-align:right;"><%=new FWCurrency(donnee.getSimpleBienImmobilierNonHabitable().getValeurVenale()).toStringFormat()%></td>
				<td  style="text-align:right;"><%=new FWCurrency(donnee.getSimpleBienImmobilierNonHabitable().getMontantDetteHypothecaire()).toStringFormat()%></td>
				<td  style="text-align:right;"><%=new FWCurrency(donnee.getSimpleBienImmobilierNonHabitable().getMontantInteretHypothecaire()).toStringFormat()%></td>
				<td  style="text-align:right;"><%=new FWCurrency(donnee.getSimpleBienImmobilierNonHabitable().getMontantRendement()).toStringFormat()%></td>							
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
