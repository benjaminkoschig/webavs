<%@page import="globaz.aquila.process.journal.utils.COUtilsJournal"%>
<%@page
	import="ch.globaz.perseus.business.models.informationfacture.InformationFacture"%>
<%@page import="globaz.prestation.tools.PRDateFormater"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatFacture"%>
<%@page
	import="ch.globaz.perseus.business.services.PerseusServiceLocator"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.jade.client.util.JadeNumericUtil"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="ch.globaz.pyxis.business.model.AdresseTiersDetail"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page
	import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeSoin"%>
<%@page import="globaz.perseus.vb.rentepont.PFFactureRentePontViewBean"%>
<%@ page language="java" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail_ajax_hybride/header.jspf"%>
<%-- tpl:insert attribute="zoneInit" --%>
<%
    idEcran="PPF1231";
	PFFactureRentePontViewBean viewBean = (PFFactureRentePontViewBean) session.getAttribute("viewBean"); 
	boolean viewBeanIsNew="add".equals(request.getParameter("_method"));
	boolean bBtnDelete = false;
	boolean bBtnValidate = false;
	bButtonValidate = false;

	bButtonDelete = false;
	
	boolean checkLabel = false;
	
	if(!objSession.hasRight("perseus.rentepont.factureRentePont", FWSecureConstants.ADD)){
			bButtonCancel = true;
			bButtonValidate = false;
	}
	
	if (!viewBeanIsNew) {
			bButtonUpdate = true;
			bButtonCancel = true;
	}else{
	    bButtonValidate = true;
		btnValLabel = objSession.getLabel("JSP_PF_DECISION_A_BOUTON_PREVALIDER");
		checkLabel = true;
	}
	if (CSEtatFacture.VALIDE.getCodeSystem().equals(viewBean.getFactureRentePont().getSimpleFactureRentePont().getCsEtat())) {
		bButtonDelete = false;
		bButtonValidate = false;
		bButtonUpdate = false;
	}
	if (CSEtatFacture.ENREGISTRE.getCodeSystem().equals(viewBean.getFactureRentePont().getSimpleFactureRentePont().getCsEtat()) 
			&& objSession.hasRight("perseus.rentepont.factureRentePont", FWSecureConstants.ADD)) {
		if (viewBeanIsNew) {
		    bButtonValidate = true;
		}else {
			bButtonValidate = false;
		}
		
		bBtnDelete = true;
		if (((objSession.hasRight("perseus.facture.validation", FWSecureConstants.UPDATE)) ||(objSession.hasRight("perseus.rentepont.factureRentePont", FWSecureConstants.ADD) && Float.valueOf(viewBean.getFactureRentePont().getSimpleFactureRentePont().getMontantRembourse()) < viewBean.getMontantMaxValidationUser()))) {
		    bBtnValidate = true;
			bButtonUpdate = true;
		}
		
		if(!viewBean.isPaiementOKPourValidation()) {
		    bBtnValidate = false;
		}
	}
		
	PersonneEtendueComplexModel personne = viewBean.getDossier().getDemandePrestation().getPersonneEtendue();
	String affichePersonnne = "";
	String idDossier = request.getParameter("idDossier");
	if (idDossier == null) {
		idDossier = viewBean.getDossier().getId();
	}
	
	affichePersonnne = PFUserHelper.getDetailAssure(objSession,personne);
	
	String idAdressePaiementDefault = "";
    String idApplicationPaiementDefault = "";
    String idAdresseCourrierDefault = "";
    String idApplicationCourrierDefault = "";
%>


