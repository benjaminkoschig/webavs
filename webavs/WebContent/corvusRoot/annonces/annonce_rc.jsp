<%@page import="globaz.pyxis.db.tiers.ITIPersonneAvsDefTable"%>
<%@page import="globaz.pyxis.db.tiers.ITITiersDefTable"%>
<%@page import="globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A"%>
<%@ include file="/theme/find/header.jspf" %>

<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>

<%@page import="globaz.corvus.db.annonces.REAnnoncesAbstractLevel1AManager"%>
<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<%@page import="globaz.commons.nss.NSUtil"%>
<%@page import="globaz.corvus.vb.demandes.RENSSDTO"%>
<%@page import="globaz.corvus.vb.demandes.REDemandeParametresRCDTO"%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<%@page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	// Les labels de cette page commence par la préfix "JSP_ANN_R"

	idEcran = "PRE0070";

	rememberSearchCriterias = true;

	bButtonNew = false;

	String forIdRenteAccordee = request.getParameter("idRenteAccordee");
	if (JadeStringUtil.isNull(forIdRenteAccordee)) {
		forIdRenteAccordee = "";
	}

	String forMoisRapport = request.getParameter("forMoisRapport");
	if (JadeStringUtil.isNull(forMoisRapport)) {
		forMoisRapport = "";
	}

	String forCsEtat = request.getParameter("forCsEtat");
	if (JadeStringUtil.isNull(forCsEtat)) {
		forCsEtat = "";
	}

	String forCsCodeTraitement = request.getParameter("forCsCodeTraitement");
	if (JadeStringUtil.isNull(forCsCodeTraitement)) {
		forCsCodeTraitement = "";
	}

	String csTypeBasesCalcul = request.getParameter("csTypeBasesCalcul");
	String csEtatRenteAccordee = request.getParameter("csEtatRenteAccordee");
	String dateFinDroit = request.getParameter("dateFinDroit");
	String idTierRequerant = request.getParameter("idTierRequerant");
	String idBaseCalcul = request.getParameter("idBaseCalcul");
	String isPreparationDecisionValide = request.getParameter("isPreparationDecisionValide");
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
	String noDemandeRente = request.getParameter("noDemandeRente");
	String rechercheFamille = request.getParameter("rechercheFamille");

	RENSSDTO nssDto;
	REDemandeParametresRCDTO demandeDto;

	try{
		nssDto = (RENSSDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO);
	} catch (Exception ex) {
		nssDto = null;
	}

	try {
		demandeDto = (REDemandeParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO);
	} catch (Exception ex) {
		demandeDto = null;
	}

	String orderByMoisRapport = REAnnoncesAbstractLevel1A.FIELDNAME_MOIS_RAPPORT;
	String orderByNomPrenom = ITITiersDefTable.DESIGNATION_1 + "," + ITITiersDefTable.DESIGNATION_2;
	String orderByNumAvs = ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL;
	String orderByNumAnnonce = REAnnoncesAbstractLevel1A.FIELDNAME_ID_ANNONCE;
	String orderByEtat = REAnnoncesAbstractLevel1A.FIELDNAME_ETAT + "," + orderByNomPrenom;
