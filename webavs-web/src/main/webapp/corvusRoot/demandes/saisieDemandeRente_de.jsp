<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@ page import="com.google.gson.Gson"%>

<%@ page import="globaz.corvus.api.demandes.IREDemandeRente"%>
<%@ page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="globaz.corvus.vb.demandes.RESaisieDemandeRenteViewBean"%>
<%@ page import="globaz.externe.IPRConstantesExternes"%>
<%@ page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page import="globaz.globall.util.JAUtil"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.prestation.api.IPRInfoCompl"%>
<%@ page import="globaz.prestation.interfaces.util.nss.PRUtil"%>
<%@ page import="globaz.prestation.tools.PRCodeSystem"%>
<%@ page import="globaz.pyxis.db.adressecourrier.TIPays"%>

<%@ page import="java.util.Map"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>

<%@ include file="/theme/detail/header.jspf" %>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<%
	idEcran="PRE0001";

	// Les labels de cette page commencent par LABEL_JSP_SDR_D
	boolean isSupprimable = false;

	RESaisieDemandeRenteViewBean viewBean = (RESaisieDemandeRenteViewBean) session.getAttribute("viewBean");

	Map<String, String> codesCsCodeInfirmite = PRCodeSystem.getCUCSinMap(viewBean.getSession(), IREDemandeRente.CS_GROUPE_INFIRMITE);
	Map<String, String> codesCsCodeAtteinte  = PRCodeSystem.getCUCSinMap(viewBean.getSession(), IREDemandeRente.CS_GROUPE_ATTEINTE_INVALIDITE);
	Map<String, String> officesAi = viewBean.getMapOfficesAi();

	String requerantDescription = request.getParameter("requerantDescription");
	selectedIdValue = request.getParameter("selectedId");

	bButtonUpdate = viewBean.isModifiable() && bButtonUpdate;
	bButtonValidate = false;
	bButtonCancel = false;
	bButtonDelete = false;
	boolean hasRight = viewBean.getSession().hasRight("corvus.demandes.saisieDemandeRente", FWSecureConstants.UPDATE);

	if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE.equals(viewBean.getCsEtat())
		|| IREDemandeRente.CS_ETAT_DEMANDE_RENTE_AU_CALCUL.equals(viewBean.getCsEtat())) {
		if (!JadeStringUtil.isIntegerEmpty(viewBean.getIdDemandeRente())){
			isSupprimable = true;
		}
	}

	if (JadeStringUtil.isBlankOrZero(viewBean.getCsEtat())) {
		viewBean.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
	}
	
	String showModalDialogURLNbrPageMotivation = servletContext + "/corvusRoot/demandes/majNbrPageMotivation_dialog.jsp" 
												+ "?idDemandeRente=" + viewBean.getIdDemandeRente() 
												+ "&csTypeDemandeRente=" + viewBean.getCsTypeDemandeRente() 
												+ "&mainServletPath=" + mainServletPath 
												+ "&";
	String showModalDialogURLGenrePrononce = servletContext + "/corvusRoot/demandes/majGenrePrononce_dialog.jsp" 
											+ "?idDemandeRente=" + viewBean.getIdDemandeRente()
											+ "&csTypeDemandeRente=" + viewBean.getCsTypeDemandeRente()
											+ "&mainServletPath=" + mainServletPath
											+ "&";
	String showModalDialogURLGestionnaire = servletContext + "/corvusRoot/demandes/majGestionnaire_dialog.jsp" 
											+ "?idDemandeRente=" + viewBean.getIdDemandeRente() 
											+ "&csTypeDemandeRente=" + viewBean.getCsTypeDemandeRente() 
											+ "&mainServletPath=" + mainServletPath 
											+ "&";
%>

<%@ include file="/theme/detail/javascripts.jspf" %>

<%
	if (viewBean.isModifie()) {
%>			<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu" />
			<ct:menuChange displayId="options" menuId="corvus-optionsempty" />
<%
	} else {
%>			<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options" />
			<ct:menuChange displayId="options" menuId="corvus-optionsdemanderentes">
				<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDemandeRente()%>" />
				<ct:menuSetAllParams key="noDemandeRente" value="<%=viewBean.getIdDemandeRente()%>" />
				<ct:menuSetAllParams key="idTierRequerant" value="<%=viewBean.getIdTiers()%>" />
				<ct:menuSetAllParams key="idRenteCalculee" value="<%=viewBean.getIdRenteCalculee()%>" />
				<ct:menuSetAllParams key="csTypeDemande" value="<%=viewBean.getCsTypeDemandeRente()%>" />
				<ct:menuSetAllParams key="numNSS" value="<%=viewBean.getNssRequerant()%>" />
				<ct:menuSetAllParams key="idBasesCalcul" value="" />
			
<%
		if ((viewBean.getIsCalculerCopie() != null && viewBean.getIsCalculerCopie().booleanValue()) || viewBean.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE)) {
 %>				<ct:menuActivateNode active="no" nodeId="calcul" /> 
<%
		} else {
%> 				<ct:menuActivateNode active="yes" nodeId="calcul" /> 

<%
		}

		if (viewBean.getCsTypeCalcul().equals(IREDemandeRente.CS_TYPE_CALCUL_STANDARD)
			|| viewBean.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE)) {
			if(!viewBean.isInfoComplRenteVeuvePerdure()) {
%>				<ct:menuActivateNode active="no" nodeId="terminer" />
<%
			} else {
%>				<ct:menuActivateNode active="yes" nodeId="terminer" />
<%
			}
		} else {
%>				<ct:menuActivateNode active="yes" nodeId="terminer" />
<%
		}

		if (viewBean.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE)
			|| viewBean.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE)
			|| viewBean.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE)
			|| viewBean.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE)) {
%>				<ct:menuActivateNode active="no" nodeId="saisieManuelle" />
<%
		} else {
%>				<ct:menuActivateNode active="yes" nodeId="saisieManuelle" />
<%
		}

		if (viewBean.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE)) {
%>				<ct:menuActivateNode active="no" nodeId="preparerDecision" />
				<ct:menuActivateNode active="no" nodeId="prepIntMoratoires" />
<%
		} else {
			if (!viewBean.isPreparationDecisionValide()) {
%>				<ct:menuActivateNode active="no" nodeId="preparerDecision" />
<%
			}
		}

		if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(viewBean.getCsTypeDemandeRente()) 
			|| IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(viewBean.getCsTypeDemandeRente())) {
%>				<ct:menuActivateNode active="no" nodeId="copierDemande" />
<%
		} else {
%>				<ct:menuActivateNode active="yes" nodeId="copierDemande" />
<%		}
		
		
		if( (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(viewBean.getCsTypeDemandeRente()) ||
				 IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(viewBean.getCsTypeDemandeRente()))
				 && viewBean.getCsTypeCalcul().equals(IREDemandeRente.CS_TYPE_CALCUL_STANDARD)
				 && (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE.equals(viewBean.getCsEtat()) || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(viewBean.getCsEtat()) || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE.equals(viewBean.getCsEtat()))){
%>			<ct:menuActivateNode active="yes" nodeId="communicationMutationOAI" />
<%		} else{ 
%> 
		<ct:menuActivateNode active="no" nodeId="communicationMutationOAI" /> 
<%		} 
%>	 
		</ct:menuChange>
