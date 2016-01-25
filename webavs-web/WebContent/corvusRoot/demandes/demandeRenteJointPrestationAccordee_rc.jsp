<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@page import="globaz.globall.db.BSession"%>
<%@page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@page import="globaz.jade.publish.client.JadePublishDocument"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>

<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<%@page import="globaz.commons.nss.NSUtil"%>
<%@page import="globaz.corvus.vb.demandes.RENSSDTO"%>
<%@page import="globaz.corvus.vb.demandes.REDemandeParametresRCDTO"%>
<%@page import="globaz.corvus.servlet.IREActions"%>
	
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<link href="<%=servletContext%>/theme/ajax/templateZoneAjax.css" rel="stylesheet" type="text/css" />
		
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>

<%	//Les labels de cette page commence par la préfix "JSP_DRE_R"
	idEcran="PRE0000";
	
	globaz.corvus.vb.demandes.REDemandeRenteJointPrestationAccordeeListViewBean viewBean = (globaz.corvus.vb.demandes.REDemandeRenteJointPrestationAccordeeListViewBean) request.getAttribute("viewBean");
	
	actionNew =  servletContext + mainServletPath + "?userAction=" + globaz.corvus.servlet.IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".afficher&_method=add";
	userActionNew =  globaz.corvus.servlet.IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".afficher";
	
	rememberSearchCriterias = false;
	bButtonFind = false;
	bButtonNew = false;
	
	RENSSDTO nssDto;
	REDemandeParametresRCDTO demandeDto;
	
	try{
		nssDto = (RENSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO);
	}catch(Exception ex){
		nssDto = null;
	}
	
	try{
		demandeDto = (REDemandeParametresRCDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO);
	}catch(Exception ex){
		demandeDto = null;
	}
%>
<%-- /tpl:put --%>
	
<%@ include file="/theme/find/javascripts.jspf" %>
	
<%-- tpl:put name="zoneScripts" --%>
<script type='text/javascript' src='<%=servletContext%>/scripts/ajax/ajaxUtils.js'></script>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>
	
