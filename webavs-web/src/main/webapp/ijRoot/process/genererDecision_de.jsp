<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@ page import="globaz.ij.api.prononces.IIJPrononce"%>
<%@ page import="globaz.ij.db.decisions.IJDecisionIJAI"%>
<%@ page import="globaz.ij.servlet.IIJActions"%>
<%@ page import="globaz.ij.vb.process.IJGenererDecisionViewBean"%>

<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.ij.itext.IJDecision" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "PIJ3009";
	userActionValue = IIJActions.ACTION_GENERER_DECISION + ".executer";
	IJGenererDecisionViewBean viewBean = (IJGenererDecisionViewBean) session.getAttribute("viewBean");

	String eMailAddress = viewBean.getEMailAddress();
	if (JadeStringUtil.isBlankOrZero(viewBean.getEMailAddress())) {
		eMailAddress = objSession.getUserEMail();
	}

	bButtonUpdate = false;
	bButtonValidate = false;
	bButtonDelete = false;
	bButtonCancel = true;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<!-- tinyMCE -->
<script type="text/javascript" src="<%=servletContext%>/scripts/tiny_mce/tiny_mce.js"></script>
<script type="text/javascript">
	tinyMCE.init({
		theme : "advanced",
		mode : "exact",
		elements : "remarque",
		content_css : "<%=servletContext%>/theme/tinyMceStyle.css",
		theme_advanced_styles : "Rouge=red;Vert=green;Bleu=blue",
		theme_advanced_buttons1_add : "fontselect",
		theme_advanced_toolbar_location : "top",
		handle_event_callback : "interceptForbiddenCharacters",
		debug : false,		
		theme_advanced_disable : "strikethrough,justifyleft,justifycenter,justifyright,justifyfull,bullist,numlist,outdent,indent,cut,copy,paste,undo,redo,link,unlink,image,cleanup,help,code,hr,formatselect,fontselect,fontsizeselect,sub,sup,forecolor,backcolor,charmap,visualaid,anchor,newdocument,separator"
	});
</script>
<!-- /tinyMCE -->

<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<script type="text/javascript"> 

	var $champsAdressesPaiements,
		$selectBeneficiaire,
		$selectPersonnalisation;

	function add () {
	}

	function upd () {
	}

	function validate () {
		//ici pas de validation, mais  redirection vers -> Choix des annexes et copies
		document.forms[0].elements('userAction').value="<%=IIJActions.ACTION_GENERER_DECISION%>.allerVersChoisirParagraphes";
		return true;
	}

	function cancel () {
		document.forms[0].elements('userAction').value = "<%=IIJActions.ACTION_PRONONCE_JOINT_DEMANDE%>.chercher";
		document.forms[0].submit();
	}

	function del () {
	}

	function init () {
	}

	function majAdressePaiement () {
		var s_beneficiaire = $selectBeneficiaire.val();
		var s_personnalisation = $selectPersonnalisation.val();

		if (s_beneficiaire && s_personnalisation) {
			$champsAdressesPaiements.hide();
			$('.' + s_beneficiaire + '.' + s_personnalisation).show();
		}
	}

	function interceptForbiddenCharacters (event) {
		if (event.keyCode === 60 || event.keyCode === 62) {
			event.returnValue = false;
			return false;
		}
	}

	$(document).ready(function () {
		$champsAdressesPaiements = $('.adressePaiement');
		$selectBeneficiaire = $('#beneficiaire');
		$selectPersonnalisation = $('#personnalisationAdressePaiement');

		$selectBeneficiaire.change(function () {
			majAdressePaiement();
		});
		$selectPersonnalisation.change(function () {
			majAdressePaiement();
		});

		majAdressePaiement();
	});
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_GENERER_DECISION"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
					<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>
								<input	name="prenomNomTiersPrincipal" 
										type="hidden" 
										value="<%=viewBean.getNomPrenomTiersPrincipal()%>" />
								<input	name="noAvsTiersPrincipal" 
										type="hidden" 
										value="<%=viewBean.getNoAVSTiersPrincipal()%>" />
								<b>
									<ct:FWLabel key="JSP_ASSURE" />
								</b>
							</td>
							<td colspan="4">
								<input	type="text" 
										value="<%=viewBean.getDetailRequerantDetail()%>" 
										size="100" 
										class="disabled" 
										readonly />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_NO_PRONONCE" />
							</td>
							<td colspan="4">
								<input	type="text" 
										name="idPrononce" 
										class="disabled" 
										readonly 
										value="<%=viewBean.getIdPrononce()%>" />
								<input	type="hidden" 
										name="idDemande" 
										value="<%=viewBean.getIdDemande()%>" />
							</td>
						</tr>
