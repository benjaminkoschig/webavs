<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.prestation.interfaces.util.nss.PRUtil"%>
<%@page import="globaz.naos.util.AFIDEUtil"%>
<%@page import="globaz.naos.translation.CodeSystem"%>
<%@page import="globaz.naos.db.affiliation.AFAffiliation"%>
<%@page import="globaz.naos.util.AFUtil"%>
<%@page import="globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation"%>
<%@page import="ch.globaz.vulpecula.web.views.registre.ConventionViewService"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	idEcran = "CAF0003";
	globaz.naos.db.affiliation.AFAffiliationViewBean viewBean = (globaz.naos.db.affiliation.AFAffiliationViewBean)session.getAttribute ("viewBean");	
	key="globaz.naos.db.affiliation.AFAffiliationViewBean-affiliationId"+viewBean.getAffiliationId();
	String method = request.getParameter("_method");
	String jspLocation = servletContext + mainServletPath + "Root/ide_select.jsp";
	
	Vector listeConventions = ConventionViewService.getAllConventions();	
	
	String langue = PRUtil.getISOLangueTiers(viewBean.getSession().getIdLangueISO()).toLowerCase();
	String linkNoga =  viewBean.getSession().getApplication().getProperty("exploitation.codeNOGA.URL") + "/Default?lang=" + langue + "-CH";	
%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="JavaScript">
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%
//date début décompte
String libDateDebutReelle = viewBean.getDebutAffiliationReelle();
if(!JadeStringUtil.isEmpty(libDateDebutReelle)) {
	libDateDebutReelle = "<img style=\"margin-left: 100px;vertical-align: middle\" src=\""+request.getContextPath()+"/images/avertissement.gif\" border=\"0\" alt=\"Date anticipée pour décomptes avant affiliation au "+libDateDebutReelle+"\">";
} else {
	libDateDebutReelle = "";
}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 

<SCRIPT language="JavaScript">
var activeCheckTypeAff = false;
var confirmOnce = true;
function add() {
	if ("<%=request.getParameter("_valid")%>" != "fail") {
		<% if (viewBean.getAffilieNumero()==null || viewBean.getAffilieNumero().length() == 0) { %>
			document.forms[0].elements('affilieNumero').value= "<%=viewBean.getNouveauAffilieNumero()%>";
		<% } %>
		//document.forms[0].elements('affilieNumero').value= "<%=viewBean.getAffilieNumero()%>"
		document.forms[0].elements('typeAffiliation').value= "<%=viewBean.getTypeAffiliation()%>"
		document.forms[0].elements('personnaliteJuridique').value= "<%=globaz.naos.translation.CodeSystem.PERS_JURIDIQUE_NA%>"
		document.forms[0].elements('dateDebut').value= "<%=viewBean.getDateDebut()%>"
		document.forms[0].elements('motifCreation').value= "<%=globaz.naos.translation.CodeSystem.MOTIF_AFFIL_NOUVELLE_AFFILIATION%>"
	}
	document.forms[0].elements('userAction').value="naos.affiliation.affiliation.ajouter"
	activeCheckTypeAff = true;
	checkTypeAff();
}

function upd() {	
	$("#lienRegistreIde").hide();
}
	

function updateChampIde(tag){
	
	if(tag.select && tag.select.selectedIndex != -1){ 
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("numeroIDESansCHE").value = element.numeroIdeFormatedWithoutPrefix;
		document.getElementById("numeroIDE").value = element.numeroIdeUnformatedWithPrefix;
		
		document.getElementById("ideRaisonSociale").value = element.raisonSociale;
		document.getElementById("ideStatut").value = element.ideStatut;
		document.getElementById("libelleStatutIde").value = element.libelleStatutIde;
		var compareTo ='<%=viewBean.getNumeroIDE()%>';
		if(compareTo==element.numeroIdeUnformatedWithPrefix){
			displayFieldIDEPartage();
		}else{
			if(element.isIdeAllreadyUsed == "true"){
				document.getElementById("isIdePartage").style.display = "";
			}else {
				document.getElementById("isIdePartage").style.display = "none";
			}
		}
		document.getElementById("categorieNoga").value = element.categorieNoga;
 		document.getElementById("categorieNogaCode").value = element.categorieNoga;
 		rebuildNoga(element.csCodeNoga);
		
	}else{
		document.getElementById("numeroIDE").value = '';
		document.getElementById("ideRaisonSociale").value = '';
		document.getElementById("ideStatut").value = '';
		document.getElementById("libelleStatutIde").value = '';
		document.getElementById("isIdePartage").style.display = "none";
}
	displayFieldIDEPassif();
}

function validate() {
	
	// si attendre sur popup?
	if(confirmerMutationActivite()){
		return ;
	}
	
	if(document.forms[0].elements('affilieNumero').value != '<%= viewBean.getAffilieNumero()%>' && '<%= viewBean.getAffilieNumero()%>'!='' ) 
	{
		if(!window.confirm("Vous êtes sur le point de modifier le numéro d'un affilié ! Voulez-vous continuer?"))
		{
			return ;
		}
	}
	
	var exit = true;
    state = validateFields();

	document.forms[0].elements('userAction').value="naos.affiliation.affiliation.modifier";
	 if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.affiliation.affiliation.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.affiliation.affiliation.modifier";
	
	
	return (exit && state);
}

function cancel() {
 if (document.forms[0].elements('_method').value == "add")
 	document.forms[0].elements('userAction').value="back";
 else
	document.forms[0].elements('userAction').value="naos.affiliation.affiliation.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer le détail de la période d'affiliation sélectionnée! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="naos.affiliation.affiliation.supprimer";
		document.forms[0].submit();
	}
}
function updateGenreAff (data){
	checkTypeAff();
	displayFieldCodeSuva();
	displayFieldIDEPassif();
	oldData = <%=viewBean.getTypeAffiliation()%>;
	if(data==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_PROVIS%>){
		document.forms[0].elements('traitement').checked = true;
	}
	
	if(oldData==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_PROVIS%> && data != <%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_PROVIS%>){
		document.forms[0].elements('traitement').checked = false;
	}
}

