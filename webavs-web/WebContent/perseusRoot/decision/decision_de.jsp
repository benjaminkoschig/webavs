<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="ch.globaz.perseus.business.constantes.CSCaisse"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.jade.client.util.JadeNumericUtil"%>
<%@page import="ch.globaz.perseus.business.models.decision.CopieDecision"%>
<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatDemande"%>
<%@page import="ch.globaz.perseus.business.models.decision.AnnexeDecision"%>
<%@page import="ch.globaz.perseus.business.services.PerseusServiceLocator"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@page import="ch.globaz.perseus.business.constantes.CSChoixDecision"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeDecision"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatDecision"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFActions"%>
<%@page import="globaz.perseus.vb.decision.PFDecisionViewBean"%>
<%@page import="java.util.Date"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="java.util.Calendar"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page
	import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@ page language="java" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/decision/detail.css"/>
<%-- tpl:put name="zoneInit" --%>


<%
	//Les labels de cette page commence par le préfix "JSP_PF_DECISION_A"
	idEcran = "PRE0211";
	autoShowErrorPopup = true;

	PFDecisionViewBean viewBean = (PFDecisionViewBean) session.getAttribute("viewBean");
	String idDemande = request.getParameter("idDemande");
	boolean isNewProject = "true".equals(request.getParameter("isProject"));
	boolean isNewSuppressionVolontaire = "true".equals(request.getParameter("isNewSuppressionVolontaire"));
	viewBean.setNewProject(isNewProject);
	viewBean.setNewSuppressionVolontaire(isNewSuppressionVolontaire);
	boolean viewBeanIsNew="add".equals(request.getParameter("_method"));
	String eMailAddress = objSession.getUserEMail();
	PersonneEtendueComplexModel personneEtendue = viewBean.getDecision().getDemande().getSituationFamiliale().getRequerant()
			.getMembreFamille().getPersonneEtendue();

	String date = JadeDateUtil.getGlobazFormattedDate(new Date(JadeDateUtil.now()));
	
	//Calendar c = Calendar.getInstance();
	//String date = JadeDateUtil.getGlobazFormattedDate(c.getTime());
	
	bButtonUpdate = false;
	bButtonCancel = false;
	bButtonNew = false;
	bButtonValidate = false;
	if(objSession.hasRight("perseus", FWSecureConstants.REMOVE)){
		bButtonDelete = true;
	}else{
		bButtonDelete = false;
	}
	
	boolean bButtonEnregistrer = true;
	boolean bButtonPreValider = true;
	boolean bButtonValider = false;
	boolean bButtonValiderChoixProjet = false;
	if (CSEtatDecision.VALIDE.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsEtat())) {
		bButtonEnregistrer = false;
		bButtonPreValider = false;
		bButtonDelete = false;
	}
	boolean isPaiementOKPourValidation = PerseusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise();
	if (CSEtatDecision.PRE_VALIDE.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsEtat()) && isPaiementOKPourValidation) {
		//if (!objSession.getUserId().equals(viewBean.getDecision().getSimpleDecision().getUtilisateurPreparation()))
		// TODO Verifier qu'on a bien une gestion des droit qui fonctionne ici.
		if (objSession.hasRight("perseus.decision.validation", FWSecureConstants.UPDATE)) {
			bButtonValider = true;
		}
		bButtonPreValider = false;
	} else {
		bButtonValider = false;
	}
	
	//Controle de l'etat de la décision pour afficher ou non le bouton de validation du choix du requérant
	if (CSTypeDecision.PROJET.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsTypeDecision()) && CSEtatDecision.VALIDE.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsEtat()) && JadeNumericUtil.isEmptyOrZero(viewBean.getDecision().getSimpleDecision().getCsChoix())) {
		//Restriction des utilisateurs pour la CCVD
		if (CSCaisse.CCVD.getCodeSystem().equals(viewBean.getDecision().getDemande().getSimpleDemande().getCsCaisse())){
			if (objSession.hasRight("perseus.decision.validation", FWSecureConstants.UPDATE)) {
				bButtonValiderChoixProjet = true;
			}
		} else {
			//Aucune restriction pour l'AGLAU
			bButtonValiderChoixProjet = true;
		}
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%@ include file="/perseusRoot/ajax/javascriptsAndCSS.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="perseus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="perseus-optionsDecision">
	<ct:menuSetAllParams key="idDemande" value="<%=viewBean.getDecision().getDemande().getId()%>"/>
	<ct:menuSetAllParams key="idDossier" value="<%=viewBean.getDecision().getDemande().getDossier().getId() %>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getDecision().getId()%>"/>
</ct:menuChange>

<script type="text/javascript"
	src="<%=rootPath%>/scripts/jadeBaseFormulaire.js" />
</script>
<script type="text/javascript">

	var toggleButtonState = 0;//etat pour bouton toggle
	var annexesChanged = 0;//etat zone anexxe
	var copiesChanged = 0;//etat zone copies
	var messageDelete = "<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var servletContext = "<%=servletContext%>";
	var url = "<%=IPFActions.ACTION_DECISION%>";

	var theAction = {
	$userAction: null,
	s_action: "<%=IPFActions.ACTION_DECISION%>.modifier",
		init : function() {
			this.$userAction = $('[name=userAction]');
			this.bindEvent();
		},
		bindEvent : function() {
			var that = this;
			//Si annexes changés, set valeur hiden
			$('#btnPreValider').click(function() {
				that.preValider();
				$('#btnSupprimer').fadeOut(0);
				$('#btnPreValider').fadeOut(0);
				$('#btnValidate').fadeOut(0);
				$('#btnEnregistrer').fadeOut(0);
				$('#btnValiderChoixProjet').fadeOut(0);
				$('#btnImprimer').fadeOut(0);
				$('#btnAjouter').fadeOut(0);
				$('#addBtn').fadeOut(0);
				$('#btnDel').fadeOut(0);
				$('#btnNew').fadeOut(0);
				$('#btnUpd').fadeOut(0);
				$('#btnVal').fadeOut(0);
				$('#btnCan').fadeOut(0);
			});
			$('#btnEnregistrer').click(function() {
				that.enregistrer();
			});
			$('#btnValidate').click(function() {
				that.validate();
			});
			$('#btnValiderChoixProjet').click(function() {
				that.validateChoixDecision();
			});
			$('#btnImprimer').click(function() {
				that.imprimer();
			});
		},
		enregistrer : function() {
			$('#etatValidation').val('enregistree');
			this.defaultAction();
		},
		preValider : function() {
			$('#etatValidation').val('prevalidee');
			this.defaultAction();
		},
		validate : function() {
			var that = this;
			var messageBase =  "<%= objSession.getLabel("JSP_PF_DECISION_D_AVERTISSEMENT_VALIDATION")%>";
			var messageRetenue = "";
			var hauteurPopUpSelonMessage = 300;
			
			<%switch(viewBean.getMessageRetenuePourDemandePrecedante()){
				case RETENU_EXISTANTE : %>
					messageRetenue ="<br/><br/><div class=\"ui-state-highlight\" style=\"overflow:hidden; text-align: center;\">"+"<%=objSession.getLabel("JSP_PF_RETENUE_EXISTANTE_DEMANDE_PRECEDENTE")%>"+"</div>";					
					<% break; 
				case LA_RETENU_N_A_PU_ETRE_TROUVE : %>
					messageRetenue ="<br/><br/><div class=\"ui-state-error\" style=\"overflow:hidden; text-align: center;\">"+"<%=objSession.getLabel("JSP_PF_RETENUE_PAS_CALCULE")%>"+"</div>";
					hauteurPopUpSelonMessage += 60;
					<% break;
				case PAS_DE_RETENU : %>
					messageRetenue ="";
					<% break; 
			}%>
			
			var message = messageBase + messageRetenue;
						
					
			$("<div>"+message+"</div>" ).dialog({
				resizable: false,
				height:hauteurPopUpSelonMessage,
				width:500,
				modal: true,
				buttons: {
					"Oui" : function() {		
						$('#btnSupprimer').fadeOut(0);
						$('#btnPreValider').fadeOut(0);
						$('#btnValidate').fadeOut(0);
						$('#btnEnregistrer').fadeOut(0);
						$('#btnValiderChoixProjet').fadeOut(0);
						$('#btnImprimer').fadeOut(0);
						$('#btnAjouter').fadeOut(0);
						$('#addBtn').fadeOut(0);
						$('#btnDel').fadeOut(0);
						$('#btnNew').fadeOut(0);
						$('#btnUpd').fadeOut(0);
						$('#btnVal').fadeOut(0);
						$('#btnCan').fadeOut(0);
						$( this ).dialog( "close" );
						<% if (!JadeStringUtil.isEmpty(viewBean.getDecision().getDemande().getDossier().getDossier().getAnnoncesChangements())) { %>
						$( "#dialog-confirm-vider-annonces" ).dialog({
							resizable: false,
							height:250,
							width:500,
							modal: true,
							buttons: {
								"Oui" : function() {
									$( this ).dialog( "close" );
									$('#viderAnnoncesChangement').val('true');
									$('#etatValidation').val('validee');
									that.defaultAction();
								},
								"Non" : function() {
									$( this ).dialog( "close" );
									$('#viderAnnoncesChangement').val('false');
									$('#etatValidation').val('validee');
									that.defaultAction();
								}
							}
						});
					<% } else { %>
						$('#viderAnnoncesChangement').val('false');
						$('#etatValidation').val('validee');
						that.defaultAction();
					<% } %>
					},
					"Non" : function() {
						$( this ).dialog( "close" );
					}
				}
			});
		},

		validateChoixDecision : function() {
			
			var isReponsePositive = $("#choixRequerantZoneRadio [value='55026002']").prop("checked")
			var isNonAReponsePositive = true;
			var that = this;
			
			if((isReponsePositive == true) && (<%= !viewBean.getHasCreancier() %>)){
				$("<div align=\"center\">"+"<%= objSession.getLabel("JSP_PF_DECISION_VALIDATION_CHOIX_DECISION") %>" + "</div>").dialog({
					resizable: false,
					height: 250,
					width:500,
					modal:true,
					buttons: {
						"Oui": function() {
							isNonAReponsePositive = false;
							$( this ).dialog( "close");
							
							that.$userAction.val("perseus.creancier.creancier.afficher");
							document.forms[0].submit();
						},
						"Non": function() {
							$( this ).dialog( "close");
							that.validateChoixRequerant();
						}
					}
				})
			}else{
				this.validateChoixRequerant();
			}
		},
		
		validateChoixRequerant : function() {
			if (confirm("<%= objSession.getLabel("JSP_PF_DECISION_D_AVERTISSEMENT_VALIDATION_CHOIX_DECISION") %>")) {
				$('#etatValidation').val('valideechoisie');
				this.defaultAction();
			}
		},
		imprimer : function() {
			this.$userAction.val("perseus.decision.decisionProcess.executer");
			document.forms[0].submit();
		},
		defaultAction : function() {
			if($('#annexesIsChanged').attr('value')=="1"){
				getSelectAsString();		
			}
			if(copiesChanged==1){
				getCopiesAsString();
			}
			
			this.$userAction.val(this.s_action);
			document.forms[0].submit();
			//return false;
		}
	};

	//***********************nombre de caractères maximum pour la zone remarques à insérer dans le document********************
	var maxlength_textarea = function(){
			$txtarea = $('#remarqueTextArea');
			var max = 136;		//Nombre de caractères maximum pour notre textarea
			var countEnter = 0;
			
			$txtarea.keypress(function(e) {				
				if(e.keyCode == $.ui.keyCode.ENTER){
					return false;					
				}else{
					$txtarea.keyup(function() {
						var nombreCaractere = $txtarea.val().length;		
						if(nombreCaractere > max){
							$txtarea.val($txtarea.val().substring(0, max));					
						}
					});

					$txtarea.change(function() {
						var nombreCaractere = $txtarea.val().length;		
						if(nombreCaractere > max){
							$txtarea.val($txtarea.val().substring(0, max));					
						}
					});
				}
			});
			

		};
	
	//***********************Initialisation zone annexe ****************************
	var initAnnexeZone = function (){
		
		var chmAjouter = $('#chmAjouter');

		//init bouton ajouter
		$('#btnAjouter').click(function(){
			
			//Si chaine vide, alert
			if(chmAjouter.val()==''){
				alert("Text empty");
			}
			else{
				insertAnnexe(chmAjouter.val(),chmAjouter.val());
			}
		});
		
		//init bouton supprimer
		$('#btnSupprimer').click(function(){
			var csCode = $('#listeAnnexes :selected').attr('value');
			deleteAnnexe(Number(csCode));
		});
	};

	//********************************** INSERT ANNEXE ****************
	var insertAnnexe  = function (annexeLabel,type){
		var chmAjouter = $('#chmAjouter');
		
		var chaine = '<option value="'+type+'">' + annexeLabel +'</option>';
		$(chaine).appendTo('#listeAnnexes');
		chmAjouter.val('');
		setAnnexesChange();
	};
	//**********************************************************************

	//********************************** DELETE ANNEXE *********************
	var deleteAnnexe = function (type){		
		$('#listeAnnexes :selected').remove();
		setAnnexesChange();
	};
	//**********************************************************************
	//************************* Annexes change ***********************************
	var setAnnexesChange = function () {
		var annexesIsChanged = $('#annexesIsChanged');
		annexesIsChanged.attr('value','1');
	};
	//*******************************************************************************

	//******************** Traitement zone annexe submit ***************************
	var getSelectAsString = function () {
		
		var chaineToSend = "";
		var separator = "\n";
		
		$('#listeAnnexes').find('option').each(function() { 
			chaineToSend += $(this).text();			
			chaineToSend += separator;
		}); 

		//set champ hidden
		$('#listeAnnexesString').attr('value',chaineToSend);
	};
	//*******************************************************************************

//*************************** Traitement zone copies submit **********************
var getCopiesAsString = function (){
	
	var idTiersSeparator = '-';
	//iteration sur les ligne du tableau des copies, exclu th
	$('#tableCopies tr:gt(0)').each(function(){
		var chaine = $('[name=idTiersCopies]',this).val();
		//Set les champs caché
		$('[name=copies]',this).attr('value',chaine);
	});
};
//***********************************************************************************
//*******************************Init zone widget copies ************************
var initZoneWidgetCopies = function () {
	//Valeur init zone widget Copies
	$('#widgetTiers').hide();
	$('#widgetAdmin').show();
	$('#addBtn').hide();
	
	$('#radioTiers').click(function(){
		$('#widgetAdmin').hide();
		$('#widgetTiers').val('');
		$('#widgetTiers').show();
		$('#widgetTiers').focus();
		
	});
	
	$('#radioAdmin').click(function(){
		$('#widgetTiers').hide();
		$('#widgetAdmin').val('');
		$('#widgetAdmin').show();
		$('#widgetAdmin').focus();
	});
		
	$('.btnDelete').click(function(){
		$(this).parent().parent().remove();	
		copiesChanged = 1;
		$('#copiesIsChanged').attr('value','1');
	});

	$('.checkBoxCopies').click(function(){
		//Set le changement dans copies
		copiesChanged = 1;
		$('#copiesIsChanged').attr('value','1');
	});
	
	//Evenement onclick bouton add dest copies
	$('#addBtn').click(function(){
		manipTable.addRow();
	});
};

//objet de manipulation de la table
var manipTable = {
		 designation:'',
		 //ajout d'une ligne au tableau
		 addRow:function(){
			//Construction de la requete
			var chaine = ''; 
			
			//test quel service
			if($('#radioAdmin').is(':checked')){
				//set les champs cachés
				inputValue = $('#widgetAdmin').val();
				idTiersValue = $('.idAdmin1').val();
				//alert(idTiersValue);
				//designation = designation1;//alert(designation1);
			}
			if($('#radioTiers').is(':checked')){
				inputValue = $('#widgetTiers').val();
				idTiersValue = $('.idTiers1').val();
				//alert(idTiersValue);
				//designation = designation1 +" "+designation2;
			}
			
			
			chaine+="<tr><td>"+this.designation+"</td>";
			chaine+='<td><img class="btnDelete" src="'+servletContext+'/images/moins.png"/></td>';
			chaine +='<input type="hidden" name="idTiersCopies" value="'+idTiersValue+'"/>';
			chaine +='<input type="hidden" name="copies" value=""/></tr>';
			
			
			//ajout de la ligne dans le tableau
			$(chaine).appendTo('#tableCopies');
			//on masque les boutons et init valeur widget
			$('#addBtn').hide();
			$('#widgetAdmin').val('');
			$('#widgetTiers').val('');
			//Set le changement dans copies
			copiesChanged = 1;
			$('#copiesIsChanged').attr('value','1');
			//evenement suppression
			$('.btnDelete').click(function(){
				$(this).parent().parent().remove();	
				copiesChanged = 1;
				$('#copiesIsChanged').attr('value','1');
			});
			$('.checkBoxCopies').click(function(){
				copiesChanged = 1;
				$('#copiesIsChanged').attr('value','1');
			});
		},
		//fin manipTable
		initEvents:function(){	
		}
};
//*******************************************************************************
	$(function() {
		theAction.init();
		initAnnexeZone();
		maxlength_textarea();
		initZoneWidgetCopies();
		<% if (CSEtatDecision.VALIDE.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsEtat()) && !bButtonValiderChoixProjet) { %>
		jsManager.addAfter(function (){
			$(':input,.btnDelete').not('#eMailAddress,#btnImprimer,#isSendToGed, :hidden').each(function (){
				$(this).prop('disabled', true);
				globazNotation.utilsInput.changeColorDisabled($(this));
			});
		},'Disabling inputs fields to avoid modifications');
		<% } %>
		<% if (CSEtatDecision.VALIDE.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsEtat()) && bButtonValiderChoixProjet) { %>
		jsManager.addAfter(function (){
			$(':input,.btnDelete').not('#eMailAddress,#btnImprimer,#dateChoixProjet,#dateSurDecision,#btnValiderChoixProjet,#isSendToGed,[name="decision.simpleDecision.csChoix"], :hidden').each(function (){
				$(this).prop('disabled', true);
				globazNotation.utilsInput.changeColorDisabled($(this));
			});
		},'Disabling inputs fields to avoid modifications');
		<% } %>
/*		$("[name=decision\\.simpleDecision\\.utilisateurPreparation]").attr("disabled", true);
		if (!viewBean.getDecision().getSimpleDecision().getCsEtat().equals(CSEtatDecision.ENREGISTRE.getCodeSystem())) {
		}*/

		<% if (viewBean.getHasCreancier()) { %>
			globazNotation.utils.consoleInfo("<div style='height:120px;'><br/><%= objSession.getLabel("JSP_PF_DECISION_D_AVERTISSEMENT_CREANCIERS") %></div>", "<%= objSession.getLabel("JSP_PF_DECISION_D_AVERTISSEMENT_CREANCIERS_TITRE") %>", true);
		<% } %>
		
		$( "#dialog-confirm-vider-annonces" ).hide();
	});
	
	
	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_PF_DECISION_D_TITRE" />

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
<tr>
	<td>
	<input type="hidden" name="etatValidation" id="etatValidation" />
	<input type="hidden" name="viderAnnoncesChangement" id="viderAnnoncesChangement" />
	<table>
		<tr>
			<td><label style="font-weight: bold;" for="eMailAddress"><ct:FWLabel
				key="JSP_PF_DECISION_A_ADRMAIL" /></label>&nbsp;</td>
			<td><input type="text" name="eMailAddress" id="eMailAddress"
				value="<%=eMailAddress%>" class="libelleLong">&nbsp;</td>
			<td width="30px"></td>
			<TD><label style="font-weight: bold;" for="dateDocument"><ct:FWLabel
				key="JSP_PF_DECISION_A_DATE_SUR_DOC" /></label>&nbsp;</TD>
			<TD><input type="text" class="clearable"
				name="decision.simpleDecision.dateDocument" value="<%=viewBean.getDecision().getSimpleDecision().getDateDocument()%>"
				data-g-calendar="mandatory:true" />&nbsp;</TD>
			<td><input type="hidden" name="decision.simpleDecision.dateDocument" value="<%=viewBean.getDecision().getSimpleDecision().getDateDocument()%>"/></td>
			
		</tr>
		<tr>
			<td><label style="font-weight: bold;" for="utilisateurPreparation"><ct:FWLabel key="JSP_PF_DOS_D_GESTIONNAIRE" /></label></TD>
			<td>
			<ct:FWListSelectTag 			
				name="decision.simpleDecision.utilisateurPreparation"
				data="<%=globaz.perseus.utils.PFGestionnaireHelper.getResponsableData(objSession)%>"
				notation="data-g-select='mandatory:true'"
				defaut="<%=(JadeStringUtil.isBlank(viewBean.getDecision().getSimpleDecision().getUtilisateurPreparation())) ? viewBean.getGestionnaire():viewBean.getDecision().getSimpleDecision().getUtilisateurPreparation()%>" />
			</td>
			<td />
			<% if (CSTypeDecision.SUPPRESSION.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsTypeDecision()) || (isNewSuppressionVolontaire && viewBeanIsNew)) { %>
			<td><label style="font-weight: bold;" for="utilisateurPreparation"><ct:FWLabel key="JSP_PF_DECISION_A_DATE_SUPPRESSION" /></label></td>
			<td><input type="text" class="clearable"
				name="decision.simpleDecision.dateSuppression" value="<%=viewBean.getDecision().getSimpleDecision().getDateSuppression()%>"
				data-g-calendar="mandatory:true" />&nbsp;</td>
			<% } %>
		</tr>
	</table>
	</td>
