<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.fweb.util.JavascriptEncoder"%>
<%@ page import="ch.globaz.al.business.services.ALServiceLocator" %>
<%@ page import="ch.globaz.al.business.models.dossier.DossierComplexModel" %>
<%@ page import="ch.globaz.al.business.models.allocataire.AllocataireComplexModel" %>


<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<% 
idEcran = "AL0100";
rememberSearchCriterias = true;

DossierComplexModel currentDossier = ALServiceLocator.getDossierComplexModelService().read(request.getParameter("searchModel.forIdDossier"));
String idAllocataire = currentDossier.getAllocataireComplexModel().getAllocataireModel().getIdAllocataire();
String nomAllocataire = currentDossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1();
String prenomAllocataire = currentDossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2();



%>

<%-- /tpl:insert --%>


<%@ include file="/theme/find/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/dossier/cloud-zoom.css" />
<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/dossier/cloud-zoom.1.0.2.js"/></script>
<script type="text/javascript">

usrAction = "al.dossier.dossierEnvoi.lister";
bFind=true;

function resetSearchFields(){
}

$(function() {
	
	$('#searchFieldsDossierEnvoiTable tr').css({'height':'26px'});
	$('#newDossierEnvoiTable tr').css({'height':'26px'});
	
	// ---------------------------------------------------------------------
	// Action pour afficher la boite de sélection des templates
	// ---------------------------------------------------------------------
	$('#idBtnNewDocument').click(function(e) {
		$('#dialogTemplateSelection').dialog('open');
		if(allCSDocument == null){
			getCSDocuments(<%=request.getParameter("searchModel.forIdDossier")%>);
		}
	});
	// ---------------------------------------------------------------------
	// Réaction sur la sélection du template
	// ---------------------------------------------------------------------
	$('#listeModele').change(function(e){
		refreshPreviewPicture();
	});

	// ---------------------------------------------------------------------
	// Initialisation du dialogue de sélection des templates
	// ---------------------------------------------------------------------
	$('#dialogTemplateSelection').dialog(
			{
				autoOpen:false,
				buttons:[	{
								text:"<%=objSession.getLabel("AL0100_POPUP_BOUTON_OK")%>",
								click: function() {
													callDocumentGeneration();
													$(this).dialog("close");
													}
							},
							{
								text:"<%=objSession.getLabel("AL0100_POPUP_BOUTON_CANCEL")%>",
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

});

//----------------------------------------------------------------
//Object javascript pour enregistrer les codes systèmes ALENVOIDOC
//----------------------------------------------------------------
function CSDocument(_cu,_libelleShort,_libelle) {
	this.cu = _cu;
	this.libelleShort = _libelleShort;
	this.libelle = _libelle;
}
var allCSDocument = null;
var allFormuleIdCS = null;
var currentIdDossier = null;
//------------------------------------------------------------
//Appel Ajax pour récupérer les codes systèmes des documents
//------------------------------------------------------------
function getCSDocuments(_currentIdDossier) {
	currentIdDossier = _currentIdDossier;
	var o_options= {
		serviceClassName: 'ch.globaz.al.business.services.envoi.EnvoiItemService',
		serviceMethodName:'getAvailableDocumentsCSList',
		parametres: currentIdDossier,
		callBack: loadCSDocuments
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}
//------------------------------------------------------------
//CallBack Ajax pour charger les codes systèmes documents AL
//------------------------------------------------------------
function loadCSDocuments(data){
	// Check si réception ok et création de l'array en relation
	if(data.length>0){
		allCSDocument = new Array;
		allFormuleIdCS = new Array;
	}
	// Itération sur tous les codes systèmes ALENVOIDOC
	for(var i_Element=0; i_Element<data.length;i_Element++){
		var s_CodeSysteme = data[i_Element].idCode;
		var s_LibelleShort = data[i_Element].libelle;
		var s_Libelle = data[i_Element].currentCodeUtilisateur.libelle;
		var s_CodeUser = data[i_Element].currentCodeUtilisateur.codeUtilisateur;
		allCSDocument[s_CodeSysteme]=new CSDocument(s_CodeUser,s_LibelleShort,s_Libelle);
	}
	// Appel ajax de la liste des documents présents
	if(data.length>0){
		generateDocumentList(currentIdDossier);
	}else{
		// fermeture de la dialog box
		alert('Aucun document n\'est actuellement configuré dans le système.');
		$('#dialogTemplateSelection').dialog('close');
		allCSDocument = null;
	}
}
//------------------------------------------------------------
//Appel Ajax pour générer la combobox des documents disponibles
//------------------------------------------------------------
function generateDocumentList(_currentIdDossier) {
	var o_options= {
		serviceClassName: 'ch.globaz.al.business.services.envoi.EnvoiItemService',
		serviceMethodName:'getAvailableDocumentsList',
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
		var s_FormuleId = data[i_Element].envoiTemplateSimpleModel.idFormule;
		var s_TextToInsert = '<option value="'+s_FormuleId+'"';
		if(i_Element===0){
			s_TextToInsert += ' selected="selected"';
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
		s_TextToInsert+='</option>';
		$('#listeModele').append(s_TextToInsert);
		allFormuleIdCS[s_FormuleId] = s_CodeSysteme;
	}
	
	if(data.length<=0){
		// fermeture de la dialog box
		alert('Aucun document n\'est actuellement configuré dans le système.\n\nA insérer depuis <Paramètres><Formules>');
		$('#dialogTemplateSelection').dialog('close');
		allCSDocument = null;
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
	var s_selectedFormule = $('#listeModele option:selected').val();
	// récupération du cs correspondant
	var s_codeSysteme = allFormuleIdCS[s_selectedFormule];
	// set l'image
	var s_pathImage = '<%=request.getContextPath()%>/images/al/';
	
	return s_pathImage+s_codeSysteme;
}

//------------------------------------------------------------
// Appel des méthodes nécessaires à la génération du document
//------------------------------------------------------------
function callDocumentGeneration(){
	var currentIdFormule = $('#listeModele option:selected').val();
	generateDocument(currentIdDossier, currentIdFormule);
}
//------------------------------------------------------------
// Appel Ajax pour générer le document wordML
//------------------------------------------------------------
function generateDocument(_currentIdDossier, _currentIdFormule) {
	var o_options= {
		serviceClassName: 'ch.globaz.al.business.services.envoi.EnvoiItemService',
		serviceMethodName:'generateWordFile',
		parametres:_currentIdDossier+","+_currentIdFormule,
		callBack: openDocumentInWord
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}
//------------------------------------------------------------
// CallBack Ajax pour afficher erreurs/documents WordML
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
				location.reload();
			}
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

</script>

<%-- /tpl:insert --%>

<div id="dialogTemplateSelection" title="<%=objSession.getLabel("AL0100_POPUP_TITRE")%>">
	<table id="listeModeleTable" style="widht=100%">
		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td></td>
			<td align="center">
				<select style='font-family:"Consolas";font-size:16px;width:600px' id="listeModele">
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





<%@ include file="/theme/find/bodyStart.jspf" %>
	<%-- tpl:insert attribute="zoneTitle" --%>
		<%=objSession.getLabel("AL0100_TITRE_PRINCIPAL") %>
		<%=" "+request.getParameter("searchModel.forIdDossier")%>
		<%=" - "+objSession.getLabel("AL0100_TITRE_ALLOCATAIRE") %>
		<%=idAllocataire %> <%=nomAllocataire %> <%=prenomAllocataire %>
	<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>

<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
	<table style="width:100%">
		<tr>
		<td width="20px">&nbsp;</td>
		<td style="vertical-align:middle">
			<div id="searchFieldsDossierEnvoi" style="float:left;width:440px; height:180px">
				<INPUT type="hidden" name="userAction" value="">
				<INPUT type="hidden" name="_sl" value="">
				<INPUT type="hidden" name="_method" value="">
				<INPUT type="hidden" name="_valid" value="">
				<INPUT type="hidden" name="colonneSelection" value="null">

				<INPUT type="hidden" name="searchModel.forIdDossier" value="<%=request.getParameter("searchModel.forIdDossier")%>">
				<INPUT type="hidden" name="idAllocataire" value="<%=idAllocataire%>">
				<INPUT type="hidden" name="nomAllocataire" value="<%=nomAllocataire%>">
				<INPUT type="hidden" name="prenomAllocataire" value="<%=prenomAllocataire%>">

				<table id="searchFieldsDossierEnvoiTable" class="zone" style="width=100%" >
					<tr>
						<td></td>
						<td class="subtitle" colspan ="3"><ct:FWLabel key="AL0100_AFFICHER_DOCUMENTS"/></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td></td>
						<td><ct:FWLabel key="AL0100_AFFICHER_DOCUMENTS_STATUS"/></td>
						<td><ct:FWCodeSelectTag
							name="searchModel.forEnvoiStatus" codeType="ALENVOISTS"
							defaut="" wantBlank="true" />
						</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td></td>
						<td><ct:FWLabel key="AL0100_AFFICHER_DOCUMENTS_DATE_CREATIONDESLE"/></td>
						<td><input tabindex="2" class="clearable" type="text"
							name="searchModel.forJobDateMin" value=""
							data-g-calendar="mandatory:false" />
						</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td></td>
						<td><ct:FWLabel key="AL0100_AFFICHER_DOCUMENTS_DATE_CREATIONJUSQUAU"/></td>
						<td><input tabindex="3" class="clearable" type="text"
							name="searchModel.forJobDateMax" value=""
							data-g-calendar="mandatory:false" />
						</td>
						<td></td>
						<td></td>
						<!--<td align="right"><INPUT type="button" name="btnClear" id="idBtnClear" value="Clear"/></td>-->
						<td></td>
					</tr>
					<tr>
						<td colspan="4"></td>
						<td align="right"><INPUT type="submit" name="btnFind" id="idBtnFind" value="<%=btnFindLabel%>"/></td>
						<td></td>
					</tr>
				</table>
			</div>
			
			<div id="newDossierEnvoi" style="float:right;width:440px; height:180px">
				<table id="newDossierEnvoiTable" class="zone" style="width:100%">
					<tr>
						<td></td>
						<td class="subtitle" colspan="2"><ct:FWLabel key="AL0100_NOUVEAU_DOCUMENT_TITRE"/></td>
						<td></td>
					</tr>
					<tr>
						<td></td>
						<td colspan="3"><ct:FWLabel key="AL0100_NOUVEAU_DOCUMENT_LIGNE1"/></td>
					</tr>
					<tr>
						<td></td>
						<td colspan="3"><ct:FWLabel key="AL0100_NOUVEAU_DOCUMENT_LIGNE2"/></td>
					</tr>
					<tr>
						<td></td>
						<td colspan="3"><ct:FWLabel key="AL0100_NOUVEAU_DOCUMENT_LIGNE3"/></td>
					</tr>
					<tr>
						<td colspan="2"><td>
						<td align="right"><INPUT type="button" id="idBtnNewDocument" name="btnNewDocument" value="<%=objSession.getLabel("AL0100_BOUTON_CREER_DOCUMENT") %>"/></td>
						<td></td>
					</tr>
				</table>
			</div>
		</td>
		<td  width="20px">&nbsp;</td>
		</tr>
		</table>
	</td>
</tr>

<tr>	
	<td>
	<table id="tableWithIFrame" style="width:100%;">
		<tr>
		<td  width="20px">&nbsp;</td>
		<td>
			<IFRAME id="fr_list"  name="fr_list" scrolling="YES" style="border : solid 1px black; width:100%;" height="<%=IFrameHeight%>">
			</IFRAME>
		</td>
		<td  width="20px">&nbsp;</td>
		</tr>
	</table>
	</td>
</tr>

<tr>
	<td>&nbsp;</td>
</tr>
<%-- /tpl:insert --%>

<!--<%@ include file="/theme/find/bodyButtons.jspf" %>-->

<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>

<!-- Si bodyButtons n'est pas inclus, compléter par ...	

					</TBODY>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD>

 ... fin du complément ...	-->

					</TBODY>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD>



<!--<%@ include file="/theme/find/bodyEnd.jspf" %>-->

<!-- Si bodyEnd n'est pas inclus, compléter par ...	

			</TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>

 ... fin du complément + scripts...	-->

			</TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>

<div id="waitingPopup" style="width:120;height:50;position : absolute ; visibility : hidden">
<table border="0" cellspacing="0" cellpadding="0" bgColor="#FFFFFF" style="border: solid  1 black ; width:200;height:100%;">
	<tr>
		<td>
			<img src="<%=request.getContextPath()%>/images/<%=languePage%>/labelRecherche.gif">
		</td>
		<td>
			<img src="<%=request.getContextPath()%>/images/points.gif">
		</td>
		<td>
			<img src="<%=request.getContextPath()%>/images/disc.gif">
		</td>
	</tr>
</table>
</div>

<script type="text/javascript">
	document.getElementById("waitingPopup").style.left = document.body.clientWidth/2 - document.getElementById("waitingPopup").offsetWidth/2;
	//document.getElementById("waitingPopup").style.top  = getAnchorPosition("waitingPopup").y + <%=IFrameHeight%>/2 - document.getElementById("waitingPopup").clientHeight/2;
	document.getElementById("waitingPopup").style.top  = getAnchorPosition("fr_list").y+<%=IFrameHeight%>/2 - document.getElementById("waitingPopup").clientHeight/2;
	if (bFind)
		showWaitingPopup();

	oBtnFind = document.getElementById("btnFind");
	if (oBtnFind) {
		savedFindOnClick = oBtnFind.onclick;
	}
	oBtnNew = document.getElementById("btnNew");
	if (oBtnNew) {
		savedNewOnClick = oBtnNew.onclick;
	}

	oBtnExport = document.getElementById("btnExport");
	if (oBtnExport) {
		savedExportOnClick = oBtnExport.onclick;
	}
</script>


<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<ct:menuChange displayId="options" menuId="allocataire-detail" showTab="options" checkAdd="no" >		
	<ct:menuSetAllParams checkAdd="no" key="idAllocataire" value="<%=idAllocataire%>"/>
	<ct:menuSetAllParams checkAdd="no" key="nomAllocataire" value="<%=nomAllocataire%>"/>
	<ct:menuSetAllParams checkAdd="no" key="prenomAllocataire" value="<%=prenomAllocataire%>"/>
	<ct:menuSetAllParams checkAdd="no" key="typeActivite" value="<%=JadeStringUtil.isEmpty(request.getParameter(\"typeActivite\"))?\"\":request.getParameter(\"typeActivite\")%>"  />	
	<ct:menuSetAllParams checkAdd="no" key="_method" value="<%=JadeStringUtil.isEmpty(request.getParameter(\"_method\"))?\"\":request.getParameter(\"_method\")%>"  />	
	<ct:menuSetAllParams checkAdd="no" key="idDossier" value="<%=request.getParameter(\"searchModel.forIdDossier\")%>"/>	
</ct:menuChange>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyClose.jspf" %>
