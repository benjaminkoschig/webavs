<%@page import="globaz.prestation.interfaces.util.nss.PRUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@ page import="globaz.externe.IPRConstantesExternes" %>
<%@ page import="globaz.jade.client.util.JadeStringUtil" %>
<%@ page import="globaz.cygnus.vb.dossiers.RFDossierJointTiersViewBean" %>
<%@ page import="globaz.cygnus.utils.RFGestionnaireHelper" %>
<%@ page import="globaz.cygnus.servlet.IRFActions" %>
<%@ page import="globaz.framework.bean.FWViewBeanInterface" %>
<%@ page import="globaz.pyxis.db.adressecourrier.TIPays" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>

<%@ include file="/theme/detail/header.jspf" %>
<%
	//Les labels de cette page commence par le préfix "JSP_RF_DOS_D"
	idEcran = "PRF0001";

	RFDossierJointTiersViewBean viewBean = (RFDossierJointTiersViewBean) session.getAttribute("viewBean");

	autoShowErrorPopup = true;

	bButtonDelete = false;
%>

<%@ include file="/theme/detail/javascripts.jspf" %>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal" showTab="options" />
<ct:menuChange displayId="options" menuId="cygnus-optionsdossiers">
	<ct:menuSetAllParams key="idDossier" value="<%=viewBean.getIdDossier()%>" />
	<ct:menuSetAllParams key="nss" value="<%=viewBean.getNss()%>" />
</ct:menuChange>

<script type="text/javascript">
	function readOnly(flag) {
		// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
		for (i = 0; i < document.forms[0].length; i++) {
			if (!document.forms[0].elements[i].readOnly 
				&& document.forms[0].elements[i].name != 'csNationaliteAffiche' 
				&& document.forms[0].elements[i].name != 'csSexeAffiche' 
				&& document.forms[0].elements[i].name != 'csCantonAffiche' 
				&& document.forms[0].elements[i].type != 'hidden') {
				document.forms[0].elements[i].disabled = flag;
			}
		}
	}

	function add() {
	}

	function upd() {
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add") {
			document.forms[0].elements('userAction').value = "back";
		} else {
			document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_DOSSIER_JOINT_TIERS%>.rechercher";
		}
	}

	function validate() {
		state = true;
		if (document.forms[0].elements('_method').value == "add") {
			document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_DOSSIER_JOINT_TIERS%>.ajouter";
		} else {
			document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_DOSSIER_JOINT_TIERS%>.modifier";
		}
		return state;
	}

	function del() {
		if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")) {
			document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_DOSSIER_JOINT_TIERS%>.supprimer";
			document.forms[0].submit();
		}
	}

	function init() {
<%
	if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
%>		errorObj.text = "<%=viewBean.getMessage()%>";
		showErrors()
		errorObj.text = "";
<%
	}
%>	}

	function postInit() {
		document.getElementById("csNationaliteAffiche").disabled = "true";
<%
	if (!viewBean.isNew()) {
%>		document.getElementById("partiallikeNSS").disabled = "true";
<%
	}
%>	}

	function nssFailure() {
		document.getElementById("idTiers").value = null;
		document.getElementById("nss").value = null;
	}

	function nssUpdateHiddenFields() {
		document.getElementById("nom").value = document.getElementById("nomAffiche").value;
		document.getElementById("prenom").value = document.getElementById("prenomAffiche").value;
		document.getElementById("csSexe").value = document.getElementById("csSexeAffiche").value;
		document.getElementById("dateNaissance").value = document.getElementById("dateNaissanceAffiche").value;
		document.getElementById("dateDeces").value = document.getElementById("dateDecesAffiche").value;
		document.getElementById("csNationalite").value = document.getElementById("csNationaliteAffiche").value;
		document.getElementById("csCanton").value = document.getElementById("csCantonAffiche").value;
		document.getElementById("noAVS").value = document.getElementById("likeNSS").value;
	}

	function nssChange(tag) {

		if (tag.select != null) {
			var element = tag.select.options[tag.select.selectedIndex];

			if (element.nss != null) {
				document.getElementById("nss").value = element.nss;
			}

			if (element.nom != null) {
				document.getElementById("nom").value = element.nom;
				document.getElementById("nomAffiche").value = element.nom;
			}

			if (element.prenom != null) {
				document.getElementById("prenom").value = element.prenom;
				document.getElementById("prenomAffiche").value = element.prenom;
			}

			if (element.provenance != null) {
				document.getElementById("provenance").value = element.provenance;
			}

			if (element.codeSexe != null) {
				for (var i = 0; i < document.getElementById("csSexeAffiche").length ; i++) {
					if (element.codeSexe == document.getElementById("csSexeAffiche").options[i].value) {
						document.getElementById("csSexeAffiche").options[i].selected = true;
					}
				}
				document.getElementById("csSexe").value = element.codeSexe;
			}

			if (element.id != null) {
				document.getElementById("idTiers").value = element.idAssure;
			}

			if (element.dateNaissance != null) {
				document.getElementById("dateNaissance").value = element.dateNaissance;
				document.getElementById("dateNaissanceAffiche").value = element.dateNaissance;
			}

			if (element.dateDeces != null) {
				document.getElementById("dateDeces").value = element.dateDeces;
				document.getElementById("dateDecesAffiche").value = element.dateDeces;
			}

			if (element.codePays != null) {
				for (var i = 0; i < document.getElementById("csNationaliteAffiche").length; i++) {
					if (element.codePays == document.getElementById("csNationaliteAffiche").options[i].value) {
						document.getElementById("csNationaliteAffiche").options[i].selected = true;
					}
				}
				document.getElementById("csNationalite").value = element.codePays;
			}

			if (element.codeCantonDomicile != null) {
				for (var i = 0; i < document.getElementById("csCantonAffiche").length; i++) {
					if (element.codeCantonDomicile == document.getElementById("csCantonAffiche").options[i].value) {
						document.getElementById("csCantonAffiche").options[i].selected = true;
					}
				}
				document.getElementById("csCanton").value = element.codeCantonDomicile;
			}

			if ('<%=PRUtil.PROVENANCE_TIERS%>' == element.provenance) {
				document.getElementById("nomAffiche").disabled = true;
				document.getElementById("prenomAffiche").disabled = true;
				document.getElementById("csSexeAffiche").disabled = true;
				document.getElementById("dateNaissanceAffiche").disabled = true;
				document.getElementById("dateDecesAffiche").disabled = true;
				document.getElementById("csNationaliteAffiche").disabled = true;
				document.getElementById("csCantonAffiche").disabled = true;
			}
		}
	}
</script>

<%@ include file="/theme/detail/bodyStart.jspf" %>

		<ct:FWLabel key="JSP_RF_DOS_D_TITRE" />

<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%
	String urlGED = servletContext + "/cygnus" 
					+ "?userAction=" + IRFActions.ACTION_DOSSIER_JOINT_TIERS + ".actionAfficherDossierGed" 
					+ "&noAVSId=" + viewBean.getNss() 
					+ "&idTiersExtraFolder=" + null 
					+ "&serviceNameId=" + viewBean.getSession().getApplication().getProperty(IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED);
%>				<tr>
					<td>
						<ct:FWLabel key="JSP_RF_DOS_D_GESTIONNAIRE" />
					</td>
					<td colspan="5">
<%
	if (viewBean.isNew()) {
%>						<ct:FWListSelectTag	name="idGestionnaire" 
											data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" 
											defaut="<%=viewBean.getSession().getUserId()%>" />
<%
	} else {
%>						<ct:FWListSelectTag	name="idGestionnaire" 
											data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" 
											defaut="<%=viewBean.getIdGestionnaire()%>" />
<%
	}
%>						<input type="hidden" name="nss" value="<%=viewBean.getNss()%>" />
						<input type="hidden" name="provenance" value="<%=viewBean.getProvenance()%>" />
						<input type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>" />
					</td>
				</tr>
				<tr>
					<td colspan="6">
						&nbsp;
					</td>
				</tr>
<%
	if (!viewBean.getIsAfficherDetail()) {
%>				<tr>
					<td>
						<ct:FWLabel key="JSP_RF_DOS_D_NSS" />
					</td>
					<td>
<%
		String params = "&provenance1=TIERS";
		String jspLocation = servletContext + "/cygnusRoot/numeroSecuriteSocialeSF_select.jsp";
%>						<ct1:nssPopup	name="likeNSS" 
										onFailure="nssFailure();" 
										onChange="nssChange(tag);" 
										params="<%=params%>" 
										value="<%=viewBean.getNumeroAvsFormateSansPrefixe()%>" 
										newnss=""
										jspName="<%=jspLocation%>" 
										avsMinNbrDigit="3" 
										nssMinNbrDigit="3" 
										avsAutoNbrDigit="11" 
										nssAutoNbrDigit="10" />
					</td>
					<td>
						<ct:FWLabel key="JSP_RF_DOS_D_NOM" />
					</td>
					<td>
						<input type="hidden" name="nom" value="<%=viewBean.getNom()%>" />
						<input type="text" name="nomAffiche" value="<%=viewBean.getNom()%>" disabled="true" readonly />
					</td>
					<td>
						<ct:FWLabel key="JSP_RF_DOS_D_PRENOM" />
					</td>
					<td>
						<input type="hidden" name="prenom" value="<%=viewBean.getPrenom()%>" />
						<input type="text" name="prenomAffiche" value="<%=viewBean.getPrenom()%>" disabled="true" readonly />
					</td>
<%
		if (!viewBean.isNew()) {
%>					<td class="mtd" align="left">
						<a href="#" onclick="window.open('<%=urlGED%>','GED_CONSULT')">
							<ct:FWLabel key="JSP_LIEN_GED"/>
						</a>
					</td>
<%
		}
%>				</tr>
				<tr>
					<td>
						<ct:FWLabel key="JSP_RF_DOS_D_DATE_NAISSANCE" />
					</td>
					<td>
						<input type="hidden" name="dateNaissance" value="<%=viewBean.getDateNaissance()%>" />
						<input type="text" name="dateNaissanceAffiche" value="<%=viewBean.getDateNaissance()%>" disabled="true" readonly />
					</td>
					<td>
						<ct:FWLabel key="JSP_RF_DOS_D_SEXE" />
					</td>
					<td>
						<ct:select name="csSexeAffiche" defaultValue="<%=viewBean.getCsSexe()%>" disabled="disabled">
							<ct:optionsCodesSystems csFamille="PYSEXE" />
						</ct:select>
						<input type="hidden" name="csSexe" value="<%=viewBean.getCsSexe()%>" />
					</td>
					<td>
						<ct:FWLabel key="JSP_RF_DOS_D_NATIONALITE" />
					</td>
					<td>
						<ct:FWListSelectTag	name="csNationaliteAffiche" 
											data="<%=viewBean.getTiPays()%>" 
											defaut="<%=JadeStringUtil.isIntegerEmpty(viewBean.getCsNationalite()) ? TIPays.CS_SUISSE : viewBean.getCsNationalite()%>" />
						<input type="hidden" name="csNationalite" value="<%=viewBean.getCsNationalite()%>" />
					</td>
				</tr>
				<tr>
					<td>
						<ct:FWLabel key="JSP_RF_DOS_D_CANTON_DOMICILE" />
					</td>
					<td>
						<ct:select name="csCantonAffiche" defaultValue="<%=viewBean.getCsCanton()%>" wantBlank="true" disabled="disabled">
							<ct:optionsCodesSystems csFamille="PYCANTON" />
						</ct:select>
						<input type="hidden" name="csCanton" value="<%=viewBean.getCsCanton()%>" />
					</td>
					<td>
						<ct:FWLabel key="JSP_RF_DOS_D_DATE_DECES" />
					</td>
					<td>
						<input type="hidden" name="dateDeces" value="<%=viewBean.getDateDeces()%>" />
						<input type="text" name="dateDecesAffiche" value="<%=viewBean.getDateDeces()%>" disabled="true" readonly />
					</td>
					<td colspan="2">
						&nbsp;
					</td>
				</tr>
<%
	} else {
%>				<tr valign="top">
					<td>
						<ct:FWLabel key="JSP_RF_DOS_D_TIERS" />
					</td>
					<td>
						<%=viewBean.getDetailAssure()%>
					</td>
					<td class="mtd" align="left">
						<a href="#" onclick="window.open('<%=urlGED%>','GED_CONSULT')">
							<ct:FWLabel key="JSP_LIEN_GED"/>
						</a>
					</td>
					<td colspan="3">
						&nbsp;
					</td>
				</tr>
<%
	}
