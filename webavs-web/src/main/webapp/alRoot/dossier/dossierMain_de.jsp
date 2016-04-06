<%@page import="ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel"%>
<%@page import="ch.globaz.al.business.models.droit.TarifAggregator"%>
<%@page import="globaz.al.vb.dossier.ALDossierMainViewBean"%>
<%@page import="ch.globaz.al.business.adapters.prestation.PrestationHolder"%>
<%@page import="ch.globaz.al.business.models.prestation.DetailPrestationComplexModel"%>
<%@page import="ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.jade.client.util.JadeCodesSystemsUtil"%>
<%@page import="ch.globaz.al.business.models.dossier.DossierComplexSearchModel"%>
<%@page import="ch.globaz.al.business.services.models.dossier.DossierComplexModelService"%>
<%@page import="ch.globaz.al.business.models.dossier.DossierModel"%>
<%@page import="java.util.Date"%>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/detail/header.jspf" %>

<%@page import="ch.globaz.al.business.constantes.ALCSPays"%>
<%@page import="ch.globaz.al.business.constantes.ALConstGed"%>
<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.al.business.models.prestation.EntetePrestationModel"%>
<%@page import="ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel"%>
<%@page import="globaz.globall.util.JANumberFormatter" %>
<%@page import="java.util.Map"%>
<%@page import="java.util.Map.Entry"%>

<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALDossierMainViewBean viewBean = (ALDossierMainViewBean) session.getAttribute("viewBean"); 
	selectedIdValue=viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER"); btnCanLabel = objSession.getLabel("ANNULER"); btnNewLabel = objSession.getLabel("NOUVEAU");
	
	boolean hasNewRight = objSession.hasRight(userActionNew, FWSecureConstants.ADD); boolean hasUpdateRight = objSession.hasRight(userActionUpd, FWSecureConstants.UPDATE);
	
	idEcran="AL0004";
%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<script type="text/javascript">
 
  var LANGUAGES ="<ct:FWLabel key='AL0004_BENEFICAIRE_W_BENEF'/>";
  var CS_PAIEMENT_TIERS = "<%=ALCSDossier.PAIEMENT_TIERS%>"; 
  var CS_PAIEMENT_DIRECT = "<%=ALCSDossier.PAIEMENT_DIRECT%>"; 
  var CS_PAIEMENT_INDIRECT = "<%=ALCSDossier.PAIEMENT_INDIRECT%>";   
  
  var CS_SALARIE = "<%=ALCSDossier.ACTIVITE_SALARIE%>";  
    
  var NB_AFFILIATION = "<%=viewBean.getListAffiliation().size()%>";
  
  var urlServletPath = "<%=servletContext%><%=mainServletPath%>";

</script>

<%@page import="globaz.fweb.util.JavascriptEncoder"%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.al.business.constantes.ALCSPrestation"%>
<%@page import="globaz.jade.client.util.JadeNumericUtil"%>
<%@page import="ch.globaz.al.business.models.droit.CalculBusinessModel"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="ch.globaz.al.business.models.dossier.DossierComplexModel"%>
<%@page import="ch.globaz.al.business.constantes.ALCSDossier"%>
<%@page import="ch.globaz.al.business.services.ALServiceLocator"%>
<%@page import="ch.globaz.al.business.constantes.ALCSDroit"%>
<%@page import="ch.globaz.naos.business.service.AffiliationService"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/ajax_webaf.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/scripts/jquery.tablesorter.js"></script>
<script type="text/javascript">

searchAjaxInputId = "searchNssCriteria";
autoSuggestContainerId = "autoSuggestContainer";
prefixModel = "dossierComplexModel.allocataireComplexModel";
function add() {
    document.forms[0].elements('userAction').value="al.dossier.dossierMain.ajouter";
}
function upd() {
	//on affiche les liens retirer qui sont cachés si on est pas en mode modifier
	for (key in document.getElementsByTagName('<%=ALCSDossier.PAIEMENT_INDIRECT%>'))
  	{
		if(document.getElementsByTagName('a')[key].className=='removeLink')
			document.getElementsByTagName('a')[key].style.display='inline';
  	}
	
    document.forms[0].elements('userAction').value="al.dossier.dossierMain.modifier";


	//Curseur par défaut sur le champ NSS ou titre si nss non éditable
	if(document.getElementById('partialforNumAvs')!=null)
		document.getElementById('partialforNumAvs').focus();
	else if(document.getElementById('dossierComplexModel.dossierModel.activiteAllocataire')){
		var activiteAllocataire = document.getElementById('dossierComplexModel.dossierModel.activiteAllocataire');
		if(!activiteAllocataire.getAttributeNode('readOnly')) {
			activiteAllocataire.focus();
		}
	}
	
	document.getElementById('dossierAgricoleComplexModel.allocataireAgricoleComplexModel.agricoleModel.idAllocataire').value = <%=viewBean.getDossierComplexModel().getAllocataireComplexModel().getAllocataireModel().getIdAllocataire()%>;
	document.getElementById('dossierAgricoleComplexModel.allocataireAgricoleComplexModel.agricoleModel.domaineMontagne').value = <%=viewBean.getDossierAgricoleComplexModel().getAllocataireAgricoleComplexModel().getAgricoleModel().getDomaineMontagne()%>;
    
}

function validate() {
	var typeDomaine = $('#TypeDomaineAgriculteur').val();
	var msgErreur = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_ERREUR_DOMAINE_ACTIVITE_OBLIGATOIRE"))%>';
	if(typeDomaine==null || typeDomaine.length==0 || typeDomaine==""){
		alert(msgErreur);
		return;
	}
	
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.dossier.dossierMain.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.dossier.dossierMain.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.dossier.dossier.chercher";
	} else {
        document.forms[0].elements('userAction').value="al.dossier.dossierMain.afficher";
	}
}

function del() {
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.dossier.dossierMain.supprimer";
        document.forms[0].submit();
    }
}

function init(){
	initDebugManager();

	$('#modePaiementDossier').change(function(){	
		if(this.value == CS_PAIEMENT_DIRECT){			
			var idTiersAlloc = document.getElementsByName('dossierComplexModel.allocataireComplexModel.allocataireModel.idTiersAllocataire')[0].value;
			$('#idTiersBeneficiaire').val(idTiersAlloc);
		}
		if(this.value == CS_PAIEMENT_INDIRECT){	
			$('#idTiersBeneficiaire').val('0');
		}
		
		//si CS_PAIEMENT_TIERS, on ne fait rien car rempli par widget
	
	});
	
	//test sur le paramètre selectedIndex de la requête, si défini, alors ca signifie qu'on revient de la sélection du tiers
	isFromSelectionTiers = '<%=JavascriptEncoder.getInstance().encode(request.getParameter("selectedIndex")!=null?request.getParameter("selectedIndex"):"")%>';

	initNssInputComponent('<%=JavascriptEncoder.getInstance().encode(idEcran)%>','forNumAvs');
	//1-0-3 K_WIDGET :initNumAffInputComponent(document.getElementById('dossierComplexModel.dossierModel.numeroAffilie'),document.getElementById('dossierComplexModel.dossierModel.activiteAllocataire'),document.getElementById('dateCalcul'));
	$('#forNumAffilieWidget').val($('#numAffilieValue').val());
		
	initCodeCaisseInputComponent('dossierComplexModel.caisseAFComplexModel.codeAdministration');
	initPagination("linePrestation",document.getElementById("paginationPrevious"),
			document.getElementById("paginationNext"),document.getElementById("paginationNumero"),
			"show","hide",15,<%=JavascriptEncoder.getInstance().encode(Integer.toString(viewBean.getEntetesPrestations().size()))%>);
	//pas besoin d'afficher le bouton, on appelle le onclick par code quand on est
	//met mode de paiement => tiers beneficiaire
	if(document.forms[0].elements('benefSelector')!= null)
		document.forms[0].elements('benefSelector').style.display="none";

	//Définition des méthodes event des éléments de l'écran
	if(document.getElementById('btnCalcul')!= null){
		document.getElementById('btnCalcul').onclick = function(){
			document.getElementsByName("userAction")[0].value="al.dossier.dossierMain.afficher";
			document.forms[0].submit();
		};	
	}

	$('#dossierComplexModel\\.dossierModel\\.activiteAllocataire').on('change', function(){
		var val = $(this).val();		
		if (val == 61040001 || val == 61040002 || val == 61040006) {
			$("#TypeDomaineAgriculteur").css("visibility", "visible");
			$("#TypeDomaineAgriculteur").css("width", "80px");	
		} else {
			$("#TypeDomaineAgriculteur").css("visibility", "hidden");	
			$("#TypeDomaineAgriculteur").css("width", "0px");	
			$("#TypeDomaineAgriculteur").val("false");
		}
		
		if(val == 61040005){
			$("#complementActiviteAllocataire").css("visibility", "visible");
			$("#complementActiviteAllocataire").css("width", "80px");	
		}else{
			$("#complementActiviteAllocataire").css("visibility", "hidden");	
			$("#complementActiviteAllocataire").css("width", "0px");
			$("#complementActiviteAllocataire").val("");
		}
		
		$('#activiteAllocataire').val(val);
		
		$("#TypeDomaineAgriculteur").focus();
		document.getElementById('dossierComplexModel.domaineMontagne').value = "true";		
		document.getElementById('dossierAgricoleComplexModel.allocataireAgricoleComplexModel.agricoleModel.idAllocataire').value = document.getElementsByName('dossierComplexModel.dossierModel.idAllocataire')[0].value;
		
		updateActiviteAllocLink(this.value);
	});
	
	if ($('#TypeDomaineAgriculteur').length>0) {
		document.getElementById('TypeDomaineAgriculteur').onchange = function(){
			document.getElementById('dossierComplexModel.domaineMontagne').value = this.value;
			document.getElementById('dossierAgricoleComplexModel.allocataireAgricoleComplexModel.agricoleModel.idAllocataire').value = document.getElementsByName('dossierComplexModel.dossierModel.idAllocataire')[0].value;
			updateActiviteAllocLink(document.getElementById('dossierComplexModel.dossierModel.activiteAllocataire').value);
		};
	}

	//Si le param typeActivite est défini, on set le champ en fonction
	var params = extractUrlParams();
	if(params["typeActivite"] != undefined && params["typeActivite"]!=""){
		document.getElementById('dossierComplexModel.dossierModel.activiteAllocataire').value=params["typeActivite"];
		$("#dossierComplexModel.dossierModel.activiteAllocataire").val(params["typeActivite"]);
		updateActiviteAllocLink(document.getElementById('dossierComplexModel.dossierModel.activiteAllocataire').value);
	}
	
	if(params["domaineMontagne"] != undefined && params["domaineMontagne"]!=""){
		document.getElementById('dossierComplexModel.domaineMontagne').value = params["domaineMontagne"];
		document.getElementById('domaineMontagne').value = params["domaineMontagne"];
		$("#TypeDomaineAgriculteur").val(params["domaineMontagne"]);
		updateActiviteAllocLink(document.getElementById('dossierComplexModel.dossierModel.activiteAllocataire').value);
	}


	//Gestion de l'affichage des onglets, si param ongletDisplay est défini on affiche l'onglet spécifié
	//sinon on affiche l'onglet droit
	mlog('param ongletDisplay='+params["ongletDisplay"]);
	if(params["ongletDisplay"] != undefined && params["ongletDisplay"]!="" && params["ongletDisplay"]=="prestations")
		switchOnglets("prestations");
	else
		switchOnglets("droits");
	
	var activite = document.getElementById("dossierComplexModel.dossierModel.activiteAllocataire").value;
	if (activite=="<%=ALCSDossier.ACTIVITE_AGRICULTEUR%>" || activite=="<%=ALCSDossier.ACTIVITE_COLLAB_AGRICOLE%>" || activite=="<%=ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE%>") {
		$("#TypeDomaineAgriculteur").css("visibility","visible");
		$("#TypeDomaineAgriculteur").css("width","80px");
	}
	
	if(activite == CS_SALARIE){
		$("#complementActiviteAllocataire").css("visibility","visible");
		$("#complementActiviteAllocataire").css("width","80px");
	}
	
	
}

