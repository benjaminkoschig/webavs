<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.perseus.vb.rentepont.PFSituationfamilialeViewBean"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeDemande"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatDemande"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%@page import="ch.globaz.pyxis.business.model.TiersSimpleModel"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneSimpleModel"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="globaz.prestation.interfaces.tiers.PRTiersHelper"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.perseus.business.models.situationfamille.EnfantFamille"%>
<%@page import="globaz.jade.context.JadeThread"%>

<% 

PFSituationfamilialeViewBean viewBean = (PFSituationfamilialeViewBean) session.getAttribute("viewBean");
String idRentePont = request.getParameter("idRentePont");

idEcran="PPF1221";
autoShowErrorPopup = true;

PersonneEtendueComplexModel requerant = viewBean.getSituationFamiliale().getRequerant().getMembreFamille().getPersonneEtendue(); 
String afficheRequerant = PFUserHelper.getDetailAssure(objSession,requerant);

PersonneEtendueComplexModel conjoint = viewBean.getSituationFamiliale().getConjoint().getMembreFamille().getPersonneEtendue();
String afficheConjoint = "";
if (!JadeStringUtil.isEmpty(conjoint.getTiers().getIdTiers())) {
	afficheConjoint = "<div>" + PFUserHelper.getDetailAssure(objSession,conjoint) + "</div>";
}

boolean viewBeanInUpdate="upd".equals(request.getParameter("_method"));
bButtonDelete = false;


if (objSession.hasRight("perseus", FWSecureConstants.ADD)) {
	bButtonCancel = true;
	bButtonUpdate = true;
}else{
	bButtonCancel = false;
	bButtonUpdate = false;
}

/*
if (CSEtatDemande.VALIDE.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getCsEtatDemande())) {
	bButtonUpdate = false;
}
*/

%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%@ include file="/perseusRoot/ajax/javascriptsAndCSS.jspf" %>

<%-- tpl:insert attribute="zoneScripts" --%>

	<ct:menuChange displayId="menu" menuId="perseus-menuprincipal" showTab="options" />
	<ct:menuChange displayId="options" menuId="perseus-optionsrentepont">
		<ct:menuSetAllParams key="idRentePont" value="<%=idRentePont%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getId()%>"/>
		<ct:menuSetAllParams key="idDossier" value="<%=viewBean.getRentePont().getSimpleRentePont().getIdDossier() %>"/>
	</ct:menuChange>

<ct:serializeObject variableName="situationFamiliale" objectName="viewBean.situationFamiliale" destination="javascript"/>

<!-- <script type="text/javascript" src="<%=rootPath%>/scripts/rentepont/situationfamiliale_de.js"></script>-->
<script type="text/javascript" src="<%=rootPath%>/scripts/rentepont/situationfamiliale_MembrePart.js"></script>

<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_SITUATION_FAMILIALE="<%=viewBean.getId() %>";
	var PAGE_ID_RENTEPONT="<%=idRentePont %>";
	var MAIN_URL="<%=formAction%>";
	var AVERTISSEMENT_DOSSIER_CONJOINT = "<ct:FWLabel key='JSP_PF_RENTEPONT_D_AVERTISSEMENT_DOSSIER_CONJOINT'/>";
	var AVERTISSEMENT_DOSSIER_CONJOINT_TITRE = "<ct:FWLabel key='JSP_PF_RENTEPONT_D_AVERTISSEMENT_DOSSIER_CONJOINT_TITRE'/>";
	
$(function(){
	// configure AJAX
	$.ajaxSetup({
		url: MAIN_URL,
		error: function (req, textStatus, errorThrown) {ajaxUtils.displayError(req.responseText);}
	});
	
	$('#widgetTiers').hide();

	<% if (viewBean.hasConjointDossier()) { %>
		globazNotation.utils.consoleInfo("<div style='height:120px;'><br/><%= objSession.getLabel("JSP_PF_RENTEPONT_D_AVERTISSEMENT_DOSSIER_CONJOINT") %></div>", "<%= objSession.getLabel("JSP_PF_RENTEPONT_D_AVERTISSEMENT_DOSSIER_CONJOINT_TITRE") %>", true);
	<% } %>
});

</script>

