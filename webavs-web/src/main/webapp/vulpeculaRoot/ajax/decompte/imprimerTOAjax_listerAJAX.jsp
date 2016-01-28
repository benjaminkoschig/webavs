<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
	<c:forEach var="taxation" items="${viewBean.list}">
		<tr class="taxationOffice">
			<td class="id">${taxation.id}</td>
			<td>${taxation.employeurAffilieNumero}</td>
			<td>${taxation.idPassageFacturation}</td>
			<td><ct:FWCodeLibelle csCode="${taxation.etat.value}"/></td>
			<td><input type="checkbox" class="toPrint" /></td>
		</tr>
	</c:forEach>
</liste>