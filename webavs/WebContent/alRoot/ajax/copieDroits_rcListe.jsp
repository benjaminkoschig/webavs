<%@page import="globaz.jade.client.util.JadeCodesSystemsUtil"%>
<%@ page import="globaz.al.vb.ajax.ALCopieDroitsListViewBean"%>
<%@ page import="ch.globaz.al.business.models.droit.DroitComplexModel"%>
<%@ page import="ch.globaz.al.business.services.ALServiceLocator"%>
<%@ page import="ch.globaz.al.business.constantes.ALCSDroit"%>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>


<%-- taglib uri="/WEB-INF/taglib.tld" prefix="ct" --%>
<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String servletContext = request.getContextPath();
	ALCopieDroitsListViewBean viewBean = (ALCopieDroitsListViewBean) request.getAttribute("viewBean");
%>

<table id="copieDroitsTable">
	<thead>
		<tr>
			<th>Prénom</th>
			<th>Type</th>
			<th>Début</th>
			<th>Fin</th>
			<th>Echéance calculée</th>
			<th>Attestation</th>
			<th>Sélection</th>
		</tr>
	</thead>
	<tbody>
		
		<%
		
		String debutDroitRef = viewBean.getDroitReference().getDroitModel().getDebutDroit();
		String finDroitRef = viewBean.getDroitReference().getDroitModel().getFinDroitForcee();
		String idDroitRef = viewBean.getDroitReference().getId();
		
		for(int i=0;i<viewBean.getSearchModel().getSize();i++){ 
			DroitComplexModel currentDroit = (DroitComplexModel)viewBean.getSearchModel().getSearchResults()[i];
			System.out.println("1 droit trouvé");
			boolean beSelected = currentDroit.getId().equals(idDroitRef);
			String classLine = "";
			if(!debutDroitRef.equals(currentDroit.getDroitModel().getDebutDroit()) || !finDroitRef.equals(currentDroit.getDroitModel().getFinDroitForcee())){
				classLine = "unmatchedPeriod";
			}
		%>
		<tr class="<%=classLine%>">
			<td><input type="hidden" value="<%=currentDroit.getId()%>" class="idDroitValue"/><%=currentDroit.getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2() %></td>
			<td><%=JadeCodesSystemsUtil.getCodeLibelle(currentDroit.getDroitModel().getTypeDroit()) %></td>
			<td><%=currentDroit.getDroitModel().getDebutDroit() %></td>
			<td><%=currentDroit.getDroitModel().getFinDroitForcee() %></td>
			<td><%=ALServiceLocator.getDatesEcheanceService().getDateFinValiditeDroitCalculee(currentDroit)%></td>
			<td><%if(ALCSDroit.TYPE_FORM.equals(currentDroit.getDroitModel().getTypeDroit()) || !currentDroit.getEnfantComplexModel().getEnfantModel().getCapableExercer()){ %><input type="text" class="medium attestationDate" data-g-calendar=" " value="<%=currentDroit.getDroitModel().getDateAttestationEtude()%>"/><%} %></td>
			<td><input type="checkbox" name="aCopier" <%=beSelected?"checked='checked'":"" %>/></td>
		</tr>
		
		<%} %>
	</tbody>
</table>