<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCRenteAvsAiAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCRenteijapi"%>
<%
PCRenteAvsAiAjaxViewBean viewBean=(PCRenteAvsAiAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>

<%@page import="ch.globaz.pegasus.business.models.renteijapi.RenteIjApi"%>
<%@page import="globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.RenteAvsAi"%>

<%@page import="globaz.framework.util.FWCurrency"%><message>
	<liste>
		<%
		String idGroup=null;
		
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			
			RenteAvsAi donnee=((RenteAvsAi)itDonnee.next());
			String genreRente = donnee.getSimpleRenteAvsAi().getCsTypeRente();
			
		
			
			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}
			%>
			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td><%=new FWCurrency(donnee.getSimpleRenteAvsAi().getMontant()).toStringFormat() %></td>
				<td><%=(!IPCRenteijapi.CS_SANS_RENTE_AVS.equals(genreRente))?objSession.getCode(genreRente):objSession.getCode(genreRente)+" - "+objSession.getCodeLibelle(donnee.getSimpleRenteAvsAi().getCsTypePc()) %></td>
				<td><%=donnee.getSimpleRenteAvsAi().getDateDepot() %></td>
				<td><%=donnee.getSimpleRenteAvsAi().getDateDecision() %></td>
				<td><%=donnee.getSimpleRenteAvsAi().getDateEcheance() %></td>
				<td class="test"><%=donnee.getSimpleDonneeFinanciereHeader().getDateDebut() %> - <%=donnee.getSimpleDonneeFinanciereHeader().getDateFin() %></td>
			</tr>
<%
			idGroup=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup();

		}%>
	</liste>
	<%@ include file="/pegasusRoot/ajax/commonListDonneeFinanciere.jspf" %>
</message>
