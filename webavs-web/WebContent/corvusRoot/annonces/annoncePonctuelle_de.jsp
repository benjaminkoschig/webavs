<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme"%>
<%@page import="globaz.framework.menu.FWMenuBlackBox"%>
<%@page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>
<%@page import="globaz.prestation.tools.PRCodeSystem"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.corvus.vb.annonces.REAnnoncePonctuelleViewBean"%>
<%@page import="globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations"%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="com.google.gson.Gson"%>

<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
	idEcran="PRE0009";

	REAnnoncePonctuelleViewBean viewBean = (REAnnoncePonctuelleViewBean)session.getAttribute("viewBean");
	String params = "&provenance1=TIERS&provenance2=CI";
	String jspLocation = servletContext + "/corvusRoot/numeroSecuriteSocialeSF_select.jsp";

	Map<String,String> csEtatCivil = PRCodeSystem.getCUCSinMap(viewBean.getSession(), "SFECIVIL");
	Map<String,String> codesCasSpeciaux = PRCodeSystem.getCUCSinMap(viewBean.getSession(), IRERenteAccordee.CS_GROUPE_CAS_SPECIAUX_RENTE_01);
	Map<String,String> codesCsCodeInfirmite = PRCodeSystem.getCUCSinMap(viewBean.getSession(), globaz.corvus.api.demandes.IREDemandeRente.CS_GROUPE_INFIRMITE);
	
	Map<String, String> officesAi = viewBean.getMapOfficesAi();

	Map<String, String> cantons = PRCodeSystem.getLibellesPourGroupeInMap(viewBean.getSession(), "PYCANTON");
	
	String btnAnnPonctLabel = viewBean.getSession().getLabel("JSP_BTN_ANN_PONCT");
	String btnAnnPonctNouvelNSSLabel= viewBean.getSession().getLabel("JSP_BTN_ANN_PONCT_NOUVEL_NSS");

	bButtonValidate = false;
	bButtonNew = false;
	bButtonCancel = true;
	bButtonValidate = true && viewBean.getSession().hasRight(IREActions.ACTION_ANNONCES_PONCTUELLES, FWSecureConstants.UPDATE);
	bButtonDelete = false;

	// Les labels de cette page commence par la préfix "JSP_ANN_PONCT"
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%@taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options" />
<ct:menuChange displayId="options" menuId="corvus-optionsrentesaccordees">	
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdRenteAccordee()%>" />
	<ct:menuSetAllParams key="noDemandeRente" value="<%=viewBean.getIdDemandeRente()%>" />
	<ct:menuSetAllParams key="idRenteAccordee" value="<%=viewBean.getIdRenteAccordee()%>" />
	<ct:menuSetAllParams key="idTierRequerant" value="<%=viewBean.getIdTiersBeneficiaire()%>" />
	<ct:menuSetAllParams key="idTiersBeneficiaire" value="<%=viewBean.getIdTiersBeneficiaire()%>" />
	<ct:menuSetAllParams key="idBaseCalcul" value="<%=viewBean.getIdBaseCalcul()%>" />
	<ct:menuSetAllParams key="idRenteCalculee" value="<%=viewBean.getIdRenteCalculee()%>"/>
	<ct:menuSetAllParams key="csTypeBasesCalcul" value="<%=viewBean.getDroitApplique()%>"/>
<%	if (!REPmtMensuel.isValidationDecisionAuthorise(viewBean.getSession())) { %>
	<ct:menuActivateNode active="no" nodeId="optdiminution" />
<%	}%>
</ct:menuChange>

<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/ajaxUtils.js"></script>
<script type="text/javascript" src="<%=servletContext%>/corvusRoot/script/annonces/annoncePonctuelle_de.js"></script>
<script type="text/javascript">
	annoncePonctuelle.o_mapCodesCasSpeciaux = <%=new Gson().toJson(codesCasSpeciaux)%>;
