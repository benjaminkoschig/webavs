<%@ include file="/theme/list_ajax_el/header.jspf" %>
<c:forEach var="entity" items="${viewBean.list}" varStatus="status">
	<c:choose>
	 	<c:when test="${status.index%2==0}">
			<tr class="droit bmsRowOdd" idDossier="${entity.droitModel.idDossier}" idEntity="${entity.id}" onmouseover="jscss('swap',this,'bmsRowOdd','bmsRowHighlighted')" onmouseout="jscss('swap',this,'bmsRowHighlighted','bmsRowOdd')">
		</c:when>
		<c:otherwise>
			<tr class="droit bmsRowEven" idDossier="${entity.droitModel.idDossier}" idEntity="${entity.id}" onmouseover="jscss('swap',this,'bmsRowEven','bmsRowHighlighted')" onmouseout="jscss('swap',this,'bmsRowHighlighted','bmsRowEven')">
		</c:otherwise>
	</c:choose>
		<c:choose>
			<c:when test="${viewBean.isActif(entity)}">
				<td>
					<img src="images/active.png"  width="25" height="25" />
					<a class="linkDossier"><img style="margin-left:4px;" src="images/amal/view_detailed.png" /></a>
				</td>
			</c:when>
			<c:otherwise>
				<td>
					<img src="images/inactive.png"  width="25" height="25" />
					<a class="linkDossier"><img style="margin-left:4px;" src="images/amal/view_detailed.png" /></a>
				</td>
			</c:otherwise>
		</c:choose>
		<td style="text-align: left;"><c:out value="${entity.enfantComplexModel.personneEtendueComplexModel.tiers.designation1} ${entity.enfantComplexModel.personneEtendueComplexModel.tiers.designation2}" /></td>
		<td><c:out value="${entity.enfantComplexModel.paysModel.codeIso}" /></td>
		<td><c:out value="${entity.enfantComplexModel.personneEtendueComplexModel.personne.dateNaissance}" /></td>
		<td><c:out value="${entity.droitModel.debutDroit}" /></td>
		<td><c:out value="${entity.droitModel.finDroitForcee}" /></td>
		<td><ct:FWCode csCode="${entity.droitModel.motifFin}"/></td>
		<td><ct:FWCode csCode="${entity.droitModel.typeDroit}"/></td>
	</tr>	
</c:forEach>		
