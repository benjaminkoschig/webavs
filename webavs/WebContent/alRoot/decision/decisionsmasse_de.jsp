<%@page import="ch.globaz.naos.business.model.AffiliationSearchSimpleModel"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.globall.parameters.FWParametersSystemCodeManager"%>
<%@page import="globaz.globall.parameters.FWParametersSystemCode"%>
<%@page import="globaz.globall.parameters.FWParametersCode"%>
<%@page import="globaz.jade.client.util.JadeCodesSystemsUtil"%>
<%@page import="globaz.al.vb.decision.ALDecisionsmasseViewBean"%>
<%@page import="ch.globaz.al.business.models.dossier.DossierComplexModel"%>
<%@page import="ch.globaz.al.business.services.models.dossier.DossierComplexModelService"%>
<%@page import="ch.globaz.al.business.models.dossier.DossierModel"%>
<%@page import="ch.globaz.naos.business.service.AffiliationService"%>
<%@page import="ch.globaz.naos.business.service.AFBusinessServiceLocator"%>
<%@page import="ch.globaz.naos.business.model.AffiliationSimpleModel"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
//formAction = request.getContextPath() + mainServletPath + "Root/decision/decisionsmasse_upload_de.jsp";
ALDecisionsmasseViewBean viewBean = (ALDecisionsmasseViewBean) session.getAttribute("viewBean"); 
idEcran="AL0034";

bButtonNew = false;
bButtonUpdate = false;
bButtonDelete = false;
bButtonValidate = false;
bButtonCancel = false;

boolean hasUpdateRight = objSession.hasRight(userActionUpd, FWSecureConstants.UPDATE);
String processStarted = request.getParameter("process");
boolean processLaunched = "launched".equalsIgnoreCase(processStarted);
%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.css" rel="stylesheet" />

<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/ajax_webaf.js"></script>
<script type="text/javascript">
var processLauched = <%=processLaunched%>;
function add() {}
function upd() {}
function validate() {}
function cancel() {}
function del() {
}

function callBackUpload(data) {
	 $("#FILE_PATH_FOR_POPULATION").prop("disabled", false);
	 $("#FILE_PATH_FOR_POPULATION").val(data.path+"/"+data.fileName);
}

function init(){
	document.getElementById("forNumAffilieWidget").focus();	
}

function onClickLister(){
	document.getElementById("btnListe").disabled = true;
	document.getElementById("btnGen").disabled = true;
	
	state = validateFields();
	if (document.getElementById("filterEtat").value=="<%= viewBean.getSearchModel().ETATACTIF %>"){
		if(document.getElementById("fichierListeDossiers").value.length == 0) {
			document.forms[0].elements('userAction').value="al.decision.decisionsmasse.listeactif";
		} else {
		    	document.forms[0].elements('userAction').value="al.decision.decisionsmasse.listeimporter";
		}
	}else{
		document.forms[0].elements('userAction').value="al.decision.decisionsmasse.listeradie";
	}
	chargeUrl(state);
}

function onClickGenerer(){
	document.getElementById("btnListe").disabled = true;
	document.getElementById("btnGen").disabled = true;
	
	 state = validateFields();
	
	if (document.getElementById("filterEtat").value=="<%= viewBean.getSearchModel().ETATACTIF %>"){
		if(document.getElementById("fichierListeDossiers").value.length == 0) {
			document.forms[0].elements('userAction').value="al.decision.decisionsmasse.searchactif";
		} else {
				document.forms[0].elements('userAction').value="al.decision.decisionsmasse.importer";
		}
	}else{
		document.forms[0].elements('userAction').value="al.decision.decisionsmasse.searchradie";
	}
	chargeUrl(state);
}

function chargeUrl(state){
	if (state){
		document.forms[0].submit();
	}
}

