<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
<c:forEach var="entity" items="${viewBean.searchModel.searchResults}">
		<tr idEntity="${entity.id}|${entity.referenceRubriqueSimpleModel.idCodeReference}">
			<td>${entity.rubriqueSimpleModel.idExterne}</td>
			<td>${entity.codeSystemLibelleSimpleModel.libelle}</td>
		</tr>
</c:forEach>
</liste>