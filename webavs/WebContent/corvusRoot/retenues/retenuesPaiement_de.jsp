<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.corvus.api.retenues.IRERetenues"%>
<%@ page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="globaz.corvus.vb.retenues.RERetenuesPaiementViewBean"%>
<%@ page import="globaz.framework.util.FWCurrency"%>
<%@ page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.osiris.api.APISection"%>
<%@ page import="globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiersManager"%>
<%@ page import="globaz.osiris.db.comptes.CASectionManager"%>
<%@ page import="globaz.osiris.db.comptes.CACompteAnnexeManager"%>
<%@ page import="globaz.osiris.external.IntRole"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_RET_D"

	idEcran="PRE0021";

	RERetenuesPaiementViewBean viewBean = (RERetenuesPaiementViewBean) session.getAttribute("viewBean");
	
	selectedIdValue = viewBean.getIdRetenue();

	String idTierRequerant = request.getParameter("forIdTiersBeneficiaire");
	String idRenteAccordee = request.getParameter("forIdRenteAccordee");
	String idBaseCalcul = request.getParameter("idBaseCalcul");
	String csTypeBasesCalcul = request.getParameter("csTypeBasesCalcul");
	
	String montantRenteAccordee = request.getParameter("montantRenteAccordee");
	if (montantRenteAccordee == null) {
		montantRenteAccordee = viewBean.getMontantRenteAccordee();
	}
	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
	String csType = request.getParameter("csTypeRetenue");
	if (csType != null) {
		viewBean.setCsTypeRetenue(csType);
	}
	
	String params = "&provenance1=TIERS";
		  params += "&provenance2=CI";
	String jspLocation = servletContext + "/corvusRoot/numeroSecuriteSocialeSF_select.jsp";
	String options = viewBean.getListeRubrique(viewBean.getIdRubrique());
	
	boolean hasUpdateRight = controller.getSession().hasRight(IREActions.ACTION_RETENUES_SUR_PMT, FWSecureConstants.UPDATE);
	boolean isMethodAdd = "add".equals(request.getParameter("_method"));
	
	bButtonUpdate =  bButtonUpdate && hasUpdateRight && !isMethodAdd;
	bButtonValidate = (bButtonValidate || isMethodAdd) && hasUpdateRight;
	bButtonCancel = bButtonCancel && hasUpdateRight;

	if (viewBean.isMontantTotalDejaRetenuIsSupA0()){
		bButtonDelete = false;
	} else {
		bButtonDelete = bButtonDelete && hasUpdateRight;
	}
	
	// BZ 6398
	if ("true".equals(request.getParameter("reloadAll"))) {
%>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript">
	$(document).ready(function () {
		// rechargement de toute la CA page
		var $parent = parent.jQuery(parent.document);
		var $mainForm = $parent.find('form[name="mainForm"]');
		var $boutonNouveau = $mainForm.find('input[name="btnNew"]');
		$boutonNouveau.click();
		$mainForm.submit();
	});
</script>
<%
	} else {
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<script type="text/javascript" src="<%=servletContext%>/corvusRoot/script/retenues/retenuesPaiement_de.js"></script>
<script type="text/javascript">
	retenuePaiement.s_actionRetenueSurPaiement = '<%=IREActions.ACTION_RETENUES_SUR_PMT%>';
	retenuePaiement.s_messageConfirmationSuppression = "<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO' />";
	retenuePaiement.b_retenueDejaUtiliseeDansPaiementMensuel = <%=viewBean.isMontantTotalDejaRetenuIsSupA0()%>;

	function init() {
		// recharger la page rcListe du parent si une modification a ete effectuee
<%		if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
		// mise a jour de la liste du parent
		if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		}
<%		} %>
		document.forms[0].target="_self";
	}

	function testDisabledTable() {
<%		if (viewBean.isMontantTotalDejaRetenuIsSupA0()) { %>
		var body = document.getElementsByTagName("TABLE")[1];
		disabledTable(body.getElementsByTagName("input"));
		disabledTable(body.getElementsByTagName("select"));
<%		} %>
	}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RET_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
					<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td width="25%">
								<input	type="hidden" 
										name="forIdTiersBeneficiaire" 
										value="<%=idTierRequerant%>" />
								<input	type="hidden" 
										name="idTierRequerant" 
										value="<%=idTierRequerant%>" />
								<input	type="hidden" 
										name="forIdRenteAccordee" 
										value="<%=idRenteAccordee%>" />
								<input	type="hidden" 
										name="idRetenue" 
										value="<%=viewBean.getIdRetenue()%>" />
								<input	type="hidden" 
										name="idRenteAccordee" 
										value="<%=idRenteAccordee%>" />
								<input	type="hidden" 
										name="montantRenteAccordee" 
										value="<%=montantRenteAccordee%>" />
								<input	type="hidden" 
										name="menuOptionToLoad" 
										value="<%=menuOptionToLoad%>" />	
								<ct:FWLabel key="JSP_RET_D_TYPE" />
							</td>
							<td width="25%">
