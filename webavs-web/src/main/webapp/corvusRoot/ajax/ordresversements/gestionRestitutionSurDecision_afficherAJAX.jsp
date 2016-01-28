<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/xml;charset=ISO-8859-1" %>
<%@ page isELIgnored ="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<message>
	<contenu>
		<input	type="hidden" 
				id="provenance" 
				value="Restitution" />
		<input	type="hidden" 
				id="idSoldePourRestitution" 
				value="${viewBean.idSoldePourRestitution}" />
		<input	type="hidden" 
				id="idDecision" 
				value="${viewBean.idDecision}" />
		<input	type="hidden" 
				id="montantARecouvrer" 
				value="${viewBean.montantARecouvrer}" />
		<input	type="hidden" 
				id="montantPrestationAccordee" 
				value="${viewBean.montantPrestationAccordeePrincipale}" />
		<input	type="hidden" 
				id="codeSystemeRetenueMensuelle" 
				value="${viewBean.codeSystemeRetenueMensuelle}" />
		<input	type="hidden" 
				id="codeSystemeRestitution" 
				value="${viewBean.codeSystemeRestitution}" />
		<table width="100%">
			<tbody>
				<tr>
					<td width="25%">
						&#160;
					</td>
					<td width="25%">
						&#160;
					</td>
					<td width="50%">
						&#160;
					</td>
				</tr>
				<tr class="forceTRHeight">
					<td colspan="3">
						<strong>
							<ct:FWLabel key="JSP_ORDRE_VERSEMENT_GESTION_RESTITION_TITRE" />
						</strong>
					</td>
				</tr>
				<tr>
					<td colspan="3">
						&#160;
					</td>
				</tr>
				<tr class="forceTRHeight">
					<td class="forceAlignRight">
						<ct:FWLabel key="JSP_ORDRE_VERSEMENT_MECANISME_RECOUVREMENT" />&#160;
					</td>
					<td colspan="2">
						<select id="csTypeSoldePourRestitution" name="csTypeSoldePourRestitution">
							<c:forEach items="${viewBean.mapCodesSystemeTypeSoldePourRestitution}" var="unCodeSysteme">
								<option value="${unCodeSysteme.key}"<c:if test="${unCodeSysteme.key == viewBean.csTypeSoldePourRestitution}"> selected="selected"</c:if>>
									${unCodeSysteme.value}
								</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr class="forceTRHeight">
					<td class="forceAlignRight">
						<ct:FWLabel key="JSP_ORDRE_VERSEMENT_MONTANT_A_RECOUVRER" />&#160;
					</td>
					<td class="forceAlignRight">
						<strong id="spanMontantARecouvrer" class="alignWithInput">
						</strong>
					</td>
				</tr>
				<tr class="forceTRHeight retenueMensuelle">
					<td class="forceAlignRight">
						<ct:FWLabel key="JSP_ORDRE_VERSEMENT_MONTANT_RETENU_MENSUELLEMENT" />&#160;
					</td>
					<td class="forceAlignRight">
						<input	type="text" 
								id="montantRetenueMensuelle" 
								name="montantRetenueMensuelle" 
								data-g-amount="mandatory:true" 
								value="${viewBean.montantRetenueMensuelle}" />
					</td>
					<td>
						<ct:FWLabel key="JSP_ORDRE_VERSEMENT_TEXTE_MONTANT_PRESTATION_ACCORDEE_PARTIE_1" />
						${viewBean.beneficiairePrestationAccordeePrincipale}
						<ct:FWLabel key="JSP_ORDRE_VERSEMENT_TEXTE_MONTANT_PRESTATION_ACCORDEE_PARTIE_2" />
						<span id="spanMontantPrestationAccordee"></span>
						&#160;<ct:FWLabel key="JSP_ORDRE_VERSEMENT_TEXTE_MONTANT_PRESTATION_ACCORDEE_PARTIE_3" />
					</td>
				</tr>
				<tr class="forceTRHeight restitution">
					<td colspan="3">
						<div data-g-boxmessage="type:WARN">
							<strong>
								<ct:FWLabel key="JSP_ORDRE_VERSEMENT_TEXTE_POUR_LA_RESTITUTION" />
							</strong>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="3" class="forceAlignRight">
						<span class="bouton btnAjaxValidate">
							<ct:FWLabel key="JSP_ORDRE_VERSEMENT_VALIDER" />
						</span>
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