<%	if (viewBean.getMapVerification().size() > 0) { %>
	annoncePonctuelle.o_mapAnnonceRentesLieeSiModif = <%=new Gson().toJson(viewBean.getMapVerification())%>;
<%	} %>

	function init(){
<%	if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
		annoncePonctuelle.b_hasError = true;
		errorObj.text="<%=viewBean.getMessage().replaceAll("\n", "<br/>")%>";
		showErrors();
		errorObj.text="";
<%	}%>
	}
	
	function cancel(){
		annoncePonctuelle.$userAction.val("<%=globaz.corvus.servlet.IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>.chercher");
		annoncePonctuelle.$form.submit();
	}

	function upd(){
		annoncePonctuelle.enableModifiableInput();
	}

	function del(){;}

	function validate() {
		annoncePonctuelle.$userAction.val("<%=IREActions.ACTION_ANNONCES_PONCTUELLES%>.modifier");
		return true;
	}

	function creerAnnoncePonctuelle() {
		annoncePonctuelle.$userAction.val("<%=IREActions.ACTION_ANNONCES_PONCTUELLES%>.ajouterAnnoncePonctuelle");
		action(COMMIT);
	}

	function creerAnnoncePonctuelleAncienNss() {
		annoncePonctuelle.$userAction.val("<%=IREActions.ACTION_ANNONCES_PONCTUELLES%>.ajouterAnnoncePonctuelleAncienNss");
		action(COMMIT);
	}
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
				<ct:FWLabel key="JSP_ANN_PONCT_TITRE" />
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>
								<table class="mainContent" width="100%" border="0">
									<tbody>
										<tr>
											<td colspan="6">
												<re:PRDisplayRequerantInfoTag	session="<%=(globaz.globall.db.BSession)controller.getSession()%>" 
																				idTiers="<%=viewBean.getIdTiersBeneficiaire()%>" 
																				style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_BEN%>" />
											</td>
										</tr>
										<tr>
											<td colspan="6">
												&nbsp;
											</td>
										</tr>
								<%	if(!JadeStringUtil.isEmpty(viewBean.getAncienNSS())){%>
										<tr>
											<td class="labelTD 1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_ANCIEN_NSS" />
											</td>
											<td class="interCollone">
											</td>
											<td class="2emeCollone">
												<ct1:nssPopup	name="ancienNSS"
																cssclass="modifiable"  
																onFailure="" 
																onChange="" 
																params="<%=params%>" 
																value="<%=viewBean.getAncienNSS()%>" 
																newnss="" 
																jspName="<%=jspLocation%>" 
																avsMinNbrDigit="3" 
																nssMinNbrDigit="3" />
											</td>
										</tr>
										<tr>
											<td colspan="3">
												&nbsp;
											</td>
										</tr>
								<%	}%>
										<tr>
											<td class="labelTD 1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_ETAT_CIVIL" />
											</td>
											<td class="interCollone">
											</td>
											<td class="2emeCollone">
												<b>
													<%=viewBean.getSession().getCodeLibelle(viewBean.getCsEtatCivil())%>
												</b>
												<input 	type="hidden" 
														name="csEtatCivil" 
														value="<%=viewBean.getCsEtatCivil()%>" />
											</td>
											<td colspan="3">
												&nbsp;
											<td/>
										</tr>
										<tr>
											<td colspan="6">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td class="labelTD 1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_GENRE_PRESTATION" />
											</td>
											<td class="interCollone">
											</td>
											<td class="2emeCollone">
												<b>
													<%=viewBean.getGenrePrestation()%>
												</b>
												<input	type="hidden" 
														id="genrePrestation" 
														name="genrePresattion" 
														value="<%=viewBean.getGenrePrestation()%>" />
											</td>
											<td class="labelTD 3emeCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_MONTANT_PRESTATION" />
											</td>
											<td class="interCollone">
											</td>
											<td class="4emeCollone">
												<b>
													<%=viewBean.getMontantPrestation()%>
												</b>
												<input	type="hidden" 
														id="montantPrestation" 
														name="montantPrestation" 
														value="<%=viewBean.getMontantPrestation()%>" />
											</td>	
										</tr>
										<tr>
											<td class="labelTD 1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_DATE_DEBUT" />
											</td>
											<td class="interCollone">
											</td>
											<td class="2emeCollone">
												<b>
													<%=viewBean.getDateDebut()%>
												</b> 
												<input	type="hidden" 
														id="dateDebut" 
														name="dateDebut" 
														value="<%=viewBean.getDateDebut()%>" />
											</td>
											<td class="labelTD">
												<ct:FWLabel key="JSP_ANN_PONCT_CANTON" />
											</td>
											<td class="interCollone">
											</td>
											<td class="2emeCollone">
												<select class="modifiable annonceRentesLieeSiModif"
														id="canton"	
														name="csCanton"
														value="<%=viewBean.getCsCanton()%>"> 
													<option value="">
														<ct:FWLabel key="JSP_ANN_PONCT_CANTON_INCONNU" />
													</option>
											<%	for(String unCanton : cantons.keySet()) {%>
													<option value="<%=cantons.get(unCanton)%>" <%=cantons.get(unCanton).equals(viewBean.getCsCanton())?"selected ":""%>>
														<%=unCanton%>
													</option>
											<%	} %>
												</select>
											</td>
										</tr>
										<tr>
											<td colspan="6">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td class="labelTD 1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_REFUGIE" />
											</td>
											<td class="interCollone">
											</td>
											<td class="2emeCollone">
												<input	type="checkbox"
														class="modifiable" 
														name="codeRefugie" 
														id="codeRefugie" 
														value="on" 
														<%=viewBean.isRefugie()?"checked":""%> />
											</td>
											<td colspan="3">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td colspan="6">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td class="labelTD 1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_NSS_COMPLEMENTAIRE_1" />
											</td>
											<td class="interCollone">
											</td>
											<td class="2emeCollone">
												<ct1:nssPopup	name="nssComplementaire1" 
																cssclass="modifiable" 
																onFailure="" 
																onChange="nssComplementaire1Change(tag);" 
																params="<%=params%>" 
																value="<%=viewBean.getNssComplementaire1()%>" 
																newnss=""
																jspName="<%=jspLocation%>" 
																avsMinNbrDigit="3" 
																nssMinNbrDigit="3" />
												<input	type="hidden" 
														name="idTiersComplementaire1" 
														value="<%=viewBean.getIdTiersComplementaire1()%>" />
											</td>
											<td class="labelTD 3emeCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_NSS_COMPLEMENTAIRE_2" />
											</td>
											<td class="interCollone">
											</td>
											<td class="4emeCollone">
												<ct1:nssPopup	name="nssComplementaire2" 
																cssclass="modifiable" 
																onFailure="" 
																onChange="nssComplementaire2Change(tag);" 
																params="<%=params%>"
																value="<%=viewBean.getNssComplementaire2()%>" 
																newnss=""
																jspName="<%=jspLocation%>" 
																avsMinNbrDigit="3" 
																nssMinNbrDigit="3" />
												<input	type="hidden" 
														name="idTiersComplementaire2" 
														value="<%=viewBean.getIdTiersComplementaire2()%>" />
											</td>
										</tr>
										<tr>
											<td colspan="6">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td class="labelTD 1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_DROIT_APPLIQUE" />
											</td>
											<td class="interCollone">
											</td>
											<td colspan="2emeCollone">
												<b>
													<%=viewBean.getDroitApplique()%>
												</b>
												<input	type="hidden" 
														name="droitApplique" 
														value="<%=viewBean.getDroitApplique()%>" />
											</td>
											<td colspan="3">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td class="labelTD 1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_CAS_SPECIAUX" />
											</td>
											<td class="interCollone">
											</td>
											<td class="2emeCollone" colspan="4">
												<input	type="text" 
														id="cs1" 
														name="cs1" 
														class="casSpeciaux modifiable" 
														size="2" 
														maxlength="2" 
														value="<%=viewBean.getCs1()%>" />
												<input	type="text" 
														id="cs2" 
														name="cs2" 
														class="casSpeciaux modifiable"  
														size="2" 
														maxlength="2" 
														value="<%=viewBean.getCs2()%>" />
												<input	type="text" 
														id="cs3" 
														name="cs3"  
														class="casSpeciaux modifiable" 
														size="2" 
														maxlength="2" 
														value="<%=viewBean.getCs3()%>" />
												<input	type="text" 
														id="cs4" 
														name="cs4"  
														class="casSpeciaux modifiable" 
														size="2" 
														maxlength="2" 
														value="<%=viewBean.getCs4()%>" />
												<input	type="text" 
														id="cs5" 
														name="cs5"  
														class="casSpeciaux modifiable" 
														size="2" 
														maxlength="2" 
														value="<%=viewBean.getCs5()%>" />
												<img	id="imageOKCodeCasSpeciaux" 
														name="imageOKCodeCasSpeciaux" 
														src="<%=request.getContextPath()%>/images/ok.gif" 
														alt="" />
												<img	id="imageKOCodeCasSpeciaux" 
														name="imageKOCodeCasSpeciaux" 
														src="<%=request.getContextPath()%>/images/erreur.gif" 
														alt="" />
											</td>
										</tr>
										<tr>
											<td class="labelTD 1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_RAM" />
											</td>
											<td class="interCollone">
											</td>
											<td class="2emeCollone">
												<input	type="text" 
														class="modifiable annonceRentesLieeSiModif" 
														id="RAM"
														name="RAM" 
														value="<%=viewBean.getRAM()%>" 
														size="8" />
											</td>
											<td colspan="3">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td colspan="6">
												&nbsp;
											</td>
										</tr>
