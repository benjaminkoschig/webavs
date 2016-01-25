<%-- tpl:insert page="/theme2/detail.jtpl" --%><%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.fx.vb.print.FXTagsListViewBean"%>
<%@page import="globaz.fx.vb.print.FXTemplatesViewBean"%>
<%@page import="ch.globaz.fx.business.service.templates.FXTemplate"%>
<%@page import="ch.globaz.fx.business.service.templates.FXTemplateSimple"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme2/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
FXTemplatesViewBean viewBean = (FXTemplatesViewBean)request.getAttribute("viewBean");
userActionValue = "fx.print.templates.ajouter";
System.out.println(viewBean.getTemplate().getTemplateSimple().getDescription());
FXTemplate template = viewBean.getTemplate();
FXTemplateSimple templateSimple = template.getTemplateSimple();
%>
<%-- /tpl:put --%>
<%@ include file="/theme2/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

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
	$('#cancelTagChanges').click(function() {
		loadTagsList();
	});
});


$('document').ready(loadTagsList);

function validate() {
	return true;
}

function upd() {
	$('*[name="userAction"]').val("fx.print.templates.modifier");
}

function cancel() {
	
}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme2/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un modèle<%-- /tpl:put --%>
<%@ include file="/theme2/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<fieldset class="floating">
							<legend>Généralités</legend>
							<dl>
								<dt>Id:</dt>
								<dd><input type="text" size="30" name="template.templateSimple.name" value="<%=templateSimple.getName() %>"><!-- headers/HEADER_ENTETE --></dd>
								<dt>Description:</dt>
								<dd>
									<input name="template.templateSimple.description" type="text" size="30" value="<%=templateSimple.getDescription() %>">
								</dd>
							</dl>
						</fieldset>
<!--						<fieldset class="floating">
							<legend>Tags</legend>
							<dl>
								<dt>Tags:</dt>
								<dd id="tagsSelect">
								</dd>
							</dl>
							<a id="cancelTagChanges" href="javascript:;">Annuler</a> les changements.
						</fieldset> -->
						<fieldset class="floating">
							<legend>Historique d'activité</legend>
							<div style="height: 6em; overflow: auto; margin-top: 2ex; width: 40em;">
								<ul>
									<li>le 03.12.2006 à 14h02, ssiiadm a ajouté "rev 6" (pile "Globaz"); pile "Client" active;</li>
									<li>le 01.12.2006 à 15h40, dirFER a activé la pile "Client" (rev 4);</li>
									<li>le 01.12.2006 à 15h05, dirFER a forcé "rev 2" (pile "Client");</li>
									<li>et ainsi de suite...</li>
								</ul>
							</div>
						</fieldset>
						<div class="resetFloat"></div>
						<fieldset class="floating">
							<legend>Historique</legend>
							<dl>
								<dt>Pile active:</dt>
								<dd>
									<select>
										<option>Globaz</option>
										<option>Client</option>
									</select>
								</dd>
							</dl>
							<div class="floating" style="border: 1px solid black; padding: 1ex">
								<div style="vertical-align: middle; margin-bottom: 1ex;">Globaz <button> + </button></div>
								<div style="border: 3px double yellow; margin-bottom: 1px; padding: 0.3ex">rev 6 - 03.12.2006</div>
								<div style="border: 1px solid black; margin-bottom: 1px; padding: 0.3ex">rev 5 - 03.11.2006</div>
								<div style="border: 1px solid black; margin-bottom: 1px; padding: 0.3ex">rev 3 - 03.09.2006</div>
							</div>
							<div class="floating" style="border: 1px solid #999; padding: 1ex">
								<div style="text-align: center; color: #999; margin-bottom: 1ex;">Client <button> + </button></div>
								<div style="border: 1px solid #999; margin-bottom: 1px; padding: 0.3ex; color: #999;">rev 4 - 03.10.2006</div>
								<div style="border: 1px solid #999; margin-bottom: 1px; padding: 0.3ex; color: #999;">rev 2 - 03.08.2006</div>
							</div>
							<div class="resetFloat"></div>
						</fieldset>
						<fieldset class="floating">
							<legend>Révision sélectionnée</legend>
							<dl>
								<dt>Révision</dt>
								<dd>6</dd>
								<dt>Pile</dt>
								<dd>Globaz</dd>
								<dt>Mise à jour</dt>
								<dd>03.12.2006 à 14h02 par ssiiadm</dd>
								<dt>Commentaire:</dt>
								<dd>J'ai ajouté un peu de gras.</dd>
							</dl>
							<p><a href="">Télécharger le fichier</a></p>
							<p><button>Forcer ce modèle!</button></p>
						</fieldset>
						<div class="resetFloat"></div>
						<input type="hidden" name="id" value="<%=templateSimple.getId() %>">
						<%-- /tpl:put --%>
<%@ include file="/theme2/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme2/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme2/detail/footer.jspf" %>
<%-- /tpl:insert --%>