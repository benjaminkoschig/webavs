<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme"%>

<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
	
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%
	idEcran="AM0080";
	rememberSearchCriterias = true;
	bButtonNew = false;
	
	String currentJobType = request.getParameter("searchModel.forTypeJob");
	String currentUserId = objSession.getUserId();
	if(currentJobType.equals("42002302")){
		currentUserId = "";
	}else{
		String parameterCurrentUserId = request.getParameter("searchModel.forUserJob");
		if(parameterCurrentUserId!=null){
			currentUserId=parameterCurrentUserId;
		}
	}
	
	
%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript">

bFind = true;

usrAction = "amal.documents.documents.lister";
$(document).ready(function() {
	// -------------------------------------------------------------------------------
	// INITIALISATION
	// -------------------------------------------------------------------------------
	// attribue une id à tous les champs ayant un nom mais pas encore d'id
	$('*',document.forms[0]).each(function(){
		if(this.name!=null && this.id==""){
			this.id=this.name;
		}
	});
	// augmente la taille de l'iframe en hauteur :)
	$('iframe').css('height','550px');
	// initialisation des options
	$('.optionLine').hide();
	$('#legendOptionId').css('background-image','url(\'<%=request.getContextPath()%>/images/amal/arrow_collapsed.png\')');	

	// -------------------------------------------------------------------------------
	// EVENEMENTS
	// -------------------------------------------------------------------------------
	// manage the onclick event sur checkbox
	$('#searchModel\\.forNotStatusEnvoiChecked').click(function(){
		if($('#searchModel\\.forNotStatusEnvoiChecked').prop('checked')){
			$('#searchModel\\.forNotStatusEnvoi').val('');
		}else{
			$('#searchModel\\.forNotStatusEnvoi').val('<%=IAMCodeSysteme.AMDocumentStatus.SENT.getValue()%>');
		}
		$('input[name="btnFind"]').click();
	});
	// manage the on click event on legend option
	$('#legendOptionId').click(function(){
		$('.optionLine').toggle();
		var s_backImage = $('#legendOptionId').css('background-image');
		if(s_backImage.indexOf('expanded')>=0){
			$('#legendOptionId').css('background-image','url(\'<%=request.getContextPath()%>/images/amal/arrow_collapsed.png\')');	
		}else{
			$('#legendOptionId').css('background-image','url(\'<%=request.getContextPath()%>/images/amal/arrow_expanded.png\')');	
		}
	});
});

function clearFields() {
	$('#searchModel\\.forUserJob').val('');
	$('#searchModel\\.forDateMin').val('');
	$('#searchModel\\.forDateMax').val('');
	$('#searchModel\\.forNotStatusEnvoi').val('<%=IAMCodeSysteme.AMDocumentStatus.SENT.getValue()%>');
	$('#searchModel\\.forNotStatusEnvoiChecked').prop('checked',false);
	$('input[name="btnFind"]').click();
}

</script>

<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyStart.jspf" %>
	<%-- tpl:insert attribute="zoneTitle" --%>
		Gestion des documents
	<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
				<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
		<div id="divRecherche" style="background-image:url('<%=request.getContextPath()%>/images/summary_bg.png');
					background-repeat:repeat-x; float:left;border:1px solid #226194;">
			
			<INPUT type="hidden" name="searchModel.forTypeJob" value="<%=currentJobType%>">
					
			<TABLE border="0" cellspacing="0" cellpadding="0" width="100%" style="border-collapse:collapse; font-size: 12px">
				<tr style="height:48px">
					<td>&nbsp;&nbsp;&nbsp;</td>
					<td colspan="6">
						<h4>Paramètres de recherche</h4>
					</td>
					<td>&nbsp;&nbsp;&nbsp;</td>
				</tr>
				<TR>
					<td>&nbsp;</td>
					<td style="width:120px">Utilisateur</td>
					<TD><INPUT class="clearable" type="text"
						name="searchModel.forUserJob" value="<%=currentUserId%>" onchange=""></TD>
					<td>Date de création dès le</td>
					<TD><input class="clearable" type="text"
						name="searchModel.forDateMin" value=""
						data-g-calendar="mandatory:false" /></TD>
					<td>Date de création jusqu'au</td>
					<TD><input class="clearable" type="text"
						name="searchModel.forDateMax" value=""
						data-g-calendar="mandatory:false" /></TD>
					<td>&nbsp;</td>
				</TR>
				<TR style="height:16px">
					<td>&nbsp;</td>
					<TD colspan="6">&nbsp;</TD>
					<td>&nbsp;</td>
				</TR>
				<tr style="font-size: 0.9em;font-style:italic">
					<td>&nbsp;</td>
					<td colspan="6">
						<fieldset style="border-top-width:1px;border-left-width:0px;border-right-width:0px;border-bottom-width:0px"
								  > 
							<legend style="padding-left: 16px"
								  style="background-image: url('<%=request.getContextPath()%>/images/amal/arrow_expanded.png')"
								  style="background-attachment: scroll"
								  style="background-repeat: no-repeat"
								  style="background-origin: padding-box"
								  style="background-clip: border-box"
								  style="background-position-x: 0%"
								  style="background-position-y: 40%"
								  style="background-size: auto"
								  style="background-color: transparent"
								  style="cursor:hand"
								  id="legendOptionId"
							>
								Options&nbsp;
							</legend>
						</fieldset>
					</td>
					<td>&nbsp;</td>
				</tr>
				<TR class="optionLine" style="font-size: 0.9em;font-style:italic;">
					<td>&nbsp;</td>
					<TD>Afficher &quot;envoyés&quot;</TD>
					<TD>
						<INPUT type="hidden" name="searchModel.forNotStatusEnvoi" value="<%=IAMCodeSysteme.AMDocumentStatus.SENT.getValue()%>" />
						<input type="checkbox" name="searchModel.forNotStatusEnvoiChecked" />
					</TD>			
					<TD>&nbsp;</TD>
					<TD>&nbsp;</TD>
					<TD>&nbsp;</TD>
					<TD>
						<input style="font-size: 0.9em;" type="button" id="clearButton" onclick="clearFields()" accesskey="C" value="Effacer" />
						[ALT+C]
					</TD>
				</TR>
				<TR>
					<td>&nbsp;</td>
					<TD colspan="6">&nbsp;</TD>
					<td>&nbsp;</td>
				</TR>
			</TABLE>
		</div>
	</td>
</tr>


<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf"%>
<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf"%>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf"%>
