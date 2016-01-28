<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="ch.globaz.al.business.constantes.*"%>
<%@page import="globaz.al.vb.allocataire.ALAllocataireViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/detail/header.jspf" %>

<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALAllocataireViewBean viewBean = (ALAllocataireViewBean) session.getAttribute("viewBean"); 
	selectedIdValue=viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	
	idEcran="AL0006";
	//un nouvel allocataire sera tjrs lié un nouveau dossier
	actionNew+="&dossierIsNew=true";
	//on ne peut supprimer directement un allocataire, que en cascade
	bButtonDelete = false;
	
 	PaysSearchSimpleModel paysSearchModel = (PaysSearchSimpleModel) request.getAttribute("list_pays"); 
 	String prefixAllocataireModel = viewBean.isAgricoleContext()?"allocataireAgricoleComplexModel":"allocataireComplexModel";
 	
 	//Nom des champs (utile pour prefix du champ dynamique)
 	Map mapInputAllocName = new HashMap();
 
 	mapInputAllocName.put("personneEtendueComplexModel.tiers.idTiers",".personneEtendueComplexModel.tiers.idTiers");
 	mapInputAllocName.put("personneEtendueComplexModel.personne.idTiers",".personneEtendueComplexModel.personne.idTiers");
 	mapInputAllocName.put("personneEtendueComplexModel.personneEtendue.idTiers",".personneEtendueComplexModel.personneEtendue.idTiers");
 	mapInputAllocName.put("personneEtendueComplexModel.personneEtendue.spy",".personneEtendueComplexModel.personneEtendue.spy");
 	mapInputAllocName.put("personneEtendueComplexModel.personne.spy",".personneEtendueComplexModel.personne.spy");
 	mapInputAllocName.put("personneEtendueComplexModel.tiers.spy",".personneEtendueComplexModel.tiers.spy");
 	mapInputAllocName.put("personneEtendueComplexModel.personneEtendue.new",".personneEtendueComplexModel.personneEtendue.new");
 	mapInputAllocName.put("personneEtendueComplexModel.personne.new",".personneEtendueComplexModel.personne.new");
 	mapInputAllocName.put("personneEtendueComplexModel.tiers.new",".personneEtendueComplexModel.tiers.new");
 	mapInputAllocName.put("allocataireModel.idTiersAllocataire",".allocataireModel.idTiersAllocataire");
 	mapInputAllocName.put("personneEtendueComplexModel.personneEtendue.numAvsActuel",".personneEtendueComplexModel.personneEtendue.numAvsActuel");
 	mapInputAllocName.put("personneEtendueComplexModel.personne.sexe",".personneEtendueComplexModel.personne.sexe");
 	mapInputAllocName.put("personneEtendueComplexModel.tiers.langue",".personneEtendueComplexModel.tiers.langue");
 	mapInputAllocName.put("personneEtendueComplexModel.tiers.titreTiers",".personneEtendueComplexModel.tiers.titreTiers");
 	mapInputAllocName.put("personneEtendueComplexModel.personne.etatCivil",".personneEtendueComplexModel.personne.etatCivil");
 	mapInputAllocName.put("personneEtendueComplexModel.tiers.designation1",".personneEtendueComplexModel.tiers.designation1");
 	mapInputAllocName.put("personneEtendueComplexModel.personne.dateNaissance",".personneEtendueComplexModel.personne.dateNaissance");
 	mapInputAllocName.put("personneEtendueComplexModel.tiers.designation2",".personneEtendueComplexModel.tiers.designation2");
 	mapInputAllocName.put("personneEtendueComplexModel.tiers.idPays",".personneEtendueComplexModel.tiers.idPays");
 	mapInputAllocName.put("allocataireModel.idPaysResidence",".allocataireModel.idPaysResidence");
 	mapInputAllocName.put("allocataireModel.permis",".allocataireModel.permis");
 	mapInputAllocName.put("allocataireModel.cantonResidence",".allocataireModel.cantonResidence");
 	mapInputAllocName.put("allocataireModel.langueAffilie",".allocataireModel.langueAffilie");
 	
  	String idDossier = request.getParameter("idDossier");
  	if("null".equals(idDossier))
  		idDossier="";
