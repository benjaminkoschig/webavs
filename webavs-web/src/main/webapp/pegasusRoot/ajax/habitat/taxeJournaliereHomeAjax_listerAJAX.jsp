<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.jade.log.business.JadeBusinessMessageLevels"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<%@page import="globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere"%>

<%@page import="globaz.pegasus.vb.habitat.PCTaxeJournaliereHomeAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHome"%>

<%@page import="globaz.pegasus.utils.PCCommonHandler"%>

<%
PCTaxeJournaliereHomeAjaxViewBean viewBean=(PCTaxeJournaliereHomeAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

%>


<%@page import="globaz.pegasus.utils.PCTaxeJournaliereHomeHandler"%><message>
	<liste>
		<%
		if(!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
			String idGroup=null;
			for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
				TaxeJournaliereHome donnee=((TaxeJournaliereHome)itDonnee.next());
				if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
					idGroup=null;
				}
				%>
				
				<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
					<td>&#160;</td>
					<td><%=PCTaxeJournaliereHomeHandler.getLibelleHomeAvecChambre(donnee.getTypeChambre(),objSession)%></td>	
					<td>
						<img class="detailPrixChambres" style="float:left" src="images/aide.gif" 
						data-id-chambre="<%= donnee.getTypeChambre().getId() %>" 
						data-id-home="<%= donnee.getSimpleTaxeJournaliereHome().getIdHome() %>" 
						data-dateDebut="<%= donnee.getSimpleDonneeFinanciereHeader().getDateDebut() %>" 
						data-dateFin="<%= donnee.getSimpleDonneeFinanciereHeader().getDateFin() %>" 
						data-g-bubble='text:tooltipTextLibelle,wantMarker:false,position:right'/>
						<span style="float:right"><%=PCTaxeJournaliereHomeHandler.getPrix(donnee,objSession)%></span>
					</td>									
					<td><%=PCCommonHandler.getCurrencyFormtedDefault(donnee.getSimpleTaxeJournaliereHome().getMontantJournalierLCA()) %></td>		
					<td><%=PCCommonHandler.getCurrencyFormtedDefault(donnee.getSimpleTaxeJournaliereHome().getPrimeAPayer()) %></td>		
					<td><%=PCTaxeJournaliereHomeHandler.getLibelleAssurenceMaladie(donnee.getTiersAssurenceMaladie(),objSession)%></td>
					<td><% if(donnee.getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu().booleanValue()){%>
						<img src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
						<%} else {
							%>&#160;<%
						}%>
					</td>
					<td><%=donnee.getSimpleTaxeJournaliereHome().getDateEcheance() %></td>
					<td><%=donnee.getSimpleDonneeFinanciereHeader().getDateDebut() %> - <%=donnee.getSimpleDonneeFinanciereHeader().getDateFin() %></td>
				</tr>
	         <%
				idGroup=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup();
			}
		}%>
	</liste>
	<%@ include file="/pegasusRoot/ajax/commonListDonneeFinanciere.jspf" %>
</message>
