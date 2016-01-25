<%@page import="globaz.apg.enums.APModeEditionDroit"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.jade.client.util.JadeNumericUtil"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="globaz.jade.client.util.JadeDateUtil"%>
<%@ page import="java.util.Date"%>
<%@ page import="globaz.apg.api.droits.IAPDroitAPG"%>
<%@ page import="globaz.apg.servlet.APAbstractDroitDTOAction"%>
<%@ page import="globaz.apg.servlet.IAPActions"%>
<%@ page import="globaz.apg.vb.droits.APDroitMatPViewBean"%>
<%@ page import="globaz.globall.util.JANumberFormatter"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@ page import="globaz.pyxis.db.tiers.TIPersonne"%>
<%@ page import="globaz.prestation.interfaces.util.nss.PRUtil"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>

<%@ include file="/theme/detail/header.jspf" %>
<%
	idEcran="PAP0003";
	
	APDroitMatPViewBean viewBean = (APDroitMatPViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdDroit();
	
	bButtonUpdate = viewBean.isModifiable() && bButtonUpdate;
	bButtonValidate = false;
	bButtonCancel = false;
	bButtonDelete = false;
%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ap-optionsempty"/>

<script type="text/javascript">

var EDITION_MODE = false;
<%if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.CREATION) || viewBean.getModeEditionDroit().equals(APModeEditionDroit.EDITION) ){%>
	EDITION_MODE = true;
<%}%>

	var ACTION_DROIT = "apg.droits.droitMatP",
	jsonAnnonce;

	function add () {
		nssUpdateHiddenFields();
		document.forms[0].elements('userAction').value = "apg.droits.droitMatP.ajouter"
	}

	function upd () {
		document.getElementById("nomAffiche").disabled = true;
		document.getElementById("prenomAffiche").disabled = true;
		document.getElementById("dateNaissanceAffiche").disabled = true;
		document.getElementById("dateDecesAffiche").disabled = true;
		document.getElementById("csEtatCivilAffiche").disabled = true;
		document.getElementById("csSexeAffiche").disabled = true;
	}

	function validate () {
		if (document.forms[0].elements('_method').value === "read") {
			document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_ENFANT_MAT%>.chercher";
		} else {
			nssUpdateHiddenFields();
			if (document.forms[0].elements('_method').value == "add"){
		    	document.forms[0].elements('userAction').value = ACTION_DROIT + ".ajouter";		        
			} else {
			   	document.forms[0].elements('userAction').value = ACTION_DROIT + ".modifier";
			}
		}
	  action(COMMIT);
	}

	function cancel () {
		if (document.forms[0].elements('_method').value == "add") {
			document.forms[0].elements('userAction').value="back";
		} else {
			document.forms[0].elements('userAction').value="apg.droits.droitMatP.afficher";
		}
	}

	function del () {
		if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")) {
			document.forms[0].elements('userAction').value = "apg.droits.droitMatP.supprimer";
			document.forms[0].submit();
		}
	}

	function init () {
	}

	function arret () {
		nssUpdateHiddenFields();
		document.forms[0].elements('userAction').value = "apg.droits.droitMatP.arreterEtape1";
		document.forms[0].elements('arreter').value = "on";
		document.forms[0].submit();
	}

	function limiteur () {
		// limite la saisie de la remarque à 255 caractères
		maximum = 252;
		if (document.forms[0].elements('remarque').value.length > maximum) {
			document.forms[0].elements('remarque').value = document.forms[0].elements('remarque').value.substring(0, maximum);
		}
	}

	function postInit () {
		if (<%=viewBean.isTrouveDansTiers()%>) {
			document.getElementById("nomAffiche").disabled = true;
			document.getElementById("prenomAffiche").disabled = true;
			document.getElementById("dateNaissanceAffiche").disabled = true;
			document.getElementById("csEtatCivilAffiche").disabled = true;
			document.getElementById("csSexeAffiche").disabled = true;
		}
	}

	function nssFailure () {
		var param_nss = "756." + document.getElementById("partiallikeNSS").value;
		document.getElementById("idAssure").value = null;
		document.getElementById("nss").value = null;
		document.getElementById("provenance").value = null;	
		document.getElementById("nomAffiche").disabled = false;
		document.getElementById("prenomAffiche").disabled = false;
		document.getElementById("nomPrenom").value = "";
		document.getElementById("dateNaissanceAffiche").disabled = false;
		document.getElementById("dateDecesAffiche").disabled = false;
		document.getElementById("csEtatCivilAffiche").disabled = false;
		document.getElementById("csSexeAffiche").disabled = false;
		checkNss(param_nss);
	}

	function nssUpdateHiddenFields () {
		document.getElementById("nom").value = document.getElementById("nomAffiche").value;
		document.getElementById("prenom").value = document.getElementById("prenomAffiche").value;
		document.getElementById("dateNaissance").value = document.getElementById("dateNaissanceAffiche").value;
		document.getElementById("dateDeces").value = document.getElementById("dateDecesAffiche").value;
		document.getElementById("csEtatCivil").value = document.getElementById("csEtatCivilAffiche").value;
		document.getElementById("nss").value = document.getElementById("likeNSS").value;
		document.getElementById("csSexe").value = document.getElementById("csSexeAffiche").value;
	}

	function nssChange (tag) {
		var param_nss = "756." + document.getElementById("partiallikeNSS").value;
		if (tag.select !== null) {
			var element = tag.select.options[tag.select.selectedIndex];
			if (element.nss !== null) {
				document.getElementById("nss").value = element.nss;
			}

			if (element.nom !== null) {
				document.getElementById("nom").value = element.nom;
				document.getElementById("nomAffiche").value = element.nom;
			}

			if (element.prenom !== null) {
				document.getElementById("prenom").value = element.prenom;
				document.getElementById("prenomAffiche").value = element.prenom;
			}

			if (element.nom !== null && element.prenom !== null) {
				document.getElementById("nomPrenom").value = element.nom + " " + element.prenom;
			}

			if (element.codeSexe !== null) {
				for (var i = 0; i < document.getElementById("csSexeAffiche").length ; i++) {
					if (element.codeSexe === document.getElementById("csSexeAffiche").options[i].value) {
						document.getElementById("csSexeAffiche").options[i].selected = true;
					}
				}
				document.getElementById("csSexe").value = element.codeSexe;
			}

			if (element.provenance !== null) {
				document.getElementById("provenance").value = element.provenance;
			}

			if (element.id !== null) {
				document.getElementById("idAssure").value = element.idAssure;
			}

			if (element.dateNaissance !== null) {
				document.getElementById("dateNaissance").value = element.dateNaissance;
				document.getElementById("dateNaissanceAffiche").value = element.dateNaissance;
			}

			if (element.dateDeces !== null) {
				document.getElementById("dateDeces").value = element.dateDeces;
				document.getElementById("dateDecesAffiche").value = element.dateDeces;
			}

			if (element.codeEtatCivil !== null) {
				for (var i = 0; i < document.getElementById("csEtatCivilAffiche").length ; i++) {
					if (element.codeEtatCivil === document.getElementById("csEtatCivilAffiche").options[i].value) {
						document.getElementById("csEtatCivilAffiche").options[i].selected = true;
					}
				}
				document.getElementById("csEtatCivil").value = element.codeEtatCivil;
			}

			if ('<%=PRUtil.PROVENANCE_TIERS%>' === element.provenance) {
				document.getElementById("nomAffiche").disabled = true;
				document.getElementById("prenomAffiche").disabled = true;
				document.getElementById("dateNaissanceAffiche").disabled = true;
				document.getElementById("dateDecesAffiche").disabled = true;
				document.getElementById("csEtatCivilAffiche").disabled = true;
				document.getElementById("csSexeAffiche").disabled = true;
			}
		}
		checkNss(param_nss);
	}
</script>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<ct:FWLabel key="JSP_TITRE_SAISIE_MAT_1"/>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<tr>
							<td>
								<label for="idGestionnaire">
									<ct:FWLabel key="JSP_GESTIONNAIRE" />
								</label>
							</td>
							<td colspan="5">
								<ct:FWListSelectTag	name="idGestionnaire" 
													data="<%=viewBean.getResponsableData()%>" 
													defaut="<%=viewBean.getIdGestionnaire()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								<hr/>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								<h6>
									<ct:FWLabel key="JSP_TIERS" />
								</h6>
							</td>
						</tr>
						<!--Gestion du NSS -->
						<tr>
							<td>
								<label for="partiallikeNSS">
									<ct:FWLabel key="JSP_NSS_ABREGE" />
								</label>
							</td>
							<td colspan="5">
<%
	String params = "&provenance1=TIERS&provenance2=CI";
	String jspLocation = servletContext + "/ijRoot/numeroSecuriteSocialeSF_select.jsp";
%>								<ct1:nssPopup	name="likeNSS" 
												onFailure="nssFailure();" 
												onChange="nssChange(tag);" 
												params="<%=params%>" 
												value="<%=viewBean.getNumeroAvsFormateSansPrefixe()%>" 
												newnss="<%=viewBean.isNNSS()%>" 
												jspName="<%=jspLocation%>" 
												avsMinNbrDigit="3" 
												nssMinNbrDigit="3" 
												avsAutoNbrDigit="11" 
												nssAutoNbrDigit="10" />
								<input	type="text" 
										name="nomPrenom" 
										value="<%=viewBean.getNom()%> <%=viewBean.getPrenom()%>" 
										class="libelleLongDisabled" />
								<input	type="hidden" 
										name="nss" 
										id="nss" 
										value="<%=viewBean.getNss()%>" />
								<input	type="hidden" 
										name="idAssure" 
										value="<%=viewBean.getIdAssure()%>" />
								<input	type="hidden" 
										name="provenance" 
										value="<%=viewBean.getProvenance()%>" />
								<input	type="hidden" 
										id="idDroit" 
										name="idDroit" 
										value="<%=viewBean.getIdDroit()%>" />
								<input	type="hidden" 
										name="<%=APAbstractDroitDTOAction.PARAM_ID_DROIT%>" 
										value="<%=viewBean.getIdDroit()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="nomAffiche">
									<ct:FWLabel key="JSP_NOM" />
								</label>
							</td>
							<td>
								<input	type="hidden" 
										name="nom" 
										value="<%=viewBean.getNom()%>" />
  								<input	type="text" 
  										id="nomAffiche" 
  										name="nomAffiche" 
  										value="<%=viewBean.getNom()%>" />
							</td>
							<td>
								<label for="prenomAffiche">
									<ct:FWLabel key="JSP_PRENOM" />
								</label>
							</td>
							<td colspan="3">
								<input	type="hidden" 
										name="prenom" 
										value="<%=viewBean.getPrenom()%>" />
								<input	type="text" 
										id="prenomAffiche" 
										name="prenomAffiche" 
										value="<%=viewBean.getPrenom()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="csSexeAffiche">
									<ct:FWLabel key="JSP_SEXE" />
								</label>
							</td>
							<td colspan="5">
								<ct:FWCodeSelectTag	name="csSexeAffiche" 
													wantBlank="<%=true%>" 
													codeType="PYSEXE" 
													defaut="<%=viewBean.getCsSexe()%>" />
								<input	type="hidden" 
										name="csSexe" 
										value="<%=viewBean.getCsSexe()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="csEtatCivilAffiche">
									<ct:FWLabel key="JSP_ETAT_CIVIL" />
								</label>
							</td>
							<td>
								<ct:FWCodeSelectTag	name="csEtatCivilAffiche" 
													wantBlank="<%=true%>" 
													codeType="PYETATCIVI" 
													defaut="<%=viewBean.getCsEtatCivil()%>" />
								<input	type="hidden" 
										name="csEtatCivil" 
										id="csEtatCivil" 
										value="<%=viewBean.getCsEtatCivil()%>" />
							</td>
							<td>
								<label for="dateNaissanceAffiche">
									<ct:FWLabel key="JSP_DATE_NAISSANCE" />
								</label>
							</td>
							<td colspan="3">
								<input	type="hidden" 
										name="dateNaissance" 
										value="<%=viewBean.getDateNaissance()%>" />
								<input	type="text" 
										id="dateNaissanceAffiche" 
										name="dateNaissanceAffiche" 
										data-g-calendar=" " 
										value="<%=viewBean.getDateNaissance()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="npa">
									<ct:FWLabel key="JSP_NPA" />
								</label>
							</td>
							<td>
								<input	type="text" 
										name="npa" 
										id="npa" 
										value="<%=viewBean.getNpa()%>" 
										class="numero" />
							</td>
							<td>
								<label for="pays">
									<ct:FWLabel key="JSP_PAYS_DOMICILE" />
								</label>
							</td>
							<td colspan="3">
								<ct:FWListSelectTag	name="pays" 
													data="<%=viewBean.getTiPays()%>" 
													defaut="<%=JadeStringUtil.isIntegerEmpty(viewBean.getPays()) ? TIPays.CS_SUISSE : viewBean.getPays()%>" />
								<script type="text/javascript">
									document.getElementById("likeNSS").onkeypress = new Function ("", "return filterCharForPositivFloat(window.event);");
								</script>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<label for="dateDebutDroit">
									<ct:FWLabel key="JSP_DATE_DEBUT" />
								</label>
							</td>
							<td>
								<input	type="text" 
										id="dateDebutDroit" 
										name="dateDebutDroit" 
										data-g-calendar=" " 
										value="<%=viewBean.getDateDebutDroit()%>" />
							</td>
							<td>
								<label for="dateRepriseActiv">
									<ct:FWLabel key="JSP_DATE_REPRISE_ACTIVITE" />
								</label>
							</td>
							<td>
								<input	type="text" 
										id="dateRepriseActiv" 
										name="dateRepriseActiv" 
										data-g-calendar=" " 
										value="<%=viewBean.getDateRepriseActiv()%>" />
							</td>
							<td>
								<label for="dateDecesAffiche">
									<ct:FWLabel key="JSP_DATE_DECES" />
								</label>
							</td>
							<td>
								<input	type="hidden" 
										name="dateDeces" 
										value="<%=viewBean.getDateDeces()%>" />
								<input	type="text" 
										id="dateDecesAffiche" 
										name="dateDecesAffiche" 
										data-g-calendar=" " 
										value="<%=viewBean.getDateDeces()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="dateDepot">
									<ct:FWLabel key="JSP_DATE_DEPOT" />
								</label>
							</td>
							<td>
								<input	type="text" 
										id="dateDepot" 
										name="dateDepot" 
										data-g-calendar=" " 
										value="<%=viewBean.getDateDepot()%>" />
							</td>
							<td>
								<label for="dateReception">
									<ct:FWLabel key="JSP_DATE_RECEPTION" />
								</label>
							</td>
							<td colspan="3">
								<input	type="text" 
										id="dateReception" 
										name="dateReception" 
										data-g-calendar=" " 
										value="<%=viewBean.getDateReception()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<label for=isSoumisCotisation">
									<ct:FWLabel key="JSP_SOUMIS_IMPOT_SOURCE" />
								</label>
							</td>
							<td>
								<input	type="checkbox" 
										id="isSoumisCotisation" 
										name="isSoumisCotisation" 
										<%=viewBean.getIsSoumisCotisation().booleanValue() ? "checked" : ""%> />
							</td>
							<td>
								<label for=tauxImpotSource">
									<ct:FWLabel key="JSP_TAUX_IMPOT_SOURCE_CARTE" />
								</label>
							</td>
							<td colspan="3">
								<input	type="text" 
										id="tauxImpotSource" 
										name="tauxImpotSource" 
										value="<%=viewBean.getTauxImpotSource()%>" 
										class="numero" 
										onchange="validateFloatNumber(this);" 
										onkeypress="return filterCharForFloat(window.event);" 
										style="text-align: right" />
								<ct:FWLabel key="JSP_TAUX_CANTON" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<label for="droitAcquis">
									<ct:FWLabel key="JSP_DROIT_ACQUIS" />
								</label>
							</td>
							<td>
								<input	type="text" 
										id="droitAcquis" 
										name="droitAcquis" 
										class="libelle" 
										onchange="validateFloatNumber(this);" 
										onkeypress="return filterCharForFloat(window.event);" 
										style="text-align: right" 
										value="<%=JANumberFormatter.fmt(viewBean.getDroitAcquis(), true, true, false, 2)%>" />
							</td>
							<td>
								<label for="csProvenanceDroitAcquis">
									<ct:FWLabel key="JSP_PROVENANCE_DROIT" />
								</label>
							</td>
							<td colspan="2">
								<ct:select	name="csProvenanceDroitAcquis" 
											wantBlank="true" 
											defaultValue="<%=viewBean.getCsProvenanceDroitAcquis()%>">
									<ct:optionsCodesSystems csFamille="<%=IAPDroitAPG.GROUPE_CS_PROVENANCE_DROIT_ACQUIS%>">
									</ct:optionsCodesSystems>
								</ct:select>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<label for="reference">
									<ct:FWLabel key="JSP_REFERENCE" />
								</label>
							</td>
							<td colspan="5">
								<input	type="text" 
										id="reference" 
										name="reference" 
										value="<%=viewBean.getReference()%>" 
										class="libelleLong" />
								<!-- champs relatifs à la demande -->
								<input	type="hidden" 
										name="idDemande" 
										value="<%=viewBean.getIdDemande()%>" />
								<input	type="hidden" 
										name="arreter" 
										value="" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="remarque">
									<ct:FWLabel key="JSP_REMARQUE" />
								</label>
							</td>
							<td colspan="5">
								<textarea	id="remarque" 
											name="remarque" 
											cols="85" 
											rows="3" 
											onKeyDown="limiteur();">
									<%=viewBean.getRemarque()%>
								</textarea>
								<br/>
								<ct:FWLabel key="JSP_REMARQUE_COMMENT" />
							</td>
						</tr>
<%@ include file="plausibilites.jsp" %>
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
			<%-- tpl:put name="zoneButtons" --%>
				<input	type="button" 
						value="<ct:FWLabel key="JSP_ARRET" /> (alt+<ct:FWLabel key="AK_MATERNITE_ARRET" />)" 
						onclick="arret()" 
						accesskey="<ct:FWLabel key="AK_MATERNITE_ARRET" />" />
				<input	type="button" 
						value="<ct:FWLabel key="JSP_SUIVANT" /> (alt+<ct:FWLabel key="AK_MATERNITE_SUIVANT" />)" 
						onclick="validate()" 
						accesskey="<ct:FWLabel key="AK_MATERNITE_SUIVANT" />" />
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>