function confirmerMutationActivite(){
	if(confirmOnce){
	//D0181
	// n'est pas une création	
	if (document.forms[0].elements('_method').value != "add"){
		// si l'activité a changé
		//ignorer les retour charriot et single quote qui feraient peter le javascript de la page
		if(document.forms[0].elements('activite').value.replace(/\r|\n|'/gi,'') != '<%=viewBean.getActivite().replaceAll("(\\r|\\n|\')", "")%>' && document.forms[0].elements('activite').value != "" ) 
		{
			if(document.forms[0].elements('numeroIDESansCHE').value != '' && !document.getElementById('ideAnnoncePassive').checked &&
					document.forms[0].elements('raisonSociale').value.replace(/\r|\n|'/gi,'') == '<%=viewBean.getRaisonSociale().replaceAll("(\\r|\\n|\')", "")%>' &&
					document.forms[0].elements('typeAffiliation').value == '<%=viewBean.getTypeAffiliation()%>' && 
					document.forms[0].elements('brancheEconomique').value == '<%=viewBean.getBrancheEconomique()%>'){
				
			
			$( "#dialog_confirm_mutation_activite" ).dialog({
			      resizable: false,
			      height:140,
			      modal: true,
			      buttons: {

			      "<ct:FWLabel key='NAOS_DIALOG_LINK_OR_CREATE_ANNONCE_BOUTON_OUI'/>": function() {
			    	  document.forms[0].elements('confirmerAnnonceActivite').value=true;
			          $( this ).dialog( "close" );
			          if(validate()) action(COMMIT);
			        },

			        "<ct:FWLabel key='NAOS_DIALOG_LINK_OR_CREATE_ANNONCE_BOUTON_NON'/>": function() {
			        	document.forms[0].elements('confirmerAnnonceActivite').value=false;
				        $( this ).dialog( "close" );
				        if(validate()) action(COMMIT);
			        }
			      }
			    });
			
			//avoid validate popup twice
			confirmOnce = false;
			
			return true;
			}
		}
	}
	}
	return false;
}

function checkEnvoiAutomatique() {

var type = document.getElementById("typeAffiliation");
var occa = document.getElementById("occasionnel");


if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP%>) {
setCtrl("envoiAutomatiqueLAA",false);
setCtrl("envoiAutomatiqueLPP",false);
}
if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP_EMPLOY%> &&occa.checked==false) {
setCtrl("envoiAutomatiqueLAA",true);
setCtrl("envoiAutomatiqueLPP",true);
}
if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP_EMPLOY%> &&occa.checked==true) {
setCtrl("envoiAutomatiqueLAA",false);
setCtrl("envoiAutomatiqueLPP",false);
}
if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_EMPLOY%> &&occa.checked==false) {
setCtrl("envoiAutomatiqueLAA",true);
setCtrl("envoiAutomatiqueLPP",true);
}
if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_EMPLOY%> &&occa.checked==true){
setCtrl("envoiAutomatiqueLAA",false);
setCtrl("envoiAutomatiqueLPP",false);
}
if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_NON_ACTIF%>) {
setCtrl("envoiAutomatiqueLAA",false);
setCtrl("envoiAutomatiqueLPP",false);
}
if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_TSE%>) {
setCtrl("envoiAutomatiqueLAA",false);
setCtrl("envoiAutomatiqueLPP",false);
}
if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_FICHIER_CENT%>) {
setCtrl("envoiAutomatiqueLAA",true);
setCtrl("envoiAutomatiqueLPP",false);
}
if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_BENEF_AF%>) {
setCtrl("envoiAutomatiqueLAA",false);
setCtrl("envoiAutomatiqueLPP",false);
}
if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_NON_SOUMIS%>) {
setCtrl("envoiAutomatiqueLAA",false);
setCtrl("envoiAutomatiqueLPP",false);
}
if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_LTN%>) {
//setCtrl("envoiAutomatiqueLAA",false);
	setCtrl("envoiAutomatiqueLPP",false);
	}
}

function setCtrl(ctrl, state) {
 elem = document.getElementById(ctrl);
 if(elem!=null) {
 	elem.checked=state;
 }
}
 
 
 
var messageAnnonceCreationIdeAjout = "<ct:FWLabel key='NAOS_JSP_AFFILIATION_MESSAGE_ANNONCE_CREATION_IDE_A_AJOUTER'/>";
var messageAnnonceCreationIdeLier = "<ct:FWLabel key='NAOS_JSP_AFFILIATION_MESSAGE_ANNONCE_CREATION_IDE_A_LIER'/>";
var idAnnonceCrationEnCoursForTiers = '<%= viewBean.giveMeFirstIdAnnonceCreationEnCoursForIdTiers()%>';
 
function init(){
	
	<%
	
	if(viewBean.isAnnonceIdeCreationToAdd() && !JadeStringUtil.isBlankOrZero(viewBean.giveMeFirstIdAnnonceCreationEnCoursForIdTiers())){
	%>
	    
	  $( "#dialog_link_or_create_annonce" ).dialog({

	      resizable: false,

	      height:140,

	      modal: true,

	      buttons: {

	      "<ct:FWLabel key='NAOS_DIALOG_LINK_OR_CREATE_ANNONCE_BOUTON_OUI'/>": function() {
	    	  $("#textInfoCreationAnnonce").append(messageAnnonceCreationIdeLier);
		      $("#idAnnonceIdeCreationLiee").val(idAnnonceCrationEnCoursForTiers);
	          $( this ).dialog( "close" );

	        },

	        "<ct:FWLabel key='NAOS_DIALOG_LINK_OR_CREATE_ANNONCE_BOUTON_NON'/>": function() {
	        $("#textInfoCreationAnnonce").append(messageAnnonceCreationIdeAjout);
		    $("#idAnnonceIdeCreationLiee").val("");
	        $( this ).dialog( "close" );

	        }
	      }
	    });

	<%} else{%>
		$("#textInfoCreationAnnonce").append(messageAnnonceCreationIdeAjout);
	<%}%>
	    
	
	<%
		String typeAff = "0";
		if (! globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getTypeAffiliation())) {
			typeAff = viewBean.getTypeAffiliation();
		}
	%>
	var type = <%=typeAff%>;
	if(type == <%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP%>) {
		document.all('nonTypeAssocieDisplay').style.display='none';
		document.all('typeAssocieDisplay').style.display='block';	
	} else {
		document.getElementById("typeAssocie").value = "";
		document.all('typeAssocieDisplay').style.display='none';
		document.all('nonTypeAssocieDisplay').style.display='block';
	}
	
	resetEAAVisibility();
	displayFieldCodeSuva();
	displayFieldIDEPassif();
	displayFieldIDEPartage();
	
	
	<%if(!JadeStringUtil.isBlankOrZero(viewBean.getWarningMessageAnnonceIdeCreationNotAdded())){%>
		globazNotation.utils.consoleWarn("<%=viewBean.getWarningMessageAnnonceIdeCreationNotAdded()%>",'Avertissement',true);
		<%viewBean.setWarningMessageAnnonceIdeCreationNotAdded("");%>
	<%}%>
	
}
function showPartie1() {
	document.all('tPartie2').style.display='none';
	document.all('tPartie1').style.display='block';
}