<%
		}
%>

<style type="text/css">
	.widgetOffice {
		width: 60px;
	}
</style>
<script type="text/javascript">

	var o_mapCodeInfirmite = <%=new Gson().toJson(codesCsCodeInfirmite)%>;
	var o_mapCodeAtteinte = <%=new Gson().toJson(codesCsCodeAtteinte)%>;
	var o_mapOfficesAi = <%=new Gson().toJson(viewBean.getMapOfficesAi())%>;

	function calculerCopie() {
		document.forms[0].elements('userAction').value = document.forms[0].elements('userAction').value="<%=IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.calculerCopie";
		document.forms[0].submit();
	}

	function init(){
		changeTypeDemandeRente(document.getElementById("csTypeDemandeRente"));
	}

	function postInit(){
		changeTypeDemandeRente(document.getElementById("csTypeDemandeRente"));

		document.getElementById("nomRequerantAffiche").disabled = true;
		document.getElementById("prenomRequerantAffiche").disabled = true;
		document.getElementById("csSexeRequerantAffiche").disabled = true;
		document.getElementById("dateNaissanceRequerantAffiche").disabled = true;
		document.getElementById("dateDecesRequerantAffiche").disabled = true;
		document.getElementById("csNationaliteRequerantAffiche").disabled = true;
		document.getElementById("csCantonDomicileRequerantAffiche").disabled = true;
		document.getElementById("csEtat").disabled = true;
		
<%
	if (!JadeStringUtil.isEmpty(viewBean.getIdDemandeRente())) {
%>		document.getElementById("csTypeDemandeRente").disabled = true;
<%
	}
%>
		metAJourCsAtteinteInv();
		metAJourCsInfirmiteInv();
		metAJourCsAtteinteApi();
		metAJourCsInfirmiteApi();

		document.getElementById("isCelibataireSansEnfantsVieillesse").disabled = false;
		document.getElementById("isCelibataireSansEnfantsInv").disabled = false;

		if (document.forms[0].elements('_method').value == "add") {
			document.getElementById("isAccuseDeReception").disabled = false;
		} else {
			document.getElementById("isAccuseDeReception").disabled = true;
		}
		//K151030_003
		if(document.forms[0].elements('_method').value == "upd" ||
				document.forms[0].elements('_method').value == "add"){
			document.getElementById("addPeriodeInvButton").disabled = false;
			document.getElementById("boutonAjpouterPeriode").disabled = false;
		}else{
			document.getElementById("addPeriodeInvButton").disabled = true;
			document.getElementById("boutonAjpouterPeriode").disabled = true;
		}
	}

	function add() {
	}

	function delet() {
		if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")) {
			document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.supprimerSaisieDemandeRente";
			document.forms[0].submit();
		}
	}

	function upd() {
		nssUpdateHiddenFields();
		document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.ajouterSaisieDemandeRente";

		document.getElementById("addPeriodeInvButton").disabled = false;
		document.getElementById("boutonAjpouterPeriode").disabled = false;
		
		document.getElementById("nomRequerantAffiche").disabled = true;
		document.getElementById("prenomRequerantAffiche").disabled = true;
		document.getElementById("csSexeRequerantAffiche").disabled = true;
		document.getElementById("dateNaissanceRequerantAffiche").disabled = true;
		document.getElementById("dateDecesRequerantAffiche").disabled = true;
		document.getElementById("csNationaliteRequerantAffiche").disabled = true;
		document.getElementById("csCantonDomicileRequerantAffiche").disabled = true;
		document.getElementById("csTypeDemandeRente").disabled=true;
		document.getElementById("csEtat").disabled = true;

		document.forms[0].elements('modifie').value = "true";
	}

	function validate() {
		nssUpdateHiddenFields();
		state = validateFields();
		if (document.forms[0].elements('_method').value == "add") {
			document.forms[0].elements('modifie').value = "true";
			document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.ajouterSaisieDemandeRente";
		} else {
			if (document.forms[0].elements('isEditionPartielle').value == "true") {
				document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.majPartielleDemande";
			} else {
				document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.ajouterSaisieDemandeRente";
			}
		}

		return state;
	}

	function arret() {
		nssUpdateHiddenFields();

		if (document.forms[0].elements('isEditionPartielle').value == "true") {
			document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.arreterMajPartielleDemande";
		} else {
			document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.arreterSaisieDemandeRente";
		}
		document.forms[0].submit();
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add") {
			document.forms[0].elements('userAction').value = "back";
		} else {
			document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE%>.chercher";
		}
	}

	function informationsComplementaires() {
		if (document.forms[0].elements('modifie').value == "true") {
			alert("<%=viewBean.getSession().getLabel("JSP_POPUP_INFORMATIONS_COMPLEMENTAIRE_NOUVELLE_DEMANDE")%> \n\n <%=viewBean.getSession().getLabel("JSP_POPUP_INFORMATIONS_COMPLEMENTAIRE_CAUSE")%> :\n-> <%=viewBean.getSession().getLabel("JSP_POPUP_INFORMATIONS_COMPLEMENTAIRE_CAUSE_NOUVELLE_DEMANDE")%> \n-> <%=viewBean.getSession().getLabel("JSP_POPUP_INFORMATIONS_COMPLEMENTAIRE_CAUSE_MODIFICATION_DEMANDE")%>");
		} else {
			if (document.forms[0].elements('_method').value == "add") {
								
				alert("<%=viewBean.getSession().getLabel("JSP_POPUP_INFORMATIONS_COMPLEMENTAIRE_NOUVELLE_DEMANDE")%> \n\n <%=viewBean.getSession().getLabel("JSP_POPUP_INFORMATIONS_COMPLEMENTAIRE_CAUSE")%> :\n-> <%=viewBean.getSession().getLabel("JSP_POPUP_INFORMATIONS_COMPLEMENTAIRE_CAUSE_NOUVELLE_DEMANDE")%> \n-> <%=viewBean.getSession().getLabel("JSP_POPUP_INFORMATIONS_COMPLEMENTAIRE_CAUSE_MODIFICATION_DEMANDE")%>");
				
				
			} else {
				document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.afficherInformationsComplementaires";
				document.forms[0].submit();
			}
		}
	}

	function nssFailure() {
		document.getElementById("idAssure").value = null;
		document.getElementById("nssRequerant").value = null;
		document.getElementById("provenance").value = null;
	}

	function nssUpdateHiddenFields() {
		document.getElementById("nomRequerant").value = document.getElementById("nomRequerantAffiche").value;
		document.getElementById("prenomRequerant").value = document.getElementById("prenomRequerantAffiche").value;
		document.getElementById("csSexeRequerant").value = document.getElementById("csSexeRequerantAffiche").value;
		document.getElementById("dateNaissanceRequerant").value = document.getElementById("dateNaissanceRequerantAffiche").value;
		document.getElementById("dateDecesRequerant").value = document.getElementById("dateDecesRequerantAffiche").value;
		document.getElementById("csNationaliteRequerant").value = document.getElementById("csNationaliteRequerantAffiche").value;
		document.getElementById("csCantonRequerant").value = document.getElementById("csCantonDomicileRequerantAffiche").value;
		document.getElementById("nssRequerant").value = document.getElementById("likeNSS").value;
	}

	function nssChange(tag) {
		if(tag.select != null) {
			var element = tag.select.options[tag.select.selectedIndex];

			if (element.nss!=null) {
				document.getElementById("nssRequerant").value = element.nss;
			}

			if (element.nom != null) {
				document.getElementById("nomRequerant").value = element.nom;
				document.getElementById("nomRequerantAffiche").value = element.nom;
			}

			if (element.prenom != null) {
				document.getElementById("prenomRequerant").value = element.prenom;
				document.getElementById("prenomRequerantAffiche").value = element.prenom;
			}

			if (element.provenance != null) {
				document.getElementById("provenance").value = element.provenance;
			}

			if (element.id != null) {
				document.getElementById("idAssure").value = element.idAssure;
			}

			if (element.id != null){
				document.getElementById("idTiers").value = element.idAssure;
			}

			if (element.codeSexe != null) {
				for (var i = 0; i < document.getElementById("csSexeRequerantAffiche").length ; i++) {
					if (element.codeSexe == document.getElementById("csSexeRequerantAffiche").options[i].value) {
						document.getElementById("csSexeRequerantAffiche").options[i].selected = true;
					}
				}
				document.getElementById("csSexeRequerant").value = element.codeSexe;
			}

			if (element.dateNaissance != null){
				document.getElementById("dateNaissanceRequerant").value = element.dateNaissance;
				document.getElementById("dateNaissanceRequerantAffiche").value = element.dateNaissance;
			}

			if (element.dateDeces != null) {
				document.getElementById("dateDecesRequerant").value = element.dateDeces;
				document.getElementById("dateDecesRequerantAffiche").value = element.dateDeces;
			}

			if (element.codePays != null) {
				for (var i = 0; i < document.getElementById("csNationaliteRequerantAffiche").length; i++) {
					if (element.codePays == document.getElementById("csNationaliteRequerantAffiche").options[i].value) {
						document.getElementById("csNationaliteRequerantAffiche").options[i].selected = true;
					}
				}
				document.getElementById("csNationaliteRequerant").value = element.codePays;
			}

			if (element.codeCantonDomicile != null) {
				for (var i = 0; i < document.getElementById("csCantonDomicileRequerantAffiche").length; i++) {
					if (element.codeCantonDomicile == document.getElementById("csCantonDomicileRequerantAffiche").options[i].value) {
						document.getElementById("csCantonDomicileRequerantAffiche").options[i].selected = true;
					}
				}
				document.getElementById("csCantonRequerant").value = element.codeCantonDomicile;
			}

			if ('<%=PRUtil.PROVENANCE_TIERS%>' == element.provenance) {
				document.getElementById("nomRequerantAffiche").disabled = true;
				document.getElementById("prenomRequerantAffiche").disabled = true;
				document.getElementById("csSexeRequerantAffiche").disabled = true;
				document.getElementById("dateNaissanceRequerantAffiche").disabled = true;
				document.getElementById("dateDecesRequerantAffiche").disabled = true;
				document.getElementById("csNationaliteRequerantAffiche").disabled = true;
				document.getElementById("csCantonDomicileRequerantAffiche").disabled = true;
			}
		}
	}

	function changeTypeDemandeRente(typeDemande) {
		if (typeDemande.value == <%=IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API%>) {
			document.all('demandeAPI').style.display = 'block';
			document.all('demandeInvalidite').style.display = 'none';
			document.all('demandeVieillesse').style.display = 'none';
			document.all('demandeSurvivant').style.display = 'none';
		} else if (typeDemande.value == <%=IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE%>) {
			document.all('demandeAPI').style.display = 'none';
			document.all('demandeInvalidite').style.display = 'block';
			document.all('demandeVieillesse').style.display = 'none';
			document.all('demandeSurvivant').style.display = 'none';
		} else if (typeDemande.value == <%=IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE%>) {
			document.all('demandeAPI').style.display = 'none';
			document.all('demandeInvalidite').style.display = 'none';
			document.all('demandeVieillesse').style.display = 'block';
			document.all('demandeSurvivant').style.display = 'none';
		} else if (typeDemande.value == <%=IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT%>) {
			document.all('demandeAPI').style.display = 'none';
			document.all('demandeInvalidite').style.display = 'none';
			document.all('demandeVieillesse').style.display = 'none';
			document.all('demandeSurvivant').style.display = 'block';
		}
	}

	function addPeriodeAPI() {
		document.forms[0].elements('modifie').value = "true";
		document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.actionAjouterPeriodeAPI";
		document.forms[0].submit();
	}

	function addPeriodeInv() {
		document.forms[0].elements('modifie').value = "true";
		document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.actionAjouterPeriodeINV";
		document.forms[0].submit();
	}

	function metAJourCsInfirmiteApi() {
		if (document.getElementById('codeCsInfirmiteApi').value != "" 
			&& typeof o_mapCodeInfirmite[document.getElementById('codeCsInfirmiteApi').value] != 'undefined') {
			document.getElementById("imageOKKOInfirmiteApi").src = '<%=request.getContextPath() + "/images/ok.gif"%>';
			document.getElementById('csInfirmiteApi').value = o_mapCodeInfirmite[document.getElementById('codeCsInfirmiteApi').value];
		} else {
			document.getElementById("imageOKKOInfirmiteApi").src = '<%=request.getContextPath() + "/images/erreur.gif"%>';
			document.getElementById('csInfirmiteApi').value = "";
		}
	}

	function metAJourCsAtteinteApi() {
		if (document.getElementById('codeCsInfirmiteApi').value != "" 
			&& typeof o_mapCodeAtteinte[document.getElementById('codeCsAtteinteApi').value] != 'undefined') {
			document.getElementById("imageOKKOAtteinteApi").src = '<%=request.getContextPath() + "/images/ok.gif"%>';
			document.getElementById('csAtteinteApi').value = o_mapCodeAtteinte[document.getElementById('codeCsAtteinteApi').value];
		} else {
			document.getElementById("imageOKKOAtteinteApi").src = '<%=request.getContextPath() + "/images/erreur.gif"%>';
			document.getElementById('csAtteinteApi').value = "";
		}
	}

	function metAJourCsInfirmiteInv() {
		if (document.getElementById('codeCsInfirmiteInv').value != "" 
			&& typeof o_mapCodeInfirmite[document.getElementById('codeCsInfirmiteInv').value] != 'undefined') {
			document.getElementById("imageOKKOInfirmiteInv").src = '<%=request.getContextPath() + "/images/ok.gif"%>';
			document.getElementById('csInfirmiteInv').value = o_mapCodeInfirmite[document.getElementById('codeCsInfirmiteInv').value];
		} else {
			document.getElementById("imageOKKOInfirmiteInv").src = '<%=request.getContextPath() + "/images/erreur.gif"%>';
			document.getElementById('csInfirmiteInv').value = "";
		}
	}

	function metAJourCsAtteinteInv() {
		if (document.getElementById('codeCsAtteinteInv').value != "" 
			&& typeof o_mapCodeAtteinte[document.getElementById('codeCsAtteinteInv').value] != 'undefined'){
			document.getElementById("imageOKKOAtteinteInv").src = '<%=request.getContextPath() + "/images/ok.gif"%>';
			document.getElementById('csAtteinteInv').value = o_mapCodeAtteinte[document.getElementById('codeCsAtteinteInv').value];
		} else {
			document.getElementById("imageOKKOAtteinteInv").src = '<%=request.getContextPath() + "/images/erreur.gif"%>';
			document.getElementById('csAtteinteInv').value = "";
		}
	}

	function readOnly(flag) {
		// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
		$(':input:not(:button,[readyOnly],.forceDisable,[type="hidden"])').attr('disabled', flag);
	}

	function majNbrPageMotivation(input) {
		if (window.showModalDialog) {
			var value = window.showModalDialog("<%=showModalDialogURLNbrPageMotivation%>nbrPage=" + input.value, null, "dialogHeight:200px; help:no; status:no; resizable:no; scroll:no");
			if (value != null) {
				input.value = value;
			}
		}
	}

	function majGenrePrononce(input) {
		if (window.showModalDialog) {
			var value = window.showModalDialog("<%=showModalDialogURLGenrePrononce%>csGenrePrononce=" + input.value, null, "dialogHeight:200px; help:no; status:no; resizable:no; scroll:no");
			if (value != null) {
				input.value = value;
			}
		}
	}
	
	
	
	function majGestionnaire(input) {
		if (window.showModalDialog) {
			var value = window.showModalDialog("<%=showModalDialogURLGestionnaire%>idGestionnaire=" + (input.value == '' ? 0 : input.value), null, "dialogHeight:200px; help:no; status:no; resizable:no; scroll:no");
			if (value != null) {
				input.value = value;
			}
		}
	}
	
	$(document).ready(function() {
		$widgetOffice = $("#codeOfficeAiInv");
		$widgetOffice.attr('maxlength', '3');

		$widgetOffice.focus(function() {
			$widgetOffice.trigger(eventConstant.WIDGET_FORCE_LOAD);
		});
		$widgetOffice.keyup(function(event) {
			if ((event.keyCode >= 96 && event.keyCode <= 105)
				|| event.keyCode == 8
				|| (event.keyCode >= 48 && event.keyCode <= 57)) {
				$widgetOffice.trigger(eventConstant.WIDGET_FORCE_LOAD);
			}
		});

		if (document.getElementsByName('_method')[0].value == "add") {
			nssOnKeyUp('likeNSS');
			partiallikeNSSPopupTag.onKeyUp();
		}
			
		$( "#csGenrePrononceAiApi" ).change(function() {

			$( ".showHideNbPageMotivationAPI" ).show();
									
			if('52807002' == this.value){
				
				$( ".showHideNbPageMotivationAPI" ).hide();
			}

			});
		
		$( "#csGenrePrononceAiApi" ).change()	
		
		$( "#csGenreDroitApi" ).change(function() {

			$( ".showHideIsAssistancePratique" ).show();
									
			if('52809003' == this.value || '52809004' == this.value){
				
				$( ".showHideIsAssistancePratique" ).hide();
			}

			});
		
		$( "#csGenreDroitApi" ).change()	
		
		
	});