%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="ch.globaz.pyxis.business.model.PaysSearchSimpleModel" %>
<%@page import="ch.globaz.pyxis.business.model.PaysSimpleModel"%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.helios.translation.CodeSystem"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/ajax_webaf.js"></script>
<script type="text/javascript" language="Javascript">

//variables nécessaires à la recherche ajax et variant selon la page...
searchAjaxInputId = "searchNssCriteria";
autoSuggestContainerId = "autoSuggestContainer";
prefixModel = '<%=JavascriptEncoder.getInstance().encode(prefixAllocataireModel)%>';

function add() {
    document.forms[0].elements('userAction').value="al.allocataire.allocataire.ajouter";
}
function upd() {
    document.forms[0].elements('userAction').value="al.allocataire.allocataire.modifier";
  	//Curseur par défaut sur le champ NSS ou titre si nss non éditable
	if(document.getElementById('partialforNumAvs')!=null)
		document.getElementById('partialforNumAvs').focus();
	else if(document.getElementById(prefixModel+'.personneEtendueComplexModel.tiers.titreTiers')){
		document.getElementById(prefixModel+'.personneEtendueComplexModel.tiers.titreTiers').focus();
	}
}
function validate() {
    state = validateFields();

    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.allocataire.allocataire.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.allocataire.allocataire.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		//on n'a pas l'idDossier si on est dans le contexte nouvel allocataire, car le dossier est aussi nouveau
		//donc, pour éviter que ce soit selectedId=null et donc prob
		document.forms[0].elements('selectedId').value="";
		document.forms[0].elements('userAction').value="al.dossier.dossier.chercher";
	} else {
        document.forms[0].elements('userAction').value="al.allocataire.allocataire.afficher";
	}
}

function del() {
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.allocataire.allocataire.supprimer";
        document.forms[0].submit();
    }
}

function init(){
	initDebugManager();
	if(document.forms[0].elements('_method').value!='add'){
		$("#AL0006tiersZone input").each(function(index) {
			$(this).attr("disabled", true);
			$(this).attr("readonly", true);
			$(this).css("readOnly");
			});
	
		$("#AL0006tiersZone select").each(function(index) {
			$(this).attr("disabled", true);
			$(this).attr("readonly", true);
			$(this).css("readOnly");
			});
	}
	//$("#AL0006tiersZone > input").each(function() {alert('1')};)
	//test sur le paramètre selectedIndex de la requête, si défini, alors ca signifie qu'on revient de la sélection du tiers
	isFromSelectionTiers = '<%=JavascriptEncoder.getInstance().encode(request.getParameter("selectedIndex")!=null?request.getParameter("selectedIndex"):"")%>';
	initNssInputComponent('<%=JavascriptEncoder.getInstance().encode(idEcran)%>','forNumAvs');

	if(isFromSelectionTiers!=''){	
		mlog('before synchroTiersAlloc');
		synchroTiersAjax();
	}
	
	//Si le pays de résidence change et que pas CH, on ne montre pas le canton de résidence
	var paysSuisse = '<%=JavascriptEncoder.getInstance().encode(ALCSPays.PAYS_SUISSE)%>';
	document.getElementById(prefixModel+'.allocataireModel.idPaysResidence').onchange = function(){
	
		if(this.value != paysSuisse)
			document.getElementById('cantonContainer').style.display='none';
		else
			document.getElementById('cantonContainer').style.display='block';

	};

	var titreMonsieur = '<%=JavascriptEncoder.getInstance().encode(ALCSAllocataire.TITRE_MONSIEUR)%>';
	document.getElementById(prefixModel+'.personneEtendueComplexModel.tiers.titreTiers').onclick = function(){
		mlog('value of titre : '+this.value);
		if(this.value != titreMonsieur && this.value!='')
			document.getElementById(prefixModel+'.personneEtendueComplexModel.personne.sexe').value='<%=JavascriptEncoder.getInstance().encode(ALCSTiers.SEXE_FEMME)%>';
		if(this.value == titreMonsieur)
				document.getElementById(prefixModel+'.personneEtendueComplexModel.personne.sexe').value='<%=JavascriptEncoder.getInstance().encode(ALCSTiers.SEXE_HOMME)%>';
		if(this.value == '')
					document.getElementById(prefixModel+'.personneEtendueComplexModel.personne.sexe').value='';
	
	};

}

