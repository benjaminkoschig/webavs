<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored ="false" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>  
<c:set var="i" value="1" />
<c:set var="detailPrestLink" value="window.location.href='al?userAction=al.prestation.entetePrestation.afficher&selectedId=" />
<liste>
	<c:forEach var="prestation" items="${viewBean.list}">
		<c:set var="detailLink" value="${detailPrestLink}${prestation.id}'" />
		<tr class="row">
		      <td>
	    		<c:if test="${prestation.saisieOuProvisoire}">
	     			<ct:ifhasright element="al.prestation.entetePrestation.supprimerPrestation" crud="crud">
			        	<a class="deleteLink" title="<ct:FWLabel key="LINK_DEL_PRESTATION_DESC"/>" 
			               href="${contextPath}${mainServletPath}?userAction=al.prestation.entetePrestation.supprimerPrestation&amp;id=${prestation.id}" />
					</ct:ifhasright>
		       </c:if>
		       <a class="goToDossierLink" style="width=34px;height=34px;" title="<ct:FWLabel key="LINK_GO_DOSSIER_DESC"/>" href="${contextPath}${mainServletPath}?userAction=al.dossier.dossierMain.afficher&amp;selectedId=${prestation.dossierModel.idDossier}" />
       		</td>
			<td onclick="<c:out value="${detailLink }"/>" style="text-align:left"><c:out value="${prestation.allocataireComplexModel.personneEtendueComplexModel.tiers.designation1}  ${prestation.allocataireComplexModel.personneEtendueComplexModel.tiers.designation2}"></c:out></td>
			<td onclick="<c:out value="${detailLink }"/>"  style="text-align:center">${prestation.allocataireComplexModel.personneEtendueComplexModel.personneEtendue.numAvsActuel}</td>
			<td onclick="<c:out value="${detailLink }"/>"  style="text-align:right">${prestation.entetePrestationModel.jourDebutMut}</td>
			<td onclick="<c:out value="${detailLink }"/>"  style="text-align:right">${prestation.entetePrestationModel.moisDebutMut}</td>
			<td onclick="<c:out value="${detailLink }"/>"  style="text-align:right">${prestation.entetePrestationModel.jourFinMut}</td>
			<td onclick="<c:out value="${detailLink }"/>"  style="text-align:right">${prestation.entetePrestationModel.moisFinMut}</td>
			<td onclick="<c:out value="${detailLink }"/>"  style="text-align:right">${prestation.entetePrestationModel.nombreUnite}</td>
			<td onclick="<c:out value="${detailLink }"/>"  style="text-align:left"><ct:FWCodeLibelle csCode="${prestation.entetePrestationModel.unite}"/></td>
			<td onclick="<c:out value="${detailLink }"/>"  style="text-align:center">${prestation.entetePrestationModel.nombreEnfants}</td>
			<td onclick="<c:out value="${detailLink }"/>"  style="text-align:right">${prestation.entetePrestationModel.tauxVersementFormatte}</td>
			<td onclick="<c:out value="${detailLink }"/>"  style="text-align:right">${prestation.entetePrestationModel.montantTotalFormatte}</td>
			<td onclick="<c:out value="${detailLink }"/>"  style="text-align:center">
			<c:choose>
			  <c:when test="${prestation.saisieOuProvisoire}">
			  	<img src="images/cadenas_ouvert.gif" 
			  		 alt="<ct:FWCodeLibelle csCode="${prestation.entetePrestationModel.etatPrestation}"/>"
			  	/>
			  </c:when>
			  <c:otherwise>
			  	<img src="images/cadenas.gif" 
			  		 alt="<ct:FWCodeLibelle csCode="${prestation.entetePrestationModel.etatPrestation}"/>"
			  	/>
			  </c:otherwise>
			</c:choose>
			</td>
			<!--<td>
				<c:choose>
				  <c:when test="${prestation.saisieOuProvisoire}">
						<input type="radio" tabindex="${i}" name="idDossierSelected" value="${prestation.dossierModel.id}"/>
				  </c:when>
				  <c:otherwise>
				  </c:otherwise>
				</c:choose>
			</td>-->
		</tr>
	</c:forEach>
</liste>