<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
<c:forEach var="passage" items="${viewBean.passages}" varStatus="status">
		<tr class="prestationDetailContent" idEntity="${passage.id}">
			<td style="width: 30px;" data-idPassage="${passage.id}"><img class="prestation" alt="" src="images/icon-expand.gif" /></td>
	  		<td><c:out value="${passage.id} - ${passage.libelle}" /></td>
	  		<td><ct:FWCodeLibelle csCode="${passage.status}"/></td>
		</tr>
</c:forEach>
</liste>