function postInit(){

	
	$('input[name=cantonSaisie]').pilotSelect('#cantonSaisie-slave');

	//Curseur par défaut sur le champ NSS ou titre si nss non éditable
	
	if(document.getElementById('partialforNumAvs')!=null){
		
		document.getElementById('partialforNumAvs').focus();
	}
	else if(document.getElementById(prefixModel+'.personneEtendueComplexModel.tiers.titreTiers')){
	
		document.getElementById(prefixModel+'.personneEtendueComplexModel.tiers.titreTiers').focus();
	}

}



//définir cette méthode si traitement après remplissage ajax du formulaire
function callbackFillInputAjax(){

	mlog('callbackFillInputAjax - begin');
	var allocSync = false;
	var tiersSync = false;
	var idAllocataire = '';
	if(response.search.results.length==1){
	
		for(var i=0;i<response.search.results[0].properties.length;i++){
			if(response.search.results[0].properties[i].name==prefixModel+'.allocataireModel.idAllocataire'){
				idAllocataire = response.search.results[0].properties[i].value;
				allocSync=true;
				tiersSync=true;
				break;
			}
			
		}
	}
	
	if(!tiersSync && document.getElementById(prefixModel+'.personneEtendueComplexModel.tiers.idTiers').value!=''){
		tiersSync=true;
		
	}
		
	accordStatutLogoWithResponse(tiersSync);
	
	if(allocSync)
		document.location.href=url+"?userAction=al.dossier.dossierMain.afficher&_method=add&idAllocataire="+idAllocataire;

}

