<%@page import="ch.globaz.perseus.business.constantes.IPFActions"%>
<%@page import="globaz.op.wordml.types.ViewValue"%>
<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.jade.context.JadeThreadContext"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatFacture"%>
<%@page import="globaz.perseus.vb.qd.PFDetailfactureViewBean"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.prestation.interfaces.tiers.PRTiersHelper"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="ch.globaz.perseus.business.services.models.dossier.DossierService"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail_ajax_hybride/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%

	PFDetailfactureViewBean viewBean = (PFDetailfactureViewBean) session.getAttribute("viewBean");
	idEcran="PPF1131";
	
	bButtonUpdate = false;
	if (CSEtatFacture.ENREGISTRE.getCodeSystem().equals(viewBean.getSimpleFacture().getCsEtat())) {
		bButtonDelete = true;
		bButtonValidate = true;
	} else {
		bButtonValidate = false;
		bButtonDelete = false;
	}
	
	if(!objSession.hasRight("perseus.qd.detailfacture", FWSecureConstants.ADD)){
		bButtonValidate = false;
		bButtonDelete = false;
	}
		
	PersonneEtendueComplexModel personne = viewBean.getQd().getQdAnnuelle().getDossier().getDemandePrestation().getPersonneEtendue();
	String affichePersonnne = "";
	
	affichePersonnne = PFUserHelper.getDetailAssure(objSession,personne);
	
%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/javascripts.jspf" %>
<%@ include file="/perseusRoot/ajax/javascriptsAndCSS.jspf" %>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<%-- tpl:insert attribute="zoneScripts" --%>


<script type="text/javascript">
	var messageDelete = "<%=objSession.getLabel("JSP_PF_DOS_SUPPRESSION_CONFIRMATION")%>";
	var url = "perseus.qd.detailfacture";
	var userAction;

	$(function(){
		userAction=$('[name=userAction]',document.forms[0])[0];
	})
	
	function validateValider() {
		if (window.confirm("<%=objSession.getLabel("JSP_PF_FACT_VALIDER_CONFIRMATION")%>")){
		    state = true;
		    userAction.value= url+".actionValider";
		} else {
			state = false;
		}
		return state;
	}
	
	function validateRestituer() {
		if (window.confirm("<%=objSession.getLabel("JSP_PF_FACT_RESTITUER_CONFIRMATION")%>")){
		    state = true;
			userAction.value= url+".actionRestituer";
		} else {
			state = false;
		}
		return state;
	}
	
	function validateModifier() {
		userAction.value= "perseus.qd.facture.afficher";
		return true;
	}

	
	
</script>

