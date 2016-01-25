<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="com.google.gson.Gson"%>
<%@page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>
<%@page import="globaz.corvus.vb.historiques.REHistoriqueRentesJoinTiersViewBean"%>
<%@page import="globaz.prestation.tools.PRCodeSystem"%>
<%@page import="globaz.prestation.tools.PRDateFormater"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%><%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="java.util.Map"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>

<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_SAM_D"

	idEcran="PRE0047";

	REHistoriqueRentesJoinTiersViewBean viewBean = (REHistoriqueRentesJoinTiersViewBean) session.getAttribute("viewBean");

	Map<String, String> codesPrestations = PRCodeSystem.getCUCSinMap(viewBean.getSession(), IRERenteAccordee.CS_GROUPE_GENRE_PRESTATION_02_0);
	Map<String, String> codesCasSpeciaux = PRCodeSystem.getCUCSinMap(viewBean.getSession(), IRERenteAccordee.CS_GROUPE_CAS_SPECIAUX_RENTE_01);

	bButtonUpdate = controller.getSession().hasRight(IREActions.ACTION_HISTORIQUE_RENTES, FWSecureConstants.UPDATE);
	
	if (viewBean.getIsModifie() != null 
		&& viewBean.getIsModifie().booleanValue() 
		&& controller.getSession().hasRight(IREActions.ACTION_HISTORIQUE_RENTES, FWSecureConstants.UPDATE)) {
		bButtonDelete = true;
	} else {
		bButtonDelete = false;
	}

	String params = "&provenance1=TIERS&provenance2=CI";

	String jspLocation = servletContext + "/corvusRoot/numeroSecuriteSocialeSF_select.jsp";
	String idTierRequerant = viewBean.getIdTiersRequerant();
	String idTiers = viewBean.getIdTiers();

	//Arrive lors de la création d'un nouvel historique....
	if (globaz.jade.client.util.JadeStringUtil.isBlankOrZero(idTiers)) {
		idTiers = idTierRequerant;
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu" />

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<script type="text/javascript" >

	var o_mapCodesPrestation = <%=new Gson().toJson(codesPrestations)%>;
	var o_mapCodesCasSpeciaux = <%=new Gson().toJson(codesCasSpeciaux)%>;

	var $cs1, // code cas spécial 1
		$cs2, // code cas spécial 2
		$cs3, // code cas spécial 3
		$cs4, // code cas spécial 4
		$cs5, // code cas spécial 5
		$imageCsOk, // image si codes cas spéciaux valides
		$imageCsKo, // image si codes cas spéciaux invalides
		$nss, // champ permettant de mettre à jour le bénéficiaire de la rente
		$codePrestation,
		$imageCodePrestOk,
		$imageCodePrestKo;

	function readOnly(flag) {
		// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
		for(i=0; i < document.forms[0].length; i++) {
			if (!document.forms[0].elements[i].readOnly 
				&& document.forms[0].elements[i].className != 'forceDisable' 
				&& document.forms[0].elements[i].type != 'hidden') {
				document.forms[0].elements[i].disabled = flag;
			}
		}
	}

	function updateTierBeneficiaire() {
		isBenefUpdate = true;
		document.all('updateTiersBeneficiaire').style.display = 'block';
		document.all('noUpdateTiersBeneficiaire').style.display = 'none';
	}

	function add() {	 
		document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_HISTORIQUE_RENTES%>.ajouter";
	}

	function validate() {
		state = true;
		if (document.forms[0].elements('_method').value == "add"){
			document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_HISTORIQUE_RENTES%>.ajouter";
		} else {
			document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_HISTORIQUE_RENTES%>.modifier";
		}
		return state;
	}

	function del() {
		if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
			document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_HISTORIQUE_RENTES%>" + ".supprimer";
			document.forms[0].submit();
		}
	}

	function postInit(){
<%
	if (REHistoriqueRentesJoinTiersViewBean.MODE_AFFICHAGE_UPDATE.equals(viewBean.getModeAffichage())) {
%>		document.all('updateTiersBeneficiaire').style.display = 'block';
<%
	} else {
%>		if (document.forms[0].elements('_method').value === "add") {
			document.all('updateTiersBeneficiaire').style.display = 'block';
		} else {
			document.all('updateTiersBeneficiaire').style.display = 'none';
		}
<%
	}
%>	}

	function upd() {
<%
	if (JadeStringUtil.isBlankOrZero(viewBean.getIdRenteAccordee())) {
%>		document.all('updateTiersBeneficiaire').style.display = 'block';
<%
	}
%>		document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_HISTORIQUE_RENTES%>.modifier";
	}

	function cancel() {
		document.forms[0].elements('userAction').value = "<%=globaz.corvus.servlet.IREActions.ACTION_HISTORIQUE_RENTES%>" + ".chercher";
	}

	function init() {
	}

	function nssChange(tag) {
		if (tag.select) {
			var element = tag.select.options[tag.select.selectedIndex];

			if (element.id != null) {
				document.getElementById("idTiers").value = element.idAssure;
			}
		}
		document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_HISTORIQUE_RENTES%>.afficher";
		document.forms[0].elements('changeBeneficiaire').value="true";
		document.forms[0].submit();
	}

	function miseAJourCodePrestation() {
		if ($codePrestation.val() !== "") {
			if (typeof(o_mapCodesPrestation[$codePrestation.val()]) !== 'undefined') {
				$imageCodePrestKo.hide();
				$imageCodePrestOk.show();
			} else {
				$imageCodePrestKo.show();
				$imageCodePrestOk.hide();
			}
		}
	}

	function miseAJourCodeCasSpeciaux(){
		var areCodesCasSpeciauxValid = true;

		// code cas spécial 1
		if ($cs1.val() !== "") {
			if (typeof(o_mapCodesCasSpeciaux[$cs1.val()]) !== 'undefined') {
				areCodesCasSpeciauxValid &= true;
			} else {
				areCodesCasSpeciauxValid = false;
			}
		}

		// code cas spécial 2
		if ($cs2.val() !== "") {
			if (typeof(this.o_mapCodesCasSpeciaux[$cs2.val()]) !== 'undefined') {
				areCodesCasSpeciauxValid &= true;
			} else {
				areCodesCasSpeciauxValid = false;
			}
		}

		// code cas spécial 3
		if ($cs3.val() !== "") {
			if (typeof(this.o_mapCodesCasSpeciaux[$cs3.val()]) !== 'undefined') {
				areCodesCasSpeciauxValid &= true;
			} else {
				areCodesCasSpeciauxValid = false;
			}
		}

		// code cas spécial 4
		if ($cs4.val() !== "") {
			if (typeof(this.o_mapCodesCasSpeciaux[$cs4.val()]) !== 'undefined') {
				areCodesCasSpeciauxValid &= true;
			} else {
				areCodesCasSpeciauxValid = false;
			}
		}

		// code cas spécial 5
		if ($cs5.val() !== "") {
			if (typeof(this.o_mapCodesCasSpeciaux[$cs5.val()]) !== 'undefined') {
				areCodesCasSpeciauxValid &= true;
			} else {
				areCodesCasSpeciauxValid = false;
			}
		}

		if (areCodesCasSpeciauxValid) {
			$imageCsKo.hide();
			$imageCsOk.show();
		} else {
			$imageCsOk.hide();
			$imageCsKo.show();
		}
	}

	function formatAAMMField (field) {
		var value = field.value;

		if (value !== "" && value.length < 5) {
			if (value.indexOf(".") === -1) {
				while (value.length < 4) {
					value = "0" + value;
				}
			} else {
				while (value.length < 5) {
					value = "0" + value;
				}
			}

			if (value.length === 4 && value.indexOf(".") === -1) {
				field.value = value.substring(0, 2) + "." + value.substring(2, 4);
			}
		}
	}

	function formatAdField (field) {
		var value = field.value;

		if (value !== "" && value.length < 3) {
			if (value.indexOf(".") === -1) {
				while (value.length < 2) {
					value = "0" + value;
				}
			} else {
				while (value.length < 3) {
					value = "0" + value;
				}
			}

			if (value.length === 2 && value.indexOf(".") === -1) {
				field.value = value.substring(0, 1) + "." + value.substring(1, 2);
			}
		}
	}

	$(document).ready(function(){
		$cs1 = $('#cs1');
		$cs2 = $('#cs2');
		$cs3 = $('#cs3');
		$cs4 = $('#cs4');
		$cs5 = $('#cs5');

		$imageCsOk = $('#imageOKCodeCasSpeciaux');
		$imageCsKo = $('#imageKOCodeCasSpeciaux');

		$nss = $('#partialnssTiersBeneficiairea');
		$codePrestation = $('#codePrestation');

		$imageCodePrestOk = $('#imageOKCodePrestation');
		$imageCodePrestKo = $('#imageKOCodePrestation');

		var o_mapCodesPrestationSansFraction = {};
		// on enlève la fraction de rente aux codes prestations
		$.each(o_mapCodesPrestation, function (key, value) {
			o_mapCodesPrestationSansFraction[key.substring(0, key.indexOf('.'))] = 'ok';
		});
		o_mapCodesPrestation = o_mapCodesPrestationSansFraction;

		$codePrestation.keyup(function () {
			miseAJourCodePrestation();
		});

		$('.codeCasSpeciaux').keyup(function () {
			miseAJourCodeCasSpeciaux();
		});
	});

	$('html').bind(eventConstant.JADE_FW_ACTION_DONE, function() {
		miseAJourCodeCasSpeciaux();
		miseAJourCodePrestation();

		if($nss.val() === ''){
			$nss.focus();
		} else {
			$codePrestation.focus();
		}
	});
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
				<ct:FWLabel key="JSP_HIS_D_TITRE" />
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
					<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td colpsan="6">
								<table>
									<tr>
										<td colspan="6">
											<table>
												<input type="hidden" name="idTiersIn" value="<%=viewBean.getIdTiersIn()%>">
												<input type="hidden" name="reloadHistory" value="false">
												<input type="hidden" name="isPrendreEnCompteCalculAcor" value="<%=viewBean.getIsPrendreEnCompteCalculAcor()%>">
												<input type="hidden" name="idTierRequerant" value="<%=idTierRequerant%>">
												<tbody id="updateTiersBeneficiaire" style="display:none;">
													<tr>
														<td colspan="6">
															NSS&nbsp;&nbsp;
															<ct1:nssPopup	name="nssTiersBeneficiairea" 
																			onFailure="" 
																			onChange="nssChange(tag);" 
																			params="<%=params%>" 
																			value="" 
																			newnss="" 
																			jspName="<%=jspLocation%>" 
																			avsMinNbrDigit="3" 
																			nssMinNbrDigit="3" 
																			avsAutoNbrDigit="11" 
																			nssAutoNbrDigit="10" />
															&nbsp;<ct:FWLabel key="JSP_RAC_R_INSERER_NOUVEAU_NSS"/>
															<br/>
															<br/>
															<input type="hidden" name="idTiers" value="<%=idTiers%>" />
															<input type="hidden" name="changeBeneficiaire" value="" />
														</td>
													</tr>
													<tr>
														<br/>
														<br/>
													</tr>
												</tbody>
												<tbody id="noUpdateTiersBeneficiaire" style="display:block;">
													<tr>
														<td colspan="4">
															<re:PRDisplayRequerantInfoTag	session="<%=(globaz.globall.db.BSession)controller.getSession()%>" 
																							idTiers="<%=idTiers%>" 
																							style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_BEN%>" />
														</td>
														<td colspan="2">
															&nbsp;
														</td>
													</tr>
												</tbody>
											</table>
										</td>
									</tr>
									<tr>
										<td colspan="6">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td width="20%" colspan="1" align="left" class="ongletGlobazBlue">
											<ct:FWLabel key="JSP_SAM_D_DONNEES_OBLIGATOIRES"/>
										</td>
										<td width="80%">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td colspan="6" class="areaGlobazBlue">
											<table>
												<tr>
													<td>
														<ct:FWLabel key="JSP_SAM_D_GENRE_PRESTATION" />
													</td>
													<td>
														<input	type="text" 
																id="codePrestation" 
																name="codePrestation" 
																size="8" 
																value="<%=viewBean.getCodePrestation()%>" />
														<img	id="imageOKCodePrestation" 
																name="imageOKCodePrestation" 
																src="<%=request.getContextPath()%>/images/ok.gif" 
																alt="" />
														<img	id="imageKOCodePrestation" 
																name="imageKOCodePrestation" 
																src="<%=request.getContextPath()%>/images/erreur.gif" 
																alt="" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_DEBUT_DROIT" />
													</td>
													<td>
														<input	id="dateDebutDroit" 
																name="dateDebutDroit" 
																data-g-calendar="type:month" 
																value="<%=viewBean.getDateDebutDroit()%>" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_MONTANT" />
													</td>
													<td>
														<input	type="text" 
																id="montantPrestation" 
																name="montantPrestation" 
																value="<%=viewBean.getMontantPrestation()%>" />
													</td>
												</tr>
												<tr>
													<td>
														<ct:FWLabel key="JSP_SAM_D_RAM" />
													</td>
													<td>
														<input	type="text" 
																id="ram" 
																name="ram" 
																value="<%=viewBean.getRam()%>" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_ECHELLE" />
													</td>
													<td>
														<input	type="text" 
																id="echelle" 
																name="echelle" 
																size="2" 
																maxlength="2" 
																value="<%=viewBean.getEchelle()%>" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_ANNEE_MONTANT_RAM" />
													</td>
													<td>
														<input	type="text" 
																id="anneeMontantRAM" 
																name="anneeMontantRAM" 
																value="<%=viewBean.getAnneeMontantRAM()%>" />
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td colspan="6">
											<br/>
										</td>
									</tr>
									<tr>
										<td colspan="1" align="left" class="ongletGlobazBlue">
											<ct:FWLabel key="JSP_SAM_D_DUREE_COTISATION" />
										</td>
									</tr>
									<tr>
										<td colspan="6" class="areaGlobazBlue">
											<table>
												<tr>
													<td>
														<ct:FWLabel key="JSP_SAM_D_D_COT_AV_73" />
													</td>
													<td>
														<input	type="text" 
																id="dureeCotAv73" 
																name="dureeCotAv73" 
																value="<%=viewBean.getDureeCotAv73() == null ? "" : viewBean.getDureeCotAv73()%>" 
																onchange="formatAAMMField(this);" 
																size="5" 
																maxlength="5" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_D_COT_AP_73" />
													</td>
													<td>
														<input	type="text" 
																id="dureeCotAp73" 
																name="dureeCotAp73" 
																value="<%=viewBean.getDureeCotAp73() == null ? "" : viewBean.getDureeCotAp73()%>" 
																onchange="formatAAMMField(this);" 
																size="5" 
																maxlength="5" />
													</td>
													<td colspan="2">
														&nbsp;
													</td>
												</tr>
												<tr>
													<td>
														<ct:FWLabel key="JSP_SAM_D_MOIS_APP_AV_73" />
													</td>
													<td>
														<input	type="text" 
																id="moisAppointAv73" 
																name="moisAppointAv73" 
																value="<%=viewBean.getMoisAppointAv73() == null ? "" : viewBean.getMoisAppointAv73()%>" 
																size="2" 
																maxlength="2" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_MOIS_APP_AP_73" />
													</td>
													<td>
														<input	type="text" 
																id="moisAppointAp73" 
																name="moisAppointAp73" 
																value="<%=viewBean.getMoisAppointAp73() == null ? "" : viewBean.getMoisAppointAp73()%>" 
																size="2" 
																maxlength="2" />
													</td>
													<td colspan="2">
														&nbsp;
													</td>
												</tr>
												<tr>
													<td>
														<ct:FWLabel key="JSP_SAM_D_D_COT_CLASSE" />
													</td>
													<td>
														<input	type="text" 
																id="dureeCotiClasseAge" 
																name="dureeCotiClasseAge" 
																value="<%=viewBean.getDureeCotiClasseAge()%>" 
																size="2" 
																maxlength="2" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_ANNEE_NIVEAU" />
													</td>
													<td>
														<input	type="text" 
																id="anneeNiveau" 
																name="anneeNiveau" 
																value="<%=viewBean.getAnneeNiveau()%>" 
																size="2" 
																maxlength="2" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_D_COT_RAM" />
													</td>
													<td>
														<input	type="text" 
																id="dureeCotRam" 
																name="dureeCotRam" 
																value="<%=viewBean.getDureeCotRam()%>" 
																onchange="formatAAMMField(this);" 
																size="5" 
																maxlength="5" />
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td colspan="6">
											<br/>
										</td>
									</tr>
									<tr>
										<td colspan="1" align="left" class="ongletGlobazBlue">
											<ct:FWLabel key="JSP_SAM_D_ELEMENTS_INVALIDITE" />
										</td>
									</tr>
									<tr>
										<td colspan="6" class="areaGlobazBlue">
											<table>
												<tr>
													<td>
														<ct:FWLabel key="JSP_SAM_D_DEGRE" />
													</td>
													<td>
														<input	type="text" 
																id="degreInvalidite" 
																name="degreInvalidite" 
																value="<%=viewBean.getDegreInvalidite()%>" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_CLE_INFIRMITE" />
													</td>
													<td>
														<input	type="text" 
																id="cleInfirmiteAtteinteFct" 
																name="cleInfirmiteAtteinteFct" 
																value="<%=viewBean.getCleInfirmiteAtteinteFct()%>" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_SURV_EV_ASS" />
													</td>
													<td>
														<input	id="survenanceEvenementAssure" 
																name="survenanceEvenementAssure" 
																data-g-calendar="type:month" 
																value="<%=viewBean.getSurvenanceEvenementAssure()%>" />
													</td>
												</tr>
												<tr>
													<td>
														<ct:FWLabel key="JSP_SAM_D_INVALIDITE_PRECOCE" />
													</td>
													<td>
														<input	type="checkbox" 
																id="isInvaliditePrecoce" 
																name="isInvaliditePrecoce" 
																<%=(viewBean.getIsInvaliditePrecoce() != null && viewBean.getIsInvaliditePrecoce().booleanValue()) ? "checked" : ""%> />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_OFFICE_AI" />
													</td>
													<td>
														<input	type="text" 
																id="officeAI" 
																name="officeAI" 
																value="<%=viewBean.getOfficeAI()%>" />
													</td>
													<td colspan="2">
														&nbsp;
													</td>
												</tr>
												<tr>
													<td>
														<ct:FWLabel key="JSP_SAM_D_D_COT_ETR_AV_73" />
													</td>
													<td>
														<input	type="text" 
																id="dureeCotiEtrangereAv73" 
																name="dureeCotiEtrangereAv73" 
																value="<%=viewBean.getDureeCotiEtrangereAv73()%>" 
																size="5" 
																maxlength="5" 
																onchange="formatAAMMField(this);" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_D_COT_ETR_AP_73" />
													</td>
													<td>
														<input	type="text" 
																id="dureeCotiEtrangereAp73" 
																name="dureeCotiEtrangereAp73" 
																value="<%=viewBean.getDureeCotiEtrangereAp73()%>" 
																size="5" 
																maxlength="5" 
																onchange="formatAAMMField(this);" />
													</td>
													<td colspan="2">
														&nbsp;
													</td>
												</tr>
												<tr>
													<td>
														<ct:FWLabel key="JSP_SAM_D_SUPPL_AI_RAM" />
													</td>
													<td>
														<input	type="text" 
																id="supplementCarriere" 
																name="supplementCarriere" 
																value="<%=viewBean.getSupplementCarriere()%>" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_FRACTION_RENTE" />
													</td>
													<td>
														<input	type="text" 
																id="fractionRente" 
																name="fractionRente" 
																value="<%=viewBean.getFractionRente()%>" 
																size="1" 
																maxlength="1" />
													</td>
													<td colspan="2">
														&nbsp;
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td colspan="6">
											<br/>
										</td>
									</tr>
									<tr>
										<td colspan="1" align="left" class="ongletGlobazBlue">
											<ct:FWLabel key="JSP_SAM_D_BONIFICATION" />
										</td>
									</tr>
									<tr>
										<td colspan="6" class="areaGlobazBlue">
											<table>
												<tr>
													<td>
														<ct:FWLabel key="JSP_SAM_D_BONUS_EDUCATIF" />
													</td>
													<td>
														<input	type="text" 
																id="montantBTE" 
																name="montantBTE" 
																value="<%=viewBean.getMontantBTE()%>" />
													</td>
													<td colspan="2">
														&nbsp;
													</td>
													<td colspan="2">
														&nbsp;
													</td>
												</tr>
												<tr>
													<td>
														<ct:FWLabel key="JSP_SAM_D_ANNNES_EDUC" />
													</td>
													<td>
														<input	type="text" 
																id="nbrAnneeBTE" 
																name="nbrAnneeBTE" 
																value="<%=viewBean.getNbrAnneeBTE()%>" 
																size="5" 
																maxlength="5" 
																onchange="formatAAMMField(this);" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_ANNNES_ASSIST" />
													</td>
													<td>
														<input	type="text" 
																id="nbrAnneeBTA" 
																name="nbrAnneeBTA" 
																value="<%=viewBean.getNbrAnneeBTA()%>" 
																size="5" 
																maxlength="5" 
																onchange="formatAAMMField(this);" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_ANNNES_TRANSIT" />
													</td>
													<td>
														<input	type="text" 
																id="nbrAnneeBTR" 
																name="nbrAnneeBTR" 
																value="<%=viewBean.getNbrAnneeBTR()%>" 
																size="3" 
																maxlength="3" 
																onchange="formatAdField(this);" />
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td colspan="6">
											<br/>
										</td>
									</tr>
									<tr>
										<td colspan="1" align="left" class="ongletGlobazBlue">
											<ct:FWLabel key="JSP_SAM_D_RETRAITE_FLEXIBLE" />
										</td>
									</tr>
									<tr>
										<td colspan="6" class="areaGlobazBlue">
											<table>
												<tr>
													<td>
														<ct:FWLabel key="JSP_SAM_D_DUREE_AJOURNEMENT" />
													</td>
													<td>
														<input	type="text" 
																id="dureeAjournement" 
																name="dureeAjournement" 
																size="4" 
																maxlength="4" 
																value="<%=viewBean.getDureeAjournement()%>" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_SUPPL_AJOUR" />
													</td>
													<td>
														<input	type="text" 
																id="supplementAjournement" 
																name="supplementAjournement" 
																value="<%=viewBean.getSupplementAjournement()%>" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_DATE_REVOC" />
													</td>
													<td>
														<input	type="text" 
																id="dateRevocationAjournement" 
																name="dateRevocationAjournement" 
																data-g-calendar="type:month" 
																value="<%=viewBean.getDateRevocationAjournement()%>" />
													</td>
												</tr>
												<tr>
													<td>
														<ct:FWLabel key="JSP_SAM_D_AJOURNE" />
													</td>
													<td>
														<input	type="checkbox" 
																name="isRenteAjournee" 
																<%=(viewBean.getIsRenteAjournee() != null && viewBean.getIsRenteAjournee().booleanValue()) ? "checked" : ""%> />
													</td>
													<td colspan="2">
														&nbsp;
													</td>
												</tr>
												<tr>
													<td>
														<ct:FWLabel key="JSP_SAM_D_ANNEES_ANTICIPATION" />
													</td>
													<td>
														<input	type="text" 
																id="nbrAnneeAnticipation" 
																name="nbrAnneeAnticipation" 
																value="<%=viewBean.getNbrAnneeAnticipation()%>" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_RED_ANTICIPATION" />
													</td>
													<td>
														<input	type="text" 
																id="montantReductionAnticipation" 
																name="montantReducAnticipation" 
																value="<%=viewBean.getMontantReducAnticipation()%>" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_DEB_ANTICIPATION" />
													</td>
													<td>
														<input	id="dateDebutAnticipation"
																name="dateDebutAnticipation"
																data-g-calendar="type:month"
																value="<%=viewBean.getDateDebutAnticipation()%>" />
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td colspan="6">
											<br/>
										</td>
									</tr>
									<tr>
										<td colspan="1" align="left" class="ongletGlobazBlue">
											<ct:FWLabel key="JSP_SAM_D_DIVERS"/>
										</td>
									</tr>
									<tr>
										<td colspan="6" class="areaGlobazBlue">
											<table>
												<tr>
													<td>
														<ct:FWLabel key="JSP_SAM_D_FIN_DROIT" />
													</td>
													<td>
														<input	id="dateFinDroit"
																name="dateFinDroit"
																data-g-calendar="type:month"
																value="<%=viewBean.getDateFinDroit()%>" />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_CODES_CAS_SPECIAL" />
													</td>
													<td colspan="3">
														<input	type="text" 
																id="cs1" 
																name="cs1" 
																class="codeCasSpeciaux" 
																size="2" 
																maxlength="2" 
																value="<%=viewBean.getCs1() == null ? "" : viewBean.getCs1()%>" />
														<input	type="text" 
																id="cs2" 
																name="cs2" 
																class="codeCasSpeciaux" 
																size="2" 
																maxlength="2" 
																value="<%=viewBean.getCs2() == null ? "" : viewBean.getCs2()%>" />
														<input	type="text" 
																id="cs3" 
																name="cs3" 
																class="codeCasSpeciaux" 
																size="2" 
																maxlength="2" 
																value="<%=viewBean.getCs3() == null ? "" : viewBean.getCs3()%>" />
														<input	type="text" 
																id="cs4" 
																name="cs4" 
																class="codeCasSpeciaux" 
																size="2" 
																maxlength="2" 
																value="<%=viewBean.getCs4() == null ? "" : viewBean.getCs4()%>" />
														<input	type="text" 
																id="cs5" 
																name="cs5" 
																class="codeCasSpeciaux" 
																size="2" 
																maxlength="2" 
																value="<%=viewBean.getCs5() == null ? "" : viewBean.getCs5()%>" />
														<img	id="imageOKCodeCasSpeciaux" 
																name="imageOKCodeCasSpeciaux" 
																src="<%=request.getContextPath()%>/images/ok.gif" 
																alt="" />
														<img	id="imageKOCodeCasSpeciaux" 
																name="imageKOCodeCasSpeciaux" 
																src="<%=request.getContextPath()%>/images/erreur.gif" 
																alt="" />
													</td>
												</tr>
												<tr>
													<td>
														<ct:FWLabel key="JSP_SAM_D_CODES_REVENUS_SPLITTES" />
													</td>
													<td>
														<input	type="checkbox" 
																id="isRevenuSplitte" 
																name="isRevenuSplitte" 
																value="on" 
																<%=(viewBean.getIsRevenuSplitte() != null && viewBean.getIsRevenuSplitte().booleanValue()) ? "CHECKED" : ""%> />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_CODES_SURVIVANT_INVALIDE" />
													</td>
													<td>
														<input	type="checkbox" 
																id="isSurvivantInvalid" 
																name="isSurvivantInvalid" 
																value="on" 
																<%=(viewBean.getIsSurvivantInvalid() != null && viewBean.getIsSurvivantInvalid().booleanValue()) ? "CHECKED" : ""%> />
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_CODE_REVENU" />
													</td>
													<td>
														<input	type="text" 
																id="codeRevenu" 
																name="codeRevenu" 
																value="<%=viewBean.getCodeRevenu()%>" />
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td colspan="6">
											<br/>
										</td>
									</tr>
									<tr>
										<td colspan="6" class="areaGlobazBlue">
											<table>
												<tr>
													<td>
														<ct:FWLabel key="JSP_SAM_D_DROIT" />
													</td>
													<td>
														<input	type="text" 
																id="droitApplique" 
																name="droitApplique" 
																size="2" 
																maxlength="2" 
																value="<%=viewBean.getDroitApplique()%>" />
													</td>
													<td>
														&nbsp;
													</td>
													<td>
														<ct:FWLabel key="JSP_SAM_D_TRANSFERE" />
													</td>
													<td>
														<input	type="checkbox" 
																id="isTransfere" 
																name="isTransfere" 
																<%=(viewBean.getIsTransfere() != null && viewBean.getIsTransfere().booleanValue()) ? "CHECKED" : ""%> />
													</td>
													<td>
														&nbsp;
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>