<%
		boolean interdireChangementTypeRetenue = !isMethodAdd;
		String changementTypeRetenueDisabled = interdireChangementTypeRetenue ? "true" : "";
%>
								<ct:select	id="csTypeRetenue" 
											name="csTypeRetenue" 
											disabled="<%=changementTypeRetenueDisabled%>" 
											defaultValue="<%=viewBean.getCsTypeRetenue()%>">
									<ct:optionsCodesSystems csFamille="<%=IRERetenues.CS_GROUPE_TYPE_RETENUE%>" />
								</ct:select>
							</td>
							<td width="50%" colspan="2" rowspan="4">
								<pre><span class="IJAfficheText"><%=viewBean.getCcpOuBanqueFormatte()%></span></pre>
								<pre><span class="IJAfficheText"><%=viewBean.getAdresseFormattee()%></span></pre>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_RET_D_MONTANT_A_RETENIR" />
							</td>
							<td>
								<input	type="text" 
										name="montantRetenuMensuel" 
										class="valeursCommunes modifiable nonModifiableApresPaiement" 
										value="<%=new FWCurrency(viewBean.getMontantRetenuMensuel()).toStringFormat()%>" 
										data-g-amount="mandatory:true,unsigned:true" />
							</td>
						</tr>
						<tr>
							<td colspan="4">
								&nbsp;
							</td>
						</tr>
						<tr class="trOptionnels <%=IRERetenues.CS_TYPE_ADRESSE_PMT%>">
							<td>
								<ct:FWLabel key="JSP_RET_D_ADRESSE_DE_PAIEMENT" />
							</td>
							<td colspan="3">
<%
		if (viewBean.isModifiable()) {
%>								<ct:FWSelectorTag	name="selecteurAdresses" 
													methods="<%=viewBean.getMethodesSelectionAdressePaiement()%>" 
													providerApplication="pyxis" 
													providerPrefix="TI" 
													providerAction="pyxis.adressepaiement.adressePaiement.chercher" 
													target="fr_main" 
													redirectUrl="<%=mainServletPath%>" />
<%	
		}
