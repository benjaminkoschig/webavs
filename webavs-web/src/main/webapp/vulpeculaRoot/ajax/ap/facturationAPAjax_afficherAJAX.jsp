<%@ include file="/theme/list_ajax_el/header.jspf" %>
<c:forEach var="facture" items="${viewBean.factures.factures}" varStatus="status">
	<tr class="facture" style="cursor:hand" data-id="${facture.id}">
		<td><img src="images/icon-expand.gif" class="expandFA"/></td>
		<td>
			<c:if test="${facture.etat != 'GENERE' and facture.etat != 'COMPTABILISE' and facture.etat != 'SUPPRIME' and facture.etat != 'REFUSE'}">
				<img src="images/delete_icon.gif" title="Supprimer" data-idFacture="${facture.id}" class="deleteFA"/>
			</c:if>
			<c:if test="${facture.etat != 'GENERE' and facture.etat != 'COMPTABILISE' and facture.etat != 'SUPPRIME' and facture.etat != 'REFUSE'}">
				<img src="images/btnX.gif" title="Refuser" data-idFacture="${facture.id}" class="refuseFA"/>
			</c:if>
		</td>
		<td><c:out value="${facture.associationParent.designation1} ${facture.associationParent.designation2}" /></td>
		<td>${facture.annee.value}</td>
		<td style="text-align: right;"><fmt:formatNumber value="${facture.montantFacture.value}" minFractionDigits="2" /></td>
		<td>${facture.idPassageFacturation}</td>
		<td>${facture.dateFacturation.swissValue}</td>
		<td style="cursor:hand" data-idModele="${facture.modele.id}" class="modeleEntete">${facture.modele.libelle}</td>
		<td>${facture.etat}</td>
		<td><button class="printFactureAP" data-idFacture="${facture.id}"><ct:FWLabel key="JSP_BOUTON_PRINT" /></button></td>
	</tr>  
	<tr style="display:none;">
		<td></td>
		<td></td>
		<td colspan="6" style="text-align:left;background-color:white">
			<table style="width:100%">
			<c:forEach var="association" items="${facture.associations}" varStatus="status">
				<tr>
					<td style="text-align: left;font-weight: bold;width:60%">${association.libelle}</td>
				</tr>
				<c:forEach var="cotisation" items="${association.cotisations}">
					<tr>
						<td style="text-align: left;font-style: italic;">${cotisation.libelle}</td>
					</tr>
					<c:forEach var="ligne" items="${cotisation.lignes}">
					<c:if test="${not ligne.ligneFacteur && not ligne.ligneRabais}">
						<tr>
							<td></td>
							<td style="text-align: left;"><ct:FWCodeLibelle csCode="${ligne.typeParametre.value}"/></td>
							<td style="text-align: right;"><fmt:formatNumber value="${ligne.massePourCotisation.value}" minFractionDigits="2" /></td>
							<td style="text-align: right;">${ligne.tauxCotisation.value}</td>
							<td style="text-align: right;"><fmt:formatNumber value="${ligne.montantCotisation.value}" minFractionDigits="2" /></td>
						</tr>
					</c:if>
					</c:forEach>
					<c:forEach var="ligne" items="${cotisation.lignes}">
					<c:if test="${ligne.ligneFacteur}">
						<tr>
							<td></td>
							<td style="text-align: left;"><ct:FWCodeLibelle csCode="${ligne.typeParametre.value}"/></td>
							<td style="text-align: right;"><fmt:formatNumber value="${ligne.massePourCotisation.value}" minFractionDigits="2" /></td>
							<td style="text-align: right;">${ligne.facteur}</td>
							<td style="text-align: right;"><fmt:formatNumber value="${ligne.montantCotisation.value}" minFractionDigits="2" /></td>
						</tr>
					</c:if>
					<c:if test="${ligne.ligneRabais}">
						<tr>
							<td></td>
							<td style="text-align: left;"><ct:FWCodeLibelle csCode="${ligne.typeParametre.value}"/></td>
							<td style="text-align: right;"><fmt:formatNumber value="${ligne.massePourCotisation.value}" minFractionDigits="2" /></td>
							<td style="text-align: right;">${ligne.tauxCotisation.value}</td>
							<td style="text-align: right;"><fmt:formatNumber value="${ligne.montantCotisation.value}" minFractionDigits="2" /></td>
						</tr>
					</c:if>
					</c:forEach>
				<c:if test="${not cotisation.uneLigne}">
				<tr>
					<td colspan="4"></td>
					<td style="text-align: right;border-top:1px solid black;"><fmt:formatNumber value="${cotisation.montantTotal.value}" minFractionDigits="2" /></td>
				</tr>
				</c:if>
				</c:forEach>
				<c:if test="${association.reduction}">
					<tr>
						<td style="text-align: left;">${viewBean.labelReductionAssociation}&nbsp;${association.rabaisAssociation.value}%</td>
						<td></td>
						<td style="text-align: right;"><fmt:formatNumber value="${association.montantCotisations.value}" minFractionDigits="2" /></td>
						<td></td>
						<td style="text-align: right;"><fmt:formatNumber value="${association.montantRabais.value}" minFractionDigits="2" /></td>
					</tr>
				</c:if>
				<tr>
					<td style="text-align: left;">Total ${association.libelle}</td>
					<td colspan="3"></td>
					<td style="text-align: right;border-top:2px solid black;font-weight: bold;"><fmt:formatNumber value="${association.montantAssociation.value}" minFractionDigits="2" /></td>
				</tr>
				<tr>
				</tr>
			</c:forEach>
				<tr>
					<td style="text-align: left;font-weight: bold;"><ct:FWLabel key="JSP_TOTAL_FACTURE"/></td>
					<td colspan="3"></td>
					<td style="text-align: right;color:red;text-decoration: underline;border-top:3px solid black;font-weight: bold;"><fmt:formatNumber value="${facture.montantFacture.value}" minFractionDigits="2" /></td>
				</tr>
				
			</table>
		</td>
	</tr>
</c:forEach>