</tr>
<TR>
	<TD colspan="6"><br>
	</TD>
</TR>
<TR>
	<TD colspan="6">
	<hr>
	</TD>
</TR>
<tr>
	<td>
	<table>
		<tr>
			<td><label style="font-weight: bold;" for="idDemande"><ct:FWLabel key="JSP_PF_DECISION_A_TYPE_DECISION" /></label></td>
			<td><label style="font: italic;"> <%=viewBean.getTypeDecision()%></label></td>
		</tr>
		<TR>
			<TD height="10"></TD>
		</TR>
		<tr>
			<td><label style="font-weight: bold;" for="idDemande"><ct:FWLabel key="JSP_PF_DECISION_A_NODEMANDE" /></label></td>
			<td><%=viewBean.getDecision().getDemande().getId()%>
			<input type="hidden" name="decision.simpleDecision.idDemande" value="<%=viewBean.getDecision().getDemande().getId()%>"/>
			<input type="hidden" name="idDemande" value="<%=viewBean.getDecision().getDemande().getId()%>"/>
			</td>
		</tr>
		<TR>
			<TD height="10"></TD>
		</TR>
		<tr>
			<td><label style="font-weight: bold;"><ct:FWLabel key="JPS_PF_DECISION_A_PERDIODE"/></label> </td>
			<td colspan="2">
				<ct:FWLabel key="JSP_PF_DECISION_A_PERIODE_DU"/>
				<b><%=" "+viewBean.getDecision().getDemande().getSimpleDemande().getDateDebut() + " " %></b>
				<ct:FWLabel key="JSP_PF_DECISION_A_PERIODE_AU"/>
				<b><%=" "+ viewBean.getDecision().getDemande().getSimpleDemande().getDateFin() %></b>
			</td>
		</tr>
		<TR>
			<TD height="10"></TD>
		</TR>
		<tr>
			<td valign="top"><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DECISION_A_REQUERANT" />&nbsp;</label></td>
			<td valign="top"><%=viewBean.getDetailAssure()%></td>
		</tr>
