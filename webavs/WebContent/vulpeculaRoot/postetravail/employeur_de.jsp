<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/find_ajax_el/header.jspf" %>

<c:set var="bButtonNew" value="false" />

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° écran et titre --%>
<c:set var="idEcran" value="PTT1009"/>
<c:set var="labelTitreEcran" value="JSP_EMPLOYEURS"/>

<%@ include file="/theme/find_ajax_el/javascripts.jspf" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjaxList.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/postetravail/employeur_de.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/widget/globazwidget.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/widget.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar/AnchorPosition.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar/PopupWindow.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/menuPopup.js"></script>


<script type="text/javascript">
	globazGlobal.ACTION_AJAX = '${userActionListerAjax}';
</script>

<%@ include file="/theme/find_ajax_el/bodyStart.jspf" %>
	<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/find_ajax_el/bodyStart2.jspf" %>

<c:if test="${viewBean.protege}">
	<tr>
		<td><span style="color:red">${viewBean.protegeLibelle}</span></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
</c:if>


<tr>
	<td>
	<div class="area">
		<div class="areaSearch">
			<table width="100%">
				<tr><td width ="20%"><ct:FWLabel key='JSP_EMPLOYEUR_NO_AFFILIATION'/></td><td><input type="text" value="${viewBean.searchModel.likeNumeroAffilie}" id="searchModel.likeNumeroAffilie"></input></td></tr>
				<tr><td width ="20%"><ct:FWLabel key='JSP_EMPLOYEUR_RAISON_SOCIALE'/></td><td><input type="text" value="${viewBean.searchModel.likeRaisonSocialeUpper}" id="searchModel.likeRaisonSocialeUpper"></input></td></tr>
				<tr>
					<td width ="20%">
						<ct:FWLabel key='JSP_CONVENTION'/>
					</td>
					<td>
						<select id="idConvention">
								<option value="" />
							<c:forEach var="convention" items="${viewBean.conventions}">
								<c:choose>
									<c:when test="${convention.id==viewBean.searchModel.forIdConvention}">
										<option value="${convention.id}" selected="selected">${convention.designation}</option>
									</c:when>
									<c:otherwise>
										<option value="${convention.id}">${convention.designation}</option>
									</c:otherwise>								
								</c:choose>
							</c:forEach>
						</select>
					</td>
				</tr>
			</table>													
		</div>		
		<%@ include file="/theme/find_ajax_el/searchNewButtons.jspf" %> 
		<div>
			<table class="areaTable" width="100%">
				<thead>
					<tr>
						<th></th>
						<th data-orderKey="affilieNumero"><ct:FWLabel key='JSP_EMPLOYEUR_NO_AFFILIATION'/></th>
						<th style="text-align: left;" data-orderKey="raisonSocialeUpper"><ct:FWLabel key='JSP_EMPLOYEUR_RAISON_SOCIALE'/></th>
						<th data-orderKey="designation"><ct:FWLabel key='JSP_CONVENTION'/></th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
	</td>
</tr>

<%@ include file="/theme/find_ajax_el/bodyButtons.jspf" %>

<%@ include file="/theme/find_ajax_el/bodyErrors.jspf" %>

<%@ include file="/theme/find_ajax_el/footer.jspf" %>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />