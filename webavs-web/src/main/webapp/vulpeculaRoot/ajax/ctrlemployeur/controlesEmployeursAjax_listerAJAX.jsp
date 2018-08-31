<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
<c:forEach var="ce" items="${viewBean.list}">
		<c:choose>
			<c:when test="${viewBean.hasNote(ce)}">
				<tr idEntity="${ce.id}" style="height: 24px;background-color:#FFFF66" >	
			</c:when>
			<c:otherwise>
				<tr idEntity="${ce.id}" style="height: 24px;">
			</c:otherwise>
		</c:choose>
	  		<td></td>
	  		<td>${ce.id}</td>
	  		<td>${ce.numeroMeroba}</td>
	  		<td>${ce.dateControleAsSwissValue}</td>
	  		<td>${ce.dateAuAsSwissValue}</td>
	  		<td style="text-align: right;"><fmt:formatNumber minFractionDigits="2" type="number" value="${ce.montantAsValue}" /></td>
	  		<td><ct:FWCodeLibelle csCode="${ce.typeAsValue}"/></td>
	  		<c:choose>
	  			<c:when test="${ce.autresMesures}">
	  				<td>X</td>
	  			</c:when>
	  			<c:otherwise>
	  				<td></td>
	  			</c:otherwise>
	  		</c:choose>
	  		<td>${ce.idUtilisateur}</td>
		</tr>
</c:forEach>
</liste>