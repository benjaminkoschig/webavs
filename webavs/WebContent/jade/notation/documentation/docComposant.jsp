<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <% String servletContext = "../../../";%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">
<head>
<meta name="User-Lang" content="FR"/>
<meta name="Context_URL" content="../../../"/> 

	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<title>Documentation for notation</title>
	<script type="text/javascript" src="<%=servletContext%>scripts/jquery.js"> </script>
	<script type="text/javascript" src="<%=servletContext%>scripts/jquery-ui.js"> </script>
	
	<!-- <script type="text/javascript" src="./scripts/jsnotation/syntaxhighlighte/XRegExp.js"></script> -->
	<script type="text/javascript" src="../javascriptLibs/syntaxhighlighte/shCore.js"></script>
	<script type="text/javascript" src="../javascriptLibs/syntaxhighlighte/shBrushJScript.js"></script>
	<script type="text/javascript" src="../javascriptLibs/syntaxhighlighte/shBrushXml.js"></script>
	<script type="text/javascript" src="<%=servletContext%>scripts/globazJqueryPlugin.js"></script>
	<script type="text/javascript" src="<%=servletContext%>scripts/widget/globazwidget.js"> </script>
	<script type="text/javascript" src="../javascriptLibs/jsHint/jshint.js"></script>
    <script type="text/javascript" src="testJshint.js"></script>
    
    
  <%@ include file="../notationLibJs.jspf" %> 
  <script type="text/javascript" src="notationDocumentation.js"></script>
  <script type="text/javascript" src="documentationNotation.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=servletContext%>theme/jquery/jquery-ui.css" />
    <link rel="stylesheet" type="text/css" href="<%=servletContext%>theme/master.css" />
	<link rel="stylesheet" type="text/css" href="../javascriptLibs/syntaxhighlighte/shCore.css"/>
	<link rel="stylesheet" type="text/css" href="../javascriptLibs/syntaxhighlighte/shThemeDefault.css"/>
	<link rel="stylesheet" type="text/css" href="documentationNotation.css"/>
	<link rel="stylesheet" type="text/css" href="../test/testNotation.css"  />
</head>
<body>
	<div id="documentationNotation">
	   <div id='search'><input type='text' name='search' id='searchNotation' /></div>	
	</div>
<form action="">

 <div id="testInput" style="display:none"> 
 	 <%@ include file="../fragementNotation.jspf" %> 
 </div> 
 </form>
</body>

</html>