%>							</td>
						</tr>
						<tr class="trOptionnels <%=IRERetenues.CS_TYPE_ADRESSE_PMT%>">
							<td colspan="4">
								&nbsp;
							</td>
						</tr>
						<tr class="trOptionnels <%=IRERetenues.CS_TYPE_ADRESSE_PMT%>">
							<td>
								<ct:FWLabel key="JSP_RET_D_BENEFICIAIRE_NSS_AFFILIE" />
							</td>
							<td>
								<span>
									<strong>
										<%=viewBean.getNssTiersAdressePmt()%>
									</strong>
								</span>
								<input	type="hidden" 
										name="nssBeneficiaire" 
										class="valeursSpecifiques" 
										value="<%=viewBean.getNssTiersAdressePmt()%>" />
								<input	type="hidden" 
										name="idTiersAdressePmt" 
										class="valeursSpecifiques" 
										value="<%=viewBean.getIdTiersAdressePmt()%>" />
								<input	type="hidden" 
										name="idDomaineApplicatif" 
										class="valeursSpecifiques" 
										value="<%=viewBean.getIdDomaineApplicatif()%>" />
							</td>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr class="trOptionnels <%=IRERetenues.CS_TYPE_ADRESSE_PMT%>">
							<td>
								<ct:FWLabel key="JSP_RET_D_REFERENCE_INTERNE" />
							</td>
							<td colspan="3">
								<input	type="text" 
										name="referenceInterne" 
										value="<%=viewBean.getReferenceInterne()%>" 
										class="libelleLong valeursSpecifiques modifiable nonModifiableApresPaiement" 
										maxlength="23" />
							</td>
						</tr>
						<tr class="trOptionnels <%=IRERetenues.CS_TYPE_IMPOT_SOURCE%>">
							<td>
								<ct:FWLabel key="JSP_RET_D_CANTON_IMPOSITION" />
							</td>
							<td>
								<ct:FWCodeSelectTag	codeType="PYCANTON" 
													name="cantonImposition" 
													notation="class=\"valeursSpecifiques modifiable nonModifiableApresPaiement\"" 
													defaut="<%=viewBean.getCantonImposition()%>" 
													wantBlank="true" />
							</td>
							<td>
								<ct:FWLabel key="JSP_RET_D_TAUX" />
							</td>
							<td>
								<input	type="text"	
										name="tauxImposition" 
										class="valeursSpecifiques modifiable nonModifiableApresPaiement" 
										value="<%=JadeStringUtil.isDecimalEmpty(viewBean.getTauxImposition())?"":viewBean.getTauxImposition()%>" 
										data-g-rate="nbMaxDecimal:2" />
								&nbsp;%
							</td>
						</tr>
						<tr class="trOptionnels <%=IRERetenues.CS_TYPE_COMPTE_SPECIAL%>">
							<td>
								<ct:FWLabel key="JSP_RET_D_RUBRIQUE" />
							</td>
							<td colspan="3">
								<select name="idRubrique" >
									<%=options%>
								</select>
							</td>
						</tr>
						<tr class="trOptionnels <%=IRERetenues.CS_TYPE_FACTURE_EXISTANTE%> <%=IRERetenues.CS_TYPE_FACTURE_FUTURE%>">
							<td>
								<ct:FWLabel key="JSP_RET_D_REFERENCE_EXTERNE" />
							</td>
							<td>
								<ct:widget	id='widgetIdExterne' 
											name='idExterne' 
											defaultValue="<%=viewBean.getIdExterne()%>"
											styleClass="widgetIdExterne valeursSpecifiques modifiable nonModifiableApresPaiement" 
											notation="data-g-string=' '" >
									<ct:widgetManager 	managerClassName="<%=CACompteAnnexeManager.class.getName()%>" 
														defaultLaunchSize="0" >
										<ct:widgetCriteria 	criteria="likeIdExterneRole" 
															label="JSP_RET_D_REFERENCE_EXTERNE" />
									<ct:widgetCriteria 	criteria="forIdTiersIn" 
														fixedValue="<%=viewBean.getIdTiersFamilleInline()%>" 
														label="JSP_RET_D_REFERENCE_EXTERNE" />
										<ct:widgetLineFormatter format="<b>#{idExterneRole}</b> - #{description} - #{cARole.description}" />
										<ct:widgetJSReturnFunction>
											<script type="text/javascript">
												function(element){
													$('#role').val($(element).attr('cARole.idRole'));
													$('#idCompteAnnexe').val($(element).attr('idCompteAnnexe'));
													resetSection();
													updateLabelRole();
													this.value = $(element).attr('idExterneRole');
												}
											</script>
										</ct:widgetJSReturnFunction>
									</ct:widgetManager>
								</ct:widget>
							</td>
							<td colspan="2">
								<div id="divRole" class="valeursAMasquer">
									<ct:FWLabel key="JSP_RET_D_ROLE" />
									&nbsp;:&nbsp;
									<b>
										<span id="labelRole">
										</span>
									</b>
								</div>
								<select	id="role" 
										name="role" 
										class="valeursSpecifiques" 
										style="display:none;">
									<option value="<%=IntRole.ROLE_ASSURE%>" <%=IntRole.ROLE_ASSURE.equals(viewBean.getRole())?"selected=\"selected\"":""%>>
										<%=viewBean.getSession().getCodeLibelle(IntRole.ROLE_ASSURE)%>
									</option>
									<option value="<%=IntRole.ROLE_AFFILIE%>" <%=IntRole.ROLE_AFFILIE.equals(viewBean.getRole())?"selected=\"selected\"":""%>>
										<%=viewBean.getSession().getCodeLibelle(IntRole.ROLE_AFFILIE)%>
									</option>
									<option value="<%=IntRole.ROLE_ADMINISTRATION%>" <%=IntRole.ROLE_ADMINISTRATION.equals(viewBean.getRole())?"selected=\"selected\"":""%>>
										<%=viewBean.getSession().getCodeLibelle(IntRole.ROLE_ADMINISTRATION)%>
									</option>
									<option value="<%=IntRole.ROLE_BANQUE%>" <%=IntRole.ROLE_BANQUE.equals(viewBean.getRole())?"selected=\"selected\"":""%>>
										<%=viewBean.getSession().getCodeLibelle(IntRole.ROLE_BANQUE)%>
									</option>
									<option value="<%=IntRole.ROLE_CONTRIBUABLE%>" <%=IntRole.ROLE_CONTRIBUABLE.equals(viewBean.getRole())?"selected=\"selected\"":""%>>
										<%=viewBean.getSession().getCodeLibelle(IntRole.ROLE_CONTRIBUABLE)%>
									</option>
									<option value="<%=IntRole.ROLE_APG%>" <%=IntRole.ROLE_APG.equals(viewBean.getRole())?"selected=\"selected\"":""%>>
										<%=viewBean.getSession().getCodeLibelle(IntRole.ROLE_APG)%>
									</option>
									<option value="<%=IntRole.ROLE_IJAI%>" <%=IntRole.ROLE_IJAI.equals(viewBean.getRole())?"selected=\"selected\"":""%>>
										<%=viewBean.getSession().getCodeLibelle(IntRole.ROLE_IJAI)%>
									</option>
									<option value="<%=IntRole.ROLE_AF%>" <%=IntRole.ROLE_AF.equals(viewBean.getRole())?"selected=\"selected\"":""%>>
										<%=viewBean.getSession().getCodeLibelle(IntRole.ROLE_AF)%>
									</option>
									<option value="<%=IntRole.ROLE_RENTIER%>" <%=IntRole.ROLE_RENTIER.equals(viewBean.getRole())?"selected=\"selected\"":""%>>
										<%=viewBean.getSession().getCodeLibelle(IntRole.ROLE_RENTIER)%>
									</option>
									<option value="<%=IntRole.ROLE_AFFILIE_PARITAIRE%>" <%=IntRole.ROLE_AFFILIE_PARITAIRE.equals(viewBean.getRole())?"selected=\"selected\"":""%>>
										<%=viewBean.getSession().getCodeLibelle(IntRole.ROLE_AFFILIE_PARITAIRE)%>
									</option>
									<option value="<%=IntRole.ROLE_AFFILIE_PERSONNEL%>" <%=IntRole.ROLE_AFFILIE_PERSONNEL.equals(viewBean.getRole())?"selected=\"selected\"":""%>>
										<%=viewBean.getSession().getCodeLibelle(IntRole.ROLE_AFFILIE_PERSONNEL)%>
									</option>
									<option value="<%=IntRole.ROLE_ADMINISTRATEUR%>" <%=IntRole.ROLE_ADMINISTRATEUR.equals(viewBean.getRole())?"selected=\"selected\"":""%>>
										<%=viewBean.getSession().getCodeLibelle(IntRole.ROLE_ADMINISTRATEUR)%>
									</option>
								</select>
							</td>
						</tr>
						<tr class="trOptionnels <%=IRERetenues.CS_TYPE_FACTURE_EXISTANTE%>">
							<td>
								<ct:FWLabel key="JSP_RET_D_FACTURE" />
							</td>
							<td>
								<input 	type="hidden" 
										id="idCompteAnnexe" 
										name="idCompteAnnexe" 
										class="valeursSpecifiques" 
										value="" />
								<ct:widget 	id='widgetNoFacture' 
											name='noFacture' 
											defaultValue="<%=viewBean.getNoFacture()%>" 
											styleClass="widgetNoFacture valeursSpecifiques modifiable nonModifiableApresPaiement" 
											notation="data-g-string=' '" >
									<ct:widgetManager 	managerClassName="<%=CASectionJoinCompteAnnexeJoinTiersManager.class.getName()%>" 
														defaultLaunchSize="0" 
														defaultSearchSize="6" >
										<ct:widgetCriteria 	criteria="likeIdExterne" 
															label="JSP_RET_D_FACTURE" />
									<ct:widgetCriteria 	criteria="forIdTiersIn" 
														fixedValue="<%=viewBean.getIdTiersFamilleInline()%>" 
														label="..." />
										<ct:widgetCriteria 	criteria="forSoldePositif" 
															fixedValue="true" 
															label="..." />
										<ct:widgetDynamicCriteria>
											<script type="text/javascript">
												function(){
													return 'forIdCompteAnnexe=' + $('#idCompteAnnexe').val();
												}
											</script>
										</ct:widgetDynamicCriteria>
										<ct:widgetLineFormatter format="<b>#{idExterne}</b> : #{solde}CHF #{typeSection}<br/>(#{categorieSection})" />
										<ct:widgetJSReturnFunction>
											<script type="text/javascript">
												function(element){
													$('#widgetIdExterne').val($(element).attr('idExterneRole'));
													$('#idCompteAnnexe').val($(element).attr('idCompteAnnexe'));
													$('#role').val($(element).attr('idRole'));
													$('#typeSection').val($(element).attr('idTypeSection'));
													updateMontantTotalARetenir($(element).attr('solde'));
													updateLabelTypeSection();
													updateLabelRole();
													this.value = $(element).attr('idExterne');
													$('#dateDebutRetenue').focus();
												}
											</script>
										</ct:widgetJSReturnFunction>
									</ct:widgetManager>
								</ct:widget>
							</td>
							<td colspan="2">
								<div id="divTypeSection" class="valeursAMasquer">
									<ct:FWLabel key="JSP_RET_TYPE_SECTION" />
									&nbsp;:&nbsp;
									<b>
										<label id="labelTypeSection">
										</label>
									</b>
								</div>
								<select	id="typeSection" 
										name="idTypeSection" 
										class="valeursSpecifiques" 
										style="display: none;" >
