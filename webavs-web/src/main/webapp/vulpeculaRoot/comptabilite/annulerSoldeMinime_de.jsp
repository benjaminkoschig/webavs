<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT6004" />
<c:set var="labelTitreEcran" value="JSP_ANNULER_SOLDE_MINIME" />

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<%@ include file="/theme/detail_el/javascripts.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/widget/globazwidget.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/widget.css" />

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
	
	function propageName(elem) {
		$('#importFilename').val(elem.fileName);
	}
	
	$(function () {	
		$('#launch').click(function () {
				$("#launch").prop('disabled', true);
				document.forms[0].elements('userAction').value="vulpecula.comptabilite.annulerSoldeMinime.executer";
				document.forms[0].submit();
		});
	});
</script>

<%@ include file="/theme/detail_el/bodyStart.jspf"%>
<ct:FWLabel key="${labelTitreEcran}" />
<%@ include file="/theme/detail_el/bodyStart2.jspf"%>

<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div style="width: 100%;text-align: center;">
<table>
	<tr>
		<td><ct:FWLabel key="JSP_EMAIL"/></td>
		<td><input id="email" type="text" name="email" value="${viewBean.email}" /></td>
	</tr>						
	<tr>
		<td><ct:FWLabel key="JSP_JOURNAL_DATE"/></td>
		<td><input id="dateJournal" name="dateJournal" data-g-calendar="" value="" /></td>
	</tr>
	<tr>
		<td><label for="genreMembre"><ct:FWLabel key="JSP_JOURNAL_TYPE_MEMBRE"/></label></td>
		<td>
			<ct:FWListSelectTag name="typeMembre" data="${viewBean.typesMembres}" defaut="${viewBean.typeMembre}"/>
		</td>				
	</tr>
	<tr>
		<td><ct:FWLabel key="JSP_JOURNAL_LIBELLE"/></td>
		<td><input id="libelle" name="libelle" class="libelleLong" type=text" value="" /></td>
	</tr>

	<tr> 
		<td><ct:FWLabel key="JSP_MONTANT_MINIMUM"/></td>
		<td><input type="text" id="montantMinime" name="montantMinime" data-g-amount=" " value="" /></td>
	</tr>

	<tr> 
		<td><ct:FWLabel key="MODESIMULATION"/></td>
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
		<td style="text-align: center;" colspan="2">
			<br />
			<input id="launch" type="button" name="launch" value='<ct:FWLabel key="JSP_LANCER"/>'/>
		</td>
	</tr>
	</c:if>
</table>
<c:if test="${processLaunched}">
	<div style="margin-top:20px;vertical-align:middle; color: white; font-weight: bold; text-align: center;background-color: green">
		<ct:FWLabel key="FW_PROCESS_STARTED" />
	</div>
</c:if>
</div>
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf"%>
<%@ include file="/theme/detail_el/bodyErrors.jspf"%>
<%@ include file="/theme/detail_el/footer.jspf"%>