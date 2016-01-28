<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<META http-equiv="Content-Style-Type" content="text/css">
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery-ui.js"></script>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/menu.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/utils.js"></script>
<script type="text/javascript">
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
	document.getElementById("menu").style.display='block';
	document.getElementById("options").style.display='none';
	jscss("add", document.getElementById("menuButton"), "btnSelected");
	jscss("remove", document.getElementById("optionsButton"), "btnSelected");
	selectFirstItem();
}

function selectOptions() {
	document.getElementById("menu").style.display='none';
	document.getElementById("options").style.display='block';
	jscss("add", document.getElementById("optionsButton"), "btnSelected");
	jscss("remove", document.getElementById("menuButton"), "btnSelected");
	selectFirstItem();
}

function selectRightTab() {
	<%if (displayOptionsAsDefault) {%>
	selectOptions();
	<%} else {%>
	selectMenu();
	<%}%>
}
$(function(){
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

<style type="text/css">
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
</style>
<TITLE></TITLE>
</HEAD>
<body bgcolor="#FFFFFF" style="margin-right:0px; width: 100%" onload="selectRightTab()" onkeydown="handleMenuEvent(event)">
<div class="bigContainer">
	<div class="headContainer">
		<span class="menuHeader" id="menuButton" onclick="selectMenu();" onMouseOver="changeOver(this)" onMouseOut="changeOut(this)">Menu</span>
		<span class="menuHeader" id="optionsButton" onclick="selectOptions();" onMouseOver="changeOver(this)" onMouseOut="changeOut(this)">Options</span>
	</div>
	<div class="bodyContainer">
		<div class="singleMenu" style="height=500" id="menu"><ct:FWMenuTag displayId="menu"/></div>
		<div class="singleMenu" style="height=500" id="options"><ct:FWMenuTag displayId="options" /></div>
	</div>
</div>
<div class="smallContainer" id="apps">
	<div class="smallbodyContainer">
		<div class="singleMenu" style="height=200" id="applications">
		</div>
	</div>
</div>
</body>
</html>