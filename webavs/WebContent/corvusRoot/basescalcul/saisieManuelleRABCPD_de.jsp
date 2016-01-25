	<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_SAM_D"

	idEcran="PRE0038";

	globaz.corvus.vb.basescalcul.RESaisieManuelleRABCPDViewBean viewBean = (globaz.corvus.vb.basescalcul.RESaisieManuelleRABCPDViewBean) session.getAttribute("viewBean");

	String noDemandeRente = request.getParameter("noDemandeRente");
	String idTierRequerant = request.getParameter("idTierRequerant");

	java.util.List codesPrestations = globaz.prestation.tools.PRCodeSystem.getCUCS(viewBean.getSession(), globaz.corvus.api.basescalcul.IRERenteAccordee.CS_GROUPE_GENRE_PRESTATION_02_0);
	java.util.List codesCasSpeciaux = globaz.prestation.tools.PRCodeSystem.getCUCS(viewBean.getSession(), globaz.corvus.api.basescalcul.IRERenteAccordee.CS_GROUPE_CAS_SPECIAUX_RENTE_01);
	java.util.Iterator iterator4 = codesCasSpeciaux.iterator();

	bButtonUpdate = false;
	bButtonDelete = false;
	bButtonCancel = false;

	String params = "&provenance1=TIERS";
		  params += "&provenance2=CI";
		  params += "&extraParam1=forceSingleAdrMode";

	String jspLocation = servletContext + "/corvusRoot/numeroSecuriteSocialeSF_select.jsp";


%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionssaisiemanuelle" showTab="menu">
		<ct:menuSetAllParams key="noDemandeRente" value="<%=noDemandeRente%>"/>
		<ct:menuSetAllParams key="idTierRequerant" value="<%=idTierRequerant%>"/>
		<ct:menuSetAllParams key="noBaseCalcul" value="<%=viewBean.getIdBasesCalcul()%>"/>
</ct:menuChange>

