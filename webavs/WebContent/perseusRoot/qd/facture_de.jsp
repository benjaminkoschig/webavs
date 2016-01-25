<%@page import="ch.globaz.perseus.business.models.qd.CSTypeQD"%>
<%@page import="ch.globaz.perseus.businessimpl.services.models.dossier.DossierServiceImpl"%>
<%@page import="ch.globaz.perseus.business.models.informationfacture.InformationFacture"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@page
	import="ch.globaz.perseus.business.services.models.variablemetier.VariableMetierService"%>
<%@page
	import="ch.globaz.perseus.business.models.variablemetier.VariableMetier"%>
<%@page import="ch.globaz.perseus.business.constantes.CSVariableMetier"%>
<%@page import="ch.globaz.pyxis.business.model.AdresseTiersDetail"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.prestation.interfaces.tiers.PRTiersHelper"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page
	import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page
	import="ch.globaz.perseus.business.services.models.dossier.DossierService"%>
<%@page
	import="ch.globaz.perseus.business.services.models.decision.DecisionService"%>
<%@page
	import="ch.globaz.perseus.business.services.models.qd.FactureService"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="globaz.perseus.vb.qd.PFFactureViewBean"%>
<%@ page language="java" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail_ajax_hybride/header.jspf"%>
<%-- tpl:insert attribute="zoneInit" --%>
<%
    PFFactureViewBean viewBean = (PFFactureViewBean) session.getAttribute("viewBean");
	autoShowErrorPopup = true;
	idEcran = "PPF1121";
	String btnValidNewLabel = "";
	boolean bBtnValidNew = true;
	boolean bBtnValidate = true;
	boolean bBtnCancel = true;
	boolean isFactureExistante = !(viewBean.getFacture().getQd().getTypeQD() == null);
	boolean isQdInitialFraisDeGarde = false;

	String csTypeQdInitial = "";
	String csFraisDeGarde = CSTypeQD.FRAIS_GARDE.getCodeSystem();

	String idAdressePaiementDefault = "";
	String idApplicationPaiementDefault = "";

	String idAdresseCourrierDefault = "";
	String idApplicationCourrierDefault = "";

	if (isFactureExistante) {
		csTypeQdInitial = viewBean.getFacture().getQd().getTypeQD().getCodeSystem();
	}
	btnValidNewLabel = objSession
			.getLabel("JSP_PF_DECISION_A_BOUTON_PREVALIDER_ET_NOUVELLE");

	btnValLabel = objSession
			.getLabel("JSP_PF_DECISION_A_BOUTON_PREVALIDER");

	PersonneEtendueComplexModel personne = viewBean.getDossier()
			.getDemandePrestation().getPersonneEtendue();
	String affichePersonnne = "";

	String idDossier = request.getParameter("idDossier");

	if (idDossier == null) {
		idDossier = viewBean.getDossier().getId();
	}

	affichePersonnne = PFUserHelper.getDetailAssure(objSession,
			personne);

	if (objSession
			.hasRight("perseus.qd.facture", FWSecureConstants.ADD)) {
		bButtonUpdate = true;
		bButtonDelete = true;
		bBtnValidate = true;
	} else {
		bButtonUpdate = false;
		bButtonDelete = false;
		bBtnCancel = false;
		bBtnValidate = false;
	}
	bButtonValidate = false;
	bButtonCancel = false;
%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/javascripts.jspf"%>
<%@ include file="/perseusRoot/ajax/javascriptsAndCSS.jspf"%>
<%@ include file="/perseusRoot/scripts/qd/checkerFacture.jsp"%>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript" src="<%=rootPath%>/scripts/jadeBaseFormulaire.js" /></script>

