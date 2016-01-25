<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<%@ page language="java" errorPage="/errorPage.jsp"%>
<%@ page import="globaz.templates.*" %>
<%@ page import="globaz.globall.db.BSession"%>
<head>
<meta name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<meta http-equiv="Content-Style-Type" content="text/css">
<title></title>
<script>if (self != top){top.location.href=window.location.href;}</script>
</head>
<%
String context = request.getContextPath();
if (context.length() > 0) {
	context += "/";
}
String fullContext = context + "lynxRoot/" + globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session) + "/";
String logoSrc = fullContext + "appLogo.jsp";
String labelSrc = fullContext + "appLabel.jsp";
String iconSrc = context + globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session) + "/appIcons.jsp?servlet=lynx";
String menuSrc = fullContext + "appMenu.jsp";
String aboutSrc = fullContext + "appAbout.jsp";
String mainSrc = fullContext+"appMain.jsp";
String infoSrc = "appInfo.jsp";
//FWFrameworkFramesetInfo framesetInfo = new FWFrameworkFramesetInfo();

//enleve l'attribut mis en Session par MUSCA pour la gestion des menus pour les interets moratoires
BSession bSession = (BSession) session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_SESSION);
bSession.removeAttribute("musca_interet_moratoire");

// Construction d'un String représentant les paramètres reçus par ce frameset
// Ces paramètres seront transmis au frame principal (fr_main)
java.util.Enumeration theParameters = request.getParameterNames();
StringBuffer parametersString = new StringBuffer();
if (theParameters.hasMoreElements()) {
	parametersString.append('?');
}
while (theParameters.hasMoreElements()) {
	String key = (String)theParameters.nextElement();
	String value = request.getParameter(key);
	parametersString.append(key);
	parametersString.append('=');
	parametersString.append(value);
	if (theParameters.hasMoreElements()) {
		parametersString.append('&');
	}
}
mainSrc = mainSrc + parametersString.toString();
//String titleRow = framesetInfo.getTitleHeight() + ",";
%>
</head>
<frameset rows="40px, *, 18px" border="0">
	<frameset cols="160px, *, 300px" border="0">
		<frame name="fr_logo" scrolling="no" src="<%=logoSrc%>">
		<frame name="fr_pagelabel" scrolling="auto" src="<%=labelSrc%>">
		<frame name="fr_appicons" scrolling="no" src="<%=iconSrc%>">
	</frameset>

	<frameset cols="160px, *" border="0">
		<frame name="fr_menu" src="<%=menuSrc%>">
		<frame name="fr_main" scrolling="auto" src="<%=mainSrc%>">
	</frameset>

	<frameset cols="160px, *" border="0">
		<frame name="fr_about" scrolling="auto" src="<%=aboutSrc%>">
		<frame name="fr_error" src="<%=infoSrc%>">
	</frameset>

	<noframes>
		<body>
		<p>L'affichage de cette page requiert un navigateur prenant en charge les cadres...</p>
		</body>
	</noframes>
</frameset>
</html>