<script type="text/javascript" src="<%=rootPath%>/scripts/jadeBaseFormulaire.js"/></script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_FAC_R_TITRE" /><%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
	<tr>
		<td>
			<table cellspacing="5" cellpadding="2">
				<tr>
					<td><ct:FWLabel key="JSP_PF_FAC_D_ASUURE"/></td>
					<td width="400"><%=affichePersonnne %></td>
				</tr>
				<tr><td colspan="2"><hr></td></tr>
				<tr>
					<td colspan="2">
						<fieldset style="padding: 10px;">
							<legend><ct:FWLabel key="JSP_PF_FAC_INFORMATIONS_QD"/></legend>
							
							<table cellspacing="5px">
								<tr>
									<td width="300">
										<ct:FWLabel key="JSP_PF_FAC_D_EXCEDANT_REVENU" />
									</td>
									<td>
										<%=new FWCurrency(viewBean.getSimpleQDAnnuelle().getExcedantRevenuCompense()).toStringFormat() %>
									</td>
									<td>/</td>
									<td>
										<%=new FWCurrency(viewBean.getSimpleQDAnnuelle().getExcedantRevenu()).toStringFormat() %>
									</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>
										<ct:FWLabel key="JSP_PF_FAC_D_QD_UTILISEE" />
									</td>

									<td>
										<%=new FWCurrency(viewBean.getQd().getMontantUtilise()).toStringFormat() %>
									</td>
									<td>/</td>
									<td>
										<%=new FWCurrency(viewBean.getQd().getMontantLimite()).toStringFormat() %>
									</td>
									<td>&nbsp;</td>
								</tr>
							</table>
														
						</fieldset>
					</td>
				</tr>
				<tr><td colspan="2"><hr></td></tr>
				<tr>
					<td><ct:FWLabel key="JSP_PF_FAC_D_GESTIONNAIRE" /></td>
					<td style="border: 1px solid;">
						<%=PFGestionnaireHelper.getDetailGestionnaire(objSession, viewBean.getSimpleFacture().getIdGestionnaire()) %>
					</td>
				</tr>
				
				<tr>
					<td><ct:FWLabel key="JSP_PF_FAC_D_DATE_RECEPTION" /></td>
					<td style="border: 1px solid;">
						<%=viewBean.getSimpleFacture().getDateReception() %>
					</td>
				</tr>

				<tr>
					<td><ct:FWLabel key="JSP_PF_FAC_D_DATE_FACTURE" /></td>
					<td style="border: 1px solid;">
						<%=viewBean.getSimpleFacture().getDateFacture() %>
					</td>
				</tr>
				
				<tr>
					<td><ct:FWLabel key="JSP_PF_FAC_D_DATE_PRISEENCHARGE" /></td>
					<td style="border: 1px solid;">
						<%=viewBean.getSimpleFacture().getDatePriseEnCharge() %>
					</td>
				</tr>

				<tr>
            		<td><ct:FWLabel key="JSP_PF_FAC_D_TYPE" /></td>
					<td style="border: 1px solid;">
						<%=objSession.getCodeLibelle(viewBean.getQd().getSimpleQD().getCsType()) %>
					</td>					
				</tr>

				<tr>
            		<td><ct:FWLabel key="JSP_PF_FAC_L_ETAT" /></td>
					<td style="border: 1px solid;">
						<%=objSession.getCodeLibelle(viewBean.getSimpleFacture().getCsEtat()) %>
					</td>					
				</tr>	

				<tr>
            		<td><ct:FWLabel key="JSP_PF_FAC_D_MEMBRE_FAMILLE" /></td>
					<td style="border: 1px solid;">
						<%=viewBean.getMemberFamilleTiers().getDesignation1() %>
						<%=viewBean.getMemberFamilleTiers().getDesignation2() %>
					</td>
				</tr>

				<tr>
            		<td><ct:FWLabel key="JSP_PF_FAC_D_CaC_CASDERIGUEUR" /></td>
					<td style="border: 1px solid;">
						<input id="cbCasDeRigueur" disabled="disabled" type="checkbox" name="facture.simpleFacture.casDeRigueur" <%=(viewBean.getFacture().getSimpleFacture().getCasDeRigueur()) ? "checked" : ""%>/>
					</td>
				</tr> 
				
				<tr>
            		<td><ct:FWLabel key="JSP_PF_FAC_D_CaC_HYGIENISTEDENTAIRE" /></td>
					<td style="border: 1px solid;">
						<input id="cbHygieniste" disabled="disabled" type="checkbox" name="facture.simpleFacture.hygienisteDentaire" <%=(viewBean.getFacture().getSimpleFacture().getHygienisteDentaire()) ? "checked" : ""%>/>
					</td>
				</tr>  
				
				<% if(viewBean.getFacture().getSimpleFacture().getHygienisteDentaire()){ %>
				
					<tr id="minutesHygienisteTr">
            			<td><ct:FWLabel key="JSP_PF_FAC_D_MINUTES_HYGIENISTE" /></td>
						<td style="border: 1px solid;"><%=viewBean.getFacture().getSimpleFacture().getMinutesHygieniste() %></td>
					</tr>
				<% } %>
				
				
				<tr><td colspan="2"><hr></td></tr>
				<tr>
					<td valign="top"><ct:FWLabel key="JSP_PF_FAC_D_FOURNISSEUR" /></td>
					<td style="border: 1px solid;">
                    	<%=viewBean.getSimpleFacture().getFournisseur() %>
                    	&nbsp;
					</td>		
                 </tr>

				<tr>
					<td valign="top"><ct:FWLabel key="JSP_PF_FAC_D_PERIODE" /></td>
					<td style="border: 1px solid;">
                    	<%=viewBean.getSimpleFacture().getDateDebutTraitement() + " - " + viewBean.getSimpleFacture().getDateFinTraitement() %>
					</td>		
                 </tr>

				<tr>
					<td valign="top"><ct:FWLabel key="JSP_PF_FAC_D_LIBELLE" /></td>
					<td style="border: 1px solid;">
                    	<%=viewBean.getSimpleFacture().getLibelle() %>
                    	&nbsp;
					</td>		
                 </tr>
				
				<tr>
					<td><ct:FWLabel key="JSP_PF_FAC_D_MONTANT" /></td>
					<td style="border: 1px solid;" data-g-amountformatter=" " align="left">
						<%=viewBean.getSimpleFacture().getMontant() %>
					</td>
				</tr>
				
				<tr>
					<td><ct:FWLabel key="JSP_PF_FAC_D_MONTANT_REMBOURSE" /></td>
					<td style="border: 1px solid;" data-g-amountformatter=" ">
						<%=viewBean.getSimpleFacture().getMontantRembourse() %>
					</td>
				</tr>	

				<tr>
					<td><ct:FWLabel key="JSP_PF_FAC_D_MONTANT_EXCEDANT_COMPENSE" /></td>
					<td style="border: 1px solid;" data-g-amountformatter=" ">
						<%=viewBean.getSimpleFacture().getExcedantRevenuCompense() %>
					</td>
				</tr>	

				<tr>
					<td><ct:FWLabel key="JSP_PF_FAC_D_MONTANT_QD_DEPASSE" /></td>
					<td style="border: 1px solid;" data-g-amountformatter=" ">
						<%=viewBean.getSimpleFacture().getMontantDepassant() %>
					</td>
				</tr>	
				
				<tr>
            		<td><ct:FWLabel key="JSP_PF_FAC_D_MOTIF" /></td>
					<td style="border: 1px solid;">
						<% if (IPFConstantes.CS_MOTIF_FACTURE_AUTRE.equals(viewBean.getSimpleFacture().getCsMotif())) { %>
							<%=viewBean.getSimpleFacture().getMotifLibre() %>
							&nbsp;
						<% } else { %>
							<%=objSession.getCodeLibelle(viewBean.getSimpleFacture().getCsMotif()) %>
							&nbsp;
						<% } %>
					</td>		
				</tr>  
				<tr><td colspan="2"><hr></td></tr>
				<tr>
					<td valign="top"><ct:FWLabel key="JSP_PF_FAC_D_ADRESSE_COURRIER" /></td>
					<td style="border: 1px solid;">
					 	<pre style="font-size: x-small; font-family:Verdana,Arial;"><%=viewBean.getAdresseCourrier() %></pre>
					</td>
				</tr>
				<tr>
					<td valign="top"><ct:FWLabel key="JSP_PF_FAC_D_ADRESSE_PAIEMENT" /></td>
					<td style="border: 1px solid;">
					 	<pre style="font-size: x-small; font-family:Verdana,Arial;"><%=viewBean.getAdresseVersement() %></pre>
					</td>
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_PF_FAC_D_NUMERO_REFERENCE" /></td>
					<td style="border: 1px solid;">
						<%=viewBean.getSimpleFacture().getNumRefFacture() %>
						&nbsp;
					</td>
				</tr>
				<tr><td colspan="2"><hr></td></tr>
				<tr>
					<td><ct:FWLabel key="JSP_PF_FAC_D_ETAT_COMPTABILISATION" /></td>
					<td style="border: 1px solid;">
						<%=viewBean.getEtatComptabilisation() %>
						&nbsp;
						<input type="hidden" id="idFacture"  name="id" value="<%= viewBean.getId()%>" />
 						<input type="hidden" id="idDossier"  name="idDossier" value="<%= viewBean.getFacture().getQd().getQdAnnuelle().getDossier().getId()%>" /> 
					</td>
				</tr>
				
				<% if (viewBean.getHasDateValidation()) { %>
				<tr>
					<td><ct:FWLabel key="JSP_PF_FAC_D_DATE_VALIDATION" /></td>
					<td style="border: 1px solid;">
						<%=viewBean.getSimpleFacture().getDateValidation() %>
						&nbsp;
					</td>
				</tr>				
				<% } %>
			</table>
		</td>
	</tr>
				<%-- /tpl:insert --%>
				<%@ include file="/theme/detail_ajax_hybride/bodyButtons.jspf" %>
				
				<%-- tpl:insert attribute="zoneButtons" --%>
					<% if (viewBean.getIsRestitutionPossible() && objSession.hasRight("perseus.qd.detailfacture", FWSecureConstants.ADD)) { %>
						<input class="btnCtrl" id="btnRestit" type="button" value="Restituer la facture" onclick="if(validateRestituer()) action(COMMIT);" /> 
					<% } %>
					<% if (objSession.hasRight("perseus.qd.detailfacture", FWSecureConstants.UPDATE) && CSEtatFacture.ENREGISTRE.getCodeSystem().equals(viewBean.getSimpleFacture().getCsEtat())) { %>
						<input class="btnCtrl" id="btnModifier" type="button" value="Modifier la facture" onclick="if(validateModifier()) action(COMMIT);" /> 
					<% } %>
					<% if (CSEtatFacture.ENREGISTRE.getCodeSystem().equals(viewBean.getSimpleFacture().getCsEtat())) { %>
						<% if (((objSession.hasRight("perseus.facture.validation", FWSecureConstants.UPDATE)) || (objSession.hasRight("perseus.qd.detailfacture", FWSecureConstants.ADD) && Float.valueOf(viewBean.getSimpleFacture().getMontantRembourse()) < viewBean.getMontantMaxValidationUser())) 
								&& viewBean.isPaiementOKPourValidation()) { %>
							<input class="btnCtrl" id="btnVal" type="button" value="Valider la facture" onclick="if(validateValider()) action(COMMIT);" /> 
						<% } %>
					<% } %>
					
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/footer.jspf" %>
