<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery-ui.js"></script>
<%
	idEcran = "GEB0001";
	bButtonNew = false;
%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<script>
bFind = true;
usrAction = "orion.pucs.pucsFile.lister";
$(document).ready(function(){

	$('#selectionner_all_handling').click(function(){
		if ($('#selectionner_all_handling:checked').val() != null) {
			$("input[name*='idPucsEntryHandling']", window.frames[0].document).attr('checked', true);
		}else{	
			$("input[name*='idPucsEntryHandling']", window.frames[0].document).attr('checked', false);
		}	
	});
	$('#selectionner_to_handle').click(function(){
		if ($('#selectionner_to_handle:checked').val() != null) {
			$("input[name*='idPucsEntryToHandle']", window.frames[0].document).attr('checked', true);
		} else{
			$("input[name*='idPucsEntryToHandle']", window.frames[0].document).attr('checked', false);
		}
	});
	
	$('#simulation').click(function(){
		removeInputs();
		$("input[name*='userAction']").attr('value', 'orion.pucs.pucsImport.afficher');
		$("<input type='hidden' name='mode' value='simulation'>").appendTo('form');
		initializeForm();
		$(document.forms[0]).attr('target','');
		document.forms[0].submit();
	});
	$('#maj').click(function(){
		removeInputs();
		$("input[name*='userAction']").attr('value', 'orion.pucs.pucsImport.afficher');
		$("<input type='hidden' name='mode' value='miseajour'>").appendTo('form');
		initializeForm();
		$(document.forms[0]).attr('target','');
		document.forms[0].submit();
	});
});

function initializeForm(){
	$('input[type=checkbox][checked]', window.frames[0].document).each(
		    function() { 
		    	$("<input type='hidden' name='idPucsEntry' value='"+$(this).attr('value')+"'>").appendTo('form');
		    } 
	);
}
function removeInputs(){
	$("input[name*='idPucsEntry']").each(function(){
		$(this).remove();
	});
}
</script>

<ct:menuChange displayId="menu" menuId="EBMenuPrincipal" showTab="menu">
</ct:menuChange>
	
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="TITRE_FICHIER_PUCS"/><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:insert attribute="zoneMain" --%>

<tr>
	<td>
		<input type="checkbox" id="selectionner_all_handling"/><ct:FWLabel key="FICHIERS_TRAITEMENT"/>
	</td>
	<td>
		&nbsp;
	</td>
	<td>
		<ct:FWLabel key="MENU_NO_AFFILIE"/>
	</td>
	<td> 
		<input type="text" name="likeAffilie">
	</td>
	<td>
		&nbsp;<ct:FWLabel key="PUCS_TYPE"/>
	</td>
	<td>
	
		<select name="type">
			<option value = ""></option>
			<option value = "1"><ct:FWLabel key="PUCS_TYPE_PUCS"/></option>
			<option value = "2"><ct:FWLabel key="PUCS_TYPE_DAN"/></option>
		    <option value = "4"><ct:FWLabel key="PUCS_TYPE_SWISS_DEC"/></option>
		</select>
	</td>
</tr>
<tr>
	<td>
		<input type="checkbox" id="selectionner_to_handle"/><ct:FWLabel key="FICHIERS_A_TRAITER"/>
	</td>
	<td>
		&nbsp;
	</td>
	<td>
		<ct:FWLabel key="MENU_DATE_RECEPTION"/>
	</td>
	<td>
		<ct:FWCalendarTag name="dateSoumission" value = ""/>
	</td>
</tr>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<ct:ifhasright element="orion.pucs.pucsImport.afficher" crud="u">
<input type="button" id="simulation" value="<ct:FWLabel key="PUCS_SIMULATION"/>"/>&nbsp;
</ct:ifhasright>
<ct:ifhasright element="orion.pucs.pucsImport.afficher" crud="u">
<input type="button" id="maj" value="<ct:FWLabel key="PUCS_MISE_A_JOUR"/>"/>
</ct:ifhasright>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>