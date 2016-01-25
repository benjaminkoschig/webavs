<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.IjApg"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCIjApgAjaxViewBean"%>
<%
	PCIjApgAjaxViewBean viewBean=(PCIjApgAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>
<message>
	<liste>
		<%
		String idGroup=null;
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			IjApg donnee=((IjApg)itDonnee.next());
			
			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}
			%>
			
			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td><%=objSession.getCodeLibelle(donnee.getSimpleIjApg().getCsGenrePrestation()) %> <%=donnee.getSimpleIjApg().getAutreGenrePresation() %></td>
				<td><%=JadeStringUtil.isBlankOrZero(donnee.getSimpleIjApg().getMontant())?new FWCurrency(donnee.getSimpleIjApg().getMontantBrutAC()).toStringFormat():new FWCurrency(donnee.getSimpleIjApg().getMontant()).toStringFormat() %></td>
				<td><%=donnee.getTiersFournisseurPrestation().getDesignation1() +" "+ donnee.getTiersFournisseurPrestation().getDesignation2()%></td>
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
