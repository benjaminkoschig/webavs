<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/xml;charset=ISO-8859-1" %>
<%@ page isELIgnored ="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<message>
	<contenu>
		<input	type="hidden" 
				id="idOrdreVersement" 
				name="idOrdreVersement" 
				value="${viewBean.idOrdreVersement}" />
		<input	type="hidden" 
				id="provenance" 
				value="OV" />
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
					<td class="forceAlignRight">
						<ct:FWLabel key="JSP_ORDRE_VERSEMENT_DESIGNATION" />&#160;
					</td>
					<td colspan="2">
						<strong>
							${viewBean.detailOrdreVersement}
						</strong>
					</td>
					<td class="forceAlignRight">
						<label for="isCompense">
							<ct:FWLabel key="JSP_ORDRE_VERSEMENT_COMPENSE" />&#160;
						</label>
						<input	type="checkbox" 
								id="isCompense" 
								name="isCompense" 
								<c:if test="${viewBean.compense}">checked="checked"</c:if> />
					</td>
				</tr>
				<tr class="forceTRHeight">
					<td class="forceAlignRight">
						<ct:FWLabel key="JSP_ORDRE_VERSEMENT_MONTANT_DETTE" />&#160;
					</td>
					<td class="forceAlignRight alignWithInput">
						<strong id="spanMontantDette">
							${viewBean.montantDette}
						</strong>
						<input	type="hidden" 
								id="montantDette" 
								value="${viewBean.montantDette}" />
					</td>
					<td colspan="3">
					</td>
				</tr>
				<tr>
					<td class="forceAlignRight">
						<label for="montantCompense">
							<ct:FWLabel key="JSP_ORDRE_VERSEMENT_MONTANT_COMPENSE" />&#160;
						</label>
					</td>
					<td class="forceAlignRight">
						<input	type="text" 
								id="montantCompense" 
								name="montantCompense" 
								value="${viewBean.montantCompense}" 
								data-g-amount="mandatory:true" />
					</td>
					<td colspan="2">
					</td>
				</tr>
				<tr>
					<td colspan="3">
					</td>
					<td colspan="2" class="forceAlignRight">
						<ct:ifhasright element="corvus.ordresversements.ordresVersementsAjax.modifierAjax" crud="u">
							<span class="bouton btnAjaxValidate">
								<ct:FWLabel key="JSP_ORDRE_VERSEMENT_VALIDER" />
							</span>
						</ct:ifhasright>
						<span class="bouton btnAjaxCancel">
							<ct:FWLabel key="JSP_ORDRE_VERSEMENT_ANNULER" />
						</span>
					</td>
				</tr>
			</tbody>
		</table>
	</contenu>
	<c:if test="${not empty viewBean.message}">
		<error>
			${viewBean.message}
		</error>
	</c:if>
</message>
