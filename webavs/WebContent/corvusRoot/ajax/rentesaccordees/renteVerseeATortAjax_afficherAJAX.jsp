<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/xml;charset=ISO-8859-1" %>
<%@ page isELIgnored ="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<message>
	<contenu>
		<div>
			<form>
				<c:if test="${not empty viewBean.idRenteVerseeATort}">
					<input	type="hidden" 
							id="idRenteVerseeATort" 
							name="idRenteVerseeATort" 
							value="${viewBean.idRenteVerseeATort}" />
				</c:if>
				<c:if test="${not empty viewBean.idOrdreVersement}">
					<input	type="hidden" 
							id="idOrdreVersement" 
							name="idOrdreVersement" 
							value="${viewBean.idOrdreVersement}" />
				</c:if>
				<c:if test="${not empty viewBean.idRenteSelectionnee}">
					<input	type="hidden" 
							id="idPrestationSelectionnee" 
							value="${viewBean.idRenteSelectionnee}" />
				</c:if>
				<c:if test="${viewBean.suppressionDesDecisionsFaite}">
					<input	type="hidden" 
							id="suppressionDesDecisionsFaite" 
							value="${viewBean.suppressionDesDecisionsFaite}" />
				</c:if>
				<input	type="hidden" 
						id="idDemandeRente" 
						name="idDemandeRente" 
						value="${viewBean.idDemandeRente}" />
				<input	type="hidden" 
						id="idTiers" 
						name="idTiers" 
						value="${viewBean.idTiers}" />
				<table width="100%">
					<tbody>
						<tr class="trLigneDetail">
							<td width="30%">
								<strong>
									<u>
										<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_DETAIL" />
									</u>
								</strong>
							</td>
							<td width="25%">
							</td>
							<td width="25%">
							</td>
							<td width="20%">
							</td>
						</tr>
						<c:if test="${not empty viewBean.idTiers}">
							<tr class="trLigneDetail">
								<td>
									<strong>
										<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_ASSURE" />&#160;:&#160;
									</strong>
								</td>
								<td colspan="3">
									<span id="detailRequerant">
										<strong>${viewBean.nss} <c:if test="${not empty viewBean.dateDeces}">( <span style="font-family:wingdings">U</span>${viewBean.dateDeces} ) </c:if></strong>/ ${viewBean.nom} ${viewBean.prenom} / ${viewBean.dateNaissance} / ${viewBean.sexe} / ${viewBean.nationalite}
									</span>
								</td>
							</tr>
						</c:if>
						<tr class="trLigneDetail">
							<td>
								<strong>
									<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_TYPE" />&#160;:&#160;
								</strong>
							</td>
							<td colspan="3">
								<select id="csTypeRenteVerseeATort" name="csTypeRenteVerseeATort" <c:if test="${not viewBean.modificationPossible}">disabled="disabled"</c:if>>
									<c:forEach items="${viewBean.mapCodesSystemeTypeRenteVerseeATort}" var="unCodeSysteme">
										<option value="${unCodeSysteme.key}"<c:if test="${unCodeSysteme.key == viewBean.csTypeRenteVerseeATort}"> selected="selected"</c:if>>
											${unCodeSysteme.value}
										</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr class="trLigneDetail necessairePourSaisieManuelle">
							<td>
								<strong>
									<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_DESCRIPTION" />&#160;:&#160;
								</strong>
							</td>
							<td>
								<input	type="hidden" 
										id="codeSystemeTypeSaisieManuelle" 
										value="${viewBean.codeSystemeSaisieManuelle}" />
								<textarea	id="description" 
											name="description" 
											<c:if test="${not viewBean.modificationPossible}">disabled="disabled"</c:if> 
											cols="31"
											rows="3"><c:out value="${viewBean.descriptionSaisieManuelle}" /></textarea>
								<input	type="hidden" 
										id="descriptionPourEnvoiAjax" 
										value="<c:out value="${viewBean.descriptionSaisieManuelle}" />" />
							</td>
						</tr>
						<c:choose>
							<c:when test="${empty viewBean.lignesDetail}">
								<input	type="hidden" 
										id="isSaisieManuelle" 
										value="true" />
								<tr class="trLigneDetail">
									<td>
										<strong>
											<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_PRESTATION_ACCORDEE" />&#160;:&#160;
										</strong>
									</td>
									<td>
										<select id="idRenteSelectionnee" <c:if test="${not viewBean.modificationPossible || not empty viewBean.idRenteSelectionnee}">disabled="disabled"</c:if>>
											<c:forEach items="${viewBean.rentesNouveauDroit}" var="uneRenteAccordee">
												<c:set var="beneficiaire" value="${uneRenteAccordee.beneficiaire}"/>
												<option value="${uneRenteAccordee.id}" idTiers="${beneficiaire.id}"<c:if test="${(empty viewBean.idRenteSelectionnee && uneRenteAccordee.enCours && uneRenteAccordee.rentePrincipale)}"> selected="selected"</c:if>>
													${beneficiaire.nom} ${beneficiaire.prenom} / ${uneRenteAccordee.codePrestation.codePrestation} / ${uneRenteAccordee.moisDebut} - ${uneRenteAccordee.moisFin} / ${uneRenteAccordee.montant}
												</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr class="trLigneDetail">
									<td>
										<strong>
											<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_MONTANT_VOULU" />&#160;:&#160;
										</strong>
									</td>
									<td>
										<input	type="text" 
												id="montant" 
												name="montant" 
												data-g-amount="mandatory:true" 
												<c:if test="${not viewBean.modificationPossible}">disabled="disabled"</c:if>
												value="${viewBean.montant}" />
									</td>
								</tr>
							</c:when>
							<c:otherwise>
								<input	type="hidden" 
										id="isSaisieManuelle" 
										value="false" />
								<tr class="trLigneDetail">
									<td>
										<strong>
											<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_DETAIL_PRESTATIONS_DUES" />&#160;:&#160;
										</strong>
									</td>
									<td colspan="3">
										<table width="100%">
											<thead>
												<tr>
													<th width="33%" class="alignCenter">
														<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_PERIODE_VERSEE_A_TORT" />
													</th>
													<th width="33%" class="alignCenter">
														<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_MONTANT_MENSUEL" />
													</th>
													<th width="33%" class="alignCenter">
														<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_MONTANT_POUR_LA_PERIODE" />
													</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${viewBean.lignesDetail}" var="uneLigne" varStatus="statusLigne">
													<tr class="<c:out value="${statusLigne.count % 2 ne 0 ? 'nonOdd': 'odd'}"/>">
														<td class="alignCenter">
															${uneLigne.dateDebut} - ${uneLigne.dateFin}
														</td>
														<td class="alignRight">
															${uneLigne.montantMensuel}&#160;<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_CHF" />
														</td>
														<td class="alignRight alignWithInput">
															${uneLigne.nombreMoisDansPeriode} x ${uneLigne.montantMensuel} = <strong>${uneLigne.montantTotalPourLaPeriodeFormatte}</strong>
														</td>
													</tr>
												</c:forEach>
												<tr height="2px">
													<td colspan="2">
													</td>
													<td>
														<hr />
													</td>
												</tr>
												<tr>
													<td>
													</td>
													<td class="alignRight">
														<strong>
															<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_TOTAL" />&#160;:&#160;
														</strong>
													</td>
													<td class="alignRight alignWithInput">
														<strong>
															${viewBean.montantTotalVerseeATort}
														</strong>
													</td>
												</tr>
												<tr>
													<td colspan="3">
													</td>
												</tr>
											</tbody>
										</table>
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
						<tr class="trLigneDetail">
							<td colspan="4">
								<span class="floatRight bouton btnAjaxCancel">
									<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_ANNULER" />
								</span>
								<c:if test="${viewBean.modificationPossible}">
									<c:if test="${not empty viewBean.idRenteVerseeATort && empty viewBean.idOrdreVersement}">
										<ct:ifhasright element="corvus.rentesaccordees.renteVerseeATort.modifierAjax" crud="u">
											<span class="floatRight bouton btnAjaxDelete">
												<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_SUPPRIMER" />
											</span>
										</ct:ifhasright>
									</c:if>
									<ct:ifhasright element="corvus.rentesaccordees.renteVerseeATort.modifierAjax" crud="u">
										<span class="floatRight bouton btnAjaxValidate">
											<ct:FWLabel key="JSP_RENTE_VERSEE_A_TORT_VALIDER" />
										</span>
									</ct:ifhasright>
								</c:if>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
			<div class="lastModification">
				${viewBean.spies}
			</div>
		</div>
	</contenu>
	<c:if test="${not empty viewBean.message}">
		<error>
			${viewBean.message}
		</error>
	</c:if>
</message>