function postInit(){

	//champ date calcul tjrs actif, fait dans postInit car c'est après le init qu'on disabled selon le mode
	if(document.getElementById('dateCalcul')!= null)
		document.getElementById('dateCalcul').disabled = false;
	if(document.getElementById('anchor_dateCalcul')!= null)
		document.getElementById('anchor_dateCalcul').disabled = false;
	if(document.getElementById('btnCalcul')!= null)
		document.getElementById('btnCalcul').disabled = false;
	if(document.getElementById('partialforNumAvs')!=null)
		document.getElementById('partialforNumAvs').focus();
}

//Gère l'affichage des onglets du tableau droits / prestations
function switchOnglets(ongletActif){
	mlog('switchOnglets('+ongletActif+')');
	if(ongletActif == "droits"){
		document.getElementById('tab2').className='inactive';
		document.getElementById('tab1').className='selected';

		document.getElementById('droits').style.display='block';
		document.getElementById('calculZone').style.display='block';
		document.getElementById('prestations').style.display='none';
	}

	if(ongletActif == "prestations"){
		document.getElementById('tab1').className='inactive';
		document.getElementById('tab2').className='selected';

		document.getElementById('prestations').style.display='block';
		document.getElementById('calculZone').style.display='none';
		document.getElementById('droits').style.display='none';
	}

}

//met à jour le paramètre typeActivite du lien Détail allocataire
function updateActiviteAllocLink(activite){
	var params = null;
	var allocDetailLink = document.getElementById('a-allocataire.afficher').href;
	
	if(document.getElementById('a-allocataire.afficher')!= null){

		params = extractStringUrlParams(allocDetailLink);
		
		if(allocDetailLink.indexOf("typeActivite=")!=-1)
			allocDetailLink = allocDetailLink.replace("typeActivite="+params["typeActivite"],"typeActivite="+activite);
		else		
			allocDetailLink = allocDetailLink+"&typeActivite="+activite;
		
		if ($("#TypeDomaineAgriculteur").css('visibility') == 'visible') {			
			if(allocDetailLink.indexOf("domaineMontagne=")!=-1)
				allocDetailLink = allocDetailLink.replace("domaineMontagne="+params["domaineMontagne"],"domaineMontagne="+document.getElementById('domaineMontagne').value);
			else		
				allocDetailLink = allocDetailLink+"&domaineMontagne="+document.getElementById('domaineMontagne').value;
			
		} else {
			allocDetailLink = allocDetailLink.replace("&domaineMontagne="+params["domaineMontagne"],"");
		}
		
		document.getElementById('a-allocataire.afficher').href = allocDetailLink;

	}

}

//Méthode appelé après la recup des infos tiers (fillForm)
function callbackFillInputAjax(){
	mlog('**callbackFillInputAjax**');
	var paysSuisse = '<%=JavascriptEncoder.getInstance().encode(ALCSPays.PAYS_SUISSE)%>';
	var paysDesignation =  '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("AL0004_ALLOC_PAYS"))%>';
	var cantonDesignation = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("AL0004_ALLOC_CANTON"))%>';
	
	var allocDetailLink = document.getElementById('a-allocataire.afficher').href;
	var idAllocataire = document.getElementsByName('dossierComplexModel.dossierModel.idAllocataire')[0].value;
	
	//gestion en cas de paiement direct
	var modeDossier = $('#modePaiementDossier').val();
	var idTiersAllocataire = document.getElementsByName('dossierComplexModel.allocataireComplexModel.allocataireModel.idTiersAllocataire')[0].value;

	//on fait qqqch que si callback de la recherche sur allocataire
	
	if(response.search.typeSearch.indexOf('AllocataireComplexModel')!=-1){
		mlog('callbackFillInputAjax2 - AllocataireComplexModel idAllocataire: '+idAllocataire);
		var selectedIdReg = new RegExp("selectedId=\\d+", "g");
		//si allocataire existant, on modifie le lien détail
		if(idAllocataire!=''){
			//si en paiement direct, on ajoute directement l'allocataire comme bénéficiaire
			if(modeDossier==CS_PAIEMENT_DIRECT){
				$('#idTiersBeneficiaire').val(idTiersAllocataire);
			}	
				
			if(document.getElementById('a-allocataire.afficher')!= null){
				//on génére le lien détail allocataire en remplacant contexte nouveau, par détail alloc lié par NSS
				allocDetailLink = allocDetailLink.replace("_method=add","selectedId="+idAllocataire);
		
				//dans le cas où on avait déja regénéré le lien détail avec un autre idAllocataire
				allocDetailLink = allocDetailLink.replace(selectedIdReg,"selectedId="+idAllocataire);
	
				document.getElementById('a-allocataire.afficher').href = allocDetailLink;
			}	
			document.getElementById("labelAllocataireResidence").innerHTML="";
			if(document.getElementById(prefixModel+"allocataireModel.idPaysResidence")==paysSuisse)		
				document.getElementById("labelAllocataireResidence").innerHTML=cantonDesignation;		
			else
				document.getElementById("labelAllocataireResidence").innerHTML=paysDesignation;
			
			document.getElementById("dossierComplexModel.domaineMontagne").value = document.getElementById("TypeDomaineAgriculteur").value;
			document.getElementById('dossierAgricoleComplexModel.allocataireAgricoleComplexModel.agricoleModel.idAllocataire').value = document.getElementsByName('dossierComplexModel.dossierModel.idAllocataire')[0].value;
		
		}
		//sinon on redirige sur le détail alloc en passant le nss en param
		else{

			allocDetailLink = allocDetailLink.replace(selectedIdReg,"_method=add");
			allocDetailLink +="&forNumAvsActuel="+document.getElementById('partialforNumAvs').value;	
			document.getElementById('a-allocataire.afficher').href = allocDetailLink;
			
			if(document.getElementById('partialforNumAvs').value.length == 12 && response.search.results.length == 0)
				window.location.replace(allocDetailLink);
		}
	}
}

function callbackWidgetNumAffilie(){
	alert('callbackWidgetNumAffilie 1');
	
}

