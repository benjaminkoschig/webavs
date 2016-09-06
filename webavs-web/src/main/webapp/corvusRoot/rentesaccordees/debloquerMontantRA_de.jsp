<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiersManager"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="globaz.corvus.vb.process.REDebloquerMontantRAViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PRE3001";

	bButtonCancel = false;
	bButtonValidate = true;
	bButtonDelete =  false;
	bButtonUpdate = false;
	bButtonNew = false;

	REDebloquerMontantRAViewBean viewBean = (REDebloquerMontantRAViewBean) session.getAttribute("viewBean");
	userActionValue = IREActions.ACTION_DEBLOQUER_MONTANT_RENTE_ACCORDEE + ".executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu" />
	<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
	<script type="text/javascript" src="<%=servletContext%>/corvusRoot/ajax.js"></script>
	<script type="text/javascript" src="<%=servletContext%>/corvusRoot/script/rentesaccordees/debloquerMontantRA_de.js"></script>
	<script type="text/javascript">
		debloquerMontantRA.s_actionRenteAccordee = '<%=IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>';
	</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_DEB_D_TITRE" /><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>
								<table border="0" cellspacing="10" width="100%">
									<tr>
										<td>
											<label for="tiersBeneficiaireInfo">
												<b>
													<ct:FWLabel key="JSP_DEB_D_BENEFICIAIRE" />
												</b>
											</label>
										</td>
										<td colspan="5">
											<ct:inputText	name="tiersBeneficiaireInfo" 
															readonly="true" 
															styleClass="RElibelleExtraLongDisabled" 
															disabled="true" 
															tabindex="-1" />
										</td>
									</tr>
									<tr>
										<td>
											<label for="idRenteAccordee">
												<ct:FWLabel key="JSP_DEB_D_NO_RENTE_ACCODREE" />
											</label>
										</td>
										<td colspan="5">
											<input	type="text" 
													name="idRenteAccordee" 
													class="disabled" 
													readonly 
													tabindex="-2" 
													value="<%=viewBean.getIdRenteAccordee()%>" />
											<input	type="hidden" 
													name="forIdRenteAccordee" 
													value="<%=viewBean.getIdRenteAccordee()%>" />
											<input	type="hidden" 
													name="idTiersAdrPmt" 
													value="<%=viewBean.getIdTiersAdrPmt()%>" />
											<input	type="hidden" 
													name="idDomaineAdrPmt" 
													value="<%=viewBean.getIdDomaineAdrPmt()%>" />
										</td>
									</tr>
									<tr>
										<td>
											<label for="eMailAdress">
												<ct:FWLabel key="JSP_DEB_D_EMAIL" />
											</label>
										</td>
										<td colspan="5">
											<input	id="eMailAdress" 
													name="eMailAdress" 
													styleClass="libelleLong" 
													tabindex="1" 
													value="<%=JadeStringUtil.isEmpty(viewBean.getEMailAdress())?controller.getSession().getUserEMail():viewBean.getEMailAdress()%>" />
										</td>
									</tr>
									<tr>
										<td>
											<label for="montantBloqueAffiche">
												<ct:FWLabel key="JSP_DEB_D_MONTANT_RA_BLOQUE" />
											</label>
										</td>
										<td colspan="5">
											<input	name="montantBloqueAffiche" 
													readonly="true" 
													class="montant" 
													disabled="true" 
													tabindex="-3" 
													value="<%=viewBean.getMontantBloque() %>" />
										</td>
									</tr>
									<tr>
										<td>
											<label for="montantDebloqueAffiche">
												<ct:FWLabel key="JSP_DEB_D_MONTANT_RA_DEBLOQUE" />
											</label>
										</td>
										<td colspan="5">
											<input	name="montantDebloqueAffiche" 
													readonly="true" 
													class="montant" 
													disabled="true" 
													tabindex="-3" 
													value="<%=viewBean.getMontantDebloque() %>" />
										</td>
									</tr>
									<tr>
										<td colspan="6">
											<hr />
										</td>
									</tr>
									<tr>
										
										<td align="right">
											<label for="section">
												<ct:FWLabel key="JSP_DEB_D_SECTION" />
											</label>
										</td>
										<td colspan="2" align ="left">
											<ct:widget 	id='widgetNoFacture' 
														name='noFacture' 
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
													<ct:widgetCriteria 	criteria="forSoldeNegatif" 
																		fixedValue="true" 
																		label="..." />
													<ct:widgetLineFormatter format="<strong>#{idExterneRole}</strong> : #{descriptionCompteAnnexe} / #{role}<br /><strong>#{idExterne}</strong> : #{solde}CHF (#{typeSection})" />
													<ct:widgetJSReturnFunction>
														<script type="text/javascript">
															function(element) {
																debloquerMontantRA.$idCompteAnnexe.val($(element).attr('idCompteAnnexe'));
																debloquerMontantRA.$typeSection.val($(element).attr('idTypeSection'));
																debloquerMontantRA.$idSection.val($(element).attr('idSection'));
																debloquerMontantRA.$soldeSection.val($(element).attr('solde'));
																this.value = $(element).attr('idExterne') + ' (' + $(element).attr('solde') + 'CHF)';
																debloquerMontantRA.adapterMontantADebloquerSiBesoin();
																debloquerMontantRA.checkBoutonValider();
															}
														</script>
													</ct:widgetJSReturnFunction>
												</ct:widgetManager>
											</ct:widget>
											<input	type="hidden" 
													id="idCompteAnnexe" 
													name="idCompteAnnexe" />
											<input	type="hidden" 
													id="idSection" 
													name="idSection" />
											<input	type="hidden" 
													id="soldeSection" />
										</td>
										<td colspan="4">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td>
											<label for="montantADebloquer">
												<b>
													<ct:FWLabel key="JSP_DEB_D_MONTANT_RA_A_DEBLOQUE" />
												</b>
											</label>
										</td>
										<td>
											<input	name="montantADebloquer" 
													id="montantADebloquer" 
													data-g-amount="mandatory:true,blankAsZero:false" 
													value="<%=viewBean.getMontantADebloquer() %>" />
										</td colspan="4">
											&nbsp;
										<td/>
									</tr>
									<tr>
										<td colspan="6">
											<br />
										</td>
									</tr>
									<tr>
										<td colspan="3">
											&nbsp;
										</td>
										<td valign="top">
											<label for="addressePaiement">
												<ct:FWLabel key="JSP_DEB_D_ADR_PMT" />
											</label>
										</td>
										<td>
											<pre><span class="IJAfficheText"><%=viewBean.getCcpOuBanqueFormatte()%></pre>
											<pre><span class="IJAfficheText"><%=viewBean.getAdresseFormattee()%></span></pre>
										</td>
										<td valign="bottom">
											<ct:FWSelectorTag	name="selecteurAdresses"
																methods="<%=viewBean.getMethodesSelectionAdresse()%>"
																providerApplication="pyxis"
																providerPrefix="TI"
																providerAction="pyxis.adressepaiement.adressePaiement.chercher"
																target="fr_main"
																redirectUrl="<%=mainServletPath%>"/>
										</td>
									</tr>
									<tr>
										<td colspan="6">
											<br />
										</td>
									</tr>
									<tr>
										<td colspan="3">
											&nbsp;
										</td>
										<td align="right" valign="top">
											<label for="refPmt">
												<ct:FWLabel key="JSP_DEB_D_REF_PMT" />
											</label>
										</td>
										<td colspan="2">
											<textarea name="refPmt" cols="50" rows="2"><%=viewBean.getRefPmt()%></textarea>
										</td>
									</tr>
									<tr>
										<td colspan="6">
											<br />
										</td>
									</tr>
									<tr>
										<td>
											&nbsp;
										</td>
										<td colspan="5" align="right">
											<strong class="errorText">
												<span id="spanErreurSection">
													<ct:FWLabel key="JSP_DEB_D_ERREUR_SECTION_OBLIGATOIRE" />
												</span>
												<span id="spanErreurMontant">
													<ct:FWLabel key="JSP_DEB_D_ERREUR_MONTANT_INVALIDE" />
												</span>
											</strong>
										</td>
									</tr>
								</table>
							</tr>
						</td>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>