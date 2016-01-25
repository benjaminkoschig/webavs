<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<META http-equiv="Content-Style-Type" content="text/css">
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery-ui.js"></script>
<%@ page import="globaz.framework.screens.menu.*"%>
<%@ page import="globaz.templates.*"%>
<%
	boolean displayOptionsAsDefault = false;
	String tabToShow = request.getParameter("tab");
	if ("options".equals(tabToShow)) {
		displayOptionsAsDefault = true;
	}
/*	String menuDisplayStyle = "";
	String optionsDisplayStyle = "";
	if (displayOptionsAsDefault) {
		menuDisplayStyle = ";display:'none'";
	}
	else {
		optionsDisplayStyle = ";display:'none'";
	}*/
	//globaz.framework.menu.FWMenuBlackBox menuBB = (globaz.framework.menu.FWMenuBlackBox)session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_USER_MENU);
//	menuBB.setActionParameter();
%>
<script language="JavaScript" src="<%=request.getContextPath()%>/scripts/menu.js"></script>
<script language="JavaScript1.2">
<!--
function changeOver (elem) {
	jscss("add", elem, "isHovered");
	// elem.style.textDecoration = "underline";
	//elem.style.color = "white";
}

function changeOut (elem) {
	//elem.style.textDecoration = "none";
	jscss("remove", elem, "isHovered");
//	elem.style.color = "silver";
}

function selectMenu() {
	document.all.menu.style.display='block';
	document.all.options.style.display='none';
	jscss("add", document.all.menuButton, "btnSelected");
	jscss("remove", document.all.optionsButton, "btnSelected");
}

function selectOptions() {
	document.all.menu.style.display='none';
	document.all.options.style.display='block';
	jscss("add", document.all.optionsButton, "btnSelected");
	jscss("remove", document.all.menuButton, "btnSelected");
}
/*
This example function takes four parameters:

a
	defines the action you want the function to perform.
o
	the object in question.
c1
	the name of the first class
c2
	the name of the second class


Possible actions are:

swap
	replaces class c1 with class c2 in object o.
add
	adds class c1 to the object o.
remove
	removes class c1 from the object o.
check
	test if class c1 is already applied to object o and returns true or false.
*/
function jscss(a,o,c1,c2)
{
  switch (a){
    case 'swap':
      o.className=!jscss('check',o,c1)?o.className.replace(c2,c1):o.className.replace(c1,c2);
    break;
    case 'add':
      if(!jscss('check',o,c1)) {
      	o.className += o.className ? ' ' + c1 : c1;
      }
//      alert(o.className);
    break;
    case 'remove':
      var rep=o.className.match(' '+c1)?' '+c1:c1;
      o.className=o.className.replace(rep,'');
    break;
    case 'check':
      return new RegExp('\\b'+c1+'\\b').test(o.className)
    break;
  }
//  alert(o.id + " # " + o.className);
}

function selectRightTab() {
	<%if (displayOptionsAsDefault) {%>
	selectOptions();
	<%} else {%>
	selectMenu();
	<%}%>
}

$(function() {
	$('frame[name=fr_main]', top).ready(function(){ 
		$.ajax({
			url: '<%=request.getContextPath()%>/fxRoot/application.jsp',
			success: function(data){
				$('#applications').html(data); 
			}
		});
	});
});

//-->
</script>
<style>

span.menuHeader {
    font-size : 12px;
    font-family : Verdana,Arial;
    color : black;
    padding : 3 7;
    border: 1px solid #B5C7DE;
    font-weight: bold;
	width: 40%;
	text-align: center;
	cursor: hand;
/*	background-color: red;*/
}
/*    border: 1px solid #226194;*/

span.isHovered {
	color: #ffffff;
	border-color: #ffffff;
	background-color: #93A5Bc;
}


span.btnSelected {
	color: white;
	background-color: #226194;
	border-color: #ffffff;
}

div.headContainer {
	background-color: #B5C7DE;
	border: solid 1px;
	border-color: #aaaaaa;
	padding: 2px;
	margin-bottom: 3px;
	width: 100%
}

div.bigContainer {
	background-color: #ffffff;
	border: solid 1px;
	width:150px;
	padding: 2px;
}

div.bodyContainer {
	border: solid 1px #aaaaaa;
	padding: 1px;
	background-color: #eeeeee;
}

div.singleMenu {
	background-color : #B3C4DB;
}

div.smallContainer {
	background-color: #ffffff;
	border: solid 1px;
	width:150px;
	padding: 2px;
	height: 20;
	margin-top: 2px;
}
 
div.smallbodyContainer {
	border: solid 1px #aaaaaa;
	padding: 1px;
	background-color: #eeeeee;
}

<%@page import="globaz.jade.common.Jade"%>

body {
	background-color: <%=Jade.getInstance().getWebappBackgroundColor()%>;
}
</style>
<TITLE></TITLE>
</HEAD>
<BODY bgcolor="#FFFFFF" style="margin-right:0px; width: 100%" onload="selectRightTab()">
<div class="bigContainer">
	<div class="headContainer">
		<span class="menuHeader" id="menuButton" onclick="selectMenu();" onMouseOver="changeOver(this)" onMouseOut="changeOut(this)"><ct:FWLabel key="MENU"/></span>
		<span class="menuHeader" id="optionsButton" onclick="selectOptions();" onMouseOver="changeOver(this)" onMouseOut="changeOut(this)"><ct:FWLabel key="MENU_OPTIONS"/></span>
	</div>
	<div class="bodyContainer">
		<div class="singleMenu" style="height:500;" id="menu"><ct:FWMenuTag displayId="menu"/></div>
		<div class="singleMenu" style="height:500;" id="options"><ct:FWMenuTag displayId="options" /></div>
	</div>
</div>
<div class="smallContainer" id="apps">
	<div class="smallbodyContainer">
		<div class="singleMenu" style="height=200" id="applications">
		</div>
	</div>
</div>
</BODY>
</html>