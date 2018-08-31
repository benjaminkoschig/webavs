<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.globall.db.BSessionUtil"%>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT7002"/>
<c:set var="labelTitreEcran" value="JSP_IMPRESSION_FACTURATION"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<c:set var="tableHeight" value="auto" scope="page" />

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
	var $launchFacturaion = $("#launchPrintFacturation");
	$launchFacturaion.click(function () {
		$launchFacturaion.prop('disabled', true);
		if(globazGlobal.checkFields()){
			document.forms[0].elements('userAction').value="vulpecula.ap.impressionFacturation.executer";
			document.forms[0].submit();
	}else{
		$launchFacturaion.removeAttr('disabled');
	}
	});
});

globazGlobal.checkFields = function () {
	var idEmployeur = $('#idEmployeur').val();
	var idPassage = $('#idPassage').val();
	var dateImpression = $('#dateImpression').val();
	var email = $('#email').val();
	var messageErreurPassage = "${viewBean.messageErreurPassage}";
	var messageErreurEmail = "${viewBean.messageErreurEmail}";

	if(idPassage.length==0){
		showErrorDialog(messageErreurPassage);
		return false;
	}
	if(email.length <= 0){
		showErrorDialog(messageErreurEmail);
		return false;
	}
	
	return true;
};
</script>

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
				<td>
					<label for="email">
						<ct:FWLabel key="JSP_EMAIL"/>
					</label>
				</td>
				<td>
					<input id="email" style="width:200px;" type="text" data-g-string="mandatory:true" name="email" value="${viewBean.email}" />
				</td>
			</tr>
			<tr> 
	            <td>
	            	<ct:FWLabel key="JSP_PASSAGE"/>
            	</td>
	            <td> 
	            	<input type="hidden" name="idPlan" value="11"/>
	              <input type="text" id="idPassage" data-g-string="mandatory:true" name="idPassage" maxlength="15" size="15" style="text-align:right;" value="${viewBean.idPassage}">
	              <%
	              	Object[] psgMethodsName = new Object[]{
						new String[]{"setIdPassage","getIdPassage"},
						new String[]{"setLibellePassage","getLibelle"},
						};
					Object[] psgParams= new Object[]{
					        new String[]{"idPlan", "planToFilter"},
					};
					String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/ap/impressionFacturation_de.jsp";	
				%>
				<ct:ifhasright element="musca.facturation.passage.chercher" crud="r">
		            <ct:FWSelectorTag name="passageSelector" 
						methods="<%=psgMethodsName%>"
						providerPrefix="FA"			
						providerApplication ="musca"			
						providerAction ="musca.facturation.passage.chercher"			
						providerActionParams ="<%=psgParams%>"
						redirectUrl="<%=redirectUrl%>"/>
				</ct:ifhasright>
				</td>
			</tr>	
			<tr>
				<td style="text-align: center;" colspan="2">
					<br />
					<input id="launchPrintFacturation" type="button" name="launchPrintFacturation" value='<ct:FWLabel key="JSP_LANCER"/>'/>
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
