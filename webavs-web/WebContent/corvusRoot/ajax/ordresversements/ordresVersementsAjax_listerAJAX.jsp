<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/xml;charset=ISO-8859-1" %>
<%@ page isELIgnored ="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<message>
	<liste>
		<c:choose>
			<c:when test="${viewBean.ordresVersementPresents}">
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
		<tr>
			<td class="forceAlignLeft">
				<c:out value="${viewBean.designationBeneficiairePrincipal}" />
			</td>
			<td class="forceAlignLeft">
				<c:out value="${viewBean.typeOrdreVersementBeneficiairePrincipal}" />
			</td>
			<td>
				&#160;
			</td>
			<td>
				&#160;
			</td>
			<td class="forceAlignRight">
				<c:out value="${viewBean.montantBeneficiairePrincipal}" />
			</td>
			<td>
				<c:out value="${viewBean.idOrdreVersementBeneficiairePrincipal}" />
			</td>
		</tr>
		<c:if test="${not empty viewBean.lignesInteretsMoratoires}">
			<tr>
				<td>
					&#160;
				</td>
				<td class="forceAlignLeft">
					<c:out value="${viewBean.typeOrdreVersementInteretMoratoire}" />
				</td>
				<td>
					&#160;
				</td>
				<td>
					&#160;
				</td>
				<td class="forceAlignRight">
					<c:out value="${viewBean.montantTotalInteretsMoratoires}" />
				</td>
				<td>
					&#160;
				</td>
			</tr>
		</c:if>
		<tr class="beneficiairePrincipal ligneTotal">
			<td class="beneficiairePrincipal" colspan="6">
			</td>
		</tr>
		<tr>
			<td class="forceAlignLeft beneficiairePrincipal">
				<strong>
					<ct:FWLabel key="JSP_ORDRE_VERSEMENT_TOTAL_POUR" />&#160;<c:out value="${viewBean.designationBeneficiairePrincipal}" />
				</strong>
			</td>
			<td colspan="3" class="beneficiairePrincipal">
				&#160;
			</td>
			<td class="forceAlignRight beneficiairePrincipal">
				<strong>
					<c:out value="${viewBean.montantTotalBeneficiairePrincipal}" />
				</strong>
			</td>
			<td class="beneficiairePrincipal">
				&#160;
			</td>
		</tr>
		<tr class="beneficiairePrincipal ligneTotal">
			<td class="beneficiairePrincipal" colspan="6">
			</td>
		</tr>
		<c:forEach items="${viewBean.lignesDettesEnCompta}" var="uneDetteEnCompta" varStatus="statusLigne">
			<c:choose>
				<c:when test="${viewBean.modificationPossible}">
					<tr idEntity="${uneDetteEnCompta.idOrdreVersement}-OV">
				</c:when>
				<c:otherwise>
					<tr>
				</c:otherwise>
			</c:choose>
				<td class="forceAlignLeft <c:out value="${viewBean.modificationPossible ? 'modifiable' : ''}" />">
					<c:out value="${uneDetteEnCompta.designation}" />
				</td>
				<td class="forceAlignLeft <c:out value="${viewBean.modificationPossible ? 'modifiable' : ''}" />">
					<c:out value="${uneDetteEnCompta.typeOrdreVersement}" />
				</td>
				<td class="<c:out value="${viewBean.modificationPossible ? 'modifiable' : ''}" />">
					<span>
						<c:choose>
							<c:when test="${uneDetteEnCompta.compense}">
								<img src="${pageContext.request.contextPath}/images/dialog-ok-apply.png" alt="oui" />
							</c:when>
							<c:otherwise>
								<img src="${pageContext.request.contextPath}/images/edit-delete.png" alt="non" />
							</c:otherwise>
						</c:choose>
					</span>
				</td>
				<td class="forceAlignRight <c:out value="${viewBean.modificationPossible ? 'modifiable' : ''}" />">
					<c:choose>
						<c:when test="${uneDetteEnCompta.montantDettePlusGrandQueZero}">
							-<c:out value="${uneDetteEnCompta.montantDette}" />
						</c:when>
						<c:otherwise>
							<c:out value="${uneDetteEnCompta.montantDette}" />
						</c:otherwise>
					</c:choose>
				</td>
				<td class="forceAlignRight <c:out value="${viewBean.modificationPossible ? 'modifiable' : ''}" />">
					<c:choose>
						<c:when test="${uneDetteEnCompta.montantPlusGrandQueZero}">
							-<c:out value="${uneDetteEnCompta.montant}" />
						</c:when>
						<c:otherwise>
							<c:out value="${uneDetteEnCompta.montant}" />
						</c:otherwise>
					</c:choose>
				</td>
				<td class="<c:out value="${viewBean.modificationPossible ? 'modifiable' : ''}" />">
					<c:out value="${uneDetteEnCompta.idOrdreVersement}" />
				</td>
			</tr>
		</c:forEach>
		<c:forEach items="${viewBean.lignesRentesVerseesATort}" var="uneRenteVerseeATort" varStatus="statusLigne">
			<tr>
				<td class="forceAlignLeft">
					<c:out value="${uneRenteVerseeATort.designation}" />
				</td>
				<td class="forceAlignLeft">
					<c:out value="${uneRenteVerseeATort.typeOrdreVersement}" />
				</td>
				<td class="<c:out value="${viewBean.modificationPossible ? 'modifiable' : ''}" />">
					<span class="<c:out value="${viewBean.modificationPossible ? 'modifiable' : ''}" />">
						<c:choose>
							<c:when test="${uneRenteVerseeATort.compense}">
								<img src="${pageContext.request.contextPath}/images/dialog-ok-apply.png" alt="oui" />
							</c:when>
							<c:otherwise>
								<img src="${pageContext.request.contextPath}/images/edit-delete.png" alt="non" />
							</c:otherwise>
						</c:choose>
					</span>
				</td>
				<td class="forceAlignRight">
					<c:choose>
						<c:when test="${uneRenteVerseeATort.montantDettePlusGrandQueZero}">
							-<c:out value="${uneRenteVerseeATort.montantDette}" />
						</c:when>
						<c:otherwise>
							<c:out value="${uneRenteVerseeATort.montantDette}" />
						</c:otherwise>
					</c:choose>
				</td>
				<td class="forceAlignRight">
					<c:choose>
						<c:when test="${uneRenteVerseeATort.montantPlusGrandQueZero}">
							-<c:out value="${uneRenteVerseeATort.montant}" />
						</c:when>
						<c:otherwise>
							<c:out value="${uneRenteVerseeATort.montant}" />
						</c:otherwise>
					</c:choose>
				</td>
				<td class="<c:out value="${viewBean.modificationPossible ? 'modifiable' : ''}" />">
					<c:out value="${uneRenteVerseeATort.idOrdreVersement}" />
				</td>
			</tr>
		</c:forEach>
		<c:forEach items="${viewBean.lignesCompensationsInterDecisionNegatives}" var="uneCID" varStatus="statusLigne">
			<tr>
				<td class="forceAlignLeft">
					<ct:FWLabel key="JSP_ORDRE_VERSEMENT_CID_COMPENSATION" />&#160;<c:out value="${uneCID.designation}" />
				</td>
				<td>
					&#160;
				</td>
				<td>
					&#160;
				</td>
				<td>
					&#160;
				</td>
				<td class="forceAlignRight">
					<c:choose>
						<c:when test="${uneCID.montantPlusGrandQueZero}">
							-<c:out value="${uneCID.montant}" />
						</c:when>
						<c:otherwise>
							<c:out value="${uneCID.montant}" />
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					<c:out value="${uneCID.idOrdreVersement}" />
				</td>
			</tr>
		</c:forEach>
		<c:if test="${(not empty viewBean.lignesDettesEnCompta) || (not empty viewBean.lignesRentesVerseesATort) || (not empty viewBean.lignesCompensationsInterDecisionNegatives)}">
			<tr class="beneficiairePrincipal ligneTotal">
				<td class="beneficiairePrincipal" colspan="6">
				</td>
			</tr>
			<tr class="beneficiairePrincipal">
				<td colspan="4" class="forceAlignLeft beneficiairePrincipal">
					&#160;
				</td>
				<td class="forceAlignRight beneficiairePrincipal">
					<strong>
						<c:out value="${viewBean.montantTotalDettesCompensees}" />
					</strong>
				</td>
				<td class="beneficiairePrincipal">
					&#160;
				</td>
			</tr>
			<tr class="beneficiairePrincipal ligneTotal">
				<td class="beneficiairePrincipal" colspan="4">
				</td>
				<td class="simpleLigne">
				</td>
				<td class="beneficiairePrincipal">
				</td>
			</tr>
			<tr class="beneficiairePrincipal">
				<td class="forceAlignLeft beneficiairePrincipal">
					<strong>
						<c:choose>
							<c:when test="${not empty viewBean.lignesCreanciers || (not empty viewBean.lignesCompensationsInterDecisionPositives) || (not empty viewBean.lignesSoldesPourRestitution)}">
								<ct:FWLabel key="JSP_ORDRE_VERSEMENT_SOUS_TOTAL" />
							</c:when>
							<c:otherwise>
								<ct:FWLabel key="JSP_ORDRE_VERSEMENT_TOTAL" />
							</c:otherwise>
						</c:choose>
					</strong>
				</td>
				<td colspan="3" class="beneficiairePrincipal">
					&#160;
				</td>
				<td class="forceAlignRight beneficiairePrincipal">
					<strong>
						<c:out value="${viewBean.montantSousTotal}" />
					</strong>
				</td>
				<td class="beneficiairePrincipal">
					&#160;
				</td>
			</tr>
			<tr class="beneficiairePrincipal ligneTotal">
				<td class="beneficiairePrincipal" colspan="6">
				</td>
			</tr>
		</c:if>
		<c:forEach items="${viewBean.lignesCreanciers}" var="unCreancier" varStatus="statusLigne">
			<tr>
				<td class="forceAlignLeft">
					<c:out value="${unCreancier.designation}" />
				</td>
				<td class="forceAlignLeft">
					<c:out value="${unCreancier.typeOrdreVersement}" />
				</td>
				<td>
					&#160;
				</td>
				<td>
					&#160;
				</td>
				<td class="forceAlignRight">
					<c:if test="${unCreancier.montantPlusGrandQueZero}">-</c:if>${unCreancier.montant}
				</td>
				<td>
					<c:out value="${unCreancier.idOrdreVersement}" />
				</td>
			</tr>
		</c:forEach>
		<c:forEach items="${viewBean.lignesCompensationsInterDecisionPositives}" var="uneCID" varStatus="statusLigne">
			<c:choose>
				<c:when test="${viewBean.modificationPossible}">
					<tr idEntity="${uneCID.idOrdreVersement}-CID">
				</c:when>
				<c:otherwise>
					<tr>
				</c:otherwise>
			</c:choose>
				<td class="forceAlignLeft <c:out value="${viewBean.modificationPossible ? 'modifiable' : ''}" />">
					<ct:FWLabel key="JSP_ORDRE_VERSEMENT_CID_PONCTION" />&#160;${uneCID.designation}
				</td>
				<td class="forceAlignLeft <c:out value="${viewBean.modificationPossible ? 'modifiable' : ''}" />">
					&#160;
				</td>
				<td class="<c:out value="${viewBean.modificationPossible ? 'modifiable' : ''}" />">
					&#160;
				</td>
				<td class="forceAlignRight <c:out value="${viewBean.modificationPossible ? 'modifiable' : ''}" />">
					&#160;
				</td>
				<td class="forceAlignRight <c:out value="${viewBean.modificationPossible ? 'modifiable' : ''}" />">
					<c:out value="${uneCID.montant}" />
				</td>
				<td class="<c:out value="${viewBean.modificationPossible ? 'modifiable' : ''}" />">
					&#160;
				</td>
			</tr>
		</c:forEach>
		<c:forEach items="${viewBean.lignesSoldesPourRestitution}" var="uneRestitution">
			<tr>
				<td class="forceAlignLeft">
					<ct:FWLabel key="JSP_ORDRE_VERSEMENT_RESTITUTION" />
				</td>
				<td class="forceAlignLeft">
					&#160;
				</td>
				<td class="forceAlignLeft">
					&#160;
				</td>
				<td class="forceAlignLeft">
					&#160;
				</td>
				<td class="forceAlignRight">
					<c:out value="${uneRestitution.montant}" />
				</td>
				<td class="forceAlignLeft">
					&#160;
				</td>
			</tr>
		</c:forEach>
		<c:if test="${(not empty viewBean.lignesCreanciers) || (not empty viewBean.lignesCompensationsInterDecisionPositives) || (not empty viewBean.lignesSoldesPourRestitution)}">
			<tr class="beneficiairePrincipal ligneTotal">
				<td class="beneficiairePrincipal" colspan="6">
				</td>
			</tr>
			<c:if test="${viewBean.plusieursEntreesPourCreancierCIDEtRestitution}">
				<tr class="beneficiairePrincipal">
					<td colspan="4" class="beneficiairePrincipal">
						&#160;
					</td>
					<td class="forceAlignRight beneficiairePrincipal">
						<strong>
							<c:out value="${viewBean.montantTotalCreanciersCIDEtRestitution}" />
						</strong>
					</td>
					<td class="beneficiairePrincipal">
						&#160;
					</td>
				</tr>
			</c:if>
			<tr class="beneficiairePrincipal ligneTotal">
				<td class="beneficiairePrincipal" colspan="4">
				</td>
				<td class="simpleLigne">
				</td>
				<td class="beneficiairePrincipal">
				</td>
			</tr>
			<tr class="beneficiairePrincipal">
				<td class="forceAlignLeft beneficiairePrincipal">
					<strong>
						<ct:FWLabel key="JSP_ORDRE_VERSEMENT_TOTAL" />
					</strong>
				</td>
				<td colspan="3" class="beneficiairePrincipal">
					&#160;
				</td>
				<td class="forceAlignRight beneficiairePrincipal">
					<strong>
						<c:out value="${viewBean.montantTotalSolde}" />
					</strong>
				</td>
				<td class="beneficiairePrincipal">
					&#160;
				</td>
			</tr>
			<tr class="beneficiairePrincipal ligneTotal">
				<td class="beneficiairePrincipal" colspan="6">
				</td>
			</tr>
		</c:if>
		<c:if test="${viewBean.retenueMensuelleEnCours}">
			<c:set var="retenueMensuelle" value="${viewBean.soldePourRestitutionBeneficiairePrincipal}" />
			<tr class="beneficiairePrincipal">
				<td class="beneficiairePrincipal forceALignRight" colspan="5">
					<c:choose>
						<c:when test="${viewBean.retenueMensuelleEgalMontantTotalRetenue}">
							<ct:FWLabel key="JSP_ORDRE_VERSEMENT_RETENUE_MENSUELLE_EGAL_MONTANT_TOTAL_RETENU" />
						</c:when>
						<c:otherwise>
							<ct:FWLabel key="JSP_ORDRE_VERSEMENT_RETENUE_MENSUELLE_PARTIE_1" />&#160;<c:out value="${retenueMensuelle.montantRetenueMensuelle}" />&#160;<ct:FWLabel key="JSP_ORDRE_VERSEMENT_RETENUE_MENSUELLE_PARTIE_2" />
						</c:otherwise>
					</c:choose>
				</td>
				<td class="beneficiairePrincipal">
					&#160;
				</td>
			</tr>
		</c:if>
		<tr class="beneficiairePrincipal">
			<td class="beneficiairePrincipal" colspan="6">
				&#160;
			</td>
		</tr>
		<c:if test="${viewBean.modificationPossible && (viewBean.compensationInterDecisionPossible || viewBean.retenueMensuellePossible)}">
			<tr class="beneficiairePrincipal ligneTotal">
				<td class="beneficiairePrincipal forceAlignRight" colspan="6">
					<c:if test="${viewBean.retenueMensuellePossible}">
						<ct:ifhasright element="corvus.ordresversements.ordresVersementsAjax.gererRestitutionAJAX" crud="u">
							<span id="boutonRestitution">
								<ct:FWLabel key="JSP_ORDRE_VERSEMENT_CREER_RESTITUTION" />
							</span>
						</ct:ifhasright>
					</c:if>
					<c:if test="${viewBean.compensationInterDecisionPossible}">
						<ct:ifhasright element="corvus.ordresversements.ordresVersementsAjax.creerCIDAJAX" crud="u">
							<span id="boutonCID">
								<ct:FWLabel key="JSP_ORDRE_VERSEMENT_CREER_CID" />
							</span>
						</ct:ifhasright>
					</c:if>
				</td>
			</tr>
			<tr class="beneficiairePrincipal ligneTotal">
				<td class="beneficiairePrincipal" colspan="6">
				</td>
			</tr>
		</c:if>
	</liste>
	<contenu>
		<montantPrestationsDues>
			${viewBean.montantBeneficiairePrincipal}
		</montantPrestationsDues>
		<montantPrestationsDejaVersees>
			${viewBean.montantTotalPrestationsDejaVersees}
		</montantPrestationsDejaVersees>
		<montantInteretsMoratoires>
			${viewBean.montantTotalInteretsMoratoires}
		</montantInteretsMoratoires>
		<montantCreanciersEtAutresDettes>
			${viewBean.montantTotalCreanciersEtAutresDettes}
		</montantCreanciersEtAutresDettes>
		<montantSolde>
			${viewBean.montantTotalSolde}
		</montantSolde>
		<modificationPossible>
			${viewBean.modificationPossible}
		</modificationPossible>
		<retenuePresenteMaisModificationInterdite>
			${viewBean.retenuePresenteMaisModificationInterdite}
		</retenuePresenteMaisModificationInterdite>
		<input	type="hidden" 
				id="montantTotalSolde" 
				value="${viewBean.montantTotalSolde}" />
	</contenu>
	<c:if test="${not empty viewBean.message}">
		<error>
			<c:out value="${viewBean.message}" />
		</error>
	</c:if>
</message>
