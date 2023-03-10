<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Param?trage global de la page ************************************************************** --%>
<%-- labels n? ecran et titre --%>
<c:set var="idEcran" value="PPT2007"/>
<c:set var="labelTitreEcran" value="JSP_BOUCLEMENT_CP"/>

<%-- visibilt?s des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>

<%--  *************************************************************** Script propre ? la page **************************************************************** --%>
<script type="text/javascript">
//fonctions de bases ? red?finir
globazGlobal.messageAnneeRequise = '${viewBean.messageAnneeRequise}';

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

function init(){
}

function postInit() {
	$('input,select').removeAttr('disabled');
}
//chargement du dom jquery
$(function () {
	$('#launch').click(function () {
		if($('#annee').val().length>0) {
			$(this).prop('disabled', true);
			document.forms[0].elements('userAction').value="vulpecula.congepaye.bouclement.executer";
			document.forms[0].submit();
		} else {
			showErrorDialog(globazGlobal.messageAnneeRequise);
		}
	});
	
});
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
<div>
	<div style="text-align: center;">
	<table>
		<tr>
			<td><ct:FWLabel key="JSP_EMAIL"/></td>
			<td>
				<input name="email" value="${viewBean.email}" />
			</td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_CONVENTIONS"/></td>
			<td>
				<select name="idConvention">
					<option value="" />
					<c:forEach var="convention" items="${viewBean.conventions}">
						<c:choose>
							<c:when test="${viewBean.idConvention==convention.id}">
								<option selected="selected" value="${convention.id}">${convention.designation}</option>
							</c:when>
							<c:otherwise>
								<option value="${convention.id}">${convention.designation}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_ANNEE"/></td>
			<td>
				<c:choose>
					<c:when test="${empty viewBean.annee}">
						<input id="annee" name="annee" data-g-integer="" value="${viewBean.currentYear}" />
					</c:when>
					<c:otherwise>
						<input id="annee" name="annee" data-g-integer="" value="${viewBean.annee}" />
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_SOLDES_CONGES_PAYES"/></td>
			<td>
				<c:choose>
					<c:when test="${viewBean.miseAJour}">
						<input name="miseAJour" type="checkbox" checked="checked" />
					</c:when>
					<c:otherwise>
						<input name="miseAJour" type="checkbox" />
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<c:if test="${not processLaunched}">	
		<tr>
			<td style="text-align: center;" colspan="2">
				<br />
				<input id="launch" type="button" name="launch" value='<ct:FWLabel key="JSP_LANCER"/>'/>
			</td>
		</tr>
		</c:if>
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