<script language="javascript">
<%	if(nssDto != null
		&& !JadeStringUtil.isEmpty(nssDto.getNSS())){
%>
		bFind=true;
<%	}else if (demandeDto != null){
		if (!JadeStringUtil.isBlankOrZero(demandeDto.getLikeNom())
			&& !JadeStringUtil.isBlankOrZero(demandeDto.getLikePrenom())){
%>
			bFind=true;
<%		}
	} else {
%>
		bFind=false;
<%	}%>

	var $firstInput,
		$boutonRechercher,
		$champsAControler;

	$(document).ready(function() {
		$boutonRechercher = $('input[name="btnFind"]');

		checkPrintButton();
		
		if($('.likeNumeroAVS').val()!='' && $('.likeNumeroAVS').val()!=null){
			if($('.likeNumeroAVS').val().length > 13){
				bFind=true;
			}
		}
		
		$firstInput = $('#partiallikeNumeroAVS');
		$firstInput.focus().select().addClass('hasFocus');

		$('[name="fr_list"]').one('load', function () {
			setTimeout(function () {
				$firstInput.focus().select().addClass('hasFocus');
			}, 50);
		});

		// tous les champs sauf celui avec comme valeur "756." devant le numéro NSS
		$champsAControler = $('.obligatoirePourRecherche:not([name="likeNumeroAVSNssPrefixe"])');

		$champsAControler.keyup(function () {
			checkBoutonRechercher();	
		}).change(function () {
			checkBoutonRechercher();
		});

		checkBoutonRechercher();
	});

	$('html').one(eventConstant.JADE_FW_ACTION_DONE, function () {
		$firstInput.focus().select().addClass('hasFocus');
	});

	function checkBoutonRechercher() {
		var b_disableSearchButton = true;

		$champsAControler.each(function () {
			var $this = $(this);
			if ($this.val()) {
				b_disableSearchButton = false;
			}
		});

		if (b_disableSearchButton) {
			$boutonRechercher.prop('disabled', true);
		} else {
			$boutonRechercher.removeProp('disabled');
		}
	}

	// Entité spéciale afin de pouvoir afficher les codes préstation des rentes accordées à la demande
	// pour le bug N°5199
	usrAction = "corvus.demandes.demandeRenteJointPrestationAccordee.lister";
	
	function clearFields () {
		document.getElementsByName("likeNumeroAVS")[0].value="";
		document.getElementById("partiallikeNumeroAVS").value="";
		document.getElementById("likeNom").value="";
		document.getElementById("likePrenom").value="";
		document.getElementById("forDateNaissance").value="";
		document.getElementById("forCsSexe").value="";
		document.getElementById("forCsType").value="";
		document.getElementById("forCsTypeCalcul").value="";
		document.getElementById("forCsEtatDemande").value="blank";
		document.getElementById("forIdGestionnaire").value="<%=viewBean.getForIdGestionnaire()%>";
		document.getElementById("orderBy").value="<%=viewBean.getOrderByDefaut()%>";
		document.getElementById("orderBy").text=document.getElementById("orderBy").options[document.getElementById("orderBy").selectedIndex].text;
		document.getElementById('isRechercheFamille').checked = false;		
		document.getElementById('enCours').checked = false;
		document.getElementById("forDroitDu").value="";
		document.getElementById("forDroitAu").value="";

		isRechercheFamille();
		rechercheFamille();
		checkBoutonRechercher();

		// Grise le bouton "Imprimer" pour éviter les impressions erronées
		setPrintButtonDisabledState(true);

		document.getElementById('partiallikeNumeroAVS').focus();
	}

	function isRechercheFamille(){				
		if (document.getElementsByName('likeNumeroAVSNNSS')[0].value == "true"){
			if (document.getElementsByName('likeNumeroAVS')[0].value.length > 15){
				document.getElementById('isRechercheFamille').checked=true;
				document.getElementById("likeNom").disabled=true;
				document.getElementById("likePrenom").disabled=true;
				document.getElementById("forDateNaissance").disabled=true;
				document.getElementById("forCsSexe").disabled=true;
				bFind=true;
			}else{
				document.getElementById('isRechercheFamille').checked=false;
				document.getElementById("likeNom").disabled=false;
				document.getElementById("likePrenom").disabled=false;
				document.getElementById("forDateNaissance").disabled=false;
				document.getElementById("forCsSexe").disabled=false;
			}
		} else {
			if (document.getElementsByName('likeNumeroAVS')[0].value.length > 13){
				document.getElementById('isRechercheFamille').checked=true;
				document.getElementById("likeNom").disabled=true;
				document.getElementById("likePrenom").disabled=true;
				document.getElementById("forDateNaissance").disabled=true;
				document.getElementById("forCsSexe").disabled=true;
				bFind=true;
			}else{
				document.getElementById('isRechercheFamille').checked=false;
				document.getElementById("likeNom").disabled=false;
				document.getElementById("likePrenom").disabled=false;
				document.getElementById("forDateNaissance").disabled=false;
				document.getElementById("forCsSexe").disabled=false;
			}
		}
	}

	function rechercheFamille(){
		// pas de recherche famille si le NSS n'est pas complet			
		if (document.getElementsByName('likeNumeroAVSNNSS')[0].value == "true"){
			if (document.getElementsByName('likeNumeroAVS')[0].value.length <= 15){
				if (document.getElementById('isRechercheFamille').checked){
					document.getElementById('isRechercheFamille').checked=false;
				}
				return;
			}
		} else {
			if (!document.getElementsByName('likeNumeroAVS')[0].value.length <= 13){
				if (document.getElementById('isRechercheFamille').checked){
					document.getElementById('isRechercheFamille').checked=false;
				}
				return;
			}
		}
	
		if (document.getElementById('isRechercheFamille').checked){
			document.getElementById("likeNom").disabled=true;
			document.getElementById("likePrenom").disabled=true;
			document.getElementById("forDateNaissance").disabled=true;
			document.getElementById("forCsSexe").disabled=true;
		} else {
			document.getElementById("likeNom").disabled=false;
			document.getElementById("likePrenom").disabled=false;
			document.getElementById("forDateNaissance").disabled=false;
			document.getElementById("forCsSexe").disabled=false;
		}
	}
	
	var fwkOnClickFind = window.onClickFind;
	
	//Surcharge de la méthode onClickFind du framework, pour vérifier la recherche famille avant le submit du formulaire.
	window.onClickFind = function () {
		// appel à la méthode originale
		fwkOnClickFind();
		//Complément pour traitement rentes
		//Permet d'activer si nécessaire la checkbox isRechercheFamille avant la recherche.
		//Car en faisant un [return] clavier, le submit (la rechercher) s'effectue avant
		//le onChange du nssPopupTag, du coup la checkbox isRechercheFamille n'est pas prise en compte
		isRechercheFamille();
		rechercheFamille();											
	}

	function likeNomChange(){
		if(document.getElementsByName("likeNom")[0].value!=""){
			document.getElementsByName("orderBy")[0].value="<%=viewBean.getOrderByDefaut()%>";
		}
	}

	function likePrenomChange(){
		if(document.getElementsByName("likePrenom")[0].value!=""){
			document.getElementsByName("orderBy")[0].value="<%=viewBean.getOrderByDefaut()%>";		
		}
	}
	
	function imprimerListeDemandeRente(){
		var count = window.fr_list.managerCount ;
		if(count){
			if(count > 0){
				if(count > 100){
					if(confirm("<%=viewBean.getSession().getLabel("JSP_DRE_R_CONFIRMATION_IMPRIMER_DEBUT") %>"+count+" <%=viewBean.getSession().getLabel("JSP_DRE_R_CONFIRMATION_IMPRIMER_FIN") %>")){
						lancerImpression();
					}
				}else{
					lancerImpression();
				}
			}
		}
	}
	
	function setPrintButtonDisabledState(state) {
		document.getElementById('bouttonImprimer').disabled = state;
	}	
	
	function checkPrintButton(){
		var count = window.fr_list.managerCount;
		var hasNoItems = (count == undefined) || (count == 0);
		
		setPrintButtonDisabledState(hasNoItems);
	}
	
	function lancerImpression(){
		$('body').overlay();
		
		document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE%>.imprimerListeDemandeRente";
		document.forms[0].target="_main";
		document.forms[0].submit();
	}
	
	function onNewButtonClick() {
		var action = '<%=actionNew%>';
		var inputNumeroAVS = $("#partiallikeNumeroAVS");
		var valeurChamp = inputNumeroAVS.val();
		var isNssCleared = (valeurChamp.length == 0);
		
		if (isNssCleared) {
			action += "&fromScratch=true";
		}
		
		document.location.href=action;
	}