<ct:serializeObject variableName="conjointAjaxViewBean" objectName="viewBean.ajaxConjointViewBean" destination="javascript"/>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_RENTEPONT_FAMILLE_D_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
		
						
						
						<tr>
							<td>
								<table cellpadding="0" cellspacing="0" width="100%" id="ConjointContainer">
									<tr>
										<td><b><ct:FWLabel key="JSP_PF_FAMILLE_D_PERIODE"/></b></td>
										<td colspan="2"><ct:FWLabel key="JSP_PF_FAMILLE_D_PERIODE_DU"/>
											<span><strong><%=" "+viewBean.getRentePont().getSimpleRentePont().getDateDebut() + " " %></strong></span>
											<ct:FWLabel key="JSP_PF_FAMILLE_D_PERIODE_AU"/>
											<span><strong><%=" "+viewBean.getRentePont().getSimpleRentePont().getDateFin()%></strong></span>
										</td>
									</tr>
									<tr><td colspan="3">&nbsp;</td></tr>
									<tr><td colspan="3"><hr></td></tr>
									<tr><td colspan="3">&nbsp;</td></tr>
									<tr>
										<td width="30%" valign="top">
											<b><ct:FWLabel key="JSP_PF_FAMILLE_D_REQUERANT"/></b><br/>
										</td>
										<td colspan="2"><%=afficheRequerant %></td>
									</tr>
									<tr><td colspan="3">&nbsp;</td></tr>
									<tr><td colspan="3"><hr></td></tr>
									<tr><td colspan="3">&nbsp;</td></tr>
									<tr><td colspan="3">
									<tr >
										<td id="lienDonneesFinancieresConjoint" valign="top" width="30%">
											<b><ct:FWLabel key="JSP_PF_FAMILLE_D_CONJOINT"/></b><br/>
										</td>
										<td align="left" width="100%">
											<input type="hidden" name="idRentePont" id="idRentePont" value='<%=idRentePont%>'/>
											<%
												String params = "&provenance1=TIERS";
												String jspLocation = servletContext + "/perseusRoot/numeroSecuriteSocialeSF_select.jsp";
												
											%>
											
											<ct:FWListSelectTag name="csNationaliteAffiche" data="<%=PRTiersHelper.getPays(objSession)%>" notation="style='display: none'"
												defaut="<%=JadeStringUtil.isIntegerEmpty(conjoint.getTiers().getIdPays())?TIPays.CS_SUISSE:conjoint.getTiers().getIdPays()%>"/>
							
											
											<ct:widget id='<%="widgetTiers"%>' name='<%="widgetTiers"%>' styleClass="widgetTiers">
												<ct:widgetService methodName="find" className="<%=PersonneEtendueService.class.getName()%>">
													<ct:widgetCriteria criteria="forNumeroAvsActuel" label="JSP_PF_DOS_W_NSS"/>									
													<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
													<ct:widgetJSReturnFunction>
														<script type="text/javascript">
															function(element){
																var idPays =null, pays=null,res=null,li=null;
																$('#idTiers').val($(element).attr('tiers.id'));
																$('#nss').val($(element).attr('personneEtendue.numAvsActuel'));;
																this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
																idPays = $(element).attr('tiers.idPays');
																pays = $('[name=csNationaliteAffiche]').val(idPays).find(':selected').text();
																$('#resultAutocompete').children().remove();
																res = '<div><b>'+$(element).attr('personneEtendue.numAvsActuel')+'</b><br /> '+ 
																		$(element).attr('tiers.designation1')+' '+
																		$(element).attr('tiers.designation2')+' / '+
																		$(element).attr('personne.dateNaissance')+' / '+
																		$(element).attr('cs(personne.sexe)');
																res +=' / '+pays+'</div>'; 
																$('#resultAutocompete').append(res);
															}
														</script>										
													</ct:widgetJSReturnFunction>
													</ct:widgetService> 
											</ct:widget>
							
											<!-- Workaround pour ne pas sumiter les forumlaire sur la séléction de l'autocomple -->
											<input type='text' name='test' style="display: none" />
											 
											<input type="hidden" id="nss" name="conjoint.membreFamille.personneEtendue.personneEtendue.numAvsActuel" value="<%=JadeStringUtil.toNotNullString(conjoint.getPersonneEtendue().getNumAvsActuel())%>"/>
											<input type="hidden" id="idTiers" name="idTiersConjoint" value="<%=JadeStringUtil.toNotNullString(conjoint.getTiers().getId())%>"/>
											<div id="resultAutocompete"><%=afficheConjoint%></div>
										</td>
										<td align="right">
											<a href="#" id="linkSupprimerConjoint"><%=btnDelLabel%></a>
										</td>
									</tr>
									<tr><td colspan="3">&nbsp;</td></tr>
									<tr><td colspan="3">
										<div class="btnAjax">
												<% if (bButtonUpdate) { %>
													<input class="btnAjaxDelete" type="button" value="<%=btnDelLabel%>">
													<input class="btnAjaxAdd" type="button" value="<%=btnNewLabel%>">
													<input class="btnAjaxValidate" type="button" value="<%=btnValLabel%>">
													<input class="btnAjaxValidateNouvellePeriode" type="button" value="<%=btnValLabel%>">
													<input class="btnAjaxCancel" type="button" value="<%=btnCanLabel%>">
													<input class="btnAjaxUpdate" type="button" value="<%=btnUpdLabel%>">
												<% } %>
											</div>
										</td>
									</tr>
								<tr><td colspan="3"><hr></td></tr>
								<tr><td colspan="3">&nbsp;</td></tr>
							</table>
							<div class="areaMembre">
								<div class="areaTitre"><ct:FWLabel key="JSP_PF_FAMILLE_D_ENFANT"/></div>
									<table class="areaDFDataTable" cellpadding="5">
										<thead>
											<tr>
												<th><ct:FWLabel key="JSP_PF_FAMILLE_D_ENFANT"/></th>
												<th><ct:FWLabel key="JSP_PF_FAMILLE_D_ENFANT_SOURCE"/></th>
												<th><ct:FWLabel key="JSP_PF_FAMILLE_D_ENFANT_GARDE"/></th>
												<th><ct:FWLabel key="JSP_PF_FAMILLE_D_ENFANT_FORMATION"/></th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<ct:forEach items="<%=viewBean.getListeEnfantFamille().iterator()%>" var="enfantFamille">
												<% EnfantFamille ef = (EnfantFamille)pageContext.getAttribute("enfantFamille"); %>
												<TR idEntity="<%=ef.getId()%>">
													<TD><%=PFUserHelper.getDetailAssure(objSession, ef.getEnfant().getMembreFamille().getPersonneEtendue())%></TD>
													<TD><%=objSession.getCodeLibelle(ef.getSimpleEnfantFamille().getCsSource())%></TD>
													<TD><%=objSession.getCodeLibelle(ef.getSimpleEnfantFamille().getCsGarde())%></TD>
													<TD><%=objSession.getCodeLibelle(ef.getSimpleEnfantFamille().getCsFormation())%></TD>
												</TR>
												</ct:forEach>
											</tr>
										</tbody>
									</table>
									<table class="areaDFDetail">
										<tr>
											<td><ct:FWLabel key="JSP_PF_FAMILLE_D_ENFANT"/></td> 
											<td>
												<ct:widget id='<%="widgetTiersEnfant"%>' name='<%="widgetTiersEnfant"%>' styleClass="widgetTiers" notation="data-g-string='mandatory:true'">
													<ct:widgetService methodName="find" className="<%=PersonneEtendueService.class.getName()%>">
														<ct:widgetCriteria criteria="forNumeroAvsActuel" label="JSP_PF_DOS_W_NSS"/>									
														<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
														<ct:widgetJSReturnFunction>
															<script type="text/javascript">
																function(element){
																	var idPays =null, pays=null,res=null;
																	$('#idTiersEnfant').val($(element).attr('tiers.id'));
																	$('#nssEnfant').val($(element).attr('personneEtendue.numAvsActuel'));;
																	this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
																	idPays = $(element).attr('tiers.idPays');
																	pays = $('[name=csNationaliteAffiche]').val(idPays).find(':selected').text();
																	$('#resultAutocompeteEnfant').children().remove();
																	res = '<div><br /><b>'+$(element).attr('personneEtendue.numAvsActuel')+'</b><br /> '+ 
																			$(element).attr('tiers.designation1')+' '+
																			$(element).attr('tiers.designation2')+' / '+
																			$(element).attr('personne.dateNaissance')+' / '+
																			$(element).attr('cs(personne.sexe)');
																	res +=' / '+pays+'</div>'; 
																	$('#resultAutocompeteEnfant').append(res);
																}
															</script>										
														</ct:widgetJSReturnFunction>
													</ct:widgetService> 
												</ct:widget>
												
												<input type="hidden" id="nssEnfant" name="nssEnfant" />
												<input type="hidden" id="idTiersEnfant" name="idTiersEnfant" />
																																			
											</td> 
											<td colspan="2">&nbsp;</td> 
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td colspan="3"><div id="resultAutocompeteEnfant"></div></td>
										</tr>
										<tr>
											<td colspan="4">&nbsp;</td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PF_FAMILLE_D_ENFANT_EST_IL"/></td> 
											<td>
												<ct:FWCodeSelectTag codeType="PFSOUREN" name="csSource" wantBlank="true" defaut=""/>
											</td> 
											<td colspan="2">&nbsp;</td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PF_FAMILLE_D_ENFANT_GARDE"/></td> 
											<td>
												<ct:FWCodeSelectTag codeType="<%=IPFConstantes.CSGROUP_TYPE_GARDE %>" name="csGarde" wantBlank="true" defaut="" notation="data-g-select=' '"/>
											</td> 
											<td><ct:FWLabel key="JSP_PF_FAMILLE_D_ENFANT_FORMATION"/></td> 
											<td>
												<ct:FWCodeSelectTag codeType="<%=IPFConstantes.CSGROUP_TYPE_ENFANT %>" name="csFormation" wantBlank="true" defaut=""/>
											</td>
										</tr>
									</table>
									<div class="btnAjax">
										<% if (bButtonUpdate) { %>
											<input class="btnAjaxDelete" type="button" value="<%=btnDelLabel%>">
											<input class="btnAjaxAdd" type="button" value="<%=btnNewLabel%>">
											<input class="btnAjaxValidate" type="button" value="<%=btnValLabel%>">
											<input class="btnAjaxValidateNouvellePeriode" type="button" value="<%=btnValLabel%>">
											<input class="btnAjaxCancel" type="button" value="<%=btnCanLabel%>">
											<input class="btnAjaxUpdate" type="button" value="<%=btnUpdLabel%>">
										<% } %>
									</div>
								</div>
							</td>
						</tr>
						
						<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
