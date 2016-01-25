<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp"%>
<%@ page import="globaz.templates.*" %>

<HEAD>
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE></TITLE>

<%
String tmp = "naosRoot/" + globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session) + "/";
String logoSrc = tmp + "appLogo.jsp";
String labelSrc = tmp + "appLabel.jsp";
String iconSrc ="";
if (globaz.jade.common.Jade.getInstance().getLanguagePath()) {
	iconSrc = request.getContextPath()+"/" +globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session) + "/appIcons.jsp?servlet=naos";
} else {
	iconSrc = request.getContextPath()+"/appIcons.jsp?servlet=naos";
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

function onLoadFrame() {
	
	overloaActionMenu.changActionMenu();
};


function onLoadMenuFrame () {
	window.top.fr_menu.$(function (){
		overloaActionMenu.changActionMenu();
	})
}

var overloaActionMenu = {
	changActionMenu: function () {
		var $menu =  window.top.fr_menu.$("#naos_af_ged");
		var action = $menu.attr("onclick");
		if (action) {
			action =  action.split("'")[1];
			$menu.unbind("click",window.top.fr_menu.doAction);
			$menu.removeAttr("onclick");
			$menu.click(function (event) {
				event.preventDefault();
				event.stopImmediatePropagation();
				event.stopPropagation();
				window.open(action,"GED_CONSULT")
			});
		}
	}
};

</script>
</HEAD>
<%  
		String titleRow = "";
		if (framesetInfo.hasTitle()) {
			titleRow += framesetInfo.getTitleHeight() + ",";
		}
%>
<FRAMESET rows="<%=titleRow%>*, <%=framesetInfo.getErrorHeight()%> border="0">
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
		<FRAME name="fr_menu" onload="onLoadFrame()" scrolling="auto" src="<%=menuSrc%>" />
		<FRAME name="fr_main" scrolling="auto" src="<%=mainSrc%>" />
	</FRAMESET>
	
	<FRAMESET cols="<%=framesetInfo.getNaviWidth()%> ,*" border="0px">
		<FRAME name="fr_about" scrolling="auto" src="<%=aboutSrc%>" />
		<FRAME name="fr_error" scrolling="auto" src="<%=infoSrc%>" />
	</FRAMESET>
	
	<NOFRAMES><BODY bgcolor=#FFFFFF><P>L'affichage de cette page requiert un navigateur prenant en charge les cadres.</P></BODY></NOFRAMES>
</FRAMESET>
</HTML>