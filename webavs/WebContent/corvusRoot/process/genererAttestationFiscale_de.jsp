<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>

<%@ include file="/theme/process/header.jspf" %>

<%@ page import="globaz.jade.client.util.JadeStringUtil" %>
<%@ page import="globaz.framework.controller.FWController" %>
<%@ page import="globaz.corvus.servlet.IREActions" %>
<%@ page import="globaz.corvus.vb.process.REGenererAttestationFiscaleViewBean" %>
<%@ page import="globaz.globall.util.JACalendar" %>
<%@ page import="globaz.corvus.vb.process.REGenererAttestationFiscaleViewBean" %>

<%
	// Les labels de cette page commence par la préfix "JSP_ATT_FISC"
	idEcran="PRE2006";

	REGenererAttestationFiscaleViewBean viewBean = (REGenererAttestationFiscaleViewBean) session.getAttribute("viewBean");
	FWController controller = (FWController) session.getAttribute("objController");

	userActionValue = IREActions.ACTION_GENERER_ATTESTATION_FISCALE + ".executer";

	String params = "&provenance1=TIERS&provenance2=CI";
	String jspLocation = servletContext + "/corvusRoot/numeroSecuriteSocialeSF_select.jsp";

%>

<%@ include file="/theme/process/javascripts.jspf" %>

	<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
	<script type="text/javascript" src="<%=servletContext%>/corvusRoot/script/process/genererAttestationFiscale_de.js"></script>

	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
	<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu" />

	<script type="text/javascript">
		var $userAction,
			$mainForm,
			$isDepuisDemande,
			$nss,
			$anneeAttestationFiscale,
			$dateImpressionMMAAAA,
			$radioAttestationUnique;

		function changeType () {
			$userAction.val("<%=IREActions.ACTION_GENERER_ATTESTATION_FISCALE%>.afficher");
			$mainForm.submit();
		}

		function validate () {
			var valide = true;

			if ($isDepuisDemande.val() === "true" 
				|| $radioAttestationUnique.is(':checked')) {
				if ($nss.val() === "") {
					addTexteError("<ct:FWLabel key='ERREUR_NSS_OBLIGATOIRE_ATT_FISC'/>");
					valide = false;
				}
			} else {
				if ($dateImpressionMMAAAA.val() === "") {
					addTexteError("<ct:FWLabel key='ERREUR_DATE_OBLIGATOIRE_MULTIPLE_ATT_FISC'/>");
					valide = false;
				}
			}

			if ($anneeAttestationFiscale.val() === "") {
				addTexteError("<ct:FWLabel key='ERREUR_ANNEE_OBLIGATOIRE_ATT_FISC'/>");
				valide = false;
			} else if ($anneeAttestationFiscale.val().length !== 4) {
				addTexteError("<ct:FWLabel key='ERREUR_FORMAT_ANNEE_ATT_FISC'/>");
				valide = false;
			}

			if ($adresseEmail.val() === "") {
				addTexteError('<ct:FWLabel key="ERREUR_ADRESSE_EMAIL_REQUISE" />');
				valide = false;
			}

			showErrors();
			errorObj.text = "";
			return valide;
		}

		function addTexteError (texteError) {
			if (errorObj.text == "") {
				errorObj.text = texteError;
			} else {
				errorObj.text = errorObj.text + "<br />" + texteError;
			}
		}

		$(document).ready(function () {
			$userAction = $('[name="userAction"]');
			$mainForm = $('form');
			$isDepuisDemande = $('#isDepuisDemande');
			$nss = $('[name="NSS"]');
			$anneeAttestationFiscale = $('#anneeAttestation');
			$dateImpressionMMAAAA = $('#dateImpressionAttMMAAA');
			$radioAttestationUnique = $('#attestationUnique');
		});
	</script>

