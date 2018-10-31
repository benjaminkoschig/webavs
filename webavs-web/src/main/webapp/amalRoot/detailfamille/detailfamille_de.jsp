<!-- ************************************************************************************* -->
<!-- GLOBAL GLOBAZ IMPORT AND HEADER INCLUSION -->

<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="java.util.TreeMap"%>
<%@page import="globaz.amal.utils.AMDetailFamilleHelper"%>
<%@page import="ch.globaz.amal.business.models.caissemaladie.CaisseMaladie"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="ch.globaz.amal.business.services.AmalServiceLocator"%>
<%@page import="ch.globaz.amal.business.models.caissemaladie.CaisseMaladieSearch"%>
<%@ page language="java" import="globaz.globall.http.*"	errorPage="/errorPage.jsp" contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/detail/header.jspf"%>

<!-- FIN GLOBAZ IMPORT AND HEADER INCLUSION -->
<!-- ************************************************************************************* -->


<!-- ************************************************************************************* -->
<!-- INITIALIZATION AND SPECIFIC LAMAL DETAILFAMILLE INCLUSION -->

<%-- tpl:put name="zoneInit" --%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme"%>
<%@page import="ch.horizon.jaspe.util.JACalendar"%>
<%@page import="ch.horizon.jaspe.util.JAUtil"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Set"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JADate"%>
<%@page import="globaz.globall.util.JAVector"%>
<%@page import="globaz.globall.parameters.FWParametersCode"%>
<%@page import="globaz.globall.parameters.FWParametersSystemCodeManager"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.amal.vb.contribuable.AMContribuableViewBean"%>
<%@page import="globaz.amal.vb.detailfamille.AMDetailfamilleViewBean"%>
<%@page import="ch.globaz.amal.business.models.contribuable.Contribuable"%>
<%@page import="ch.globaz.amal.business.models.documents.SimpleDocument"%>
<%@page import="ch.globaz.amal.business.models.famille.FamilleContribuable"%>
<%@page import="ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%@page import="globaz.jade.log.*"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="ch.globaz.pyxis.business.model.TiersSimpleModel"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneSimpleModel"%>
<%@page import="globaz.prestation.interfaces.tiers.PRTiersHelper"%>
<%@page import="globaz.amal.utils.AMUserHelper"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoi"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiSearch"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurAnnonceDetail"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurAnnonceDetailSearch"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetail"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetailSearch"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatus"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatusSearch"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJob"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJobSearch"%>
<%@page import="ch.globaz.envoi.business.models.parametrageEnvoi.FormuleListSearch"%>
<%@page import="ch.globaz.envoi.business.models.parametrageEnvoi.FormuleList"%>
<%@page import="ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex"%>
<%@page import="ch.globaz.amal.business.models.revenu.RevenuFullComplex"%>
<%@page import="ch.globaz.amal.business.models.revenu.SimpleRevenu"%>
<%@page import="ch.globaz.amal.business.models.parametremodel.ParametreModelComplexSearch"%>
<%@page import="ch.globaz.amal.business.models.parametremodel.ParametreModelComplex"%>
<%@page import="ch.globaz.amal.business.models.parametremodel.SimpleParametreModel"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%

	JadeLogger.info(null,"MAXIMUM : "+(Runtime.getRuntime().maxMemory()/ 1048576)+" Mb");
	JadeLogger.info(null,"TOTAL : "+(Runtime.getRuntime().totalMemory()/ 1048576)+" Mb");
	JadeLogger.info(null,"FREE : "+(Runtime.getRuntime().freeMemory()/ 1048576)+" Mb");
	//
	//Les labels de cette page commencent par le préfix "JSP_AM_SUBSIDE_D"
	idEcran="AM0006";

	AMDetailfamilleViewBean viewBean = (AMDetailfamilleViewBean) session.getAttribute("viewBean");
	boolean viewBeanIsNew = "add".equals(request.getParameter("_method"));

	// Get the contribuable Id of the current detailfamille-viewbean
	if (null != request.getParameter("contribuableId") 
		&& 
		null != request.getParameter("membreFamilleId")
		&&
		viewBeanIsNew==true) {

		JadeLogger.info(viewBean, "CREATION NOUVEAU SUBSIDE" );
		
		viewBean.setIdContribuable(request.getParameter("contribuableId"));
		viewBean.retrieveContribuable();
		
		viewBean.setIdFamille(request.getParameter("membreFamilleId"));
		viewBean.retrieveFamilleContribuable();
		viewBean.retrieveFormuleList();
	}

	String selectedTabId = request.getParameter("selectedTabId");
	if (JadeStringUtil.isBlankOrZero(selectedTabId)) {
		selectedTabId = "0";
	}

	String linkRetourDetail = "amal?userAction="+IAMActions.ACTION_FAMILLE+".afficher&selectedId="+viewBean.getFamilleContribuable().getSimpleFamille().getId();
	String linkRetourDetailLibelle = "Détails";
	String linkRetourContribuable = "amal?userAction="+IAMActions.ACTION_CONTRIBUABLE+".afficher&selectedTabId="+selectedTabId+"&selectedId="+viewBean.getFamilleContribuable().getSimpleFamille().getIdContribuable();
	String linkRetourContribuableLibelle = "Retour dossier";
	
	PersonneEtendueComplexModel personne= viewBean.getFamilleContribuable().getPersonneEtendue();
	boolean idTiersEmpty = JadeStringUtil.isBlankOrZero(personne.getTiers().getIdTiers());
	
	autoShowErrorPopup = true;
	
	bButtonDelete = false;
	
	Boolean contribReprise = false;
	if (viewBean.getContribuable().getContribuable().isNew()) {
		contribReprise = true;
		bButtonUpdate = false;
		//En cas de contribuable historique, on n'affiche pas les liens de la zone contribuable
		linkRetourContribuable = "";
		linkRetourContribuableLibelle = "";
		linkRetourDetail = "";
		linkRetourDetailLibelle = "";
	}

	// Charge la liste de toutes les assurances
	String[] allAssurances = viewBean.getNomAssurancesAll();
	
	boolean hasRightNew = objSession.hasRight(userActionNew, FWSecureConstants.ADD);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>

