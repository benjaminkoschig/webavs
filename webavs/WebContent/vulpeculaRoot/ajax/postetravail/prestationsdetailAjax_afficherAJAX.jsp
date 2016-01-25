<%@ include file="/theme/list_ajax_el/header.jspf" %>
<table class="detailTable" style="width: 100%" cellspacing="0" cellpadding="0">
	<thead>
		<tr style="font-weight: bold;" class="bmsRowOdd">
			<td style="width: 5%"><ct:FWLabel key="JSP_TRAVAILLEUR"/></td>
			<td style="width: 5%"><ct:FWLabel key="JSP_EMPLOYEUR"/></td>
			<td style="width: 20%"><ct:FWLabel key="JSP_NOM_PRENOM"/></td>
			<td style="width: 10%"><ct:FWLabel key="JSP_MONTANT"/></td>
			<td style="width: 5%"><ct:FWLabel key="JSP_QUA"/></td>
			<td style="width: 5%"><ct:FWLabel key="JSP_GEN"/></td>
			<td style="width: 5%"><ct:FWLabel key="JSP_TYPE"/></td>
			<td style="width: 20%"><ct:FWLabel key="JSP_PERIODE"/></td>
		</tr>
	</thead>
	<tbody>
	<c:forEach var="prestation" items="${viewBean.prestations}" varStatus="status">
		<c:set var="userAction" value="" />
		<c:choose>
			<c:when test="${prestation['class'].simpleName == 'AbsenceJustifiee'}">
				<c:set var="userAction" value="vulpecula?userAction=vulpecula.absencesjustifiees.afficher&selectedId=${prestation.id}" />
			</c:when>
			<c:when test="${prestation['class'].simpleName == 'CongePaye'}">
				<c:set var="userAction" value="vulpecula?userAction=vulpecula.congepaye.afficher&selectedId=${prestation.id}" />
			</c:when>
			<c:when test="${prestation['class'].simpleName == 'ServiceMilitaire'}">
				<c:set var="userAction" value="vulpecula?userAction=vulpecula.servicemilitaire.afficher&selectedId=${prestation.id}" />
			</c:when>
		</c:choose>	
		<c:choose>
		 	<c:when test="${status.index%2==0}">
				<tr class="none" onclick="document.location='${userAction}'" onmouseover="jscss('swap',this,'none','bmsRowHighlighted')" onmouseout="jscss('swap',this,'bmsRowHighlighted','none')">
			</c:when>
			<c:otherwise>
				<tr class="bmsRowEven" onclick="document.location='${userAction}'" onmouseover="jscss('swap',this,'bmsRowEven','bmsRowHighlighted')" onmouseout="jscss('swap',this,'bmsRowHighlighted','bmsRowEven')">
			</c:otherwise>
		</c:choose>
				<td>${prestation.idTravailleur}</td>
				<td>${prestation.noAffilie}</td>
				<td>${prestation.nomPrenomTravailleur}</td>
				<td>${prestation.montant.value}</td>
				<td><ct:FWCode csCode="${prestation.qualification.value}" /></td>
				<td><ct:FWCode csCode="${prestation.typeSalaire.value}"/></td>
				<td><ct:FWCode csCode="${prestation.typePrestation}"/></td>
				<td>${prestation.periodeAsString}</td>
			</tr>
	</c:forEach>
</table>