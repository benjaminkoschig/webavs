<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
	<c:forEach var="entity" items="${viewBean.searchModel.searchResults}">
		<c:choose>
			<c:when test="${entity.traite}">
				<c:set var="color" value="#FFFFFF"/>
			</c:when>
			<c:otherwise>
				<c:set var="color" value="#F7BE81"/>
			</c:otherwise>
		</c:choose>	
		<tr idEntity="${entity.id}" style="background-color: ${color}" hasModification="${entity.modification=='true'}" isValidate="${entity.traite}">
<!-- 			<td> -->
<%-- 				<ct:menuPopup menu="vulpecula-optionstravailleurs"> --%>
<%-- 					<ct:menuParam key="selectedId" value="${entity.id}"/> --%>
<%-- 				</ct:menuPopup> --%>
<!-- 			</td>		 -->
			<td><c:out value="${entity.id}" /></td>
			<td style="text-align: left;"><c:out value="${entity.nom}" /></td>
			<td style="text-align: left;"><c:out value="${entity.prenom}" /></td>
			<td><c:out value="${entity.nss}" /></td>
			<td><c:out value="${entity.dateNaissance}" /></td>
			<td><c:out value="${entity.numeroEntreprise}" /></td>
			<td><c:out value="${entity.nomEntreprise}" /></td>
			<td class="modif">
				<c:choose>
    				<c:when test="${entity.modification=='true'}">modification travailleur</c:when>    
    				<c:otherwise>nouveau travailleur</c:otherwise>
				</c:choose>
			</td>
			
<%-- 			<td><c:out value="${entity.traite}" /></td> --%>
		</tr>			
	</c:forEach>
</liste>

