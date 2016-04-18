<%@page import="ch.globaz.perseus.business.constantes.CSTypeRetenue"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@ page import="globaz.corvus.api.demandes.IREDemandeRente"%>
<%@ page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="globaz.corvus.utils.REPmtMensuel"%>
<%@ page import="globaz.corvus.vb.demandes.REInfoComplViewBean"%>
<%@ page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page import="globaz.framework.util.FWMessage"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.prestation.api.IPRInfoCompl"%>
<%@ page import="globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>

<%@ include file="/theme/detail/header.jspf" %>
<%
	idEcran="PRE0029";

	// Les labels de cette page commencent par LABEL_JSP_INF_D

	REInfoComplViewBean viewBean = (REInfoComplViewBean)session.getAttribute("viewBean");

	if (JadeStringUtil.isBlankOrZero(viewBean.getCsVetoPrestation())){
		viewBean.setCsVetoPrestation(IREDemandeRente.CS_VETO_PRESTATION_PAS_VETO);
	}
	
%>
<%@ include file="/theme/detail/javascripts.jspf" %>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>

		<script type="text/javascript">
			var $form = null;
			var $userAction = null;
			var $method = null;
			var $remarque = null;
			var $boutonRetour = null;
			var $boutonTerminer = null;

			function upd() {
				if(disableFormInputs){
					disableFormInputs();
				}
				$boutonRetour.hide();
			}

			function add() {
			}

			function del() {
			}

			function cancel() {
				if ($method.val() == "add"){
					$userAction.val("back");
				} else {
					$userAction.val("<%=IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.afficherInformationsComplementaires");
				}
			}

			function validate() {
				state = validateFields();
				$userAction.val("<%=IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.ajouterInformationsComplementaires");
				return state;
			}

			function init() {
			}

			function retour() {
				$userAction.val("<%=IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.arreterInformationsComplementaires");
				$form.submit();
			}

			function imprimer(){
<%	if (viewBean.doPrintButtonGenereTransfertDossier()) {
%>				$userAction.val("<%=IREActions.ACTION_GENERER_TRANSFERT_DOSSIER%>.afficher");
				$form.submit();
<%	} else if (viewBean.doPrintButtonGenereRenteSurvivantPerdure()) {
%>				$userAction.val("<%=IREActions.ACTION_GENERER_RENTE_VEUVE_PERDURE%>.afficher");
				$form.submit();
<%	}%>
			}

			function terminer() {
				$userAction.val('<%=IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.terminerDemandeVieillesse');
				$form.submit();
			}

			function limiteur() {
				// limite la saisie de la remarque à 255 caractères
				maximum = 252;
				if ($remarque.val().length > maximum){
					$remarque.val($remarque.val().substring(0, maximum));
				}
			}

			function removeZero(field) {
				var value = field.value;

				while(value.charAt(0)=='0'){
					value = value.substring(1, value.length);
				}

				field.value = value;
			}

<%
	if (viewBean.isValide()) {
%>			function disableFormInputs() {
				$body = $("#bodyCorps");
				disableItem($body.children('input'));
				disableItem($body.children('select'));
				disableItem($body.children('textarea'));
			}

			function disableItem($item) {
				$item.attr('readonly','true');
				$item.attr('tabindex','-1');
				$item.attr('disabled','true');
	}
<%
	}
%>
			$(document).ready(function () {
				$form = $('form[name="mainForm"]');
				$userAction = $('input[name="userAction"]');
				$method = $('input[name="_method"]');

				$remarque = $('#remarque');

				$boutonRetour = $('#bRetour');
				$boutonTerminer = $('#bTerminer');
				$typeInfoCompl = $('#typeInfoCompl');
				$btnVal = $('#btnVal');
				$msgBlocage = $('#msgBlocage');
				$msgBlocage.fadeOut(0);
				
				$typeInfoCompl.change(function () {
					if(<%=viewBean.getIsBloque()%>){
						if($typeInfoCompl.val() === '<%=IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_TRANSFERT_DOSSIER%>') {
							$btnVal.fadeOut(0); 
							$msgBlocage.fadeIn(0);
						} else {
							$btnVal.fadeIn(0);
							$msgBlocage.fadeOut(0);
						}
					}
				});
			});

			$('html').bind(eventConstant.JADE_FW_ACTION_DONE, function(){
				if(disableFormInputs){
					disableFormInputs();
				}
			});
		</script>
		
		<style>
			.ui-state-error{height:5px;}
		</style>