<%
	if (viewBean.isRenteAI()) {
%>										<tr>
											<td class="labelTD 1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_DEGRE_INVALIDITE" />
											</td>
											<td class="interCollone">
											</td>
											<td class="2emeCollone">
												<input 	type="text" 
														id="degreInvalidite" 
														name="degreInvalidite" 
														class="modifiable annonceRentesLieeSiModif" 
														value="<%=viewBean.getDegreInvalidite()%>" 
														maxlength="3" 
														size="3" />
											</td>
										</tr>
<%
	}
	if (viewBean.isRenteAPI()) {
%>										<tr>
											<td class="labelTD 1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_GENRE_API" />
											</td>
											<td class="interCollone">
											</td>
											<td class="2emeCollone" colspan="4">
												<select	id="csGenreDroitAPI" 
														name="csGenreDroitAPI" 
														class="modifiable">
<%
		for (JadeCodeSysteme unCodeSysteme : viewBean.getFamilleCsGenreDroitAPI()) {
%>													<option value="<%=unCodeSysteme.getIdCodeSysteme()%>"<%=unCodeSysteme.getIdCodeSysteme().equals(viewBean.getCsGenreDroitAPI()) ? " selected=\"selected\"" : ""%>>
														<%=unCodeSysteme.getTraduction(viewBean.getLangue())%>
													</option>
<%
		}
%>												</select>
											</td>
										</tr>
<%
	}
	if (viewBean.isRenteAI() || viewBean.isRenteAPI()) {
%>										<tr>
											<td class="labelTD 1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_CLE_INFIRM_AYANT_DROIT" />
											</td>
											<td class="interCollone">
											</td>
											<td class="2emeCollone">
												<input	type="text" 
														id="cleInfirmite" 
														name="cleInfirmite" 
														class="modifiable annonceRentesLieeSiModif" 
														size="5" 
														value="<%=viewBean.getCleInfirmite()%>" />
											</td>
											<td colspan="3">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td class="labelTD 1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_OFFICE_AI" />
											</td>
											<td class="interCollone">
											</td>
											<td colspan="4">
											<%	String defaultValue = globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getOfficeAI())?viewBean.getNoOfficeAICantonal():viewBean.getOfficeAI();%>
												<select class="modifiable annonceRentesLieeSiModif"
														id="codeOfficeAi"	
														name="officeAI"
														value="<%=defaultValue%>"> 
											<%	for(String codeOfficeAi : officesAi.keySet()) {%>
													<option value="<%=codeOfficeAi%>" <%=codeOfficeAi.equals(defaultValue)?"selected ":""%>>
														<%=codeOfficeAi%> - <%=officesAi.get(codeOfficeAi)%>
													</option>
											<%	} %>
												</select>
											</td>
										</tr>