<%@ include file="/theme/process/bodyStart.jspf" %>
			<ct:FWLabel key="JSP_ATT_FISC_TITRE" />
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<tr>
							<td>
								<input	type="hidden" 
										id="isDepuisDemande" 
										value="<%=viewBean.getIsDepuisDemande()%>" />
								<input	type="hidden" 
										id="isAttestationUnique" 
										name="isAttestationUnique" 
										value="<%=!viewBean.getIsGenerationMultiple()%>" />
							</td>
							<td>
								<table border="0" cellspacing="0" cellpadding="0" width="50%">
									<tbody class="depuisDemande">
										<tr>
											<td width="40%">
												<strong>
													<ct:FWLabel key="JSP_ATT_FISC_UNIQUE" />
												</strong>
											</td>
											<td width="60%">
												&nbsp;
											</td>
										</tr>
									</tbody>
									<tbody class="depuisMenu">
										<tr>
											<td width="50%">
												<ct:FWLabel key="JSP_ATT_FISC_UNIQUE" />
											</td>
											<td width="50%">
												<input	type="radio" 
														id="attestationUnique" 
														name="isGenerationMultiple" 
														value="" 
														<%=!viewBean.getIsGenerationMultiple() ? "checked=\"checked\"" : ""%> />
											</td>
										</tr>
										<tr>
											<td>
												<ct:FWLabel key="JSP_ATT_FISC_MULTIPLE" />
											</td>
											<td>
												<input	type="radio" 
														id="attestationMultiple" 
														name="isGenerationMultiple" 
														value="on" 
														<%=viewBean.getIsGenerationMultiple().booleanValue() ? "checked=\"checked\"" : ""%> />
											</td>
										</tr>
									</tbody>
									<tbody>
										<tr>
											<td>
												&nbsp;
											</td>
										</tr>
										<tr>
											<td>
												&nbsp;
											</td>
										</tr>
									</tbody>
									<tbody class="attestationUnique">
										<tr>
											<td>
												<ct:FWLabel key="JSP_ATT_FISC_NSS" />
											</td>
											<td>
												<ct1:nssPopup	name="NSS" 
																onFailure="" 
																onChange="" 
																params="<%=params%>" 
																value="<%=viewBean.getNumeroAvsFormateSansPrefixe()%>" 
																newnss="<%=viewBean.isNNSS()%>" 
																jspName="<%=jspLocation%>" 
																avsMinNbrDigit="3" 
																nssMinNbrDigit="3" />
											</td>
										</tr>
									</tbody>
									<tbody>
										<tr>
											<td>
												<label for="AnneeAttestation">
													<ct:FWLabel key="JSP_ATT_FISC_ANNEE" />
												</label>
											</td>
											<td>
												<input	type="text" 
														id="anneeAttestation" 
														name="anneeAttestations" 
														style="width: 4em;" 
														data-g-integer="mandatory:true,sizeMax:4"
														value="<%=viewBean.getAnneeAttestations()%>" />
											</td>
											<td>
												&nbsp;
											</td>
										</tr>
									</tbody>
									<tbody class="attestationUnique">
										<tr>
											<td>
												<label for="DateImpressionAttJJMMAAA">
													<ct:FWLabel key="JSP_ATT_FISC_DATEIMPRESSION_JJMMAAAA" />
												</label>
											</td>
											<td>
												<input	id="dateImpressionAttJJMMAAA" 
														name="dateImpressionAttJJMMAAA" 
														data-g-calendar=" " 
														value="<%=JACalendar.todayJJsMMsAAAA()%>" />
											</td>
										</tr>
									</tbody>
									<tbody class="attestationsMultiples">
										<tr>
											<td>
												<label for="dateImpressionAttMMAAA">
													<ct:FWLabel key="JSP_ATT_FISC_DATEIMPRESSION_MMAAAA" />
												</label>
											</td>
											<td>
												<input	id="dateImpressionAttMMAAA" 
														name="dateImpressionAttMMAAA" 
														data-g-calendar="type:month,mandatory:true" 
														value="<%=viewBean.getDateImpressionAttMMAAA()%>" />
											</td>
										</tr>
									</tbody>
									<tbody>
										<tr>
											<td>
												<label for="eMailAddress">
													<ct:FWLabel key="JSP_ATT_FISC_MAIL" />
												</label>
											</td>
											<td>
												<input	type="text" 
														id="eMailAddress" 
														name="eMailAddress" 
														data-g-email="mandatory:true" 
														value="<%=JadeStringUtil.isEmpty(viewBean.getEMailAddress()) ? controller.getSession().getUserEMail() : viewBean.getEMailAddress()%>" 
														class="libelleLong" />
											</td>
										</tr>
										<tr>
											<td>
												&nbsp;
											</td>
										</tr>
									</tbody>
<%
	if (!JadeStringUtil.isBlankOrZero(viewBean.getDisplaySendToGed())) {
%>									<tbody>
										<tr>
											<td>
												<ct:FWLabel key="JSP_ENVOYER_DANS_GED" />
											</td>
											<td>
												<input	type="checkbox" 
														name="isSendToGed" 
														value="on" 
														checked="checked" />
												<input	type="hidden" 
														name="displaySendToGed" 
														value="1" />
											</td>
										</tr>
										<tr>
											<td>
												&nbsp;
											</td>
										</tr>
									</tbody>
<%
	} else {
%>										<input	type="hidden" 
												name="isSendToGed" 
												value="false" />
										<input	type="hidden" 
												name="displaySendToGed" 
												value="0" />
<%
	}
%>									<tbody class="attestationsMultiples">
										<tr>
											<td colspan="2">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td colspan="2">
												<fieldset>
													<legend>
														<ct:FWLabel key="JSP_ATT_PLAGE_NSS" />
													</legend>
													<div>
														<label for="nssDe">
															<ct:FWLabel key="JSP_ATT_PLAGE_NSS_DE" />
														</label>
														&nbsp;
														<strong>
															756.
														</strong>
														<input	type="text" 
																id="nssDe" 
																value="0000.0000.00" 
																maxlength="12" 
																size="15" />
														<input	type="hidden" 
																id="hiddenNssDe" 
																name="nssDe" 
																value="756.0000.0000.00" />
														&nbsp;
														<label for="nssA">
															<ct:FWLabel key="JSP_ATT_PLAGE_NSS_A" />
														</label>
														&nbsp;
														<strong>
															756.
														</strong>
														<input	type="text" 
																id="nssA" 
																value="9999.9999.99" 
																maxlength="12" 
																size="15" />
														<input	type="hidden" 
																id="hiddenNssA" 
																name="nssA" 
																value="756.9999.9999.99" />
													</div>
												</fieldset>
											</td>
										</tr>
									</tbody>
									<tbody>
										<tr>
											<td>
												&nbsp;
											</td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>

<%@ include file="/theme/process/footer.jspf" %>

<%@ include file="/theme/process/bodyClose.jspf" %>