<%@ include file="/theme/detail/bodyStart.jspf" %>
				<ct:FWLabel key="JSP_INF_D_TITRE" />
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<tr>
							<td colspan="4">
									<input	type="hidden" 
											name="idDemandeRente" 
											value="<%=viewBean.getIdDemandeRente()%>" />
									<input	type="hidden" 
											name="idInfoCompl" 
											value="<%=viewBean.getIdInfoCompl()%>" />
									<input	type="hidden" 
											name="idTiers" 
											value="<%=viewBean.getIdTiers()%>" />
									<re:PRDisplayRequerantInfoTag	session="<%=(globaz.globall.db.BSession)controller.getSession()%>"
																	idTiers="<%=viewBean.getIdTiers()%>"
																	style="<%=PRDisplayRequerantInfoTag.STYLE_CONDENSED%>" />
							</td>
						</tr>
						<tr>
							<td colspan="4" height="40">
								<hr />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_INF_D_TYPE_INF_COMP" />
							</td>
							<td>
								<ct:select	id="typeInfoCompl"
											name="typeInfoCompl" 
											wantBlank="true" 
											defaultValue="<%=viewBean.getTypeInfoCompl()%>">
									<ct:optionsCodesSystems csFamille="REINFCMP">
<%
	if (JadeStringUtil.isEmpty(viewBean.getTypeInfoCompl())) {
%>										<ct:excludeCode code="<%=IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_ENVOYE%>" />
<%
	} else if (!viewBean.isInfoComplementaireEnvoye()) {
%>										<ct:excludeCode code="<%=IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_ENVOYE%>" />
<%
	}
	if (!viewBean.isValideForRenteSurvivantPerdure()) {
%>										<ct:excludeCode code="<%=IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_RENTE_VEUVE_PERDURE%>" />
<%
	}
	if (!viewBean.isDemandeRenteVieillesse()) {
%>										<ct:excludeCode code="<%=IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_DECES%>"/>
<%
	}