</script>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<% 
	// on n'affiche pas l'icône du post-it si on créer une nouvelle demande (donc si on a pas encore d'ID de demande)
	if (!JadeStringUtil.isBlank(viewBean.getIdDemandeRente())) {
%>				<span class="postItIcon">
					<ct:FWNote sourceId="<%=viewBean.getIdRequerant()%>" tableSource="<%=globaz.corvus.application.REApplication.KEY_POSTIT_RENTES%>" />
				</span>
<%
	}
%>				<ct:FWLabel key="JSP_SDR_D_TITRE_PAGE" />
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_TYPE_DEMANDE" />
								<input	type="hidden" 
										name="idInfoComplementaire" 
										value="<%=viewBean.getIdInfoComplementaire()%>" />
								<input	type="hidden" 
										name="modifie" 
										value="<%=viewBean.isModifie()%>" />
<% 
	String v = "false";
	if (viewBean.getIsCalculerCopie() != null && viewBean.getIsCalculerCopie().booleanValue()) {
		v = "true";
	}
%>								<input	type="hidden" 
										name="isCalculerCopie" 
										value="<%=v%>" />
								<!-- Champs a usage interne de la jsp uniquement. N'est pas transmit/utilisé dans les actions/viewBean... -->
								<input	type="hidden" 
										name="isEditionPartielle" 
										value="false" />
							</td>
							<td>
								<ct:select name="csTypeDemandeRente" onchange="changeTypeDemandeRente(this)">
									<option value="<%=IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE%>"<%=IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(viewBean.getCsTypeDemandeRente())?"selected":""%>>
										<label>
											Vieillesse
										</label>
									</option>
									<option value="<%=IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE%>"<%=IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(viewBean.getCsTypeDemandeRente())?"selected":""%>>
										<label>
											Invalidité
										</label>
									</option>
									<option value="<%=IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT%>"<%=IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(viewBean.getCsTypeDemandeRente())?"selected":""%>>
										<label>
											Survivant
										</label>
									</option>
									<option value="<%=IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API%>"<%=IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(viewBean.getCsTypeDemandeRente())?"selected":""%>>
										<label>
											API
										</label>
									</option>
								</ct:select>
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_TYPE_CALCUL" />
							</td>
							<td>
								<ct:select name="csTypeCalcul" defaultValue="<%=viewBean.getCsTypeCalcul()%>">
									<ct:optionsCodesSystems csFamille="RETYPCALC">
									</ct:optionsCodesSystems>
								</ct:select>
							</td>
							<td colspan="2">
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
								<ct:FWLabel key="JSP_SDR_D_NSS" />
							</td>
							<td>
