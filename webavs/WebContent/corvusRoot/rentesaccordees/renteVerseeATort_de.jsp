<%@ include file="/theme/detail_el/header.jspf" %>

<%-- ******************************************************************* Directives de page ****************************************************************** --%>

<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PRE0035" />
<c:set var="labelTitreEcran" value="JSP_RENTE_VERSEE_A_TORT_TITRE" />

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page" />
<c:set var="bButtonUpdate" value="false" scope="page" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/ajax/templateZoneAjax.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/corvusRoot/rentesaccordees/renteVerseeATort_de.css" />

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<c:url var="corvusPath" value="${requestScope.mainServletPath}" />
<script type="text/javascript">
	globazGlobal.actionAjaxDetail = "corvus.rentesaccordees.renteVerseeATortAjax";
	globazGlobal.corvusPath = "${corvusPath}";
</script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/ajaxUtils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/AbstractSimpleAJAXDetailZone.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/AbstractScalableAJAXTableZone.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultDetailAjax.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/corvusRoot/script/rentesaccordees/renteVerseeATort_de.js"></script>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options" />
<c:choose>
	<c:when test="${viewBean.afficherMenuRenteAccordee}">
		<ct:menuChange displayId="options" menuId="corvus-optionsrentesaccordees" showTab="options">
			<ct:menuSetAllParams key="selectedId" value="${viewBean.idRenteAccordee}" />

			<ct:menuSetAllParams key="csEtatDecision" value="${viewBean.csEtatDecision}" />
			<ct:menuSetAllParams key="csEtatRenteAccordee" value="${viewBean.csEtatRenteAccordee}" />
			<ct:menuSetAllParams key="csTypeBasesCalcul" value="${viewBean.csTypeBasesCalcul}" />
			<ct:menuSetAllParams key="dateFinDroit" value="${viewBean.dateFinDroit}" />
			<ct:menuSetAllParams key="idBaseCalcul" value="${viewBean.idBaseCalcul}" />
			<ct:menuSetAllParams key="idDecision" value="${viewBean.idDecision}" />
			<ct:menuSetAllParams key="idLot" checkAdd="no" value="${viewBean.idLot}" />
			<ct:menuSetAllParams key="idPrestation" value="${viewBean.idPrestation}" />
			<ct:menuSetAllParams key="idRenteAccordee" value="${viewBean.idRenteAccordee}" />
			<ct:menuSetAllParams key="idRenteCalculee" value="${viewBean.idRenteCalculee}" />
			<ct:menuSetAllParams key="idTierBeneficiaire" value="${viewBean.idTierBeneficiaire}" />
			<ct:menuSetAllParams key="idTierRequerant" value="${viewBean.idTierRequerant}" />
			<ct:menuSetAllParams key="idTiersBeneficiaire" value="${viewBean.idTiersBeneficiaire}" />
			<ct:menuSetAllParams key="idTiersVueGlobale" value="${viewBean.idTiersVueGlobale}" />
			<ct:menuSetAllParams key="isPreparationDecisionValide" value="${viewBean.isPreparationDecisionValide}" />
			<ct:menuSetAllParams key="montantPrestation" value="${viewBean.montantPrestation}" />
			<ct:menuSetAllParams key="montantRenteAccordee" value="${viewBean.montantRenteAccordee}" />
			<ct:menuSetAllParams key="noDemandeRente" value="${viewBean.idDemandeRente}" />
			<ct:menuSetAllParams key="provenance" checkAdd="no" value="2" />

			<c:choose>
				<c:when test="${viewBean.diminutionRenteAccordeeAutorisee}">
					<ct:menuActivateNode active="yes" nodeId="optdiminution" />
				</c:when>
				<c:otherwise>
					<ct:menuActivateNode active="no" nodeId="optdiminution" />
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${viewBean.preparationDecisionValide}">
					<ct:menuActivateNode active="yes" nodeId="preparerDecisionRA" />
				</c:when>
				<c:otherwise>
					<ct:menuActivateNode active="no" nodeId="preparerDecisionRA" />
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${viewBean.afficherPointMenuAnnoncePonctuelle}">
					<ct:menuActivateNode active="yes" nodeId="annoncePonctuelle" />
				</c:when>
				<c:otherwise>
					<ct:menuActivateNode active="no" nodeId="annoncePonctuelle" />
				</c:otherwise>
			</c:choose>

			<ct:menuActivateNode active="yes" nodeId="optfamille" />

		</ct:menuChange>
	</c:when>
	<c:otherwise>
		<ct:menuChange displayId="options" menuId="corvus-optionsdecisions" showTab="options">
			<ct:menuSetAllParams key="selectedId" checkAdd="no" value="${viewBean.idDecision}" />

			<ct:menuSetAllParams key="csEtatDecision" value="${viewBean.csEtatDecision}" />
			<ct:menuSetAllParams key="csEtatRenteAccordee" value="${viewBean.csEtatRenteAccordee}" />
			<ct:menuSetAllParams key="csTypeBasesCalcul" value="${viewBean.csTypeBasesCalcul}" />
			<ct:menuSetAllParams key="dateFinDroit" value="${viewBean.dateFinDroit}" />
			<ct:menuSetAllParams key="idBaseCalcul" value="${viewBean.idBaseCalcul}" />
			<ct:menuSetAllParams key="idDecision" value="${viewBean.idDecision}" />
			<ct:menuSetAllParams key="idLot" checkAdd="no" value="${viewBean.idLot}" />
			<ct:menuSetAllParams key="idPrestation" value="${viewBean.idPrestation}" />
			<ct:menuSetAllParams key="idRenteAccordee" value="${viewBean.idRenteAccordee}" />
			<ct:menuSetAllParams key="idRenteCalculee" value="${viewBean.idRenteCalculee}" />
			<ct:menuSetAllParams key="idTierBeneficiaire" value="${viewBean.idTierBeneficiaire}" />
			<ct:menuSetAllParams key="idTierRequerant" value="${viewBean.idTierRequerant}" />
			<ct:menuSetAllParams key="idTiersBeneficiaire" value="${viewBean.idTiersBeneficiaire}" />
			<ct:menuSetAllParams key="idTiersVueGlobale" value="${viewBean.idTiersVueGlobale}" />
			<ct:menuSetAllParams key="isPreparationDecisionValide" value="${viewBean.isPreparationDecisionValide}" />
			<ct:menuSetAllParams key="montantPrestation" value="${viewBean.montantPrestation}" />
			<ct:menuSetAllParams key="montantRenteAccordee" value="${viewBean.montantRenteAccordee}" />
			<ct:menuSetAllParams key="noDemandeRente" value="${viewBean.idDemandeRente}" />
			<ct:menuSetAllParams key="provenance" checkAdd="no" value="2" />

			<c:if test="!${viewBean.decisionEnAttente}">
				<ct:menuActivateNode active="no" nodeId="imprimerDecision" />
			</c:if>

			<c:choose>
				<c:when test="${viewBean.idLotValide}">
					<ct:menuActivateNode active="no" nodeId="afficherLot" />
				</c:when>
				<c:otherwise>
					<ct:menuActivateNode active="yes" nodeId="afficherLot" />
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${viewBean.validationDecisionAuthorisee}">
					<ct:menuActivateNode active="yes" nodeId="validerdecision" />
				</c:when>
				<c:otherwise>
					<ct:menuActivateNode active="no" nodeId="validerdecision" />
				</c:otherwise>
			</c:choose>

		</ct:menuChange>
	</c:otherwise>
