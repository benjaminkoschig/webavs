<%@page import="ch.globaz.perseus.business.constantes.CSTypeDemande"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatDemande"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="globaz.perseus.vb.donneesfinancieres.PFEnfantDFViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<% 

PFEnfantDFViewBean viewBean = (PFEnfantDFViewBean) session.getAttribute("viewBean"); 
idEcran="PPF0421";
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
		<% } %>
	}
	function del() {}
	
	//Fonction permettant la validation d'une modification ou d'un ajout
	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="perseus.donneesfinancieres.enfantDF.ajouter";
	    else if (document.forms[0].elements('_method').value == "upd")
	        document.forms[0].elements('userAction').value="perseus.donneesfinancieres.enfantDF.modifier";
	    
	    return state;
	}
	
	//Fonction permettant d'annuler une opération en cours
	function cancel() {
		if (document.forms[0].elements('_method').value == "upd")
			document.forms[0].elements('userAction').value="back";
	}
	
	function init() {}
	function postInit() {}
	
	$(function(){
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
			<td valign="top" width="200"><b><ct:FWLabel key="JSP_PF_FINANCE_D_MEMBRE_FAMILLE"/></b></td>
			<td><%=PFUserHelper.getDetailAssure(objSession,viewBean.getMembreFamille().getPersonneEtendue()) %></td>
		</tr>
	</table>
	<hr />

				<table cellpadding="5" cellspacing="0" width="95%">
					<tr>
						<td colspan="6"><b><ct:FWLabel key="JSP_PF_FINANCE_ENF_FORTUNE"/></b></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_ENF_FORTUNE_ENFANT"/></td>
						<td><ct:inputText name="fortuneEnfant.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td colspan="4">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="6"><b><ct:FWLabel key="JSP_PF_FINANCE_ENF_REVENUS"/></b></td>
					</tr>
					<tr>
						<td>
							<ct:FWLabel key="JSP_PF_FINANCE_ENF_REVENUS_ACTIVITE_1"/>
							<b><ct:FWLabel key="JSP_PF_FINANCE_ENF_REVENUS_ACTIVITE_2"/></b>
							<ct:FWLabel key="JSP_PF_FINANCE_ENF_REVENUS_ACTIVITE_3"/>
						</td>
						<td><ct:inputText name="revenuActiviteEnfant.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_ENF_AUTRES_REVENUS"/></td>
						<td><ct:inputText name="autresRevenusEnfant.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
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
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_PENSION_ALIMENTAIRE_ENFANT"/></td>
						<td><ct:inputText name="pensionAlimentaireEnfant.simpleDonneeFinanciere.valeur" id="pensionAlimentaireEnfant" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_ENF_RENTES_ENFANT"/></td>
						<td><ct:inputText name="renteEnfant.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
												
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_D_PENSIONS_BRAPA"/></td>
						<td><ct:inputText name="brapaEnfant.simpleDonneeFinanciere.valeur" id="brapaEnfant" notation="data-g-amount='blankAsZero:false'" /></td>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr> 
						<td colspan="6"><b><ct:FWLabel key="JSP_PF_FINANCE_ENF_DEPENSES"/></b></td>
					</tr>
					<!-- 
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_ENF_LOYER"/></td>
						<td><ct:inputText name="loyerAnnuel.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_ENF_CHARGES"/></td>
						<td><ct:inputText name="chargesAnnuelles.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td colspan="2">&nbsp;</td>
					</tr>
					-->
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_ENF_FRAIS_REPAS"/></td>
						<td><ct:inputText name="fraisRepas.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_ENF_FRAIS_VETEMENTS"/></td>
						<td><ct:inputText name="fraisVetements.simpleDonneeFinanciere.valeur" notation="data-g-amount='blankAsZero:false'" /></td>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_PF_FINANCE_ENF_FRAIS_TRANSPORT"/></td>
						<td><ct:inputText name="fraisTransport.simpleDonneeFinanciere.valeur" id="fraisTransport" notation="data-g-amount='blankAsZero:false'" /></td>
						<td><ct:FWLabel key="JSP_PF_FINANCE_ENF_FRAIS_TRANSPORT_MODIF"/></td>
						<td><ct:inputText name="fraisTransport.simpleDonneeFinanciere.valeurModifieeTaxateur" id="fraisTransportAcceptes" notation="data-g-amount='blankAsZero:false'" /></td>
					</tr>
				</table>

							</td>
						</tr>
						<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
