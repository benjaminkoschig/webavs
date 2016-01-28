<%@ include file="/theme/list_ajax_el/header.jspf" %>
<table id="decomptessalairesTable" style="width: 100%" cellspacing="0" cellpadding="0">
	<thead>
		<tr>
			<th><ct:FWLabel key="JSP_ID_DECOMPTE"/>
			<th><ct:FWLabel key="JSP_DECOMPTE" /></th>
			<th><ct:FWLabel key="JSP_TYPE" /></th>
			<th><ct:FWLabel key="JSP_PERIODE_SALAIRE" /></th>
			<th><ct:FWLabel key="JSP_HEURES" /></th>
			<th><ct:FWLabel key="JSP_SALAIRE_HORAIRE" /></th>
			<th><ct:FWLabel key="JSP_SALAIRE" /></th>
			<th><ct:FWLabel key="JSP_TAUX" /></th>
			<th><ct:FWLabel key="JSP_ABSENCES" /></th>
		</tr>
	</thead>
	<tbody id="decomptessalairesContent">
		<liste>
		<c:forEach var="salaire" items="${viewBean.historiqueSalaire}">
			<fmt:formatNumber var="heures" value="${salaire.heures}" minFractionDigits="2" maxFractionDigits="2" />
			
			<tr idEntity="${salaire.id}">
			 	<td>${salaire.decompte.id}</td>
				<td>${salaire.decompte.numeroDecompte}</td>
				<td><ct:FWCode csCode="${salaire.decompte.typeAsValue}"/></td>
				<td>${salaire.periode}</td>
				<td style="text-align: right;">${heures}</td>
				<td style="text-align: right;">${salaire.salaireHoraireAsValue}</td>
				<td style="text-align: right;">${salaire.salaireTotalAsValue}</td>
				<td style="text-align: right;">${salaire.tauxContribuableForCaissesSociales.value}</td>
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
	</tbody>
</table>
<script>
	$( "#decomptessalairesContent tr:even" ).addClass("bmsRowEven");
	$( "#decomptessalairesContent tr:odd" ).addClass("bmsRowOdd");
</script>