<%-- tpl:insert page="/theme2/find.jtpl" --%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme2/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "fw_templates_rc";
String formAction = servletContext + mainServletPath;
%>
<%-- /tpl:put --%>
<%@ include file="/theme2/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.fx.vb.print.FXTagsListViewBean"%>
<link type="text/css" href="<%=servletContext%>/theme2/jquery/jquery-ui.css" rel="stylesheet" />
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
<script type="text/javascript">
function loadTagsList() {
	$.ajax({
		complete: function (xhr, txt) {},
		data: {userAction: "fx.print.tags.lister.ajax", outputType: "<%=FXTagsListViewBean.OUTPUT_MULTISELECT%>"},
		error: function (req, textStatus, errorThrown) {alert("error: " + textStatus + " - " + errorThrown + "-" + req.status + " - ContentType: " + req.contentType);},
		success: function (data) {$('#tagsSelect').html(data)},
		type: "post",
		url: '<%=formAction%>' 
	});
}

$(function() {
	$('#dateSelection').datepicker({ dateFormat: 'dd.mm.yy', showButtonPanel: 'true' });
});

$('document').ready(loadTagsList);
usrAction = "fx.print.templates.lister";
bFind = true;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme2/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Recherche des modèles<%-- /tpl:put --%>
<%@ include file="/theme2/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<span>Recherche des modèles en fonction de leur libellé, tags, révision ou date de modification.</span>
						
						<fieldset class="floating">
							<legend>Libellé</legend>
							<dl>
								<dt>Nom ou description:</dt>
								<dd><input type="text" name="likeLabel"></dd>
								<!-- <dt>Tag:</dt>
								<dd id="tagsSelect">
									<select multiple="multiple">
									</select><br/>
								</dd>-->
							</dl>
						</fieldset>
						<fieldset class="floating">
							<legend>Révision</legend>
							<dl>
								<dt>Pile active:</dt>
								<dd>
									<select>
										<option>Égal</option>
										<option>Globaz</option>
										<option>Client</option>
									</select>
								</dd>
								<dt>Révision forcée:</dt>
								<dd>
									<select>
										<option>Égal</option>
										<option>Oui</option>
										<option>Non</option>
									</select>
								</dd>
								
							</dl>
						</fieldset>
						<fieldset class="floating">
							<legend>Date</legend>
							Date de mise à jour: 
							<select>
								<option>plus récent que</option>
								<option>plus vieux que</option>
							</select>
							<input type="text" id="dateSelection" size="10">
						</fieldset>
						<div class="resetFloat"></div>
	 					<%-- /tpl:put --%>
<%@ include file="/theme2/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme2/find/bodyEnd.jspf" %>
<%-- /tpl:insert --%>