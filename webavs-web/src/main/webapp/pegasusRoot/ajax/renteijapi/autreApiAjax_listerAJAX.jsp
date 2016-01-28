<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCAutreApiAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.AutreApi"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCRenteijapi"%>
<%
	PCAutreApiAjaxViewBean viewBean=(PCAutreApiAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String autreRenteLbl  =objSession.getCodeLibelle(IPCRenteijapi.CS_AUTRES_API_AUTRE);
%>

<%@page import="globaz.fweb.util.JavascriptEncoder"%><message>
	<liste>
		<%
		String idGroup=null;
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			AutreApi donnee=((AutreApi)itDonnee.next());
			
			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}
			%>
			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td><%=new FWCurrency(donnee.getSimpleAutreApi().getMontant()).toStringFormat() %></td>
				<td><%=(!IPCRenteijapi.CS_AUTRES_API_AUTRE.equals(donnee.getSimpleAutreApi().getCsType()))?objSession.getCodeLibelle(donnee.getSimpleAutreApi().getCsType()):autreRenteLbl +" - "+donnee.getSimpleAutreApi().getAutre()%></td>
				<td><%=objSession.getCodeLibelle(donnee.getSimpleAutreApi().getCsGenre()) %></td>
				<td><%=objSession.getCodeLibelle(donnee.getSimpleAutreApi().getCsDegre()) %></td>
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