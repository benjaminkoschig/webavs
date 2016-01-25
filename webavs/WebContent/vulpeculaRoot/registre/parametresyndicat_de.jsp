<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1015"/>
<c:set var="labelTitreEcran" value="JSP_SYNDICAT_PARAMETRE"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
//fonctions de bases à redéfinir

function add () {
}

function upd() {
}

function validate() {
	state = true

	if (document.forms[0].elements('_method').value == "add"){
		document.forms[0].elements('userAction').value="vulpecula.registre.parametresyndicat.ajouter";
	}
	else
		document.forms[0].elements('userAction').value="vulpecula.registre.parametresyndicat.modifier";
	return (state);
}

function cancel() {
	document.forms[0].elements('userAction').value="vulpecula.registre.syndicat.afficher";
	document.forms[0].elements('idSyndicat').value="${viewBean.idSyndicat}";
}

function del() {
	if(window.confirm(jQuery.i18n.prop("ajax.deleteMessage"))) {
		document.forms[0].elements('userAction').value="vulpecula.registre.parametresyndicat.supprimer";
		document.forms[0].submit();
	}
}

function init(){
}

function postInit() {
	$('#adresse').attr('disabled',true);
}

//chargement du dom jquery
$(function () {
	
});
</script>
<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div>
	<table>
		<tr>
			<td><ct:FWLabel key="JSP_NUMERO"/></td>
			<td>
				<input id="idSyndicat" name="idSyndicat" value="${viewBean.idSyndicat}" class="readOnly" readonly="readonly" tabindex="-1" />
			</td>
			<td><ct:FWLabel key="JSP_POURCENTAGE"/></td>
			<td>
				<input id="pourcentage" name="pourcentage" value="${viewBean.pourcentage}" data-g-rate="nbMaxDecimal:2,mandatory:true">
			</td>			
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_DESIGNATION"/></td>
			<td>
				<input id="designation" name="designation" value="${viewBean.designation}" class="readOnly" readonly="readonly" tabindex="-1">
			</td>
			<td><ct:FWLabel key="JSP_MONTANT_PAR_TRAVAILLEUR"/></td>
			<td>
				<input id="montantParTravailleur" name="montantParTravailleur" value="${viewBean.montantParTravailleur}" data-g-amount="">
			</td>
		</tr>
		<tr>
			<td rowspan="2">
				<ct:FWLabel key="JSP_ADRESSE"/>
			</td>
			<td rowspan="2">
				<textarea id="adresse" rows="5" cols="40" class="readOnly" readonly="readonly" tabindex="-1">${viewBean.adresse}</textarea>
			</td>
			<td><ct:FWLabel key="JSP_PERIODE"/></td>
			<td>
				<span>
					<input id="dateDebut" name="dateDebut" type="text" data-g-calendar="" value="${viewBean.dateDebut}" />
					<span><ct:FWLabel key="JSP_FIN"/></span>
					<input id="dateFin" name="dateFin" type="text" data-g-calendar="" value="${viewBean.dateFin}" />
				</span>
			</td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_CAISSE_METIER"/></td>
			<td>
				<select name="idCaisseMetier">
					<c:forEach var="caisseMetier" items="${viewBean.caissesMetiers}">
						<c:choose>
							<c:when test="${caisseMetier.id==viewBean.idCaisseMetier}">
								<option selected="selected" value="${caisseMetier.id}">${caisseMetier.designation1}</option>
							</c:when>
							<c:otherwise>
								<option value="${caisseMetier.id}">${caisseMetier.designation1}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</td>
		</tr>
	</table>
</div>
<input name="idSyndicat" type="hidden" value="${viewBean.idSyndicat}" />

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>