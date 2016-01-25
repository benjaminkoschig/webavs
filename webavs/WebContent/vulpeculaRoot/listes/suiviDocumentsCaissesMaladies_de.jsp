<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Param�trage global de la page ************************************************************** --%>
<%-- labels n� ecran et titre --%>
<c:set var="idEcran" value="PPT2008"/>
<c:set var="labelTitreEcran" value="JSP_SUIVI_DOCUMENTS_CAISSE_MALADIE"/>

<%-- visibilt�s des boutons --%>
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

<%--  *************************************************************** Script propre � la page **************************************************************** --%>
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

function init(){
}

function postInit() {
	$('input').removeProp('disabled');
	$('select').removeProp('disabled');
}

$(function () {
	$('#launch').click(function () {
		$(this).prop('disabled', true);
		document.forms[0].elements('userAction').value="vulpecula.listes.suiviDocumentsCaissesMaladies.executer";
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
				<td><ct:FWLabel key="JSP_EMAIL"/></td>
				<td><input id="email" type="text" name="email" value="${viewBean.email}" /></td>
			</tr>		
			<tr>
				<td><ct:FWLabel key="JSP_TYPE_DE_LISTE"/></td>
				<td>
					<select name="typeListe">
						<c:forEach var="entry" items="${viewBean.typesListes}">
							<c:choose>
								<c:when test="${viewBean.typeListe==entry.key}">
									<option selected="selected" value="${entry.key}">${entry.value}</option>
								</c:when>
								<c:otherwise>
									<option value="${entry.key}">${entry.value}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
			</tr>	
			<tr>
				<td>
					<label for="simulation"><ct:FWLabel key="JSP_SIMULATION"/></label>
				</td>
				<td>
					<c:choose>
						<c:when test="${viewBean.simulation}">
							<input id="simulation" name="simulation" type="checkbox" checked="checked" />
						</c:when>
						<c:otherwise>
							<input id="simulation" name="simulation" type="checkbox" />
						</c:otherwise>
					</c:choose>
				</td>
			</tr>	
			<c:if test="${not processLaunched}">	
			<tr>
				<td style="text-align: center;" colspan="4">
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