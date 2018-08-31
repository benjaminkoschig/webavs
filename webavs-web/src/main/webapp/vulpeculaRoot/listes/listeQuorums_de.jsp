<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.globall.db.BSessionUtil"%>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT2017"/>
<c:set var="labelTitreEcran" value="JSP_IMPRESSION_LISTES_QUORUMS"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<c:set var="userActionListe" value="vulpecula.listes" />
<ct:checkRight var="hasCreateRightOnListes" element="${userActionListe}" crud="c" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
function add () {
}

function upd() {
}

function validate() {
}

function cancel() {
}

function del() {
}

function init() {
}

function postInit() {
	$('input').removeProp('disabled');
	$('select').removeProp('disabled');
}

$(function () {
	var $launch = $("#launch");
	$launch.click(function () {
		$launch.prop('disabled', true);
		document.forms[0].elements('userAction').value="vulpecula.listes.listeQuorums.executer";
		document.forms[0].submit();
	});
})

</script>
<style type="text/css">
	#mainWrapper {
		height: auto !important;
	}
</style>


<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div id="informations" style="position: absolute; right:0px; width: 30%">
</div>
<div style="width: 100%;text-align: center;">
	<div>
		<table>
			<tr>
				<td nowrap width="128"><label for="email"><ct:FWLabel key="JSP_EMAIL"/></label></td>
				<td><input id="email" type="text" name="email" value="${viewBean.email}"/></td>
			</tr>
			<tr>
				<td><label for="dateFrom"><ct:FWLabel key="JSP_DATE_FROM_REFERENCE"/></label></td>
				<td><input id="dateFrom" name="dateFrom" value="${viewBean.dateFrom}" data-g-calendar=""/></td>
				
				<td nowrap width="60"><label for="dateTo"><ct:FWLabel key="JSP_DATE_TO_REFERENCE"/></label></td>
				<td><input id="dateTo" name="dateTo" value="${viewBean.dateTo}" data-g-calendar=""/></td>
			</tr>
			<tr>
				<td><label for="detail"><ct:FWLabel key="JSP_LISTES_QUORUMS_CHECKBOX_DETAIL_DONNEES"/></label></td>
				<td><c:choose>
						<c:when test="${viewBean.detail}">
							<input id="detail" name="detail" type="checkbox"
								checked="checked" />
						</c:when>
						<c:otherwise>
							<input id="detail" name="detail" type="checkbox" />
						</c:otherwise>
					</c:choose></td>
			</tr>

			<tr>
				<td><label for="codeConvention"><ct:FWLabel key="JSP_LISTES_QUORUMS_CHECKBOX_CONVENTION"/></label></td>
				<td><select id="idListeConvention" name="codeConvention">
						<c:choose>
							<c:when test="${empty viewBean.codeConvention}">
								<option selected="selected" value=""></option>
								<c:forEach var="convention" items="${viewBean.conventions}">
									<option value="${convention.code}">${convention.code} - ${convention.designation}</option>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<c:forEach var="convention" items="${viewBean.conventions}">
									<c:choose>
										<c:when test="${convention.code==viewBean.codeConvention}">
											<option selected="selected" value="${convention.code}">${convention.code} - ${convention.designation}</option>
										</c:when>
										<c:otherwise>
											<option value="${convention.code}">${convention.code} - ${convention.designation}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</c:otherwise>
						</c:choose>
				</select></td>
			</tr>
			<tr>
				<td style="text-align: center;" colspan="2">
					<br />
					<input id="launch" type="button" name="launch" value='<ct:FWLabel key="JSP_LANCER"/>'/>
				</td>
			</tr>
		</table>
	</div>
</div>
<c:if test="${processLaunched}">
	<div style="margin-top:20px;vertical-align:middle; color: white; font-weight: bold; text-align: center;background-color: green">
		<ct:FWLabel key="FW_PROCESS_STARTED"/>
	</div>
</c:if>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>