%>				<tr>
					<td colspan="6">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<label for="forCsEtatDossier">
							<ct:FWLabel key="JSP_RF_DOS_D_ETAT" />
						</label>
						&nbsp;
					</td>
					<td>
						<ct:FWListSelectTag name="csEtatDossier" data="<%=viewBean.getCsEtatDossierData(false)%>" defaut="<%=viewBean.getCsEtatDossier()%>" />
					</td>
					<td colspan="4">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td colspan="6">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<label for="dateDebut">
							<ct:FWLabel key="JSP_RF_DOS_D_DATE_DEBUT" />
						</label>
						&nbsp;
					</td>
					<td>
						<input	type="text" 
								data-g-calendar=" " 
								name="dateDebutPeriodeDossier" 
								value="<%=viewBean.getDateDebutPeriodeDossier()%>" />
					</td>
					<td>
						<label for="dateFin">
							<ct:FWLabel key="JSP_RF_DOS_D_DATE_FIN" />
						</label>
						&nbsp;
					</td>
					<td>
						<input	type="text" 
								data-g-calendar=" " 
								name="dateFinPeriodeDossier" 
								value="<%=viewBean.getDateFinPeriodeDossier()%>" />
					</td>
				</tr>

<%@ include file="/theme/detail/bodyButtons.jspf" %>

<%@ include file="/theme/detail/bodyErrors.jspf" %>

	<ct:menuChange displayId="options" menuId="cygnus-optionsdossiers" />
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDossier()%>" menuId="cygnus-optionsdossiers" />
	<ct:menuSetAllParams key="idDossier" value="<%=viewBean.getIdDossier()%>" menuId="cygnus-optionsdossiers" />
	<ct:menuSetAllParams key="csEtatDossier" value="<%=viewBean.getCsEtatDossier()%>" menuId="cygnus-optionsdossiers" />
	<ct:menuSetAllParams key="nss" value="<%=viewBean.getNss()%>" menuId="cygnus-optionsdossiers" />
	<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiers()%>" menuId="cygnus-optionsdossiers" />
	<ct:menuSetAllParams key="idTierRequerant" value="<%=viewBean.getIdTiers()%>" menuId="cygnus-optionsdossiers" />
	<ct:menuSetAllParams key="idTiersVueGlobale" value="<%=viewBean.getIdTiers()%>" menuId="cygnus-optionsdossiers" />
	<ct:menuSetAllParams key="detailRequerant" value="<%=viewBean.getDetailAssure()%>" menuId="cygnus-optionsdossiers" />
	<ct:menuSetAllParams key="nom" value="<%=viewBean.getNom()%>" menuId="cygnus-optionsdossiers" />
	<ct:menuSetAllParams key="prenom" value="<%=viewBean.getPrenom()%>" menuId="cygnus-optionsdossiers" />
	<ct:menuSetAllParams key="dateNaissance" value="<%=viewBean.getDateNaissance()%>" menuId="cygnus-optionsdossiers" />
	<ct:menuSetAllParams key="libelleCourtSexe" value="<%=viewBean.getLibelleCourtSexe()%>" menuId="cygnus-optionsdossiers" />
	<ct:menuSetAllParams key="libellePays" value="<%=viewBean.getLibellePays()%>" menuId="cygnus-optionsdossiers" />
	<ct:menuSetAllParams key="dateDeces" value="<%=viewBean.getDateDeces()%>" menuId="cygnus-optionsdossiers" />
	<ct:menuSetAllParams key="csSexe" value="<%=viewBean.getCsSexe()%>" menuId="cygnus-optionsdossiers" />
	<ct:menuSetAllParams key="csNationalite" value="<%=viewBean.getCsNationalite()%>" menuId="cygnus-optionsdossiers" />

	<script type="text/javascript">
		reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);
	</script>

<%@ include file="/theme/detail/footer.jspf" %>