%>
<%@ include file="/theme/find/javascripts.jspf" %>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<%
	if (JadeStringUtil.isNull(menuOptionToLoad) || JadeStringUtil.isEmpty(menuOptionToLoad)) {
%>	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu" />
	<ct:menuChange displayId="options" menuId="corvus-optionsannonces" />
<%
	} else if ("rentesaccordees".equals(menuOptionToLoad)) {
%>	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options" />
	<ct:menuChange displayId="options" menuId="corvus-optionsrentesaccordees">
		<ct:menuSetAllParams key="selectedId" value="<%=forIdRenteAccordee%>"/>
		<ct:menuSetAllParams key="idRenteAccordee" value="<%=forIdRenteAccordee%>" />
		<ct:menuSetAllParams key="noDemandeRente" value="<%=noDemandeRente%>" />
		<ct:menuSetAllParams key="idTierRequerant" value="<%=idTierRequerant%>" />
		<ct:menuSetAllParams key="idBaseCalcul" value="<%=idBaseCalcul%>" />
		<ct:menuSetAllParams key="csTypeBasesCalcul" value="<%=csTypeBasesCalcul%>" />
<%
		if (IRERenteAccordee.CS_ETAT_AJOURNE.equals(csEtatRenteAccordee)
			|| IRERenteAccordee.CS_ETAT_CALCULE.equals(csEtatRenteAccordee)
			|| IRERenteAccordee.CS_ETAT_DIMINUE.equals(csEtatRenteAccordee)
			|| !JadeStringUtil.isBlankOrZero(dateFinDroit)
			|| !REPmtMensuel.isValidationDecisionAuthorise(objSession)) {
%>		<ct:menuActivateNode active="no" nodeId="optdiminution" />
<%
		}
		if ("false".equals(isPreparationDecisionValide)) {
%>		<ct:menuActivateNode active="no" nodeId="preparerDecisionRA" />
<%
		}
%>	</ct:menuChange>
<%
	}
%>
<script type="text/javascript">
<%
	if (nssDto != null && !JadeStringUtil.isEmpty(nssDto.getNSS())) {
%>	bFind = true;
<%
	} else if (demandeDto != null) {
		if (!JadeStringUtil.isBlankOrZero(((REDemandeParametresRCDTO) demandeDto).getLikeNom()) 
			&& !JadeStringUtil.isBlankOrZero(((REDemandeParametresRCDTO) demandeDto).getLikePrenom())) {
%>	bFind = true;
<%
		} else if (!JadeStringUtil.isBlankOrZero(((REDemandeParametresRCDTO) demandeDto).getIdRenteAccordee())) {
%>	bFind = true;
<%
		} else if (forIdRenteAccordee != null && !JadeStringUtil.isBlank(forIdRenteAccordee)) {
%>	bFind = true;
<%
		}
	} else {
%>	bFind = false;
<%
	}
%>

	usrAction = "corvus.annonces.annonce.lister";

	function clearFields() {
		document.getElementsByName("likeNumeroAVS")[0].value = "";
		document.getElementsByName("partiallikeNumeroAVS")[0].value = "";
		document.getElementById("likeNom").value = "";
		document.getElementById("likePrenom").value = "";
		document.getElementById("forCsEtat").value = "";
		document.getElementById("forCsCodeTraitement").value = "";
		document.getElementById("orderBy").value = "<%=orderByNomPrenom%>";
		document.getElementById("forMoisRapport").value = "";
		document.getElementById("forNoRenteAccordee").value = "";

		isRechercheFamille();
		rechercheFamille();

		document.getElementsByName('partiallikeNumeroAVS')[0].focus();
	}

	function envoyerAnnonces() {
		document.getElementsByName("mainForm")[0].elements('userAction').value = "<%=IREActions.ACTION_ENVOYER_ANNONCE%>.afficher";
		document.getElementsByName("mainForm")[0].target = "fr_main";
		document.getElementsByName("mainForm")[0].submit();
	}

	function isRechercheFamille() {
		if (document.getElementsByName('likeNumeroAVSNNSS')[0].value == "true") {
			if (document.getElementsByName('likeNumeroAVS')[0].value.length > 15) {
				enableRechercheFamille(true);
				bFind = true;
			} else {
				enableRechercheFamille(false);
			}
		} else {
			if (document.getElementsByName('likeNumeroAVS')[0].value.length > 13) {
				enableRechercheFamille(true);
				bFind = true;
			} else {
				enableRechercheFamille(false);
			}
		}
	}

	function rechercheFamille() {
		// pas de recherche famille si le NSS n'est pas complet			
		if (document.getElementsByName('likeNumeroAVSNNSS')[0].value == "true") {
			if (document.getElementsByName('likeNumeroAVS')[0].value.length <= 15) {
				if (document.getElementById('isRechercheFamille').checked) {
					enableRechercheFamille(false);
				}
				return;
			}
		} else {
			if (!document.getElementsByName('likeNumeroAVS')[0].value.length <= 13) {
				if (document.getElementById('isRechercheFamille').checked) {
					enableRechercheFamille(false);
				}
				return;
			}
		}
	}

	function disableInput(value) {
		if (typeof(value) != "boolean") {
			alert(typeof(value));
			return;
		}
		document.getElementById("likeNom").disabled = value;
		document.getElementById("likePrenom").disabled = value;
		document.getElementById("forMoisRapport").disabled = value;
		document.getElementById("forNoRenteAccordee").disabled = value;
	}

	function enableRechercheFamille(value) {
		if (typeof(value) != "boolean") {
			return;
		}
		document.getElementById('isRechercheFamille').checked = value;
		disableInput(value);

		if (document.getElementById('isRechercheFamille').checked) {
			document.getElementById('orderBy').value = "<%=orderByEtat%>";
			document.getElementById('btnFind').focus();
		} else {
			document.getElementById('orderBy').value = "<%=orderByNomPrenom%>";
			document.getElementById('likeNom').focus();
		}
	}

	var fwkOnClickFind = window.onClickFind;

	//Surcharge de la méthode onClickFind du framework, pour vérifier la recherche famille avant le submit du formulaire.
	window.onClickFind = function() {
		// appel à la méthode originale
		fwkOnClickFind();

		//Complément:
		//Permet d'activer si nécessaire la checkbox isRechercheFamille avant la recherche.
		//Car en faisant un [return] clavier, le submit (la rechercher) s'effectue avant
		//le onChange du nssPopupTag, du coup la checkbox isRechercheFamille n'est pas prise en compte
		isRechercheFamille();
		rechercheFamille();
	}

	var $firstInput;
	$(document).ready(function() {
		isRechercheFamille();
		rechercheFamille();

		$firstInput = $('#partiallikeNumeroAVS');
		$firstInput.focus().select().addClass('hasFocus');

		$('[name="fr_list"]').one('load', function () {
			setTimeout(function () {
				$firstInput.focus().select().addClass('hasFocus');
			}, 50);
		});
	});

	$('html').one(eventConstant.JADE_FW_ACTION_DONE, function () {
		$firstInput.focus().select().addClass('hasFocus');
	});
