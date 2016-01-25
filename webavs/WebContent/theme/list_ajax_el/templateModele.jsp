<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
	<c:forEach var="entity" items="${viewBean.searchModel.searchResults}">
		<tr idEntity="${entity.id}">
			<td>
				<ct:menuPopup menu="vulpecula-optionstravailleurs">
					<ct:menuParam key="selectedId" value="${entity.id}"/>
				</ct:menuPopup>
			</td>
			<td><c:out value="${entity.id}" /></td>
		</tr>			
	</c:forEach>
</liste>