$(document).ready(function(){
	var jsonWarn = '<%=viewBean.getAddWarnings()%>';	
	if(jsonWarn.length > 0) {
	
		var initializationPopup = jQuery.parseJSON(jsonWarn);
		var servletContxt = '<%=servletContext%>';
		//ajout du servletContext devant l'url configuré
		initializationPopup.options.url = servletContxt.concat(initializationPopup.options.url);
		$('#popup-container').data('al-popup',initializationPopup);
		$('#popup-container').trigger('newWarnMsg');
		
	}

	try {
		$("#droits").tablesorter( {
			sortInitialOrder: "desc",
			sortReset   : true,
			headers: {
				0: {
					sorter: false
				}, 
				1: {
					sorter: false
				}, 
				2: {
					sorter: false
				}, 
				3: {
					sorter: true
				}, 
				4: {
					sorter: false
				}, 
				5: {
					sorter: false
				}, 
				6: {
					sorter: false
				}, 
				7: {
					sorter: false
				},
				8: {
					sorter: false
				},
				9: {
					sorter: false
				},
				10: {
					sorter: false
				}, 
				11: {
					sorter: false
				},
				12: {
					sorter: false
				}
			}
		});
	} catch (e) {
		if (typeof console !== 'undefined') {
			console.log(e);
		}
	}
});

var MAIN_URL = "<%=(servletContext + mainServletPath)%>";
</script>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<%@ include file="/theme/detail/bodyStart.jspf" %>

<%
	if(!viewBean.getDossierComplexModel().isNew()){
%>

<div id="dialog-dossiersLies" title="Dossiers liés">
<table>
	<tr>
		<th></th>
		<th><ct:FWLabel key="AL0004_DOSSIER_LIE_NUM_DOSSIER"/></th>
		<th><ct:FWLabel key="AL0004_DOSSIER_LIE_NOM_ALLOC"/></th>
		<th><ct:FWLabel key="AL0004_DOSSIER_LIE_NSS_ALLOC"/></th>
		<th><ct:FWLabel key="AL0004_DOSSIER_LIE_AFFILIE"/></th>
		<th><ct:FWLabel key="AL0004_DOSSIER_LIE_ALLOC_ACTIVITE"/></th>
		<th><ct:FWLabel key="AL0004_DOSSIER_LIE_STATUT_DOSSIER"/></th>
		<th><ct:FWLabel key="AL0004_DOSSIER_LIE_ETAT_DOSSIER"/></th>
		<th><ct:FWLabel key="AL0004_DOSSIER_LIE_DROITS_DATES"/></th>
		<th><ct:FWLabel key="AL0004_DOSSIER_LIE_TYPE_LIEN"/></th>
	</tr>
	<!-- dynamic content js here -->
	
	<tr>
		<td></td>		
		<td>
			<input type="hidden" name="new_dossierFils" id="new_dossierFilsId" value="" size="6"/>
			<input class="small" data-g-autocomplete="    service:ch.globaz.al.business.services.models.dossier.DossierComplexModelService,
                                method:searchWithCsTranslated,
                                nbOfCharBeforeLaunch:1,
                                criterias:¦{
                                    forIdDossier: 'No dossier',
                                    likeNumeroAffilie:'N° affilié'         
                                }¦,
                                modelReturnVariables:¦dossierModel.idDossier,allocataireComplexModel.personneEtendueComplexModel.tiers.designation1,allocataireComplexModel.personneEtendueComplexModel.tiers.designation2,allocataireComplexModel.personneEtendueComplexModel.personneEtendue.numAvsActuel,dossierModel.numeroAffilie,dossierModel.activiteAllocataire,dossierModel.statut,dossierModel.etatDossier¦,
                                lineFormatter:¦#{dossierModel.idDossier} - #{allocataireComplexModel.personneEtendueComplexModel.tiers.designation1} #{allocataireComplexModel.personneEtendueComplexModel.tiers.designation2} - #{allocataireComplexModel.personneEtendueComplexModel.personneEtendue.numAvsActuel} -  #{dossierModel.numeroAffilie}¦,
                                functionReturn:¦
                                    function(element){

                                    	this.value='';
                                    	$('#new_dossierFilsId').val('');
                                   	
                                    	var obj = {
                                    	id_lien:'new',
                                    	dossier_id:$(element).attr('dossierModel.idDossier'),
                                    	allocataire_nom:$(element).attr('allocataireComplexModel.personneEtendueComplexModel.tiers.designation1'),
                                    	allocataire_prenom:$(element).attr('allocataireComplexModel.personneEtendueComplexModel.tiers.designation2'),
                                    	allocataire_nss:$(element).attr('allocataireComplexModel.personneEtendueComplexModel.personneEtendue.numAvsActuel'),
										affilie_numero:$(element).attr('dossierModel.numeroAffilie'),
										dossier_activite:$(element).attr('dossierModel.activiteAllocataire'),
										dossier_etat:$(element).attr('dossierModel.etatDossier'),
										dossier_statut:$(element).attr('dossierModel.statut'),
										dossier_radiation:'',
										droit_debut:'',
										csTypeLien:{value:$('#new_typeLienId').val(),libelle:$('#new_typeLienId option:selected').text(),collection:[{value:'61120001',libelle:'Allocataire'},{value:'61120002',libelle:'Conjoint'}]}	                  	
                                    	};
                       
                                    	renderRowForLienTable(obj,'bottom','add');

                                    }
                                      ¦" type="text">
		</td>
		<td colspan="7"><strong><ct:FWLabel key="AL0004_DOSSIER_LIE_NEW"/></strong></td>
		<td>
			<select name="new_typeLien" id="new_typeLienId" size="1">
			<%
				for (Entry<String, String> entry : viewBean.getAllCsTypeLien().entrySet()){
			%>
				<option value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
			<%
				}
			%>
			</select>
		</td>
	</tr>
</table>
</div>
<div id="dialog-copieDroits" title="Copie de droits">
	<table id="droitRef">
		<thead>
			<tr>
				<th colspan="2"></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>Début droit</td>
				<td><input type="text" name="debutDroitCopie" id="debutDroitCopieId" value="" data-g-calendar=" "/></td>
			</tr>
			<tr>
				<td>Fin droit</td>
				<td><input type="text" name="finDroitCopie" id="finDroitCopieId" value=""  data-g-calendar=" "/></td>
			</tr>
		</tbody>
	</table>
	<div id="ajaxContent">
	</div>
	
</div>
<%
	}
%>
		
<%-- tpl:insert attribute="zoneTitle" --%>
<%=(viewBean.getDossierComplexModel().isNew())?objSession.getLabel("AL0004_TITRE_NEW"):objSession.getLabel("AL0004_TITRE")+viewBean.getDossierComplexModel().getId()%>
<%
	if(hasUpdateRight) {
%>
		<a href="#" id="idLinkDossiersLies" class="<%=viewBean.getNbDossiersLies()>0?"dossiersLink":"nodossiersLink"%> "/>
<%
	}