<!-- FIN INITIALIZATION AND SPECIFIC LAMAL DETAILFAMILLE INCLUSION -->
<!-- ************************************************************************************* -->





<!-- ************************************************************************************* -->
<!-- JAVASCRIPT AND CSS PART -->

<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/detailfamille/cloud-zoom.css" />
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/amal.css" rel="stylesheet" />
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/ajax_amal.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/jade/notation/javascriptLibs/syntaxhighlighte/shCore.css"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/jade/notation/javascriptLibs/syntaxhighlighte/shThemeDefault.css"/>
<script type="text/javascript" src="<%=servletContext%>/jade/notation/javascriptLibs/syntaxhighlighte/shCore.js"></script>
<script type="text/javascript" src="<%=servletContext%>/jade/notation/javascriptLibs/syntaxhighlighte/shBrushXml.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.css" rel="stylesheet" />
<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>

<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">



//---------------------------------------------------------------------------------------------------
// Object javascript pour enregistrer les codes systèmes AMMODELES
// ---------------------------------------------------------------------------------------------------
function CSDocument(_cu,_libelleShort,_libelle) {
	this.cu = _cu;
	this.libelleShort = _libelleShort;
	this.libelle = _libelle;
}
// nom à problèmes (multiple prénom ou nom)
var allCSDocument = new Array;
<%
for (Iterator it = viewBean.getListeDocuments().iterator(); it.hasNext();) {
	FWParametersCode code = (FWParametersCode) it.next();
	String codeSysteme = code.getId(); // Code système
	String codeUtilisateur = code.getCodeUtilisateur(objSession.getIdLangue()).getCodeUtilisateur(); // CU (1 - )
	String codeLibelleShort = code.getLibelle().trim(); // Libelle Short
	String codeLibelle = code.getCodeUtilisateur(objSession.getIdLangue()).getLibelle().trim(); // Libelle Long
%>
allCSDocument['<%=codeSysteme%>']=new CSDocument('<%=codeUtilisateur%>','<%=codeLibelleShort%>','<%=codeLibelle%>');
<%
}
%>
//---------------------------------------------------------------------------------------------------
// Fin initialisation Object javascript pour enregistrer les codes systèmes AMMODELES
//---------------------------------------------------------------------------------------------------





//variables nécessaires aux scripts
var allCM = new Array();
var CMList = new Array();
var MAIN_URL="<%=formAction%>";
var ACTION_CONTRIBUABLE="<%=IAMActions.ACTION_CONTRIBUABLE%>";
var ACTION_FAMILLE="<%=IAMActions.ACTION_FAMILLE%>";
var ACTION_DETAILFAMILLE="<%=IAMActions.ACTION_DETAILFAMILLE%>";
var jobManualEdited = '<%=IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue()%>';
var jobManualQueued = '<%=IAMCodeSysteme.AMJobType.JOBMANUALQUEUED.getValue()%>';
var amContextPath = '<%=request.getContextPath()%>';
var amRequestURL = '<%=request.getRequestURL()%>';
var amRequestURI = '<%=request.getRequestURI()%>';
var hasRightNew = <%=hasRightNew%>;
var actionMethod;
var userAction;
var lastRowStyle = "";
var lastRowKeySubside = "";
var isFromSelectionCM = '';

$(function(){
	actionMethod=$('[name=_method]',document.forms[0]).val();
	userAction=$('[name=userAction]',document.forms[0])[0];
	// attribue une id à tous les champs ayant un nom mais pas encore d'id
	$('*',document.forms[0]).each(function(){
		if(this.name!=null && this.id==""){
			this.id=this.name;
		}
	});
});

// document ready disponible dans detailfamille_de.js également
$(document).ready(function() {
	//--------------------------------------------------
	// Au démarrage, cacher la zone des c-m.
	// et l'initialiser avec toutes les valeurs des c-m.
	//--------------------------------------------------
	$('#autoSuggestContainer').hide();
	// chargement de l'array avec les valeur 290-arcosana,194332
	allCM = new Array();
	CMList = new Array();
	<%
	for(int iCaisse=0;iCaisse<allAssurances.length;iCaisse++){
	%>
	allCM[<%=iCaisse%>]='<%=allAssurances[iCaisse]%>';
	<%
	}
	%>
	
	isFromSelectionCM = '<%=(request.getParameter("selectedIndex")!=null?request.getParameter("selectedIndex"):"")%>';
	if(isFromSelectionCM!=''){
		// reprise depuis l'id tiers retournée
		var idSelectedTiers = $('#noCaisseMaladie').val();
		var myText = '';
		// Find the CM in the list
		for(var iCaisse=0;iCaisse<allCM.length;iCaisse++){
			var myValues = allCM[iCaisse].split(',');
			if(myValues[1]==idSelectedTiers){
				myText = myValues[0];
				break;
			}
		}
		$('#noCaisseMaladieVisible').attr('value',myText);
		$('#noCaisseMaladieVisible').focus();
		if(myText!=''){
			$('#noCaisseMaladie').attr('value',idSelectedTiers);
		}else{
			$('#noCaisseMaladie').attr('value','0');
		}
	}
	// Génération de l'historique des envois et des annonces
	<%
	if(viewBeanIsNew==false){
	%>
	generateHistorique(<%=viewBean.getId()%>);
	<%
	}
	%>
	
	// ---------------------------------------------------------------------
	// Action pour afficher la boite de sélection des templates
	// ---------------------------------------------------------------------
	$('#idBtnNewDocument').click(function(e) {
		$('#dialogTemplateSelection').dialog('open');
		if(allFormuleIdCS == null){
			generateDocumentList('oui');
		}
	});
	// ---------------------------------------------------------------------
	// Initialisation du dialogue de sélection des templates
	// ---------------------------------------------------------------------
	$('#dialogTemplateSelection').dialog(
			{
				autoOpen:false,
				buttons:[	{
								text:"OK",
								click: function() {
													callDocumentGeneration();
													$(this).dialog("close");
													}
							},
							{
								text:"Cancel",
								click: function() {$(this).dialog("close");}
							}
						],
				modal:true,
				closeOnEscape:true,
				draggable : false,
				resizable : false,
				width : 640
			}
	);
	
	$('#modelViewXML').dialog(
			{
				autoOpen:false,
				buttons:[	{
								text:"OK",
								click: function() {
									$(this).dialog("close");
								}
							}
						],
				modal:true,
				closeOnEscape:true,
				draggable : true,
				resizable : true,
				width : 1000,
				height: "auto",
				minHeight:600
				
			}
	);
	// ---------------------------------------------------------------------
	// Réaction sur la sélection du template
	// ---------------------------------------------------------------------
	$('#listeModeleCorrespondance').change(function(e){
		refreshPreviewPicture();
	});
	
	//Init du menu déroulant de sélection des caisses maladies
	$("#searchModel\\.inIdTiersCaisse").multiselect({
		height: 250,
		minWidth:250,
		noneSelectedText: 'Sélectionner des champs',
		checkAllText: "Tous",
		uncheckAllText: "Aucun",
		selectedText: "# élément(s) sélectionné(s)"
	});
	//$("#searchModel\\.inIdTiersCaisse").multiselect().multiselectfilter();

	$("#noCaisseMaladieVisible").change(function (e) {
		<%if (!viewBeanIsNew) {%>
			if ($(".sedexLine").length>0) {
				alert('Attention, le changement de caisse ne sera pas répercuté sur les annonces SEDEX ! Pour cela, procéder à une nouvelle attribution.');
			}
		<%}%>
	});
});
var allFormuleIdCS = null;
var currentIdDossier = '<%=viewBean.getId()%>';