<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/javascripts.jspf"%>
<%@ include file="/perseusRoot/ajax/javascriptsAndCSS.jspf"%>
<%@ include file="/perseusRoot/scripts/rentepont/checkerFactureRentePont.jsp"%>
<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript">
	var sousTypesSoins = <%=viewBean.getAllSousTypesInJson()%>;
	var csSousTypeSoin = <%=viewBean.getFactureRentePont().getSimpleFactureRentePont().getCsSousTypeSoinRentePont()%>;
	var url = "perseus.rentepont.factureRentePont";
	var actionMethod;
	var userAction;
	var $motifLibre, $motifCs, $montantRembourse, $excedantRevenuPrisEnCompte, $montant, isModifier;
	isModifier = false;
	
	$typeSoin = $("#typeSoin");
	$sousTypeSoin = $("#sousTypeSoin");
	//Set le type de soin
	$typeSoin.val('<%=viewBean.getFactureRentePont().getSimpleFactureRentePont().getCsTypeSoinRentePont()%>');
	$sousTypeSoin.val('<%=viewBean.getFactureRentePont().getSimpleFactureRentePont().getCsSousTypeSoinRentePont()%>');
	
	globazGlobal.label = {};
	globazGlobal.label.JSP_PF_FACRP_AVERTISSEMENT_FACTURE_SIMILAIRE = <%=viewBean.getLabel("JSP_PF_FACRP_AVERTISSEMENT_FACTURE_SIMILAIRE")%>;
	globazGlobal.label.JSP_PF_FACRP_MONTANT_REMBOURSE_DIFFERENT_FACTURE =  <%=viewBean.getLabel("JSP_PF_FACRP_MONTANT_REMBOURSE_DIFFERENT_FACTURE")%>;
	globazGlobal.label.JSP_PF_MANDATORY_INFORMATIONS_FACTURE = <%=viewBean.getLabel("JSP_PF_MANDATORY_INFORMATIONS_FACTURE")%>;
	globazGlobal.label.JSP_PF_MANDATORY_MISSING_INFORMATIONS = <%=viewBean.getLabel("JSP_PF_MANDATORY_MISSING_INFORMATIONS")%>;
	
	globazGlobal.label.JSP_PF_MANDATORY_ADRESSE_COURRIER = <%=viewBean.getLabel("JSP_PF_MANDATORY_ADRESSE_COURRIER")%>;
	globazGlobal.label.JSP_PF_MANDATORY_ADRESSE_PAIEMENT = <%=viewBean.getLabel("JSP_PF_MANDATORY_ADRESSE_PAIEMENT")%>;
	
	listeSelect = {
		$selectSousTypeSoin: null,
		
		init: function (csSousTypeSoin) {
			this.$selectMaster = $("#typeSoin");
			this.$selectSousTypeSoin=$("#sousTypeSoin");	
			this.$selectSousTypeSoin.hide();
			this.addEvent();	
			if($.trim(this.$selectMaster.val()).length){
				this.addOptions(this.$selectMaster.val());
				if(csSousTypeSoin){
					this.$selectSousTypeSoin.val(csSousTypeSoin);
				}
			}
	
		},
		
		createOptions : function (codeSystemParent) {
			var options = "",
			     typeSoins = sousTypesSoins[codeSystemParent];
			for ( key in typeSoins) {
				options += "<option value='"+key+"'>"+typeSoins[key]+"</option>";
			}
			return options;
		},
		
		addOptions: function (codeSystemParent) {
			this.$selectSousTypeSoin.children().remove();
			this.$selectSousTypeSoin.append(this.createOptions(codeSystemParent));
			this.$selectSousTypeSoin.show();
		},
		
		addEvent: function () {
			var that = this;
			this.$selectMaster.change(function () {
				that.addOptions(this.value);
			});
		}
	}
	
	$(function () {
		listeSelect.init(csSousTypeSoin);

		$motifLibre = $("#motifLibre");
		$motifCs = $("#motifCs");
		$montant = $("#montant");
		$montantRembourse = $("#montantRembourse");
		$excedantRevenuPrisEnCompte = $("#excedantRevenuPrisEnCompte");
		
		$ancienMontantARembourserFacture = $("#ancienMontantARembourserFacture");
		$ancienMontantFacture = $("#ancienMontantFacture");
		
		changeMotif();
		actionMethod=$('[name=_method]',document.forms[0]).val();
		userAction=$('[name=userAction]',document.forms[0])[0];
		
		
		$motifCs.change(changeMotif);
		$montantRembourse.change(changeMontantRembourse);
		$montant.change(changeMontant);
		
		$( "#dialog:ui-dialog" ).dialog( "destroy" );
		$( "#dialog-confirm" ).hide();
		
		if(<%=viewBean.getModificationFacture()%>){
			upd();
		}
		
	})
	
	function changeMontant() {
		var montantMax = globazNotation.utilsFormatter.amountTofloat($('#montantFactureMax').text());
		if (globazNotation.utilsFormatter.amountTofloat($montant.val().replace("'", "")) < montantMax) {
			$montantRembourse.val($montant.val());
		} else {
			$montantRembourse.val($('#montantFactureMax').text());
		}
		$montantRembourse.change();
	}
	
	function changeMontantRembourse() {
		//Calcul de l'excedant à remboursé
		var excedantArembourser = globazNotation.utilsFormatter.amountTofloat($('#excedantRevenu').text()) - globazNotation.utilsFormatter.amountTofloat($('#excedantRevenuCompense').text());
		//Si un ancien excédant avait été remboursé
		if (excedantArembourser <= 0) {
			$excedantRevenuPrisEnCompte.val("0");
		} else {
			if (globazNotation.utilsFormatter.amountTofloat($montantRembourse.val().replace("'", "")) < excedantArembourser) {
				$excedantRevenuPrisEnCompte.val($montantRembourse.val());
			} else {
				$excedantRevenuPrisEnCompte.val(excedantArembourser);
			}
		}
		var montantRemb = globazNotation.utilsFormatter.amountTofloat($montantRembourse.val().replace("'", ""));
		var excedantPris = globazNotation.utilsFormatter.amountTofloat($excedantRevenuPrisEnCompte.val().replace("'", ""));
		$montantRembourse.val(globazNotation.utilsFormatter.formatStringToAmout(montantRemb - excedantPris));
		$excedantRevenuPrisEnCompte.change();
	}
	
	function changeMotif() {
		if ($motifCs.val() === "<%=IPFConstantes.CS_MOTIF_FACTURE_AUTRE%>") {
			$motifLibre.show();
		} else {
			$motifLibre.hide();
			$motifLibre.val("");
		}
	}
	
	/*
	Permet de mettre à disable/enable tous les boutons de la page en un seul appel
	*/
	function disableButtons(doDisable) {
		$("#btnUpd").prop("disabled", doDisable);
		$("#btnVal").prop("disabled", doDisable);
	}
	
	function messageLimiteDepassee(){
		$( "#dialog-confirm" ).dialog({
			resizable: false,
			height:140,
			width:420,
			modal: true,
			buttons: {
				"Continuer" : function() {
					$( this ).dialog( "close" );
					document.forms[0].elements('userAction').value="perseus.rentepont.factureRentePont.supprimer";
			        document.forms[0].submit();
				},
				"Annuler" : function() {
					disableButtons(false);
					$( this ).dialog( "close" );
				}
			}
		});
	}
	

	/*
	 Attention, le comportement par défaut du framework est court-circuité ici avec la méthode validate() qui
	 retourne toujours false de manière à ce que l'appel commit() conditionné par cette dernière ne soit 
	 jamais exécuté. Dû à la nature asynchrone de l'implémentation de la méthode validate() ci-dessous, les
	 appels commit() sont directement faits dans le callback de l'appel ajax. 
	 */
	function validate() {

		var $isValid = false;
		
		$isValid = checkForCreate();
		if(!$isValid){
			disableButtons(false);
			return false;
		}
		
		if (actionMethod == "add")	{
	    	document.forms[0].elements('userAction').value="perseus.rentepont.factureRentePont.ajouter";
	    	
			//Si le montant de la facture est différent du montant remboursé le motif est obligatoire
			if (globazNotation.utilsFormatter.amountTofloat($montantRembourse.val()) != globazNotation.utilsFormatter.amountTofloat($montant.val()) || globazNotation.utilsFormatter.amountTofloat($excedantRevenuPrisEnCompte.val()) > 0) {
				if ($motifCs.val() == "") {
					alert(globazGlobal.label.JSP_PF_FACRP_MONTANT_REMBOURSE_DIFFERENT_FACTURE);
					disableButtons(false);
					return false;
				}
			}
			
			if (($("#dateInfoFacture").val().length != 0 && $("#txtInfoFacture").val().length == 0) || ($("#dateInfoFacture").val().length == 0 && $("#txtInfoFacture").val().length != 0)) {
				alert(globazGlobal.label.JSP_PF_MANDATORY_INFORMATIONS_FACTURE);
				disableButtons(false);
				return false;
			}
	    	
		    if (globazNotation.utilsFormatter.amountTofloat($montantRembourse.val()) >= <%=viewBean.getLimiteAvertissement()%>) {
				$( "#dialog-confirm" ).dialog({
					resizable: false,
					height:140,
					width:420,
					modal: true,
					buttons: {
						"Continuer" : function() {
							$( this ).dialog( "close" );
							action(COMMIT);
						},
						"Annuler" : function() {
							$( this ).dialog( "close" );
						}
					}
				});
			} else {
				action(COMMIT);
			}
	    } else {
	    	if(isModifier){
	    		//Si le montant de la facture est différent du montant remboursé le motif est obligatoire
				if (globazNotation.utilsFormatter.amountTofloat($montantRembourse.val()) != globazNotation.utilsFormatter.amountTofloat($montant.val()) || globazNotation.utilsFormatter.amountTofloat($excedantRevenuPrisEnCompte.val()) > 0) {
					if ($motifCs.val() == "") {
						alert(globazGlobal.label.JSP_PF_FACRP_MONTANT_REMBOURSE_DIFFERENT_FACTURE);
						disableButtons(false);
						return false;
					} else if(globazNotation.utilsFormatter.amountTofloat($montantRembourse.val()) >= <%=viewBean.getLimiteAvertissement()%>){
						messageLimiteDepassee();
					} else {
						document.forms[0].elements('userAction').value="perseus.rentepont.factureRentePont.supprimer";
				        document.forms[0].submit();
					}
				}else if (($("#dateInfoFacture").val().length != 0 && $("#txtInfoFacture").val().length == 0) || ($("#dateInfoFacture").val().length == 0 && $("#txtInfoFacture").val().length != 0)) {
					alert(globazGlobal.label.JSP_PF_MANDATORY_INFORMATIONS_FACTURE);
					disableButtons(false);
					return false;
				}else if (globazNotation.utilsFormatter.amountTofloat($montantRembourse.val()) >= <%=viewBean.getLimiteAvertissement()%>) {
					messageLimiteDepassee();
				}else{
					document.forms[0].elements('userAction').value="perseus.rentepont.factureRentePont.supprimer";
			        document.forms[0].submit();
				}
			}else{
				if (window.confirm("<%=objSession.getLabel("JSP_PF_FACT_VALIDER_CONFIRMATION")%>")){
			    	document.forms[0].elements('userAction').value="perseus.rentepont.factureRentePont.modifier";
			    	action(COMMIT);
				} else {
					disableButtons(false);
					return false;
				}
			}
	    }
	}    
	
	function validateRestituer() {
		if (window.confirm("<%=objSession.getLabel("JSP_PF_FACT_RESTITUER_CONFIRMATION")%>")){
		    state = true;
		    document.forms[0].elements('userAction').value="perseus.rentepont.factureRentePont.modifier";
		} else {
			state = false;
		}
		return state;
	}
	
	function add() {}
	function upd() {
		isModifier = true;

		//Montant de la QD
		var montantUtiliseQD = Number(<%=viewBean.getFactureRentePont().getQdRentePont().getSimpleQDRentePont().getMontantUtilise()%>);
		var excedantRevenuCompenseQD = Number(<%=viewBean.getFactureRentePont().getQdRentePont().getSimpleQDRentePont().getExcedantRevenuCompense()%>);
		var excedantRevenuCompenseFacture = Number(<%=viewBean.getFactureRentePont().getSimpleFactureRentePont().getExcedantRevenuCompense()%>);
		
		//Montant des factures
		var montantUtiliseFacture = Number(<%=new FWCurrency(viewBean.getFactureRentePont().getSimpleFactureRentePont().getMontant()).toString()%>);
		var montantDepassantFacture = Number(<%=new FWCurrency(viewBean.getFactureRentePont().getSimpleFactureRentePont().getMontantDepassant()).toString()%>);
		var montantRembourseFacture = Number(<%=new FWCurrency(viewBean.getFactureRentePont().getSimpleFactureRentePont().getMontantRembourse()).toString()%>);
		var montantMaxCompense = Number(<%=new FWCurrency(viewBean.getMontantMaxCompense()).toString()%>);
		
		//set des montants pour supprimer la facture en cas de modification
		$ancienMontantFacture.val(montantUtiliseFacture);
		$ancienMontantARembourserFacture.val(montantRembourseFacture);
 		 		
 		//Ajustement des montants
 		$qdUtilise = $("#qdUtilise"); 	
 		$excedantRevenuCompense = $("#excedantRevenuCompense");
 		$montantFactureMax = $("#montantFactureMax");
 		$qdUtilise.text(globazNotation.utilsFormatter.formatStringToAmout(montantUtiliseQD - montantRembourseFacture));
 		$montantFactureMax.text(globazNotation.utilsFormatter.formatStringToAmout(montantRembourseFacture + montantMaxCompense));
 		$excedantRevenuCompense.text(globazNotation.utilsFormatter.formatStringToAmout(excedantRevenuCompenseQD - excedantRevenuCompenseFacture));
 		
 		//Set le type de soin
 		$typeSoin.val('<%=viewBean.getFactureRentePont().getSimpleFactureRentePont().getCsTypeSoinRentePont()%>'); 
 		$sousTypeSoin.val('<%=viewBean.getFactureRentePont().getSimpleFactureRentePont().getCsSousTypeSoinRentePont()%>');
		
		//Renomage du bouton en pré-valider
		var preValiderLabel = "<%=objSession.getLabel("JSP_PF_DECISION_A_BOUTON_PREVALIDER")%>";
 		$("#btnValidate").val(preValiderLabel);

 		//Instruction pour le PFFactureRentePontHelper afin qu'il sache qu'on modifie une facture
 		$("#modificationFacture").val("true");
 		
 		$("#btnDelete").hide();
	}
	
	function del() {
   		if (window.confirm("<%=objSession.getLabel("JSP_PF_DOS_SUPPRESSION_CONFIRMATION")%>")) {
			document.forms[0].elements('userAction').value = "perseus.rentepont.factureRentePont.supprimer";
			document.forms[0].submit();
		}
	}

	//Fonction permettant d'annuler une opération en cours
	function cancel() {
		document.forms[0].elements('userAction').value = "back";
	}

	function init() {
	}

	function postInit() {
	}