<% if (viewBean.getDecision().getDemande().getSimpleDemande().getFromRI()) { %>
		<tr>
			<td><label style="font-weight: bold;" for="montantToucheAuRI"><ct:FWLabel key="JSP_PF_DECISION_A_MONTANT_TOUCHE_AU_RI" /></label>&nbsp;
			</td>
			<td><input type="text" name="decision.simpleDecision.montantToucheAuRI" value="<%=viewBean.getDecision().getSimpleDecision().getMontantToucheAuRI()%>" data-g-amount=" ">&nbsp;
			</td>
		</tr>
<% } %>
		<TR>
			<TD height="15"></TD>
		</TR>
		<tr>
			<td valign="top"><label style="font-weight: bold;" for="adresseCourrier"><ct:FWLabel
				key="JSP_PF_DECISION_A_ADRESSE_COURRIER" />&nbsp;</label></td>
			<td valign="top">
				<div name="adresseCourrier" data-g-adresse="mandatory:true,service:findAdresse,defaultvalue:¦<%=viewBean.getAdresseCourrier()%>¦">
    				<input type="hidden" id="idTiersAdresseCourrier" class="avoirAdresse.idTiers" name="decision.simpleDecision.idTiersAdresseCourrier" value="<%=viewBean.getDecision().getSimpleDecision().getIdTiersAdresseCourrier() %>">
    				<input type="hidden" id="idDomaineApplicatifAdresseCourrier" class="avoirAdresse.idApplication" name="decision.simpleDecision.idDomaineApplicatifAdresseCourrier" value="<%=viewBean.getDecision().getSimpleDecision().getIdDomaineApplicatifAdresseCourrier() %>">
				</div>
			</td>