function postInit() {
	$("#selectStatutDossier").multiselect("enable");
	$("#selectActiviteDossier").multiselect("enable");
	$("#selectTypeDroit").multiselect("enable");
	$("#selectTarifDossier").multiselect("enable");
	$("#selectAffilieNum").multiselect("enable");

	document.getElementById("btnListe").disabled = false;
	document.getElementById("btnGen").disabled = false;
	
	
	document.getElementById("forNumAffilieWidget").disabled = false;
	document.getElementById("filterAffilieId").disabled = false;
	document.getElementById("filterEtat").disabled = false;
	document.getElementById("dateValiditeGREAT").disabled = false;
	document.getElementById("dateValiditeLESS").disabled = false;
	document.getElementById("dateFinValiditeGREAT").disabled = false;
	document.getElementById("dateFinValiditeLESS").disabled = false;
	document.getElementById("dateDebutValidite").disabled = false;
	document.getElementById("fileName").disabled = false;
	document.getElementById("texteLibre").disabled = false;
	document.getElementById("insertionGED").disabled = false;
	document.getElementById("gestionTexteLibre").disabled = false;
	document.getElementById("gestionCopie").disabled = false;
	document.getElementById("dateImpression").disabled = false;
	document.getElementById("email").disabled = false;
	document.getElementById("triImpression").disabled = false;
	document.getElementById("filterEtat").selectedIndex = "<%=viewBean.getEtatFilter().equalsIgnoreCase(viewBean.getSearchModel().ETATACTIF) ? 0:1%>";
	hiddenDateSelector(document.getElementById("filterEtat").value);
	
}

function cleanAllCriteria(){
	<% viewBean.setFilterEtat(viewBean.getSearchModel().ETATACTIF);%>
	
	postInit();
	$("#selectStatutDossier").multiselect("checkAll");
	$('#selectStatutDossier').multiselect('refresh');
	$("#selectActiviteDossier").multiselect("checkAll");
	$('#selectActiviteDossier').multiselect('refresh');
	$("#selectTypeDroit").multiselect("checkAll");
	$('#selectTypeDroit').multiselect('refresh');
	$("#selectTarifDossier").multiselect("checkAll");
	$('#selectTarifDossier').multiselect('refresh');
	$("#selectAffilieNum").multiselect("uncheckAll");
	$('#selectAffilieNum').multiselect('refresh');
	document.getElementById("FILE_PATH_FOR_POPULATION").value = "";
	document.getElementById("fichierListeDossiers").value = "";
	document.getElementById("dateValiditeGREAT").value = "";
	document.getElementById("dateValiditeLESS").value = "";
	document.getElementById("dateFinValiditeGREAT").value = "";
	document.getElementById("dateFinValiditeLESS").value = "";
	document.getElementById("texteLibre").value = "";
	document.getElementById("dateDebutValidite").value = "";
	document.getElementById("insertionGED").checked = "<%if(viewBean.getInsertionGED()) { %>checked<%} %>";
	document.getElementById("gestionTexteLibre").checked  = "<%if(viewBean.getGestionTexteLibre()) { %>checked<%} %>";
	document.getElementById("gestionCopie").checked  = "<%if(viewBean.getGestionCopie()) { %>checked<%} %>";
	document.getElementById("dateImpression").value  = "<%=viewBean.getDateImpression() %>";
	document.getElementById("email").value  = "<%=viewBean.getEmail() %>";
	document.getElementById("triImpression").selectedIndex = 0;
	
}

function hiddenDateSelector(etat){
	if (etat=="<%=viewBean.getSearchModel().ETATACTIF %>"){
		document.getElementById("selectActifTR").style.display = "";
		document.getElementById("fichierListeDossiers").disabled = false;
		document.getElementById("dateDebutValidite").disabled = false;
	}else{
		document.getElementById("selectActifTR").style.display = "none";
		document.getElementById("fichierListeDossiers").disabled = true;
		document.getElementById("dateDebutValidite").disabled = true;
		
	}
}

