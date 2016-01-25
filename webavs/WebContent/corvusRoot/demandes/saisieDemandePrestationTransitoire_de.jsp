<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="globaz.corvus.vb.demandes.RESaisieDemandeRenteViewBean"%>
<%@ page import="globaz.prestation.api.IPRInfoCompl"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<%@ page import="globaz.commons.nss.NSUtil"%>
<%@ page import="globaz.corvus.api.demandes.IREDemandeRente"%><%@page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="globaz.framework.secure.FWSecureConstants"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ include file="/theme/detail/header.jspf" %>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js">
</script>

<%
	idEcran="PRE0003";

	globaz.corvus.vb.demandes.RESaisieDemandeRenteViewBean viewBean = (globaz.corvus.vb.demandes.RESaisieDemandeRenteViewBean) session.getAttribute("viewBean");
	
	java.util.Map<String, String> codesCsCodeInfirmite = globaz.prestation.tools.PRCodeSystem.getCUCSinMap(viewBean.getSession(), globaz.corvus.api.demandes.IREDemandeRente.CS_GROUPE_INFIRMITE);
	java.util.Map<String, String> codesCsCodeAtteinte  = globaz.prestation.tools.PRCodeSystem.getCUCSinMap(viewBean.getSession(), globaz.corvus.api.demandes.IREDemandeRente.CS_GROUPE_ATTEINTE_INVALIDITE);
	java.util.Map<String, String> officesAi = viewBean.getMapOfficesAi();

	 bButtonValidate = false;
	 bButtonCancel = false;
	 bButtonUpdate = false;
	 bButtonDelete = false;
	
	String showModalDialogURLNbrPageMotivation = servletContext + "/corvusRoot/demandes/majNbrPageMotivation_dialog.jsp?idDemandeRente="+viewBean.getIdDemandeRente()+"&csTypeDemandeRente="+viewBean.getCsTypeDemandeRente()+"&mainServletPath="+mainServletPath+"&";
	String showModalDialogURLGenrePrononce = servletContext + "/corvusRoot/demandes/majGenrePrononce_dialog.jsp?idDemandeRente="+viewBean.getIdDemandeRente()+"&csTypeDemandeRente="+viewBean.getCsTypeDemandeRente()+"&mainServletPath="+mainServletPath+"&";
	String showModalDialogURLGestionnaire = servletContext + "/corvusRoot/demandes/majGestionnaire_dialog.jsp?idDemandeRente="+viewBean.getIdDemandeRente()+"&csTypeDemandeRente="+viewBean.getCsTypeDemandeRente()+"&mainServletPath="+mainServletPath+"&";
	
	boolean hasDroitBoutonArret = viewBean.getSession().hasRight("corvus.demandes.saisieDemandeRente.arreterSaisieDemandeRente", FWSecureConstants.UPDATE);
%>
<%@ include file="/theme/detail/javascripts.jspf" %>

	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="corvus-optionsempty" />

<style type="text/css">
	.widgetOffice{
		width: 60px;
	}
</style>

