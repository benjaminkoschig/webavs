<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.fortuneparticuliere.PCBetailAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%
PCDessaisissementFortuneAjaxViewBean viewBean=(PCDessaisissementFortuneAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
String autreMotilLbl = objSession.getCodeLibelle(IPCDroits.CS_AUTRES_MOTIF_DESSAISISSEMENT);
%>


<%@page import="globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.Betail"%>



<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.pegasus.vb.dessaisissement.PCDessaisissementFortuneAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortune"%>
<%@page import="ch.globaz.pegasus.business.models.dessaisissement.SimpleDessaisissementFortune"%><message>
	<liste>
		<%
		String idGroup=null;
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			DessaisissementFortune donneeComplex=((DessaisissementFortune)itDonnee.next());
			if(!donneeComplex.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}
			SimpleDessaisissementFortune donnee=donneeComplex.getSimpleDessaisissementFortune();
			%>
			
			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donneeComplex.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td><%=(IPCDroits.CS_AUTRES_MOTIF_DESSAISISSEMENT.equals(donnee.getCsMotifDessaisissement()))?autreMotilLbl+" - "+donnee.getAutreMotifDessaisissement():objSession.getCodeLibelle(donnee.getCsMotifDessaisissement()) %></td>
				<td><%=new FWCurrency(donnee.getMontantBrut()).toStringFormat() %></td>
				<td><%=new FWCurrency(donnee.getDeductionMontantDessaisi()).toStringFormat() %></td>
				<td><% if(donnee.getIsContrePrestation().booleanValue()){%>
					<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
					<%} else {
						%>&#160;<%
					}%></td>
				<td><%=donneeComplex.getSimpleDonneeFinanciereHeader().getDateDebut() %> - <%=donneeComplex.getSimpleDonneeFinanciereHeader().getDateFin() %></td>
			</tr>
<%
			idGroup=donneeComplex.getSimpleDonneeFinanciereHeader().getIdEntityGroup();
		}%>
	</liste>
	<%@ include file="/pegasusRoot/ajax/commonListDonneeFinanciere.jspf" %>
</message>