$(document).ready(function() {
	<%
	if (!(request.getParameter("domaineMontagne") == null)) {
		if (request.getParameter("domaineMontagne").equals("true")) {%>
			$("#domaineMontagne").val("true");
	<% } else { %>
			$("#domaineMontagne").val("false");
	<% } 
	}
	%>
});
</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
			<%=(viewBean.getAllocataireComplexModel().isNew())?objSession.getLabel("AL0006_TITRE_NEW"):objSession.getLabel("AL0006_TITRE")+viewBean.getAllocataireComplexModel().getId()%>
			<%=viewBean.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1() %>&nbsp;<%=viewBean.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2() %>
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<tr><td>
		
		<%-- tpl:insert attribute="zoneMain" --%>
              <table id="AL0006tiersZone" class="zone">
             	<tr>
                	<td class="label subtitle" colspan="6">
                	
                		<%if(viewBean.getAllocataireComplexModel().isNew()){ %>
                			<ct:FWLabel key="AL0006_TITRE_TIERS"/>
                			<div id="idTiers"></div>
                			<div><a class="syncLink" href="#" onclick="synchroTiersAjax();" title="<%=objSession.getLabel("LINK_SYNC_TIERS_DESC")%>"></a></div>
                			<div id="statutSynchroTiers" name="statutSync"></div>                			
                		<%}else{ %>
                			
                			<ct:FWLabel key="AL0006_TITRE_TIERS"/>
                			<div id="idTiers"> (<a href="<%=servletContext + "/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId="+viewBean.getAllocataireComplexModel().getPersonneEtendueComplexModel().getId()%>"><%=viewBean.getAllocataireComplexModel().getPersonneEtendueComplexModel().getId()%></a>)</div>
                			<div id="statutSynchroTiers"><img src="images/dialog-ok-apply.png" alt="Synchronisation réussie" width="16" height="16"/></div>
                			
                		<%}%>
                	
                		
                	</td>
                </tr>
            
                <!--  Champs nécessaire pour écriture dans les tiers (création / update) -->
                <ct:inputHidden name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.tiers.idTiers\")%>"/>
                <ct:inputHidden name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.personne.idTiers\")%>"/>
                <ct:inputHidden name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.personneEtendue.idTiers\")%>"/>
                <ct:inputHidden name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.personneEtendue.spy\")%>"/>
                <ct:inputHidden name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.personne.spy\")%>"/>
                <ct:inputHidden name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.tiers.spy\")%>"/>
                <ct:inputHidden name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.personneEtendue.new\")%>"/>
                <ct:inputHidden name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.personne.new\")%>"/>
                <ct:inputHidden name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.tiers.new\")%>"/>

                <!-- nécessaire pour updater allocataire existant récupéré -->
                <ct:inputHidden name="<%=prefixAllocataireModel+mapInputAllocName.get(\"allocataireModel.idTiersAllocataire\")%>"/>

                 
                <tr>
                    <td class="label"><ct:FWLabel key="AL0006_TIERS_NSS"/></td>
                    <td>
                    	<%if(viewBean.getAllocataireComplexModel().isNew()){ %>
                    		<nss:nssPopup avsMinNbrDigit="2" nssMinNbrDigit="2" name="forNumAvs" newnss="true" tabindex="1" />
                    		<ct:inputHidden name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.personneEtendue.numAvsActuel\")%>"/>
                
                    	
                    	<%}else{ %>
                    		<ct:inputText tabindex="1" name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.personneEtendue.numAvsActuel\")%>" 
                    		styleClass="nss readOnly" readonly="readonly" />
                    	<%}%>
						<% 
							Object[] tiersMethodsName = new Object[]{
									
									new String[]{prefixAllocataireModel+".personneEtendueComplexModel.personneEtendue.numAvsActuel","numAvsActuel"},
									
									/* On prend le NSS puis synchro sur le NSS car une seule méthode spy, toujours le même spy pour 3 tables
										différentes...donc prob lors d'update dans les tables dont on a pas pu récup le spy
									new String[]{"allocataireComplexModel.allocataireModel.idTiersAllocataire","idTiers"},
									new String[]{"allocataireComplexModel.personneEtendueComplexModel.tiers.idTiers","idTiers"},
									new String[]{"allocataireComplexModel.personneEtendueComplexModel.personne.idTiers","idTiers"},
									new String[]{"allocataireComplexModel.personneEtendueComplexModel.personneEtendue.idTiers","idTiers"},
								
									new String[]{"allocataireComplexModel.personneEtendueComplexModel.personne.sexe","sexe"},
									new String[]{"allocataireComplexModel.personneEtendueComplexModel.tiers.titreTiers","titre_tiers"},
									new String[]{"allocataireComplexModel.personneEtendueComplexModel.personne.etatCivil","etatCivil"},
									new String[]{"allocataireComplexModel.personneEtendueComplexModel.tiers.designation1","designation1_tiers"},
									new String[]{"allocataireComplexModel.personneEtendueComplexModel.tiers.designation2","designation2_tiers"},
									new String[]{"allocataireComplexModel.personneEtendueComplexModel.personne.dateNaissance","dateNaissance"},
									new String[]{"allocataireComplexModel.personneEtendueComplexModel.tiers.idPays","idPaysTiers"},
									new String[]{"allocataireComplexModel.personneEtendueComplexModel.tiers.langue","langue"},
									new String[]{"allocataireComplexModel.personneEtendueComplexModel.tiers.spy","spy"},
									new String[]{"allocataireComplexModel.personneEtendueComplexModel.personne.spy","spy"},
									new String[]{"allocataireComplexModel.personneEtendueComplexModel.personneEtendue.spy","spy"},
									*/
																
								};
						if(viewBean.getAllocataireComplexModel().isNew()){
						
						%> 
                    	<ct:FWSelectorTag name="tiersSelector"
								methods="<%=tiersMethodsName%>"
								providerApplication="pyxis" 
								providerPrefix="TI"
								providerAction="pyxis.tiers.tiers.chercher"
						/>
						<% } %>
                    	<div id="autoSuggestContainer" class="suggestList"></div>
                  	</td>
                    <td class="label"><ct:FWLabel key="AL0006_TIERS_SEXE"/></td>
                    <td>
		              	<ct:select wantBlank="true" tabindex="6" name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.personne.sexe\")%>" >
	                    		<ct:optionsCodesSystems csFamille="PYSEXE"></ct:optionsCodesSystems>	
	                    </ct:select> 
                    </td>
                   
                </tr>
               
                <tr>
                    <td class="label"><ct:FWLabel key="AL0006_TIERS_TITRE"/></td>
                    <td>   	
                    	<ct:select tabindex="2" name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.tiers.titreTiers\")%>" wantBlank="true">
	                    	<ct:option value="<%=ALCSAllocataire.TITRE_MONSIEUR %>" label="<%=objSession.getCodeLibelle(ALCSAllocataire.TITRE_MONSIEUR) %>">objSession.getCodeLibelle(ALCSAllocataire.TITRE_MONSIEUR)</ct:option>
	                    	<ct:option value="<%=ALCSAllocataire.TITRE_MADAME %>" label="<%=objSession.getCodeLibelle(ALCSAllocataire.TITRE_MADAME) %>">objSession.getCodeLibelle(ALCSAllocataire.TITRE_MADAME)</ct:option>
	                    	<ct:option value="<%=ALCSAllocataire.TITRE_MADEMOISELLE %>" label="<%=objSession.getCodeLibelle(ALCSAllocataire.TITRE_MADEMOISELLE) %>">objSession.getCodeLibelle(ALCSAllocataire.TITRE_MADEMOISELLE)</ct:option>
	                    </ct:select>
                    </td>
                    <td class="label"><ct:FWLabel key="AL0006_TIERS_ETATCIVIL"/></td>
                    <td>
 						<ct:select tabindex="7" name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.personne.etatCivil\")%>" defaultValue="<%=ALCSAllocataire.ETAT_CIVIL_MARIE%>">
	                    	<ct:optionsCodesSystems csFamille="PYETATCIVI"></ct:optionsCodesSystems>	
	                    </ct:select> 
                    </td>
                  
                </tr>
                <tr>
                    <td class="label"><ct:FWLabel key="AL0006_TIERS_NOM"/></td>
                    <td><ct:inputText tabindex="3" name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.tiers.designation1\")%>" styleClass="normal" /></td>
                    <td class="label"><ct:FWLabel key="AL0006_TIERS_NAISSANCE"/> </td>
                    <td>		
					<ct:FWCalendarTag name="dateNaissance" tabindex="8"
							value="<%=viewBean.getAllocataireComplexModel().getPersonneEtendueComplexModel().getPersonne().getDateNaissance()%>" 
							doClientValidation="CALENDAR"/>
					<ct:inputHidden name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.personne.dateNaissance\")%>" id="dateNaissanceValue"/>
					<script language="JavaScript">
							document.getElementsByName('dateNaissance')[0].onblur=function(){fieldFormat(document.getElementsByName('dateNaissance')[0],'CALENDAR');document.getElementById('dateNaissanceValue').value = this.value;};
									
							function theTmpReturnFunctiondateNaissance(y,m,d) { 
								if (window.CalendarPopup_targetInput!=null) {
									var d = new Date(y,m-1,d,0,0,0);
									window.CalendarPopup_targetInput.value = formatDate(d,window.CalendarPopup_dateFormat);
									document.getElementById('dateNaissanceValue').value = document.getElementsByName('dateNaissance')[0].value;		
								}else {
									alert('Use setReturnFunction() to define which function will get the clicked results!'); 
								}
							}
							cal_dateNaissance.setReturnFunction('theTmpReturnFunctiondateNaissance');
					</script>
					</td>
					
                </tr>
                <tr>
                    <td class="label"><ct:FWLabel key="AL0006_TIERS_PRENOM"/> </td>
                    <td>
                    	<ct:inputText tabindex="4" name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.tiers.designation2\")%>" styleClass="normal"/>
                  	</td>
                    <td class="label"><ct:FWLabel key="AL0006_TIERS_NATIONALITE"/> </td>
                    <td>
                   
  				<ct:select tabindex="9" name='<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.tiers.idPays\")%>' defaultValue="<%=viewBean.getAllocataireAgricoleComplexModel().getPersonneEtendueComplexModel().getTiers().getIdPays() %>">
					  	<%for(int i=0; i<paysSearchModel.getSize(); i++){ 
					  		PaysSimpleModel pays = (PaysSimpleModel) paysSearchModel.getSearchResults()[i];%>
					  		<ct:option value="<%=pays.getIdPays()%>" label="<%=pays.getLibelleFr()%>" ></ct:option>
					  	<%} %>
					  </ct:select>	
					 	
                    </td>
                    
                      <tr>
                	<td></td>
                	<td></td>
                	<td class="label"><ct:FWLabel key="AL0006_TIERS_LANGUAGE"/> </td>
                	<td><ct:select tabindex="10" name="<%=prefixAllocataireModel+mapInputAllocName.get(\"personneEtendueComplexModel.tiers.langue\")%>" defaultValue="<%=ALCSTiers.LANGUE_FRANCAIS%>">
                		<ct:optionsCodesSystems csFamille="PYLANGUE"></ct:optionsCodesSystems>
                		  </ct:select> 
                	</td>		
                </tr>
                    
                  
                </tr>
             
          
          </table>	
  
          <table id="AL0006allocataireZone" class="zone">

                <tr>
                	<td class="subtitle label" colspan="4">
                		<ct:FWLabel key="AL0006_TITRE_ALLOC"/>
                		<%
                			int limiteNbDossiers = -1; 
                			if(!JadeStringUtil.isEmpty(request.getParameter("dossierIsNew")) && "true".equals(request.getParameter("dossierIsNew"))){
                				limiteNbDossiers = 0;
                			}
                			else
                				limiteNbDossiers = 1;
                		%>
                		<div id="statutWarningAlloc"><%=viewBean.getNbDossiersActifs()>limiteNbDossiers?"<img src='images/dialog-warning.png' alt='"+objSession.getLabel("AL0006_ALLOC_WARNING_ACTIF")+"' width='16' height='16'/>":"" %></div>
                	</td>
                </tr>
                <tr>
                    <td class="label"><ct:FWLabel key="AL0006_ALLOC_PAYS"/></td>
                    <td >
		
					  <ct:select tabindex="10" name="<%=prefixAllocataireModel+mapInputAllocName.get(\"allocataireModel.idPaysResidence\")%>" defaultValue="<%=viewBean.getAllocataireComplexModel().getAllocataireModel().getIdPaysResidence()%>">
					  	<%for(int i=0; i<paysSearchModel.getSize(); i++){ 
					  		PaysSimpleModel pays = (PaysSimpleModel) paysSearchModel.getSearchResults()[i];%>
					  		<ct:option value="<%=pays.getIdPays()%>" label="<%=pays.getLibelleFr()%>"></ct:option>
					  	<%} %>
					  </ct:select>		                    	
                    </td>
                    <td class="label"><ct:FWLabel key="AL0006_ALLOC_PERMIS"/> </td>
                    <td>  	
                    	<ct:select tabindex="13" name="<%=prefixAllocataireModel+mapInputAllocName.get(\"allocataireModel.permis\")%>" wantBlank="true" defaultValue="">
	                    	<ct:optionsCodesSystems csFamille="ALALLPERM"></ct:optionsCodesSystems>	
	                    </ct:select>
                    </td>
                </tr>
        
                <tr id="cantonContainer" <%=ALCSPays.PAYS_SUISSE.equals(viewBean.getAllocataireComplexModel().getAllocataireModel().getIdPaysResidence())?"style='display:block'":"style='display:none'"%>>
                    <td class="label"><ct:FWLabel key="AL0006_ALLOC_CANTON"/></td>
                    <td>      	
                    	
                    	<ct:select tabindex="11" id="cantonSaisie-slave" name="<%=prefixAllocataireModel+mapInputAllocName.get(\"allocataireModel.cantonResidence\")%>" wantBlank="true">
	                    	<ct:optionsCodesSystems csFamille="ALCANTON"></ct:optionsCodesSystems>	
	                    </ct:select>    
          			</td>
          			
                    <td class="label">&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                
                <tr>
                	<td class="label"><ct:FWLabel key="AL0006_TIERS_LANGUE"/></td>
                 	<td>
	                    <ct:inputHidden name='<%=prefixAllocataireModel+mapInputAllocName.get(\"allocataireModel.langueAffilie\")%>' defaultValue="true"/>
	                    <% if(viewBean.getAllocataireComplexModel().isNew()||viewBean.getAllocataireComplexModel().getAllocataireModel().getLangueAffilie().booleanValue()){ %> 
	                    	<input type="checkbox"  checked="checked" name="langueAffilie"
	                    		onclick="reportCheckboxInModel('allocataireComplexModel.allocataireModel');"/>
	                    <%}else{%>
	                    	<input type="checkbox" name="langueAffilie"    		
	                    		   onclick="reportCheckboxInModel('allocataireComplexModel.allocataireModel');"/>
	                    <%}%>	
	                   
                  	</td>
                
                </tr>

           		<%if(viewBean.isAgricoleContext()){%>
                <tr <%if(viewBean.isPecheur()){%>style="display: none;"<%}%>>
                    <td class="label"><ct:FWLabel key="AL0006_ALLOC_DOMAINE_AGRI"/></td>
                    <td>

	                    <ct:select name="allocataireAgricoleComplexModel.agricoleModel.domaineMontagne" id="domaineMontagne" >
		                    	<ct:option value="" label=''/>	
		                    	<ct:option value="true" label='<%=objSession.getLabel("AL0006_ALLOC_DOMAINE_MONTAGNE") %>' />	
		                    	<ct:option value="false" label='<%=objSession.getLabel("AL0006_ALLOC_DOMAINE_PLAINE") %>' />
		                </ct:select> 
                    
                    </td>
                    
                </tr>
                <tr <%if(viewBean.isPecheur()){%>style="display: none;"<%}%>>
                    <td class="label"><ct:FWLabel key="AL0006_ALLOC_PROF_ACCESSOIRE"/></td>
                    <td>

	                   <ct:inputHidden name="dossierModel.professionAccessoire"/>
			                    <% if(!JadeStringUtil.isBlankOrZero(idDossier) && viewBean.getDossierModel(idDossier).getProfessionAccessoire()){ %> 
			                    	<input type="checkbox" checked="checked" name="professionAccessoire"
			                    		onclick="reportCheckboxInModel('dossierModel');"/>
			                    <%}else{%>
			                    	<input type="checkbox" name="professionAccessoire"          		
			                    		   onclick="reportCheckboxInModel('dossierModel');"/>
			                    <%}%>

                    </td>
                    
                </tr>
                <%} %>
             
          </table>
          
          <ct:inputHidden name="agricoleContext"/>
          <%-- Paramètres GET nécessaires au retour (après ajout,modif,...)--%>
          <ct:inputHidden name="idDossier" defaultValue="<%=idDossier%>"/>
   		  <ct:inputHidden name="typeActivite" defaultValue="<%=request.getParameter(\"typeActivite\") %>"/>
          <ct:inputHidden name="dossierIsNew" defaultValue="<%=request.getParameter(\"dossierIsNew\") %>"/>
          <ct:inputHidden name="fromPage" defaultValue="<%=request.getParameter(\"fromPage\") %>"/>
			<%-- /tpl:insert --%>			