</script>
<%	if(viewBean.getAttachedDocuments() != null && viewBean.getAttachedDocuments().size()>0){
		for(int i=0;i<viewBean.getAttachedDocuments().size();i++){
			String docName = ((JadePublishDocument)viewBean.getAttachedDocuments().get(i)).getDocumentLocation();
			int index = docName.lastIndexOf("/");
			if(index == -1){
				index = docName.lastIndexOf("\\");
			}
			docName = docName.substring(index);
%>
			<script>
				window.open("<%=request.getContextPath()+ "/work/" + docName%>");
			</script>
<%		}
	}
%>
<%-- /tpl:put --%>
	
<%@ include file="/theme/find/bodyStart.jspf" %>
	
	<%-- tpl:put name="zoneTitle" --%>
		<ct:FWLabel key="JSP_DRE_R_TITRE"/>
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/find/bodyStart2.jspf" %>
					<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>
								<table border="0" cellspacing="0" cellpadding="0" width="100%">
									<tr>		
										<td>
											<label for="likeNumeroAVS">
												<ct:FWLabel key="JSP_DRE_R_NSS"/>
											</label>
											&nbsp;
											<input type="hidden" name="hasPostitField" value="<%=true%>">
										</td>
									<%	
										if ( nssDto != null ) {
									%>
											<td>
												<ct1:nssPopup 	avsMinNbrDigit="99"
																nssMinNbrDigit="99"
																newnss=""
																name="likeNumeroAVS"
																onChange="isRechercheFamille();" 
																value="<%=nssDto.getNSSForSearchField()%>" 
																cssclass="obligatoirePourRecherche" />
											</td>
									<%	} else {%>
											<td>
												<ct1:nssPopup 	avsMinNbrDigit="99"
																nssMinNbrDigit="99"
																newnss=""
																name="likeNumeroAVS"
																onChange="isRechercheFamille();" 
																cssclass="obligatoirePourRecherche" />
											</td>
									<%	}%>
										<td>
											<label for="likeNom">
												<ct:FWLabel key="JSP_DRE_R_NOM"/>
											</label>
											&nbsp;
										</td>
									
									<%	if (demandeDto != null) {
									%>
											<td>
												<input	type="text" 
														id="likeNom" 
														name="likeNom" 
														onchange="likeNomChange();" 
														class="obligatoirePourRecherche" 
														value="<%=demandeDto.getLikeNom()%>">
											</td>
									<%	} else {%>
											<td>
												<input	type="text" 
														id="likeNom" 
														name="likeNom" 
														value="" 
														class="obligatoirePourRecherche" 
														onchange="likeNomChange();">
											</td>
									<%	}%>
									
										<td>
											<label for="likePrenom">
												<ct:FWLabel key="JSP_DRE_R_PRENOM"/>
											</label>
											&nbsp;
										</td>
										
									<%	if (demandeDto != null) {
									%>
											<td>
												<input	type="text" 
														id="likePrenom" 
														name="likePrenom" 
														onchange="likePrenomChange();" 
														class="obligatoirePourRecherche" 
														value="<%=demandeDto.getLikePrenom()%>">
											</td>
									<%	} else {%>
											<td>
												<input	type="text" 
														id="likePrenom" 
														name="likePrenom" 
														onchange="likePrenomChange();" 
														class="obligatoirePourRecherche" 
														value="">
											</td>
									<%	}%>
									</tr>
									<tr>
										<td>
											<label for="forDateNaissance">
												<ct:FWLabel key="JSP_DRE_R_DATENAISSANCE"/>
											</label>
											&nbsp;
										</td>
									<%	if (demandeDto != null) {
									%>
											<td>
												<input	id="forDateNaissance" 
														name="forDateNaissance"
														data-g-calendar="yearRange:¦1900:<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>¦"
														class="obligatoirePourRecherche" 
														value="<%=demandeDto.getForDateNaissance()%>" />
											</td>
									<%	} else {%>
											<td>
												<input	id="forDateNaissance" 
														name="forDateNaissance"
														data-g-calendar="yearRange:¦1900:<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>¦"
														class="obligatoirePourRecherche" 
														value="" />
											</td>
									<%	}%>
										<td>
											<label for="forCsSexe">
												<ct:FWLabel key="JSP_DRE_R_SEXE"/>
											</label>
											&nbsp;
										</td>	
										<td colspan="3">
											<ct:FWCodeSelectTag name="forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true" notation="class=\"obligatoirePourRecherche\""/>
										</td>
									</tr>
									<tr>
										<td colspan="6">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td>
											<label for="forCsEtatDemande">
												<ct:FWLabel key="JSP_DRE_R_ETAT"/>
											</label>
											&nbsp;
										</td>
										<td>
											<ct:FWListSelectTag data="<%=viewBean.getEtatsDemande()%>" defaut="blank" name="forCsEtatDemande" notation="class=\"obligatoirePourRecherche\""/>
										</td>
										<td>
											<label for="forCsType">
												<ct:FWLabel key="JSP_DRE_R_TYPE"/>
											</label>
											&nbsp;
										</td>
										<td>
											<ct:FWCodeSelectTag codeType="RETYPEDEM" name="forCsType" wantBlank="true" defaut="blank" notation="class=\"obligatoirePourRecherche\""/>
										</td>
										<td>
											<label for="forCsTypeCalcul">
												<ct:FWLabel key="JSP_SDR_D_TYPE_CALCUL"/>
											</label>
											&nbsp;
										</td>
										<td>
											<ct:FWCodeSelectTag codeType="RETYPCALC" name="forCsTypeCalcul" wantBlank="true" defaut="blank" notation="class=\"obligatoirePourRecherche\"" />
										</td>										
									</tr>										
									<tr>
										<td>
											<label for="forDroitDu">
												<ct:FWLabel key="JSP_DRE_R_DROIT_DU"/>
											</label>
											&nbsp;
										</td>
									<%	if (demandeDto != null) {
									%>
											<td>
												<input	id="forDroitDu" 
														name="forDroitDu"
														data-g-calendar="type:month,mandatory:false"
														class="obligatoirePourRecherche" 
														value="<%=demandeDto.getForDroitDu()%>" />
												<ct:FWLabel key="JSP_DRE_R_FORMAT_DATE_DEBUT"/> 
											</td>
									<%	} else {%>
											<td>
												<input	id="forDroitDu" 
														name="forDroitDu"
														data-g-calendar="type:month,mandatory:false"
														class="obligatoirePourRecherche" 
														value="" />
												<ct:FWLabel key="JSP_DRE_R_FORMAT_DATE_DEBUT"/> 
											</td>
									<%	}%>
										<td>
											<label for="forDroitAu">
												<ct:FWLabel key="JSP_DRE_R_DROIT_AU"/>
											</label>
											&nbsp;
										</td>
									<%	if (demandeDto != null) {
									%>
											<td>
												<input	id="forDroitAu" 
														name="forDroitAu"
														data-g-calendar="type:month"
														class="obligatoirePourRecherche" 
														value="<%=demandeDto.getForDroitAu()%>" />
												<ct:FWLabel key="JSP_DRE_R_FORMAT_DATE_DEBUT"/> 
											</td>
									<%	} else {%>
											<td>
												<input	id="forDroitAu" 
														name="forDroitAu"
														data-g-calendar="type:month"
														class="obligatoirePourRecherche" 
														value="" />
												<ct:FWLabel key="JSP_DRE_R_FORMAT_DATE_DEBUT"/> 
											</td>
									<%}%>	
									</tr>
									<tr>
										<td>
											<label for="enCours">
												<ct:FWLabel key="JSP_DRE_R_EN_COURS"/>
											</label>
											&nbsp;
										</td>
										<td>
											<input type="checkbox" id="enCours" name="enCours" value="on">
										</td>
										<td>
										</td>
										<td>
										</td>
										<td>
											<ct:FWLabel key="JSP_DRE_R_RECHERCHE_FAMILLE"/> 
										</td>
										<td>
											<input type="checkbox" id="isRechercheFamille" name="isRechercheFamille" value="on" onclick="rechercheFamille();">
										</td>
									</tr>
									<tr>
										<td>
											<label for="forIdGestionnaire">
												<ct:FWLabel key="JSP_DRE_R_GESTIONNAIRE"/>
											</label>
											&nbsp;
										</td>
										<td>
											<ct:FWListSelectTag data="<%=globaz.corvus.utils.REGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=viewBean.getForIdGestionnaire()%>" name="forIdGestionnaire" notation="class=\"obligatoirePourRecherche\""/>
										</td>
										<td colspan="2">
											&nbsp;
										</td>
										<td>
											<ct:FWLabel key="JSP_TRIER_PAR"/>
											&nbsp;
										</td>
										<td>
											<ct:FWListSelectTag data="<%=viewBean.getOrderByData()%>" defaut="<%=viewBean.getOrderBy()%>" name="orderBy" />
										</td>
									</tr>
									<tr>
										<td colspan="6">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td>
											<input 	type="button" 
													onclick="clearFields()" 
													accesskey="<ct:FWLabel key="AK_EFFACER"/>" 
													value="<ct:FWLabel key="JSP_EFFACER"/>">
											<label>
												[ALT+<ct:FWLabel key="AK_EFFACER"/>]
											</label>
										</td>
										<td colspan="5">
											&nbsp;
										</td>
										<td>
											<label>
												[ALT+<ct:FWLabel key="AK_IMPRIMER"/>]
											</label>
											<input 	id="bouttonImprimer"
													type="button" 
													value="<ct:FWLabel key="JSP_IMPRIMER"/>" 
													onclick="imprimerListeDemandeRente()" 
													accesskey="<ct:FWLabel key="AK_IMPRIMER"/>">
										</td>
										<script>
											isRechercheFamille();
											rechercheFamille();											
										</script>
									</tr>
								</table>
							</td>
						</tr>						
					<%-- /tpl:put --%>
	
<%@ include file="/theme/find/bodyButtons.jspf" %>
			<%-- tpl:put name="zoneButtons" --%>
				<input type="submit" name="btnFind" value="<%=btnFindLabel%>">
				<ct:ifhasright element="<%=IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE%>" crud="u">		
					<input type="button" name="btnNew"  value="<%=btnNewLabel%>" onClick="onNewButtonClick()" >									
				</ct:ifhasright>
			<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
	
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
	
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>