<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme2/detail.jtpl" --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.fx.vb.print.FXTagsListViewBean"%>
<%@ page import="globaz.fx.vb.print.FXTemplatesViewBean"%>
<%@ page import="ch.globaz.fx.business.service.templates.FXTemplate"%>
<%@ page import="ch.globaz.fx.business.service.templates.FXTemplateSimple"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<html>
<!--# set echo="url" -->
<%
// Ici on calcule des userActions par défaut
String requestUserAction = "";
int userActionLastDotIndex = 0;
String partialUserActionAction = "42";
String userActionNew = partialUserActionAction + ".afficher";
String userActionUpd = partialUserActionAction + ".modifier";
String userActionDel = partialUserActionAction + ".supprimer";
// Quelques variables standard
String lastModification = "";
String creationSpy = null;
String languePage = "FR";
String servletContext = "../../../";
String mainServletPath = "pegasus";
String selectedIdValue = "";
String userActionValue = "";
String actionNew =  "#";
int tableHeight = 243;
String subTableWidth = "100%";
String applicationId = "TEST";
String formAction =  servletContext + mainServletPath;
String key = "none";

String btnUpdLabel = "Modifier";
String btnDelLabel = "Supprimer";
String btnValLabel = "Valider";
String btnCanLabel = "Annuler";
String btnNewLabel = "Nouveau";
//boolean bButtonNew = objSession.hasRight(userActionNew, "ADD");
boolean bButtonNew = false;
boolean bButtonUpdate = false;
boolean bButtonDelete = false;
boolean bButtonValidate = false;
boolean bButtonCancel = false;
String idEcran =  "1337 5kR33n";
boolean autoShowErrorPopup = session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_NO_JSP_POPUP) == null;
boolean vBeanHasErrors = false;
boolean hasDefaultForm = true;

FXTemplatesViewBean viewBean = new FXTemplatesViewBean();
viewBean.setMessage("");
viewBean.setMsgType("");
%>
<head>
<script type="text/javascript">
var langue = "<%=languePage%>"; 
</script>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%@ include file="../../../theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<link rel="stylesheet" type="text/css" href="<%=servletContext%>theme/widget.css"/>
<script type="text/javascript" src="<%=servletContext%>scripts/widget/globazwidget.js"></script> 
<script type="text/javascript">

function validate() {
	return true;
}

function upd() {
	$('*[name="userAction"]').val("fx.print.templates.modifier");
}

function cancel() {
	
}

function init() {
	
}

$(document).ready(function(){
	readOnly(false);
	action(UPDATE);
	upd();
});
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Mode Quirks - berk...<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td colspan="3">
<%@ include file="../fragementNotation.jspf" %>
							</td>
						</tr> 
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>