</c:choose>

<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
	<div class="area">
		<table width="98%">
			<tbody>
				<tr>
					<td colspan="2">
						<span>
							<strong>
								<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_NO_DEMANDE" />&nbsp;:
							</strong>
						</span>
						<span>
							${viewBean.idDemandeRente}
							<input	type="hidden" 
									id="idDemandeRente" 
									value="${viewBean.idDemandeRente}" />
							<input	type="hidden" 
									id="modificationPossible" 
									value="${viewBean.modificationPossible}" />
							<input	type="hidden" 
									id="supprimerLaDecisionSiModification" 
									value="${viewBean.supprimerLaDecisionSiModification}" />
							<c:if test="${viewBean.supprimerLaDecisionSiModification}">
								<input	type="hidden" 
										id="messageAvertissementSuppressionDecision" 
										value="<ct:FWLabel key="AVERTISSEMENT_SUPPRESSION_DECISION_SI_MODIFICATION" />" />
							</c:if>
						</span>
					</td>
					<td>
						<c:if test="${viewBean.modificationPossible}">
							<ct:ifhasright element="corvus.rentesaccordees.renteVerseeATortAjax.creerNouvelleEntiteeAJAX" crud="u">
								<span id="boutonAjouter" class="floatRight">
									<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_AJOUTER" />
								</span>
							</ct:ifhasright>
						</c:if>
					</td>
				</tr>
				<tr>
					<c:choose>
						<c:when test="${not empty viewBean.message}">
							<td colspan="3">
								<div data-g-boxmessage="type:ERROR">
									<strong>
										${viewBean.message}
									</strong>
								</div>
							</td>
						</c:when>
						<c:when test="${empty viewBean.apercues}">
							<td colspan="3">
								<div data-g-boxmessage="type:WARN">
									<strong>
										<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_PAS_DE_RENTE_VERSEE_A_TORT" />
									</strong>
								</div>
							</td>
						</c:when>
						<c:otherwise>
							<c:forEach items="${viewBean.apercues}" var="unApercu" varStatus="statusApercu">
								<td width="49%" class="top-buffer">
									<span class="floatLeft">
										<strong>
											<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_ASSURE" />&nbsp;:&nbsp;
										</strong>
									</span>
									<span class="floatLeft">
										<strong>
											${unApercu.nss} <c:if test="${not empty unApercu.dateDeces}">( <span style="font-family:wingdings">U</span>${unApercu.dateDeces} ) </c:if>
										</strong>
										<br/>${unApercu.nom} ${unApercu.prenom} / ${unApercu.dateNaissance} / ${unApercu.sexe} / ${unApercu.nationalite}
									</span>
									<table width="100%" class="areaTable">
										<thead>
											<tr>
												<th width="20%">
													<strong>
														<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_CODE_PRESTATION" />
													</strong>
												</th>
												<th width="40%">
													<strong>
														<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_PERIODE" />
													</strong>
												</th>
												<th width="40%">
													<strong>
														<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_MONTANT" />
													</strong>
												</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${unApercu.lignesApercu}" var="uneLigne" varStatus="statusLigne">
												<tr class="renteVerseeATort <c:out value="${statusLigne.count % 2 ne 0 ? 'nonOdd': 'odd'}" />" idEntity="${uneLigne.idRenteVerseeATort}-${viewBean.modificationPossible}">
													<td>
														${uneLigne.codePrestation}
													</td>
													<td>
														<c:choose>
															<c:when test="${uneLigne.estUneSaisieManuelle}">
																<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_SAISIE_MANUELLE" />
															</c:when>
															<c:otherwise>
																${uneLigne.dateDebut} - ${uneLigne.dateFin}
															</c:otherwise>
														</c:choose>
													</td>
													<td class="alignCenterCurrency">
														${uneLigne.montantFormatte}&nbsp;<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_CHF" />
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</td>
								<td width="1%">
									&nbsp;
								</td>
								<c:if test="${(statusApercu.count % 2) eq 0 && statusApercu.count > 0}">
									</tr>
									<tr>
								</c:if>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</tr>
				<tr>
					<td class="top-buffer">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<div class="areaDetail" id="detailRenteVerseeATort">
						</div>
					</td>
				</tr>
			</tbody>
		</table>
		<c:if test="${not viewBean.modificationPossible}">
			<div data-g-boxmessage="type:WARN">
				<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_MODIFICATION_IMPOSSIBLE" />
			</div>
		</c:if>
	</div>
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>