<html>
<head>
<title>test recherche automatique ci</title>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/autocomplete.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/selectionPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<%@ page import="globaz.globall.util.*"%>
<%
String servletContext = request.getContextPath();
String mainServletPath = (String)request.getAttribute("mainServletPath");
%>
<script>
var frameName = "loadedList";
var jspName = "ci_select.jsp?like=";
var selectName = "selection";
var anchorList = "anchorCI";
var divName = "listCI";
var forceSel = false;
var nbrDigit = 10;
</script>
<style>
  .listCI {
  	position:absolute;
  	width:800;
  	height:500;
  	font-family = Wingdings;
  	font-size : 14;
    visibility:hidden
  }
  .selectCI {
      border : solid 1px silver;
      background-color = #f0f0f0;
	  width:400;
  }
</style>
</head>
<body bgcolor="#FFFFFF" text="#000000">
<div id="listCI" class="listCI"></div>
<table border="1"><tr><td>
<input type="text" name="numeroAvs" value="" onKeyUp="popupList(this,'anchorCI1')" onFocus="stopPopup(this)"></td><td>
<input type="text" name="other" value="" onKeyUp="popupList(this,'anchorCI2')" onFocus="stopPopup(this)"></td></tr>
<tr><td><span id="anchorCI1">1</span></td><td><span id="anchorCI2">2</span></td></tr></table>
<iframe width="0" height="0" src="" id="loadedList" name="loadedList"></iframe>
blabladfakljdfha
</body>
</html>