<%
	if (!JadeStringUtil.isBlankOrZero(viewBean.getIdDecision())) { 
%>						<tr>
							<td>
								<ct:FWLabel key="JSP_NO_DECISION" />
							</td>
							<td colspan="4">
								<input	type="text" 
										name="dummy" 
										class="disabled" 
										readonly 
										value="<%=viewBean.getIdDecision()%>" />
							</td>
						</tr>
<%
	}
%>						<tr>
							<td width="25%" height="30">
								&nbsp;
							</td>
							<td width="15%" height="30">
								&nbsp;
							</td>
							<td width="20%" height="30">
								&nbsp;
							</td>
							<td width="20%" height="30">
								&nbsp;
							</td>
							<td width="20%" height="30">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_ADRESSE_EMAIL" />
							</td>
							<td colspan="2">
								<input	type="text" 
										name="eMailAddress" 
										size=35 
										value="<%=eMailAddress!=null?eMailAddress:""%>" />
							</td>
<%
	if ("1".equals(viewBean.getDisplaySendToGed())) { 
%>							<td>
								<ct:FWLabel key="JSP_ENVOYER_DANS_GED" />
							</td>
							<td>
								<input	type="checkbox" 
										name="isSendToGed" 
										value="TRUE" 
										CHECKED />
								<input	type="hidden" 
										name="displaySendToGed" 
										value="1" />
							</td>
<%
	} else {
%>							<td colspan="2">
								<input	type="hidden" 
										name="isSendToGed" 
										value="FALSE" />
								<input	type="hidden" 
										name="displaySendToGed" 
										value="0" />
							</td>
<%
	}
%>						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_DATE_SUR_DOCUMENT" />
							</td>
							<td colspan="4">
								<input	name="dateSurDocument" 
										data-g-calendar=" " 
										value="<%=viewBean.getDateSurDocument()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="5" height="30">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_GARANTIE_REVISION" />
							</td>
							<td colspan="4">
								<select name="garantitRevision"  value="<%=viewBean.getGarantitRevision()%>">
<%
	if (viewBean.getRevisionAGarantir() != null) {
		if (viewBean.getRevisionAGarantir().contains("4") 
			&& viewBean.getRevisionAGarantir().contains("5")) {
%>									<option value="4" <%="4".equals(viewBean.getGarantitRevision())?"selected=\"selected\"":""%>>
										<ct:FWLabel key="JSP_REV_4" />
									</option>
									<option value="5" <%="5".equals(viewBean.getGarantitRevision())?"selected=\"selected\"":""%>>
										<ct:FWLabel key="JSP_REV_LAST" />
									</option>
<%
		} else if (viewBean.getRevisionAGarantir().contains("4") 
					&& !viewBean.getRevisionAGarantir().contains("5")) { 
%>									<option value="4" <%="4".equals(viewBean.getGarantitRevision())?"selected=\"selected\"":""%>>
										<ct:FWLabel key="JSP_REV_4" />
									</option>
									<option value="4">
										<ct:FWLabel key="JSP_REV_LAST" />
									</option>
<%
		} else if (!viewBean.getRevisionAGarantir().contains("4") 
					&& viewBean.getRevisionAGarantir().contains("5")) { 
%>									<option value="5" <%="5".equals(viewBean.getGarantitRevision())?"selected=\"selected\"":""%>>
										<ct:FWLabel key="JSP_REV_5" />
									</option>
									<option value="5">
										<ct:FWLabel key="JSP_REV_LAST" />
									</option>
<%
		}
	}
