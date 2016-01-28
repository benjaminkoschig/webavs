<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/xml;charset=ISO-8859-1" %>
<%@ page isELIgnored ="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<message>
	<contenu>
		<c:if test="${not empty viewBean.idOrdreVersement}">
			<input	type="hidden" 
					id="idOrdreVersement" 
					name="idOrdreVersement" 
					value="${viewBean.idOrdreVersement}" />
		</c:if>
		<c:if test="${not empty viewBean.idCompensationInterDecision}">
			<input	type="hidden" 
					id="idCompensationInterDecision" 
					name="idCompensationInterDecision" 
					value="${viewBean.idCompensationInterDecision}" />
		</c:if>
		<input	type="hidden" 
				id="soldeDecisionDeficitaire" 
				value="${viewBean.soldeDecisionDeficitaire}" />
		<input	type="hidden" 
				id="idDecisionDeficitaire" 
				value="${viewBean.idDecisionDeficitaire}" />
		<input	type="hidden" 
				id="provenance" 
				value="CID" />
		<table width="100%">
			<tbody>
				<tr>
					<td width="15%">
						&#160;
					</td>
					<td width="25%">
						&#160;
					</td>
					<td width="15%">
						&#160;
					</td>
					<td width="25%">
						&#160;
					</td>
					<td width="20%">
						&#160;
					</td>
				</tr>
				<tr class="forceTRHeight">
					<c:choose>
						<c:when test="${not empty viewBean.idOrdreVersement}">
							<td class="forceAlignRight">
								<ct:FWLabel key="JSP_ORDRE_VERSEMENT_DECISION_PONCTIONNEE" />&#160;
							</td>
							<td class="forceAlignLeft">
								<strong>
									${viewBean.decisionPonctionnee.beneficiairePrincipal.nom} ${viewBean.decisionPonctionnee.beneficiairePrincipal.prenom} (N° décision : ${viewBean.decisionPonctionnee.id})
								</strong>
							</td>
						</c:when>
						<c:otherwise>
							<td class="forceAlignRight">
								<ct:FWLabel key="JSP_ORDRE_VERSEMENT_DECISION_SUR_LAQUELLE_COMPENSER" />&#160;
							</td>
							<td class="forceAlignRight">
								<select id="choixDecision" name="idDecisionPonctionne">
									<option value=""></option>
									<c:forEach items="${viewBean.decisionsPourCompensationInterDecision}" var="uneDecisionPourCID" varStatus="statusLigne">
										<option montantDisponible="${uneDecisionPourCID.solde}" value="${uneDecisionPourCID.id}">
											${uneDecisionPourCID.beneficiairePrincipal.nom} ${uneDecisionPourCID.beneficiairePrincipal.prenom} : ${uneDecisionPourCID.solde} (N° décision : ${uneDecisionPourCID.id})
										</option>
									</c:forEach>
								</select>
							</td>
						</c:otherwise>
					</c:choose>
				</tr>
				<tr class="forceTRHeight">
					<td class="forceAlignRight">
						<ct:FWLabel key="JSP_ORDRE_VERSEMENT_MONTANT_DISPONIBLE" />&#160;
					</td>
					<td class="forceAlignRight alignWithInput">
						<strong id="montantDisponible">
							${viewBean.montantDisponible}
						</strong>
					</td>
					<td colspan="3">
					</td>
				</tr>
				<tr class="forceTRHeight">
					<td class="forceAlignRight">
						<ct:FWLabel key="JSP_ORDRE_VERSEMENT_MONTANT_COMPENSE" />&#160;
					</td>
					<td class="forceAlignRight">
						<input	type="text" 
								id="montantCompense" 
								name="montantCompense" 
								data-g-amount="mandatory:true" 
								value="${viewBean.montantCompense}" />
					</td>
					<td colspan="3">
					</td>
				</tr>
				<tr>
					<td colspan="3">
					</td>
					<td colspan="2" class="forceAlignRight">
						<span class="bouton btnAjaxValidate">
							<ct:FWLabel key="JSP_ORDRE_VERSEMENT_VALIDER" />
						</span>
						<c:if test="${not empty viewBean.idCompensationInterDecision}">
							<span class="bouton btnAjaxDelete">
								<ct:FWLabel key="JSP_ORDRE_VERSEMENT_SUPPRIMER" />
							</span>
						</c:if>
						<span class="bouton btnAjaxCancel">
							<ct:FWLabel key="JSP_ORDRE_VERSEMENT_ANNULER" />
						</span>
					</td>
				</tr>
			</tbody>
		</table>
	</contenu>
	<c:if test="${not empty viewBean.message}">
		<errorJson>
			${viewBean.message}
		</errorJson>
		<error>
			${viewBean.message}
		</error>
	</c:if>
</message>