<%
	String params = "&provenance1=TIERS" 
					+ "&extraParam1=forceSingleAdrMode";
	String jspLocation = servletContext + "/corvusRoot/numeroSecuriteSocialeSF_select.jsp";
%>								<ct1:nssPopup 	name="likeNSS" 
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
								<input	type="hidden" 
										name="nssRequerant" 
										value="<%=viewBean.getNssRequerant()%>" />
								<input	type="hidden" 
										name="idAssure" 
										value="<%=viewBean.getIdAssure()%>" />
								<input	type="hidden" 
										name="provenance" 
										value="<%=viewBean.getProvenance()%>" />
								<input	type="hidden" 
										name="idTiers" 
										value="<%=viewBean.getIdTiers()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_NOM" />
							</td>
							<td>
								<input	type="hidden" 
										name="nomRequerant" 
										value="<%=viewBean.getNomRequerant()%>" />
								<input	type="text" 
										name="nomRequerantAffiche" 
										value="<%=viewBean.getNomRequerant()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_PRENOM" /></td>
							<td>
								<input	type="hidden" 
										name="prenomRequerant" 
										value="<%=viewBean.getPrenomRequerant()%>" />
								<input	type="text" 
										name="prenomRequerantAffiche" 
										value="<%=viewBean.getPrenomRequerant()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_NAISSANCE" />
							</td>
							<td>
								<input	type="hidden" 
										name="dateNaissanceRequerant" 
										value="<%=viewBean.getDateNaissanceRequerant()%>" />
								<input	type="text" 
										name="dateNaissanceRequerantAffiche" 
										value="<%=viewBean.getDateNaissanceRequerant()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_SEXE" />
							</td>
							<td>
								<ct:select name="csSexeRequerantAffiche" defaultValue="<%=viewBean.getCsSexeRequerant()%>">
									<ct:optionsCodesSystems csFamille="PYSEXE">
									</ct:optionsCodesSystems>
								</ct:select>
								<input	type="hidden" 
										name="csSexeRequerant" 
										value="<%=viewBean.getCsSexeRequerant()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_NATIONALITE" />
							</td>
							<td>
								<ct:FWListSelectTag	name="csNationaliteRequerantAffiche" 
													data="<%=viewBean.getTiPays()%>" 
													defaut="<%=JAUtil.isIntegerEmpty(viewBean.getCsNationaliteRequerant()) ? TIPays.CS_SUISSE : viewBean.getCsNationaliteRequerant()%>" />
								<input	type="hidden" 
										name="csNationaliteRequerant" 
										value="<%=viewBean.getCsNationaliteRequerant()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_CANTON_DOMICILE" />
							</td>
							<td>
								<ct:select	name="csCantonDomicileRequerantAffiche" 
											defaultValue="<%=viewBean.getCsCantonRequerant()%>" 
											wantBlank="true">
									<ct:optionsCodesSystems csFamille="PYCANTON">
									</ct:optionsCodesSystems>
								</ct:select>
								<input	type="hidden" 
										name="csCantonRequerant" 
										value="<%=viewBean.getCsCantonRequerant()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_DECES" />
							</td>
							<td colspan="2">
								<input	type="hidden" 
										name="dateDecesRequerant" 
										value="<%=viewBean.getDateDecesRequerant()%>" />
								<input	type="text" 
										name="dateDecesRequerantAffiche" 
										value="<%=viewBean.getDateDecesRequerant()%>" />
							</td>
							<td>
								<a	href="#" onclick="window.open('<%=servletContext%><%=("/corvus")%>?userAction=<%=IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE%>.actionAfficherDossierGed&amp;noAVSId=<%=viewBean.getNssRequerant()%>&amp;idTiersExtraFolder=<%=viewBean.getIdTiers()%>&amp;serviceNameId=<%=viewBean.getSession().getApplication().getProperty(IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED)%>','GED_CONSULT')">
									<label>
										<ct:FWLabel key="JSP_LIEN_GED"/>
									</label>
								</a>
							</td>
							<td>
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
								<ct:FWLabel key="JSP_SDR_D_DATE_DEPOT" />
							</td>
							<td>
								<input	id="dateDepot"
										name="dateDepot"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateDepot()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_RECEPTION" />
							</td>
							<td>
								<input	id="dateReception"
										name="dateReception"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateReception()%>" />
							</td>
							<td colspan="2">
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
							<%if(viewBean.getSession().hasRight(IREActions.ACTION_SAISIE_DEMANDE_RENTE, FWSecureConstants.UPDATE)) { %>
								<a href="#" onclick="majGestionnaire(document.mainForm.idGestionnaire);">
									<ct:FWLabel key="JSP_SDR_D_GESTIONNAIRE" />
								</a>
							<% } else { %>
								<ct:FWLabel key="JSP_SDR_D_GESTIONNAIRE" />
							<% } %>
							</td>
							<td>
								<ct:FWListSelectTag	name="idGestionnaire" 
													data="<%=viewBean.getResponsableData()%>" 
													defaut="<%=viewBean.getIdGestionnaire()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_ETAT" />
							</td>
							<td>
								<ct:select name="csEtat" defaultValue="<%=viewBean.getCsEtat()%>">
									<ct:optionsCodesSystems csFamille="REETATDEM">
									</ct:optionsCodesSystems>
								</ct:select>
							</td>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="6">
								<hr/>
							</td>
						</tr>
					</tbody>
					<tbody id="demandeVieillesse" style="display:none;">
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_TRAITEMENT" />
							</td>
							<td>
								<input 	id="dateTraitement"
										name="dateTraitement"
										data-g-calendar="type:default" 
										value="<%=viewBean.getDateTraitement()%>" />
							</td>
							<td colspan="4">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_ANNEE_ANTICIPATION" />
							</td>
							<td>
								<ct:select name="csAnneeAnticipationVie" defaultValue="<%=viewBean.getCsAnneeAnticipationVie()%>" wantBlank="true">
									<ct:optionsCodesSystems csFamille="REANNANTI">
									</ct:optionsCodesSystems>
								</ct:select>
							</td>
							<td colspan="4">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_AJOURNEMENT" />
							</td>
							<td>
								<select name="isAjournementRequerantVie">
									<option value="true"<%=viewBean.getIsAjournementRequerantVie().booleanValue()?"selected":""%>>
										<ct:FWLabel key="JSP_SDR_D_OUI" />
									</option>
									<option value="false"<%=!viewBean.getIsAjournementRequerantVie().booleanValue()?"selected":""%>>
										<ct:FWLabel key="JSP_SDR_D_NON" />
									</option>
								</select>
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_REVOCATION" />
							</td>
							<td>
								<input  id="dateRevocationRequerantVie"
										name="dateRevocationRequerantVie"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateRevocationRequerantVie()%>" />
							</td>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_ACCUSE_RECEPTION" />
							</td>
							<td>
								<input type="checkbox" name="isAccuseDeReception" />
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
								<ct:FWLabel key="JSP_SDR_D_CELIBATAIRE_SANS_ENFANT" />
							</td>
							<td>
								<input type="checkbox" name="isCelibataireSansEnfantsVieillesse" />
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								<a href="#" onclick="informationsComplementaires();">
									<ct:FWLabel key="JSP_SDR_D_INFORMATIONS_COMPLEMENTAIRES" />
								</a>
							</td>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
					</tbody>
					<tbody id="demandeAPI" style="display:none;">
						<tr>
							<td>
