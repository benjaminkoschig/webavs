<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp"%>
<%@ page import="globaz.templates.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<HEAD>
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE></TITLE>

<%
String tmp = "dracoRoot/" + globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session) + "/";
String logoSrc = tmp + "appLogo.jsp";
String labelSrc = tmp + "appLabel.jsp";
String iconSrc ="";
if (globaz.jade.common.Jade.getInstance().getLanguagePath()) {
	iconSrc = request.getContextPath()+"/" +globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session) + "/appIcons.jsp?servlet=draco";
} else {
	iconSrc = request.getContextPath()+"/appIcons.jsp?servlet=draco";
}

String menuSrc = tmp+"appMenu.jsp";
String aboutSrc = tmp+"appAbout.jsp";
String infoSrc = "appInfo.jsp";
String mainSrc = tmp+"appMain.jsp";
FWFrameworkFramesetInfo framesetInfo = new FWFrameworkFramesetInfo();
%>
<script>
if (self != top) {
	top.location.href=window.location.href;
}

</script>
</HEAD>
<%  
		String titleRow = "";
		if (framesetInfo.hasTitle()) {
			titleRow += framesetInfo.getTitleHeight() + ",";
		}
%>
<FRAMESET rows="<%=titleRow%>*, <%=framesetInfo.getErrorHeight()%> border="0px">
<%
if (framesetInfo.hasTitle()) {
%>
	<FRAMESET cols="<%=framesetInfo.getNaviWidth()%> ,* , <%=framesetInfo.getAppIconsWidth()%>" border="0px">
		<FRAME name="fr_logo" scrolling="no" src="<%=logoSrc%>" />
		<FRAME name="fr_pagelabel" scrolling="auto" src="<%=labelSrc%>" />
		<FRAME name="fr_appicons" scrolling="no" src="<%=iconSrc%>" />
	</FRAMESET>
<%
}
%>
	<FRAMESET cols="<%=framesetInfo.getNaviWidth()%> ,*" border="0px">
		<FRAME name="fr_menu" scrolling="auto" src="<%=menuSrc%>" />
		<FRAME name="fr_main" scrolling="auto" src="<%=mainSrc%>" />
	</FRAMESET>
	
	<FRAMESET cols="<%=framesetInfo.getNaviWidth()%> ,*" border="0px">
		<FRAME name="fr_about" scrolling="auto" src="<%=aboutSrc%>" />
		<FRAME name="fr_error" scrolling="auto" src="<%=infoSrc%>" />
	</FRAMESET>
	
	<NOFRAMES><BODY bgcolor=#FFFFFF><P>L'affichage de cette page requiert un navigateur prenant en charge les cadres.</P></BODY></NOFRAMES>
</FRAMESET>

</HTML>