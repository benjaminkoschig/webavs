<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- chemin des images --%>
<c:set var="imageOk" value="ok.gif" />
<c:set var="imageErreur" value="erreur.gif" />

<liste>
	
	<c:forEach var="typeChambre" items="${viewBean.searchModel.searchResults}">
		<tr idEntity="${typeChambre.id}">
		
			<td class="mtd" width="">
				<ct:menuPopup menu="pegasus-optionstypeschambres" detailLabelId="MENU_OPTION_DETAIL">
					<ct:menuParam key="idHome" value="${typeChambre.simpleTypeChambre.idHome}"/>
					<ct:menuParam key="nomHome" value="${viewBean.nomHome}" />
					<ct:menuParam key="idTypeChambre" value="${typeChambre.simpleTypeChambre.idTypeChambre}"/>
					<ct:menuParam key="designationTypeChambre" value="${typeChambre.designationTypeChambre}"/>
				</ct:menuPopup>
			</td>
			
			<td class="mtd">
				<c:out value="${empty typeChambre.designationTypeChambre ? '&#160' : typeChambre.designationTypeChambre}" />
			</td>
			<td class="mtd">
				<c:choose>
				    <c:when test="${empty typeChambre.simpleTypeChambre.csCategorie}">
				       &#160
				    </c:when>
				    <c:otherwise>
				        <ct:FWCodeLibelle csCode="${typeChambre.simpleTypeChambre.csCategorie}"/>
				    </c:otherwise>
				</c:choose>
			</td>
			
			<c:set var="imgApiFacturee" value="${empty typeChambre.simpleTypeChambre.isApiFacturee ? '' : typeChambre.simpleTypeChambre.isApiFacturee ? imageOk : imageErreur }"/>
			<c:set var="imgParticularite" value="${empty typeChambre.simpleTypeChambre.isParticularite ? '' : typeChambre.simpleTypeChambre.isParticularite ? imageOk : imageErreur }"/>
			
			<td class="mtd" align="center"><img src="${pageContext.request.contextPath}/images/${imgApiFacturee}"></img></td>
			<td class="mtd" align="center"><img src="${pageContext.request.contextPath}/images/${imgParticularite}"></img></td>
			<c:if test="${viewBean.isLvpc}">
				<td class="mtd" align="right"><ct:FWCodeLibelle csCode="${typeChambre.simpleTypeChambre.csCategorieArgentPoche}"/></td>
			</c:if>
			
			<td class="mtd" align="right"><c:out value="${typeChambre.simpleTypeChambre.id}" /></td>
		</tr>
	</c:forEach>
	
</liste> 
<ct:serializeObject objectName="viewBean.typeChambre" destination="xml"/>