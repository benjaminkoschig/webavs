<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.amal.vb.sedexpt.AMSedexptcreationannonceViewBean" %>
<%@ page import="ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>

<%-- tpl:put name="zoneInit" --%>
<%
AMSedexptcreationannonceViewBean viewBean = (AMSedexptcreationannonceViewBean)session.getAttribute("viewBean");

boolean viewBeanIsNew = true;
// Disable button
bButtonNew=false;
bButtonUpdate=false;
bButtonDelete=false;
%>
<% idEcran="AM3007"; %>
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
	$('#email').removeProp('disabled');
	$('#dateReductionPrimeDe').removeProp('disabled');
	$('#dateReductionPrimeA').removeProp('disabled');
	$('#annee').removeProp('disabled');
	$('#simulation').removeProp('disabled');
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
	$("#launchGenererAnnoncePT").attr('disabled',!isOkForLaunch);
}

$(function () {
	$("#launchGenererAnnoncePT").click(function () {
		$("#launchGenererAnnoncePT").prop('disabled', true);
		document.forms[0].elements('userAction').value="amal.sedexpt.sedexptcreationannonce.launchGenererAnnoncePT";
		document.forms[0].submit();
	});

	$("#launchGenererAnnoncePT").prop('disabled', true);
	
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
			Création d'annonce de demande PT
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%-- Champ --%>
						<tr><td>&nbsp;<td></tr>
	<tr>
		<td><%-- tpl:insert attribute="zoneMain" --%>
			<table id="AL0028zone" class="zone">
				<tr>
					<td>Sous-type Message</td>
					<td>
						<input tabindex="0" style="width: 10cm" type="text" value="<%=AMMessagesSubTypesAnnonceSedex.getSubTypeCSLibelle(AMMessagesSubTypesAnnonceSedex.DEMANDE_PRIME_TARIFAIRE.getValue())%>" disabled="disabled"/>
					</td>
				</tr>
				<tr>
	            	<td> <ct:FWLabel key="AL0028_EMAIL"/></td>
					<td><input tabindex="1" style="width: 10cm" type="text" id="email"  name=email  value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>"/></td>
				</tr>
				<tr>
					<td>Date Réduction de prime de </td>
					<td>
						<input tabindex="2" class="clearable" type="text" id="dateReductionPrimeDe"
							   name="dateReductionPrimeDe" value=""
							   data-g-calendar="mandatory:false"/>
					</td>
				</tr>
				<tr>
					<td>Date Réduction de prime à </td>
					<td>
						<input tabindex="3" class="clearable" type="text" id="dateReductionPrimeA"
							   name="dateReductionPrimeA" value=""
							   data-g-calendar="mandatory:false"/>
					</td>
				</tr>
				<tr>
					<td>Année</td>
					<td><input tabindex="4" class="clearable" type="text" id="annee" name="annee" maxlength="4"/></td>
				</tr>
				<tr>
					<td>Simulation</td>
					<td><input tabindex="5" type="checkbox" id="simulation" name="simulation"/></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><input tabindex="6" id="launchGenererAnnoncePT" type="button" name="launchGenererAnnoncePT" value='Lancer'/></td>
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