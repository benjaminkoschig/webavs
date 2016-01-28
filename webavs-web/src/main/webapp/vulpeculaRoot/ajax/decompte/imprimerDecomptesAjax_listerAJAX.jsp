<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
	<c:forEach var="decompte" items="${viewBean.list}">
		<tr class="decompte">
			<td class="id">${decompte.id}</td>
			<td>${decompte.numeroDecompte}</td>
			<td>${decompte.employeurAffilieNumero}</td>
			<td><ct:FWCodeLibelle csCode="${decompte.type.value}"/></td>
			<c:choose>
				<c:when test="${decompte.idPassageFacturation==0}">
					<td>-</td>
				</c:when>
				<c:otherwise>
					<td>${decompte.idPassageFacturation}</td>
				</c:otherwise>
			</c:choose>
			<td><ct:FWCodeLibelle csCode="${decompte.etat.value}"/></td>
			<c:choose>
				<c:when test="${decompte.special}">
					<td><button type="button" class="toPrint toPrintSpecial"><ct:FWLabel key="JSP_IMPRIMER"/></button></td>
				</c:when>
				<c:otherwise>
					<td><button type="button" class="toPrint toPrintStandard"><ct:FWLabel key="JSP_IMPRIMER"/></button></td>
				</c:otherwise>
			</c:choose>
		</tr>
	</c:forEach>
</liste>