<%
	if (!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(viewBean.getCsEtat()) 
		&& !IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(viewBean.getCsEtat())) { 
%>								<a href="#" onclick="majGenrePrononce(document.mainForm.csGenrePrononceAiApi);">
									<ct:FWLabel key="JSP_SDR_D_GENRE_PRONONCE_AI" /> 
								</a> 
<%
	} else {
%>								<ct:FWLabel key="JSP_SDR_D_GENRE_PRONONCE_AI" />
<%
	}
%>							</td>
							<td>
								<ct:FWCodeSelectTag	name="csGenrePrononceAiApi" 
													codeType="REGEPRON" 
													wantBlank="false"
													defaut="<%=viewBean.getCsGenrePrononceAiApi()%>" />
							</td>
							
							<td>
								<span class="showHideNbPageMotivationAPI">
<%
	if (!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(viewBean.getCsEtat()) 
		&& !IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(viewBean.getCsEtat())) {
%>								<a href="#" onclick="majNbrPageMotivation(document.mainForm.nbPagesMotivationApi);">
									<ct:FWLabel key="JSP_SDR_D_NB_PAGE_MOTIVATION" />
								</a>
<%
	} else {
%>								<ct:FWLabel key="JSP_SDR_D_NB_PAGE_MOTIVATION" />
<%
	}