//code to execute when the DOM is ready
$(document).ready(function() {	
	
	$("#selectAffilieNum").multiselect({
		height: 180,
		minWidth: 250,
		noneSelectedText: "",
		checkAllText: "Tous",
		uncheckAllText: "Aucun",
		selectedText: function(numChecked, numTotal, checkedItems){

			var csValueChecked = "";
			var csLabelCourtChecked ="";
			var csLabelLongChecked="";
			var nb = 0;
			
			$(checkedItems).each(function() {
				nb++;			
				var csValue = $(this).val().substring(0,$(this).val().indexOf('|'));
				var labelCourtCs = $(this).val().substring($(this).val().indexOf('|')+1,$(this).val().length);
				var labelLongCs = $(this).next().html();
			
				csValueChecked+=csValue;
				csLabelCourtChecked=nb + " affiliés sélectionnés";
				csLabelLongChecked=labelLongCs;
				if (nb<numChecked) {
					csValueChecked+=",";
				}
				
			});
			$('#filterAffilieId').val(csValueChecked);
			
			
			if(nb==1){
				return csLabelLongChecked;
			}
			else{
				return csLabelCourtChecked;
			}
			
		},
		uncheckAll: function(){
			$('#filterAffilieId').val('');
		},
		click: function(event, ui){
			var csValueChecked = "";
			var nb = 0;
			$.each( $("#selectAffilieNum").multiselect("getChecked"), function(numChecked){
				nb++;
				var csValue = $(this).val().substring(0,$(this).val().indexOf('|'));
			
				csValueChecked+=csValue;
				if (nb<numChecked) {
					csValueChecked+=",";
				}
			});
			
			$('#filterAffilieId').val(csValueChecked);
		   }
	});
	

	$("#selectStatutDossier").multiselect({
		height: 180,
		noneSelectedText: "Aucun",
		checkAllText: "Tous",
		uncheckAllText: "Aucun",
		selectedList:1,
		selectedText: function(numChecked, numTotal, checkedItems){

			var csValueChecked = "";
			var csLabelCourtChecked ="";
			var csLabelLongChecked="";
			var nb = 0;
			$(checkedItems).each(function() {
				
				nb++;			
				var csValue = $(this).val().substring(0,$(this).val().indexOf('-'));
				var labelCourtCs = $(this).val().substring($(this).val().indexOf('-')+1,$(this).val().length);
				var labelLongCs = $(this).next().html();
			
				csValueChecked+=csValue;
				csLabelCourtChecked+=labelCourtCs;
				csLabelLongChecked+=labelLongCs;
				if (nb<numChecked) {
					csLabelCourtChecked +=", ";
					csLabelLongChecked+=", ";
					csValueChecked+=",";
				}
				
			});
			if(numChecked==numTotal){
				$('#filterStatutId').val("");
				return "Tous";
			}else{
				$('#filterStatutId').val(csValueChecked);
			}
			
			if(nb==1){
				return csLabelLongChecked;
			}
			else{
				return csLabelCourtChecked;
			}
			
		},
		uncheckAll: function(){
			$('#filterStatutId').val('');
		}, 
		click: function(event, ui){
			var csValueChecked = "";
			var nb = 0;
			$.each( $("#selectStatutDossier").multiselect("getChecked"), function(numChecked){
				nb++;
				var csValue = $(this).val().substring(0,$(this).val().indexOf('-'));
			
				csValueChecked+=csValue;
				if (nb<numChecked) {
					csValueChecked+=",";
				}
			});
			
			$('#filterStatutId').val(csValueChecked);
		   }
		
	});
	
	$("#selectActiviteDossier").multiselect({
		height: 270,
		noneSelectedText: "Aucun",
		checkAllText: "Tous",
		uncheckAllText: "Aucun",
		selectedList:1,
		selectedText: function(numChecked, numTotal, checkedItems){
		
			var csValueChecked = "";
			var csLabelCourtChecked ="";
			var csLabelLongChecked="";
			var nb = 0;
			$(checkedItems).each(function() {
				
				nb++;			
				var csValue = $(this).val().substring(0,$(this).val().indexOf('-'));
				var labelCourtCs = $(this).val().substring($(this).val().indexOf('-')+1,$(this).val().length);
				var labelLongCs = $(this).next().html();
				
				csValueChecked+=csValue;
				csLabelCourtChecked=nb+ " activités sélectionnées";
				csLabelLongChecked+=labelLongCs;
				if (nb<numChecked) {
					csLabelLongChecked+=", ";
					csValueChecked+=",";
				}
				
			});
			
			if(numChecked==numTotal){
				$('#filterActiviteId').val("");
				return "Tous";
			}else{
				$('#filterActiviteId').val(csValueChecked);
			}
			
			if(nb==1){
				return csLabelLongChecked;
			}
			else{
				return csLabelCourtChecked;
			}
			
		},
		uncheckAll: function(){
			$('#filterActiviteId').val('');
		}, 
		click: function(event, ui){
			var csValueChecked = "";
			var nb = 0;
			$.each( $("#selectActiviteDossier").multiselect("getChecked"), function(numChecked){
				nb++;
				var csValue = $(this).val().substring(0,$(this).val().indexOf('-'));
			
				csValueChecked+=csValue;
				if (nb<numChecked) {
					csValueChecked+=",";
				}
			});
			
			$('#filterActiviteId').val(csValueChecked);
		   }
		
	});
	
	$("#selectTypeDroit").multiselect({
		height: 110,
		noneSelectedText: "Aucun",
		checkAllText: "Tous",
		uncheckAllText: "Aucun",
		selectedList:1,
		selectedText: function(numChecked, numTotal, checkedItems){

			var csValueChecked = "";
			var csLabelCourtChecked ="";
			var csLabelLongChecked="";
			var nb = 0;
			$(checkedItems).each(function() {
				
				nb++;			
				var csValue = $(this).val().substring(0,$(this).val().indexOf('-'));
				var labelCourtCs = $(this).val().substring($(this).val().indexOf('-')+1,$(this).val().length);
				var labelLongCs = $(this).next().html();
			
				csValueChecked+=csValue;
				csLabelCourtChecked+=labelCourtCs;
				csLabelLongChecked+=labelLongCs;
				if (nb<numChecked) {
					csLabelCourtChecked +=", ";
					csLabelLongChecked+=", ";
					csValueChecked+=",";
				}
				
			});
			
			if(numChecked==numTotal){
				$('#filterDroitId').val("");
				return "Tous";
			}else{
				$('#filterDroitId').val(csValueChecked);
			}
			if(nb==1){
				return csLabelLongChecked;
			}
			else{
				return csLabelCourtChecked;
			}
			
		},
		uncheckAll: function(){
			$('#filterDroitId').val('');
		}, 
		click: function(event, ui){
			var csValueChecked = "";
			var nb = 0;
			$.each( $("#selectTypeDroit").multiselect("getChecked"), function(numChecked){
				nb++;
				var csValue = $(this).val().substring(0,$(this).val().indexOf('-'));
			
				csValueChecked+=csValue;
				if (nb<numChecked) {
					csValueChecked+=",";
				}
			});
			
			$('#filterDroitId').val(csValueChecked);
		   }
		
	});
	
	$("#selectTarifDossier").multiselect({
		height: 270,
		noneSelectedText: "Aucun",
		checkAllText: "Tous",
		uncheckAllText: "Aucun",
		selectedList:1,
		selectedText: function(numChecked, numTotal, checkedItems){

			var csValueChecked = "";
			var csLabelCourtChecked ="";
			var csLabelLongChecked="";
			var nb = 0;
			$(checkedItems).each(function() {
				
				nb++;			
				var csValue = $(this).val().substring(0,$(this).val().indexOf('-'));
				var labelCourtCs = $(this).val().substring($(this).val().indexOf('-')+1,$(this).val().length);
				var labelLongCs = $(this).next().html();
			
				csValueChecked+=csValue;
				csLabelCourtChecked= nb + " tarifs sélectionnés";
				csLabelLongChecked+=labelLongCs;
				if (nb<numChecked) {
					csLabelLongChecked+=", ";
					csValueChecked+=",";
				}
				
			});
			
			if(numChecked==numTotal){
				$('#filterTarifId').val("");
				return "Tous";
			}else{
				$('#filterTarifId').val(csValueChecked);
			}
			
			if(nb==1){
				return csLabelLongChecked;
			}
			else{
				return csLabelCourtChecked;
			}
			
		},
		uncheckAll: function(){
			$('#filterTarifId').val('');
		}, 
		click: function(event, ui){
			var csValueChecked = "";
			var nb = 0;
			$.each( $("#selectTarifDossier").multiselect("getChecked"), function(numChecked){
				nb++;
				var csValue = $(this).val().substring(0,$(this).val().indexOf('-'));
			
				csValueChecked+=csValue;
				if (nb<numChecked) {
					csValueChecked+=",";
				}
			});
			
			$('#filterTarifId').val(csValueChecked);
		   }
		
	});
	
	
}); 

