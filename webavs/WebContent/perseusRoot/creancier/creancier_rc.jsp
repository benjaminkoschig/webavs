<%@page import="ch.globaz.perseus.business.models.lot.Prestation"%>
<%@page import="ch.globaz.perseus.business.models.lot.PrestationSearchModel"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee"%>
<%@page import="ch.globaz.perseus.business.models.creancier.CreancierSearchModel"%>
<%@page import="ch.globaz.perseus.business.models.creancier.Creancier"%>
<%@page import="globaz.perseus.vb.creancier.PFCreancierListViewBean"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatDemande"%>
<%@page import="ch.globaz.perseus.business.models.demande.Demande"%>
<%@ page language="java" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>

<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="globaz.perseus.vb.creancier.PFCreancierViewBean"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="ch.globaz.perseus.business.services.PerseusServiceLocator"%>

<%-- tpl:insert attribute="zoneInit" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
	<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
// Les labels de cette page commence par la préfix "JSP_PF_CREANCIER_R"

	idEcran="PPF0112";
	IFrameDetailHeight = "520";
	
	String affichePersonnne = "";
	String idDemande = request.getParameter("idDemande");
	if (idDemande == null) {
		idDemande = (String) request.getAttribute("idDemande");
	}
	
	Demande demande = PerseusServiceLocator.getDemandeService().read(idDemande);
	
	PersonneEtendueComplexModel personne = demande.getDossier().getDemandePrestation().getPersonneEtendue();
	BSession objSession = ((globaz.globall.db.BSession)session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_SESSION));
	affichePersonnne = PFUserHelper.getDetailAssure(objSession,personne);
	actionNew = actionNew + "&idDemande="+idDemande;
	
	CreancierSearchModel searchModel = new CreancierSearchModel();
	searchModel.setForIdDemande(idDemande);
	int nbrCreancier = PerseusServiceLocator.getCreancierService().count(searchModel);
	String etatDemande = demande.getSimpleDemande().getCsEtatDemande();

	
	boolean afficherRepartireCreance = false;
	bButtonFind = false;
	
	// Bouton de 'répartition des créances' caché si la demande est enregistrée.
	if (CSEtatDemande.ENREGISTRE.getCodeSystem().equals(etatDemande) && (nbrCreancier != 0)){
		afficherRepartireCreance = true;
	}

	// Bouton de 'répartition des créances' visible uniquement en présence de créancier si l'état de 
	// la demande est calculé.
	if (CSEtatDemande.CALCULE.getCodeSystem().equals(etatDemande) && (nbrCreancier != 0)){
		afficherRepartireCreance = true;
	}
	
	// Boutons 'nouveau' et bouton 'répartition de créances' cachés si la demande est en état enregistré
	// Le bouton de répartition de créances est visible uniquement si des créanciers sont présent.
	if (CSEtatDemande.VALIDE.getCodeSystem().equals(etatDemande)){
		bButtonNew = false;
		afficherRepartireCreance = false;
	}
	
	// Bouton 'repartirCreance' caché si aucune PCFAccordée n'est présente
	if (PerseusServiceLocator.getPCFAccordeeService().readForDemande(idDemande) == null){
		afficherRepartireCreance = false;
	}
	
	// Bouton 'répartition créances' visible uniquement si l'état de la demande est validé ET que des créanciers
	// sont présent dans la liste
	if (CSEtatDemande.VALIDE.getCodeSystem().equals(etatDemande) && (nbrCreancier != 0)){
		afficherRepartireCreance = true;
	}
%>
 	

<%-- /tpl:insert --%>
<%@ include file="/theme/capage/javascripts.jspf" %>

<%-- tpl:insert attribute="zoneScripts" --%>
<%@ include file="/perseusRoot/ajax/javascriptsAndCSS.jspf" %>
<link rel="stylesheet" type="text/css" media="screen" href="<%=rootPath%>/css/formTableLess.css">

