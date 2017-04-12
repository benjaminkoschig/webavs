<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.amal.vb.sedexco.AMSedexcocomparaisonViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>

<%-- tpl:put name="zoneInit" --%>
<%
AMSedexcocomparaisonViewBean viewBean = (AMSedexcocomparaisonViewBean)session.getAttribute("viewBean");

boolean viewBeanIsNew = true;
// Disable button
bButtonNew=false;
bButtonUpdate=false;
bButtonDelete=false;
%>
<% idEcran="AM3005"; %>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneScripts" --%>

<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<SCRIPT type="text/javascript" src="<%=servletContext%>/scripts/onglet.js"></SCRIPT>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
function add() {
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
function postInit(){
	//$('#launchListeComparaisons').removeProp('disabled');
	$('#email').removeProp('disabled');
	$('#annee').removeProp('disabled');
	$('#selectIdTiersCM').removeProp('disabled');
}

function isYearFilled() {
	var objRegExp  = /^(\d{4}$)/;
	
	if (objRegExp.test( $('#annee').val())) {
		return true;
	} else {
		return false;
	}
}


function processCanBeLaunch() {
	var isOkForLaunch = isYearFilled();
	$("#launchListeComparaisons").attr('disabled',!isOkForLaunch);
}

$(function () {
	$("#launchListeComparaisons").click(function () {		
		$("#launchListeComparaisons").prop('disabled', true);
		document.forms[0].elements('userAction').value="amal.sedexco.comparaisons.launchGenerationComparaison";
		document.forms[0].submit();
	});

	$("#launchListeComparaisons").prop('disabled', true);
	
	$("#email").blur(function() {
		processCanBeLaunch();
	});
	
	 $('#annee').blur(function() {
		 processCanBeLaunch();
	});

});

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			Liste de comparaisons
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%-- Champ --%>
						<tr><td>&nbsp;<td></tr>
	<tr>
			<td><%-- tpl:insert attribute="zoneMain" --%>
				<table id="AL0028zone" class="zone">
				
				<tr>
	              	 		<td> <ct:FWLabel key="AL0028_EMAIL"/>
							</td>
							<td>
							<input tabindex="1"  type="text" id="email"  name=email  value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>"/>
	                		</td>
	                		
						</tr>
					
						
					<tr>
						<td>Année</td>
						<td>							
	                		<input tabindex="4" class="clearable" type="text" id="annee" name="annee" maxlength="4"/>	
	                	</td>
	                	
				</tr>
					<tr>
						<td>Caisse maladie</td>
						<td>
	                		<input name="selectIdTiersCM" id="selectIdTiersCM"
								class="jadeAutocompleteAjax" type="text"
								data-g-autocomplete="service:¦ch.globaz.amal.business.services.sedexCO.AnnoncesCOService¦,
								 method:¦find¦,
								 criterias:¦{
								 	forCodeAdministrationLike:'Code',
								 	forDesignation1Like:'Designation'
								 }¦,
								 constCriterias:¦forGenreAdministration=509008¦,
								 lineFormatter:¦<b>#{admin.codeAdministration}</b><br />#{tiers.designation1} #{tiers.designation2}¦,
								 modelReturnVariables:¦tiers.id,tiers.designation1,tiers.designation2,admin.codeAdministration¦,nbReturn:¦20¦,
								 functionReturn:¦
								 	function(element){
								 		this.value=$(element).attr('admin.codeAdministration') + ' - ' + $(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
								 		$('#idTiersCM').val($(element).attr('tiers.id'))
								 	}¦
								 ,nbOfCharBeforeLaunch:¦3¦" />
								 <input type="hidden" name="idTiersCM" value="" id="idTiersCM"/>
	                	</td>
	                	<tr>
	                		<td>&nbsp;</td>
	                		<td>
	                		<input id="launchListeComparaisons" type="button" name="launchListeComparaisons" value='Lancer'/>
	                		</td>
	                	</tr>
	                	
				</tr>
				</table>
					</td>
		</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<script language="javascript">
if (document.forms[0].elements('_method').value != "add") {
	reloadMenuFrame(top.fr_menu, MENU_TAB_MENU);
}
</SCRIPT>
<script language="javascript">
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>