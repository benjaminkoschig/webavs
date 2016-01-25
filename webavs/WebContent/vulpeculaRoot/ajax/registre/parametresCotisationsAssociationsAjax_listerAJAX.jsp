<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
	<c:forEach var="entity" items="${viewBean.searchModel.searchResults}">
	<tr idEntity="${entity.id}">
		<td>${entity.id}</td>
		<td>${entity.cotisationAssociationProfessionnelleSimpleModel.libelle}</td>
		<td><c:out value="${entity.administrationComplexModel.tiers.designation1} - ${entity.administrationComplexModel.tiers.designation2}"></c:out></td>
		<td><ct:FWCodeLibelle csCode="${entity.cotisationAssociationProfessionnelleSimpleModel.genre}"/></td>
		<td>${entity.parametreCotisationAssociationSimpleModel.taux}</td>
		<td>${entity.parametreCotisationAssociationSimpleModel.fourchetteDebut}</td>
		<td>${entity.parametreCotisationAssociationSimpleModel.fourchetteFin}</td>
	</tr>		
	</c:forEach>
</liste>
