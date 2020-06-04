<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="AL0037"/>
<c:set var="labelTitreEcran" value="AL0037_TITLE"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<c:set var="userActionListe" value="al.listes" />
<ct:checkRight var="hasCreateRightOnListes" element="${userActionListe}" crud="c" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
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

function init(){
}

function showErrorMessage(message) {
	var html = '<div>';
	html += message;
	html += '</div>';

	$html = $(html);
	$html.dialog({
		position: 'center',
		title: "Erreur",
		width: 400,
		height: 50,
		show: "blind",
		hide: "blind",
		closeOnEscape: true,
		buttons: {'Close': popupClose}
	});
}

function popupClose() {
	$html.dialog("close");
}

function postInit() {
	$('input').removeProp('disabled');
	$('select').removeProp('disabled');
}

$(function () {
	$('#launch').click(function () {
		var dateDebut = $('#dateDebut').val();
		var dateFin = $('#dateFin').val();
		if (!dateDebut || !dateFin) {
			showErrorMessage("Aucune période n'est renseignée");
			return;
		}
		$(this).prop('disabled', true);
		document.forms[0].elements('userAction').value="al.impotsource.listeIS.executer";
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
				<td><ct:FWLabel key="AL0037_EMAIL"/></td>
				<td><input id="email" type="text" name="email" value="${viewBean.email}" /></td>
			</tr>		
			<tr>
				<td><ct:FWLabel key="AL0037_CAISSE_AF"/></td>
				<td>
					<select name="caisseAF">
						<option value=""><ct:FWLabel key="AL0037_TOUTES"/></option>
						<c:forEach var="caisseAF" items="${viewBean.caissesAF}">
							<c:choose>
								<c:when test="${viewBean.caisseAF==caisseAF.id}">
									<option selected="selected" value="${caisseAF.id}">${caisseAF.designation1}</option>
								</c:when>
								<c:otherwise>
									<option value="${caisseAF.id}">${caisseAF.designation1}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
			</tr>		
			<tr>				
				<td><ct:FWLabel key="AL0037_CANTON"/></td>
				<td>
					<select name="canton">
						<option value=""><ct:FWLabel key="AL0037_TOUS"/></option>
						<c:forEach var="canton" items="${viewBean.cantons}">
							<c:choose>
								<c:when test="${viewBean.canton==canton.id || (empty viewBean.canton && canton.id==viewBean.cantonDefaut)}">
									<option selected="selected" value="${canton.id}">${canton.libelle}</option>				
								</c:when>
								<c:otherwise>
									<option value="${canton.id}">${canton.libelle}</option>				
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td><ct:FWLabel key="AL0037_PERIODE_DE"/></td>
				<td colspan="3">
					<input	type="text"
							  id="dateDebut"
							  name="dateDebut"
							  data-g-calendar="mandatory:true"
							  value="${viewBean.dateDebut}" />
				<ct:FWLabel key="AL0037_PERIODE_A"/>
				<input	type="text"
						  id="dateFin"
						  name="dateFin"
						  data-g-calendar="mandatory:true"
						  value="${viewBean.dateFin}" />
				</td>
			</tr>
			<tr>				
				<td><ct:FWLabel key="AL0037_TYPE_DE_LISTE"/></td>
				<td>
					<select name="typeListe">
						<option value="0"><ct:FWLabel key="AL0037_TOUS"/></option>
						<c:forEach var="liste" items="${viewBean.listes}">
							<c:choose>
								<c:when test="${liste.id==viewBean.typeListe}">
									<option selected="selected" value="${liste.id}">${liste.libelle}</option>				
								</c:when>
								<c:otherwise>
									<option value="${liste.id}">${liste.libelle}</option>				
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>					
				</td>
			</tr>

			<c:if test="${not processLaunched}">	
			<tr>
				<td style="text-align: center;" colspan="4">
					<br />
					<input id="launch" type="button" name="launch" value='<ct:FWLabel key="AL0037_LANCER"/>'/>
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

<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>