</script>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<ct:FWLabel key="JSP_ANN_R_TITRE" />
<%@ include file="/theme/find/bodyStart2.jspf" %>
							<tr>
								<td>
									<ct:FWLabel key="JSP_ANN_R_NSS" />
									<input type="hidden" name="isPRE0070" value="true" />
								</td>
<%
	if (nssDto != null) {
%>								<td>
									<ct1:nssPopup 	avsMinNbrDigit="99" 
													nssMinNbrDigit="99" 
													newnss="" 
													name="likeNumeroAVS" 
													onChange="isRechercheFamille()" 
													value="<%=nssDto.getNSSForSearchField()%>" />
								</td>
<%
	} else {
%>								<td>
									<ct1:nssPopup	avsMinNbrDigit="99" 
													nssMinNbrDigit="99" 
													newnss="" 
													onChange="isRechercheFamille()" 
													name="likeNumeroAVS" />
								</td>
<%
	}
%>								<td>
									<ct:FWLabel key="JSP_ANN_R_NOM" />
								</td>
<%
	if (demandeDto != null && JadeStringUtil.isEmpty(forIdRenteAccordee)) {
%>								<td>
									<input	type="text" 
											id="likeNom" 
											name="likeNom" 
											value="<%=demandeDto.getLikeNom()%>" />
								</td>
<%
	} else {
%>								<td>
									<input	type="text" 
											id="likeNom" 
											name="likeNom" 
											value="" />
								</td>
<%
	}
%>								<td>
									<ct:FWLabel key="JSP_ANN_R_PRENOM" />
								</td>
<%
	if (demandeDto != null && JadeStringUtil.isEmpty(forIdRenteAccordee)) {
%>								<td>
									<input	type="text" 
											id="likePrenom" 
											name="likePrenom" 
											value="<%=demandeDto.getLikePrenom()%>" />
								</td>
<%
	} else {
%>								<td>
									<input	type="text" 
											id="likePrenom" 
											name="likePrenom" 
											value="" />
								</td>
<%
	}
