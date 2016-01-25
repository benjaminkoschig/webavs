<%@ include file="/theme/detail_el/header.jspf" %>

<%-- ******************************************************************* Directives de page ****************************************************************** --%>

<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° écran et titre --%>
<c:set var="idEcran" value="PRE0034"/>
<c:set var="labelTitreEcran" value="JSP_ORDRE_VERSEMENT_TITRE"/>

<%-- visibilités des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/ajax/templateZoneAjax.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/corvusRoot/rentesaccordees/renteVerseeATort_de.css" />

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/ajaxUtils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/AbstractSimpleAJAXDetailZone.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/AbstractScalableAJAXTableZone.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/corvusRoot/script/ordresversements/ordresVersements_de.js"></script>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>

<style type="text/css">

	td.doubleLigne {
		border: none;
		border-left: 1px solid silver !important;
		border-right: 1px solid silver !important;
		border-top: 1px solid silver !important;
		border-bottom: 3px double black !important;
	}

	td.simpleLigne {
		border: none;
		border-left: 1px solid silver !important;
		border-right: 1px solid silver !important;
		border-top: 1px solid silver !important;
		border-bottom: 2px solid black !important;
		height: 3px;
	}

	.forceAlignLeft {
		text-align: left !important;
	}

	.forceAlignRight {
		text-align: right !important;
	}

	.beneficiairePrincipal {
		background-color: silver !important;
		border: 1px solid silver !important;
	}

	.ligneTotal {
		height: 3px;
	}

	.alignWithInput {
		padding-right: 13px;
	}

	img {
		vertical-align: middle;
	}
</style>

<c:url var="corvusPath" value="${requestScope.mainServletPath}" />
<script type="text/javascript">
	globazGlobal.corvusPath = '${corvusPath}';
</script>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionsdecisions" showTab="options" checkAdd="no">

	<ct:menuSetAllParams key="idDecision" value="${viewBean.idDecision}"/>
	<ct:menuSetAllParams key="idLot" value="${viewBean.idLot}"/>
	<ct:menuSetAllParams key="idPrestation" value="${viewBean.idPrestation}"/>
	<ct:menuSetAllParams key="idTierBeneficiaire" value="${viewBean.idTierBeneficiaire}"/>
	<ct:menuSetAllParams key="idTierRequerant" value="${viewBean.idTierRequerant}"/>
	<ct:menuSetAllParams key="idTiersVueGlobale" value="${viewBean.idTierRequerant}"/>
	<ct:menuSetAllParams key="montantPrestation" value="${viewBean.montantPrestation}"/>
	<ct:menuSetAllParams key="noDemandeRente" value="${viewBean.idDemandeRente}"/>
	<ct:menuSetAllParams key="selectedId" value="${viewBean.idDecision}"/>

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

<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>

	<table width="100%">
		<tbody>
			<tr>
				<td width="10%">
					<ct:FWLabel key="JSP_ORDRE_VERSEMENT_PRESTATION_NO" />
				</td>
				<td width="18%">
					<span id="noPrestation">
						<strong>
							${viewBean.idPrestation}
						</strong>
					</span>
					<input	type="hidden" 
							id="forIdPrestation" 
							value="${viewBean.idPrestation}" />
					<input	type="hidden" 
							id="idDecision" 
							value="${viewBean.idDecision}" />
				</td>
				<td width="18%">
					&nbsp;
				</td>
				<td width="18%">
					&nbsp;
				</td>
				<td width="18%">
					&nbsp;
				</td>
				<td width="18%">
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
					<ct:FWLabel key="JSP_ORDRE_VERSEMENT_PRESTATION_BENEFICIAIRE" />
				</td>
				<td colspan="5">
					${viewBean.detailTiers}
				</td>
			</tr>
			<tr>
				<td colspan="6">
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
				<td>
					<ct:FWLabel key="JSP_ORDRE_VERSEMENT_PRESTATIONS_DUES" />
				</td>
				<td>
					<ct:FWLabel key="JSP_ORDRE_VERSEMENT_PRESTATIONS_DEJA_VERSEES" />
				</td>
				<td>
					<ct:FWLabel key="JSP_ORDRE_VERSEMENT_INTERETS_MORATOIRES" />
				</td>
				<td>
					<ct:FWLabel key="JSP_ORDRE_VERSEMENT_CREANCIERS" />
				</td>
				<td>
					<ct:FWLabel key="JSP_ORDRE_VERSEMENT_SOLDE" />
				</td>
			</tr>
			<tr>
				<td>
					<ct:FWLabel key="JSP_ORDRE_VERSEMENT_MONTANT" />
				</td>
				<td>
					<span>
						<strong id="montantPrestationsDues">
						</strong>
					</span>
				</td>
				<td>
					<span>
						<strong id="montantPrestationsDejaVersees">
						</strong>
					</span>
				</td>
				<td>
					<span>
						<strong id="montantInteretsMoratoires">
						</strong>
					</span>
				</td>
				<td>
					<span>
						<strong id="montantCreanciers">
						</strong>
					</span>
				</td>
				<td>
					<span>
						<strong id="montantSolde">
						</strong>
					</span>
				</td>
			</tr>
			<tr>
				<td colspan="6">
					&nbsp;
				</td>
			</tr>
		</tbody>
	</table>
	<div class="area" id="zoneAjaxOrdreVersement">
		<table width="100%" class="areaTable">
			<thead>
				<tr>
					<th class="notSortable" width="45%">
						<ct:FWLabel key="JSP_ORDRE_VERSEMENT_DESIGNATION" />
					</th>
					<th class="notSortable" width="18%">
						<ct:FWLabel key="JSP_ORDRE_VERSEMENT_TYPE" />
					</th>
					<th class="notSortable" width="7%">
						<ct:FWLabel key="JSP_ORDRE_VERSEMENT_COMPENSE" />
					</th>
					<th class="notSortable" width="12%">
						<ct:FWLabel key="JSP_ORDRE_VERSEMENT_MONTANT_DETTE" />
					</th>
					<th class="notSortable" width="12%">
						<ct:FWLabel key="JSP_ORDRE_VERSEMENT_MONTANT" />
					</th>
					<th class="notSortable" width="5%">
						<ct:FWLabel key="JSP_ORDRE_VERSEMENT_NO" />
					</th>
				</tr>
			</thead>
			<tbody />
		</table>
		<div class="areaDetail" id="zoneEditionOrdreVersement" />
	</div>
	<br />
	<div id="banniereModificationInterdite">
		<div data-g-boxmessage="type:WARN">
			<ct:FWLabel key="JSP_ORDRE_VERSEMENT_MODIFICATION_IMPOSSIBLE" />
		</div>
	</div>
	<div id="banniereModificationRestitutionInterdite" style="width: 100%;">
		<div data-g-boxmessage="type:WARN">
			<ct:FWLabel key="JSP_ORDRE_VERSEMENT_MODIFICATION_RESTITUTION_INTERDITE" />
		</div>
	</div>

<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>