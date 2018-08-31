<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
	<c:forEach var="entity" items="${viewBean.searchModel.searchResults}">
		<c:set var="hasNote" value="${viewBean.hasNote(entity)}"></c:set>
		<c:choose>
			<c:when test="${hasNote}">
				<tr style="background-color: #FFFF66;" idEntity="${entity.id}">
			</c:when>
			<c:otherwise>
				<tr idEntity="${entity.id}">
			</c:otherwise>
		</c:choose>
			<td><a href="vulpecula?userAction=vulpecula.decomptedetail.decomptedetail.afficher&amp;selectedId=${entity.id}" title="<ct:FWLabel key='JSP_DETAIL_DECOMPTE'/>"><img style="margin-left:4px;" src="images/amal/view_detailed.png" /></a></td>
			<td><c:out value="${entity.id}" /></td>
			<td><c:out value="${entity.decompteSimpleModel.numeroDecompte}" /></td>
			<td><c:out value="${entity.employeurComplexModel.affiliationTiersComplexModel.affiliation.affilieNumero}" /></td>
			<td style="text-align: left;"><c:out value="${entity.employeurComplexModel.affiliationTiersComplexModel.affiliation.raisonSociale}" /></td>
			<td><c:out value="${entity.employeurComplexModel.administrationComplexModel.tiers.designation1}" /></td>
			<td><ct:FWCode csCode="${entity.decompteSimpleModel.type}"/></td>
			<td>${entity.decompteSimpleModel.periodeFormatte}</td>
			<c:choose>
				<c:when test="${entity.decompteSimpleModel.etat==entity.etatDecompte}">
					<td>TO - <ct:FWCodeLibelle csCode="${entity.taxationOfficeSimpleModel.etat}"/></td>
				</c:when>
				<c:otherwise>
					<td><ct:FWCodeLibelle csCode="${entity.decompteSimpleModel.etat}"/></td>
				</c:otherwise>
			</c:choose>
			<td>${entity.decompteSimpleModel.dateReception}</td>
			<td>${entity.decompteSimpleModel.dateRappel}</td>
			<td><ct:FWCode csCode="${entity.decompteSimpleModel.motifProlongation}"/></td>
		</tr>			
	</c:forEach>
</liste>