%>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_ANN_R_MOIS_RAPPORT" />
								</td>
								<td>
									<input	id="forMoisRapport" 
											name="forMoisRapport" 
											data-g-calendar="type:month" 
											value="<%=forMoisRapport%>" />
								</td>
								<td>
									<ct:FWLabel key="JSP_ANN_R_NO_RENTE_ACCORDEE" />
								</td>
<%
	if (nssDto == null && !forIdRenteAccordee.equals("")) {
%>								<td>
									<input	type="text" 
											id="forNoRenteAccordee" 
											name="forNoRenteAccordee" 
											value="<%=forIdRenteAccordee%>" />
								</td>
<%
	} else {
		if ( demandeDto != null ) {
%>								<td>
									<input	type="text" 
											id="forNoRenteAccordee" 
											name="forNoRenteAccordee" 
											value="<%=((REDemandeParametresRCDTO) demandeDto).getIdRenteAccordee() %>" />
								</td>
<%
		} else {
%>								<td>
									<input	type="text" 
											id="forNoRenteAccordee" 
											name="forNoRenteAccordee" 
											value="" />
								</td>
<%
		}
	} 
%>								<td colspan="2">
									<input	type="hidden" 
											id="forCodeEnregistrement" 
											name="forCodeEnregistrement" 
											value="01" />
									<input 	type="hidden" 
											id="menuOptionToLoad" 
											name="menuOptionToLoad" 
											value="<%=menuOptionToLoad%>" />
								</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_ANN_R_ETAT" />
								</td>
								<td>
									<ct:FWCodeSelectTag	codeType="REETAANN" 
														name="forCsEtat" 
														wantBlank="true" 
														defaut="<%=forCsEtat%>" />
								</td>
								<td>
									<ct:FWLabel key="JSP_ANN_R_CODE_TRAITEMENT" />
								</td>
								<td>
									<ct:FWCodeSelectTag	codeType="RECTRANN" 
														name="forCsCodeTraitement" 
														wantBlank="true" 
														defaut="<%=forCsCodeTraitement%>" />
								</td>
								<td colspan="2">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_ANN_R_TRIE_PAR" />
								</td>
								<td>
									<select id="orderBy" name="orderBy">
										<option value="<%=orderByMoisRapport%>">
											<ct:FWLabel key="JSP_ANN_R_TRI_MOIS_RAPPORT" />
										</option>
										<option value="<%=orderByNomPrenom%>" selected="selected">
											<ct:FWLabel key="JSP_ANN_R_TRI_NOM_PRENOM" />
										</option>
										<option value="<%=orderByNumAvs%>">
											<ct:FWLabel key="JSP_ANN_R_TRI_NSS" />
										</option>
										<option value="<%=orderByNumAnnonce%>">
											<ct:FWLabel key="JSP_ANN_R_TRI_NO_ANNONCE" />
										</option>
										<option value="<%=orderByEtat%>">
											<ct:FWLabel key="JSP_ANN_R_TRI_ETAT" />
										</option>
									</select>
								</td>
								<td colspan="2">
									&nbsp;
								</td>
								<td>
									<ct:FWLabel key="JSP_ANN_R_RECHERCHE_FAMILLE" />
								</td>
								<td>
									<input	type="checkbox" 
											id="isRechercheFamille" 
											name="isRechercheFamille" 
											onclick="rechercheFamille()" />
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<input 	type="button" 
											onclick="clearFields()" 
											accesskey="<ct:FWLabel key="AK_EFFACER" />" 
											value="<ct:FWLabel key="JSP_EFFACER" />" />
									<label>
										[ALT+<ct:FWLabel key="AK_EFFACER" />]
									</label>
								</td>
							</tr>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<ct:ifhasright element="<%=IREActions.ACTION_ANNONCES%>" crud="u">
					<input	type="button" 
							class="btnCtrl" 
							value="<ct:FWLabel key="JSP_ANN_R_ENVOYER_ANNONCES" />" 
							onclick="envoyerAnnonces()" />
				</ct:ifhasright>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%@ include file="/theme/find/bodyClose.jspf" %>