%>									</ct:optionsCodesSystems>
								</ct:select>
							</td>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_INF_D_DATE_EDITION" />
							</td>
							<td>
								<input	id="dateInfoCompl"
										name="dateInfoCompl"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateInfoCompl()%>" />
								<input	type="hidden" 
										name="dateInfoComp" 
										value="<%=viewBean.getDateInfoCompl()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_INF_D_CAISSE_AGENCE" />
							</td>
							<td>
								<input	type="text" 
										name="noCaisse" 
										value="<%=viewBean.getNoCaisse()%>" 
										size="4" 
										maxlength="3" 
										onchange="removeZero(this);" />
								&nbsp;/&nbsp;
								<input	type="text" 
										name="noAgence" 
										value="<%=viewBean.getNoAgence()%>" 
										size="4" 
										maxlength="3" 
										onchange="removeZero(this);" />
							</td>
						</tr>
						<tr>
							<td colspan="4">
								&nbsp;
							</td>
						</tr>
						<tbody id="bodyCorps">
							<tr>
								<td>
									<ct:FWLabel key="JSP_INF_D_CAS_DE_SUCCESSION" />
								</td>
								<td colspan="3">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_INF_D_MOTIF_REMPLACEMENT" />
								</td>
								<td colspan="3">
									<ct:select	name="motif" 
												wantBlank="true" 
												defaultValue="<%=viewBean.getMotif()%>">
										<ct:optionsCodesSystems csFamille="REMOTREMP">
										</ct:optionsCodesSystems>
									</ct:select>
								</td>
							</tr>
							<tr>
								<td colspan="4">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_INF_D_TRANSFERT_01" />
								</td>
								<td>
									<input	type="checkbox" 
											name="isTransfere" 
											value="on"
											<%=viewBean.getIsTransfere().booleanValue()?"checked":""%> />
								</td>
								<td>
									<ct:FWLabel key="JSP_INF_D_TIERS_RESPONSABLE" />
								</td>
								<td>
									<ct:select	name="csTiersResponsable" 
												defaultValue="<%=viewBean.getCsTiersResponsable()%>" 
												wantBlank="true">
										<ct:optionsCodesSystems csFamille="<%=IPRInfoCompl.CS_GROUPE_TIERS_RESPONSABLE%>">
										</ct:optionsCodesSystems>
									</ct:select>
								</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_INF_D_INDEPENDANT" />
								</td>
								<td>
									<input	type="checkbox" 
											name="isIndependant" 
											value="on" 
											<%=viewBean.getIsIndependant().booleanValue()?"checked":""%> />
								</td>
								<td>
									<ct:FWLabel key="JSP_INF_D_PERMIS_DE_SEJOUR" />
								</td>
								<td>
									<input	type="checkbox" 
											name="isPermisSejours" 
											value="on" 
											<%=viewBean.getIsPermisSejours().booleanValue()?"checked":""%> />
								</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_INF_D_CAS_PENIBLE_AI" />
								</td>
								<td>
									<input	type="checkbox" 
											name="isCasPenibleAI" 
											value="on" 
											<%=viewBean.getIsCasPenibleAI().booleanValue()?"checked":""%> />
								</td>
								<td>
									<ct:FWLabel key="JSP_INF_D_REFUGIE" />
								</td>
								<td>
									<input	type="checkbox" 
											name="isRefugie" 
											value="on" 
											<%=viewBean.getIsRefugie().booleanValue()?"checked":""%> />
								</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_INF_D_VETO_PRESTATION" />
								</td>
								<td colspan="3">
									<ct:select	name="csVetoPrestation" 
												defaultValue="<%=viewBean.getCsVetoPrestation() %>">
										<ct:optionsCodesSystems csFamille="REVETO">
										</ct:optionsCodesSystems>
									</ct:select>
								</td>
							</tr>
							<tr>
								<td colspan="4">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_INF_D_REMARQUE_DECISION" />
								</td>
								<td colspan="3">
									<textarea	name="remarque" 
												id="remarque" 
												cols="85" 
												rows="3" 
												onKeyDown="limiteur();">
										<%=viewBean.getRemarque()%>
									</textarea>
								</td>
							</tr>
<%
	if (viewBean.isCalculPrevisionnel()) {
%>							<tr>
								<td colspan="4">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_INF_D_TAXE_FACTUREE_ASSURE" />
								</td>
								<td colspan="3">
									<input	type="text" 
											id="taxeFactureeAssure" 
											name="taxeFactureeAssure" 
											value="<%=viewBean.getTaxeFactureeAssure()%>" 
											data-g-amount=" " />
								</td>
							</tr>
							
<%
	}
%>
	<tr>
		<td colspan="4">
			<div id="msgBlocage" >
				<!-- La hauteur de ce div est définit en haut dans une balise <style> .ui-state-error{height:5px;} -->
				<div class="msgBlocage" data-g-boxmessage="type:ERROR" >
					<ct:FWLabel key="JSP_INF_ERREUR_RENTE_BLOQUE" />					
				</div>
			</div>
		</td>
	</tr>


						</tbody>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%
	if (viewBean.doShowPrintButton()) {
%>				<input	id="bImprimer" 
						type="button" 
						onclick="imprimer();"
						value="<%=viewBean.getSession().getLabel("JSP_IMPRIMER") %>" />
<%
	}
	if (viewBean.hasNoError()) {
%>				<input	id="bRetour" 
						type="button" 
						value="<%=viewBean.getSession().getLabel("JSP_RETOUR") %>" 
						onclick="retour();" />
<%
	}
	if (viewBean.isInfoComplementaireDeces()) {
%>				<input	id="bTerminer" 
						type="button" 
						value="<%=viewBean.getSession().getLabel("JSP_TERMINER") %>"
						onclick="terminer();" />
<%
	}
%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%@ include file="/theme/detail/footer.jspf" %>