%>							</span>
							</td>
							<td colspan="3">
							<span class="showHideNbPageMotivationAPI">
								<input	type="text" 
										size="3" 
										name="nbPagesMotivationApi" 
										value="<%=viewBean.getNbPagesMotivationApi()%>" />
							</span>
							</td>
							
						</tr>
						
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_OFFICE_AI" />
							</td>
							<td colspan="5">
								<ct:select 	name="codeOfficeAiApi" >
<%
	String defaultValue = JadeStringUtil.isIntegerEmpty(viewBean.getCodeOfficeAiApi()) ? viewBean.getNoOfficeAICantonal() : viewBean.getCodeOfficeAiApi();

	for(String codeOfficeAi : officesAi.keySet()) {
%>									<option value="<%=codeOfficeAi%>" <%=codeOfficeAi.equals(defaultValue) ? "selected " : ""%>>
										<%=codeOfficeAi%> - <%=officesAi.get(codeOfficeAi)%>
									</option>
<%
	}
%>								</ct:select>
							</td>
						
						</tr>
						
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_INFIRMITE" />
							</td>
							<td>
								<input	type="text" 
										size="3" 
										maxlength="3" 
										id="codeCsInfirmiteApi" 
										name="codeCsInfirmiteApi" 
										value="<%=viewBean.getCodeCsInfirmiteApi()%>" 
										onkeyup="metAJourCsInfirmiteApi()" />
								<input	type="hidden" 
										id="csInfirmiteApi" 
										name="csInfirmiteApi" 
										value="<%=viewBean.getCsInfirmiteApi()%>" />
								<img	src="" 
										alt="" 
										id="imageOKKOInfirmiteApi" 
										name="imageOKKOInfirmiteApi" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_ATTEINTE" />
							</td>
							<td>
								<input	type="text" 
										size="2"
										maxlength="2" 
										id="codeCsAtteinteApi" 
										name="codeCsAtteinteApi" 
										value="<%=viewBean.getCodeCsAtteinteApi()%>" 
										onkeyup="metAJourCsAtteinteApi()" />
								<input	type="hidden" 
										id="csAtteinteApi" 
										name="csAtteinteApi" 
										value="<%=viewBean.getCsAtteinteApi()%>" />
								<img	src="" 
										alt="" 
										id="imageOKKOAtteinteApi"
										name="imageOKKOAtteinteApi" />
							</td>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						
						<tr>
							<td colspan="6">
								<hr>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_GENRE_DROIT_API" />
							</td>
							<td colspan="5">
								<ct:FWCodeSelectTag	name="csGenreDroitApi" 
													codeType="REGEDRAPI" 
													wantBlank="true" 
													defaut="<%=viewBean.getCsGenreDroitApi()%>" />
							</td>
						</tr>
			
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_DEBUT_IMPOTENCE" />
							</td>
							<td>
								<input	type="hidden" 
										name="idPeriodeAPI" 
										value="<%=viewBean.getIdPeriodeAPI()%>" />
								<input	id="dateDebutImpotence"
										name="dateDebutImpotence"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateDebutImpotence()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_FIN_IMPOTENCE" />
							</td>
							<td>
								<input	id="dateFinImpotence"
										name="dateFinImpotence"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateFinImpotence()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DEGRE_IMPOTENCE" />
							</td>
							<td>
								<ct:FWCodeSelectTag	name="csDegreImpotence" 
													codeType="REDEGIMPO" 
													defaut="<%=viewBean.getCsDegreImpotence()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_RESIDENCE_HOME" />
							</td>
							<td>
								<select name="isResidenceDansHome">
									<option	value="true" <%=viewBean.getIsResidenceDansHome().booleanValue()?"selected":""%>>
										<ct:FWLabel key="JSP_SDR_D_OUI" />
									</option>
									<option	value="false" <%=!viewBean.getIsResidenceDansHome().booleanValue()?"selected":""%>>
										<ct:FWLabel key="JSP_SDR_D_NON" />
									</option>
								</select>
							</td>
							<td>
							<span class="showHideIsAssistancePratique">
								<ct:FWLabel key="JSP_SDR_D_ASSISTANCE_PRATIQUE" />
							</span>
							</td>
							<td>
							<span class="showHideIsAssistancePratique">
								<select name="isAssistancePratique">
									<option value="true" <%=viewBean.getIsAssistancePratique().booleanValue()?"selected":""%>>
										<ct:FWLabel key="JSP_SDR_D_OUI" />
									</option>
									<option value="false"<%=!viewBean.getIsAssistancePratique().booleanValue()?"selected":""%>>
										<ct:FWLabel key="JSP_SDR_D_NON" />
									</option>
								</select>
							</span>
							</td>
							
							<td>
								<ct:FWLabel key="JSP_SDR_D_TYPE_PRESTATION_HISTORIQUE" />
							</td>
							<td>
								<input	type="text" 
										size="4"
										id="typePrestationHistorique" 
										name="typePrestationHistorique" 
										value="<%=viewBean.getTypePrestationHistorique()%>"  />
							</td>
						</tr>
						<tr>
							<td colspan="5">
								&nbsp;
							</td>
							<td>
								<% if(hasRight){ %>
									<input	type="button" 
											name="boutonAjpouterPeriode" 
											value="<ct:FWLabel key="JSP_AJOUTER" />" 
											onclick="addPeriodeAPI()" />
								<% } %>
							</td>
					
						</tr>
						
						<tr>
							<td colspan="4" rowspan="1">
								<iframe	id="periodesFrame" 
										name="periodesFrame" 
										width="100%" 
										height="100"
										src="<%=request.getContextPath()%>/corvus?userAction=corvus.demandes.saisieDemandeRente.actionListerPeriodesAPI&forIdDemandeRente=<%=viewBean.getIdDemandeRente()%>&modifiable=<%=viewBean.isModifiable()%>" >
								</iframe>
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_SURVENANCE_EVENEMENT_ASSURE" />
							</td>
							<td>
								<input	id="dateSurvenanceEvenementAssureAPI"
										name="dateSurvenanceEvenementAssureAPI"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateSurvenanceEvenementAssureAPI()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<a href="#" onclick="informationsComplementaires();">
									<ct:FWLabel key="JSP_SDR_D_INFORMATIONS_COMPLEMENTAIRES" />
								</a>
							</td>
							<td colspan="5">
								&nbsp;
							</td>
						</tr>
					</tbody>
					<tbody id="demandeInvalidite" style="display:none;">
						<tr>
							<td>