<% if (		CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsTypeDecision()) 
				|| CSEtatDemande.ENREGISTRE.getCodeSystem().equals(viewBean.getDecision().getDemande().getSimpleDemande().getCsEtatDemande()) 
				|| CSTypeDecision.SUPPRESSION.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsTypeDecision()) 
				|| isNewSuppressionVolontaire 
				|| CSTypeDecision.NON_ENTREE_MATIERE.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsTypeDecision())){ %>
			<td />
			<td />
<% } else { %>
			<td valign="top"><label style="font-weight: bold;"><ct:FWLabel
				key="JSP_PF_DECISION_A_ADRESSE_PAIEMENT" />&nbsp;</label></td>
			<td valign="top">
				<div data-g-adresse="mandatory:true,service:findAdressePaiement,defaultvalue:¦<%=viewBean.getAdressePaiement()%>¦">
    				<input type="hidden" id="idTiersAdressePaiement" class="avoirPaiement.idTiers" name="decision.simpleDecision.idTiersAdressePaiement" value="<%=viewBean.getDecision().getSimpleDecision().getIdTiersAdressePaiement() %>"> 
					<input type="hidden" id="idDomaineApplicatifAdressePaiement" class="avoirPaiement.idApplication" name="decision.simpleDecision.idDomaineApplicatifAdressePaiement" value="<%=viewBean.getDecision().getSimpleDecision().getIdDomaineApplicatifAdressePaiement() %>">
				</div>
			</td>
<% } %>
		</tr>