//------------------------------------------------------------
//Appel Ajax pour générer la combobox des documents disponibles
//------------------------------------------------------------
function generateDocumentList(_currentIdDossier) {
	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService',
		serviceMethodName:'getAvailableDocumentsListCorrespondance',
		parametres:_currentIdDossier,
		callBack: createDocumentsList
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}
//------------------------------------------------------------
//CallBack Ajax pour générer la liste des documents disponibles
//------------------------------------------------------------
function createDocumentsList(data){
	
	for(var i_Element=0;i_Element<data.length;i_Element++){
		var s_CodeSysteme = data[i_Element].formuleList.definitionformule.csDocument;
		var s_FormuleId = data[i_Element].simpleParametreModel.idFormule;
		var s_TextToInsert = '<option value="'+s_FormuleId+'"';
		if(i_Element===0){
			s_TextToInsert += ' selected="selected"';
			allFormuleIdCS = new Array;
		}
		s_TextToInsert+=' title="'+allCSDocument[s_CodeSysteme].libelle+'"';
		s_TextToInsert+='>';
		s_TextToInsert+=allCSDocument[s_CodeSysteme].cu+' - ';
		s_TextToInsert+=allCSDocument[s_CodeSysteme].libelleShort+' - ';
		if(allCSDocument[s_CodeSysteme].libelle.length>50){
			s_TextToInsert+=allCSDocument[s_CodeSysteme].libelle.substring(0,50);
		}else{
			s_TextToInsert+=allCSDocument[s_CodeSysteme].libelle;
		}
		if(data[i_Element].formuleList.definitionformule.csManuAuto == '42001100'){
			s_TextToInsert += ' *';
		}
		s_TextToInsert+='</option>';
		$('#listeModeleCorrespondance').append(s_TextToInsert);
		allFormuleIdCS[s_FormuleId] = s_CodeSysteme;
	}
	
	if(data.length<=0){
		// fermeture de la dialog box
		alert('Aucun document n\'est actuellement configuré dans le système.\n\nA insérer depuis <Paramètres><Formules>');
		$('#dialogTemplateSelection').dialog('close');
		allFormuleIdCS = null;
	}else{
		refreshPreviewPicture();
	}
}
// ---------------------------------------------------------------------
// Raffraichissement de l'image de preview
// ---------------------------------------------------------------------
function refreshPreviewPicture(){
	// Affecter les valeurs des images
	var s_shortPath = getPictureDetail();
	var s_previewPath = s_shortPath+'preview.png';
	var s_normalPath = s_shortPath+'.png';
	$('#modeleSpecimen').attr('src',s_previewPath);
	$('#zoom1').attr('href',s_normalPath);
	// Appeler à nouveau CloudZoom pour raffraichissement
    $('.cloud-zoom, .cloud-zoom-gallery').CloudZoom();
}
//------------------------------------------------------------
// Get picture base path (without extension)
//------------------------------------------------------------
function getPictureDetail(){
	// id formule
	var s_selectedFormule = $('#listeModeleCorrespondance option:selected').val();
	// récupération du cs correspondant
	var s_codeSysteme = allFormuleIdCS[s_selectedFormule];
	// set l'image
	var s_pathImage = '<%=request.getContextPath()%>/images/amal/';
	
	return s_pathImage+s_codeSysteme;
}
//------------------------------------------------------------
//Appel des méthodes nécessaires à la génération du document
//------------------------------------------------------------
function callDocumentGeneration(){
	var currentIdFormule = $('#listeModeleCorrespondance option:selected').val();
	generateDocument(currentIdDossier, currentIdFormule);
}
//------------------------------------------------------------
//Appel Ajax pour générer le document wordML
//------------------------------------------------------------
function generateDocument(_currentIdDossier, _currentIdFormule) {
	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService',
		serviceMethodName:'generateDocumentCorrespondance',
		parametres:_currentIdDossier+","+_currentIdFormule,
		callBack: openDocumentInWord
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}
//------------------------------------------------------------
//CallBack Ajax pour afficher erreurs/documents WordML
//------------------------------------------------------------
function openDocumentInWord(data){

	// ERREUR
	if(data.error.length>0){
		var s_message = "\nERROR : \n"+data.error;
		s_message += "\n\nWARNING : \n" +data.warning;
		alert(s_message);
	}else{
		// WARNING
		if(data.warning.length>0){
			alert("WARNING : \n\n" + data.warning);
			// OUVRIR LE FICHIER XML (save as...) ?
		}else{
			// OUVERTURE DE WORD ET DU DOCUMENT Y RELATIF
			if(data.document.length>0){
				launchWord(data.document);
			}
			location.reload();
		}
	}
}
//------------------------------------------------------------
//Lancement de word si data est correctement renseigné 
//------------------------------------------------------------
function launchWord(_filePath){
	try{
		var s_filepath=""+_filePath;
		if(s_filepath.length<=0){
			alert("Error, file not found !");
		}else{
			var word=null;
			try {
		  		if(word==null){
		  			word = new ActiveXObject('Word.Application');
		  		}
			    word.application.visible="true";
		  	} catch(e){
			   	word = new ActiveXObject('Word.Application');
			    word.application.visible="true";
		  	}
		    var currentDocument = word.documents.open(s_filepath);
		}
	} catch (err){
		var s_errorMessage="\r\nError Description : "+err.description;
		alert(s_errorMessage);
	}
}


