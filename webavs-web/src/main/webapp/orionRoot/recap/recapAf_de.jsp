<%@page import="ch.globaz.al.web.application.ALApplication"%>
<%@page import="org.apache.axis.utils.SessionUtils"%>
<%@page import="ch.globaz.orion.business.models.af.LigneRecapAfEnrichie"%>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.orion.vb.recap.EBRecapAfViewBean"%>
<%@page import="ch.globaz.orion.business.models.af.LigneRecapAf" %>
<%@page import="ch.globaz.al.business.constantes.ALConstGed" %>
<%@page import="ch.globaz.orion.business.models.af.UniteTempsEnum" %>
<%@page import="ch.globaz.orion.business.models.af.MotifChangementAfEnum" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored ="false" %>

<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<%
	ALApplication alApplication = new ALApplication();

	idEcran = "GEB0006";

	EBRecapAfViewBean viewBean = (EBRecapAfViewBean) session.getAttribute("viewBean");
	LigneRecapAfEnrichie selectedLine = viewBean.getRecapAfAndLignes().getListLignesRecapAf().get(0);
	
	String lienAffiliation =  servletContext + "/al?userAction=al.dossier.dossierMain.afficher&selectedId=";
	String imageInfoRemarque = servletContext + "/images/small_info.png";
	
	String lienMaterniteDebut = servletContext + "/apg?userAction=apg.droits.droitLAPGJointDemande.chercher&typePrestation=MATERNITE&idDroit=";
	String lienMaterniteMilieu = "&noAVS=";
	String lienMaterniteFin = "&idTiers=";
	
	String lienMaternite = lienMaterniteDebut + selectedLine.getIdDroitMaternite() + lienMaterniteMilieu + selectedLine.getNss() + lienMaterniteFin + selectedLine.getIdTiers();
	String lienPeriodeMaternite = servletContext + "/apg?userAction=apg.prestation.prestationJointLotTiersDroit.chercher&typePrestation=MATERNITE&forIdDroit=";
	
	//Ajouter le nss entre les deux liens Ged!
	String lienGedDebut = servletContext + "/al?userAction=al.ged.lecture.allocataire&noAVSId=";
	String lienGedFin = "&amp;serviceNameId=" + alApplication.getProperty(ALConstGed.AL_PROPERTY_AFFICHAGE_DOSSIER_GED_ALLOC);
	String lienGedComplet = lienGedDebut + viewBean.getLigneRecapAfSelected().getNss() + lienGedFin;
	
	String actionValider = "orion.recap.recapAf.valider";
	String actionValiderRadier = "orion.recap.recapAf.validerRadier";
	
	formAction = servletContext + mainServletPath;
	
	String user = objSession.getUserId();
	
%>


<%@page import="globaz.jade.common.Jade"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta name="User-Lang" content="<%=languePage%>"/> 
<meta name="Context_URL" content="<%=servletContext%>"/> 
<meta name="formAction" content="<%=formAction%>"/>   
<meta name="TypePage" content="AJAX"/> 
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/moduleStyle.css"/>
<link type="text/css" href="<%=servletContext%>/theme/jquery/jquery-ui.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/ajax/templateZoneAjax.css"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
<SCRIPT type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=servletContext%>/scripts/ValidationGroups.js"></SCRIPT>
<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/ajaxUtils.js"/></script>

<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/AbstractScalableAJAXTableZone.js"/></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/AbstractSimpleAJAXDetailZone.js"/></script>

 <%@ include file="/jade/notation/notationLibJs.jspf" %> 
<script type="text/javascript" src="<%=servletContext%>/scripts/jsnotation/core/specificationMoteur/specific_ajax.js"/></script>


<%@ include file="/jade/notation/notationLibJs.jspf" %> 

<script type="text/javascript">

var dataJson = <%=viewBean.getLignesRecapAfAsJson()%>;
var selectedLine = 0;

