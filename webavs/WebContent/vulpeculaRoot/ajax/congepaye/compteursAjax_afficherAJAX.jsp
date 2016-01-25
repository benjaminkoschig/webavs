<%@ include file="/theme/list_ajax_el/header.jspf" %>
<c:forEach var="ligneCompteur" items="${viewBean.lignes}" varStatus="status">
	
	<c:choose>
	 	<c:when test="${status.index%2==0}">
			<tr class="bmsRowOdd ligneCompteur" onmouseover="jscss('swap',this,'bmsRowOdd','bmsRowHighlighted')" onmouseout="jscss('swap',this,'bmsRowHighlighted','bmsRowOdd')">
		</c:when>
		<c:otherwise>
			<tr class="bmsRowEven ligneCompteur" onmouseover="jscss('swap',this,'bmsRowEven','bmsRowHighlighted')" onmouseout="jscss('swap',this,'bmsRowHighlighted','bmsRowEven')">
		</c:otherwise>
	</c:choose>
			<td colspan="4"></td>
			<td>${ligneCompteur.montant.value}</td>
			<td><a href="vulpecula?userAction=vulpecula.congepaye.congepaye.afficher&selectedId=${ligneCompteur.congePaye.id}"><ct:FWLabel key="JSP_CONGE_PAYE"/></a></td>
		</tr>
</c:forEach>