function mouseDown(e) {
	 var ctrlPressed=0;
	 var altPressed=0;
	 var shiftPressed=0;

	 if (parseInt(navigator.appVersion)>3) {

	  var evt = e ? e:window.event;
	   // NEWER BROWSERS [CROSS-PLATFORM]
	   shiftPressed=evt.shiftKey;
	   altPressed  =evt.altKey;
	   ctrlPressed =evt.ctrlKey;
	   self.status=""
	    +  "shiftKey="+shiftPressed 
	    +", altKey="  +altPressed 
	    +", ctrlKey=" +ctrlPressed 
	  
	    if (shiftPressed && altPressed && ctrlPressed){
			$("#adminLink").show();  
		} 
	 }
	 return true;
	}
	
if (parseInt(navigator.appVersion)>3) {
 document.onmousedown = mouseDown;
}
	
</script>
<style type="text/css">

div#AL0034zone div {
	width:30%;
	height:30px;
	vertical-align:middle;
	float:left;
	/*border:1px solid red*/
}

div#AL0034zone {
	padding:5px;
}


div#AL0034zone label {
	width:110px;
}

.globazMultiWidgets .radiosZoneGauche span{
	display:none; !important
}
</style>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%>
	<ct:FWLabel key="AL0034_TITRE" />
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<% if (processLaunched) { %>
<tr>
	<td style="height: 2em; color: white; font-weight: bold; text-align: center; background-color: green" colspan="6"><ct:FWLabel key="FW_PROCESS_STARTED" /></td>
</tr>
<% } %>	

