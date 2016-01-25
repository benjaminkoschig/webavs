<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp"%>
<%@ page import="globaz.templates.*" %>

<HEAD>
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE></TITLE>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/images/globaz_icone.ico" />
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery-ui.js"></script>
<link type="text/css" href="<%=request.getContextPath()%>/theme/jquery/jquery-ui.css" rel="stylesheet" />

<%
String tmp = "pyxisRoot/" + globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session) + "/";
String logoSrc = tmp + "appLogo.jsp";
String labelSrc = tmp + "appLabel.jsp";
String iconSrc ="";
if (globaz.jade.common.Jade.getInstance().getLanguagePath()) {
	iconSrc = request.getContextPath()+"/" +globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session) + "/appIcons.jsp?servlet=pyxis";
} else {
	iconSrc = request.getContextPath()+"/appIcons.jsp?servlet=pyxis";
}

String menuSrc = tmp+"appMenu.jsp";
String aboutSrc = tmp+"appAbout.jsp";
String infoSrc = "appInfo.jsp";
String mainSrc = tmp+"appMain.jsp?mode="+request.getParameter("mode");
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
		var $menu =  window.top.fr_menu.$("#ged");
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

function modal(but) {
	$("#rootModalDialog").dialog({ 
		width:500,
		modal : true,
		resizable:false,
		closeOnEscape :true,
		buttons : but
	});

}
</script>

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
<div  id="rootModalDialog"><div>
	
</body>

</HTML>