<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCPensionAlimentaireAjaxViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>


<%
PCPensionAlimentaireAjaxViewBean viewBean=(PCPensionAlimentaireAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>


<%@page import="globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.PensionAlimentaire"%>
<%@page import="ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere"%>
<message>
	<liste>
		<%
		FWCurrency montantPension = new FWCurrency("0.00");
		String idGroup=null;
		String currentId = "-1";
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){			
			PensionAlimentaire donnee=((PensionAlimentaire)itDonnee.next());									
			
			String nomAffilie="";
			if(donnee.getTiers()!=null){
				nomAffilie=donnee.getTiers().getDesignation1();
			}						
			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}
				montantPension = new FWCurrency(donnee.getSimplePensionAlimentaire().getMontantPensionAlimentaire());								
			%>

			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td><%=objSession.getCodeLibelle(donnee.getSimplePensionAlimentaire().getCsTypePension()) %></td>
				<td style="text-align:right;"><%=montantPension.toStringFormat() %></td>				
				<td><%=objSession.getCodeLibelle(donnee.getSimplePensionAlimentaire().getCsMotif()) %></td>	
						
				<td><%=nomAffilie %></td>
				<td><%=objSession.getCodeLibelle(donnee.getSimplePensionAlimentaire().getCsLienAvecRequerantPC()) %></td>
				<td align="center" ><% if(donnee.getSimplePensionAlimentaire().getIsDeductionRenteEnfant().booleanValue()){%>
					<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
					<%} else {
						%>&#160;<%
					}%>
				</td>	
				<td><%=donnee.getSimplePensionAlimentaire().getDateEcheance() %></td>								
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
		}
		%>
	</liste>
	<%@ include file="/pegasusRoot/ajax/commonListDonneeFinanciere.jspf" %>
</message>