%>
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
			<tr><td>
			<%-- tpl:insert attribute="zoneMain" --%>
			<div id="popup-container" data-al-popup=''></div>
              <table id="allocZone" class="zone">
	              <tr class="zoneHeader">
	                	<%-- zone ids liés au dossier --%> 
	                	<ct:inputHidden name="dossierComplexModel.dossierModel.idAllocataire"/>
	            		<!--  Champs nécessaire pour écriture dans les tiers (création / update) -->
		                <ct:inputHidden name="dossierComplexModel.allocataireComplexModel.personneEtendueComplexModel.tiers.idTiers"/>
		                <ct:inputHidden name="dossierComplexModel.allocataireComplexModel.personneEtendueComplexModel.personne.idTiers"/>
		                <ct:inputHidden name="dossierComplexModel.allocataireComplexModel.personneEtendueComplexModel.personneEtendue.idTiers"/>
		                <ct:inputHidden name="dossierComplexModel.allocataireComplexModel.personneEtendueComplexModel.personneEtendue.spy"/>
		   
		                <ct:inputHidden name="dossierComplexModel.allocataireComplexModel.personneEtendueComplexModel.personne.spy"/>
		             
		                <ct:inputHidden name="dossierComplexModel.allocataireComplexModel.personneEtendueComplexModel.tiers.spy"/>
		       
 						<!--  Champs nécessaire pour écriture allocataire (création / update) -->
		                <ct:inputHidden name="dossierComplexModel.allocataireComplexModel.allocataireModel.idAllocataire"/>
		                <ct:inputHidden name="dossierComplexModel.allocataireComplexModel.allocataireModel.idTiersAllocataire"/>
		                <ct:inputHidden name="dossierComplexModel.allocataireComplexModel.allocataireModel.spy"/>
		                
		                <input type="hidden" id="domaineMontagne"/>
		                <input type="hidden" name="dossierAgricoleComplexModel.allocataireAgricoleComplexModel.agricoleModel.idAllocataire" />
		                <input type="hidden" name="dossierAgricoleComplexModel.allocataireAgricoleComplexModel.agricoleModel.domaineMontagne" id="dossierComplexModel.domaineMontagne"/>
		              
		                <ct:inputHidden name="dossierComplexModel.allocataireComplexModel.personneEtendueComplexModel.personne.new"/>
		                <ct:inputHidden name="dossierComplexModel.allocataireComplexModel.personneEtendueComplexModel.personneEtendue.new"/>
		                <ct:inputHidden name="dossierComplexModel.allocataireComplexModel.personneEtendueComplexModel.tiers.new"/>
	            		<ct:inputHidden name="dossierComplexModel.allocataireComplexModel.allocataireModel.new"/>
	            		<ct:inputHidden name="dossierComplexModel.dossierModel.idTiersBeneficiaire" id="idTiersBeneficiaire"/>
	            		<ct:inputHidden name="dossierComplexModel.dossierModel.activiteAllocataire" id="activiteAllocataire"/>
	                	<%-- /zone --%>
	                	<td class="subtitle">
	                		<ct:FWLabel key="AL0004_TITRE_ALLOC"/>
	                		
	                		<%
	                		if(viewBean.getDossierComplexModel().isNew()){
	                			                		%>
	                			<span><a class="syncLink" href="#" onclick="synchroAllocAjax();" title="<%=objSession.getLabel("LINK_SYNC_TIERS_DESC")%>"/></span>
                		
	                		<%
                			}
                			%>
	                		<%=viewBean.isCompteAnnexeAllError()?"<a href='#' class='warningLink info_bulle'><span>"+objSession.getLabel("RETRIEVE_CA_ERROR_MSG")+"</span></a>":""%>
	                	</td>
	                	
	                	<td class="relLinks" colspan="3">	
	                		<%
		                			if(!JadeStringUtil.isBlankOrZero(viewBean.getIdCompteAnnexeAllocataire())){
		                		%>
		                	<a title='<ct:FWLabel key="LINK_ALLOC_COMPTA_DESC"/>'  href="<%=servletContext + 
		                			"/osiris?userAction=osiris.comptes.apercuParSection.chercher&selectedId="+viewBean.getIdCompteAnnexeAllocataire()%>">
		                			<ct:FWLabel key="LINK_ALLOC_COMPTA"/>
		                	</a> |
		                  	
		                	<%
		                		} 
		                		// REVENUS LIEN
		                		//On peut saisir des revenus seulement si le dossier est créé (facilite la gestion des liens retour)	
		                		if(!viewBean.getDossierComplexModel().isNew()){ 
		                			boolean linkDisplay = false;
		                	%>
			                	<ct:ifhasright element="al.allocataire.revenus.afficher" crud="crud">
			                		<%
			                			linkDisplay = true;
			                		%>
			                		<a title='<ct:FWLabel key="LINK_REVENUS"/>' href="<%=servletContext + mainServletPath + 
		                			"?userAction=al.allocataire.revenus.afficher&_method=add&idAllocataire="+ 
		                					viewBean.getDossierComplexModel().getAllocataireComplexModel().getAllocataireModel().getIdAllocataire()+ 
		                					"&idDossier="+viewBean.getId()%>"><ct:FWLabel key="LINK_REVENUS"/></a> |
		                		</ct:ifhasright>
		                		<%
		                			if(!linkDisplay){
		                		%>
		                		<ct:ifhasright element="al.allocataire.revenus.afficher" crud="r">
			                		<a title='<ct:FWLabel key="LINK_REVENUS"/>' href="<%=servletContext + mainServletPath + 
		                			"?userAction=al.allocataire.revenus.afficher&idAllocataire="+ 
		                					viewBean.getDossierComplexModel().getAllocataireComplexModel().getAllocataireModel().getIdAllocataire()+ 
		                					"&idDossier="+viewBean.getId()%>"><ct:FWLabel key="LINK_REVENUS"/></a> |
		                		</ct:ifhasright>
		                		<%
		                			}
		                		} 
		                				                			                
		                		// ALLOCATAIRE LIEN
		                		String linkAlloc = servletContext + mainServletPath + "?userAction=al.allocataire.allocataire.afficher&typeActivite="+viewBean.getDossierComplexModel().getDossierModel().getActiviteAllocataire();
		                				                			                	
		                		if(JadeStringUtil.isEmpty(viewBean.getDossierComplexModel().getDossierModel().getIdAllocataire()))
		                			linkAlloc +="&_method=add";
		                		else
		                			linkAlloc +="&selectedId="+viewBean.getDossierComplexModel().getDossierModel().getIdAllocataire();
		                		if(viewBean.getDossierComplexModel().isNew())
		                			linkAlloc +="&dossierIsNew=true";
		                		else
		                			linkAlloc +="&idDossier="+viewBean.getDossierComplexModel().getId();
		                		%>
	                		<a title='<ct:FWLabel key="LINK_GED_DESC"/>' href="<%=servletContext + mainServletPath + "?userAction=al.ged.lecture.allocataire&noAVSId=" + viewBean.getDossierComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel()%>&amp;serviceNameId=<%=viewBean.getSession().getApplication().getProperty(ALConstGed.AL_PROPERTY_AFFICHAGE_DOSSIER_GED_ALLOC)%> "target="GED_CONSULT">
		                			<ct:FWLabel key="LINK_GED"/>
		                	</a> |  	
		                	<a id="a-allocataire.afficher" title='<ct:FWLabel key="LINK_DETAIL"/>'
		                		href="<%=linkAlloc%>"><ct:FWLabel key="LINK_DETAIL"/>
		                	</a>
	                	</td>
	              </tr>
	              <tr>
	                	<td><ct:FWLabel key="AL0004_ALLOC_DESC"/></td>
	                	<td>
		                	<%--ce champ n'est pas utilisé pour la persistence --%>
		                	<input type="text" readonly="readonly" class="long readOnly" name="allocataireDesignation"
		                		value="<%=viewBean.getDossierComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1()%>&nbsp;<%=viewBean.getDossierComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2()%> "
		                		disabled="disabled"
		                	/>
	                	</td>
	              </tr>
	              <tr>
	              		<td>&nbsp;</td>
	                	<td>
	                		<%--ce champ n'est pas utilisé pour la persistence --%>
	                		<%
	                			String allocData = "";
	                			if(!JadeStringUtil.isBlankOrZero(viewBean.getDossierComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel().getPersonne().getDateNaissance()))
	                			    allocData+=viewBean.getDossierComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel().getPersonne().getDateNaissance();
	                			if(!JadeStringUtil.isBlankOrZero(viewBean.getDossierComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel().getPersonne().getSexe()))
	                			    allocData+=" - "+objSession.getCode(viewBean.getDossierComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel().getPersonne().getSexe());
	                			if(!JadeStringUtil.isBlankOrZero(viewBean.getDossierComplexModel().getAllocataireComplexModel().getPaysModel().getCodeIso()))
	                			    allocData+=" - "+viewBean.getDossierComplexModel().getAllocataireComplexModel().getPaysModel().getCodeIso();
	                			if(!JadeStringUtil.isBlankOrZero(viewBean.getDossierComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel().getPersonne().getEtatCivil()))
	                			    allocData+=" - "+objSession.getCodeLibelle(viewBean.getDossierComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel().getPersonne().getEtatCivil());
	                		%>
	                		<input readonly="readonly" class="long readOnly" type="text" name="allocataireData"
	                			value='<%=allocData%>'
	                			disabled="disabled"
	                		/>          		
	                	</td>
	              </tr>
	              <tr>
	                	
	                	<td><ct:FWLabel key="AL0004_ALLOC_ACTIVITE"/></td>
	                	<td class="longSelect" id="tdActivite">
	                	<%
	                		if (viewBean.hasAnnonceRafam()){
	                	%>
	                		<input name="dossierComplexModel.dossierModel.activiteAllocataire" type="text" class="readOnly" disabled="disabled" readonly="readonly" value="<%=objSession.getCodeLibelle(viewBean.getDossierComplexModel().getDossierModel().getActiviteAllocataire())%>" />
	                	<%
	                		}else{
	                	%> 
	                		<ct:FWCodeSelectTag name="dossierComplexModel.dossierModel.activiteAllocataire" tabindex="2"
			            	defaut="<%=viewBean.getDossierComplexModel().getDossierModel().getActiviteAllocataire()%>"
							codeType="ALDOSACTAL" wantBlank="false" />	
	                    <%
		                 }

	                    if (viewBean.hasAnnonceRafam()){
	                    	     String type = objSession.getLabel("AL0006_ALLOC_DOMAINE_MONTAGNE");
	                    	     if (!viewBean.getDossierAgricoleComplexModel().getAllocataireAgricoleComplexModel().getAgricoleModel().isNew()) {
	                    	         boolean typeDomaine = viewBean.getDossierAgricoleComplexModel().getAllocataireAgricoleComplexModel().getAgricoleModel().getDomaineMontagne();
	                    	                    	                    	
	                    	        if (!typeDomaine) {	                    			
	                    	           type = objSession.getLabel("AL0006_ALLOC_DOMAINE_PLAINE");
	                    	       	}
	                    %>
	                    		<input name="dossierAgricoleComplexModel.allocataireAgricoleComplexModel.agricoleModel.domaineMontagne" type="text" class="readOnly" disabled="disabled" readonly="readonly" size="10" value="<%=type%>" />
	                     <%
	                     	}
	                     } else {
	                     %>
	                    	 <select id="TypeDomaineAgriculteur" name="dossierAgricoleComplexModel.allocataireAgricoleComplexModel.agricoleModel.domaineMontagne" style="width:0px;visibility:hidden">">
		                    	<%
	                    	 	//Type montagne par défaut
	                    	 	String typeDomaine = "";
	                    	 	                  	 		                    
	                    	 	if (!viewBean.getDossierAgricoleComplexModel().getAllocataireAgricoleComplexModel().getAgricoleModel().isNew()) {
	                    	 	     typeDomaine = viewBean.getDossierAgricoleComplexModel().getAllocataireAgricoleComplexModel().getAgricoleModel().getDomaineMontagne().toString();
	                    	 	}
	                    	 %>
		                    		<option value="" label='' <%=JadeStringUtil.isEmpty(typeDomaine)?"selected=selected":""%>/>
		                    		<option value="true" label='<%=objSession.getLabel("AL0006_ALLOC_DOMAINE_MONTAGNE")%>' <%=typeDomaine.equals("true")?"selected=selected":""%> />	
			                    	<option value="false" label='<%=objSession.getLabel("AL0006_ALLOC_DOMAINE_PLAINE")%>' <%=typeDomaine.equals("false")?"selected=selected":""%> />
		                    	</select> 
	                     <%
 	                     	}
 	                     %>
           				
           				
           				<select id="complementActiviteAllocataire" name="dossierComplexModel.dossierModel.complementActiviteAllocataire" style="width:0px;visibility:hidden">				
           					<%
           					String complActiviteValue ="";
           					if(!viewBean.getDossierComplexModel().isNew()){
           						complActiviteValue = viewBean.getDossierComplexModel().getDossierModel().getComplementActiviteAllocataire();
           					}%>
           					
           					<option value="" <%=JadeStringUtil.isBlankOrZero(complActiviteValue)?"selected":"" %>></option>
							<% 
							for (Entry<String, String> entry : viewBean.getAllCsComplActivite().entrySet()){	
							%>
								<option value="<%=entry.getKey()%>" <%=complActiviteValue.equals(entry.getKey())?"selected":"" %>><%=entry.getValue()%></option>
							<%
							}
							%>
									
           				</select>	                 
	              </tr>
	              <tr>
	                	<td><ct:FWLabel key="AL0004_ALLOC_NSS"/></td>
	                	<td>
      		
	                	<%
      			                		if(viewBean.getDossierComplexModel().isNew()){
      			                	%>
                    		<nss:nssPopup avsMinNbrDigit="2" nssMinNbrDigit="2" name="forNumAvs" newnss="true" tabindex="1"/>
                    		<ct:inputHidden name="dossierComplexModel.allocataireComplexModel.personneEtendueComplexModel.personneEtendue.numAvsActuel"/>
                    	
                    	<%
                    	                    		}else{
                    	                    	%>
                    		<ct:inputText name="dossierComplexModel.allocataireComplexModel.personneEtendueComplexModel.personneEtendue.numAvsActuel" 
                    		styleClass="nss edit readOnly" readonly="readonly" />
                    	<%
                    		}
                    	%>		     		
	                		<div id="autoSuggestContainer" class="suggestList"></div>
	             
	                	</td>
	           
	              </tr>
	              <tr>
		              <td id="labelAllocataireResidence">
		              		<%
		              			if(ALCSPays.PAYS_SUISSE.equals(viewBean.getDossierComplexModel().getAllocataireComplexModel().getAllocataireModel().getIdPaysResidence())){
		              		%>
		              			<ct:FWLabel key="AL0004_ALLOC_CANTON"/>
		              		<%
		              			}else{
		              		%>
		              			<ct:FWLabel key="AL0004_ALLOC_PAYS"/>
		              		<%
		              			}
		              		%>
		              </td>
		              <td>
		              	
		                <input readonly="readonly" class="readOnly nss" type="text" name="allocataireResidence" disabled="disabled"
		                	value="<%=ALCSPays.PAYS_SUISSE.equals(viewBean.getDossierComplexModel().getAllocataireComplexModel().getAllocataireModel().getIdPaysResidence())
		                		?objSession.getCodeLibelle(viewBean.getDossierComplexModel().getAllocataireComplexModel().getAllocataireModel().getCantonResidence())
		                				:viewBean.getDossierComplexModel().getAllocataireComplexModel().getPaysResidenceModel().getLibelleFr()%>"
		                />
		                <%-- Utile pour savoir si on affiche le canton ou le pays de résidence --%>
		                <ct:inputHidden name="dossierComplexModel.allocataireComplexModel.allocataireModel.idPaysResidence"/>
		                <ct:inputHidden name="dossierComplexModel.allocataireComplexModel.allocataireModel.cantonResidence"/>
		              </td>
	              </tr>
	          
	           
              </table>
              <div id="postItZone">
                <ct:FWNote sourceId="<%=viewBean.getDossierComplexModel().getDossierModel().getId()%>" tableSource="globaz.al.vb.dossier.ALDossierViewBean"/>
                <%=viewBean.getDossierListHTMLWarnings()%>
              </div>
              <table id="affilieZone" class="zone">
	              <tr>
	                	<td class="subtitle" colspan="2">
	                		<ct:FWLabel key="AL0004_TITRE_AFFILIE"/>
	                		<%=viewBean.getAffilieWarnsError()%>
	                	</td>
	                	<td class="relLinks" colspan="2" >
	                		<a href="<%=servletContext + mainServletPath + "?userAction=al.prestation.recap.chercher&recapSearchModel.forNumeroAffilie="+viewBean.getDossierComplexModel().getDossierModel().getNumeroAffilie()%>" 
	                		   title="<ct:FWLabel key="LINK_AFFILIE_RECAP_DESC"/>">
	                		   	<ct:FWLabel key="LINK_AFFILIE_RECAP"/>
	                		</a> |
	                		
	                		<%
	                			if(!JadeStringUtil.isBlankOrZero(viewBean.getIdCompteAnnexeAffilie())){
	                		%>
		                	<a  
		              			href="<%="/webavs/osiris?userAction=osiris.comptes.apercuParSection.chercher&selectedId=" +viewBean.getIdCompteAnnexeAffilie()%>"
		                		title="<ct:FWLabel key="LINK_AFFILIE_COMPTA_DESC"/>">
		                			<ct:FWLabel key="LINK_AFFILIE_COMPTA"/> |<%
		                				}
		                			%>
		                			
		                	</a>   
		                			
		               
		                	<a title='<ct:FWLabel key="LINK_GED_DESC"/>' href="<%=servletContext + mainServletPath + "?userAction=al.ged.lecture.affilie&noAffiliationId=" + viewBean.getAffiliation().getNumeroAffilie()%>&amp;serviceNameId=<%=viewBean.getSession().getApplication().getProperty(ALConstGed.AL_PROPERTY_AFFICHAGE_DOSSIER_GED_AFFIL)%>" target="GED_CONSULT">
		                			<ct:FWLabel key="LINK_GED"/>
		                	</a> |  
	                		<a href="<%=servletContext + mainServletPath + "?userAction=al.attribut.affilie.afficher&selectedId="+viewBean.getAffiliation().getIdAffiliation()+"&idDossier="+viewBean.getId()%>" 
	                		   title="<ct:FWLabel key="LINK_AFFILIE_ATTRIBUT_DESC"/>">
	                		   	<ct:FWLabel key="LINK_AFFILIE_ATTRIBUT"/>
	                		</a> | 
	                		<a href="<%="/webavs/naos?userAction=naos.affiliation.autreDossier.modifier&numAffilie=" + viewBean.getAffiliation().getNumeroAffilie()%>"
	                		   title="<ct:FWLabel key="LINK_DETAIL_AFFILIE_DESC"/>">
	                		   		<ct:FWLabel key="LINK_DETAIL"/>
	                		</a>
	                	</td>
	              </tr>
	              <tr>
	                	<td><ct:FWLabel key="AL0004_AFFILIE_NUMCAISSE"/></td>
	                	<td>
	                		<ct:inputText name="affiliation.codeCaisseProf" styleClass="small readOnly" readonly="readonly" 
	                		disabled="disabled"/>
	                	</td>
	                	<td><ct:FWLabel key="AL0004_AFFILIE_TYPE"/></td>
	                	<td style="text-align:right;">
	                		<ct:inputText name="typeAffilie" styleClass="readOnly" readonly="readonly" 
	                			/>
	                	</td>
	              </tr>
	              <tr>
	                    <td><ct:FWLabel key="AL0004_AFFILIE_NUM"/></td>  	
	                	<td class="mediumSelect">
 
	                	<ct:inputHidden name="dossierComplexModel.dossierModel.numeroAffilie" id="numAffilieValue"/>
	                    <%
	                    	if (viewBean.isAffilieLocked()){
	                    %>
	           
								<input class="readOnly" disabled="disabled" readonly="readonly" type="text" value="<%=viewBean.getDossierComplexModel().getDossierModel().getNumeroAffilie()%>" />
	                    <%
	                    	}else{
	                    %>
  
						<ct:widget tabindex="3" name="forNumAffilieWidget" id="forNumAffilieWidget" styleClass="normal" >
						<ct:widgetService methodName="widgetFind" className="<%=AffiliationService.class.getName()%>" defaultSearchSize="20">
							<ct:widgetCriteria criteria="likeNumeroAffilie" label="CRITERIA_NUM_AFFILIE"/>
							<ct:widgetCriteria criteria="likeDesignationUpper" label="CRITERIA_NOM_AFFILIE"/>			
							<ct:widgetLineFormatter format="#{affiliation.affilieNumero} #{affiliation.raisonSocialeCourt} "/>
							<ct:widgetJSReturnFunction>
								<script type="text/javascript">
								function(element){
									this.value=$(element).attr('affiliation.affilieNumero');
									$('#numAffilieValue').val($(element).attr('affiliation.affilieNumero'));
									synchroAffilieAjax($(element).attr('affiliation.affilieNumero'),
											document.getElementById('dossierComplexModel.dossierModel.activiteAllocataire').value,
											document.getElementById('dateCalcul').value);
								}
								</script>										
							</ct:widgetJSReturnFunction>
						</ct:widgetService>			
						</ct:widget>
						<%
						}
						%>
	                	</td>
     	
	                	<td style="text-align:right;">  	
		                <%
  			            if(viewBean.getListAffiliation().size()>0){
  			              %>
		                	<a href='#' class='warningLink info_bulle' title='<ct:FWLabel key="LINK_AFFIL_MORE_INFOS"/>'>
		                	<span style="text-align:left;"><%
		                	for(int i=0;i<viewBean.getListAffiliation().size();i++){
		                	%>
		                		<%=viewBean.getListAffiliation().get(i)%><br />
		                		<%
		                	}
		                }
		                %>
						</span></a>
          	
		                </td>		
	                	<td style="text-align:right;">
	                		<input value='<%=JadeStringUtil.isEmpty(viewBean.getAffiliation().getDateDebutAffiliation())?"":viewBean.getAffiliation().getDateDebutAffiliation()+"-"+viewBean.getAffiliation().getDateFinAffiliation()%>' 
	                			class="readOnly" readonly="readonly" type="text" 
	                			name="affiliation.periode" id="affiliationPeriode" disabled="disabled"/>
	                	</td>
	              </tr>
	              <tr>
	                	<td><ct:FWLabel key="AL0004_AFFILIE_NOM"/></td>
	                	<td><ct:inputText name="affiliation.designation" readonly="readonly" styleClass="readOnly" disabled="disabled"/></td>
	                	
	                	<td><ct:FWLabel key="AL0004_AFFILIE_PERIODE"/></td>
	                	<td style="text-align:right;">
	                		<input 
	                		name="affiliation.periodiciteCotisation" readonly="readonly" class="readOnly" type="text" 
	                			value="<%=objSession.getCodeLibelle(viewBean.getAffiliation().getPeriodicitieAffiliation())%>"
	                			disabled="disabled"/>
	                	</td>      
	              </tr>
	              <tr>
	                	<td><ct:FWLabel key="AL0004_AFFILIE_CANTON"/></td>
	                	<td>
	                		<input name="affiliation.canton" class="readOnly" type="text" disabled="disabled"
	                			value="<%=objSession.getCodeLibelle(viewBean.getAffiliation().getCanton())%>" readonly="readonly"
	                		/>
	                	</td>
	                	<td><ct:FWLabel key="AL0004_AFFILIE_ETAT"/></td>
	                	<td style="text-align:right;">
	                		<input name="affiliation.couvert" class="readOnly" readonly="readonly" type="text" disabled="disabled"
	                			value='<%=JadeStringUtil.isEmpty(viewBean.getAffiliation().getIdAffiliation())?"":(viewBean.getAffiliation().getCouvert().booleanValue()?objSession.getLabel("AL0004_AFFILIE_ETAT_ACTIF"):objSession.getLabel("AL0004_AFFILIE_ETAT_INACTIF"))%>'
	                		/>
	                	</td>
	              </tr>
	              <tr>
	                	<td><ct:FWLabel key="AL0004_DEBUT_ACTIVITE"/></td>
	                	<td>               		
	                		<input id="debutActivite" name="dossierComplexModel.dossierModel.debutActivite" tabindex="4" 
							  value="<%=viewBean.getDossierComplexModel().getDossierModel().getDebutActivite()%>" type="text" data-g-calendar=" ">
	                	</td>
	                	<td><ct:FWLabel key="AL0004_FIN_ACTIVITE"/></td>
	                	<td style="text-align:right;">
	                		<input id="finActivite" name="dossierComplexModel.dossierModel.finActivite" tabindex="5" 
							  value="<%=viewBean.getDossierComplexModel().getDossierModel().getFinActivite()%>" type="text" data-g-calendar=" ">
	                	</td>
	              </tr>
              </table>
              <table id="validiteZone" class="zone">
	              <tr>
	                	<td class="subtitle" colspan="4">
	                		<ct:FWLabel key="AL0004_TITRE_VALIDITE"/>
	                		
	                	</td>
	                	<td class="relLinks" colspan="4">
	                		<%
	                			if(!viewBean.getDossierComplexModel().isNew()){
	                		%>
		                		<a title='<ct:FWLabel key="LINK_DECISION_DESC"/>' href="<%=servletContext + mainServletPath + "?userAction=al.decision.decision.afficher&_method=upd&selectedId="+viewBean.getId()%>">
		                			<ct:FWLabel key="LINK_DECISION"/>
		                		</a> | 
		                		
		                		<a title='<ct:FWLabel key="LINK_DETAIL_DOSSIER_DESC"/>' href="<%=servletContext + mainServletPath + "?userAction=al.dossier.dossier.afficher&selectedId="+viewBean.getId()%>">
		                			<ct:FWLabel key="LINK_DETAIL"/>
		                		</a>
	                		<%
	                			}
	                		%>
	                	</td>
	              </tr>
	              <tr>
	                	<td><ct:FWLabel key="AL0004_DOSSIER_ETAT"/></td>
	                	<td class="mediumSelect">
	                	
	                	<ct:select name="dossierComplexModel.dossierModel.etatDossier" tabindex="6" wantBlank="false">
							<ct:optionsCodesSystems csFamille="ALDOSETAT">		
								<%
											if(!ALCSDossier.ETAT_RADIE.equals(viewBean.getDossierComplexModel().getDossierModel().getEtatDossier())){
										%>
									<ct:excludeCode code="<%=ALCSDossier.ETAT_RADIE%>"/>
								<%
									}
								%>
							</ct:optionsCodesSystems>
						</ct:select>	
	                	
                        </td>
	                	<td><ct:FWLabel key="AL0004_DOSSIER_DEBUT"/></td>
	                	<td>             
	                		<input id="debutValidite" name="dossierComplexModel.dossierModel.debutValidite" tabindex="8" 
							  value="<%=viewBean.getDossierComplexModel().getDossierModel().getDebutValidite()%>" type="text" data-g-calendar=" ">
	                	
	                		<ct:FWLabel key="AL0004_DOSSIER_JOURS"/>
	 
	                		<input id="nbJoursDebut" name="dossierComplexModel.dossierModel.nbJoursDebut" type="text" data-g-integer="sizeMax:2" size="2"
								value="<%=viewBean.getDossierComplexModel().isNew()?"":viewBean.getDossierComplexModel().getDossierModel().getNbJoursDebut()%>" tabindex="10"/>
	                
	                
	                	</td>
	                	<td><ct:FWLabel key="AL0004_DOSSIER_UNITE"/></td>
	                	
	                	<td class="mediumSelect">       
							<ct:select name="dossierComplexModel.dossierModel.uniteCalcul" tabindex="12" wantBlank="false">
								<ct:optionsCodesSystems csFamille="ALDOSUNITE">
									<ct:excludeCode code="<%=ALCSDossier.UNITE_CALCUL_SPECIAL%>"/>
								</ct:optionsCodesSystems>
							</ct:select>			
						</td>      
	           
	                	<td>
	                		<ct:FWLabel key="AL0004_DOSSIER_CAF_AUTREPARENT"/>
	                	</td>
	                	<td>

	                	   <ct:inputHidden name="dossierComplexModel.dossierModel.idTiersCaisseConjoint"/>
	                       <%
	                       	String caisseDescription = "" ;
	                       	if(!JadeStringUtil.isEmpty(viewBean.getDossierComplexModel().getCaisseAFComplexModel().getTiers().getDesignation1()))
	                       	     caisseDescription = viewBean.getDossierComplexModel().getCaisseAFComplexModel().getTiers().getDesignation1() ;
	                       	if(!JadeStringUtil.isEmpty(viewBean.getDossierComplexModel().getCaisseAFComplexModel().getTiers().getDesignation2()))
	                       	     caisseDescription = caisseDescription.concat(viewBean.getDossierComplexModel().getCaisseAFComplexModel().getTiers().getDesignation2());
	                       	if(!JadeStringUtil.isEmpty(viewBean.getDossierComplexModel().getCaisseAFComplexModel().getTiers().getDesignation3()))
	                       	     caisseDescription = caisseDescription.concat(viewBean.getDossierComplexModel().getCaisseAFComplexModel().getTiers().getDesignation3());
	                       	if(!JadeStringUtil.isEmpty(viewBean.getDossierComplexModel().getCaisseAFComplexModel().getTiers().getDesignation4()))
	                       	     caisseDescription = caisseDescription.concat(viewBean.getDossierComplexModel().getCaisseAFComplexModel().getTiers().getDesignation4());
	                       %>
	                       <ct:inputText name="dossierComplexModel.caisseAFComplexModel.admin.codeAdministration" styleClass="small" tabindex="14" title="<%=caisseDescription%>"/>
	                      	 
	                      	<%
	                      	 Object[] caisseMethodsName = new Object[]{
	                      	 	new String[]{"dossierComplexModel.dossierModel.idTiersCaisseConjoint","getIdTiers"},
	                      	 	//pour afficher code dès retour sur le dossier ,sans avoir sauvegarder...
	                      	 	new String[]{"dossierComplexModel.caisseAFComplexModel.admin.codeAdministration","getCodeAdministration"}
	                      	};
	                      	 	                      	%>
                    		<ct:FWSelectorTag name="caisseSelector" 
								methods="<%=caisseMethodsName%>"
								providerApplication="pyxis" 
								providerPrefix="TI"
								providerAction="pyxis.tiers.administration.chercher"
							/>
	                		 <div id="autoSuggestCodeCaisseContainer" class="suggestList"></div>     		
	                	</td>          	
	               </tr>
	               <tr>
	                	<td><ct:FWLabel key="AL0004_DOSSIER_STATUT"/></td>
	                	<td class="mediumSelect">
	                		<%=viewBean.renderHTMLSelectStatut()%>
	                	</td>
	                	<td><ct:FWLabel key="AL0004_DOSSIER_FIN"/></td>
	                	<td>
	                	
	                		<input id="finValidite" name="dossierComplexModel.dossierModel.finValidite" tabindex="9" 
							  value="<%=viewBean.getDossierComplexModel().getDossierModel().getFinValidite()%>" type="text" data-g-calendar=" ">
	                	
	                		<ct:FWLabel key="AL0004_DOSSIER_JOURS"/>
	 
	                		<input id="nbJoursFin" name="dossierComplexModel.dossierModel.nbJoursFin" type="text" data-g-integer="sizeMax:2" size="2"
								value="<%=viewBean.getDossierComplexModel().isNew()?"":viewBean.getDossierComplexModel().getDossierModel().getNbJoursFin()%>" tabindex="11"/>
	                
	                	</td>
	                	<td><ct:FWLabel key="AL0004_DOSSIER_VERSEMENT"/></td>
	                	<td class="mediumSelect">
	                		<ct:inputHidden name="dossierComplexModel.dossierModel.idTiersBeneficiaire"/>
	                		
	                		<div data-g-commutator="master:$('#modePaiementDossier'),
			                        condition:($('#modePaiementDossier').val()==CS_PAIEMENT_TIERS),
			                        actionTrue:¦show('#selectionBeneficiaire')¦,
			                        actionFalse:¦hide('#selectionBeneficiaire')¦"/>
	                			
	                		<select tabindex="13" id="modePaiementDossier" name="modePaiementDossier">
	                			<option value="<%=ALCSDossier.PAIEMENT_INDIRECT%>" <%=viewBean.getPaiementMode()==null?"style='color:red'":""%><%=(viewBean.getPaiementMode()==null ||ALCSDossier.PAIEMENT_INDIRECT.equals(viewBean.getPaiementMode()))?"selected='selected'":""%>><%=objSession.getCodeLibelle(ALCSDossier.PAIEMENT_INDIRECT)%></option>
	                			<option value="<%=ALCSDossier.PAIEMENT_DIRECT%>" <%=ALCSDossier.PAIEMENT_DIRECT.equals(viewBean.getPaiementMode())?"selected='selected'":""%>><%=objSession.getCodeLibelle(ALCSDossier.PAIEMENT_DIRECT)%></option>
	                			<option value="<%=ALCSDossier.PAIEMENT_TIERS%>" <%=ALCSDossier.PAIEMENT_TIERS.equals(viewBean.getPaiementMode())?"selected='selected'":""%>><%=objSession.getCodeLibelle(ALCSDossier.PAIEMENT_TIERS)%></option>	
	                		</select>
	                		
						</td>	

	                	<td><ct:FWLabel key="AL0004_DOSSIER_LOI_AUTREPARENT"/></td>
	                	<td class="mediumSelect">
	                		 <ct:FWCodeSelectTag name="dossierComplexModel.dossierModel.loiConjoint"
			            	defaut="<%=viewBean.getDossierComplexModel().getDossierModel().getLoiConjoint()%>"
							codeType="ALTARCAT" wantBlank="true" tabindex="15"/>
	                	</td>
	                	     	
	              </tr>
	              <tr>
	              	<td colspan="4"></td>
	              	<td colspan="4">
	              	<fieldset id="selectionBeneficiaire">	
					<div data-g-multiwidgets="languages:LANGUAGES" style="float:left;">	

						<ct:widget id='widgetTiers' name='widgetTiers' 
					          defaultValue="<%=viewBean.getInfosBeneficiaire()%>"
					           styleClass="widgetTiers">
						<ct:widgetService methodName="find" className="<%=PersonneEtendueService.class.getName()%>">
							<ct:widgetCriteria criteria="forDesignation1Like" label="AL0004_BENEFICIAIRE_W_NOM"/>								
							<ct:widgetCriteria criteria="forDesignation2Like" label="AL0004_BENEFICIAIRE_W_PRENOM"/>
							<ct:widgetCriteria criteria="forNumeroAvsActuel" label="AL0004_BENEFICIAIRE_W_AVS"/>									
							<ct:widgetCriteria criteria="forDateNaissance" label="AL0004_BENEFICIAIRE_W_NAISS"/>																						
							<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
							<ct:widgetJSReturnFunction>
								<script type="text/javascript">
									function(element){
										$('#idTiersBeneficiaire').val($(element).attr('tiers.id'));	
										this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
									}
								</script>										
							</ct:widgetJSReturnFunction>
							</ct:widgetService>
						</ct:widget>
						<ct:widget id='widgetAdmin' name='widgetAdmin' 
					            styleClass="widgetAdmin"  
					            defaultValue="<%=viewBean.getInfosBeneficiaire()%>">
						<ct:widgetService methodName="find" className="<%=AdministrationService.class.getName()%>">										
							<ct:widgetCriteria criteria="forCodeAdministrationLike" label="AL0004_BENEFICIAIRE_W_CODE_ADMIN"/>																
								<ct:widgetCriteria criteria="forDesignation1Like" label="AL0004_BENEFICIAIRE_W_DESIGNATION_ADMIN"/>								
							<ct:widgetLineFormatter format="#{tiers.designation2} #{tiers.designation1}, #{tiers.idTiers} "/>
							<ct:widgetJSReturnFunction>
								<script type="text/javascript">
									function(element){
										$('#idTiersBeneficiaire').val($(element).attr('tiers.id'));
										this.value=$(element).attr('tiers.designation2')+' '+$(element).attr('tiers.designation1');
									}
								</script>										
							</ct:widgetJSReturnFunction>
						</ct:widgetService>
						</ct:widget>

					</div>
					<div id="idTiers"> (<a href="<%=servletContext+"/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId="+viewBean.getDossierComplexModel().getDossierModel().getIdTiersBeneficiaire()%>"><%=viewBean.getDossierComplexModel().getDossierModel().getIdTiersBeneficiaire()%></a>)</div> 
				</fieldset>
	            </td>
	           
	            </tr>
              </table>
              
              <div id="tabs">
	              <ul>
	                <li id="tab1" class="selected" onmouseover="if(this.className!='selected')this.className='active';" onmouseout="if(this.className!='selected'){this.className='inactive';}" 
	                	onclick="switchOnglets('droits');"><ct:FWLabel key="AL0004_DROITS_ONGLET"/></li>
	                <li id="tab2" class="inactive" onmouseover="if(this.className!='selected')this.className='active';" onmouseout="if(this.className!='selected')this.className='inactive';"
	                	onclick="switchOnglets('prestations');" ><ct:FWLabel key="AL0004_PRESTATIONS_ONGLET"/></li>    
	              </ul>
	             
              </div>
              <div id="calculZone">
              		
              		<%
              		              			//on affiche la projection du calcul que si il y a des droits
              		 if(objSession.hasRight(userActionUpd, FWSecureConstants.UPDATE)){
              		              		%>
              		<ct:FWCalendarTag name="dateCalcul" 
	                	value="<%=viewBean.getDateCalcul()%>"
						doClientValidation="CALENDAR"/>
					<input type="button" name="btnCalcul" id="btnCalcul" value="<%=objSession.getLabel("AL0004_DROITS_CALCULER")%>"/>
              		<%
              			}else {
              		%>
              			<ct:inputHidden name="dateCalcul"/>
              		<%
              			}
              		%>
              </div>
        
              <table class="al_list" id="droits">

                <%
                	String rowStyle = "";
                    String previousId = "0";
                    String montantCalcule = "0";
                    String detailLink = "window.location.href='al?userAction=al.droit.droit.afficher&selectedId=";
                    int lineDisplayCpt = 0;
                    boolean condition = false;
                                                
                    String modeAffichageDroits = viewBean.getDroitsViewMode();
                                                
                    if(Integer.parseInt(modeAffichageDroits)>0){
                %>
                <%@ include file="/alRoot/dossier/part_html_droits_list/dossierMain_droits_list.jspf" %>
                <%
                	}
                    if(Integer.parseInt(modeAffichageDroits)==0 ||Integer.parseInt(modeAffichageDroits)>1 ){
                %>
                 <%@ include file="/alRoot/dossier/part_html_droits_list/dossierMain_droits_list_v1.jspf" %>
                 <%
                 	}
                 %>         	
            </table>
                      
           	<table style="display:none;" class="al_list" id="prestations">
                 
                <tr>
	                  <th scope="col"></th>
	                  <th scope="col"><ct:FWLabel key="AL0004_PREST_ENTETE_PERIODE"/></th>
	                  <th scope="col" ><ct:FWLabel key="AL0004_PREST_ENTETE_UNITE"/></th>
	                  <th scope="col" ><ct:FWLabel key="AL0004_PREST_ENTETE_NBENF"/></th>
	                  <th scope="col" ><ct:FWLabel key="AL0004_PREST_ENTETE_TARIF"/></th>
	                  <th scope="col" ><ct:FWLabel key="AL0004_PREST_ENTETE_MONTANT"/></th>
	                  <th scope="col" ></th>
	                  <th scope="col" ><ct:FWLabel key="AL0004_PREST_ENTETE_DATECO"/></th>
	                  <th scope="col" ><ct:FWLabel key="AL0004_PREST_ENTETE_ETAT"/></th>
	                  <th scope="col" ><ct:FWLabel key="AL0004_PREST_ENTETE_TYPE"/></th>        
                </tr>
                <%
                	int cptPrestationDisplay = 0;
                                                int prestationPerLine = 15;
                                                for(int i=0;i<viewBean.getEntetesPrestations().size();i++){
                                                	rowStyle="show";
                                                	if(cptPrestationDisplay+1>prestationPerLine)
                                                		rowStyle="hide";
                                                    PrestationHolder.EnteteAndPrestationAdapter enteteAndDetailPrestation = (PrestationHolder.EnteteAndPrestationAdapter)viewBean.getEntetesPrestations().get(i);
                                                    EntetePrestationModel entetePrestation = enteteAndDetailPrestation.getEntetePrestation();
                                                    RecapitulatifEntrepriseModel recap = enteteAndDetailPrestation.getRecap();
                                                    String libelleNumFacture = "";
                                                    //recap peut être null (cas prestation ADI suisse)
                                                    if(recap!=null){
                                                        libelleNumFacture = " - ".concat(objSession.getLabel("AL0004_PREST_NUMFACTURE")).concat(recap.getNumeroFacture());
                                                    }
                                                	if(!(cptPrestationDisplay % 2 == 0))
                                                		rowStyle+=" odd";
                                                	else
                                                		rowStyle+=" nonodd";
                                                	
                                            		String detailPrestLink = "window.location.href='al?userAction=al.prestation.entetePrestation.afficher&selectedId=";
                                                	String actionDetailPrest = detailPrestLink.concat(entetePrestation.getId())+"'";
                %>	
                	 
                <tr class='<%=rowStyle%><%=ALCSPrestation.ETAT_TMP.equals(entetePrestation.getEtatPrestation())?" tmpPrest":ALCSPrestation.ETAT_CO.equals(entetePrestation.getEtatPrestation())?" coPrest":""%>'
                	id='<%="linePrestation"+i%>' onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'highlighted')" onMouseOut="jscss('swap', this, 'highlighted', '<%=rowStyle%>')">
       				<td>
       					<%
       						if(ALServiceLocator.getPrestationBusinessService().isEditable(entetePrestation.getEtatPrestation())){
       					%>
       					<ct:ifhasright element="al.prestation.entetePrestation.supprimerPrestation" crud="crud">
				        	<a title='<ct:FWLabel key="LINK_DEL_PRESTATION_DESC"/>' class="deleteLink" 
				               href="<%=servletContext + mainServletPath + "?userAction=al.prestation.entetePrestation.supprimerPrestation&id="+entetePrestation.getId()%>"
				               onclick="return confirm('<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>')"/>
						</ct:ifhasright>
						<%
							}
						%>
       				</td>
 	            	<td onclick="<%=actionDetailPrest%>"><%=entetePrestation.getPeriodeDe()+" - "+entetePrestation.getPeriodeA()%></td>
 	                <td onclick="<%=actionDetailPrest%>"><%=entetePrestation.getNombreUnite()+" / "+objSession.getCode(entetePrestation.getUnite())%></td>
 	                <td onclick="<%=actionDetailPrest%>"><%=entetePrestation.getNombreEnfants()%></td>
 	                <td onclick="<%=actionDetailPrest%>">
 	                <%
 	                	String categorieTarif = enteteAndDetailPrestation.getCategorieTarif();
 	                 	                 	                 	                 	                	if(TarifAggregator.TARIF_UNDEFINED.equals(categorieTarif)) {
 	                 	                 	                 	                           					out.print(objSession.getCode(categorieTarif));
 	                 	                 	                 	                 	                	} else if(TarifAggregator.TARIF_MULTIPLE.equals(categorieTarif)) {
 	                 	                 	                 	                                			out.print(categorieTarif);
 	                 	                 	                 	                 	                	} else {
 	                 	                 	                 	                                			out.print(objSession.getCode(categorieTarif));
 	                 	                 	                 	                 	                	}
 	                %>
					</td>
 	                <td class="number" onclick="<%=actionDetailPrest %>"><%=entetePrestation.getMontantTotal() %></td>
 	                <td onclick="<%=actionDetailPrest %>"><%=!ALCSPrestation.STATUT_CH.equals(entetePrestation.getStatut())?objSession.getCodeLibelle(entetePrestation.getStatut()):"" %></td>
 	                <td onclick="<%=actionDetailPrest %>"><%=entetePrestation.getDateVersComp() %></td>
 	                <td onclick="<%=actionDetailPrest %>"><%=ALServiceLocator.getPrestationBusinessService().isEditable(entetePrestation.getEtatPrestation())?"<img src='images/cadenas_ouvert.gif' alt='"+objSession.getCodeLibelle(entetePrestation.getEtatPrestation())+"'/>":"<img src='images/cadenas.gif' alt='"+objSession.getCodeLibelle(entetePrestation.getEtatPrestation()).concat(libelleNumFacture)+"'/" %></td>
 	                <td onclick="<%=actionDetailPrest %>"><%=objSession.getCodeLibelle(entetePrestation.getBonification()) %></td>
       			</tr>        
                <%
                	cptPrestationDisplay++;
                } 	
                %>              
      			<tfoot>
                  <tr class='<%="odd".equals(rowStyle)?"nonodd":"odd" %>'>
                    <td>
                    <%if(!viewBean.getDossierComplexModel().isNew() && hasNewRight){ 
                    	String bonification = JadeNumericUtil.isEmptyOrZero(viewBean.getDossierComplexModel().getDossierModel().getIdTiersBeneficiaire())?ALCSPrestation.BONI_INDIRECT:ALCSPrestation.BONI_DIRECT;
                    %>
                    	<a class="addLink" href="<%=servletContext + mainServletPath + "?userAction=al.prestation.generationDossier.afficher&_method=add&idDossier="+viewBean.getDossierComplexModel().getDossierModel().getIdDossier()+"&periodicite="+viewBean.getAffiliation().getPeriodicitieAffiliation()+"&bonification="+bonification%>" title='<%=objSession.getLabel("LINK_NEW_PRESTATION_DESC") %>'/>
                    <%} %>
                    </td>
                    <td colspan="9" style="text-align:center;">   
		                    <a href="#" id="paginationPrevious" >Préc.</a>
		                    <div id="paginationNumero"></div>
		                    <a href="#" id="paginationNext">Suiv.</a>
                    </td>
                  </tr>
                </tfoot>
              </table>   
 
			<%-- /tpl:insert --%>
			</td></tr>

										