<table id="subtable" style="height: 100px; margin-left:10px;" >
<tr><td colspan="6">&nbsp;<td></tr>	
	<tr><td class="subtitle" colspan="2">
			<ct:FWLabel key="AL0034_TITRE_CRITERE"/>
	</td></tr>
	<tr><td colspan="6"><p style="padding:20px 0"><em><ct:FWLabel key="AL0034_RECOMMANDATION_CRITERE"/></em></p><td></tr>
	<tr style="padding-bottom: 1em;">
		<td><ct:FWLabel key="AL0034_RECHERCHE_AFFILIE"/></td>  	
		<td class="mediumSelect">
			<div>
				<select multiple id="selectAffilieNum" tabindex="1">
				<%
				for(String affilie : viewBean.getInAffilie()){
				    String tempAffilieRaisonSociale = "affiliation.raisonSociale";
				    AffiliationSearchSimpleModel searchModel = new AffiliationSearchSimpleModel();
				    searchModel.setForNumeroAffilie(affilie);
				    try{
				        searchModel = AFBusinessServiceLocator.getAffiliationService().find(searchModel);
				        if (searchModel.getSize() >= 1) {
			                AffiliationSimpleModel affilieSM = (AffiliationSimpleModel) searchModel.getSearchResults()[0];
			                tempAffilieRaisonSociale = affilieSM.getRaisonSocialeCourt();
			            }
				    }catch (Exception e ){
				        tempAffilieRaisonSociale = "";
				    }
				    %><option selected value="<%=affilie+"|"+tempAffilieRaisonSociale%>"><%=affilie+" - "+ tempAffilieRaisonSociale%></option><%
				}
				%>
				</select>
			</br></div>
			<ct:widget tabindex="1" name="forNumAffilieWidget" id="forNumAffilieWidget" styleClass="normal" >
				<ct:widgetService methodName="widgetFind" className="<%=AffiliationService.class.getName()%>" defaultSearchSize="20">
					<ct:widgetCriteria criteria="likeNumeroAffilie" label="CRITERIA_NUM_AFFILIE"/>
					<ct:widgetCriteria criteria="likeDesignationUpper" label="CRITERIA_NOM_AFFILIE"/>			
					<ct:widgetLineFormatter format="#{affiliation.affilieNumero} #{affiliation.raisonSocialeCourt} "/>
					<ct:widgetJSReturnFunction>
							<script type="text/javascript">
							function(element){
								var x = document.getElementById('selectAffilieNum');
								var saisie = $(element).attr('affiliation.affilieNumero')+'|'+$(element).attr('affiliation.raisonSocialeCourt');
								var saisie2 = $(element).attr('affiliation.affilieNumero')+' - '+$(element).attr('affiliation.raisonSocialeCourt');
								x.options[x.length] = new Option(saisie2, saisie, true, true);
							    $('#selectAffilieNum').multiselect('refresh');
								this.value=''
								}
							</script>										
					</ct:widgetJSReturnFunction>
				</ct:widgetService>			
			</ct:widget> 
			<ct:inputHidden id="filterAffilieId" name="filterAffilie" />
		</td>
		<td><ct:FWLabel key="AL0034_RECHERCHE_ACTIVITE"/></td>
		<td>
			<select multiple id="selectActiviteDossier" tabindex="2">
				<% 
					FWParametersSystemCodeManager cmActiviteDossier = new FWParametersSystemCodeManager();
					cmActiviteDossier.setSession(objSession);
					cmActiviteDossier.setForActif(true);
					cmActiviteDossier.setForIdGroupe("ALDOSACTAL");
					cmActiviteDossier.setForIdLangue(objSession.getIdLangue());
					try {
						cmActiviteDossier.find();
					} catch (Exception e) {
						e.printStackTrace();
					}
					for(int i=0;i<cmActiviteDossier.size();i++){
						FWParametersCode code = (FWParametersCode) cmActiviteDossier.getEntity(i);
						%><option <%=viewBean.selectedActivite(code.getIdCode())%> value="<%=code.getIdCode()+"-"+JadeCodesSystemsUtil.getCode(objSession,code.getIdCode())%>"><%=JadeCodesSystemsUtil.getCodeLibelle(objSession,code.getIdCode()) %></option><% 	
					}
				%>
			</select>
			<ct:inputHidden  id="filterActiviteId" name="filterActivite"  />
		</td>
		<td><ct:FWLabel key="AL0034_RECHERCHE_STATUT"/></td>
		<td>
			<select multiple id="selectStatutDossier" tabindex="3">
				<% 
					FWParametersSystemCodeManager cmStatutDossier = new FWParametersSystemCodeManager();
        			cmStatutDossier.setSession(objSession);
					cmStatutDossier.setForActif(true);
					cmStatutDossier.setForIdGroupe("ALDOSSTATU");
					cmStatutDossier.setForIdLangue(objSession.getIdLangue());
					try {
						cmStatutDossier.find();
					} catch (Exception e) {
						e.printStackTrace();
					}
					for(int i=0;i<cmStatutDossier.size();i++){
						FWParametersCode code = (FWParametersCode) cmStatutDossier.getEntity(i);
						%><option <%=viewBean.selectedStatut(code.getIdCode())%> value="<%=code.getIdCode()+"-"+JadeCodesSystemsUtil.getCode(objSession,code.getIdCode())%>"><%=JadeCodesSystemsUtil.getCode(objSession,code.getIdCode())+" - "+JadeCodesSystemsUtil.getCodeLibelle(objSession,code.getIdCode()) %></option><% 	
					}
				%>
			</select>
			<ct:inputHidden  id="filterStatutId" name="filterStatut"  />
		</td>
	</tr>
	<tr style="padding-bottom: 1em;">
		<td><ct:FWLabel key="AL0034_RECHERCHE_TARIF"/></td>
		<td>
			<select multiple id="selectTarifDossier" tabindex="4">
				<% 
					FWParametersSystemCodeManager cmTarifDossier = new FWParametersSystemCodeManager();
        			cmTarifDossier.setSession(objSession);
					cmTarifDossier.setForActif(true);
					cmTarifDossier.setForIdGroupe("ALTARCAT");
					cmTarifDossier.setForIdLangue(objSession.getIdLangue());
					try {
						cmTarifDossier.find();
					} catch (Exception e) {
						e.printStackTrace();
					}
					for(int i=0;i<cmTarifDossier.size();i++){
						FWParametersCode code = (FWParametersCode) cmTarifDossier.getEntity(i);
						%><option <%=viewBean.selectedTarif(code.getIdCode())%> value="<%=code.getIdCode()+"-"+JadeCodesSystemsUtil.getCode(objSession,code.getIdCode())%>"><%=JadeCodesSystemsUtil.getCodeLibelle(objSession,code.getIdCode()) %></option><% 	
					}
				%>
			</select>
			<ct:inputHidden id="filterTarifId" name="filterTarif"  />
		</td>
		<td><ct:FWLabel key="AL0034_RECHERCHE_DROIT"/></td>
		<td>
			<select multiple id="selectTypeDroit" tabindex="5">
				<% 
					FWParametersSystemCodeManager cmTypeDroit = new FWParametersSystemCodeManager();
        			cmTypeDroit.setSession(objSession);
					cmTypeDroit.setForActif(true);
					cmTypeDroit.setForIdGroupe("ALDOSTYDRO");
					cmTypeDroit.setForIdLangue(objSession.getIdLangue());
					try {
						cmTypeDroit.find();
					} catch (Exception e) {
						e.printStackTrace();
					}
					for(int i=0;i<cmTypeDroit.size();i++){
						FWParametersCode code = (FWParametersCode) cmTypeDroit.getEntity(i);
						%><option <%=viewBean.selectedDroit(code.getIdCode())%> value="<%=code.getIdCode()+"-"+JadeCodesSystemsUtil.getCode(objSession,code.getIdCode())%>"><%=JadeCodesSystemsUtil.getCodeLibelle(objSession,code.getIdCode()) %></option><% 	
					}
				%>
			</select>
			<ct:inputHidden id="filterDroitId" name="filterDroit" />
		</td>
		<td><ct:FWLabel key="AL0034_RECHERCHE_ETAT"/></td>
		<td><select name="filterEtat" onchange="hiddenDateSelector(this.value)" tabindex="6">.
 					<option <%=viewBean.selectedEtat(viewBean.ETATACTIF)%> value="<%=viewBean.ETATACTIF%>"><ct:FWLabel key="AL0034_RECHERCHE_ETAT_ACTIF"/></option>
 					<option <%=viewBean.selectedEtat(viewBean.ETATRADIE)%> value="<%=viewBean.ETATRADIE%>"><ct:FWLabel key="AL0034_RECHERCHE_ETAT_RADIE"/></option>
 			</select>
 		</td>
	</tr>
	<tr id="selectActifTR">	
		<td>
					<label style="width:150px" for="dateValiditeGREAT"><ct:FWLabel key="AL0034_RECHERCHE_DATE_VALIDITE_DEBUT"/></label>
		</td><td>
					<input tabindex="7" class="clearable" type="text" name="dateValiditeGREAT" id="dateValiditeGREAT" data-g-calendar="mandatory:false" value="<%=viewBean.getDateValiditeGREAT() %>"/>
		</td >
		<td>
					<label style="width:150px" for="dateValiditeLESS"><ct:FWLabel key="AL0034_RECHERCHE_DATE_AU"/></label>
		</td><td>
					<input tabindex="8" class="clearable" type="text" name="dateValiditeLESS" id="dateValiditeLESS" data-g-calendar="mandatory:false" value="<%=viewBean.getDateValiditeLESS() %>"/>
		</td>
		<td data-g-tooltip="libelle:<%= objSession.getLabel("AL0034_FICHIER_LISTE_DOSSIER_INFO")%>">
					<label for="fichierListeDossiers"><ct:FWLabel key="AL0034_FICHIER_LISTE_DOSSIER"/></label>
		</td><td>
						<input type ="hidden" name="fileName" id="FILE_PATH_FOR_POPULATION" />
					<input  id="fichierListeDossiers" name="originalFileName" class="notSend" type = "file" data-g-upload="callBack: callBackUpload, protocole:jdbc" > 	
		</td>
	</tr>
	<tr>
		<td>
					<label style="width:150px" for="dateFinValiditeGREAT"><ct:FWLabel key="AL0034_RECHERCHE_DATE_VALIDITE_FIN"/></label>
		</td><td>
					<input tabindex="9" class="clearable" type="text" name="dateFinValiditeGREAT" id="dateFinValiditeGREAT" data-g-calendar="mandatory:false" value="<%=viewBean.getDateFinValiditeGREAT() %>"/>
		</td>
		<td>
					<label style="width:150px" for="dateFinValiditeLESS"><ct:FWLabel key="AL0034_RECHERCHE_DATE_AU"/></label>
		</td><td>
					<input tabindex="10" class="clearable" type="text" name="dateFinValiditeLESS" id="dateFinValiditeLESS" data-g-calendar="mandatory:false" value="<%=viewBean.getDateFinValiditeLESS() %>"/>
		</td>
				
				
	</tr>	
			
	<tr><td colspan="6">&nbsp;<td></tr>			
	<tr>
		<td colspan="6">
			<table width="100%">
					<tr>
					<td style="width:1px; padding: 0 10px;"><label for="texteLibre" style="display:block;white-space: nowrap;"><b><ct:FWLabel key="AL0034_TEXTE_LIBRE"/></b></label></td>
					<td><hr /></td>
					</tr>
			</table>
		</td></tr>
	<tr><td colspan="6">
					<textarea tabindex="11" id="texteLibre" name="texteLibre" style="width:100%;height:100px"><%=viewBean.getTexteLibre()%></textarea>
		</td></tr>
	<tr><td colspan="6">&nbsp;<td></tr>
	<tr>
		<td colspan="6">
				<table width="100%">
					<tr>
					<td style="width:1px; padding: 0 10px;"><label for="texteLibre" style="display:block;white-space: nowrap;"><b><ct:FWLabel key="AL0034_MODIFIER_DATE"/></b></td>
					<td><hr /></td>
					</tr>
				</table>
		</td></tr>
	<tr><td colspan="6">&nbsp;<td></tr>
	<tr><td id="selectActifTD1" style = "display:inline" >
					<label style="width:150px" for="dateValidite"><ct:FWLabel key="AL0034_DATE_VALIDITE"/></label>
				</td>
				<td><input tabindex="12" class="clearable" type="text" name="dateDebutValidite" id="dateDebutValidite" data-g-calendar="mandatory:false" value="<%=viewBean.getDateDebutValidite() %>"/></td>
	</tr>
	<tr>
				<td colspan="6">
				<table width="100%">
					<tr>
					<td style="width:1px; padding: 0 10px;"><label for="gestionCopie" style="display:block;white-space: nowrap;"><b><ct:FWLabel key="AL0034_OPTION_IMPRESS"/></b></label></td>
					<td><hr /></td>
					</tr>
				</table>
		</td></tr>
			
	<tr><td colspan="6">&nbsp;<td></tr>
	<tr><td>
				<label for="gestionCopie"><ct:FWLabel key="AL0034_GESTION_COPIE"/></label>
		</td><td>
				<input tabindex="13" name="gestionCopie" id="gestionCopie" type="checkbox" tabindex="4" <%if(viewBean.getGestionCopie()) { %>checked="checked"<%} %>/>
		</td><td>
				<label for="gestionTexteLibre"><ct:FWLabel key="AL0034_GESTION_TEXTE_LIBRE"/></label>
		</td><td>
				<input tabindex="14" name="gestionTexteLibre" id="gestionTexteLibre" type="checkbox" tabindex="4" <%if(viewBean.getGestionTexteLibre()) { %>checked="checked"<%} %>/>
		</td><td>
				<label for="dateImpression" class="marge"><ct:FWLabel key="AL0034_DATE_IMPRESSION"/></label>
		</td><td>
				<input tabindex="15" class="clearable" type="text" name="dateImpression" id="dateImpression" data-g-calendar="mandatory:false" value="<%=viewBean.getDateImpression() %>"/>
		</td></tr>
	<tr><td>
				<label for="insertionGED"><ct:FWLabel key="AL0034_INSERTION_GED"/></label>
		</td><td>
				<input tabindex="16" name="insertionGED" id="insertionGED" type="checkbox" tabindex="4" <%if(viewBean.getInsertionGED()) { %>checked="checked"<%} %>/>
		</td>
		<td>
				<label for="triImpression"><ct:FWLabel key="AL0034_TRI_IMPRESSION"/></label>
		</td>
		<td><select name="triImpression" tabindex="17">
 						<option <%=viewBean.selectedTri(viewBean.getSearchModel().ORDER_AFFILIE_ALLOC)%> value="<%= viewBean.getSearchModel().ORDER_AFFILIE_ALLOC %>"><ct:FWLabel key="AL0034_FILTRE_TRI_ORDER_AFFILIE_ALLOC"/></option>
 						<option <%=viewBean.selectedTri(viewBean.getSearchModel().ORDER_ALLOC)%> value="<%= viewBean.getSearchModel().ORDER_ALLOC %>"><ct:FWLabel key="AL0034_FILTRE_TRI_ORDER_ALLOC"/></option>
 						<option <%=viewBean.selectedTri(viewBean.getSearchModel().ORDER_DIRECT_AFFILIE)%> value="<%= viewBean.getSearchModel().ORDER_DIRECT_AFFILIE %>"><ct:FWLabel key="AL0034_FILTRE_TRI_ORDER_DIRECT_AFFILIE"/></option>
 						<option <%=viewBean.selectedTri(viewBean.getSearchModel().ORDER_DIRECT_ALLOC)%> value="<%= viewBean.getSearchModel().ORDER_DIRECT_ALLOC %>"><ct:FWLabel key="AL0034_FILTRE_TRI_ORDER_DIRECT_ALLOC"/></option>
 			</select></td>
		<td>
				<label for="email"><ct:FWLabel key="AL0034_EMAIL"/></label>
		</td><td>
				<input tabindex="18"  type="text" id="email" name="email" value="<%=viewBean.getEmail() %>"/>
		</td></tr>
	
	
<%-- /tpl:insert --%>

<%@ include file="/theme/detail/bodyButtons.jspf" %>

<% if (hasUpdateRight) { %>
<INPUT type="button" class="btnCtrl" id="btnListe" value="<%=objSession.getLabel("LISTE")%>" onclick="onClickLister();return false;" tabindex="19">
<INPUT type="button" class="btnCtrl" id="btnGen" value="<%=objSession.getLabel("GENERER")%>" onclick="onClickGenerer();return false;" tabindex="20">
<% } %>
<INPUT type="button" class="btnCtrl" id="btnEff" value="<%=objSession.getLabel("EFFACER")%>" onclick="cleanAllCriteria();return false;" tabindex="21">
	</table>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>	
<%-- tpl:insert attribute="zoneEndPage" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/detail/footer.jspf" %>

				