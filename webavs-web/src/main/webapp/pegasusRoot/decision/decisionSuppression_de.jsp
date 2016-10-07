	<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDecision"%>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>

<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>

<%@page import="globaz.pegasus.vb.decision.PCDecisionSuppressionViewBean"%>
<%@page import="java.util.ArrayList" %>
<%@ page import="globaz.jade.json.MultiSelectHandler" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.services.models.home.HomeService"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDemandes"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/decision/detail.css"/>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
	//Les labels de cette page commence par le préfix "JSP_PC_DECSUPP_D"
	idEcran="PPC0083";

	PCDecisionSuppressionViewBean viewBean = (PCDecisionSuppressionViewBean) session.getAttribute("viewBean");
	MultiSelectHandler handler = new MultiSelectHandler();

	
	autoShowErrorPopup = false;
	
	bButtonDelete = false;
	btnNewLabel = "Ajouter";
	
	String btnPrintLabel = objSession.getLabel("JSP_PC_DECSUP_D_GENERER_DOC_TRANSFERT");
	
	boolean viewBeanIsNew="add".equals(request.getParameter("_method"));
	//Labels pour les boutons non standard
	String btnPreVal = objSession.getLabel("JSP_PC_DECREF_D_BTN_PREVALIDER");
	String btnVal = objSession.getLabel("JSP_PC_DECREF_D_BTN_VALIDER");
	String btnDeVal = objSession.getLabel("JSP_PC_DECSUPP_D_BTN_DEVALIDER");//bouton deValid
	String titleErroxBox = objSession.getLabel("JSP_GLOBAL_ERROR_BOX_TITLE");//titre box erreur
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>

<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsdecision">
	<ct:menuActivateNode active="no" nodeId="VALIDATIONS"/>
	<ct:menuActivateNode active="no" nodeId="IMPRESSIONS"/>
	<ct:menuSetAllParams key="idVersionDroit" value="<%=viewBean.getIdVersionDroit()%>"/>
	<ct:menuSetAllParams key="idDroit" value="<%=viewBean.getIdDroit()%>"/>
	<ct:menuSetAllParams key="noVersion" value="<%=viewBean.getNoVersion()%>"/>
	<ct:menuSetAllParams key="idDemandePc" value="<%=viewBean.getIdDemandePc()%>"/>
	<ct:menuSetAllParams key="idDecision" value="<%=viewBean.getIdDecision() %>"/>

</ct:menuChange>
<script language="JavaScript">
	var globaz = {};
	globaz.amountRestitution = <%= viewBean.hasMontantRestitution() %>;
</script>


<script type="text/javascript" src="<%=rootPath%>/scripts/decision/detail_SUP.js"></script>
<!--  Pour gestion de la liste a deux niveau -->
<script type="text/javascript" src="<%=servletContext%>/pegasusRoot/scripts/decision/decisionListHandlerSup.js"></script>

<script language="JavaScript">

var MAIN_URL="<%=formAction%>";


function add() {}
function upd() {}
function init (){}
var ACTION_DECISION="<%=IPCActions.ACTION_DECISION%>";
var ACTION_DECISION_SUPPRESSION = "<%=IPCActions.ACTION_DECISION_DETAIL_SUPPRESSION%>";
var ACTION_DECISION_DEVALIDE = "<%= IPCActions.ACTION_DECISION_DEVALIDER%>";
var ACTION_DECISION_VALIDE = "<%= IPCActions.ACTION_DECISION_SUP_VALIDATION%>";
var ACTION_DECISION_PREVALIDE = "<%= IPCActions.ACTION_DECISION_SUP_PREVALIDATION%>";
var ACTION_DECISION_IMPRIMER = "<%= IPCActions.ACTION_DECISION_SUP_IMPRIMER%>";
var devalidMsgBox = "<%=objSession.getLabel("JSP_PC_DECALCUL_D_MESSAGE_DEVALIDER")%>";
var confirmButton = "<%=objSession.getLabel("JSP_PC_DECALCUL_D_MESSAGE_DEVALIDER_CONFIRM")%>";
var cancelButton = "<%=objSession.getLabel("JSP_PC_DECALCUL_D_MESSAGE_DEVALIDER_CANCEL")%>";


var actionMethod;
var userAction;