$(function () {
	colorCurrentLine(0);
	
	if(dataJson[0].statutLigne === "A_TRAITER" && dataJson[0].uniteTravailCaisse === "MOIS" && dataJson[0].motifChangementCaisse != "VACANCES"){
		$('#validerEtRadier').show();
		$('#divRemarqueCertificat').show();
	} else {
		$('#validerEtRadier').hide();
		$('#divRemarqueCertificat').hide();
	};
	
	if(dataJson[0].statutLigne === "A_TRAITER"){
		$('#valider').show();
	} else {
		$('#valider').hide();
	};
	
	//Redéfinition du lien vers les périodes
	var lienPeriodeMaternite = "<%=lienPeriodeMaternite%>" + dataJson[0].idDroitMaternite;
	$('#lienPeriodeMat a').attr("href", lienPeriodeMaternite);
	
	$('#validerEtRadier').click(function(){
		deleteRemarqueQuotes();
		if($('#dateFinChangement').val() === ""){
			alert('La date de fin du changement doit être renseignée');
		}else if($('#motifChangement').val() === ""){
			alert('Le motif de fin doit être renseigné');
		}else {
			//Action valider radier 
			document.forms[0].elements('userAction').value="orion.recap.recapAf.validerRadier";
			
			$("#main-container").overlay();
			$("#waitDialog").show();
			
			$.ajax({
				data:$("form :input").serializeArray(),
				url: globazNotation.utils.getFromAction(),
				success: function (data) {
					$("#main-container").removeOverlay();
					$("#waitDialog").hide();
					if (ajaxUtils.hasError(data)) {
						ajaxUtils.displayError(data);
					} else {
						//Modificaiton du data JSON pour garder les modification au niveau de l'écran
						updateDataJson(selectedLine);
						dataJson[selectedLine].etatDossierAfLibelle = "<ct:FWLabel key='DETAIL_RECAP_ETAT_DOSSIER_RADIE'/>";
						
						//Mise à jour de la ligne dans le tableau
						updateLigneTableau(selectedLine);
						
						//Mise à jour du tableau des notes
						updateTableauBlocNote(selectedLine);
						
						//Sélection de la ligne suivante
						loadSelectedLine(selectedLine+1);
						
						//met à jour le statut de la récap si toutes les lignes ont été traitées
						if(isAllLignesTraitees()){
							updateStatutRecapToTraitee();
						}
					}
				},
				type: 'POST'
			});
		}
	});

	$('#valider').click(function(){
		deleteRemarqueQuotes();
		
		var idLigneRecap = $('#idLigneRecapDetail').val();
		//Action valider radier
		document.forms[0].elements('userAction').value="orion.recap.recapAf.valider";
		
		$.ajax({
			data:$("form :input").serializeArray(),
			url: globazNotation.utils.getFromAction(),
			success: function (data) {
				if (ajaxUtils.hasError(data)) {
					ajaxUtils.displayError(data);
				} else {
					//Modificaiton du data JSON pour garder les modification au niveau de l'écran
					updateDataJson(selectedLine);
					
					//Mise à jour de la ligne dans le tableau
					updateLigneTableau(selectedLine);
					
					//Mise à jour du tableau des notes
					updateTableauBlocNote(selectedLine);
					
					//Sélection de la ligne suivante
					loadSelectedLine(selectedLine+1);
					
					//met à jour le statut de la récap si toutes les lignes ont été traitées
					if(isAllLignesTraitees()){
						updateStatutRecapToTraitee();
					}
				}
			},
			type: 'POST'
		});
		
	});
	
	$('#annuler').click(function(){
		document.forms[0].elements('userAction').value="orion.recap.recapAf.chercher";
		document.forms[0].submit();
	});
	
	
	$('#motifChangement').change(function() {
		hideOrShowValiderRadier();
	});
	
	$('#uniteTravail').change(function() {
		hideOrShowValiderRadier();
	});
		
});

