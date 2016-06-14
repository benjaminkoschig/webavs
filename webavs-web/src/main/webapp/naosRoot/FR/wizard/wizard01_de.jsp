<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.naos.util.AFUtil"%>
<%@page import="globaz.framework.controller.FWDefaultServletAction"%>
<%@page import="globaz.naos.util.AFIDEUtil"%>
<%@page import="java.util.Vector"%>
<%@page import="ch.globaz.vulpecula.web.views.registre.ConventionViewService"%>
<%@page import="globaz.naos.translation.CodeSystem"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	Vector listeConventions = ConventionViewService.getAllConventions();

	idEcran = "CAF0044";
	globaz.naos.db.wizard.AFWizard viewBean = (globaz.naos.db.wizard.AFWizard)session.getAttribute ("viewBean");
	bButtonNew 		= false;
	bButtonUpdate 	= false;
	bButtonDelete 	= false;
	bButtonValidate = false;
	bButtonCancel 	= false;	
	String jspLocation = servletContext + mainServletPath + "Root/ide_select.jsp";

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="JavaScript">

function trim(s) {
  while (s.substring(0,1) == ' ') {
    s = s.substring(1,s.length);
  }
  while (s.substring(s.length-1,s.length) == ' ') {
    s = s.substring(0,s.length-1);
  }
  return s;
}

function updateChampIde(tag){
	if(tag.select && tag.select.selectedIndex != -1){ 
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("numeroIDESansCHE").value = element.numeroIdeFormatedWithoutPrefix;
		document.getElementById("numeroIDE").value = element.numeroIdeUnformatedWithPrefix;
		
		document.getElementById("ideRaisonSociale").value = element.raisonSociale;
		document.getElementById("ideStatut").value = element.ideStatut;
		document.getElementById("libelleStatutIde").value = element.libelleStatutIde;
	}else{
		document.getElementById("numeroIDE").value = '';
		document.getElementById("ideRaisonSociale").value = '';
		document.getElementById("ideStatut").value = '';
		document.getElementById("libelleStatutIde").value = '';
	}
	displayFieldIDEPassif();
}

function add() {

<% if (viewBean.getAffilieNumero()==null || viewBean.getAffilieNumero().length() == 0) { %>
	document.forms[0].elements('affilieNumero').value= "<%=viewBean.getNouveauAffilieNumero()%>";
<% } %>
<% if(JadeStringUtil.isEmpty(viewBean.getTypeAffiliation())) { %>
checkTypeAff();
<% } %>
}

function upd() {

}

function validate() {
	var exit = true;
    state = validateFields();
    
	var message = "ERREUR : Tous les champs obligatoires ne sont pas remplis !";

	if (trim(document.forms[0].elements('idTiers').value) == "")
	{
		message = message + "\nVous n'avez pas saisi le tiers !";
		exit = false;
	}
	/*if (trim(document.forms[0].elements('affilieNumero').value) == "")
	{
		message = message + "\nVous n'avez pas saisi le numéro d'affilié !";
		exit = false;
	}
	
	if (trim(document.forms[0].elements('dateDebut').value) == "")
	{
		message = message + "\nVous n'avez pas saisi la date de début !";
		exit = false;
	}*/
	
	if (exit == false)
	{
		alert (message);
	}
	return (exit && state);
}

function cancel() {
}

function del() {
}

var messageAnnonceCreationIdeAjout = "<ct:FWLabel key='NAOS_JSP_AFFILIATION_MESSAGE_ANNONCE_CREATION_IDE_A_AJOUTER'/>";
var messageAnnonceCreationIdeLier = "<ct:FWLabel key='NAOS_JSP_AFFILIATION_MESSAGE_ANNONCE_CREATION_IDE_A_LIER'/>";
var idAnnonceCrationEnCoursForTiers = '<%= viewBean.getAffiliation().giveMeFirstIdAnnonceCreationEnCoursForIdTiers()%>';