<%	
	}
	if (viewBean.isRenteAI()) {
		boolean b = viewBean.getIsInvaliditePrecoce() != null 
					&& viewBean.getIsInvaliditePrecoce().booleanValue();
%>										<tr>
											<td class="labelTD 1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_INVALIDITE_PRECOCE" />
											</td>
											<td class="interCollone">
											</td>
											<td class="2emeCollone">
												<input	type="checkbox" 
														id="isInvaliditePrecoce" 
														name="isInvaliditePrecoce" 
														class="modifiable annonceRentesLieeSiModif" 
														value="on"
														<%=b?"CHECKED":""%> />
											</td>
											<td colspan="3">
												&nbsp;
											</td>
										</tr>
<%
	}
	if (viewBean.isRenteAI() || viewBean.isRenteAPI()) {
%>
										<tr>
											<td colspan="6">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td class="labelTD 1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_SURVENANCE_EVENEMENT_ASSURE_AYANT_DROIT" />
											</td>
											<td class="interCollone">
											</td>
											<td class="2emeCollone">
												<input	type="text" 
														data-g-calendar="type:month" 
														id="survenanceEvenementAssure" 
														name="survenanceEvenementAssure" 
														class="modifiable annonceRentesLieeSiModif" 
														value="<%=viewBean.getSurvenanceEvenementAssure()%>" />
											</td>
											<td colspan="3">
												&nbsp;
											</td>
										</tr>
<%
	}
	if ("9".equals(viewBean.getDroitApplique()) 
		|| "09".equals(viewBean.getDroitApplique())) {
%>										<tr>
											<td class="labelTD 1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_REVENU_PRIS_EN_COMPTE" />
											</td>
											<td class="interCollone">
											</td>
											<td class="2emeCollone">
												<input	type="text" 
														name="revenuPrisEnCompte9" 
														class="modifiable" 
														value="<%=viewBean.getRevenuPrisEnCompte9()%>" 
														maxlength="1" 
														size="1" />
											</td>
											<td colspan="3">
												&nbsp;
											</td>
										</tr>
<%
	}
