<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1301"/>
<c:set var="labelTitreEcran" value="JSP_COMPTEURS"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<c:set var="travailleur" value="${viewBean.travailleur}" />
<c:set var="employeur" value="${viewBean.employeur}" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/utilsFormatter.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/amount.js"></script>
<script type="text/javascript" src="${rootPath}/congepaye/compteurs_de.js"></script>
<script type="text/javascript">
globazGlobal.ID_POSTE_TRAVAIL = '${viewBean.idPosteTravail}';
</script>

<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div class="content">
	<div class="blocLeft">
		<div class="bloc blocMedium">
			<%@ include file="/vulpeculaRoot/blocs/travailleur.jspf" %>
		</div>
	</div>
	<div class="blocRight">
		<%@ include file="/vulpeculaRoot/blocs/employeur.jspf" %>
	</div>
	<div style="margin-top: 24px;">
		<table>
			<thead>
				<th></th>
				<th><ct:FWLabel key="JSP_ANNEE"/></th>
				<th><ct:FWLabel key="JSP_CUMUL_COTISATIONS"/></th>
				<th><ct:FWLabel key="JSP_MONTANT_RESTANT"/></th>
				<th><ct:FWLabel key="JSP_MONTANT_VERSE"/></th>
				<th><ct:FWLabel key="JSP_CONGE_PAYE"/></th>
			</thead>
			<tbody>
				<c:forEach var="compteur" items="${viewBean.compteurs}">
					<tr class="compteur" data-compteurId="${compteur.id}">
						<td><img class="compteurSearch" alt="" src="images/icon-expand.gif"></td>
						<td>${compteur.anneeAsValue}</td>
						<td>${compteur.cumulCotisation.toStringValue()}</td>
						<td>${compteur.montantRestant.toStringValue()}</td>
						<td></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>