<% if (		(!CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsTypeDecision()) 
				&& !CSEtatDemande.ENREGISTRE.getCodeSystem().equals(viewBean.getDecision().getDemande().getSimpleDemande().getCsEtatDemande()) 
				&& !JadeStringUtil.isBlank(viewBean.getDecision().getSimpleDecision().getCsTypeDecision()) 
				&& !CSEtatDemande.ENREGISTRE.getCodeSystem().equals(viewBean.getDecision().getDemande().getSimpleDemande().getCsEtatDemande()) 
				&&  !CSTypeDecision.NON_ENTREE_MATIERE.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsTypeDecision())) 
			|| isNewProject) { %>
		<tr>
			<td colspan="2"><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DECISION_A_PCFACCORDEE"/></label></td>
		</tr>
		<tr>
			<td colspan="2">
				<table bgcolor="white" cellpadding="5">
					<thead>
						<th><ct:FWLabel key="JSP_PF_PCFACCORDEE_DATE_CALCUL"/></th>
						<th><ct:FWLabel key="JSP_PF_PCFACCORDEE_PRESTATION_MENSUELLE"/></th>
						<th><ct:FWLabel key="JSP_PF_PCFACCORDEE_EXCEDANT_REVENU"/></th>
					</thead>
					<tbody>
						<td><%=viewBean.getPcfaccordee().getSimplePCFAccordee().getDateCalcul() %></td>
						<td data-g-amountformatter=" "><%=viewBean.getPcfaccordee().getSimplePCFAccordee().getMontant() %></td>
						<td data-g-amountformatter=" "><%=viewBean.getPcfaccordee().getSimplePCFAccordee().getExcedantRevenu() %></td>
					</tbody>
				</table>
			</td>
			<td colspan="2">
				<label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DECISION_A_ETAT_COMPTABILISATION" />&nbsp;</label><br/>
				<%=viewBean.getEtatComptabilisation() %>
			</td>
		</tr>
		<TR>
			<TD colspan="6"><br>
			</TD>
		</TR>
		<tr>
			<td colspan="2"><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DECISION_A_PERSONNES_DANS_CALCUL"/></label></td>
		</tr>
		<tr>
			<td colspan="4" valign="top">
				<%=viewBean.getListWebPersonnesDansCalcul() %>
			</td>
		</tr>
<% } %>
	</table>
	</td>