<script language="JavaScript">
	var url = "perseus.qd.facture";
	var urlAnnuler = "perseus.dossier.dossier";
	var $membreFamilleTr, $libelleTr, $infosQDTr, $montantTr, $montantRembourseTr, $motifTr, $paiementTr, $factureTr;
	var $nbFactures, $nbMinutes, $infosHygienisteTr, $infosFacture, $infosFactureSaisir;
	var $minutesHygienisteTr, $hygienisteDentaireTr;
	var $casDeRigueurTr;
	var $dateFacture, $datePriseEnCharge, $dateReception, $typeQD, $membreFamille, $montant, $montantRembourse, $motif, $idQD, $csTypeQdParenteInitial, $excedantRevenuPrisEnCompte, $motifLibre, $motifCs;
	var idDossier = "<%=idDossier%>";
	var listQD = new Array();
	var isAdressePaiementRenseigne = true;
	var isAdresseCourrierRenseigne = true;
	var typeQdSelectionnee = "";
	var $isModifierFacture,	$isAnneeQdDepart, $anneQdDepart, $anneeQdSeletionne;
	
	globazGlobal.label = {};
	globazGlobal.label.JSP_PF_FAC_AVERTISSEMENT_FACTURE_SIMILAIRE = <%=viewBean
					.getLabel("JSP_PF_FAC_AVERTISSEMENT_FACTURE_SIMILAIRE")%>;
	globazGlobal.label.JSP_PF_FAC_MONTANT_REMBOURSE_DIFFERENT_FACTURE = <%=viewBean
					.getLabel("JSP_PF_FAC_MONTANT_REMBOURSE_DIFFERENT_FACTURE")%>;
	globazGlobal.label.JSP_PF_FAC_D_CAC_OK_MIN_KO = <%=viewBean.getLabel("JSP_PF_FAC_D_CAC_OK_MIN_KO")%>;
	globazGlobal.label.JSP_PF_MANDATORY_INFORMATIONS_FACTURE = <%=viewBean
					.getLabel("JSP_PF_MANDATORY_INFORMATIONS_FACTURE")%>;
	globazGlobal.label.JSP_PF_MANDATORY_MISSING_INFORMATIONS = <%=viewBean
					.getLabel("JSP_PF_MANDATORY_MISSING_INFORMATIONS")%>;
	
	$(function() {
		//désactivation des boutons standards
		$membreFamilleTr = $("#membreFamilleTr"); 
		$membreFamilleTr.hide();
		$libelleTr = $("#libelleTr");
		$infosQDTr = $("#infosQDTr");
		$montantTr = $("#montantTr");
		$hygienisteDentaireTr = $("#hygienisteDentaireTr");
		$minutesHygienisteTr = $("#minutesHygienisteTr");
		$montantRembourseTr = $("#montantRembourseTr");
		$motifTr = $("#motifTr");
		$casDeRigueurTr = $("#casDeRigueurTr");
		$nbFactures = $("#nbFactures");
		$nbMinutes = $("#nbMinutes");
		$paiementTr = $("#paiementTr");
		$factureTr = $("#factureTr");
		$infosHygienisteTr = $("#infosHygienisteTr");
		$infosFacture = $("#infosFacture");
		$infosFactureSaisir = $("#infosFactureSaisir");
		//récupération des éléments
		$dateFacture = $("#dateFacture");
		$datePriseEnCharge = $("#datePriseEnCharge");
		$dateReception = $("#dateReception");
		$typeQD = $('#facture\\.qd\\.simpleQD\\.csType');
		$membreFamille = $("#membreFamille");
		$montant = $("#montant");
		$montantRembourse = $("#montantRembourse");
		$excedantRevenuPrisEnCompte = $("#excedantRevenuPrisEnCompte");
		$idQD = $("#idQD");
		$ancienMontantFacture = $("#ancienMontantFacture");
		$ancienMontantARembourserFacture = $("#ancienMontantARembourserFacture");
		$dateFacturePourTypeDecision = $("#dateFacturePourTypeDecision");
		$informationSurLaDecision = $("#informationSurLaDecision");
		$informationSurLaDecision.fadeOut(0);		
		$isModifierFacture = false;
		$isAnneeQdDepart = true;
		hasBeenChangedOnce = false;
		$montantFactureInitial = "0";
		$qdParenteUtilise = $("#qdParenteUtilise");
		$qdParenteLimite = $("#qdParenteLimite");
		
		$('#idAdresseCourrier').change(function(){
			isAdresseCourrierRenseigne = true;
		});
		
		$('#idTiersAdressePaiement').change(function(){
			isAdressePaiementRenseigne = true;
		});
				
		if(<%=JadeStringUtil.isEmpty(viewBean
					.getAdressePaiementAssure().getAdresseFormate())%>){
			isAdressePaiementRenseigne = false;
		}
		
		if(<%=JadeStringUtil.isEmpty(viewBean
					.getAdresseCourrierAssure().getAdresseFormate())%>){
			isAdresseCourrierRenseigne = false;
		}
				
		if($idQD.val() !== ""){
			$isModifierFacture = true;
			$montantFactureInitial = <%=viewBean.getFacture().getSimpleFacture().getMontant()%>;
			$("#btnUpd").fadeOut(0);
			$("#btnDel").fadeOut(0);
			$("#btnNew").fadeOut(0);
			$("#btnVal").fadeIn(0);
			<%bButtonUpdate = true;
			//bButtonValidate = true;
			bBtnValidate = true;
			bButtonNew = true;%>
			$("#modificationFacture").val("true");
			$ancienMontantFacture.val($montant.val());
			$ancienMontantARembourserFacture.val($montantRembourse.val());
			$("#ancienTypeQdParente").val()
		}
		
		$motifLibre = $("#motifLibre");
		$motifCs = $("#motifCs");
		
		<%if (!"_fail".equals(request.getParameter("valid"))) {%>
			$libelleTr.hide();
			$infosQDTr.hide();
			$infosHygienisteTr.hide();
			$infosFacture.hide();
			$infosFactureSaisir.hide();
			$minutesHygienisteTr.hide();
			$hygienisteDentaireTr.hide();
			$casDeRigueurTr.hide();
			$montantTr.hide();
			$motifTr.hide();
			$montantRembourseTr.hide();
			$paiementTr.hide();
			$factureTr.hide();
		<%} else {%>
			changeDateOrTypeFacture();
			changeMembreFamille();
		<%}%>
		changeMotif();
		
		//Déclaration des événements pour les méthodes
		$dateFacture.change(changeDateOrTypeFacture);
		$dateReception.change(changeDateOrTypeFacture);
		$datePriseEnCharge.change(changeDateOrTypeFacture);
		$typeQD.bind('change',changeDateOrTypeFacture);
		$datePriseEnCharge.change(changeDateOrTypeFacture);
		$membreFamille.change(changeMembreFamille);
		$montant.change(changeMontant);
		$motifCs.change(changeMotif);
		
		$( "#dialog:ui-dialog" ).dialog( "destroy" );
		$( "#dialog-confirm" ).hide();
		if($isModifierFacture){
			changeDateOrTypeFacture();
		}
		
		if(<%=viewBean.getFacture().getSimpleFacture()
					.getAcceptationForcee()%>){
			$("#forcerAcceptation").prop('checked', true);
		}
		if(<%=!JadeStringUtil.isEmpty(viewBean.getFacture()
					.getSimpleFacture().getMotifLibre())%>){
			$("#motifLibre").val("<%=viewBean.getFacture().getSimpleFacture().getMotifLibre()%>");
		}
		if(<%=viewBean.getFacture().getSimpleFacture()
					.getHygienisteDentaire()%>){
			$("#cbHygieniste").prop('checked', true);
			printMinutes();
			$("#txtMinutes").val('<%=viewBean.getFacture().getSimpleFacture()
					.getMinutesHygieniste()%>');
		}
		if(<%=viewBean.getFacture().getSimpleFacture()
					.getCasDeRigueur()%>){
			$('#cbCasDeRigueur').prop('checked', true);
		}
	});
	
	function printMinutes(){
		if($("#cbHygieniste").attr('checked')) {
		    $minutesHygienisteTr.show();
		} else {
			$("#txtMinutes").val("");
		}
	}
	
	//Fonction de sortie de la date ou du type de facture
	function changeDateOrTypeFacture() {
		
		var isDatesValid = true;
		
		if($("#dateReception").val().length > 0 && !globazNotation.utilsDate._isValidGlobazDate($("#dateReception").val())){
			isDatesValid = false;
		}

		if($("#dateFacture").val().length > 0 && !globazNotation.utilsDate._isValidGlobazDate($("#dateFacture").val())){
			isDatesValid = false;
		} 

		if($("#datePriseEnCharge").val().length > 0 && !globazNotation.utilsDate._isValidGlobazDate($("#datePriseEnCharge").val())){
			isDatesValid = false;
		}
				
		if (isDatesValid  && $typeQD.val() != "") {
			
			var datePrise = " ";
			if(null != $datePriseEnCharge.val() && $datePriseEnCharge.val() != ""){
				datePrise = $datePriseEnCharge.val();
			}
			
			
			var options = {
					serviceClassName: 'ch.globaz.perseus.business.services.models.dossier.DossierService',
					serviceMethodName: 'getListQDSearchByParameters',
					parametres: idDossier+","+$dateFacture.val()+","+$dateReception.val()+","+$typeQD.val()+","+datePrise,
					criterias: '',
					cstCriterias: '',
					callBack: function (data) {
						//Pour chaque élément LI
						var nbOptions = 0;
						$membreFamille[0].options.length = nbOptions + 1;
						$membreFamille[0].options[nbOptions].value = "";
						$membreFamille[0].options[nbOptions].text = "";
						
						for (var i = 0; i < data.length; i++) {
							//Si il est de class widgetSuggestion
							var qd = data[i];
								nbOptions++;
								var idMf = qd.membreFamille.simpleMembreFamille.idMembreFamille;
								$membreFamille[0].options.length = nbOptions + 1;
								$membreFamille[0].options[nbOptions].value = idMf;
								$membreFamille[0].options[nbOptions].text = qd.membreFamille.personneEtendue.personneEtendue.numAvsActuel + " " + qd.membreFamille.personneEtendue.tiers.designation1 + " " + qd.membreFamille.personneEtendue.tiers.designation2;
								listQD.length++;
								listQD[idMf] = new Object();
								listQD[idMf].idQD = qd.simpleQD.idQD;
								listQD[idMf].excedantRevenu = qd.qdAnnuelle.simpleQDAnnuelle.excedantRevenu;
								listQD[idMf].excedantRevenuCompense = qd.qdAnnuelle.simpleQDAnnuelle.excedantRevenuCompense;
								listQD[idMf].montantLimite = qd.montantLimite;
								listQD[idMf].montantUtilise = qd.montantUtilise;
								listQD[idMf].montantMaximalRemboursable = qd.montantMaximalRemboursable;
								listQD[idMf].montantUtiliseQdParente = qd.montantUtiliseQdParente;
								listQD[idMf].montantLimiteQdParente = qd.montantLimiteQdParente;
								listQD[idMf].isAi = qd.membreFamille.simpleMembreFamille.isAI;
								if($isAnneeQdDepart){
									$isAnneeQdDepart = false;
									$anneQdDepart = qd.qdAnnuelle.simpleQDAnnuelle.annee;
									$anneeQdSeletionne = qd.qdAnnuelle.simpleQDAnnuelle.annee;
									
								}else{
									$anneeQdSeletionne = qd.qdAnnuelle.simpleQDAnnuelle.annee;
									$csTypeQdParenteInitial = qd.qdParente.simpleQD.csType;
								}
								
							}
						if($isModifierFacture){
							if($anneQdDepart === $anneeQdSeletionne){
								$membreFamille.val('<%=viewBean.getFacture().getQd().getMembreFamille()
					.getSimpleMembreFamille().getIdMembreFamille()%>');	
							}
							
							changeMembreFamille();
						}

					}
			};
			ajaxRead(options);
			
			$membreFamilleTr.show();
			changeMontant();
		} else {
			$membreFamilleTr.hide();
			$membreFamille.val("");
		}
		
		if ($dateFacture.val() != "" || $datePriseEnCharge.val() != "") {
			if(isDatesValid) {
				var dateAControler = "";
				if($datePriseEnCharge.val() !== ""){
					dateAControler = $datePriseEnCharge.val();
				}else{
					dateAControler = $dateFacture.val();
				}
				
				var optionsPourTypeDecision = {
						serviceClassName: 'ch.globaz.perseus.business.services.models.decision.DecisionService',
						serviceMethodName: 'getInformationSurLaDecision',
						parametres: idDossier+","+dateAControler,
						criterias: '',
						cstCriterias: '',
						callBack: function (data) {
							$("#typeDecision").text(data[2]);
							$("#dateFacturePourTypeDecision").text(data[0] + " - " + data[1]);
							$informationSurLaDecision.show();
						}
				};			
				ajaxRead(optionsPourTypeDecision);
				changeMontant();
			}
		}
	}
	
	
	function ajaxRead (o_options){
		var ajax = Object.create($.extend(true, {}, globazNotation.readwidget));
		ajax.options = o_options;
		ajax.read();
	}
	
	function remplirInfosQD(qd) {
		
		var typeQdSelectionnee = $("select[name='facture.qd.simpleQD.csType'] option:selected").val();
		var csFraisDeGarde = "<%=csFraisDeGarde%>";
		
		if(typeQdSelectionnee === csFraisDeGarde){
			$("#infosQDParente").hide();
		} else {
			$("#infosQDParente").show()
		}
		
		var montantUtiliseQdParente = Number(qd.montantUtiliseQdParente);
		var montantLimiteQdParente = Number(qd.montantLimiteQdParente);
		var montantLimite = Number(qd.montantLimite);
		var montantUtilise = Number(qd.montantUtilise);
		var excedantRevenuCompense = Number(qd.excedantRevenuCompense);
		var montantMaximalRemboursable = Number(qd.montantMaximalRemboursable);
		var montantutiliseDansLaFactureAModifier = <%=viewBean.getFacture().getSimpleFacture().getMontantRembourse()%>;
		var excedantDeRevenuDansLaFactureAModifier = <%=viewBean.getFacture().getSimpleFacture().getExcedantRevenuCompense()%>;
		
		var csTypeQdInitial = "<%=csTypeQdInitial%>";
		 
		if($membreFamille.val() == '<%=viewBean.getFacture().getQd().getMembreFamille().getSimpleMembreFamille().getIdMembreFamille()%>'){
			$isMemeMembreFamille = true;	
		}else{
			$isMemeMembreFamille = false;
		}
		
		if($anneQdDepart === $anneeQdSeletionne && typeQdSelectionnee === csTypeQdInitial) {
			$isMemeQd = true;
		} else {
			$isMemeQd = false;
		}
		
		if($isModifierFacture){
			if($isMemeMembreFamille && $isMemeQd){
				if(montantUtilise > 0){
					$('#qdUtilise').text(globazNotation.utilsFormatter.formatStringToAmout(montantUtilise - montantutiliseDansLaFactureAModifier));	
				} else {
					$('#qdUtilise').text(globazNotation.utilsFormatter.formatStringToAmout(montantUtilise));	
				}
				
				if(montantUtiliseQdParente > 0){
					$("#qdParenteUtilise").text(globazNotation.utilsFormatter.formatStringToAmout(montantUtiliseQdParente - montantutiliseDansLaFactureAModifier));
				} else {
					$("#qdParenteUtilise").text(globazNotation.utilsFormatter.formatStringToAmout(montantUtiliseQdParente));
				}
				
				$('#montantFactureMax').text(globazNotation.utilsFormatter.formatStringToAmout(montantMaximalRemboursable + montantutiliseDansLaFactureAModifier));
			
			} else {
				$('#qdUtilise').text(globazNotation.utilsFormatter.formatStringToAmout(montantUtilise));	
				$('#montantFactureMax').text(globazNotation.utilsFormatter.formatStringToAmout(montantMaximalRemboursable));
				$("#qdParenteUtilise").text(globazNotation.utilsFormatter.formatStringToAmout(montantUtiliseQdParente));
			}
			
			$('#excedantRevenuCompense').text(globazNotation.utilsFormatter.formatStringToAmout(excedantRevenuCompense - excedantDeRevenuDansLaFactureAModifier));
		} else {
			$('#qdUtilise').text(globazNotation.utilsFormatter.formatStringToAmout(montantUtilise));
			$('#montantFactureMax').text(globazNotation.utilsFormatter.formatStringToAmout(montantMaximalRemboursable));
			$('#excedantRevenuCompense').text(globazNotation.utilsFormatter.formatStringToAmout(excedantRevenuCompense));
			$("#qdParenteUtilise").text(globazNotation.utilsFormatter.formatStringToAmout(montantUtiliseQdParente));
		}
		
		$('#excedantRevenu').text(globazNotation.utilsFormatter.formatStringToAmout(qd.excedantRevenu));
		$('#qdLimite').text(globazNotation.utilsFormatter.formatStringToAmout(montantLimite));
		$("#qdParenteLimite").text(globazNotation.utilsFormatter.formatStringToAmout(montantLimiteQdParente));
	}
	
	function changeMembreFamille() {
		if ($membreFamille.val() != "") {
			$idQD.val(listQD[$membreFamille.val()].idQD);
			remplirInfosQD(listQD[$membreFamille.val()]);
			
			$infosQDTr.show();
			$infosHygienisteTr.show();
			$infosFacture.show();
			$infosFactureSaisir.show();
			$hygienisteDentaireTr.show();
			$libelleTr.show();
			$montantTr.show();
			$montantRembourseTr.show();
			$motifTr.show();
			$casDeRigueurTr.show();
			$paiementTr.show();
			$factureTr.show();
			
			if (listQD[$membreFamille.val()].isAi === "true") {
				alert("Attention, cet enfant est à l'AI/PC !");
			}
			
			var datePrise = " ";
			if(null != $datePriseEnCharge.val() && $datePriseEnCharge.val() != ""){
				datePrise = $datePriseEnCharge.val();
			}

			var optionsHygieniste = {
				serviceClassName: "ch.globaz.perseus.business.services.models.qd.FactureService",
				serviceMethodName: "getInformationsAboutHygienisteDentaire",
				parametres: datePrise+','+$dateFacture.val()+','+$membreFamille.val(),
				criterias: '',
				cstCriterias: '',
				callBack: function (data) {
					$nbFactures.text(data.nbFactures);
					$nbMinutes.text(data.nbMinutes);
				},
				errorCallBack: null
			};
			ajaxRead(optionsHygieniste);
			
			printMinutes();
			changeMontant();
			
		} else {
			$idQD.val();
			$infosQDTr.hide();
			$libelleTr.hide();
			$infosHygienisteTr.hide();
			$infosFacture.hide();
			$infosFactureSaisir.hide();
			$minutesHygienisteTr.hide();
			$hygienisteDentaireTr.hide();
			$montantTr.hide();
			$casDeRigueurTr.hide();
			$montantRembourseTr.hide();
			$motifTr.hide();
			$paiementTr.hide();
			$factureTr.hide();
		}
	}
	
	function changeMontant() {
		
		if($montantFactureInitial != $montant.val()){
			hasBeenChangedOnce = true;
		}
		
		if(hasBeenChangedOnce){
			var montantMax = globazNotation.utilsFormatter.amountTofloat($('#montantFactureMax').text());
			if (globazNotation.utilsFormatter.amountTofloat($montant.val().replace("'", "")) < montantMax) {
				$montantRembourse.val(globazNotation.utilsFormatter.formatStringToAmout($montant.val()));
			} else if(montantMax > 0){
				$montantRembourse.val($('#montantFactureMax').text());
			}
			//Calcul de l'excedant à remboursé
			var excedantArembourser = globazNotation.utilsFormatter.amountTofloat($('#excedantRevenu').text()) - globazNotation.utilsFormatter.amountTofloat($('#excedantRevenuCompense').text());
			//Si un ancien excédant avait été remboursé
			if (excedantArembourser <= 0) {
				$excedantRevenuPrisEnCompte.val("0");
			} else {
				if (globazNotation.utilsFormatter.amountTofloat($montantRembourse.val().replace("'", "")) < excedantArembourser) {
					$excedantRevenuPrisEnCompte.val($montantRembourse.val());
				} else {
					$excedantRevenuPrisEnCompte.val(globazNotation.utilsFormatter.formatStringToAmout(excedantArembourser));
				}
			}
			var montantRemb = globazNotation.utilsFormatter.amountTofloat($montantRembourse.val().replace("'", ""));
			var excedantPris = globazNotation.utilsFormatter.amountTofloat($excedantRevenuPrisEnCompte.val().replace("'", ""));
			$montantRembourse.val(globazNotation.utilsFormatter.formatStringToAmout(montantRemb - excedantPris));
		}
	}
	
	function changeMotif() {
		
		if ($motifCs.val() === "<%=IPFConstantes.CS_MOTIF_FACTURE_AUTRE%>") {
			$motifLibre.show();
		} else {
			$motifLibre.hide();
			$motifLibre.val("");
		}
	}

	function annuler() {
		$mainForm.find('[name=userAction]').val(urlAnnuler + ".chercher");
	}

	/*
		Permet de mettre à disable/enable tous les boutons de la page en un seul appel
	 */
	function disableButtons(areDisabled) {
		$("#btnVal").prop("disabled", areDisabled);
		$("#btnCancel").prop("disabled", areDisabled);
	}

	function supprimerSeparateur() {
		$montant.val(globazNotation.utilsFormatter
				.amountTofloat($montant.val()));
		$montantRembourse.val(globazNotation.utilsFormatter
				.amountTofloat($montantRembourse.val()));
		$excedantRevenuPrisEnCompte.val(globazNotation.utilsFormatter
				.amountTofloat($excedantRevenuPrisEnCompte.val()));

		$ancienMontantFacture.val(globazNotation.utilsFormatter
				.amountTofloat($ancienMontantFacture.val()));
		$ancienMontantARembourserFacture.val(globazNotation.utilsFormatter
				.amountTofloat($ancienMontantARembourserFacture.val()));
	}
	/*
	S140507_002
	appel valider mais modifie la valeur de l'input hidden passé dans la request qui sera detecté à 
	l'action pour determiner l'action (redirect) au succes de l'ajout d'une facture
	*/
	function validateAndNew() {
		document.getElementById('PFQdRenew').value = 'true';
		return validate();
	}
	
	/*
	 Attention, le comportement par défaut du framework est court-circuité ici avec la méthode validate() qui
	 retourne toujours false de manière à ce que l'appel commit() conditionné par cette dernière ne soit 
	 jamais exécuté. Dû à la nature asynchrone de l'implémentation de la méthode validate() ci-dessous, les
	 appels commit() sont directement faits dans le callback de l'appel ajax. 
	 */
	function validate() {
		if (isAdd()) {
			$mainForm.find('[name=userAction]').val(url + ".ajouter");
		} else {
			$mainForm.find('[name=userAction]').val(url + ".modifier");
		}

		var valMontant = $montant.val();
		var valDateFacture = $dateFacture.val();
		var valIdQd = $idQD.val();
		var valIdFacture =<%=viewBean.getFacture().getId()%>;
		var valMembreFamille = $("#membreFamille").val();

		globazGlobal.label.JSP_PF_FACTURE_PAS_ADRESSE_COURRIER = <%=viewBean.getLabel("JSP_PF_FACTURE_PAS_ADRESSE_COURRIER")%>;
		globazGlobal.label.JSP_PF_FACTURE_PAS_ADRESSE_PAIEMENT = <%=viewBean.getLabel("JSP_PF_FACTURE_PAS_ADRESSE_PAIEMENT")%>;

		var $isValid = false;

		$isValid = checkForCreate();
		if (!$isValid) {
			disableButtons(false);
			return false;
		}

		if (($("#dateInfoFacture").val().length != 0 && $("#txtInfoFacture")
				.val().length == 0)
				|| ($("#dateInfoFacture").val().length == 0 && $(
						"#txtInfoFacture").val().length != 0)) {
			alert(globazGlobal.label.JSP_PF_MANDATORY_INFORMATIONS_FACTURE);
			disableButtons(false);
			return false;
		}

		//Si le montant de la facture est différent du montant remboursé le motif est obligatoire
		if (globazNotation.utilsFormatter
				.amountTofloat($montantRembourse.val()) != globazNotation.utilsFormatter
				.amountTofloat($montant.val())
				|| globazNotation.utilsFormatter
						.amountTofloat($excedantRevenuPrisEnCompte.val()) > 0) {
			if ($motifCs.val() == "") {
				alert(globazGlobal.label.JSP_PF_FAC_MONTANT_REMBOURSE_DIFFERENT_FACTURE);
				disableButtons(false);
				return false;
			}
		}

		// Si le montant remboursé est égal ou supérieur à la limite, l'utilisateur doit confirm l'opération
		if (globazNotation.utilsFormatter
				.amountTofloat($montantRembourse.val()) >=
<%=viewBean.getLimiteAvertissement()%>
	) {
			$("#dialog-confirm").dialog({
				resizable : false,
				height : 140,
				width : 420,
				modal : true,
				buttons : {
					"Continuer" : function() {
						$(this).dialog("close");
						supprimerSeparateur();

						action(COMMIT); // Commit internalisé à cause de l'asynchronisme
					},
					"Annuler" : function() {
						disableButtons(false);
						$(this).dialog("close");
					}
				}
			});
		} else {
			supprimerSeparateur();
			action(COMMIT); // Commit internalisé à cause de l'asynchronisme
		}
	}
</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/bodyStart.jspf"%>
<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="JSP_PF_FAC_R_TITRE" />
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/bodyStart2.jspf"%>
<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
		<table cellspacing="5" width="100%">
			<tr>
				<td><ct:FWLabel key="JSP_PF_FAC_D_ASUURE" /></td>
				<td><%=affichePersonnne%></td>
			</tr>
			<tr>
				<td colspan="2"><hr><input
									type="hidden" id="PFQdRenew"
									name="PFQdRenew" value="false" /></td>
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_PF_FAC_D_GESTIONNAIRE" /></td>
				<td><label style="font: italic;"> <%=objSession.getUserId()%>
						- <%=objSession.getUserFullName()%>
				</label> <input type="hidden" name="facture.simpleFacture.idGestionnaire"
					id="idGestionnaire" value="<%=objSession.getUserId()%>" /></td>
			</tr>

			<tr>
				<td><ct:FWLabel key="JSP_PF_FAC_D_DATE_RECEPTION" /></td>
				<td><ct:inputText name="facture.simpleFacture.dateReception"
						id="dateReception" notation="data-g-calendar='mandatory:true '" />
				</td>
			</tr>

			<tr>
				<td><ct:FWLabel key="JSP_PF_FAC_D_DATE_FACTURE" /></td>
				<td><ct:inputText name="facture.simpleFacture.dateFacture"
						id="dateFacture" notation="data-g-calendar='mandatory:true '" />
				</td>
			</tr>

			<tr>
				<td><ct:FWLabel key="JSP_PF_FAC_D_DATE_PRISEENCHARGE" /></td>
				<td><ct:inputText
						name="facture.simpleFacture.datePriseEnCharge"
						id="datePriseEnCharge"
						notation="data-g-calendar='mandatory:false '" /></td>
			</tr>

			<tr>
				<td><ct:FWLabel key="JSP_PF_FAC_D_TYPE" /></td>
				<td><ct:FWListSelectTag name="facture.qd.simpleQD.csType"
						data="<%=viewBean.getListQd()%>"
						defaut="<%=viewBean.getFacture().getQd().getSimpleQD().getCsType()%>"
						notation="data-g-select='mandatory:true '" /> <ct:inputHidden
						name="facture.qd.simpleQD.id" id="idQD" /></td>
			</tr>

			<tr id="membreFamilleTr">
				<td><ct:FWLabel key="JSP_PF_FAC_D_MEMBRE_FAMILLE" /></td>
				<td><ct:select name="membreFamille" id="membreFamille"
						wantBlank="true" notation="data-g-select='mandatory:true'">

					</ct:select></td>
			</tr>

			<tr id="hygienisteDentaireTr">
				<td><ct:FWLabel key="JSP_PF_FAC_D_CaC_HYGIENISTEDENTAIRE" /></td>
				<td><input id="cbHygieniste" type="checkbox"
					name="hygienisteDentaire" onclick="printMinutes()" /></td>
			</tr>

			<tr id="minutesHygienisteTr">
				<td><ct:FWLabel key="JSP_PF_FAC_D_MINUTES_HYGIENISTE" /></td>
				<td><ct:inputText
						name="facture.simpleFacture.minutesHygieniste" id="txtMinutes"
						notation="data-g-integer='mandatory:true'" /></td>
			</tr>

			<tr id="casDeRigueurTr">
				<td><ct:FWLabel key="JSP_PF_FAC_D_CaC_CASDERIGUEUR" /></td>
				<td><input id="cbCasDeRigueur" type="checkbox"
					name="casDeRigueur" /></td>
			</tr>

			<tr id="infosHygienisteTr">
				<td colspan="2" style="margin: 5px; padding: 5px;">
					<fieldset style="padding: 10px;">
						<legend>
							<ct:FWLabel key="JSP_PF_FAC_D_HYGIENISTEDENTAIRE" />
						</legend>

						<table cellspacing="5px">
							<tr>
								<td width="130"><ct:FWLabel
										key="JSP_PF_FAC_D_NB_FACTURE_HYGIENISTE" /></td>
								<td><span id="nbFactures"></span></td>
							</tr>
							<tr>
								<td><ct:FWLabel key="JSP_PF_FAC_D_NB_MINUTES_HYGIENISTE" /></td>
								<td><span id="nbMinutes"></span></td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>

			<tr id="infosFacture">
				<td colspan="2" style="margin: 5px; padding: 5px;">
					<fieldset style="padding: 10px;">
						<legend>
							<ct:FWLabel key="JSP_PF_CONCERNE_INFORMATION_FACTURE" />
						</legend>

						<table cellspacing="5px">
							<%
							    viewBean.chercherInformationFacture();
							    for (int i = 0; i < viewBean.getSizeInformationFacture(); i++) {
							%>
							<tr>
								<td width="130">
									<%
									    InformationFacture infoFact = viewBean.getUneInformationFacture(i);
									%> <%=infoFact.getSimpleInformationFacture().getDate()%>
								</td>
								<td><%=infoFact.getSimpleInformationFacture().getDescription()%>
								</td>
							</tr>
							<%
							    }
							%>
						</table>
					</fieldset>
				</td>
			</tr>

			<tr id="informationSurLaDecision">
				<td colspan="2">
					<fieldset style="padding: 10px;">
						<legend>
							<ct:FWLabel key="JSP_PF_FAC_INFORMATIONS_DECISION" />
						</legend>

						<table cellspacing="5px">
							<tr>
								<td width="300"><ct:FWLabel
										key="JSP_PF_FAC_TYPE_DE_DECISION" /></td>
								<td id="typeDecision"></td>
							</tr>
							<tr>
								<td><ct:FWLabel key="JSP_PF_FAC_PERIODE_DECISION" /></td>

								<td id="dateFacturePourTypeDecision"></td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>

			<tr id="infosQDTr">
				<td colspan="2" style="margin: 5px; padding: 5px;">
					<fieldset style="padding: 10px;">
						<legend>Informations concernant les QDs</legend>

						<table cellspacing="5px">
							<tr>
								<td width="300"><ct:FWLabel
										key="JSP_PF_FAC_D_EXCEDANT_REVENU" /></td>
								<td><span id="excedantRevenuCompense"></span></td>
								<td>/</td>
								<td><span id="excedantRevenu"></span></td>
								<td>&nbsp;</td>
							</tr>
							<tr id="infosQDParente">
								<td><ct:FWLabel key="JSP_PF_FAC_D_QD_UTILISEE" /></td>

								<td><span id="qdParenteUtilise"></span></td>
								<td>/</td>
								<td><span id="qdParenteLimite"></span></td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td><ct:FWLabel key="JSP_PF_FAC_D_QD_TYPE_FAC_UTILISEE" /></td>
								<td><span id="qdUtilise"></span></td>
								<td>/</td>
								<td><span id="qdLimite"></span></td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td><ct:FWLabel key="JSP_PF_FAC_D_MONTANT_MAX" /></td>
								<td colspan="3"><span id="montantFactureMax"></span>
									&nbsp;CHF &nbsp;</td>
								<td><input type="checkbox" name="forcerAcceptation"
									id="forcerAcceptation" /> &nbsp; <ct:FWLabel
										key="JSP_PF_FAC_D_FORCER_ACCEPTATION" /></td>
							</tr>
						</table>

					</fieldset>
				</td>
			</tr>

			<tr id="factureTr">
				<td colspan="2" style="margin: 5px; padding: 5px;">
					<fieldset style="padding: 10px;">
						<legend>Facture</legend>

						<table>
							<tr>
								<td valign="top" width="200px"><ct:FWLabel
										key="JSP_PF_FAC_D_FOURNISSEUR" /></td>
								<td><textarea rows="2" cols="60"
										name="facture.simpleFacture.fournisseur" data-g-string=" "><%=JadeStringUtil.toNotNullString(viewBean.getFacture().getSimpleFacture().getFournisseur())%></textarea>
								</td>
							</tr>

							<tr>
								<td valign="top" width="200px"><ct:FWLabel
										key="JSP_PF_FAC_D_PERIODE" /></td>
								<td><ct:FWLabel key="JSP_PF_FAC_D_PERIODE_DU" /> <ct:inputText
										id="dateDebutTraitement"
										name="facture.simpleFacture.dateDebutTraitement"
										notation="data-g-calendar=' '" /> <ct:FWLabel
										key="JSP_PF_FAC_D_PERIODE_AU" /> <ct:inputText
										id="dateFinTraitement"
										name="facture.simpleFacture.dateFinTraitement"
										notation="data-g-calendar=' '" /></td>
							</tr>

							<tr id="libelleTr">
								<td valign="top" width="200px"><ct:FWLabel
										key="JSP_PF_FAC_D_LIBELLE" /></td>
								<td><textarea rows="2" cols="60"
										name="facture.simpleFacture.libelle" data-g-string=" "><%=JadeStringUtil.toNotNullString(viewBean.getFacture().getSimpleFacture().getLibelle())%></textarea>
								</td>
							</tr>

							<tr id="montantTr">
								<td><ct:FWLabel key="JSP_PF_FAC_D_MONTANT" /></td>
								<td><ct:inputText name="facture.simpleFacture.montant"
										id="montant" notation="data-g-amount='mandatory:true'" /></td>
							</tr>

							<tr id="montantRembourseTr">
								<td><ct:FWLabel key="JSP_PF_FAC_D_MONTANT_REMBOURSE" /></td>
								<td><ct:inputText
										name="facture.simpleFacture.montantRembourse"
										id="montantRembourse"
										notation="data-g-amount='mandatory:true'" /> &nbsp; <ct:FWLabel
										key="JSP_PF_FAC_D_MONTANT_EXCEDANT_COMPENSE" /> &nbsp; <ct:inputText
										name="facture.simpleFacture.excedantRevenuCompense"
										id="excedantRevenuPrisEnCompte" notation="data-g-amount=' '" />
								</td>
							</tr>

							<tr id="motifTr">
								<td valign="top"><ct:FWLabel key="JSP_PF_FAC_D_MOTIF" /></td>
								<td><ct:select name="facture.simpleFacture.csMotif"
										id="motifCs" wantBlank="true" notation="data-g-select=' '">
										<ct:optionsCodesSystems
											csFamille="<%=IPFConstantes.CSGROUP_MOTIF_FACTURE%>" />
									</ct:select> <br /> <input type="text"
									name="facture.simpleFacture.motifLibre" id="motifLibre"
									data-g-string=" " size="60" /></td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>

			<tr id="paiementTr">
				<td colspan="2" style="margin: 5px; padding: 5px;">
					<fieldset style="padding: 5px;">
						<legend>
							<ct:FWLabel key="JSP_PF_FAC_D_VERSEMENT" />
						</legend>

						<%
						    if (JadeStringUtil.isBlankOrZero(viewBean.getFacture().getSimpleFacture().getIdTiersAdressePaiement())) {
						        AdresseTiersDetail adresse = PFUserHelper.getAdressePaiementAssure(viewBean.getDossier()
						                .getDemandePrestation().getPersonneEtendue().getTiers().getId(),
						                IPFConstantes.CS_DOMAINE_ADRESSE, JACalendar.todayJJsMMsAAAA());
						        if (adresse.getAdresseFormate() != null) {
						            idAdressePaiementDefault = viewBean.getDossier().getDemandePrestation().getPersonneEtendue()
						                    .getTiers().getId();
						        }
						    } else {
						        idAdressePaiementDefault = viewBean.getFacture().getSimpleFacture().getIdTiersAdressePaiement();
						    }

						    if (JadeStringUtil
						            .isBlankOrZero(viewBean.getFacture().getSimpleFacture().getIdApplicationAdressePaiement())) {
						        idApplicationPaiementDefault = IPFConstantes.CS_DOMAINE_ADRESSE;
						    } else {
						        idApplicationPaiementDefault = viewBean.getFacture().getSimpleFacture()
						                .getIdApplicationAdressePaiement();
						    }

						    if (JadeStringUtil.isBlankOrZero(viewBean.getFacture().getSimpleFacture().getIdTiersAdresseCourrier())) {
						        AdresseTiersDetail adresse = PFUserHelper.getAdresseAssure(viewBean.getDossier().getDemandePrestation()
						                .getPersonneEtendue().getTiers().getId(), JACalendar.todayJJsMMsAAAA());
						        if (adresse.getAdresseFormate() != null) {
						            idAdresseCourrierDefault = viewBean.getDossier().getDemandePrestation().getPersonneEtendue()
						                    .getTiers().getId();
						        }
						    } else {
						        idAdresseCourrierDefault = viewBean.getFacture().getSimpleFacture().getIdTiersAdresseCourrier();
						    }

						    if (JadeStringUtil
						            .isBlankOrZero(viewBean.getFacture().getSimpleFacture().getIdApplicationAdresseCourrier())) {
						        idApplicationCourrierDefault = IPFConstantes.CS_DOMAINE_ADRESSE;
						    } else {
						        idApplicationCourrierDefault = viewBean.getFacture().getSimpleFacture()
						                .getIdApplicationAdresseCourrier();
						    }
						%>

						<table>
							<tr>
								<td width="200px"><ct:FWLabel
										key="JSP_PF_FAC_D_ADRESSE_COURRIER" /></td>
								<td>
									<div
										data-g-adresse="mandatory:true, service:findAdresse,defaultvalue:¦<%=viewBean.getAdresseCourrierAssure().getAdresseFormate()%>¦">
										<input type="hidden" id="idAdresseCourrier"
											class="avoirAdresse.idTiers"
											name="facture.simpleFacture.idTiersAdresseCourrier"
											value="<%=idAdresseCourrierDefault%>"> <input
											type="hidden" id="idDomaineApplicatifCourrier"
											class="avoirAdresse.idApplication"
											name="facture.simpleFacture.idApplicationAdresseCourrier"
											value="<%=idApplicationCourrierDefault%>">
									</div>

								</td>
								<td width="200px"><ct:FWLabel
										key="JSP_PF_FAC_D_ADRESSE_PAIEMENT" /></td>
								<td>
									<div
										data-g-adresse="mandatory:true, service:findAdressePaiement,defaultvalue:¦<%=viewBean.getAdressePaiementAssure().getAdresseFormate()%>¦">
										<input type="hidden" id="idTiersAdressePaiement"
											class="avoirPaiement.idTiers"
											name="facture.simpleFacture.idTiersAdressePaiement"
											value="<%=idAdressePaiementDefault%>"> <input
											type="hidden" id="idApplicationAdressePaiement"
											class="avoirPaiement.idApplication"
											name="facture.simpleFacture.idApplicationAdressePaiement"
											value="<%=idApplicationPaiementDefault%>">
									</div> <input type="hidden" id="modificationFacture"
									name="modificationFacture" value="false" /> <input
									type="hidden" id="ancienMontantFacture"
									name="ancienMontantFacture" value="0.0" /> <input
									type="hidden" id="ancienMontantARembourserFacture"
									name="ancienMontantARembourserFacture" value="0.0" /> <input
									type="hidden" id="ancienTypeQdParente"
									name="ancienTypeQdParente" value="" />
								</td>
							</tr>
							<tr>
								<td colspan="2">&nbsp;</td>
							</tr>
							<tr>
								<td><ct:FWLabel key="JSP_PF_FAC_D_NUMERO_REFERENCE" /></td>
								<td><input type="text" id="numRefFacture"
									name="facture.simpleFacture.numRefFacture"
									value="<%=JadeStringUtil.toNotNullString(viewBean.getFacture().getSimpleFacture().getNumRefFacture())%>"
									size="60" /></td>
							</tr>
						</table>

					</fieldset>
				</td>
			</tr>

			<tr id="infosFactureSaisir">
				<td colspan="2" style="margin: 5px; padding: 5px;">
					<fieldset style="padding: 5px;">
						<legend>
							<ct:FWLabel key="JSP_PF_SAISIR_INFORMATION_FACTURE" />
						</legend>

						<table>
							<tr>
								<td><ct:FWLabel key="JSP_PF_DATE_INFORMATION_FACTURE" /></td>
								<td><input id="dateInfoFacture" type="text"
									name="informationFacture.simpleInformationFacture.date"
									data-g-calendar="mandatory:false" /></td>
								<td rowspan="2">
									<div data-g-boxmessage="type:WARN">
										<ct:FWLabel key="JSP_PF_MANDATORY_INFORMATIONS_FACTURE" />
									</div>
								</td>
							</tr>
							<tr>
								<td valign="top"><ct:FWLabel
										key="JSP_PF_DESCRIPTION_INFORMATION_FACTURE" /></td>
								<td><textarea id="txtInfoFacture" rows="5" cols="80"
										name="informationFacture.simpleInformationFacture.description"></textarea>
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>

		</table>

		<div id="dialog-confirm"
			title="<ct:FWLabel key="JSP_PF_FACT_CONFIRM_MONTANT_PLUS_1000_TITLE"/>">
			<p>
				<span class="ui-icon ui-icon-alert"
					style="float: left; margin: 0 7px 20px 0;"></span>
				<ct:FWLabel key="JSP_PF_FACT_CONFIRM_MONTANT_PLUS_1000" />
			</p>
		</div>

	</td>
</tr>

<%-- /tpl:insert --%>

<%@ include file="/theme/detail_ajax_hybride/bodyButtons.jspf"%>
<%-- tpl:insert attribute="zoneButtons" --%>
<%
    if (bBtnValidNew) {
%><input class="btnCtrl" id="btnValidNew" type="button"
	value="<%=btnValidNewLabel%>"  onclick="if(validateAndNew()) action(COMMIT);">
<%
    }
%>

<%if (bBtnValidate) {%><input class="btnCtrl" id="btnVal" type="button" value="<%=btnValLabel%>" onclick="if(validate()) action(COMMIT);"><%}%>

<%
    if (bBtnCancel) {
%><input class="btnCtrl" id="btnCancel" type="button"
	value="<%=btnCanLabel%>" onclick="annuler(); action(ROLLBACK);">
<%
    }
%>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/bodyErrors.jspf"%>
<%-- tpl:insert attribute="zoneEndPage" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/footer.jspf"%>