<%@page import="globaz.fx.user.client.bean.FXUserViewBean"%><%-- tpl:insert page="/theme2/detail.jtpl" --%><%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme2/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.fx.vb.print.FXTagsViewBean"%>
<%
FXTagsViewBean viewBean = new FXTagsViewBean();
hasDefaultForm = false;
bButtonCancel = false;
bButtonDelete = false;
bButtonNew = false;
bButtonUpdate = false;
bButtonValidate = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme2/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="ch.globaz.fx.business.service.templates.FXTemplateTag"%><link type="text/css" href="<%=servletContext%>/theme2/jquery/jquery-ui.css" rel="stylesheet" />
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
	

<style type="text/css">
</style>

<script type="text/javascript">
function deleteTag(aTagId) {
	$.ajax({
		complete: function (xhr, txt) {$('#newTagLabel').val("").focus();},
		data: {userAction: "fx.print.tags.supprimer.ajax", idTag: aTagId},
		error: function (req, textStatus, errorThrown) {alert("error: " + textStatus + " - " + errorThrown + "-" + req.status + " - ContentType: " + req.contentType);},
		success: function (txt) {loadList();},
		type: "post",
		url: '<%=formAction%>' 
	});

}

function loadList() {
	$.ajax({
		complete: function (xhr, txt) {},
		data: {userAction: "fx.print.tags.lister.ajax"},
		error: function (req, textStatus, errorThrown) {alert("error: " + textStatus + " - " + errorThrown + "-" + req.status + " - ContentType: " + req.contentType);},
		success: function (data) {$('#tagsTable').html(data)},
		type: "post",
		url: '<%=formAction%>' 
	});
}
// Dialog
$(function() {
/*	$('#dialog').dialog({
		autoOpen: true,
		modal: true,
		width: 600,
		buttons: {
			"Ok": function() { 
				$(this).dialog("close"); 
			}, 
			"Cancel": function() { 
				$(this).dialog("close"); 
			} 
		}
	});*/
	

	// Add button
	$('#btn_add_tag').click(function(){
		var value = $('#newTagLabel').val();
		$.ajax({
			complete: function (xhr, txt) {$('#newTagLabel').val("").focus();},
			data: {userAction: "fx.print.tags.ajouter.ajax", label: value},
			error: function (req, textStatus, errorThrown) {alert("error: " + textStatus + " - " + errorThrown + "-" + req.status + " - ContentType: " + req.contentType);},
			success: function (data) {loadList();},
			type: "post",
			url: '<%=formAction%>' 
		});
		return false;
	});

});

$(document).ready(loadList);
</script>

<%-- /tpl:put --%>
<%@ include file="/theme2/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Tags des modèles<%-- /tpl:put --%>
<%@ include file="/theme2/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<p>La raison d'être d'un tag est de retrouver tous les modèles associés. Le texte (libre) doit faire entre 1 et 32 caractères.</p>
<p>Ajouter: <input type="text" id="newTagLabel">&nbsp;<button id="btn_add_tag">&nbsp;+&nbsp;</button></p>

<!--<div id="dialog" title="ajout d'un tag">Bravo tu as ajouté ton chnis</div> -->
<%--<a href="javascript:loadList();">F5</a> --%>
<div id="tagsTable"></div>
						<%-- /tpl:put --%>
<%@ include file="/theme2/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme2/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme2/detail/footer.jspf" %>
<%-- /tpl:insert --%>