<%@page import="globaz.globall.parameters.FWParametersSystemCodeManager"%>
<%@page import="globaz.globall.parameters.FWParametersSystemCode"%>
<%@page import="globaz.globall.parameters.FWParametersCode"%>
<%@page import="ch.globaz.al.business.constantes.ALCSDossier"%>
<%@page import="globaz.jade.client.util.JadeCodesSystemsUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<% 
String formatterAffilie = objSession.getApplication().getProperty("formatNumAffilie");
idEcran = "AL0002";
rememberSearchCriterias = true;
actionNew = servletContext + mainServletPath + "?userAction=al.dossier.dossierMain.afficher&_method=add";

//récupération des éventuels paramètres de préconfiguration de la recherche
String idAllocataire = "";
if(!JadeStringUtil.isEmpty(request.getParameter("idAllocataire")))
	idAllocataire=request.getParameter("idAllocataire");

String nomAllocataire = "";
if(!JadeStringUtil.isEmpty(request.getParameter("nomAllocataire")))
	nomAllocataire=request.getParameter("nomAllocataire");

String prenomAllocataire = "";
if(!JadeStringUtil.isEmpty(request.getParameter("prenomAllocataire")))
	prenomAllocataire=request.getParameter("prenomAllocataire");

String idEnfant = "";
if(!JadeStringUtil.isEmpty(request.getParameter("idEnfant")))
	idEnfant=request.getParameter("idEnfant");

String nomEnfant = "";
if(!JadeStringUtil.isEmpty(request.getParameter("nomEnfant")))
	nomEnfant=request.getParameter("nomEnfant");

String prenomEnfant = "";
if(!JadeStringUtil.isEmpty(request.getParameter("prenomEnfant")))
	prenomEnfant=request.getParameter("prenomEnfant");

%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.css" rel="stylesheet" />

<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/numaffilieformatter.js"></script>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<script type="text/javascript">

usrAction = "al.dossier.dossier.lister";

function resetSearchFields(){
	
	document.getElementById("searchModel.likeNomAllocataire").value="";
	document.getElementById("searchModel.forIdDossier").value="";
	document.getElementById("searchModel.likeNomEnfant").value="";
	document.getElementById("searchModel.likePrenomAllocataire").value="";
	document.getElementById("searchModel.likeNomAffilie").value="";
	document.getElementById("searchModel.likePrenomEnfant").value="";
	document.getElementById("searchModel.forIdAllocataire").value="";
	document.getElementById("searchModel.likeNssAllocataire").value="";
	document.getElementById("likeNssAllocataire").value="";
	document.getElementById("partiallikeNssAllocataire").value="";
	
	document.getElementById("searchModel.forNumAffilie").value="";
	document.getElementById("searchModel.likeNomAffilie").value="";
	document.getElementById("searchModel.likeNssEnfant").value="";
	document.getElementById("likeNssEnfant").value="";
	document.getElementById("partiallikeNssEnfant").value="";
	document.getElementById("forDateEnfant").value="";
	document.getElementById("searchModel.forDateEnfant").value="";

	//document.forms[0].submit();	
}