<script language="JavaScript">
	var bFind = true;
	var detailLink = "<%=actionNew%>";
	var usrAction = "perseus.creancier.creancier.lister";
	var ACTION_CREANCE_ACCORDEE= "perseus.creancier.creanceAccordee";
	
	// Fonction permettant de modifier le nom du bouton en fonction de l'état de la demande
	<% if (CSEtatDemande.VALIDE.getCodeSystem().equals(etatDemande) || CSEtatDemande.ENREGISTRE.getCodeSystem().equals(etatDemande) ) { %>
		var LABEL_BUTTON_CRANCE_ACCRODEE='<ct:FWLabel key="BOUTON_PF_DETAIL_REPARTITION"/>';
	<% } else { %>
		var LABEL_BUTTON_CRANCE_ACCRODEE='<ct:FWLabel key="BOUTON_PF_REPARTIR_CREANCE"/>';
	<% } %>
	
	var b_afficheBoutton = <%=Boolean.toString(afficherRepartireCreance)%>;
	var creance = {
			addButtonRepartirCreance: function(){
				var $userAction = $('[name=userAction]');
				var $form= $('form')
				var s_target=$form.attr('target');
				$('<input/>',{
					type: 'button',
					value:LABEL_BUTTON_CRANCE_ACCRODEE,
					click: function() {
						$userAction.val(ACTION_CREANCE_ACCORDEE+".afficher"); 
						$form.attr('target','_self');
						$form.submit();
						form.attr('target',s_target);
					},
					"class": 'btnCtrl'
				}).prependTo('#btnArea');
			}
	}
	
	$(function (){ 
		if(b_afficheBoutton) {
			creance.addButtonRepartirCreance();
		}
	})
	

</script>
<style>
	.span span {
		padding-right:25px;
		padding-left:5px;
	}
	label{
		padding:5px 15px 0 0;
	}
</style>

<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_CREANCIER_TITRE"/><%-- /tpl:insert --%>
				
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
	<tr>
		<td>
	 		<label><ct:FWLabel key="JSP_PF_CREANCIER_R_ID_DEMANDE" /></label>
	 	</td>
	 	<td>
			<span><strong><%= idDemande%></strong></span>
		</td>
	</tr>
		<td>			
			<label><ct:FWLabel key="JSP_PF_CREANCIER_R_REQUERANT" /></label>
		 </td>
		 <td>
			<span><strong><%= affichePersonnne%></strong></span>
			<input type="hidden" name="creancierSearchModel.forIdDemande" value="<%=idDemande %>" />
		</td>
	</tr>
	<tr>
		<td colspan="2"><br></td>
	</tr>
	<tr>
		<td width="300px"><ct:FWLabel key="JSP_PF_CREANCIER_R_PERIODE_DEMANDE"/></td>
		<td colspan="2">
			<ct:FWLabel key="JSP_PF_CREANCIER_R_PERIODE_DU"/>
			<span><strong><%=" "+demande.getSimpleDemande().getDateDebut() + " " %></strong></span>
			<ct:FWLabel key="JSP_PF_CREANCIER_R_PERIODE_AU"/>
			<span><strong><%=" "+ demande.getSimpleDemande().getDateFin()%></strong></span>
		</td>
	</tr>
	<% 
		//HACK:Normalement simuler un viewBean
		//TODO:Simuler un viewBean
		PrestationSearchModel psm = new PrestationSearchModel();
		psm.setForIdDemande(demande.getId());
		psm = PerseusServiceLocator.getPrestationService().search(psm);
		
		if (psm.getSize() == 1) {
			Prestation pres = (Prestation) psm.getSearchResults()[0];
		//PCFAccordee pcfa = PerseusServiceLocator.getPCFAccordeeService().readForDemande(demande.getId());
		//if (pcfa != null && !JadeStringUtil.isEmpty(pcfa.getSimplePCFAccordee().getDateDecision())) {
		//	String dateFinRetro = JadeDateUtil.addDays(JadeDateUtil.addMonths("01." + pcfa.getSimplePCFAccordee().getDateDecision(), 1), -1);
	%>
				<tr><td colspan="2"><hr /></td></tr>
				<tr>
					<td><ct:FWLabel key="JSP_PF_CREANCIER_R_PERIODE_RETRO"/></td>
					<td colspan="2">
						<ct:FWLabel key="JSP_PF_CREANCIER_R_PERIODE_DU"/>
						<span><strong><%=" " + pres.getSimplePrestation().getDateDebut() + " " %></strong></span>
						<ct:FWLabel key="JSP_PF_CREANCIER_R_PERIODE_AU"/>
						<span><strong><%=" " + pres.getSimplePrestation().getDateFin() %></strong></span>
					</td>
				</tr>
	<% } %>
	
	 		<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				
				
				<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