function deleteRemarqueQuotes(){
	var remarqueObj = $('#remarqueDetail');
	var remarque = remarqueObj.val();
	if(remarque!=null && remarque.length!=0){
		var remarqueWithoutQuote = remarque.replace(/'/g, ' ').replace(/"/g, '');
		remarqueObj.val(remarqueWithoutQuote);
	}
}

function hideOrShowValiderRadier(){
	if(dataJson[selectedLine].statutLigne === "A_TRAITER" && $('#uniteTravail').val() === "MOIS" && $('#motifChangement').val() != "VACANCES"){
		$('#validerEtRadier').show();
		$('#divRemarqueCertificat').show();
	} else {
		$('#validerEtRadier').hide();
		$('#divRemarqueCertificat').hide();
	}
}
	
function loadSelectedLine(idSelectedLine){
	if(idSelectedLine>=dataJson.length){
		idSelectedLine=dataJson.length-1;
	}
	
	//mise en surbrillance de la ligne courante
	colorCurrentLine(idSelectedLine);
	
	selectedLine = idSelectedLine;
	
	$('#idLigneRecapDetail').val(dataJson[idSelectedLine].idLigneRecap);
	
	//Génération des liens WebAVS
	$('#lienGed').attr("href", "<%=lienGedDebut%>" + dataJson[idSelectedLine].nss + "<%=lienGedFin%>");
	$('#lienAf').attr("href", "<%=lienAffiliation%>" + dataJson[idSelectedLine].idDossierAf);
	
	if(dataJson[idSelectedLine].motifChangement === "CONGE_MATERNITE"){
		var lienMaternite = "<%=lienMaterniteDebut%>" + dataJson[idSelectedLine].idDroitMaternite + "<%=lienMaterniteMilieu%>" + dataJson[idSelectedLine].nss + "<%=lienMaterniteFin%>" + dataJson[idSelectedLine].idTiers;
		var lienPeriodeMaternite = "<%=lienPeriodeMaternite%>" + dataJson[idSelectedLine].idDroitMaternite;
		var linkToAdd="<strong><a id='lienMat' href='"+lienMaternite+"'><ct:FWLabel key='DETAIL_RECAP_DONNEES_WEBAVS_BLOC_NOTES_AF_MATERNITE'/></a></strong><br><span id='lienPeriodeMat'></span>";
		$('#zoneLiensMat').html(linkToAdd);
		
		if(dataJson[idSelectedLine].hasOwnProperty('lienPrestationsMaternite')){
			$('#lienPeriodeMat').html(dataJson[idSelectedLine].lienPrestationsMaternite);
			$('#lienPeriodeMat a').attr("href", lienPeriodeMaternite);
			$('#lienPeriodeMat').show()
		} else {
			$('#lienPeriodeMat').hide();
		}
		
		$('#zoneLiensMat').show();
	} else {
		$('#zoneLiensMat').hide();
	}
	
	
	
	$('#idDossierAf').val(dataJson[idSelectedLine].idDossierAf);
	
	//Création du tableau de notes
	var blocNote = dataJson[idSelectedLine].listeBlocNoteAF;
	
	$('#postItZone').empty();
	var windowNote = "'<%=request.getContextPath()%>/orion?userAction=framework.noteit.list&forSourceId="+ dataJson[idSelectedLine].idDossierAf +"&forTableSource=globaz.al.vb.dossier.ALDossierViewBean','note','height=350px,width=450px,resizable=yes,scrollbars=yes'";
	$('#postItZone').append('<p><strong><ct:FWLabel key="DETAIL_RECAP_DONNEES_WEBAVS_BLOC_NOTES_AF"/></strong>&emsp; <img border="0" style="cursor:hand" src="<%=request.getContextPath()%>/images/icon_postit_ok.gif"  onclick="window.open('+ windowNote + ');"></p>');
	
	$('#tableNote tbody').empty();
	
	for(var k in blocNote) {
		$('#tableNote tbody').append( '<tr><td style="text-align:center;">' + blocNote[k].dateCreation + '</td><td style="text-align:center;">' + blocNote[k].user + '</td><td style="text-align:center;">' + blocNote[k].description + '</td><td style="text-align:center;">' + blocNote[k].memo + '</td></tr>' );
	}
	
	
	//Zone de détail des données provenant d'EBu à valider
	$('.prenomDetail').html(dataJson[idSelectedLine].prenomAllocataire);
	$('.nomDetail').html(dataJson[idSelectedLine].nomAllocataire);
	$('.idDossierAfDetail').html(dataJson[idSelectedLine].idDossierAf);
	$('.etatDossierAf').html(dataJson[idSelectedLine].etatDossierAfLibelle);
	$('#nbUniteTravail').val(dataJson[idSelectedLine].nbUniteTravailCaisse);
	$('#uniteTravail').val(dataJson[idSelectedLine].uniteTravailCaisse);
	$('#motifChangement').val(dataJson[idSelectedLine].motifChangementCaisse);
	$('#dateDebutChangement').val(dataJson[idSelectedLine].dateDebutChangementStrCaisse);
	$('#dateFinChangement').val(dataJson[idSelectedLine].dateFinChangementStrCaisse);
	$('#remarqueDetail').val(dataJson[idSelectedLine].remarqueCaisse);
	
	if(dataJson[idSelectedLine].statutLigne === "A_TRAITER" && dataJson[idSelectedLine].uniteTravailCaisse === "MOIS" && dataJson[idSelectedLine].motifChangementCaisse != "VACANCES"){
		$('#validerEtRadier').show();
		$('#divRemarqueCertificat').show();
	} else {
		$('#validerEtRadier').hide();
		$('#divRemarqueCertificat').hide();
	}
	
	if(dataJson[idSelectedLine].statutLigne === "A_TRAITER"){
		$('#valider').show();
	} else {
		$('#valider').hide();
	}

}

function updateDataJson(selectedLine){
	dataJson[selectedLine].nbUniteTravailCaisse = $('#nbUniteTravail').val();
	dataJson[selectedLine].uniteTravailCaisse = $('#uniteTravail').val();
	dataJson[selectedLine].motifChangementCaisse = $('#motifChangement').val();
	dataJson[selectedLine].dateDebutChangementStrCaisse = $('#dateDebutChangement').val();
	dataJson[selectedLine].dateFinChangementStrCaisse = $('#dateFinChangement').val();
	dataJson[selectedLine].remarqueCaisse = $('#remarqueDetail').val();
	dataJson[selectedLine].statutLigne = "TRAITEE";
}

function updateLigneTableau(selectedLine){
	var $tdFinded = $('#tbodyLignesRecap tr').eq(selectedLine).find('td');
	$tdFinded.eq(2).html(dataJson[selectedLine].nbUniteTravailCaisse);
	$tdFinded.eq(3).html($("#uniteTravail option:selected").text());
	$tdFinded.eq(4).html($("#motifChangement option:selected").text());
	$tdFinded.eq(5).html(dataJson[selectedLine].dateDebutChangementStrCaisse);
	$tdFinded.eq(6).html(dataJson[selectedLine].dateFinChangementStrCaisse);

	var remarqueDetailValue = $('#remarqueDetail').val();
	if(remarqueDetailValue!=null && remarqueDetailValue.length!=0)	{
		$tdFinded.eq(7).html("<img src='<%=imageInfoRemarque%>' data-g-bubble='text:¦" + remarqueDetailValue + "¦,wantMarker:false,position:right' title=''<ct:FWLabel key='DETAIL_RECAP_HEADER_LISTE_LIGNES_REMARQUE'/>'/>");
		notationManager.addNotationOnFragment($('#tbodyLignesRecap tr').eq(selectedLine).find('td').eq(7));
	}
	
	$tdFinded.eq(8).html("<ct:FWLabel key='DETAIL_RECAP_STATUT_LIGNE_TRAITEE'/>");
}

function isAllLignesTraitees(){
	var allTreated = true;
	for(i in dataJson){
		var statut = dataJson[i].statutLigne;
		if("TRAITEE"!=statut && "AUCUN_CHANGEMENT"!=statut){
			allTreated = false;
			break;
		}
	}
	return allTreated;
}

function colorCurrentLine(idSelectedLine){
	$(".rowRecap").removeClass("currentLine");
	$("#idLigne_"+idSelectedLine).addClass("currentLine");
}

function updateStatutRecapToTraitee(){
	$("#statutRecapAf").html("<strong><ct:FWLabel key='DETAIL_RECAP_STATUT'/></strong><ct:FWLabel key='DETAIL_RECAP_STATUT_TRAITEE'/>");
}

function updateTableauBlocNote(selectedLine){
	var date = getTodayDateDDMMYYYY();
	
	var note = new Object();
	note["user"] = "<%=user%>";
	note["dateCreation"] = date;
	note["description"] = buildDescriptionNote();
	note["memo"] = "";
	
	dataJson[selectedLine].listeBlocNoteAF.push(note);
	
	$('#tableNote tbody').append( '<tr><td style="text-align:center;">' + note.dateCreation + '</td><td style="text-align:center;">' + note.user + '</td><td style="text-align:center;">' + note.description + '</td><td style="text-align:center;">' + note.memo + '</td></tr>' );
}

function buildDescriptionNote(){
	var description = "Récap ";
	
	description = description.concat($('#periode').val()).concat(" ");
	if($('#motifChangement').val() != "MOIS"){
		description = description.concat($('#nbUniteTravail').val()).concat(" ").concat($('#uniteTravail').val()).concat(",");
	}
	
	if($('#motifChangement').val() != ""){
		description = description.concat(" Motif : ").concat($('#motifChangement option:selected').text());
	}
	
	if($('#dateDebutChangement').val() != ""){
		description = description.concat(" Date de début : ").concat($('#dateDebutChangement').val());
	}
	
	if($('#dateFinChangement').val() != ""){
		description = description.concat(" Date de fin : ").concat($('#dateFinChangement').val());
	}
	
	if($('#remarqueDetail').val() != ""){
		description = description.concat(" Remarque : ").concat($('#remarqueDetail').val());
	}
		
	return description;
}


function getTodayDateDDMMYYYY(){
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!
	var yyyy = today.getFullYear();

	if(dd<10) {
	    dd='0'+dd
	} 

	if(mm<10) {
	    mm='0'+mm
	} 

	today = dd+'.'+mm+'.'+yyyy;
	
	return today;
}

</script>

<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/bootstrap.css"/>
<style type="text/css">

* {
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;
}

.centre {
	vertical-align: middle  !important; 
	text-align: center !important;
	text-align: center;
}
	
.panel-primary {
    border-color: #428bca;
}
.panel {
    margin-bottom: 20px;
    background-color: #fff;
    border: 1px solid transparent;
    border-radius: 4px;
    -webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
    box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
}

.panelWarning {
    margin-bottom: 20px;
    background-color: #faa732;
    border: 1px solid transparent;
    border-radius: 4px;
    -webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
    box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
}

.panel-heading {
    padding: 10px 15px;
    border-bottom: 1px solid transparent;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;
    color: #fff;
    background-color: #4878A2;
    border-color: #428bca;
}

.panel-heading-infos {
    padding: 10px 15px;
    border-bottom: 1px solid transparent;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;
    color: #fff;
    background-color: #4878A2;
    border-color: #428bca;
}


.std-body-height {
    width: 100%;
    overflow-y: auto;
}

.std-body-height-warn {
 	min-height:220px;
    width: 100%;
    overflow-y: auto;
    text-align: center;
}

.panel-body {
    padding: 15px;
}

.dl-horizontal dd {
    margin-left: 170px;
}

.dl-horizontal dt {
    width: 160px;
}

.montantAlign {
	width: 110px;  
	text-align: right;
}

.dl-horizontal-warning-red dd {
	color: red;
}

.dl-horizontal-warning-green dd {
	color: green;
}

.ui-dialog{
	height:650px;
}

.nbNoteIcone{
	top :2px !important;
}

#memo{
	height: 100px !important;
}

