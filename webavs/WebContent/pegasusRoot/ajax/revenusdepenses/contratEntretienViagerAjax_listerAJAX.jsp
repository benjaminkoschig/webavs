<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCContratEntretienViagerAjaxViewBean"%>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCContratEntretienViagerAjaxListViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViager"%>
<%@page import="globaz.framework.util.FWCurrency"%>


<%
PCContratEntretienViagerAjaxViewBean viewBean=(PCContratEntretienViagerAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager = new SimpleLibelleContratEntretienViager();
PCContratEntretienViagerAjaxListViewBean listViewBean = new PCContratEntretienViagerAjaxListViewBean();
%>


<%@page import="globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.ContratEntretienViager"%>
<%@page import="ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere"%>
<message>
	<liste>
		<%
		FWCurrency montantContrat = new FWCurrency("0.00");
		String idGroup=null;
		String currentId = "-1";
		listViewBean = (PCContratEntretienViagerAjaxListViewBean)viewBean.getListViewBean();
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){			
			ContratEntretienViager donnee=((ContratEntretienViager)itDonnee.next());			

			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}		
			
				montantContrat = new FWCurrency(donnee.getSimpleContratEntretienViager().getMontantContrat());

				// l'id contratEntretienViager du CEV
				String idCev = donnee.getSimpleContratEntretienViager().getId();
				// correspondance avec le libellé
				StringBuffer listeLibelle = new StringBuffer();
				for(int i = 0 ; i < listViewBean.getSearchLibelle().getSearchResults().length ; i++){
					simpleLibelleContratEntretienViager = (SimpleLibelleContratEntretienViager)listViewBean.getSearchLibelle().getSearchResults()[i]; 
					if(simpleLibelleContratEntretienViager.getIdContratEntretienViager().equals(idCev)){
						listeLibelle.append(objSession.getCodeLibelle(simpleLibelleContratEntretienViager.getCsLibelleContratEntretienViager()));
						listeLibelle.append("<br/>");
					}	
				}
							
			%>

			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td style="text-align:right;"><%=montantContrat.toStringFormat() %></td>
				<td><%=listeLibelle.toString() %></td>	
				<td align="center" ><% if(donnee.getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu().booleanValue()){%>
					<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
					<%} else {
						%>&#160;<%
					}%>
				</td>					
				<td><%=donnee.getSimpleDonneeFinanciereHeader().getDateDebut() %> - <%=donnee.getSimpleDonneeFinanciereHeader().getDateFin() %></td>
			</tr>
		<%
		idGroup=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup();
		}%>
	</liste>
	<%@ include file="/pegasusRoot/ajax/commonListDonneeFinanciere.jspf" %>
</message>
