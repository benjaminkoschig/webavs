<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
<c:forEach var="decompte" items="${viewBean.decomptes}" varStatus="status">
	<fmt:formatNumber var="montantTotalFormate" value="${decompte.masseSalarialeTotal.toStringValue()}" minFractionDigits="2" maxFractionDigits="2"></fmt:formatNumber>
	<tr idEntity="${decompte.id}">
		<td></td>
		<td>${decompte.id}</td>
		<td>${decompte.numeroDecompte.value}</td>
		<td style="text-align: left;"><ct:FWCodeLibelle csCode="${decompte.typeAsValue}" /></td>
		<td>${decompte.periode}</td>
		<td>${decompte.dateReceptionAsSwissValue}</td>
		<td>${decompte.dateComptabilisationAsSwissValue}</td>
		<td>${decompte.dateRectificationAsSwissValue}</td>
		<td style="text-align: right;">${montantTotalFormate}</td>
		<td><ct:FWCodeLibelle csCode="${decompte.etat.value}"/></td>
		<td>${decompte.dateRappelAsSwissValue}</td>
	</tr>  	
</c:forEach>
</liste>