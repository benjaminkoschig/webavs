<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCAutreRenteAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.AutreRente"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCRenteijapi"%>

<%
	PCAutreRenteAjaxViewBean viewBean=(PCAutreRenteAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String autreRenteLbl = objSession.getCodeLibelle(IPCRenteijapi.CS_AUTRES_RENTES_AUTRE);
%>

<message>
	<liste>
		<%
		String idGroup=null;
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			AutreRente donnee=((AutreRente)itDonnee.next());
			
			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}
			%>
			
			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td><%=(!IPCRenteijapi.CS_AUTRES_RENTES_AUTRE.equals(donnee.getSimpleAutreRente().getCsGenre()))?objSession.getCodeLibelle(donnee.getSimpleAutreRente().getCsGenre()):autreRenteLbl +" - "+donnee.getSimpleAutreRente().getAutreGenre() %></td>
				<td><%=objSession.getCodeLibelle(donnee.getSimpleAutreRente().getCsType()) %></td>
				<td><%=new FWCurrency(donnee.getSimpleAutreRente().getMontant()).toStringFormat() %></td>
				<td><%=donnee.getSimpleAutreRente().getFournisseurPrestation() %></td>
				<td><%=JadeStringUtil.isBlankOrZero(donnee.getSimpleAutreRente().getDateEcheance())?"":donnee.getSimpleAutreRente().getDateEcheance() %></td>
				<td><% if(donnee.getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu().booleanValue()){%>
					<img src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
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