<script language="javascript">

	var o_mapCodeInfirmite = <%=new Gson().toJson(codesCsCodeInfirmite)%>;
	var o_mapCodeAtteinte = <%=new Gson().toJson(codesCsCodeAtteinte)%>;
	var o_mapOfficesAi = <%=new Gson().toJson(viewBean.getMapOfficesAi())%>;

	function init(){
	}

	function postInit(){
 		enableFields();
 		metAJourCsInfirmiteInv();
 		metAJourCsAtteinteInv();
	}

	function enableFields(){		
		document.getElementById("csTypeDemandeRente").disabled=true;
		document.getElementById("csTypeCalcul").disabled=true;
		document.getElementById("partiallikeNSS").disabled=true;
		document.getElementById("nomRequerantAffiche").disabled=true;
		document.getElementById("prenomRequerantAffiche").disabled=true;
		document.getElementById("dateNaissanceRequerantAffiche").disabled=true;
		document.getElementById("csSexeRequerantAffiche").disabled=true;
		document.getElementById("csNationaliteRequerantAffiche").disabled=true;
		document.getElementById("csCantonDomicileRequerantAffiche").disabled=true;
		document.getElementById("dateDecesRequerantAffiche").disabled=true;
		document.getElementById("csCantonDomicileRequerantAffiche").disabled=true;
		document.getElementById("dateDecesRequerantAffiche").disabled=true;
		document.getElementById("csEtat").disabled=true;
		
		document.getElementById("dateDepot").disabled=false;
		document.getElementById("dateReception").disabled=false;
		document.getElementById("idGestionnaire").disabled=false;
		
		document.getElementById("csGenrePrononceAiInv").disabled=false;
		document.getElementById("codeOfficeAiInv").disabled=false;
		document.getElementById("codeCsInfirmiteInv").disabled=false;
		document.getElementById("codeCsAtteinteInv").disabled=false;
		document.getElementById("nbPagesMotivationInv").disabled=false;
		
		document.getElementById("dateDebutInvalidite").disabled=false;
		document.getElementById("dateFinInvalidite").disabled=false;
		document.getElementById("degreInvalidite").disabled=false;
		document.getElementById("dateSurvenanceEvenementAssureInv").disabled=false;
		document.getElementById("pourcentRedFauteGrave").disabled=false;
		document.getElementById("pourcentRedNonCollaboration").disabled=false;
		document.getElementById("dateDebutRedNonCollaboration").disabled=false;
		document.getElementById("dateFinRedNonCollaboration").disabled=false;
		document.getElementById("btnAjouterPeriode").disabled=false;
		
	}
	

	function arret() {
		document.forms[0].elements('userAction').value = "<%=globaz.corvus.servlet.IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.arreterSaisieDemandePrestationTransitoire";
		document.forms[0].submit();
	}
	
	function validate() {
		return validateFields();
	}

	function suivant(){
		validate();
		document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.ajouterSaisieDemandePrestationTransitoire";
		document.forms[0].submit();
	}
	
	function addPeriodeInv(){
		document.forms[0].elements('modifie').value = "true";
		document.forms[0].elements('userAction').value = "<%=globaz.corvus.servlet.IREActions.ACTION_SAISIE_DEMANDE_RENTE%>.actionAjouterPeriodeINVPrestationTransitoire";
		document.forms[0].submit();
	}

	function metAJourCsInfirmiteInv(){
		if (document.getElementById('codeCsInfirmiteInv').value != "" 
			&& typeof o_mapCodeInfirmite[document.getElementById('codeCsInfirmiteInv').value] != 'undefined'){
			document.getElementById("imageOKKOInfirmiteInv").src='<%=request.getContextPath()+"/images/ok.gif"%>';
			document.getElementById('csInfirmiteInv').value = o_mapCodeInfirmite[document.getElementById('codeCsInfirmiteInv').value];
		} else {
			document.getElementById("imageOKKOInfirmiteInv").src='<%=request.getContextPath()+"/images/erreur.gif"%>';
			document.getElementById('csInfirmiteInv').value = "";
		}
	}

	function metAJourCsAtteinteInv(){
		if (document.getElementById('codeCsAtteinteInv').value != "" 
			&& typeof o_mapCodeAtteinte[document.getElementById('codeCsAtteinteInv').value] != 'undefined'){
			document.getElementById("imageOKKOAtteinteInv").src='<%=request.getContextPath()+"/images/ok.gif"%>';
			document.getElementById('csAtteinteInv').value = o_mapCodeAtteinte[document.getElementById('codeCsAtteinteInv').value];
		} else {
			document.getElementById("imageOKKOAtteinteInv").src='<%=request.getContextPath()+"/images/erreur.gif"%>';
			document.getElementById('csAtteinteInv').value = "";
		}
	}

	function majNbrPageMotivation(input) {
		if (window.showModalDialog) {
			var value = window.showModalDialog("<%=showModalDialogURLNbrPageMotivation%>nbrPage="+input.value,null,"dialogHeight:200px;help:no;status:no;resizable:no;scroll:no");
			if (value != null) {
				input.value = value;
			}
		}
	}

	function majGenrePrononce(input) {
		if (window.showModalDialog) {
			var value = window.showModalDialog("<%=showModalDialogURLGenrePrononce%>csGenrePrononce="+input.value,null,"dialogHeight:200px;help:no;status:no;resizable:no;scroll:no");
			if (value != null) {
				input.value = value;
			}
		}
	}
	
	function majGestionnaire(input) {
		if (window.showModalDialog) {
			var value = window.showModalDialog("<%=showModalDialogURLGestionnaire%>idGestionnaire="+(input.value == ''?0:input.value),null,"dialogHeight:200px;help:no;status:no;resizable:no;scroll:no");
			if (value != null) {
				input.value = value;
			}
		}
	}
	