function init(){}

function postInit(){
	$('#buttonModal').removeProp('disabled');
	$('#buttonModalCalcul').removeProp('disabled');
	$('#idBtnNewDocument').removeProp('disabled');
	$("#searchModel\\.inIdTiersCaisse").multiselect("enable");
	<%
	if(viewBeanIsNew){
	%>
	upd();
	<%
	}
	%>
	upd();
	
	//Désactiver le bouton en mode lecture
	<% if(!hasRightNew) { %>
		$('#idBtnNewDocument').prop('disabled', 'disabled');
	<% } %>
}

function add() {}

function upd() {
	// KEEP NOT MODIFIABLE FIELDS NOT EDITABLE
	$('#dateEnvoi').prop('disabled', 'disabled');
	//$('#annonceCaisseMaladie').prop('disabled', 'disabled');
	$('#dateAnnonceCaisseMaladie').prop('disabled', 'disabled');
	//$('#montantContribution').prop('disabled', 'disabled');
	$('#montantContribution1').prop('disabled', 'disabled');
	$('#supplExtra').prop('disabled', 'disabled');
	$('#montantExact').prop('disabled', 'disabled');
	$('#montantExact1').prop('disabled', 'disabled');
	$('#montantExact2').prop('disabled', 'disabled');
	$('#montantExact3').prop('disabled', 'disabled');
	$('select[name="detailFamille.codeTraitementDossier"]').prop('disabled','disabled');
}

function cancel() {
	if (actionMethod == "add") {
		// SHOULD GO BACK TO FAMILLE MEMBER DETAIL
		userAction.value = ACTION_FAMILLE + ".afficher";
        document.forms[0].elements('_method').value = "";
        document.forms[0].elements('selectedId').value = "<%=request.getParameter("membreFamilleId")%>";
	} else {
		// SHOULD GO BACK TO FAMILLE MEMBER DETAIL
		userAction.value = ACTION_FAMILLE + ".afficher";
        document.forms[0].elements('selectedId').value = "<%=viewBean.getFamilleContribuable().getId()%>";
	}
}
function validate() {
	state = true;
	if (actionMethod == "add") {
		// SAVE AND 
		// SHOULD GO BACK TO FAMILLE MEMBER DETAIL (dest surchargé)
		userAction.value = ACTION_DETAILFAMILLE + ".ajouter";
	} else {
		// SAVE AND
		// SHOULD GO BACK TO FAMILLE MEMBER DETAIL (dest surcharge)
		userAction.value = ACTION_DETAILFAMILLE + ".modifier";
	}
	return state;
}

function del() {}

</script>
<!-- Script spécific detailfamille -->
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/jquery.contextMenu.css" rel="stylesheet" />
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/detailfamille/cloud-zoom.1.0.2.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/controleurenvoi/controleurenvoistatus.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/detailfamille/detailfamille.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/jquery.contextMenu.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/jquery.ui.position.js"/></script>
<!-- Inclusion de la page permettant d'afficher une box de progress -->
<%@ include file="/amalRoot/progressDialog/progressDialog.jspf" %>

<style>
<!--
pre {
	font-size: 12px;
}
-->
</style>
<!-- Dialogue de sélection du template à créer -->
<div id="dialogTemplateSelection" title="CREATION DE DOCUMENT AMAL">
	<table id="listeModeleTable" style="widht=100%">
		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td></td>
			<td align="center">
				<select style='font-family:"Consolas";font-size:16px;width:600px' id="listeModeleCorrespondance">
				</select>
			</td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="center">
				<div id="divPreview" style="width:372px">
				<a href="" class="cloud-zoom" id="zoom1" style="width:372px" rel="position:'inside', adjustX:0, adjustY:0">
					<img id="modeleSpecimen"
						src="" 
						width="372px"
						height="526px"
						border="1px solid black"
						alt=""
						>
				</a>
				</div>
			</td>
			<td></td>
		</tr>
		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
	</table>
</div>
<div id="modelViewXML" title="Affichage du code XML de l'annonce">
	<table>
		<tr>
			<td>
				<pre id="contentXml" class="brush: xml">

				</pre>
			</td>
		</tr>
	</table>