var listeAnnexes=<%=viewBean.getAnnexesJSArray() %>;
var listeCopies=<%=viewBean.getCopiesJSArray() %>;
var motifTexteLibre = "<%= viewBean.getCsCodeTexteLibreMotif() %>";
var motifDecision = "<%= viewBean.getDecisionSuppression().getSimpleDecisionSuppression().getCsMotif() %>";
var $texteLibreMotifDiv;


$(function(){
	actionMethod=$('[name=_method]',document.forms[0]).val();
	userAction=$('[name=userAction]',document.forms[0])[0];

	$texteLibreMotif = $('.texteLibreMotif');
	
	//gestion des erreurs
	pegasusErrorsUtils.dealErrors(<%= request.getParameter("decisionErrorMsg") %>,"<%= titleErroxBox %>");
	
	// attribue une id à tous les champs ayant un nom mais pas encore d'id
	$('*',document.forms[0]).each(function(){
		if(this.name!=null && this.id==""){
			this.id=this.name;
		}
	});
	
	if(motifDecision === motifTexteLibre){
		$texteLibreMotif.show()
	}else{
		$texteLibreMotif.hide()
	}
	
	//valeur init adresse widget
	$('#homeWidget1').hide();
	$('#buttonAdresse').css('margin-left','70px');

	//Gestion evenement onclick sur bouton widget adresse
	$('#buttonAdresse').click(function(){
		$('#homeWidget1').css('margin-left','10px');
		$('#homeWidget1').show();
		$('#homeWidget1').focus();
	});

	$('#homeWidget1').blur(function(){
		$('#homeWidget1').hide();	
	});
	
	//valeur init zone billag
	$('#phraseCaseCoche').show();
	$('#phraseCaseNonCoche').hide();
	$('#billagChk').attr('checked', true);

	//Ajout bouton valider
	setPreValidBouton(true,"<%= btnPreVal %>");
	<%
	if(viewBean.isPreValider()){%>	
		setValidBouton("<%= btnVal %>");
		setPreValidBouton(false);
	<%}%>

	<%
	if(viewBean.isValider()){%>	
	//on enleve le bouton modifier
		$('#btnUpd').hide();
		setPreValidBouton(false);
		$(".infoWarnRetro").hide();
	<%}
	
	if(viewBean.isDevalidable()){%>
		setDeValidBouton(true,"<%=btnDeVal%>");
	<%}%>
	
	<%
	if(IPCDecision.CS_MOTIF_SUPPRESSION_TRANSFERT_DOSSIER.equals(viewBean.getDecisionSuppression().getSimpleDecisionSuppression().getCsMotif())){%>
		setPrintBouton(true,"<%= btnPrintLabel %>");
		
			//$('#motifListe').trigger("change.comutator");
			//},"Initialisation de la zone de parametrage de transfert de dossier");
	<%}%>
});

function cancel() {
	userAction.value=ACTION_DECISION+".chercher";
}  


function validate() {
		
		fillCopiesAndAnnexesHiddenFields()
		state = true;
		userAction.value=ACTION_DECISION_SUPPRESSION+".modifier";
		return state;
}    

function validateDecision(){
	fillCopiesAndAnnexesHiddenFields()
	state = true;
	userAction.value=ACTION_DECISION_VALIDE;
	return state;
}
function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
    	userAction.value=ACTION_DEMANDE+".supprimer";
        document.forms[0].submit();
    }
}


function hideInputC(){
	$('#homeWidget1').hide();
	$('#buttonAdresse').show()
	$('#homeWidget1').val('');
}

//Validation de la decision, retourn le booleen et l'url
function preValidateDecision(){
	fillCopiesAndAnnexesHiddenFields()
	state = true;
	userAction.value=ACTION_DECISION_PREVALIDE;
	return state;
}

function printDecision(){
	state = true;
	userAction.value=ACTION_DECISION_IMPRIMER;
	return state;
}


