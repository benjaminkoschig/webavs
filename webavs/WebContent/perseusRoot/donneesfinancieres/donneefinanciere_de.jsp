<%@page import="ch.globaz.perseus.business.constantes.CSTypeDemande"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatDemande"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%@page import="globaz.perseus.vb.donneesfinancieres.PFDonneefinanciereViewBean"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<% 

PFDonneefinanciereViewBean viewBean = (PFDonneefinanciereViewBean) session.getAttribute("viewBean"); 
idEcran="PPF0411";
autoShowErrorPopup = true;

bButtonDelete = false;
bButtonCancel = true;

if (CSEtatDemande.VALIDE.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getCsEtatDemande())) {
	bButtonUpdate = false;
}

%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript">

	function add() {}
	function upd() {
		//Si la demande est de type aide catégorielle, seulement 3 champs modifiables
		<% if (CSTypeDemande.AIDES_CATEGORIELLES.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getTypeDemande())) { %>
			readOnly(true);
			$("#aideFormation")[0].disabled = false;
			$("#aideFormationDateDebut")[0].disabled = false;
			$("#aideFormationDateFin")[0].disabled = false;
			$("#aidesLogement")[0].disabled = false;
			$("#brapa")[0].disabled = false;
		<% } %>
		$("#linkDF").hide();
	}
	function del() {}
	
	//Fonction permettant la validation d'une modification ou d'un ajout
	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="perseus.donneesfinancieres.donneefinanciere.ajouter";
	    else if (document.forms[0].elements('_method').value == "upd")
	        document.forms[0].elements('userAction').value="perseus.donneesfinancieres.donneefinanciere.modifier";
	    
	    return state;
	}
	
	//Fonction permettant d'annuler une opération en cours
	function cancel() {
		if (document.forms[0].elements('_method').value == "upd")
			document.forms[0].elements('userAction').value="back";
	}
	
	function init() {}
	function postInit() {}
	
	$(function() {
		$( "#tabs" ).tabs();
		$("#fraisTransport").change(function() {
			if ($("#fraisTransportAcceptes").val() == '') {
				$("#fraisTransportAcceptes").val($("#fraisTransport").val());
				$("#fraisTransportAcceptes").change();
			}	
		});

	});	
	
</script>

<style type="text/css">
	.montant {
		width: 120px;
	}
	
	.tabs_style {
		background-color: #B3C4DB !important;
	} 
	