</div>
<div id="dialogNewAnnonceRP" title="Nouvelle annonce SEDEX RP" style="display: none;">
	<table>
		<tr>
			<td colspan="2">
				<strong>Demande du rapport d'assurance</strong>
			</td>
		</tr>
		<tr>
			<td width="100px"><input type="radio" id="typeNewAnnonce_insuranceQuery" name="typeNewAnnonce" class="typeNewAnnonce" value="insuranceQuery" checked="checked" ><label for="typeNewAnnonce_insuranceQuery">Caisse</label></td>
			<td>
				<select name="searchModel.inIdTiersCaisse" multiple="multiple" id="searchModel.inIdTiersCaisse" >
					<%
					AMDetailFamilleHelper dfH = new AMDetailFamilleHelper();
					HashMap<String, String> mapGroupes = dfH.getGroupesCM();
					
					Map<String, Map<String, String>> mapGroupesCaisses = dfH.buildCMList();						
						for (String idGroupe : mapGroupesCaisses.keySet()) {
							String libGroupe = mapGroupes.get(idGroupe);
							%>
								<optgroup label="<%=libGroupe%>">
									<% 
									Map<String, String> mapCaisses = mapGroupesCaisses.get(idGroupe);
									//for (String idCaisse : mapCaisses.keySet()) {
									for (Map.Entry entry : mapCaisses.entrySet()) {
										String libCaisse = mapCaisses.get(entry.getKey());
									%>
										<option selected="selected" value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
									<% }%>
								</optgroup>
							<%
						}
					%>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<strong>Annonce standard</strong>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td width="100px"><input type="radio" id="typeNewAnnonce_decreeStop" name="typeNewAnnonce" class="typeNewAnnonce" value="decreeStop"><label for="typeNewAnnonce_decreeStop">Type</label></td>
			<td>
				<select id="createDecreeStop">
					<option name="subTypeAnnonce" value="101">Nouvelle décision</option>
					<option name="subTypeAnnonce" value="201">Interruption</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<select id="decreeStopCaisse" >
					<%
					AMDetailFamilleHelper dfH2 = new AMDetailFamilleHelper();
					HashMap<String, String> mapGroupes2 = dfH.getGroupesCM();
					
					Map<String, Map<String, String>> mapGroupesCaisses2 = dfH.buildCMList();						
						for (String idGroupe : mapGroupesCaisses2.keySet()) {
							String libGroupe2 = mapGroupes2.get(idGroupe);
							%>
								<optgroup label="<%=libGroupe2%>">
									<% 
									Map<String, String> mapCaisses = mapGroupesCaisses2.get(idGroupe);
									//for (String idCaisse : mapCaisses.keySet()) {
									for (Map.Entry entry : mapCaisses.entrySet()) {
										String selected = "";
										if (!viewBean.getDetailFamille().isNew() && viewBean.getDetailFamille().getNoCaisseMaladie().equals(entry.getKey())) {
											selected = "selected=\"selected\"";
										}
									%>
										<option <%=selected%> value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
									<% }%>
								</optgroup>
							<%
						}
					%>
				</select>
			</td>
		</tr>
	</table>
</div>
<div id="dialog-simulation-reponse" title="Simuler une réponse" style="display: none;">
		<table>
			<tr>
				<td>
					Choisissez la réponse à simuler :
				</td>
			</tr>
			<tr>
				<td>
					<select id="subTypeReponse" name="subTypeReponse">
						<option value="102" name="confDecree" class="decreeType">Confirmation décision</option>
						<option value="103" name="rejectDecree" class="decreeType">Rejet décision</option>
						<option value="202" name="confStop" class="stopType">Confirmation interruption</option>
						<option value="203" name="rejectStop" class="stopType">Rejet interruption</option>
					</select>
				</td>
			</tr>
		</table>
</div>
<%-- /tpl:put --%>

<!-- FIN JAVASCRIPT AND CSS PART -->
<!-- ************************************************************************************* -->




<!-- ************************************************************************************* -->
<!-- ZONE PRINCIPALE TITLE AND BODY -->

<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%>
<%
if(!viewBeanIsNew){
%>
Détail du subside <%=viewBean.getDetailFamille().getDebutDroit()%>
<%}else{%>
Nouveau subside
<%}%>
 - <%=viewBean.getFamilleContribuable().getPersonneEtendue().getTiers().getDesignation1()%> <%=viewBean.getFamilleContribuable().getPersonneEtendue().getTiers().getDesignation2()%>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>

<tr><td>