function showPartie2() {
	document.all('tPartie1').style.display='none';
	document.all('tPartie2').style.display='block';
	//var focusElement = document.forms[0].membreAssociation;
	//if (focusElement.disabled == false) {
	//	document.forms[0].membreAssociation.focus();
	//}
}
function checkTypeAff() {
	if (activeCheckTypeAff) {
		var type = document.getElementById("typeAffiliation");
		if(type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP%>) {
			document.all('nonTypeAssocieDisplay').style.display='none';
			document.all('typeAssocieDisplay').style.display='block';
			document.all('periodicite').value = <%=globaz.naos.translation.CodeSystem.PERIODICITE_TRIMESTRIELLE%>;
			document.all('personnaliteJuridique').value = <%=globaz.naos.translation.CodeSystem.PERS_JURIDIQUE_RAISON_INDIVIDUELLE%>;
			document.all('declarationSalaire').value = "";
		} else {
			document.getElementById("typeAssocie").value = "";
			document.all('typeAssocieDisplay').style.display='none';
			document.all('nonTypeAssocieDisplay').style.display='block';
			if(type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_EMPLOY%>) {
				document.all('periodicite').value = <%=globaz.naos.translation.CodeSystem.PERIODICITE_MENSUELLE%>;
				document.all('personnaliteJuridique').value = <%=globaz.naos.translation.CodeSystem.PERS_JURIDIQUE_SA%>;
				document.all('declarationSalaire').value = <%=globaz.naos.translation.CodeSystem.DECL_SAL_PRE_LISTE%>;
			} else if(type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP_EMPLOY%>) {
				document.all('periodicite').value = <%=globaz.naos.translation.CodeSystem.PERIODICITE_TRIMESTRIELLE%>;
				document.all('personnaliteJuridique').value = <%=globaz.naos.translation.CodeSystem.PERS_JURIDIQUE_RAISON_INDIVIDUELLE%>;
				document.all('declarationSalaire').value = <%=globaz.naos.translation.CodeSystem.DECL_SAL_PRE_LISTE%>;
			} else if(type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_NON_ACTIF%>) {
				document.all('periodicite').value = <%=globaz.naos.translation.CodeSystem.PERIODICITE_TRIMESTRIELLE%>;
				document.all('personnaliteJuridique').value = <%=globaz.naos.translation.CodeSystem.PERS_JURIDIQUE_NA%>;
				document.all('declarationSalaire').value = "";
			} else if(type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_NON_SOUMIS%>) {
				document.all('periodicite').value = "";
				document.all('personnaliteJuridique').value = "";
				document.all('declarationSalaire').value = "";
			}else if(type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_LTN%>) {
				document.all('periodicite').value = <%=globaz.naos.translation.CodeSystem.PERIODICITE_ANNUELLE%>;
				document.all('personnaliteJuridique').value = <%=globaz.naos.translation.CodeSystem.PERS_JURIDIQUE_RAISON_INDIVIDUELLE%>;
				document.all('declarationSalaire').value = <%=globaz.naos.translation.CodeSystem.DECL_SAL_PRE_LISTE%>; 
			}else {
				document.all('declarationSalaire').value = '';
			}
		}
		checkEnvoiAutomatique();
	}
}

function displayFieldCodeSuva() {
	var type = document.getElementById("typeAffiliation");
	if(type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_EMPLOY%> 
		|| type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP_EMPLOY%>
		|| type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_LTN%>
		|| type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_EMPLOY_D_F%>) {
		document.all('displayCodeSuva').style.display='block';
	} else {
		document.all('displayCodeSuva').style.display='none';
	}
}

function messageSiPassif() {

	if (document.getElementById('ideAnnoncePassive').checked) {
	document.getElementById('messageAnnoncePassive').style.visibility="visible";
	}
	else {
	document.getElementById('messageAnnoncePassive').style.visibility="hidden";
	}
}

function displayFieldIDEPassif() {
	var typeListeActif = <%=AFIDEUtil.getListGenreAffilieActif()%>;
	var typeListePassif = <%=AFIDEUtil.getListGenreAffiliePassif()%>;
	if (<%=!AFIDEUtil.hasAffiliationCotisationAVSOuverte(viewBean.getSession(), viewBean.getAffiliationId())%> 
		&& '' != document.getElementById('numeroIDESansCHE').value
		&& ( contains(typeListeActif, document.getElementById('typeAffiliation').options[document.getElementById('typeAffiliation').selectedIndex].value) 
				|| contains(typeListePassif, document.getElementById('typeAffiliation').options[document.getElementById('typeAffiliation').selectedIndex].value))) {
		document.all('displayIDEAnnoncePassive').disabled='';
	} else {
		document.getElementById('ideAnnoncePassive').checked=false;
		document.all('displayIDEAnnoncePassive').disabled='disabled';
	}
	messageSiPassif();
}
function displayFieldIDEPartage() {
	if(<%=viewBean.isIDEPartage()%>){
		document.getElementById("isIdePartage").style.display = "";
	}else {
		document.getElementById("isIdePartage").style.display = "none";
	}
}
function contains(a, obj) {
    for (var i = 0; i < a.length; i++) {
        if (a[i] == obj) {
            return true;
        }
    }
    return false;
}

function resetEAAVisibility() {
	if (document.forms[0].elements('releveParitaire').checked ) {
		document.all('eaa').style.display = "block";
	} else {
		document.all('eaa').style.display = "none";
		document.forms[0].elements('envoiAutomatiqueAnnonceSalaires').checked = false;
	}
}
function compteaLaCarte() {
	if (document.forms[0].elements('relevePersonnel').checked) {
	
	document.all('periodicite').value = <%=globaz.naos.translation.CodeSystem.PERIODICITE_MENSUELLE%>;	
	document.all('periodicite').disabled = true;
	
	} else {
	document.all('periodicite').disabled = false;
	}
}

var optionsList = new Array();
<% globaz.globall.parameters.FWParametersSystemCodeManager mgr = viewBean.getCSNoga(); 
	for(int i=0;i<mgr.size();i++) {
		globaz.globall.parameters.FWParametersSystemCode code = (globaz.globall.parameters.FWParametersSystemCode) mgr.getEntity(i);  %>
		optionsList[<%=i%>] = { name: "<%=code.getIdCode()%>", text: "<%=code.getCurrentCodeUtilisateur().getCodeUtilisateur()+ " - " + code.getCurrentCodeUtilisateur().getLibelle()%>", link: "<%= code.getIdSelection()%>" };	
<%  } %>

function rebuildNoga(idCode) {

	// effacer le code noga
	var oSelect = document.getElementById("codeNoga");
	while(oSelect.length != 0){
		oSelect.remove(oSelect.children[0]);
	}
	var oOption = document.createElement("OPTION");
	oOption.text="";
	oOption.value="";
	oSelect.add(oOption);
	// reconstruire les champs
	var categorie = document.getElementById("categorieNoga");
	var categorieCode = document.getElementById("categorieNogaCode");
	var categorieSelected = 0;
	if(categorie.style.display=='inline') {
		categorieSelected = categorie.selectedIndex;
	} else {
		categorieSelected = categorieCode.selectedIndex;
	}
	if((idCode!=null&&idCode!=0) || categorieSelected != 0 ) {
		// sans libellé
		categorieCode.style.display='inline';
		categorie.style.display='none';
		categorieCode.selectedIndex = categorieSelected;
	} else {	
		categorie.style.display='inline';
		categorieCode.style.display='none';
		categorie.selectedIndex = categorieSelected;
	}
	//redesign
	categorie.style.width='40px';
	oSelect.style.width='350px';
	var motifFin = document.getElementById("motifFin");
	motifFin.style.width='300px';
	
	for(i=0;i<optionsList.length;i++) {
		if(optionsList[i].link==categorie.options[categorieSelected].value) {
			var oOption = document.createElement("OPTION");
			oOption.text=optionsList[i].text;
			oOption.value=optionsList[i].name;
			oSelect.add(oOption);
		}
	}
	if(idCode!=null) {
		for(i=0;i<oSelect.length;i++) {
			if(oSelect.options[i].value==idCode) {
				oSelect.options[i].selected = true;
			}
		}
	}
	if(categorieSelected != 0) {
		oSelect.focus();
	} else {
		categorie.focus();
	}
}

