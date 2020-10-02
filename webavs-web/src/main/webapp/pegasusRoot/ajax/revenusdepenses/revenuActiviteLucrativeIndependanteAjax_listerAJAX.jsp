<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCRevenuActiviteLucrativeIndependanteAjaxViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>


<%
PCRevenuActiviteLucrativeIndependanteAjaxViewBean viewBean=(PCRevenuActiviteLucrativeIndependanteAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>


<%@page import="globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependante"%>
<%@page import="ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere"%>
<message>
	<liste>
		<%
		FWCurrency montantRevenu = new FWCurrency("0.00");
		FWCurrency fraisDeGarde = new FWCurrency("0.00");
		String idGroup=null;
		String currentId = "-1";
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){			
			RevenuActiviteLucrativeIndependante donnee=((RevenuActiviteLucrativeIndependante)itDonnee.next());									
			String nomCaisse="";
			if(donnee.getCaisse()!=null){
				nomCaisse=donnee.getCaisse().getTiers().getDesignation1() +" "+donnee.getCaisse().getTiers().getDesignation2();
			}			
			String nomAffilie="";
			if(donnee.getTiersAffilie()!=null){
				nomAffilie = donnee.getTiersAffilie().getDesignation1() + " " +donnee.getTiersAffilie().getDesignation2();
			}						
			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}
				montantRevenu = new FWCurrency(donnee.getSimpleRevenuActiviteLucrativeIndependante().getMontantRevenu());
				fraisDeGarde = new FWCurrency(donnee.getSimpleRevenuActiviteLucrativeIndependante().getFraisDeGarde());
			%>

			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td><%=objSession.getCodeLibelle(donnee.getSimpleRevenuActiviteLucrativeIndependante().getCsDeterminationRevenu()) %></td>
				<td><%=objSession.getCodeLibelle(donnee.getSimpleRevenuActiviteLucrativeIndependante().getCsGenreRevenu()) %></td>
				<td style="text-align:right;"><%=montantRevenu.toStringFormat() %></td>
				<td style="text-align:right;"><%=fraisDeGarde.toStringFormat() %></td>
				<td><%=nomAffilie %></td>
				<td><%=nomCaisse %></td>
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
	<%@ include file="/pegasusRoot/ajax/commonListDonneeFinanciereWithWarning.jspf" %>
</message>
