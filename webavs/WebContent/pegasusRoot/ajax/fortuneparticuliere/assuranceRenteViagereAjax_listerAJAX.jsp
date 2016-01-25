<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere"%>
<%@page import="globaz.pegasus.vb.fortuneparticuliere.PCAssuranceRenteViagereAjaxViewBean"%>

<%
PCAssuranceRenteViagereAjaxViewBean viewBean=(PCAssuranceRenteViagereAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>



<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.AssuranceRenteViagere"%>
<%@page import="globaz.framework.util.FWCurrency"%><message>
	<liste>
		<%
		String idGroup=null;
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			AssuranceRenteViagere donnee=((AssuranceRenteViagere)itDonnee.next());
			String nomCompagnie="";
			if(donnee.getCompagnie()!=null){
				nomCompagnie=donnee.getCompagnie().getTiers().getDesignation1();
			}
			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}			
					%>
			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td><%=new FWCurrency(donnee.getSimpleAssuranceRenteViagere().getMontantValeurRachat()).toStringFormat() %></td>
				<td><%=donnee.getSimpleAssuranceRenteViagere().getNumeroPolice() %></td>
				<td><%=nomCompagnie %></td>
				<td><%=new FWCurrency(donnee.getSimpleAssuranceRenteViagere().getMontantRenteViagere()).toStringFormat() %></td>
				<td><%=new FWCurrency(donnee.getSimpleAssuranceRenteViagere().getExcedentRenteViagere()).toStringFormat() %></td>
				<td><% if(donnee.getSimpleDonneeFinanciereHeader().getIsDessaisissementFortune().booleanValue()){%>
					<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
					<%} else {
						%>&#160;<%
					}%></td>
				<td><% if(donnee.getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu().booleanValue()){%>
					<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
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