</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/bodyStart.jspf"%>
<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="JSP_PF_FACRP_R_TITRE" />
<%-- /tpl:insert  --%>

<%@ include file="/theme/detail_ajax_hybride/bodyStart2.jspf"%>
<%-- tpl:insert attribute="zoneMain" --%>


<tr>
	<td>
		<table cellspacing="5" width="100%">
			<tr>
				<td><ct:FWLabel key="JSP_PF_FACRP_D_ASUURE" /></td>
				<td><%=affichePersonnne%></td>
			</tr>
			<tr>
				<td colspan="2"><hr></td>
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_PF_FACRP_D_GESTIONNAIRE" /></td>
				<td><ct:FWListSelectTag name="idGestionnaire"
						data="<%=globaz.perseus.utils.PFGestionnaireHelper.getResponsableData(objSession)%>"
						notation="data-g-select='mandatory:true'"
						defaut="<%=(viewBean.getIdGestionnaire() != null && !viewBean.getIdGestionnaire().isEmpty() ? viewBean
                    .getIdGestionnaire() : objSession.getUserId())%>" />
				</td>
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_PF_FACRP_D_DATE_RECEPTION" /></td>
				<td><ct:inputText
						name="factureRentePont.simpleFactureRentePont.dateReception"
						id="dateReception" notation="data-g-calendar='mandatory:true '" />
				</td>
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_PF_FACRP_D_DATE_FACTURE" /></td>
				<td><ct:inputText
						name="factureRentePont.simpleFactureRentePont.dateFacture"
						id="dateFacture" notation="data-g-calendar='mandatory:true '" />
				</td>
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_PF_FACRP_D_DATE_PRISEENCHARGE" /></td>
				<td><ct:inputText
						name="factureRentePont.simpleFactureRentePont.datePriseEnCharge"
						id="datePriseEnCharge"
						notation="data-g-calendar='mandatory:false '" /></td>
			</tr>

			<tr>
				<td><ct:FWLabel key="JSP_PF_FACRP_D_TYPE" /></td>
				<td>
					 <ct:select name='factureRentePont.simpleFactureRentePont.csTypeSoinRentePont' id="typeSoin" notation="data-g-select='mandatory:true'">
						<option value=''></option>
						<%
						    for (String key1 : viewBean.getTypesSoins().keySet()) {
						    	String selected = "";
						        if (key1.equals(viewBean.getFactureRentePont().getSimpleFactureRentePont().getCsTypeSoinRentePont())) {
						        	selected = "selected='selected'";
						        }
						%>
						<option value='<%=key1%>' <%=selected%> >
							<%=viewBean.getTypesSoins().get(key1)%>
						</option>
						<%}%>
					</ct:select> <br /> 
					<select name='factureRentePont.simpleFactureRentePont.csSousTypeSoinRentePont' id="sousTypeSoin">
					</select> 	
				</td>
			</tr>

			<tr id="membreFamilleTr">
				<td><ct:FWLabel key="JSP_PF_FACRP_D_MEMBRE_FAMILLE" /></td>
				<td><ct:FWListSelectTag name="factureRentePont.simpleFactureRentePont.idTiersMembreFamille"
						data="<%=viewBean.getListMembresFamille()%>"
						defaut='<%=!JadeStringUtil.isEmpty(viewBean.getIdTiersMembreFamille())?viewBean.getIdTiersMembreFamille():""%>'
						 /></td>
			</tr>

			<tr id="hygienisteDentaireTr">
				<td><ct:FWLabel key="JSP_PF_FACRP_D_HYGIENISTE_DENTAIRE" /></td>
				<td><input type="checkbox" name="hygienisteDentaire"
					<%=(viewBean.getFactureRentePont().getSimpleFactureRentePont().getHygienisteDentaire() != null && viewBean
                    .getFactureRentePont().getSimpleFactureRentePont().getHygienisteDentaire()) ? "checked='checked'"
                    : ""%> /></td>
			</tr>

			<tr id="casDeRigueurTr">
				<td><ct:FWLabel key="JSP_PF_FACRP_D_CASDERIGUEUR" /></td>
				<td><input type="checkbox" name="casDeRigueur"
					<%=(viewBean.getFactureRentePont().getSimpleFactureRentePont().getCasDeRigueur() != null && viewBean
                    .getFactureRentePont().getSimpleFactureRentePont().getCasDeRigueur()) ? "checked='checked'" : ""%> /></td>
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

			<tr id="infosQDTr">
				<td colspan="2" style="margin: 5px; padding: 5px;">
					<fieldset style="padding: 10px;">
						<legend>Informations concernant la QD</legend>

						<table cellspacing="5px">
							<tr>
								<td width="300"><ct:FWLabel
										key="JSP_PF_FACRP_D_EXCEDANT_REVENU" /></td>
								<td><span id="excedantRevenuCompense"><%=new FWCurrency(viewBean.getFactureRentePont().getQdRentePont().getSimpleQDRentePont()
                    .getExcedantRevenuCompense()).toStringFormat()%></span>
								</td>
								<td>/</td>
								<td><span id="excedantRevenu"><%=new FWCurrency(viewBean.getFactureRentePont().getQdRentePont().getSimpleQDRentePont()
                    .getExcedantRevenu()).toStringFormat()%></span>
								</td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td><ct:FWLabel key="JSP_PF_FACRP_D_QD_UTILISEE" /></td>

								<td><span id="qdUtilise"><%=new FWCurrency(viewBean.getFactureRentePont().getQdRentePont().getSimpleQDRentePont()
                    .getMontantUtilise()).toStringFormat()%></span>
								</td>
								<td>/</td>
								<td><span id="qdLimite"><%=new FWCurrency(viewBean.getFactureRentePont().getQdRentePont().getSimpleQDRentePont()
                    .getMontantLimite()).toStringFormat()%></span>
								</td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td><ct:FWLabel key="JSP_PF_FACRP_D_MONTANT_MAX" /></td>
								<td colspan="3"><span id="montantFactureMax"><%=new FWCurrency(viewBean.getMontantMaxCompense()).toStringFormat()%></span>
									&nbsp;CHF &nbsp;</td>
								<td><input type="checkbox" id="acceptationForceeCB" name="acceptationForcee"
									<%=(viewBean.getFactureRentePont().getSimpleFactureRentePont().getAcceptationForcee() != null && viewBean
                    .getFactureRentePont().getSimpleFactureRentePont().getAcceptationForcee()) ? "checked='checked'"
                    : ""%> />
									&nbsp; <ct:FWLabel key="JSP_PF_FACRP_D_FORCER_ACCEPTATION" />
								</td>
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
										key="JSP_PF_FACRP_D_FOURNISSEUR" /></td>
								<td><textarea rows="2" cols="60"
										name="factureRentePont.simpleFactureRentePont.fournisseur"
										data-g-string=" "><%=JadeStringUtil.toNotNullString(viewBean.getFactureRentePont().getSimpleFactureRentePont()
                    .getFournisseur())%></textarea>
								</td>
							</tr>

							<tr>
								<td valign="top" width="200px"><ct:FWLabel
										key="JSP_PF_FACRP_D_PERIODE" /></td>
								<td><ct:FWLabel key="JSP_PF_FACRP_D_PERIODE_DU" /> <ct:inputText
										id="dateDebutTraitement"
										name="factureRentePont.simpleFactureRentePont.dateDebutTraitement"
										notation="data-g-calendar=' '" /> <ct:FWLabel
										key="JSP_PF_FACRP_D_PERIODE_AU" /> <ct:inputText
										id="dateFinTraitement"
										name="factureRentePont.simpleFactureRentePont.dateFinTraitement"
										notation="data-g-calendar=' '" /></td>
							</tr>

							<tr id="libelleTr">
								<td valign="top" width="200px"><ct:FWLabel
										key="JSP_PF_FACRP_D_LIBELLE" /></td>
								<td><textarea rows="2" cols="60"
										name="factureRentePont.simpleFactureRentePont.libelle"
										data-g-string=" "><%=JadeStringUtil.toNotNullString(viewBean.getFactureRentePont().getSimpleFactureRentePont()
                    .getLibelle())%></textarea>
								</td>
							</tr>

							<tr id="montantTr">
								<td><ct:FWLabel key="JSP_PF_FACRP_D_MONTANT" /></td>
								<td><ct:inputText
										name="factureRentePont.simpleFactureRentePont.montant"
										id="montant" notation="data-g-amount='mandatory:true'" /></td>
							</tr>

							<tr id="montantRembourseTr">
								<td><ct:FWLabel key="JSP_PF_FACRP_D_MONTANT_REMBOURSE" /></td>
								<td><ct:inputText
										name="factureRentePont.simpleFactureRentePont.montantRembourse"
										id="montantRembourse"
										notation="data-g-amount='mandatory:true'" /></td>
							</tr>

							<tr id="excedantRevenuCompenseTr">
								<td><ct:FWLabel
										key="JSP_PF_FACRP_D_MONTANT_EXCEDANT_COMPENSE" />&nbsp;</td>
								<td><ct:inputText
										name="factureRentePont.simpleFactureRentePont.excedantRevenuCompense"
										id="excedantRevenuPrisEnCompte" notation="data-g-amount=' '" />
								</td>
							</tr>

							<tr id="motifTr">
								<td valign="top"><ct:FWLabel key="JSP_PF_FACRP_D_MOTIF" /></td>
								<td><ct:select
										name="factureRentePont.simpleFactureRentePont.csMotif"
										id="motifCs" wantBlank="true" notation="data-g-select=' '">
										<ct:optionsCodesSystems
											csFamille="<%=ch.globaz.perseus.business.constantes.IPFConstantes.CSGROUP_MOTIF_FACTURE%>" />
									</ct:select> <br /> <input type="text"
									name="factureRentePont.simpleFactureRentePont.motifLibre"
									id="motifLibre" data-g-string=" " size="60"
									value="<%=viewBean.getFactureRentePont().getSimpleFactureRentePont().getMotifLibre()%>" />
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>

			<tr id="paiementTr">

				<%
				    if (JadeStringUtil.isBlankOrZero(viewBean.getFactureRentePont().getSimpleFactureRentePont().getIdTiersAdressePaiement())) { 
					    AdresseTiersDetail adresse = PFUserHelper.getAdressePaiementAssure(viewBean.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getId(),
					            IPFConstantes.CS_DOMAINE_ADRESSE, JACalendar.todayJJsMMsAAAA());
					    if(adresse.getAdresseFormate() != null){
					        idAdressePaiementDefault = viewBean.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getId();
					    }
						} else { 
					            idAdressePaiementDefault = viewBean.getFactureRentePont().getSimpleFactureRentePont().getIdTiersAdressePaiement();
						} 
					
						if (JadeStringUtil.isBlankOrZero(viewBean.getFactureRentePont().getSimpleFactureRentePont().getIdApplicationAdressePaiement())) { 
						    idApplicationPaiementDefault = IPFConstantes.CS_DOMAINE_ADRESSE; 
						} else { 
						    idApplicationPaiementDefault = viewBean.getFactureRentePont().getSimpleFactureRentePont().getIdApplicationAdressePaiement(); 
						} 

					
						if (JadeStringUtil.isBlankOrZero(viewBean.getFactureRentePont().getSimpleFactureRentePont().getIdTiersAdresseCourrier())) { 
						   AdresseTiersDetail adresse = PFUserHelper.getAdresseAssure(viewBean.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getId(), JACalendar.todayJJsMMsAAAA());
					    if(adresse.getAdresseFormate() != null){
					        idAdresseCourrierDefault = viewBean.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getId();
					    }
						} else { 
						    idAdresseCourrierDefault = viewBean.getFactureRentePont().getSimpleFactureRentePont().getIdTiersAdresseCourrier(); 
						} 
					
						if (JadeStringUtil.isBlankOrZero(viewBean.getFactureRentePont().getSimpleFactureRentePont().getIdApplicationAdresseCourrier())) { 
						    idApplicationCourrierDefault = IPFConstantes.CS_DOMAINE_ADRESSE; 
						} else { 
				    	idApplicationCourrierDefault = viewBean.getFactureRentePont().getSimpleFactureRentePont().getIdApplicationAdresseCourrier(); 
					} 
				%>
				<td colspan="2" style="margin: 5px; padding: 5px;">
					<fieldset style="padding: 5px;">
						<legend>
							<ct:FWLabel key="JSP_PF_FACRP_D_VERSEMENT" />
						</legend>
						<table>
							<tr>
								<td width="200px"><ct:FWLabel
										key="JSP_PF_FACRP_D_ADRESSE_COURRIER" /></td>
								<td>
									<div
										data-g-adresse="mandatory:true, service:findAdresse,defaultvalue:¦<%=viewBean.getAdresseCourrierAssure()%>¦">
										<input type="hidden" id="idAdresseCourrier"
											class="avoirAdresse.idTiers"
											name="factureRentePont.simpleFactureRentePont.idTiersAdresseCourrier"
											value="<%=idAdresseCourrierDefault%>"> <input
											type="hidden" id="idDomaineApplicatifCourrier"
											class="avoirAdresse.idApplication"
											name="factureRentePont.simpleFactureRentePont.idApplicationAdresseCourrier"
											value="<%=idApplicationCourrierDefault%>">
									</div>
								</td>
								<td width="200px"><ct:FWLabel
										key="JSP_PF_FACRP_D_ADRESSE_PAIEMENT" /></td>
								<td>
									<div
										data-g-adresse="mandatory:true, service:findAdressePaiement,defaultvalue:¦<%=viewBean.getAdressePaiementAssure()%>¦">
										<input type="hidden" id="idAdressePaiement"
											class="avoirPaiement.idTiers"
											name="factureRentePont.simpleFactureRentePont.idTiersAdressePaiement"
											value="<%=idAdressePaiementDefault%>"> <input
											type="hidden" id="idDomaineApplicatif"
											class="avoirPaiement.idApplication"
											name="factureRentePont.simpleFactureRentePont.idApplicationAdressePaiement"
											value="<%=idApplicationPaiementDefault%>">
									</div> <input type="hidden" id="modificationFacture"
									name="modificationFacture" value="false" /> <input
									type="hidden" id="ancienMontantFacture"
									name="ancienMontantFacture" value="0.0" /> <input
									type="hidden" id="ancienMontantARembourserFacture"
									name="ancienMontantARembourserFacture" value="0.0" />
								</td>
							</tr>
							<tr>
								<td colspan="2">&nbsp;</td>
							</tr>
							<tr>
								<td><ct:FWLabel key="JSP_PF_FACRP_D_NUMERO_REFERENCE" /></td>
								<td><input type="text"
									id="numRefFacture"
									name="factureRentePont.simpleFactureRentePont.numRefFacture"
									size="60"
									value="<%=JadeStringUtil.toNotNullString(viewBean.getFactureRentePont().getSimpleFactureRentePont()
                    .getNumRefFacture())%>" />
								</td>
							</tr>
						</table>

					</fieldset>
				</td>
			</tr>

			<%
			    if (viewBean.hasDateValidation()) {
			%>
			<tr id="validationTr">
				<td colspan="2" style="margin: 5px; padding: 5px;">
					<fieldset style="padding: 5px;">
						<legend>
							<ct:FWLabel key="JSP_PF_FACRP_INFORMATIONS_CONCERNANT_VALIDATION" />
						</legend>
						<table>
							<tr>
								<td width="200"><ct:FWLabel
										key="JSP_PF_FACRP_DATE_DE_VALIDATION" /></td>
								<td><span><%=viewBean.getFactureRentePont().getSimpleFactureRentePont().getDateValidation()%></span>
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
			<%
			    }
			%>

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
    if (bBtnDelete) {
%><input class="btnCtrl" id="btnDelete" type="button"
	value="<%=btnDelLabel%>" onclick="del();">
<%
    }
%>
<%
 if (bBtnValidate) { 
 %> 
<input class="btnCtrl" id="btnValidate" type="button"
	value="<%=btnValLabel%>" onclick="if(validate()) action(COMMIT);">
<%
   } else if (viewBean.getIsRestitutionPossible()
            && objSession.hasRight("perseus.rentepont.factureRentePont", FWSecureConstants.ADD)) {
%>
<input class="btnCtrl" id="btnRestituer" type="button"
	value="Restituer la facture"
	onclick="if(validateRestituer()) action(COMMIT);" />
<%
    }
%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/bodyErrors.jspf"%>
<%-- tpl:insert attribute="zoneEndPage" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/footer.jspf"%>