</tr>
<!-- Le bloc ci-dessous affiche les champs 'Remarques à insérer dans le document', selon conditions   -->
<%if(viewBean.champRemarqueVisible()) {%>
<!-- ATTENTION : Pour la modification d'affichage de ce champ, il faut modifier la methode 'champRemarqueVisible' dans PFDecisionViewBean et dans le checker 'Decisionchecker' -->
&nbsp;
&nbsp;
<TR>
	<TD colspan="6">
	<hr>
	</TD>
</TR>
<tr>
	<td>
	<table>
		<!-- Si la décision est en état validée, le titre 'Remarque à insérer' change en 'Remarque inséré -->
		<%if (!CSEtatDecision.VALIDE.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsEtat())){ %>
		<tr>
			<td colspan="2"><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DECISION_A_REMARQUE_A_INSERER_DANS_DOCUMENT"/></label></td>
		</tr>
		<%} else {%>
		<tr>
			<td colspan="2"><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DECISION_A_REMARQUE_INSERE_DANS_DOCUMENT"/></label></td>
		</tr>
		<%} %>
		<!-- Si le texte à insérer ne contient pas de paragraphe précédent, le bloc ci-dessous ne s'affiche pas. -->
		<%if(!JadeStringUtil.isEmpty(viewBean.getParagraphePrecedent())) {%>
			<TR>
				<TD colspan="6"><br></TD>
			</TR>
			<!-- Si la décision n'est pas validée, le paragraphe précédent la remarque insérée, apparait -->
			<%if (!CSEtatDecision.VALIDE.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsEtat())){ %>
	 			<tr>
		 			<td><u><i><ct:FWLabel key="JSP_PF_DECISION_A_PARAGRAPHE_PRECEDENT"/></i></u></td>
		 		</tr>
		 		<tr>
					<td valign="top" style="height: 1cm; width: 20.7cm"><i><%=viewBean.getParagraphePrecedent()%></i></td>
				</tr>
			<%}%>
		<%}%>   
		<tr>
			<td valign="top">			
				<textarea  name="decision.simpleDecision.remarqueUtilisateur" id="remarqueTextArea" style="height: 2cm; width: 20.7cm;" rows="" cols=""><%= viewBean.getDecision().getSimpleDecision().getRemarqueUtilisateur() %></textarea>
			</td>
		</tr>
	</table>
	</td>
</tr>
<%} %>
<!-- Le bloc ci-dessous affiche les options liées à un 'Ajout d'aides catégorielles', selon conditions   -->
<%if (viewBean.champAjoutAideCategorielle()) {%>
&nbsp;
&nbsp;
<TR>
	<TD colspan="6">
	<hr>
	</TD>
</TR>
<tr>
	<td>
	<table>
		<tr>
			<td colspan="2"><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DECISION_A_TYPES_AJOUT_AIDES_CATEGORIELLES"/></label></td>
		</tr>
		<TR>
			<TD colspan="12"></TD>
		</TR>
		<tr>
			<td>
				<input type="checkbox" name="boolPensionAlimentaire" <%=(viewBean.getDecision().getSimpleDecision().getPensionAlimentaire() != null && viewBean.getDecision().getSimpleDecision().getPensionAlimentaire()) ? "checked='checked'" : ""%> /><label style="font-weight: normal;"><ct:FWLabel key="JSP_PF_DECISION_A_AIDE_A_LA_PENSION_ALIMENTAIRE"/></label><br>
				<input type="checkbox" name="boolAideAuLogement" <%=(viewBean.getDecision().getSimpleDecision().getAideAuLogement() != null && viewBean.getDecision().getSimpleDecision().getAideAuLogement()) ? "checked='checked'" : ""%> /><label style="font-weight: normal;"><ct:FWLabel key="JSP_PF_DECISION_A_AIDE_INDIVIDUELLE_AU_LOGEMENT"/></label><br>
				<input type="checkbox" name="boolAideAuxEtudes" <%=(viewBean.getDecision().getSimpleDecision().getAideAuxEtudes() != null && viewBean.getDecision().getSimpleDecision().getAideAuxEtudes()) ? "checked='checked'" : ""%>/><label style="font-weight: normal;"><ct:FWLabel key="JSP_PF_DECISION_A_AIDE_AUX_ETUDES_ET_A_LA_FORMATION_PROFESSIONNELLE"/></label>
			</td>
		</tr>
	</table>
	</td>
</tr>

<%} %>



<TR>
	<TD colspan="6"><br>
	</TD>
</TR>
<TR>
	<TD colspan="6">
	<hr>
	</TD>