function init() { 

		
	<%
	
	if(viewBean.getAffiliation().isAnnonceIdeCreationToAdd() && !JadeStringUtil.isBlankOrZero(viewBean.getAffiliation().giveMeFirstIdAnnonceCreationEnCoursForIdTiers())){
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
	
	
	<%} else {%>
		$("#textInfoCreationAnnonce").append(messageAnnonceCreationIdeAjout);
	<%}%>
		
	displayFieldIDEPassif();
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
	if ("" != document.getElementById('numeroIDESansCHE').value
		&& ( contains(typeListeActif, document.getElementById('typeAffiliation').options[document.getElementById('typeAffiliation').selectedIndex].value) 
				|| contains(typeListePassif, document.getElementById('typeAffiliation').options[document.getElementById('typeAffiliation').selectedIndex].value))) {
		document.all('displayIDEAnnoncePassive').disabled='';
	} else {
		document.getElementById('ideAnnoncePassive').checked=false;
		document.all('displayIDEAnnoncePassive').disabled='disabled';
	}
	messageSiPassif();
}

function contains(a, obj) {
    for (var i = 0; i < a.length; i++) {
        if (a[i] == obj) {
            return true;
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
	else if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP_EMPLOY%> &&occa.checked==false) {
	setCtrl("envoiAutomatiqueLAA",true);
	setCtrl("envoiAutomatiqueLPP",true);
	}
	else if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP_EMPLOY%> &&occa.checked==true) {
	setCtrl("envoiAutomatiqueLAA",false);
	setCtrl("envoiAutomatiqueLPP",false);
	}
	else if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_EMPLOY%> &&occa.checked==false) {
	setCtrl("envoiAutomatiqueLAA",true);
	setCtrl("envoiAutomatiqueLPP",true);
	}
	else if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_EMPLOY%> &&occa.checked==true){
	setCtrl("envoiAutomatiqueLAA",false);
	setCtrl("envoiAutomatiqueLPP",false);
	}
	else if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_NON_ACTIF%>) {
	setCtrl("envoiAutomatiqueLAA",false);
	setCtrl("envoiAutomatiqueLPP",false);
	}
	else if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_TSE%>) {
	setCtrl("envoiAutomatiqueLAA",false);
	setCtrl("envoiAutomatiqueLPP",false);
	}
	else if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_FICHIER_CENT%>) {
	setCtrl("envoiAutomatiqueLAA",true);
	setCtrl("envoiAutomatiqueLPP",false);
	}
	else if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_BENEF_AF%>) {
	setCtrl("envoiAutomatiqueLAA",false);
	setCtrl("envoiAutomatiqueLPP",false);
	}
	else if (type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_LTN%>) {
	//setCtrl("envoiAutomatiqueLAA",false);
	setCtrl("envoiAutomatiqueLPP",false);
	}else if(type.value == <%=CodeSystem.TYPE_AFFILI_CAP_EMPLOYEUR%> || type.value == <%=CodeSystem.TYPE_AFFILI_CAP_INDEPENDANT%> || type.value == <%=CodeSystem.TYPE_AFFILI_CGAS_EMPLOYEUR%> || type.value == <%=CodeSystem.TYPE_AFFILI_CGAS_INDEPENDANT%>){
		setCtrl("envoiAutomatiqueLAA",false);
		setCtrl("envoiAutomatiqueLPP",false);	
	}
}

function setCtrl(ctrl, state) {
 elem = document.getElementById(ctrl);
 if(elem!=null) {
 	elem.checked=state;
 }
}

function next() { 
	document.forms[0].elements('userAction').value="naos.wizard.wizard.afficherSelectionPlanCaisse";
	document.forms[0].submit();
}

function changeCodeLpp(){
	if (document.forms[0].elements('codeLpp').value == "") {
		document.forms[0].elements('nomTiersEnfant').value = "";
	}
	showPartie2();
	document.forms[0].codeLpp.focus();
}

