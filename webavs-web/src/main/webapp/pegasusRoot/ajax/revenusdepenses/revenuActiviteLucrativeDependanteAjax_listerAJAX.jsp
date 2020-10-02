<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuActiviteLucrativeDependante"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenu"%>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCRevenuActiviteLucrativeDependanteAjaxViewBean"%>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCRevenuActiviteLucrativeDependanteAjaxListViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<%
PCRevenuActiviteLucrativeDependanteAjaxViewBean viewBean=(PCRevenuActiviteLucrativeDependanteAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu = new SimpleTypeFraisObtentionRevenu();
PCRevenuActiviteLucrativeDependanteAjaxListViewBean listViewBean = new PCRevenuActiviteLucrativeDependanteAjaxListViewBean();
%>


<%@page import="globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeDependante"%>
<%@page import="ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere"%>
<message>
	<liste>
		<%
		FWCurrency montantActiviteLucrative = new FWCurrency("0.00");
		FWCurrency deductionsSociales = new FWCurrency("0.00");
		FWCurrency deductionsLPP = new FWCurrency("0.00");
		FWCurrency fraisDeGarde = new FWCurrency("0.00");
		FWCurrency montantFrais = new FWCurrency("0.00");		
		String idGroup=null;
		listViewBean = (PCRevenuActiviteLucrativeDependanteAjaxListViewBean)viewBean.getListViewBean();
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){			
			RevenuActiviteLucrativeDependante donnee=((RevenuActiviteLucrativeDependante)itDonnee.next());									
			String nomEmployeur="";
			if(donnee.getEmployeur()!=null){
				nomEmployeur=donnee.getEmployeur().getDesignation1()+ " " + donnee.getEmployeur().getDesignation2();
			}			
			if(!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)){
				idGroup=null;
			}		
			
				montantActiviteLucrative = new FWCurrency(donnee.getSimpleRevenuActiviteLucrativeDependante().getMontantActiviteLucrative());
				deductionsSociales = new FWCurrency(donnee.getSimpleRevenuActiviteLucrativeDependante().getDeductionsSociales());
				deductionsLPP = new FWCurrency(donnee.getSimpleRevenuActiviteLucrativeDependante().getDeductionsLpp());
				montantFrais = new FWCurrency(donnee.getSimpleRevenuActiviteLucrativeDependante().getMontantFrais());
				fraisDeGarde = new FWCurrency(donnee.getSimpleRevenuActiviteLucrativeDependante().getFraisDeGarde());
				
				// l'id contratEntretienViager du CEV
				String idCev = donnee.getSimpleRevenuActiviteLucrativeDependante().getId();
				// correspondance avec le libellé
				StringBuffer listeFrais = new StringBuffer();
				for(int i = 0 ; i < listViewBean.getSearchFrais().getSearchResults().length ; i++){
					simpleTypeFraisObtentionRevenu = (SimpleTypeFraisObtentionRevenu)listViewBean.getSearchFrais().getSearchResults()[i]; 			
					
					if(simpleTypeFraisObtentionRevenu.getIdRevenuActiviteLucrativeDependante().equals(idCev)){
						if((objSession.getCodeLibelle(simpleTypeFraisObtentionRevenu.getCsFraisObtentionRevenu()).equals("Autres")))
							listeFrais.append(donnee.getSimpleRevenuActiviteLucrativeDependante().getAutreFraisObtentionRevenu());
						else
							listeFrais.append(objSession.getCodeLibelle(simpleTypeFraisObtentionRevenu.getCsFraisObtentionRevenu()));
						listeFrais.append("<br/>");
					}	
				}											
			%>

			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>
				<td><%=objSession.getCodeLibelle(donnee.getSimpleRevenuActiviteLucrativeDependante().getCsGenreRevenu())%>
				<% if(!donnee.getSimpleRevenuActiviteLucrativeDependante().getTypeRevenu().equals(""))%>
				 	<%=donnee.getSimpleRevenuActiviteLucrativeDependante().getTypeRevenu() %>
				</td>					
				<td><%=JadeStringUtil.escapeXML(nomEmployeur)%></td>
				<td style="text-align:right;"><%=montantActiviteLucrative.toStringFormat() %></td>
				<td style="text-align:right;"><%=deductionsSociales.toStringFormat() %></td>
				<td style="text-align:right;"><%=deductionsLPP.toStringFormat() %></td>
				<td><%=listeFrais %></td>
				<td style="text-align:right;"><%=montantFrais.toStringFormat() %></td>
				<td style="text-aligh:right;"><%=fraisDeGarde.toStringFormat()%></td>
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
		}%>
	</liste>
	<%@ include file="/pegasusRoot/ajax/commonListDonneeFinanciereWithWarning.jspf" %>
</message>