<%
		for(String[] elements : viewBean.getTypesSectionsCA()) {
%>									<option value="<%=elements[0]%>" <%=elements[0].equals(viewBean.getIdTypeSection())?"selected=\"selected\"":""%>>
										<%=elements[1]%>
									</option>
<%
		} 
%>								</select>
							</td>
						</tr>
						<tr>
							<td colspan="4">
								&nbsp;
							</td>
						</tr>
						<tr class="trOptionnels <%=IRERetenues.CS_TYPE_ADRESSE_PMT%> <%=IRERetenues.CS_TYPE_FACTURE_EXISTANTE%> <%=IRERetenues.CS_TYPE_FACTURE_FUTURE%>">
							<td>
								<ct:FWLabel key="JSP_RET_D_MONTANT_TOTAL_A_RETENIR" />
							</td>
							<td>
								<input	type="text" 
										id="montantTotalARetenir"
										name="montantTotalARetenir" 
										class="valeursCommunes modifiable" 
										value="<%=new FWCurrency(viewBean.getMontantTotalARetenir()).toStringFormat()%>" 
										data-g-amount="mandatory:true,unsigned:true" />
							</td>
							<td>
								<ct:FWLabel key="JSP_RET_D_MONTANT_DEJA_RETENU" />
							</td>
							<td>
								<span style="text-align: right;">
									<strong>
										<%=new FWCurrency(viewBean.getMontantDejaRetenu()).toStringFormat()%>
									</strong>
								</span>
								<input	type="hidden" 
										name="montantDejaRetenu" 
										value="<%=new FWCurrency(viewBean.getMontantDejaRetenu()).toStringFormat()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_RET_D_DATE_DEBUT_RETENUES" />
							</td>
							<td>
								<input	id="dateDebutRetenue" 
										name="dateDebutRetenue" 
										class="valeursCommunes modifiable nonModifiableApresPaiement" 
										data-g-calendar="type:month" 
										value="<%=viewBean.getDateDebutRetenue()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_RET_D_DATE_FIN_RETENUES" />
							</td>
							<td>
								<input	id="dateFinRetenue" 
										name="dateFinRetenue" 
										class="valeursCommunes modifiable" 
										data-g-calendar="type:month" 
										value="<%=viewBean.getDateFinRetenue()%>" />
							</td>
						</tr>
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%
	}
%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>