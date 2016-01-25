<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
	<c:forEach var="entity" items="${viewBean.searchModel.searchResults}" varStatus="status">
		<tr idEntity="${entity.id}" >
			<td><ct:FWCodeLibelle csCode="${entity.canton}"/></td>
			<td><ct:FWCodeLibelle csCode="${entity.typeImposition}"/></td>
			<td>${entity.taux}</td>
			<td>${entity.periodeDebut}</td>
			<td>${entity.periodeFin}</td>
		</tr>	
	</c:forEach>		
</liste>
	