<%
	if (!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(viewBean.getCsEtat()) 
		&& !IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(viewBean.getCsEtat()) 
		&& viewBean.getSession().hasRight(IREActions.ACTION_SAISIE_DEMANDE_RENTE, FWSecureConstants.UPDATE)) {
%>								<a href="#" onclick="majGenrePrononce(document.mainForm.csGenrePrononceAiInv);">
									<ct:FWLabel key="JSP_SDR_D_GENRE_PRONONCE_AI" />
								</a>
<%
	} else {
%>								<ct:FWLabel key="JSP_SDR_D_GENRE_PRONONCE_AI" />
<%
	}
%>							</td>
							<td>
								<ct:FWCodeSelectTag	name="csGenrePrononceAiInv" 
													codeType="REGEPRON" 
													wantBlank="false" 
													defaut="<%=viewBean.getCsGenrePrononceAiInv()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_OFFICE_AI" />
							</td>
							<td colspan="3">
								<ct:select 	name="codeOfficeAiInv" > 
<%
	String defaultValue = JadeStringUtil.isIntegerEmpty(viewBean.getCodeOfficeAiInv()) ? viewBean.getNoOfficeAICantonal() : viewBean.getCodeOfficeAiInv();

	for (String codeOfficeAi : officesAi.keySet()) {
%>									<option value="<%=codeOfficeAi%>" <%=codeOfficeAi.equals(defaultValue)?"selected ":""%>>
										<%=codeOfficeAi%> - <%=officesAi.get(codeOfficeAi)%>
									</option>
<%
	}
%>								</ct:select>
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_INFIRMITE" />
							</td>
							<td>
								<input	type="text"
										size="3" 
										maxlength="3" 
										id="codeCsInfirmiteInv"
										name="codeCsInfirmiteInv" 
										value="<%=viewBean.getCodeCsInfirmiteInv()%>" 
										onkeyup="metAJourCsInfirmiteInv()" />
								<input	type="hidden" 
										id="csInfirmiteInv"
										name="csInfirmiteInv" 
										value="<%=viewBean.getCsInfirmiteInv()%>" />
								<img 	src="" 
										alt="" 
										id="imageOKKOInfirmiteInv"
										name="imageOKKOInfirmiteInv" >
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_ATTEINTE" />
							</td>
							<td>
								<input 	type="text" 
										size="2"
										maxlength="2" 
										id="codeCsAtteinteInv"
										name="codeCsAtteinteInv" 
										value="<%=viewBean.getCodeCsAtteinteInv()%>" 
										onkeyup="metAJourCsAtteinteInv()" />
								<input 	type="hidden" 
										id="csAtteinteInv"
										name="csAtteinteInv" 
										value="<%=viewBean.getCsAtteinteInv()%>" />
								<img 	src="" 
										alt="" 
										id="imageOKKOAtteinteInv"
										name="imageOKKOAtteinteInv" >
							</td>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
<%	if (!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(viewBean.getCsEtat()) 
		&& !IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(viewBean.getCsEtat()) 
		&& viewBean.getSession().hasRight(IREActions.ACTION_SAISIE_DEMANDE_RENTE, FWSecureConstants.UPDATE)) {
%>								<a href="#" onclick="majNbrPageMotivation(document.mainForm.nbPagesMotivationInv);">
									<ct:FWLabel key="JSP_SDR_D_NB_PAGE_MOTIVATION" />
								</a>
<%
	} else {
%>								<ct:FWLabel key="JSP_SDR_D_NB_PAGE_MOTIVATION" />
<%
	}
