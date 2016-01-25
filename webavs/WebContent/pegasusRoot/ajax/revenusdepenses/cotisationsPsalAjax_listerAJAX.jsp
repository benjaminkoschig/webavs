<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCCotisationsPsalAjaxViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>


<%
PCCotisationsPsalAjaxViewBean viewBean=(PCCotisationsPsalAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>


<%@page import="globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.CotisationsPsal"%>
<%@page import="ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere"%>
<message>
	<liste>
		<%
		FWCurrency montantCotisation = new FWCurrency("0.00");
		String idGroup=null;
		String currentId = "-1";
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){			
			CotisationsPsal donnee=((CotisationsPsal)itDonnee.next());									
			String nomCaisse="";
			if(donnee.getCaisse()!=null){
				nomCaisse=donnee.getCaisse().getTiers().getDesignation1() +" " +donnee.getCaisse().getTiers().getDesignation2();
			}			
					
			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}
			montantCotisation = new FWCurrency(donnee.getSimpleCotisationsPsal().getMontantCotisationsAnnuelles());								
			%>

			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td style="text-align:right;"><%=montantCotisation.toStringFormat() %></td>										
				<td><%=nomCaisse %></td>
				<td><%=donnee.getSimpleCotisationsPsal().getDateEcheance() %></td>					
				<td><%=donnee.getSimpleDonneeFinanciereHeader().getDateDebut() %> - <%=donnee.getSimpleDonneeFinanciereHeader().getDateFin() %></td>
			</tr>
		<%
		idGroup=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup();
		}%>
	</liste>
	<%@ include file="/pegasusRoot/ajax/commonListDonneeFinanciere.jspf" %>
</message>
