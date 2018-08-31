<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT6001" />
<c:set var="labelTitreEcran" value="JSP_LISTESINTERNES" />

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
		$('#recapitulatifsParConvention').click(function() {
			$(this).prop('disabled', true);
			document.forms[0].elements('userAction').value="vulpecula.comptabilite.listesInternes.executer";
			document.forms[0].elements('genre').value='RECAP_CONVENTION';
			document.forms[0].submit();
		});
		$('#recapitulatifsParCaisse').click(function() {
			$(this).prop('disabled', true);
			document.forms[0].elements('userAction').value="vulpecula.comptabilite.listesInternes.executer";
			document.forms[0].elements('genre').value='RECAP_CAISSE';
			document.forms[0].submit();
		});
		$('#recapitulatifsParGenreCaisse').click(function() {
			$(this).prop('disabled', true);
			document.forms[0].elements('userAction').value="vulpecula.comptabilite.listesInternes.executer";
			document.forms[0].elements('genre').value='RECAP_GENRE_CAISSE';
			document.forms[0].submit();
		});
		$('#recapitulatifsParCaisseConvention').click(function() {
			$(this).prop('disabled', true);
			document.forms[0].elements('userAction').value="vulpecula.comptabilite.listesInternes.executer";
			document.forms[0].elements('genre').value='RECAP_CAISSE_CONVENTION';
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
		<td><ct:FWLabel key="JSP_ANNEE"/></td>
		<td><input id="annee" type="text" name="annee" value="${viewBean.annee}" /></td>
	</tr>
	<tr>
		<td>
			<label for="avecTo"><ct:FWLabel key="JSP_TAXATION_OFFICE" /></label>
		</td>
		<td>
			<c:choose>
				<c:when test="${viewBean.avecTo}">
					<input id="avecTo" name="avecTo" type="checkbox" checked="checked" />
				</c:when>
				<c:otherwise>
					<input id="avecTo" name="avecTo" type="checkbox" />
				</c:otherwise>
			</c:choose>
		</td>
	</tr>					
	<tr>
		<td colspan="2">
			<input id="recapitulatifsParConvention" style="width:400px;" type="button" value='<ct:FWLabel key="JSP_LISTESINTERNES_RECAPITULATIF_PAR_CONVENTION"/>' />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<input id="recapitulatifsParCaisse" style="width:400px;" type="button" value='<ct:FWLabel key="JSP_LISTESINTERNES_RECAPITULATIF_PAR_CAISSE"/>' />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<input id="recapitulatifsParGenreCaisse" style="width:400px;" type="button" value='<ct:FWLabel key="JSP_LISTESINTERNES_RECAPITULATIF_PAR_GENRE_CAISSE"/>' />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<input id="recapitulatifsParCaisseConvention" style="width:400px;" type="button" value='<ct:FWLabel key="JSP_LISTESINTERNES_RECAPITULATIF_PAR_CONVENTION_CAISSE"/>' />
		</td>
	</tr>
</table>
<c:if test="${processLaunched}">
	<div style="margin-top:20px;vertical-align:middle; color: white; font-weight: bold; text-align: center;background-color: green">
		<ct:FWLabel key="FW_PROCESS_STARTED" />
	</div>
</c:if>
<input name="genre" type="hidden" />
</div>
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf"%>
<%@ include file="/theme/detail_el/bodyErrors.jspf"%>
<%@ include file="/theme/detail_el/footer.jspf"%>