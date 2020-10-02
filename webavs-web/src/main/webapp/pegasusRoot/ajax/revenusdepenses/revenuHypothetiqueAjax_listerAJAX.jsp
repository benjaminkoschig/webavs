<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCRevenuHypothetiqueAjaxViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRevenuHypothetique"%>
<%
PCRevenuHypothetiqueAjaxViewBean viewBean=(PCRevenuHypothetiqueAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
String motifAutreLbl = objSession.getCodeLibelle(IPCRevenuHypothetique.CS_MOTIF_REVENU_HYPO_AUTRES);
%>


<%@page import="globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.RevenuHypothetique"%>
<%@page import="ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere"%>

<message>
	<liste>
		<%
		FWCurrency revenuNet = new FWCurrency("0.00");
		FWCurrency revenuBrut = new FWCurrency("0.00");
		FWCurrency deductionsSociales = new FWCurrency("0.00");
		FWCurrency deductionLPP = new FWCurrency("0.00");
		FWCurrency fraisDeGarde = new FWCurrency("0.00");
		String idGroup=null;
		
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			RevenuHypothetique donnee=((RevenuHypothetique)itDonnee.next());
			
			revenuNet = new FWCurrency(donnee.getSimpleRevenuHypothetique().getMontantRevenuHypothetiqueNet());
			revenuBrut = new FWCurrency(donnee.getSimpleRevenuHypothetique().getMontantRevenuHypothetiqueBrut());
			deductionsSociales = new FWCurrency(donnee.getSimpleRevenuHypothetique().getDeductionsSociales());
			deductionLPP = new FWCurrency(donnee.getSimpleRevenuHypothetique().getDeductionLPP());
			fraisDeGarde = new FWCurrency(donnee.getSimpleRevenuHypothetique().getFraisDeGarde());
			
			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}
			%>

			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td><%=IPCRevenuHypothetique.CS_MOTIF_REVENU_HYPO_AUTRES.equals(donnee.getSimpleRevenuHypothetique().getCsMotif())? motifAutreLbl +" - "+donnee.getSimpleRevenuHypothetique().getAutreMotif():objSession.getCodeLibelle(donnee.getSimpleRevenuHypothetique().getCsMotif()) %></td>				
				<td style="text-align:right;"><%=revenuBrut.toStringFormat() %></td>
				<td style="text-align:right;"><%=deductionsSociales.toStringFormat() %></td>
				<td style="text-align:right;"><%=deductionLPP.toStringFormat() %></td>
				<td style="text-align:right;"><%=fraisDeGarde.toStringFormat() %></td>	
				<td style="text-align:right;"><%=revenuNet.toStringFormat() %></td>							
				<!-- <td></td> -->
				<td><%=donnee.getSimpleDonneeFinanciereHeader().getDateDebut() %> - <%=donnee.getSimpleDonneeFinanciereHeader().getDateFin() %></td>
			</tr>
<%
			idGroup=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup();
		}%>
	</liste>
	<%@ include file="/pegasusRoot/ajax/commonListDonneeFinanciereWithWarning.jspf" %>
</message>