%>							</td>
							<td colspan="5">
								<input 	type="text" 
										size="3" 
										name="nbPagesMotivationInv" 
										value="<%=viewBean.getNbPagesMotivationInv()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								<hr/>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_DEBUT_INVALIDITE" />
							</td>
							<td>
								<input	type="hidden" 
										name="idPeriodeInvalidite" 
										value="<%=viewBean.getIdPeriodeInvalidite()%>" />
								<input	id="dateDebutInvalidite"
										name="dateDebutInvalidite"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateDebutInvalidite()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_FIN_INVALIDITE" />
							</td>
							<td>
								<input	id="dateFinInvalidite"
										name="dateFinInvalidite"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateFinInvalidite()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DEGRE_INVALIDITE" />
							</td>
							<td>
								<input 	type="text" 
										name="degreInvalidite" 
										value="<%=viewBean.getDegreInvalidite()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="5">
								&nbsp;
							</td>
							<td>
								<% if(hasRight){ %>
								<input 	type="button" 
										name="addPeriodeInvButton" 
										value="<ct:FWLabel key="JSP_AJOUTER" />" 
										onclick="addPeriodeInv()" />
										<% } %>
							</td>
						</tr>
						<tr>
							<td colspan="4" rowspan="1">
								<iframe	id="periodesFrame" 
										name="periodesFrame" 
										width="100%" 
										height="100"
										src="<%=request.getContextPath()%>/corvus?userAction=corvus.demandes.saisieDemandeRente.actionListerPeriodesINV&forIdDemandeRente=<%=viewBean.getIdDemandeRente()%>&modifiable=<%=viewBean.isModifiable()%>">
								</iframe>
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_SURVENANCE_EVENEMENT_ASSURE" />
							</td>
							<td>
								<input	id="dateSurvenanceEvenementAssureInv"
										name="dateSurvenanceEvenementAssureInv"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateSurvenanceEvenementAssureInv()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<ct:FWLabel key="JSP_SDR_D_RED_FAUTE_GRAVE" />
							</td>
							<td>
								<input	type="text" 
										size="5" 
										style="text-align:right;" 
										name="pourcentRedFauteGrave" 
										value="<%=viewBean.getPourcentRedFauteGrave()%>" />
								%
							</td>
							<td colspan="3">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<ct:FWLabel key="JSP_SDR_D_RED_NON_COLLABO" />
							</td>
							<td>
								<input	type="text" 
										size="5" 
										style="text-align:right;" 
										name="pourcentRedNonCollaboration" 
										value="<%=viewBean.getPourcentRedNonCollaboration()%>" />
								%
							</td>
							<td colspan="3">
								<ct:FWLabel key="JSP_SDR_D_DU" />
								&nbsp;
								<input	id="dateDebutRedNonCollaboration"
										name="dateDebutRedNonCollaboration"
										data-g-calendar="type:month"
										value="<%=viewBean.getDateDebutRedNonCollaboration()%>" />
								<ct:FWLabel key="JSP_SDR_D_FORMAT" />
								&nbsp;
								<ct:FWLabel key="JSP_SDR_D_AU" />
								&nbsp;
								<input	id="dateFinRedNonCollaboration"
										name="dateFinRedNonCollaboration"
										data-g-calendar="type:month"
										value="<%=viewBean.getDateFinRedNonCollaboration()%>" />
								<ct:FWLabel key="JSP_SDR_D_FORMAT" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_CELIBATAIRE_SANS_ENFANT" />
							</td>
							<td>
								<input type="checkbox" name="isCelibataireSansEnfantsInv" />
							</td>
							<td>
								<a href="#" onclick="informationsComplementaires();">
									<ct:FWLabel key="JSP_SDR_D_INFORMATIONS_COMPLEMENTAIRES" />
								</a>
								<input	type="hidden" 
										name="idInfoCompl" 
										value="<%=viewBean.getIdInfoComplementaire()%>" />
								<input	type="hidden" 
										name="idTiers" 
										value="<%=viewBean.getIdTiers()%>" />
								<input	type="hidden" 
										name="idDemandeRente" 
										value="<%=viewBean.getIdDemandeRente()%>" />
							</td>
							<td colspan="3">
								&nbsp;
							</td>
						</tr>
					</tbody>
					<tbody id="demandeSurvivant" style="display:none;">
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SRD_D_TIERS_RESPONSABLE" />
							</td>
							<td>
								<ct:select	name="infoComplCsTiersResponsable" 
											defaultValue="<%=viewBean.getInfoComplCsTiersResponsable()%>" 
											wantBlank="true">
									<ct:optionsCodesSystems csFamille="<%=IPRInfoCompl.CS_GROUPE_TIERS_RESPONSABLE%>">
									</ct:optionsCodesSystems>
								</ct:select>
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								<a href="#" onclick="informationsComplementaires();">
									<ct:FWLabel key="JSP_SDR_D_INFORMATIONS_COMPLEMENTAIRES" />
								</a>
							</td>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<% if (isSupprimable 
		&& viewBean.getSession().hasRight(IREActions.ACTION_SAISIE_DEMANDE_RENTE, FWSecureConstants.UPDATE)) {
%>					<input	type="button" 
							value="<ct:FWLabel key="JSP_SUPPRIMER" />" 
							onclick="delet();" />
<%
	} 

	if (viewBean.getSession().hasRight(IREActions.ACTION_SAISIE_DEMANDE_RENTE, FWSecureConstants.UPDATE)) {
%>					<input	type="button" 
							value="<ct:FWLabel key="JSP_SUIVANT" /> (alt+<ct:FWLabel key="AK_REQ_SUIVANT" />)" 
							onclick="if(validate()) action(COMMIT);" 
							accesskey="<ct:FWLabel key="AK_REQ_SUIVANT" />" />
<%
	}
	
	if (viewBean.getSession().hasRight(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".arreterSaisieDemandeRente", FWSecureConstants.UPDATE)) {
%>					<input	type="button" 
							value="<ct:FWLabel key="JSP_ARRET" /> (alt+<ct:FWLabel key="AK_REQ_ARRET" />)" 
							onclick="arret()" 
							accesskey="<ct:FWLabel key="AK_REQ_ARRET" />" />
<%
	}
	
	if (viewBean.getIsCalculerCopie() != null 
		&& viewBean.getIsCalculerCopie().booleanValue() 
		&& viewBean.getSession().hasRight(IREActions.ACTION_SAISIE_DEMANDE_RENTE, FWSecureConstants.UPDATE)) {
%>					<input	type="button" 
							value="<ct:FWLabel key="JSP_CALCULER" /> (alt+<ct:FWLabel key="AK_CALCULER" />)" 
							onclick="calculerCopie()" 
							accesskey="<ct:FWLabel key="AK_CALCULER" />" />
<%
	}
%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%@ include file="/theme/detail/footer.jspf" %>