</style>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_FINANCE_D_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
						<tr>
						
							<td>
	<table>
		<tr>
			<td><b><ct:FWLabel key="JSP_PF_FIANCE_D_PERIODE"/></b></td>
			<td colspan="2">
				<ct:FWLabel key="JSP_PF_FIANCE_D_PERIODE_DU"/>
				<span><strong><%=" "+viewBean.getDemande().getSimpleDemande().getDateDebut() + " " %></strong></span>
				<ct:FWLabel key="JSP_PF_FIANCE_D_PERIODE_AU"/>
				<span><strong><%=" "+viewBean.getDemande().getSimpleDemande().getDateFin()%></strong></span>
			</td>
		</tr>
		<tr>
			<td colspan="2"><br></td>
		</tr>
		<tr>
			<td valign="top" width="200">
				<b>
					<% if (!JadeStringUtil.isEmpty(viewBean.getIdRequerant())) { %>
						<ct:FWLabel key="JSP_PF_FINANCE_D_REQUERANT"/><br/>
						<% if (!JadeStringUtil.isEmpty(viewBean.getDemande().getSituationFamiliale().getConjoint().getId())) { %>
							<a id="linkDF" href="<%="perseus?userAction=perseus.donneesfinancieres.donneefinanciere.afficher&idDemande=" + viewBean.getDemande().getId() + "&idConjoint=" + viewBean.getDemande().getSituationFamiliale().getConjoint().getId()%>">
								<ct:FWLabel key="JSP_PF_FINANCE_D_CONJOINT"/>
							</a>				
						<% } %>
					<% } else { %>
						<ct:FWLabel key="JSP_PF_FINANCE_D_CONJOINT"/><br/>
						<a id="linkDF" href="<%="perseus?userAction=perseus.donneesfinancieres.donneefinanciere.afficher&idDemande=" + viewBean.getDemande().getId() + "&idRequerant=" + viewBean.getDemande().getSituationFamiliale().getRequerant().getId()%>">
							<ct:FWLabel key="JSP_PF_FINANCE_D_REQUERANT"/>
						</a>				
					<% } %>
				</b>
			</td>
			<td>
				<%=PFUserHelper.getDetailAssure(objSession,viewBean.getMembreFamille().getPersonneEtendue()) %>
			</td>
		</tr>
		<% if (!JadeStringUtil.isEmpty(viewBean.getIdRequerant())) { %>
			<tr>
				<td valign="top"><b><ct:FWLabel key="JSP_PF_FINANCE_D_LCALITE"/></b></td>
				<td>
					<%=viewBean.getAdresseAssure() %>
					<br />
					 <% if (viewBean.getLoyerAnnuel().getPenurieLogement() != null && !viewBean.getLoyerAnnuel().getPenurieLogement()) { %>
						<input type="checkbox" name="penurieLogement"/>
					<% } else { %>
						<input type="checkbox" name="penurieLogement" checked="checked"/>
					<% } %> 
					&nbsp; <ct:FWLabel key="JSP_PF_FINANCE_D_PENURIE_LOGEMENT"/>
				</td>
			</tr>
		<% } %>
	</table>
	<hr />
			
	<DIV class="container">
		<DIV id="tabs">
			<UL>
				<LI><A href="#tabs-1"><ct:FWLabel key="JSP_PF_FINANCE_D_FORTUNE_DETTES"/></A></LI>
				<LI><A href="#tabs-2"><ct:FWLabel key="JSP_PF_FINANCE_D_REVENUS"/></A></LI>
				<LI><A href="#tabs-3"><ct:FWLabel key="JSP_PF_FINANCE_D_DEPENSES_RECONNUES"/></A></LI>
			</UL>
			<DIV id="tabs-1" class="tabs_style">
				<table cellpadding="5" cellspacing="0" width="95%">
					<tr>
						<td colspan="6"><b><ct:FWLabel key="JSP_PF_FINANCE_D_FORTUNE_MOB"/></b></td>
					</tr>
					<tr>
						<td width="20%"><ct:FWLabel key="JSP_PF_FINANCE_D_ARGENT"/></td>
						<td width="13%"><ct:inputText name="liquidite.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td width="20%"><ct:FWLabel key="JSP_PF_FINANCE_D_RACHAT_ASSUR_VIE"/></td>
						<td width="13%"><ct:inputText name="rachatAssuranceVie.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td width="20%"><ct:FWLabel key="JSP_PF_FINANCE_D_AUTRES_BIENS"/></td>
						<td width="13%"><ct:inputText name="autreBien.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_MEMBRE_HOIRIE"/></td>
						<td><ct:inputText name="hoirie.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_NOM_HOIRIE"/></td>
						<td><ct:inputText name="hoirie.nomHoirie" /></td>
						<td colspan="2">&nbsp;</td>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_CESSION_BIENS"/></td>
						<td><ct:inputText name="cession.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_CESSION"/></td>
						<td><ct:inputText name="cession.dateCession" notation="data-g-calendar=' '" /></td>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="6"><b><ct:FWLabel key="JSP_PF_FINANCE_D_IMMEUBLES"/></b></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_IMMEUBLE_HABITE"/></td>
						<td><ct:inputText name="immeubleHabite.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_AUTRES_IMMEUBLES"/></td>
						<td><ct:inputText name="autresImmeubles.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_IMMOBILIERS_ETRANGERS"/></td>
						<td><ct:inputText name="biensEtrangers.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
					</tr>
					<tr> 
						<td colspan="6"><b><ct:FWLabel key="JSP_PF_FINANCE_D_DETTES"/></b></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DETTES_HYPOTHECAIRES"/></td>
						<td><ct:inputText name="dettesHypothecaires.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_AUTRES_DETTES"/></td>
						<td><ct:inputText name="autresDettes.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td colspan="2">&nbsp;</td>
					</tr>
				</table>
			</DIV>
			<DIV id="tabs-2" class="tabs_style">
				<table cellpadding="5" cellspacing="0" width="95%">
					<tr>
						<td colspan="6"><b><ct:FWLabel key="JSP_PF_FINANCE_D_REVENU_ACTIVITE"/></b></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_SALAIRE_NET"/></td>
						<td><ct:inputText name="salaireNet.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'"/></td>
						<td colspan="2">

							<% if (viewBean.getSalaireNet().getAvec13eme() != null && viewBean.getSalaireNet().getAvec13eme()) { %>
								<input type="checkbox" name="avec13emeSalaire" checked="checked"/>
							<% } else { %>
								<input type="checkbox" name="avec13emeSalaire" />
							<% } %> 
							&nbsp; <ct:FWLabel key="JSP_PF_FINANCE_D_13EME_SALAIRE"/>
							&nbsp;&nbsp;<br/>
							<% if (viewBean.getSalaireNet().getPlusieursEmployeurs() != null && viewBean.getSalaireNet().getPlusieursEmployeurs()) { %>
								<input type="checkbox" name="plusieursEmployeurs" checked="checked"/>
							<% } else { %>
								<input type="checkbox" name="plusieursEmployeurs" />
							<% } %> 
							&nbsp; <ct:FWLabel key="JSP_PF_FINANCE_D_PLUSIEURS_EMPLOYEURS"/>

						</td>
						
						
					</tr>
					<% if (viewBean.getDemande().getSimpleDemande().getPermisB()){ %>
						<tr>	
							<td>
								<ct:FWLabel key="JSP_PF_FINANCE_D_REVENU_IMPOT_SOURCE"/></br>
								<i><ct:FWLabel key="JSP_PF_FINANCE_D_REVENU_IMPOT_SOURCE_DETAILS"/></i>
							</td>
							<td><ct:inputText name="revenuBrutImpotSource.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						</tr>
						<% } %> 
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_TAUX_OCCUPATION"/></td>
						<td><ct:inputText name="tauxOccupation.simpleDonneeFinanciere.valeur" notation="data-g-integer=' '" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_REVENU_INDEPENDANT"/></td>
						<td><ct:inputText name="revenuIndependant.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_SALAIRE_NATURE"/></td>
						<td><ct:inputText name="salaireNature.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
					</tr>
					<tr>
						<td colspan="6"><b><ct:FWLabel key="JSP_PF_FINANCE_D_INDEMNITES_JOUR"/></b></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_IJA_MALADIE"/></td>
						<td><ct:inputText name="indemnitesJournalieresMaladie.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'"/></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_DEBUT"/></td>
						<td><ct:inputText name="indemnitesJournalieresMaladie.dateDebut" notation="data-g-calendar=' '" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_FIN"/></td>
						<td><ct:inputText name="indemnitesJournalieresMaladie.dateFin" notation="data-g-calendar=' '" /></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_IJA_ACCIDENT"/></td>
						<td><ct:inputText name="indemnitesJournalieresAccidents.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_DEBUT"/></td>
						<td><ct:inputText name="indemnitesJournalieresAccidents.dateDebut" notation="data-g-calendar=' '" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_FIN"/></td>
						<td><ct:inputText name="indemnitesJournalieresAccidents.dateFin" notation="data-g-calendar=' '" /></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_IJA_CHOMAGE"/></td>
						<td><ct:inputText name="indemnitesJournalieresChomage.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_DEBUT"/></td>
						<td><ct:inputText name="indemnitesJournalieresChomage.dateDebut" notation="data-g-calendar=' '" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_FIN"/></td>
						<td><ct:inputText name="indemnitesJournalieresChomage.dateFin" notation="data-g-calendar=' '" /></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_IJA_APG"/></td>
						<td><ct:inputText name="indemnitesJournalieresAPG.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_DEBUT"/></td>
						<td><ct:inputText name="indemnitesJournalieresAPG.dateDebut" notation="data-g-calendar=' '" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_FIN"/></td>
						<td><ct:inputText name="indemnitesJournalieresAPG.dateFin" notation="data-g-calendar=' '" /></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_IJA_AI"/></td>
						<td><ct:inputText name="indemnitesJournalieresAI.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_DEBUT"/></td>
						<td><ct:inputText name="indemnitesJournalieresAI.dateDebut" notation="data-g-calendar=' '" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_FIN"/></td>
						<td><ct:inputText name="indemnitesJournalieresAI.dateFin" notation="data-g-calendar=' '" /></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_IJA_MILITAIRE"/></td>
						<td><ct:inputText name="indemnitesJournalieresMilitaire.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_DEBUT"/></td>
						<td><ct:inputText name="indemnitesJournalieresMilitaire.dateDebut" notation="data-g-calendar=' '" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_FIN"/></td>
						<td><ct:inputText name="indemnitesJournalieresMilitaire.dateFin" notation="data-g-calendar=' '" /></td>
					</tr>
					<tr>
						<td colspan="6"><b><ct:FWLabel key="JSP_PF_FINANCE_D_PENSION_ALLOCATIONS_ETC"/></b></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_PENSIONS_BRAPA"/></td>
						<td><ct:inputText name="brapa.simpleDonneeFinanciere.valeur" id="brapa" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_PENSIONS_SANS_AVANCE"/></td>
						<td><ct:inputText name="pensionAlimentaire.simpleDonneeFinanciere.valeur" id="pensionAlimentaire" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_AF"/></td>
						<td><ct:inputText name="allocationsFamiliales.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_AIDES_FORMATION"/></td>
						<td><ct:inputText name="aideFormation.simpleDonneeFinanciere.valeur" id="aideFormation" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_DEBUT"/></td>
						<td><ct:inputText name="aideFormation.dateDebut" id="aideFormationDateDebut" notation="data-g-calendar=' '" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_FIN"/></td>
						<td><ct:inputText name="aideFormation.dateFin" id="aideFormationDateFin" notation="data-g-calendar=' '" /></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_ALCANT_MAT"/></td>
						<td><ct:inputText name="allocationCantonaleMaternite.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_DEBUT"/></td>
						<td><ct:inputText name="allocationCantonaleMaternite.dateDebut" notation="data-g-calendar=' '" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_FIN"/></td>
						<td><ct:inputText name="allocationCantonaleMaternite.dateFin" notation="data-g-calendar=' '" /></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_ALLOCATIONS_AMINH"/></td>
						<td><ct:inputText name="allocationsAMINH.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_DEBUT"/></td>
						<td><ct:inputText name="allocationsAMINH.dateDebut" notation="data-g-calendar=' '" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DATE_FIN"/></td>
						<td><ct:inputText name="allocationsAMINH.dateFin" notation="data-g-calendar=' '" /></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_CONTRAT_VIAGER"/></td>
						<td><ct:inputText name="contratEntretiensViager.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_AUTRES_RENTES"/></td>
						<td><ct:inputText name="autresRentes.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_AIDESIND_LOGEMENT" /></td>
						<td><ct:inputText name="aidesLogement.simpleDonneeFinanciere.valeur" id="aidesLogement" notation="data-g-amount='blankAsZero:false'" /></td>
					</tr>
					<tr>
						<td valign="top"><ct:FWLabel key="JSP_PF_FINANCE_D_RENTES"/></td>
						<td valign="top"><ct:inputText name="totalRentes.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td colspan="4">
							<ct:FWMultiCheckbox name="totalRentes.listCsTypeRentes" codeType="<%=IPFConstantes.CSGROUP_TYPE_RENTE %>" height="80px" tableWidth="450px" defaultValues="<%=viewBean.getTotalRentes().getListCsTypeRentes() %>" />
						</td>
					</tr>
					<tr>
						<td colspan="6"><b><ct:FWLabel key="JSP_PF_FINANCE_D_RENDEMENT_FORTUNE"/></b></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_LOYERS_FERMAGES"/></td>
						<td><ct:inputText name="loyersEtFermages.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_VALEURLOC_PROPREIMM"/></td>
						<td><ct:inputText name="valeurLocativePropreImmeuble.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_SOUS_LOCATION"/></td>
						<td><ct:inputText name="sousLocation.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_INTERETS_RECUS"/></td>
						<td><ct:inputText name="interetFortune.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td colspan="4">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="6"><b><ct:FWLabel key="JSP_PF_FINANCE_D_AUTRES_REVENUS"/></b></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_VALEUR_USUFRUIT"/></td>
						<td><ct:inputText name="valeurUsufruit.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_DROIT_HABITATION"/></td>
						<td><ct:inputText name="droitHabitation.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_CREANCES_TIERS"/></td>
						<td><ct:inputText name="autresCreances.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_REVENUS_SUCC_NONP"/></td>
						<td><ct:inputText name="successionNonPartagee.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td colspan="2">&nbsp;</td>
					</tr>
					<% if (viewBean.getDemande().getSimpleDemande().getCalculParticulier() && !JadeStringUtil.isEmpty(viewBean.getIdRequerant())) { %>
						<tr>
							<td colspan="6"><b><ct:FWLabel key="JSP_PF_FINANCE_D_CALCUL_PARTICULIER"/></b></td>
						</tr>
						<tr>
							<td><ct:FWLabel key="JSP_PF_FINANCE_D_REVENU_HYPOTHETIQUE"/></td>
							<td><ct:inputText name="revenuHypothetiqueCasRigueur.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
							<td colspan="4">&nbsp;</td>
						</tr>
					<% } %>
					
				</table>
			</DIV>
			<DIV id="tabs-3" class="tabs_style">
				<table cellpadding="5" cellspacing="0" width="95%">
					<% if (!JadeStringUtil.isEmpty(viewBean.getIdRequerant())) { %>
					<tr>
						<td colspan="6"><b><ct:FWLabel key="JSP_PF_FINANCE_D_LOYER"/></b></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_LOYER_ANNUEL"/></td>
						<td><ct:inputText name="loyerAnnuel.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false, mandatory:true'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_CHARGES_ANNUELLES"/></td>
						<td><ct:inputText name="chargesAnnuelles.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_NB_PERSONNES_LOGEMENT"/></td>
						<td><ct:inputText name="loyerAnnuel.nbPersonnesLogement" notation="data-g-integer='mandatory:true'" /></td>
					</tr>
					<!-- 
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_TAILLE_ASSISTANCE"/></td>
						<td><ct:inputText name="tailleUniteAssistance.simpleDonneeFinanciere.valeur" notation="data-g-integer=' '" /></td>
						<td colspan="4">&nbsp;</td>
					</tr>
					-->
					<% } %>
					<tr>
						<td colspan="6"><b><ct:FWLabel key="JSP_PF_FINANCE_D_COTISATIONS_DUES"/></b></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_COTISATIONS_DUES"/></td>
						<td><ct:inputText name="cotisationNonActif.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td colspan="4">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="6"><b><ct:FWLabel key="JSP_PF_FINANCE_D_DEDUC_FRAIS_IMMEUBLE"/></b></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_INTERETS_HYPO"/></td>
						<td><ct:inputText name="interetsHypothecaires.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_FRAIS_IMMEUBLES"/></td>
						<td><ct:inputText name="fraisEntretiensImmeuble.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="6"><b><ct:FWLabel key="JSP_PF_FINANCE_D_PENSION_AL_VERSEE"/></b></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_PENSION_AL_VERSEE"/></td>
						<td><ct:inputText name="pensionAlimentaireVersee.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td colspan="4">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="6"><b><ct:FWLabel key="JSP_PF_FINANCE_D_FRAIS_OBT_REVENU"/></b></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_FRAIS_REPAS_REVENU"/></td>
						<td><ct:inputText name="fraisRepas.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_FRAIS_VETEMENTS_REVENU"/></td>
						<td><ct:inputText name="fraisVetements.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_FRAIS_TRANSPORT_REVENU"/></td>
						<td><ct:inputText name="fraisTransport.simpleDonneeFinanciere.valeur" id="fraisTransport" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_FRAIS_TRANSPORT_REVENU_MODIF"/></td>
						<td><ct:inputText name="fraisTransport.simpleDonneeFinanciere.valeurModifieeTaxateur" id="fraisTransportAcceptes" notation="data-g-amount='blankAsZero:false'" /></td>
						<td colspan="2">&nbsp;</td>
					</tr>
				</table>
			</DIV>
		</DIV>
	</DIV>
	
							</td>
						</tr>
						<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