<script language="JavaScript">

  function readOnly(flag) {
  	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
    for(i=0; i < document.forms[0].length; i++) {
        if (!document.forms[0].elements[i].readOnly &&
        	document.forms[0].elements[i].className != 'forceDisable' &&
        	document.forms[0].elements[i].type != 'hidden') {
            document.forms[0].elements[i].disabled = flag;
        }
    }
  }

  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_SAISIE_MANUELLE_RABCPD%>.ajouterRABCPD";
  }

  function validate() {

	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_SAISIE_MANUELLE_RABCPD%>.ajouterRABCPD";
	document.forms[0].submit();

  }

  function init(){
  }

  function updateTierBeneficiaire() {
	isBenefUpdate = true;
	document.all('updateTiersBeneficiaire').style.display = 'block';
	document.all('noUpdateTiersBeneficiaire').style.display = 'none';
  }

  function postInit(){

	  	var selectedIndex = "<%=viewBean.getFractionRenteRenteAccordee()%>";
	  	var $fractionRenteCombo = $(document.all('fractionRenteRenteAccordeeSelect'));
	  	$fractionRenteCombo.prop("selectedIndex", selectedIndex);
	  	
	  	$fractionRenteCombo.change(function () {
	  		var value = $(this).prop("selectedIndex");
	  		var treatedValue = (parseInt(value) > 0) ? value : '';
		  	var $destination = $(document.all('fractionRenteRenteAccordee'));
	  		
		  	$destination.val(treatedValue); 
		});	  	
	  
		document.all('updateTiersBeneficiaire').style.display = 'none';
		document.all('noUpdateTiersBeneficiaire').style.display = 'block';
		document.getElementById("imageOKKOCodePrestation").src='<%=request.getContextPath()+"/images/erreur.gif"%>';
		metAJourCodeCasSpeciaux();
		
		var monEl = document.getElementById("anchor_dateDebutDroitRenteAccordee");
		jscss("add", monEl, "hidden", null);
		monEl = document.getElementById("anchor_debAnticipationRenteAccordee");
		jscss("add", monEl, "hidden", null);
		monEl = document.getElementById("anchor_finDroitRenteAccordee");
		jscss("add", monEl, "hidden", null);
  }

  	function nssChange(tag) {
		if(tag.select==null) {

		}else {

			var element = tag.select.options[tag.select.selectedIndex];

			if (element.id!=null){
				document.getElementById("idTiersBeneficiaire").value=element.idAssure;
			}
		}

			document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_SAISIE_MANUELLE_RABCPD%>.afficher";
			document.forms[0].submit();
	}

  	function metAJourCodePrestation(){
 		<%java.util.Iterator iterator3 = codesPrestations.iterator();
 		while(iterator3.hasNext()){
 		String cu = ((String)(iterator3.next())).substring(0,2);
 		String cs = ((String)(iterator3.next())).substring(0,2);%>
 		if (document.forms[0].elements('genrePrestationRenteAccordee').value == "<%=cu%>"){
 			document.getElementById("imageOKKOCodePrestation").src='<%=request.getContextPath()+"/images/ok.gif"%>';
 		} else
 		<%}%>
 		<%if (codesPrestations.size() != 0){%>
 		{
 			document.getElementById("imageOKKOCodePrestation").src='<%=request.getContextPath()+"/images/erreur.gif"%>';
 			//document.forms[0].elements('codePrestation').value = "";
 			}
 		<%}else{%>
 			document.getElementById("imageOKKOCodePrestation").src='<%=request.getContextPath()+"/images/erreur.gif"%>';
 			//document.forms[0].elements('codePrestation').value = "";
 		<%}%>
 	}

 	function metAJourCodeCasSpeciaux(){
 		var areCodesCasSpeciauxValid = true;

 		var codeCasSpeciaux1 = document.forms[0].elements('codesCasSpecialRenteAccordee1').value;
 		var codeCasSpeciaux2 = document.forms[0].elements('codesCasSpecialRenteAccordee2').value;
 		var codeCasSpeciaux3 = document.forms[0].elements('codesCasSpecialRenteAccordee3').value;
 		var codeCasSpeciaux4 = document.forms[0].elements('codesCasSpecialRenteAccordee4').value;
 		var codeCasSpeciaux5 = document.forms[0].elements('codesCasSpecialRenteAccordee5').value;

 		// check de la validite du cs1
 		if(codeCasSpeciaux1!=""){
 			var existCodeCS1 = false;
	 		<%iterator4 = codesCasSpeciaux.iterator();
	 		while(iterator4.hasNext()){
	 			String cu = ((String)(iterator4.next())).substring(0,2);
	 			String cs = ((String)(iterator4.next())).substring(0,2);%>
		 		if (codeCasSpeciaux1 == "<%=cu%>"){
		 			existCodeCS1 = true;
		 		}
	 		<%}%>
 			if(! existCodeCS1){
 				areCodesCasSpeciauxValid = false;
 			}
 		}

 		// check de la validite du cs2
 		if(codeCasSpeciaux2!=""){
 			var existCodeCS2 = false;
	 		<%iterator4 = codesCasSpeciaux.iterator();
	 		while(iterator4.hasNext()){
	 			String cu = ((String)(iterator4.next())).substring(0,2);
	 			String cs = ((String)(iterator4.next())).substring(0,2);%>
		 		if (codeCasSpeciaux2 == "<%=cu%>"){
		 			existCodeCS2 = true;
		 		}
	 		<%}%>
 			if(! existCodeCS2){
 				areCodesCasSpeciauxValid = false;
 			}
 		}

 		// check de la validite du cs3
 		if(codeCasSpeciaux3!=""){
 			var existCodeCS3 = false;
	 		<%iterator4 = codesCasSpeciaux.iterator();
	 		while(iterator4.hasNext()){
	 			String cu = ((String)(iterator4.next())).substring(0,2);
	 			String cs = ((String)(iterator4.next())).substring(0,2);%>
		 		if (codeCasSpeciaux3 == "<%=cu%>"){
		 			existCodeCS3 = true;
		 		}
	 		<%}%>
 			if(! existCodeCS3){
 				areCodesCasSpeciauxValid = false;
 			}
 		}

 		// check de la validite du cs4
 		if(codeCasSpeciaux4!=""){
 			var existCodeCS4 = false;
	 		<%iterator4 = codesCasSpeciaux.iterator();
	 		while(iterator4.hasNext()){
	 			String cu = ((String)(iterator4.next())).substring(0,2);
	 			String cs = ((String)(iterator4.next())).substring(0,2);%>
		 		if (codeCasSpeciaux4 == "<%=cu%>"){
		 			existCodeCS4 = true;
		 		}
	 		<%}%>
 			if(! existCodeCS4){
 				areCodesCasSpeciauxValid = false;
 			}
 		}

 		// check de la validite du cs5
 		if(codeCasSpeciaux5!=""){
 			var existCodeCS5 = false;
	 		<%iterator4 = codesCasSpeciaux.iterator();
	 		while(iterator4.hasNext()){
	 			String cu = ((String)(iterator4.next())).substring(0,2);
	 			String cs = ((String)(iterator4.next())).substring(0,2);%>
		 		if (codeCasSpeciaux5 == "<%=cu%>"){
		 			existCodeCS5 = true;
		 		}
	 		<%}%>
 			if(! existCodeCS5){
 				areCodesCasSpeciauxValid = false;
 			}
 		}

 		if (areCodesCasSpeciauxValid){
 			document.getElementById("imageOKKOCodeCasSpeciaux").src='<%=request.getContextPath()+"/images/ok.gif"%>';
		}else{
 			document.getElementById("imageOKKOCodeCasSpeciaux").src='<%=request.getContextPath()+"/images/erreur.gif"%>';
 		}

 	}

    function formatAAMMField(field){
  	var value = field.value;

  	if(value != "" && value.length<5){

  		if(value.indexOf(".") == -1){

		  	while(value.length<4){
		  		value = "0" + value;
		  	}
  		}else{

		  	while(value.length<5){
		  		value = "0" + value;
		  	}
  		}

  		if(value.length==4 && value.indexOf(".") == -1){
  			field.value = value.substring(0, 2) + "." + value.substring(2, 4);
  		}
  	}
  }

  function formatAdField(field){
    var value = field.value;

  	if(value != "" && value.length<3){

  		if(value.indexOf(".") == -1){

		  	while(value.length<2){
		  		value = "0" + value;
		  	}
  		}else{

		  	while(value.length<3){
		  		value = "0" + value;
		  	}
  		}

  		if(value.length==2 && value.indexOf(".") == -1){
  			field.value = value.substring(0, 1) + "." + value.substring(1, 2);
  		}
  	}
  }
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_SAM_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
						<TD colpsan="6">
						<TABLE cellspacing="2">
								<TR>
									<TD colspan="6">
										<input type="hidden" name="isBasesCalculModifiable" value="<%=viewBean.getIsBasesCalculModifiable().booleanValue()%>">
										<input type="hidden" name="idTierRequerant" value="<%=idTierRequerant%>">
											<re:PRDisplayRequerantInfoTag
												session="<%=(globaz.globall.db.BSession)controller.getSession()%>"
												idTiers="<%=idTierRequerant%>"
												style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED%>"/>
									</TD>
								</TR>
								<TBODY id="updateTiersBeneficiaire" style="display:none;">
									<TR>
										<TD colspan="6">
										<b>Bénéficiaire</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

											<ct1:nssPopup name="nssTiersBeneficiairea" onFailure="" onChange="nssChange(tag);" params="<%=params%>"
												  value="<%=viewBean.getNssTiersBeneficiaireWithoutPrefix()%>" newnss=""
												  jspName="<%=jspLocation%>" avsMinNbrDigit="3" nssMinNbrDigit="3" avsAutoNbrDigit="11" nssAutoNbrDigit="10"  />
											<input type="hidden" name="idTiersBeneficiaire" value="<%=viewBean.getIdTiersBeneficiaire()%>">
										</TD>
									</TR>
								</TBODY>
								<TBODY id="noUpdateTiersBeneficiaire" style="display:none;">
									<TR>
										<TD colspan="6">
											<re:PRDisplayRequerantInfoTag
												session="<%=(globaz.globall.db.BSession)controller.getSession()%>"
												idTiers="<%=viewBean.getIdTiersBeneficiaire()%>"
												style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_BEN%>"/>

												&nbsp;&nbsp;
											<a href="#" onclick="updateTierBeneficiaire();"><ct:FWLabel key="JSP_BAC_R_MODIFIER"/></a>
										</TD>
									</TR>
								</TBODY>
								<TR>
									<TD colspan="6">&nbsp;</TD>
								</TR>
								<TR>
									<TD colspan="6">&nbsp;</TD>
								</TR>
								<TR>
									<TD><LABEL for="noDemande"><ct:FWLabel key="JSP_BAC_D_DEMANDE_NO"/></LABEL></TD>
									<TD><INPUT type="text" name="idDemandeRente" value="<%=noDemandeRente%>" class="disabled" readonly>
										<INPUT type="hidden" name="noDemandeRente" value="<%=noDemandeRente%>" class="disabled" readonly>
									<TD>
										<LABEL for="addressePaiement"><ct:FWLabel key="JSP_ADDRESSE_PAIEMENT"/></LABEL>
										<INPUT type="hidden" name="idTiersAdressePmtIC" value="<%=viewBean.getIdTiersAdressePmtIC()%>">
									</TD>
									<TD rowspan="2">
										<PRE><span class="IJAfficheText"><%=viewBean.getCcpOuBanqueFormatte()%></span></PRE>
										<PRE><span class="IJAfficheText"><%=viewBean.getAdresseFormattee()%></span></PRE>
									</TD>
									<TD>
										<ct:FWSelectorTag
											name="selecteurAdresses"

											methods="<%=viewBean.getMethodesSelectionAdressePaiement()%>"
											providerApplication="pyxis"
											providerPrefix="TI"
											providerAction="pyxis.adressepaiement.adressePaiement.chercher"
											target="fr_main"
											redirectUrl="<%=mainServletPath%>"/>
									</TD>
								</TR>
								<TR>
									<TD><ct:FWLabel key="JSP_BAC_R_NUM_BASE_CALCUL"/></TD>
									<TD><INPUT type="text" name="idBasesCalcul" value="<%=viewBean.getIdBasesCalcul()%>" class="disabled" readonly></TD>
									<TD colspan="2">&nbsp;</TD>
									<TD colspan="2"><ct:FWLabel key="JSP_BAC_R_REF_PAIEMENT"/><br>
										<textarea name="referencePmt" cols="50" rows="2">
											<%=viewBean.getReferencePmt()%>
										</textarea>
									</TD>
								</TR>
						<TR>
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<TD><b><ct:FWLabel key="JSP_SAM_D_DONNEES_OBLIGATOIRES"/></b></TD>
							<TD colspan="5"><hr></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SAM_D_GENRE_PRESTATION"/></TD>
							<TD>
								<INPUT type="text" name="genrePrestationRenteAccordee" size="8" value="<%=viewBean.getGenrePrestationRenteAccordee()%>" onkeyup="metAJourCodePrestation()">
								<IMG src="" alt="" name="imageOKKOCodePrestation">
							</TD>
							<TD><ct:FWLabel key="JSP_SAM_D_DEBUT_DROIT"/></TD>
							<TD>
								<input	id="dateDebutDroitRenteAccordee"
										name="dateDebutDroitRenteAccordee"
										data-g-calendar="type:month"
										<%if (!JadeStringUtil.isBlankOrZero(viewBean.getDateDebutDroitRenteAccordee())){%>
											value="<%=viewBean.getDateDebutDroitRenteAccordee()%>" 
										<%	} %>
										/>
							</TD>
							<TD><ct:FWLabel key="JSP_SAM_D_MONTANT"/></TD>
							<TD><INPUT type="text" name="montantRenteAccordee" value="<%=viewBean.getMontantRenteAccordee()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SAM_D_RAM"/></TD>
							<TD><INPUT type="text" name="RAMBasesCalcul" value="<%=viewBean.getRAMBasesCalcul()%>" <%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>></TD>
							<TD><ct:FWLabel key="JSP_SAM_D_ECHELLE"/></TD>
							<TD><INPUT type="text" name="echelleBasesCalcul" size="2" maxlength="2"
								value="<%=viewBean.getEchelleBasesCalcul()%>" <%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>></TD>
							<TD><ct:FWLabel key="JSP_SAM_D_ANNEE_TRAITEMENT"/></TD>
							<TD><INPUT type="text" name="anneeTraitementBasesCalcul" size="4" maxlength="4"
							    value="<%=viewBean.getAnneeTraitementBasesCalcul()%>"></TD>
						</TR>
						<TR>
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<TD><b><ct:FWLabel key="JSP_SAM_D_DUREE_COTISATION"/></b></TD>
							<TD colspan="5"><hr></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SAM_D_D_COT_AV_73"/></TD>
							<TD><INPUT type="text" name="dureeCotAv73BasesCalcul" value="<%=viewBean.getDureeCotAv73BasesCalcul()%>" onchange="formatAAMMField(this);"
								size="5" maxlength="5" <%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>>
							</TD>
							<TD><ct:FWLabel key="JSP_SAM_D_D_COT_AP_73"/></TD>
							<TD><INPUT type="text" name="dureeCotAp73BasesCalcul" value="<%=viewBean.getDureeCotAp73BasesCalcul()%>" onchange="formatAAMMField(this);"
								size="5" maxlength="5" <%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>></TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SAM_D_MOIS_APP_AV_73"/></TD>
							<TD><INPUT type="text" name="moisAppAv73BasesCalcul" value="<%=viewBean.getMoisAppAv73BasesCalcul()%>"
								size="2" maxlength="2" <%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>></TD>
							<TD><ct:FWLabel key="JSP_SAM_D_MOIS_APP_AP_73"/></TD>
							<TD><INPUT type="text" name="moisAppAp73BasesCalcul" value="<%=viewBean.getMoisAppAp73BasesCalcul()%>"
								size="2" maxlength="2" <%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>></TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SAM_D_D_COT_CLASSE"/></TD>
							<TD><INPUT type="text" name="dureeCotClasseBasesCalcul" value="<%=viewBean.getDureeCotClasseBasesCalcul()%>"
								size="2" maxlength="2" <%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>></TD>
							<TD><ct:FWLabel key="JSP_SAM_D_ANNEE_NIVEAU"/></TD>
							<TD><INPUT type="text" name="anneeNiveauBasesCalcul" value="<%=viewBean.getAnneeNiveauBasesCalcul()%>"
								size="2" maxlength="2" <%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>></TD>
							<TD><ct:FWLabel key="JSP_SAM_D_D_COT_RAM"/></TD>
							<TD><INPUT type="text" name="dureeCotRAMBasesCalcul" value="<%=viewBean.getDureeCotRAMBasesCalcul()%>" onchange="formatAAMMField(this);"
								size="5" maxlength="5" <%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>></TD>
						</TR>
						<TR>
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<TD><b><ct:FWLabel key="JSP_SAM_D_ELEMENTS_INVALIDITE"/></b></TD>
							<TD colspan="5"><hr></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SAM_D_DEGRE"/></TD>
							<TD><INPUT type="text" name="degreInvaliditeDemandeRente" value="<%=viewBean.getDegreInvaliditeDemandeRente()%>" class="disabled" readonly></TD>
							<TD><ct:FWLabel key="JSP_SAM_D_CLE_INFIRMITE"/></TD>
							<TD><INPUT type="text" name="cleInfirmiteDemandeRente" value="<%=viewBean.getCleInfirmiteDemandeRente()%>" class="disabled" readonly></TD>
							<TD><ct:FWLabel key="JSP_SAM_D_SURV_EV_ASS"/></TD>
							<TD><INPUT type="text" name="survEvAssDemandeRente" value="<%=viewBean.getSurvEvAssDemandeRente()%>" class="disabled" readonly></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SAM_D_INVALIDITE_PRECOCE"/></TD>
							<TD><INPUT type="checkbox" name="invaliditePrecoceBasesCalcul" <%=viewBean.getInvaliditePrecoceBasesCalcul().booleanValue()?"CHECKED":""%>></TD>
							<TD><ct:FWLabel key="JSP_SAM_D_OFFICE_AI"/></TD>
							<TD><INPUT type="text" name="codeOfficeAIDemandeRente" value="<%=viewBean.getCodeOfficeAIDemandeRente()%>" class="disabled" readonly></TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SAM_D_D_COT_ETR_AV_73"/></TD>
							<TD><INPUT type="text" name="dureeCotEtrAv73BasesCalcul"
							value="<%=viewBean.getDureeCotEtrAv73BasesCalcul()%>" size="5" maxlength="5" onchange="formatAAMMField(this);"
							<%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>></TD>
							<TD><ct:FWLabel key="JSP_SAM_D_D_COT_ETR_AP_73"/></TD>
							<TD><INPUT type="text" name="dureeCotEtrAp73BasesCalcul"
							value="<%=viewBean.getDureeCotEtrAp73BasesCalcul()%>" size="5" maxlength="5" onchange="formatAAMMField(this);"
							<%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>></TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SAM_D_SUPPL_AI_RAM"/></TD>
							<TD><INPUT type="text" name="suppAIRAMBasesCalcul" value="<%=viewBean.getSuppAIRAMBasesCalcul()%>" <%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>></TD>
							<TD><ct:FWLabel key="JSP_SAM_D_FRACTION_RENTE"/></TD>
							<TD>
								<select name="fractionRenteRenteAccordeeSelect">
								  <option value="">-</option>
								  <option value="1">1</option>
								  <option value="2">2</option>
								  <option value="3">3</option>
								  <option value="4">4</option>
								</select>							
								<INPUT type="hidden" name="fractionRenteRenteAccordee" value="<%=viewBean.getFractionRenteRenteAccordee()%>">
							</TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<TD><b><ct:FWLabel key="JSP_SAM_D_BONIFICATION"/></b></TD>
							<TD colspan="5"><hr></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SAM_D_BONUS_EDUCATIF"/></TD>
							<TD><INPUT type="text" name="bonusEducatifBasesCalcul" value="<%=viewBean.getBonusEducatifBasesCalcul()%>" <%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>></TD>
							<TD colspan="2">&nbsp;</TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SAM_D_ANNNES_EDUC"/></TD>
							<TD><INPUT type="text" name="anneesEducatifBasesCalcul"
							value="<%=viewBean.getAnneesEducatifBasesCalcul()%>" size="5" maxlength="5" onchange="formatAAMMField(this);"
							 <%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>></TD>
							<TD><ct:FWLabel key="JSP_SAM_D_ANNNES_ASSIST"/></TD>
							<TD><INPUT type="text" name="anneesAssistanceBasesCalcul"
							value="<%=viewBean.getAnneesAssistanceBasesCalcul()%>" size="5" maxlength="5" onchange="formatAAMMField(this);"
							<%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>></TD>
							<TD><ct:FWLabel key="JSP_SAM_D_ANNNES_TRANSIT"/></TD>
							<TD><INPUT type="text" name="anneesTransitionBasesCalcul"
							value="<%=viewBean.getAnneesTransitionBasesCalcul()%>" size="3" maxlength="3" onchange="formatAdField(this);"
							<%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>></TD>
						</TR>
						<TR>
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<TD><b><ct:FWLabel key="JSP_SAM_D_RETRAITE_FLEXIBLE"/></b></TD>
							<TD colspan="5"><hr></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SAM_D_DUREE_AJOURNEMENT"/></TD>
							<TD><INPUT type="text" name="dureeAjournementRenteAccordee"
								size="4" maxlength="4" value="<%=viewBean.getDureeAjournementRenteAccordee()%>"></TD>
							<TD><ct:FWLabel key="JSP_SAM_D_SUPPL_AJOUR"/></TD>
							<TD><INPUT type="text" name="supplementAjournementRenteAccordee" value="<%=viewBean.getSupplementAjournementRenteAccordee()%>"></TD>
							<TD><ct:FWLabel key="JSP_SAM_D_DATE_REVOC"/></TD>
							<TD><INPUT type="text" name="dateRevocationDemandeRente"
								size="7" maxlength="7" value="<%=viewBean.getDateRevocationDemandeRente()%>"	class="disabled" readonly>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SAM_D_ANNEES_ANTICIPATION"/></TD>
							<TD><INPUT type="text" name="anneesAnticipationDemandeRente"
								value="<%=viewBean.getAnneesAnticipationDemandeRente()%>" class="disabled" readonly>
							</TD>
							<TD><ct:FWLabel key="JSP_SAM_D_RED_ANTICIPATION"/></TD>
							<TD><INPUT type="text" name="reductionAnticipationRenteAccordee" value="<%=viewBean.getReductionAnticipationRenteAccordee()%>"></TD>
							<TD><ct:FWLabel key="JSP_SAM_D_DEB_ANTICIPATION"/></TD>
							<TD>
								<input	id="debAnticipationRenteAccordee"
										name="debAnticipationRenteAccordee"
										data-g-calendar="type:month"
										value="<%=viewBean.getDebAnticipationRenteAccordee()%>" />
							 </TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SAM_D_AJOURNE"/></TD>
							<TD>
								<INPUT type="checkbox" name="isAjourneDemandeRente" <%=viewBean.getIsAjourneDemandeRente().booleanValue()?"CHECKED":""%> class="forceDisable" disabled="disabled">
							</TD>
							<TD><ct:FWLabel key="JSP_SAM_D_TAUX_RED_ANTIC"/></TD>
							<TD><INPUT type="text" name="tauxReductionAnticipation" value="<%=viewBean.getTauxReductionAnticipation()%>"></TD>
						</TR>
						<TR>
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<TD><b><ct:FWLabel key="JSP_SAM_D_DIVERS"/></b></TD>
							<TD colspan="5"><hr></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SAM_D_FIN_DROIT"/></TD>
							<TD>
								<input	id="finDroitRenteAccordee"
										name="finDroitRenteAccordee"
										data-g-calendar="type:month"
										value="<%=viewBean.getFinDroitRenteAccordee()%>" />
							</TD>
							<TD><ct:FWLabel key="JSP_SAM_D_CODES_CAS_SPECIAL"/></TD>
							<TD colspan="3">
								<INPUT type="text" name="codesCasSpecialRenteAccordee1" size="2" maxlength="2" value="<%=viewBean.getCodesCasSpecialRenteAccordee1()%>" onchange="metAJourCodeCasSpeciaux()">
								<INPUT type="text" name="codesCasSpecialRenteAccordee2" size="2" maxlength="2" value="<%=viewBean.getCodesCasSpecialRenteAccordee2()%>" onchange="metAJourCodeCasSpeciaux()">
								<INPUT type="text" name="codesCasSpecialRenteAccordee3" size="2" maxlength="2" value="<%=viewBean.getCodesCasSpecialRenteAccordee3()%>" onchange="metAJourCodeCasSpeciaux()">
								<INPUT type="text" name="codesCasSpecialRenteAccordee4" size="2" maxlength="2" value="<%=viewBean.getCodesCasSpecialRenteAccordee4()%>" onchange="metAJourCodeCasSpeciaux()">
								<INPUT type="text" name="codesCasSpecialRenteAccordee5" size="2" maxlength="2" value="<%=viewBean.getCodesCasSpecialRenteAccordee5()%>" onchange="metAJourCodeCasSpeciaux()">
								<IMG src="" alt="" name="imageOKKOCodeCasSpeciaux">
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SAM_D_CODES_REVENUS_SPLITTES"/></TD>
							<TD>
								<INPUT type="checkbox" value="on" name="codeRevenusSplittesBasesCalcul" <%=viewBean.getCodeRevenusSplittesBasesCalcul().booleanValue()?"CHECKED":""%> <%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="forceDisable" disabled="disabled" <%}%>>
							</TD>
							<TD><ct:FWLabel key="JSP_SAM_D_CODES_SURVIVANT_INVALIDE"/></TD>
							<TD><INPUT type="text" name="codeSurvivantInvalideRenteAccordee"
								size="1" maxlength="1" value="<%=viewBean.getCodeSurvivantInvalideRenteAccordee()%>">
							</TD>
							<TD><ct:FWLabel key="JSP_SAM_D_CODE_REVENU"/></TD>
							<TD><INPUT type="text" name="codeRevenu9emeBasesCalcul" value="<%=viewBean.getCodeRevenu9emeBasesCalcul()%>" <%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>></TD>
						</TR>
						<TR>
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="6"><hr></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SAM_D_DROIT"/></TD>
							<TD><INPUT type="text" name="droitBasesCalcul" size="2" maxlength="2"
								value="<%=viewBean.getDroitBasesCalcul()%>" <%if (!viewBean.getIsBasesCalculModifiable().booleanValue()) {%> class="disabled" readonly<%}%>>
							</TD>
							<TD><ct:FWLabel key="JSP_SAM_D_TRANSFERE"/></TD>
							<TD>
								<INPUT type="checkbox" name="isTransfere" <%=viewBean.getIsTransfere().booleanValue()?"CHECKED":""%> class="forceDisable" disabled="disabled">
								<input type="hidden" name="isTransfere" value="<%=viewBean.getIsTransfere().booleanValue()%>">
							</TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						</TABLE>
						</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>