</TR>
<tr>
	<td>
	<table>
		<tr>
			<td valign="top"><label style="font-weight: bold;"><ct:FWLabel
				key="JSP_PF_DECISION_A_REMARQUES" /></label>
			</td>
			<td><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DECISION_A_ANNEXES"/></label></td>
		</tr><!--
		<tr>
			<td valign="top"><input type="checkbox"><LABEL><ct:FWLabel
				key="JSP_PF_DECISION_A_CASE_AJOUTER_REMPLACER" /></LABEL></td>
		</tr>
		--><tr>
			<td valign="top"><textarea name="decision.simpleDecision.remarquesGenerales" style="height: 2cm; width: 10cm;" rows="" cols=""><%= viewBean.getDecision().getSimpleDecision().getRemarquesGenerales() %></textarea>
			<td>
				<select id="listeAnnexes" style="width:400;" size="6">
					<%
					for(AnnexeDecision annexe:viewBean.getDecision().getListAnnexes())
					{
					%>
						<option value="<%=annexe.getSimpleAnnexeDecision().getId()%>"><%=annexe.getSimpleAnnexeDecision().getDescriptionAnnexe() %></option>
					<%
					}
					%>
				</select>
				<input type="hidden" name="listeAnnexesString" id="listeAnnexesString" value=""/>
				<input type="hidden" name="annexesIsChanged" id="annexesIsChanged" value="0"/><br/>
				<input id="btnSupprimer" type="button" value="Supprimer"/><br /><br />
				<input id="chmAjouter" style="width:400;" type="text" /><br/>
				<input id="btnAjouter" type="button" value="Ajouter" />
			</td>
		</tr>
	</table>
		<input type="hidden" name="decisionId" value="<%= viewBean.getDecision().getId() %>" />
<!--		<input name="btImprimer" type="button" class="btnCtrl" onclick="imprimer()" value="Imprimer" />-->
		 	
	</td>
</tr>

<tr>
	<td colspan="6">
	<hr>
	</td>
</tr>
<tr>
	<td>
	<table>
		<tr>
			<td colspan="2">
				<label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DECISION_A_COPIES" /></label>
			</td>
		</tr>
		<tr>
		</tr>
		<tr>
			<td valign="top">
				<table id="tableCopies" bgcolor="white" cellpadding="5">
				<tr>
					<th><ct:FWLabel key="JSP_PF_DECISION_A_DESTINATAIRE"/></th>
					<th>&nbsp;</th>
				</tr>
				<% for(CopieDecision copie:viewBean.getDecision().getListCopies()){
					%>
					<tr>
						<td><%=copie.getDesignation1()+" "+copie.getDesignation2() %></td>
						<td><img class="btnDelete" src="<%=servletContext%>/images/moins_lock.png"/></td>
						<input type="hidden" name="idTiersCopies" value="<%= copie.getSimpleCopieDecision().getIdTiers() %>"/>
						<input type="hidden" name="copies" value=""/>
					</tr>
					
					<%
				} %>
			</table>
			</td>
			<td>
			<div id="widgetCopiesZone">
				<div id="radioZone">
					<input type="radio" id="radioAdmin" name="groupe1" checked="checked"/><ct:FWLabel key="JSP_PF_DECISION_A_ADMINISTRATION"/> <br/>
					<input type="radio" id="radioTiers" name="groupe1"/><ct:FWLabel key="JSP_PF_DECISION_A_TIERS"/><br />
					<input type="hidden" name="copiesIsChanged" id="copiesIsChanged" value=""/>
				</div>
				<div id="widgetZone">
					<input type="hidden" class="idTiers1" />
					<ct:widget id='widgetTiers' name='widgetTiers' styleClass=" selecteurHome">
						<ct:widgetService methodName="find" className="<%=ch.globaz.pyxis.business.service.PersonneEtendueService.class.getName()%>">
						<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PF_DECISION_W_NOM"/>								
						<ct:widgetCriteria criteria="forDesignation2Like" label="JSP_PF_DECISION_W_PRENOM"/>
						<ct:widgetCriteria criteria="forNumeroAvsActuel" label="JSP_PF_DECISION_W_AVS"/>									
						<ct:widgetCriteria criteria="forDateNaissance" label="JSP_PF_DECISION_W_NAISS"/>								
						<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
						<ct:widgetJSReturnFunction>
							<script type="text/javascript">
								function(element){
									$(this).parents('#widgetZone').find('.idTiers1').val($(element).attr('tiers.idTiers'));
									$(this).parents('#widgetZone').find('.idTiers1').trigger('change');
									this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2')+' '+$(element).attr('personneEtendue.numAvsActuel')+' '+$(element).attr('personne.dateNaissance');
									manipTable.designation = $(element).attr('tiers.designation1') +' '+ $(element).attr('tiers.designation2');
									$('#addBtn').show();
									$('#addBtn').focus();
								}
							</script>										
						</ct:widgetJSReturnFunction>
						</ct:widgetService>
					</ct:widget>
				
					<ct:widget id='widgetAdmin' name='widgetAdmin' styleClass=" selecteurHome">
						<input type="hidden" class="idAdmin1" />
						<ct:widgetService methodName="find" className="<%=ch.globaz.pyxis.business.service.AdministrationService.class.getName()%>">								
						<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PF_DECISION_W_NOM"/>									
						<ct:widgetCriteria criteria="forCodeAdministrationLike" label="JSP_PF_DECISION_W_CODE"/>
						<ct:widgetCriteria criteria="forGenreAdministration" label="JSP_PF_DECISION_W_GENRE"/>								
						<ct:widgetCriteria criteria="forCanton" label="JSP_PF_DECISION_W_CANTON"/>								
						<ct:widgetLineFormatter format="#{tiers.designation1} #{admin.codeAdministration} #{admin.genreAdministration} #{admin.canton}"/>
						<ct:widgetJSReturnFunction>
							<script type="text/javascript">
								function(element){
									$(this).parents('#widgetZone').find('.idAdmin1').val($(element).attr('admin.idTiersAdministration'));
									$(this).parents('#widgetZone').find('.idAdmin1').trigger('change');
									this.value=$(element).attr('admin.codeAdministration')+' '+$(element).attr('tiers.designation1')+' '+$(element).attr('admin.genreAdministration')+' '+$(element).attr('admin.canton');
									manipTable.designation  = $(element).attr('tiers.designation1');
									$('#addBtn').show();
									$('#addBtn').focus();
								}
							</script>										
						</ct:widgetJSReturnFunction>
						</ct:widgetService>
					</ct:widget>
				</div>
				<img id="addBtn" src="<%=servletContext%>/images/add.png""/>
			</div>
			</td>
		</tr>
	</table>
	
	<div id="dialog-confirm-vider-annonces" title='<ct:FWLabel key="JSP_PF_DECISION_VIDER_ANNONCES"/>'>
		<p><pre><%=JadeStringUtil.toNotNullString(viewBean.getDecision().getDemande().getDossier().getDossier().getAnnoncesChangements()) %></pre></p>
	</div>
	
	</td>
