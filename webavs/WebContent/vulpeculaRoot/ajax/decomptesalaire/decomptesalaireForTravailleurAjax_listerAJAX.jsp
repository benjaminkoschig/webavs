<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
<c:forEach var="salaire" items="${viewBean.salaires}">
	<fmt:formatNumber var="heures" value="${salaire.heures}" minFractionDigits="2" maxFractionDigits="2" />
	
	<tr idEntity="${salaire.id}">
	 	<td></td>
	 	<td>${salaire.decompte.id}</td>
	 	<td style="text-align: left;"><c:out value="${salaire.employeur.raisonSociale}" /></td>
		<td>${salaire.decompte.numeroDecompte}</td>
		<td><ct:FWCode csCode="${salaire.decompte.typeAsValue}"/></td>
		<td>${salaire.periode}</td>
		<td style="text-align: right;">${heures}</td>
		<td style="text-align: right;">${salaire.salaireHoraireAsValue}</td>
		<td style="text-align: right;">${salaire.salaireTotalAsValue}</td>
		<td style="text-align: right;">${salaire.tauxContribuableAffiche.value}</td>
		<td>
			<c:forEach var="typeAbsence" items="${salaire.typesAbsences}" varStatus="salaireStatus">
				<c:choose>
	 			<c:when test="${salaireStatus.index==0}">
	 				<ct:FWCode csCode="${typeAbsence.value}"/>
	 			</c:when>
			<c:otherwise>
	  			<c:out value="," />
	  			<ct:FWCode csCode="${typeAbsence.value}"/>
			</c:otherwise>			  			
				</c:choose>
			</c:forEach>
		</td>
	</tr>
</c:forEach>
</liste>