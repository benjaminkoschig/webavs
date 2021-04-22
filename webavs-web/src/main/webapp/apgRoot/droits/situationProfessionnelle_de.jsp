<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.prestation.api.IPRDemande"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.apg.servlet.IAPActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>

<%@page import="globaz.framework.util.FWMessageFormat"%><script language="JavaScript">

</script>
<%
idEcran="PAP0017";
globaz.apg.vb.droits.APSituationProfessionnelleViewBean viewBean = (globaz.apg.vb.droits.APSituationProfessionnelleViewBean) session.getAttribute("viewBean");

bButtonCancel = false;

bButtonValidate = viewBean.isModifiable() && bButtonValidate && controller.getSession().hasRight(IAPActions.ACTION_SITUATION_PROFESSIONNELLE, FWSecureConstants.UPDATE);
bButtonUpdate = viewBean.isModifiable() && bButtonUpdate && controller.getSession().hasRight(IAPActions.ACTION_SITUATION_PROFESSIONNELLE, FWSecureConstants.UPDATE);
bButtonDelete = viewBean.isModifiable() && bButtonUpdate && controller.getSession().hasRight(IAPActions.ACTION_SITUATION_PROFESSIONNELLE, FWSecureConstants.UPDATE);


%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/apgRoot/droits/situationProfessionnelle.js"></script>
<%-- tpl:put name="zoneScripts" --%>
		<script language="JavaScript">

  
  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_SITUATION_PROFESSIONNELLE%>.ajouter"
  }
  function upd() {
    pourcentClick(document.forms[0].elements('isPourcentAutreRemun'), document.forms[0].elements('periodiciteAutreRemun'));
   	pourcentClick(document.forms[0].elements('isPourcentMontantVerse'), document.forms[0].elements('periodiciteMontantVerse'));
   	parent.isModification = true;
   	
  	if (document.all('isIndependant').checked) {
	  	document.forms[0].elements('isVersementEmployeur')[0].disabled = true;
	  	document.forms[0].elements('isVersementEmployeur')[1].disabled = true;
	}
  	disableIsAllocExpl();
  }
  
  function disableIsAllocExpl(){
	if(isEmployeurActif() && isJourIsole()){
		document.forms[0].elements('isAllocationExploitation').checked = false;
	}
  }
  
  function isJourIsole(){
	var isJourIsole = <%=viewBean.isJourIsole()%>;
	return isJourIsole;
  }

  function validate() {
    state = true;
    parent.isNouveau = false;
    parent.isModification = false;
    
    checkDepartement();
    checkIbanEmployeur();
    checkACM();
    
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_SITUATION_PROFESSIONNELLE%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_SITUATION_PROFESSIONNELLE%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_SITUATION_PROFESSIONNELLE%>.afficher";
  }

  function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_SITUATION_PROFESSIONNELLE%>.supprimer";
        document.forms[0].submit();
    }
  }
  	
  function init(){
  	parent.isNouveau = false;
	<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	  	// mise a jour de la liste du parent
		if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		}
    	parent.isNouveau = true;
	<%}%>
	
	document.forms[0].target="fr_main";
	
	var isRetourPyxis=false;
	
	// retour depuis Pyxis
	if(parent.isNouveau && 
	   <%=!globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getIdAffilieEmployeur())%>){
		isRetourPyxis=true;
		checkPeriodeApgPeriodeAff();
	}
	
	// Les prestations ACM_NE sont calculée uniquement pour les cas APG et non maternité	
	var isAPG = <%=viewBean.isAPG()%>;
	var isMaternite = <%=viewBean.isMaternite()%>;
  	var isPaternite = <%=viewBean.isPaternite()%>;
	var isPrestationAcmNeEnable = <%=viewBean.isPrestationAcmNeEnable()%>;
	var isPrestationAcmAlphaEnable = <%=viewBean.isPrestationAcmAlfaEnable().booleanValue()%>;
	var isPrestationAcm2AlphaEnable = <%=viewBean.isPrestationAcm2AlfaEnable().booleanValue()%>;
	var isPorteEnCompte = <%=viewBean.getIsPorteEnCompte().booleanValue()%>;
	
	// Création d'une instance contenant tous les paramètres du droit en cours
	var viewControllerData = new ViewControllerData();
	viewControllerData.className = '<%=viewBean.getNameClassForAPRechercherTypeAcmService()%>';
	viewControllerData.methodName = '<%=viewBean.getNameMethodForAPRechercherTypeAcmService()%>';
	viewControllerData.isModification = <%=bButtonUpdate%>;
	viewControllerData.isAcmAlphaEnable = isPrestationAcmAlphaEnable;
	viewControllerData.isAcm2AlphaEnable = isMaternite && viewControllerData.isAcmAlphaEnable && isPrestationAcm2AlphaEnable;
	viewControllerData.isAcmNeEnable = isAPG && isPrestationAcmNeEnable;
	viewControllerData.isPorteEnCompte = isPorteEnCompte;
	
	if (document.forms[0].elements('_method').value == "add"){
		$('input[type="checkbox"][name="hasAcmAlphaPrestations"]').attr('checked', viewControllerData.isAcmAlphaEnable);
		$('input[type="checkbox"][name="hasAcm2AlphaPrestations"]').attr('checked', viewControllerData.isAcm2AlphaEnable);
	} else {		
		$('input[type="checkbox"][name="hasAcmAlphaPrestations"]').attr('checked', <%=viewBean.getHasAcmAlphaPrestations().booleanValue()%>);
		$('input[type="checkbox"][name="hasAcm2AlphaPrestations"]').attr('checked', <%=viewBean.getHasAcm2AlphaPrestations().booleanValue()%>);
	}

	// Création d'une instance du controlleur de vue de cette page
	var viewController = new ViewController(viewControllerData);
	viewController.init(isRetourPyxis);
	
	manageAdressePaiement();
  }
  
  function postInit(){
  	if (document.all('isIndependant').checked) {
  		document.forms[0].elements('isVersementEmployeur')[0].checked = true;
		document.forms[0].elements('isVersementEmployeur')[1].checked = false;
	  	// revenu indépendant
	  	document.all('blockWithoutAnneeTaxation').style.display = 'none';
	  	document.all('blockAnneeTaxation').style.display = 'block';
  		document.all('specialEmployeur').style.display = 'none';
		document.forms[0].elements('isVersementEmployeur')[1].disabled=true;
		document.forms[0].elements('isVersementEmployeur')[0].disabled=true;
   	} else {
   		document.all('blockWithoutAnneeTaxation').style.display = 'block';
	  	document.all('blockAnneeTaxation').style.display = 'none';
   	}
  	if(<%=viewBean.isPaternite()%>) {
		document.forms[0].elements('isAllocationExploitation').checked = false;
	}
   	showisAMATFExcluded();
  	manageAdressePaiement();
  }
  	
  function montantIndependantChange() {
  	// revenu indépendant
  	document.all('specialEmployeur').style.display = 'none';
  	document.forms[0].elements('isIndependant').checked = true;
  	
  	// on vide tous les champs des autres salaires
  	document.forms[0].elements('heuresSemaine').value = '';
  	document.forms[0].elements('salaireHoraire').value = '';
  	document.forms[0].elements('salaireMensuel').value = '';
  	document.forms[0].elements('autreSalaire').value = '';
  	document.forms[0].elements('montantVerse').value = '';
  }
  
  function manageAdressePaiement(){
	  if(document.forms[0].elements('isVersementEmployeur')[0].checked && <%=!JadeStringUtil.isBlankOrZero(viewBean.getIdAffilieEmployeur()) %>){
		  $(".withoutAdressePaiement").hide();
		  $(".withAdressePaiement").show();
	  } else {
		  $(".withoutAdressePaiement").show();
		  $(".withAdressePaiement").hide();
	  }
  }
  
  function boutonIndependantChange() {
	  	if (document.all('isIndependant').checked) {
	  		
	  		document.all('specialEmployeur').style.display = 'none';
	  		
		  	document.forms[0].elements('isIndependant').checked = true;
			
			document.forms[0].elements('isVersementEmployeur')[0].checked = true;
			document.forms[0].elements('isVersementEmployeur')[1].checked = false;
		  	document.forms[0].elements('isVersementEmployeur')[0].disabled = true;
		  	document.forms[0].elements('isVersementEmployeur')[1].disabled = true;
	  	
	  		// on vide tous les champs des autres salaires
		  	document.forms[0].elements('heuresSemaine').value = '';
		  	document.forms[0].elements('salaireHoraire').value = '';
		  	document.forms[0].elements('salaireMensuel').value = '';
		  	document.forms[0].elements('autreSalaire').value = '';
		  	document.forms[0].elements('montantVerse').value = '';
		  	
		  	document.all('blockWithoutAnneeTaxation').style.display = 'none';
		  	document.all('blockAnneeTaxation').style.display = 'block';
		  	
		  	document.forms[0].elements('anneeTaxation').value = '<%=viewBean.getAnneeFromDateDebutDroit()%>';

		  	//si independant cocher auto. alloc. expl. sauf si non-actif
		  	if(isEmployeurActif() && !isJourIsole()){
		  		// pour la maternite, on ne donne pas les allocations d'exploitation se independant et actif
		  		<%if (!globaz.apg.util.TypePrestation.TYPE_MATERNITE.equals(viewBean.getTypePrestation())) {%>
		  			document.forms[0].elements('isAllocationExploitation').checked = true;
		  		<%}%>
		  	}else{
		  	  	disableIsAllocExpl();
		  	}
		} else {
			
	  		document.all('specialEmployeur').style.display = 'block';
	  		
	  		document.forms[0].elements('isIndependant').checked = false;
	  		
			document.forms[0].elements('isVersementEmployeur')[0].checked = false;
			document.forms[0].elements('isVersementEmployeur')[1].checked = true;
	  	  	document.forms[0].elements('isVersementEmployeur')[0].disabled = false;
		  	document.forms[0].elements('isVersementEmployeur')[1].disabled = false;

		  	document.all('blockWithoutAnneeTaxation').style.display = 'block';
		  	document.all('blockAnneeTaxation').style.display = 'none';
		  	
		  	document.forms[0].elements('anneeTaxation').value = '';
		  	
		  	document.forms[0].elements('isAllocationExploitation').checked = false;
		}
	  	
	  	manageAdressePaiement();
  }
  
  function montantHoraireChange() {
  	// revenu salarié
  	document.all('specialEmployeur').style.display = 'block';
  	document.forms[0].elements('isIndependant').checked = false;
  	document.forms[0].elements('revenuIndependant').value = '';
  	
  	// on vide tous les champs des autres salaires
  	document.forms[0].elements('salaireMensuel').value = '';
  	document.forms[0].elements('autreSalaire').value = '';
  	
  	// on set les menus deroulant de pourcentage
  	setMenusDeroulantsPourcentages(<%=globaz.prestation.api.IPRSituationProfessionnelle.CS_PERIODICITE_HEURE%>);
  }
  
  function checkMontantHoraire(){
    var valeurCourante = toFloat(document.forms[0].elements('salaireHoraire').value);
  	if(parseFloat(valeurCourante)>100){
	  	parent.warningObj.text += "<%=objSession.getLabel("MONTANT_SAL_HORAIRE_ELEVE_WARNING")%>";
	  	parent.showWarnings();
		parent.warningObj.text = "";
	}
  }
  
  function montantMensuelChange() {
  	// revenu salarié
  	document.all('specialEmployeur').style.display = 'block';
  	document.forms[0].elements('isIndependant').checked = false;
  	document.forms[0].elements('revenuIndependant').value = '';
  	
  	// on vide tous les champs des autres salaires
  	document.forms[0].elements('heuresSemaine').value = '';
  	document.forms[0].elements('salaireHoraire').value = '';
  	document.forms[0].elements('autreSalaire').value = '';
  	
  	// on set les menus deroulant de pourcentage
  	setMenusDeroulantsPourcentages(<%=globaz.prestation.api.IPRSituationProfessionnelle.CS_PERIODICITE_MOIS%>);
  }
  
  function montantAutreChange() {
  	// revenu salarié
  	document.all('specialEmployeur').style.display = 'block';
  	document.forms[0].elements('isIndependant').checked = false;
  	document.forms[0].elements('revenuIndependant').value = '';
  	
  	// on vide tous les champs des autres salaires
  	document.forms[0].elements('heuresSemaine').value = '';
  	document.forms[0].elements('salaireHoraire').value = '';
  	document.forms[0].elements('salaireMensuel').value = '';
  	
  	// on set les menus deroulant de pourcentage
  	setMenusDeroulantsPourcentages(document.forms[0].elements('periodiciteAutreSalaire').value);
  }
  
  function pourcentClick(checkbox, select) {
  	select.disabled = checkbox.checked;
  	
  	if (checkbox.checked) {
  		if (document.forms[0].elements('salaireHoraire').value != '') {
		  	select.value = <%=globaz.prestation.api.IPRSituationProfessionnelle.CS_PERIODICITE_HEURE%>;
  		} else if (document.forms[0].elements('salaireMensuel').value != '') {
		  	select.value = <%=globaz.prestation.api.IPRSituationProfessionnelle.CS_PERIODICITE_MOIS%>;
  		} else {
 		 	select.value = document.forms[0].elements('periodiciteAutreSalaire').value;
  		}
  	}
  }
  
  function setMenusDeroulantsPourcentages(codeSysteme) {
  	if (document.forms[0].elements('isPourcentMontantVerse').checked) {
	  	document.forms[0].elements('periodiciteMontantVerse').value = codeSysteme;
	}
	
  	if (document.forms[0].elements('isPourcentAutreRemun').checked) {
	  	document.forms[0].elements('periodiciteAutreRemun').value = codeSysteme;
	}
  }
  
  function periodiciteAutreSalaireChangee() {
  	setMenusDeroulantsPourcentages(document.forms[0].elements('periodiciteAutreSalaire').value);
  }
  
  function readOnly(flag) {
  	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
    for(i=0; i < document.forms[0].length; i++) {
        if (!document.forms[0].elements[i].readOnly && 
        	document.forms[0].elements[i].className != 'forceDisable' &&
        	document.forms[0].elements[i].type != 'hidden') {
        	// On ne souhaite pas passer ce champs 'csAssuranceAssociation' en disabled
        	if(document.forms[0].elements[i].name != 'csAssuranceAssociation'){        		
            	document.forms[0].elements[i].disabled = flag;
        	}
        }
    }
  }
  
	function rechercherAffilie(value) {
	
		if (value!=""){
			// si le numero d'affilie n'a pas une longueur de 8 -> ###.####
			// on essaye de rajouter le formatage si la longueur vaut 7 ####### -> ###.####
			if(value.length == 7){
				 var valueForm = "";
				 var valueForm = valueForm.concat(value.substring(0, 3),".",value.substring(3,7));
				 document.forms[0].elements('numAffilieEmployeur').value = valueForm;
				 value = valueForm;
			}
			  						
			document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_SITUATION_PROFESSIONNELLE%>.rechercherAffilie";
			document.forms[0].submit();
			
			//checkPeriodeApgPeriodeAff();
		}		
	}
	
	function checkPeriodeApgPeriodeAff(){
		parent.warningObj.text = "";
		<%
		if(JadeDateUtil.isGlobazDate(viewBean.getDateFinAffiliation())){%>
				parent.warningObj.text = "<%=FWMessageFormat.format(viewBean.getSession().getLabel("JSP_DATE_AFFILIATION_NON_VIDE_NUMAFFILIE"),viewBean.getNumAffilieEmployeur() ,globaz.prestation.tools.PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(viewBean.getDateFinAffiliation()))%>"
		<%}else {%>
			
			<%if(!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdAffilieEmployeur())){%>
			
				// l'affilie ne cotise pas a notre caisse
				<%if(!viewBean.hasCotisationsInOurCaisseForPeriodeDroit()){%>
				
					parent.warningObj.text = "<ct:FWLabel key='JSP_AFFILIE_COTISE_PAS_CAISSE'/></br>";
				<%}%>
			
				// l'affilie n'a pas de salarie
				<%if(!viewBean.hasCotisationsAssuranceParitaire()){%>
				
					parent.warningObj.text += "<ct:FWLabel key='JSP_AFFILIE_SANS_SALARIE'/>";
				<%}%>
			<%}%>
					
		<%}%>
		
		// on set automatiquement le flag LAMat a true si
		// - le droit est un droit Mat
		// - la caisse donne la LAMat
		// - l'affilie cotise une assurance LAMat
		<%if("true".equals(globaz.prestation.application.PRAbstractApplication.getApplication(globaz.apg.application.APApplication.DEFAULT_APPLICATION_APG).getProperty("isDroitMaterniteCantonale")) && 
		     globaz.apg.util.TypePrestation.TYPE_MATERNITE.equals(viewBean.getTypePrestation())) {%>
		     
		     <%if(viewBean.hasCotisationAssuranceLAMat()){%>
				document.forms[0].elements('hasLaMatPrestations').checked = true;
			<%}else{%>
				document.forms[0].elements('hasLaMatPrestations').checked = false;
			<%}%>
		<%}%>
		
		parent.showWarnings();
		parent.warningObj.text = "";
	}
	
	function isEmployeurActif(){
		<%
		boolean dateDebutDroitGreaterOrEqualDateDebut = true;
		boolean dateDebutDroitLowerOrEqualDateFin = true;
		
		if(!globaz.jade.client.util.JadeStringUtil.isNull(viewBean.getDateDebutAffiliation())){
			dateDebutDroitGreaterOrEqualDateDebut = globaz.globall.db.BSessionUtil.compareDateFirstGreaterOrEqual(viewBean.getSession(), 
			                                                                                                      viewBean.getDateDebutDroit(), 
			                                                                                                      viewBean.getDateDebutAffiliation());
		}
		
		if(!globaz.jade.client.util.JadeStringUtil.isNull(viewBean.getDateFinAffiliation())){
			dateDebutDroitLowerOrEqualDateFin = globaz.globall.db.BSessionUtil.compareDateFirstLowerOrEqual(viewBean.getSession(), 
		                                                                           							viewBean.getDateDebutDroit(), 
		                                                                           							viewBean.getDateFinAffiliation());
		}

		if(!(dateDebutDroitGreaterOrEqualDateDebut &&
		    (dateDebutDroitLowerOrEqualDateFin || 
		      globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getDateFinAffiliation())))){%>
			return false;
		<%} else {%>
			return true;
		<%}%>
	}
	
    function boutonCollaborateurAgricoleChange() {
    	if(document.forms[0].elements('isCollaborateurAgricole').checked){
	  		document.forms[0].elements('isTravailleurAgricole').checked = false;
	  	}
	}
	
    function boutonTravailleurAgricoleChange() {
    	if(document.forms[0].elements('isTravailleurAgricole').checked){
	  		document.forms[0].elements('isCollaborateurAgricole').checked = false;
	  	}
	}
	
	function checkDepartement(){
		// l'affilie possede une liste de departement
		<%if(viewBean.getDepartementsData().size()>1){%>
			// pas de departement selectionne
			if("" == document.forms[0].elements('idParticulariteEmployeur').value){
				parent.warningObj.text = "<ct:FWLabel key='JSP_DEPARTEMENT_MANQUANT'/>";
				parent.showWarnings();
			}
		<%}%>
	}
  function checkACM(){
	  // Si c'est un droit Pandémie => Pas de ACM.
	  <%if(viewBean.isPandemie() || viewBean.isPaternite()){%>
		  $('input[type="checkbox"][name="hasAcmAlphaPrestations"]').prop('checked', false);
	  <%}%>
  }
	
	function checkIbanEmployeur(){
		// Si l'employeur ne possède pas d'IBAN (IBAN vaut CH0000000000000000000)
		<%if(!viewBean.getIsIbanValide()){%>
			var isPorteEnCompte = $('#isPorteEnCompte').is(':checked');
			if(!isPorteEnCompte){
				parent.warningObj.text = "<ct:FWLabel key='JSP_IBAN_INVALIDE'/>";
				parent.showWarnings();
			}
		<%}%>
	}
	
	function toFloat(input){
		var output = input.replace("'", ""); 
		return output;
	}
	
	function OnClickACM(){
		var isACMChecked = $('input[type="checkbox"][name="hasAcmAlphaPrestations"]').is(":checked");
		// si checkbox ACM a été clické et sa valeur est coché, alors on coche obligatoiremment ACM2
		// si checkbox ACM a été clické et sa valeur est décoché, alors on décoche obligatoiremment ACM2
		$('input[type="checkbox"][name="hasAcm2AlphaPrestations"]').prop('checked', isACMChecked);
	}
	
	function OnClickACM2(){
		var isACMChecked = $('input[type="checkbox"][name="hasAcmAlphaPrestations"]').is(':checked');
		var isACM2Checked = $('input[type="checkbox"][name="hasAcm2AlphaPrestations"]').is(':checked');
		
		// si checkbox ACM2 a été clické et sa valeur est coché ET ACM est décoché, alors on coche obligatoirement ACM
		if(!isACMChecked && isACM2Checked){
			$('input[type="checkbox"][name="hasAcmAlphaPrestations"]').prop('checked', true);
		}
	}
	
	function showisAMATFExcluded(){
		var isAMATFExluded = $('input[type="checkbox"][name="hasLaMatPrestations"]').is(':checked');
		var excludeAmat = document.getElementById("idisAMATFExcluded");
		if(isAMATFExluded){
			excludeAmat.style.visibility = "visible";
		}else{
			excludeAmat.style.visibility = "hidden";
			var isAMATFExluded = $('input[type="checkbox"][name="isAMATFExcluded"]').prop("checked", false);
		}
	}
	
	
	function OnClickPorterEnCompte(){
		var isPorteEnCompte = $('#isPorteEnCompte').is(':checked');
		if(isPorteEnCompte){
			$("#personnelDeclarePar").show();
		} else {
			$("#personnelDeclarePar").hide();
		}
	}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_EMPLOYEUR"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><LABEL for="nomEmployeur"><ct:FWLabel key="JSP_EMPLOYEUR"/></LABEL></TD>
							<TD>
								<INPUT type="text" name="nomEmployeur" value="<%=viewBean.getNomEmployeur()%>" class="disabled" readonly>
								<% if (viewBean.isModifiable()) { %>
									<ct:FWSelectorTag name="selecteurEmployeur"	
										methods="<%=viewBean.getMethodesSelecteurEmployeur()%>"
										providerApplication="pyxis"
										providerPrefix="TI"
										providerAction="pyxis.tiers.tiers.chercher"
										target="fr_main"
										redirectUrl="<%=mainServletPath%>"/>
								<% } %>
								
								<INPUT type="hidden" name="idTiersEmployeur" value="<%=viewBean.getIdTiersEmployeur()%>">
								<INPUT type="hidden" name="dateDebutAffiliation" value="<%=viewBean.getDateDebutAffiliation()%>">
								<INPUT type="hidden" name="dateFinAffiliation" value="<%=viewBean.getDateFinAffiliation()%>">
								<INPUT type="hidden" name="genreAffiliation" value="<%=viewBean.getGenreAffiliation()%>">
							</TD>
							<TD><LABEL for="idAffilie"><ct:FWLabel key="JSP_NO_AFFILIE"/></LABEL></TD>
							<TD>
								<% if (viewBean.isRetourDesTiers()){%>		
									<ct:FWListSelectTag data="<%=viewBean.getAffiliationsEmployeur()%>" name="idAffilieEmployeur" defaut="<%=viewBean.getIdAffilieEmployeur()%>"/>
									<script language="javascript">
									 	  	firstElement = document.getElementById("selecteurEmployeur");
									 	  	secondElement  = document.getElementById("idAffilieEmployeur");
									 	  	firstElement.onblur = function() {secondElement.focus();};				 	  	
								 	</script>	
								<%} else {%>
									<INPUT type="text" name="numAffilieEmployeur" value="<%=viewBean.getNumAffilieEmployeur()%>"  onchange="rechercherAffilie(value)" class="montant">
									<script language="javascript">
									 	  	firstElement = document.getElementById("selecteurEmployeur");
									 	  	secondElement  = document.getElementById("numAffilieEmployeur");
									 	  	//TODO: Provoquait une erreure
									 	  	//firstElement.onblur = function() {secondElement.focus();};				 	  	
								 	</script>		
									<INPUT type="hidden" name="idAffilieEmployeur" value ="<%=viewBean.getIdAffilieEmployeur()%>">
								<%}%>
							</TD>
							<TD><LABEL for="adressePaiement"><ct:FWLabel key="JSP_ADRESSE_DE_PAIEMENT"/></LABEL></TD>
							
							<TD>
								<INPUT type="radio" name="isVersementEmployeur" value="on" <%=viewBean.getIsVersementEmployeur().booleanValue()?"CHECKED":""%> onclick="manageAdressePaiement()" disabled=<%=viewBean.getIsVersementEmployeur().booleanValue()?"disabled":""%>> <ct:FWLabel key="JSP_EMPLOYEUR"/>
							    <INPUT type="radio" name="isVersementEmployeur" value="" <%=!viewBean.getIsVersementEmployeur().booleanValue()?"CHECKED":""%> onclick="manageAdressePaiement()" disabled=<%=viewBean.getIsVersementEmployeur().booleanValue()?"disabled":""%>> <ct:FWLabel key="JSP_ASSURE"/>
							</TD>
						</TR>
						<TR>
							<TD>
								<LABEL for="isIndependant"><ct:FWLabel key="JSP_INDEPENDANT_NON_ACTIF"/></LABEL>
								<INPUT type="checkbox" name="isIndependant" value="on" onclick="boutonIndependantChange();" <%=viewBean.getIsIndependant().booleanValue()?"CHECKED":""%>>
							</TD>
							<TD id="blockAnneeTaxation">
								<LABEL><ct:FWLabel key="JSP_ANNEE_TAXATION"/>&nbsp;<IMG TITLE="<%=viewBean.getSession().getLabel("JSP_ANNEE_TAXATION_INFO")%>" SRC="<%=request.getContextPath()%>/images/prepare.gif" border="0"/></LABEL>
								<INPUT type="text" name="anneeTaxation" class="numeroCourt" value="<%=viewBean.getAnneeTaxation()%>"/>
							</TD>
							<TD id="blockWithoutAnneeTaxation">&nbsp;</TD>
							<TD><LABEL for="revenuIndependant"><ct:FWLabel key="JSP_REVENU_INDEPENDANT"/></LABEL></TD>
							<TD><INPUT type="text" name="revenuIndependant" value="<%=viewBean.getRevenuIndependant()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="montantIndependantChange(); return filterCharForFloat(window.event);"></TD>
							<%if (!viewBean.isPrestationAcmAlfaEnable().booleanValue()) {%>
							<TD>
								<LABEL for="isAllocationMax"><ct:FWLabel key="JSP_MONTANT_MAX"/></LABEL>
								<INPUT type="checkbox" name="isAllocationMax" value="on" <%=viewBean.getIsAllocationMax().booleanValue()?"CHECKED":""%>>
							</TD>
							<%}else{%>
							<TD>
								<INPUT type="hidden" name="isAllocationMax" value="">
							</TD>
							<%}%>
							<TD>
								<%if (viewBean.isDroitMaterniteCantonaleFromProperties() && 
								      globaz.apg.util.TypePrestation.TYPE_MATERNITE.equals(viewBean.getTypePrestation())) {%>
									<LABEL for="hasLaMatPrestations"><ct:FWLabel key="JSP_LAMAT" /></LABEL>
									<INPUT id="idhasLaMatPrestations" type="checkbox" name="hasLaMatPrestations" onclick="showisAMATFExcluded()" value="on" <%=viewBean.getHasLaMatPrestations().booleanValue()?"CHECKED":""%>>
									<SPAN id="idisAMATFExcluded"><LABEL for="isAMATFExcluded"><ct:FWLabel key="JSP_EXCLUDE_AMATFEDERAL" /></LABEL>
									<INPUT type="checkbox" name="isAMATFExcluded" value="on" <%=viewBean.getIsAMATFExcluded().booleanValue()?"CHECKED":""%>></SPAN>
								<%}else{%>
									<INPUT type="hidden" name="hasLaMatPrestations" value="">
									<INPUT type="hidden" name="isAMATFExcluded" value="">
								<%}%>
								<!-- ************************* D0063 ************************** -->
								<!-- Sélection des types ACM selon propertie "apg.type.de.prestation" -->
								
								<DIV class="prestationAcmAlpha">				
										<LABEL for="hasAcmAlphaPrestations"><ct:FWLabel key="JSP_ACM" /></LABEL>
										<INPUT type="checkbox" onclick="OnClickACM()" name="hasAcmAlphaPrestations">
								</DIV>
								<DIV class="prestationAcm2Alpha">
									<LABEL for="hasAcm2AlphaPrestations"><ct:FWLabel key="JSP_ACM2" /></LABEL>
									<INPUT type="checkbox" onclick="OnClickACM2()" name="hasAcm2AlphaPrestations">
								</DIV>
							</TD>
						</TR>
						<TR><TD colspan="6"><HR></TD>
						</TR>
							<TR class="prestationAcmNe">
								<TD><LABEL for="nomAssociation"><ct:FWLabel key="JSP_ASSOCIATION" /></LABEL></TD>
								<TD>
									<input name="nomAssociation" type="text" class="disabled" value="<%=viewBean.getNomAssociation()%>" readonly>	
								</TD>
								
								<TD><LABEL for="montantJournalierAcmNe"><ct:FWLabel key="JSP_TOTAL_ACM" /></LABEL></TD>
								<TD><INPUT type="text" name="montantJournalierAcmNe" value="<%=viewBean.getMontantJournalierAcmNe()%>"
										class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" >
								</TD>
							</TR>
							<TR class="prestationAcmNe"><TD colspan="6"><HR></TD></TR>
							<TR class="prestationNonAcm"><TD><INPUT type="hidden" name="hasAcmAlphaPrestations" value="" ></TD></TR>
							<TR class="prestationNonAcm2"><TD><INPUT type="hidden" name="hasAcm2AlphaPrestations" value="" ></TD></TR>
													
							<INPUT type="hidden" id="nomTypePrestationAcmAlpha" value="<%=globaz.apg.enums.APTypeDePrestation.ACM_ALFA.getNomTypePrestation()%>" >
							<INPUT type="hidden" id="nomTypePrestationAcmNe" value="<%=globaz.apg.enums.APTypeDePrestation.ACM_NE.getNomTypePrestation()%>" >				
							<INPUT type="hidden" id="csAssuranceAssociation" name="csAssuranceAssociation"  value="<%=viewBean.getCsAssuranceAssociation()%>">
													
						<!-- ************************* D0063 ************************** -->
						<TR>
							<TD><LABEL for="heuresSemaine"><ct:FWLabel key="JSP_HEURES"/></LABEL></TD>
							<TD><INPUT type="text" name="heuresSemaine" value="<%=viewBean.getHeuresSemaine()%>" class="numeroCourt" style="text-align:right" onchange="validateFloatNumber(this);" onkeypress="montantHoraireChange(); return filterCharForFloat(window.event);"><ct:FWLabel key="JSP_PAR_SEMAINE"/></TD>
							<TD><LABEL for="salaireHoraire"><ct:FWLabel key="JSP_SALAIRE_HORAIRE"/></LABEL></TD>
							<TD><INPUT type="text" name="salaireHoraire" value="<%=viewBean.getSalaireHoraire()%>" class="montant" onchange="validateFloatNumber(this);checkMontantHoraire();" onkeypress="montantHoraireChange(); return filterCharForFloat(window.event);"></TD>
                            <TD><LABEL for="nbJoursIndemnise"><ct:FWLabel key="JSP_NOMBRE_JOURS_COMPTABILIS"/></LABEL></TD>
                            <%if(viewBean.getTypeDemande().isProcheAidant()){%>
                            <TD>
                                <INPUT type="text" id="nbJoursIndemnise" style="width:30px" name="nbJoursIndemnise" value="<%=viewBean.calculerNbjourDuDroit()%>" /> /
                                <INPUT type="text" id="nbJoursPaye" readonly disabled style="width:30px" name="nbJoursPaye" value="<%=viewBean.calculerNbjourDuDroit()%>" />
                            </TD>
                            <%}%>
						</TR>
						<TR>
							<TD class="withoutAdressePaiement" colspan="2">&nbsp;</TD>
							<TD class="withAdressePaiement"><ct:FWLabel key="JSP_ADRESSE_DE_PAIEMENT"/><input type="hidden" name="crNomPrenom" value="crNomPrenom"/><input type="hidden" name="nomEmployeurAvecVirgule" value="<%=viewBean.getNomEmployeurAvecVirgule()%>"/></TD>
							<% Object[] adresseParams= new Object[]{new String[]{"idTiersEmployeur","idTiers"}, new String[]{"nomEmployeurAvecVirgule","cr1Text"}, new String[]{"crNomPrenom", "cr1"} }; %>
							
							<TD class="withAdressePaiement">
								<ct:FWSelectorTag name="selecteurAdresses"
									methods="<%=viewBean.getMethodesSelectionAdressePaiement()%>"
									providerApplication="pyxis"
									providerPrefix="TI"
									providerAction="pyxis.adressepaiement.adressePaiement.chercher"
									providerActionParams ="<%=adresseParams%>"
									target="fr_main"
									redirectUrl="<%=mainServletPath%>"/>
							</TD>
							<TD><LABEL for="salaireMensuel"><ct:FWLabel key="JSP_SALAIRE_MENSUEL"/></LABEL></TD>
							<TD><INPUT type="text" name="salaireMensuel" value="<%=viewBean.getSalaireMensuel()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="montantMensuelChange(); return filterCharForFloat(window.event);"></TD>
							<TD><LABEL for="autreSalaire"><ct:FWLabel key="JSP_AUTRE_SALAIRE"/></LABEL></TD>
							<TD>
								<INPUT type="text" name="autreSalaire" value="<%=viewBean.getAutreSalaire()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="montantAutreChange(); return filterCharForFloat(window.event);">
								<ct:select name="periodiciteAutreSalaire" defaultValue="<%=viewBean.getPeriodiciteAutreSalaire()%>" onchange="periodiciteAutreSalaireChangee()">
									<ct:optionsCodesSystems csFamille="APPERSITP">
										<ct:excludeCode code="<%=globaz.prestation.api.IPRSituationProfessionnelle.CS_PERIODICITE_HEURE%>"/>
										<ct:excludeCode code="<%=globaz.prestation.api.IPRSituationProfessionnelle.CS_PERIODICITE_MOIS%>"/>
									</ct:optionsCodesSystems>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD class="withoutAdressePaiement" colspan="2">&nbsp;</TD>
							<TD class="withAdressePaiement" colspan="2" rowspan="4">
								<PRE style="font-size:1em"><%=viewBean.getAdressePaiementEmployeur()%></PRE>
							</TD>
							<TD><LABEL for="autreRemuneration"><ct:FWLabel key="JSP_AUTRE_REMUNERATION"/></LABEL></TD>
							<TD><INPUT type="text" name="autreRemuneration" value="<%=viewBean.getAutreRemuneration()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
							<TD	colspan="2">
								<INPUT type="checkbox" name="isPourcentAutreRemun" value="on" <%=viewBean.getIsPourcentAutreRemun().booleanValue()?"CHECKED":""%> onclick="pourcentClick(this, document.forms[0].elements('periodiciteAutreRemun'))">
								<ct:FWLabel key="JSP_POURCENT"/>
								<ct:select name="periodiciteAutreRemun" defaultValue="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getPeriodiciteAutreRemun())?globaz.prestation.api.IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE:viewBean.getPeriodiciteAutreRemun()%>" disabled="true">
									<ct:optionsCodesSystems csFamille="APPERSITP"/>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD class="withoutAdressePaiement" colspan="2"></TD>
							<TD><LABEL for="salaireNature"><ct:FWLabel key="JSP_SALAIRE_NATURE"/></LABEL></TD>
							<TD><INPUT type="text" name="salaireNature" value="<%=viewBean.getSalaireNature()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
							<TD colspan="3"><ct:FWCodeSelectTag codeType="APPERSITP" defaut="<%=viewBean.getPeriodiciteSalaireNature()%>" name="periodiciteSalaireNature"/></TD>
						</TR>
						<TR><TD class="withAdressePaiement" colspan="6">&nbsp;</TD></TR>
						<TR><TD class="withAdressePaiement" colspan="6">&nbsp;</TD></TR>
						<TR><TD colspan="6"><HR></TD></TR>
						<TR>
							<TD><LABEL for="isCollaborateurAgricole"><ct:FWLabel key="JSP_COLL_AGRICOLE"/></LABEL></TD>
							<TD><INPUT type="checkbox" name="isCollaborateurAgricole" value="on"  onclick="boutonCollaborateurAgricoleChange();" <%=viewBean.getIsCollaborateurAgricole().booleanValue()?"CHECKED":""%>></TD>
							<TD><LABEL for="isTravailleurAgricole"><ct:FWLabel key="JSP_TRAV_AGRICOLE"/></LABEL></TD>
							<TD><INPUT type="checkbox" name="isTravailleurAgricole" value="on" onclick="boutonTravailleurAgricoleChange();" <%=viewBean.getIsTravailleurAgricole().booleanValue()?"CHECKED":""%>></TD>
							<%if (!globaz.apg.util.TypePrestation.TYPE_MATERNITE.equals(viewBean.getTypePrestation())) {%>
								<TD><LABEL for="isAllocationExploitation"><ct:FWLabel key="JSP_ALLOC_EXPLOI"/></LABEL></TD>
								<TD><INPUT type="checkbox" name="isAllocationExploitation" value="on" <%=viewBean.getIsAllocationExploitation().booleanValue()?"CHECKED":""%>></TD>
							<%}%>
						</TR>
					</TBODY>
					<TBODY id="specialEmployeur">
						<TR><TD colspan="6"><HR></TD></TR>
						<TR>
							<TD colspan="2"></TD>
							<TD><LABEL for="montantVerse"><ct:FWLabel key="JSP_SALAIRE_VERSE"/></LABEL></TD>
							<TD><INPUT type="text" name="montantVerse" value="<%=viewBean.getMontantVerse()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
							<TD	colspan="2">
								<INPUT type="checkbox" name="isPourcentMontantVerse" value="on" <%=viewBean.getIsPourcentMontantVerse().booleanValue()?"CHECKED":""%> onclick="pourcentClick(this, document.forms[0].elements('periodiciteMontantVerse'))">
								<ct:FWLabel key="JSP_POURCENT"/>
								<ct:select name="periodiciteMontantVerse" defaultValue="<%=viewBean.getPeriodiciteMontantVerse()%>" disabled="true">
									<ct:optionsCodesSystems csFamille="APPERSITP"/>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<% if (viewBean.isTypeAPG()) { %>
							<TD><LABEL for="dateDebut"><ct:FWLabel key="JSP_DATE_DEBUT_SALAIRE"/></LABEL></TD>
							<TD><ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>"/></TD>
							<TD><LABEL for="dateFin"><ct:FWLabel key="JSP_DATE_FIN_SALAIRE"/></LABEL></TD>
							<TD colspan="3"><ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>"/></TD>
							<% } else { %>
							<TD colspan="6"></TD>
							<% } %>
						</TR>
					</TBODY>
					<TBODY>
						<TR><TD colspan="6"><HR></TD></TR>
						<TR>
							<TD><LABEL for="dateFinContrat"><ct:FWLabel key="JSP_DATE_FIN_CONTRAT"/></LABEL></TD>
							<TD><ct:FWCalendarTag name="dateFinContrat" value="<%=viewBean.getDateFinContrat()%>"/></TD>
							<TD	colspan="2">
								<INPUT type="hidden" name="<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>" value="<%=viewBean.getIdDroit()%>">
							</TD>
							<% if (viewBean.isDepartementEnabled()) { %>
							<TD><LABEL for="idParticularite"><ct:FWLabel key="JSP_DEPARTEMENT"/></LABEL></TD>
							<TD><ct:FWListSelectTag data="<%=viewBean.getDepartementsData()%>" defaut="<%=viewBean.getIdParticulariteEmployeur()%>" name="idParticulariteEmployeur"/></TD>
							<% } else { %>
							<TD colspan="2"></TD>
							<% } %>
						</TR>
				<%if(viewBean.isModuleActifForPorterEnCompte()){ %>
					</TBODY>
					<TBODY>
						<TR><TD colspan="6"><HR></TD></TR>
						<TR>
							<TD><LABEL for="isPorteEnCompte"><ct:FWLabel key="JSP_PORTE_EN_COMPTE"/></LABEL></TD>
							<TD colspan="2">
								<INPUT id="isPorteEnCompte" type="checkbox" name="isPorteEnCompte" value="on" onclick="OnClickPorterEnCompte()" <%=viewBean.getIsPorteEnCompte().booleanValue()?"CHECKED":""%>>
								&nbsp;&nbsp;
								<span id="personnelDeclarePar"><%=viewBean.getPersonnelDeclarePar(request.getContextPath(), viewBean.getDateDebutDroit())%></span>
							</TD>
							<TD colspan="3">
						</TR>
				<%} %>
				
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