function initCheckBox() {
	var nssToSearch;
	//On tente de récupérer le numéro NSS dans le cas d'un back
	var nssPrefixe = $('input[name="likeNssAllocataireNssPrefixe"]').val();
	var nssAllocataire = $('input[name="partiallikeNssAllocataire"]').val();
	var nssEnfant = $('input[name="partiallikeNssEnfant"]').val();
	if(nssAllocataire!="") {
		nssToSearch = nssPrefixe + nssAllocataire;
		$('input[name="searchModel.likeNssAllocataire"]').val(nssToSearch);
	}
	else if(nssEnfant!="") {
		nssToSearch = nssPrefixe + nssEnfant;
		$('input[name="searchModel.likeNssEnfant"]').val(nssToSearch);
	}
	//surcharge du comportement du nss taglib pour être en accord avec l'écran AL0004
	// commenter cette ligne pour gérer aussi le n°avs
	$('input[name="partiallikeNssAllocataire"]').keypress(function(e){	
	});
	$('input[name="partiallikeNssAllocataire"]').keyup(function(e){
		//si on arrive dans le champ grâce au tab, on fait pas la suite, car prob pour sélection
		if(e.keyCode!=9){
			nssOnKeyUp('likeNssAllocataire');
			// pour formater a chaque passage, activer la ligne ci-dessous		
			nssAction('likeNssAllocataire');
	
			var nssToSearch = $('input[name="likeNssAllocataireNssPrefixe"]').val()+this.value;
			$('input[name="searchModel.likeNssAllocataire"]').val(nssToSearch);
		
		}	
	});

	//surcharge du comportement du nss taglib pour être en accord avec l'écran AL0004
	// commenter cette ligne pour gérer aussi le n°avs
	$('input[name="partiallikeNssEnfant"]').keypress(function(e){	
	});
	$('input[name="partiallikeNssEnfant"]').keyup(function(e){
		//si on arrive dans le champ grâce au tab, on fait pas la suite, car prob pour sélection
		if(e.keyCode!=9){
			nssOnKeyUp('likeNssEnfant');
			// pour formater a chaque passage, activer la ligne ci-dessous
			nssAction('likeNssEnfant');
			var nssToSearch = $('input[name="likeNssEnfantNssPrefixe"]').val()+this.value;
			$('input[name="searchModel.likeNssEnfant"]').val(nssToSearch);
		}
	});
	
	//on efface tous les champs quand on entre dans iddossier 
	/*document.getElementById('searchModel.forIdDossier').onfocus = function(){
		resetZonePartForm('AL0002');
	};*/
	//la date peut-être définie sans être entrer dans le champ => back par exemple, il faut donc setter la date effectivement utilisée
	//if(document.getElementById('forDateEnfantValue').value=='' && document.getElementById('forDateEnfant').value!='')
		document.getElementById('forDateEnfantValue').value = document.getElementById('forDateEnfant').value;
	
	
	//si l'un de ces critères est rempli, on lance une recherche 
	var idAllocataire='<%=JavascriptEncoder.getInstance().encode(idAllocataire) %>';
	var nomAllocataire='<%=JavascriptEncoder.getInstance().encode(nomAllocataire) %>';
	var prenomAllocataire='<%=JavascriptEncoder.getInstance().encode(prenomAllocataire) %>';
	var idEnfant='<%=JavascriptEncoder.getInstance().encode(idEnfant) %>';
	var nomEnfant='<%=JavascriptEncoder.getInstance().encode(nomEnfant) %>';
	var prenomEnfant='<%=JavascriptEncoder.getInstance().encode(prenomEnfant) %>';
	
	if(idAllocataire!=''){
		document.getElementById("searchModel.forIdAllocataire").value=idAllocataire;
		document.getElementById("searchModel.likeNomAllocataire").value=nomAllocataire;
		document.getElementById("searchModel.likePrenomAllocataire").value=prenomAllocataire;
	}
		
	if(idEnfant!=''){
		document.getElementById("searchModel.forIdEnfant").value=idEnfant;
		document.getElementById("searchModel.likeNomEnfant").value=nomEnfant;
		document.getElementById("searchModel.likePrenomEnfant").value=prenomEnfant;
	}
		
	if(idEnfant!='' || idAllocataire!='')
		document.forms[0].submit();	

	//on vide le critère pour avoir le comportement par défaut si on relance la recherche
	document.getElementById("searchModel.forIdAllocataire").value="";
	document.getElementById("searchModel.forIdEnfant").value="";


}
// code to execute when the DOM is ready
$(document).ready(function() {	

		$('input:checkbox').each(function(){   
		   var nameCb = $(this).attr("name");
		   $('input:hidden[name='+nameCb+']').val($(this).attr("checked"));
			
		});
		
		//les checkbox true/false sont reportées dans un champ hidden du meme nom
	   $('input:checkbox').click(function(){   
		   var nameCb = $(this).attr("name");
		   $('input:hidden[name='+nameCb+']').val($(this).attr("checked"));
			
		});
		
		
	   $("#selectEtatDossier").multiselect({
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
				
				$('#filterEtatId').val(csValueChecked);

				if(numChecked==numTotal){
					return "Tous";
				}
				if(nb==1){
					return csLabelLongChecked;
				}
				else{
					return csLabelCourtChecked;
				}
				
			},
			uncheckAll: function(){
				$('#filterEtatId').val('');
			}
			
		});
	   $("#selectEtatDossier").multiselect("checkAll");
	   
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
				
				$('#filterStatutId').val(csValueChecked);
				
				if(numChecked==numTotal){
					return "Tous";
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
			}
			
		});
	   $("#selectStatutDossier").multiselect("checkAll");
	   
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
					csLabelCourtChecked+=labelCourtCs;
					csLabelLongChecked+=labelLongCs;
					if (nb<numChecked) {
						csLabelCourtChecked +=", ";
						csLabelLongChecked+=", ";
						csValueChecked+=",";
					}
					
				});
				
				$('#filterActiviteId').val(csValueChecked);
				
				if(numChecked==numTotal){
					return "Tous";
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
			}
			
		});
	   $("#selectActiviteDossier").multiselect("checkAll");
	   

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

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
		<%-- tpl:insert attribute="zoneTitle" --%>
		<ct:FWLabel key="AL0002_TITRE" />
		<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
		<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
	<div id="AL0002">
		<a id="adminLink" onclick="doAction('/webavs/al?userAction=al.parametres.metier.modify', 'fr_main');" href="#" style="display:none;">params metier AF</a>
    	<table class="searchTab" id="searchTab">
        	<tr>
              	<td><ct:FWLabel key="AL0002_RECHERCHE_NOM"/></td>
				<td>
					<ct:inputText tabindex="1" name="searchModel.likeNomAllocataire" styleClass="normal" />

				</td>
                <td><ct:FWLabel key="AL0002_RECHERCHE_NUM_DOSSIER"/></td>
				<td>
					<ct:inputText tabindex="4" name="searchModel.forIdDossier" styleClass="normal" />
				</td>  
                <td><ct:FWLabel key="AL0002_RECHERCHE_NOM_ENFANT"/></td>
				<td>
					<ct:inputText tabindex="7" name="searchModel.likeNomEnfant" styleClass="normal" />
				</td>
				<td></td>
            </tr> 
           
            <tr>               
                <td><ct:FWLabel key="AL0002_RECHERCHE_PRENOM"/></td>
				<td>
					<ct:inputText tabindex="2" name="searchModel.likePrenomAllocataire" styleClass="normal" />
				</td>
                <td><ct:FWLabel key="AL0002_RECHERCHE_NOM_AFFILIE"/></td>
				<td>
					<ct:inputText tabindex="5" name="searchModel.likeNomAffilie" styleClass="normal" />
				</td>
                <td><ct:FWLabel key="AL0002_RECHERCHE_PRENOM_ENFANT"/></td>
				<td>
					<ct:inputText tabindex="8" name="searchModel.likePrenomEnfant" styleClass="normal" />
				</td>
				<td></td>
			
             </tr>
             <tr>
                <td><ct:FWLabel key="AL0002_RECHERCHE_NUM_NSS"/></td>
				<td>		
					<nss:nssPopup avsMinNbrDigit="2" nssMinNbrDigit="2" name="likeNssAllocataire" newnss="true" tabindex="3"/>
			
					<ct:inputHidden name="searchModel.likeNssAllocataire"/>	
					<ct:inputHidden name="searchModel.forIdAllocataire"/>	
				</td>
 				<td><ct:FWLabel key="AL0002_RECHERCHE_NUM_AFFILIE"/></td>
				<td>
					<input type="text" name="searchModel.forNumAffilie" class="normal" tabindex="6" data-g-numaffilieformatter="formatterClass:<%=formatterAffilie%>" />
				</td>
 				<td><ct:FWLabel key="AL0002_RECHERCHE_NUM_NSS_ENFANT"/>
 				</td>
				<td>
					<nss:nssPopup avsMinNbrDigit="2" nssMinNbrDigit="2" name="likeNssEnfant" newnss="true" tabindex="9"/>
					<ct:inputHidden name="searchModel.likeNssEnfant"/>
					<ct:inputHidden name="searchModel.forIdEnfant"/>					
				</td>
				<td></td>
             </tr>
             <tr>
             	<td colspan="2"></td>
             	<td colspan="2"></td>
             	<td><ct:FWLabel key="AL0002_RECHERCHE_NAISSANCE"/></td>
             	<td>
             		<ct:FWCalendarTag name="forDateEnfant" value=""
									doClientValidation="CALENDAR" tabindex="11"/>
					<ct:inputHidden name="searchModel.forDateEnfant" id="forDateEnfantValue"/>
					<script language="JavaScript">
						document.getElementsByName('forDateEnfant')[0].onblur=function(){fieldFormat(document.getElementsByName('forDateEnfant')[0],'CALENDAR');document.getElementById('forDateEnfantValue').value = this.value;};
								
						function theTmpReturnFunctionforDateEnfant(y,m,d) { 
								if (window.CalendarPopup_targetInput!=null) {
										var d = new Date(y,m-1,d,0,0,0);
										window.CalendarPopup_targetInput.value = formatDate(d,window.CalendarPopup_dateFormat);
										document.getElementById('forDateEnfantValue').value = document.getElementsByName('forDateEnfant')[0].value;		
								}else {
										alert('Use setReturnFunction() to define which function will get the clicked results!'); 
								}	
						}
						cal_forDateEnfant.setReturnFunction('theTmpReturnFunctionforDateEnfant');
					</script>	
             	
             		<input type="button" value="Clear" style="width:60px;" onclick="resetSearchFields();" accesskey="C" tabindex="11"/><ct:FWLabel key="AL0002_RECHERCHE_CLEAR"/>
             	</td>     	
             </tr>
      		<tr><td colspan="6"><hr/></td></tr>
        	<tr>
        		<td><ct:FWLabel key="AL0002_RECHERCHE_ETAT"/></td>
        		<td>
        		<select multiple id="selectEtatDossier">
        		<% 
					FWParametersSystemCodeManager cmEtatDossier = new FWParametersSystemCodeManager();
        			cmEtatDossier.setSession(objSession);
					cmEtatDossier.setForActif(true);
					cmEtatDossier.setForIdGroupe("ALDOSETAT");
					cmEtatDossier.setForIdLangue(objSession.getIdLangue());
					try {
						cmEtatDossier.find();
					} catch (Exception e) {
						e.printStackTrace();
					}
					for(int i=0;i<cmEtatDossier.size();i++){
						FWParametersCode code = (FWParametersCode) cmEtatDossier.getEntity(i);
						%><option value="<%=code.getIdCode()+"-"+JadeCodesSystemsUtil.getCode(objSession,code.getIdCode())%>"><%=JadeCodesSystemsUtil.getCode(objSession,code.getIdCode())+" - "+JadeCodesSystemsUtil.getCodeLibelle(objSession,code.getIdCode()) %></option><% 	
					}
				%>
				</select>
				<input type="hidden" id="filterEtatId" name="filterEtat"  value=""/>
	            </td>
        		<td><ct:FWLabel key="AL0002_RECHERCHE_STATUT"/></td>
        		<td>
        		<select multiple id="selectStatutDossier">
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
						%><option value="<%=code.getIdCode()+"-"+JadeCodesSystemsUtil.getCode(objSession,code.getIdCode())%>"><%=JadeCodesSystemsUtil.getCode(objSession,code.getIdCode())+" - "+JadeCodesSystemsUtil.getCodeLibelle(objSession,code.getIdCode()) %></option><% 	
					}
				%>
				</select>
				<input type="hidden" id="filterStatutId" name="filterStatut"  value=""/>
        		
        		</td>
        		<td><ct:FWLabel key="AL0002_RECHERCHE_ACTIVITE"/></td>
        		<td>
        		<select multiple id="selectActiviteDossier">
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
						%><option value="<%=code.getIdCode()+"-"+JadeCodesSystemsUtil.getCode(objSession,code.getIdCode())%>"><%=JadeCodesSystemsUtil.getCode(objSession,code.getIdCode())+" - "+JadeCodesSystemsUtil.getCodeLibelle(objSession,code.getIdCode()) %></option><% 	
					}
				%>
				</select>
				<input type="hidden" id="filterActiviteId" name="filterActivite"  value=""/>
        		
        		</td>
        	</tr>
        </table>   
	</div>
	</td>
</tr>
	 					<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>
<ct:menuChange displayId="options" menuId="empty-detail"/>				
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
