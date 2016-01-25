<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCAssuranceVieAjaxViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%
PCAssuranceVieAjaxViewBean viewBean=(PCAssuranceVieAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>


<%@page import="globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.AssuranceVie"%>
<%@page import="ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere"%>
<message>
	<liste>
		<%
		FWCurrency valeurRachat = new FWCurrency("0.00");
		String idGroup=null;
		String currentId = "-1";
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){			
			AssuranceVie donnee=((AssuranceVie)itDonnee.next());									
			String nomCompagnie="";
			if(donnee.getTiersCompagnie()!=null){
				nomCompagnie=JadeStringUtil.escapeXML(donnee.getTiersCompagnie().getDesignation1() +" "+donnee.getTiersCompagnie().getDesignation2());
			}			
					
			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}
				valeurRachat = new FWCurrency(donnee.getSimpleAssuranceVie().getMontantValeurRachat());								
			%>

			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td style="text-align:right;"><%=valeurRachat.toStringFormat() %></td>
				<td><%=donnee.getSimpleAssuranceVie().getNumeroPolice() %></td>										
				<td><%=nomCompagnie %></td>
				<td><%=donnee.getSimpleAssuranceVie().getDateEcheance() %></td>
				<td align="center" ><% if(donnee.getSimpleDonneeFinanciereHeader().getIsDessaisissementFortune().booleanValue()){%>
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
