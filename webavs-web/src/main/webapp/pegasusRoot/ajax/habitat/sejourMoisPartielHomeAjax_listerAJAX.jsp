<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" import="ch.globaz.pegasus.business.models.habitat.SejourMoisPartielHome" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.jade.log.business.JadeBusinessMessageLevels"%>
<%@page import="globaz.pegasus.utils.PCCommonHandler"%>
<%@page import="globaz.pegasus.vb.habitat.PCSejourMoisPartielHomeAjaxViewBean"%>
<%@page import="java.util.Iterator"%>
<%@ page import="globaz.framework.util.FWCurrency" %>

<%
PCSejourMoisPartielHomeAjaxViewBean viewBean=(PCSejourMoisPartielHomeAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

%>


<message>
	<liste>
		<%
		if(!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
			String idGroup=null;
			for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
				SejourMoisPartielHome donnee=((SejourMoisPartielHome)itDonnee.next());
				if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
					idGroup=null;
				}
				%>
				
				<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
					<td>&#160;</td>
					<td><%=new FWCurrency(donnee.getSimpleSejourMoisPartielHome().getPrixJournalier()) %></td>
					<td><%=new FWCurrency(donnee.getSimpleSejourMoisPartielHome().getFraisNourriture()) %></td>
					<td><%=donnee.getSimpleSejourMoisPartielHome().getNbJours() %></td>
					<td><%=donnee.getSimpleDonneeFinanciereHeader().getDateDebut() %> - <%=donnee.getSimpleDonneeFinanciereHeader().getDateFin() %></td>
				</tr>
	         <%
				idGroup=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup();
			}
		}%>
	</liste>
	<%@ include file="/pegasusRoot/ajax/commonListDonneeFinanciere.jspf" %>
</message>