input {
  width: auto;
  height: 25px !important;
}

select {
  width: auto;
  height: 25px;
}

textarea {
	width: auto !important;
}

.error {
	border-color: red;
}

.currentLine{
	background-color: #D8D8D8;
}

</style>

<TITLE><%=idEcran%></TITLE>
</HEAD>
<body style="background-color: #B3C4DB">
	<div class="title text-center" style="width: 100%">
<!-- 		<span style="float: left;"   tableSource: EBPUCS_FILE, inList: false"> </span> -->
		<ct:FWLabel key="HEADER_TITRE_DETAIL_RECAP_AF_GEB0006"/>
		<span class="idEcran"><%=(null==idEcran)?"":idEcran%></span>
	</div>
	
	<div class="modal hide" id="waitDialog" data-backdrop="static" data-keyboard="false">
	    <div class="modal-header" align="center">
	        <h3><ct:FWLabel key="DETAIL_LOADING_MESSAGE_RADIATION"/></h3>
	    </div>
	    <div class="modal-body">
	        <div id="ajax_loader">
	            <img src="<%=request.getContextPath() %>/images/loading_animated.gif" style="display: block; margin-left: auto; margin-right: auto;">
	        </div>
	    </div>
	</div>
	
	<form name="mainForm" action="<%=formAction%>" method="post">
	<input type="hidden" name="userAction" value="">
	<input type="hidden" id="periode" name="periode" value="${viewBean.recapAfAndLignes.recapAf.anneeMoisRecapStr}"/>
	<input type="hidden" name="user" value="<%=user%>"/> 
	<input type="hidden" name="numeroAffilie" value="${viewBean.recapAfAndLignes.recapAf.partner.numeroAffilie}"/>
	
	<div id="main-container" class="container-fluid" style="margin-top: 20px">
		<div class="row-fluid">
			<div class="span12">
				<!-- Panel d'information de la récap -->
				<div class="panel panel-primar">
					<div class="panel-heading-infos std-body-height">
						<strong><ct:FWLabel key="DETAIL_RECAP_AFFILIE"/></strong> ${viewBean.recapAfAndLignes.recapAf.partner.numeroAffilie} - ${viewBean.recapAfAndLignes.recapAf.partner.nom}
					</div>
					<div class="panel-heading-infos std-body-height">
						<strong><ct:FWLabel key="DETAIL_RECAP_PERIODE"/></strong> ${viewBean.recapAfAndLignes.recapAf.anneeMoisRecapStr}&emsp;&emsp; 
						<strong><ct:FWLabel key="DETAIL_RECAP_RECUE_LE"/></strong> ${viewBean.recapAfAndLignes.recapAf.lastModificationDateStr}&emsp;&emsp; 
						<span id="statutRecapAf"><strong><ct:FWLabel key="DETAIL_RECAP_STATUT"/></strong> <ct:FWLabel key="${viewBean.recapAfAndLignes.recapAf.statut.getLabel()}"/></span>
					</div>
				</div>
			</div> <!-- fin du span12 -->
		</div>
		<div class="row-fluid">
			<div class="span12">
				<!-- Lignes de la recap-->
				<div class="panel panel-primar">
					<div class="panel-heading">
						<strong><ct:FWLabel key="DETAIL_RECAP_TITRE_LISTE_LIGNES"/></strong>
					</div>
					<div class="panel-body std-body-height">
						<!-- Affichage des particularitées -->
						<div>
							<table class="table table-bordered table-hover table-condensed">
								<thead>
									<tr>
										<th><strong><ct:FWLabel key="DETAIL_RECAP_HEADER_LISTE_LIGNES_NSS"/></strong></th>
										<th style="text-align:center;"><strong><ct:FWLabel key="DETAIL_RECAP_HEADER_LISTE_LIGNES_NOM_PRENOM"/></strong></th>
										<th style="text-align:center;"><strong><ct:FWLabel key="DETAIL_RECAP_HEADER_LISTE_LIGNES_NOMBRE"/></strong></th>
										<th style="text-align:center;"><strong><ct:FWLabel key="DETAIL_RECAP_HEADER_LISTE_LIGNES_UNITE"/></strong></th>
										<th style="text-align:center;"><strong><ct:FWLabel key="DETAIL_RECAP_HEADER_LISTE_LIGNES_MOTIF"/></strong></th>
										<th style="text-align:center;"><strong><ct:FWLabel key="DETAIL_RECAP_HEADER_LISTE_LIGNES_DATE_DEBUT"/></strong></th>
										<th style="text-align:center;"><strong><ct:FWLabel key="DETAIL_RECAP_HEADER_LISTE_LIGNES_DATE_FIN"/></strong></th>
										<th style="text-align:center;"><strong><ct:FWLabel key="DETAIL_RECAP_HEADER_LISTE_LIGNES_REMARQUE"/></strong></th>
										<th style="text-align:center;"><strong><ct:FWLabel key="DETAIL_RECAP_HEADER_LISTE_LIGNES_STATUT"/></strong></th>
									</tr>
								</thead>
								<tbody id="tbodyLignesRecap">
								<c:forEach var="ligne" items="${viewBean.recapAfAndLignes.listLignesRecapAf}">
								<tr id="idLigne_${ligne.idSelectedLine}" class="rowRecap" onclick="loadSelectedLine(${ligne.idSelectedLine});">
									<input type="hidden" name="idSelectedLine" value="${ligne.idSelectedLine}"/>
									<td>${ligne.nss}</td>
									<td style="text-align:center;">${ligne.nomAllocataire} ${ligne.prenomAllocataire}</td>
									<td id="tableNbUniteTravail" style="text-align:center;">${ligne.nbUniteTravailCaisse}</td>
									<td id="tableUniteTravail" style="text-align:center;"><ct:FWLabel key="${ligne.uniteTravailCaisse.getLabel()}"/></td>
									<td id="tableMotifChangement" style="text-align:center;"><ct:FWLabel key="${ligne.motifChangementCaisse.getLabel()}"/></td>
									<td id="tableDateDebutChangement" style="text-align:center;">${ligne.dateDebutChangementStrCaisse}</td>
									<td id="tableDateFinChangement" style="text-align:center;">${ligne.dateFinChangementStrCaisse}</td>
									<td id="tableRemarque" style="text-align:center;">
										<c:if test="${ligne.isRemarque()}">
											<img src="<%=imageInfoRemarque%>" data-g-bubble='text:¦${ligne.getRemarqueCaisse()}¦,wantMarker:false,position:right' title="<ct:FWLabel key="DETAIL_RECAP_HEADER_LISTE_LIGNES_REMARQUE"/>"/>
										</c:if>
									</td>
									<td id="tableStatutLigne" style="text-align:center;"><ct:FWLabel key="${ligne.statutLigne.getLabel()}"/></td>
								</tr>
							</c:forEach>
								</tbody>
							</table>			
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span6">	
				<!-- Données WebAVS -->
				<div class="panel panel-primar">
					<div class="panel-heading">
						<strong><ct:FWLabel key="DETAIL_RECAP_DONNEES_WEBAVS"/></strong>
					</div>
					<div class="panel-body std-body-height-warn">
						<div style="text-align:left; text-decoration: underline;">
								<strong><span class="prenomDetail">${viewBean.ligneRecapAfSelected.prenomAllocataire}</span> <span class="nomDetail">${viewBean.ligneRecapAfSelected.nomAllocataire}</span> - <span class="idDossierAfDetail">${viewBean.ligneRecapAfSelected.idDossierAf}</span> - <span class="etatDossierAf">${viewBean.ligneRecapAfSelected.etatDossierAfLibelle}</span></strong>
						</div><br>
						<div style="text-align:left;">
						<input type="hidden" id="idDossierAf" name="idDossierAf" value="${viewBean.getLigneRecapAfSelected().getIdDossierAf()}"/>
							<p><strong><a id="lienAf" href="<%=lienAffiliation+viewBean.getLigneRecapAfSelected().getIdDossierAf()%>"><ct:FWLabel key="DETAIL_RECAP_DONNEES_WEBAVS_DOSSIER_AF"/></a></strong>&emsp;&emsp;
							<strong><a id="lienGed" target="GED_CONSULT" href="<%=lienGedComplet%>"/><ct:FWLabel key="DETAIL_RECAP_DONNEES_WEBAVS_GED"/></a></strong></p>
							<div id="postItZone">
								<p><strong><ct:FWLabel key="DETAIL_RECAP_DONNEES_WEBAVS_BLOC_NOTES_AF"/></strong>&emsp; <ct:FWNote sourceId="${viewBean.getLigneRecapAfSelected().getIdDossierAf()}" tableSource="globaz.al.vb.dossier.ALDossierViewBean"/></p>
			              	</div>
						</div>
						<table id="tableNote" class="table table-bordered table-hover table-condensed">
								<thead>
									<tr>
										<th style="text-align:center;"><strong><ct:FWLabel key="DETAIL_RECAP_DONNEES_WEBAVS_BLOC_NOTES_AF_DATE"/></strong></th>
										<th style="text-align:center;"><strong><ct:FWLabel key="DETAIL_RECAP_DONNEES_WEBAVS_BLOC_NOTES_AF_USER"/></strong></th>
										<th style="text-align:center;"><strong><ct:FWLabel key="DETAIL_RECAP_DONNEES_WEBAVS_BLOC_NOTES_AF_DESC"/></strong></th>
										<th style="text-align:center;"><strong><ct:FWLabel key="DETAIL_RECAP_DONNEES_WEBAVS_BLOC_NOTES_AF_NOTE"/></strong></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="note" items="${viewBean.ligneRecapAfSelected.listeBlocNoteAF}">
										<tr>
											<td style="text-align:center;">${note.dateCreation}</td>
											<td style="text-align:center;">${note.user}</td>
											<td style="text-align:center;">${note.description}</td>
											<td style="text-align:center;">${note.memo}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
							<div id="zoneLiensMat" style="text-align:left;">
								<c:if test="${viewBean.ligneRecapAfSelected.isMotifCongeMaternite()}">	
										<strong><a id="lienMat" href="<%=lienMaternite%>"><ct:FWLabel key="DETAIL_RECAP_DONNEES_WEBAVS_BLOC_NOTES_AF_MATERNITE"/></a></strong><br>
										<span id="lienPeriodeMat">
											<%=viewBean.getLigneRecapAfSelected().getLienPrestationsMaternite()%>
										</span>
									
								</c:if>
							</div>
					</div>
				</div>
				</div>
				<div class="span6">	
				<!-- Données à valider -->
				<div class="panel panel-primar">
					<div class="panel-heading">
						<strong><ct:FWLabel key="DETAIL_RECAP_DONNEES_A_VALIDER"/></strong>
					</div>
					<div class="panel-body std-body-height-warn">
						<input id="idLigneRecapDetail" type="hidden" name="idLigneRecapDetail" value="${viewBean.ligneRecapAfSelected.idLigneRecap}"/>
						<div style="text-align:left; text-decoration: underline;">
							<strong><span class="prenomDetail">${viewBean.ligneRecapAfSelected.prenomAllocataire}</span> <span class="nomDetail">${viewBean.ligneRecapAfSelected.nomAllocataire}</span> - <span class="idDossierAfDetail">${viewBean.ligneRecapAfSelected.idDossierAf}</span></strong>
						</div><br>
						<table class="table table-bordered table-hover table-condensed">
							<thead>
								<tr>
									<th style="text-align:center;"><strong><ct:FWLabel key="DETAIL_RECAP_HEADER_LISTE_LIGNES_NOMBRE"/></strong></th>
									<th style="text-align:center;"><strong><ct:FWLabel key="DETAIL_RECAP_HEADER_LISTE_LIGNES_UNITE"/></strong></th>
									<th style="text-align:center;"><strong><ct:FWLabel key="DETAIL_RECAP_HEADER_LISTE_LIGNES_MOTIF"/></strong></th>
									<th style="text-align:center;"><strong><ct:FWLabel key="DETAIL_RECAP_HEADER_LISTE_LIGNES_DATE_DEBUT"/></strong></th>
									<th style="text-align:center;"><strong><ct:FWLabel key="DETAIL_RECAP_HEADER_LISTE_LIGNES_DATE_FIN"/></strong></th>
								</tr>
							</thead>
							<tbody>
								<tr>
								<td><input id="nbUniteTravail" name="nbUniteTravail" type="text"  value="${viewBean.ligneRecapAfSelected.nbUniteTravailCaisse}"/></td>
									<td>
									<select id="uniteTravail" name="uniteTravail" value="${viewBean.ligneRecapAfSelected.uniteTravailCaisse}">
									<OPTION value=''></OPTION>
										<%for(UniteTempsEnum unite : UniteTempsEnum.values()) {%>
											<% if(viewBean.getLigneRecapAfSelected().getUniteTravailCaisse() != null && viewBean.getLigneRecapAfSelected().getUniteTravailCaisse().equals(unite)) {%>
												<OPTION selected="selected" value='<%=unite%>'><ct:FWLabel key="<%=unite.getLabel()%>"/></OPTION>
											<%} else {%>
												<OPTION value='<%=unite%>'><ct:FWLabel key="<%=unite.getLabel()%>"/></OPTION>
											<%} %>
										<%}%>	
									</select></td>
									<td><select id="motifChangement" name="motifChangement" value="${viewBean.ligneRecapAfSelected.motifChangementCaisse}"/>
										<OPTION value=''></OPTION>
										<%for(MotifChangementAfEnum motif : MotifChangementAfEnum.values()) {%>
											<% if(viewBean.getLigneRecapAfSelected().getMotifChangementCaisse() != null && viewBean.getLigneRecapAfSelected().getMotifChangementCaisse().equals(motif)) {%>
												<OPTION selected="selected" value='<%=motif%>'><ct:FWLabel key="<%=motif.getLabel()%>"/></OPTION>
											<%} else {%>
												<OPTION value='<%=motif%>'><ct:FWLabel key="<%=motif.getLabel()%>"/></OPTION>
											<%} %>
										<%}%>	
									</select></td>
									</td>
									<td><input id="dateDebutChangement" name="dateDebutChangement" type="text" data-g-calendar="" value="${viewBean.ligneRecapAfSelected.dateDebutChangementStrCaisse}"/></td>
									<td><input id="dateFinChangement" name="dateFinChangement" type="text" data-g-calendar ="" value="${viewBean.ligneRecapAfSelected.dateFinChangementStrCaisse}"/></td>
								</tr>
							</tbody>
						</table>
						<div style="text-align:left;">
							<p><strong><ct:FWLabel key="DETAIL_RECAP_HEADER_LISTE_LIGNES_REMARQUE"/></strong></p>
							<textarea id="remarqueDetail" name="remarqueDetail" maxlength="254" rows="4" cols="100">${viewBean.ligneRecapAfSelected.getRemarqueCaisse()}</textarea>
						</div>
						<div id="divRemarqueCertificat" style="text-align:left;">
							<p><strong><ct:FWLabel key="DETAIL_RECAP_HEADER_LISTE_LIGNES_TEXTE_CERTIFICAT"/></strong></p>
							<textarea id="texteLibre" name="texteLibre" maxlength="254" rows="4" cols="100"></textarea>
						</div>
					</div>
				</div>
			</div>
			</div>
			<div class="row-fluid">
				  <div class="span12 text-right">
				   	<button type="button" id="validerEtRadier" class="btn"><strong><ct:FWLabel key="DETAIL_RECAP_BTN_VALIDER_RADIER"/></strong></button>
					<button type="button" id="valider" class="btn"><strong><ct:FWLabel key="DETAIL_RECAP_BTN_VALIDER"/></strong></button>
					<button type="button" id="annuler" class="btn"><strong><ct:FWLabel key="DETAIL_RECAP_BTN_RETOUR"/></strong></button>
				  </div>
			</div>
			</div>
		</div>
	</form>
	
	<SCRIPT>
	if(top.fr_error!=null) {
		top.fr_error.location.replace(top.fr_error.location.href);
	}	
	</SCRIPT>
	
	<ct:menuChange displayId="menu" menuId="EBMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="EBMenuVide" showTab="menu"/>	
	</body>
</html>