%>								</select>
							</td>
						</tr>
						<tr>
							<td colspan="5">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="5">
								<hr/>
							</td>
						</tr>
						<tr>
							<td colspan="5">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td valign="top">
								<ct:FWLabel key="JSP_ADR_COURRIER" />
							</td>
							
							
							<td valign="top" align="right" colspan="2">
							<LABEL for="nom"><ct:FWLabel key="JSP_TIERS"/></LABEL>&nbsp;
								<ct:FWSelectorTag	name="selecteurBeneficiaire" 
													methods="<%=viewBean.getMethodesSelectionAdresseCourrier()%>" 
													providerApplication="pyxis" 
													providerPrefix="TI" 
													providerAction="pyxis.tiers.tiers.chercher" 
													target="fr_main" 
													redirectUrl="<%=mainServletPath%>" />
								&nbsp;
							</td>
							
						</tr>
						
						
						<tr>
							<td valign="top">
							</td>
							<td valign="top" align="right" colspan="2">
							<LABEL for="nom"><ct:FWLabel key="JSP_ADMINISTRATION"/></LABEL>&nbsp;
								<ct:FWSelectorTag	name="selecteurAdministration" 
													methods="<%=viewBean.getMethodesSelectionAdresseCourrier()%>" 
													providerApplication="pyxis" 
													providerPrefix="TI" 
													providerAction="pyxis.tiers.administration.chercher" 
													target="fr_main" 
													redirectUrl="<%=mainServletPath%>" />
								&nbsp;
							</td>
							
							<td align="left" colspan="2">
								<span class="IJAfficheText">
									<%=viewBean.getAdresseCourrierFormatee()%>
								</span>
								<input	type="hidden" 
										name="idTierAdresseCourrier" 
										value="<%=viewBean.getIdTierAdresseCourrier()%>" />
							</td>
						
						<tr>
							<td colspan="5">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="5">
								<hr />
							</td>
						</tr>
						<tr>
							<td colspan="5">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td valign="top">
								<ct:FWLabel key="JSP_PAYER_A" />
								&nbsp;
								<select	id="beneficiaire" 
										name="beneficiaire" 
										value="<%=viewBean.getBeneficiaire()%>">
									<option	value="<%=IJDecision.BENEFICIAIRE_ASSURE%>" <%=IJDecision.BENEFICIAIRE_ASSURE.equals(viewBean.getBeneficiaire()) ? "selected=\"selected\"" : ""%>>
										<ct:FWLabel key="JSP_ASSURE" />
									</option>
									<option value="<%=IJDecision.BENEFICIAIRE_EMPLOYEUR%>" <%=IJDecision.BENEFICIAIRE_EMPLOYEUR.equals(viewBean.getBeneficiaire()) ? "selected=\"selected\"" : ""%>>
										<ct:FWLabel key="JSP_EMPLOYEUR" />
									</option>
								</select>
							</td>
							<td valign="top" align="left">
								<select	id="personnalisationAdressePaiement" 
										name="personnalisationAdressePaiement" 
										value="<%=viewBean.getPersonnalisationAdressePaiement()%>">
									<option value="standard" <%="standard".equals(viewBean.getPersonnalisationAdressePaiement()) ? "selected=\"selected\"" : ""%>>
										<ct:FWLabel key="JSP_STANDARD" />
									</option>
									<option value="personnalise" <%="personnalise".equals(viewBean.getPersonnalisationAdressePaiement()) ? "selected=\"selected\"" : ""%>>
										<ct:FWLabel key="JSP_PERSONNALISE" />
									</option>
									<option value="aucun" <%="aucun".equals(viewBean.getPersonnalisationAdressePaiement()) ? "selected=\"selected\"" : ""%>>
										<ct:FWLabel key="JSP_AUCUN" />
									</option>
								</select>
							</td>
							<td valign="top" align="right">
								<span class="adressePaiement assure employeur personnalise">
									<ct:FWSelectorTag	name="selecteurAdressePaiement" 
														methods="<%=viewBean.getMethodesSelectionAdressePaiement()%>" 
														providerApplication="pyxis" 
														providerPrefix="TI" 
														providerAction="pyxis.adressepaiement.adressePaiement.chercher" 
														target="fr_main" 
														redirectUrl="<%=mainServletPath%>" />
									&nbsp;
								</span>
							</td>
							<td valign="top" align="left">
								<span class="IJAfficheText">
									<span class="adressePaiement assure standard">
										<%=viewBean.getAdresseCourrierAssureFormatee()%>
									</span>
									<span class="adressePaiement employeur standard">
										<%=viewBean.getAdresseCourrierEmployeurFormatee()%>
									</span>
									<span class="adressePaiement assure employeur personnalise">
										<%=viewBean.getAdresseCourrierPersonnaliseeFormatee()%>
									</span>
									<span class="adressePaiement assure employeur aucun">
										<ct:FWLabel key="JSP_AUCUNE_INFO_PAIEMENT" />
									</span>
								</span>
							</td>
							<td valign="top">
								<span class="IJAfficheText">
									<span class="adressePaiement assure standard">
										<%=viewBean.getAdressePaiementAssureFormatee()%>
									</span>
									<span class="adressePaiement employeur standard">
										<%=viewBean.getAdressePaiementEmployeurFormatee()%>
									</span>
									<span class="adressePaiement assure employeur personnalise">
										<%=viewBean.getAdressePaiementPersonnaliseeFormatee()%>
									</span>
								</span>
								<input	type="hidden" 
										name="idTierAssureAdressePaiement" 
										value="<%=viewBean.getIdTierAssureAdressePaiement()%>" />
								<input	type="hidden" 
										name="idTierEmployeurAdressePaiement" 
										value="<%=viewBean.getIdTierEmployeurAdressePaiement()%>" />
								<input	type="hidden" 
										name="idTiersAdressePaiementPersonnalisee" 
										value="<%=viewBean.getIdTiersAdressePaiementPersonnalisee()%>" />
								<input	type="hidden" 
										name="idDomaineApplicationAdressePaiementPersonnalisee" 
										value="<%=viewBean.getIdDomaineApplicationAdressePaiementPersonnalisee()%>" />
								<input	type="hidden" 
										name="numAffilieAdressePaiementPersonnalisee" 
										value="<%=viewBean.getNumAffilieAdressePaiementPersonnalisee()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="5">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="5">
								<hr/>
							</td>
						</tr>
						<tr>
							<td colspan="5">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_PERS_REF" />
							</td>
							<td colspan="4">
								<ct:FWListSelectTag data="<%=viewBean.getPersonnesReference()%>" 
													defaut="<%=viewBean.getIdPersonneReference()%>" 
													name="idPersonneReference" />
							</td>
						</tr>
						<tr>