<%@ include file="/theme/detail/bodyButtons.jspf" %>
			<%-- tpl:insert attribute="zoneButtons" --%>
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>	 
<%if(ALCSDossier.STATUT_IS.equals(viewBean.getDossierComplexModel().getDossierModel().getStatut())){ %>
<ct:menuChange displayId="options" menuId="dossier-main-adi" showTab="options" >
	<ct:menuSetAllParams key="selectedId" menuId="dossier-main-adi" value="<%=viewBean.getId()%>"  />
	<ct:menuSetAllParams key="id" menuId="dossier-main-adi" value="<%=viewBean.getId()%>"  />	
	<ct:menuSetAllParams key="searchModel.forIdDossier" menuId="dossier-main-adi" value="<%=viewBean.getDossierComplexModel().getId()%>"  />
	<ct:menuSetAllParams key="idExterne" menuId="dossier-main-adi" value="<%=viewBean.getDossierComplexModel().getId()%>"  />
	<% if(hasUpdateRight) { %>
		<ct:menuActivateNode menuId="dossier-main-adi" active="yes" nodeId="RADIER_ADI"/>
	<% } else { %>
		<ct:menuActivateNode menuId="dossier-main-adi" active="no" nodeId="RADIER_ADI"/>
	<% } %>
</ct:menuChange>
<%}else{ %>
<ct:menuChange displayId="options" menuId="dossier-main" showTab="options" >
	<ct:menuSetAllParams key="selectedId" menuId="dossier-main" value="<%=viewBean.getId()%>"  />
	<ct:menuSetAllParams key="id" menuId="dossier-main" value="<%=viewBean.getId()%>"  />	
	<ct:menuSetAllParams key="searchModel.forIdDossier" menuId="dossier-main" value="<%=viewBean.getDossierComplexModel().getId()%>"  />
	<ct:menuSetAllParams key="idExterne" menuId="dossier-main" value="<%=viewBean.getDossierComplexModel().getId()%>"  />
	<% if(hasUpdateRight) { %>
		<ct:menuActivateNode menuId="dossier-main" active="yes" nodeId="RADIER"/>
	<% } else { %>
		<ct:menuActivateNode menuId="dossier-main" active="no" nodeId="RADIER"/>
	<% } %>
</ct:menuChange>
<%} %>

<%-- tpl:insert attribute="zoneEndPage" --%>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/simple-popup.js"></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/dossier/dossier.js"></script>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
