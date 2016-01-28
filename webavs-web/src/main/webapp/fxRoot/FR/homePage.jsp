<!doctype HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<%@ page language="java" errorPage="/errorPage.jsp"%>
<head>
<title></title>
<%
String tmp="";
tmp = "fxRoot/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/";
String logoSrc = tmp+"appLogo.jsp";
String labelSrc = tmp+"appLabel.jsp";
String iconSrc ="";
if (globaz.jade.common.Jade.getInstance().getLanguagePath()) {
	iconSrc = request.getContextPath()+"/" +globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session) + "/appIcons.jsp?servlet=fx";
} else {
	iconSrc = request.getContextPath()+"/appIcons.jsp?servlet=fx";
}
String menuSrc = tmp+"appMenu.jsp";
String aboutSrc = tmp+"appAbout.jsp";
String infoSrc = "appInfo.jsp";
String mainSrc = tmp+"appMain.jsp";
%>
<script type="text/javascript">
if (self != top) {
	top.location.href=window.location.href;
}
</script>

</head>
<frameset rows="40px,*,18px">
	<frameset cols="160px,*,300px">
		<frame name="fr_logo" scrolling="no" src="<%=logoSrc%>" frameborder="0"/>
		<frame name="fr_pagelabel" scrolling="auto" src="<%=labelSrc%>" frameborder="0"/>
		<frame name="fr_appicons" scrolling="no" src="<%=iconSrc%>" frameborder="0"/>
	</frameset>

	<frameset cols="160px,*">
		<frame name="fr_menu" scrolling="auto" src="<%=menuSrc%>" frameborder="0"/>
		<frame name="fr_main" scrolling="auto" src="<%=mainSrc%>" frameborder="0"/>
	</frameset>
	<frameset cols="160px,*">
		<frame name="fr_about" scrolling="auto" src="<%=aboutSrc%>" frameborder="0"/>
		<frame name="fr_error" src="<%=infoSrc%>" scrolling="auto" frameborder="0"/>
	</frameset>
	<noframes>
		<body><p>L'affichage de cette page requiert un navigateur prenant en charge les cadres.</p></body>
	</noframes>
</frameset>
</html>