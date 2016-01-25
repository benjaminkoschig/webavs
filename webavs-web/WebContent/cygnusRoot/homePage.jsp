<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp"%>
<%@ page import="globaz.templates.*" %>

<HEAD>
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE></TITLE>
<script>
if (self != top) {
	top.location.href=window.location.href;
}
</script>
<%
String context = request.getContextPath();
if (context.length() > 0) {
	context += "/";
}
String tmp = context + "cygnusRoot/";
String fullContext = context + globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session) + "/";

String logoSrc = tmp + "appLogo.jsp";
String labelSrc = tmp + "appLabel.jsp";
String iconSrc = fullContext + "appIcons.jsp?servlet=cygnus";
String menuSrc = tmp + "appMenu.jsp";
String aboutSrc = tmp + "appAbout.jsp";
String mainSrc = tmp + "appMain.jsp";
String infoSrc = context + "appInfo.jsp";

FWFrameworkFramesetInfo framesetInfo = new FWFrameworkFramesetInfo();

// Construciton d'un String représentant les paramètres reçus par ce frameset
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
%>
</HEAD>
<%  
		String titleRow = "";
		if (framesetInfo.hasTitle()) {
			titleRow += framesetInfo.getTitleHeight() + ",";
		}
%>
<FRAMESET rows="<%=titleRow%>*,<%=framesetInfo.getErrorHeight()%> border="0">
<%
if (framesetInfo.hasTitle()) {
%>
	<FRAMESET cols="<%=framesetInfo.getNaviWidth()%>,*,<%=framesetInfo.getAppIconsWidth()%>" border="0">
		<FRAME name="fr_logo" scrolling="no" src="<%=logoSrc%>">
		<FRAME name="fr_pagelabel" scrolling="auto" src="<%=labelSrc%>">
		<FRAME name="fr_appicons" scrolling="no" src="<%=iconSrc%>">
	</FRAMESET>
<%
}
%>
	<FRAMESET cols="<%=framesetInfo.getNaviWidth()%>,*" border="0">
		<FRAME name="fr_menu" scrolling="auto" src="<%=menuSrc%>">
		<FRAME name="fr_main" scrolling="auto" src="<%=mainSrc%>">
	</FRAMESET>

	<FRAMESET cols="<%=framesetInfo.getNaviWidth()%>,*" border="0">
		<FRAME name="fr_about" scrolling="auto" src="<%=aboutSrc%>">
		<FRAME name="fr_error" scrolling="auto" src="<%=infoSrc%>">
	</FRAMESET>

	<NOFRAMES>
		<BODY bgcolor="#FFFFFF">
		<P>L'affichage de cette page requiert un navigateur prenant en charge les cadres.</P>
		</BODY>
	</NOFRAMES>
</FRAMESET>
</HTML>