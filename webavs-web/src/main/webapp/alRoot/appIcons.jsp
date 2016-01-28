<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<HEAD>
<TITLE></TITLE>
<META http-equiv="Content-Type" content="text/html;">
<META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<style>
<!--
a.menu:link {color: black; text-decoration: none; }
a.menu:visited {color: black; text-decoration: none; }
a.menu:active {color: black; }
a.menu:hover {color: black; text-decoration: underline; } 

.menuTable {
		width : 100%;
		height : 100%;
	}
	
		

-->
</style>
<SCRIPT>
<%
String context = request.getContextPath();
String servlet = "/al";
%>
function back() {
 top.fr_main.location.href = "<%=context + servlet%>?userAction=back";
}

function showProcess() {
	  hidePostit();
	 top.fr_main.location.href = "<%=context + "/fx"%>?userAction=fx.job.job.chercher";
	}

function imprime() {
 var mainFrame = top.fr_main;
 mainFrame.focus();
 if (mainFrame.print) {
  mainFrame.print();
 }
 this.focus();
}</SCRIPT>
</HEAD>
<BODY>
<table class="menutable">
	<tr>
		<td align='right'>
		<a href="javascript:showProcess()"><IMG id="icon_process" SRC="<%=request.getContextPath()%>/images/icon_process.png" ALT="Process" border="0"/></a>
		<a href="javascript:imprime()"><IMG id="icon_print" style="border:1; border-color:white; border-style:solid" SRC="<%=request.getContextPath()%>/images/icon_print.png" ALT="Print" ></a>
		<a href="javascript:back()"><IMG id="icon_back" style="border:1; border-color:white; border-style:solid" SRC="<%=request.getContextPath()%>/images/icon_back.png" ALT="Back" ></a>
		</td>

		<td>&nbsp;</td>
	</tr>
</table>
</BODY>
</HTML>