</tr>

<% if (CSTypeDecision.PROJET.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsTypeDecision()) && CSEtatDecision.VALIDE.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsEtat())) { %>
<tr>
	<td colspan="6">
	<hr>
	</td>
</tr>
<tr>
	<td>
	<table>
		<tr>
			<td valign="top">
				<label style="font-weight: bold;" for="csChoix"><ct:FWLabel key="JSP_PF_DECISION_A_CHOIX_REQUERANT" /></label>&nbsp;
			</td>
			<td valign="top" colspan="2" id="choixRequerantZoneRadio">
				<ct:FWCodeRadioTag
					codeType="<%=ch.globaz.perseus.business.constantes.IPFConstantes.CSGROUP_CHOIX_DECISION%>"
					name="decision.simpleDecision.csChoix"
					wantBlank="false"
					defaut="<%=JadeStringUtil.toNotNullString(viewBean.getDecision().getSimpleDecision().getCsChoix())%>"/>&nbsp;
			</td>
			<td colspan="2">&nbsp;</td>
			<td valign="top">
				<label style="font-weight: bold;" for="dateChoixProjet"><ct:FWLabel key="JSP_PF_DECISION_A_DATE_CHOIX_PROJET" /></label>&nbsp;
			</td>
			<td valign="top" colspan="2">
				<input type="text" class="clearable"
				name="decision.simpleDecision.dateChoix" value="<%=viewBean.getDecision().getSimpleDecision().getDateChoix()%>"
				id="dateChoixProjet"
				data-g-calendar="mandatory:true" />&nbsp;
			</td>
			<%if(CSEtatDecision.VALIDE.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsEtat()) && bButtonValiderChoixProjet) {%>
			<td>&nbsp;</td>
			<td valign="top">
				<label style="font-weight: bold;" for="dateSurDecision"><ct:FWLabel key="JSP_PF_DECISION_A_DATE_SUR_DECISION_SUITE_AU_PROJET" /></label></td>
			<td>&nbsp;</td>
			<td valign="top">
				<input type="text" class="clearable" 
				name="dateSurDecision" value="<%=viewBean.getDateSurDecision()%>"
				id="dateSurDecision"
				data-g-calendar="mandatory:true" />&nbsp;
			</td>
			<%} %>
		</tr>
	</table>
	</td>
	<td>
	
	</td>
</tr>
<% } %>
<% if(CSEtatDecision.VALIDE.getCodeSystem().equals(viewBean.getDecision().getSimpleDecision().getCsEtat())&&  viewBean.hasGed(viewBean.getDecision().getDemande().getSimpleDemande().getCsCaisse())) { %>
<tr>
	<td colspan="6">
	<hr>
	</td>
</tr>
<tr>
	<td align="center"><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DECISION_GED"/></label>
	<input type="checkbox" name="isSendToGed" id="isSendToGed" <%=viewBean.getIsSendToGed()%>></td>
</tr>
<% } %>

	
<%
if(!viewBean.getDecision().getSimpleDecision().getCsEtat().equals(CSEtatDecision.VALIDE.getCodeSystem())){
	switch(viewBean.getMessageRetenuePourDemandePrecedante()){
				case RETENU_EXISTANTE : %>
					<tr>
						<td colspan="6">
							<div data-g-boxmessage="type:WARN" align="right"> <ct:FWLabel key="JSP_PF_RETENUE_EXISTANTE_DEMANDE_PRECEDENTE" /></div>
						</td>
					</tr>					
					<% break; 
				case LA_RETENU_N_A_PU_ETRE_TROUVE : %>
					<tr>
						<td colspan="6">
							<div data-g-boxmessage="type:ERROR" align="right"> <ct:FWLabel key="JSP_PF_RETENUE_PAS_CALCULE" /></div>
						</td>
					</tr>
					<% break;
				case PAS_DE_RETENU : %>
					<% break;
			}
		}%>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>

<%if (!JadeStringUtil.isBlank(viewBean.getDecision().getSimpleDecision().getCsEtat())&& objSession.hasRight(IPFActions.ACTION_IMPRIMER_DOC, FWSecureConstants.READ)) {%>
	<input class="btnCtrl" type="button" id="btnImprimer" value='<ct:FWLabel key="JSP_PF_DECISION_A_BOUTON_IMPRIMER"/>' />
<%}%>
<%if (bButtonEnregistrer && objSession.hasRight("perseus", FWSecureConstants.UPDATE)) {%><input class="btnCtrl" type="button" id="btnEnregistrer"
	value='<ct:FWLabel key="JSP_PF_DECISION_A_BOUTON_ENREGISTRER"/>' /><%}%>
<%if (bButtonPreValider && objSession.hasRight("perseus", FWSecureConstants.UPDATE)) {%><input class="btnCtrl" type="button" id="btnPreValider"
	value='<ct:FWLabel key="JSP_PF_DECISION_A_BOUTON_PREVALIDER"/>' /><%}%>
<%if (bButtonValider) {%><input class="btnCtrl" id="btnValidate" type="button" value="<%=btnValLabel%>"><%}%>
<%if (bButtonValiderChoixProjet) {%><input class="btnCtrl" type="button" id="btnValiderChoixProjet"
	value='<ct:FWLabel key="JSP_PF_DECISION_A_BOUTON_VALIDER_CHOIX_DECISION"/>' /><%}%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>