</td></tr>											
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>	
<% 
   	//si on vient de la page de recherche personne, on affiche pas le même menu, car pas idDossier
if(!JadeStringUtil.isEmpty(request.getParameter("fromPage")) && "personneAF.chercher".equals(request.getParameter("fromPage"))){ %>
	<ct:menuChange displayId="options" menuId="allocataire-detail2" showTab="options" >			
		<ct:menuSetAllParams key="idAllocataire" value="<%=viewBean.getAllocataireComplexModel().getAllocataireModel().getId()%>"  />
		<ct:menuSetAllParams key="nomAllocataire" value="<%=viewBean.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1()%>"  />
		<ct:menuSetAllParams key="prenomAllocataire" value="<%=viewBean.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2()%>"  />		
	</ct:menuChange>

<%} else if(!viewBean.getAllocataireComplexModel().isNew()){ %>
<ct:menuChange displayId="options" menuId="allocataire-detail" showTab="options" checkAdd="no" >			
	<ct:menuSetAllParams checkAdd="no" key="idAllocataire" value="<%=viewBean.getAllocataireComplexModel().getAllocataireModel().getId()%>"  />
	<ct:menuSetAllParams key="nomAllocataire" value="<%=viewBean.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1()%>"  />
	<ct:menuSetAllParams key="prenomAllocataire" value="<%=viewBean.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2()%>"  />				
	<ct:menuSetAllParams checkAdd="no" key="typeActivite" value="<%=JadeStringUtil.isEmpty(request.getParameter(\"typeActivite\"))?\"\":request.getParameter(\"typeActivite\")%>"  />	
	<ct:menuSetAllParams checkAdd="no" key="domaineMontagne" value="<%=JadeStringUtil.isEmpty(request.getParameter(\"domaineMontagne\"))?\"\":request.getParameter(\"domaineMontagne\")%>"  />
	<% if(!JadeStringUtil.isEmpty(request.getParameter("dossierIsNew")) && "true".equals(request.getParameter("dossierIsNew"))){ %>
		<ct:menuSetAllParams checkAdd="no" key="_method" value="add"  />
		<ct:menuSetAllParams checkAdd="no" key="idDossier" value=""  />	
	<%}else{ %>
		<ct:menuSetAllParams checkAdd="no" key="_method" value="<%=JadeStringUtil.isEmpty(request.getParameter(\"_method\"))?\"\":request.getParameter(\"_method\")%>"  />	
		<ct:menuSetAllParams checkAdd="no" key="idDossier" value="<%=JadeStringUtil.isEmpty(request.getParameter(\"idDossier\"))?\"\":request.getParameter(\"idDossier\")%>"  />	
	<%} %>
</ct:menuChange>
<% }else{ %>
<ct:menuChange displayId="options" menuId="allocataire-detail" showTab="options" checkAdd="no" >			
	<ct:menuSetAllParams checkAdd="no" key="idAllocataire" value="<%=viewBean.getAllocataireComplexModel().getAllocataireModel().getId()%>"  />		
	<ct:menuSetAllParams checkAdd="no" key="_method" value="add"  />	
	<ct:menuSetAllParams checkAdd="no" key="idDossier" value=""  />
	<ct:menuSetAllParams checkAdd="no" key="typeActivite" value="<%=JadeStringUtil.isEmpty(request.getParameter(\"typeActivite\"))?\"\":request.getParameter(\"typeActivite\")%>"  />		
	<ct:menuSetAllParams checkAdd="no" key="domaineMontagne" value="<%=JadeStringUtil.isEmpty(request.getParameter(\"domaineMontagne\"))?\"\":request.getParameter(\"domaineMontagne\")%>"  />
</ct:menuChange>	
<%} %>

<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