function changeCodeLaa(){
	if (document.forms[0].elements('codeLaa').value == "") {
		document.forms[0].elements('nomTiersEnfantAf').value = "";
	} 
	showPartie2();
	document.forms[0].codeLaa.focus();
}

function showPartie1() {
	document.all('tPartie2').style.display='none';
	document.all('tPartie1').style.display='block';
}

function showPartie2() {
	document.all('tPartie1').style.display='none';
	document.all('tPartie2').style.display='block';
	var focusElement = document.forms[0].membreAssociation;
	if (focusElement.disabled == false) {
		document.forms[0].membreAssociation.focus();
	}
}
function checkTypeAff() {

	var type = document.getElementById("typeAffiliation");

	
	if(type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP%>) {
		document.getElementById("typeAssocie").disabled = false;
		document.getElementById('periodicite').value = <%=globaz.naos.translation.CodeSystem.PERIODICITE_TRIMESTRIELLE%>;
		document.getElementById('personnaliteJuridique').value = <%=globaz.naos.translation.CodeSystem.PERS_JURIDIQUE_RAISON_INDIVIDUELLE%>;
		document.getElementById('declarationSalaire').value = "";
	} else {
		document.getElementById("typeAssocie").value = "";
		document.getElementById("typeAssocie").disabled = true;
		if(type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_EMPLOY%>) {
			document.getElementById('periodicite').value = <%=globaz.naos.translation.CodeSystem.PERIODICITE_MENSUELLE%>;
			document.getElementById('personnaliteJuridique').value = <%=globaz.naos.translation.CodeSystem.PERS_JURIDIQUE_SA%>;
			document.getElementById('declarationSalaire').value = <%=globaz.naos.translation.CodeSystem.DECL_SAL_PRE_LISTE%>;
		} else if(type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP_EMPLOY%>) {
			document.getElementById('periodicite').value = <%=globaz.naos.translation.CodeSystem.PERIODICITE_TRIMESTRIELLE%>;
			document.getElementById('personnaliteJuridique').value = <%=globaz.naos.translation.CodeSystem.PERS_JURIDIQUE_RAISON_INDIVIDUELLE%>;
			document.getElementById('declarationSalaire').value = <%=globaz.naos.translation.CodeSystem.DECL_SAL_PRE_LISTE%>;
		} else if(type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_NON_ACTIF%>) {
			document.getElementById('periodicite').value = <%=globaz.naos.translation.CodeSystem.PERIODICITE_TRIMESTRIELLE%>;
			document.getElementById('personnaliteJuridique').value = <%=globaz.naos.translation.CodeSystem.PERS_JURIDIQUE_NA%>;
			document.getElementById('declarationSalaire').value = "";
		}else if(type.value==<%=globaz.naos.translation.CodeSystem.TYPE_AFFILI_LTN%>) {
			document.getElementById('periodicite').value = <%=globaz.naos.translation.CodeSystem.PERIODICITE_ANNUELLE%>;
			document.getElementById('personnaliteJuridique').value = <%=globaz.naos.translation.CodeSystem.PERS_JURIDIQUE_RAISON_INDIVIDUELLE%>;
			document.getElementById('declarationSalaire').value = <%=globaz.naos.translation.CodeSystem.DECL_SAL_PRE_LISTE%>;
		} else {
			document.getElementById('declarationSalaire').value = '';
		}
	}
	checkEnvoiAutomatique();
	displayFieldIDEPassif();

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

function resetEAAVisibility() {
	if (document.forms[0].elements('releveParitaire').checked ||
		document.forms[0].elements('relevePersonnel').checked) {
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
					Saisie de donn&eacute;es de l'Affiliation - 01
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						
						<div style="display:none" id="dialog_link_or_create_annonce" title="<ct:FWLabel key='NAOS_DIALOG_LINK_OR_CREATE_ANNONCE_TITRE'/>">
  							 <p>
  							 	<ct:FWLabel key="NAOS_DIALOG_LINK_OR_CREATE_ANNONCE_TEXTE"/>
  							</p>
						</div>
						
						<INPUT type="hidden" id="idAnnonceIdeCreationLiee" name="idAnnonceIdeCreationLiee" value="<%=viewBean.getIdAnnonceIdeCreationLiee()%>">
						<TR> 
						<TD> 
						<TABLE border="0" cellspacing="0" cellpadding="0">
						<TBODY> 
							<TR>
								<% if (viewBean.getTiers() != null) { %>
								<TD nowrap width="161"><A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdTiers()%>">Tiers</A></TD>
								<% } else { %>
								<TD nowrap width="161">Tiers</TD>
								<% } %>
								<TD nowrap colspan="3"> 
									<%
										String method = request.getParameter("_method");
										String tmpIdTiers = "";
										String tmpNom = "";
										String tmpIdTiersExterne = "";
				
										if (viewBean.getTiers() != null) {
											tmpIdTiers = viewBean.getIdTiers();
											tmpNom = viewBean.getTiers().getNom();
											tmpIdTiersExterne = viewBean.getTiers().idTiersExterneFormate();
										}

										String designationCourt = "";
										String designationLong = "";
										
										if (!"".equals(viewBean.getNomTiersSelection())) {
											// si on a cliqué sur [...] mettre a jour les raison sociale depuis
											// le tiers selectioné.
											tmpNom = viewBean.getNomTiersSelection();
											viewBean.setRaisonSociale("");
											viewBean.setRaisonSocialeCourt("");
											viewBean.setNomTiersSelection("");
										}
										if (method != null && method.equals("add")
												&& globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getRaisonSocialeCourt())
												&& globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getRaisonSociale())) {
											if(tmpNom.length()>100) {
												designationLong = tmpNom.substring(0,100);
												designationCourt = tmpNom.substring(0,30);
											} else {
												designationLong = tmpNom;
											 	if(tmpNom.length()>30) {
													designationCourt = tmpNom.substring(0,30);
												} else {
													designationCourt = tmpNom;
												}
											}
										} else {
											designationCourt = viewBean.getRaisonSocialeCourt();
											designationLong =  viewBean.getRaisonSociale();
										}
										
										
										
										String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/wizard/wizard01_de.jsp";	
									%>
									<INPUT type="hidden" name="wizardPage" value="01">
									<INPUT type="hidden" name="idTiers" value="<%=tmpIdTiers%>">
									<% if(tmpIdTiersExterne.length()!=0) { %>
									<input type="text" name="idExterne" size="16" maxlength="16" value="<%=tmpIdTiersExterne%>" readOnly tabindex="-1" class="disabled">
									<% } %>
									<INPUT type="text" name="nom" size="60" maxlength="60" value="<%=globaz.jade.client.util.JadeStringUtil.change(tmpNom,"\"","&quot;")%>" tabindex="-1" readOnly class="disabled">
 									
								<%
									Object[] caisseMethods= new Object[]{
										new String[]{"setIdTiers","getIdTiers"},
										new String[]{"setNomTiersSelection","getNom"}
									};
								%>
								<ct:FWSelectorTag 
									name="caisseSelector" 
									
									methods="<%=caisseMethods%>"
									providerApplication ="pyxis"
									providerPrefix="TI"
									providerAction ="pyxis.tiers.tiers.chercher"
									redirectUrl="<%=redirectUrl%>"/> 
							</TD></TR>
							<TR> 
								<TD colspan="4"> 
									<hr size="3" width="100%">
								</TD>
							</TR>
							<TR>
							<TR>
								<TD nowrap>Raison sociale (long)</TD>
								<TD nowrap colspan="3"><input type="text" name="raisonSociale" size="100" maxlength="100" value="<%=globaz.jade.client.util.JadeStringUtil.change(designationLong,"\"","&quot;")%>" tabindex="-1" ></TD> 
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
								<TD nowrap height="31" >P&eacute;riode</TD>
								<TD nowrap> 
									<ct:FWCalendarTag name="dateDebut" doClientValidation="CALENDAR" value="<%=viewBean.getDateDebut()%>" /> 
									&agrave; 
									<ct:FWCalendarTag name="dateFin" doClientValidation="CALENDAR" value="<%=viewBean.getDateFin()%>" /> 
								</TD>
								<TD nowrap>Genre d'affiliation</TD>
								<TD nowrap> 
									<ct:FWCodeSelectTag 
		                				name="typeAffiliation" 
										defaut='<%=JadeStringUtil.isBlankOrZero(viewBean.getTypeAffiliation())?new String(globaz.naos.translation.CodeSystem.TYPE_AFFILI_EMPLOY):viewBean.getTypeAffiliation()%>'
										codeType="VETYPEAFFI"/>
										<script>
											document.getElementById("typeAffiliation").onchange = new Function("","checkTypeAff()");
											
										</script>									
								</TD>
							</TR>
							<TR> 
								<TD colspan="4"> 
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
								<TD nowrap width="161" height="31">Motif de cr&eacute;ation</TD>
								<TD nowrap colspan="2">
									<ct:FWCodeSelectTag 
		                				name="motifCreation" 
										defaut="<%=viewBean.getMotifCreation()%>"
										codeType="VEMOTIFAFF"/> 									
								</TD>
								<TD nowrap width="100">Motif de fin</TD>
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
								<TD nowrap width="161" height="31">Personnalit&eacute; juridique</TD>
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
									<ct:FWCodeSelectTag 
		                				name="typeAssocie"
										defaut="<%=viewBean.getTypeAssocie()%>"
										codeType="VETYPEASSO"
										wantBlank="true"/> 									
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
								<TD nowrap>D&eacute;cl. salaires</TD>
								<TD nowrap>
									<ct:FWCodeSelectTag 
		                				name="declarationSalaire" 
										defaut="<%=viewBean.getDeclarationSalaire()%>"
										codeType="VEDECLARAT"
										wantBlank="true"/>&nbsp;&nbsp;
								</TD>
							</TR>
							<TR> 
								<TD nowrap width="161" height="31">Branche &eacute;conomique</TD>
								<TD nowrap colspan="3">
									<ct:FWCodeSelectTag 
		                				name="brancheEconomique" 
		                				libelle="both"
										defaut="<%=viewBean.getBrancheEconomique()%>"
										codeType="VEBRANCHEE"
										wantBlank="true"/> 									
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
								<%} %>
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
																	
								</TD>
								<TD nowrap >&nbsp;Activit&eacute;(s)</TD>
								<td>
									<TEXTAREA name="activite" rows="5" cols="40" onkeyup="maxLength(this, 254);"><%=viewBean.getActivite()%></TEXTAREA>
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
								<TD nowrap width="210">accomptes à la carte</TD>
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
									<INPUT type="checkbox" name="envoiAutomatiqueLAA" <%=(viewBean.getEnvoiAutomatiqueLAA().booleanValue())? "checked" : ""%> >LAA &nbsp;
									<INPUT type="checkbox" name="envoiAutomatiqueLPP" <%=(viewBean.getEnvoiAutomatiqueLPP().booleanValue())? "checked" : ""%> >LPP
								</TD>
								<!-- TD nowrap>Personnel occasionnel</TD-->
								<TD nowrap colspan="3"> 
									<INPUT type="checkbox" name="occasionnel" <%=(viewBean.isOccasionnel().booleanValue())? "checked" : ""%> >
									Personnel occasionnel
									<script>
									//document.getElementById("occasionnel").onchange = new Function("","checkEnvoiAutomatique()");
									</script>
								</TD>
								<!--TD nowrap >Irr&eacute;couvrable</TD>
								<TD nowrap > 
									<INPUT type="checkbox" name="irrecouvrable" <%=(viewBean.isIrrecouvrable().booleanValue())? "checked" : ""%> >
								</TD-->
							</TR>
							<TR> 
								<TD nowrap width="161">Exon&eacute;ration art.5 al.5</TD>
								<TD nowrap colspan="2"> 
									<INPUT type="checkbox" name="exonerationGenerale" <%=(viewBean.isExonerationGenerale().booleanValue())? "checked" : ""%> >
								</TD>
								<!--TD nowrap width="161"></TD-->
								<TD nowrap colspan="3"> 
									<INPUT type="checkbox" name="traitement" <%=(viewBean.isTraitement().booleanValue())? "checked" : ""%>  onclick="displayFieldIDEPassif()">
									Affiliation provisoire
								</TD>

							</TR>
								
							<TR> 
								<TD colspan="4"> 
									<hr size="3" width="100%">
								</TD>
							</TR>
							
														
							<TR> 
							
								<TD nowrap width="161" height="31"><ct:FWLabel key="NAOS_JSP_AFFILIATION_IDE_NUMERO"/></TD>
								<TD nowrap width="310" > 
									
									<INPUT type="hidden" id="numeroIDE" name="numeroIDE" value="<%=viewBean.getNumeroIDE()%>">
									<INPUT type="hidden" id="idTiersIDE" name="idTiersIDE" value="<%=viewBean.getIdTiers()%>">
									
									<INPUT name="prefixeNumeroIDE" size="4" maxlength="4" type="text" value="CHE-" readOnly tabindex="-1" class="prefixeIDEDisable" disabled="disabled">
									
									<%if(viewBean.isIdeReadOnly()) {%>
						   				
						   				<INPUT type="text" id="numeroIDESansCHE" name="numeroIDESansCHE" onchange="displayFieldIDEPassif();"  value="<%=AFIDEUtil.giveMeNumIdeFormatedWithoutPrefix(viewBean.getNumeroIDESansCHE())%>" readOnly="readonly"  disabled="disabled">
						   			
									<%} else {
									    
									    
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
											
											String redirectUrl1 = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+FWDefaultServletAction.getIdLangueIso(session)+"/wizard/wizard01_de.jsp";
									%>
									
						   			<ct:FWPopupList name="numeroIDESansCHE" 
												onChange="updateChampIde(tag);" 
												value="<%=AFIDEUtil.giveMeNumIdeFormatedWithoutPrefix(viewBean.getAffiliation().getNumeroIDESansCHE())%>" 
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
									<%}%>
								</TD>
							
								<TD>
									<%if(!JadeStringUtil.isBlankOrZero(viewBean.getNumeroIDE())) { %>
										<div id="lienRegistreIde">
											<a href="<%=AFIDEUtil.giveMeLienRegistreIde(viewBean.getSession(),viewBean.getAffiliation().getNumeroIDE())%>" target="_blank"><ct:FWLabel key="NAOS_JSP_AFFILIATION_IDE_LIEN_REGISTRE"/></a> 
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
							<%} else if(viewBean.isIdeReadOnly() && JadeStringUtil.isBlank(viewBean.getMessage())) {%>
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
									<INPUT type="text" id="ideRaisonSociale" name="ideRaisonSociale" class="libelleLong10Disabled" readonly value="<%=viewBean.getIdeRaisonSociale()%>">
								</td>
							</tr>
							<TR> 
								<TD nowrap width="161" height="31"><ct:FWLabel key="NAOS_JSP_AFFILIATION_IDE_NUMERO_STATUT"/></TD>
								
						
								<TD colspan="2">
									<INPUT type="text" id="libelleStatutIde" name="libelleStatutIde" class="libelleLong10Disabled" readonly  value="<%=CodeSystem.getLibelle(viewBean.getSession(),viewBean.getIdeStatut())%>">
									<INPUT type="hidden" id="ideStatut" name="ideStatut"  value="<%=viewBean.getIdeStatut()%>">
								</TD>
								
							</TR>
							
							
							<TR id="displayIDEAnnoncePassive">
							<TD nowrap width="161"><ct:FWLabel key="NAOS_JSP_AFFILIATION_IDE_CHECKBOX_PASSIVE"/></TD>
							<TD> 
									<INPUT id="ideAnnoncePassive" type="checkbox" name="ideAnnoncePassive" <%=(viewBean.isIdeAnnoncePassive().booleanValue())? "checked" : ""%> onclick="messageSiPassif()">
									
								</TD>
								<TD colspan="2"><div id="messageAnnoncePassive" style="visibility:hidden;"><B><ct:FWLabel key="NAOS_JSP_AFFILIATION_MESSAGE_IDE_ANNONCE_PASSIVE"/></B></div></TD>
							</TR>
							
							<TR> 
								<TD colspan="4"> 
									<hr size="3" width="100%">
								</TD>
							</TR>
          					
							<TR> 
								<TD nowrap width="161" height="31"></TD>
								<!--TD nowrap colspan="2"></TD>
								<TD nowrap width="140" ></TD-->
								<TD nowrap colspan="4">
									<B><A href="javascript:showPartie1()">Page 1</A></B> 
									-- 
									<A href="javascript:showPartie2()">Page 2</A></TD>
							</TR>
						</TBODY> 
						</TABLE>
						<TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie2" style="display:none">
						<TBODY> 
							<!--TR>              
								<TD width="161" height="31">RS listes</TD> 
								<TD><input type="text" name="raisonSocialeCourt" size="30" maxlength="30" value="<%=designationCourt%>" tabindex="-1" ></TD>
								<TD nowrap width="140">&nbsp;Demande d'affiliation</TD>
								<TD nowrap width="300"><ct:FWCalendarTag name="dateDemandeAffiliation" value="<%=viewBean.getDateDemandeAffiliation()%>" /></TD>
							
							<TR>
							<TR> 
								<TD nowrap width="161" height="31" >Membre du comit&eacute;</TD>
								<TD nowrap colspan="3">
									<
									ct:FWCodeSelectTag 
		                				name="membreComite" 
										defaut="<%=viewBean.getMembreComite()%>"
										codeType="VEMEMBRECO"
										wantBlank="true"/> 									
								</TD>
							</TR>
							<TR> 
								<TD width="161" height="31" >Contrôle LAA</TD>
								<TD nowrap colspan="3">
									<INPUT type="checkbox" name="envoiAutomatiqueLAA" <%=(viewBean.getEnvoiAutomatiqueLAA().booleanValue())? "checked" : ""%> >
								</TD>
							</TR-->
							<TR>
								<TD nowrap colspan="4" height="253"></TD>
							</TR>
							<tr> 
            					<td>Affiliation sécurisée (CI)</td>
           						 <td colspan=3> 
									<ct:FWCodeSelectTag name="accesSecurite" defaut="317000" codeType="CISECURI" wantBlank="false"/> 
            					</td>
          					</tr>  
          					
							<TR> 
								<TD nowrap  height="11" colspan="4"> 
									<hr size="3" width="100%">
								</TD>
							</TR>
							<TR> 
								<TD nowrap >&nbsp;</TD>
								<TD nowrap colspan="3"><A href="javascript:showPartie1()">Page 
									1</A> -- <B><A href="javascript:showPartie2()">Page 
									2</A></B> </TD>
							</TR>
						</TBODY> 
						</TABLE>
						</TD>
						</TR>
						<TR>
							<TD colspan="5" align="left" height="18"> 
								<INPUT class="btnCtrl" id="btnNext" type="button" value=">>" onclick="if(validate()) next();">
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
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