%>										<tr>
											<td colspan="6">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td colspan="6">
												&nbsp;
											</td>
										</tr>
								</tbody>
							</table>
							<table class="checkboxContent">
								<tbody>
										<tr>
											<td class="labelTD" class="1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_ANNONCE_RENTE" />
											</td>
											<td class="interCollone">
											</td>
											<td class="2emeCollone">
												<input	type="checkbox" 
														class="genererAnnonceCheckbox" 
														id="annonceRente" 
														name="creerAnnonceRente" 
														value="on" />
												<span class="necessaire">
													<b>
														<ct:FWLabel key="JSP_ANN_PONCT_NECESSAIRE"/>
													</b>
												</span>
											</td>
											<td class="3emeCollone">
										<ct:ifhasright	element="<%=IREActions.ACTION_ANNONCES_PONCTUELLES%>" 
														crud="u">
													<input	class="btnCtrl bouttonAnnoncePonctuelle" 
															id="btnCreerAnnoncePonctuelle" 
															type="button" 
															value="<%=btnAnnPonctLabel%>" 
															onclick="creerAnnoncePonctuelle()" />
										</ct:ifhasright>
												<span class="pendantValidation">
													<ct:FWLabel key="JSP_ANN_PONCT_PENDANT_VALIDATION" />
												</span>
												&nbsp;
											</td>
										</tr>
										<tr>
								<%	// BZ 5176, si rente principale, case à cocher pour générer des annonces ponctuelles sur les rentes liées
									if(viewBean.isBesoinAnnonceRentesLieesSiModification()){ %>
											<td class="labelTD 1ereCollone">
												<ct:FWLabel key="JSP_ANN_PONCT_ANNONCE_RENTES_LIEES" />
											</td>
											<td class="interCollone">
											</td>
											<td class="2emeCollone">
												<input	type="checkbox"
														class="genererAnnonceCheckbox" 
														id="annonceRentesLiees"
														name="creerAnnoncesRentesLiees" 
														value="on" />
												<span class="necessaire">
													<b>
														<ct:FWLabel key="JSP_ANN_PONCT_NECESSAIRE"/>
													</b>
												</span>
											</td>
								<%	} else { %>
											<td class="labelTD 1ereCollone" colspan="2">
												&nbsp;
											</td>
											<td class="2emeCollone">
												&nbsp;
											</td>
								<%	} %>
											<td class="3emeCollone" colspan="3">
											<ct:ifhasright	element="<%=IREActions.ACTION_ANNONCES_PONCTUELLES%>" 
															crud="u">
										<%	if(!JadeStringUtil.isEmpty(viewBean.getAncienNSS())){%>
												<input	class="btnCtrl bouttonAnnoncePonctuelle" 
														id="btnCreerAnnoncePonctuelleNouvelNSS" 
														type="button" 
														value="<%=btnAnnPonctNouvelNSSLabel%>" 
														onclick="creerAnnoncePonctuelleAncienNss()" />
										<%	}%>
											</ct:ifhasright>
												<span class="pendantValidation">
													<ct:FWLabel key="JSP_ANN_PONCT_PENDANT_VALIDATION" />
												</span>
												&nbsp;
											</td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
						<input	type="hidden" 
								name="idBasesCalcul" 
								value="<%=viewBean.getIdBaseCalcul()%>" />
						<input	type="hidden" 
								name="besoinAnnonceRentesLieesSiModification" 
								value="<%=viewBean.isBesoinAnnonceRentesLieesSiModification()%>" />
						<input	type="hidden" 
								id="mapVerification" 
								name="mapVerification" />
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
	<%-- tpl:put name="zoneButtons" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>