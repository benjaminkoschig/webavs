<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1115"/>
<c:set var="labelTitreEcran" value="JSP_CONTROLES_EMPLOYEURS"/>

<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="true" scope="page" />
<c:set var="bButtonCancel" value="true" scope="page" />
<c:set var="bButtonDelete" value="true" scope="page"/>
<c:set var="bButtonUpdate" value="true" scope="page" />

<c:set var="employeur" value="${viewBean.employeur}" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
globazGlobal.idEmployeur = ${employeur.id};
//fonctions de bases à redéfinir

function add () {
	document.forms[0].elements('userAction').value="vulpecula.ctrlemployeur.controleEmployeur.afficher&_method=_add";
}

function upd() {
}

function validate() {
	var state = true;
	 if (document.forms[0].elements('_method').value == "add") {
	     document.forms[0].elements('userAction').value="vulpecula.ctrlemployeur.controleEmployeur.ajouter";
	 } else {
	     document.forms[0].elements('userAction').value="vulpecula.ctrlemployeur.controleEmployeur.modifier";
	 }
	 return state;
}

function cancel() {
	document.forms[0].elements('userAction').value="back";
}

function del() {
	var message = jQuery.i18n.prop("ajax.deleteMessage");
	if(confirm(message)) {
		document.forms[0].elements('userAction').value="vulpecula.ctrlemployeur.controleEmployeur.supprimer";
		document.forms[0].submit();
	}
}

function init(){
}
//chargement du dom jquery
$(function () {
	
});
</script>


<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<c:if test="${not empty viewBean.id}">
	<span class="postItIcon" data-g-note="idExterne:${viewBean.id}, tableSource:PT_CONTROLES_EMPLOYEURS"></span>
</c:if>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<input type="hidden" name="tab" />
<div class="content">
	<div class="blocLeft">
			<%@ include file="/vulpeculaRoot/blocs/employeur.jspf" %>
	</div>
</div>
<div style="margin-top: 30px;">
	<table>
		<tr>
			<td>
				<label for="dateControle"><ct:FWLabel key="JSP_DATE_CONTROLE"/></label>
			</td>
			<td>
				<input id="dateControle" name="dateControle" value="${viewBean.dateControle}" data-g-calendar="" />			
			</td>
		</tr>
		<tr>
			<td>
				<label for="montant"><ct:FWLabel key="JSP_MONTANT"/></label>
			</td>
			<td>
				<input id="montant" name="montant" value="${viewBean.montant}" data-g-amount="" />
			</td>
		</tr>
		<tr>
			<td>
				<label for="dateAu"><ct:FWLabel key="JSP_CONTROLE_AU"/></label>
			</td>
			<td>
				<input id="dateAu" name="dateAu" value="${viewBean.dateAu}" data-g-calendar="" /> 
			</td>
		</tr>
		<tr>
			<td><label for="type"><ct:FWLabel key="JSP_TYPE"/></label></td>
			<td>
				<select name="type">
					<option value=""></option>
					<c:choose>
						<c:when test="${empty viewBean.id}">
							<c:forEach var="type" items="${viewBean.types}">
								<c:choose>
									<c:when test="${viewBean.typeControleDefaut==type.id}">
										<option selected="selected" value="${type.id}">${type.libelle}</option>
									</c:when>
									<c:otherwise>
										<option value="${type.id}">${type.libelle}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<c:forEach var="type" items="${viewBean.types}">
								<c:choose>
									<c:when test="${type.id==viewBean.type}">
										<option selected="selected" value="${type.id}">${type.libelle}</option>
									</c:when>
									<c:otherwise>
										<option value="${type.id}">${type.libelle}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</select>
			</td>
		</tr>
		<tr>
			<td>
				<label for="autresMesures"><ct:FWLabel key="JSP_AUTRES_MESURES" /></label>
			</td>
			<td>
				<c:choose>
					<c:when test="${viewBean.autresMesures}">
						<input id="autresMesures" name="autresMesures" type="checkbox" checked="checked">
					</c:when>
					<c:otherwise>
						<input id="autresMesures" name="autresMesures" type="checkbox">
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td>
				<label for="numeroMeroba"><ct:FWLabel key="JSP_NO_RAPPORT"/></label>
			</td>
			<td>
				<input id="numeroMeroba" name="numeroMeroba" value="${viewBean.numeroMeroba}" />
			</td>
		</tr>
		<tr>
			<td>
				<label for="controleur"><ct:FWLabel key="JSP_CONTROLEUR" /></label>
			</td>
			<td>
				<input id="reviseur" name="reviseur" value="${viewBean.reviseur}" size="50" />
			</td>
		</tr>
	</table>
</div>
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>