</script>

<%@ include file="/theme/detail/bodyStart.jspf" %>
		<% 	// on n'affiche pas l'icône du post-it si on créer une nouvelle demande (donc si on a pas encore d'ID de demande)
			if(!JadeStringUtil.isBlank(viewBean.getIdDemandeRente())){%>
				<span class="postItIcon">
					<ct:FWNote sourceId="<%=viewBean.getIdRequerant()%>" tableSource="<%=globaz.corvus.application.REApplication.KEY_POSTIT_RENTES%>"/>
				</span>
		<%	} %>
			<ct:FWLabel key="JSP_SDR_D_TITRE_PAGE"/>
			<%@ include file="/theme/detail/bodyStart2.jspf" %>
					<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_TYPE_DEMANDE" />
								<input	type="hidden" 
										name="idInfoComplementaire" 
										value="<%=viewBean.getIdInfoComplementaire()%>" />
								<input	type="hidden" 
										name="modifie" 
										value="<%=viewBean.isModifie()%>" />
							</td>
							<td>
								<ct:select name="csTypeDemandeRente" disabled="true">
									<option value="<%=globaz.corvus.api.demandes.IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE%>"<%=globaz.corvus.api.demandes.IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(viewBean.getCsTypeDemandeRente())?"selected":""%>>
										<label>
											Invalidité
										</label>
									</option>
								</ct:select>
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_TYPE_CALCUL" />
							</td>
							<td>
								<ct:select 	name="csTypeCalcul" 
											defaultValue="<%=viewBean.getCsTypeCalcul()%>" 
											disabled="true">
									<ct:optionsCodesSystems csFamille="RETYPCALC">
									</ct:optionsCodesSystems>
								</ct:select>
							</td>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_NSS" />
							</td>
							<td><%
								String params = "&provenance1=TIERS";
								params += "&extraParam1=forceSingleAdrMode";
								String jspLocation = servletContext + "/corvusRoot/numeroSecuriteSocialeSF_select.jsp";
							%>	<ct1:nssPopup 	name="likeNSS" 
												onFailure="nssFailure();" 
												onChange="nssChange(tag);" 
												params="<%=params%>"
												value="<%=viewBean.getNumeroAvsFormateSansPrefixe()%>" 
												newnss=""
												jspName="<%=jspLocation%>" 
												avsMinNbrDigit="3" 
												nssMinNbrDigit="3" 
												avsAutoNbrDigit="11" 
												nssAutoNbrDigit="10" />
								<input	type="hidden" 
										name="nssRequerant" 
										value="<%=viewBean.getNssRequerant()%>" />
								<input	type="hidden" 
										name="idAssure" 
										value="<%=viewBean.getIdAssure()%>" />
								<input	type="hidden" 
										name="provenance" 
										value="<%=viewBean.getProvenance()%>" />
								<input	type="hidden" 
										name="idTiers" 
										value="<%=viewBean.getIdTiers()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_NOM" />
							</td>
							<td>
								<input	type="hidden" 
										name="nomRequerant" 
										value="<%=viewBean.getNomRequerant()%>" />
								<input	type="text" 
										name="nomRequerantAffiche" 
										value="<%=viewBean.getNomRequerant()%>" 
										disabled="true"/>
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_PRENOM"/></td>
							<td>
								<input	type="hidden" 
										name="prenomRequerant" 
										value="<%=viewBean.getPrenomRequerant()%>" />
								<input	type="text" 
										name="prenomRequerantAffiche" 
										value="<%=viewBean.getPrenomRequerant()%>" 
										disabled="true"/>
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_NAISSANCE" />
							</td>
							<td>
								<input	type="hidden" 
										name="dateNaissanceRequerant" 
										value="<%=viewBean.getDateNaissanceRequerant()%>" />
								<input	type="text" 
										name="dateNaissanceRequerantAffiche" 
										value="<%=viewBean.getDateNaissanceRequerant()%>" 
										disabled="true" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_SEXE" />
							</td>
							<td>
								<ct:select 	name="csSexeRequerantAffiche" 
											defaultValue="<%=viewBean.getCsSexeRequerant()%>" disabled="true">
									<ct:optionsCodesSystems csFamille="PYSEXE">
									</ct:optionsCodesSystems>
								</ct:select>
								<input	type="hidden" 
										name="csSexeRequerant" 
										value="<%=viewBean.getCsSexeRequerant()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_NATIONALITE" />
							</td>
							<td>
								<ct:FWListSelectTag	name="csNationaliteRequerantAffiche" 
													data="<%=viewBean.getTiPays()%>" 
													defaut="<%=globaz.globall.util.JAUtil.isIntegerEmpty(viewBean.getCsNationaliteRequerant())?globaz.pyxis.db.adressecourrier.TIPays.CS_SUISSE:viewBean.getCsNationaliteRequerant()%>" 
													 />
								<input	type="hidden" 
										name="csNationaliteRequerant" 
										value="<%=viewBean.getCsNationaliteRequerant()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_CANTON_DOMICILE" />
							</td>
							<td>
								<ct:select	name="csCantonDomicileRequerantAffiche" 
											defaultValue="<%=viewBean.getCsCantonRequerant()%>" 
											wantBlank="true"
											disabled="true" >
									<ct:optionsCodesSystems csFamille="PYCANTON">
									</ct:optionsCodesSystems>
								</ct:select>
								<input	type="hidden" 
										name="csCantonRequerant" 
										value="<%=viewBean.getCsCantonRequerant()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_DECES" />
							</td>
							<td colspan="2">
								<input	type="hidden" 
										name="dateDecesRequerant" 
										value="<%=viewBean.getDateDecesRequerant()%>" />
								<input	type="text" 
										name="dateDecesRequerantAffiche" 
										value="<%=viewBean.getDateDecesRequerant()%>" 
										disabled="disabled"/>
							</td>
							<td>
								<a	href="#" onclick="window.open('<%=servletContext%><%=("/corvus")%>?userAction=<%=globaz.corvus.servlet.IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE%>.actionAfficherDossierGed&amp;noAVSId=<%=viewBean.getNssRequerant()%>&amp;idTiersExtraFolder=<%=viewBean.getIdTiers()%>&amp;serviceNameId=<%=viewBean.getSession().getApplication().getProperty(globaz.externe.IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED)%>','GED_CONSULT')" >
									<label>
										<ct:FWLabel key="JSP_LIEN_GED"/>
									</label>
								</a>
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_DEPOT" />
							</td>
							<td>
								<input	id="dateDepot"
										name="dateDepot"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateDepot()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_RECEPTION" />
							</td>
							<td>
								<input	id="dateReception"
										name="dateReception"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateReception()%>" />
							</td>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<a href="#" onclick="majGestionnaire(document.mainForm.idGestionnaire);">
									<ct:FWLabel key="JSP_SDR_D_GESTIONNAIRE" />
								</a>
							</td>
							<td>
								<ct:FWListSelectTag	name="idGestionnaire" 
													data="<%=viewBean.getResponsableData()%>" 
													defaut="<%=viewBean.getIdGestionnaire()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_ETAT" />
							</td>
							<td>
								<ct:select name="csEtat" defaultValue="<%=viewBean.getCsEtat()%>">
									<ct:optionsCodesSystems csFamille="REETATDEM">
									</ct:optionsCodesSystems>
								</ct:select>
							</td>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="6">
								<hr/>
							</td>
						</tr>
					</tbody>

					<tbody id="demandeInvalidite">
						<tr>
							<td>
						<%	if (!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(viewBean.getCsEtat()) 
								&& !IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(viewBean.getCsEtat()) 
								&& viewBean.getSession().hasRight(IREActions.ACTION_SAISIE_DEMANDE_RENTE, FWSecureConstants.UPDATE)) {%>
								<a href="#" onclick="majGenrePrononce(document.mainForm.csGenrePrononceAiInv);">
									<ct:FWLabel key="JSP_SDR_D_GENRE_PRONONCE_AI" />
								</a>
						<%	} else {%>
								<ct:FWLabel key="JSP_SDR_D_GENRE_PRONONCE_AI" />
						<%	} %>														
							</td>
							<td>
								<ct:FWCodeSelectTag	name="csGenrePrononceAiInv" 
													codeType="REGEPRON" 
													wantBlank="false" 
													defaut="<%=viewBean.getCsGenrePrononceAiInv()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_OFFICE_AI" />
							</td>
							<td colspan="3">
								<ct:select 	name="codeOfficeAiInv" > 
							<%	String defaultValue = globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getCodeOfficeAiInv())?viewBean.getNoOfficeAICantonal():viewBean.getCodeOfficeAiInv();
								for(String codeOfficeAi : officesAi.keySet()) {%>
									<option value="<%=codeOfficeAi%>" <%=codeOfficeAi.equals(defaultValue)?"selected ":""%>>
										<%=codeOfficeAi%> - <%=officesAi.get(codeOfficeAi)%>
									</option>
							<%	} %>
								</ct:select>
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_INFIRMITE" />
							</td>
							<td>
								<input	type="text"
										size="3" 
										maxlength="3" 
										id="codeCsInfirmiteInv"
										name="codeCsInfirmiteInv" 
										value="<%=viewBean.getCodeCsInfirmiteInv()%>" 
										onkeyup="metAJourCsInfirmiteInv()" />
								<input	type="hidden" 
										id="csInfirmiteInv"
										name="csInfirmiteInv" 
										value="<%=viewBean.getCsInfirmiteInv()%>" />
								<img 	src="" 
										alt="" 
										id="imageOKKOInfirmiteInv"
										name="imageOKKOInfirmiteInv" >
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_ATTEINTE" />
							</td>
							<td>
								<input 	type="text" 
										size="2"
										maxlength="2" 
										id="codeCsAtteinteInv"
										name="codeCsAtteinteInv" 
										value="<%=viewBean.getCodeCsAtteinteInv()%>" 
										onkeyup="metAJourCsAtteinteInv()" />
								<input 	type="hidden" 
										id="csAtteinteInv"
										name="csAtteinteInv" 
										value="<%=viewBean.getCsAtteinteInv()%>" />
								<img 	src="" 
										alt="" 
										id="imageOKKOAtteinteInv"
										name="imageOKKOAtteinteInv" >
							</td>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>							
						<%	if (!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(viewBean.getCsEtat()) &&
								!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(viewBean.getCsEtat()) && 
						  		viewBean.getSession().hasRight(IREActions.ACTION_SAISIE_DEMANDE_RENTE, FWSecureConstants.UPDATE)) {%>
								<a href="#" onclick="majNbrPageMotivation(document.mainForm.nbPagesMotivationInv);">
									<ct:FWLabel key="JSP_SDR_D_NB_PAGE_MOTIVATION" />
								</a>
						<%	} else {%>
								<ct:FWLabel key="JSP_SDR_D_NB_PAGE_MOTIVATION"/>
						<%	} %>																																			
							</td>
							<td colspan="5">
								<input 	type="text" 
										size="3" 
										name="nbPagesMotivationInv" 
										value="<%=viewBean.getNbPagesMotivationInv()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								<hr/>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_DEBUT_INVALIDITE" />
							</td>
							<td>
								<input	type="hidden" 
										name="idPeriodeInvalidite" 
										value="<%=viewBean.getIdPeriodeInvalidite()%>" />
								<input	id="dateDebutInvalidite"
										name="dateDebutInvalidite"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateDebutInvalidite()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_FIN_INVALIDITE" />
							</td>
							<td>
								<input	id="dateFinInvalidite"
										name="dateFinInvalidite"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateFinInvalidite()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DEGRE_INVALIDITE" />
							</td>
							<td>
								<input 	type="text" 
										name="degreInvalidite" 
										value="<%=viewBean.getDegreInvalidite()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="5">
								&nbsp;
							</td>
							<td>
								<input 	type="button" 
										id="btnAjouterPeriode"
										name="btnAjouterPeriode" 
										value="<ct:FWLabel key="JSP_AJOUTER"/>" 
										onclick="addPeriodeInv()" />
							</td>
						</tr>
						<tr>
							<td colspan="4" rowspan="1">
								<iframe	id="periodesFrame" 
										name="periodesFrame" 
										width="100%" 
										height="100"
										src="<%=request.getContextPath()%>/corvus?userAction=corvus.demandes.saisieDemandeRente.actionListerPeriodesINVPrestationTransitoire&forIdDemandeRente=<%=viewBean.getIdDemandeRente()%>&modifiable=<%=viewBean.isModifiable()%>">
								</iframe>
							</td>
							<td>
								<ct:FWLabel key="JSP_SDR_D_DATE_SURVENANCE_EVENEMENT_ASSURE"/>
							</td>
							<td>
								<input	id="dateSurvenanceEvenementAssureInv"
										name="dateSurvenanceEvenementAssureInv"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateSurvenanceEvenementAssureInv()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<ct:FWLabel key="JSP_SDR_D_RED_FAUTE_GRAVE" />
							</td>
							<td>
								<input	type="text" 
										size="5" 
										style="text-align:right;" 
										name="pourcentRedFauteGrave" 
										value="<%=viewBean.getPourcentRedFauteGrave()%>" />
								
							</td>
							<td colspan="3">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<ct:FWLabel key="JSP_SDR_D_RED_NON_COLLABO" />
							</td>
							<td>
								<input	type="text" 
										size="5" 
										style="text-align:right;" 
										name="pourcentRedNonCollaboration" 
										value="<%=viewBean.getPourcentRedNonCollaboration()%>" />
								
							</td>
							<td colspan="3">
								<ct:FWLabel key="JSP_SDR_D_DU" />
								&nbsp;
								<input	id="dateDebutRedNonCollaboration"
										name="dateDebutRedNonCollaboration"
										data-g-calendar="type:month"
										value="<%=viewBean.getDateDebutRedNonCollaboration()%>" />
								<ct:FWLabel key="JSP_SDR_D_FORMAT" />
								&nbsp;
								<ct:FWLabel key="JSP_SDR_D_AU" />
								&nbsp;
								<input	id="dateFinRedNonCollaboration"
										name="dateFinRedNonCollaboration"
										data-g-calendar="type:month"
										value="<%=viewBean.getDateFinRedNonCollaboration()%>" />
								<ct:FWLabel key="JSP_SDR_D_FORMAT" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_SDR_D_CELIBATAIRE_SANS_ENFANT" />
							</td>
							<td>
								<input type="checkbox" name="isCelibataireSansEnfantsInv" />
							</td>
							<td>
								<a href="#" onclick="informationsComplementaires();">
									<ct:FWLabel key="JSP_SDR_D_INFORMATIONS_COMPLEMENTAIRES" />
								</a>
								<input	type="hidden" 
										name="idInfoCompl" 
										value="<%=viewBean.getIdInfoComplementaire()%>" />
								<input	type="hidden" 
										name="idTiers" 
										value="<%=viewBean.getIdTiers()%>" />
								<input	type="hidden" 
										name="idDemandeRente" 
										value="<%=viewBean.getIdDemandeRente()%>" />
							</td>
							<td colspan="3">
								&nbsp;
							</td>
						</tr>
				<%@ include file="/theme/detail/bodyButtons.jspf" %>
				
				<input type="button" value="<ct:FWLabel key="JSP_SUIVANT"/> (alt+<ct:FWLabel key="AK_REQ_SUIVANT"/>)" onclick="suivant();" accesskey="<ct:FWLabel key="AK_REQ_SUIVANT"/>">
				<%if(hasDroitBoutonArret)){ %>
					<input type="button" value="<ct:FWLabel key="JSP_ARRET"/> (alt+<ct:FWLabel key="AK_REQ_ARRET"/>)" onclick="arret()" accesskey="<ct:FWLabel key="AK_REQ_ARRET"/>">
				<%} %>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%@ include file="/theme/detail/footer.jspf" %>
