<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1"%>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/find_ajax_el/header.jspf"%>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1008" />
<c:set var="labelTitreEcran" value="JSP_COTISATIONS_ASSOCIATIONS" />
<c:set var="userActionNew" value="vulpecula.registre.detailParamCotiAP.afficher"/>

<%@ include file="/theme/find_ajax_el/javascripts.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjaxList.js"></script>
<script type="text/javascript" src="${rootPath}/registre/cotisationsAP_de.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/widget/globazwidget.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/widget.css" />

<script type="text/javascript">
	globazGlobal.ACTION_AJAX = '${userActionListerAjax}';
</script>

<%@ include file="/theme/find_ajax_el/bodyStart.jspf"%>
<ct:FWLabel key="${labelTitreEcran}" />
<%@ include file="/theme/find_ajax_el/bodyStart2.jspf"%>

<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<tr>
	<td>
		<div class="area">
			<div class="areaSearch">
			<table width="100%">
				<tr><td width ="20%"><ct:FWLabel key='JSP_LIBELLE'/><td><input type="text" id="searchModel.forLibelleUpperLike"  value="${viewBean.searchModel.forLibelleUpperLike}" ></input></td></tr>
				<tr><td width ="20%"><ct:FWLabel key='JSP_CODE'/><td><input type="text" id="searchModel.forCodeLike" value="${viewBean.searchModel.forCodeLike}" ></input></td></tr>
				<tr><td width ="20%"><ct:FWLabel key='JSP_ASSOCIATION_PROFESSIONNELLE'/><td><input type="text" id="searchModel.forAssociationLike" value="${viewBean.searchModel.forAssociationLike}" ></input></td></tr>
				<tr><td width ="20%"><ct:FWLabel key="JSP_A_FACTURER_DEFAUT"/><td><ct:FWCodeSelectTag notation="data-g-select=''" name="searchModel.forFacturerDefaut" codeType="PTCATCOTAP" defaut="${viewBean.searchModel.forFacturerDefaut}" wantBlank="true" tabindex="8"/></td></tr>
				
				<%-- <tr>
					<td width ="20%">
						<ct:FWLabel key='JSP_CODE'/>
					</td>
					<td>
						<select id="idCode">
								<option value="" />
							<c:forEach var="code" items="${viewBean.codes}">
								<c:choose>
									<c:when test="${code.id==viewBean.searchModel.forCodeLike}">
										<option value="${code.id}" selected="selected">${code.designation}</option>
									</c:when>
									<c:otherwise>
										<option value="${code.id}">${code.designation}</option>
									</c:otherwise>								
								</c:choose>
							</c:forEach>
						</select>
					</td>
				</tr> --%>
			</table>				
			</div>
			<%@ include file="/theme/find_ajax_el/searchNewButtons.jspf" %>
			<div>
				<table class="areaTable" width="100%">
					<thead>
						<th></th>
						<th data-orderKey="libelleUpper"><ct:FWLabel key="JSP_LIBELLE" /></th>
						<th data-orderKey="genre"><ct:FWLabel key="JSP_GENRE" /></th>
						<th data-orderKey="codeAdministration"><ct:FWLabel key="JSP_ASSOCIATION_PROFESSIONNELLE" /></th>
						<th><ct:FWLabel key="JSP_A_FACTURER_DEFAUT" /></th>
					</thead>
					<tbody>
						<%-- Rempli dynamiquement ! --%>
					</tbody>
				</table>
			</div>
			
		</div>
	</td>
</tr>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/find_ajax_el/bodyButtons.jspf"%>
<%@ include file="/theme/find_ajax_el/bodyErrors.jspf"%>
<%@ include file="/theme/find_ajax_el/footer.jspf"%>