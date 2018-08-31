<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@page import="globaz.vulpecula.vb.comptabilite.PTListeRecapParRubriqueViewBean"%>
<%@page import="globaz.osiris.db.comptes.CARoleViewBean"%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT6005"/>
<c:set var="labelTitreEcran" value="JSP_PT_LISTES_RECAP_PAR_RUBRIQUES"/>

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
<%
PTListeRecapParRubriqueViewBean viewBean = (PTListeRecapParRubriqueViewBean) session.getAttribute("viewBean");

globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>

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
	$('#launch').prop('disabled', true);
}

$(function () {
	$('input').keyup(function() {
		var canLaunch = true;
		var inputObj = $(".jadeAutocompleteAjax");
		for(i = 0 ; i < inputObj.size(); i++){
			if(canLaunch && 
					(inputObj[i].value == undefined || inputObj[i].value == null || inputObj[i].value == "")) {
				canLaunch = false;
			}
		}
		if(!canLaunch){
			var roleFromString = $('#fromIdExterneRole')[0].value; 
			var roleToString = $('#toIdExterneRole')[0].value;
			if (roleFromString != undefined && roleFromString != null && roleFromString != "" &&
					roleToString != undefined && roleToString != null && roleToString != "") {
				canLaunch = true;
			}
		}
		$('#launch')[0].disabled = !canLaunch; 
	});
})


$(function () {
	$('#launch').click(function () {
		$("#launch").prop('disabled', true);
		document.forms[0].elements('userAction').value="vulpecula.comptabilite.listeRecapParRubrique.executer";
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
				<td><label for="email"><ct:FWLabel key="JSP_EMAIL"/></label></td>
				<td><input id="email" type="text" name="email" value="${viewBean.email}" /></td>
			</tr>
			
			<tr>
            <td width="128">R&ocirc;le</td>
            <td>
               <select name="forIdRole" tabindex="2">
              	<%=CARoleViewBean.createOptionsTags(objSession, viewBean.getForIdRole())%>
              </select>
            </td>
            </tr>
            

<%
	String selectBlock = globaz.osiris.parser.CASelectBlockParser.getForIdGenreSelectBlock(objSession);

	if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectBlock)) {
		out.print("<tr>");
		out.print("<td align=\"left\">Genre</td>");
		out.print("<td align=\"left\">");
		out.print(selectBlock);
		out.print("</td>");
		out.print("</tr>");
	}
%>

<%
	String selectCategorieSelect= globaz.osiris.parser.CASelectBlockParser.getForIdCategorieSelectBlock(objSession);

	if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCategorieSelect)) {
		out.print("<tr>");
		out.print("<td align=\"left\">Cat&eacute;gorie</td>");
		out.print("<td align=\"left\">");
		out.print(selectCategorieSelect);
		out.print("</td>");
		out.print("</tr>");
	}
%>


	        <tr>
				<td><label for="date"><ct:FWLabel key="JSP_PERIODE"/></label></td>
				<td>
					<input name="fromDateDebut" value="" data-g-calendar="" />
						&nbsp;à...&nbsp;
					<input name="toDateFin" value="" data-g-calendar="" />
				</td>				
			</tr>

			<tr>
	            <td width="128">N° débiteur de...</td>
	            	<td>
	            		<input type="text" id="fromIdExterneRole" name="fromIdExterneRole" value="" class="libelleLong"/>
	            			&nbsp;à...&nbsp;
	            		<input type="text" id="toIdExterneRole" name="toIdExterneRole" value="" class="libelleLong"/>
	            	</td>
	            </td>
	        </tr>

			<tr data-g-clone=" ">
				<td><ct:FWLabel key="JSP_RUBRIQUE" /></td>
				<td><input name="fromIdExternes"
					class="jadeAutocompleteAjax" type="text"
					data-g-autocomplete="service:¦ch.globaz.vulpecula.business.services.rubrique.RubriqueService¦,
					 method:¦find¦,
					 criterias:¦{'likeIdExterne':'Id externe'}¦,
					 lineFormatter:¦#{idExterne}¦,
					 modelReturnVariables:¦idRubrique,idExterne¦,nbReturn:¦20¦,
					 functionReturn:¦
					 	function(element){
					 		this.value=$(element).attr('idExterne');
					 	}¦
					 ,nbOfCharBeforeLaunch:¦3¦" />
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

<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>