<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
	<c:forEach var="entity" items="${viewBean.searchModel.searchResults}">
		<tr idEntity="${entity.cotisationAssociationProfessionnelleSimpleModel.id}">
			<td><c:out value="${entity.cotisationAssociationProfessionnelleSimpleModel.id}" /></td>
			<td>
				<c:out value="${entity.cotisationAssociationProfessionnelleSimpleModel.libelle}"/>
			</td>
			<td>
				<ct:FWCodeLibelle csCode="${entity.cotisationAssociationProfessionnelleSimpleModel.genre}"/>
			</td>
			<td>
				<c:out value="${entity.administrationComplexModel.admin.codeAdministration} - ${entity.administrationComplexModel.tiers.designation1} ${entity.administrationComplexModel.tiers.designation2}" /> 
			</td>
		</tr>			
	</c:forEach>
</liste>