<%
	boolean isDecisionSoumisIS = viewBean.initIsDecisionSoumisIS();

	if (isDecisionSoumisIS) { 
		String canton = "";

		if (!JadeStringUtil.isBlankOrZero(viewBean.getCsCantonImpotSource())) {
			canton = objSession.getCodeLibelle(viewBean.getCsCantonImpotSource());
		}
%>							<td>
								<ct:FWLabel key="JSP_TAUX_IMP" />
							</td>
							<td colspan="2">
								<input	type="text" 
										name="dummy1" 
										class="disabled" 
										value="<%=canton%>" 
										readonly />
								<input	type="hidden" 
										name="cantonTauxImposition" 
										value="<%=viewBean.getCsCantonImpotSource()%>" />
							</td>
							<td>
								<input	type="hidden" 
										name="tauxImposition" 
										value="<%=viewBean.getTauxImposition()%>" />
								<input	type="text" 
										name="dummy2" 
										value="<%=viewBean.getTauxImposition()%>" 
										class="disabled" 
										readonly />
							</td>
							<td>
								[%]
							</td>
<%
	} else {
%>							<td colspan="5">
								&nbsp;
							</td>
<%
	}
%>						</tr>
						<tr>
							<td valign="top">
								<ct:FWLabel key="JSP_REM_DECISION" />
							</td>
							<td colspan="4">
								<textarea rows="10" cols="100" name="remarque">
									<%=viewBean.getRemarque()%>
								</textarea>
							</td>
						</tr>
						<tr>
							<td colspan="5">
								&nbsp;
							</td>
						</tr>
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<input	type="button" 
						value="<ct:FWLabel key="JSP_SUIVANT" /> (alt+<ct:FWLabel key="AK_PRO_SUIVANT"/>)" 
						onclick="if(validate()) action(COMMIT);" 
						accesskey="<ct:FWLabel key="AK_PRO_SUIVANT"/>" />
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>