function maxLength(zone,max)
{
	if(zone.value.length>=max){
		zone.value=zone.value.substring(0,max);
	}
}
</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<span class="postItIcon"><ct:FWNote sourceId="<%=viewBean.getAffiliationId()%>" tableSource="globaz.naos.db.affiliation.AFAffiliationViewBean"/></span>
			Affiliation - D&eacute;tail 
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						
						<div style="display:none" id="dialog_link_or_create_annonce" title="<ct:FWLabel key='NAOS_DIALOG_LINK_OR_CREATE_ANNONCE_TITRE'/>">
  							 <p>
  							 	<ct:FWLabel key="NAOS_DIALOG_LINK_OR_CREATE_ANNONCE_TEXTE"/>
  							</p>
						</div>
						<div style="display:none" id="dialog_confirm_mutation_activite" title="<ct:FWLabel key='NAOS_DIALOG_CONFIRM_MUTATION_ACTIVITE_TITRE'/>">
  							 <p>
  							 	<ct:FWLabel key="NAOS_DIALOG_CONFIRM_MUTATION_ACTIVITE_TEXTE"/>
  							</p>
						</div>
						
						<INPUT type="hidden" id="idAnnonceIdeCreationLiee" name="idAnnonceIdeCreationLiee" value="<%=viewBean.getIdAnnonceIdeCreationLiee()%>">
						
						<TR> 
						<TD> 
						<TABLE border="0" cellspacing="0" cellpadding="0">
						<TBODY> 
							<TR> 
								<TD nowrap width="161"><A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdTiers()%>" class="external_link">Tiers</A>
								</TD>
								<TD nowrap colspan="3"> 
									<INPUT type="hidden" name="selectedId" value="<%=viewBean.getAffiliationId()%>">
									<% if(viewBean.getTiers().idTiersExterneFormate().length()!=0) { %>
									<input type="text" name="idExterne" size="16" maxlength="16" value="<%=viewBean.getTiers().idTiersExterneFormate()%>" readOnly tabindex="-1" class="disabled">
									<% } %>
		            				<input type="text" name="nom" size="60" maxlength="60" value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getTiers().getNom(),"\"","&quot;")%>" readOnly tabindex="-1" class="disabled">
		            			</TD>
		            		</TR>
							<TR> 
								<TD nowrap  height="11" colspan="4"> 
									<hr size="3" width="100%">
								</TD>
							</TR>
									<%  String designationCourt = "";
										String designationLong = "";
										if (method != null && method.equals("add")) {
											if(viewBean.getTiers().getNom().length()>100) {
												designationLong = viewBean.getTiers().getNom().substring(0,100);
												designationCourt = viewBean.getTiers().getNom().substring(0,30);
											} else {
												designationLong = viewBean.getTiers().getNom();
											 	if(viewBean.getTiers().getNom().length()>30) {
													designationCourt = viewBean.getTiers().getNom().substring(0,30);
												} else {
													designationCourt = viewBean.getTiers().getNom();
												}
											}
										} else {
											designationCourt = viewBean.getRaisonSocialeCourt();
											designationLong = viewBean.getRaisonSociale();
										}
									%>
							<TR>
								<TD>Raison sociale (long)</TD>
								<TD colspan="3"><input type="text" id="raisonSociale" name="raisonSociale" size="100" maxlength="100" value="<%=globaz.jade.client.util.JadeStringUtil.change(designationLong,"\"","&quot;")%>" tabindex="-1" ></TD> 
							</TR>

							<TR> 
								<TD nowrap>N&deg; d'affili&eacute;</TD>
								<TD nowrap> 
									<INPUT name="affilieNumero" size="20" maxlength="20" type="text" value="<%=viewBean.getAffilieNumero()%>">
								</TD>
								<TD nowrap>Ancien n&deg; d'affili&eacute;</TD>
								<TD nowrap>
									<INPUT name="ancienAffilieNumero" type="text" size="20" maxlength="20" value="<%=viewBean.getAncienAffilieNumero()%>">
								</TD>
							</TR>
							<TR>
								<TD nowrap height="31"  width="161">P&eacute;riode<%=libDateDebutReelle%></TD>
								<TD nowrap>
									<ct:FWCalendarTag name="dateDebut" doClientValidation="CALENDAR" value="<%=viewBean.getDateDebut()%>" /> 
									&agrave;
									<ct:FWCalendarTag name="dateFin" doClientValidation="CALENDAR" value="<%=viewBean.getDateFin()%>" /> 
								</TD>
								<TD nowrap>Genre d'affiliation</TD>
								<TD nowrap>
									<ct:FWCodeSelectTag 
		                				name="typeAffiliation" 
										defaut="<%=viewBean.getTypeAffiliation()%>"
										codeType="VETYPEAFFI"/> 
										<script>
											document.getElementById("typeAffiliation").onchange = new Function("","return updateGenreAff(document.getElementById('typeAffiliation').options[document.getElementById('typeAffiliation').selectedIndex].value);");
											
										</script>
								</TD>
							</TR>
							
							<TR> 
								<TD nowrap  height="11" colspan="4"> 
									<hr size="3" width="100%">
								</TD>
							</TR>
						</TBODY> 
						</TABLE>
						<TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie1">
						<TBODY>
							<TR> 
								<TD nowrap height="31"  width="161">Raison sociale (court)</TD>
								<TD nowrap colspan="2">
									<input type="text" name="raisonSocialeCourt" size="30" maxlength="30" value="<%=globaz.jade.client.util.JadeStringUtil.change(designationCourt,"\"","&quot;")%>">
								</TD>
								<TD nowrap>Demande affiliation</TD>
								<TD nowrap>
									<ct:FWCalendarTag name="dateDemandeAffiliation" value="<%=viewBean.getDateDemandeAffiliation()%>" /> 									
								</TD>
							</TR>
							<TR> 
								<TD nowrap height="31"  width="161">Motif de cr&eacute;ation</TD>
								<TD nowrap colspan="2">
									<ct:FWCodeSelectTag name="motifCreation" defaut="<%=viewBean.getMotifCreation()%>" codeType="VEMOTIFAFF" />
								</TD>
								<TD nowrap>Motif de fin</TD>
								<TD nowrap>
									<ct:FWCodeSelectTag 
		                				name="motifFin" 
										defaut="<%=viewBean.getMotifFin()%>"
										codeType="VEMOTIFFIN"
										wantBlank="true"
										except="<%=viewBean.getExceptMotifFin()%>"/> 									
								</TD>
							</TR>
							<TR> 
								<TD nowrap height="31" width="161">Personnalit&eacute; juridique</TD>
								<TD nowrap colspan="2">
									<ct:FWCodeSelectTag 
		                				name="personnaliteJuridique"
		                				libelle="both" 
										defaut="<%=viewBean.getPersonnaliteJuridique()%>"
										codeType="VEPERSONNA"
										wantBlank="true"/> 									
										<script>
											document.getElementById("personnaliteJuridique").onchange = new Function("","return displayFieldIDEPassif();");
										</script> 									
								</TD>
								<TD nowrap>Type d'associé</TD>
								<TD nowrap> 
									<TABLE border="0" cellspacing="0" cellpadding="0" id="typeAssocieDisplay">
									<TBODY>
									<TR><TD>
										<ct:FWCodeSelectTag 
			                				name="typeAssocie"
											defaut="<%=viewBean.getTypeAssocie()%>"
											codeType="VETYPEASSO"
											wantBlank="true"/> 									
	
									</TD></TR>
									</TBODY> 
									</TABLE>
									<TABLE border="0" cellspacing="0" cellpadding="0" id="nonTypeAssocieDisplay">
									<TBODY>
									<TR><TD>
										<INPUT type="text" name="nontypeAssocie" size="17" tabindex="-1" class="disabled" readOnly>
									</TD></TR>
									</TBODY> 
									</TABLE>
								</TD>
							</TR>
							<TR> 
								<TD nowrap width="161" height="31">P&eacute;riodicit&eacute;</TD>
								<TD nowrap colspan="2">
									<ct:FWCodeSelectTag 
		                				name="periodicite" 
										defaut="<%=viewBean.getPeriodicite()%>"
										codeType="VEPERIODIC"
										wantBlank="true"/>
								</TD> 									
								<TD nowrap width="100">D&eacute;cl. salaires</TD>
								<TD nowrap>
									<ct:FWCodeSelectTag 
		                				name="declarationSalaire" 
										defaut="<%=viewBean.getDeclarationSalaire()%>"
										codeType="VEDECLARAT"
										wantBlank="true"/>&nbsp;&nbsp;
								</TD>
							</TR>
							<TR> 
								<TD nowrap height="31" width="161">Branche &eacute;conomique</TD>
								<TD nowrap colspan="3">
									<ct:FWCodeSelectTag 
		                				name="brancheEconomique" 
		                				libelle="both"
										defaut="<%=viewBean.getBrancheEconomique()%>"
										wantBlank="true"
										codeType="VEBRANCHEE"/> 									
								</TD>
								<%
								if (listeConventions.size() > 1) {
								%>				
								<TD>Convention
									<ct:FWListSelectTag name="convention"
									defaut="<%=viewBean.getConvention()%>"
	            					data="<%=listeConventions%>"
	            					/>
				
								</TD>
								<% } %>
							</TR>
							<TR>
								<TD nowrap height="31" width="161">Code Noga</TD>
								<TD nowrap colspan="2"> 
									<ct:FWCodeSelectTag 
		                				name="categorieNoga"
										defaut="<%=viewBean.getCategorieNoga()%>"
										codeType="VENOGACAT"
										libelle="both"
										wantBlank="true"/>
									<ct:FWCodeSelectTag 
		                				name="categorieNogaCode"
										defaut="<%=viewBean.getCategorieNoga()%>"
										codeType="VENOGACAT"
										libelle="code"
										wantBlank="true" />
									<select name="codeNoga"><option></option></select>
									<script>
										document.getElementById("categorieNoga").onchange = new Function("","rebuildNoga()");
										document.getElementById("categorieNogaCode").onchange = new Function("","rebuildNoga()");
										rebuildNoga(<%=viewBean.getCodeNoga()%>);
									</script>
									<a href="<%= linkNoga %>" target="new"><ct:FWLabel key="NAOS_JSP_NOGA_LISTE_AFFILIES_CODE_NOGA_LIEN"/></a>								
								</TD>								
								<TD nowrap>&nbsp;Activit&eacute;(s)</TD>
								<td>
									<TEXTAREA name="activite" rows="5" cols="40" onkeyup="maxLength(this, 254);"><%=viewBean.getActivite()%></TEXTAREA>
									<INPUT type="hidden" name="confirmerAnnonceActivite" value="<%=viewBean.getConfirmerAnnonceActivite()%>"/>
								</td>				
							</TR>							
							<TR> 
								<TD nowrap width="161">Facturation</TD>
								<TD nowrap width="210"> par relev&eacute;</TD>
								<TD nowrap width="100"> 
									<INPUT type="checkbox" name="releveParitaire" onclick="resetEAAVisibility()" <%=(viewBean.isReleveParitaire().booleanValue())? "checked" : ""%> >
								</TD>
								<TD rowspan="2" colspan="2">
									<DIV id="eaa" style="display: none">
										<INPUT type="checkbox" name="envoiAutomatiqueAnnonceSalaires" <%=(viewBean.getEnvoiAutomatiqueAnnonceSalaires().booleanValue())? "checked" : ""%> >&nbsp;
										Envoi automatique des
									relevés à blanc</DIV>
								</TD>
							</TR>
							<TR> 
								<TD nowrap width="161">&nbsp;</TD>
								<TD nowrap width="210">acomptes à la carte</TD>
								<TD nowrap width="100"> 
									<INPUT type="checkbox" name="relevePersonnel" onclick="compteaLaCarte()" <%=(viewBean.isRelevePersonnel().booleanValue())? "checked" : ""%> >
								</TD>
							</TR>
							<TR> 
								<TD nowrap width="161">&nbsp;</TD>
								<TD nowrap width="210">Code facturation</TD>
							
								<TD nowrap> 
									<ct:FWCodeSelectTag 
		                				name="codeFacturation" 
										defaut="<%=viewBean.getCodeFacturation()%>"
										wantBlank="true"
										codeType="VECODEFACT"/>								
								</TD>
							</TR>
							<TR> 
								<TD width="161">Contrôle LAA/LPP</TD>
								<TD nowrap colspan="2">
									<script>
									//document.getElementById("typeAffiliation").onchange = new Function("","checkEnvoiAutomatique()");
									
									</script>
								<INPUT type="hidden" name=wantGenerationSuiviLAALPP value="true"/>
								<% if (method != null && method.equals("add")) { %>
									<INPUT type="checkbox" name="envoiAutomatiqueLAA" <%=(viewBean.getEnvoiAutomatiqueLAA().booleanValue())? "checked" : ""%> >LAA &nbsp;
									<INPUT type="checkbox" name="envoiAutomatiqueLPP" <%=(viewBean.getEnvoiAutomatiqueLPP().booleanValue())? "checked" : ""%> >LPP
								<% } else { 
								       if (viewBean.isEnvoiAutomatiqueLAA().booleanValue()) {
								%>
									<INPUT type="checkbox" name="envoiAutomatiqueLAAReadOnly" checked tabindex="-1" readonly disabled>LAA &nbsp;
								<%     } else { %> 
									<INPUT type="checkbox" name="envoiAutomatiqueLAA">LAA &nbsp;
								<%     } 
									   if (viewBean.hasLPPetMotif()==true){
										   
											if (viewBean.isEnvoiAutomatiqueLPP().booleanValue()){   %>
												<INPUT type="checkbox" name="envoiAutomatiqueLPPReadOnly" checked tabindex="-1" readonly disabled>LPP
										<%	} else { %>
												<INPUT type="checkbox" name="envoiAutomatiqueLPPReadOnly" tabindex="-1" readonly disabled>LPP
										<%	} %>
								<%   } else {
											if(viewBean.isEnvoiAutomatiqueLPP().booleanValue()) {%>
												<INPUT type="checkbox" name="envoiAutomatiqueLPPReadOnly" checked tabindex="-1" readonly disabled>LPP
										<%	} else { %>
												<INPUT type="checkbox" name="envoiAutomatiqueLPP">LPP
								<%     } 
								   } 
								 }%>
								</TD>
								<!--TD nowrap>&nbsp;Irr&eacute;couvrable</TD>
								<TD nowrap> 
									<INPUT type="checkbox" name="irrecouvrable" <%=(viewBean.isIrrecouvrable().booleanValue())? "checked" : ""%> >
								</TD-->
								<!--TD nowrap></TD-->
								<TD nowrap colspan="3"> 
									<INPUT type="checkbox" name="occasionnel" <%=(viewBean.isOccasionnel().booleanValue())? "checked" : ""%> >
									Personnel occasionnel
										<script>
									document.getElementById("occasionnel").onchange = new Function("","checkEnvoiAutomatique()");
									</script>
								</TD>
							</TR>
							<TR>
								<TD nowrap width="161" >Exon&eacute;ration art.5 al.5</TD>
								<TD nowrap colspan="2"> 
									<INPUT type="checkbox" name="exonerationGenerale" <%=(viewBean.isExonerationGenerale().booleanValue())? "checked" : ""%> >
								</TD>
								<!--TD nowrap></TD-->
								<TD nowrap colspan="3"> 
									<INPUT type="checkbox" name="traitement" <%=(viewBean.isTraitement().booleanValue())? "checked" : ""%> onclick="displayFieldIDEPassif()">
									Affiliation provisoire
								</TD>
								<!--TD nowrap>&nbsp;Bonus/malus</TD>
								<TD nowrap> 
									<INPUT type="checkbox" name="bonusMalus" <%=(viewBean.getBonusMalus().booleanValue())? "checked" : ""%>>
								</TD-->
							</TR>
							
							<%if(viewBean.isEbusinessConnected()){ %>
							<TR id="isEBusiness">
								<TD nowrap width="161"><ct:FWLabel key="NAOS_JSP_AFFILIATION_EBUSINESS_CHECKBOX_PASSIVE"/></TD>
								<TD nowrap colspan="2"> 
										<INPUT id="isActivAffilieEBusiness" type="checkbox" name="isActivAffilieEBusiness" <%=(viewBean.isActivAffilieEBusiness())? "checked" : ""%> disabled="disabled" readonly="readonly">
								</TD>
								<TD colspan="2"></TD>
							</TR>
							<%}%>
							
							<TR> 
								<TD nowrap  height="11" colspan="4"> 
									<hr size="3" width="100%">
								</TD>
							</TR>
							
							<TR> 
							
								<TD nowrap width="161" height="31"><ct:FWLabel key="NAOS_JSP_AFFILIATION_IDE_NUMERO"/></TD>
								<TD nowrap width="310" > 
									<INPUT name="prefixeNumeroIDE" size="4" maxlength="4" type="text" value="CHE-" readOnly tabindex="-1" class="prefixeIDEDisable" disabled="disabled">
									<INPUT type="hidden" id="numeroIDE" name="numeroIDE" value="<%=viewBean.getNumeroIDE()%>">
									<INPUT type="hidden" id="idTiersIDE" name="idTiersIDE" value="<%=viewBean.getIdTiers()%>">
									
						   			<%
						   			    if(viewBean.isIdeReadOnly() || viewBean.isIdeProvisoire()) {
						   			%>
						   				
						   				<INPUT type="text" id="numeroIDESansCHE" name="numeroIDESansCHE" onchange="displayFieldIDEPassif();"  value="<%=AFIDEUtil.giveMeNumIdeFormatedWithoutPrefix(viewBean.getNumeroIDESansCHE())%>" readOnly="readonly"  disabled="disabled">
						   			
									<%
								    } else {
														   			    
						   			Object[] ideMethodsName= new Object[]{
												new String[]{"setNumeroIDE","getNumeroIDE"},
												new String[]{"setIdeStatut","getStatut"},
												new String[]{"setIdeRaisonSocialeb64","getRaisonSocialeb64"},
											};
						   			 
									Object[] ideParams = new Object[]{
										new String[]{"numeroIDESansCHE","forNumeroIDE"},
										new String[]{"raisonSociale","forRaisonSociale"},
										new String[]{"selectedId","ideAnnonceIdAffiliation"},
										new String[]{"idTiersIDE","idTiers"},
									};
									
									String redirectUrl1 = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/affiliation/affiliation_de.jsp";
								%>
						   			
						   			<ct:FWPopupList name="numeroIDESansCHE" 
												onChange="updateChampIde(tag);" 
												value="<%=AFIDEUtil.giveMeNumIdeFormatedWithoutPrefix(viewBean.getNumeroIDESansCHE())%>" 
												className="libelle" jspName="<%=jspLocation%>"
												autoNbrDigit="9" 
												minNbrDigit="9" 
												/>&nbsp;&nbsp;
												
										<ct:ifhasright element="naos.ide.ideSearch.chercher" crud="r" >
										&nbsp;&nbsp;&nbsp;<ct:FWSelectorTag
											name="ideSelector"
											title="Rechercher N° IDE"
											methods="<%=ideMethodsName%>"
											providerApplication ="naos"
											providerPrefix="AF"
											providerAction ="naos.ide.ideSearch.chercher"
											providerActionParams ="<%=ideParams%>"
											redirectUrl="<%=redirectUrl1%>"
										/>
										</ct:ifhasright>
									<%
									    }
									%>
								</TD>
								<TD id="isIdePartage"> 
									<div style="color:red">&nbsp;<ct:FWLabel key="NAOS_JSP_AFFILIATION_MESSAGE_IDE_PARTAGE"/></div>
								</TD>
								<TD>
									<%
									    if(!JadeStringUtil.isBlankOrZero(viewBean.getNumeroIDE())) {
									%>
										<div id="lienRegistreIde">
											<a href="<%=AFIDEUtil.giveMeLienRegistreIde(viewBean.getSession(),viewBean.getNumeroIDE())%>" target="_blank"><ct:FWLabel key="NAOS_JSP_AFFILIATION_IDE_LIEN_REGISTRE"/></a> 
										</div>
									<%}%>
								</TD>
							</TR>
							
							<%if (viewBean.isMessageAnnonceIdeCreationAjouteeToDisplay()){ %>
							<TR> 
								<TD >&nbsp;</TD>
								<TD colspan="3"> 
									<B id="textInfoCreationAnnonce"></B>
								</TD>
							</TR>
							<%} else if(viewBean.isInMutationOnly()) {%>
							<TR> 
								<TD >&nbsp;</TD>
								<TD colspan="3"> 
									<B><ct:FWLabel key="NAOS_JSP_AFFILIATION_MESSAGE_IDE_MUTATION_ANNONCE_EN_COURS_EXISTE"/></B>
								</TD>
							</TR>
							<%} else if(viewBean.isIdeReadOnly()) {%>
							<TR> 
								<TD >&nbsp;</TD>
								<TD colspan="3"> 
									<B><ct:FWLabel key="NAOS_JSP_AFFILIATION_MESSAGE_IDE_READ_ONLY_CAR_ANNONCE_EN_COURS_EXISTE"/></B>
								</TD>
							</TR>
							<%}%>
							
							<tr>
								<TD nowrap width="161" height="31"><ct:FWLabel key="NAOS_JSP_AFFILIATION_IDE_RAISON_SOCIALE"/></TD>
								<td colspan="2">
									<INPUT type="text" id="ideRaisonSociale" name="ideRaisonSociale" class="libelleLong10Disabled" readonly  value="<%=viewBean.getIdeRaisonSociale()%>">
								</td>
							</tr>
							<TR> 
								<TD nowrap width="161" height="31"><ct:FWLabel key="NAOS_JSP_AFFILIATION_IDE_NUMERO_STATUT"/></TD>
								
								
								<TD colspan="2">
									<INPUT type="text" id="libelleStatutIde" name="libelleStatutIde" class="libelleLong10Disabled" readonly  value="<%=CodeSystem.getLibelle(viewBean.getSession(),viewBean.getIdeStatut())%>">
									<INPUT type="hidden" id="ideStatut" name="ideStatut"  value="<%=viewBean.getIdeStatut()%>">
								</TD>
								
							</TR>							
							<TR >
								<TD nowrap width="161"><ct:FWLabel key="NAOS_JSP_AFFILIATION_IDE_CHECKBOX_NON_ANNONCANTE"/></TD>
								<TD nowrap colspan="2"> 
									<INPUT id="ideNonAnnoncante" type="checkbox" name="ideNonAnnoncante" <%=(viewBean.isIdeNonAnnoncante().booleanValue())? "checked" : ""%> >
								</TD>
							</TR>
							<TR id="displayIDEAnnoncePassive">
							<TD nowrap width="161"><ct:FWLabel key="NAOS_JSP_AFFILIATION_IDE_CHECKBOX_PASSIVE"/></TD>
							<TD nowrap colspan="2"> 
									<INPUT id="ideAnnoncePassive" type="checkbox" name="ideAnnoncePassive" <%=(viewBean.isIdeAnnoncePassive().booleanValue())? "checked" : ""%> onclick="messageSiPassif()">
									
								</TD>
								<TD colspan="2"><div id="messageAnnoncePassive" style="visibility:hidden;"><B><ct:FWLabel key="NAOS_JSP_AFFILIATION_MESSAGE_IDE_ANNONCE_PASSIVE"/></B></div></TD>
							</TR>

							<TR> 
								<TD nowrap  height="11" colspan="4"> 
									<hr size="3" width="100%">
								</TD>
							</TR>
							
							<TR> 
								<TD nowrap width="161" height="31"></TD>
								<!--TD nowrap colspan="2"></TD>
								<TD nowrap height="31"></TD-->
								<TD nowrap colspan="4">
									<B><A href="javascript:showPartie1()">Page 1</A></B> 
									-- 
									<A href="javascript:showPartie2()">Page 2</A>
									
									<%String urlGestionEnvoi = "\\leo?userAction=leo.envoi.envoi.chercher&"+
									globaz.leo.db.envoi.LEEnvoiViewBean.SRC_typeProv1+"="+globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_NUMERO+
									"&"+globaz.leo.db.envoi.LEEnvoiViewBean.SRC_valProv1+"="+viewBean.getAffilieNumero()+
									"&"+globaz.leo.db.envoi.LEEnvoiViewBean.SRC_typeProv2+"="+globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_ID_TIERS+
									"&"+globaz.leo.db.envoi.LEEnvoiViewBean.SRC_valProv2+"="+viewBean.getIdTiers()+
									"&"+globaz.leo.db.envoi.LEEnvoiViewBean.SRC_typeProv3+"="+globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_ROLE+
									"&"+globaz.leo.db.envoi.LEEnvoiViewBean.SRC_valProv3+"="+globaz.pyxis.db.tiers.TIRole.CS_AFFILIE+
									"&"+globaz.leo.db.envoi.LEEnvoiViewBean.SRC_typeProv4+"="+globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE+
									"&"+globaz.leo.db.envoi.LEEnvoiViewBean.SRC_valProv4+"="+globaz.naos.application.AFApplication.DEFAULT_APPLICATION_NAOS+
									"&"+globaz.leo.db.envoi.LEEnvoiViewBean.SRC_typeInterProv1+"="+globaz.naos.translation.CodeSystem.getLibelle(session,globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_NUMERO)+
									"&"+globaz.leo.db.envoi.LEEnvoiViewBean.SRC_valInterProv2+"="+viewBean.getTiers().getNomPrenom()+
									"&"+globaz.leo.db.envoi.LEEnvoiViewBean.SRC_typeInterProv2+"="+globaz.naos.translation.CodeSystem.getLibelle(session,globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_ID_TIERS)+
									"&"+globaz.leo.db.envoi.LEEnvoiViewBean.SRC_valInterProv3+"="+globaz.naos.translation.CodeSystem.getLibelle(session, globaz.pyxis.db.tiers.TIRole.CS_AFFILIE)+
									"&"+globaz.leo.db.envoi.LEEnvoiViewBean.SRC_typeInterProv3+"="+globaz.naos.translation.CodeSystem.getLibelle(session,globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_ROLE)+
									"&"+globaz.leo.db.envoi.LEEnvoiViewBean.SRC_typeInterProv4+"="+globaz.naos.translation.CodeSystem.getLibelle(session,globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE)+
									"&"+globaz.leo.db.envoi.LEEnvoiViewBean.SRC_GO_BACK+"=\\naos?userAction=naos.affiliation.affiliation.afficher";%>
									<ct:ifhasright element="leo.envoi.envoi.chercher" crud="r">
									-- 
									<A href="<%=request.getContextPath()%><%=urlGestionEnvoi%>">Gestion des envois</A>
									 </ct:ifhasright>
								</TD>
							</TR>
						</TBODY> 
						</TABLE>
						<TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie2" style="display:none">
						<TBODY>
							<!--TR>              
								<TD nowrap width="161" height="31">RS listes</TD> 
								<TD nowrap width="310">
								<input type="text" name="raisonSocialeCourt" size="30" maxlength="30" value="<%=designationCourt%>" tabindex="-1" ></TD>
								<TD nowrap width="140">&nbsp;Demande d'affiliation</TD>
								<TD nowrap width="300"><ct:FWCalendarTag name="dateDemandeAffiliation" value="<%=viewBean.getDateDemandeAffiliation()%>" /></TD>
							</TR>
							<TR>
							<TR> 
								<TD nowrap width="161" height="31" >Membre du comit&eacute;</TD>
								<TD nowrap colspan="3">
									<ct:FWCodeSelectTag 
		                				name="membreComite" 
										defaut="<%=viewBean.getMembreComite()%>"
										codeType="VEMEMBRECO"
										wantBlank="true"/> 									
								</TD>
							</TR>
							<TR> 
								<TD width="161" height="31" >Contrôle LAA</TD>
								<TD nowrap colspan="3">
								<% if (method != null && method.equals("add")) { %>
									<INPUT type="checkbox" name="envoiAutomatique" <%=(viewBean.getEnvoiAutomatiqueLAA().booleanValue())? "checked" : ""%> >
								<% } else { 
								       if (viewBean.isEnvoiAutomatiqueLAA().booleanValue()) {
								%>
									<INPUT type="checkbox" name="envoiAutomatiqueReadOnly" checked tabindex="-1" readonly disabled>
								<%     } else { %> 
									<INPUT type="checkbox" name="envoiAutomatique">
								<%     } 
								   } %>
								</TD>
							</TR-->
							<!--TR> 
								<TD nowrap width="161" height="31" >Pas assujetti à l'AVS</TD>
								<TD><INPUT type="checkbox" name="nonAVSAvant"> avant affiliation</TD>
								<TD><INPUT type="checkbox" name="nonAVSApres"> après radialtion</TD>
							</TR-->
							<!--TR>
								<TD nowrap colspan="4" height="17"></TD>
							</TR-->
							<TR>
								<TD nowrap colspan="4" width="900">
									<TABLE border="0" cellspacing="0" cellpadding="0">
									<TBODY>
									<TR style="font-weight: bold">
										<TD nowrap width="160" height="14">Suivi des caisses</TD>
										<TD nowrap width="150" >N&deg; d'affili&eacute; externe</TD>
										<TD nowrap width="130">D&eacute;but</TD>
										<TD nowrap width="130">Fin</TD>
										<TD nowrap width="*">Num&eacute;ro et Nom de caisse</TD>
									</TR>
									</TBODY> 
									</TABLE>
								</TD>
							</TR>
							<%  java.util.List suivi = AFSuiviCaisseAffiliation.listAllCaisse(viewBean.getAffiliationId(), viewBean.getSession());
								for (int i=0 ; i < suivi.size() && i < 6; i++) {
								String style="font-weight: bold";
								AFSuiviCaisseAffiliation suiviCaisse = (AFSuiviCaisseAffiliation)suivi.get(i);
								String libelleGenre = globaz.naos.translation.CodeSystem.getLibelle(session,suiviCaisse.getGenreCaisse());
								if(suiviCaisse.getAccessoire()){
									style = "style=font-style:italic";
									libelleGenre += " (accessoire)";
								}
							%>
							<TR>
								<TD nowrap colspan="4" width="900">
									<TABLE border="0" cellspacing="0" cellpadding="0">
									<TBODY>
									<TR>
										<TD nowrap width="160" height="25" <%=style%>><%=libelleGenre%></TD>
								<%
									String libelleAdmin = "";
									if (suiviCaisse.getAdministration() != null) {
										libelleAdmin = suiviCaisse.getAdministration().getCodeAdministration()+ " " + suiviCaisse.getAdministration().getNom();
									}
									if (JadeStringUtil.isIntegerEmpty(suiviCaisse.getMotif())) {
								%>
								    	<TD nowrap width="150" <%=style%>>
											<%=suiviCaisse.getNumeroAffileCaisse()%>
										</TD>
										<TD nowrap width="130" <%=style%>> 
											<%= suiviCaisse.getDateDebut()%>
										</TD>
										<TD nowrap width="130" <%=style%>> 
											<%= suiviCaisse.getDateFin()%>
										</TD>
										<TD nowrap width="*" <%=style%>>
											<%=libelleAdmin%>
										</TD>
								<%	} %>
									</TR>
									</TBODY> 
									</TABLE>
								</TD>
							</TR>
							<% } %>
							<% for (int i=0 ; i < (6 -suivi.size()); i++) { 
								 if (suivi.size() == 0 && i == 1) {%>						    
							<TR>
								<TD nowrap colspan="2" align="center"><B>Aucun suivi.</B></TD>
							</TR>
							<% } else {%>
							<TR>
								<TD nowrap colspan="2"><img src="<%=servletContext%>/images/blank.gif" height="35"></TD>
							</TR>
							<% }} %>
							<tr> 
            					<td width="150" nowrap="nowrap">Affiliation sécurisée (CI)</td>
           						 <td width="150" nowrap="nowrap"> 
								<% if (viewBean.hasRightAccesSecurity()) {%> 
									<ct:FWCodeSelectTag name="accesSecurite" defaut="<%=viewBean.getAccesSecurite()%>" codeType="CISECURI" wantBlank="false"/> 
								<% } else { %>
									<input title="Vous n'avez pas les droits requis pour modifier cette valeur" size="13" tabindex="-1" name='accesSecuriteInv' readonly class="disabled" value='<%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getAccesSecurite(),session)%>'>
								<% } %>
            					</td>
          					</tr>   
          					<TR id="displayCodeSuva">
          						<td height="31"><ct:FWLabel key="NAOS_JSP_CAF0003_CODE_SUVA"/></td>
          						<td>
          							<ct:FWCodeSelectTag 
									name="codeSUVA"
									defaut="<%=viewBean.getCodeSUVA()%>"
									codeType="VECODESUVA"
									wantBlank="true"
									libelle="" /> 
								</td>
							</TR>
							<TR>
								<TD nowrap height="31" colspan="3">&nbsp;</TD>
							</TR>
							<TR> 
								<TD nowrap width="161" height="31"></TD>
								<!--TD nowrap width="310"></TD>
								<TD nowrap height="31" width="140" ></TD-->
								<TD nowrap colspan="3">
									<A href="javascript:showPartie1()">Page 1</A> 
									-- 
									<B><A href="javascript:showPartie2()">Page 2</A></B> 
									<ct:ifhasright element="leo.envoi.envoi.chercher" crud="r">
									-- 
									<A href="<%=request.getContextPath()%><%=urlGestionEnvoi%>">Gestion des envois</A>
									</ct:ifhasright>
								</TD>
							</TR>
						</TBODY> 
						</TABLE>
						</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%	} %>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFOptionsAffiliation" showTab="options">
		<ct:menuSetAllParams key="affiliationId" value="<%=viewBean.getAffiliationId()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getAffiliationId()%>"/>
		<ct:menuSetAllParams key="likeNumAffilie" value="<%=viewBean.getAffilieNumero()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