<%-- tpl:put name="zoneMain" --%>


		<INPUT type="hidden" name="action" value="merge">
		<INPUT type="hidden" name="modeleId" value="empty">
		<INPUT type="hidden" name="modeleType" value="empty">
	 	<INPUT type="hidden" id="detailfamille.calculs.anneeHistorique" name="calculs.anneeHistorique" value="">
	 	<INPUT type="hidden" id="detailfamille.calculs.typeDemande" name="calculs.typeDemande" value="">
	 	<INPUT type="hidden" id="detailfamille.calculs.idContribuable" name="calculs.idContribuable" value="">
	 	<INPUT type="hidden" id="detailfamille.calculs.idRevenu" name="calculs.idRevenu" value="">
	 	<INPUT type="hidden" id="detailfamille.calculs.revenuIsTaxation" name="calculs.revenuIsTaxation" value="">
	 	<INPUT type="hidden" id="detailfamille.calculs.allSubsidesAsString" name="calculs.allSubsidesAsString" value="">
	 	<INPUT type="hidden" id="idContribuable" value="<%=viewBean.getContribuable().getId()%>">
				<table width="100%">
					<tr>
						<td align="left" style="vertical-align:top">
						<div class="conteneurContrib">	
							<%@ include file="/amalRoot/contribuable/contribuable_contribuable_div_infos.jspf" %>							
							<div>&nbsp</div>

							<%@ include file="/amalRoot/contribuable/contribuable_membreFamille_div_infos.jspf" %>
						</div>
						</td>
						<td></td>
						<%if (!viewBeanIsNew){  %>
						<td align="right" style="vertical-align:top">
						<div class="conteneurQuickInfo">	
							<div class="subtitleQuickInfo">Subsides <%=viewBean.getDetailFamille().getAnneeHistorique() %></div>
							<div class="reLinkQuickInfo"></div>							
							<div class="dataQuickInfo" style="min-height:60px;height:252px;overflow:auto;clear: both;">
								<table id='tableHisto' width="100%" style="border: 1px solid #B3C4DB ;border-collapse:collapse; background-color:#FFFFFF" >
									<col align="left"><col>
									<col align="center"><col>
									<col align="center"><col>
									<col align="center"><col>
									<col align="center"><col>
									<col align="right"><col>
									<col align="right"><col>
									<col align="left"><col>
									<col align="center"><col>
									<col align="center"><col>
									<col align="center"><col>
									<col align="center"><col>
									<tr style="font-style:italic" style="font-weight: bold; background-color:#eeeeee">
										<td style="border-bottom: 1px solid black" align="left" width="8px"></td>
										<td style="border-bottom: 1px solid black" align="center" width="60px">Année</td>
										<td style="border-bottom: 1px solid black" align="center" width="60px">Type</td>
										<td style="border-bottom: 1px solid black" align="center" width="30px">&nbsp;R&nbsp;</td>
										<td style="border-bottom: 1px solid black" align="center" width="60px">Status</td>
										<td style="border-bottom: 1px solid black" align="right" width="120px">Contrib</td>
										<td style="border-bottom: 1px solid black" align="right" width="40px"></td>
										<td style="border-bottom: 1px solid black" align="left" width="120px">Document</td>
										<td style="border-bottom: 1px solid black" align="center" width="140px">Envoi</td>
										<td style="border-bottom: 1px solid black" align="center" width="120px">Début</td>
										<td style="border-bottom: 1px solid black" align="center" width="124px">Fin</td>
										<td style="border-bottom: 1px solid black" align="center"></td>
									</tr>
									<tr><td>&nbsp;</td></tr>
								
									<%
									for (Iterator it = Arrays.asList(viewBean.getDetailFamilleSearch().getSearchResults()).iterator(); it.hasNext();) {
										SimpleDetailFamille detailFamille = (SimpleDetailFamille) it.next();
										String detailModele = detailFamille.getNoModeles();
									
									%>
									<tr <%=!detailFamille.getCodeActif()?"class='subsideDisabled'":""%>>
										<%if (viewBean.getDetailFamille().getId().equals(detailFamille.getId())){ %>
										<td>></td>
										<%}else{%>
										<td></td>
										<% // fin if
										}
										%>
										<td align="center"><%=detailFamille.getAnneeHistorique() %></td>
										<td align="center" title="<%=objSession.getCodeLibelle(detailFamille.getTypeDemande())%>">
										<%=objSession.getCode(detailFamille.getTypeDemande())%>
										</td>
										<td align="center" title="<%=(detailFamille.getRefus()?"Refus":"")%>">
										<%=(detailFamille.getRefus()?"R":"")%>
										</td>
										<td title="<%=objSession.getCodeLibelle(detailFamille.getCodeTraitementDossier())%>">
											<%=objSession.getCode(detailFamille.getCodeTraitementDossier())%>
										</td>
										<%	
											double yearMontantContribution = 0.0;
											double yearMontantSupplement = 0.0;
											String yearMontantContributionTotal = "0.0";
											try{
												yearMontantContribution = Double.parseDouble(detailFamille.getMontantContribution());
			                					yearMontantSupplement = Double.parseDouble(detailFamille.getSupplExtra());
			                					yearMontantContributionTotal = ""+(yearMontantContribution+yearMontantSupplement);
			                					FWCurrency currentMontant = new FWCurrency();
			                					currentMontant.add(yearMontantContributionTotal);
			                					yearMontantContributionTotal = currentMontant.toStringFormat();
											}catch(Exception ex){
												
											}
			                			 %>
										<td align="right"><%=yearMontantContributionTotal%>&nbsp;&nbsp;</td>
										<td align="right"><%=objSession.getCode(detailModele)%></td>
										<td align="left" title="<%=objSession.getCodeLibelle(detailModele)%>">
										<%="- " +viewBean.getLibelleCodeSysteme(objSession.getCode(detailModele)) %>
										</td>
										<td align="center"><%=detailFamille.getDateEnvoi()%></td>
										<td align="center"><%=detailFamille.getDebutDroit() %></td>
										<td align="center"><%=detailFamille.getFinDroit() %></td>
									</tr>
									<%
									// END for subside
									}
									%>
									<tr><td>&nbsp;</td></tr>
									<tr style="font-style:italic" style="font-weight: bold; background-color:#eeeeee">
										<td style="border-bottom: 1px solid black" ></td>
										<td colspan="11" style="border-bottom: 1px solid black" >
											<img class="divInfoSubsideHistoEnvoiExpand" src="<%=request.getContextPath()%>/images/icon-collapse.gif" width="12px" height="12px"/>
											Historique des envois et des annonces <span id="nbElemHistoEnvoi"></span>
										</td>
									</tr>
									<tr><td>&nbsp;</td></tr>
									<tr id='ajaxLoaderImgLine'>
										<td colspan="11" align="center" height="120">
										<img id='ajaxLoaderImg'
											width="16px"
											height="16px"
											src="<%=request.getContextPath()%>/images/amal/ajax-loader-1.gif" title="Chargement" border="0"
											/>
										</td>
									</tr>
								</table>
							</div>									
						</div>
						<% if (!contribReprise) {%>
							<button type="button" id="idBtnNewDocument">
								<b>Créer un document</b>	
							</button>
						<% } %>
						</td>
						<%//end if (!viewBeanIsNew){  
						}
						%>
						<td></td>
					</tr>
				</table>
				<ul id="myMenu" class="contextMenu">
				    <li class="viewXmlHeader">
				        <a href="#viewXmlHeader">Voir entête</a>
				    </li>
					<li class="viewXml">
				        <a href="#viewXml">Voir XML</a>
				    </li>
				    <%if(hasRightNew) { %>
					    <li class="reSend">
					        <a href="#reSend">Réémission</a>
					    </li>
					    <li class="simuRep">
					        <a href="#simuRep">Simuler</a>
					    </li>
					    <li class="delAnnonce">
					        <a href="#delAnnonce">Supprimer</a>
					    </li>
				    <%} %>
				</ul>
				<TABLE id="zoneContrib" width="100%" style="border:1px solid black" style="background-color:#D7E4FF">
                <tr>
                	<td>
                		<table id="saisieDetailFamille" width="100%">
                			<col width="16px" align="left"/>
                			<col width="240px" align="left"/>
                			<col width="240px" align="left"/>
                			<col width="40px" align="left"/>
                			<col width="240px" align="left"/>
			                <tr style="height:4px">
			                	<td colspan="10"></td>
		                	</tr>
			             	<tr>
			             		<td></td>
			                	<td colspan="9" style="font-weight:bold;" title="id : <%=viewBean.getDetailFamille().getIdDetailFamille()%>">
			                		Détails Subside &nbsp;
			                		<%if(!viewBeanIsNew){%> 
			                		(<%=viewBean.getDetailFamille().getDebutDroit()%>)
			                		<%}%>
			                	</td>
			                </tr>
			                <tr style="height:4px">
			                	<td colspan="10"></td>
		                	</tr>
			                <tr>
			                	<td></td>
			                	<td>Type de demande</td>
								<td>
									<ct:FWCodeSelectTag codeType="AMTYDE" wantBlank="true" name="detailFamille.typeDemande" defaut="<%=viewBean.getDetailFamille().getTypeDemande()%>"/>
								</td>
			                	<td></td>
			                	<td>Code Annonce CM</td>
								<td>
									<select id="annonceCaisseMaladie" disabled="disabled" name="detailFamille.annonceCaisseMaladie">
										<option <%=(!viewBeanIsNew && !viewBean.getDetailFamille().getAnnonceCaisseMaladie()?"selected=\"selected\"":"")%> value="false">Non</option>
										<option <%=(!viewBeanIsNew && viewBean.getDetailFamille().getAnnonceCaisseMaladie()?"selected=\"selected\"":"")%> value="true">Oui</option>
									</select>
								</td>
			                	<td></td>
			                	<td></td>
			                	<td></td>
			                	<td></td>
			                </tr>
			                <tr>
			                	<td></td>
			                	<td>Date réception demande</td>
								<td><input id="dateRecepDemande" disabled="disabled" type="text"
									name="detailFamille.dateRecepDemande"
									data-g-calendar="mandatory:false"
									value='<%=(!viewBeanIsNew?viewBean.getDetailFamille().getDateRecepDemande():"")%>'/>
								</td>
			                	<td></td>
			                	<td>Année historique</td>
								<td><input id="anneeHistorique" disabled="disabled" type="text"
									name="detailFamille.anneeHistorique"
									value='<%=(!viewBeanIsNew?viewBean.getDetailFamille().getAnneeHistorique():"")%>'/>
								</td>
			                	<td></td>
			                	<td></td>
			                	<td></td>
			                	<td></td>
			                </tr>
			                <tr>
			                	<td></td>
			                	<td>Code refus</td>
								<td>
									<select id="refus" disabled="disabled" name="detailFamille.refus">
										<option <%=(!viewBeanIsNew && !viewBean.getDetailFamille().getRefus()?"selected=\"selected\"":"")%> value="false">Non</option>
										<option <%=(!viewBeanIsNew && viewBean.getDetailFamille().getRefus()?"selected=\"selected\"":"")%> value="true">Oui [R]</option>
									</select>
								</td>
			                	<td></td>
			                	<td>Début droit</td>
								<td><input id="debutDroit" disabled="disabled"
									 name="detailFamille.debutDroit" type="text"
									 data-g-calendar="type:month; mandatory:false"
									 value='<%=(!viewBeanIsNew?viewBean.getDetailFamille().getDebutDroit():"")%>' />
								</td>
			                	<td></td>
			                	<td></td>
			                	<td></td>
			                	<td></td>
			                </tr>
			                <tr>
			                	<td></td>
			                	<td>Code traitement dossier</td>
								<td>
									<ct:FWCodeSelectTag codeType="AMTRDO" wantBlank="true" name="detailFamille.codeTraitementDossier" defaut="<%=viewBean.getDetailFamille().getCodeTraitementDossier()%>"/>
								</td>
			                	<td></td>
			                	<td>Fin droit</td>
								<td><input id="finDroit" disabled="disabled"
									 name="detailFamille.finDroit" type="text"
									 data-g-calendar="type:month; mandatory:false"
									 value='<%=(!viewBeanIsNew?viewBean.getDetailFamille().getFinDroit():"")%>' />
								</td>
			                	<td></td>
			                	<td></td>
			                	<td></td>
			                	<td></td>
			                </tr>
			                <tr>
			                	<td></td>
			                	<td>Code actif</td>
								<td>
									<select id="codeActif" disabled="disabled" name="detailFamille.codeActif">
										<option <%=(!viewBeanIsNew && viewBean.getDetailFamille().getCodeActif()?"selected=\"selected\"":"")%> value="true">Oui</option>
										<option <%=(!viewBeanIsNew && !viewBean.getDetailFamille().getCodeActif()?"selected=\"selected\"":"")%> value="false">Non</option>										
									</select>
								</td>
			                	<td></td>
			                	<td>Date réception demande recalcul</td>
								<td>
								<input id="dateReceptionDemandeRecalcul" disabled="disabled"
									 name="detailFamille.dateRecepDemandeRecalcul" type="text"
									 data-g-calendar="mandatory:false"
									 value='<%=(!viewBeanIsNew?viewBean.getDetailFamille().getDateRecepDemandeRecalcul():"")%>' />
								</td>
			                	<td></td>
			                	<td></td>
			                	<td></td>
			                	<td></td>
			                </tr>
			                <tr style="height:4px">
			                	<td colspan="10"></td>
		                	</tr>
	               		</table>
               		</td>
                </tr>
                </TABLE>
				&nbsp;
				<TABLE id="zoneContrib" width="100%" style="border:1px solid black" style="background-color:#D7E4FF">
	              	<tr>
	                	<td>
	                		<table id="saisieDetailFamilleCM" width="100%">
	                			<col width="16px" align="left"/>
	                			<col width="240px" align="left"/>
	                			<col width="240px" align="left"/>
	                			<col width="40px" align="left"/>
	                			<col width="240px" align="left"/>
		                		<tr>
		                			<td></td>
		                			<td colspan="3" style="font-weight:bold;">Subside</td>
		                			<td colspan="6" style="font-weight:bold;">Caisse-Maladie</td>
		                		</tr>
				                <tr style="height:4px">
				                	<td colspan="10"></td>
			                	</tr>
			                	
			                	<tr>
			                		<td></td>
			                		<td>Montant contribution </td>
									<td><input id="montantContribution" disabled="disabled" type="text"
										name="detailFamille.montantContribution"
										data-g-amount=" "
										value='<%=(!viewBeanIsNew?viewBean.getDetailFamille().getMontantContribution():"")%>'/>
									</td>
									<td></td>
			                		<td>Caisse-maladie</td>
			                		<td>
								 	<INPUT id="noCaisseMaladie" name="detailFamille.noCaisseMaladie" type="hidden" name="action" value="<%=(!viewBeanIsNew?viewBean.getDetailFamille().getNoCaisseMaladie():"")%>"/>
		                			<% 
		                				// récupération du nom de l'assurance sous forme 
		                				// 290 - assura, 195315
		                				String nomAssurance = viewBean.getNomAssurance();
		                				if("".equals(nomAssurance)){
		                			%>
									<input style="width:210px" id="noCaisseMaladieVisible" disabled="disabled" type="text"
										value='<%=(!viewBeanIsNew?viewBean.getDetailFamille().getNoCaisseMaladie():"")%>'/>
									<% 
									} else {
										nomAssurance=nomAssurance.split(",")[0];								
									%>
									<input style="width:210px" id="noCaisseMaladieVisible" disabled="disabled" type="text"
										value='<%=(!viewBeanIsNew?nomAssurance:"")%>'/>
									<% } %>
		
									<% 
										Object[] tiersMethodsName = new Object[]{
												new String[]{"detailFamille.noCaisseMaladie","idTiers"},
											};
									%>	
			                    	<ct:FWSelectorTag name="tiersSelector"
											methods="<%=tiersMethodsName%>"
											providerApplication="pyxis" 
											providerPrefix="TI"
											providerAction="pyxis.tiers.administration.chercher"								
									/>
			                    	<div id="autoSuggestContainer" class="suggestList">
										<select id="cmSuggestChoice" name="suggestChoice" size="4" style="position:absolute;top:0px;left:0px;">
										<%
											for(int iCaisse=0;iCaisse<allAssurances.length;iCaisse++){
												String[] valuesCM=allAssurances[iCaisse].split(",");										
										%>
										<option <%=(iCaisse==0?"selected=\"selected\"":"")%> value='<%=valuesCM[1]%>'><%=valuesCM[0]%></option>
										<%
											}
										%>
										</select>
			                    	</div>
			                		</td>
			                		<td></td>
			                		<td></td>
			                		<td></td>
			                		<td></td>
			                		<td></td>
			                	</tr>
			                	<tr>
			                		<td>+</td>
			                		<td>Supplément famille</td>
									<td><input id="montantExact1" disabled="disabled" type="text"
										name="detailFamille.supplExtra"
										data-g-amount=" "
										value='<%=(!viewBeanIsNew?(!viewBean.getDetailFamille().getAnneeHistorique().equals("1997") && !viewBean.isSupplementPCFamille()? viewBean.getDetailFamille().getSupplExtra():""):"")%>'/>
									</td>
			                		<td></td>
			                		<td>No assuré</td>
									<td><input style="width:210px" id="noAssure" disabled="disabled" type="text"
										name="detailFamille.noAssure"
										value='<%=(!viewBeanIsNew?viewBean.getDetailFamille().getNoAssure():"")%>'/>
									</td>
			                		<td></td>
			                		<td></td>
			                		<td></td>
			                		<td></td>
			                	</tr>
			                	<tr>
			                		<td>+</td>
			                		<td>Supplément PC famille</td>
									<td><input id="montantExact3" disabled="disabled" type="text"
										name="detailFamille.montantSupplementPCFamille"
										data-g-amount=" "
										value='<%=(!viewBeanIsNew?(!viewBean.getDetailFamille().getAnneeHistorique().equals("1997") && viewBean.isSupplementPCFamille() ?viewBean.getDetailFamille().getSupplExtra():""):"")%>'/>
									</td>
			                		<td></td>
		                			<td>Prime</td>
									<td><input style="width:210px" id="montantPrimeAssurance" disabled="disabled" type="text"
										name="detailFamille.montantPrimeAssurance"
										data-g-amount=" "
										value='<%=(!viewBeanIsNew?viewBean.getDetailFamille().getMontantPrimeAssurance():"")%>'/>
									</td>
			                		<td></td>
			                		<td></td>
			                		<td></td>
			                		<td></td>
			                	</tr>
			                	<tr>
			                		<td>+</td>
			                		<td>Supplément extraordinaire</td>
									<td><input id="montantExact2" disabled="disabled" type="text"
										name="detailFamille.supplExtra"
										data-g-amount=" "
										value='<%=(!viewBeanIsNew || !viewBean.isSupplementPCFamille()?(viewBean.getDetailFamille().getAnneeHistorique().equals("1997")?viewBean.getDetailFamille().getSupplExtra():""):"")%>'/>
									</td>
			                		<td></td>
		                			<td>Prime exacte</td>
									<td><input style="width:210px" id="montantExact" disabled="disabled" type="text"
										name="detailFamille.montantExact"
										data-g-amount=" "
										value='<%=(!viewBeanIsNew?viewBean.getDetailFamille().getMontantExact():"")%>'/>
									</td>
			                		<td></td>
			                		<td></td>
			                		<td></td>
			                		<td></td>
			                	</tr>
			                	<tr>
			                		<td>=</td>
			                		<td>Montant total</td>
			                		<%
			                		String montantContributionTotal="0.0";
			                		double montantContribution = 0.0;
			                		double montantSupplement = 0.0;
			                		double montantSupplementPCFamille = 0.0;
			                		try{
			                			montantContribution = Double.parseDouble(viewBean.getDetailFamille().getMontantContribution());
			                			montantSupplement = Double.parseDouble(viewBean.getDetailFamille().getSupplExtra());
			                			montantContributionTotal = ""+(montantContribution+montantSupplement);
			                		}catch(Exception ex){
			                		}
			                		%>
									<td><input id="montantContribution1" disabled="disabled" type="text"
										name="detailFamille.montantContribution"
										data-g-amount=" "
										value='<%=(!viewBeanIsNew?""+montantContributionTotal:"")%>'/>
									</td>
			                		<td colspan="7"></td>
			                	</tr>
			                	<tr style="height:4px">
				                	<td colspan="10"></td>
			                	</tr>
		                	</table>
	                	</td>
	              	</tr>
            	</TABLE>
				
<%-- /tpl:insert --%>

<!-- FIN DE LA ZONE PRINCIPALE BODY -->
<!-- ************************************************************************************* -->




<!-- ************************************************************************************* -->
<!-- ZONE COMMON BUTTON AND END OF PAGE -->

<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="amal-menuprincipal" showTab="menu" />
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>

<!-- FIN ZONE COMMON BUTTON AND END OF PAGE -->
<!-- ************************************************************************************* -->