var multiSelectData = <%= handler.createMotifsWithSousMotifs(viewBean.CS_MOTIF) %>;
var initValue = {"masterValue":"<%=viewBean.getDecisionSuppression().getSimpleDecisionSuppression().getCsMotif()%>","childValue":"<%=viewBean.getDecisionSuppression().getSimpleDecisionSuppression().getCsSousMotif()%>"};
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
	<span class="postItIcon" data-g-note="idExterne:<%=viewBean.getDecisionSuppression().getDecisionHeader().getSimpleDecisionHeader().getIdDecisionHeader()%>, 
					tableSource: PCDECHEA"></span>
		<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_DECSUPP_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD colspan="6" align="center">
			<!--  bloc zone informations générales -->
			<div id="zoneInfoRefus">
				<div id="ligneRequerant">
					<span class="label"><ct:FWLabel key="JSP_PC_DECSUPP_D_REQ"/></span>
					<%=viewBean.getTiersInfosAsString(IPCDroits.CS_ROLE_FAMILLE_REQUERANT) %>
					<a class="lienTiers external_link"  href="./pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=viewBean.getIdTierRequerant()%>">	
						<ct:FWLabel key="JSP_PC_DECALCUL_D_TIERS"/>
					</a>
					
				
					<a id="aGed" href="#"><ct:FWLabel key="JSP_PC_GED_LINK_LABEL"/></a>
					
				</div>	
				<!--  adresse de courrier label et widget -->
				<div id="adresseZone">
					<div id="blocAdrCRS">
						<span id="labelAdresse" class="label" >
							<ct:FWLabel key="JSP_PC_DECSUPP_D_ADRCOUR"/>
							<button id="buttonAdresse">
						    	...
						    </button>
						</span>
					    
					    	<div id="descAdresse">
						    	<pre><span class="detailAdresseTiersC"><%= viewBean.getAdresseCourrier() %></span></pre>
		       					<input type="hidden" class="idHome" />
								<ct:widget id='<%="homeWidget1"%>' name='<%="homeWidget1"%>' styleClass=" selecteurHome">
									<ct:widgetService methodName="search" className="<%=HomeService.class.getName()%>">
										<ct:widgetCriteria criteria="likeNumeroIdentification" label="JSP_PC_TAXE_JOURNALIERE_HOME_W_NO_IDENTIFICATION"/>								
										<ct:widgetCriteria criteria="likeDesignation" label="JSP_PC_TAXE_JOURNALIERE_HOME_W_DESIGNATION"/>
										<ct:widgetCriteria criteria="likeNpa" label="JSP_PC_TAXE_JOURNALIERE_HOME_W_NPA"/>									
										<ct:widgetCriteria criteria="likeLocalite" label="JSP_PC_TAXE_JOURNALIERE_HOME_W_LOCALITE"/>								
										<ct:widgetLineFormatter format="${simpleHome.numeroIdentification} ${simpleHome.nomBatiment} ${cs(tiersHome.tiers.titreTiers)} ${tiersHome.tiers.designation2} ${tiersHome.tiers.designation1} - (${tiersHome.localite.numPostal} ${tiersHome.localite.localite})"/>
										<ct:widgetJSReturnFunction>
											<script type="text/javascript">
												function(element){
													$(this).parents('.descAdresse').find('.idHome').val($(element).attr('simpleHome.id'));
													$(this).parents('.descAdresse').find('.idHome').trigger('change');
													this.value=$(element).attr('simpleHome.numeroIdentification')+' '+$(element).attr('simpleHome.nomBatiment')+' '+$(element).attr('tiersHome.tiers.designation2')+' '+$(element).attr('tiersHome.tiers.designation1')+' '+$(element).attr('simpleHome.numeroIdentification');
													hideInputC();
													buildStringC(element);
													}
											</script>										
										</ct:widgetJSReturnFunction>
									</ct:widgetService>
								</ct:widget>
						    </div>
						   
					   
					</div>
					
				</div>
				<div id="lignePersonneRef">
					<span class="label"><ct:FWLabel key="JSP_PC_DECSUPP_D_PERSONEREF"/></span>
					<ct:FWListSelectTag data="<%=PCGestionnaireHelper.getResponsableData(objSession)%>" defaut="<%=viewBean.getGestionnaire()%>" name="decisionSuppression.decisionHeader.simpleDecisionHeader.preparationPar"/>
					<span class="label" id="lblEmail"><ct:FWLabel key="JSP_PC_DECSUPP_D_EMAIL"/></span>
					<span id="valEmail"><input type="text" name="gestMail" value="<%=controller.getSession().getUserEMail()%>" /></span>					
				</div>
				
				<div id="ligneDemande">	
					<span class="label"><ct:FWLabel key="JSP_PC_DECSUPP_D_NO_DROIT"/></span>
					<span id="nodroit"><%=viewBean.getIdDroit() %></span>
				
					<span class="label" id="lblnoversion"><ct:FWLabel key="JSP_PC_DECSUPP_D_NO_VERDRO"/></span>
					<span id="noversion"><%=viewBean.getNoVersionDroit() %></span>
				
				</div>
			
				<div id="ligneDecision">
					<span class="label"><ct:FWLabel key="JSP_PC_DECSUPP_D_DATE_DECISION"/></span>
					<span id="valDecision">
					
					<ct:inputText notation="data-g-calendar='mandatory: true'" name="decisionSuppression.decisionHeader.simpleDecisionHeader.dateDecision"/></span>
				
				
					<span class="label" id="lblNoDecision"><ct:FWLabel key="JSP_PC_DECSUPP_D_NO_DECISION"/></span>
					<span id="valDecision"><%= viewBean.getDecisionSuppression().getDecisionHeader().getSimpleDecisionHeader().getNoDecision()%></span>
				
					
				</div>
				<div id="ligneDecisionCompl">
					<span class="label" id="lblEtat"><ct:FWLabel key="JSP_PC_DECSUPP_D_ETAT"/></span>
					<span id="valEtat"><%=objSession.getCodeLibelle(viewBean.getDecisionSuppression().getDecisionHeader().getSimpleDecisionHeader().getCsEtatDecision())%></span>
				
					
					<span class="label" id="lblGenreDec"><ct:FWLabel key="JSP_PC_DECSUPP_D_GENREDEC"/></span>
					<span id="valGenreSup"><%= objSession.getCodeLibelle(viewBean.getDecisionSuppression().getDecisionHeader().getSimpleDecisionHeader().getCsGenreDecision())%></span>
				</div>
				
				<div id="ligneDateSupp">
					<span class="label"><ct:FWLabel key="JSP_PC_DECSUP_D_DATE_SUPP"/></span>
					<span id="dateSupp">
						<input type="text" disabled="disabled"  data-g-calendar="mandatory:true, type:month" value="<%= viewBean.getDateSuppressionFormatee()%>" name="decisionSuppression.simpleDecisionSuppression.dateSuppression"/>
					</span>
				</div>
					
				<br/><br/>				
					<div id="zoneInfosRefusSupp">
						
						<!--  motis -->
						<label class="label"><ct:FWLabel key="JSP_PC_PREP_DECISION_REFUS_DE_MOTIF"/></label>
						<span><%=objSession.getCodeLibelle(viewBean.getDecisionSuppression().getSimpleDecisionSuppression().getCsMotif()) %></span> 
						<!--  sous motif si il y a  -->
						<%if(!JadeStringUtil.isBlankOrZero(viewBean.getDecisionSuppression().getSimpleDecisionSuppression().getCsSousMotif())||!viewBean.getDecisionSuppression().getSimpleDecisionSuppression().getCsSousMotif().equals("0")){
						%>
							<br/><br/><label class="label" style="text-align:left;float:left;"><ct:FWLabel key="JSP_PC_PREP_DECISION_REFUS_DE_SOUSMOTIF"/></label>
							<span><%=objSession.getCodeLibelle(viewBean.getDecisionSuppression().getSimpleDecisionSuppression().getCsSousMotif()) %></span>
						<%} %>
						
						<br/><br/>
							<label class="texteLibreMotif labelMotif"><ct:FWLabel key="JSP_PC_PREP_DECISION_SUPPR_DE_CHAMP_LIBRE_MOTIF"/></label>
							<span class="texteLibreMotif"><%=viewBean.getDecisionSuppression().getSimpleDecisionSuppression().getChampLibreMotif() %></span> 
						
						
						<%if(viewBean.getDecisionSuppression().getSimpleDecisionSuppression().getCsMotif().equals(IPCDecision.CS_MOTIF_SUPPRESSION_TRANSFERT_DOSSIER)){
						%>
							<!-- PARAMETRES DE DEMANDE DE TRANSFERT DE DOSSIER -->
						<div id="zoneTransfertDossier">
							<p><span class="labelMotif"><ct:FWLabel key="JSP_PREP_DECISION_SUPPR_D_MOTIF_TRANSFERT"/></span>
							<textarea class="textMotif" data-g-string="mandatory:true" name="transfert.simpleTransfertDossierSuppression.textMotifTransfert"><%=viewBean.getTextMotifTransfert()%></textarea></p>								
							<p><span class="labelMotif"><ct:FWLabel key="JSP_PREP_DECISION_SUPPR_D_MOTIF_CONTACT"/></span>
							<textarea class="textMotif" rows="3" cols="80" data-g-string="mandatory:true" name="transfert.simpleTransfertDossierSuppression.textMotifContact" class="libelleLong" ><%=viewBean.getTextMotifContact() %></textarea></p>
							
							<table>
								<tr>
									<td><label class="labelMotif" for="transfertNouvelleCaisse"><ct:FWLabel key="JSP_PREP_DECISION_SUPPR_D_NOUVELLE_CAISSE"/></label></td>
									<td>
										<ct:widget id='nouvelleCaisse' name='nouvelleCaisse' notation='data-g-string="mandatory:true"' styleClass="libelleLong selecteurHome" defaultValue='<%=viewBean.getNouvelleCaisse() %>'>
											<input type="hidden" class="idAdmin1" />
											<ct:widgetService defaultLaunchSize="1" methodName="find" className="<%=ch.globaz.pyxis.business.service.AdministrationService.class.getName()%>">								
											<ct:widgetCriteria criteria="forCodeAdministrationLike" label="JSP_DEMANDE_TRANSFERT_DOSSIER_W_CODE"/>
											<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_DEMANDE_TRANSFERT_DOSSIER_W_NOM"/>																						
											<ct:widgetLineFormatter format="#{admin.codeAdministration} #{tiers.designation1} #{tiers.designation2}"/>
											<ct:widgetJSReturnFunction>
												<script type="text/javascript">
													function(element){
														this.value=$(element).attr('admin.codeAdministration')+' '+$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
														$('#idNouvelleCaisse').val($(element).attr('admin.idTiersAdministration'));
													}
												</script>										
											</ct:widgetJSReturnFunction>
											</ct:widgetService>
										</ct:widget>
										<input type="hidden" name="transfert.simpleTransfertDossierSuppression.idNouvelleCaisse" id="idNouvelleCaisse" value="<%=viewBean.getIdNouvelleCaisseTransfert() %>"/>
									</td>	
								</tr>
								<tr>
									<td><span class="labelMotif"><ct:FWLabel key="JSP_PREP_DECISION_SUPPR_D_ANNEXES"/></span></td>
									<td>
										<div data-g-multistring='tagName:annexeSelect,defaultValues:listeAnnexes' ></div>
										<input type="hidden" name="listeAnnexes" id="listeAnnexes" />
									</td>
									<td><span class="labelMotif"><ct:FWLabel key="JSP_PREP_DECISION_SUPPR_D_COPIES"/></span></td>
									<td>
										<div data-g-multistring='tagName:copieSelect,mode:autocompletion,languages:¦Tiers,Administration¦,defaultValues:listeCopies' >
											<input	id="widget-multistring-copies-tiers" 
												class="jadeAutocompleteAjax widgetTiers" 
												data-g-autocomplete="service:¦ch.globaz.pyxis.business.service.PersonneEtendueService¦,
																	method:¦findByAlias¦,
																	criterias:¦{
																		forDesignation1Like: '<ct:FWLabel key="JSP_PREP_DECISION_SUPPR_W_NOM"/>',
																		forDesignation2Like: '<ct:FWLabel key="JSP_PREP_DECISION_SUPPR_W_PRENOM"/>',
																		forNumeroAvsActuel: '<ct:FWLabel key="JSP_PREP_DECISION_SUPPR_W_AVS"/>',
																		forAlias: '<ct:FWLabel key="JSP_PREP_DECISION_SUPPR_W_ALIAS"/>'
																	}¦,
																	lineFormatter:¦#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel}¦,
																	modelReturnVariables:¦tiers.id,tiers.designation1,tiers.designation2,personneEtendue.numAvsActuel,personne.dateNaissance¦,
																	functionReturn:¦
																		function(element){
																			return {
																				id:$(element).attr('tiers.id'),
																				text:$(element).attr('tiers.designation1')+' '+globazNotation.utilsString.toProperCase($(element).attr('tiers.designation2'))
																			};
																		}
																	¦" 
												type="text" />	
											<input	id="widget-multistring-copies-admin" 
												class="widgetAdmin" 
												data-g-autocomplete="service:ch.globaz.pyxis.business.service.AdministrationService,
																	method:¦find¦,
																	criterias:¦{
																		forCodeAdministrationLike: '<ct:FWLabel key="JSP_DEMANDE_TRANSFERT_DOSSIER_W_CODE"/>',
																		forDesignation1Like: '<ct:FWLabel key="JSP_DEMANDE_TRANSFERT_DOSSIER_W_NOM"/>'
																	}¦,
																	lineFormatter:¦#{tiers.designation1} #{tiers.designation2}, #{tiers.idTiers}¦,
																	modelReturnVariables:¦tiers.id,tiers.designation1,tiers.designation2¦,
																	functionReturn:¦
																		function(element){
																			return {
																				id:$(element).attr('tiers.id'),
																				text:$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2')
																			};
																		}
																	¦" 
												type="text" />
																				
										</div>
										<input type="hidden" name="listeCopies" id="listeCopies"/>
									</td>
								</tr> 		
							</table>
						</div>
						<!--  fin zone transfert de dossier -->
							
						<%} %>
						
					</div>
					<br/>
					
					<%if(viewBean.displayCompleteRestitutionMessage()){%>	
						<div data-g-boxmessage="type:WARN" class="infoWarnRetro">
							<ct:FWLabel key="JSP_PC_DECALCUL_D_INFO_COMPTABILISATION"/><br/>
							<b><ct:FWLabel key="JSP_PC_DECALCUL_D_SOLDE_FAVEUR"/></b><span data-g-amountformatter=" "><%= viewBean.getMontantDecision()%></span> CHF
						</div>
					<%} else { %>
						<div data-g-boxmessage="type:WARN" class="infoWarnRetro">
							<b><ct:FWLabel key="JSP_PC_DECALCUL_D_SOLDE_FAVEUR"/></b><span data-g-amountformatter=" "><%= viewBean.getMontantDecision()%></span> CHF
						</div>
					<%}%>
			</div>
		
			<!-- **************************** Confimation dévalidation -->
			<div id="dialog-devalid-confim" title="<%= objSession.getLabel("JSP_PC_DECALCUL_D_MESSAGE_DEVALIDER_TITRE")%>">
    			<p class="confirmContent"><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>
    			<p class="confirmAttention">
    				<%=objSession.getLabel("JSP_PC_DECALCUL_D_MESSAGE_DEVALIDER")%>
    			</p>
			</div>
			
			<!-- **************************** Confirmation que l'utilisateur veut bien créer un lot de décision de restitution -->
			<div id="dialog-confirm-creation-lot" title="<%= objSession.getLabel("JSP_PC_DECALCUL_D_CONFIRMATION_COMPTA_AUTO_TITRE")%>">
    			<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span><%= objSession.getLabel("JSP_PC_DECALCUL_D_CONFIRMATION_COMPTA_AUTO")%></p>
<!--     			<p> -->
<%-- 							<%= objSession.getLabel("JSP_VALID_LOT_D_EMAIL")%> --%>
<%-- 							<INPUT type="text" name="mailProcessCompta" value="<%=viewBean.getMailProcessCompta()%>" class="libelleLong"> --%>
<!-- 					</p> -->
			</div>
			
				<input type="hidden" name="isComptabilisationAuto" id="isComptabilisationAuto" value="<%=viewBean.isComptabilisationAuto()%>"/>
				<input type="hidden" name="idVersionDroit" value="<%=viewBean.getIdVersionDroit()%>"/>
				<input type="hidden" name="idDroit" value="<%=viewBean.getIdDroit()%>"/>
				<input type="hidden" name="noVersion" value="<%=viewBean.getNoVersionDroit()%>"/>
				<input type="hidden" name="idDecision" value="<%=viewBean.getIdDecision() %>"/>
			
		</TD>
	</TR>

	
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>